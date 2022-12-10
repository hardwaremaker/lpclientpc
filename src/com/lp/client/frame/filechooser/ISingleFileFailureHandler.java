package com.lp.client.frame.filechooser;

import java.io.File;

/**
 * Interface um nicht existierende Datei mit WrapperFileChooser zu behandeln.
 * @author Alexander Daum
 *
 */
public interface ISingleFileFailureHandler {
	/**
	 * Wird aufgerufen, wenn die im {@link WrapperFileChooser} ausgew&auml;hlte Datei nicht existiert
	 * @param notFound die nicht gefundene Datei
	 * @return true, falls abgebrochen werden soll, false um die Auswahl erneut zu starten
	 */
	boolean cancelOnFileNotFound(File notFound);
}
