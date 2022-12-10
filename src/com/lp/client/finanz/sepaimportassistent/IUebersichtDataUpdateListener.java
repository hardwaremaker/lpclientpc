package com.lp.client.finanz.sepaimportassistent;

import com.lp.server.finanz.service.BuchungKompakt;
import com.lp.server.util.BelegZahlungAdapter;

public interface IUebersichtDataUpdateListener {

	public void dataUpdated(BelegZahlungAdapter belegZahlung);
	
	public void dataUpdated(BuchungKompakt buchung);
}
