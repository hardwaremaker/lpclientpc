package com.lp.client.frame.filechooser;

import javax.swing.filechooser.FileFilter;

import com.lp.server.util.HvOptional;

public class WrapperOpenFileChooser extends WrapperFileChooser {

	public WrapperOpenFileChooser() {
		this(null);
	}

	public WrapperOpenFileChooser(String configToken) {
		super(configToken);
		getFileChooser().enableAllFileFilter();
	}

	@Override
	protected int showDialogImpl() {
		return getFileChooser().showOpenDialog(
				HvOptional.ofNullable(getParentComponent()));
	}
	
	protected void addChoosableFileFilter(FileFilter filter) {
		getFileChooser().addChoosableFileFilter(filter);
	}
}
