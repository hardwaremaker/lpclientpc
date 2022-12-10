package com.lp.client.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.DokumentenlinkDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.util.Helper;

public class DokumentenlinkAction {
	protected final LpLogger myLogger = (LpLogger) com.lp.client.util.logger.LpLogger
			.getInstance(this.getClass());

	private DokumentenlinkDto dokumentenlinkDto;
	private String modulPlatzhalter;
	
	public DokumentenlinkAction(DokumentenlinkDto dokumentenlinkDto, String modulPlatzhalter) {
		this.dokumentenlinkDto = dokumentenlinkDto;
		this.modulPlatzhalter = modulPlatzhalter;
	}

	protected DokumentenlinkDto getDokumentenlinkDto() throws ExceptionLP, Throwable {
		return dokumentenlinkDto;
	}
	
	public void open() throws ExceptionLP, Throwable {
		String pfad = getPath(modulPlatzhalter, getDokumentenlinkDto());
		
		if (Helper.short2boolean(dokumentenlinkDto.getBUrl())) {
			openUrl(getDokumentenlinkDto(), pfad);
		} else {
			openFile(getDokumentenlinkDto(), pfad);
		}
	}

	private void openFile(DokumentenlinkDto dokumentenlinkDto, String pfad) throws Throwable {
		try {
			java.io.File f = new File(pfad);
			if (!f.exists()) {
				boolean b = new File(pfad).mkdirs();
				if (b == false) {
					// Fehlgeschlagen
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							pfad + " Konnte nicht angelegt werden!");
					return;
				}
				f = new File(pfad);
			}

			try {

				if (Helper.short2boolean(dokumentenlinkDto
						.getBPfadAusArbeitsplatzparameter())) {
					ArbeitsplatzparameterDto pfadDto = DelegateFactory
							.getInstance().getParameterDelegate()
							.holeArbeitsplatzparameter(
									ParameterFac.ARBEITSPLATZPARAMETER_PROGRAMMPFAD_FUER_DOKUMENTENLINK);

					if (pfadDto == null
							|| pfadDto.getCWert() != null
							&& pfadDto.getCWert().trim().length() == 0) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("lp.dokumentenlink.pfadausparameter.nichtdefiniert"));
					} else {
						Runtime runtime = Runtime.getRuntime();
						runtime.exec(pfadDto.getCWert() + " " + pfad);
					}

				} else {
					java.awt.Desktop.getDesktop().open(f);
				}
			} catch (Exception e1) {
				String message = "Dokumentenpfad: "	+ f.toString();
				LpLogger.getInstance(this.getClass()).warn(message, e1);
			}
		} catch (java.lang.IllegalArgumentException e1) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					e1.getMessage());
		}
	}

	private void openUrl(DokumentenlinkDto dokumentenlinkDto, String pfad) {
		try {
			int i = pfad.indexOf("://");
			URI uri = new URI((i < 0 ? "http://" : "") + pfad.trim());
			java.awt.Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException ex1) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.fehlerhafteurl") + ": " + pfad);
		} catch (IOException ex1) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					ex1.getMessage());
		}
	}

	protected String getPath(String modulPlatzhalter,
			DokumentenlinkDto dokumentenlinkDto) throws ExceptionLP, Throwable {
		StringBuilder builder = new StringBuilder();
		builder.append(dokumentenlinkDto.getCBasispfad());

		modulPlatzhalter = eliminateSpecialChars(modulPlatzhalter);
		builder.append(modulPlatzhalter);

		if (dokumentenlinkDto.getCOrdner() != null) {
			builder.append(dokumentenlinkDto.getCOrdner());
		}

		return builder.toString();
	}
	
	protected String eliminateSpecialChars(String value) {
		String[] charFrom = new String[] { "\u00E4", "\u00C4", "\u00F6",
				"\u00D6", "\u00FC", "\u00DC", "\u00DF" };
		String[] charTo = new String[] { "ae", "Ae", "oe", "Oe", "ue", "Ue",
				"ss" };

		for (int i = 0; i < charFrom.length; i++)
			value = value.replaceAll(charFrom[i], charTo[i]);

		return value.replaceAll("[^a-zA-Z0-9-.]", "_");
	}

}
