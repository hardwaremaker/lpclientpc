package com.lp.client.frame.filechooser.filter;

public class HvTaggedUntypedStringAcceptor extends HvTaggedFileBaseAcceptor<String> {
	public HvTaggedUntypedStringAcceptor(String extension) {
		super(extension);
	}

	@Override
	protected String asString(String element) {
		return element;
	}
}
