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
package com.lp.client.frame.delegate;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungServiceFac;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.RueckgabeUeberleitungDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

public class BestellvorschlagDelegate extends Delegate {
	private Context context;
	private BestellvorschlagFac bestellvorschlagFac;

	public BestellvorschlagDelegate() throws Throwable {
		context = new InitialContext();
		bestellvorschlagFac = lookupFac(context, BestellvorschlagFac.class);

	}

	public String getKEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG() throws ExceptionLP {
		try {
			return bestellvorschlagFac.getKEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Integer createBestellvorschlag(BestellvorschlagDto bestellvorschlagDto) throws ExceptionLP {
		Integer iIdBestellvorschlag = null;
		try {
			iIdBestellvorschlag = bestellvorschlagFac.createBestellvorschlag(bestellvorschlagDto,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iIdBestellvorschlag;
	}

	public void removeBestellvorschlag(Integer iId) throws ExceptionLP {
		try {
			bestellvorschlagFac.removeBestellvorschlag(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeBestellvorschlag(BestellvorschlagDto bestellvorschlagDto) throws ExceptionLP {
		try {
			bestellvorschlagFac.removeBestellvorschlag(bestellvorschlagDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateBestellvorschlag(BestellvorschlagDto bestellvorschlagDto) throws ExceptionLP {
		try {
			bestellvorschlagFac.updateBestellvorschlag(bestellvorschlagDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public void updateBestellvorschlags(BestellvorschlagDto[] bestellvorschlagDtos) throws ExceptionLP {
		try {
			bestellvorschlagFac.updateBestellvorschlags(bestellvorschlagDtos, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	public BestellvorschlagDto bestellvorschlagFindByPrimaryKey(Integer iId) throws ExceptionLP {
		BestellvorschlagDto besvosDto = null;
		try {
			besvosDto = bestellvorschlagFac.bestellvorschlagFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return besvosDto;
	}

	/**
	 * leitet Bestellvorschlaege in Bestellungen ueber
	 * 
	 * @param fk              FilterKriterium
	 * @param ski             String
	 * @param kostenstelleIId KostenstelleDto
	 * @return Boolean
	 * @throws ExceptionLP
	 */
	public RueckgabeUeberleitungDto createBESausBVjeLieferant(FilterKriterium[] fk, SortierKriterium[] ski,
			Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen, boolean gemeinsameArtikelBestellen,
			Integer standortIId, boolean bRahmenbestellungErzeugen, boolean bInklGesperrteArtikel) throws ExceptionLP {
		RueckgabeUeberleitungDto ok = null;

		try {
			ok = bestellvorschlagFac.createBESausBVjeLieferant(fk, ski, LPMain.getTheClient(), kostenstelleIId,
					bProjektklammerberuecksichtigen, gemeinsameArtikelBestellen, standortIId,
					bRahmenbestellungErzeugen, bInklGesperrteArtikel);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return ok;
	}

	/**
	 * leitet Bestellvorschlaege in Bestellungen ueber
	 * 
	 * @param fk              FilterKriterium
	 * @param ski             String
	 * @param kostenstelleIId kostenstelleIId
	 * @return Boolean
	 * @throws ExceptionLP
	 */
	public RueckgabeUeberleitungDto createBESausBVfueBestimmtenLieferant(FilterKriterium[] fk, SortierKriterium[] ski,
			Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen, boolean gemeinsameArtikelBestellen,
			Integer standortIId, boolean bRahmenbestellungErzeugen, boolean bInklGesperrteArtikel) throws ExceptionLP {
		RueckgabeUeberleitungDto ok = null;

		try {
			ok = bestellvorschlagFac.createBESausBVfueBestimmtenLieferant(fk, ski, LPMain.getTheClient(),
					kostenstelleIId, bProjektklammerberuecksichtigen, gemeinsameArtikelBestellen, standortIId,
					bRahmenbestellungErzeugen, bInklGesperrteArtikel);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return ok;
	}

	/**
	 * leitet Bestellvorschlaege in Bestellungen ueber
	 * 
	 * @param fk              FilterKriterium
	 * @param ski             String
	 * @param kostenstelleIId kostenstelleIId
	 * @return Boolean
	 * @throws ExceptionLP
	 */
	public RueckgabeUeberleitungDto createBESausBVfuerBestimmtenLieferantUndTermin(FilterKriterium[] fk,
			SortierKriterium[] ski, Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen,
			boolean gemeinsameArtikelBestellen, Integer standortIId, boolean bRahmenbestellungErzeugen, boolean bInklGesperrteArtikel)
			throws ExceptionLP {
		RueckgabeUeberleitungDto ok = null;

		try {
			ok = bestellvorschlagFac.createBESausBVfuerBestimmtenLieferantUndTermin(fk, ski, LPMain.getTheClient(),
					kostenstelleIId, bProjektklammerberuecksichtigen, gemeinsameArtikelBestellen, standortIId,
					bRahmenbestellungErzeugen, bInklGesperrteArtikel);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return ok;
	}

	/**
	 * leitet Bestellvorschlaege in Bestellungen ueber
	 * 
	 * @param fk              FilterKriterium
	 * @param ski             String
	 * @param kostenstelleIId kostenstelleIId
	 * @return Boolean
	 * @throws ExceptionLP
	 */
	public RueckgabeUeberleitungDto createBESausBVfuerAlleLieferantenMitGleichenTermin(FilterKriterium[] fk,
			SortierKriterium[] ski, Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen,
			boolean gemeinsameArtikelBestellen, Integer standortIId, boolean bRahmenbestellungErzeugen, boolean bInklGesperrteArtikel)
			throws ExceptionLP {
		RueckgabeUeberleitungDto ok = null;

		try {
			ok = bestellvorschlagFac.createBESausBVfuerAlleLieferantenMitGleichenTermin(fk, ski, LPMain.getTheClient(),
					kostenstelleIId, bProjektklammerberuecksichtigen, gemeinsameArtikelBestellen, standortIId,
					bRahmenbestellungErzeugen, bInklGesperrteArtikel);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return ok;
	}

	/**
	 * Die Anzahl der BV-Eintraege dieses Mandanten ermitteln.
	 * 
	 * @return BestellvorschlagDto[]
	 * @throws ExceptionLP
	 */
	public long getAnzahlBestellvorschlagDesMandanten() throws ExceptionLP {
		try {
			return bestellvorschlagFac.getAnzahlBestellvorschlagDesMandanten(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return 0;
		}
	}

	public Map getAllLieferantenDesBestellvorschlages() throws ExceptionLP {
		try {
			return bestellvorschlagFac.getAllLieferantenDesBestellvorschlages(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void erstelleBestellvorschlag(Integer iVorlaufzeit, Integer iToleranz,
			Date dateFuerEintraegeOhneLiefertermin, ArrayList<Integer> arLosIId, ArrayList<Integer> arAuftragIId,
			boolean bMitNichtlagerbewirtschafteten, boolean bNurLospositionenBeruecksichtigen,
			boolean vormerklisteLoeschen, boolean bNichtFreigegebeneAuftraegeBeruecksichtigen,
			Integer partnerIIdStandort, boolean bArtikelNurAufAuftraegeIgnorieren, boolean bExakterAuftragsbezug) throws ExceptionLP {

		try {

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_LAGERMIN_JE_LAGER, ParameterFac.KATEGORIE_ARTIKEL,
							LPMain.getTheClient().getMandant());

			boolean bLagerMinJeLager = (Boolean) parameter.getCWertAsObject();

			if (bLagerMinJeLager == false) {
				bestellvorschlagFac.erstelleBestellvorschlag(iVorlaufzeit, iToleranz, dateFuerEintraegeOhneLiefertermin,
						arLosIId, arAuftragIId, bMitNichtlagerbewirtschafteten, bNurLospositionenBeruecksichtigen,
						vormerklisteLoeschen, true, bNichtFreigegebeneAuftraegeBeruecksichtigen, LPMain.getTheClient(),
						null, bArtikelNurAufAuftraegeIgnorieren, bExakterAuftragsbezug);
			} else {

				if (partnerIIdStandort == null) {

					Map m = DelegateFactory.getInstance().getLagerDelegate().getAlleStandorte();

					Iterator it = m.keySet().iterator();

					// Bei ersten Standort loeschen
					boolean bBVLoeschen = true;

					while (it.hasNext()) {
						Integer partnerIIdStandortZeile = (Integer) it.next();
						bestellvorschlagFac.erstelleBestellvorschlag(iVorlaufzeit, iToleranz,
								dateFuerEintraegeOhneLiefertermin, arLosIId, arAuftragIId,
								bMitNichtlagerbewirtschafteten, bNurLospositionenBeruecksichtigen, vormerklisteLoeschen,
								bBVLoeschen, bNichtFreigegebeneAuftraegeBeruecksichtigen, LPMain.getTheClient(),
								partnerIIdStandortZeile, bArtikelNurAufAuftraegeIgnorieren, bExakterAuftragsbezug);
						bBVLoeschen = false;
					}

				} else {
					bestellvorschlagFac.erstelleBestellvorschlag(iVorlaufzeit, iToleranz,
							dateFuerEintraegeOhneLiefertermin, arLosIId, arAuftragIId, bMitNichtlagerbewirtschafteten,
							bNurLospositionenBeruecksichtigen, vormerklisteLoeschen, true,
							bNichtFreigegebeneAuftraegeBeruecksichtigen, LPMain.getTheClient(), partnerIIdStandort,
							bArtikelNurAufAuftraegeIgnorieren, bExakterAuftragsbezug);
				}
			}

		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void erstelleBestellvorschlagAnhandStuecklistenmindestlagerstand(java.sql.Date dLiefertermin,
			boolean vormerklisteLoeschen, Integer partnerIIdStandort) throws ExceptionLP {

		try {

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_LAGERMIN_JE_LAGER, ParameterFac.KATEGORIE_ARTIKEL,
							LPMain.getTheClient().getMandant());

			boolean bLagerMinJeLager = (Boolean) parameter.getCWertAsObject();

			if (bLagerMinJeLager == false) {
				bestellvorschlagFac.erstelleBestellvorschlagAnhandStuecklistenmindestlagerstand(dLiefertermin,
						vormerklisteLoeschen, LPMain.getTheClient(), null,true);
			} else {

				if (partnerIIdStandort == null) {

					Map m = DelegateFactory.getInstance().getLagerDelegate().getAlleStandorte();

					Iterator it = m.keySet().iterator();

					// Bei ersten Standort loeschen
					boolean bBVLoeschen = true;
					while (it.hasNext()) {
						Integer partnerIIdStandortZeile = (Integer) it.next();
						bestellvorschlagFac.erstelleBestellvorschlagAnhandStuecklistenmindestlagerstand(dLiefertermin,
								vormerklisteLoeschen, LPMain.getTheClient(), partnerIIdStandortZeile,bBVLoeschen);
						bBVLoeschen = false;
					}

				} else {
					bestellvorschlagFac.erstelleBestellvorschlagAnhandStuecklistenmindestlagerstand(dLiefertermin,
							vormerklisteLoeschen, LPMain.getTheClient(), partnerIIdStandort, true);
				}
			}

		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void toggleBearbeitet(Integer bestellvorschlagIId) throws ExceptionLP {

		try {
			bestellvorschlagFac.toggleBearbeitet(bestellvorschlagIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void erstelleBestellvorschlagAnhandEinesAngebots(Integer angebotIId, java.sql.Date dLiefertermin)
			throws ExceptionLP {

		try {
			bestellvorschlagFac.erstelleBestellvorschlagAnhandEinesAngebots(angebotIId, dLiefertermin,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void erstelleBestellvorschlagAnhandEinesEkag(Integer einkaufsangebotIId, int menge,
			Timestamp tGeplanterFertigungstermin, Integer vorlaufzeit) throws ExceptionLP {

		try {
			bestellvorschlagFac.erstelleBestellvorschlagAnhandEkag(einkaufsangebotIId, menge,
					tGeplanterFertigungstermin, vorlaufzeit, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeLockDesBestellvorschlagesWennIchIhnSperre() throws ExceptionLP {
		try {
			bestellvorschlagFac.removeLockDesBestellvorschlagesWennIchIhnSperre(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void pruefeBearbeitenDesBestellvorschlagsErlaubt() throws ExceptionLP {
		try {
			bestellvorschlagFac.pruefeBearbeitenDesBestellvorschlagsErlaubt(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Die BV-Eintraege dieses Mandanten verdichten.
	 * 
	 * @throws ExceptionLP
	 */
	public void verdichteBestellvorschlag(Long lVerdichtungszeitraum, boolean bMengenBeruecksichtigen,
			boolean bBeruecksichtigeProjektklammer, boolean bPreiseaktualisieren) throws ExceptionLP {
		try {
			bestellvorschlagFac.verdichteBestellvorschlag(lVerdichtungszeitraum, bMengenBeruecksichtigen,
					bBeruecksichtigeProjektklammer, bPreiseaktualisieren, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Die spaeter wiederbeschaffbaren Positionen dieses Mandanten loeschen.
	 * 
	 * @throws ExceptionLP
	 * @param tNaechsterBV Date
	 */
	public void loescheSpaeterWiederbeschaffbarePositionen(java.sql.Date tNaechsterBV) throws ExceptionLP {
		try {
			bestellvorschlagFac.loescheSpaeterWiederbeschaffbarePositionen(tNaechsterBV, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void bestellvorschlagInLiefergruppenanfragenUmwandeln(String projekt) throws ExceptionLP {
		try {
			bestellvorschlagFac.bestellvorschlagInLiefergruppenanfragenUmwandeln(projekt, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void loescheBestellvorschlaegeAbTermin(Date dTermin) throws ExceptionLP {
		try {
			bestellvorschlagFac.loescheBestellvorschlaegeAbTermin(dTermin, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void artikellieferantZuruecksetzen(ArrayList<Integer> bestellvorschlagIIds) throws ExceptionLP {
		try {
			bestellvorschlagFac.artikellieferantZuruecksetzen(bestellvorschlagIIds, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void gemeinsameArtikelLoeschen() throws ExceptionLP {
		try {
			bestellvorschlagFac.gemeinsameArtikelLoeschen(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void uebernimmLieferantAusLieferantOptimieren(Integer bestellvorschlagIId, Integer lieferantIIdNeu)
			throws ExceptionLP {
		try {
			bestellvorschlagFac.uebernimmLieferantAusLieferantOptimieren(bestellvorschlagIId, lieferantIIdNeu,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void pruefeMindestbestellwert(Integer lieferantIId) throws ExceptionLP {
		try {
			boolean b = bestellvorschlagFac.mindestbestellwertErreicht(lieferantIId, LPMain.getTheClient());
			if (b == false) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
						LPMain.getTextRespectUISPr("bes.warning.mindestbestellwertnichterreicht"));
			}
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public String pruefeUndImportiereBestellvorschlagXLS(byte[] xlsDatei, java.sql.Timestamp tLiefertermin,
			boolean bVorhandenenBestellvorschlagLoeschen, boolean bImportierenWennKeinFehler) throws ExceptionLP {
		try {
			return bestellvorschlagFac.pruefeUndImportiereBestellvorschlagXLS(xlsDatei, tLiefertermin,
					bVorhandenenBestellvorschlagLoeschen, bImportierenWennKeinFehler, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public RueckgabeUeberleitungDto createBESausBVzuRahmen(FilterKriterium[] fk, SortierKriterium[] ski,
			Integer standortIId, boolean gemeinsameArtikelBestellen) throws ExceptionLP {
		RueckgabeUeberleitungDto toReturn = null;
		try {
			toReturn = bestellvorschlagFac.createBESausBVzuRahmen(fk, ski, standortIId, gemeinsameArtikelBestellen,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return toReturn;
	}

}