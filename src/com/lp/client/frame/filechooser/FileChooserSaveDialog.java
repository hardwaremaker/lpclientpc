package com.lp.client.frame.filechooser;

import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.filechooser.filter.FileExtension;
import com.lp.client.frame.filechooser.filter.HvTaggedFileExtensionFilter;
import com.lp.client.frame.filechooser.filter.HvTaggedUntypedStringFilter;
import com.lp.client.frame.filechooser.save.IDataSaver;
import com.lp.server.util.HvOptional;
import com.lp.util.Helper;

public class FileChooserSaveDialog<T> implements ChooserSaveDialogFilter<T> {

	private WrapperSaveFileChooser<T> wrapperFileChooser;
	private FileFilter preselectedFileFilter;
	
	public FileChooserSaveDialog(WrapperSaveFileChooser<T> wrapperFileChooser) {
		this.wrapperFileChooser = wrapperFileChooser;
		wrapperFileChooser.disableAllFileFilter();
	}

	protected WrapperSaveFileChooser<T> getWrapperFileChooser() {
		return wrapperFileChooser;
	}
	
	@Override
	public ChooserSaveDialog<T> dialogTitle(String title) {
		getWrapperFileChooser().setDialogTitle(title);
		return this;
	}

	@Override
	public ChooserSaveDialog<T> prompt(String prompt) {
		getWrapperFileChooser().setPrompt(prompt);
		return this;
	}
	
	@Override
	public ChooserSaveDialog<T> directory(File directory) {
		getWrapperFileChooser().setDirectory(directory);
		return this;
	}

	@Override
	public ChooserSaveDialog<T> selectedFile(File preselectedFilename) {
		if (preselectedFilename != null) {
			getWrapperFileChooser().setFilename(preselectedFilename);
		}
		return this;
	}

	@Override
	public ChooserSaveDialog<T> locale(Locale locale) {
		getWrapperFileChooser().setLocale(locale);
		return this;
	}

	@Override
	public ChooserSaveDialog<T> filename(String filename) {
		getWrapperFileChooser().setFilename(filename);
		return this;
	}

	@Override
	public ChooserSaveDialog<T> filename(File file) {
		if (file != null) {
			getWrapperFileChooser().setFilename(file);			
		}
		return this;
	}
	
//	public ChooserSaveDialog<T> addDataSaver(IDataSaver<T> saver, HvFileFilterBase hvFileFilter) {
//		getWrapperFileChooser().addChoosableFileFilter(hvFileFilter);
//		return this;
//	}
//
	@Override
	public <E extends FileFilter & IDataSaver<T>> ChooserSaveDialogFilter<T> addFileFilterSaver(E filter) {
		getWrapperFileChooser().addChoosableSaveFileFilter(filter);
		return this;
	}

	@Override
	public <E extends IDataSaver<T>> ChooserSaveDialogFilter<T> addFileSaver(E saver) {
		getWrapperFileChooser().filesaver(saver);
		return this;
	}
	
	@Override
	public FileChooserSaveDialog<T> preselected() {
		HvOptional<FileFilter> lastFilter = getWrapperFileChooser().getLastFileFilter();
		if(lastFilter.isPresent()) {
			preselectedFileFilter =  lastFilter.orElse(null);
		}
		return this;
	}

	@Override
	public FileChooserSaveDialog<T> addFilters(final FileFilter... filters) {
		if (filters != null) {
			for (FileFilter fileFilter : filters) {
				if (fileFilter != null) {
					getWrapperFileChooser().addChoosableFileFilter(fileFilter);
				}
			}
		}
	
		return this;
	}
	
	@Override
	public FileChooserSaveDialog<T> addFilters(final FileExtension... extensions) {
		if (extensions != null) {
			for (FileExtension extension : extensions) {
				getWrapperFileChooser().addChoosableFileFilter(
						new HvTaggedFileExtensionFilter(extension));
			}
		}
		return this;
	}
	
	@Override
	public FileChooserSaveDialog<T> addFilters(final String... extensions) {
		if (extensions != null) {
			for (String extension : extensions) {
				if (!Helper.isStringEmpty(extension)) {
					getWrapperFileChooser().addChoosableFileFilter(
							new HvTaggedUntypedStringFilter(extension));
				}
			}
		}
		return this;
	}
	
	@Override
	public WrapperSaveFileChooser<T> build() {
		// erst hier setzen wg. SP5883
		setPreselectedFileFilter(preselectedFileFilter);
		return getWrapperFileChooser();
	}
	
	private void setPreselectedFileFilter(FileFilter filter) {
		if (filter == null) {
			filter = getWrapperFileChooser().getFirstFileFilter().orElse(null);
			
			// SwingFileChooser "Fix": Ohne Filter gibt es ein leeres Datenformat
			if (filter == null) {
				getWrapperFileChooser().enableAllFileFilter();
			}
		}
		// TODO: ghp, die Frage ist, ob wir null (kein Filter) tatsaechlich setzen wollen?
		if (filter != null) {
			getWrapperFileChooser().setPreselectedFileFilter(filter);
		}
	}
}
