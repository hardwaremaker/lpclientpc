package com.lp.client.frame.filechooser.filter;

public class HvTaggedFileExtensionFilter extends HvTaggedFileFilter {
	public HvTaggedFileExtensionFilter(FileExtension extension) {
		super(new HvTaggedFileExtensionAcceptor(extension));
	}
	
	@Override
	public String getTag() {
		String firstExtension = acceptor().getStringExtensions().get(0);
		return firstExtension.toUpperCase();
	}
}
