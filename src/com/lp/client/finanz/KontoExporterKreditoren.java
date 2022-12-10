package com.lp.client.finanz;

import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.system.service.ParameterFac;

public class KontoExporterKreditoren extends KontoExporter {

	public KontoExporterKreditoren() {
	}

	@Override
	protected String getKontotyp() {
		return FinanzServiceFac.KONTOTYP_KREDITOR;
	}

	@Override
	protected String getExportPath() throws Throwable {
		return getParameterWertFinanz(ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_KREDITORENKONTEN);
	}

	@Override
	protected String getMessageTokenKeineKonten() {
		return "fb.export.keinekreditorenkonteneingetragen";
	}

}
