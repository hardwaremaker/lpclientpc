package com.lp.client.frame.filechooser.filter;

import java.io.File;

public abstract class HvFileFilterWithDirectoriesBase extends HvFileFilterBase {

	@Override
	public boolean accept(File f) {
		return f.isDirectory() || super.accept(f);
	}
}
