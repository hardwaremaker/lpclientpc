package com.lp.client.frame.filechooser.filter;

import java.util.EnumSet;

public class XlsAcceptor extends FileExtensionAcceptor {

	public XlsAcceptor() {
		super();
	}
	
	public XlsAcceptor(FileExtensionAcceptor acceptorDecorator) {
		super(acceptorDecorator);
	}

	@Override
	protected EnumSet<FileExtension> validExtensionsImpl() {
		return EnumSet.of(FileExtension.XLS);
	}
}
