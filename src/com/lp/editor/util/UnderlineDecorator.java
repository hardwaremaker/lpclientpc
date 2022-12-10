package com.lp.editor.util;

public class UnderlineDecorator extends TextDecorator {

	public UnderlineDecorator(IStyledText decoratedText) {
		super(decoratedText);
	}

	@Override
	public String asHtml() {
		return "<u>" + super.asHtml() + "</u>";
	}
}
