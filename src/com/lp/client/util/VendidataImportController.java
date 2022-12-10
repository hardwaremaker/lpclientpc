/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.util;

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
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.remote.IPayloadPublisher;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.eingangsrechnung.service.VendidataImportStats;
import com.lp.server.eingangsrechnung.service.VendidataImporterResult;
import com.lp.util.EJBVendidataImportExceptionLP;

public class VendidataImportController implements IVendidataImportController<VendidataImporterResult> {
	
	private File importFile;
	private static final String ARCHIVE_DIRECTORY = "old";

	public VendidataImportController() {
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
	public VendidataImporterResult checkImport() throws FileNotFoundException, IOException {
		return importXML(readFile(importFile), true);
	}

	@Override
	public VendidataImporterResult doImport() throws FileNotFoundException, IOException {
		return importXMLFile(importFile);
	}

	@Override
	public VendidataImporterResult importXML(String xmlContent, boolean checkOnly) {
		
		try {
			VendidataImporterResult importResult = DelegateFactory.getInstance().getEingangsrechnungDelegate().importXml(xmlContent, checkOnly);
			if (!checkOnly && !importResult.hasErrors()) {
				moveImportFileToArchive();
			}
			return importResult;
		} catch (ExceptionLP e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new VendidataImporterResult(new ArrayList<EJBVendidataImportExceptionLP>(), new VendidataImportStats());
	}

	@Override
	public VendidataImporterResult importXMLFile(File file) throws FileNotFoundException, IOException {
		return importXML(readFile(file), false);
	}

	@Override
	public VendidataImporterResult importXMLFile(String filename) throws FileNotFoundException, IOException {
		return importXMLFile(new File(filename));
	}

	private String readFile(File file) throws FileNotFoundException, IOException {
		BufferedReader reader = null;
		
		try {
			FileInputStream fileInputStream = new FileInputStream(importFile);
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

	@Override
	public VendidataImporterResult doImport(IPayloadPublisher publisher) {
		return null;
	}

	@Override
	public VendidataImporterResult checkImport(IPayloadPublisher publisher)
			throws FileNotFoundException, IOException {
		return null;
	}
}
