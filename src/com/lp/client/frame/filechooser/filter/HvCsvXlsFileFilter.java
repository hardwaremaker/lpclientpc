package com.lp.client.frame.filechooser.filter;

public class HvCsvXlsFileFilter extends HvFileFilterWithDirectoriesBase {

	private FileExtensionAcceptor csvXlsAcceptHandler = new CsvAcceptor(new XlsAcceptor());
	
	@Override
	protected FileExtensionAcceptor getAcceptHandler() {
		return csvXlsAcceptHandler;
	}

	@Override
	public String getDescription() {
		return getAcceptHandler().extensionsAsString();
	}

}
