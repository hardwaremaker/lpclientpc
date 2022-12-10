package com.lp.client.frame.filechooser.filter;

import java.util.EnumSet;

public class TiffAcceptor extends FileExtensionAcceptor {

	public TiffAcceptor() {
		super();
	}

	public TiffAcceptor(FileExtensionAcceptor acceptorDecorator) {
		super(acceptorDecorator);
	}

	@Override
	protected EnumSet<FileExtension> validExtensionsImpl() {
		return EnumSet.of(FileExtension.TIF, FileExtension.TIFF);
	}
}
