package com.lp.client.frame.filechooser.filter;

import java.io.File;
import java.util.Locale;

import com.lp.client.frame.filechooser.save.CsvSaverFormatted;
import com.lp.client.frame.filechooser.save.IDataSaver;

public class HvCsvFileFilterSemikolonSaver extends HvCsvFileFilter implements IDataSaver<Object[][]> {

	private CsvSaverFormatted csvSaver;
	
	public HvCsvFileFilterSemikolonSaver(Locale locale) {
		csvSaver = new CsvSaverFormatted(locale, new Character(';'));
	}
	
	@Override
	public void save(Object[][] data, File file) throws Exception {
		csvSaver.save(data, file);
	}

	@Override
	public String getDescription() {
		return "CSV-Dateien (Semikolon)" + getAcceptHandler().extensionsAsString();
	}
}
