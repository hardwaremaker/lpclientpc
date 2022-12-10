package com.lp.client.frame.filechooser.filter;

public class HvPdfFileFilter extends HvFileFilterWithDirectoriesBase {
	
	private FileExtensionAcceptor pdfAcceptor = new PdfAcceptor();
	
	@Override
	public String getDescription() {
		return "PDF-Dateien " + getAcceptHandler().extensionsAsString();
	}

	@Override
	protected FileExtensionAcceptor getAcceptHandler() {
		return pdfAcceptor;
	}

}
