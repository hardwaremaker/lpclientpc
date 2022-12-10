package com.lp.client.frame.filechooser.open;

import java.io.File;

import com.lp.client.frame.filechooser.filter.FileExtension;

public class CsvXlsFile extends WrapperFile {	

	public CsvXlsFile(File f) {
		super(f);
	}
	
	public CsvXlsFile(File f, FileExtension extension) {
		super(f, extension);
	}
	
	
	public boolean isCsv() {
		return hasFile() && FileExtension.CSV.equals(getExtension());
	}
	
	public boolean isXls() {
		return hasFile() && FileExtension.XLS.equals(getExtension());
	}
}
