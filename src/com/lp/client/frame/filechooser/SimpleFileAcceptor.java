package com.lp.client.frame.filechooser;

import java.io.File;
import java.util.function.Function;

import com.lp.client.frame.filechooser.WrapperFileChooser.SelectResult;

public class SimpleFileAcceptor implements Function<File, SelectResult> {

	private File selectedFile = null;

	public SimpleFileAcceptor() {
	}
	
	public File getSelectedFile() {
		return selectedFile;
	}

	@Override
	public SelectResult apply(File t) {
		selectedFile = t;
		return SelectResult.ACCEPT;
	}

}
