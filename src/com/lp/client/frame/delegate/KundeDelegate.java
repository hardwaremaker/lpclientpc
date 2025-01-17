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
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.kueche.service.KuecheReportFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.KundeKennungDto;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.KundenInfoFuerStrukturAnsicht;
import com.lp.server.partner.service.KundespediteurDto;
import com.lp.server.partner.service.KurzbriefDto;
import com.lp.server.partner.service.MaileSerienbriefResponse;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.partner.service.PartnerkommentarDto;
import com.lp.server.partner.service.PartnerkommentarartDto;
import com.lp.server.partner.service.SerienbriefEmpfaengerDto;
import com.lp.server.partner.service.StatistikParamDto;
import com.lp.server.partner.service.VendidataPartnerExportResult;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <br>Delegate Klasse von Modul Kunde.</p> <p>Copyright Logistik Pur Software
 * GmbH (c) 2004-2008</p> <p>Erstellungsdatum 2004-09-01</p> <p> </p>
 * 
 * @author uli walch
 * 
 * @version $Revision: 1.32 $
 */
public class KundeDelegate extends Delegate {
	private Context context = null;
	private KundeFac kundeFac = null;
	private KundeReportFac kundeReportFac = null;
	private PartnerReportFac partnerReportFac = null;

	public KundeDelegate() throws Exception {
		context = new InitialContext();
		kundeFac = lookupFac(context, KundeFac.class);
		kundeReportFac = lookupFac(context, KundeReportFac.class);
		partnerReportFac = lookupFac(context, PartnerReportFac.class);

	}

	public KundeDto kundeFindByPrimaryKey(Integer iId) throws ExceptionLP {

		KundeDto k = null;
		try {
			k = kundeFac.kundeFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}
	
	public KundeKennungDto kundekennungFindByPrimaryKey(Integer iId) throws ExceptionLP {

		KundeKennungDto k = null;
		try {
			k = kundeFac.kundekennungFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public KundespediteurDto kundespediteurFindByPrimaryKey(Integer iId) throws ExceptionLP {

		KundespediteurDto k = null;
		try {
			k = kundeFac.kundespediteurFindByPrimaryKey(iId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public KundeDto kundeFindByPrimaryKeyOhneExc(Integer iId) throws ExceptionLP {

		KundeDto k = null;
		try {
			k = kundeFac.kundeFindByPrimaryKeyOhneExc(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public KundeDto kundeFindByiIdPartnercNrMandantOhneExc(Integer iIdPartnerI, String cNrMandantI) throws ExceptionLP {

		KundeDto k = null;
		try {
			k = kundeFac.kundeFindByiIdPartnercNrMandantOhneExc(iIdPartnerI, cNrMandantI,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}
	public boolean sindBeiQuellUndZielKundeSokosVorhanden(Integer partnerIIdQuelle, Integer partnerIIdZiel) throws ExceptionLP {

	
		try {
			return kundeFac.sindBeiQuellUndZielKundeSokosVorhanden(partnerIIdQuelle, partnerIIdZiel,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return false;
	}
	public KundeDto[] kundefindByKontoIIdDebitorenkonto(Integer kontoIId) throws ExceptionLP {

		KundeDto[] k = null;
		try {
			k = kundeFac.kundefindByKontoIIdDebitorenkonto(kontoIId);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public ArrayList<KundeDto> holeAlleWurzelkundenFuerStruktursicht(boolean bMitVersteckten) throws ExceptionLP {

		ArrayList<KundeDto> k = null;
		try {
			k = kundeFac.holeAlleWurzelkundenFuerStruktursicht(bMitVersteckten, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public ArrayList<KundenInfoFuerStrukturAnsicht> holeNaechsteEbene(Integer kundeIId, boolean bMitVersteckten)
			throws ExceptionLP {

		ArrayList<KundenInfoFuerStrukturAnsicht> k = null;
		try {
			k = kundeFac.holeNaechsteEbene(kundeIId, bMitVersteckten, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public KundeDto[] kundeFindByVkpfArtikelpreislisteIIdStdpreislisteOhneExc(
			Integer iIdVkpfArtikelpreislisteStandardpreislisteI) throws ExceptionLP {
		KundeDto[] aKundeDto = null;
		try {
			aKundeDto = kundeFac.kundeFindByVkpfArtikelpreislisteIIdStdpreislisteOhneExc(
					iIdVkpfArtikelpreislisteStandardpreislisteI, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return aKundeDto;
	}

	public void updateKunde(KundeDto kundeDtoI) throws ExceptionLP {
		try {
			kundeFac.updateKunde(kundeDtoI, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateKundespediteur(KundespediteurDto dto) throws ExceptionLP {
		try {
			kundeFac.updateKundespediteur(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	public void updateKundeKennung(KundeKennungDto dto) throws ExceptionLP {
		try {
			kundeFac.updateKundeKennung(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public Integer getNextKundennummer() throws ExceptionLP {
		try {
			return kundeFac.getNextKundennummer(LPMain.getInstance().getTheClient());

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	
	public String generateKundennr(Integer kundeIId) throws ExceptionLP {
		try {
			return kundeFac.generateKundennr(kundeIId);

		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createKunde(KundeDto kundeDtoI) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = kundeFac.createKunde(kundeDtoI, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createKundespediteur(KundespediteurDto dto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = kundeFac.createKundespediteur(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createKundeKennung(KundeKennungDto kundeKennungDto) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = kundeFac.createKundeKennung(kundeKennungDto, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public Integer createVerstecktenKundenAusLieferant(Integer iIdLieferant) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = kundeFac.createVerstecktenKundenAusLieferant(iIdLieferant, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}
	
	public Set<Integer> getSpediteurIIdsEinesKunden(Integer kundeIId) throws ExceptionLP {
		Set<Integer> iIds = null;
		try {
			iIds = kundeFac.getSpediteurIIdsEinesKunden(kundeIId, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iIds;
	}

	public Integer createKundeAusPartner(Integer partnerIId) throws ExceptionLP {
		Integer iId = null;
		try {
			iId = kundeFac.createKundeAusPartner(partnerIId, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return iId;
	}

	public void removeKunde(KundeDto kundeDtoI) throws ExceptionLP {
		try {
			kundeFac.removeKunde(kundeDtoI, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeKundespediteur(KundespediteurDto dto) throws ExceptionLP {
		try {
			kundeFac.removeKundespediteur(dto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeKundeKennung(KundeKennungDto dto) throws ExceptionLP {
		try {
			kundeFac.removeKundeKennung(dto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeKundeRechnungsadresse(KundeDto kundeDtoI) throws ExceptionLP {
		try {
			kundeFac.removeKundeRechnungsadresse(kundeDtoI, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void updateKundeRechnungsadresse(KundeDto kundeDtoI) throws ExceptionLP {
		try {
			kundeFac.updateKundeRechnungsadresse(kundeDtoI, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void zusammenfuehrenKunde(KundeDto kundeZielDto, int kundeQuellDtoIId,
			Integer kundePartnerIId) throws ExceptionLP {
		try {
			kundeFac.zusammenfuehrenKunde(kundeZielDto, kundeQuellDtoIId,  kundePartnerIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public JasperPrintLP printKundenstatistik(StatistikParamDto statistikParamDtoI) throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			print = kundeReportFac.printKundenstatistik(statistikParamDtoI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public void pruefeKreditlimit(Integer kundeIId, InternalFrame iframe) throws ExceptionLP {
		try {
			KundeDto kundeDto = kundeFac.kundeFindByPrimaryKey(kundeIId, LPMain.getInstance().getTheClient());

			if (kundeDto.getNKreditlimit() != null) {

				BigDecimal offenRechnung = DelegateFactory.getInstance().getRechnungDelegate()
						.berechneSummeOffenNetto(kundeDto.getIId(), false);

				BigDecimal offenLs = DelegateFactory.getInstance().getLsDelegate()
						.berechneOffenenLieferscheinwert(kundeDto.getIId());

				if (offenRechnung.add(offenLs).doubleValue() > kundeDto.getNKreditlimit().doubleValue()) {

					MessageFormat mf = new MessageFormat(LPMain.getTextRespectUISPr("kunde.kreditlimitueberschritten"));
					mf.setLocale(LPMain.getTheClient().getLocUi());
					Object pattern[] = {
							Helper.formatZahl(kundeDto.getNKreditlimit(), LPMain.getTheClient().getLocUi()) + " "
									+ LPMain.getInstance().getTheClient().getSMandantenwaehrung(),
							Helper.formatZahl(offenRechnung, LPMain.getTheClient().getLocUi()) + " "
									+ LPMain.getInstance().getTheClient().getSMandantenwaehrung(),
							Helper.formatZahl(offenLs, LPMain.getTheClient().getLocUi()) + " "
									+ LPMain.getInstance().getTheClient().getSMandantenwaehrung() };

					// PJ19629
					String sMsg = null;
					if (iframe.bRechtDarfPreiseSehenVerkauf) {
						sMsg = mf.format(pattern);
					} else {
						sMsg = LPMain.getTextRespectUISPr("kunde.kreditlimitueberschritten.ohnewerte");
					}

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"), sMsg);
				}

			}

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public boolean pruefeKreditlimitMitJaNeinFrage(InternalFrame iframe, Integer kundeIId) throws ExceptionLP {
		boolean b = true;
		try {
			KundeDto kundeDto = kundeFac.kundeFindByPrimaryKey(kundeIId, LPMain.getInstance().getTheClient());

			if (kundeDto.getNKreditlimit() != null) {

				BigDecimal offenRechnung = DelegateFactory.getInstance().getRechnungDelegate()
						.berechneSummeOffenNetto(kundeDto.getIId(), false);

				BigDecimal offenLs = DelegateFactory.getInstance().getLsDelegate()
						.berechneOffenenLieferscheinwert(kundeDto.getIId());

				if (offenRechnung.add(offenLs).doubleValue() > kundeDto.getNKreditlimit().doubleValue()) {

					MessageFormat mf = new MessageFormat(LPMain.getTextRespectUISPr("kunde.kreditlimitueberschritten"));
					mf.setLocale(LPMain.getTheClient().getLocUi());
					Object pattern[] = {
							Helper.formatZahl(kundeDto.getNKreditlimit(), LPMain.getTheClient().getLocUi()) + " "
									+ LPMain.getInstance().getTheClient().getSMandantenwaehrung(),
							Helper.formatZahl(offenRechnung, LPMain.getTheClient().getLocUi()) + " "
									+ LPMain.getInstance().getTheClient().getSMandantenwaehrung(),
							Helper.formatZahl(offenLs, LPMain.getTheClient().getLocUi()) + " "
									+ LPMain.getInstance().getTheClient().getSMandantenwaehrung() };

					// PJ19629
					String sMsg = null;
					if (iframe.bRechtDarfPreiseSehenVerkauf) {
						sMsg = mf.format(pattern);
					} else {
						sMsg = LPMain.getTextRespectUISPr("kunde.kreditlimitueberschritten.ohnewerte");
					}

					sMsg += LPMain.getTextRespectUISPr("kunde.kreditlimitueberschritten.zusatz");
					b = DialogFactory.showModalJaNeinDialog(iframe, sMsg);
				}

			}

		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return b;
	}

	public void pruefeKunde(Integer kundeIId, String belegartCNr, InternalFrame internalFrame) throws ExceptionLP {
		pruefeKunde(kundeIId, belegartCNr, false, internalFrame);
	}

	public void pruefeKunde(Integer kundeIId, String belegartCNr, boolean bNurBelegspezifischeHinweise,
			InternalFrame internalFrame) throws ExceptionLP {
		try {
			KundeDto kundeDto = kundeFac.kundeFindByPrimaryKey(kundeIId, LPMain.getInstance().getTheClient());

			String kdName = " (" + kundeDto.getPartnerDto().formatFixName1Name2() + ")";

			if (bNurBelegspezifischeHinweise == false) {
				if (kundeDto.getTLiefersperream() != null) {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.hinweis.kunde") + kdName,
							LPMain.getInstance().getTextRespectUISPr("kund.gesperrt"));
				}
				if (kundeDto.getSHinweisintern() != null) {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.hinweis.kunde") + kdName,
							LPMain.getInstance().getTextRespectUISPr("kund.hinweisintern") + " "
									+ kundeDto.getSHinweisintern());
				}
				if (kundeDto.getSHinweisextern() != null) {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.hinweis.kunde") + kdName,
							LPMain.getInstance().getTextRespectUISPr("kund.hinweisextern") + " "
									+ kundeDto.getSHinweisextern());
				}

				pruefeMahnstufe(kundeIId);
			}

			if (belegartCNr != null) {
				// Partnerhinweise anzeigen
				ArrayList<PartnerkommentarDto> alHinweise = DelegateFactory.getInstance().getPartnerServicesDelegate()
						.getPartnerhinweise(kundeDto.getPartnerIId(), true, belegartCNr);
				for (int i = 0; i < alHinweise.size(); i++) {

					PartnerkommentarartDto pkDto = DelegateFactory.getInstance().getPartnerServicesDelegate()
							.partnerkommentarartFindByPrimaryKey(alHinweise.get(i).getPartnerkommentarartIId());

					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hinweis") + ": " + pkDto.getCBez() + kdName,
							Helper.strippHTML(alHinweise.get(i).getXKommentar()));
				}

				ArrayList<byte[]> bilder = DelegateFactory.getInstance().getPartnerServicesDelegate()
						.getPartnerkommentarBilderUndPDFAlsBilderUmgewandelt(kundeDto.getPartnerIId(), true,
								belegartCNr, PartnerServicesFac.PARTNERKOMMENTARART_HINWEIS);

				if (bilder != null && bilder.size() > 0) {
					DialogFactory.showArtikelHinweisBild(bilder, internalFrame);
				}
			}
			if (bNurBelegspezifischeHinweise == false) {
				if (kundeDto.getTBonitaet() != null) {
					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
							.getParameterDelegate()
							.getParametermandant(ParameterFac.PARAMETER_BONITAETSWARNUNGSZEITRAUM,
									ParameterFac.KATEGORIE_KUNDEN, LPMain.getInstance().getTheClient().getMandant());

					if (parameter.getCWert() != null) {
						int iMonate = (Integer) parameter.getCWertAsObject();
						Calendar c = Calendar.getInstance();
						c = Helper.cutCalendar(c);
						for (int i = 0; i < iMonate; i++) {
							c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
						}
						java.sql.Timestamp t = new java.sql.Timestamp(c.getTimeInMillis());
						if (kundeDto.getTBonitaet().before(t)) {
							DialogFactory.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr("lp.hinweis") + kdName,
									LPMain.getInstance().getTextRespectUISPr("kund.bonitaetspruefungabgelaufen"));
						}
					}
				}
				ParametermandantDto parameterErwerbsberechtigung = (ParametermandantDto) DelegateFactory.getInstance()
						.getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_KUNDE_ERWERBSBERECHTIGUNG_ANZEIGEN,
								ParameterFac.KATEGORIE_KUNDEN, LPMain.getInstance().getTheClient().getMandant());
				boolean bErwerbsberechtigung = (java.lang.Boolean) parameterErwerbsberechtigung.getCWertAsObject();
				if (bErwerbsberechtigung) {
					if (kundeDto.getTErwerbsberechtigung() != null) {
						if (kundeDto.getTErwerbsberechtigung().before(new Timestamp(System.currentTimeMillis()))) {
							DialogFactory.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr("lp.hinweis") + kdName,
									LPMain.getInstance().getTextRespectUISPr("kund.erwerbsberechtigungabgelaufen"));
						}
					}
				}
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}

	}

	public void pruefeMahnstufe(Integer kundeIId) throws ExceptionLP, Throwable {
		ParametermandantDto parameterKundenhinweis = (ParametermandantDto) DelegateFactory.getInstance()
				.getParameterDelegate().getParametermandant(ParameterFac.PARAMETER_KUNDENHINWEIS_AB_MAHNSTUFE,
						ParameterFac.KATEGORIE_KUNDEN, LPMain.getInstance().getTheClient().getMandant());

		if (parameterKundenhinweis.getCWert() != null) {
			int iMahnstufe = (Integer) parameterKundenhinweis.getCWertAsObject();

			if (iMahnstufe > 0) {
				int iAnzahl = DelegateFactory.getInstance().getMahnwesenDelegate()
						.anzahlDerOffenenRechnungenMitMahnstufeGroesser(kundeIId, iMahnstufe);

				if (iAnzahl > 0) {
					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.hinweis.kunde"),
							LPMain.getInstance().getMessageTextRespectUISPr("kunde.hinweis.rechungenmitmahnstufe",
									iAnzahl, iMahnstufe));
				}

			}

		}
	}

	public JasperPrintLP printKurzbrief(KurzbriefDto kurzbriefDtoI, Integer iIdKundeI, boolean bMitLogo)
			throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			print = partnerReportFac.printKurzbrief(kurzbriefDtoI, LPMain.getInstance().getTheClient(), iIdKundeI,
					bMitLogo);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printLastschrift(Integer partnerbankIId, boolean bMitLogo) throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			print = partnerReportFac.printLastschrift(partnerbankIId, bMitLogo, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printPartnerstammblatt(Integer partnerIId) throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			print = partnerReportFac.printPartnerstammblatt(partnerIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printAdressetikett(Integer partnerIId, Integer ansprechparterIId, int iOption)
			throws ExceptionLP {

		try {
			return partnerReportFac.printAdressetikett(partnerIId, ansprechparterIId, iOption,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printNewsletterAenderung(Integer partnerIId) throws ExceptionLP {

		try {
			return partnerReportFac.printNewsletterAenderung(partnerIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printKundenliste(boolean bUmsatzNachRechnungsadresse, boolean bMitVersteckten,
			boolean bMitInteressenten, boolean bMitAnsprechpartner, Integer kundeIIdSelektiert, int iProjektemitdrucken,
			String cPlz, Integer landIId, Integer brancheIId, Integer partnerklasseIId) throws ExceptionLP {

		try {
			return kundeReportFac.printKundenliste(LPMain.getInstance().getTheClient(), bUmsatzNachRechnungsadresse,
					bMitVersteckten, bMitInteressenten, bMitAnsprechpartner, kundeIIdSelektiert, iProjektemitdrucken,
					cPlz, landIId, brancheIId, partnerklasseIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printEmpfaengerliste(Integer serienbriefIId) throws ExceptionLP {

		try {
			return partnerReportFac.printEmpfaengerliste(serienbriefIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public JasperPrintLP printBeauskunftung(Integer partnerIId, Integer beauskunftungIId) throws ExceptionLP {

		try {
			return partnerReportFac.printBeauskunftung(partnerIId, beauskunftungIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MaileSerienbriefResponse maileSerienbrief(Integer serienbriefIId, String sAbsenderEmail) throws ExceptionLP {

		MaileSerienbriefResponse response = new MaileSerienbriefResponse();
		try {

			SerienbriefEmpfaengerDto[] serienbriefEmpfaengerDtos = partnerReportFac
					.getSerienbriefEmpfaenger(serienbriefIId, LPMain.getInstance().getTheClient());

			int i = 0;
			for (SerienbriefEmpfaengerDto dto : serienbriefEmpfaengerDtos) {
				if (dto.isBBekommtEmail()) {
					i++;
				}
			}

			if (DialogFactory.showModalJaNeinDialog(null,
					LPMain.getInstance().getMessageTextRespectUISPr("part.serienbrief.frage.mail.anzahl", i))) {
				response = partnerReportFac.maileSerienbrief(serienbriefIId, serienbriefEmpfaengerDtos, sAbsenderEmail,
						LPMain.getInstance().getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return response;
	}

	public JasperPrintLP druckeSerienbrief(Integer serienbriefIId, boolean bMitLogo) throws ExceptionLP {

		JasperPrintLP print = null;
		try {

			SerienbriefEmpfaengerDto[] serienbriefEmpfaengerDtos = partnerReportFac
					.getSerienbriefEmpfaenger(serienbriefIId, LPMain.getInstance().getTheClient());

			int i = 0;
			for (SerienbriefEmpfaengerDto dto : serienbriefEmpfaengerDtos) {
				if (dto.isBBekommtBrief()) {
					i++;
				}
			}

			if (DialogFactory.showModalJaNeinDialog(null,
					LPMain.getInstance().getMessageTextRespectUISPr("part.serienbrief.frage.print.anzahl", i))) {

				print = partnerReportFac.druckeSerienbrief(serienbriefIId, serienbriefEmpfaengerDtos, bMitLogo,
						LPMain.getInstance().getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public Integer faxeSerienbrief(Integer serienbriefIId, String sAbsender) throws ExceptionLP {
		Integer iAnz = null;
		try {

			SerienbriefEmpfaengerDto[] serienbriefEmpfaengerDtos = partnerReportFac
					.getSerienbriefEmpfaenger(serienbriefIId, LPMain.getInstance().getTheClient());

			int i = 0;
			for (SerienbriefEmpfaengerDto dto : serienbriefEmpfaengerDtos) {
				if (dto.isBBekommtFax()) {
					i++;
				}
			}

			if (DialogFactory.showModalJaNeinDialog(null,
					LPMain.getInstance().getMessageTextRespectUISPr("part.serienbrief.frage.mail.anzahl", i))) {

				iAnz = partnerReportFac.faxeSerienbrief(serienbriefIId, serienbriefEmpfaengerDtos, sAbsender,
						LPMain.getInstance().getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iAnz;
	}

	public JasperPrintLP printLieferstatistik(Integer kundeIId, Integer artikelIId, Integer artikelgruppeIId, Date dVon,
			Date dBis, Integer iSortierung, boolean bMitTexteingaben, boolean bVerdichtetNachArtikel,
			boolean bSortiertNachArtikelgruppe, boolean bEingeschranekt, boolean bMonatsstatistik, int iOptionAdrsse,
			boolean bRechnungsdatum) throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			myLogger.error(
					"KD>Lieferstatistik delegate before: " + (System.currentTimeMillis() - Defaults.lUhrQuickDirtyBS),
					null);
			Defaults.lUhrQuickDirtyBS = System.currentTimeMillis();

			print = kundeReportFac.printLieferStatistik(LPMain.getInstance().getTheClient(), kundeIId, artikelIId,
					artikelgruppeIId, dVon, dBis, iSortierung, bMitTexteingaben, bVerdichtetNachArtikel,
					bSortiertNachArtikelgruppe, bEingeschranekt, bMonatsstatistik, iOptionAdrsse, bRechnungsdatum);
			myLogger.error(
					"KD>Lieferstatistik delegate after: " + (System.currentTimeMillis() - Defaults.lUhrQuickDirtyBS),
					null);

			Defaults.lUhrQuickDirtyBS = System.currentTimeMillis();
			myLogger.error("KD>Lieferstatistik delegate aftertime: " + System.currentTimeMillis(), null);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printKundenstammblatt(Integer kundeIId, boolean bStatistikadresse) throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			print = kundeReportFac.printKundenstammblatt(kundeIId, bStatistikadresse,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printLiefermengen(Integer kundeIId) throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			print = kundeReportFac.printLiefermengen(kundeIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public boolean pruefeObFixpreiseBeiWaehrungsaenderungVorhanden(Integer kundeIId, String waehrungCNr)
			throws ExceptionLP {
		try {
			return kundeFac.pruefeObFixpreiseBeiWaehrungsaenderungVorhanden(kundeIId, waehrungCNr,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return true;
		}

	}

	public JasperPrintLP printKundenpreisliste(Integer kundeIId, Integer artikelgruppeIId, Integer artikelklasseIId,
			boolean bMitInaktiven, String artikelNrVon, String artikelNrBis, boolean bMitVersteckten,
			java.sql.Date datGueltikeitsdatumI, boolean nurSonderkonditionen,
			boolean bMitArtikelbezeichnungenInMandantensprache, Integer shopgruppeIId) throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			print = kundeReportFac.printKundenpreisliste(kundeIId, artikelgruppeIId, artikelklasseIId, bMitInaktiven,
					artikelNrVon, artikelNrBis, bMitVersteckten, datGueltikeitsdatumI, nurSonderkonditionen,
					bMitArtikelbezeichnungenInMandantensprache, shopgruppeIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printWartungsauswertung(java.sql.Timestamp tStichtag, boolean bVerdichtet,
			boolean bSortiertNachArtikellieferant) throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			print = kundeReportFac.printWartungsauswertung(tStichtag, bVerdichtet, bSortiertNachArtikellieferant,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public JasperPrintLP printGeburtstagsliste(java.sql.Timestamp tVon, java.sql.Timestamp tBis) throws ExceptionLP {

		JasperPrintLP print = null;
		try {
			print = partnerReportFac.printGeburtstagsliste(tVon, tBis, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return print;
	}

	public String createDebitorenkontoNummerZuKundenAutomatisch(Integer kundeIId, String kontonummerVorgabe)
			throws ExceptionLP {
		KontoDto k = null;
		try {
			k = kundeFac.createDebitorenkontoZuKundenAutomatisch(kundeIId, false, kontonummerVorgabe,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		if (k == null)
			return null;
		else
			return k.getCNr();
	}

	public KontoDto createDebitorenkontoZuKundenAutomatisch(Integer kundeIId, String kontonummerVorgabe)
			throws ExceptionLP {
		KontoDto k = null;
		try {
			k = kundeFac.createDebitorenkontoZuKundenAutomatisch(kundeIId, true, kontonummerVorgabe,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return k;
	}

	public void pruefeKundenUIDNummer(Integer kundeIId) throws ExceptionLP {
		try {
			kundeFac.pruefeKundenUIDNummer(kundeIId, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void provisionsempfaengerErsetzen(ArrayList personalIId, Integer personalIIdNeu) throws ExceptionLP {
		try {
			kundeFac.provisionsempfaengerErsetzen(personalIId, personalIIdNeu, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public VendidataPartnerExportResult exportiere4VendingKunden(boolean checkOnly) throws ExceptionLP {
		try {
			return kundeFac.exportiere4VendingKunden(checkOnly, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public SerienbriefEmpfaengerDto[] getSerienbriefEmpfaenger(Integer serienbriefIId) throws ExceptionLP {
		try {
			return partnerReportFac.getSerienbriefEmpfaenger(serienbriefIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
	
	public SerienbriefEmpfaengerDto[] getSerienbriefEmpfaenger(Integer serienbriefIId, String localeCNr) throws ExceptionLP {
		try {
			return partnerReportFac.getSerienbriefEmpfaenger(serienbriefIId, localeCNr, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
	
}
