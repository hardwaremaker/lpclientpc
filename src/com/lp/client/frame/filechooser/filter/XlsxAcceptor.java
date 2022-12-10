package com.lp.client.frame.filechooser.filter;

import java.util.EnumSet;

public class XlsxAcceptor extends FileExtensionAcceptor {

	public XlsxAcceptor() {
		super();
	}
	
	public XlsxAcceptor(FileExtensionAcceptor acceptorDecorator) {
		super(acceptorDecorator);
	}

	@Override
	protected EnumSet<FileExtension> validExtensionsImpl() {
		return EnumSet.of(FileExtension.XLSX);
	}
}
