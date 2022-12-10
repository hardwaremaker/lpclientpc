package com.lp.client.frame.filechooser;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.open.IWrapperFile;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;

public class WrapperFileChooserOpenDialogEnd<T extends IWrapperFile> extends FileChooserOpenDialogEnd<T> {
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());
	private Class<T> fileClass;

	private enum SelectResult {
		ACCEPT, CANCEL, RETRY
	}
	
	
	public WrapperFileChooserOpenDialogEnd(Class<T> fileClass, WrapperFileChooser fileChooser) {
		super(fileChooser);
		this.fileClass = fileClass;
	}

	@Override
	public T openSingle() {
		for(;;) {
			File f = chooseFile();
			if (f == null) return createWrapperFile(f);
	
			SelectResult result = verify(f);
			if (SelectResult.RETRY.equals(result)) {
				continue;
			}
			
			return createWrapperFile(
					SelectResult.ACCEPT.equals(result) ? f : null);
		}
	}

	@Override
	public T openSingleUnchecked() {
		for(;;) {
			File f = chooseFile();
			if (f == null) return createWrapperFile(f);
	
			SelectResult result = verifyFilemode(f);
			if (SelectResult.RETRY.equals(result)) {
				continue;
			}
			
			return createWrapperFile(
					SelectResult.ACCEPT.equals(result) ? f : null);
		}
	}
	

	/**
	 * Returns true if the given File f could be verified.
	 * @param f
	 * @return true if verification is successfull
	 */
	protected SelectResult verify(File f) {
		SelectResult result = verifyFilemode(f);
		if (SelectResult.ACCEPT.equals(result)) {
			result = verifyFileExtension(f);
		}
		return result;
	}

	protected SelectResult verifyFilemode(File f) {
		FileSelectionMode mode = build().getFileSelectionMode();
		if (FileSelectionMode.DIRECTORIES.equals(mode)) {
			return f.isDirectory() ? SelectResult.ACCEPT : 
				(askRetryFilemode(f, true) 
						? SelectResult.RETRY : SelectResult.CANCEL);
		}
		
		if (FileSelectionMode.FILES.equals(mode)) {
			return f.isFile() ? SelectResult.ACCEPT : 
				(askRetryFilemode(f, false) 
						? SelectResult.RETRY : SelectResult.CANCEL);
		}
		
		return SelectResult.ACCEPT;
	}

	protected SelectResult verifyFileExtension(File f) {
		StringBuilder probedFilters = new StringBuilder();
		for(FileFilter filter : build().getFileFilters()) {
			if (filter.accept(f)) return SelectResult.ACCEPT;
		
			if (probedFilters.length() > 0) {
				probedFilters.append(", ");
			}
			probedFilters.append(filter.getDescription());
		}
		
		if (build().isAllFileFilterUsed()) return SelectResult.ACCEPT;
		
		return askRetryWrongExtension(f, probedFilters.toString()) 
				? SelectResult.RETRY : SelectResult.CANCEL;
	}
	
	protected boolean askRetryFilemode(File f, boolean expectedDirectory) {
		return DialogFactory.showModalJaNeinDialog(
				build().getParentComponent(), 
				LPMain.getMessageTextRespectUISPr(
						expectedDirectory 
								? "lp.datei.open.keinverzeichnis" 
								: "lp.datei.open.keinedatei", 
				f.getAbsolutePath()),
				LPMain.getTextRespectUISPr("lp.frage"), 
				JOptionPane.INFORMATION_MESSAGE, JOptionPane.NO_OPTION);
	}

	protected boolean askRetryWrongExtension(File f, String probedFilters) {
		return DialogFactory.showModalJaNeinDialog(
				build().getParentComponent(), 
				LPMain.getMessageTextRespectUISPr("lp.datei.open.falscheendung", 
						f.getAbsolutePath(), "(" + probedFilters + ")"),
				LPMain.getTextRespectUISPr("lp.frage"), 
				JOptionPane.INFORMATION_MESSAGE, JOptionPane.NO_OPTION);
	}
	
	private T createWrapperFile(File file) {
		try {
			Constructor<T> constructor = fileClass.getConstructor(new Class[] {File.class});
			T wrapperFile = constructor.newInstance(new Object[] {file});
			return wrapperFile;
		} catch (NoSuchMethodException e) {
			myLogger.error("NoSuchMethodException", e);
		} catch (SecurityException e) {
			myLogger.error("SecurityException", e);
		} catch (IllegalArgumentException e) {
			myLogger.error("IllegalArgumentException", e);
		} catch (InvocationTargetException e) {
			myLogger.error("InvocationTargetException", e);
		} catch (InstantiationException e) {
			myLogger.error("InstantiationException", e);
		} catch (IllegalAccessException e) {
			myLogger.error("IllegalAccessException", e);
		}
		return null;
	}

	@Override
	public List<T> openMultiple() {
		List<T> wrapperFiles = new ArrayList<T>();
		List<File> files = chooseFiles();
		if (files == null) return wrapperFiles;
		
		for (File file : files) {
			wrapperFiles.add(createWrapperFile(file));
		}
		return wrapperFiles;
	}
}
