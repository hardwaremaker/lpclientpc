package com.lp.client.frame.filechooser.filter;

public class HvXmlFileFilter extends HvFileFilterWithDirectoriesBase {

	private FileExtensionAcceptor xmlAcceptHandler = new XmlAcceptor();

	@Override
	protected FileExtensionAcceptor getAcceptHandler() {
		return xmlAcceptHandler;
	}

	@Override
	public String getDescription() {
		return "XML-Dateien " + getAcceptHandler().extensionsAsString();
	}

}
