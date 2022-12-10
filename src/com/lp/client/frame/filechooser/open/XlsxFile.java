package com.lp.client.frame.filechooser.open;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lp.client.frame.filechooser.filter.FileExtension;

public class XlsxFile extends WrapperFile {
	public XlsxFile(File file) {
		super(file, FileExtension.XLSX);
	}

	public XSSFWorkbook createWorkbook() throws InvalidFormatException, IOException {
		return new XSSFWorkbook(getFile());
	}
}
