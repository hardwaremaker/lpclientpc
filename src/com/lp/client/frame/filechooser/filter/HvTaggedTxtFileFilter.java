package com.lp.client.frame.filechooser.filter;

public class HvTaggedTxtFileFilter extends HvTaggedFileFilter {

	private final String tag;
	
	public HvTaggedTxtFileFilter() {
		this("TXT");
	}
	
	public HvTaggedTxtFileFilter(String tag) {
		super(new HvTaggedFileExtensionAcceptor(FileExtension.TXT));
		this.tag = tag;
	}
	
	@Override
	public String getTag() {
		return tag;
	}
}
