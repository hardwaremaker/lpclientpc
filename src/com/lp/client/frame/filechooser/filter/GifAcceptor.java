package com.lp.client.frame.filechooser.filter;

import java.util.EnumSet;

public class GifAcceptor extends FileExtensionAcceptor {

	public GifAcceptor() {
		super();
	}

	public GifAcceptor(FileExtensionAcceptor acceptorDecorator) {
		super(acceptorDecorator);
	}

	@Override
	protected EnumSet<FileExtension> validExtensionsImpl() {
		return EnumSet.of(FileExtension.GIF);
	}

}
