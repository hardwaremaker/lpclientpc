package com.lp.client.frame.filechooser.filter;

public class HvXlsFileFilter extends HvFileFilterWithDirectoriesBase {
	
	private FileExtensionAcceptor xlsAcceptor = new XlsAcceptor();
	
	@Override
	protected FileExtensionAcceptor getAcceptHandler() {
		return xlsAcceptor;
	}

	@Override
	public String getDescription() {
		return "XLS-Dateien " + getAcceptHandler().extensionsAsString();
	}

}
