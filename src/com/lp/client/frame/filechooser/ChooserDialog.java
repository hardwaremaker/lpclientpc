package com.lp.client.frame.filechooser;

import java.io.File;
import java.util.Locale;

public interface ChooserDialog<T extends ChooserDialog<? super T>> {

	/**
	 * Den Titel des Dialogs bestimmen
	 * 
	 * @param title ist der gew&uuml;nschte Titel
	 * @return
	 */
	T dialogTitle(String title);
	
	/**
	 * Das vorausgew&auml;hlte Verzeichnis bestimmen
	 * 
	 * @param directory das gew&uuml;nschte Verzeichnis
	 * @return
	 */
	T directory(File directory);
	
	/**
	 * Einen vorausgew&auml;hlten Dateinamen setzen</br>
	 * <p>Enth&auml;lt das File auch ein Verzeichnis, wird
	 * versucht, dieses Verzeichnis als aktuelles Verzeichnis
	 * auszuw&auml;hlen.</p>
	 * @param preselectedFilename falls null wird nichts gemacht
	 * @return
	 */
	T selectedFile(File preselectedFilename);
	
	/**
	 * Die Locale des Dialogs setzen
	 * 
	 * @param locale
	 * @return
	 */
	T locale(Locale locale);
	
	/**
	 * Den Text des "&Ouml;ffnen" bzw. "Speichern" Buttons des
	 * Dialogs setzen</br>
	 * 
	 * @param prompt
	 * @return
	 */
	T prompt(String prompt);
}
