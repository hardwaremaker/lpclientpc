package com.lp.client.frame.filechooser.filter;

import java.io.File;

public class DirectoryAcceptor extends FileAcceptor {

	public DirectoryAcceptor() {
		super();
	}

	public DirectoryAcceptor(IAccept<File> acceptorDecorator) {
		super(acceptorDecorator);
	}

	@Override
	public boolean acceptImpl(File file) {
		return file.isDirectory();
	}

}
