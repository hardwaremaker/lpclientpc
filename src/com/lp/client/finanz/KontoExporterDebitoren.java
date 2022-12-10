package com.lp.client.finanz;

import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.system.service.ParameterFac;

public class KontoExporterDebitoren extends KontoExporter {

	public KontoExporterDebitoren() {
	}

	@Override
	protected String getKontotyp() {
		return FinanzServiceFac.KONTOTYP_DEBITOR;
	}

	@Override
	protected String getExportPath() throws Throwable {
		return getParameterWertFinanz(ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_DEBITORENKONTEN);
	}

	@Override
	protected String getMessageTokenKeineKonten() {
		return "fb.export.keinedebitorenkonteneingetragen";
	}

}
