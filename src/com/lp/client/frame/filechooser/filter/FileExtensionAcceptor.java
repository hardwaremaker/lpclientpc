package com.lp.client.frame.filechooser.filter;

import java.util.EnumSet;

public abstract class FileExtensionAcceptor extends FilenameAcceptor {

	public FileExtensionAcceptor() {
		super();
	}

	public FileExtensionAcceptor(FileExtensionAcceptor acceptorDecorator) {
		super(acceptorDecorator);
	}

	@Override
	protected boolean acceptImpl(String filename) {
		for (FileExtension extension : validExtensionsImpl()) {
			if (filename.toLowerCase().endsWith(extension.getValue()))
				return true;
		}
		return false;
	}

	protected abstract EnumSet<FileExtension> validExtensionsImpl();
	
	public EnumSet<FileExtension> getExtensions() {
		EnumSet<FileExtension> extensions = validExtensionsImpl();
		if (getAcceptorDecorator() instanceof FileExtensionAcceptor) {
			extensions.addAll(((FileExtensionAcceptor)getAcceptorDecorator()).getExtensions());
		}
		return extensions;
	}

	public String extensionsAsString() {
		EnumSet<FileExtension> extensions = getExtensions();
		StringBuilder builder = new StringBuilder("(");
		int i = 0;
		for (FileExtension ext : extensions) {
			builder.append("*.").append(ext.getValue());
			if (++i < extensions.size()) {
				builder.append(", ");
			}
		}
		builder.append(")");
		return builder.toString();
	}
}
