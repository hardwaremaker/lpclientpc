package com.lp.editor.util;

public class ItalicDecorator extends TextDecorator {

	public ItalicDecorator(IStyledText decoratedText) {
		super(decoratedText);
	}

	@Override
	public String asHtml() {
		return "<i>" + super.asHtml() + "</i>";
	}
}
