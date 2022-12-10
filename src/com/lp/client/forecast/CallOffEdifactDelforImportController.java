package com.lp.client.forecast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.remote.IPayloadPublisher;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.forecast.service.CallOffXlsImportStats;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.forecast.service.EdiFileInfo;
import com.lp.util.EJBLineNumberExceptionLP;

public class CallOffEdifactDelforImportController implements
		ICallOffXlsImportController {

	private File[] files;
	private Integer forecastId;
	
	public CallOffEdifactDelforImportController(Integer forecastId, File[] edifactFiles) {
		this.forecastId = forecastId;
		this.files = edifactFiles;
	}
	
	private CallOffXlsImporterResult callImport(boolean checkOnly) throws FileNotFoundException, IOException, ExceptionLP, Throwable {
		List<EdiFileInfo> ediContents = new ArrayList<EdiFileInfo>();
		for (File file : files) {
			ediContents.add(new EdiFileInfo(readFile(file), file.getName()));
		}	
		
		return DelegateFactory.getInstance().getForecastDelegate()
			.importDelforEdifact(forecastId, ediContents, checkOnly);
	}
	
	private void archiveFiles() throws IOException {
		for (File file : files) {
			moveToArchive(file);
		}
	}
	
	private void moveToArchive(File file) throws IOException {
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
			throw e;
		} catch (IOException e) {
			LpLogger.getInstance(this.getClass()).error("Kopieren von '" + file.getName() + "' ist fehlgeschlagen");
			throw e;
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
			file.delete();
		}
	}	
	private byte[] readFile(File file) throws IOException {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file.getAbsolutePath());
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException ex) {
			}

			try {
				if (ios != null)
					ios.close();
			} catch (IOException ex) {
			}
		}

		return ous.toByteArray();
	}
	
	
	@Override
	public CallOffXlsImporterResult checkImport() throws FileNotFoundException,
			IOException {
		try {
			return callImport(true);
		} catch (ExceptionLP e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_ERROR);
		} catch (Throwable e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_INFORMATION);
		}		

		return new CallOffXlsImporterResult(
				new ArrayList<EJBLineNumberExceptionLP>(), new CallOffXlsImportStats());
	}

	@Override
	public CallOffXlsImporterResult checkImport(IPayloadPublisher publisher)
			throws FileNotFoundException, IOException {
		return null;
	}

	@Override
	public CallOffXlsImporterResult doImport() throws FileNotFoundException,
			IOException {
		try {
			CallOffXlsImporterResult result = callImport(false);
			archiveFiles();
			return result;
		} catch (ExceptionLP e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_ERROR);
		} catch (Throwable e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_INFORMATION);
		}		

		return new CallOffXlsImporterResult(
				new ArrayList<EJBLineNumberExceptionLP>(), new CallOffXlsImportStats());
	}

	@Override
	public CallOffXlsImporterResult doImport(IPayloadPublisher publisher)
			throws FileNotFoundException, IOException {
		return null;
	}

	@Override
	public CallOffXlsImporterResult importContent(byte[] content,
			boolean checkOnly) {
		return null;
	}

	@Override
	public CallOffXlsImporterResult importFile(File file)
			throws FileNotFoundException, IOException {
		return null;
	}

	@Override
	public CallOffXlsImporterResult importFile(String filename)
			throws FileNotFoundException, IOException {
		return null;
	}

	@Override
	public void setFile(File file) {
	}

	@Override
	public void setFile(String path) {
	}

	@Override
	public File getFile() {
		return null;
	}

	@Override
	public void setFclieferadresseIId(Integer forecastIId) throws ExceptionLP,
			Throwable {

	}

	@Override
	public void setStartRow(Integer startRow) {
	}

	@Override
	public String getDialogTitle() {
		return LPMain.getTextRespectUISPr("fc.menu.edifact.delfor");
	}

	@Override
	public String getFclieferadresseAsText() {
		return "Unterschiedliche Adressen";
	}

	@Override
	public Integer getStartRow() {
		return 0;
	}
}
