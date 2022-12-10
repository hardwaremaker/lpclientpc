package com.lp.client.forecast;

import java.sql.Date;
import java.util.List;

import com.lp.server.forecast.service.CallOffXlsImporterResult;

public interface ILinienabrufEluxDeImportController extends IImportController<CallOffXlsImporterResult, List<String>> {

	void setDeliveryDate(Date deliveryDate);

	String getFclieferadresseAsText();
}
