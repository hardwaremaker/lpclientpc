package com.lp.client.frame.filechooser.open;

import java.io.File;

import com.lp.client.frame.filechooser.filter.CsvAcceptor;

public class DirectoryFileCsv extends BaseDirectoryFile<CsvFile> {

	public DirectoryFileCsv(File file) {
		super(file);
		setFileAcceptor(new CsvAcceptor());
	}

	@Override
	protected CsvFile createWrapperFile(File file) {
		return new CsvFile(file);
	}
}
