package com.lp.client.frame.filechooser.open;

import javax.swing.JOptionPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.CsvAcceptor;
import com.lp.client.frame.filechooser.filter.HvCsvXlsFileFilter;
import com.lp.client.frame.filechooser.filter.XlsAcceptor;
import com.lp.client.pc.LPMain;

public class CsvXlsFileOpener {

	private XlsFile xlsFile;
	private CsvFile csvFile;
	private FileChooserConfigToken token;
	private InternalFrame internalFrame;

	public CsvXlsFileOpener(InternalFrame internalFrame, FileChooserConfigToken token) {
		this.internalFrame = internalFrame;
		this.token = token;
		reset();
	}

	public void doOpenDialog() {
		HvCsvXlsFileFilter csvXlsFilter = new HvCsvXlsFileFilter();
		WrapperFile chosenFile = FileChooserBuilder.createOpenDialog(token, internalFrame)
				.addFilter(csvXlsFilter).openSingle();
		
		if (chosenFile == null || !chosenFile.hasFile())
			return;
		
		if (new XlsAcceptor().accept(chosenFile.getFile())) {
			xlsFile = new XlsFile(chosenFile.getFile());
			return;
		}
		
		if (new CsvAcceptor().accept(chosenFile.getFile())) {
			csvFile = new CsvFile(chosenFile.getFile());
			return;
		}

		boolean openFileAgain = DialogFactory
				.showModalJaNeinDialog(internalFrame, LPMain.getMessageTextRespectUISPr("lp.datei.open.falscheendung", 
						csvXlsFilter.getDescription(), chosenFile.getFile().getAbsolutePath()), 
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.NO_OPTION);
		reset();
		if (openFileAgain) {
			doOpenDialog();
		} 
	}
	
	private void reset() {
		xlsFile = null;
		csvFile = null;
	}
	
	public boolean isXls() {
		return xlsFile != null;
	}
	
	public XlsFile getXlsFile() {
		return xlsFile;
	}
	
	public boolean isCsv() {
		return csvFile != null;
	}
	
	public CsvFile getCsvFile() {
		return csvFile;
	}

	public boolean hasFileChosen() {
		return isXls() || isCsv();
	}
}
