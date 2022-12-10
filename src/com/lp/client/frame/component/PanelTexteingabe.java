package com.lp.client.frame.component;

import java.awt.BorderLayout;

import com.lp.editor.LpEditor;
import com.lp.editor.util.TextBlockOverflowException;

public class PanelTexteingabe extends PanelBasis implements IEditableTextEditor {
	private static final long serialVersionUID = 4683633230192260415L;

	private WrapperEditorField editor;
	
	public PanelTexteingabe(InternalFrame internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		
		jbInit();
		initComponents();
	}
	
	private void jbInit() throws Throwable {
		setLayout(new BorderLayout());
		
		editor = new WrapperEditorField(getInternalFrame(), getAdd2Title());
		setupDefaultProperties();
		
		add(editor, BorderLayout.CENTER);
	}
	
	@Override
	public void requestFocus() {
		editor.requestFocusInWindow();
	}
	
	public void setText(String text) {
		editor.setText(text);
	}
	
	public String getText() throws TextBlockOverflowException {
		return editor.getPlainText();
	}
	
	public void setDefaultText(String defaultText) {
		editor.setDefaultText(defaultText);
	}
	
	public void setEditable(boolean value) {
		editor.setEditable(value);
	}
	
	private void setupDefaultProperties() {
		LpEditor lpEditor = editor.getLpEditor();
		lpEditor.disableStyledText();
		lpEditor.showMenu(false);
		lpEditor.showTableItems(false);
		lpEditor.showTabRuler(false);
	}
	
	@Override
	protected String getLockMeWer() throws Exception {
		return null;
	}

}
