package com.lp.client.frame.filechooser.filter;

import java.io.File;
import java.util.Locale;

import com.lp.client.frame.filechooser.save.CsvSaverFormatted;
import com.lp.client.frame.filechooser.save.IDataSaver;

public class HvTxtFileFilterTabSaver extends HvTxtFileFilter implements IDataSaver<Object[][]> {

	private CsvSaverFormatted csvSaver;

	public HvTxtFileFilterTabSaver(Locale locale) {
		csvSaver = new CsvSaverFormatted(locale, new Character('\t'));
	}
	
	@Override
	public void save(Object[][] data, File file) throws Exception {
		csvSaver.save(data, file);
	}

	@Override
	public String getDescription() {
		return "TXT-Dateien (Tabulator)" + getAcceptHandler().extensionsAsString();
	}
}
