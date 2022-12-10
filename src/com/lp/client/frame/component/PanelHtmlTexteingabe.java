package com.lp.client.frame.component;

import java.awt.BorderLayout;

import com.lp.editor.util.TextBlockOverflowException;

public class PanelHtmlTexteingabe extends PanelBasis implements IEditableTextEditor {
	private static final long serialVersionUID = -2925884881365060787L;

	private WrapperHtmlField htmlField;
	
	public PanelHtmlTexteingabe(InternalFrame internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		setLayout(new BorderLayout());
		
		htmlField = new WrapperHtmlField(getInternalFrame(), getAdd2Title(), false);
		add(htmlField, BorderLayout.CENTER);
	}

	@Override
	public void requestFocus() {
		htmlField.requestFocusInWindow();
	}
	
	@Override
	public void setText(String text) {
		htmlField.setText(text);
	}

	@Override
	public String getText() throws TextBlockOverflowException {
		String text = htmlField.getText();
		
		return isHtml(text) ? text : htmlField.getHtmlText();
	}

	@Override
	public void setDefaultText(String defaultText) {
		htmlField.setDefaultText(defaultText);
	}

	@Override
	public void setEditable(boolean value) {
		htmlField.setEditable(value);
	}

	private boolean isHtml(String text) {
		return text != null && 
				(text.startsWith("<html") || text.startsWith("<HTML") || text.startsWith("<!DOCTYPE"));
	}
	
	@Override
	protected String getLockMeWer() throws Exception {
		return null;
	}
}
