package com.lp.client.frame.filechooser.filter;

public class HvTaggedFileExtensionAcceptor extends HvTaggedFileBaseAcceptor<FileExtension> {

	public HvTaggedFileExtensionAcceptor(FileExtension element) {
		super(element);
	}
	
	@Override
	protected String asString(FileExtension element) {
		return element.getValue();
	}
}
