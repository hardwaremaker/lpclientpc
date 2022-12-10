package com.lp.client.frame.filechooser.filter;

public class HvTaggedXlsFilter extends HvTaggedFileFilter {

	public HvTaggedXlsFilter() {
		super(new HvTaggedFileExtensionAcceptor(FileExtension.XLS));
	}
	
	@Override
	public String getTag() {
		return "XLS";
	}
}
