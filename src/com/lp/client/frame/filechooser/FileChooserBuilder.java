package com.lp.client.frame.filechooser;

import net.sf.jasperreports.engine.JasperPrint;

import java.awt.Component;
import java.io.File;

import com.lp.client.pc.LPMain;

public class FileChooserBuilder {

	public static ChooserOpenDialog createOpenDialog() {
		return new FileChooserOpenDialog(new WrapperOpenFileChooser());
	}

	public static ChooserOpenDialog createOpenDialog(Component parent) {
		FileChooserOpenDialog fcod = new FileChooserOpenDialog(new WrapperOpenFileChooser());
		fcod.getWrapperFileChooser().setParentComponent(parent);
		return fcod;
	}

	public static ChooserOpenDialog createOpenDialog(FileChooserConfigToken configToken) {
		return new FileChooserOpenDialog(new WrapperOpenFileChooser(configToken.asToken()));
	}

	public static ChooserOpenDialog createOpenDialog(FileChooserConfigToken configToken, Component parent) {
		FileChooserOpenDialog fcod = new FileChooserOpenDialog(new WrapperOpenFileChooser(configToken.asToken()));
		fcod.getWrapperFileChooser().setParentComponent(parent);
		return fcod;
	}

	public static ChooserSaveDialog<byte[]> createByteArraySaveDialog() {
		return new FileChooserSaveDialog<byte[]>(new WrapperSaveFileChooser<byte[]>());
	}

	public static ChooserSaveDialog<byte[]> createByteArraySaveDialog(String token) {
		return new FileChooserSaveDialog<byte[]>(new WrapperSaveFileChooser<byte[]>(token));
	}
	
	public static ChooserSaveDialog<Object[][]> createSaveDialog() {
		return new FileChooserSaveDialog<Object[][]>(new WrapperSaveFileChooser<Object[][]>());
	}

	public static ChooserSaveDialog<File> createFileSaveDialog() {
		return new FileChooserSaveDialog<File>(new WrapperSaveFileChooser<File>());
	}
	
	public static ChooserSaveDialog<JasperPrint> createJasperPrintSaveDialog(String token) {
		return new FileChooserSaveDialog<JasperPrint>(new WrapperSaveFileChooser<JasperPrint>("report_" + token));
	}
	
	public static ChooserSaveDialogJasper createReportSaveDialog(String reportname) {
		return new FileChooserSaveDialogJasper(
				new WrapperSaveFileChooser<JasperPrint>(reportname), 
				LPMain.getInstance().getUISprLocale());
	}

	public static ChooserSaveDialogJasper createReportSaveDialog(String reportname, Component parent) {
		FileChooserSaveDialogJasper fcsd = new FileChooserSaveDialogJasper(
				new WrapperSaveFileChooser<JasperPrint>(reportname), 
				LPMain.getInstance().getUISprLocale());
		fcsd.getWrapperFileChooser().setParentComponent(parent);
		return fcsd;
	}

}
