package com.lp.client.lieferschein;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.IImportResultSingleFileController;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.filechooser.open.XmlFile;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.lieferschein.service.EasydataImportResult;

public class EasydataStockMovementImportCtrl implements IImportResultSingleFileController<EasydataImportResult> {
	protected final LpLogger log = (LpLogger) LpLogger.getInstance(this.getClass());

	private static final String ARCHIVE_DIRECTORY = "old";
	private XmlFile xmlFile;
	
	public EasydataStockMovementImportCtrl(XmlFile xmlFile) {
		this.xmlFile = xmlFile;
	}

	@Override
	public EasydataImportResult doImport() {
		return importXmlEasydata(false);
	}

	@Override
	public EasydataImportResult checkImport() {
		return importXmlEasydata(true);
	}

	private EasydataImportResult importXmlEasydata(boolean checkOnly) {
		try {
			EasydataImportResult result = DelegateFactory.getInstance().getLsDelegate()
					.importXMLEasydataStockMovements(xmlFile.read("UTF-8"), checkOnly);
			if (!checkOnly && !result.hasErrors()) {
				moveFileToArchive(xmlFile.getFile());
			}
			return result;
		} catch (ExceptionLP e) {
			log.error("ExceptionLP", e);
			new DialogError(LPMain.getInstance().getDesktop(), e, DialogError.TYPE_ERROR);
		} catch (IOException e) {
			log.error("IOException", e);
			new DialogError(LPMain.getInstance().getDesktop(), e, DialogError.TYPE_ERROR);
		}
		return null;
	}
	
	@Override
	public String getFilename() {
		return xmlFile.getFile().getAbsolutePath();
	}

	private void moveFileToArchive(File file) throws IOException {
		File parent = new File(file.getParent());
		if (!parent.isDirectory()) return;
		
		File archiveFolder = new File(parent, ARCHIVE_DIRECTORY);
		if (!archiveFolder.exists()) {
			archiveFolder.mkdir();
		}
		
		File archive = new File(archiveFolder, file.getName());

		FileInputStream fileIS = null;
		FileOutputStream fileOS = null;
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		
		try {
			fileIS = new FileInputStream(file);
			inChannel = fileIS.getChannel();
			
			if (!archiveFolder.canWrite()) return;
			fileOS = new FileOutputStream(archive);
			outChannel = fileOS.getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			LpLogger.getInstance(this.getClass()).error("Kopieren von '" + file.getName() + "' ist fehlgeschlagen");
			throw e;
		} finally {
			try {
				if (inChannel != null) 
					inChannel.close();
				
				if (fileIS != null) 
					fileIS.close();
				
				if (outChannel != null)	
					outChannel.close();
				
				if (fileOS != null) 
					fileOS.close();
				
			} catch (IOException e) {
			}
		}
		
		if (archive != null && archive.exists()) {
			archive.setLastModified(file.lastModified());
			file.delete();
		}
	}
}
