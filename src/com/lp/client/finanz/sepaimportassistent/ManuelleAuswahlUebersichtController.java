package com.lp.client.finanz.sepaimportassistent;

import java.util.List;

import com.lp.server.finanz.service.BuchungKompakt;
import com.lp.server.util.BelegZahlungAdapter;

public class ManuelleAuswahlUebersichtController implements IManuelleAuswahlUebersichtController {
	
	private IManuelleAuswahlController controller;
	private INochVerfuegbarerZahlbetragUpdater zahlbetragUpdater;
	
	public ManuelleAuswahlUebersichtController(IManuelleAuswahlController controller, INochVerfuegbarerZahlbetragUpdater zahlbetragUpdater) {
		this.controller = controller;
		this.zahlbetragUpdater = zahlbetragUpdater;
	}

	@Override
	public List<BelegZahlungAdapter> getBelegZahlungen() {
		return controller.getResultWaitingForManuelleAuswahl().getManualPayments();
	}

	@Override
	public void actionDeleteBelegZahlung(BelegZahlungAdapter belegZahlungAdapter) {
		controller.getResultWaitingForManuelleAuswahl().getManualPayments().remove(belegZahlungAdapter);
		zahlbetragUpdater.dataChanged();
	}

	@Override
	public List<BuchungKompakt> getManuelleBuchungen() {
		return controller.getResultWaitingForManuelleAuswahl().getManualBookings();
	}

	@Override
	public void actionDeleteBuchung(BuchungKompakt buchung) {
		controller.getResultWaitingForManuelleAuswahl().getManualBookings().remove(buchung);
		zahlbetragUpdater.dataChanged();
	}

	@Override
	public void actionEditBelegZahlung(BelegZahlungAdapter belegZahlungAdapter) {
		zahlbetragUpdater.actionEdit(belegZahlungAdapter);
	}

	@Override
	public void actionEditBuchung(BuchungKompakt buchung) {
		zahlbetragUpdater.actionEdit(buchung);
	}

	@Override
	public Integer getBankkontoIId() {
		return controller.getBankkontoIId();
	}
	
	@Override
	public String getWaehrung() {
		return controller.getWaehrung();
	}

}
