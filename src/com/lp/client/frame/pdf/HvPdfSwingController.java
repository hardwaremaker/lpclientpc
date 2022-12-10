package com.lp.client.frame.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.function.Supplier;

import org.icepdf.ri.common.SwingController;

import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.FileChooserSaveDialog;
import com.lp.client.frame.filechooser.WrapperSaveFileChooser;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;

/**
 * Diese Klasse modifizert den SwingController von icePDF <br>
 * 
 * Zusätzliche Funktionen, die bereitgestellt werden:
 * <ul>
 * <li>zum speichern wird der HeliumV WrapperFileChooser verwendet</li>
 * </ul>
 * 
 * @author Alexander Daum
 */
public class HvPdfSwingController extends SwingController {
	private final String reportName;
	private final Supplier<Optional<String>> filenameSupplier;
	private final LpLogger myLogger = (LpLogger) LpLogger.getInstance(getClass());

	/**
	 * @param reportname       Der name des Reports. Wird als Token f&uuml;r den
	 *                         WrapperFileChooser verwendet
	 * @param filenameSupplier Ein OpSupplier f&uuml;r einen String, der als
	 *                         Dateiname zum speichern verwendet wird. Muss keine
	 *                         Dateiendung enthalten und darf null sein, wenn nicht
	 *                         ben&ouml;tigt
	 */
	public HvPdfSwingController(String reportname, Supplier<Optional<String>> filenameSupplier) {
		this.reportName = reportname;
		this.filenameSupplier = filenameSupplier;
	}

	/**
	 * Zeigt einen Dialog und w&auml;hlt eine Datei zum Speichern aus
	 * 
	 * @return Die Datei die Ausgew&auml;hlt wurde, oder ein leeres Optional wenn
	 *         abgebrochen wurde, oder ein Fehler aufgetereten ist
	 */
	private Optional<File> openFileForSaving() {
		FileChooserSaveDialog<File> fsd = new FileChooserSaveDialog<File>(new WrapperSaveFileChooser<File>(reportName));
		fsd.locale(LPMain.getInstance().getUISprLocale()).addFilters("pdf");
		if (filenameSupplier != null) {
			filenameSupplier.get().ifPresent(fsd::filename);
		}
		try {
			return Optional.ofNullable(fsd.build().selectSingle());
		} catch (Exception e) {
			myLogger.warn("Fehler beim Oeffnen von Datei aus icePDF save Dialog", e);
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.dokumente.fehlerbeimspeichern"));
			return Optional.empty();
		}
	}

	@Override
	public void saveFile() {
		Optional<File> file = openFileForSaving();
		if (!file.isPresent()) {
			return;
		}

		try {
			// Loesche Datei falls schon exisitert
			if (file.get().exists()) {
				file.get().delete();
			}
			OutputStream os = new FileOutputStream(file.get());
			document.writeToOutputStream(os);
		} catch (IOException e) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.dokumente.fehlerbeimspeichern"));
		}
	}
}
