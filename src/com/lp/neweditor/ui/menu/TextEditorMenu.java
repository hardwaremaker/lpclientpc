package com.lp.neweditor.ui.menu;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.lp.client.frame.Defaults;
import com.lp.editor.ui.LpToolBarButton;
import com.lp.editor.ui.LpToolsPanel;
import com.lp.neweditor.action.BlockEditorActions;
import com.lp.neweditor.action.ComboBoxFontSizeListener;
import com.lp.neweditor.action.ComboBoxFontTypeListener;
import com.lp.neweditor.common.EditorDefaults;
import com.lp.neweditor.data.block.TextBlockModel;
import com.lp.neweditor.ui.AutocompleteComboBox;
import com.lp.neweditor.ui.block.EditorBlock;
import com.lp.neweditor.ui.block.TextBlockView;

/**
 * 
 * @author Alexander Daum
 *
 */
public class TextEditorMenu extends EditorMenu {

	private final BlockEditorData data;
	/**
	 * Property Font Name, wird &uuml;ber ComboBox gesetzt. <br>
	 * Typ: {@link String}
	 */
	public static final String propertyFontName = "fontName";
	/**
	 * Property Font Size, wird &uuml;ber ComboBox gesetzt. <br>
	 * Typ: {@link Integer}
	 */
	public static final String propertyFontSize = "fontSize";
	private JComboBox<Integer> jComboBoxFontSizes;
	private JComboBox<String> jComboBoxFontNames;

	private final EditorToggleMenuButtonManager toggleBtnManager;

	public TextEditorMenu(EditorBlock<? extends TextBlockModel> controller, TextBlockView view) {
		super(controller);
		this.data = BlockEditorData.getInstance();
		this.toggleBtnManager = new EditorToggleMenuButtonManager();
		view.getUIComponent().addCaretListener(this::onCaretEventFromEditor);
	}

	@Override
	protected void populateToolBar(JComponent toolBar) {
		super.populateToolBar(toolBar);
		toolBar.add(createToolBarOther());
		toolBar.add(createToolBarFontStyle());
	}

	private JToolBar createToolBarOther() {
		BlockEditorActions actions = controller.getEditor().getActions();
		LpToolBarButton lpButtonEditCut = new LpToolBarButton();
		lpButtonEditCut.setAction(actions.getActionCut());
		lpButtonEditCut.setFocusable(false);
		LpToolBarButton lpButtonEditCopy = new LpToolBarButton();
		lpButtonEditCopy.setAction(actions.getActionCopy());
		lpButtonEditCopy.setFocusable(false);
		LpToolBarButton lpButtonEditPaste = new LpToolBarButton();
		lpButtonEditPaste.setAction(actions.getActionPaste());
		lpButtonEditPaste.setFocusable(false);
		LpToolBarButton lpButtonInsertDateTimeUserShortcut = new LpToolBarButton();
		lpButtonInsertDateTimeUserShortcut.setAction(actions.getActionInsertDateTimeUser());
		lpButtonInsertDateTimeUserShortcut.setFocusable(false);
		LpToolBarButton lpButtonInsertSignatur = new LpToolBarButton();
		lpButtonInsertSignatur.setAction(actions.getActionInsertSignatur());
		lpButtonInsertSignatur.setFocusable(false);
		LpToolBarButton lpButtonInsertTextbaustein = new LpToolBarButton();
		lpButtonInsertTextbaustein.setAction(actions.getActionInsertTextbaustein());
		lpButtonInsertTextbaustein.setFocusable(false);
		LpToolBarButton insertImage = new LpToolBarButton();
		insertImage.setAction(actions.getActionInsertImage());
		insertImage.setFocusable(false);
		LpToolBarButton insertTextblock = new LpToolBarButton();
		insertTextblock.setAction(actions.getActionInsertTextblock());
		insertTextblock.setFocusable(false);

		JComboBox<String> jComboBoxFontNames = getComboFontNames();
		JComboBox<Integer> jComboBoxFontSizes = getComboFontSizes();

		JToolBar jToolBarOther = createNewToolbar();
		jToolBarOther.add(lpButtonEditCut);
		jToolBarOther.add(lpButtonEditCopy);
		jToolBarOther.add(lpButtonEditPaste);
		jToolBarOther.add(lpButtonInsertDateTimeUserShortcut);
		jToolBarOther.add(lpButtonInsertSignatur);
		jToolBarOther.add(lpButtonInsertTextbaustein);
		jToolBarOther.add(insertImage);
		jToolBarOther.add(insertTextblock);
		jToolBarOther.add(jComboBoxFontNames);
		jToolBarOther.add(jComboBoxFontSizes);
		return jToolBarOther;
	}

	private JComboBox<Integer> getComboFontSizes() {
		if (jComboBoxFontSizes == null) {
			int defaultFontSize = EditorDefaults.getDefaultFontSize();
			jComboBoxFontSizes = new JComboBox<>(data.getAvailableFontSizesAsArray());
			jComboBoxFontSizes.setEditable(true);
			jComboBoxFontSizes.setSelectedItem(defaultFontSize);
			Dimension d = new Dimension(Defaults.getInstance().bySizeFactor(50),
					jComboBoxFontSizes.getPreferredSize().height);
			jComboBoxFontSizes.setPreferredSize(d);
			jComboBoxFontSizes.setMaximumSize(d);
			jComboBoxFontSizes.addItemListener(new ComboBoxFontSizeListener(getViewEditor()));
			jComboBoxFontSizes.addPopupMenuListener(new GetFocusOnPopupClose(getViewEditor()));
		}
		return jComboBoxFontSizes;
	}

	private JTextPane getViewEditor() {
		return (JTextPane) controller.getView().getUIComponent();
	}

	private JComboBox<String> getComboFontNames() {
		if (jComboBoxFontNames == null) {
			String defaultFont = EditorDefaults.getDefaultFont();
			if (!data.getSystemFonts().contains(defaultFont)) {
				// Pruefe Arial wenn defaultFont nicht vorhanden
				defaultFont = "Arial";
				if (!data.getSystemFonts().contains(defaultFont)) {
					// Verwende alphabetisch ersten font, wenn auch Arial nicht vorhanden
					defaultFont = data.getSystemFonts().get(0);
				}
			}
			jComboBoxFontNames = new AutocompleteComboBox<>(data.getSystemFontsAsArray());
			jComboBoxFontNames.addPopupMenuListener(new GetFocusOnPopupClose(controller.getView().getUIComponent()));
			jComboBoxFontNames.setSelectedItem(defaultFont);
			jComboBoxFontNames.addItemListener(new ComboBoxFontTypeListener(getViewEditor()));
			jComboBoxFontNames.setMaximumSize(jComboBoxFontNames.getPreferredSize());
		}
		return jComboBoxFontNames;
	}

	private JToolBar createToolBarFontStyle() {
		BlockEditorActions actions = controller.getEditor().getActions();
		JToggleButton lpButtonFormatStyleBold = toggleBtnManager.createButton(StyleConstants.Bold,
				actions.getActionBold());
		JToggleButton lpButtonFormatStyleItalic = toggleBtnManager.createButton(StyleConstants.Italic,
				actions.getActionItalic());
		JToggleButton lpButtonFormatStyleUnderline = toggleBtnManager.createButton(StyleConstants.Underline,
				actions.getActionUnderline());
		JToggleButton lpButtonFormatStyleStrikethrough = toggleBtnManager.createButton(StyleConstants.StrikeThrough,
				actions.getActionStrikethrough());

		JToolBar jToolBarFontStyle = createNewToolbar();
		jToolBarFontStyle.add(lpButtonFormatStyleBold);
		jToolBarFontStyle.add(lpButtonFormatStyleItalic);
		jToolBarFontStyle.add(lpButtonFormatStyleUnderline);
		jToolBarFontStyle.add(lpButtonFormatStyleStrikethrough);
		jToolBarFontStyle.add(actions.getActionColorForeground()).setFocusable(false);
		jToolBarFontStyle.add(actions.getActionColorBackground()).setFocusable(false);

		return jToolBarFontStyle;
	}

	protected void onCaretEventFromEditor(CaretEvent e) {
		if (e.getDot() == e.getMark()) {
			// Keine Auswahl, Font styles updaten
			Document doc = getViewEditor().getDocument();
			if (doc instanceof StyledDocument) {
				int caretPosition = getViewEditor().getCaretPosition();
				if (caretPosition != 0 && caretPosition == doc.getLength()) {
					caretPosition--;
				}
				Element elem = ((StyledDocument) doc).getCharacterElement(caretPosition);
				if (elem != null) {
					AttributeSet attrs = elem.getAttributes();
					selectedTextAttributesChanged(attrs);
				}
			}
		}
	}

	private void selectedTextAttributesChanged(AttributeSet attrs) {
		toggleBtnManager.propertiesChanged(attrs);
		String fontFamily = StyleConstants.getFontFamily(attrs);
		getComboFontNames().setSelectedItem(fontFamily);
		int fontSize = StyleConstants.getFontSize(attrs);
		getComboFontSizes().setSelectedItem(fontSize);
	}

}
