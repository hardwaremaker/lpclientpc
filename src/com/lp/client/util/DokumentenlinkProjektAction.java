package com.lp.client.util;

import java.io.File;
import java.io.FileFilter;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.ProjektServiceDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.DokumentenlinkDto;
import com.lp.util.Helper;

public class DokumentenlinkProjektAction extends DokumentenlinkAction {

	private ProjektDto projektDto;
	
	public DokumentenlinkProjektAction(DokumentenlinkDto dokumentenlinkDto, ProjektDto projektDto) {
		super(dokumentenlinkDto, projektDto.getCNr());
		this.projektDto = projektDto;
	}

	private String getProjektTitel(String pathSoFar, final String projektCnr,
			DokumentenlinkDto dokumentenlinkDto, boolean renameOnTitleChange) throws Throwable {
		if (Helper.short2boolean(dokumentenlinkDto.getBUrl()))
			return null;

		String title = projektDto.getCTitel();
		title = eliminateSpecialChars(title);
		String filenameWithTitle = projektCnr + "_" + title;
		
		if (renameOnTitleChange) {
			File dir = new File(pathSoFar);
			if (dir.isDirectory()) {
				File[] matchingFiles = dir.listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.getName().startsWith(projektCnr);
					}
				});
				if (matchingFiles.length == 1) {
					if (matchingFiles[0].getName().equals(projektCnr)) {
						File fileWithTitle = new File(pathSoFar + filenameWithTitle);
						if (matchingFiles[0].renameTo(fileWithTitle)) {
							return fileWithTitle.getName();
						} else {
							myLogger.warn("Ordner "
									+ matchingFiles[0].getAbsolutePath()
									+ " konnte nicht nach "
									+ fileWithTitle.getAbsolutePath()
									+ " umbenannt werden.");
						}
					}
					return matchingFiles[0].getName();
				}
			}
		}
		return filenameWithTitle;
	}
	
	@Override
	protected String getPath(String modulPlatzhalter, DokumentenlinkDto dokumentenlinkDto)
			throws ExceptionLP, Throwable {
		return getPath(modulPlatzhalter, projektDto.getBereichIId(), dokumentenlinkDto, true);
	}

	private String getPath(String modulPlatzhalter, Integer bereichId,
			DokumentenlinkDto dokumentenlinkDto, boolean renameOnTitleChange) throws ExceptionLP, Throwable {
		StringBuilder builder = new StringBuilder();
		builder.append(dokumentenlinkDto.getCBasispfad());

		modulPlatzhalter = eliminateSpecialChars(modulPlatzhalter);

		builder.append(getProjektBereich(dokumentenlinkDto, bereichId));
		if (Helper.short2boolean(dokumentenlinkDto.getBTitel())) {
			builder.append(getProjektTitel(builder.toString(),
					modulPlatzhalter, dokumentenlinkDto, renameOnTitleChange));
		} else {
			builder.append(modulPlatzhalter);
		}

		if (dokumentenlinkDto.getCOrdner() != null) {
			builder.append(dokumentenlinkDto.getCOrdner());
		}

		return builder.toString();
	}

	private String getPathBereichsaenderung(String projektCnr, Integer bereichId, DokumentenlinkDto dokumentenlinkDto) throws ExceptionLP, Throwable {
		return getPath(projektCnr, bereichId, dokumentenlinkDto, false);
	}
	
	private ProjektServiceDelegate projektServiceDelegate() throws Throwable {
		return DelegateFactory.getInstance().getProjektServiceDelegate();
	}
	
	private String getBereichPfadKonform(Integer bereichId) throws ExceptionLP, Throwable {
		String bereich = projektServiceDelegate().bereichFindByPrimaryKey(bereichId).getCBez();
		return bereich.replaceAll("[^a-zA-Z0-9-.]", "_");
	}
	
	private String getProjektBereich(DokumentenlinkDto dokumentenlinkDto, Integer bereichId)
			throws Throwable, ExceptionLP {
		StringBuilder builder = new StringBuilder();
		builder.append(getBereichPfadKonform(bereichId));
		// SP2838
		if (Helper.short2boolean(dokumentenlinkDto.getBUrl())) {
			builder.append("_");
			return builder.toString();
		}

		builder.append(System.getProperty("file.separator"));

		return builder.toString();
	}

	public void processBereichsaenderung(ProjektDto projektChanged) throws ExceptionLP, Throwable {
		DokumentenlinkDto doklinkDto = getDokumentenlinkDto();
		if (Helper.short2boolean(doklinkDto.getBUrl())) {
			return;
		}
		
		File oldDir = new File(getPathBereichsaenderung(projektDto.getCNr(), projektDto.getBereichIId(), doklinkDto));
		if (oldDir.exists()) {
			File newDir = new File(getPathBereichsaenderung(projektChanged.getCNr(), projektChanged.getBereichIId(), doklinkDto));
			File parentNewDir = newDir.getParentFile();
			if (parentNewDir != null && !parentNewDir.exists()) {
				parentNewDir.mkdirs();
			}
			boolean renamed = oldDir.renameTo(newDir);
			if (!renamed) {
				myLogger.error("Update Projekt " + projektDto.getCNr() + " mit Bereichsaenderung: "
						+ "Dokumentenlink '" + oldDir.getAbsolutePath() + "' konnte nicht nach '"
						+ newDir.getAbsolutePath() + "' umbenannt werden.");
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.info"),
						LPMain.getMessageTextRespectUISPr("projekt.update.bereichsaenderung.dokumentenlink.umbenennungfehlgeschlagen", 
								oldDir.getAbsolutePath()));
			}
		}
	}
}
