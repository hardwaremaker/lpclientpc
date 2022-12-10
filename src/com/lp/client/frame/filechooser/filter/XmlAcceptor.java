package com.lp.client.frame.filechooser.filter;

import java.util.EnumSet;

public class XmlAcceptor extends FileExtensionAcceptor {

	public XmlAcceptor() {
	}

	public XmlAcceptor(FileExtensionAcceptor acceptorDecorator) {
		super(acceptorDecorator);
	}

	@Override
	protected EnumSet<FileExtension> validExtensionsImpl() {
		return EnumSet.of(FileExtension.XML);
	}

}
