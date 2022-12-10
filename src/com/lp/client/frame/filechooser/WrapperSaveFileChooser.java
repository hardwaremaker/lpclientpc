package com.lp.client.frame.filechooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.filechooser.filter.HvTaggedFileFilter;
import com.lp.client.frame.filechooser.save.IDataSaver;
import com.lp.server.util.HvOptional;
import com.lp.server.util.Validator;

public class WrapperSaveFileChooser<T> extends WrapperFileChooser {

	private IDataSaver<T> saver;
	
	public WrapperSaveFileChooser() {
		super();
	}

	public WrapperSaveFileChooser(String token) {
		super(token);
	}

	@Override
	protected int showDialogImpl() {
		return getFileChooser().showSaveDialog(HvOptional.ofNullable(getParentComponent()));
	}

	protected <E extends FileFilter & IDataSaver<T>> void addChoosableSaveFileFilter(E filter) {
		getFileChooser().addChoosableFileFilter(filter);
	}

	protected void addChoosableFileFilter(FileFilter filter) {
		getFileChooser().addChoosableFileFilter(filter);		
	}
	
	/**
	 * Speichert die angegebenen Daten in der vom Anwender 
	 * gew&auml;hlten Datei</br>
	 * <p>Sollte die Datei schon existieren wird beim 
	 * Anwender nachgefragt, ob er sie tats&auml;chlich &uuml;berschreiben
	 * will. </p>
	 * @param data sind die zu speichernden Daten
	 * @return null wenn der Anwender keine Datei zum Speichern 
	 * gew&auml;hlt hat ansonsten die gew&auml;hlte Datei
	 * @throws Exception
	 */
	public File save(T data) throws Exception {
		return save(data, false);
	}

	/**
	 * Speichert die angegebene Daten in der vom Anwender 
	 * gew&auml;hlten Datei</br>
	 * <p>Eine bereits existierende Datei wird ohne weitere Meldung
	 * &uuml;berschrieben.</p>
	 * @param data
	 * @return null wenn der Anwender keine Datei zum Speichern 
	 * gew&auml;hlt hat ansonsten die gew&auml;hlte Datei
	 * @throws Exception
	 */
	public File saveWithoutWarning(T data) throws Exception {
		return save(data, true);
	}
	
	private File save(T data, boolean justOverwrite) throws Exception {
		SimpleFileAcceptor acceptor = new SimpleFileAcceptor();
		choose(acceptor);
		
		if(acceptor.getSelectedFile() == null) {
			return null;
		}

		if (justOverwrite || 
				shouldOverwriteFile(acceptor.getSelectedFile())) {
			return saveImpl(data, acceptor.getSelectedFile());
		}

		return null;		
	}
	
	private File saveImpl(T data, File selectedFile) throws Exception {
		if (this.saver != null) {
			this.saver.save(data, selectedFile);
		} else {
			FileFilter saver = getFileChooser().fileFilter();
			if (!(saver instanceof IDataSaver<?>)) {
				String errorMessage = "Could not save data of class '" + data.getClass()
						+ "' because the selected FileFilter '" + (saver == null ? "null" : saver.getClass())
						+ "' is not an instance of '" + IDataSaver.class + "'";
				myLogger.error(errorMessage);
				throw new Exception(errorMessage);
			}
			
			((IDataSaver<T>) saver).save(data, selectedFile);			
		}
		
		return selectedFile;
	}

	/**
	 * Einen expliziten FileSaver setzen</br>
	 * <p>Im Normalfall kommt jener Saver zur Anwendung, der vom Anwender
	 * (indirekt) &uuml;ber die Auswahl eines FileFilters gew&auml;hlt wurde.
	 * Gibt es keine "choosableFilter", oder wird der Saver hiermit explizit
	 * gesetzt, so kommt dieser Saver zur Anwendung.</p>
	 * @param saver
	 */
	public void filesaver(IDataSaver<T> saver) {
		Validator.notNull(saver, "saver");
		this.saver = saver;
	}
	
	/**
	 * L&auml;sst den Anwender die Datei ausw&auml;hlen in die
	 * sp&auml;ter geschrieben werden soll.</br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public File selectSingle() throws Exception {
		return selectSingle(false);
	}
	
	public File selectSingleWithoutWarning() throws Exception {
		return selectSingle(true);
	}
	
	private File selectSingle(boolean justOverwrite) throws Exception {
		SimpleFileAcceptor acceptor = new SimpleFileAcceptor();
		choose(acceptor);
		
		if(acceptor.getSelectedFile() == null) {
			return null;
		}

		File f = normalizeSaveFile(acceptor.getSelectedFile());
	
		if (justOverwrite || shouldOverwriteFile(f)) {
			return f;
		}

		return null;		
	}
	
	private File normalizeSaveFile(File sourceFile) {
		FileFilter filter = getFileChooser().fileFilter();
		if (filter instanceof HvTaggedFileFilter) {
			return ((HvTaggedFileFilter)filter).appendExtension(sourceFile);
		} 
		
		return sourceFile;
	}
}
