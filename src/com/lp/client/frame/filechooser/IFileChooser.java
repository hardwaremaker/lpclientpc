package com.lp.client.frame.filechooser;

import java.awt.Component;
import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

import com.lp.server.util.HvOptional;

public interface IFileChooser {
	/**
	 * Welche Art von "Dateien" kann gew&auml;hlt werden?</br>
	 * <p>Dateien, Verzeichnisse, oder beides</p>
	 * 
	 * @param fileSelectionMode Auswahl von Dateien/Verzeichnissen/beiden
	 */
	void selectionMode(FileSelectionMode fileSelectionMode);
	
	/** 
	 * Die Art der m&ouml;glichen Dateien</br>
	 * <p>Dateien, Verzeichnisse, oder beides</p>
	 * @return Auswahl von Dateien/Verzeichnissen/beiden
	 */
	FileSelectionMode selectionMode();
	
	/**
	 * Es darf nur eine Datei/Verzeichnis gew&auml;hlt werden
	 */
	void beSingleSelect();
	
	/**
	 * Es d&uuml;rfen mehrere Dateien gew&auml;hlt werden
	 */
	void beMultiSelect();
	
	/**
	 * Eine bzw. mehrere Dateien/Verzeichnisse ausw&auml;hlen k&ouml;nnen
	 * @param multiSelect true um mehrere Dateien w&auml;hlen zu k&ouml;nnen
	 */
	void enableMultiSelect(boolean multiSelect);
	
	/**
	 * Setzt den Dialog-Titel</br>
	 * 
	 * @param title der Titel des Dialogs
	 */
	void title(String title);
	
	/**
	 * Setzt die Locale des Dialogs
	 * 
	 * @param locale die Locale des Dialogs
	 */
	void locale(Locale locale);
	
	
	/**
	 * Setzt den Text des Auswahl-Buttons
	 * 
	 * @param buttonText
	 */
	void prompt(String buttonText);
	
	void fileFilter(final FileFilter newFilter);
	
	FileFilter fileFilter();
	
	/**
	 * Der Filter "Alle Dateien" soll in der Auswahl der
	 * Filter erscheinen</br>
	 * <p>Diese Auswahl muss nicht bei jedem Betriebssystem
	 * zur Verf&uuml;gung stehen. Bei Mac zum Beispiel nicht
	 * vorhanden.</p>
	 */
	void enableAllFileFilter();
	
	/**
	 * Der Filter "Alle Dateien" wird in der Auswahl der
	 * Filter nicht angeboten</br>
	 */
	void disableAllFileFilter();
	
	/**
	 * Wird der Filter "Alle Dateien" in der Auwahl angeboten?
	 * @return true wenn der Filter "Alle Dateien" vorhanden ist
	 */
	boolean isAllFileFilterUsed();
	
	/**
	 * Eine Liste aller vom Anwender ausw&auml;hlbaren
	 * Standardfilter</br>
	 * 
	 * <p>Diese Liste ist vom Betriebssystem abh&auml;ngig</p>
	 * 
	 * @return eine (leere) Liste aller ausw&auml;hlbaren FileFilter
	 */
	List<FileFilter> choosableFileFilters();
	
	void addChoosableFileFilter(FileFilter filter);
	
	/**
	 * Liefert die ausgew&auml;hlte Datei
	 * @return die ausgew&auml;hlte Datei 
	 */
	File selectedFile();

	/**
	 * Die ausgew&auml;hlten Dateien
	 * 
	 * @return die ausgew&auml;hlten Dateien
	 */
	List<File> selectedFiles();

	/**
	 * Eine Datei vorausw&auml;hlen</br>
	 * <p>&Auml;ndert der Anwender die Auswahl nicht, wird die
	 * vorausgew&auml;hlte Datei geliefert</p>
	 * <p>Setzt auch automatisch das vorausgew&auml;hlte 
	 * Verzeichnis (sollte der Pfad zur Datei nicht das aktuelle
	 * Verzeichnis sein)</p>
	 * 
	 * @param file ist die vorausgew&auml;hlte Datei
	 */
	void selectedFile(File file);
	
	
	/**
	 * Das vorausgew&auml;hlte Verzeichnis
	 * 
	 * @param directory das vorausgew&auml;hlte Verzeichnis
	 */
	void currentDirectory(File directory);
	
	/**
	 * Alle eventuell vorhandenen FileFilter entfernen</br>
	 * <p>Eine Utiltity-Funktion. Beim JFileChooser gibt es
	 * bei verschiedenen Betriebssystemen unterschiedliche
	 * Standardfilter. Die sind bereits aktiviert, obwohl 
	 * wir noch nichts gew&auml;hlt haben. Wir wollen aber
	 * &uuml;berall das gleiche Verhalten, n&auml;mlich 
	 * keine Filter</p>
	 */
	void removeAllFileFilters();
	
	int showOpenDialog(HvOptional<Component> parent); 
	
	int showSaveDialog(HvOptional<Component> parent);
}
