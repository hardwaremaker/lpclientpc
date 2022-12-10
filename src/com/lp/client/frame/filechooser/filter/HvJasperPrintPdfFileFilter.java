package com.lp.client.frame.filechooser.filter;

import java.io.File;
import java.util.Locale;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.save.JRPdfSaveContributor;

import com.lp.client.frame.filechooser.save.JasperPrintPdfSaver;

public class HvJasperPrintPdfFileFilter extends HvJasperPrintFileFilter {
	
	private JasperPrintPdfSaver pdfSaver;
	
	public HvJasperPrintPdfFileFilter(Locale locale) {
		super(new JRPdfSaveContributor(locale, null));
	}

	public JasperPrintPdfSaver getPdfSaver() {
		if (pdfSaver == null) {
			pdfSaver = new JasperPrintPdfSaver();
		}
		return pdfSaver;
	}
	
	@Override
	public void save(JasperPrint data, File file) throws Exception {
		getPdfSaver().save(data, file);
	}
}
