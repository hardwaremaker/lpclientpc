package com.lp.client.finanz.sepaimportassistent;

import java.sql.Date;

import com.lp.server.finanz.service.ISepaImportResult;
import com.lp.server.finanz.service.Iso20022StandardEnum;

public interface IManuelleAuswahlController {

	public ISepaImportResult getResultWaitingForManuelleAuswahl();
	
	public Date getZahldatum();
	
	public Integer getBankkontoIId();
	
	public Integer getAuszugsnummer();
	
	public boolean hasFibu();
	
	public String getInfoBuchungszeile(ISepaImportResult result);

	public String getWaehrung();
	
	public Iso20022StandardEnum getIso20022Standard();
}
