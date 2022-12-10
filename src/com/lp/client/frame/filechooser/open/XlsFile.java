package com.lp.client.frame.filechooser.open;

import java.io.File;
import java.io.IOException;

import com.lp.client.frame.filechooser.filter.FileExtension;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class XlsFile extends WrapperFile {

	public XlsFile(File file) {
		super(file, FileExtension.XLS);
	}

	public Workbook getWorkbook() throws BiffException, IOException {
		return Workbook.getWorkbook(getFile());
	}
	
	public Workbook getWorkbook(WorkbookSettings settings) throws BiffException, IOException {
		return Workbook.getWorkbook(getFile(), settings);
	}
}
