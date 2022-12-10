package com.lp.client.frame.filechooser.filter;

public class HvUntypedFileAcceptor extends FilenameAcceptor {
	private final String extension;
	
	/**
	 * Die zul&auml;ssige Fileextension.</br>
	 * <p>Eine Fileextension ist "eml" oder auch ".eml"</p>
	 * 
	 * @param extension
	 */
	public HvUntypedFileAcceptor(String extension) {
		this.extension = extension;
	}
	
	@Override
	protected boolean acceptImpl(String filename) {
		return filename.toLowerCase().endsWith(extension.toLowerCase());
	}

	/**
	 * Die unver&auml;nderte Extension, so wie im Constructor
	 * 
	 * @return
	 */
	public String extension() {
		return this.extension;
	}
	
	public String taggedExtension() {
		return extension().replace(".", "").toUpperCase();
	}
	
	public String extensionsAsString() {
		return "(*" + extension() + ")";
	}
}
