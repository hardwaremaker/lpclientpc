package com.lp.client.frame.filechooser.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.filechooser.save.IDataSaver;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRSaveContributor;

public class HvJasperPrintFileFilter extends FileFilter implements IDataSaver<JasperPrint> {
	
	private JRSaveContributor jrContributor;
	
	public HvJasperPrintFileFilter(JRSaveContributor jrContributor) {
		this.jrContributor = jrContributor;
	}

	@Override
	public boolean accept(File file) {
		return jrContributor.accept(file);
	}

	@Override
	public String getDescription() {
		return jrContributor.getDescription();
	}

	@Override
	public void save(JasperPrint data, File file)  throws Exception {
		jrContributor.save(data, file);
	}

}
