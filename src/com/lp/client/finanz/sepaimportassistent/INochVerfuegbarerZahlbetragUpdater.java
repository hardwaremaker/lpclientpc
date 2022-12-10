package com.lp.client.finanz.sepaimportassistent;

import com.lp.server.finanz.service.BuchungKompakt;
import com.lp.server.util.BelegZahlungAdapter;

public interface INochVerfuegbarerZahlbetragUpdater {

	public void dataChanged();

	public void actionEdit(BelegZahlungAdapter belegZahlungAdapter);
	
	public void actionEdit(BuchungKompakt buchung);
}
