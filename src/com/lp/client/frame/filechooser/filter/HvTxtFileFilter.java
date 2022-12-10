package com.lp.client.frame.filechooser.filter;

public class HvTxtFileFilter extends HvFileFilterWithDirectoriesBase {

	private FileExtensionAcceptor txtAcceptor = new TxtAcceptor();
	
	@Override
	protected FileExtensionAcceptor getAcceptHandler() {
		return txtAcceptor;
	}

	@Override
	public String getDescription() {
		return "TXT-Dateien " + getAcceptHandler().extensionsAsString();
	}

}
