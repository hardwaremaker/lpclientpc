package com.lp.client.frame.filechooser.open;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.lp.client.frame.filechooser.filter.FileExtension;
import com.lp.util.csv.LPCSVReader;

public class CsvFile extends WrapperFile {
	
	public CsvFile(File file) {
		super(file, FileExtension.CSV);
	}
	
	/**
	 * Die CSV Datei komplett mit dem LPCSVReader einlesen</br>
	 * <p>Die CSV Felder werden durch ; voneinander getrennt.
	 * " umschliesst eine Zeichenkette.
	 * 
	 * @return Liste aller gelesenen Zeilen
	 * @throws IOException
	 */
	public List<String[]> read() throws IOException {
		return readImpl(';', LPCSVReader.DEFAULT_QUOTE_CHARACTER);
	}
	
	public List<String[]> read(Character separator) throws IOException {
		return readImpl(separator, new Character('"'));
	}

	/**
	 * Liefert den LPCSVReader mit ; als Trennzeichen und " als String-Beginn
	 * @return den LPCSVReader
	 * @throws FileNotFoundException
	 */
	public LPCSVReader createLPCSVReader() throws FileNotFoundException {
		return getDefaultCSVReader(getFile(), ';', LPCSVReader.DEFAULT_QUOTE_CHARACTER);
	}
	
	private List<String[]> readImpl(Character separator, Character quote) throws IOException {
		LPCSVReader csvReader = getDefaultCSVReader(getFile(), separator, quote);
		List<String[]> lines = csvReader.readAll();
		csvReader.close();
		
		return lines;
	}

	protected LPCSVReader getDefaultCSVReader(File file, Character separator, Character quote) throws FileNotFoundException {
		return new LPCSVReader(new FileReader(file), separator, quote) ;
	}
}
