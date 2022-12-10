package com.lp.client.frame.filechooser.open;

import java.io.File;

public class DirectoryFile extends BaseDirectoryFile<WrapperFile> {

	public DirectoryFile(File directory) {
		super(directory);
	}

	@Override
	protected WrapperFile createWrapperFile(File file) {
		return new WrapperFile(file);
	}

}
