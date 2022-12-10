package com.lp.client.frame.filechooser;

import java.awt.Component;
import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.filechooser.filter.HvCsvFileFilter;
import com.lp.client.frame.filechooser.filter.HvDirectoryFilter;
import com.lp.client.frame.filechooser.filter.HvTaggedTxtFileFilter;
import com.lp.client.frame.filechooser.filter.HvTxtFileFilter;
import com.lp.client.frame.filechooser.filter.HvUntypedFileFilter;
import com.lp.client.frame.filechooser.filter.HvXlsFileFilter;
import com.lp.client.frame.filechooser.filter.HvXlsxFileFilter;
import com.lp.client.frame.filechooser.filter.HvXmlFileFilter;
import com.lp.client.frame.filechooser.open.CsvFile;
import com.lp.client.frame.filechooser.open.DirectoryFile;
import com.lp.client.frame.filechooser.open.TxtFile;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.frame.filechooser.open.XlsFile;
import com.lp.client.frame.filechooser.open.XlsxFile;
import com.lp.client.frame.filechooser.open.XmlFile;

public class FileChooserOpenDialog implements ChooserOpenDialog {

	private WrapperOpenFileChooser wrapperFileChooser;
	
	public FileChooserOpenDialog(WrapperOpenFileChooser wrapperFileChooser) {
		this.wrapperFileChooser = wrapperFileChooser;
	}

	public FileChooserOpenDialog(WrapperOpenFileChooser wrapperFileChooser, Component parent) {
		this.wrapperFileChooser = wrapperFileChooser;
		this.wrapperFileChooser.setParentComponent(parent);
	}

	protected WrapperOpenFileChooser getWrapperFileChooser() {
		return wrapperFileChooser;
	}
	
	@Override
	public ChooserOpenDialog dialogTitle(String title) {
		getWrapperFileChooser().setDialogTitle(title);
		return this;
	}

	@Override
	public ChooserOpenDialog directory(File directory) {
		// TODO check for directory, parent otherwise?
		if (directory != null) {
			getWrapperFileChooser().setDirectory(directory);
		}
		return this;
	}

	@Override
	public ChooserOpenDialog selectedFile(File preselectedFilename) {
		if (preselectedFilename != null) {
			getWrapperFileChooser().setFilename(preselectedFilename);
		}
		return this;
	}
	
	@Override
	public ChooserOpenDialog locale(Locale locale) {
		getWrapperFileChooser().setLocale(locale);
		return this;
	}
	
	@Override
	public ChooserOpenDialog prompt(String prompt) {
		getWrapperFileChooser().setPrompt(prompt);
		return this;
	}
	
	@Override
	public ChooserOpenDialogEnd<CsvFile> addCsvFilter() {
		setFileFilterImpl(new HvCsvFileFilter());
		return new WrapperFileChooserOpenDialogEnd<CsvFile>(CsvFile.class, getWrapperFileChooser());
	}

	@Override
	public ChooserOpenDialogEnd<XlsFile> addXlsFilter() {
		setFileFilterImpl(new HvXlsFileFilter());
		return new WrapperFileChooserOpenDialogEnd<XlsFile>(XlsFile.class, getWrapperFileChooser());
	}
	
	@Override
	public ChooserOpenDialogEnd<WrapperFile> addFilter(FileFilter filter) {
		setFileFilterImpl(filter);
		return new WrapperFileChooserOpenDialogEnd<WrapperFile>(WrapperFile.class, getWrapperFileChooser());
	}
	
	@Override
	public ChooserOpenDialogEnd<DirectoryFile> addDirectoryFilter() {
		setDirectoryFileFilterImpl(new HvDirectoryFilter());
		return new DirectoryFileChooserOpenDialogEnd(getWrapperFileChooser());
	}
	
	private void setFileFilterImpl(FileFilter filter) {
		getWrapperFileChooser().setFileSelectionMode(FileSelectionMode.FILES);
		setFilterImpl(filter);
	}
	
	private void setDirectoryFileFilterImpl(FileFilter filter) {
		getWrapperFileChooser().setFileSelectionMode(FileSelectionMode.DIRECTORIES);
		setFilterImpl(filter);
	}
	
	private void setFilterImpl(FileFilter filter) {
		getWrapperFileChooser().addChoosableFileFilter(filter);
		getWrapperFileChooser().setPreselectedFileFilter(filter);
	}
	
	@Override
	public ChooserOpenDialogEnd<XmlFile> addXmlFilter() {
		setFileFilterImpl(new HvXmlFileFilter());
		return new WrapperFileChooserOpenDialogEnd<XmlFile>(XmlFile.class, getWrapperFileChooser());
	}
	
	@Override
	public ChooserOpenDialogEnd<WrapperFile> addAllFileFilter() {
		return new WrapperFileChooserOpenDialogEnd<WrapperFile>(WrapperFile.class, getWrapperFileChooser());
	}
	
	@Override
	public ChooserOpenDialogEnd<XlsxFile> addXlsxFilter() {
		setFileFilterImpl(new HvXlsxFileFilter());
		return new WrapperFileChooserOpenDialogEnd<XlsxFile>(XlsxFile.class, getWrapperFileChooser());
	}

	@Override
	public ChooserOpenDialogEnd<TxtFile> addTxtFilter() {
//		setFileFilterImpl(new HvTxtFileFilter());
		setFileFilterImpl(new HvTaggedTxtFileFilter());
		return new WrapperFileChooserOpenDialogEnd<TxtFile>(TxtFile.class, getWrapperFileChooser());
	}
	
	@Override
	public ChooserOpenDialogEnd<WrapperFile> addFilters(final FileFilter... filters) {
		boolean first = true;
		for (FileFilter filter : filters) {
			if(first) {
				first = false;
				setFileFilterImpl(filter);
			} else {
				getWrapperFileChooser()
					.addChoosableFileFilter(filter);
			}
		}
		return new WrapperFileChooserOpenDialogEnd<WrapperFile>(WrapperFile.class, getWrapperFileChooser());
	}

	@Override
	public ChooserOpenDialogEnd<WrapperFile> addFilters(final String... extensions) {
		if (extensions == null) {
			return new WrapperFileChooserOpenDialogEnd<WrapperFile>(WrapperFile.class, getWrapperFileChooser());			
		}

		boolean first = true;
		for (String extension : extensions) {
			if (extension == null) continue;
	
			HvUntypedFileFilter filter = new HvUntypedFileFilter(extension);
			if (first) {
				first = false;
				setFileFilterImpl(filter);
			} else {
				getWrapperFileChooser().addChoosableFileFilter(filter);
			}
		}

		return new WrapperFileChooserOpenDialogEnd<WrapperFile>(
				WrapperFile.class, getWrapperFileChooser());			
	}
}
