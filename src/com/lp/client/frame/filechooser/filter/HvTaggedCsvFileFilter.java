package com.lp.client.frame.filechooser.filter;

public class HvTaggedCsvFileFilter extends HvTaggedFileFilter {

	private final String tag;
	
	public HvTaggedCsvFileFilter() {
		this("CSV");
	}
	
	public HvTaggedCsvFileFilter(String tag) {
		super(new HvTaggedFileExtensionAcceptor(FileExtension.CSV));
		this.tag = tag;		
	}
	
	@Override
	public String getTag() {
		return tag;
	}
}
