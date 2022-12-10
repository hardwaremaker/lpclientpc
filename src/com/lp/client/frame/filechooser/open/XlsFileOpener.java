package com.lp.client.frame.filechooser.open;

import java.awt.Component;
import java.io.File;

import javax.swing.JOptionPane;

import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.XlsAcceptor;
import com.lp.client.pc.LPMain;
import com.lp.server.util.HvOptional;

public class XlsFileOpener {

	private XlsFile xlsFile;
	private final Component parent;
	private final FileChooserConfigToken token;
	private final XlsAcceptor xlsAcceptor;
	
	public XlsFileOpener(Component parent, FileChooserConfigToken token) {
		this.parent = parent;
		this.token = token;
		xlsAcceptor = new XlsAcceptor();
	}

	public void doOpenDialog() {
		xlsFile = FileChooserBuilder.createOpenDialog(token, parent).addXlsFilter().openSingle();
		
		if (!hasFileChosen())
			return;
		
		if (xlsFile.getFile().exists() && xlsFile.getFile().isFile() && xlsAcceptor.accept(xlsFile.getFile()))
			return;
	
		boolean openFileAgain = false;
		if(!xlsAcceptor.accept(xlsFile.getFile())) {
			openFileAgain = DialogFactory
				.showModalJaNeinDialog(parent, LPMain.getMessageTextRespectUISPr("lp.datei.open.falscheendung", 
						xlsAcceptor.extensionsAsString(), xlsFile.getFile().getAbsolutePath()), 
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.NO_OPTION);
		
		} else {
			openFileAgain = DialogFactory
				.showModalJaNeinDialog(parent, LPMain.getMessageTextRespectUISPr("lp.datei.open.nichtvorhanden",
						xlsFile.getFile().getAbsolutePath()),
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.NO_OPTION);
		}
		
		xlsFile = null;
		
		if (openFileAgain) {
			doOpenDialog();
		} 
	}
	
	/**
	 * Liefert die vom Anwender gew&auml;hlte Datei. 
	 * Es kann nur eine Datei ausgew&auml;hlt werden.</br>
	 * <p>Hat der Anwender keine Datei gew&auml;hlt (Auswahl
	 * unterbrochen) wird sofort beendet.</p>
	 * <p>Wurde eine Datei gew&auml;hlt, wird gepr&uuml;ft,
	 * ob diese Datei existiert und die DateiExtension 
	 * jener entspricht, die erwartet wird. Falls eine 
	 * dieser Bedingungen nicht zutrifft, wird der Anwender 
	 * darauf hingewiesen und die Auswahl kann nochmals
	 * durchgef&uuml;hrt werden.</p>
	 * 
	 * @return HvOptional.empty wenn der Anwender keine 
	 * Datei gew&auml;hlt oder die Wahl unterbrochen hat. 
	 * Ansonsten HvOptional<Datei>
	 */
	public HvOptional<XlsFile> selectSingle() { 
		for(;;) {
			xlsFile = FileChooserBuilder.createOpenDialog(token, parent)
					.addXlsFilter().openSingle();

			if (xlsFile == null || !xlsFile.hasFile()) {
				return HvOptional.empty();
			}
			
			if (isAcceptable(xlsFile)) {
				return HvOptional.of(xlsFile);
			}
			
			if (!shouldTryAgain(xlsFile)) {
				return HvOptional.empty();
			}
		}		
	}
	
	private boolean isAcceptable(XlsFile xlsFile) {		
		File f = xlsFile.getFile();
		return f.exists() && f.isFile() && xlsAcceptor.accept(f);
	}
	
	private boolean shouldTryAgain(XlsFile xlsFile) {
		boolean openFileAgain = false;
		if(!xlsAcceptor.accept(xlsFile.getFile())) {
			openFileAgain = DialogFactory
				.showModalJaNeinDialog(parent, LPMain.getMessageTextRespectUISPr("lp.datei.open.falscheendung", 
						xlsAcceptor.extensionsAsString(), xlsFile.getFile().getAbsolutePath()), 
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.NO_OPTION);
		
		} else {
			openFileAgain = DialogFactory
				.showModalJaNeinDialog(parent, LPMain.getMessageTextRespectUISPr("lp.datei.open.nichtvorhanden",
						xlsFile.getFile().getAbsolutePath()),
						LPMain.getTextRespectUISPr("lp.frage"),
						JOptionPane.INFORMATION_MESSAGE,
						JOptionPane.NO_OPTION);
		}

		return openFileAgain;
	}
	
	public XlsFile getFile() {
		return xlsFile;
	}
	
	public boolean hasFileChosen() {
		return getFile() != null && getFile().hasFile();
	}
}
