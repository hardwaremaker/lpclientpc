package com.lp.client.frame.filechooser.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public abstract class HvFileFilterBase extends FileFilter {

	protected abstract IAccept<File> getAcceptHandler();
	
	@Override
	public boolean accept(File f) {
		return getAcceptHandler().accept(f);
	}

}
