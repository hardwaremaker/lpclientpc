package com.lp.client.frame.filechooser.filter;

import java.util.EnumSet;

public class TxtAcceptor extends FileExtensionAcceptor {

	public TxtAcceptor() {
		super();
	}
	
	public TxtAcceptor(FileExtensionAcceptor acceptorDecorator) {
		super(acceptorDecorator);
	}

	@Override
	protected EnumSet<FileExtension> validExtensionsImpl() {
		return EnumSet.of(FileExtension.TXT);
	}

}
