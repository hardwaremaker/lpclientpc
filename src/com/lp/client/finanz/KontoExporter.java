package com.lp.client.finanz;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

public abstract class KontoExporter {

	public KontoExporter() {
	}

	protected abstract String getKontotyp();
	protected abstract String getExportPath() throws Throwable;
	protected abstract String getMessageTokenKeineKonten();
	
	public String exportKonten(boolean nurVerwendete) throws Throwable {
		return exportKontenImpl(nurVerwendete);
	}
	
	public void exportAndSaveKonten(InternalFrame internalFrame, boolean nurVerwendete) throws Throwable {
		String data = exportKontenImpl(nurVerwendete);
		if (data == null) return;
		
		saveKonten(data, internalFrame);
	}
	
	private void saveKonten(String data, InternalFrame internalFrame) throws Throwable {
		LPMain.getInstance().saveFile(internalFrame,
				getExportPath(), data.getBytes("Cp1252"), false);
		DialogFactory.showModalDialog(
				LPMain.getTextRespectUISPr("lp.hint"),
				LPMain.getTextRespectUISPr("fb.export.datensichern"));
	}
	
	private String exportKontenImpl(boolean nurVerwendete) throws Throwable {
		String sDaten = DelegateFactory.getInstance().getFibuExportDelegate()
				.exportierePersonenkonten(getKontotyp(), nurVerwendete);
		if (sDaten != null) return sDaten;
		
		DialogFactory.showModalDialog(
				LPMain.getTextRespectUISPr("lp.hint"),
				LPMain.getTextRespectUISPr(getMessageTokenKeineKonten()));
		return null;
	}
	
	private ParametermandantDto getParametermandantFinanz(String parameter) throws Throwable {
		return DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FINANZ, parameter);
	}
	
	protected String getParameterWertFinanz(String parameter) throws Throwable {
		ParametermandantDto paramDto = getParametermandantFinanz(parameter);
		return paramDto != null ? paramDto.getCWert() : null;
	}
}
