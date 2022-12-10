package com.lp.client.frame.filechooser.filter;

import java.io.File;

public class HvUntypedFileFilter extends HvFileFilterWithDirectoriesBase {

	private final HvUntypedFileAcceptor acceptor;
	
	public HvUntypedFileFilter(String extension) {
		acceptor = new HvUntypedFileAcceptor(extension);
	}
	
	@Override
	protected IAccept<File> getAcceptHandler() {
		return acceptor;
	}

	@Override
	public String getDescription() {
		return acceptor.taggedExtension() 
				+ "-Dateien " 
				+  acceptor.extensionsAsString();
	}
}
