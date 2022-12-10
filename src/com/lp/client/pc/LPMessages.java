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
package com.lp.client.pc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.PersonalDelegate;
import com.lp.client.pc.erroraction.ArtikelUrsprungslandFehltError;
import com.lp.client.pc.erroraction.BuchungSteuersaetzeInKontenUnterschiedlichError;
import com.lp.client.pc.erroraction.BuchungenMitAuszugsnummerVorhandenError;
import com.lp.client.pc.erroraction.DebitorKontoError;
import com.lp.client.pc.erroraction.FehlendeEinheitkonvertierungError;
import com.lp.client.pc.erroraction.FontAmClientNichtGefundenError;
import com.lp.client.pc.erroraction.FontAmServerNichtGefundenError;
import com.lp.client.pc.erroraction.FremdsystemnummerNichtNummerischError;
import com.lp.client.pc.erroraction.GTINBasisnummerLaengeUngueltigError;
import com.lp.client.pc.erroraction.GTINGenerierungAlleArtikelnummernVergebenError;
import com.lp.client.pc.erroraction.KreditorKontoError;
import com.lp.client.pc.erroraction.LinienabrufProduktionAbliefermengeKleinerEinsError;
import com.lp.client.pc.erroraction.LinienabrufProduktionKeinLosGefundenError;
import com.lp.client.pc.erroraction.LinienabrufProduktionNichtGanzzahligeSollsatzgroessenError;
import com.lp.client.pc.erroraction.LinienabrufProduktionStklNichtImMandantError;
import com.lp.client.pc.erroraction.LosablieferungSollsatzgroesseUnterschrittenError;
import com.lp.client.pc.erroraction.LosausgabeHttpPostUnerwarteterStatusCodeError;
import com.lp.client.pc.erroraction.LosausgabeHttpPostVerbindungsfehlerError;
import com.lp.client.pc.erroraction.LosausgabeHttpTopsError;
import com.lp.client.pc.erroraction.LoserledigungHttpPostUnerwarteterStatusCodeError;
import com.lp.client.pc.erroraction.LoserledigungHttpPostVerbindungsfehlerError;
import com.lp.client.pc.erroraction.LsAlsRePosWaehrungenUnterschiedlichError;
import com.lp.client.pc.erroraction.MailtextvorlageNichtGefundenError;
import com.lp.client.pc.erroraction.ReportvarianteNichtGefundenError;
import com.lp.client.pc.erroraction.ScriptFehlerhafterZugriffError;
import com.lp.client.pc.erroraction.ScriptInstanzierungNichtMoeglichError;
import com.lp.client.pc.erroraction.ScriptInstanzierungNichtMoeglichIOError;
import com.lp.client.pc.erroraction.ScriptInstanzierungVerifyError;
import com.lp.client.pc.erroraction.ScriptKlasseNichtGefundenError;
import com.lp.client.pc.erroraction.ScriptMethodeNichtAufrufbarError;
import com.lp.client.pc.erroraction.ScriptMethodeNichtGefundenError;
import com.lp.client.pc.erroraction.ScriptNichtUebersetzbarError;
import com.lp.client.pc.erroraction.ScriptParameterMehrfachDefiniertError;
import com.lp.client.pc.erroraction.ScriptParameterNichtGefundenError;
import com.lp.client.pc.erroraction.SepaVerbuchungAuszugFalscherStatusError;
import com.lp.client.pc.erroraction.SepaZahlungenMitAuszugsnummerVorhandenError;
import com.lp.client.pc.erroraction.SepakontoauszugNichtImAktuellenGJError;
import com.lp.client.pc.erroraction.SteuerkategorieBasisKontoFehltError;
import com.lp.client.pc.erroraction.SteuerkategorieDefinitionError;
import com.lp.client.pc.erroraction.SteuerkategorieFehltError;
import com.lp.client.pc.erroraction.SteuerkategorieKontoDefinitionFehltError;
import com.lp.client.pc.erroraction.SteuerkategoriekontoFehltError;
import com.lp.client.pc.erroraction.TopsFallbackMehrerePfadeError;
import com.lp.client.pc.erroraction.UnbekannteSerienChargennummer;
import com.lp.client.pc.erroraction.WebabfrageFehlgeschlagenError;
import com.lp.client.pc.erroraction.ZugferdError;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.finanz.service.KontoVerifierEntry;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class LPMessages {
	private static Map<Integer, String> simpleErrorMsgs = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGE_STEUERBUCHUNG, "fb.error.ungueltigesteuerbuchung");

			put(EJBExceptionLP.FEHLER_GEBINDEMENGE_ERFORDERLICH,
					"artikel.gebindemenge.erforderlich.wenngebindedefiniert");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_PAUSCHALKORREKTUR_ALLGEMEINER_RABATT_MUSS_0_SEIN,
					"bes.pauschalbetrag.allgrabattmussnullsein");
			put(EJBExceptionLP.FEHLER_GENERIERE_HIERARCHISCHE_CHARGENNUMMERN,
					"stkl.hierarchische.chargennummern.error");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_PAUSCHALKORREKTUR_NUR_BIS_WARENEINGANG_MOEGLICH,
					"bes.pauschalbetrag.nurbiswemoeglich");
			put(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_MANUELL_ERLEDIGEN,
					"rech.abrechnungsvorschlag.manuellerleidgen.error");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEINE_IN_VERSCHIEDENE_LAENDERARTEN,
					"rech.ls.unterschiedliche.laenderarten");
			
			put(EJBExceptionLP.FEHLER_KAPAZITAET_TEXTBLOCK_UEBERSCHRITTEN,
					"lp.textblock.overflow");
		
			put(EJBExceptionLP.FEHLER_STATUS, "lp.error.status");
			put(EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN, "lp.error.beleg.keinemengenbehaftetenpositionen");
			put(EJBExceptionLP.FEHLER_ES_WERDEN_NUR_XLS_BIS_2007_UNTERSTUETZT, "lp.xlsbis2007.error");
			put(EJBExceptionLP.FEHLER_BEIM_AKTIVIEREN_BELEG_WURDE_GEAENDERT, "lp.report.belegwurdeinzwischengeaendert");
			put(EJBExceptionLP.FEHLER_FINANZ_KONTOLAENDERART_ZEIGT_AUF_SICH_SELBST,
					"fb.error.kontolaenderverweistaufsichselbst");
			put(EJBExceptionLP.FEHLER_ZUSAETZLICHE_SPESEN_AUF_ENDE_NICHT_MOEGLICH,
					"personal.zusspesen.zusspesenvorhanden");
			put(EJBExceptionLP.FEHLER_PROJEKT_HISTORYART_IN_AUSWAHLLISTE_ANZEIGEN_DARF_NUR_EINMAL_VORHANDEN_SEIN,
					"proj.historyart.inauswahllisteanzeigen.error");
			put(EJBExceptionLP.FEHLER_ARTIKELETIKETT_ANZAHL_SNR_UNGLEICH_LFDNR, "artkel.etikett.error.anzahl");

			put(EJBExceptionLP.FEHLER_DOKUMENTENABLAGE_OFFLINE, "lp.dokumente.offline");
			put(EJBExceptionLP.FEHLER_STORNIEREN_ZAHLUNGEN_VORHANDEN, "rechnung.essindbereitszahlungeneingetragen");
			put(EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_KEINE_POS_VORHANDEN,
					"lp.error.positionen.keineweiterenpositionen");
			put(EJBExceptionLP.FEHLER_EINTRAG_IN_ABRECHNUNGSVORSCHLAG_UND_KANN_NICHT_GEAENDERT_WERDEN,
					"rech.eintraginabrechnungsvorschlag.error");
			put(EJBExceptionLP.FEHLER_BENUTZER_AUTOMATISCHE_BELEGANLAGE_NICHT_DEFINIERT,
					"auft.error.beleganlage.benutzernichtdefinfiert");
			put(EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_STATUS, "anf.error.positionenverschieben");
			put(EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_VONBIS, "lp.error.positionen.vonnachbis");
			put(EJBExceptionLP.FEHLER_POSITION_VERTAUSCHEN_ZWISCHENSUMME_IN_SICH_SELBST,
					"lp.error.positionen.sichselbstbeinhalten");
			put(EJBExceptionLP.FEHLER_UNGUELTIGE_INSTALLATION, "lp.error.ungueltigeinstallation");
			put(EJBExceptionLP.FEHLER_SYSTEM_KOSTENSTELLE_IN_VERWENDUNG, "lp.error.kostenstelleinverwendung");
			put(EJBExceptionLP.FEHLER_SYSTEM_STUECK_KANN_NICHT_GELOESCHT_WERDEN, "lp.basiseinheit");
			put(EJBExceptionLP.FEHLER_SYSTEM_SEKUNDE_KANN_NICHT_GELOESCHT_WERDEN, "lp.basiseinheit");
			put(EJBExceptionLP.FEHLER_SYSTEM_STUNE_KANN_NICHT_GELOESCHT_WERDEN, "lp.basiseinheit");
			put(EJBExceptionLP.FEHLER_SYSTEM_MINUTE_KANN_NICHT_GELOESCHT_WERDEN, "lp.basiseinheit");
			put(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, "lp.error.anlegen");
			put(EJBExceptionLP.FEHLER_BEIM_ANLEGEN_ENTITY_EXISTS, "lp.error.anlegen.entityexists");
			put(EJBExceptionLP.FEHLER_DUPLICATE_PRIMARY_KEY, "lp.error.doppelterprimarykey");
			put(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, "lp.error.doppelterunique");
			put(EJBExceptionLP.FEHLER_ZAHL_ZU_KLEIN, "lp.error.zahlzuklein");
			put(EJBExceptionLP.FEHLER_ZAHL_ZU_GROSS, "lp.error.zahlzugross");
			put(EJBExceptionLP.FEHLER_MENGE_FUER_SERIENNUMMERNBUCHUNG_MUSS_EINS_SEIN,
					"lp.error.seriennummernbuchungmusseinssein");
			put(EJBExceptionLP.ARTIKEL_WECHSEL_LAGERBEWIRTSCHAFTET_NICHT_MOEGLICH,
					"lp.error.wechsellagerbewirtschaftetnichtmoeglich");
			put(EJBExceptionLP.ARTIKEL_LAGERBEWIRTSCHAFTET_KANN_NUR_ABGESCHALTET_WERDEN_WENN_NICHT_CHNR_SNR_BEHAFTET,
					"lp.error.wechsellagerbewirtschaftetnurmoeglich.wennnichtsnrchnrbehaftet");
			put(EJBExceptionLP.FEHLER_DARF_MIR_NICHT_MICH_SELBST_ZUORDNEN, "lp.error.darfmirnichtselbstzuordnen");
			put(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT, "lp.error.mandantparameterfehlt");
			put(EJBExceptionLP.FEHLER_PARTNER_LKZ_AENDERUNG_NICHT_MOEGLICH,
					"part.error.lkzdarfnichtmehrgeaendertwerden");
			put(EJBExceptionLP.ARTIKEL_WECHSEL_CHARGENNUMMERNTRAGEND_NICHT_MOEGLICH,
					"lp.error.wechselchargennummerntragendnichtmoeglich");
			put(EJBExceptionLP.FEHLER_ANZAHLUNG_MEHRERE_AUFTRAEGE_IN_LIEFERSCHEIN,
					"rech.anzahlungsrechnung.lieferschein.mehrereabs.error");

			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_MEHRFACHES_KOMMT, "lp.error.mehrfacheskommt");
			put(EJBExceptionLP.FEHLER_BELEGZEITEN_IN_UNTERBRECHUNG, "lp.error.belegzeiten.waehrendunterbrechung");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_MEHRFACHES_GEHT, "lp.error.mehrfachesgeht");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_GEHT_OHNE_KOMMT, "lp.error.gehtohnekommt");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_TAETIGKEIT_VOR_KOMMT, "lp.error.taetigkeitvorkommt");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_GEHT_FEHLT, "lp.error.gehtfehlt");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_SONDERTAETIGKEIT_MUSS_BEENDET_WERDEN,
					"zeiterfassung.sondertaetigkeitbeenden");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_RELATIVE_NICHT_MOEGLICH, "zeiterfassung.relativenichtmoeglich");
			put(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "lp.error.fehlerbeifindbyprimarykey");
			put(EJBExceptionLP.WARNUNG_99_KUNDEN_PRO_BUCHSTABE, "part.kunde.overflow");
			put(EJBExceptionLP.FEHLER_BEIM_UPDATE, "lp.error.fehlerbeiupdate");
			put(EJBExceptionLP.FEHLER_FLR, "lp.error.fehlerbeiflr");
			put(EJBExceptionLP.FEHLER_IS_ALREADY_LOCKED, "lp.error.fehlerisalreadylocked");
			put(EJBExceptionLP.FEHLER_LOCK_NOTFOUND, "lp.error.fehlerlocknotfound");
			put(EJBExceptionLP.FEHLER_TRANSAKTION_NICHT_DURCHGEFUEHRT__ROLLBACK, "lp.error.rollback");
			put(EJBExceptionLP.FEHLER_KEINE_VERBINDUNG_ZUM_JBOSS, "lp.error.keineverbindungzumjboss");
			put(EJBExceptionLP.FEHLER_DRUCKEN_FALSCHE_VERSION, "lp.error.falscheversion");
			put(EJBExceptionLP.FEHLER_PERSONAL_DUPLICATE_AUSWEIS, "pers.error.doppelteausweisnummern");
			put(EJBExceptionLP.FEHLER_AUSWEISNUMMER_ZESTIFT, "pers.error.stiftzuordnung");
			put(EJBExceptionLP.FEHLER_DRUCKEN_KEINE_DATEN_ZU_DRUCKEN, "lp.hint.nopages");
			put(EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN, "lp.error.reportnichtgefunden");
			put(EJBExceptionLP.FEHLER_DRUCKEN_FEHLER_IM_REPORT, "lp.error.fehlerimreport");
			put(EJBExceptionLP.FEHLER_DRUCKEN_FILE_NOT_FOUND, "lp.error.reportfilenotfound");
			put(EJBExceptionLP.FEHLER_FORMAT_NUMBER, "lp.error.belegwerte");
			put(EJBExceptionLP.FEHLER_NULLPOINTEREXCEPTION, "lp.error.fatal");
			put(EJBExceptionLP.FEHLER_FINANZ_MAHNSTUFEN_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN,
					"lp.error.mahnstufe123darfnichtgeloeschtwerden");
			put(EJBExceptionLP.FEHLER_FINANZ_MAHNTEXTE_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN,
					"lp.error.mahntext123darfnichtgeloeschtwerden");
			put(EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MENGE_UNGLEICH, "lp.error.seriennummernmengeungleich");
			put(EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_VON_BIS_ZIFFERNTEIL_UNGLEICH,
					"lp.error.seriennummernziffernteilungleich");
			put(EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_VON_BIS_PREFIX_UNGLEICH,
					"lp.error.seriennummernprefixungleich");
			put(EJBExceptionLP.FEHLER_ARTIKEL_SERIENNUMMER_MUSS_MIT_ZIFFERNTEIL_ENDEN,
					"lp.error.seriennummermussmitziffernteilenden");
			put(EJBExceptionLP.FEHLER_HAUPTLAGER_BEI_DIESEM_MANDANTEN_SCHON_VORHANDEN,
					"artikel.error.hauptlagerbeimandant");
			put(EJBExceptionLP.FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN,
					"finanz.error.mahnungenausmahnlaufgedruckt");
			put(EJBExceptionLP.FEHLER_RECHNUNG_MAHNSPERRE, "rechnung.error.rechnunginmahnsperre");
			put(EJBExceptionLP.FEHLER_RECHNUNG_NEUE_MAHNSTUFE_MUSS_GROESSER_SEIN_ALS_DIE_ALTE,
					"rechnung.error.mahnstufegroesser");
			put(EJBExceptionLP.LAGER_UPDATE_AUF_ARTIKEL_NICHT_ERLAUBT, "artikel.error.keinupdateaufartikelmoeglich");
			put(EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNSTUFEN_EINGETRAGEN, "finanz.error.keinemahnstufeeingetragen");
			put(EJBExceptionLP.FEHLER_GEHT_VOR_ENDE, "lp.error.gehtvorende");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_LAGERENTNAHME_DARF_NICHT_GELOESCHT_WERDEN,
					"fert.error.lagerentnahmedarfnichtgeloeschtwerden");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DARF_FUER_MATERIALLISTE_NICHT_DURCHGEFUEHRT_WERDEN,
					"fert.error.darffuermateriallistenichtdurchgefuehrtwerden");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_AUS_STUECKLISTE_DARF_NICHT_GELOESCHT_WERDEN,
					"fert.error.materialausstuecklistedarfnichtgeloeschtwerden");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_POSITION_AUS_ARBEITSPLAN_DARF_NICHT_GELOESCHT_WERDEN,
					"fert.error.positionausarbeitsplandarfnichtgeloeschtwerden");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_UPDATE_LOSABLIEFERUNG_FEHLER_MENGE,
					"fert.losablieferung.error.update.menge");
			put(EJBExceptionLP.FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR,
					"stkl.error.geraetesnr.mengemusseinssein");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_TEILERLEDIGT, "los.error.losteilerledigt");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_AUF_DEM_LOS_IST_MATERIAL_AUSGEGEBEN,
					"los.error.bereitsmaterialausgegben");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN, "los.error.losausgegeben");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT, "los.error.losgestoppt");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_STUECKLISTE_ARBEITSPLAN_WURDE_GEAENDERT,
					"stkl.error.arbeitsplaninstuecklisteveraendert");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_STUECKLISTE_MATERIAL_WURDE_GEAENDERT,
					"stkl.error.stuecklisteveraendert");
			put(EJBExceptionLP.FEHLER_KEINE_BERECHTIGUNG_BELEG_AKTIVIEREN, "lp.error.keinrechtzumaktivieren");
			put(EJBExceptionLP.FEHLER_GERAETESNR_BEREITS_ZUGEBUCHT, "artikel.error.geraetesnrbereitsgebucht");

			put(EJBExceptionLP.ARTIKEL_DEADLOCK, "artikel.error.artieklinuebergeordnetemartikel");
			put(EJBExceptionLP.FEHLER_ARTIKEL_ERSATZARTIKEL_DEADLOCK, "artikel.error.ersatzartikeldeadlock");
			put(EJBExceptionLP.FEHLER_ZUGEBUCHTES_MATERIAL_BEREITS_VOM_LAGER_ENTNOMMEN,
					"lp.error.warenzugangnichtstorniert");
			put(EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT, "bes.bestellvorschlagerstellengesperrt");
			put(EJBExceptionLP.FEHLER_AUSLIEFERVORSCHLAG_IST_GESPERRT, "ls.ausliefervorschlagerstellengesperrt");
			put(EJBExceptionLP.FEHLER_INTERNEBESTELLUNG_IST_GESPERRT, "fert.internebestellungerstellengesperrt");
			put(EJBExceptionLP.FEHLER_ABRECHNUNGSVORSCHLAG_IST_GESPERRT, "rech.abrechnungsvorschlaggesperrt");
			put(EJBExceptionLP.FEHLER_RELATIVE_BUCHUNG_OHNE_KOMMT,
					"zeiterfassung.error.relativezeitbuchungnichtmoeglich");
			put(EJBExceptionLP.FEHLER_RECHNUNG_WERT_DARF_NICHT_NEGATIV_SEIN, "rech.error.wertdarfnichtkleinernullsein");
			put(EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_INS_NAECHSTE_GJ_DATIERT_WERDEN,
					"error.vordatiereninspaeteresgeschaeftsjahr");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_ES_IST_NOCH_MATERIAL_AUSGEGEBEN, "fert.error.nochmaterialausgegeben");
			put(EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_INS_VORLETZTE_GJ_DATIERT_WERDEN,
					"error.vordatiereninzurueckliegendesgeschaeftsjahr");
			put(EJBExceptionLP.FEHLER_FINANZ_ZYKEL_BILANZ, "finanz.error.zykel.bilanzkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_ZYKEL_SKONTO, "finanz.error.zykel.skontokonto");
			put(EJBExceptionLP.FEHLER_ABRUFAUFTRAG_KANN_NICHT_MEHR_VERAENDERT_WERDEN, "auft.error.abruf.aendern");
			put(EJBExceptionLP.FEHLER_FINANZ_ZYKEL_UST, "finanz.error.zykel.ustkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_LIEGT_AUSSERHALB_GUELIGEM_EXPORTZEITRAUM,
					"finanz.error.belegausserhalbgueltigemzeitraum");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_HAT_KEINE_MENGENPOSITIONEN,
					"bes.warning.keinemengenbehaftetenpositionen");
			put(EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_IN_EIN_ANDERES_GJ_UMDATIERT_WERDEN,
					"lp.error.belegdarfnichtinanderesgjumdatiertwerden");
			put(EJBExceptionLP.FEHLER_UNGUELTIGE_EMAILADRESSE, "lp.error.ungueltigeemailadresse");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_AENDERUNG_LOGROESSE_ZUVIELEABLIEFERUNGEN,
					"fert.losgroesseaendern.error");
			put(EJBExceptionLP.FEHLER_UNGUELTIGE_FAXNUMMER, "lp.error.ungueltigefaxnummer");
			put(EJBExceptionLP.FEHLER_SERIENNUMMER_MUSS_UEBER_ALLE_ARTIKEL_EINDEUTIG_SEIN,
					"artikel.error.seriennumereindeutig");
			put(EJBExceptionLP.FEHLER_KUNDE_STANDARDPREISLISTE_HAT_FALSCHE_WAEHRUNG,
					"lp.error.falschewaehrung.kundebeleg");
			put(EJBExceptionLP.FEHLER_ENDSUMME_EXISTIERT, "lp.error.endsummeexistiert");
			put(EJBExceptionLP.FEHLER_ENDSUMME_NICHTVORPREISBEHAFTETERPOSITION, "lp.error.endsummeverschieben");
			put(EJBExceptionLP.FEHLER_RECHNUNG_LIEFERSCHEIN_MUSS_IM_SELBEN_GESCHAEFTSJAHR_LIEGEN,
					"rech.error.lieferscheinundrechnungimgleichengj");
			put(EJBExceptionLP.FEHLER_UNGUELTIGE_ZEITEINGABE, "lp.error.ungueltigezeiteingabe");
			put(EJBExceptionLP.FEHLER_ZUTRITTSOBJEKT_VERWENDUNGSUEBERSCHREITUNG, "lp.error.verwendungsueberschreitung");
			put(EJBExceptionLP.FEHLER_UNGUELTIGE_ZAHLENEINGABE, "lp.error.ungueltigezahleneingabe");
			put(EJBExceptionLP.FEHLER_MEHRERE_LAGERPLAETZE_PRO_LAGER_NICHT_MOEGLICH,
					"artikel.error.mehrerelagerplaetzeprolagernichtmoeglich");
			put(EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT, "fb.error.belegbereitsverbucht");
			put(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_UVAVERPROBUNG, "fb.error.buchungnichterlaubtuva");
			put(EJBExceptionLP.FEHLER_FORECAST_ES_IST_BEREITS_EIN_EIN_IMPORTPFAD_IN_EINER_LIEFERADRESSE_DEFINIERT,
					"fc.kopfdaten.importpfad.error");
			put(EJBExceptionLP.FEHLER_GUTSCHRIFT_WECHSEL_WERTGUTSCHRIFT_FEHLER, "rech.error.wechselwertgutschrift");
			put(EJBExceptionLP.WARNUNG_KUNDEN_UID_NUMMER_NICHT_HINTERLEGT, "lp.error.kundenuidnummerfehlt");
			put(EJBExceptionLP.FEHLER_PARTNER_KOMM_AENDERN_NUR_EIGENER_MANDANT,
					"part.error.nurkommunikationsdatendeseigenenmandantenaendern");
			put(EJBExceptionLP.FEHLER_RECHNUNG_HAT_KEINE_POSITIONEN, "rech.keinepositionen");
			put(EJBExceptionLP.FEHLER_ZUVIELE_LAGERORTE, "lp.error.zuvielelagerorte");
			put(EJBExceptionLP.FEHLER_CUD_TAETIGKEIT_TELEFON_NICHT_ERLAUBT,
					"zeiterfassung.error.taetigkeittelefonkannnichteditiertwerden");
			put(EJBExceptionLP.FEHLER_INVENTUR_ES_DARF_NUR_DAS_LAGER_DER_INVENTUR_VERWENDET_WERDEN,
					"artikel.error.inventur.lager");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_TEXTINKONZERNDATENSPRACHENICHTHINTERLEGT,
					"ls.warning.textkonzerndatensprache");
			put(EJBExceptionLP.FEHLER_VKPF_MENGENSTAFFEL_EXISTIERT, "vkpf.error.datumsbereich");
			put(EJBExceptionLP.FEHLER_UNZUREICHENDE_RECHTE, "lp.unzureichenderechte");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT,
					"rechnung.zahlung.rechnungiststorniert1");
			put(EJBExceptionLP.FEHLER_DRUCKEN_KEINE_DRUCKER_INSTALLIERT, "lp.drucken.keindruckerinstalliert");
			put(EJBExceptionLP.FEHLER_KEINE_BERECHTIUNG_ZUM_BUCHEN_AUF_DIESEM_LAGER,
					"lp.error.lagerbuchung.keineberechtigung");
			put(EJBExceptionLP.FEHLER_NOCLASSDEFFOUNDERROR, "lp.error.noclassdeffounderror");
			put(EJBExceptionLP.FEHLER_THECLIENT_WURDE_GELOESCHT, "lp.error.theclientgeloescht");
			put(EJBExceptionLP.FEHLER_BELEG_IST_BEREITS_AKTIVIERT, "lp.error.belegistbereitsaktiviert");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_ARTIKEL_DARF_NICHT_MEHR_GEAENDERT_WERDEN,
					"lp.error.bespos.artikelnichtmehraenderbar");
			put(EJBExceptionLP.FEHLER_ARTIKELSET_KANN_NICHT_VERSCHOBEN_WERDEN,
					"lp.error.artikelset.kannnichtverschobenwerden");
			put(EJBExceptionLP.FEHLER_ARTIKEL_KANN_NICHT_IN_ARTIKELSET_VERSCHOBEN_WERDEN,
					"lp.error.artike.kannnichtinartikelsetverschobenwerden");
			put(EJBExceptionLP.ARTIKEL_ANZAHLCHARGENNUMMERNNICHTKORREKT, "lp.error.anzahlchargennummernnichtkorrekt");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_PARTNER_UST_LKZ_UNGLEICH_FINANZAMT_LKZ,
					"fb.error.partnerlkzungleichfinanzamtlkz");
			put(EJBExceptionLP.FEHLER_GEBINDE_IN_RAHMEN_UND_ABRUF_NICHT_MOEGLICH,
					"bes.gebindeinrahmenundabrufnichzulaessig");
			put(EJBExceptionLP.FEHLER_SERIENCHARGENNUMMER_ENTHAELT_NICHT_ERLAUBTE_ZEICHEN,
					"artikel.error.snrchnrungueltig");
			put(EJBExceptionLP.FEHLER_ZEITERFASSUNG_FEHLER_ZEITDATEN, "zeiterfassung.error.fehler");
			put(EJBExceptionLP.FEHLER_ABBUCHUNG_SNRCHNR_ABGEBROCHEN, "fert.error.ausgabeabgebrochen");
			put(EJBExceptionLP.FEHLER_MAHNUNGSVERSAND_KEINE_ABSENDERADRESSE,
					"bestellung.fehler.keinemailadressedefiniert");
			put(EJBExceptionLP.FEHLER_TRANSACTION_TIMEOUT, "lp.transaktiontimeout");
			put(EJBExceptionLP.FEHLER_FEHLER_BEIM_DRUCKEN, "lp.druckerfehler");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEINE_IN_VERSCHIEDENE_LAENDER,
					"rech.error.verschiedenelieferscheinlaender");
			put(EJBExceptionLP.FEHLER_JCR_KNOTEN_EXISTIERT_BEREITS, "jcr.error.knotenvorhanden");
			put(EJBExceptionLP.FEHLER_JCR_DATEI_KONNTE_NICHT_GELESEN_WERDEN, "jcr.error.fehlerbeimlesen");
			put(EJBExceptionLP.FEHLER_JCR_KNOTEN_NICHT_GESPEICHERT, "jcr.error.nichtgespeichert");
			put(EJBExceptionLP.FEHLER_JCR_KEINE_AUFTRAEGE_ZU_KOPIEREN, "jcr.error.nichtszukopieren");
			put(EJBExceptionLP.FEHLER_KEIN_EIGENTUMSVORBEHALT_DEFINIERT, "auftrag.eigentumsvorbehalt.definieren");
			put(EJBExceptionLP.FEHLER_KEINE_LIEFERBEDINGUNGEN_DEFINIERT, "auftrag.lieferbedingungen.definieren");
			put(EJBExceptionLP.FEHLER_STEUERSATZ_INNERHALB_UNTERPOSITIONEN_UNGLEICH, "lp.error.steuersaetzeungleich");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_INTERNE_BESTELLUNG_ZU_VIELE_UNTERSTUECKLISTEN,
					"fert.internebestellung.maximaleStuecklistentiefe.erreicht");
			put(EJBExceptionLP.FEHLER_MENGENAENDERUNG_UNTER_ABGERUFENE_MENGE_NICHT_ERLAUBT,
					"auft.fehler.mengenaenderungunterabrufmenge");
			put(EJBExceptionLP.FEHLER_ARTIKELAENDERUNG_BEI_RAHMENPOSUPDATE_NICHT_ERLAUBT,
					"auft.fehler.artikeliidnichtaenderbar");
			put(EJBExceptionLP.FEHLER_RECHNUNG_FAELLIGKEIT_NICHT_BERECHENBAR,
					"rech.fehler.faelligkeitnichtberechenbar");
			put(EJBExceptionLP.FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH,
					"fert.stueckliste.warnung.setartikel");
			put(EJBExceptionLP.FEHLER_STUECKLISTENART_ARTIKELSET_BZW_HILFSSTUECKLISTE_DARF_KEINE_STUECKLISTE_ENTHALTEN,
					"fert.stueckliste.warnung.darfkeinestuecklisteenthalten");
			put(EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT, "lp.default.az.nichtdinfiniert");
			put(EJBExceptionLP.FEHLER_MINDESTBESTELLWERT_ARTIKEL_NICHT_DEFINIERT,
					"lp.mindestbestellwert.artikel.nichtdinfiniert");
			put(EJBExceptionLP.FEHLER_RECHNUNG_NOCH_NICHT_AKTIVIERT, "rech.fehler.nochnichtaktiviert");
			put(EJBExceptionLP.FEHLER_UNTERSCHIEDLICHE_MWST_SAETZE, "rech.unterschiedliche.mwstsaetze");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_NUR_KOPFARTIKEL_ZUBUCHBAR, "bes.error.nurkopfartikel.zubuchbar");
			put(EJBExceptionLP.FEHLER_FINANZ_STORNIEREN_NICHT_MOEGLICH, "finanz.stornieren.error.bereitsverwendet");
			put(EJBExceptionLP.FEHLER_RUNDUNGSARTIKEL_NICHT_DEFINIERT, "lp.error.rundungsartikelnichtdefiniert");
			put(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_VON_KLEINER_BIS, "rech.error.zwsvonkleinerbis");
			put(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_MWSTSATZ_UNTERSCHIEDLICH,
					"rech.error.zwsmwstsatzunterschiedlich");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_LOSNUMMER_NACH_BEREICH_UEBERLAUF, "fert.losnummerberech.ueberlauf");
			put(EJBExceptionLP.FEHLER_KEINE_ANZAHLUNGEN_VORHANDEN, "rech.schlussrechnung.keineanzahlungen");
			put(EJBExceptionLP.FEHLER_ZUSATZKOSTEN_FEHLER_WIEDERHOLUNGERLEDIGT,
					"er.zusatzkosten.fehler.wiederholungerledigt");
			put(EJBExceptionLP.FEHLER_FINANZ_UVA_AUF_GANZES_JAHR_NICHT_ERLAUBT,
					"finanz.error.uva.aufganzesjahrnichterlaubt");
			put(EJBExceptionLP.FEHLER_ZUSATZKOSTEN_FEHLER_WIEDERHOLUNGERLEDIGT,
					"er.zusatzkosten.fehler.wiederholungerledigt");
			put(EJBExceptionLP.FEHLER_KUNDE_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_DEBITOREN,
					"lp.error.zusammenfuehren.partner.unterschiedliche.debitoren");
			put(EJBExceptionLP.FEHLER_LIEFERANT_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_KREDITOREN,
					"lp.error.zusammenfuehren.partner.unterschiedliche.kreditoren");
			put(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_MWST,
					"lp.error.zusammenfuehren.partner.unterschiedliche.mwst");
			put(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_LKZ,
					"lp.error.zusammenfuehren.partner.unterschiedliche.lkz");
			put(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_ABW_UST,
					"lp.error.zusammenfuehren.partner.unterschiedliche.abwustland");
			put(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_UNTERSCHIEDLICHE_UID,
					"lp.error.zusammenfuehren.partner.unterschiedliche.uid");
			put(EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_BANKVERBINDUNG_IN_FIBU_VORHANDEN,
					"lp.error.partner.zusammenfuehren.bankverbindung");
			put(EJBExceptionLP.FEHLER_WERBEABGABEARTIKEL_DARF_NICHT_LAGERBEWIRTSCHAFTET_SEIN,
					"artikel.werbeabgabepflicht.darfnichtlagerbewirtschaftetsein");
			put(EJBExceptionLP.FEHLER_INSERAT_EIN_KUNDE_MUSS_VORHANDEN_SEIN, "iv.inseratkunde.loeschennichtmoeglich");
			put(EJBExceptionLP.FEHLER_EINZELPREIS_NUR_LOESCHBAR_WENN_KEINE_STAFFELN,
					"artikel.lieferant.einzelpreis.loeschen.error");
			// put(EJBExceptionLP.FEHLER_BUCHUNG_ZWISCHEN_VON_BIS,
			// "zeiterfassung.error.buchungzwischenvonbisvorhanden");
			// put(EJBExceptionLP.FEHLER_BUCHUNG_EINFUEGEN_ZWISCHEN_VON_BIS_NICHT_ERLAUBT,
			// "zeiterfassung.error.einfuegenzwischenvonbisnichterlaubt");

			put(EJBExceptionLP.FEHLER_EINHEIT_C_NR_VPE_IN_ARTIKELLIEFERANT_VORHANDEN,
					"artikel.error.bestellmengeneinheiten.bereitsvorhanden");

			put(EJBExceptionLP.FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_DEFINIERT, "part.ansprechpartner.email.error1");
			put(EJBExceptionLP.FEHLER_ANSPRECHPARTNER_EMAIL_NICHT_EINDEUTIG, "part.ansprechpartner.email.error");
			put(EJBExceptionLP.FEHLER_PROJEKTGRUPPE_DEADLOCK, "projekt.gruppe.deadlock");

			put(EJBExceptionLP.FEHLER_PJ18612_BENUTZER_MUSS_AN_MANDANT_002_ANGEMELDET_SEIN, "lp.error.pj18612");
			put(EJBExceptionLP.FEHLER_STKL_MANDANTENWECHSEL_KEINE_BERECHTIGUNG, "stkl.kopfdaten.mandantwechseln.error");
			put(EJBExceptionLP.FEHLER_ZEITMODELLTAGPAUSE_UEBERSCHNEIDEN_SICH,
					"pers.zeitmodelltagpause.error.ueberschneidung");

			put(EJBExceptionLP.FEHLER_VERRECHNUNGSMODELLTAG_UEBERSCHNEIDEN_SICH,
					"pers.verrechnungsmodelltag.error.ueberschneidung");

			put(EJBExceptionLP.FEHLER_PROJEKT_DARF_NICHT_STORNIERT_WERDEN_ZEITEN_VORHANDEN,
					"proj.error.zeitdatenvorhanden");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTES_PROJEKT_NICHT_MOEGLICH,
					"proj.error.zeitbuchungnichtmoeglich.storniert");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_INTERN_ERLEDIGTES_PROJEKT_NICHT_MOEGLICH,
					"proj.error.zeitbuchungnichtmoeglich.internerledigt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_ANGELEGTES_LOS_NICHT_MOEGLICH,
					"fert.error.zeitbuchungnichtmoeglich.angelegt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTES_PROJEKT_NICHT_MOEGLICH,
					"proj.error.zeitbuchungnichtmoeglich.erledigt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTES_LOS_NICHT_MOEGLICH,
					"fert.error.zeitbuchungnichtmoeglich.erledigt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTES_ANGEBOT_NICHT_MOEGLICH,
					"angb.error.zeitbuchungnichtmoeglich.storniert");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTES_ANGEBOT_NICHT_MOEGLICH,
					"angb.error.zeitbuchungnichtmoeglich.erledigt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_GESTOPPTES_LOS_NICHT_MOEGLICH,
					"fert.error.zeitbuchungnichtmoeglich.gestoppt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTES_LOS_NICHT_MOEGLICH,
					"fert.error.zeitbuchungnichtmoeglich.storniert");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_ERLEDIGTER_AUFTRAG_NICHT_MOEGLICH,
					"auft.error.zeitbuchungnichtmoeglich.erledigt");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_STORNIERTER_AUFTRAG_NICHT_MOEGLICH,
					"auft.error.zeitbuchungnichtmoeglich.storniert");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_ANDERN_MANDANT_NACHFUELLEN_MANDANT_KEIN_KUNDE,
					"ls.fuelle.fehlmengen.anderesmandanten.nach.error.mandantkeinkunde");
			put(EJBExceptionLP.FEHLER_LIEFERADRESSE_NUR_AENDERBAR_WENN_KEINE_PREISE_ERFASST,
					"bes.readresse.aendern.error");
			put(EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT, "auft.mandant.hauptlager_fehlt");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_HAT_NEGATIVE_MENGENPOSITIONEN, "bes.aktivieren.negativemengen.error");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_HAT_KEINEN_AUFTRAGSBEZUG, "ls.error.keinauftragsbezug");
			put(EJBExceptionLP.FEHLER_RECHNUNG_UNTERSCHIEDLICHE_MWSTSAETZE_BEI_RC,
					"rech.fehler.gleichermwstsatznotwendigbeireversecharge");
			put(EJBExceptionLP.FEHLER_ES_MUSS_MINDESTENS_EIN_MEILENSTEIN_VORHANDEN_SEIN,
					"auft.zahlungsplan.vorraussetzung.loeschen.error");
			put(EJBExceptionLP.FEHLER_ER_RUECKNAHME_NICHT_MOEGLICH_BEREITS_IN_FIBU,
					"er.mitpositionen.ruecknahme.error");
			put(EJBExceptionLP.FEHLER_FIRMENZEITMODELL_NICHT_VORHANDEN, "pers.firmenzeitmodell.error.nichtdefiniert");
			put(EJBExceptionLP.FEHLER_FIRMENZEITMODELL_NICHT_AUSREICHEND_SOLLZEIT_DEFINIERT,
					"pers.firmenzeitmodell.error.nichtausreichendsollzeit");
			put(EJBExceptionLP.FEHLER_SEPA_STORNIEREN_BEREITS_VERBUCHT, "finanz.sepakontoauszug.error.stornieren");
			put(EJBExceptionLP.FEHLER_EJB, "lp.error.ejb");
			put(EJBExceptionLP.FEHLER_SEPAIMPORT_KEIN_OFFENER_SEPAKONTOAUSZUG,
					"finanz.sepakontoauszug.error.keineoffenersepakontoauszug");
			put(EJBExceptionLP.FEHLER_FORECAST_MEHRERE_FREIGEGEBENE_FORECASTAUFTRAEGE_ZU_EINER_LIEFERADRESSE_VORHANDEN,
					"fc.mehrereforecastauftraege.zueinerlieferadresse.error");
			put(EJBExceptionLP.FEHLER_FORECAST_ES_IST_EIN_ANGELEGTER_NICHT_FREIGEGEBENER_FCAUFTRAG_VORHANDEN,
					"fc.neuanlage.error");
			put(EJBExceptionLP.FEHLER_FREIGABE_BZW_RUECKNAHME_EINES_ERLEDIGTEN_AUFTRAGES_NICHT_MOEGLICH,
					"fc.freigabe.error");
			put(EJBExceptionLP.FEHLER_RUECKNAHME_DER_FREIGABE_NICHT_MOEGLICH_DA_NICHT_FREIGEGEBENER_VORHANDEN,
					"fc.ruecknahme.freigabe.error");
			put(EJBExceptionLP.FEHLER_CHARGENEIGENSCHAFTEN_KOPIEREN_ARTIKELGRUPPEN_UNGLEICH,
					"artikel.warenbewegung.chargeneigenschaften.einfuegen.error.agungleich");
			put(EJBExceptionLP.FEHLER_CHARGENEIGENSCHAFTEN_KOPIEREN_ARTIKEL_NICHT_SNR_CHNR_BEHAFTET,
					"artikel.warenbewegung.chargeneigenschaften.einfuegen.error.nichtsnrchnr");
			put(EJBExceptionLP.FEHLER_DEFAULT_PRO_FIRST_ARTIKELGRUPPE_NICHT_DEFINIERT,
					"lp.default.profirst.artikelgruppe.nichtdinfiniert");

			// put(EJBExceptionLP.FEHLER_KUNDE_GESPERRT, "kunde.gesperrt") ;
			put(EJBExceptionLP.FEHLER_FORECAST_IMPORT_AUFTRAG_MIT_STATUS_ANGELEGT,
					"forecast.import.xls.callof.error.forecastauftragimstatusangelegt");
			put(EJBExceptionLP.FEHLER_MEHRERE_AUFTRAEGE_OHNE_SAMMELLIEFERSCHEIN,
					"rech.rechnungausmehrerenlieferscheinen.error.mehrereauftraege");
			put(EJBExceptionLP.FEHLER_SEPAEXPORT_KEINE_LASTSCHRIFTEN,
					"rechnung.lastschriftvorschlag.error.keinelastschriften");
			put(EJBExceptionLP.FEHLER_RECHNUNG_UNTERSCHIEDLICHE_MWSTSAETZE_BEI_ANZAHLUNG,
					"rech.fehler.gleichermwstsatznotwendigbeianzahlungsrechnung");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KREDITORENKONTO_NICHT_DEFINIERT,
					"finanz.error.kreditorenkontonichtdefiniert");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEFAULT_RABATT_KONTO_FEHLT,
					"finanz.error.sachkontofuerrabattnichtdefiniert");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_ARTIKELGRUPPEN_DEFAULT_KONTO_AR_FEHLT,
					"finanz.error.sachkontofuerdefaultartikelgruppennichtdefiniert");
			put(EJBExceptionLP.FEHLER_FINANZ_FINANZAMT_IM_MANDANT_NITCH_DEFINIERT,
					"finanz.finanzamtimmandantennichtdefiniert");
			put(EJBExceptionLP.FEHLER_STUECKLISTE_UNGUELTIGE_REIHENFOLGE, "stkl.ungueltigeReihenfolge.error");

			put(EJBExceptionLP.FEHLER_EMAIL_SONDERZEITENANTRAG_NICHT_ERMITTELBAR,
					"pers.urlaubsantrag.fehlender.empfaenger");
			put(EJBExceptionLP.FEHLER_MUSS_GROESSER_0_SEIN, "lp.error.menge.kannnichtreduziertwerden");

			put(EJBExceptionLP.FEHLER_ERFASSUNGSFAKTOR_MUSS_GROESSER_0_SEIN, "stkl.erfassungsfaktor.error");

			put(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_KEIN_KUNDE,
					"rech.abrechnungsvorschlag.ueberleiten.keinkunde");
			put(EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_UEBERLEITEN_MEHRERE_KUNDEN,
					"rech.abrechnungsvorschlag.ueberleiten.mehrerekunden");
			put(EJBExceptionLP.FEHLER_ER_UEBERLEITUNG_MEHRERE_ARTIKEL_ODER_AUFSCHLAEGE,
					"rech.abrechnungsvorschlag.ueberleiten.mehrereartikel.aufschlaege");
			put(EJBExceptionLP.FEHLER_REISE_UEBERLEITUNG_MEHRERE_ARTIKEL_FUER_KILOMETER_ODER_SPESEN,
					"rech.abrechnungsvorschlag.ueberleiten.reise.mehrereartikel");
			put(EJBExceptionLP.FEHLER_UMSORTIEREN_INTELLIGENTE_ZWS, "ls.sortierenachartikelnummer.error.zws");
			put(EJBExceptionLP.FEHLER_ZIELLAGER_GLEICH_ABBUCHUNGSLAGER, "ls.ziellagergleichabbuchungslager");
			put(EJBExceptionLP.FEHLER_BETREFF_IST_LEER, "lp.error.versand.leererbetreff");
			put(EJBExceptionLP.FEHLER_AUFTRAG_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
					"lp.error.auftrag.versandwegimpartner.nichtdefiniert");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_IM_PARTNER_NICHT_DEFINIERT,
					"lp.error.lieferschein.versandwegimpartner.nichtdefiniert");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_VERSANDWEG_NICHT_UNTERSTUETZT,
					"lp.error.lieferschein.versandweg.nichtunterstuetzt");
			put(EJBExceptionLP.FEHLER_DOKUMENT_NICHT_IN_PDF_UMWANDELBAR, "lp.error.pdf.nichtumwandelbar");
			put(EJBExceptionLP.FEHLER_FINANZ_KONTOLAND_ZEIGT_AUF_SICH_SELBST,
					"fb.error.kontolandverweistaufsichselbst");
			put(EJBExceptionLP.FEHLER_ARTIKEL_FREIGABE_NUR_WENN_STUECKLISTE_FREIGEGEBEN,
					"artikel.error.freigabe.nurwennstklfreigegeben");
			put(EJBExceptionLP.FEHLER_STK_FREIGABE_RUECKGAENGIG_NUR_WENN_ARTIKEL_NICHT_FREIGEGEBEN,
					"stkl.error.freigabe.ruecknahmenurwennartikelnichtfreigegeben");
			put(EJBExceptionLP.FEHLER_BEVORZUGTER_ARTIKEL_NICHT_MOEGLICH_MEHRERE_SOKOS,
					"artikel.bevorzugt.error.mehreresokos");
			put(EJBExceptionLP.FEHLER_ER_LIEFERANT_FUER_MANDANT_FEHLT, "er.lieferantfehlt");
			put(EJBExceptionLP.FEHLER_GOOGLE_APIKEY_NICHT_DEFINIERT, "geodaten.apikey.nichtdefiniert");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_EINGANGSRECHNUNG_NICHT_VOLLSTAENDIG_KONTIERT,
					"er.kontierung.unvollstaendig");
			put(EJBExceptionLP.FEHLER_FINANZ_KEINE_INTRASTATREGION_DEFINIERT, "fb.error.intrastat.regionnotwendig");
		}
	};

	private static Map<Integer, String> clientInfoErrorMsgs = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(EJBExceptionLP.FEHLER_KONTIERUNG_ZUGEORDNET, "fb.buchungsjournal.export.kontierungaufsteuerkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE, "finanz.error.keinmwstcode");
			put(EJBExceptionLP.FEHLER_ANZAHLUNG_SCHLUSSRECHNUNG_BEREITS_VORHANDEN,
					"rech.anzahlung.schlussrechnungbereitsvorhanden");
			put(EJBExceptionLP.FEHLER_SCHLUSSRECHNUNG_BEREITS_VORHANDEN, "rech.schlussrechnung.bereitsvorhanden");
			put(EJBExceptionLP.FEHLER_SCHLUSSRECHNUNG_BEREITS_VORHANDEN_ANZAHLUNG_DARF_NICHT_MEHR_GEAENDERT_WERDEN,
					"rech.anzahlung.nichtgeaendert.schlussrechnung.bereitsvorhanden");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_ANZAHLUNG_KONTO_NICHT_DEFINIERT,
					"fb.error.keinanzahlungskontodefiniert");
			// put(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
			// "fb.error.keinesteuerkategoriehinterlegt");
			put(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_REVERSE_DEFINIERT,
					"fb.error.keinesteuerkategoriereversehinterlegt");
			put(EJBExceptionLP.FEHLER_AR_ANZAHLUNGEN_REVERSE_CHARGE_ABWEICHEND,
					"rech.error.anzahlungreversechargeabweichend");
			put(EJBExceptionLP.FEHLER_ER_ANZAHLUNGEN_REVERSE_CHARGE_ABWEICHEND,
					"er.error.anzahlungreversechargeabweichend");
			put(EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_LANG, "lp.error.artikelnrzulang");
			put(EJBExceptionLP.FEHLER_VERPACKUNGSEAN_BEREITS_VORHANDEN, "artikel.error.verpackungseanbereitsvorhanden");
			put(EJBExceptionLP.FEHLER_VERKAUFSEAN_BEREITS_VORHANDEN, "artikel.error.verkaufseanbereitsvorhanden");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, "los.error.losbereitserledigt");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN, "los.error.losnichtausgegeben");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT, "los.error.losstorniert");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_LOS_OHNE_KUNDE, "los.error.losohnekunde");
			put(EJBExceptionLP.STUECKLISTE_DEADLOCK, "stkl.error.artikelinuebergeordneterstueckliste");
			put(EJBExceptionLP.FEHLER_CHARGENNUMMER_ZU_KURZ, "artikel.error.chnrzukurz");
			put(EJBExceptionLP.FEHLER_CHARGENNUMMER_ZU_LANG, "artikel.error.chnrzulang");
			put(EJBExceptionLP.FEHLER_SERIENNUMMER_ZU_KURZ, "artikel.error.snrzukurz");
			put(EJBExceptionLP.FEHLER_SERIENNUMMER_ZU_LANG, "artikel.error.snrzulang");
			put(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_VON_KLEINER_EINS, "rech.error.zwsvonkleinereins");
			put(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_BIS_KLEINER_VON, "rech.error.zwsbiskleinervon");
			put(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_POSITIONSNUMMER, "rech.error.zwspositionsnummer");
			put(EJBExceptionLP.FEHLER_FLR_DRUCK_VORLAGE_UNVOLLSTAENDIG, "system.error.fehlerinflrvorlage");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_IN_PROFORMARECHNUNG_DOPPELT_VERRECHNET,
					"rech.error.lsinproformadoppeltverrechnet");
			put(EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGER_BETRAG_ZAHLUNG_VORAUSZAHLUNG,
					"fb.error.zahlungvorauszahlungsbetrag");
			put(EJBExceptionLP.FEHLER_JCR_ROOT_EXISTIERT_NICHT, "lp.error.jcrrootnode");
			put(EJBExceptionLP.FEHLER_RECHNUNG_GS_AUF_ANZAHLUNG_NICHT_MOEGLICH,
					"rech.error.gsaufanzahlungnichtmoeglich.wennfibu");
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_ENTHAELT_VERKETTETE_LIEFERSCHEINE,
					"ls.stornieren.nichtmoeglich.enthaelt.verkettete");
			put(EJBExceptionLP.FEHLER_FINANZ_ABGABEN_UND_IST_VERSTEUERER_NICHT_UNTERSTUETZT,
					"finanz.liquiditaetsvorschau.error.istversteuerer");
			put(EJBExceptionLP.FEHLER_ZEITBUCHUNG_TAETIGKEIT_AUF_PROJEKT_NICHT_MOEGLICH,
					"proj.projekttaetigkeiten.taetigkeit.nichthinterlegt");
			put(EJBExceptionLP.FEHLER_EDIFACT_EXPORTVERZEICHNIS_NICHT_VORHANDEN,
					"lp.edifact.verzeichnisnichtvorhanden");
			put(EJBExceptionLP.FEHLER_IMPORT_DATENTYP_SPALTE_GEWICHT_FALSCH,
					"stk.import.error.datentyp.gewicht.falsch");
			put(EJBExceptionLP.FEHLER_IMPORT_DATENTYP_SPALTE_MENGE_FALSCH, "stk.import.error.datentyp.menge.falsch");
			put(EJBExceptionLP.FEHLER_KUNDE_KOMBINATION_MMZ_MINBW_NICHT_MOEGLICH, "kunde.mmz.minbw.nichtmoeglich");
			put(EJBExceptionLP.FEHLER_FREIER_LIEFERSCHEIN_OHNE_AUFTRAGSBEZUG_AN_ANDEREN_MANDANTEN,
					"ls.error.freierls.ohneauftragsbezug.ananderenmandanten");
			put(EJBExceptionLP.FEHLER_ZWISCHENSUMME_0, "lp.zwischensumme.null.error");
		}
	};

	private static Map<String, IErrorAction> clientInfoErrorActionMsgs = new HashMap<String, IErrorAction>() {
		private static final long serialVersionUID = 1L;

		{
			put("bes_bsmahnung", new BestellpositionMahnungError());
		}
	};

	private static Map<Integer, IErrorAction> infoErrorActionMsgs = new HashMap<Integer, IErrorAction>() {
		private static final long serialVersionUID = 1L;
		{
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO,
					new SteuerkategorieBasisKontoFehltError("finanz.error.keinkursverlustkonto"));
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSGEWINNKONTO,
					new SteuerkategorieBasisKontoFehltError("finanz.error.keinkursgewinnkonto"));
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_ABSTIMMKONTO_FORDERUNGEN_DEFINIERT,
					new SteuerkategorieBasisKontoFehltError("finanz.error.keinforderungenkonto"));
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_ABSTIMMKONTO_VERBINDLICHKEITEN_DEFINIERT,
					new SteuerkategorieBasisKontoFehltError("finanz.error.keinverbindlichkeitenkonto"));
			put(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT, new SteuerkategorieFehltError());
			put(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINITION_VORHANDEN,
					new SteuerkategorieDefinitionError());
			put(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIEKONTO_DEFINITION_VORHANDEN,
					new SteuerkategorieKontoDefinitionFehltError());
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_ERLOESKONTO_EK_DEFINIERT,
					new SteuerkategoriekontoFehltError("finanz.error.keinerloeskontoEK"));
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_EINFUHRUMSATZSTEUERKONTO_DEFINIERT,
					new SteuerkategoriekontoFehltError("fb.error.keineinfuhrumsatzsteuerkontohinterlegt"));
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_DEBITORENKONTO_DEFINIERT, new DebitorKontoError());
			put(EJBExceptionLP.FEHLER_SEPAIMPORT_BUCHUNGEN_MIT_AUSZUGSNUMMER_EXISTIEREN,
					new BuchungenMitAuszugsnummerVorhandenError("finanz.error.buchungenmitauszugsnummervorhanden"));
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_KREDITORENKONTO_DEFINIERT, new KreditorKontoError());
			put(EJBExceptionLP.FEHLER_LIEFERANT_FREMDSYSTEMNUMMER_NICHT_NUMMERISCH,
					new FremdsystemnummerNichtNummerischError());
			put(EJBExceptionLP.FEHLER_FINANZ_STEUERSAETZE_IN_KONTEN_UNTERSCHIEDLICH,
					new BuchungSteuersaetzeInKontenUnterschiedlichError());
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_IN_RECHNUNG_WAEHRUNGEN_UNTERSCHIEDLICH,
					new LsAlsRePosWaehrungenUnterschiedlichError());
			put(EJBExceptionLP.FEHLER_GTIN13_GENERIERUNG_LAENGE_BASISNUMMER_UNGUELTIG,
					new GTINBasisnummerLaengeUngueltigError());
			put(EJBExceptionLP.FEHLER_GTIN13_GENERIERUNG_ALLE_LAUEFENDEN_ARTIKELNUMMERN_VERGEBEN,
					new GTINGenerierungAlleArtikelnummernVergebenError());
			put(EJBExceptionLP.FEHLER_LINIENABRUF_PRODUKTION_ABLIEFERMENGE_KLEINER_EINS,
					new LinienabrufProduktionAbliefermengeKleinerEinsError());
			put(EJBExceptionLP.FEHLER_LINIENABRUF_PRODUKTION_STUECKLISTE_NICHT_IM_MANDANT,
					new LinienabrufProduktionStklNichtImMandantError());
			put(EJBExceptionLP.FEHLER_LINIENABRUF_PRODUKTION_KEIN_ZUGEHOERIGES_LOS_GEFUNDEN,
					new LinienabrufProduktionKeinLosGefundenError());
			put(EJBExceptionLP.FEHLER_LINIENABRUF_PRODUKTION_SOLLSATZGROESSEN_NICHT_GANZZAHLIG,
					new LinienabrufProduktionNichtGanzzahligeSollsatzgroessenError());
			put(EJBExceptionLP.FEHLER_LOSAUSGABE_HTTP_POST_VERBINDUNGSFEHLER,
					new LosausgabeHttpPostVerbindungsfehlerError());
			put(EJBExceptionLP.FEHLER_LOSAUSGABE_HTTP_POST_STATUS_CODE,
					new LosausgabeHttpPostUnerwarteterStatusCodeError());
			put(EJBExceptionLP.FEHLER_LOSERLEDIGUNG_HTTP_POST_VERBINDUNGSFEHLER,
					new LoserledigungHttpPostVerbindungsfehlerError());
			put(EJBExceptionLP.FEHLER_LOSERLEDIGUNG_HTTP_POST_STATUS_CODE,
					new LoserledigungHttpPostUnerwarteterStatusCodeError());
			put(EJBExceptionLP.FEHLER_KUNDE_FREMDSYSTEMNUMMER_NICHT_NUMMERISCH,
					new FremdsystemnummerNichtNummerischError());
			put(EJBExceptionLP.FEHLER_SCRIPT_NICHT_UEBERSETZBAR, new ScriptNichtUebersetzbarError());
			put(EJBExceptionLP.FEHLER_SCRIPT_KLASSE_NICHT_GEFUNDEN, new ScriptKlasseNichtGefundenError());
			put(EJBExceptionLP.FEHLER_SCRIPT_METHODE_NICHT_GEFUNDEN, new ScriptMethodeNichtGefundenError());
			put(EJBExceptionLP.FEHLER_SCRIPT_PARAMETER_MEHRFACH_DEFINIERT, new ScriptParameterMehrfachDefiniertError());
			put(EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT,
					new FehlendeEinheitkonvertierungError());
			put(EJBExceptionLP.FEHLER_SCRIPT_INSTANZIERUNG_NICHT_MOEGLICH, new ScriptInstanzierungNichtMoeglichError());
			put(EJBExceptionLP.FEHLER_SCRIPT_FALSCHE_ZUGRIFFSRECHTE, new ScriptFehlerhafterZugriffError());
			put(EJBExceptionLP.FEHLER_SCRIPT_INSTANZIERUNG_NICHT_MOEGLICH_IO,
					new ScriptInstanzierungNichtMoeglichIOError());
			put(EJBExceptionLP.FEHLER_SCRIPT_METHODE_NICHT_AUFRUFBAR, new ScriptMethodeNichtAufrufbarError());
			put(EJBExceptionLP.FEHLER_SCRIPT_PARAMETER_NICHT_GEFUNDEN, new ScriptParameterNichtGefundenError());
			put(EJBExceptionLP.FEHLER_SCRIPT_INSTANZIERUNG_NICHT_MOEGLICH_VERIFY, new ScriptInstanzierungVerifyError());
			put(EJBExceptionLP.FEHLER_SEPAVERBUCHUNG_BUCHUNGEN_MIT_AUSZUGSNUMMER_EXISTIEREN,
					new BuchungenMitAuszugsnummerVorhandenError(
							"finanz.error.buchungenmitauszugsnummervorhanden.sepaverbuchung"));
			put(EJBExceptionLP.FEHLER_SEPAVERBUCHUNG_KTOAUSZUG_NICHT_IM_AKTUELLEN_GJ,
					new SepakontoauszugNichtImAktuellenGJError());
			put(EJBExceptionLP.FEHLER_SEPAVERBUCHUNG_KTOAUSZUG_FALSCHER_STATUS,
					new SepaVerbuchungAuszugFalscherStatusError());
			put(EJBExceptionLP.FEHLER_UNBEKANNTE_SERIENCHARGENNUMMER, new UnbekannteSerienChargennummer());
			put(EJBExceptionLP.FEHLER_DRUCKEN_FONT_NICHT_GEFUNDEN_CLIENT, new FontAmClientNichtGefundenError());
			put(EJBExceptionLP.FEHLER_DRUCKEN_FONT_NICHT_GEFUNDEN_SERVER, new FontAmServerNichtGefundenError());
			put(EJBExceptionLP.FEHLER_DRUCKEN_UNBEKANNTE_REPORTVARIANTE_ZU_REPORT,
					new ReportvarianteNichtGefundenError());
			put(EJBExceptionLP.FEHLER_FERTIGUNG_SOLLSATZGROESSE_UNTERSCHRITTEN,
					new LosablieferungSollsatzgroesseUnterschrittenError());
			put(EJBExceptionLP.FEHLER_ZUGFERD_LIEFERANTENNUMMER_FEHLT, new ZugferdError());
			put(EJBExceptionLP.FEHLER_ZUGFERD_ADRESSINFO_FEHLT, new ZugferdError());
			put(EJBExceptionLP.FEHLER_ZUGFERD_EINHEIT_MAPPING_NICHT_GEFUNDEN, new ZugferdError());
			put(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_KEIN_ERGEBNIS, new WebabfrageFehlgeschlagenError());
			put(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_MEHRFACHE_ERGEBNISSE,
					new WebabfrageFehlgeschlagenError());
			put(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_UNERWARTETE_RESPONSE,
					new WebabfrageFehlgeschlagenError());
			put(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_RESPONSE_FORBIDDEN,
					new WebabfrageFehlgeschlagenError());
			put(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_UNGUELTIGER_PARAMETER,
					new WebabfrageFehlgeschlagenError());
			put(EJBExceptionLP.FEHLER_DRUCKEN_MAILTEXTVORLAGE_HTML_NICHT_GEFUNDEN,
					new MailtextvorlageNichtGefundenError());
			put(EJBExceptionLP.FEHLER_DRUCKEN_MAILTEXTVORLAGE_NICHT_GEFUNDEN, new MailtextvorlageNichtGefundenError());
			put(EJBExceptionLP.FEHLER_DRUCKEN_MAILTEXTVORLAGE_MIT_SIGNATUR_NICHT_GEFUNDEN,
					new MailtextvorlageNichtGefundenError());
			put(EJBExceptionLP.FEHLER_SEPAVERBUCHUNG_ZAHLUNGEN_MIT_AUSZUGSNUMMER_EXISTIEREN,
					new SepaZahlungenMitAuszugsnummerVorhandenError());
			put(EJBExceptionLP.FEHLER_ARTIKEL_URSPRUNGSLAND_FEHLT, new ArtikelUrsprungslandFehltError());
			put(EJBExceptionLP.FEHLER_TOPS_ARTIKEL_KEINE_HOEHE, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_ARTIKEL_KEIN_MATERIAL, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_ARTIKEL_KEINE_LASEROBERFLAECHE, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_LOS_NICHT_GEFUNDEN, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_KEIN_KUNDE_ZU_LOS, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_KEIN_DEBITORENKONTO, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_KEIN_MATERIAL, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_KEIN_ARBEITSGANG, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_IST_LETZTER_AG, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_XMLMARSHALLING, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_IO_FILEEXPORT_MATERIAL, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_IO_FILEEXPORT_ARTIKEL, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_UPDATE_MATERIAL, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_UPDATE_ARTIKEL, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_KEIN_CADFILE_IN_ARTIKEL_PFAD, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_KEIN_CADFILE_IN_STKL_PFAD, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_KEIN_CADFILE_IN_HILFSSTKL_PFAD, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_LETZTER_EXPORT_PFAD_UNTERSCHIEDLICH, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_IO_SEARCHING_CADFILE, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_IO_READING_METADATEN, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_CADFILE_NICHT_GEFUNDEN, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_UNBEKANNT, new LosausgabeHttpTopsError());
			put(EJBExceptionLP.FEHLER_TOPS_FALLBACK_MEHRERE_CADFILES_PRO_ARTIKEL, new TopsFallbackMehrerePfadeError());
		}
	};

	private static Map<Integer, String> argumentMessages = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_MUSS_GELIEFERT_SEIN, "re.error.lsmussgeliefertsein");
			put(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_AUF_MITLAUFENDES_KONTO_NICHT_ERLAUBT,
					"fb.error.buchungaufmitlaufendeskontonichtmoeglich");
			put(EJBExceptionLP.FEHLER_FINANZ_KONTO_IN_ANDERER_MWST_VERWENDET,
					"fb.error.kontoinsteuerkategorieinanderermwstvorhanden");
			put(EJBExceptionLP.FEHLER_FINANZ_KEINE_KONTONUMMER_FUER_BEREICH_VERFUEGBAR,
					"fb.error.keinekontonummerfuerbereichverfuegbar");
			put(EJBExceptionLP.FEHLER_FINANZ_KONTONUMMER_AUSSERHALB_DEFINITION,
					"fb.error.kontonummerausserhalbdeserlaubtenbereiches");
			put(EJBExceptionLP.FEHLER_POSITION_ZWISCHENSUMME_UNVOLLSTAENDIG, "lp.error.zwsunvollstaendig");
			put(EJBExceptionLP.FEHLER_FINANZ_GESCHAEFTSJAHR_EXISTIERT_NICHT,
					"finanz.error.geschaeftsjahrexistiertnicht");
			put(EJBExceptionLP.FEHLER_AUFTRAG_AKTIVIERT_ABER_KEIN_WERT, "auftrag.error.aktiviertkeinwert");
			put(EJBExceptionLP.FEHLER_BUCHUNG_BEREITS_VORHANDEN, "pers.zeiterfassung.error.buchungbereitsvorhanden");
			put(EJBExceptionLP.FEHLER_BUCHUNG_ZWISCHEN_VON_BIS, "zeiterfassung.error.buchungzwischenvonbisvorhanden");
			put(EJBExceptionLP.FEHLER_BUCHUNG_EINFUEGEN_ZWISCHEN_VON_BIS_NICHT_ERLAUBT,
					"zeiterfassung.error.einfuegenzwischenvonbisnichterlaubt");
			put(EJBExceptionLP.FEHLER_SCRIPT_FEHLERHAFTE_STAMMDATEN, "pers.reisezeiten.script.fehlerhafte.stammdaten");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_FINANZAEMTER_UNTERSCHIEDLICH,
					"fb.error.finanzaemterunterschiedlich");
			put(EJBExceptionLP.FEHLER_DRUCKEN_KEIN_DRUCKERNAME, "lp.error.keindruckernameangegeben");
			put(EJBExceptionLP.FEHLER_FINANZ_STEUERKONTO_MEHRFACH_IN_MWSTSATZ,
					"finanz.error.steuerkontoinmehrerenmwstsatz");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_KONTO_FUER_ARTIKELGRUPPE,
					"fb.error.keinkontofuerartikelgruppe");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KORREKTURBUCHUNG_ZUHOCH, "fb.error.korrekturzuhoch");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_NETTODIFFERENZ_ZUHOCH, "fb.error.nettodifferenzzuhoch");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_PERSONENKONTO_KANN_NICHT_AUTOMATISCH_ERSTELLT_WERDEN,
					"fb.error.finanzamtimmandantnichtdefiniert");
			put(EJBExceptionLP.FEHLER_DRUCKEN_MANDANTENPARAMETER_KEIN_DRUCKERNAME,
					"lp.error.mandantparameter.keindruckernameangegeben");
			put(EJBExceptionLP.FEHLER_DRUCKEN_MANDANTENPARAMETER_UNBEKANNTER_DRUCKERNAME,
					"lp.error.mandantparameter.unbekannterdruckername");
			put(EJBExceptionLP.FEHLER_SQL_EXCEPTION_MIT_INFO, "lp.error.sqlexception");
			put(EJBExceptionLP.FEHLER_FINANZ_SALDOINFO_SOLL_UNGLEICH, "finanz.saldoinfo.ungleich.soll");
			put(EJBExceptionLP.FEHLER_FINANZ_SALDOINFO_HABEN_UNGLEICH, "finanz.saldoinfo.ungleich.haben");
			put(EJBExceptionLP.FEHLER_FINANZ_SALDOINFO_MWSTSATZ_UNGLEICH, "finanz.saldoinfo.ungleich.steuersatz");
			put(EJBExceptionLP.FEHLER_ANGEBOT_ZUSAMMENFASSUNG_POSITIONEN_OHNE_ZWS,
					"angebot.fehler.zusammenfassung.positionohnezws");
			put(EJBExceptionLP.FEHLER_AUFTRAG_ZUSAMMENFASSUNG_POSITIONEN_OHNE_ZWS,
					"auftrag.fehler.zusammenfassung.positionohnezws");

			put(EJBExceptionLP.FEHLER_LIEFERSCHEIN_ARTIKEL_NICHT_IN_FORECAST_AUFTRAG,
					"ls.lspos.error.artikelnichtinforecast");
			put(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE_EXTENDED, "lp.error.duplicateunique.extended");
			put(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT_EXTENDED, "lp.error.nouniqueresults.extended");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_XML_MARSHALLING_EXC, "bes.opentrans.error.xmltransformation");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_XML_TRANSFORMATION_LIEFARTIKELNR_FEHLT,
					"bes.opentrans.error.lieferantennummerfehlt");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_XML_LIEFKUNDENNUMMER_FEHLT,
					"bes.opentrans.error.liefkundennummerfehlt");
			put(EJBExceptionLP.FEHLER_BESTELLUNG_XML_FEHLENDES_EINHEITENMAPPING,
					"bes.opentrans.error.fehlendeseinheitenmapping");
			put(EJBExceptionLP.FEHLER_FORECAST_BESTELLNUMMER_FEHLT, "fc.forecastposition.bestellnummerfehlt");
			put(EJBExceptionLP.FEHLER_FORECAST_MEHRERE_LIEFERADRESSEN_FUER_KUNDE, "fc.fclieferadresse.mehrdeutig");
			put(EJBExceptionLP.FEHLER_UNGUELTIGE_EMAILADRESSE_EXTENDED, "lp.error.ungueltigeemailadresse.extended");
			put(EJBExceptionLP.FEHLER_FORMELSTUECKLISTE_ABBRUCH, "stkl.formelstueckliste.abbruch");
			put(EJBExceptionLP.FEHLER_ES_EXISTIERT_BEREITS_EINE_VERSANDNUMMER,
					"ls.error.versandnummer.existiert.bereits");
			put(EJBExceptionLP.FEHLER_VERSANDNUMMER_NICHT_ERZEUGBAR, "ls.error.versandnummer.nichterzeugbar");
			put(EJBExceptionLP.FEHLER_PLC_PFLICHTFELD_STRASSE_FEHLT, "ls.error.versandnummer.strassefehlt");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_UNGUELTIGE_BUCHUNG_AUF_STEUERKONTO,
					"fb.error.buchungaufsteuerkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_BILANZGRUPPENDEFINITON_POS_NEG,
					"fb.error.bilanzgruppendefinition.falsch.positiv");
			put(EJBExceptionLP.FEHLER_FINANZ_BILANZGRUPPENDEFINITON_NEGATIV,
					"fb.error.bilanzgruppendefinition.falsch.negativ");
			put(EJBExceptionLP.FEHLER_ARTIKEL_URSPRUNGSLAND_FEHLT, "artikel.error.ursprungslandfehlt");
			put(EJBExceptionLP.FEHLER_MASCHINE_DUPLICATE_IDENTIFIKATIONSNR, "maschine.error.doppelteidentifikationsnr");
			put(EJBExceptionLP.FEHLER_MASCHINE_DUPLICATE_IDENTIFIKATIONSNR_ANDERER_MANDANT,
					"maschine.error.doppelteidentifikationsnr.mandant");
			put(EJBExceptionLP.FEHLER_LAND_HAT_KEINE_VORWAHL, "lp.error.landesvorwahlfehlt");
			put(EJBExceptionLP.FEHLER_SEPAIMPORT_ZUVIELE_OFFENE_AR, "fb.sepa.import.error.zuvieloffenerechnungen");
			put(EJBExceptionLP.FEHLER_SEPAIMPORT_ZUVIELE_OFFENE_ER,
					"fb.sepa.import.error.zuvieleoffeneeingangsrechnungen");
			put(EJBExceptionLP.FEHLER_GOOGLE_GEODATEN_ABFRAGE_NICHT_ERFOLGREICH,
					"geodaten.request.fehlerbeidurchfuehrung");
			put(EJBExceptionLP.FEHLER_FERTIGUNG_RESTMENGENLAGER_UNGUELTIG, "fert.error.restmengenlager.invalid");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_RZL_KEIN_UST_LAND_CODE, "fb.export.rzl.keinustlandcode");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_RZL_KEIN_NUMERISCHER_UST_LAND_CODE,
					"fb.export.rzl.keinnumerischerustlandcode");
			put(EJBExceptionLP.FEHLER_FINANZ_STEUERKONTEN_MEHRFACH_VERWENDET2,
					"fb.error.steuerkategoriekonto.mehrfachverwendet");
			put(EJBExceptionLP.FEHLER_FINANZ_INTRASTAT_UID_BENOETIGT, "fb.error.intrastat.uidnotwendig");
			put(EJBExceptionLP.FEHLER_FINANZ_INTRASTAT_ARTIKEL_BENOETIGT_GEWICHT,
					"fb.error.intrastat.artikelgewichtnotwendig");
			put(EJBExceptionLP.FEHLER_FINANZ_INTRASTAT_KEINE_WVK_NR, "finanz.error.wvknichtdefiniert");
			put(EJBExceptionLP.FEHLER_INT_ZWISCHENSUMME_MWSTSATZ_UNTERSCHIEDLICH_ZWSPOS,
					"rech.error.zwsmwstsatzunterschiedlichzwspos");
		}
	};

	private static Map<Integer, String> causeMessages = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_LAND_IM_KUNDEN, "fb.error.keinlandimkunden");
			put(EJBExceptionLP.FEHLER_FINANZ_EXPORT_DEBITORENKONTO_NICHT_DEFINIERT,
					"finanz.error.debitorenkontonichtdefiniert");
			put(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "finanz.error.exportformatnichtimplementiert");
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_ERLOESKONTO_DEFINIERT, "finanz.error.keinerloeskonto");
			// put(EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSVERLUSTKONTO,
			// "finanz.error.keinkursverlustkonto");
			// put(EJBExceptionLP.FEHLER_FINANZ_KEIN_KURSGEWINNKONTO,
			// "finanz.error.keinkursgewinnkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_STEUERKONTO, "finanz.erorr.keinsteuerkonto");
			put(EJBExceptionLP.FEHLER_FINANZ_KEIN_SKONTOKONTO, "finanz.error.keinskontokonto");
			put(EJBExceptionLP.FEHLER_SCRIPT_NICHT_GEFUNDEN, "pers.reisezeiten.kein.script.gefunden");
			put(EJBExceptionLP.FEHLER_SCRIPT_NICHT_AUSFUEHRBAR, "pers.reisezeiten.script.nicht.ausfuehrbar");
			put(EJBExceptionLP.FEHLER_EDIFACT_EXPORTDATEI_NICHT_ERSTELLBAR, "lp.edifact.dateinichterstellbar");
		}
	};

	private static Map<Integer, String> exMessages = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(EJBExceptionLP.FEHLER_HTTP_POST_IO, "lp.error.http.postio");
			put(EJBExceptionLP.FEHLER_KEYSTORE_MANAGMENT, "lp.error.keystore.managment");
			put(EJBExceptionLP.FEHLER_KEYSTORE_RECOVER, "lp.error.keystore.recover");
			put(EJBExceptionLP.FEHLER_KEYSTORE_ALGORITHMEN, "lp.error.keystore.algorithmen");
			put(EJBExceptionLP.FEHLER_KEYSTORE_CERTIFICATE, "lp.error.keystore.certificate");
			put(EJBExceptionLP.FEHLER_KEYSTORE, "lp.error.keystore");
		}
	};

	static private String getMsgFehlerBeimLoeschen(ExceptionLP ec) {
		List<?> info = ec.getAlInfoForTheClient();
		String msg = null;
		if (info != null && info.size() > 1 && info.get(1) instanceof String) {
			IErrorAction action = clientInfoErrorActionMsgs.get(info.get(1));
			if (action != null) {
				msg = action.getMsg(ec);
			}
		}
		if (msg == null) {
			msg = LPMain.getTextRespectUISPr("lp.hint.loeschen");
			if (info != null && info.size() > 0) {
				msg = msg + "\n\n" + ec.getAlInfoForTheClient().get(0);
			}
		}
		return msg;
	}

	protected String getMsgWithoutInfo(ExceptionLP ec) {
		String simpleMsg = simpleErrorMsgs.get(ec.getICode());
		if (null == simpleMsg)
			return simpleMsg;

		return LPMain.getTextRespectUISPr(simpleMsg);
	}

	protected String getMsgWithClientInfo0(ExceptionLP ec) {
		String simpleMsg = clientInfoErrorMsgs.get(ec.getICode());
		if (null == simpleMsg)
			return simpleMsg;

		String sMsg = LPMain.getTextRespectUISPr(simpleMsg);
		List<?> al = ec.getAlInfoForTheClient();
		if (al != null && al.size() > 0) {
			sMsg += " (" + al.get(0) + ")";
		}
		return sMsg;
	}

	protected String getMsgWithCauseMessage(ExceptionLP ec) {
		String errorToken = causeMessages.get(ec.getICode());
		if (errorToken == null)
			return errorToken;

		String sMsg = LPMain.getTextRespectUISPr(errorToken);
		sMsg = sMsg + "\n(" + ec.getCause().getMessage() + ")";
		return sMsg;
	}

	protected String getMsgWithArguments(ExceptionLP ec) {
		String errorToken = argumentMessages.get(ec.getICode());
		if (errorToken == null)
			return errorToken;

		return LPMain.getMessageTextRespectUISPr(errorToken, ec.getAlInfoForTheClient().toArray());
	}

	protected String getMsgWithExceptionMessage(ExceptionLP ec) {
		String errorToken = exMessages.get(ec.getICode());
		if (errorToken == null)
			return errorToken;

		String sMsg = LPMain.getTextRespectUISPr(errorToken);
		sMsg = sMsg + "\n(" + ec.getLocalizedMessage() + ")";
		return sMsg;
	}

	protected String getMsgWithClientInfoErrorAction(ExceptionLP ec) {
		IErrorAction action = infoErrorActionMsgs.get(ec.getICode());
		if (action == null)
			return null;

		return action.getMsg(ec);
	}

	public boolean shouldExceptionDialogBeShown(JDialog dialog, JPanel onPanel, ExceptionLP ec) {
		IErrorAction action = infoErrorActionMsgs.get(ec.getICode());
		if (action == null)
			return false;
		return action.shouldBeShown(dialog, onPanel, ec);
	}

	public String getMsg(ExceptionLP ec) {
		int iCode = ec.getICode();
		String sMsg = null;

		// Einfach Info "Errorcode -> Text"
		sMsg = getMsgWithoutInfo(ec);
		if (null != sMsg)
			return sMsg;

		// ErrorCode -> Text + ClientInfo.get(0)
		sMsg = getMsgWithClientInfo0(ec);
		if (null != sMsg)
			return sMsg;

		sMsg = getMsgWithExceptionMessage(ec);
		if (null != sMsg)
			return sMsg;

		sMsg = getMsgWithCauseMessage(ec);
		if (null != sMsg)
			return sMsg;

		sMsg = getMsgWithArguments(ec);
		if (null != sMsg)
			return sMsg;

		// Die Exception hat eine Liste von ClientInfo'Objekten'.
		sMsg = getMsgWithClientInfoErrorAction(ec);
		if (null != sMsg)
			return sMsg;

		switch (iCode) {
		case EJBExceptionLP.FEHLER_LIEFERSCHEIN_CHN_SNR_ZU_WENIG_AUF_LAGER: {
			sMsg = LPMain.getTextRespectUISPr("lager.error.mengenreduzierungnichtmoeglich");
			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += LPMain.getTextRespectUISPr("lp.gewuenschtemenge") + " (" + al.get(0) + ") ";
				if (al.size() == 3)
					sMsg += LPMain.getTextRespectUISPr("ls.warning.vorhandenemengemitcharge") + al.get(2) + " ("
							+ al.get(1) + ")";
				if (al.size() == 2)
					sMsg += LPMain.getTextRespectUISPr("ls.warning.vorhandenemenge") + "(" + al.get(1) + ")";
			}
			break;
		}
		case EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.zuwenigauflager");
			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 1) {
				try {
					sMsg = LPMain.getMessageTextRespectUISPr("lp.error.zuwenigauflager", al.get(0), al.get(1),
							al.get(2));
				} catch (Throwable ex) {
					ex.printStackTrace();
				}

				// SP1943 Wenn SnrChnr angegeben

				if (al.size() > 3) {
					sMsg += " (" + LPMain.getTextRespectUISPr("lp.seriennrchargennr") + ": " + al.get(3) + ")";
				}

			}
			break;
		}
		case EJBExceptionLP.FEHLER_ARTIKEL_ZEICHEN_IN_ARTIKELNUMMER_NICHT_ERLAUBT: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.zeicheninartikelnummernichterlaubt");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (" + ec.getAlInfoForTheClient().get(1) + " '" + ec.getAlInfoForTheClient().get(0)
						+ "')";
			}

			break;
		}
		case EJBExceptionLP.FEHLER_MEHRERE_MWSTSAETZE_IM_DATUMSBEREICH: {

			Locale locUi = null;
			String cNrWaehrungVon = null;

			try {
				locUi = LPMain.getTheClient().getLocUi();

			} catch (Throwable t2) {
				t2.printStackTrace();
			}

			sMsg = LPMain.getMessageTextRespectUISPr("lp.belegadatum.nichtaenderbar.mehreremwstsaetze",
					Helper.formatDatum((Timestamp) ec.getAlInfoForTheClient().get(0), locUi),
					Helper.formatDatum((Timestamp) ec.getAlInfoForTheClient().get(1), locUi));
			break;
		}
		case EJBExceptionLP.FEHLER_DURCH_ABRECHNUNGSVORSCHLAG_BEREITS_VERRECHNET: {
			sMsg = LPMain.getTextRespectUISPr("rech.mitabrechnungsvorschlag.bereitsverrechnet");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " " + ec.getAlInfoForTheClient().get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_SERIENNUMMER_ENTHAELT_NICHT_NUMERISCHE_ZEICHEN: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.zeicheninsnrnummernichterlaubt");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (" + ec.getAlInfoForTheClient().get(1) + " '" + ec.getAlInfoForTheClient().get(0)
						+ "')";
			}
			break;
		}
		case EJBExceptionLP.FEHLER_FREIGABE_AUFGRUND_FEHLENDER_LOSE_AUF_LINIENABRUFEN_NICHT_MOEGLICH: {
			try {
				sMsg = LPMain.getMessageTextRespectUISPr("fc.freigabe.error.fehlendelose",
						ec.getAlInfoForTheClient().get(0),
						Helper.formatDatum((java.util.Date) ec.getAlInfoForTheClient().get(1),
								LPMain.getTheClient().getLocUi()),
						Helper.formatDatum((java.util.Date) ec.getAlInfoForTheClient().get(2),
								LPMain.getTheClient().getLocUi()));
			} catch (Throwable ex) {
				ex.printStackTrace();
			}

			break;
		}
		case EJBExceptionLP.FEHLER_FERTIGUNG_HILFSSTUECKLISTE_DARF_KEINE_SOLLPOSITION_SEIN: {
			sMsg = LPMain.getTextRespectUISPr("fert.sollarbeitsplan.error.hilsstueckliste");
			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (" + ec.getAlInfoForTheClient().get(0) + ")";
			}
			break;
		}
		case EJBExceptionLP.FEHLER_VMI_IMPORT: {
			if (ec.getAlInfoForTheClient() != null) {
				sMsg = (String) ec.getAlInfoForTheClient().get(0);
			}
			break;
		}
		case EJBExceptionLP.FEHLER_ZEISS_IMPORT: {
			if (ec.getAlInfoForTheClient() != null) {
				sMsg = (String) ec.getAlInfoForTheClient().get(0);
			}
			break;
		}
		case EJBExceptionLP.FEHLER_EPSILON_IMPORT: {
			if (ec.getAlInfoForTheClient() != null) {
				sMsg = (String) ec.getAlInfoForTheClient().get(0);
			}
			break;
		}
		case EJBExceptionLP.FEHLER_ROLLIERENDE_PLANUNG_IMPORT: {
			if (ec.getAlInfoForTheClient() != null) {
				sMsg = (String) ec.getAlInfoForTheClient().get(0);
			}
			break;
		}

		case EJBExceptionLP.FEHLER_FINANZ_ER_MIT_POSITIONEN_NOCH_NICHT_GEDRUCKT: {
			sMsg = LPMain.getTextRespectUISPr("er.mitpositionen.export.error");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (" + ec.getAlInfoForTheClient().get(0) + "')";
			}

			break;
		}
		case EJBExceptionLP.FEHLER_PERSONAL_ZEICHEN_IN_PERSONALNUMMER_NICHT_ERLAUBT: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.zeicheninpersonalnummernichterlaubt");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (" + ec.getAlInfoForTheClient().get(1) + " '" + ec.getAlInfoForTheClient().get(0)
						+ "')";
			}

			break;
		}
		case EJBExceptionLP.FEHLER_FERTIGUNG_NEGATIVE_SOLLMENGE_ARTIKEL_SNR_CNHR_BEHAFTET: {
			sMsg = LPMain.getTextRespectUISPr("fert.error.negativesollmengen.mitsnrchr");
			try {
				if (ec.getAlInfoForTheClient() != null) {
					sMsg = LPMain.getMessageTextRespectUISPr("fert.error.negativesollmengen.mitsnrchr",
							ec.getAlInfoForTheClient().get(0),
							Helper.formatZahl((BigDecimal) ec.getAlInfoForTheClient().get(1), 3,
									LPMain.getTheClient().getLocUi()));
				}
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
			break;
		}
		case EJBExceptionLP.FEHLER_MAXIMALE_PERSONENANZAHL_OHNE_PERSONALMODUL_ERREICHT: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.anzahlpersonenueberschritten");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = LPMain.getMessageTextRespectUISPr("lp.error.anzahlpersonenueberschritten",
						ec.getAlInfoForTheClient().get(0));
			}

			break;
		}

		case EJBExceptionLP.FEHLER_FERTIGUNG_MATERIAL_VOLLSTAENDIG: {
			sMsg = LPMain.getTextRespectUISPr("fert.los.materialvollstaendig.error");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (Los: " + ec.getAlInfoForTheClient().get(0) + ") ";
			}

			break;
		}
		case EJBExceptionLP.ARTIKEL_WECHSEL_SERIENNUMMERNTRAGEND_NICHT_MOEGLICH: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.wechselseriennummerntragendnichtmoeglich");

			if (ec.getAlInfoForTheClient() != null) {
				BelegInfos bi = (BelegInfos) ec.getAlInfoForTheClient().get(0);
				if (bi != null) {
					sMsg += " (" + bi.getBelegart().trim() + " " + bi.getBelegnummer() + ")";
				}

			}

			break;
		}

		case EJBExceptionLP.FEHLER_ARTIKEL_ARTIKELNUMMER_ZU_KURZ: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.artikelnrzukurz");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " (Mindestens " + ec.getAlInfoForTheClient().get(0) + " Stellen | "
						+ ec.getAlInfoForTheClient().get(1) + ") ";
			}

			break;
		}
		case EJBExceptionLP.FEHLER_STUECKLISTE_EIGENGEFERTIGTE_UNTERSTUECKLISTE_MIT_ZIELMENGE_GLEICH_NULL: {
			sMsg = LPMain.getMessageTextRespectUISPr("stkl.error.zielmengenull.beieigengefertigterstueckliste",
					ec.getAlInfoForTheClient().get(0), ec.getAlInfoForTheClient().get(1));

			break;
		}
		case EJBExceptionLP.FEHLER_LOSARBEITSPLAN_GELOESCHT: {
			try {
				sMsg = LPMain.getMessageTextRespectUISPr("fert.arbeitszeitstatus.error",
						ec.getAlInfoForTheClient().get(0), Helper.formatDatumZeit(
								(java.util.Date) ec.getAlInfoForTheClient().get(1), LPMain.getTheClient().getLocUi()));
			} catch (Throwable ex) {
				ex.printStackTrace();
			}

			break;
		}
		case EJBExceptionLP.FEHLER_LIEFERSCHEIN_IST_VERKETTET: {
			sMsg = LPMain.getTextRespectUISPr("ls.stornieren.nichtmoeglich.istverkettet");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + ": " + ec.getAlInfoForTheClient().get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_LIEFERSCHEIN_IST_BEREITS_VERKETTET: {
			sMsg = LPMain.getTextRespectUISPr("ls.verketten.nichtmoeglich.istverkettet");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " " + ec.getAlInfoForTheClient().get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_FIRMENZEITMODELL_BEREITS_VORHANDEN: {
			sMsg = LPMain.getTextRespectUISPr("pers.zeitmodell.error.firmenzeitmodell.bereitsvorhanden");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " " + ec.getAlInfoForTheClient().get(0);
			}

			break;
		}

		case EJBExceptionLP.FEHLER_PARTNERART_DARF_NICHT_GELOESCHT_GEAENDERT_WERDEN: {
			sMsg = LPMain.getTextRespectUISPr("part.error.partnerart.darf.nicht.geloeschtwerden");

			if (ec.getAlInfoForTheClient() != null) {
				sMsg = sMsg + " " + ec.getAlInfoForTheClient().get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_PERSONAL_FEHLER_BEI_EINTRITTSDATUM: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.personalfehlerbeieintrittsdatum");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				if (al.get(0) instanceof Integer) {

					Integer personalIId = (Integer) al.get(0);

					try {
						String sZusatz = new PersonalDelegate().personalFindByPrimaryKey(personalIId).formatAnrede();
						sMsg += " " + sZusatz;

					} catch (Throwable ex) {
						ex.printStackTrace();
					}
				}
			}

			break;
		}
		case EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_ZU_VERRECHNENDER_ARTIKEL_KALKULATORISCH: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.abrechnungsvorschlag.kalkulatorischerartikel");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 1) {

				sMsg = LPMain.getMessageTextRespectUISPr("lp.error.abrechnungsvorschlag.kalkulatorischerartikel",
						al.get(0), al.get(1));

			}

			break;
		}

		case EJBExceptionLP.FEHLER_UNGUELTIGE_WERTE_CSV_IMPORT: {
			sMsg = LPMain.getTextRespectUISPr("artikel.preispflege.import.fehler");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				if (al.get(0) instanceof String) {

					sMsg += " " + al.get(0);

				}
			}

			break;
		}
		case EJBExceptionLP.FEHLER_ER_BEREITS_REISEZEIT_ZUGEORDNET: {
			sMsg = LPMain.getTextRespectUISPr("personal.zusspesen.erbereitszugeordnet");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				if (al.get(0) instanceof Integer) {

					Integer reiseIId = (Integer) al.get(0);

					try {
						ReiseDto rDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
								.reiseFindByPrimaryKey(reiseIId);

						PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
								.personalFindByPrimaryKey(rDto.getPersonalIId());

						sMsg += personalDto.getCKurzzeichen() + " - "
								+ Helper.formatDatumZeit(rDto.getTZeit(), LPMain.getTheClient().getLocUi());

					} catch (Throwable e) {
						e.printStackTrace();
					}

				}
			}

			break;
		}

		case EJBExceptionLP.FEHLER_SNR_ALS_GERAETESERIENNUMMER_VERWENDET: {
			sMsg = LPMain.getTextRespectUISPr("fert.snralsgsnrverwendet");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				if (al.get(0) instanceof String) {

					sMsg += " " + al.get(0);

				}
			}

			break;
		}
		case EJBExceptionLP.FEHLER_RAHMENAUFTRAG_IST_IM_STATUS_ANGELEGT: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("auft.rahmenauftrag.statuswechsel.angelegtteilerledigt.ungueltig",
					al.get(0));

			break;
		}
		case EJBExceptionLP.FEHLER_FERTIGUNG_UPDATE_STKL_SOLLZEIT_ARBEITSPLAN_ANZAHL_UNGLEICH: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr(
					"stkl.bearbeiten.sollzeitenanhand.losistzeitenaktialisieren.error1", al.get(0));

			break;
		}
		case EJBExceptionLP.FEHLER_LIEFERSCHEINPOSITION_MIT_WE_ANDERERMANDANT_VERKNUEPFT: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("ls.lspos.remove.bereitsmitweposverknuepft", al.get(0), al.get(1));

			break;
		}

		case EJBExceptionLP.FEHLER_FERTIGUNG_UPDATE_STKL_SOLLZEIT_ARBEITSPLAN_IN_STKL_NICHT_GEFUNDEN: {
			List<?> al = ec.getAlInfoForTheClient();

			String los = al.get(0) + "";
			String ag = al.get(1) + "";

			String uag = "";
			if (al.get(2) != null) {
				uag = al.get(2) + "";
			}
			String artikel = al.get(3) + "";

			sMsg = LPMain.getMessageTextRespectUISPr(
					"stkl.bearbeiten.sollzeitenanhand.losistzeitenaktialisieren.error2", ag, uag, artikel, los);

			break;
		}

		case EJBExceptionLP.FEHLER_ARTIKEL_DARF_NICHT_ZUGEBUCHT_WERDEN: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("artikel.keinelagerzubuchung.error", al.get(0));
			break;
		}
		case EJBExceptionLP.FEHLER_BESTELLUNG_HAT_ARTIKEL_KEINE_LAGERZUBUCHUNG: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("bs.artikelkeinelagerzubuchung.enthalten.error", al.get(0));
			break;
		}
		case EJBExceptionLP.FEHLER_ZUGEHOERIGE_BESTELLUNG_IST_ERLEDIGT: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("auft.erledigungaufheben.error.zugehoerigebestellung", al.get(0));
			break;
		}

		case EJBExceptionLP.FEHLER_IMPORT_NX_STKL_LAENGE_UNTERSCHIEDLICH: {
			sMsg = LPMain.getTextRespectUISPr("stkl.importnx.laengeunterschiedlich");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += " (" + ((ArtikelDto) al.get(0)).formatArtikelbezeichnung() + ")";
			}

			break;
		}
		case EJBExceptionLP.FEHLER_MMZ_ARTIKEL_DARF_NICHT_LAGERBEWIRTSCHAFTET_SEIN: {

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg = LPMain.getMessageTextRespectUISPr("artikel.mmz.darfnichtlagerbewirtschaftetsein",
						((ArtikelDto) al.get(0)).formatArtikelbezeichnung());

			}

			break;
		}

		case EJBExceptionLP.FEHLER_IMPORT_NX_STKL_UNTERSCHIEDLICH: {
			sMsg = LPMain.getTextRespectUISPr("stkl.importnx.stklposunterschiedlich");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 1) {
				sMsg += " (Stkl: " + ((ArtikelDto) al.get(0)).formatArtikelbezeichnung() + ", Artikel: "
						+ ((ArtikelDto) al.get(1)).formatArtikelbezeichnung() + ")";
			}

		}
			break;
		case EJBExceptionLP.FEHLER_SONDERZEITENIMPORT_DOPPELTER_EINTRAG: {
			sMsg = LPMain.getTextRespectUISPr("pers.sondrzeitenimport.error.doppelt");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += al.get(0);
			}

		}
			break;
		case EJBExceptionLP.FEHLER_BEIM_ERZEUGEN_DER_KREDITOREN_DEBITORENNUMMER: {
			sMsg = LPMain.getTextRespectUISPr("finanz.error.erzeugen.kontonummer");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg = LPMain.getMessageTextRespectUISPr("finanz.error.erzeugen.kontonummer", al.get(0));
			}

		}
			break;
		case EJBExceptionLP.FEHLER_HVMABENUTZER_ANZAHL_UEBERSCHRITTEN: {
			sMsg = LPMain.getMessageTextRespectUISPr("pers.hvmabenutzeranzahl.ueberschritten",
					ec.getAlInfoForTheClient().get(0));

		}
			break;
		case EJBExceptionLP.FEHLER_KALKULATORISCHER_ARTIKEL_MIT_PREIS_UNGLEICH_0: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.kalkulatorischerartikel.mitpreisungleichnull");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += al.get(0);
			}

		}
		case EJBExceptionLP.FEHLER_MATERIALBUCHUNG_AUF_LOS_KOMMT_VON_SICH_SELBST: {
			sMsg = LPMain.getTextRespectUISPr("fert.error.material.kommtvonsichselbst");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 1) {
				sMsg = LPMain.getMessageTextRespectUISPr("fert.error.material.kommtvonsichselbst", al.get(0),
						al.get(1));

			}

		}
			break;
		case EJBExceptionLP.FEHLER_ARTIKEL_IST_NICHT_LAGERBEWIRTSCHAFTET: {
			sMsg = LPMain.getTextRespectUISPr("artikel.error.artikelnichtlagerbewirtschaftet");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += " (" + al.get(0) + ")";
			}

		}
			break;
		case EJBExceptionLP.FEHLER_ARTIKELNUMMER_INCL_HERSTELLERNUMMER_ZU_LANG: {
			sMsg = LPMain.getTextRespectUISPr("artikel.error.artikelnummerinclherstellerzulang");

		}
			break;
		case EJBExceptionLP.FEHLER_ARTIKELNUMMER_ZU_LANG: {
			sMsg = LPMain.getTextRespectUISPr("artikel.error.artikelnummerzulang");

		}
			break;
		case EJBExceptionLP.FEHLER_ZEITBUCHUNG_AUF_FERTIGEN_ARBEITSGANG_NICHT_MOEGLICH: {
			sMsg = LPMain.getTextRespectUISPr("fert.zeitbuchung.ag.fertig.error");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 1) {
				sMsg = LPMain.getMessageTextRespectUISPr("fert.zeitbuchung.ag.fertig.error", al.get(0), al.get(1));
			}

		}
			break;
		case EJBExceptionLP.FEHLER_PARTNER_ZUSAMMENFUEHREN_NICHT_MOEGLICH_PARTNERKOMMENTAR: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("part.zusammenfuehren.error.partnerkommentar", al.get(0));

		}
			break;
		case EJBExceptionLP.FEHLER_AUFTRAG_KEINE_SNRS_AUF_POSITION: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("auft.aktivieren.keinesnrsinposition", al.get(0));

		}
			break;
		case EJBExceptionLP.FEHLER_IN_ZEITDATEN: {
			try {
				List<?> al = ec.getAlInfoForTheClient();
				String s = "";
				if (al != null && al.size() > 1) {
					if (al.get(0) instanceof Integer) {
						PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
								.personalFindByPrimaryKey((Integer) al.get(0));
						s += " (" + personalDto.getCPersonalnr() + " "
								+ personalDto.getPartnerDto().formatFixName2Name1();
						// s += " (" + personalDto.getPartnerDto().
						// formatFixAnredeTitelName2Name1();
					}
					if (al.get(1) instanceof java.sql.Timestamp) {
						s += ", " + Helper.formatDatum((java.sql.Timestamp) al.get(1), LPMain.getTheClient().getLocUi())
								+ ")";
					}

				}
				sMsg = LPMain.getTextRespectUISPr("pers.error.fehlerinzeitdaten") + s;
			} catch (Throwable ex) {
				ex.printStackTrace();
			}

			break;
		}

		case EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT: {
			MessageFormat mf = new MessageFormat(LPMain.getTextRespectUISPr("lp.error.keinwechselkurshinterlegt"));

			Locale locUi = null;
			String cNrWaehrungVon = null;

			try {
				locUi = LPMain.getTheClient().getLocUi();
				cNrWaehrungVon = LPMain.getTheClient().getSMandantenwaehrung();
			} catch (Throwable t2) {
				// @todo boese, boese PJ 5379
			}

			mf.setLocale(locUi);

			List<?> al = ec.getAlInfoForTheClient();
			Object[] pattern = null;
			if (al != null && al.size() > 1) {
				pattern = new Object[] { (String) al.get(0), (String) al.get(1) };
			} else if (al != null && al.size() > 0) {
				pattern = new Object[] { cNrWaehrungVon, (String) al.get(0) };
			} else {
				pattern = new Object[] { cNrWaehrungVon };
			}

			sMsg = mf.format(pattern);

			if (al != null) {
				for (int i = 0; i < al.size(); i++) {
					if (al.get(i) instanceof java.sql.Date) {
						try {
							sMsg += " (" + LPMain.getTextRespectUISPr("lp.waehrung.wechselkurs.pruefzeitpunkt")
									+ Helper.formatDatum((java.sql.Date) al.get(i), LPMain.getTheClient().getLocUi())
									+ ")";
						} catch (Throwable e) {
							// @todo boese, boese PJ 5379
						}
					}
				}
			}

			break;
		}

		case EJBExceptionLP.FEHLER_PERSONAL_URLAUBSBERECHNUNG_ZU_DATUM_KEINE_SOLLZEIT_DEFINIERT: {
			sMsg = LPMain.getTextRespectUISPr("personal.error.keinesollzeitzudatum") + " " + ec.getMessage();
			break;
		}
		case EJBExceptionLP.FEHLER_FINANZ_EXPORT_MEHRERE_FINANZAEMTER: {
			sMsg = LPMain.getTextRespectUISPr("fb.mehrerefinanzaemter");
			sMsg += "\n(" + ec.getCause().getMessage() + ")";
			break;
		}

		case EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_LF_HAT_KEINE_BANKVERBINDUNG: {
			sMsg = LPMain.getTextRespectUISPr("fb.lieferanthatkeinebankverbindung");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}
		case EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_TAETIGKEIT_NICHT_VORHANDEN: {
			sMsg = LPMain.getTextRespectUISPr("stk.profistimport.error.keinazartikel");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}
		case EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_MASCHINE_NICHT_VORHANDEN: {
			sMsg = LPMain.getTextRespectUISPr("stk.profistimport.error.keinemaschine");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}
		case EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_KUNDE_NICHT_VORHANDEN: {
			sMsg = LPMain.getTextRespectUISPr("stk.profistimport.error.keinkunde");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}
		case EJBExceptionLP.FEHLER_KEINE_ENTSPRECHUNG_IN_PRUEFKOMBINATION: {

			sMsg = LPMain.getTextRespectUISPr("stk.pruefkombination.nichtgefunden");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() == 6) {
				sMsg += al.get(0);
				if (al.get(1) != null) {

					sMsg += ", " + LPMain.getTextRespectUISPr("stk.pruefkombination.nichtgefunden.kontakt") + al.get(1);
				}
				if (al.get(2) != null) {

					sMsg += ", " + LPMain.getTextRespectUISPr("stk.pruefkombination.nichtgefunden.litze") + al.get(2);
				}
				if (al.get(3) != null) {

					sMsg += ", " + LPMain.getTextRespectUISPr("stk.pruefkombination.nichtgefunden.verschleissteil")
							+ al.get(3);
				}

				String stueckliste = "";

				if (al.get(4) != null) {

					stueckliste += al.get(4);
				}
				if (al.get(5) != null) {

					stueckliste += " " + al.get(5);
				}

				if (stueckliste.length() > 0) {
					sMsg += " (" + LPMain.getTextRespectUISPr("stkl.stueckliste") + ": " + stueckliste + ")";
				}

			}

			break;

		}

		case EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_BANK_HAT_KEINEN_ORT: {
			sMsg = LPMain.getTextRespectUISPr("fb.bankhatkeinenort");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}

		case EJBExceptionLP.FEHLER_STORNIEREN_ANZAHLUNG_SCHLUSSRECHNUNG_VORHANDEN: {
			sMsg = LPMain.getTextRespectUISPr("rech.anhzahlung.stornieren.error") + " ";
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + item + "\n";
			}
			break;
		}
		case EJBExceptionLP.FEHLER_MATERIALBUCHUNG_NUR_VON_KOMISSIONIERTERMINAL_MOEGLICH: {
			sMsg = LPMain.getMessageTextRespectUISPr("fert.materialbuchung.nurkommissionierterminal",
					ec.getAlInfoForTheClient().get(0), ec.getAlInfoForTheClient().get(1)) + " ";
			break;
		}
		case EJBExceptionLP.FEHLER_LOSAUSGABE_NUR_VON_KOMISSIONIERTERMINAL_MOEGLICH: {
			sMsg = LPMain.getMessageTextRespectUISPr("fert.ausgabe.nurkommissionierterminal",
					ec.getAlInfoForTheClient().get(0)) + " ";
			break;
		}
		case EJBExceptionLP.FEHLER_READRESSE_PROJEKT_BESTELLNUMMER_AENDERN_RECHNUNG_NICHT_IM_STATUS_ANGELEGT: {
			sMsg = LPMain.getTextRespectUISPr("auft.rechnungsadressebestellnummerprojekt.error") + " ";
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + item + "\n";
			}
			break;
		}

		case EJBExceptionLP.FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN: {
			sMsg = LPMain.getTextRespectUISPr("stkl.import.fehler.allgemein");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}
		case EJBExceptionLP.FEHLER_LIEFERSCHEIN_BEREITS_IN_PROFORMARECHNUNG: {
			sMsg = LPMain.getTextRespectUISPr("rech.error.ls.bereitsin.proformarechnung");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}

		case EJBExceptionLP.FEHLER_LOSGUTSCHLECHT_VORHANDEN: {
			sMsg = LPMain.getTextRespectUISPr("pers.zeiterfassung.error.buchungnichtloeschbar");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}
		case EJBExceptionLP.FEHLER_RECHNUNG_HAT_KEINEN_WERT: {
			sMsg = LPMain.getTextRespectUISPr("rechnung.error.keinwert");
			for (Iterator<?> iter = ec.getAlInfoForTheClient().iterator(); iter.hasNext();) {
				String item = (String) iter.next();
				sMsg = sMsg + "\n" + item;
			}
			break;
		}
		case EJBExceptionLP.FEHLER_MENGENREDUZIERUNG_NICHT_MOEGLICH: {
			sMsg = LPMain.getTextRespectUISPr("lager.error.mengenreduzierungnichtmoeglich");
			sMsg += "\n " + LPMain.getTextRespectUISPr("label.ident") + ": " + ec.getAlInfoForTheClient().get(0) + ", "
					+ LPMain.getTextRespectUISPr("artikel.lager.bereitsverbraucht") + ": "
					+ ec.getAlInfoForTheClient().get(1);
			break;
		}

		case EJBExceptionLP.FEHLER_AUSLIEFERVORSCHLAG_BSPOS_ZU_WE_BEREITS_VORHANDEN: {
			sMsg = LPMain.getMessageTextRespectUISPr("bes.lsausanderemmandantezubuchen.error.uk",
					ec.getAlInfoForTheClient().get(0), ec.getAlInfoForTheClient().get(1),
					ec.getAlInfoForTheClient().get(2), ec.getAlInfoForTheClient().get(3),
					ec.getAlInfoForTheClient().get(4));
			break;
		}

		case EJBExceptionLP.FEHLER_RECHNUNG_ZAHLUNGSZIEL_KEINE_TAGE: {
			sMsg = LPMain.getTextRespectUISPr("rechnung.error.zahlungszielhatkeinetage");
			sMsg += ":\n" + LPMain.getTextRespectUISPr("label.zahlungsziel") + ": " + ec.getAlInfoForTheClient().get(0)
					+ "\n" + LPMain.getTextRespectUISPr("lp.rechnung.modulname") + ": "
					+ ec.getAlInfoForTheClient().get(1);
			break;
		}

		case EJBExceptionLP.FEHLER_PRO_FIRST_PARAMETER_PRO_FIRST_FREMDFERTIGUNGSARTIKEL_NICHT_DEFINIERT: {

			if (ec.getAlInfoForTheClient() != null && ec.getAlInfoForTheClient().size() > 0) {
				sMsg = LPMain.getTextRespectUISPr("stkl.profirstimport.error.fremdfertigungsartikelnichtgefunden")
						+ " (" + ec.getAlInfoForTheClient().get(0) + ")";
			} else {
				sMsg = LPMain.getTextRespectUISPr("stkl.profirstimport.error.keinfremdfertigungsartikel");
			}

			break;
		}
		case EJBExceptionLP.FEHLER_DIMENSIONEN_BESTELLEN_ARTIKELNUMMERBEREITSVORHANDEN: {

			sMsg = LPMain.getTextRespectUISPr("bes.dimensionenbestellen.artikelummerbereitsvorhanden");

			if (ec.getAlInfoForTheClient() != null && ec.getAlInfoForTheClient().size() > 0) {
				sMsg += " (" + ec.getAlInfoForTheClient().get(0) + ")";
			}

			break;
		}
		case EJBExceptionLP.FEHLER_ANZAHLUNGSRECHNUNG_BEREITS_VORHANDEN: {

			sMsg = LPMain.getTextRespectUISPr("auft.anzahlungsre.aus.ab.error.bereitsvorhanden");

			if (ec.getAlInfoForTheClient() != null && ec.getAlInfoForTheClient().size() > 0) {
				sMsg += ec.getAlInfoForTheClient().get(0);
			}

			break;
		}

		case EJBExceptionLP.FEHLER_PRO_FIRST_PARAMETER_PRO_FIRST_BILD_PFAD_NICHT_DEFINIERT: {

			sMsg = LPMain.getTextRespectUISPr("stkl.profirstimport.error.keinbildpfad");

			break;
		}

		case EJBExceptionLP.FEHLER_RECHNUNG_POSITIONLS_EXISTIERT: {
			RechnungPositionDto rePosDto;
			RechnungDto rechnungDto = null;
			try {
				rePosDto = DelegateFactory.getInstance().getRechnungDelegate()
						.rechnungPositionFindByLieferscheinIId((Integer) ec.getAlInfoForTheClient().get(0));
				rechnungDto = DelegateFactory.getInstance().getRechnungDelegate()
						.rechnungFindByPrimaryKey(rePosDto.getRechnungIId());
				sMsg = LPMain.getTextRespectUISPr("rechnung.error.lieferscheinpositionexistiert") + ": "
						+ rechnungDto.getCNr();
			} catch (Throwable e) {
				e.printStackTrace();
			}

			break;
		}

		case EJBExceptionLP.FEHLER_BESTELLUNG_FALSCHER_STATUS: {
			sMsg = LPMain.getTextRespectUISPr("bestellung.error.falscherstatus") + " "
					+ ec.getAlInfoForTheClient().get(2) + "\n";
			sMsg = sMsg + LPMain.getTextRespectUISPr("bestellung.error.statusdurchaenderung") + " "
					+ ec.getAlInfoForTheClient().get(0) + "\n";
			sMsg = sMsg + LPMain.getTextRespectUISPr("bestellung.error.statusnachdaten") + "\n"
					+ ec.getAlInfoForTheClient().get(1) + "\n";
			break;
		}

		case EJBExceptionLP.LAGER_SERIENNUMMER_SCHON_VORHANDEN: {
			if (ec.getAlInfoForTheClient() != null && ec.getAlInfoForTheClient().size() > 0) {
				sMsg = LPMain.getTextRespectUISPr("lp.error.seriennummerschonauflager") + ": "
						+ ec.getAlInfoForTheClient().get(0);
			} else {
				sMsg = LPMain.getTextRespectUISPr("lp.error.seriennummerschonauflager");
			}

			break;
		}

		case EJBExceptionLP.FEHLER_SERIENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN: {
			sMsg = LPMain.getTextRespectUISPr("fert.seriennummerngenerator.error") + ": "
					+ ec.getAlInfoForTheClient().get(0);
			break;
		}
		case EJBExceptionLP.FEHLER_CHARGENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN: {
			sMsg = LPMain.getTextRespectUISPr("fert.chargengenerator.error") + ": " + ec.getAlInfoForTheClient().get(0);
			break;
		}
		case EJBExceptionLP.FEHLER_CHARGENNUMMER_NICHT_NUMERISCH: {
			sMsg = LPMain.getTextRespectUISPr("fert.chargennummernichtnumerisch.error") + ": "
					+ ec.getAlInfoForTheClient().get(0);
			break;
		}

		case EJBExceptionLP.FEHLER_BESTELLUNG_WEPOS_PREIS_NOCH_NICHT_ERFASST: {
			sMsg = LPMain.getTextRespectUISPr("bes.error.preisnichterfasst");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += " Lieferscheinnummer: " + al.get(0) + ", Positionsnummer: " + al.get(1);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_KALKULATORISCHER_ARTIKEL_KONNTE_NICHT_VERKETTET_WERDEN: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.kalkulatorischer.artikel.konnte.nichtverkettetwerden");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg = LPMain.getMessageTextRespectUISPr(
						"lp.error.kalkulatorischer.artikel.konnte.nichtverkettetwerden", al.get(0));
			}

			break;
		}
		case EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_ANDERER_MANDANT_LIEFERANT_NICHT_ANGELEGT: {
			sMsg = LPMain.getTextRespectUISPr("bes.error.anderermandant.nichtalslieferantangelegt");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg = al.get(0) + " " + sMsg;
			}

			break;
		}
		case EJBExceptionLP.FEHLER_BESTELLUNG_ANDERER_MANDANT_SET_KANN_NICHT_BESTELLT_WERDEN: {
			sMsg = LPMain.getTextRespectUISPr("bes.anderermandant.artikelistset");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg = sMsg + al.get(0);
			}

			break;
		}

		case EJBExceptionLP.FEHLER_IMPORT_MATERIAL_NICHT_VORHANDEN: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("stk.import.error.material.nichtdefiniert", al.get(0));

			break;
		}
		case EJBExceptionLP.FEHLER_IMPORT_TAETIGKEIT_NICHT_VORHANDEN: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("stk.import.error.taetigkeit.nichtdefiniert", al.get(0));

			break;
		}
		case EJBExceptionLP.FEHLER_LIEFERGUPPE_NICHT_ANGELEGT_IMPORT_ABGEBROCHEN: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("stk.import.error.liefergruppe.nichtdefiniert", al.get(0));

			break;
		}
		case EJBExceptionLP.FEHLER_ER_AUFTRAGSZUORDNUNG_KOPIEREN_ZUVIEL: {
			List<?> al = ec.getAlInfoForTheClient();
			try {
				sMsg = LPMain.getMessageTextRespectUISPr("er.auftragszurdnung.kopieren.zuviel", al.get(0), al.get(1));
			} catch (Throwable e) {
				e.printStackTrace();
			}

			break;
		}
		case EJBExceptionLP.FEHLER_SCHLUSSRECHNUNG_ABWEICHENDER_AUFTRAG_IN_RECHUNNGSPOSITION: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("rech.schlussrechnung.aktivieren.error.unterschiedlicheauftraege1",
					al.toArray());

			break;
		}
		case EJBExceptionLP.FEHLER_SCHLUSSRECHNUNG_ABWEICHENDER_AUFTRAG_IN_LIEFERSCHEIN: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("rech.schlussrechnung.aktivieren.error.unterschiedlicheauftraege2",
					al.toArray());

			break;
		}
		case EJBExceptionLP.FEHLER_SCHLUSSRECHNUNG_ABWEICHENDER_AUFTRAG_IN_LIEFERSCHEINPOSITION: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("rech.schlussrechnung.aktivieren.error.unterschiedlicheauftraege3",
					al.toArray());

			break;
		}
		case EJBExceptionLP.FEHLER_AUFTRAG_AUS_BESTELLUNG_KUNDE_NICHT_ANGELEGT: {
			sMsg = LPMain.getTextRespectUISPr("auft.error.anderermandant.nichtalskundeangelegt");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg = al.get(0) + " " + sMsg;
			}

			break;
		}
		case EJBExceptionLP.FEHLER_ABRECHUNGSVORSCHLAG_ARTIKEL_IN_VERRECHNUNGSMODELL_KALKULATORISCH: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.verrechnungsmodell.kalkulatorischerartikel");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg = LPMain.getMessageTextRespectUISPr("lp.error.verrechnungsmodell.kalkulatorischerartikel",
						al.get(0));
			}

			break;
		}
		case EJBExceptionLP.FEHLER_NEGATIVE_SOLLSTUNDEN_UMWANDELN_URLAUB_BEREITS_VORHANDEN: {
			sMsg = LPMain.getTextRespectUISPr("pers.negativesollstunden.umwandeln.error");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 1) {
				try {
					sMsg = LPMain.getMessageTextRespectUISPr("pers.negativesollstunden.umwandeln.error", al.get(0),
							Helper.formatDatum((java.util.Date) al.get(1), LPMain.getTheClient().getLocUi()));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}

			break;
		}
		case EJBExceptionLP.FEHLER_AUFTRAG_KEIN_VERRECHNUNGSMODELL: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("rech.abrechnungsvorschlag.neuerauftrag.keinverrechnungsmodell",
					al.get(0), al.get(1));
			break;
		}
		case EJBExceptionLP.FEHLER_ZEITDATEN_UEBERLEITUNG_AUFTRAG_KEIN_VERRECHNUNGSMODELL: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr(
					"rech.abrechnungsvorschlag.ueberleitung.auftragkeinverrechnungsmodell", al.get(0));
			break;
		}
		case EJBExceptionLP.FEHLER_ZEITDATEN_UEBERLEITUNG_KUNDE_KEIN_VERRECHNUNGSMODELL: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr(
					"rech.abrechnungsvorschlag.ueberleitung.kundekeinverrechnungsmodell", al.get(0));
			break;
		}
		case EJBExceptionLP.FEHLER_ZEITDATEN_UEBERLEITUNG_MASCHINE_KEIN_VERRECHNUNGSARTIKEL: {
			List<?> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr(
					"rech.abrechnungsvorschlag.ueberleitung.maschinekeinverrechnungsartikel", al.get(0));
			break;
		}
		case EJBExceptionLP.FEHLER_LOSAUSGABE_RUESTMENGE_NICHT_AUF_LAGER: {
			List<?> al = ec.getAlInfoForTheClient();
			try {
				sMsg = LPMain.getMessageTextRespectUISPr("fert.losausgabe.ruestmenge.nichtauflager",
						Helper.formatZahl((BigDecimal) al.get(0), 3, LPMain.getTheClient().getLocUi()), al.get(1),
						al.get(2));
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		case EJBExceptionLP.FEHLER_AUFTRAG_AUS_BESTELLUNG_LIEFERADRESSE_NICHT_ANGELEGT: {
			sMsg = LPMain.getMessageTextRespectUISPr("auft.error.anderermandant.lieferadressenichtalskundeangelegt",
					ec.getAlInfoForTheClient().get(0));

			break;
		}

		case EJBExceptionLP.FEHLER_ZEITEN_BEREITS_ABGESCHLOSSEN: {
			sMsg = LPMain.getTextRespectUISPr("pers.zeiterfassung.zeitenbereitsabgeschlossen.bis");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {

				try {
					MessageFormat mf = new MessageFormat(
							LPMain.getTextRespectUISPr("pers.zeiterfassung.zeitenbereitsabgeschlossen.bis"));
					mf.setLocale(LPMain.getTheClient().getLocUi());

					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(((java.sql.Timestamp) al.get(0)).getTime());
					c.get(Calendar.WEEK_OF_YEAR);
					Object pattern[] = { c.get(Calendar.WEEK_OF_YEAR) };

					sMsg = mf.format(pattern);
				} catch (Throwable e) {
					e.printStackTrace();
				}

			}

			break;
		}
		case EJBExceptionLP.FEHLER_FLRDRUCK_SPALTE_NICHT_VORHANDEN: {
			sMsg = LPMain.getTextRespectUISPr("lp.error.flrdruck.spaltenichtvorhanden");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += " " + al.get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_WERBEABGABEARTIKEL_NICHT_VORHANDEN: {
			sMsg = LPMain.getTextRespectUISPr("artikel.werbeabgabepflichtnichtvorhanden");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += ":  Artikelnummer: " + al.get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_INITIALKOSTENARTIKEL_NICHT_VORHANDEN: {
			sMsg = LPMain.getTextRespectUISPr("artikel.initialkostennichtvorhanden");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += ":  Artikelnummer: " + al.get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_VERPACKUNGSKOSTENARTIKEL_NICHT_VORHANDEN: {
			sMsg = LPMain.getTextRespectUISPr("artikel.verpackungskostenartikelnichtvorhanden");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += ":  Artikelnummer: " + al.get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_IN_REISEZEITEN: {
			sMsg = LPMain.getTextRespectUISPr("pers.error.fehlerinreisezeiten");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 1) {
				sMsg += " " + al.get(0) + ", ";
				try {
					sMsg += Helper.formatDatumZeit((java.util.Date) al.get(1), LPMain.getTheClient().getLocUi());
				} catch (Throwable t2) {
					//
				}
			}

			break;
		}

		case EJBExceptionLP.FEHLER_KEIN_DOKUMENT_BEI_DOKUMENTENPFLICHTIGEM_ARTIKEL_HINTERLEGT: {
			sMsg = LPMain.getTextRespectUISPr("bes.error.keindokument");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += "\r\n" + al.get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_KEIN_DOKUMENT_BEI_DOKUMENTENPFLICHTIGEM_ARTIKEL_IM_LOS_HINTERLEGT: {
			sMsg = LPMain.getTextRespectUISPr("fert.error.keindokument");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += "\r\n" + al.get(0);
			}

			break;
		}

		case EJBExceptionLP.FEHLER_ANZAHLUNGEN_NICHT_BEZAHLT: {
			sMsg = LPMain.getTextRespectUISPr("rech.schlussrechnung.anzahlungennichtbezahlt");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {
				sMsg += "\r\n" + al.get(0);
			}
			break;
		}
		case EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNTEXTE_EINGETRAGEN: {
			sMsg = LPMain.getTextRespectUISPr("finanz.error.mahntextnichtgefunden");

			List<?> al = ec.getAlInfoForTheClient();
			if (al != null && al.size() > 0) {

				sMsg += ":\r\n" + LPMain.getTextRespectUISPr("label.lieferant") + ": " + al.get(0);
			}

			break;
		}
		case EJBExceptionLP.FEHLER_LIEFERSCHEINE_MIT_VERSCHIEDENEN_PROJEKTEN: {
			sMsg = LPMain.getTextRespectUISPr("rech.error.verschiede.projekte");

			List<?> al = ec.getAlInfoForTheClient();
			for (int i = 0; i < al.size(); i++) {

				sMsg += " " + al.get(i);
			}

			break;
		}

		case EJBExceptionLP.FEHLER_FINANZ_GESCHAEFTSJAHR_GESPERRT: {
			Object jahr = null;
			if (ec.getAlInfoForTheClient() != null && ec.getAlInfoForTheClient().size() > 0)
				jahr = ec.getAlInfoForTheClient().get(0);

			sMsg = LPMain.getMessageTextRespectUISPr("finanz.error.geschaeftsjahrgesperrt", jahr);
			break;
		}
		case EJBExceptionLP.FEHLER_ALLE_LOSE_BERUECKSICHTIGEN_UND_SAMMELLIEFERSCHIEN_MEHRERE_AUFTRAEGE: {
			Object auftrag = null;
			if (ec.getAlInfoForTheClient() != null && ec.getAlInfoForTheClient().size() > 0)
				auftrag = ec.getAlInfoForTheClient().get(0);
			MessageFormat mf = new MessageFormat(
					LPMain.getTextRespectUISPr("auftrag.nachkalkulation.allels.andererauftrag.error"));

			Object pattern[] = { auftrag };

			sMsg = mf.format(pattern);
			break;
		}
		case EJBExceptionLP.FEHLER_FERTIGUNG_FERTIGUNGSGRUPPE_SOFORTVERBRAUCH_NICHT_VORHANDEN: {
			sMsg = LPMain.getMessageTextRespectUISPr("fert.error.fertigungsgruppe.sofortverbrauch.nicthvorhanden",
					ec.getMessage());
			break;
		}

		case EJBExceptionLP.FEHLER_ARTIKELLIEFERANT_PREIS_IST_NULL: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("artikel.error.artikellieferant.preisistnull", al.get(0),
					al.get(1));
			break;
		}
		case EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_STKL_NICHT_GEFUNDEN: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("stkl.profirstschachtelplanimport.error.stklnichtgefunden",
					al.get(0));
			break;
		}
		case EJBExceptionLP.FEHLER_BESTELLUNG_SPLITT_WEP_MENGE_ZU_GROSS: {
			List<? extends Object> al = ec.getAlInfoForTheClient();

			sMsg = LPMain.getMessageTextRespectUISPr("bes.bestellposition.split.error", al.get(0), al.get(1));
			break;
		}

		case EJBExceptionLP.FEHLER_ZUORDNUNG_ANSPRECHPARTNER_ZU_PARTNER: {
			List<? extends Object> al = ec.getAlInfoForTheClient();

			sMsg = LPMain.getMessageTextRespectUISPr("lp.ansprechpartner.partner.zugehoerigkeit.error", al.get(0),
					al.get(1));
			break;
		}

		case EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_LOS_GEFUNDEN: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("stkl.profirstschachtelplanimport.error.losnichtgefunden",
					al.get(0));
			break;
		}
		case EJBExceptionLP.FEHLER_EKAGLIERANT_IMPORT_KREDITORENNUMMER_FALSCH: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("agstkl.ekaglieferant.import.error.kreditorennummer", al.get(0),
					al.get(1));
			break;
		}
		case EJBExceptionLP.FEHLER_EKAGLIERANT_IMPORT_MEHRERE_BEFUELLTE_ZEILEN_EINES_ARTIKELS: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("agstkl.ekaglieferant.import.error.kreditorennummer", al.get(0),
					al.get(1));
			break;
		}
		case EJBExceptionLP.FEHLER_INTERNEBESTELLUNG_LOS_BEREITS_VORHANDEN: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("fert.internebestellung.ueberleiten.losvorhanden", al.get(0),
					al.get(1));
			break;
		}
		case EJBExceptionLP.FEHLER_EINKAUFSANGEBOT_VERDICHTEN_TEXTE_ZU_LANG: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("agstkl.einkaufsangebot.positionen.verdichten.error2", al.get(0));
			break;
		}
		case EJBExceptionLP.FEHLER_SET_ARTIKEL_KOPF_DARF_NICHT_ZUGEBUCHT_WERDEN: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("artikel.setartikel.nicht.zubuchbar", al.get(0));
			break;
		}
		case EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_LOS_MIT_AUSREICHENDER_MENGE_GEFUNDEN: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("stkl.profirstschachtelplanimport.error.losnichtausreichend",
					al.get(0), al.get(1));
			break;
		}
		case EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_STKL_IN_PRO_FIRST_NICHT_VORHANDEN: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr(
					"stkl.profirstschachtelplanimport.error.stuecklistenichtinprofirst", al.get(0));
			break;
		}
		case EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_KEIN_MATERIAL_DEFINIERT: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("stkl.profirstschachtelplanimport.error.loskeinmaterial",
					al.get(0), al.get(1));
			break;
		}
		case EJBExceptionLP.FEHLER_KUNDEMATERIAL_NICHT_DEFINIERT: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			sMsg = LPMain.getMessageTextRespectUISPr("artikel.materialzuschlag.metallnotierungdetailliert.error",
					al.get(0), al.get(1), al.get(2));
			break;
		}
		case EJBExceptionLP.FEHLER_PRO_FIRST_SCHACHTELPANIMPORT_RESTMENGE_GROESSER_LAGERMENGE: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			try {
				sMsg = LPMain.getMessageTextRespectUISPr("stkl.profirstschachtelplanimport.error.lagermengekleinerrest",
						al.get(0), Helper.formatZahl((BigDecimal) al.get(1), LPMain.getTheClient().getLocUi()));
			} catch (Throwable t2) {
				//
			}
			break;
		}
		case EJBExceptionLP.FEHLER_AUFTRAG_SON_IMPORT: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			try {
				sMsg = LPMain.getTextRespectUISPr("auft.import.son.error") + "\n";

				if (al != null && al.size() > 0) {
					sMsg += al.get(0);
				}

			} catch (Throwable t2) {
				//
			}
			break;
		}
		case EJBExceptionLP.FEHLER_AUFTRAG_WOOCOMMERCE_IMPORT: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			try {
				sMsg = LPMain.getTextRespectUISPr("auft.import.woocommerce.error") + "\n";

				if (al != null && al.size() > 0) {
					sMsg += al.get(0);
				}

			} catch (Throwable t2) {
				//
			}
			break;
		}
		case EJBExceptionLP.FEHLER_AUFTRAG_VAT_IMPORT: {
			List<? extends Object> al = ec.getAlInfoForTheClient();
			try {
				sMsg = LPMain.getTextRespectUISPr("auft.import.vat.error") + "\n";

				if (al != null && al.size() > 0) {
					sMsg += al.get(0);
				}

			} catch (Throwable t2) {
				//
			}
			break;
		}

		case EJBExceptionLP.FEHLER_BEIM_LOESCHEN: {
			sMsg = getMsgFehlerBeimLoeschen(ec);
			break;
		}

		case EJBExceptionLP.FEHLER_FINANZ_DATUM_NICHT_LETZTER_TAG_DES_MONATS: {
			sMsg = LPMain.getMessageTextRespectUISPr("finanz.error.buchungsdatum.nichtletztertag", ec.getMessage());
			break;
		}

		case EJBExceptionLP.FEHLER_PRO_FIRST_IMPORT_DATENBANKVERBIDNUNG: {
			sMsg = LPMain.getTextRespectUISPr("stk.profistimport.error.db") + "\n" + ec.getSMsg();
			break;
		}

		case EJBExceptionLP.FEHLER_FINANZ_STEUERKONTEN_MEHRFACH_VERWENDET: {
			if (ec.getAlInfoForTheClient() != null) {
				sMsg = "";
				for (KontoVerifierEntry entry : (ArrayList<KontoVerifierEntry>) ec.getAlInfoForTheClient()) {
					sMsg = sMsg + entry.toString() + "\n";
				}
			}

			break;
		}
		case EJBExceptionLP.FEHLER_VERSAND_MONATSABRECHNUNG_ANHAENGE_PFAD: {
			sMsg = LPMain.getMessageTextRespectUISPr("pers.monatsabrechnung.versand.pfad.error",
					ec.getAlInfoForTheClient().get(0));
			break;
		}
		case EJBExceptionLP.FEHLER_BEIM_DRUCKEN: {
			sMsg = getMsgFehlerBeimDrucken(ec);
			break;
		}

		}

		return sMsg;
	}

	private String getMsgFehlerBeimDrucken(ExceptionLP ec) {
		ExceptionLP causeExcLP = null;
		if (ec.getCause() instanceof EJBExceptionLP) {
			causeExcLP = createExcLP((EJBExceptionLP) ec.getCause());
		} else if (ec.getCause() != null && ec.getCause().getCause() instanceof EJBExceptionLP) {
			causeExcLP = createExcLP((EJBExceptionLP) ec.getCause().getCause());
		}

		if (causeExcLP == null) {
			return LPMain.getTextRespectUISPr("lp.drucken.fehlerbeimerstellen");
		}

		String ownMsg = getExceptionalCauseMsgFehlerBeimDrucken(causeExcLP);
		if (ownMsg != null) {
			return ownMsg;
		}

		return LPMain.getMessageTextRespectUISPr("lp.drucken.fehlerbeimerstellen.cause",
				LPMain.getTextRespectUISPr("lp.drucken.fehlerbeimerstellen"), getMsg(causeExcLP));
	}

	private String getExceptionalCauseMsgFehlerBeimDrucken(ExceptionLP exc) {
		if (EJBExceptionLP.FEHLER_DRUCKEN_FONT_NICHT_GEFUNDEN_SERVER == exc.getICode()) {
			return getMsg(exc);
		}
		return null;
	}

	private ExceptionLP createExcLP(EJBExceptionLP ejbExcLP) {
		return new ExceptionLP(ejbExcLP.getCode(), ejbExcLP.getMessage(), ejbExcLP.getAlInfoForTheClient(),
				ejbExcLP.getCause(), ejbExcLP.getExceptionData());
	}
}
