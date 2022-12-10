package com.lp.client.frame.filechooser.filter;

import java.util.EnumSet;

public class JpgAcceptor extends FileExtensionAcceptor {

	public JpgAcceptor() {
		super();
	}

	public JpgAcceptor(FileExtensionAcceptor acceptorDecorator) {
		super(acceptorDecorator);
	}

	@Override
	protected EnumSet<FileExtension> validExtensionsImpl() {
		return EnumSet.of(FileExtension.JPEG, FileExtension.JPG);
	}

}
