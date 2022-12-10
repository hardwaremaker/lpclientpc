package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.OptionalInt;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.lp.client.pc.LPMain;
import com.lp.client.rechtschreibung.IRechtschreibPruefbar;
import com.lp.neweditor.common.InsertPosition;
import com.lp.neweditor.common.SizeUnit;
import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.data.block.TextBlockModel;
import com.lp.neweditor.data.editor.BlockWithPosition;
import com.lp.neweditor.ui.block.TextBlockController;
import com.lp.neweditor.ui.editor.HvBlockEditor;
import com.lp.server.system.service.EditorContentDto;
import com.lp.server.system.service.EditorContentLegacyFormatter;
import com.lp.server.system.service.EditorTextBlockDto;
import com.lp.server.system.service.ParameterFac;

public class WrapperBlockEditorField extends PanelBasis implements IControl, IRechtschreibPruefbar {
	private static final long serialVersionUID = 1L;

	private static final String ACTION_SPECIAL_EDITORFIELD_EDIT = "action_special_editorfield_edit";
	private static final String ACTION_SPECIAL_EDITORFIELD_DEFAULT = "action_special_editorfield_default";

	private HvBlockEditor editor;

	private boolean bWithoutButtons = false;

	private ImageIcon imageIconReset = null;
	private ImageIcon imageIconEdit = null;

	private String defaultText = "";
	private boolean isMandatoryField = false;
	private boolean isActivatable = true;
	private WrapperButton wbuEdit = new WrapperButton();
	private WrapperButton wbuDefault = new WrapperButton();

	private OptionalInt maximaleLaenge = OptionalInt.empty();

	public WrapperBlockEditorField(InternalFrame internalFrameI, String addTitleI, Object keyWhenDetailPanelI)
			throws Throwable {
		jbInit();
	}

	public WrapperBlockEditorField(InternalFrame internalFrameI, String addTitleI) throws Throwable {
		super(internalFrameI, addTitleI);
		jbInit();
	}

	private void jbInit() {
		this.setLayout(new GridBagLayout());
		editor = new HvBlockEditor();
		editor.setInternalFrame(getInternalFrame());
		setDocumentWidth(ParameterCache.getPageWidth(ParameterFac.PARAMETER_EDITOR_BREITE_SONSTIGE));
		editor.setMenuVisible(false);
		wbuEdit.setMinimumSize(new Dimension(23, 23));
		wbuEdit.setPreferredSize(new Dimension(23, 23));
		wbuEdit.setActionCommand(ACTION_SPECIAL_EDITORFIELD_EDIT);
		wbuEdit.setToolTipText(LPMain.getTextRespectUISPr("text.bearbeiten"));
		wbuEdit.setIcon(getImageIconEdit());
		wbuEdit.addActionListener(this);
		wbuEdit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke('T', java.awt.event.InputEvent.CTRL_MASK), ACTION_SPECIAL_EDITORFIELD_EDIT);
		wbuEdit.getActionMap().put(ACTION_SPECIAL_EDITORFIELD_EDIT,
				new ButtonAbstractAction(this, ACTION_SPECIAL_EDITORFIELD_EDIT));

		wbuDefault.setMinimumSize(new Dimension(23, 23));
		wbuDefault.setPreferredSize(new Dimension(23, 23));
		wbuDefault.setActionCommand(ACTION_SPECIAL_EDITORFIELD_DEFAULT);
		wbuDefault.setToolTipText(LPMain.getTextRespectUISPr("text.default"));
		wbuDefault.setIcon(getImageIconReset());
		wbuDefault.addActionListener(this);
		editor.setEditable(false);

		getInternalFrame().addItemChangedListener(this);

		this.add(editor.getView().getUIComponent(), new GridBagConstraints(0, 0, 1, 2, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));

		if (!bWithoutButtons) {
			add(wbuEdit, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
			add(wbuDefault, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.1, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0), 0, 0));
		}

		setDefaults();
	}

	protected void setDocumentWidth(int width) {
		editor.setPageWidth(width, SizeUnit.PIXEL);
	}

	@Override
	public void removeContent() throws Throwable {
		editor.getModel().removeAllBlocks();
	}

	@Override
	public boolean hasContent() throws Throwable {
		return !editor.getModel().isEmpty();
	}

	@Override
	public void aktiviereRechtschreibpruefung() {
		editor.aktiviereRechtschreibpruefung();
	}

	@Override
	public void deaktiviereRechtschreibpruefung() {
		editor.deaktiviereRechtschreibpruefung();
	}

	@Override
	public void setRechtschreibpruefungLocale(Locale loc) {
		editor.setRechtschreibpruefungLocale(loc);
	}

	@Override
	public boolean isMandatoryField() {
		return isMandatoryField;
	}

	@Override
	public void setMandatoryField(boolean isMandatoryField) {
		this.isMandatoryField = isMandatoryField;
	}

	@Override
	public boolean isActivatable() {
		return isActivatable;
	}

	@Override
	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
	}
	
	public void setEditable(boolean bEnabled) {
		wbuDefault.setEnabled(bEnabled);
		wbuEdit.setEnabled(bEnabled);
		editor.setEditable(bEnabled);
	}
	
	public void setDefaults() {
		editor.getModel().beginCompoundUpdate();
		editor.getModel().removeAllBlocks();
		TextBlockController newTextBlock = new TextBlockController(editor);
		newTextBlock.setText(defaultText);
		if (maximaleLaenge.isPresent()) {
			newTextBlock.getModel().setMaximumLength(maximaleLaenge.getAsInt());
		}
		editor.addBlock(newTextBlock, new BlockPosition(0, 0), InsertPosition.BEFORE_VERTICAL);
		editor.getModel().endCompoundUpdate();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_EDITORFIELD_DEFAULT)) {
			setDefaults();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_EDITORFIELD_EDIT)) {
			getInternalFrame().showPanelBlockEditor(this, getAdd2Title());
		}
	}

	private ImageIcon getImageIconEdit() {
		if (imageIconEdit == null) {
			imageIconEdit = new ImageIcon(getClass().getResource("/com/lp/client/res/notebook.png"));
		}
		return imageIconEdit;
	}

	private ImageIcon getImageIconReset() {
		if (imageIconReset == null) {
			imageIconReset = new ImageIcon(getClass().getResource("/com/lp/client/res/undo.png"));
		}
		return imageIconReset;
	}

	public String getDefaultText() {
		return defaultText;
	}

	/**
	 * Den Default-Text fuer dieses Editorfeld setzen. wreditf: 3 optionaler
	 * Default-Text
	 *
	 * @param defaultText String
	 */
	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public void setMaxLaenge(int maxLaenge) {
		this.maximaleLaenge = OptionalInt.of(maxLaenge);
		for (BlockWithPosition block : editor.getModel()) {
			if (block.block.getModel() instanceof TextBlockModel) {
				TextBlockModel model = (TextBlockModel) block.block.getModel();
				model.setMaximumLength(maxLaenge);
			}
		}
	}

	public EditorContentDto getContent() {
		return editor.toDto();
	}

	public void setContent(EditorContentDto contentDto) {
		editor.fromDto(contentDto, false);
	}

	/**
	 * Nur fuer einfachere Kompatibilitaet mit alten WrapperEditorField. Es soll
	 * {@link WrapperBlockEditorField#setContent(EditorContentDto)} verwendet werden
	 * 
	 * @param text
	 */
	@Deprecated
	public void setText(String text) {
		EditorContentDto cDto = new EditorContentDto();
		EditorTextBlockDto blockDto = new EditorTextBlockDto(0, 0, text);
		cDto.addBlockDto(blockDto);
		setContent(cDto);
	}

	/**
	 * 
	 * @param text
	 */
	@Deprecated
	public String getText() {
		return new EditorContentLegacyFormatter().formatContent(getContent());
	}
}
