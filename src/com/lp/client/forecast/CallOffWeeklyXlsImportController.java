package com.lp.client.forecast;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.forecast.service.CallOffXlsImporterResult;

public class CallOffWeeklyXlsImportController extends CallOffDailyXlsImportController {

	public CallOffWeeklyXlsImportController() {
		setImportCaller(new ICallOffXlsImportCaller() {
			
			@Override
			public CallOffXlsImporterResult importCallOffXls(byte[] xlsDatei,
					Integer fclieferadresseIId, boolean checkOnly, Integer startRow)
					throws Throwable {
				return DelegateFactory.getInstance().getForecastDelegate().importCallOffWeeklyXls(xlsDatei, fclieferadresseIId, checkOnly, startRow);
			}
		});
	}

	@Override
	public String getDialogTitle() {
		return LPMain.getTextRespectUISPr("fc.menu.cowxlsimport");
	}
}
