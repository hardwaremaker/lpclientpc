package com.lp.editor.util;

public class PlainText implements IStyledText {

	private String plainText;
	
	public PlainText(String plainText) {
		this.plainText = plainText;
	}

	@Override
	public String getPlainText() {
		return plainText;
	}
	
	@Override
	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}
	
	@Override
	public String asHtml() {
		return plainText;
	}

}
