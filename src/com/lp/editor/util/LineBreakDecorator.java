package com.lp.editor.util;

public class LineBreakDecorator extends TextDecorator {

	public LineBreakDecorator(IStyledText decoratedText) {
		super(decoratedText);
	}

	@Override
	public String asHtml() {
		return super.asHtml() + "<br />";
	}
}
