package com.lp.client.forecast;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.forecast.service.CallOffXlsImporterResult;

public interface ICallOffXlsImportController extends IImportController<CallOffXlsImporterResult, byte[]> {

	void setFclieferadresseIId(Integer forecastIId) throws ExceptionLP, Throwable;
	
	void setStartRow(Integer startRow);
	
	String getDialogTitle();

	String getFclieferadresseAsText();

	Integer getStartRow();
}
