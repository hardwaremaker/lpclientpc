package com.lp.client.forecast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.remote.IPayloadPublisher;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.forecast.service.CallOffXlsImporterResult;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.partner.service.KundeDto;

public class LinienabrufEluxDeImportController implements ILinienabrufEluxDeImportController {

	private File importFile;
	private Integer fclieferadresseIId;
	private Date deliveryDate;
	private String fclieferadresseText;

	public LinienabrufEluxDeImportController(Integer fclieferadresseIId) throws ExceptionLP, Throwable {
		setFclieferadresseIId(fclieferadresseIId);
		findLieferadresseText();
	}

	private void findLieferadresseText() throws ExceptionLP, Throwable {
		FclieferadresseDto lieferadresseDto = DelegateFactory.getInstance().getForecastDelegate()
				.fclieferadresseFindByPrimaryKey(getFclieferadresseIId());
		KundeDto kundeLieferadresseDto = DelegateFactory.getInstance()
				.getKundeDelegate()
				.kundeFindByPrimaryKey(lieferadresseDto.getKundeIIdLieferadresse());

		fclieferadresseText = kundeLieferadresseDto.getPartnerDto()
				.formatTitelAnrede();
	}
	
	@Override
	public CallOffXlsImporterResult checkImport() throws FileNotFoundException,
			IOException {
		return importContent(readFile(getFile()), true);
	}

	@Override
	public CallOffXlsImporterResult checkImport(IPayloadPublisher publisher)
			throws FileNotFoundException, IOException {
		return importContent(readFile(getFile()), true);
	}

	@Override
	public CallOffXlsImporterResult doImport() throws FileNotFoundException,
			IOException {
		return importContent(readFile(getFile()), false);
	}

	@Override
	public CallOffXlsImporterResult doImport(IPayloadPublisher publisher)
			throws FileNotFoundException, IOException {
		return null;
	}

	@Override
	public CallOffXlsImporterResult importContent(List<String> content, boolean checkOnly) {
		try {
			CallOffXlsImporterResult result = DelegateFactory.getInstance().getForecastDelegate().
					importLinienabrufEluxDe(content, getFclieferadresseIId(), getDeliveryDate(), checkOnly);
			if (!checkOnly) moveImportFileToArchive();
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
		return null;
	}

	@Override
	public CallOffXlsImporterResult importFile(String filename)
			throws FileNotFoundException, IOException {
		return null;
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

	private List<String> readFile(File file) throws FileNotFoundException, IOException {
		BufferedReader reader = null;
		
		try {
			FileInputStream fileInputStream = new FileInputStream(importFile);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			List<String> inputs = new ArrayList<String>();
			
			String line = reader.readLine();
			while (line != null) {
				inputs.add(line);
				line = reader.readLine();
			}
			
			return inputs;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	private void moveImportFileToArchive() throws IOException {
		File parent = new File(importFile.getParent());
		if (!parent.isDirectory()) return;
		
		File archiveFolder = new File(parent, ARCHIVE_DIRECTORY);
		if (!archiveFolder.exists()) {
			archiveFolder.mkdir();
		}
		
		File archive = new File(archiveFolder, importFile.getName());
		
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		
		try {
			inChannel = new FileInputStream(importFile).getChannel();
			if (!archiveFolder.canWrite()) return;
			outChannel = new FileOutputStream(archive).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			LpLogger.getInstance(this.getClass()).error("Kopieren von '" + importFile.getName() + "' ist fehlgeschlagen");
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
			archive.setLastModified(importFile.lastModified());
			importFile.delete();
		}
	}

	public Integer getFclieferadresseIId() {
		return fclieferadresseIId;
	}

	public void setFclieferadresseIId(Integer fclieferadresseIId) {
		this.fclieferadresseIId = fclieferadresseIId;
	}

	@Override
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	@Override
	public String getFclieferadresseAsText() {
		return fclieferadresseText;
	}
}
