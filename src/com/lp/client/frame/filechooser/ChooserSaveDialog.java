package com.lp.client.frame.filechooser;

import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.filechooser.filter.FileExtension;
import com.lp.client.frame.filechooser.save.IDataSaver;

public interface ChooserSaveDialog<T> extends ChooserDialog<ChooserSaveDialog<T>> {
	
	ChooserSaveDialog<T> dialogTitle(String title);
	
	ChooserSaveDialog<T> directory(File directory);
	
	ChooserSaveDialog<T> locale(Locale locale);

	ChooserSaveDialog<T> filename(String filename);
	
	ChooserSaveDialog<T> filename(File file);

	/**
	 * Dieser Dialog soll selbst speichern, daher wird ein FileFilter
	 * verwendet, der auch Speichern kann (FileFilterSaver).</br>
	 * @param filter muss IDataSaver implementieren
	 * @return
	 */
	<E extends FileFilter & IDataSaver<T>> ChooserSaveDialogFilter<T> addFileFilterSaver(E filter);
	
	/**
	 * Dieser Dialog soll selbst speichern, aber es gibt keine 
	 * FileFilter, d.h. er soll einfach genau so speichern, wie hier
	 * angegeben</br>
	 * 
	 * @param saver muss IDataSaver implementieren
	 * @return
	 */
	<E extends IDataSaver<T>> ChooserSaveDialogFilter<T> addFileSaver(E saver);

	/**
	 * Der Dialog soll vom Anwender den Zieldateinamen erfahren.</br>
	 * <p>Dabei soll der Dialog sicherstellen, dass der Anwender nur
	 * jene Dateierweiterungen ausw&auml;hlen kann, die programmiert sind.</p>
	 * 
	 * <p>Eine beliebige Anzahl von Dateifiltern hinzuf&uuml;gen</br>
	 * <p>Jeder Filter landet als Auswahlm&ouml;glichkeit zum
	 * Umschalten der verschiedenen Dateitypen</p>
	 * <p>Der erste angegebene Filter ist der "Default" in der
	 * Filterauswahl, er ist vorausgew&auml;hlt</p>
	 * @param filters die Liste der m&ouml;glichen Filter
	 * @return
	 */
	ChooserSaveDialog<T> addFilters(final FileFilter... filters);

	ChooserSaveDialog<T> addFilters(final String... extensions);

	ChooserSaveDialog<T> addFilters(final FileExtension... extensions);
	
	WrapperSaveFileChooser<T> build();

}
