package com.lp.client.frame.filechooser.filter;

import java.io.File;

public class HvDirectoryFilter extends HvFileFilterBase {

	private IAccept<File> dirAcceptor = new DirectoryAcceptor();
	
	@Override
	public String getDescription() {
		return "Ordner";
	}

	@Override
	protected IAccept<File> getAcceptHandler() {
		return dirAcceptor;
	}

}
