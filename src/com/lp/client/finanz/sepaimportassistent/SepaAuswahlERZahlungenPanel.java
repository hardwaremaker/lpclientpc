/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.finanz.sepaimportassistent;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungAdapter;
import com.lp.server.finanz.service.SepaBetrag;
import com.lp.server.finanz.service.SepaHabenBetrag;
import com.lp.server.util.BelegAdapter;
import com.lp.server.util.BelegZahlungAdapter;

public class SepaAuswahlERZahlungenPanel extends SepaAuswahlZahlungenPanel {

	private static final long serialVersionUID = 1L;

	public SepaAuswahlERZahlungenPanel(TabbedPaneBelegZahlung tb) {
		super(tb);
		
		belegAdapterComparator = new Comparator<BelegAdapter>() {
			@Override
			public int compare(BelegAdapter beleg1, BelegAdapter beleg2) {
				// Zusatzkosten ganz nach unten in die Liste reihen
				if (EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN.equals(beleg1.getRechnungsartCNr()) &&
					!EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN.equals(beleg2.getRechnungsartCNr())) {
					return 1;
				} else if (!EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN.equals(beleg1.getRechnungsartCNr()) &&
						EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN.equals(beleg2.getRechnungsartCNr())) {
					return -1;
				}

				return beleg2.getCNr().compareTo(beleg1.getCNr());
			}
		};
	}

	@Override
	protected void updateTableData() throws ExceptionLP {
		setBelege(DelegateFactory.getInstance()
				.getFinanzDelegate().getAlleOffenenEingangsrechnungen(getTabbedPane().getController().getWaehrung()));
		
		Collections.sort(getBelege(), belegAdapterComparator);
		setBelegePartnerMap(DelegateFactory.getInstance()
				.getFinanzDelegate().getPartnerOfBelegeMap(getBelege()));
	}

	@Override
	protected String getActionCommandAddOn() {
		return TabbedPaneBelegZahlung.ACTION_ADD_ON_ER;
	}

	@Override
	protected String getPartnerTableHead() {
		return LPMain.getTextRespectUISPr("fb.sepa.import.tab.head.lieferant");
	}

	@Override
	protected BelegZahlungAdapter getBelegZahlung(Integer iId,
			List<BelegZahlungAdapter> belegZahlungen) {
		if (iId == null || belegZahlungen == null) return null;
		
		for (BelegZahlungAdapter belegZahlung : belegZahlungen) {
			if (iId.equals(belegZahlung.getRechnungIId())) {
				return belegZahlung;
			}
		}
		return null;
	}

	@Override
	protected BelegZahlungAdapter getNewBelegZahlungAdapter() {
		return new EingangsrechnungzahlungAdapter();
	}

	@Override
	protected SepaBetrag getBetragAsSepaBetrag(BigDecimal bdBetrag) {
		return new SepaHabenBetrag(bdBetrag);
	}

	@Override
	protected String getTextNummer() {
		return LPMain.getTextRespectUISPr("lp.ernummer");
	}

}
