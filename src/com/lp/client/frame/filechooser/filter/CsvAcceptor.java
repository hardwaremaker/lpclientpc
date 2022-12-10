package com.lp.client.frame.filechooser.filter;

import java.util.EnumSet;


public class CsvAcceptor extends FileExtensionAcceptor {

	public CsvAcceptor() {
		super();
	}
	
	public CsvAcceptor(FileExtensionAcceptor acceptorDecorator) {
		super(acceptorDecorator);
	}
	
	@Override
	protected EnumSet<FileExtension> validExtensionsImpl() {
		return EnumSet.of(FileExtension.CSV);
	}
}
