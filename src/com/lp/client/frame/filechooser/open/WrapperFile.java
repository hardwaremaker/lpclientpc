package com.lp.client.frame.filechooser.open;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.lp.client.frame.filechooser.filter.FileExtension;

public class WrapperFile implements IWrapperFile {

	private final File file;
	private final FileExtension extension;
	
	public WrapperFile(File file) {
		this(file, FileExtension.INVALID);
	}
	
	public WrapperFile(File file, FileExtension extension) {
		this.file = file;
		this.extension = extension;
	}
	
	public File getFile() {
		return file;
	}
	
	public FileExtension getExtension() {
		return extension;
	}
	
	public byte[] getBytes() throws IOException {
		if (!hasFile()) return null;
		
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[64 * 1024];
			ous = new ByteArrayOutputStream();
			ios = getInputStream();
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException ex) {
			}

			try {
				if (ios != null)
					ios.close();
			} catch (IOException ex) {
			}
		}

		return ous.toByteArray();
	}
	
	public InputStream getInputStream() throws FileNotFoundException {
		if (!hasFile()) return null;
		
		return new FileInputStream(getFile());
	}
	
	public boolean hasFile() {
		return getFile() != null;
	}
	
	public boolean has(FileExtension anExtension) {
		return getExtension().equals(anExtension);
	}
	
	/***
	 * Liefert die Dateigr&ouml;&szlig;e in KB</br>
	 * <p>Aus Kompatiblit&auml;tsgr&uuml;nden zu bestehendem
	 * Source Code wird a) ein double und b) mit Nachkommastellen
	 * verwendet (sic, ghp).</p>
	 * 
	 * @return
	 */
	public double getLengthInKB() {
		return hasFile() ? ((double) getFile().length() / 1024.0d) : 0.0d;
	}
}
