package com.lp.editor.util;

public abstract class TextDecorator implements IStyledText {

	protected IStyledText decoratedText;
	
	public TextDecorator(IStyledText decoratedText) {
		this.decoratedText = decoratedText;
	}

	@Override
	public String getPlainText() {
		return getDecoratedText().getPlainText();
	}
	
	@Override
	public void setPlainText(String plainText) {
		getDecoratedText().setPlainText(plainText);
	}
	
	@Override
	public String asHtml() {
		return getDecoratedText().asHtml();
	}

	protected IStyledText getDecoratedText() {
		return decoratedText;
	}
}
