
package com.lp.client.forecast;

import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.filechooser.open.FileOpenerFactory;
import com.lp.client.frame.filechooser.open.XlsFileOpener;
import com.lp.client.partner.DialogImportAllgemeinXLS;
import com.lp.client.pc.LPMain;

public class DialogForecastpositionenImportVAT_XLS extends DialogImportAllgemeinXLS {
	private static final long serialVersionUID = -831740706319730216L;

	public DialogForecastpositionenImportVAT_XLS(TabbedPane tp, Integer belegIId)
			throws Throwable {
		super(tp, LPMain.getTextRespectUISPr("fc.position.xlsimport"), belegIId, null);

	}

	@Override
	public String pruefe() throws Throwable {
		return DelegateFactory
				.getInstance()
				.getForecastDelegate()
				.pruefeUndImportiereForecastpositionXLS_VAT(getXLSDatei(),
						getBelegId(), false);
	}

	@Override
	public void importiere() throws Throwable {
		DelegateFactory
				.getInstance()
				.getForecastDelegate()
				.pruefeUndImportiereForecastpositionXLS_VAT(getXLSDatei(),
						getBelegId(), true);

	}

	@Override
	protected XlsFileOpener getXlsFileOpener() {
		return FileOpenerFactory.forecastpositionImportOpenXlsVAT(getTp().getInternalFrame());
	}
}
