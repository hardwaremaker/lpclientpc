package com.lp.client.frame.filechooser.filter;

import java.io.File;

public abstract class FilenameAcceptor extends FileAcceptor {

	public FilenameAcceptor() {
		super();
	}
	
	public FilenameAcceptor(IAccept<File> acceptorDecorator) {
		super(acceptorDecorator);
	}
	
	protected boolean acceptImpl(File file) {
		if (file == null) return false;
		String name = file.getName();
		return acceptImpl(name);
	}

	protected abstract boolean acceptImpl(String filename);
}
