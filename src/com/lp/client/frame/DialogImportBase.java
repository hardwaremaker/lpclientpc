package com.lp.client.frame;

import java.awt.Frame;

import com.lp.client.frame.component.IFileController;
import com.lp.client.partner.DialogExportBase;
import com.lp.client.pc.LPMain;

public abstract class DialogImportBase extends DialogExportBase {

	private static final long serialVersionUID = 5520408926668535057L;

	public DialogImportBase(Frame owner, String title, boolean modal,
			IFileController fileController) {
		super(owner, title, modal, fileController);
	}

	@Override
	protected String getTextEinDatensatzExportiert() {
		return LPMain.getTextRespectUISPr("dialog.import.singlesaved");
	}
	
	protected String getTextExportImportDurchgefuehrt() {
		return LPMain.getTextRespectUISPr("dialog.import.durchgefuehrt");
	};
	
	@Override
	protected String getTextEinDatensatzGefunden() {
		return LPMain.getTextRespectUISPr("dialog.import.singletotalcounts");
	}
	
	protected String getTextExportImportLaeuft() {
		return LPMain.getTextRespectUISPr("dialog.import.laeuft");
	};
	
	@Override
	protected String getTextImportExportButton() {
		return LPMain.getTextRespectUISPr("dialog.import.importieren");
	}
	
	@Override
	protected String getTextMehrereDatensaetzeExportiert() {
		return LPMain.getMessageTextRespectUISPr("dialog.import.multiplesaved", getTotalCounts());
	}
	
	@Override
	protected String getTextMehrereDatensaetzeGefunden() {
		return LPMain.getMessageTextRespectUISPr("dialog.import.multipletotalcounts", getTotalCounts());
	}
}
