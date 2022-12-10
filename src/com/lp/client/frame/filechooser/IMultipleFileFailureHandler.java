package com.lp.client.frame.filechooser;

import java.io.File;
import java.util.List;

/**
 * Interface um nicht existierende Datei mit WrapperFileChooser zu behandeln.
 * @author Alexander Daum
 *
 */
public interface IMultipleFileFailureHandler {
	/**
	 * Wird aufgerufen, wenn in WrapperFileChooser eine oder mehrere nicht existierende Datein gefunden werden.  
	 * @param notExistingFiles Liste der nicht existierenden Dateien
	 * @param existingFiles Liste der existierenden Dateien
	 * @return true, falls die Dateien trotzdem akzeptiert werden sollen, 
	 */
	boolean handleFilesNotFound(List<File> notExistingFiles, List<File> existingFiles);
}
