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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.client.remote.IPayloadPublisher;
import com.lp.client.remote.Router;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.ErgebnisgruppeDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.ISepaImportResult;
import com.lp.server.finanz.service.Iso20022BankverbindungDto;
import com.lp.server.finanz.service.Iso20022PaymentsDto;
import com.lp.server.finanz.service.Iso20022StandardDto;
import com.lp.server.finanz.service.Iso20022StandardEnum;
import com.lp.server.finanz.service.KassenbuchDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.KontoImporterResult;
import com.lp.server.finanz.service.KontoRequest;
import com.lp.server.finanz.service.KontolaenderartDto;
import com.lp.server.finanz.service.KontolandDto;
import com.lp.server.finanz.service.RechenregelDto;
import com.lp.server.finanz.service.SepaImportFac;
import com.lp.server.finanz.service.SepaImportProperties;
import com.lp.server.finanz.service.SepaImportTransformResult;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.finanz.service.SepakontoauszugDto;
import com.lp.server.kpi.service.KpiReportFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.BankverbindungId;
import com.lp.server.util.BelegAdapter;
import com.lp.server.util.HvOptional;
import com.lp.server.util.KontoId;
import com.lp.server.util.report.JasperPrintLP;


/**
 * <p><I>Business-Delegate fuer das Finanzmodul</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>02. 09. 2004</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.14 $
 */
public class FinanzDelegate extends Delegate {
	private Context context;
	private FinanzFac finanzFac;
	private SepaImportFac sepaImportFac;
	private KpiReportFac kpiReportFac;
	
	public FinanzDelegate() throws ExceptionLP {

		try {
			context = new InitialContext();
			
			finanzFac = lookupFac(context, FinanzFac.class);	
			sepaImportFac = lookupFac(context, SepaImportFac.class);	
			kpiReportFac = lookupFac(context, KpiReportFac.class);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public KassenbuchDto getHauptkassabuch() throws ExceptionLP {
		try {
			return finanzFac.getHauptkassabuch(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeKonto(KontoDto kontoDto) throws ExceptionLP {
		try {
			finanzFac
					.removeKonto(kontoDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public KontoDto updateKonto(KontoDto kontoDto) throws ExceptionLP {
		myLogger.entry();
		try {
			if (kontoDto.getIId() != null) {
				myLogger.debug("update Konto");
				return finanzFac.updateKonto(kontoDto, 
						LPMain.getTheClient());
			} else {
				myLogger.debug("create Konto");
				return finanzFac.createKonto(kontoDto, LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public KontoDto kontoFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return finanzFac.kontoFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public KontoDtoSmall kontoFindByPrimaryKeySmall(Integer iId)
			throws ExceptionLP {
		try {
			return finanzFac.kontoFindByPrimaryKeySmall(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public KontoDto kontoFindByCnrKontotypMandantOhneExc(String cNr,
			String kontotyp, String mandant) throws ExceptionLP {
		try {
			return finanzFac.kontoFindByCnrKontotypMandantOhneExc(cNr,
					kontotyp, mandant, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public boolean isMitlaufendesKonto(Integer kontoIId) throws ExceptionLP {
		try {
			return finanzFac.isMitlaufendesKonto(kontoIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return false;
		}
	}

	/**
	 *
	 * @param sCnr
	 * @param sMandant
	 * @return
	 * @throws ExceptionLP
	 * @deprecated use #kontoFindByCnrKontotypCnrMandant instead
	 */
	@Deprecated
	public KontoDto kontoFindByCnrMandant(String sCnr, String sMandant)
			throws ExceptionLP {
		try {
			return finanzFac.kontoFindByCnrMandant(sCnr, sMandant,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeFinanzamt(FinanzamtDto finanzamtDto) throws ExceptionLP {
		try {
			finanzFac.removeFinanzamt(finanzamtDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public FinanzamtDto updateFinanzamt(FinanzamtDto finanzamtDto)
			throws ExceptionLP {
		myLogger.entry();
		try {
			if (finanzamtDto.getPartnerIId() != null) {
				return finanzFac.updateFinanzamt(finanzamtDto, LPMain.getTheClient());
			} else {
				return finanzFac.createFinanzamt(finanzamtDto, LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public FinanzamtDto finanzamtFindByPrimaryKey(Integer partnerIId,
			String mandantCNr) throws ExceptionLP {
		try {
			return finanzFac.finanzamtFindByPrimaryKey(partnerIId, mandantCNr,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public FinanzamtDto finanzamtFindByPartnerIIdMandantCNr(Integer partnerIId,
			String mandantCNr) throws ExceptionLP {
		try {
			return finanzFac.finanzamtFindByPartnerIIdMandantCNr(partnerIId,
					mandantCNr, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public FinanzamtDto[] finanzamtFindAll(TheClientDto theClientDto)
			throws ExceptionLP {
		try {
			return finanzFac.finanzamtFindAll(theClientDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeBankverbindung(BankverbindungDto bankverbindungDto)
			throws ExceptionLP {
		try {
			finanzFac.removeBankverbindung(bankverbindungDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public BankverbindungDto updateBankverbindung(
			BankverbindungDto bankverbindungDto) throws ExceptionLP {
		try {
			if (bankverbindungDto.getIId() != null) {
				myLogger.debug("update Bankverbindung");
				return finanzFac.updateBankverbindung(bankverbindungDto, 
						LPMain.getTheClient());
			} else {
				myLogger.debug("create Bankverbindung");
				return finanzFac.createBankverbindung(bankverbindungDto,
						LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BankverbindungDto bankverbindungFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return finanzFac.bankverbindungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public BankverbindungDto bankverbindungFindByKontoIIdOhneExc(
			Integer kontoIId) throws ExceptionLP {
		try {
			return finanzFac.bankverbindungFindByKontoIIdOhneExc(kontoIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void removeKassenbuch(KassenbuchDto kassenbuchDto)
			throws ExceptionLP {
		try {
			finanzFac.removeKassenbuch(kassenbuchDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public KassenbuchDto updateKassenbuch(KassenbuchDto kassenbuchDto)
			throws ExceptionLP {
		try {
			if (kassenbuchDto.getIId() != null) {
				myLogger.debug("update Kassenbuch");
				return finanzFac.updateKassenbuch(kassenbuchDto,
						LPMain.getTheClient());
			} else {
				myLogger.debug("create Kassenbuch");
				return finanzFac.createKassenbuch(kassenbuchDto,
						LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public KassenbuchDto kassenbuchFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return finanzFac.kassenbuchFindByPrimaryKey(iId, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RechenregelDto rechenregelFindByPrimaryKey(String cNr)
			throws ExceptionLP {
		try {
			return finanzFac.rechenregelFindByPrimaryKey(cNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Alle Rechenregeln in der UI-Sprache holen.
	 *
	 * @throws Exception
	 * @return Map
	 */
	public Map<?, ?> getAllRechenregel() {
		/**
		 * @todo vom server holen PJ 4678
		 */
		return new TreeMap();
	}

	public void removeErgebnisgruppe(ErgebnisgruppeDto ergebnisgruppeDto)
			throws ExceptionLP {
		try {
			finanzFac.removeErgebnisgruppe(ergebnisgruppeDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ErgebnisgruppeDto updateErgebnisgruppe(
			ErgebnisgruppeDto ergebnisgruppeDto) throws ExceptionLP {
		try {
			if (ergebnisgruppeDto.getIId() == null) {
				return finanzFac.createErgebnisgruppe(ergebnisgruppeDto, LPMain
						.getTheClient());
			} else {
				return finanzFac.updateErgebnisgruppe(ergebnisgruppeDto, LPMain
						.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ErgebnisgruppeDto ergebnisgruppeFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return finanzFac.ergebnisgruppeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer getAnzahlStellenVonKontoNummer(String kontotypCNr)
			throws ExceptionLP {
		try {
			return finanzFac.getAnzahlStellenVonKontoNummer(kontotypCNr, LPMain
					.getTheClient().getMandant());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Integer getAnzahlDerFinanzaemter() throws ExceptionLP {
		try {
			return finanzFac.getAnzahlDerFinanzaemter(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public KontolaenderartDto createKontolaenderart(
			KontolaenderartDto kontolaenderartDto) throws Exception {
		try {
			return finanzFac.createKontolaenderart(kontolaenderartDto, 
					LPMain.getTheClient());
			} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void removeKontolaenderart(KontolaenderartDto kontolaenderartDto)
			throws Exception {
		try {
			finanzFac.removeKontolaenderart(kontolaenderartDto, LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeKontoland(KontolandDto kontolandDto) throws Exception {
		try {
			finanzFac.removeKontoland(kontolandDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public KontolaenderartDto updateKontolaenderart(
			KontolaenderartDto kontolaenderartDto) throws Exception {
		try {
			return finanzFac.updateKontolaenderart(kontolaenderartDto, LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public void updateKontoland(KontolandDto kontolandDto) throws Exception {
		try {
			finanzFac.updateKontoland(kontolandDto, LPMain
					.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public KontolaenderartDto kontolaenderartFindByPrimaryKey(Integer kontolaenderartId) throws Exception {
		try {
			return finanzFac.kontolaenderartFindByPrimaryKey(kontolaenderartId);
		} catch(Throwable t) {
			handleThrowable(t);
			return null;			
		}
	}
	
	public KontolaenderartDto kontolaenderartFindByPrimaryKey(
			Integer kontoIId,Integer reversechargeartId, 
			String laenderartCNr, Integer finanzamtIId, String mandantCNr) throws Exception {
		try {
			return finanzFac.kontolaenderartFindByPrimaryKey(
				kontoIId, reversechargeartId, laenderartCNr, finanzamtIId, mandantCNr);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public KontolandDto kontolandFindByPrimaryKey(Integer kontolandId) throws Exception {
		try {
			return finanzFac.kontolandFindByPrimaryKey(kontolandId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}		
	}

	public HvOptional<KontolandDto> kontolandZuDatum(Integer kontoIId,
			Integer landIId, Timestamp gueltigZum) throws Exception {
		try {
			return finanzFac.kontolandZuDatum(kontoIId, landIId, gueltigZum);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
	
/*	
	public KontolandDto kontolandFindByPrimaryKey(Integer kontoIId,
			Integer LandIId) throws Exception {
		try {
			return finanzFac.kontolandFindByPrimaryKey(kontoIId, LandIId);
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
*/
	
	public void updateKontoDtoUIDaten(Integer kontoIIdI,
			String cLetztesortierungI, Integer iLetzteselektiertebuchungI)
			throws ExceptionLP {
		try {
			finanzFac.updateKontoDtoUIDaten(kontoIIdI, cLetztesortierungI,
					iLetzteselektiertebuchungI);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public void vertauscheErgebnisgruppen(Integer iIdEG1I, Integer iIdEG2I)
			throws ExceptionLP {
		try {
			finanzFac.vertauscheErgebnisgruppen(iIdEG1I, iIdEG2I, 
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public FinanzamtDto[] finanzamtFindAllByMandantCNr(TheClientDto theClientDto)
			throws ExceptionLP {
		try {
			return finanzFac.finanzamtFindAllByMandantCNr(theClientDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Handelt es sich beim angegebenen Konto um eines welches einen
	 * Vorperiodensaldo unterstuetzt
	 *
	 * @param kontoIId
	 * @param theClientDto
	 * @return true wenn das Konto einen Vorperiodensaldo kennt. Also
	 *         beispielsweise Bankkonto oder Kassenbuch
	 */
	public boolean isKontoMitSaldo(Integer kontoIId) throws ExceptionLP {
		try {
			return finanzFac.isKontoMitSaldo(kontoIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return false;
		}
	}

	public KassenbuchDto kassenbuchFindByKontoIIdOhneExc(Integer kontoIId)
			throws ExceptionLP {
		try {
			return finanzFac.kassenbuchFindByKontoIIdOhneExc(kontoIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public KontoImporterResult importCsv(List<String[]> allLines,
			boolean checkOnly) throws ExceptionLP {
		try {
			return finanzFac.importCsv(allLines, checkOnly,
					LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public KontoRequest[] kontoExist(String mandant, KontoRequest ... kontoRequest) throws ExceptionLP {
		try {
			return finanzFac.kontoExist(mandant, kontoRequest);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public SepaImportTransformResult readAndTransformSepaKontoauszug(SepaImportProperties importProperties) throws ExceptionLP {
		try {
			return sepaImportFac.readAndTransformSepaKontoauszug(importProperties, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
	
	public List<ISepaImportResult> searchForImportMatches(KontoId kontoId, List<SepaKontoauszug> ktoauszuege) 
			throws ExceptionLP {
		try {
			return sepaImportFac.searchForImportMatches(kontoId, ktoauszuege, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public List<BankverbindungDto> bankverbindungFindByMandantCNrOhneExc(
			String mandantCNr) throws ExceptionLP {
		try {
			return finanzFac.bankverbindungFindByMandantCNrOhneExc(mandantCNr);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public Integer importSepaImportResults(List<ISepaImportResult> results) 
			throws ExceptionLP {
		try {
			return sepaImportFac.importSepaImportResults(results, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
	
	public List<BelegAdapter> getAlleOffenenEingangsrechnungen(String waehrungCnr) 
			throws ExceptionLP {
		try {
			return sepaImportFac.getAlleOffenenEingangsrechnungen(waehrungCnr, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public List<BelegAdapter> getAlleOffenenAusgangsrechnungen(String waehrungCnr) 
			throws ExceptionLP {
		try {
			return sepaImportFac.getAlleOffenenAusgangsrechnungen(waehrungCnr, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public Map<Integer, PartnerDto> getPartnerOfBelegeMap(List<BelegAdapter> belege) 
			throws ExceptionLP {
		try {
			return sepaImportFac.getPartnerOfBelegeMap(belege, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	public BigDecimal getSaldoVonBankverbindungByAuszug(Integer kontoIId,
			Integer auszugNr) throws ExceptionLP {
		try {
			return sepaImportFac.getSaldoVonBankverbindungByAuszug(kontoIId, auszugNr, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
	
	public void archiviereSepaKontoauszug(String xmlKontoauszug, String filename, 
			Integer bankverbindungIId, Integer auszugsNr) throws ExceptionLP {
		try {
			sepaImportFac.archiviereSepaKontoauszug(xmlKontoauszug, filename, 
					bankverbindungIId, auszugsNr, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	public KontoDtoSmall[] kontoFindByPrimaryKeySmall(Integer[] ids) throws ExceptionLP {
		try {
			return finanzFac.kontosFindByPrimaryKeySmall(ids) ;
		} catch(Throwable t) {
			handleThrowable(t);
			return null ;
		}
	}

	public Integer importSepaImportResults(SepakontoauszugDto sepakontoauszugDto, List<ISepaImportResult> results, IPayloadPublisher worker) 
			throws ExceptionLP {
		String payloadReference = Integer.toString(System.identityHashCode(worker));
		Router.getInstance().register(payloadReference, worker);
		
		try {
			return sepaImportFac.importSepaImportResults(sepakontoauszugDto, results, payloadReference, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		} finally {
			Router.getInstance().unregister(payloadReference);
		}
	}
	
	public SepakontoauszugDto sepakontoauszugFindByPrimaryKeySmall(Integer iId) throws ExceptionLP {
		try {
			return sepaImportFac.sepakontoauszugFindByPrimaryKeySmall(iId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
	
	public void storniereSepakontoauszug(Integer iId) throws ExceptionLP {
		try {
			sepaImportFac.storniereSepakontoauszug(iId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer iId, boolean bilanzgruppe) throws ExceptionLP {
		try {
			finanzFac.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(iId,bilanzgruppe, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
		
	public SepakontoauszugDto getSepakontoauszugNiedrigsteAuszugsnummer(Integer bankverbindungIId) throws ExceptionLP {
		try {
			return sepaImportFac.getSepakontoauszugNiedrigsteAuszugsnummer(bankverbindungIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
	
	public void pruefeSepakontoauszugAufVerbuchung(Integer sepakontoauszugIId) throws ExceptionLP {
		try {
			sepaImportFac.pruefeSepakontoauszugAufVerbuchung(sepakontoauszugIId, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}
	
	public JasperPrintLP printKpi(java.sql.Date von, java.sql.Date bis) throws ExceptionLP {
		try {
			return kpiReportFac.printKpi(von, bis, LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t);
		}
		
		return null;
	}
	
	public FinanzamtDto findFinanzamtForKunde(KundeDto kundeDto) throws ExceptionLP {
		try {
			return finanzFac.finanzamtFindByKunde(kundeDto, LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	public FinanzamtDto findFinanzamtForLieferant(LieferantDto lieferantDto) throws ExceptionLP {
		try {
			return finanzFac.finanzamtFindByLieferant(lieferantDto, LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t);
		}
		return null;
	}
	
	public Map<Iso20022StandardEnum, Iso20022PaymentsDto> getMapOfIso20022Payments() throws ExceptionLP {
		try {
			return sepaImportFac.getMapOfIso20022Payments();
		} catch(Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	
	public List<Iso20022StandardDto> iso20022StandardsFindAll() throws ExceptionLP {
		try {
			return sepaImportFac.iso20022standardsFindAll();
		} catch(Throwable t) {
			handleThrowable(t);
		}
		return null;
	}
	
	public Iso20022BankverbindungDto iso20022BankverbindungFindByBankverbindungIId(BankverbindungId bankverbindungId) throws ExceptionLP {
		try {
			return sepaImportFac.iso20022BankverbindungFindByBankverbindungIIdNoExc(bankverbindungId);
		} catch(Throwable t) {
			handleThrowable(t);
		}
		return null;
		
	}
	
	public Integer createIso20022Bankverbindung(Iso20022BankverbindungDto iso20022BankverbindungDto) throws ExceptionLP {
		try {
			return sepaImportFac.createIso20022Bankverbindung(iso20022BankverbindungDto);
		} catch(Throwable t) {
			handleThrowable(t);
		}
		return null;
	}
	
	public void updateIso20022Bankverbindung(Iso20022BankverbindungDto iso20022BankverbindungDto) throws ExceptionLP {
		try {
			sepaImportFac.updateIso20022Bankverbindung(iso20022BankverbindungDto);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}

	public void removeIso20022Bankverbindung(Integer iId) throws ExceptionLP {
		try {
			sepaImportFac.removeIso20022Bankverbindung(iId);
		} catch(Throwable t) {
			handleThrowable(t);
		}
	}
	
	public String getWaehrungOfKonto(KontoId kontoId) throws ExceptionLP {
		try {
			return sepaImportFac.getWaehrungOfKonto(kontoId, LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t);
		}
		return null;
	}
}
