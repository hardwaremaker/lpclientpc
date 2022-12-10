package com.lp.client.frame.filechooser.filter;


public class HvCsvFileFilter extends HvFileFilterWithDirectoriesBase {

	private FileExtensionAcceptor csvAcceptHandler = new CsvAcceptor();
	
	@Override
	protected FileExtensionAcceptor getAcceptHandler() {
		return csvAcceptHandler;
	}

	@Override
	public String getDescription() {
		return "CSV-Dateien " + getAcceptHandler().extensionsAsString();
	}

}
