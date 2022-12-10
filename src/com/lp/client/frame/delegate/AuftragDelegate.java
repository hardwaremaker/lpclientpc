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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.ImportAmazonCsvDto;
import com.lp.server.auftrag.service.ImportShopifyCsvDto;
import com.lp.server.auftrag.service.ImportSonCsvDto;
import com.lp.server.auftrag.service.ImportVATXlsxDto;
import com.lp.server.auftrag.service.ImportWooCommerceCsvDto;
import com.lp.server.auftrag.service.LieferstatusDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HvOptional;

/**
 * <p>
 * Delegate fuer Auftraege.
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Uli Walch, 27. 04. 2005
 * </p>
 * 
 * <p>
 * 
 * @author Uli Walch
 *         </p>
 * 
 * @version 1.0
 * 
 * @version $Revision: 1.20 $ Date $Date: 2012/09/19 11:55:16 $
 */
public class AuftragDelegate extends Delegate {
	private Context context;
	private AuftragFac auftragFac;

	public AuftragDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			auftragFac = lookupFac(context, AuftragFac.class);
		} catch (Throwable t) {
			handleThrowable(t);
		}

	}

	/**
	 * Diese Methode legt einen neuen Auftrag auf der DB an.
	 * 
	 * @param auftragDto die Daten des neuen Auftrags
	 * @return Integer PK des neuen Auftrags
	 * @throws ExceptionLP
	 */
	public Integer createAuftrag(AuftragDto auftragDto) throws ExceptionLP {
		Integer key = null;

		try {
			key = auftragFac.createAuftrag(auftragDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return key;
	}

	public LieferstatusDto lieferstatusFindByPrimaryKey(Integer iId) throws ExceptionLP {
		LieferstatusDto dto = null;

		try {
			dto = auftragFac.lieferstatusFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return dto;
	}

	/**
	 * Aktualisieren der Kopfdaten eines Auftrags.
	 * 
	 * @param auftragDtoI     die Daten des Auftrags
	 * @param waehrungOriCNrI die urspruengliche Belegwaehrung
	 * @throws ExceptionLP Ausnahme
	 */
	public boolean updateAuftrag(AuftragDto auftragDtoI, String waehrungOriCNrI) throws ExceptionLP {
		try {
			return auftragFac.updateAuftrag(auftragDtoI, waehrungOriCNrI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return false;
		}
	}

	/**
	 * Den Auftrag mit Daten aktualisieren. Der Status bleibt dabei unveraendert.
	 * 
	 * @param auftragDtoI der Auftrag
	 * @throws ExceptionLP
	 */
	public void updateAuftragOhneWeitereAktion(AuftragDto auftragDtoI) throws ExceptionLP {
		try {
			auftragFac.updateAuftragOhneWeitereAktion(auftragDtoI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Einen bestehenden Auftrag als storniert kennzeichnen. <br>
	 * Eventuell vorhandene Positionen werden geloescht.
	 * 
	 * @param oAuftragI der aktuelle Auftrag
	 * @throws ExceptionLP
	 */
	public void storniereAuftrag(AuftragDto oAuftragI) throws ExceptionLP {
		try {
			auftragFac.storniereAuftrag(oAuftragI.getIId(), LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public ArrayList<KundeDto> getRechnungsadressenEinerAuftragsadresseSortiertNachHaeufigkeit(
			Integer kundeIIdAuftragsadresse) throws ExceptionLP {
		ArrayList<KundeDto> auftragDto = null;

		try {
			auftragDto = auftragFac.getRechnungsadressenEinerAuftragsadresseSortiertNachHaeufigkeit(
					kundeIIdAuftragsadresse, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return auftragDto;
	}

	public ArrayList<KundeDto> getLieferadressenEinerRechnungsadresseSortiertNachHaeufigkeit(
			Integer kundeIIdRechnungsadresse) throws ExceptionLP {
		ArrayList<KundeDto> auftragDto = null;

		try {
			auftragDto = auftragFac.getLieferadressenEinerRechnungsadresseSortiertNachHaeufigkeit(
					kundeIIdRechnungsadresse, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return auftragDto;
	}

	/**
	 * Einen Auftrag ueber seinen Schluessel von der db holen.
	 * 
	 * @param iId Integer
	 * @throws ExceptionLP
	 * @return AuftragDto
	 */
	public AuftragDto auftragFindByPrimaryKey(Integer iId) throws ExceptionLP {
		AuftragDto auftragDto = null;

		try {
			auftragDto = auftragFac.auftragFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return auftragDto;
	}

	public AuftragDto auftragFindByMandantCNrCNr(String cNrMandantI, String cNrI) throws ExceptionLP {
		AuftragDto auftragDto = null;

		try {
			auftragDto = auftragFac.auftragFindByMandantCNrCNr(cNrMandantI, cNrI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return auftragDto;
	}

	public AuftragDto auftragFindByMandantCNrCNrOhneExc(String cNrMandantI, String cNrI) throws ExceptionLP {
		AuftragDto auftragDto = null;

		try {
			auftragDto = auftragFac.auftragFindByMandantCNrCNrOhneExc(cNrMandantI, cNrI);
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return auftragDto;
	}

	/**
	 * Auftragstatus eines bestehenden Auftrags aendern.
	 * 
	 * @param auftragDto AuftragDto
	 * @param pStatus    String
	 * @throws ExceptionLP
	 */
	public void setAuftragstatus(AuftragDto auftragDto, String pStatus) throws ExceptionLP {
		auftragDto.setStatusCNr(pStatus);
		updateAuftrag(auftragDto, null);
	}

	public void aendereRechnungsadresseProjeknummerBestellnummer(AuftragDto auftragDto) throws ExceptionLP {
		try {
			this.auftragFac.aendereRechnungsadresseProjeknummerBestellnummer(auftragDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	/**
	 * Berechnung des Materialwerts eines Auftrags. <br>
	 * Der Materialwert ist die Summe ueber die Materialwerte der enthaltenen
	 * Artikelpositionen. <br>
	 * Der Materialwert einer Artikelposition errechnet sich aus Menge x
	 * Gestehungspreis des enthaltenen Artikels.
	 * 
	 * @param iIdAuftragI pk des Auftrags
	 * @throws ExceptionLP
	 * @return BigDecimal
	 */
	public BigDecimal berechneMaterialwertAuftrag(Integer iIdAuftragI) throws ExceptionLP {
		BigDecimal materialwert = null;

		try {
			materialwert = this.auftragFac.berechneMaterialwertGesamt(iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return materialwert;
	}

	/**
	 * Berechnung des Auftragswerts eines Auftrags. <br>
	 * Der Auftragswert ist die Summe ueber die Nettopreise der enthaltenen
	 * Positionen unter Beruecksichtigung der Zu- und Abschlaege, die in den
	 * Konditionen des Auftrags hinterlegt sind.
	 * 
	 * @param iIdAuftragI pk des Auftrags
	 * @throws ExceptionLP
	 * @return BigDecimal Gesamtwert des Auftrags in der gewuenschten Waehrung
	 */
	public BigDecimal berechneGesamtwertAuftrag(Integer iIdAuftragI) throws ExceptionLP {
		BigDecimal auftragswert = null;

		try {
			auftragswert = this.auftragFac.berechneNettowertGesamt(iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return auftragswert;
	}

	public BigDecimal berechneBestellwertAuftrag(Integer iIdAuftrag) throws ExceptionLP {
		try {
			return auftragFac.berechneBestellwertAuftrag(iIdAuftrag);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public String[] berechneSummeSplittbetrag(Integer iIdAuftrag) throws ExceptionLP {
		try {
			return auftragFac.berechneSummeSplittbetragAuftrag(iIdAuftrag, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public int importiereSON_CSV(LinkedHashMap<String, ArrayList<ImportSonCsvDto>> hmNachBestellnummerGruppiert)
			throws ExceptionLP {
		try {
			return auftragFac.importiereSON_CSV(hmNachBestellnummerGruppiert, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return 0;
	}

	public int importiereWooCommerce_CSV(
			LinkedHashMap<String, ArrayList<ImportWooCommerceCsvDto>> hmNachBestellnummerGruppiert) throws ExceptionLP {
		try {
			return auftragFac.importiereWooCommerce_CSV(hmNachBestellnummerGruppiert, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return 0;
	}

	public int importiereVAT_XLSX(Integer kundeIId, Integer ansprechpartnerIId,
			LinkedHashMap<String, ArrayList<ImportVATXlsxDto>> hmNachBestellnummerGruppiert) throws ExceptionLP {
		try {
			return auftragFac.importiereVAT_XLSX(kundeIId, ansprechpartnerIId, hmNachBestellnummerGruppiert,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return 0;
	}

	/**
	 * Den Status des Auftrags aendern.
	 * 
	 * @param pkAuftrag PK des Auftrags
	 * @param status    der neue Status
	 * @throws ExceptionLP
	 * @return boolean
	 */
	public boolean aendereAuftragstatus(Integer pkAuftrag, String status) throws ExceptionLP {
		boolean statusGeaendert = false;

		try {
			statusGeaendert = this.auftragFac.aendereAuftragstatus(pkAuftrag, status, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return statusGeaendert;
	}

	/**
	 * Wenn die Zu- und Abschlaege in den Positionen geaendert wurden, dann werden
	 * im Anschluss die davon abhaengigen Werte neu berechnet.
	 * 
	 * @param iIdAuftragI PK des Auftrags
	 * @throws ExceptionLP Ausnahme
	 */
	public void updateAuftragKonditionen(Integer iIdAuftragI) throws ExceptionLP {
		try {
			auftragFac.updateAuftragKonditionen(iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void terminVerschieben(ArrayList<Integer> auftragIIds, java.sql.Timestamp tLiefertermin, java.sql.Timestamp tFinaltermin,
			java.sql.Timestamp tWunschtermin, boolean bMehrereAuftraege) throws ExceptionLP {
		try {
			ArrayList<String> al = auftragFac.terminVerschieben(auftragIIds, tLiefertermin, tFinaltermin, tWunschtermin,bMehrereAuftraege,
					LPMain.getTheClient());

			if (al != null && al.size() > 0) {

				String msg = LPMain.getTextRespectUISPr("auft.terminverschieben.error") + "\n\n";
				for (int i = 0; i < al.size(); i++) {
					msg += al.get(i) + "\n";
				}

				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), msg);
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Die Anzahl der Belege holen, die zu einem bestimmten Auftrag existieren.
	 * 
	 * @param iIdAuftragI PK des Auftrags
	 * @return int die Anzahl der Belege zu diesem Auftrag
	 * @throws ExceptionLP Ausnahme
	 */
	public int berechneAnzahlBelegeZuAuftrag(Integer iIdAuftragI) throws ExceptionLP {
		int iAnzahlBelgeO = 0;
		try {
			iAnzahlBelgeO = auftragFac.berechneAnzahlBelegeZuAuftrag(iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iAnzahlBelgeO;
	}

	public void aktiviereBelegControlled(Integer iid, Timestamp t) throws ExceptionLP {
		try {
			BelegPruefungDto pruefungDto = auftragFac.aktiviereBelegControlled(iid, t, LPMain.getTheClient());
			dialogBelegpruefung(pruefungDto);
			// // SP1881
			// DelegateFactory
			// .getInstance()
			// .getSystemDelegate()
			// .enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
			// LocaleFac.BELEGART_AUFTRAG, iid);
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
	}

	public Timestamp berechneBelegControlled(Integer iid) throws ExceptionLP {
		try {
			BelegPruefungDto pruefungDto = auftragFac.berechneBelegControlled(iid, LPMain.getTheClient());
			dialogBelegpruefung(pruefungDto);
			return pruefungDto.getBerechnungsZeitpunkt();
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
		return null;
	}

	public Timestamp berechneAktiviereBelegControlled(Integer iid) throws ExceptionLP {
		try {
			BelegPruefungDto pruefungDto = auftragFac.berechneAktiviereBelegControlled(iid, LPMain.getTheClient());
			dialogBelegpruefung(pruefungDto);
			return pruefungDto.getBerechnungsZeitpunkt();
		} catch (Throwable t1) {
			handleThrowable(t1);
		}
		return null;
	}

	public boolean checkPositionFormat(Integer iIdAuftragI) throws ExceptionLP {
		try {
			return auftragFac.checkPositionFormat(iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return false;
	}

	/**
	 * Einen Auftrag manuell auf 'Erledigt' setzen.
	 * 
	 * @param iIdAuftragI PK des Auftrags
	 * @throws ExceptionLP Ausnahme
	 */
	public void manuellErledigen(Integer iIdAuftragI) throws ExceptionLP {
		try {
			auftragFac.manuellErledigen(iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void toggleVerrechenbar(Integer iIdAuftragI) throws ExceptionLP {
		try {
			auftragFac.toggleVerrechenbar(iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void toggleAuftragsfreigabe(Integer iIdAuftragI) throws ExceptionLP {
		try {
			auftragFac.toggleAuftragsfreigabe(iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void auftragFreigeben(Integer iIdAuftragI) throws ExceptionLP {
		try {
			auftragFac.auftragFreigeben(iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAuftragBegruendung(Integer auftragIId, Integer begruendungIId) throws ExceptionLP {
		try {
			auftragFac.updateAuftragBegruendung(auftragIId, begruendungIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAuftragVersteckt(Integer auftragIId) throws ExceptionLP {
		try {
			auftragFac.updateAuftragVersteckt(auftragIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Den Status eines Auftrags von 'Erledigt' auf 'Erfasst' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param iIdAuftragI PK des Auftrags
	 * @throws ExceptionLP
	 */
	public void erledigungAufheben(Integer iIdAuftragI) throws ExceptionLP {
		try {
			auftragFac.erledigungAufheben(iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Methode zum Erzeugen eines neues Auftrags als Kopie eines bestehenden
	 * Auftrags. <br>
	 * Es werden auch die Positionen kopiert.
	 * 
	 * @param iIdAuftragI PK des bestehenden Auftrags
	 * @return Integer PK des neuen Auftrags
	 * @throws ExceptionLP Ausnahme
	 */

	public Integer erzeugeAuftragpositionUeberSchnellanlage(Integer auftragIId, ArtikelDto artikelDto,
			PaneldatenDto[] paneldatenDtos) throws ExceptionLP {
		Integer iIdAuftrag = null;
		try {
			iIdAuftrag = auftragFac.erzeugeAuftragpositionUeberSchnellanlage(auftragIId, artikelDto, paneldatenDtos,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iIdAuftrag;
	}

	public Integer erzeugeAuftragAusAuftrag(Integer iIdAuftragI, InternalFrame internalFrame) throws ExceptionLP {
		Integer iIdAuftrag = null;
		try {
			iIdAuftrag = auftragFac.erzeugeAuftragAusAuftrag(iIdAuftragI, LPMain.getTheClient());

			if (iIdAuftrag != null) {

				HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();

				AuftragDto abDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(iIdAuftrag);

				hmKunden.put(abDto.getKundeIIdAuftragsadresse(), abDto.getKundeIIdAuftragsadresse());
				if (!hmKunden.containsKey(abDto.getKundeIIdRechnungsadresse())) {
					hmKunden.put(abDto.getKundeIIdRechnungsadresse(), abDto.getKundeIIdRechnungsadresse());
				}

				if (!hmKunden.containsKey(abDto.getKundeIIdLieferadresse())) {
					hmKunden.put(abDto.getKundeIIdLieferadresse(), abDto.getKundeIIdLieferadresse());
				}

				Iterator<Integer> it = hmKunden.keySet().iterator();

				while (it.hasNext()) {
					DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(it.next(), LocaleFac.BELEGART_AUFTRAG,
							internalFrame);
				}

				// SP6638
				DelegateFactory.getInstance().getAnsprechpartnerDelegate().pruefeObAnsprechpartnerVersteckt(
						abDto.getAnsprechparnterIId(), abDto.getAnsprechpartnerIIdLieferadresse(),
						abDto.getAnsprechpartnerIIdRechnungsadresse());

			}

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iIdAuftrag;
	}

	public Integer erzeugeAuftragAusAuftragMitNeuemLiefertermin(Integer iIdAuftragI, InternalFrame internalFrame)
			throws ExceptionLP {
		Integer iIdAuftrag = null;
		try {

			java.sql.Date dLiefertermin = DialogFactory
					.showDatumseingabe(LPMain.getTextRespectUISPr("auft.datenausbestehendemauftrag.liefertermin"));

			while (dLiefertermin != null) {

				Timestamp tLiefertermin = new java.sql.Timestamp(dLiefertermin.getTime());

				iIdAuftrag = auftragFac.erzeugeAuftragAusAuftrag(iIdAuftragI, HvOptional.empty(), tLiefertermin,
						LPMain.getTheClient());

				dLiefertermin = DialogFactory.showDatumseingabe(
						LPMain.getTextRespectUISPr("auft.datenausbestehendemauftrag.liefertermin"), dLiefertermin);

			}

			if (iIdAuftrag != null) {

				HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();

				AuftragDto abDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(iIdAuftrag);

				hmKunden.put(abDto.getKundeIIdAuftragsadresse(), abDto.getKundeIIdAuftragsadresse());
				if (!hmKunden.containsKey(abDto.getKundeIIdRechnungsadresse())) {
					hmKunden.put(abDto.getKundeIIdRechnungsadresse(), abDto.getKundeIIdRechnungsadresse());
				}

				if (!hmKunden.containsKey(abDto.getKundeIIdLieferadresse())) {
					hmKunden.put(abDto.getKundeIIdLieferadresse(), abDto.getKundeIIdLieferadresse());
				}

				Iterator<Integer> it = hmKunden.keySet().iterator();

				while (it.hasNext()) {
					DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(it.next(), LocaleFac.BELEGART_AUFTRAG,
							internalFrame);
				}

				// SP6638
				DelegateFactory.getInstance().getAnsprechpartnerDelegate().pruefeObAnsprechpartnerVersteckt(
						abDto.getAnsprechparnterIId(), abDto.getAnsprechpartnerIIdLieferadresse(),
						abDto.getAnsprechpartnerIIdRechnungsadresse());

			}

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iIdAuftrag;
	}

	public Integer erzeugeNegativeMengeAusAuftrag(Integer iIdAuftragI) throws ExceptionLP {
		Integer iIdAuftrag = null;
		try {
			iIdAuftrag = auftragFac.erzeugeNegativeMengeAusAuftrag(iIdAuftragI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iIdAuftrag;
	}

	/**
	 * Methode zum Erzeugen eines eines Lieferscheins aus einem bestehenden Auftrag.
	 * <br>
	 * Nicht mengenbehaftete Positionen werden ebebfalls kopiert, mengenbehaftete
	 * Positionen muessen vom Benutzer gezielt uebernommen werden.
	 * 
	 * @param iIdAuftragI      PK des bestehenden Auftrags
	 * @param lieferscheinDtoI der Benutzer kann vorbelegte Eigenschaften
	 *                         uebersteuern
	 * @return Integer PK des neuen Lieferscheins
	 * @throws ExceptionLP Ausnahme
	 */
	public Integer erzeugeLieferscheinAusAuftrag(Integer iIdAuftragI, LieferscheinDto lieferscheinDtoI,
			InternalFrame internalFrame) throws ExceptionLP {
		Integer iIdLieferschein = null;
		try {

			iIdLieferschein = auftragFac.erzeugeLieferscheinAusAuftrag(iIdAuftragI, lieferscheinDtoI,
					rabattAusRechnungsadresse(iIdAuftragI), LPMain.getTheClient());

			if (iIdLieferschein != null) {

				HashMap<Integer, Integer> hmKunden = new HashMap<Integer, Integer>();

				LieferscheinDto lsDto = DelegateFactory.getInstance().getLsDelegate()
						.lieferscheinFindByPrimaryKey(iIdLieferschein);

				hmKunden.put(lsDto.getKundeIIdLieferadresse(), lsDto.getKundeIIdLieferadresse());

				if (!hmKunden.containsKey(lsDto.getKundeIIdRechnungsadresse())) {
					hmKunden.put(lsDto.getKundeIIdRechnungsadresse(), lsDto.getKundeIIdRechnungsadresse());
				}

				Iterator<Integer> it = hmKunden.keySet().iterator();

				while (it.hasNext()) {
					DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(it.next(),
							LocaleFac.BELEGART_LIEFERSCHEIN, internalFrame);
				}

				// SP6638
				DelegateFactory.getInstance().getAnsprechpartnerDelegate().pruefeObAnsprechpartnerVersteckt(
						lsDto.getAnsprechpartnerIId(), lsDto.getAnsprechpartnerIIdRechnungsadresse());

			}

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iIdLieferschein;
	}

	public Map getListeDerErfasstenAuftraege(Integer iIdAngebotI) throws ExceptionLP {
		Map m = null;
		try {
			m = auftragFac.getListeDerErfasstenAuftraege(iIdAngebotI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return m;
	}

	/**
	 * Liefert alle Abrufeauftraege zu einem bestimmten Rahmenauftrag.
	 * 
	 * @param iIdRahmenauftragI PK des Rahmenauftrags
	 * @return AuftragDto[] die Abrufauftraege
	 * @throws ExceptionLP Ausnahme
	 */
	public AuftragDto[] abrufauftragFindByAuftragIIdRahmenauftrag(Integer iIdRahmenauftragI) throws ExceptionLP {
		AuftragDto[] aAuftragDto = null;
		try {
			aAuftragDto = auftragFac.abrufauftragFindByAuftragIIdRahmenauftrag(iIdRahmenauftragI,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return aAuftragDto;
	}

	/**
	 * Alle Auftraege holen, die zu einem bestimmten Angebot erfasst wurden.
	 * 
	 * @param mandantCNrI     PK des Angebots
	 * @param auftragsartCNrI String
	 * @return AuftragDto[] die Auftraege
	 * @throws ExceptionLP Ausnahme
	 */
	public AuftragDto[] auftragFindByMandantCNrAuftragartCNr(String mandantCNrI, String auftragsartCNrI)
			throws ExceptionLP {
		AuftragDto[] aAuftragDto = null;
		try {
			aAuftragDto = auftragFac.auftragFindByMandantCNrAuftragartCNr(mandantCNrI, auftragsartCNrI,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return aAuftragDto;
	}

	public AuftragDto[] auftragFindByMandantCNrAuftragartCNrStatusCNr(String mandantCNrI, String auftragsartCNrI,
			String statusCNrI) throws ExceptionLP {
		AuftragDto[] aAuftragDto = null;
		try {
			aAuftragDto = auftragFac.auftragFindByMandantCNrAuftragartCNrStatusCNr(mandantCNrI, auftragsartCNrI,
					statusCNrI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return aAuftragDto;
	}

	public boolean darfWiederholungsTerminAendern(Integer auftragIId) throws ExceptionLP {
		try {
			return auftragFac.darfWiederholungsTerminAendern(auftragIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			return false;
		}
	}

	public void setzeVersandzeitpunktAufJetzt(Integer iAuftragIId, String sDruckart) throws ExceptionLP {
		try {
			auftragFac.setzeVersandzeitpunktAufJetzt(iAuftragIId, sDruckart);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public AuftragDto[] auftragFindByMandantCNrKundeIIdBestellnummerOhneExc(Integer iIdKundeI, String mandantCNrI,
			String cBestellnummerI) throws ExceptionLP {
		AuftragDto[] aAuftragDto = null;
		try {
			aAuftragDto = auftragFac.auftragFindByMandantCnrKundeIIdBestellnummerOhneExc(iIdKundeI, mandantCNrI,
					cBestellnummerI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return aAuftragDto;
	}

	public java.sql.Date getAuftragWiederholungsstart(Integer iAuftragIId) throws ExceptionLP {
		try {
			return auftragFac.getWiederholungsTermin(iAuftragIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public boolean hatAuftragVersandweg(AuftragDto auftragDto) throws ExceptionLP {
		try {
			return auftragFac.hatAuftragVersandweg(auftragDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return false;
	}

	public AuftragDto istAuftragBeiAnderemMandantenHinterlegt(Integer bestellungIId) throws ExceptionLP {
		try {
			return auftragFac.istAuftragBeiAnderemMandantenHinterlegt(bestellungIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public String createOrderResponsePost(AuftragDto auftragDto) throws ExceptionLP {
		try {
			return auftragFac.createOrderResponsePost(auftragDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return "";
	}

	public void korrekturbetragZuruecknehmen(Integer auftragIId) throws ExceptionLP {
		try {

			AuftragDto abDto = auftragFindByPrimaryKey(auftragIId);

			if (abDto.getNKorrekturbetrag() != null && abDto.getNKorrekturbetrag().doubleValue() != 0) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
						LPMain.getTextRespectUISPr("lp.korrekturbetragzurueckgenommen"));
			}

			auftragFac.korrekturbetragZuruecknehmen(auftragIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public ArrayList<Integer> wiederholendeAuftraegeUmIndexAnpassen() throws ExceptionLP {
		try {

			return auftragFac.wiederholendeAuftraegeUmIndexAnpassen(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public String wiederholendeAuftraegeMitPreisgueltigkeitAnpassen(Timestamp tDatumPreisgueltigkeit,
			double dAbweichung) throws ExceptionLP {
		try {

			return auftragFac.wiederholendeAuftraegeMitPreisgueltigkeitAnpassen(tDatumPreisgueltigkeit, dAbweichung,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void repairAuftragZws5524(Integer angebotId) throws ExceptionLP {
		try {
			auftragFac.repairAuftragZws5524(angebotId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public List<Integer> repairAuftragZws5524GetList() throws ExceptionLP {
		try {
			return auftragFac.repairAuftragZws5524GetList(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}

		return new ArrayList<Integer>();
	}

	public AuftragDto erzeugeAenderungsauftrag(Integer auftragIId) throws ExceptionLP {
		try {
			return auftragFac.erzeugeAenderungsauftrag(auftragIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void uebersteuereIntelligenteZwischensumme(Integer auftragpositionIId,
			BigDecimal bdBetragInBelegwaehrungUebersteuert) throws ExceptionLP {
		try {
			auftragFac.uebersteuereIntelligenteZwischensumme(auftragpositionIId, bdBetragInBelegwaehrungUebersteuert,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);

		}
	}

}
