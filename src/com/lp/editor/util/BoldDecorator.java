package com.lp.editor.util;

public class BoldDecorator extends TextDecorator {

	public BoldDecorator(IStyledText decoratedText) {
		super(decoratedText);
	}

	@Override
	public String asHtml() {
		return "<b>" + super.asHtml() + "</b>";
	}
}
