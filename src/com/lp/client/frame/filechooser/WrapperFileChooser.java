package com.lp.client.frame.filechooser;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.component.frameposition.ClientPerspectiveManager;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.pc.SystemProperties;
import com.lp.client.util.feature.HvFeatures;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.util.HvOptional;
import com.lp.util.Helper;

public abstract class WrapperFileChooser {
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

//	private JFileChooser fileChooser;
	private final IFileChooser fileChooser;
	private Component parent;

	private ICanceled canceledHandler;
	private ICanceledOnError canceledOnErrorHandler;
	private IApproveHandler approvedHandler;

	private FileChooserConfig config;
	private Path defaultPath;

	private ISingleFileFailureHandler singleFailureHandler;
	private IMultipleFileFailureHandler multiselectFailureHandler;

	public WrapperFileChooser() {
		this(null);
	}

	public WrapperFileChooser(String token) {
		setToken(token);
		
		boolean useOSDialog = useOSXDialog();
		if(useOSDialog) {
			fileChooser = new DeferredNativeFileDialogChooser();		
		} else {
			fileChooser = new SwingFileChooser();
		}
//		fileChooser = new JFileChooser();
//		removeAllFileFilters(fileChooser);
		getFileChooser().removeAllFileFilters();
		singleFailureHandler = new DefaultSingleFileFailureHandler();
		multiselectFailureHandler = new DefaultMultiSelectFileFailureHandler();
	}

	private boolean useOSXDialog() {
		return SystemProperties.isMacOs() && HvFeatures.OSXFileDialog.isActive();
	}
	

	private Path getDefaultPath() {
		if (defaultPath == null) {
			defaultPath = new Path();
		}
		return defaultPath;
	}

	protected void setToken(String token) {
		FileChooserConfigList configList = ClientPerspectiveManager.getInstance().readFileChooserConfig();
		config = configList.findByToken(token);
		if (config == null) {
			config = new FileChooserConfig(token);
		}
	}

	private void initConfigProperties() {
		if (config == null)
			return;

		if (/** isFilenameNotSet() && **/
		config.getPath() != null) {
			getDefaultPath().processConfigPath(config.getPath());
//			File savedPath = new File(config.getPath());
//			setFilename(savedPath);

//			if (savedPath.isFile()) {
//				setFilename(savedPath);
//			}
//			File savedDir = savedPath.isDirectory() ? savedPath : savedPath.getParent();
		}

		if (config.getFileFilter() != null) {
			List<FileFilter> choosableFilters = getFileChooser().choosableFileFilters();
			for (FileFilter filter : choosableFilters) {
				if (filter.getDescription().equals(config.getFileFilter())) {
					setPreselectedFileFilter(filter);
					break;
				}
			}
		}
	}

	private void saveConfigProperties() {
		if (config == null || Helper.isStringEmpty(config.getToken()))
			return;

		config.setPath(getSelectedFile().getAbsolutePath());
		FileFilter selectedFilter = getFileChooser().fileFilter();
		config.setFileFilter(selectedFilter != null ? selectedFilter.getDescription() : null);
		ClientPerspectiveManager.getInstance().saveFileChooserConfig(config);
	}

	protected IFileChooser getFileChooser() {
		return fileChooser;
	}
/*	
	protected JFileChooser getFileChooser() {
		return fileChooser;
	}
*/
	protected void setPreselectedFileFilter(FileFilter filter) {
		getFileChooser().fileFilter(filter);
	}

	protected void enableAllFileFilter() {
		getFileChooser().enableAllFileFilter();
	}

	protected void disableAllFileFilter() {
		getFileChooser().disableAllFileFilter();
	}

	protected boolean isAllFileFilterUsed() {
		return getFileChooser().isAllFileFilterUsed();
	}
	
	protected void setFileSelectionMode(FileSelectionMode fileSelectionMode) {
		getFileChooser().selectionMode(fileSelectionMode);
	}

	protected FileSelectionMode getFileSelectionMode() {
		return getFileChooser().selectionMode();
	}
	
	protected void enableMultiselection() {
		getFileChooser().beMultiSelect();
	}

	protected void disableMultiselection() {
		getFileChooser().beSingleSelect();
	}

	protected void setDirectory(File directory) {
		getDefaultPath()
				.processDirectory(directory.isDirectory() ? directory.getAbsolutePath() : directory.getParent());
//		getFileChooser().setCurrentDirectory(directory);
	}

	protected void setFilename(String filename) {
		getDefaultPath().processFilename(filename);
		getFileChooser().selectedFile(new File(filename));
//		isFilenameSet = Boolean.TRUE;
	}

	protected void setFilename(File file) {
		setFilename(file.getName());
		setDirectory(file);
		getFileChooser().selectedFile(file);
//		isFilenameSet = Boolean.TRUE;
	}

	protected void setLocale(Locale locale) {
		getFileChooser().locale(locale);
		// getFileChooser().updateUI();
	}

	protected void setDialogTitle(String title) {
		getFileChooser().title(title);
	}

	protected void setParentComponent(Component parent) {
		this.parent = parent;
	}

	protected Component getParentComponent() {
		return parent;
	}

	protected void setPrompt(String promptButtonText) {
		getFileChooser().prompt(promptButtonText);
	}
	
	/**
	 * 
	 * @param option
	 * @return true wenn der FileChooser erneut gestartet werden sollte.
	 */
	private boolean handleOption(int option) {
		if (JFileChooser.APPROVE_OPTION == option) {
			boolean accept = getApprovedHandler().approved();
			if (accept) {
				saveConfigProperties();
			}
			return !accept;
		} else if (JFileChooser.CANCEL_OPTION == option) {
			getCanceledHandler().canceled();
		} else {
			getCanceledOnErrorHandler().canceledOnError();
		}
		return false;
	}

	private void doChoose() {
		boolean select = true;
		while (select) {
			select = handleOption(showDialog());
		}
	}

	protected void chooseImpl(Function<File, SelectResult> approvedSingle) {
		disableMultiselection();
		setSingleSelectionHandler(approvedSingle);
		doChoose();
	}

	public void choose(Function<File, SelectResult> approvedSingle) {
		chooseImpl(approvedSingle);
	}

	public void choose(Function<File, SelectResult> approvedSingle, ICanceled canceled) {
		setCanceledHandler(canceled);
		chooseImpl(approvedSingle);
	}

	public void chooseMultiple(Function<List<File>, SelectResult> approvedMultiple) {
		enableMultiselection();
		setMultiSelectionHandler(approvedMultiple);
		doChoose();
	}

	public void chooseMultiple(Function<List<File>, SelectResult> approvedMultiple, ICanceled canceled) {
		setCanceledHandler(canceled);
		chooseMultiple(approvedMultiple);
	}

	protected int choose() {
		return showDialog();
	}

	protected int showDialog() {
		initConfigProperties();
		getDefaultPath().passToFileChooser();
		return showDialogImpl();
	}

	protected abstract int showDialogImpl();

	private ICanceled getCanceledHandler() {
		if (canceledHandler == null) {
			canceledHandler = new CanceledHandler();
		}
		return canceledHandler;
	}

	private void setCanceledHandler(ICanceled canceledHandler) {
		this.canceledHandler = new CanceledHandler(canceledHandler);
	}

	private ICanceledOnError getCanceledOnErrorHandler() {
		if (canceledOnErrorHandler == null) {
			canceledOnErrorHandler = new CanceledHandler();
		}
		return canceledOnErrorHandler;
	}

	protected IApproveHandler getApprovedHandler() {
		return approvedHandler;
	}

	/**
	 * Setzt den FailureHandler f&uuml;r Auswahl einzelner Dateien. Falls handler
	 * <code> null </code> ist, wird der defaultHandler verwendet
	 */
	public void setFailureHandler(ISingleFileFailureHandler handler) {
		if (handler != null) {
			this.singleFailureHandler = handler;
		} else {
			this.singleFailureHandler = new DefaultSingleFileFailureHandler();
		}
	}

	/**
	 * Setzt den FailureHandler f&uuml;r Auswahl mehrerer Dateien. Falls handler
	 * <code> null </code> ist, wird der defaultHandler verwendet
	 */
	public void setFailureHandler(IMultipleFileFailureHandler handler) {
		if (handler != null) {
			this.multiselectFailureHandler = handler;
		} else {
			this.multiselectFailureHandler = new DefaultMultiSelectFileFailureHandler();
		}
	}

	private void setSingleSelectionHandler(final Function<File, SelectResult> approvedSingle) {
		this.approvedHandler = new SingleSelectionApprovedHandler(approvedSingle);
	}

	private void setMultiSelectionHandler(final Function<List<File>, SelectResult> approvedMultiple) {
		this.approvedHandler = new MultiSelectionApprovedHandler(approvedMultiple);
	}

	/**
	 * Filtert die existierenden Dateien aus allen Dateien heraus und gibt diese in
	 * einer neuen Liste zur&uuml;ck. <br>
	 * 
	 * @param files
	 * @return List<File> existierendeFiles oder leere Liste falls keine existieren
	 */
	protected List<File> filtereExistierendeDateien(Iterable<File> files) {
		Iterator<File> iterator = files.iterator();
		List<File> existingFiles = new ArrayList<File>();
		while (iterator.hasNext()) {
			File file = iterator.next();
			// pruefen ob File existiert
			if (file != null && file.exists()) {
				existingFiles.add(file);
			}
		}
		return existingFiles;
	}

	protected File getSelectedFile() {
		return getFileChooser().selectedFile();
	}

	protected List<File> getSelectedFiles() {
		return getFileChooser().selectedFiles();
	}

	protected HvOptional<FileFilter> getLastFileFilter() {
		List<FileFilter> filters = getFileChooser().choosableFileFilters();
		if(filters.size() < 1) return HvOptional.empty();
		
		return HvOptional.of(filters.get(filters.size() -1));
	}

	protected HvOptional<FileFilter> getFirstFileFilter() {
		List<FileFilter> filters = getFileChooser().choosableFileFilters();
		if(filters.size() < 1) return HvOptional.empty();
		return HvOptional.of(filters.get(0));
	}

	protected List<FileFilter> getFileFilters() {
		return getFileChooser().choosableFileFilters();
	}
	
	protected static List<File> getInvalidFiles(List<File> allFiles, List<File> validFiles) {
		List<File> invalidFiles = new ArrayList<File>(allFiles.size() - validFiles.size());
		for (File f : allFiles) {
			if (!(validFiles.contains(f))) {
				invalidFiles.add(f);
			}
		}
		return invalidFiles;
	}

	protected class CanceledHandler implements ICanceled, ICanceledOnError {
		private ICanceled externCanceledHandler;

		public CanceledHandler() {
		}

		public CanceledHandler(ICanceled canceled) {
			this.externCanceledHandler = canceled;
		}

		public void canceled() {
			if (externCanceledHandler != null)
				externCanceledHandler.canceled();
		};

		public void canceledOnError() {
			myLogger.error("Unbekannter Fehler im Dateiauswahldialog");
			canceled();
		};
	}

	public static enum SelectResult {
		ACCEPT, CANCEL, RETRY
	}

	protected interface IApproveHandler {
		/**
		 * Return true to end the selection, false to try again
		 * 
		 * @return
		 */
		boolean approved();
	}

	private final class SingleSelectionApprovedHandler implements IApproveHandler {
		private final Function<File, SelectResult> acceptFun;

		private SingleSelectionApprovedHandler(Function<File, SelectResult> approvedSingle) {
			this.acceptFun = approvedSingle;
		}

		@Override
		public boolean approved() {
			File file = getFileChooser().selectedFile();
			SelectResult result = acceptFun.apply(file);
			return handleSelectResult(result);
		}
	}

	private final class MultiSelectionApprovedHandler implements IApproveHandler {
		private final Function<List<File>, SelectResult> acceptFun;

		private MultiSelectionApprovedHandler(Function<List<File>, SelectResult> approvedMultiple) {
			this.acceptFun = approvedMultiple;
		}

		@Override
		public boolean approved() {
			List<File> selectedFiles = getFileChooser().selectedFiles();
			SelectResult result = acceptFun.apply(selectedFiles);
			return handleSelectResult(result);

		}
	}

	private boolean handleSelectResult(SelectResult result) {
		switch (result) {
		case ACCEPT:
			return true;
		case CANCEL:
			getCanceledHandler().canceled();
			return true;
		case RETRY:
			return false;
		}

		return false;
	}

	protected class SingleFileHandlerExisting implements Function<File, SelectResult> {

		private final Function<File, SelectResult> child;

		public SingleFileHandlerExisting(Function<File, SelectResult> child) {
			this.child = child;
		}

		@Override
		public SelectResult apply(File t) {
			if (t.exists()) {
				return child.apply(t);
			} else {
				boolean doCancel = singleFailureHandler.cancelOnFileNotFound(t);
				return doCancel ? SelectResult.CANCEL : SelectResult.RETRY;
			}
		}
	}

	protected class MultiFileHandlerExisting implements Function<List<File>, SelectResult> {

		private final Function<List<File>, SelectResult> child;

		public MultiFileHandlerExisting(Function<List<File>, SelectResult> child) {
			this.child = child;
		}

		@Override
		public SelectResult apply(List<File> selectedFiles) {
			List<File> files = filtereExistierendeDateien(selectedFiles);
			files = Collections.unmodifiableList(files);
			boolean allExist = files.size() == selectedFiles.size();
			boolean accept = allExist;
			if (!allExist) {
				List<File> invalidFiles = getInvalidFiles(selectedFiles, files);
				accept = multiselectFailureHandler.handleFilesNotFound(invalidFiles, files);
			}
			if (accept) {
				return child.apply(files);
			} else {
				return SelectResult.RETRY;
			}
		}
	}

	/**
	 * Create a new SingleFileHandler that will only call the given method when the
	 * file really exists
	 * 
	 * @param existingFileHandler
	 * @return
	 */
	public Function<File, SelectResult> createSingleFileHandlerExisiting(
			Function<File, SelectResult> existingFileHandler) {
		return new SingleFileHandlerExisting(existingFileHandler);
	}

	public Function<List<File>, SelectResult> createMultiFileHandlerExisting(
			Function<List<File>, SelectResult> existingFileHandler) {
		return new MultiFileHandlerExisting(existingFileHandler);
	}

	private class DefaultSingleFileFailureHandler implements ISingleFileFailureHandler {
		@Override
		public boolean cancelOnFileNotFound(File notFound) {
			return !(DialogFactory.showModalJaNeinDialog(getParentComponent(),
					LPMain.getMessageTextRespectUISPr("lp.datei.open.nichtvorhanden", notFound.getAbsolutePath()),
					LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.INFORMATION_MESSAGE, JOptionPane.NO_OPTION));
		}
	}

	private class DefaultMultiSelectFileFailureHandler implements IMultipleFileFailureHandler {
		@Override
		public boolean handleFilesNotFound(List<File> notExistingFiles, List<File> existingFiles) {
			return DialogFactory.showModalJaNeinDialog(getParentComponent(),
					LPMain.getMessageTextRespectUISPr("lp.datei.open.nichtvorhanden.liste", notExistingFiles,
							existingFiles),
					LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.INFORMATION_MESSAGE, JOptionPane.NO_OPTION);
		}
	}

	/**
	 * Falls die angegebene Datei bereits existiert,
	 * wird der Anwender gefragt, ob er die Datei
	 * &uuml;berschreiben will.</br>
	 * <p>Mit dem Swing FileChooser m&uuml;ssen wir
	 * selbst fragen.</p>
	 * <p> Beim OSX NSSavePanel erkennt das
	 * Panel selbst, ob die gew&uuml;nschte Datei bereits existiert
	 * und fragt den Anwender ob er sie &uuml;berschreiben will.
	 * Nur dann wurde NSSavePanel beendet. D.h. wir haben explizit
	 * die Best&auml;tigung, dass wir &uuml;berschreiben d&uuml;rfen.</p>
	 * 
	 * @param f die m&ouml;glicherweise zu &uuml;berschreibende Datei
	 * @return true wenn die Datei &uuml;berschrieben werden darf
	 */
	public boolean shouldOverwriteFile(File f) {
		if (!f.exists()) return true;
		if (useOSXDialog()) return true;

		int option = JOptionPane.showConfirmDialog(getParentComponent(), 
				LPMain.getMessageTextRespectUISPr(
						"lp.datei.existiertueberschreiben", 
						f.getAbsolutePath()), 
				LPMain.getTextRespectUISPr("lp.datei.speichern.title"),
				JOptionPane.OK_CANCEL_OPTION);
		return JOptionPane.OK_OPTION == option;
	}
	
	private class Path {
		private String filename;
		private String directory;

		protected void processFilename(String filename) {
			this.filename = filename;
		}

		protected void processDirectory(String directory) {
			this.directory = directory;
		}

		protected void processConfigPath(String configPath) {
			File configPathFile = new File(configPath);
			if (filename == null) {
				filename = configPathFile.getName();
			}

			if (directory == null) {
				if (configPathFile.isDirectory()) {
					directory = configPathFile.getAbsolutePath();
				} else {
					File parent = configPathFile.getParentFile();
					if (parent != null && parent.isDirectory()) {
						directory = parent.getAbsolutePath();
					}
				}
			}
		}

		protected void passToFileChooser() {
			if (filename != null) {
				getFileChooser().selectedFile(new File(filename));
			}
			
			if (directory != null) {
				getFileChooser().currentDirectory(new File(directory));
			}
		}
	}
}
