package com.lp.client.finanz.sepaimportassistent;

import java.util.List;

import com.lp.server.finanz.service.BuchungKompakt;
import com.lp.server.util.BelegZahlungAdapter;

public interface IManuelleAuswahlUebersichtController {

	public List<BelegZahlungAdapter> getBelegZahlungen();
	
	public List<BuchungKompakt> getManuelleBuchungen();
	
	public Integer getBankkontoIId();

	public void actionDeleteBelegZahlung(BelegZahlungAdapter belegZahlungAdapter);

	public void actionDeleteBuchung(BuchungKompakt buchung);

	public void actionEditBelegZahlung(BelegZahlungAdapter belegZahlungAdapter);
	
	public void actionEditBuchung(BuchungKompakt buchung);

	String getWaehrung();
	
}
