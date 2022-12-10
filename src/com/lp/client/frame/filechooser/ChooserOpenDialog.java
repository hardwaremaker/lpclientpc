package com.lp.client.frame.filechooser;

import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.filechooser.open.CsvFile;
import com.lp.client.frame.filechooser.open.DirectoryFile;
import com.lp.client.frame.filechooser.open.TxtFile;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.frame.filechooser.open.XlsFile;
import com.lp.client.frame.filechooser.open.XlsxFile;
import com.lp.client.frame.filechooser.open.XmlFile;


public interface ChooserOpenDialog extends ChooserDialog<ChooserOpenDialog> {
	
	ChooserOpenDialogEnd<CsvFile> addCsvFilter();
	
	ChooserOpenDialogEnd<XlsFile> addXlsFilter();
	ChooserOpenDialogEnd<XlsxFile> addXlsxFilter();
	
	ChooserOpenDialogEnd<WrapperFile> addFilter(FileFilter filter);

	ChooserOpenDialogEnd<DirectoryFile> addDirectoryFilter();

	ChooserOpenDialogEnd<XmlFile> addXmlFilter();

	ChooserOpenDialogEnd<WrapperFile> addAllFileFilter();
	
	ChooserOpenDialogEnd<TxtFile> addTxtFilter();
	
	/**
	 * Eine beliebige Anzahl von Dateifiltern hinzuf&uuml;gen</br>
	 * <p>Jeder Filter landet als Auswahlm&ouml;glichkeit zum
	 * Umschalten der verschiedenen Dateitypen</p>
	 * <p>Der erste angegebene Filter ist der "Default" in der
	 * Filterauswahl, er ist vorausgew&auml;hlt</p>
	 * @param filters die Liste der m&ouml;glichen Filter
	 * @return
	 */
	ChooserOpenDialogEnd<WrapperFile> addFilters(final FileFilter... filters);

	ChooserOpenDialogEnd<WrapperFile> addFilters(final String... extensions);
}
