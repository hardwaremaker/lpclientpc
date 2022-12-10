package com.lp.client.frame.filechooser.filter;

/**
 * Ein FileFilter der eine beliebige Extension akzeptiert</br>
 * 
 * @author ghp
 */
public class HvTaggedUntypedStringFilter extends HvTaggedFileFilter {

	public HvTaggedUntypedStringFilter(String filextension) {
		super(new HvTaggedUntypedStringAcceptor(filextension));
	}
	
	@Override
	public String getTag() {
		String firstExtension = acceptor().getStringExtensions().get(0);
		return firstExtension.toUpperCase();
	}
}
