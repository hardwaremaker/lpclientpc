package com.lp.client.frame.filechooser.filter;

public class HvTaggedMailFilter extends HvTaggedFileFilter {
	public HvTaggedMailFilter() {
		super(new HvTaggedUntypedStringAcceptor("eml").add("msg"));
	}
	
	@Override
	public String getTag() {
		return "MAIL";
	}
}
