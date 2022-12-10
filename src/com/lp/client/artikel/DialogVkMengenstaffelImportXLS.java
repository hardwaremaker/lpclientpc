package com.lp.client.artikel;


import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XlsFileOpener;
import com.lp.client.partner.DialogImportAllgemeinXLS;
import com.lp.client.pc.LPMain;

public class DialogVkMengenstaffelImportXLS extends DialogImportAllgemeinXLS {
	private static final long serialVersionUID = 993151338334779348L;

	public DialogVkMengenstaffelImportXLS(TabbedPane tp)
			throws Throwable {
		super(tp, LPMain.getTextRespectUISPr("artikel.vkmengenstaffeln.import"), null,null);

	}

	@Override
	public String pruefe() throws Throwable {
		return DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.importiereVKMengenstgaffelXLS(getXLSDatei(),null, false);
	}

	@Override
	public void importiere() throws Throwable {
		DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.importiereVKMengenstgaffelXLS(getXLSDatei(),getTp().getInternalFrame(), true);

	}

	@Override
	protected XlsFileOpener getXlsFileOpener() {
		return FileOpenerFactory.vkMengenstaffelImportOpenXls(getTp().getInternalFrame());
	}
}
