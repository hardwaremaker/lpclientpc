package com.lp.client.frame.filechooser.save;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;

import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.util.HVPDFExporter;

public class JasperPrintPdfSaver implements IJasperPrintSaver {

	public JasperPrintPdfSaver() {
	}

	@Override
	public void save(JasperPrint jrPrint, File file) throws JRException {
		if (!file.getName().toLowerCase().endsWith(".pdf")) {
			file = new File(file.getAbsolutePath() + ".pdf");
		}
		
		if (file.exists()) {
			int option = JOptionPane.showConfirmDialog(null, LPMain.getMessageTextRespectUISPr("lp.datei.existiertueberschreiben", file.getName()), 
					"Speichern", JOptionPane.OK_CANCEL_OPTION);
			if (JOptionPane.OK_OPTION != option)
				return;
		}
		
		HVPDFExporter exporter = new HVPDFExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT,	jrPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE, file);

		try {
			exporter.exportReport();
		} catch (Throwable e) {
			//SP9863
			if(e.getCause() instanceof FileNotFoundException) {
				DialogFactory.showModalDialog(LPMain.getMessageTextRespectUISPr("lp.error"), LPMain.getMessageTextRespectUISPr("lp.datei.konntenichtgespeichertwerden", file.getName()));
			}else {
				throw e;
			}
		}
	}

}
