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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogJCRUploadSettings;
import com.lp.client.frame.component.DropListener;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.util.logger.LpLogger;
import com.lp.util.Helper;

/**
 * Superklasse fuer DropListener, die email drops verarbeiten. Hat auch eine
 * utility Methode, die dabei hilft, einen Dokumentenablage-Eintrag aus der
 * email zu erzeugen
 * 
 * @author Alexander Daum
 *
 */
public abstract class EmailDropHandler implements DropListener {

	protected Logger myLogger = LpLogger.getLogger(getClass().getCanonicalName());
	private JCRDocDto dialogDaten;

	public EmailDropHandler() {
	}

	@Override
	public final void filesDropped(List<File> files) {
		if (files.size() > 1) {
			myLogger.warn("Mehr als eine Datei bei Email Drop, ignoriere weitere");
		}
		if (files.size() == 0) {
			return;
		}

		Path f = files.get(0).toPath();
		try (InputStream is = new BufferedInputStream(Files.newInputStream(f))) {
			Optional<EmailParser> parser = EmailParser.create(is, HelperClient.getMimeTypeOfFile(f).toString(), true);
			if (parser.isPresent()) {
				if (showDialogUpload()) {
					emailDropped(parser.get(), f);
				}
			}
		} catch (IOException e) {
			myLogger.warn("Fehler beim Lesen von Email", e);
		} catch (MessagingException e) {
			myLogger.warn("Email konnte nicht gelesen werden, eventuell falsche Datei?", e);
		} catch (Throwable e) {
			myLogger.error("Fehler bei Email Drag&Drop", e);
		}
	}

	protected abstract void emailDropped(EmailParser email, Path pathToMail);

	/**
	 * Erzeugt einen Dokumentenablage-Eintrag aus einer Email
	 * 
	 * @param key       Key fuer das Objekt, meistens die ID
	 * @param idUseCase
	 * @param sRow
	 * @param mailFile  Pfad zur Datei, in der die Mail gespeichert ist. Wird
	 *                  gelesen
	 * @throws Throwable
	 */
	protected void addMailToDokumentenablage(Object key, Integer idUseCase, String sRow, Path mailFile)
			throws Throwable {
		PrintInfoDto values = DelegateFactory.getInstance().getJCRDocDelegate().getPathAndPartnerAndTable(key,
				idUseCase);
		JCRDocDto jcrDocDto = new JCRDocDto();
		String filename = mailFile.getFileName().toString();
		jcrDocDto.setDocPath(values.getDocPath().add(new DocNodeFile("")));
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setbData(Files.readAllBytes(mailFile));
		jcrDocDto.setsBelegart(dialogDaten.getsBelegart());
		jcrDocDto.setlAnleger(LPMain.getTheClient().getIDPersonal());
		jcrDocDto.setlPartner(values.getiId());
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegnummer(values.getDocPath().getLastDocNode().toString());
		jcrDocDto.setsName(" ");
		jcrDocDto.setsFilename(filename);
		jcrDocDto.setsTable(values.getTable());
		jcrDocDto.setsMIME(Helper.getMime(filename));
		jcrDocDto.setlSicherheitsstufe(dialogDaten.getlSicherheitsstufe());
		jcrDocDto.setsRow(sRow);
		jcrDocDto.setsGruppierung(dialogDaten.getsGruppierung());
		JCRFileUploader.uploadFiles(Collections.singletonList(mailFile.toFile()), jcrDocDto);
	}

	protected boolean showDialogUpload() {
		dialogDaten = new JCRDocDto();
		dialogDaten.setsBelegart(JCRDocFac.DEFAULT_EMAIL_BELEGART);
		dialogDaten.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		dialogDaten.setlSicherheitsstufe(hoechsteSicherheitsstufe());
		try {
			DialogJCRUploadSettings dialogUpload = new DialogJCRUploadSettings(LPMain.getInstance().getDesktop(),
					dialogDaten);
			return dialogUpload.getValue() == DialogJCRUploadSettings.SAVE;
		} catch (Throwable e) {
			myLogger.error("Fehler beim Anzeigen von Upload Dialog", e);
		}
		return false;
	}

	private long hoechsteSicherheitsstufe() {
		try {
			if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_3_CU)) {
				return JCRDocFac.SECURITY_HIGH;
			} else if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_2_CU)) {
				return JCRDocFac.SECURITY_MEDIUM;
			} else if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_1_CU)) {
				return JCRDocFac.SECURITY_LOW;
			} else if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_0_CU)) {
				return JCRDocFac.SECURITY_NONE;
			} else if (DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_99_CU)) {
				return JCRDocFac.SECURITY_ARCHIV;
			}
		} catch (Throwable e) {
			myLogger.warn("Fehler beim holen von Sicherheitsstufe; setze default = 0", e);
		}
		return JCRDocFac.SECURITY_NONE;
	}
}
