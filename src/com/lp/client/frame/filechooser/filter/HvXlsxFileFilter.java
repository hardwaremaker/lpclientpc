package com.lp.client.frame.filechooser.filter;

public class HvXlsxFileFilter extends HvFileFilterWithDirectoriesBase {
	private FileExtensionAcceptor xlsxAcceptor = new XlsxAcceptor();

	@Override
	protected FileExtensionAcceptor getAcceptHandler() {
		return xlsxAcceptor;
	}

	@Override
	public String getDescription() {
		return "XLSX-Dateien " + getAcceptHandler().extensionsAsString();
	}
}
