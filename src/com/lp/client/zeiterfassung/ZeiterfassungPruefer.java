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
package com.lp.client.zeiterfassung;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

/**
 * Dient zur &Uuml;berpr&uuml;fung von Belangen der Zeitbuchungen
 * 
 * @author andi
 *
 */
public class ZeiterfassungPruefer {

	private static final int BUCHUNG_MOEGLICH = 1;
	private static final int BUCHUNG_NICHT_MOEGLICH_KEIN_CHEFBUCHHALTER = 2;
	private static final int BUCHUNG_NICHT_MOEGLICH_IST_CHEFBUCHHALTER = 3;
	
	/**
	 * InternalFrame des Besitzers, zum Anzeigen von Infos/Warnungen
	 */
	private InternalFrame internalFrame = null;
	private Date aenderungenStichtag = null;
	
	public ZeiterfassungPruefer(InternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}

	/**
	 * &Uuml;berpr&uuml;ft die Buchungszeit auf ihre G&uuml;ltigkeit unter
	 * Ber&uuml;cksichtigung des Parameters
	 * {@link ParameterFac#ZEITBUCHUNGEN_NACHTRAEGLICH_BUCHEN_BIS}.
	 * 
	 * @param buchungszeit die zu pr&uuml;fende Buchungszeit
	 * 
	 * @return {@value #BUCHUNG_MOEGLICH} wenn die Buchungszeit g&uuml;ltig ist, 
	 * {@value #BUCHUNG_NICHT_MOEGLICH_KEIN_CHEFBUCHHALTER} wenn Buchung nicht
	 * m&ouml;glich ist, {@value #BUCHUNG_NICHT_MOEGLICH_IST_CHEFBUCHHALTER} wenn 
	 * Buchung nicht m&ouml;glich ist und der Benutzer das Recht 
	 * {@link RechteFac#RECHT_FB_CHEFBUCHHALTER} besitzt
	 */
	private int pruefeBuchungsdatumUeberParameter(Timestamp buchungszeit) 
			throws ExceptionLP, Throwable {

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.ZEITBUCHUNGEN_NACHTRAEGLICH_BUCHEN_BIS,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		int iTag = (Integer) parameter.getCWertAsObject();
		if(iTag == -1) {
			//keine Einschr&auml;nkung
			return BUCHUNG_MOEGLICH;
		}
		
		Timestamp aktuellerTimestamp = DelegateFactory.getInstance()
				.getSystemDelegate().getServerTimestamp();

		Calendar cAktuelleZeit = Calendar.getInstance();
		cAktuelleZeit.setTimeInMillis(aktuellerTimestamp.getTime());

		Calendar calBisDahinDarfGeaendertWerden = Calendar.getInstance();
		calBisDahinDarfGeaendertWerden.setTimeInMillis(Helper.cutTimestamp(
				aktuellerTimestamp).getTime());
		
		if(iTag < 0) {
			calBisDahinDarfGeaendertWerden.add(Calendar.DATE, iTag);
		} else {
			// Im aktuelle Monat darf geaendert werden
			calBisDahinDarfGeaendertWerden.set(Calendar.DAY_OF_MONTH, 1);
	
			if (cAktuelleZeit.get(Calendar.DAY_OF_MONTH) <= iTag) {
				// Im Vormonat darf geaendert werden
				calBisDahinDarfGeaendertWerden.set(Calendar.MONTH,
						calBisDahinDarfGeaendertWerden.get(Calendar.MONTH) - 1);
			}
		}

		aenderungenStichtag = calBisDahinDarfGeaendertWerden.getTime();
		
		if (calBisDahinDarfGeaendertWerden.getTimeInMillis() > buchungszeit.getTime()) {
			boolean bRechtChefbuchhalter = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER);

			if (bRechtChefbuchhalter) {
				return BUCHUNG_NICHT_MOEGLICH_IST_CHEFBUCHHALTER;
			} else {
				return BUCHUNG_NICHT_MOEGLICH_KEIN_CHEFBUCHHALTER;
			}
		}
		
		return BUCHUNG_MOEGLICH;
	}
	
	/**
	 * &Uuml;berpr&uuml;ft die Buchungszeit einer Zeitbuchung auf ihre G&uuml;ltigkeit.
	 * Ohne Ber&uuml;cksichtigung, ob Buchung in abgeschlossene Zeiten fallen.
	 * 
	 * @param buchungszeit die zu pr&uuml;fende Buchungszeit
	 * @return true wenn die Buchung mit der &uuml;bergebenen Zeit durchgef&uuml;hrt
	 * werden kann
	 * @throws ExceptionLP
	 * @throws Throwable
	 */
	public boolean pruefeObBuchungMoeglich(Timestamp buchungszeit) 
			throws ExceptionLP, Throwable {
		
		return pruefeObBuchungMoeglich(buchungszeit, null);
	}
	
	/**
	 * &Uuml;berpr&uuml;ft die Buchungszeit einer Zeitbuchung auf ihre G&uuml;ltigkeit.
	 * Ber&uuml;cksichtigt auch, ob Buchung in abgeschlossene Zeiten fallen.
	 * 
	 * @param buchungszeit die zu pr&uuml;fende Buchungszeit
	 * @param personalIId Id, der betroffenen Person
	 * @return true wenn die Buchung mit der &uuml;bergebenen Zeit durchgef&uuml;hrt
	 * werden kann
	 */
	public boolean pruefeObBuchungMoeglich(Timestamp buchungszeit, Integer personalIId) 
			throws ExceptionLP, Throwable {
		
		if(buchungszeit == null) {
			return true;
		}
		
		if(personalIId != null) {
			if(!pruefeAufAbgeschlosseneZeiten(buchungszeit, personalIId)) {
				return false;
			}
		}

		int pruefungsergebnis = pruefeBuchungsdatumUeberParameter(buchungszeit);

		if (pruefungsergebnis == BUCHUNG_NICHT_MOEGLICH_IST_CHEFBUCHHALTER) {
			// Warnung anzeigen
			String sMsg = createMessage(
					LPMain.getTextRespectUISPr("pers.error.zeitbuchungenduerfenichtmehrgeaendertwerden.trotzdem"),
					new Object[] { Helper.formatDatum(aenderungenStichtag, 
							LPMain.getTheClient().getLocUi()) });

			boolean b = DialogFactory.showModalJaNeinDialog(
					internalFrame, sMsg,
					LPMain.getTextRespectUISPr("lp.warning"));
			if (b == false) {
				return false;
			}

		} else if(pruefungsergebnis == BUCHUNG_NICHT_MOEGLICH_KEIN_CHEFBUCHHALTER){
			// Fehler anzeigen
			String sMsg = createMessage(
					LPMain.getTextRespectUISPr("pers.error.zeitbuchungenduerfenichtmehrgeaendertwerden"),
					new Object[] { Helper.formatDatum(aenderungenStichtag, 
							LPMain.getTheClient().getLocUi()) });

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"), sMsg);

			return false;
		}
		
		return true;
	}

	/**
	 * Erstellt eine Nachricht mit &uuml;bergebenen Text und Pattern
	 * 
	 * @param text Text der Nachricht
	 * @param pattern 
	 * @return
	 * @throws Throwable
	 */
	private String createMessage(String text, Object[] pattern) throws Throwable {
		MessageFormat mf = new MessageFormat(text);
		mf.setLocale(LPMain.getTheClient().getLocUi());
		String sMsg = mf.format(pattern);
		return sMsg;
	}

	/**
	 * Pr&uuml;ft die Buchungszeit darauf, ob sie in einen bereits abgeschlossenen
	 * Zeitraum f&auml;llt.
	 * 
	 * @param buchungszeit zu pr&uuml;fende Buchungszeit
	 * @param personalIId Id der betroffenen Person
	 * @return true, wenn die Buchungszeit nicht in einen abgeschlossenen Zeitraum
	 * f&auml;llt
	 */
	private boolean pruefeAufAbgeschlosseneZeiten(Timestamp buchungszeit,
			Integer personalIId) throws ExceptionLP, Throwable {
		// SP3285
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZEITEN_ABSCHLIESSEN)
				&& buchungszeit != null && personalIId != null) {

			java.sql.Timestamp t = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.gibtEsBereitseinenZeitabschlussBisZurKW(
							personalIId, buchungszeit);

			if (t != null) {
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(t.getTime());

				String sMsg = createMessage(
						LPMain.getTextRespectUISPr("pers.zeiterfassung.zeitenbereitsabgeschlossen.bis"), 
						new Object[] { c.get(Calendar.WEEK_OF_YEAR) });

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.error"), sMsg);
				return false;
			}

		}
		return true;
	}

}
