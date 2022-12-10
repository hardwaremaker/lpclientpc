package com.lp.client.frame.filechooser;

import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.filechooser.save.IDataSaver;

import net.sf.jasperreports.engine.JasperPrint;


public interface ChooserSaveDialogJasper {

	ChooserSaveDialogJasperFilter addJasperPrintSaver();
	
	ChooserSaveDialogJasperFilter addJasperPdfSaver();
	
	ChooserSaveDialogJasperFilter addJasperCsvSaver();
	
	ChooserSaveDialogJasper filename(String filename);
	
	ChooserSaveDialogJasper filename(File file);

	ChooserSaveDialogJasper dialogTitle(String title);
	
	ChooserSaveDialogJasper directory(File directory);
	
	ChooserSaveDialogJasper locale(Locale locale);

	<E extends FileFilter & IDataSaver<JasperPrint>> ChooserSaveDialogJasperFilter addFileFilterSaver(E filter);

	ChooserSaveDialogJasperFilter addJasperSingleSheetXlsSaver();

	ChooserSaveDialogJasperFilter addJasperMultipleSheetXlsSaver();

	ChooserSaveDialogJasperFilter addJasperOdtSaver();
}
