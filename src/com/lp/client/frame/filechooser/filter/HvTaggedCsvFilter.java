package com.lp.client.frame.filechooser.filter;

public class HvTaggedCsvFilter extends HvTaggedFileFilter {

	public HvTaggedCsvFilter() {
		super(new HvTaggedFileExtensionAcceptor(FileExtension.CSV));
	}
	
	@Override
	public String getTag() {
		return "CSV";
	}
}
