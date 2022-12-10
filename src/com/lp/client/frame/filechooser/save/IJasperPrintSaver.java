package com.lp.client.frame.filechooser.save;

import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface IJasperPrintSaver extends IDataSaver<JasperPrint> {

	void save(JasperPrint jrPrint, File file) throws JRException;
}
