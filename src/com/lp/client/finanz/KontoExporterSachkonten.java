package com.lp.client.finanz;

import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.system.service.ParameterFac;

public class KontoExporterSachkonten extends KontoExporter {

	public KontoExporterSachkonten() {
	}

	@Override
	protected String getKontotyp() {
		return FinanzServiceFac.KONTOTYP_SACHKONTO;
	}

	@Override
	protected String getExportPath() throws Throwable {
		return getParameterWertFinanz(ParameterFac.PARAMETER_FINANZ_EXPORTZIEL_SACHKONTEN);
	}

	@Override
	protected String getMessageTokenKeineKonten() {
		return "fb.export.keinesachkonteneingetragen";
	}

}
