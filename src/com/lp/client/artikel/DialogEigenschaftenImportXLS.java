package com.lp.client.artikel;

import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XlsFileOpener;
import com.lp.client.partner.DialogImportAllgemeinXLS;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.PanelFac;

public class DialogEigenschaftenImportXLS extends DialogImportAllgemeinXLS {
	private static final long serialVersionUID = -7875401800893411804L;

	public DialogEigenschaftenImportXLS(TabbedPane tp, String panelCNr) throws Throwable {
		super(tp, LPMain.getTextRespectUISPr("fc.position.xlsimport"), null,panelCNr);

	}

	@Override
	public String pruefe() throws Throwable {

		return DelegateFactory
				.getInstance()
				.getPanelDelegate()
				.pruefUndImportierePaneldaten(panelCNr, getXLSDatei(),
						false);
	}

	@Override
	public void importiere() throws Throwable {
		DelegateFactory
				.getInstance()
				.getPanelDelegate()
				.pruefUndImportierePaneldaten(panelCNr, getXLSDatei(),
						true);

	}

	@Override
	protected XlsFileOpener getXlsFileOpener() {
		return FileOpenerFactory.artikelEigenschaftenImportOpenXls(getTp().getInternalFrame());
	}
}
