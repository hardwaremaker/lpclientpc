package com.lp.client.frame.filechooser.filter;

import java.io.File;

public class HvImageFileFilter extends HvFileFilterBase {

	private IAccept<File> acceptHandler;
	private FileExtensionAcceptor extensionAcceptHandler;
	
	public HvImageFileFilter() {
		extensionAcceptHandler = new JpgAcceptor(new PngAcceptor(new TiffAcceptor(new GifAcceptor())));
		acceptHandler = new DirectoryAcceptor(extensionAcceptHandler);
	}

	@Override
	public String getDescription() {
		return "Bilder " + extensionAcceptHandler.extensionsAsString();
	}

	@Override
	protected IAccept<File> getAcceptHandler() {
		return acceptHandler;
	}

}
