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

import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.remote.IPayloadPublisher;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.forecast.service.CallOffXlsImportStats;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.util.EJBLineNumberExceptionLP;

public class CallOffDailyXlsImportController implements ICallOffXlsImportController {

	private static final Integer DEFAULT_STARTROW = new Integer(12);
	private File importFile;
	private Integer fclieferadresseIId;
	private Integer startRow;
	protected ICallOffXlsImportCaller importCaller;
	private String fclieferadresseText;

	public CallOffDailyXlsImportController() {
		setImportCaller(new ICallOffXlsImportCaller() {
			@Override
			public CallOffXlsImporterResult importCallOffXls(byte[] xlsDatei,
					Integer forecastIId, boolean checkOnly, Integer startRow) throws Throwable {
				return DelegateFactory.getInstance().getForecastDelegate().importCallOffDailyXls(xlsDatei, forecastIId, checkOnly, startRow);
			}
		});
	}
	
	protected void findLieferadresseText() throws ExceptionLP, Throwable {
		FclieferadresseDto lieferadresseDto = DelegateFactory.getInstance().getForecastDelegate()
				.fclieferadresseFindByPrimaryKey(getFclieferadresseIId());
		KundeDto kundeLieferadresseDto = DelegateFactory.getInstance()
				.getKundeDelegate()
				.kundeFindByPrimaryKey(lieferadresseDto.getKundeIIdLieferadresse());

		fclieferadresseText = kundeLieferadresseDto.getPartnerDto()
				.formatTitelAnrede();
	}
	
	protected void setImportCaller(ICallOffXlsImportCaller importCaller) {
		this.importCaller = importCaller;
	}

	@Override
	public void setFile(File file) {
		this.importFile = file;
	}

	@Override
	public void setFile(String path) {
		setFile(new File(path));
	}

	@Override
	public File getFile() {
		return importFile;
	}

	@Override
	public CallOffXlsImporterResult checkImport() throws FileNotFoundException, IOException {
		return importContent(readFile(getFile()), true);
	}

	private byte[] readFile(File file) throws IOException {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(getFile().getAbsolutePath());
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
	public CallOffXlsImporterResult checkImport(IPayloadPublisher publisher)
			throws FileNotFoundException, IOException {
		try {
			return importCaller.importCallOffXls(readFile(getFile()), getFclieferadresseIId(), true, getStartRow());
		} catch (ExceptionLP e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_ERROR);
		} catch (Throwable e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_INFORMATION);
		}
		
		return new CallOffXlsImporterResult(new ArrayList<EJBLineNumberExceptionLP>(), new CallOffXlsImportStats());
	}

	@Override
	public CallOffXlsImporterResult doImport() throws FileNotFoundException, IOException {
		return importFile(getFile());
	}

	@Override
	public CallOffXlsImporterResult doImport(IPayloadPublisher publisher)
			throws FileNotFoundException, IOException {
		try {
			CallOffXlsImporterResult result = importCaller.importCallOffXls(readFile(getFile()), getFclieferadresseIId(), false, getStartRow());
			moveImportFileToArchive(importFile);
			return result;
		} catch (ExceptionLP e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_ERROR);
		} catch (Throwable e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_INFORMATION);
		}
		return null;
	}

	@Override
	public CallOffXlsImporterResult importContent(byte[] content, boolean checkOnly) {
		try {
			CallOffXlsImporterResult result = importCaller.importCallOffXls(content, getFclieferadresseIId(), checkOnly, getStartRow());
			if (!checkOnly) moveImportFileToArchive(importFile);
			return result;
		} catch (ExceptionLP e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_ERROR);
		} catch (Throwable e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_INFORMATION);
		}
		return null;
	}

	@Override
	public CallOffXlsImporterResult importFile(File file)
			throws FileNotFoundException, IOException {
		return importContent(readFile(getFile()), false);
	}

	@Override
	public CallOffXlsImporterResult importFile(String filename)
			throws FileNotFoundException, IOException {
		return importFile(new File(filename));
	}

	public Integer getFclieferadresseIId() {
		return fclieferadresseIId;
	}
	
	@Override
	public void setFclieferadresseIId(Integer fclieferadresseIId) throws ExceptionLP, Throwable {
		this.fclieferadresseIId = fclieferadresseIId;
		findLieferadresseText();
	}

	public Integer getStartRow() {
		if (startRow == null) {
			startRow = DEFAULT_STARTROW;
		}
		return startRow;
	}
	
	@Override
	public void setStartRow(Integer startRow) {
		this.startRow = startRow;
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
	
	protected interface ICallOffXlsImportCaller {
		CallOffXlsImporterResult importCallOffXls(byte[] xlsDatei, Integer forecastIId, boolean checkOnly,
			Integer startRow) throws Throwable;		
	}

	@Override
	public String getDialogTitle() {
		return LPMain.getTextRespectUISPr("fc.menu.codxlsimport");
	}

	@Override
	public String getFclieferadresseAsText() {
		return fclieferadresseText;
	}
}
