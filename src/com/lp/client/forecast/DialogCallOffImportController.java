package com.lp.client.forecast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.IFileController;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.ForecastImportFileType;
import com.lp.server.system.service.ForecastImportLinienabruf;
import com.lp.server.system.service.IForecastImportFile;

public class DialogCallOffImportController implements IFileController {
	private static final String ARCHIVE_DIRECTORY = "old";

	private List<IForecastImportFile<?>> importFiles;
	private Integer fclieferadresseId;
	private String fclieferadresseAsText;
	private File[] originalFiles;
	
	public DialogCallOffImportController(Integer fclieferadresseId, 
			List<IForecastImportFile<?>> importFiles, File[] originalFiles) {
		this.fclieferadresseId = fclieferadresseId;
		this.importFiles = importFiles;
		this.originalFiles = originalFiles;
	}

	protected List<IForecastImportFile<?>> getFiles() {
		return importFiles;
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

	public String getFclieferadresseAsText() throws ExceptionLP, Throwable {
		if(fclieferadresseAsText == null) {
			findLieferadresseText();
		}
		return fclieferadresseAsText;
	}
	
	private void findLieferadresseText() throws ExceptionLP, Throwable {
		FclieferadresseDto lieferadresseDto = DelegateFactory.getInstance().getForecastDelegate()
				.fclieferadresseFindByPrimaryKey(fclieferadresseId);
		KundeDto kundeLieferadresseDto = DelegateFactory.getInstance()
				.getKundeDelegate()
				.kundeFindByPrimaryKey(lieferadresseDto.getKundeIIdLieferadresse());

		fclieferadresseAsText = kundeLieferadresseDto.getPartnerDto()
				.formatTitelAnrede();
	}
		
	public CallOffXlsImporterResult doImport() {
		try {
			CallOffXlsImporterResult result = DelegateFactory.getInstance()
					.getForecastDelegate().importFiles(fclieferadresseId, getFiles(), false);
			moveImportFilesToArchive();
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

	public CallOffXlsImporterResult checkImport() {
		try {
			return DelegateFactory.getInstance()
					.getForecastDelegate().importFiles(fclieferadresseId, getFiles(), true);
		} catch (ExceptionLP e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_ERROR);
		} catch (Throwable e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_INFORMATION);
		}
		
		return null;
	}
	
	public void setDeliveryDate(Date deliveryDate) {
		for (IForecastImportFile<?> oneFile : getFiles()) {
			if(ForecastImportFileType.Linienabruf.equals(oneFile.getFiletype())) {
				ForecastImportLinienabruf abruf = (ForecastImportLinienabruf) oneFile;
				abruf.setCalloffDate(new java.sql.Date(deliveryDate.getTime()));
			}
		}		
	}
	
	public boolean needsDeliveryDate() {
		for (IForecastImportFile<?> oneFile : getFiles()) {
			if(ForecastImportFileType.Linienabruf.equals(oneFile.getFiletype())) {
				return true;
			}
		}		

		return false;
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
	
	protected void moveImportFilesToArchive() throws IOException {
		for (File oneFile: originalFiles) {
			moveImportFileToArchive(oneFile);
		}
	}
}
