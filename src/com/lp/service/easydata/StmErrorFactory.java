package com.lp.service.easydata;

import com.lp.server.lieferschein.service.errors.StmAnsprechpartnerNichtEingetragenExc;
import com.lp.server.lieferschein.service.errors.StmArtikelMwstSatzExc;
import com.lp.server.lieferschein.service.errors.StmException;
import com.lp.server.lieferschein.service.errors.StmMandantKeinKundeExc;
import com.lp.server.lieferschein.service.errors.StmPflichtfeldLeerExc;
import com.lp.server.lieferschein.service.errors.StmTransformXmlExc;
import com.lp.server.lieferschein.service.errors.StmUnbekannteEanExc;
import com.lp.server.lieferschein.service.errors.StmUnbekanntePersonalnummerExc;
import com.lp.server.lieferschein.service.errors.StmUnbekanntesLagerExc;

public class StmErrorFactory {

	public IEasydataErrorAction getEasydataErrorAction(StmException exception) {
		if (exception instanceof StmAnsprechpartnerNichtEingetragenExc) {
			return new StmErrorAnsprechpartnerNichtEingetragen((StmAnsprechpartnerNichtEingetragenExc)exception);
		}
		if (exception instanceof StmTransformXmlExc) {
			return new StmErrorTransformXml((StmTransformXmlExc)exception);
		}
		if (exception instanceof StmUnbekannteEanExc) {
			return new StmErrorUnbekannteEan((StmUnbekannteEanExc)exception);
		}
		if (exception instanceof StmUnbekanntePersonalnummerExc) {
			return new StmErrorUnbekanntePersonalnummer((StmUnbekanntePersonalnummerExc)exception);
		}
		if (exception instanceof StmUnbekanntesLagerExc) {
			return new StmErrorUnbekanntesLager((StmUnbekanntesLagerExc)exception);
		}
		if (exception instanceof StmMandantKeinKundeExc) {
			return new StmErrorMandantKeinKunde((StmMandantKeinKundeExc)exception);
		}
		if (exception instanceof StmArtikelMwstSatzExc) {
			return new StmErrorArtikelOhneMwstSatz((StmArtikelMwstSatzExc)exception);
		}
		if (exception instanceof StmPflichtfeldLeerExc) {
			return new StmErrorLeeresPflichtfeld((StmPflichtfeldLeerExc)exception);
		}
		
		return new StmErrorDefault(exception);
	}
}
