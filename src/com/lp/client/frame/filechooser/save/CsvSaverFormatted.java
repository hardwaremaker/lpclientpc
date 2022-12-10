package com.lp.client.frame.filechooser.save;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import com.lp.util.csv.LPCSVWriter;

public class CsvSaverFormatted implements IDataSaver<Object[][]> {

	private Locale locale;
	private Character separator;
	private Character quote;
	
	public CsvSaverFormatted(Locale locale, Character separator) {
		this(locale, separator, LPCSVWriter.DEFAULT_QUOTE_CHARACTER);
	}
	
	public CsvSaverFormatted(Locale locale, Character separator, Character quote) {
		this.locale = locale;
		this.separator = separator;
		this.quote = quote;
	}

	private LPCSVWriter getDefaultCSVWriter(File file) throws IOException {
		return new LPCSVWriter(new FileWriter(file), separator, quote);
	}

	@Override
	public void save(Object[][] data, File file) throws Exception {
		LPCSVWriter csvWriter = getDefaultCSVWriter(file);
		for (Object[] line : data) {
			csvWriter.writeNext(line, locale);
		}
		csvWriter.close();
	}

}
