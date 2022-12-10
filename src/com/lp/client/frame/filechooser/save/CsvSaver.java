package com.lp.client.frame.filechooser.save;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import com.lp.util.csv.LPCSVWriter;

public class CsvSaver implements IDataSaver<String[][]> {

	private Character separator;
	private Character quote;
	
	public CsvSaver(Character separator) {
		this(separator, LPCSVWriter.DEFAULT_QUOTE_CHARACTER);
	}
	
	public CsvSaver(Character separator, Character quote) {
		this.separator = separator;
		this.quote = quote;
	}

	private LPCSVWriter getDefaultCSVWriter(File file) throws IOException {
		return new LPCSVWriter(new FileWriter(file), separator, quote);
	}
	
	@Override
	public void save(String[][] data, File file) throws Exception {
		getDefaultCSVWriter(file).writeAll(Arrays.asList(data));
	}

}
