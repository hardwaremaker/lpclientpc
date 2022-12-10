package com.lp.neweditor.ui.block;

import java.awt.Font;
import java.awt.event.FocusListener;

import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.text.AttributeSet;

import com.lp.neweditor.common.EditorDefaults;
import com.lp.neweditor.common.text.LpJasperScaledEditorKit;
import com.lp.neweditor.data.block.TextBlockModel;
import com.lp.neweditor.event.ModelChangedEvent;
import com.lp.neweditor.ui.menu.EditorMenu;
import com.lp.neweditor.ui.menu.EditorStatusBar;
import com.lp.neweditor.ui.menu.TextEditorMenu;
import com.lp.neweditor.ui.menu.TextEditorStatusBar;
import com.lp.neweditor.ui.popup.BasicPopupMenu;
import com.lp.neweditor.ui.popup.TextBlockPopupMenu;

public class TextBlockView extends BlockView {
	/**
	 * Property fuer Attribues des gew&auml;hlten Textes. Wird bei Caret Update
	 * gesendet. <br>
	 * Typ: {@link AttributeSet}
	 */
	public static final String propertySelectedTextAttribute = "selectedTextAttributes";

	private JTextPane editorPane;
	private EditorMenu menu;
	private EditorStatusBar statusBar;

	public TextBlockView(TextBlockController controller) {
		super(controller);
		editorPane = new JTextPane() {
			private static final long serialVersionUID = 1L;

			/*
			 * Benoetigt als Workaround fuer Zoom, beim Auswaehlen von Text werden hier
			 * nicht skalierte Koordinaten uebergeben. Es wird jetzt einfach das ganze
			 * TextPane neu gezeichnet.
			 */
			@Override
			public void repaint(int x, int y, int width, int height) {
				super.repaint(0, 0, getWidth(), getHeight());
			}
		};
		editorPane.setEditorKit(new LpJasperScaledEditorKit());
		editorPane.addCaretListener(this::onCaretUpdate);
		editorPane.setFont(getDefaultFont());
		menu = new TextEditorMenu(controller, this);
		statusBar = new TextEditorStatusBar(controller);
		editorPane.addMouseListener(popupMenu.getMouseListener());
		controller.addModelChangeListener(this::onModelChange);
	}

	private void onModelChange(ModelChangedEvent<TextBlockModel> e) {
		TextBlockModel model = e.getDataAfter();
		if (model.getDocument() == null) {
			model.setDocument(editorPane.getEditorKit().createDefaultDocument());
		}
		editorPane.setDocument(model.getDocument());
	}

	protected Font getDefaultFont() {
		return new Font(EditorDefaults.getDefaultFont(), Font.PLAIN, EditorDefaults.getDefaultFontSize());
	}

	@Override
	public JTextPane getUIComponent() {
		return editorPane;
	}

	@Override
	public EditorMenu getMenuForBlock() {
		return menu;
	}

	protected void setText(String newText) {
		editorPane.setText(newText);
	}

	protected void onCaretUpdate(CaretEvent e) {
		AttributeSet attrs = editorPane.getCharacterAttributes();
		propSupport.firePropertyChange(propertySelectedTextAttribute, null, attrs);
	}

	@Override
	public void getFocusClickBehind() {
		editorPane.requestFocusInWindow();
		editorPane.setCaretPosition(editorPane.getDocument().getLength());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected BasicPopupMenu createPopup() {
		return new TextBlockPopupMenu((EditorBlock<? extends TextBlockModel>) getController());
	}

	@Override
	public EditorStatusBar getStatusBar() {
		return statusBar;
	}

	@Override
	public void setEditable(boolean editable) {
		editorPane.setEditable(editable);
	}

	@Override
	public void addFocusListener(FocusListener listener) {
		editorPane.addFocusListener(listener);
	}

}
