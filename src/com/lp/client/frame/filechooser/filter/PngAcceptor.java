package com.lp.client.frame.filechooser.filter;

import java.util.EnumSet;

public class PngAcceptor extends FileExtensionAcceptor {

	public PngAcceptor() {
		super();
	}

	public PngAcceptor(FileExtensionAcceptor acceptorDecorator) {
		super(acceptorDecorator);
	}

	@Override
	protected EnumSet<FileExtension> validExtensionsImpl() {
		return EnumSet.of(FileExtension.PNG);
	}
}
