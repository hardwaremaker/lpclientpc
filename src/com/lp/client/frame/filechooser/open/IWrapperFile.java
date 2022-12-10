package com.lp.client.frame.filechooser.open;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.lp.client.frame.filechooser.filter.FileExtension;

public interface IWrapperFile {
	File getFile();
	FileExtension getExtension();
	
	/**
	 * Liefert die komplette Datei als ByteArray
	 * 
	 * @return byteArray der gesamten Daten
	 * @throws IOException
	 */
	byte[] getBytes() throws IOException;
	
	/**
	 * Liefert die Datei als FileInputStream
	 * 
	 * @return Datei als FileInputStream
	 * @throws FileNotFoundException
	 */
	InputStream getInputStream() throws FileNotFoundException;
}
