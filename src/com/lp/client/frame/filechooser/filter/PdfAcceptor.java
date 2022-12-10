package com.lp.client.frame.filechooser.filter;

import java.util.EnumSet;

public class PdfAcceptor extends FileExtensionAcceptor {

	public PdfAcceptor() {
		super();
	}

	public PdfAcceptor(FileExtensionAcceptor acceptorDecorator) {
		super(acceptorDecorator);
	}

	@Override
	protected EnumSet<FileExtension> validExtensionsImpl() {
		return EnumSet.of(FileExtension.PDF);
	}

}
