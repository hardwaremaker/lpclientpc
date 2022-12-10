package com.lp.client.frame.filechooser;

import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.save.JRCsvSaveContributor;
import net.sf.jasperreports.view.save.JRMultipleSheetsXlsSaveContributor;
import net.sf.jasperreports.view.save.JROdtSaveContributor;
import net.sf.jasperreports.view.save.JRPrintSaveContributor;
import net.sf.jasperreports.view.save.JRSingleSheetXlsSaveContributor;

import com.lp.client.frame.filechooser.filter.HvJasperPrintFileFilter;
import com.lp.client.frame.filechooser.filter.HvJasperPrintPdfFileFilter;
import com.lp.client.frame.filechooser.save.IDataSaver;
import com.lp.server.util.HvOptional;

public class FileChooserSaveDialogJasper implements ChooserSaveDialogJasperFilter {

	private WrapperSaveFileChooser<JasperPrint> wrapperFileChooser;
	private Locale locale;
	private FileFilter preselectedFileFilter;
	
	public FileChooserSaveDialogJasper(WrapperSaveFileChooser<JasperPrint> wrapperFileChooser, Locale locale) {
		this.wrapperFileChooser = wrapperFileChooser;
		this.locale = locale;
		wrapperFileChooser.disableAllFileFilter();
	}
	
	protected WrapperSaveFileChooser<JasperPrint> getWrapperFileChooser() {
		return wrapperFileChooser;
	}
	
	@Override
	public ChooserSaveDialogJasperFilter addJasperPrintSaver() {
		HvJasperPrintFileFilter jasperFileFilter = new HvJasperPrintFileFilter(new JRPrintSaveContributor(locale, null));
		getWrapperFileChooser().addChoosableSaveFileFilter(jasperFileFilter);
		return this;
	}

	@Override
	public ChooserSaveDialogJasperFilter addJasperPdfSaver() {
		HvJasperPrintFileFilter jasperFileFilter = new HvJasperPrintPdfFileFilter(locale);
		getWrapperFileChooser().addChoosableSaveFileFilter(jasperFileFilter);
		return this;
	}

	@Override
	public ChooserSaveDialogJasperFilter addJasperCsvSaver() {
		HvJasperPrintFileFilter jasperFileFilter = new HvJasperPrintFileFilter(new JRCsvSaveContributor(locale, null));
		getWrapperFileChooser().addChoosableSaveFileFilter(jasperFileFilter);
		return this;
	}
	
	@Override
	public ChooserSaveDialogJasperFilter addJasperSingleSheetXlsSaver() {
		HvJasperPrintFileFilter jasperFileFilter = new HvJasperPrintFileFilter(new JRSingleSheetXlsSaveContributor(locale, null));
		getWrapperFileChooser().addChoosableSaveFileFilter(jasperFileFilter);
		return this;
	}

	@Override
	public ChooserSaveDialogJasperFilter addJasperMultipleSheetXlsSaver() {
		HvJasperPrintFileFilter jasperFileFilter = new HvJasperPrintFileFilter(new JRMultipleSheetsXlsSaveContributor(locale, null));
		getWrapperFileChooser().addChoosableSaveFileFilter(jasperFileFilter);
		return this;
	}
	
	@Override
	public ChooserSaveDialogJasperFilter addJasperOdtSaver() {
		HvJasperPrintFileFilter jasperFileFilter = new HvJasperPrintFileFilter(new JROdtSaveContributor(locale, null));
		getWrapperFileChooser().addChoosableSaveFileFilter(jasperFileFilter);
		return this;
	}

	@Override
	public ChooserSaveDialogJasper filename(String filename) {
		getWrapperFileChooser().setFilename(filename);
		return this;
	}

	@Override
	public ChooserSaveDialogJasper filename(File file) {
		getWrapperFileChooser().setFilename(file);
		return this;
	}

	@Override
	public ChooserSaveDialogJasper dialogTitle(String title) {
		getWrapperFileChooser().setDialogTitle(title);
		return this;
	}

	@Override
	public ChooserSaveDialogJasper directory(File directory) {
		getWrapperFileChooser().setDirectory(directory);
		return this;
	}

	@Override
	public ChooserSaveDialogJasper locale(Locale locale) {
		this.locale = locale;
		getWrapperFileChooser().setLocale(locale);
		return this;
	}

	@Override
	public ChooserSaveDialogJasper preselected() {
		HvOptional<FileFilter> lastFilter = getWrapperFileChooser().getLastFileFilter();
		if(lastFilter.isPresent()) {
			preselectedFileFilter = lastFilter.orElse(null);
		}
		return this;
	}

	@Override
	public WrapperSaveFileChooser<JasperPrint> build() {
		// erst hier setzen wg. SP5883
		setPreselectedFileFilter(preselectedFileFilter);
		return getWrapperFileChooser();
	}
	
	private void setPreselectedFileFilter(FileFilter filter) {
		if (filter == null) {
			filter = getWrapperFileChooser().getFirstFileFilter().orElse(null);
		}
		getWrapperFileChooser().setPreselectedFileFilter(filter);
	}

	@Override
	public <E extends FileFilter & IDataSaver<JasperPrint>> ChooserSaveDialogJasperFilter addFileFilterSaver(E filter) {
		getWrapperFileChooser().addChoosableSaveFileFilter(filter);
		return this;
	}
}
