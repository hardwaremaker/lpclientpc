package com.lp.client.fertigung;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import com.lp.client.eingangsrechnung.IVendidataImportController;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.remote.IPayloadPublisher;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.eingangsrechnung.service.VendidataImportStats;
import com.lp.server.fertigung.service.VendidataArticleConsumptionImportResult;
import com.lp.util.EJBVendidataArticleExceptionLP;

public class VendidataArticleConsumptionController implements IVendidataImportController<VendidataArticleConsumptionImportResult> {

	private static final String ARCHIVE_DIRECTORY = "old";
	private File importFile;
	
	public VendidataArticleConsumptionController() {
	}

	@Override
	public void setImportFile(File fileToImport) {
		importFile = fileToImport;
	}

	@Override
	public File getImportFile() {
		return importFile;
	}

	@Override
	public VendidataArticleConsumptionImportResult checkImport() throws FileNotFoundException, IOException {
		return importXML(readFile(importFile), true);
	}

	public VendidataArticleConsumptionImportResult checkImport(IPayloadPublisher publisher) throws FileNotFoundException, IOException {
		try {
			return DelegateFactory.getInstance().getFertigungDelegate().importXml(readFile(getImportFile()), true, publisher);
		} catch (ExceptionLP e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new VendidataArticleConsumptionImportResult(
				new ArrayList<EJBVendidataArticleExceptionLP>(), new VendidataImportStats());
	}
	@Override
	public VendidataArticleConsumptionImportResult doImport() throws FileNotFoundException, IOException {
		return importXMLFile(importFile);
	}

	@Override
	public VendidataArticleConsumptionImportResult doImport(IPayloadPublisher publisher)
			throws FileNotFoundException, IOException {
		try {
			VendidataArticleConsumptionImportResult result = DelegateFactory.getInstance().getFertigungDelegate()
					.importXml(readFile(getImportFile()), false, publisher);
			moveImportFileToArchive();
			return result;
		} catch (ExceptionLP e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_ERROR);
		} catch (Throwable e) {
			new DialogError(LPMain.getInstance().getDesktop(), e,
					DialogError.TYPE_INFORMATION);
		}
		
		return new VendidataArticleConsumptionImportResult(
				new ArrayList<EJBVendidataArticleExceptionLP>(), new VendidataImportStats());
	}

	@Override
	public VendidataArticleConsumptionImportResult importXML(String xmlContent,	boolean checkOnly) {
		try {
			return DelegateFactory.getInstance().getFertigungDelegate().importXml(xmlContent, checkOnly);
		} catch (ExceptionLP e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new VendidataArticleConsumptionImportResult(
				new ArrayList<EJBVendidataArticleExceptionLP>(), new VendidataImportStats());
	}

	@Override
	public VendidataArticleConsumptionImportResult importXMLFile(File file)	throws FileNotFoundException, IOException {
		return importXML(readFile(file), false);
	}

	@Override
	public VendidataArticleConsumptionImportResult importXMLFile(String filename) throws FileNotFoundException, IOException {
		return importXMLFile(new File(filename));
	}

	private String readFile(File file) throws FileNotFoundException, IOException {
		BufferedReader reader = null;
		
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			StringBuilder builder = new StringBuilder();
			
			String line = reader.readLine();
			while (line != null) {
				builder.append(line);
				line = reader.readLine();
			}
			
			return builder.toString();
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
}
