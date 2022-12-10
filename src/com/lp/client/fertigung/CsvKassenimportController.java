package com.lp.client.fertigung;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.IImportResultController;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.filechooser.open.CsvFile;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.fertigung.service.VerbrauchsartikelImportResult;
import com.lp.server.fertigung.service.errors.ImportException;

public class CsvKassenimportController implements IImportResultController<VerbrauchsartikelImportResult> {
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	private static final String ARCHIVE_DIRECTORY = "old";
	private List<CsvFile> importFiles;
	private List<List<String[]>> importParts;
	private Integer position;
	
	public CsvKassenimportController(List<CsvFile> csvFiles) throws IOException {
		setImports(csvFiles);
	}

	public void setImports(List<CsvFile> importFiles) throws IOException {
		this.importFiles = importFiles;
		this.importParts = readFiles(importFiles);
		position = 0;
	}

	private List<List<String[]>> readFiles(List<CsvFile> importFiles) throws IOException {
		List<List<String[]>> list = new ArrayList<List<String[]>>();
		for (CsvFile file : importFiles) {
			list.add(file.read(';'));
		}
		return list;
	}
	
	public List<List<String[]>> getImportParts() {
		if (importParts == null) {
			importParts = new ArrayList<List<String[]>>();
		}
		return importParts;
	}
	
	public List<CsvFile> getImportFiles() {
		return importFiles;
	}
	
	@Override
	public boolean hasNext() {
		return getImportParts().size() > position + 1;
	}
	
	@Override
	public String getCurrentFilename() {
		return position < getImportFiles().size() ? getImportFiles().get(position).getFile().getName() : null;
	}

	@Override
	public String getNextFilename() {
		return position + 1 < getImportFiles().size() ? getImportFiles().get(position+1).getFile().getName() : null;
	}
	
	@Override
	public Integer getPosition() {
		return position + 1;
	}
	
	@Override
	public Integer getNumberOfFiles() {
		return getImportFiles().size();
	}

	@Override
	public VerbrauchsartikelImportResult checkImport() {
		if (position >= getImportFiles().size()) return new VerbrauchsartikelImportResult(new ArrayList<ImportException>());
		
		try {
			VerbrauchsartikelImportResult result = DelegateFactory.getInstance()
					.getFertigungDelegate().importCsvVerbrauchsartikel(getImportParts().get(position), true);
			return result;
		} catch (ExceptionLP e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_ERROR);
		} catch (Throwable e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_ERROR);
		}
		return null;
	}
	
	@Override
	public VerbrauchsartikelImportResult doImport() {
		if (position >= getImportFiles().size()) return new VerbrauchsartikelImportResult(new ArrayList<ImportException>());
		
		try {
			VerbrauchsartikelImportResult result = DelegateFactory.getInstance()
					.getFertigungDelegate().importCsvVerbrauchsartikel(getImportParts().get(position), false);
			if (!hasErrors(result)) {
				moveImportFileToArchive(getImportFiles().get(position).getFile());
				position++;
			}
			return result;
		} catch (ExceptionLP e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_ERROR);
		} catch (Throwable e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_ERROR);
		}
		return null;
	}

	private boolean hasErrors(VerbrauchsartikelImportResult result) {
		return !result.getImportErrors().isEmpty();
	}

	protected void moveImportFileToArchive(File file) throws IOException {
		File parent = new File(file.getParent());
		if (!parent.isDirectory()) return;
		
		File archiveFolder = new File(parent, ARCHIVE_DIRECTORY);
		if (!archiveFolder.exists()) {
			archiveFolder.mkdir();
		}
		
		File archive = new File(archiveFolder, file.getName());
		
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		
		try {
			inChannel = new FileInputStream(file).getChannel();
			if (!archiveFolder.canWrite()) return;
			outChannel = new FileOutputStream(archive).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (FileNotFoundException e) {
			LpLogger.getInstance(this.getClass()).error("Kopieren von '" + file.getName() + "' ist fehlgeschlagen");
		} catch (IOException e) {
			LpLogger.getInstance(this.getClass()).error("Kopieren von '" + file.getName() + "' ist fehlgeschlagen");
		} finally {
			try {
				if (inChannel != null) {
					inChannel.close();
				}
				if (outChannel != null) {
					outChannel.close();
				}
			} catch (IOException e) {
			}
		}
		
		if (archive != null && archive.exists()) {
			archive.setLastModified(file.lastModified());
			boolean deletionSucceeded = file.delete();
			if (!deletionSucceeded) {
				myLogger.error("Konnte Datei '" + file.getAbsolutePath() + "' nicht loeschen");
			}
		}
	}
}
