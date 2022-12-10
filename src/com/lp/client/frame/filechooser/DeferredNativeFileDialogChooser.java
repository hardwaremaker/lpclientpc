package com.lp.client.frame.filechooser;

import java.awt.Component;
import java.awt.FileDialog;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import com.lp.client.util.logger.LpLogger;
import com.lp.server.util.HvOptional;
import com.lp.server.util.Validator;

/**
 * <p>Sinn und Zweck ist, dass die ganzen Properties gesammelt werden
 * und erst beim show/saveOpenDialog verwendet werden um den
 * plattformspezifischen FileDialog zu starten.</p>
 * 
 * <p>Bei der OSX Variante beispielsweise muss man beim Instanzieren
 * der UI Komponente angeben, f&uuml;r welchen Zweck man sie 
 * ben&ouml;tigt. NSOpenPanel (laden) vs. NSSavePanel (speichern).
 * Das kann im Nachhinein nicht mehr ge&auml;ndert werden.</p>
 * 
 * @author ghp
 */
public class DeferredNativeFileDialogChooser implements IFileChooser {
	private final static LpLogger log = 
			(LpLogger) LpLogger.getInstance(DeferredNativeFileDialogChooser.class);

	private FileSelectionMode selectionMode = FileSelectionMode.FILES;
	private boolean multiSelect = false;
	private String title = "";
	private HvOptional<FileFilter> fileFilter = HvOptional.empty();
	private List<FileFilter> fileFilters = new ArrayList<FileFilter>();
	private HvOptional<File> currentDir = HvOptional.empty();
	private int dialogType = FileDialog.LOAD;
	
	private File[] outFiles = new File[1];
	private HvOptional<File> inFile = HvOptional.empty();
	
	private HvOptional<String> promptButton = HvOptional.empty();
	
	private boolean allFileFilter = false;
	
	public DeferredNativeFileDialogChooser() {
	}
	
	@Override
	public void selectionMode(FileSelectionMode fileSelectionMode) {
		selectionMode = fileSelectionMode;
	}

	@Override
	public FileSelectionMode selectionMode() {
		return selectionMode;
	}
	
	@Override
	public void beSingleSelect() {
		multiSelect = false;
	}

	@Override
	public void beMultiSelect() {
		multiSelect = true;
	}

	@Override
	public void enableMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	@Override
	public void title(String title) {
		Validator.notNull(title, "title");
		this.title = title;
	}

	@Override
	public void prompt(String buttonText) {
		Validator.notNull(buttonText, "buttonText");
		this.promptButton = HvOptional.of(buttonText);
	}
	
	@Override
	public void locale(Locale locale) {
		Validator.notNull(locale, "locale");
		log.warn("setting the locale to '" + locale.toString() + " is ignored");
	}

	@Override
	public void fileFilter(FileFilter newFilter) {
		fileFilter = HvOptional.ofNullable(newFilter);
	}

	@Override
	public FileFilter fileFilter() {
		return fileFilter.orElse(null);
	}

	@Override
	public void enableAllFileFilter() {
		fileFilters.clear();
		allFileFilter = true;
	}

	@Override
	public void disableAllFileFilter() {
		allFileFilter = false;
	}

	@Override
	public boolean isAllFileFilterUsed() {
		return allFileFilter;
	}
	
	@Override
	public List<FileFilter> choosableFileFilters() {
		return fileFilters;
	}

	@Override
	public void addChoosableFileFilter(FileFilter filter) {
		Validator.notNull(filter, "filter");
		fileFilters.add(filter);
	}
	
	@Override
	public File selectedFile() {
		return outFiles[0];
	}

	@Override
	public List<File> selectedFiles() {
		if(outFiles == null || outFiles.length == 0 || outFiles[0] == null) {
			return new ArrayList<File>();
		}
		return Arrays.asList(outFiles);
	}

	@Override
	public void selectedFile(File file) {
		inFile = HvOptional.ofNullable(file);
	}

	@Override
	public void currentDirectory(File directory) {
		Validator.notNull(directory, "directory");
		currentDir = HvOptional.of(directory);
	}

	@Override
	public void removeAllFileFilters() {
		fileFilters.clear();
	}

	@Override
	public int showOpenDialog(HvOptional<Component> parent) {
		dialogType = FileDialog.LOAD;
		Worker w = new Worker();
		w.execute();
		try {
			File f[] = w.get();
			return (f == null || f[0] == null) 
					? JFileChooser.CANCEL_OPTION 
					: JFileChooser.APPROVE_OPTION;
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
		return JFileChooser.APPROVE_OPTION;
	}

	@Override
	public int showSaveDialog(HvOptional<Component> parent) {
		dialogType = FileDialog.SAVE;
		Worker w = new Worker();
		w.execute();
		try {
			File f[] = w.get();
			return (f == null || f[0] == null) 
					? JFileChooser.CANCEL_OPTION 
					: JFileChooser.APPROVE_OPTION;
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
		return JFileChooser.APPROVE_OPTION;
	}

	private class Worker extends SwingWorker<File[], Object> {	
		@Override
		protected File[] doInBackground() throws Exception {
			OSXNativeFileDialog dlg = new OSXNativeFileDialog(title, dialogType);
			if(dialogType == FileDialog.LOAD) {
				if (selectionMode.equals(FileSelectionMode.DIRECTORIES)) {
					dlg.setCanChooseDirectories(true);
					dlg.setCanChooseFiles(false);
				}
				if (selectionMode.equals(FileSelectionMode.FILES)) {
					dlg.setCanChooseDirectories(false);
					dlg.setCanChooseFiles(true);					
				}
				if (selectionMode.equals(FileSelectionMode.FILES_DIRECTORIES)) {
					dlg.setCanChooseDirectories(true);
					dlg.setCanChooseFiles(true);
				}
/*				
				dlg.setCanChooseDirectories(
						selectionMode.equals(FileSelectionMode.DIRECTORIES) ||
						selectionMode.equals(FileSelectionMode.FILES_DIRECTORIES));
				dlg.setCanChooseFiles(
						selectionMode.equals(FileSelectionMode.FILES) ||
						selectionMode.equals(FileSelectionMode.FILES_DIRECTORIES));
*/				
				// TODO beta api allowedContentTypes fuer NSSavePanel
				// addChooseableFilter ist nur fuer NSOpenPanel
				for (FileFilter fileFilter : fileFilters) {
					dlg.addChoosableFilter(fileFilter);
					dlg.addChoosableFilter(
							"A-" + fileFilter.getDescription(), fileFilter.getDescription());
				}
			}
			
			if (dialogType == FileDialog.SAVE) {
				for (FileFilter fileFilter : fileFilters) {
					dlg.addChoosableFilter(fileFilter);
					dlg.addChoosableFilter(
							"A-" + fileFilter.getDescription(), fileFilter.getDescription());
				}				
			}
			
			if (promptButton.isPresent()) {
				dlg.setPrompt(promptButton.get());
			}
			
			if(inFile.isPresent()) {
				dlg.setNameFieldStringValue(inFile.get().getName());
			}

			if(currentDir.isPresent()) {
				dlg.setDirectory(currentDir.get().getCanonicalPath());
			}				

			if(dialogType == FileDialog.LOAD) {
				dlg.setMultipleMode(multiSelect);				
			}
			
			dlg.setCanCreateDirectories(true);
//			dlg.setVisible(true);
			
			int rc = dlg.runModal();
			if(rc == 1) {
				if(multiSelect) {
					outFiles = dlg.getFiles();
				} else {
					outFiles[0]= new File(dlg.getFile());
				}				
			} else {
				outFiles[0] = null;
			}
			
			int selectedFilterIndex = dlg.selectedFilterIndex();
			if (selectedFilterIndex >= 0 && selectedFilterIndex < fileFilters.size()) {
				fileFilter = HvOptional.of(fileFilters.get(selectedFilterIndex));
			} else {
				fileFilter = HvOptional.empty();
			}

			return outFiles;
		}
	}
}
