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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JOptionPane;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.AlergenDto;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelImportDto;
import com.lp.server.artikel.service.ArtikelTruTopsDto;
import com.lp.server.artikel.service.ArtikelalergenDto;
import com.lp.server.artikel.service.ArtikelimportFac;
import com.lp.server.artikel.service.ArtikelimportFehlerDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.ArtikelshopgruppeDto;
import com.lp.server.artikel.service.ArtikelsperrenDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.AutomotiveDto;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.artikel.service.ErsatztypenDto;
import com.lp.server.artikel.service.FarbcodeDto;
import com.lp.server.artikel.service.GebindeDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.KatalogDto;
import com.lp.server.artikel.service.LaseroberflaecheDto;
import com.lp.server.artikel.service.MedicalDto;
import com.lp.server.artikel.service.ReachDto;
import com.lp.server.artikel.service.RohsDto;
import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.artikel.service.ShopgruppewebshopDto;
import com.lp.server.artikel.service.SperrenDto;
import com.lp.server.artikel.service.VerleihDto;
import com.lp.server.artikel.service.VerpackungsmittelDto;
import com.lp.server.artikel.service.VerschleissteilDto;
import com.lp.server.artikel.service.VerschleissteilwerkzeugDto;
import com.lp.server.artikel.service.VorschlagstextDto;
import com.lp.server.artikel.service.VorzugDto;
import com.lp.server.artikel.service.WebabfrageArtikellieferantProperties;
import com.lp.server.artikel.service.WebabfrageArtikellieferantResult;
import com.lp.server.artikel.service.WebshopDto;
import com.lp.server.artikel.service.WerkzeugDto;
import com.lp.server.artikel.service.ZugehoerigeDto;
import com.lp.server.fertigung.service.VendidataArticleExportResult;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.ArtikelTruTopsId;
import com.lp.server.util.HvOptional;
import com.lp.util.EJBExceptionLP;
import com.lp.util.KeyValue;

@SuppressWarnings("static-access")
public class ArtikelDelegate extends Delegate {
	private Context context;
	private ArtikelFac artikelFac;
	private ArtikelimportFac artikelimportFac;

	public ArtikelDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			artikelFac = lookupFac(context, ArtikelFac.class);
			artikelimportFac = lookupFac(context, ArtikelimportFac.class);

		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}

	}

	public HvOptional<ArtikelTruTopsDto> artikelTruTopsFindByArtikelId(ArtikelId artikelId) throws ExceptionLP {
		try {
			return artikelFac.artikelTruTopsFindByArtikelId(artikelId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtikel(ArtikelDto artikelDto, boolean bEinmalartikel) throws ExceptionLP {
		try {
			return artikelFac.createArtikelMitParameterEinmalartikel(artikelDto, bEinmalartikel,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtikel(ArtikelDto artikelDto) throws ExceptionLP {
		try {
			return artikelFac.createArtikel(artikelDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createEinkaufsean(EinkaufseanDto einkaufseanDto) throws ExceptionLP {
		try {
			return artikelFac.createEinkaufsean(einkaufseanDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createZugehoerige(ZugehoerigeDto zugehoerigeDto) throws ExceptionLP {
		try {
			return artikelFac.createZugehoerige(zugehoerigeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createVerpackungsmittel(VerpackungsmittelDto dto) throws ExceptionLP {
		try {
			return artikelFac.createVerpackungsmittel(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getArtikelsperrenText(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.getArtikelsperrenText(artikelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getHerstellercode(Integer partnerId) throws ExceptionLP {
		try {
			return artikelFac.getHerstellercode(partnerId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Object[] kopiereArtikel(Integer artikelIId, String neueArtikelnummer, HashMap zuKopieren,
			Integer herstellerIIdNeu, Integer stuecklistepositionIId) throws ExceptionLP {
		try {
			return artikelFac.kopiereArtikel(artikelIId, neueArtikelnummer, zuKopieren, herstellerIIdNeu,
					stuecklistepositionIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer kopiereArtikelFuerDimensionenBestellen(Integer artikelIId, BigDecimal bdPositionsmenge,
			Integer dimension1, Integer dimension2, Integer dimension3) throws ExceptionLP {
		try {
			return artikelFac.kopiereArtikelFuerDimensionenBestellen(artikelIId, bdPositionsmenge, dimension1,
					dimension2, dimension3, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String pruefeCSVImport(ArtikelImportDto[] daten, boolean bestehendeArtikelUeberschreiben)
			throws ExceptionLP {
		try {
			return artikelFac.pruefeCSVImport(daten, bestehendeArtikelUeberschreiben, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return "";
		}
	}

	public void importiereArtikel(ArtikelImportDto[] daten, boolean bBestehendeArtikelUeberschreiben)
			throws ExceptionLP {
		try {
			artikelimportFac.importiereArtikel(daten, bBestehendeArtikelUeberschreiben, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public Integer createFarbcode(FarbcodeDto farbcodeDto) throws ExceptionLP {
		try {
			return artikelFac.createFarbcode(farbcodeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtikelshopgruppe(ArtikelshopgruppeDto dto) throws ExceptionLP {
		try {
			return artikelFac.createArtikelshopgruppe(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtikelallergen(ArtikelalergenDto dto) throws ExceptionLP {
		try {
			return artikelFac.createArtikelallergen(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<KeyValue> getListeDerArtikellieferanten(Integer bestellvorschlagIId, BigDecimal nMenge)
			throws ExceptionLP {
		try {
			return artikelFac.getListeDerArtikellieferanten(bestellvorschlagIId, nMenge,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<String> getEKStaffeln(Integer artikellieferantIId) throws ExceptionLP {
		try {
			return artikelFac.getEKStaffeln(artikellieferantIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<String> getTruTopsMetadaten(ArtikelId artikelId) throws ExceptionLP {
		try {
			return artikelFac.getTruTopsMetadaten(artikelId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void toggleFreigabe(Integer artikelIId, String cFreigabeZuerueckgenommen) throws ExceptionLP {
		try {
			artikelFac.toggleFreigabe(artikelIId, cFreigabeZuerueckgenommen, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public ArrayList<String> getVerfuegbarkeitErsatztypen(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.getVerfuegbarkeitErsatztypen(artikelIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createWebshop(WebshopDto dto) throws ExceptionLP {
		try {
			return artikelFac.createWebshop(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createShopgruppe(ShopgruppeDto dto) throws ExceptionLP {
		try {
			return artikelFac.createShopgruppe(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createShopgruppeVor(ShopgruppeDto dto, Integer iId) throws ExceptionLP {
		try {
			return artikelFac.createShopgruppeVor(dto, iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createShopgruppewebshop(ShopgruppewebshopDto dto) throws ExceptionLP {
		try {
			return artikelFac.createShopgruppewebshop(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createVorschlagstext(VorschlagstextDto vorschlagstextDto) throws ExceptionLP {
		try {
			return artikelFac.createVorschlagstext(vorschlagstextDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createVerleih(VerleihDto verleihDto) throws ExceptionLP {
		try {
			return artikelFac.createVerleih(verleihDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createSperren(SperrenDto sperrenDto) throws ExceptionLP {
		try {
			return artikelFac.createSperren(sperrenDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelimportFehlerDto pruefeUndImportiereArtikelXLS(byte[] xlsDatei, java.sql.Timestamp tDefaultEK,
			java.sql.Timestamp tDefaultVK, boolean bImportierenWennKeinFehler, boolean bPreisUeberschreiben,
			boolean bAlsLief1Reihen, boolean bBestehendeartikelUeberschreiben, boolean bFreigabestatusIgnorieren)
			throws ExceptionLP {
		try {
			return artikelimportFac.pruefeUndImportiereArtikelXLS(xlsDatei, tDefaultEK, tDefaultVK,
					bImportierenWennKeinFehler, bPreisUeberschreiben, bAlsLief1Reihen, bBestehendeartikelUeberschreiben,
					bFreigabestatusIgnorieren, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String importiereAlergeneXLS(byte[] xlsDatei, Integer lieferantIId) throws ExceptionLP {
		try {
			return artikelimportFac.importiereAllergeneXLS(xlsDatei, lieferantIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String importiereVerschleissteileXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler)
			throws ExceptionLP {
		try {
			return artikelimportFac.importiereVerschleissteileXLS(xlsDatei, bImportierenWennKeinFehler,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String importiereKundesokoXLS(byte[] xlsDatei, Integer kundeIId, boolean bImportierenWennKeinFehler)
			throws ExceptionLP {
		try {
			return artikelimportFac.importiereKundesokoXLS(xlsDatei, kundeIId, bImportierenWennKeinFehler,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String importiereVKMengenstgaffelXLS(byte[] xlsDatei, InternalFrame iFrame,
			boolean bImportierenWennKeinFehler) throws ExceptionLP {
		try {

			ParametermandantDto p = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_BEGRUENDUNG_BEI_VKPREISAENDERUNG);

			int begruendungAngeben = (Integer) p.getCWertAsObject();

			String bemerkung = null;
			if (begruendungAngeben > 0 && iFrame != null) {
				bemerkung = JOptionPane.showInputDialog(iFrame, LPMain.getTextRespectUISPr("artikel.vkpreis.bemerkung"),
						LPMain.getTextRespectUISPr("lp.frage"), JOptionPane.QUESTION_MESSAGE);
			}

			return artikelimportFac.importiereVKMengenstgaffelXLS(xlsDatei, bImportierenWennKeinFehler, bemerkung,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String importiereLagerminSollXLS(byte[] xlsDatei, boolean bImportierenWennKeinFehler) throws ExceptionLP {
		try {
			return artikelimportFac.importiereLagerminSollXLS(xlsDatei, bImportierenWennKeinFehler,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtikelsperren(ArtikelsperrenDto artikelsperrenDto) throws ExceptionLP {
		try {
			return artikelFac.createArtikelsperren(artikelsperrenDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtikellieferant(ArtikellieferantDto artikellieferantDto) throws ExceptionLP {
		try {
			return artikelFac.createArtikellieferant(artikellieferantDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtikelTruTops(ArtikelTruTopsDto dto)
			throws ExceptionLP {
		try {
			return artikelFac.createArtikelTruTops(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtikellieferantstaffel(ArtikellieferantstaffelDto artikellieferantstaffelDto)
			throws ExceptionLP {
		try {
			return artikelFac.createArtikellieferantstaffel(artikellieferantstaffelDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void alleSIwerteNachtragen() throws ExceptionLP {
		try {
			artikelFac.alleSIwerteNachtragen(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public byte[] getXLSForPreispflege(Integer artikelgruppeIId, boolean mitUntergruppen, Integer artikelklasseIId,
			boolean mitUnterklassen, Integer shopgruppeIId, boolean mitShopuntergruppen, String artikelNrVon,
			String artikelNrBis, boolean bMitVersteckten) throws ExceptionLP {
		try {
			return artikelFac.getXLSForPreispflege(artikelgruppeIId, mitUntergruppen, artikelklasseIId, mitUnterklassen,
					shopgruppeIId, mitShopuntergruppen, artikelNrVon, artikelNrBis, bMitVersteckten,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void preiseXLSForPreispflege(byte[] xlsFile, String cBegruendung) throws ExceptionLP {
		try {
			artikelFac.preiseXLSForPreispflege(xlsFile, cBegruendung, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public Integer createReach(ReachDto dto) throws ExceptionLP {
		try {
			return artikelFac.createReach(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createLaseroberflaeche(LaseroberflaecheDto dto) throws ExceptionLP {
		try {
			return artikelFac.createLaseroberflaeche(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelDto pruefeObHerstellernummerandererArtikelVerwendet(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.pruefeObHerstellernummerandererArtikelVerwendet(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createAllergen(AlergenDto dto) throws ExceptionLP {
		try {
			return artikelFac.createAllergen(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public int ichBinZuschittOderBasisArtikel(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.ichBinZuschittOderBasisArtikel(artikelIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return -1;
		}
	}

	public Integer createVorzug(VorzugDto dto) throws ExceptionLP {
		try {
			return artikelFac.createVorzug(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createRohs(RohsDto dto) throws ExceptionLP {
		try {
			return artikelFac.createRohs(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createWerkzeug(WerkzeugDto dto) throws ExceptionLP {
		try {
			return artikelFac.createWerkzeug(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createVerschleissteilwerkzeug(VerschleissteilwerkzeugDto dto) throws ExceptionLP {
		try {
			return artikelFac.createVerschleissteilwerkzeug(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createVerschleissteil(VerschleissteilDto dto) throws ExceptionLP {
		try {
			return artikelFac.createVerschleissteil(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createAutomotive(AutomotiveDto dto) throws ExceptionLP {
		try {
			return artikelFac.createAutomotive(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createErsatztypen(ErsatztypenDto dto) throws ExceptionLP {
		try {
			return artikelFac.createErsatztypen(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createMedicale(MedicalDto dto) throws ExceptionLP {
		try {
			return artikelFac.createMedicale(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createGebinde(GebindeDto dto) throws ExceptionLP {
		try {
			return artikelFac.createGebinde(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtgru(ArtgruDto artgruDto) throws ExceptionLP {
		try {
			return artikelFac.createArtgru(artgruDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createArtkla(ArtklaDto artklaDto) throws ExceptionLP {
		try {
			return artikelFac.createArtkla(artklaDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createKatalog(KatalogDto katalogDto) throws ExceptionLP {
		try {
			return artikelFac.createKatalog(katalogDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer createHersteller(HerstellerDto herstellerDto) throws ExceptionLP {
		try {
			return artikelFac.createHersteller(herstellerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArrayList<GebindeDto> getGebindeEinesArtikelsUndEinesLieferanten(Integer artikelIId, Integer lieferantIId,
			java.sql.Date tDatumPreisgueltigkeit) throws ExceptionLP {
		try {
			return artikelFac.getGebindeEinesArtikelsUndEinesLieferanten(artikelIId, lieferantIId,
					tDatumPreisgueltigkeit, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellieferantDto getArtikelEinkaufspreis(Integer artikelIId, Integer artikellieferantIId,
			BigDecimal fMenge, String waehrungCNr, java.sql.Date tDatumPreisgueltigkeit) throws ExceptionLP {
		try {
			return artikelFac.getArtikelEinkaufspreis(artikelIId, artikellieferantIId, fMenge, waehrungCNr,
					tDatumPreisgueltigkeit, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellieferantDto getArtikelEinkaufspreisMitOptionGebinde(Integer artikelIId, Integer artikellieferantIId,
			BigDecimal fMenge, String waehrungCNr, java.sql.Date tDatumPreisgueltigkeit, Integer gebindeIId)
			throws ExceptionLP {
		try {
			return artikelFac.getArtikelEinkaufspreisMitOptionGebinde(artikelIId, artikellieferantIId, fMenge,
					waehrungCNr, tDatumPreisgueltigkeit, gebindeIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellieferantDto getArtikelEinkaufspreisDesBevorzugtenLieferanten(Integer artikelIId, BigDecimal fMenge,
			String waehrungCNr) throws ExceptionLP {
		try {
			return artikelFac.getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikelIId, fMenge, waehrungCNr,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellieferantDto getArtikelEinkaufspreisEinesLieferantenEinerBestellung(Integer artikelIId,
			Integer bestellungIId, BigDecimal fMenge) throws ExceptionLP {
		try {
			return artikelFac.getArtikelEinkaufspreisEinesLieferantenEinerBestellung(artikelIId, bestellungIId, fMenge,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellieferantDto getGuenstigstenEKPreis(Integer artikelIId, BigDecimal fMenge, java.sql.Date zeitpunkt,
			String waehrungCNr, Integer lieferantIIdVergleich) throws ExceptionLP {
		try {
			return artikelFac.getGuenstigstenEKPreis(artikelIId, fMenge, zeitpunkt, waehrungCNr, lieferantIIdVergleich,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public boolean sindVorschlagstexteVorhanden() throws ExceptionLP {
		try {
			return artikelFac.sindVorschlagstexteVorhanden();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return false;
		}
	}

	public boolean sindArtikelgruppenEingeschraenkt() throws ExceptionLP {
		try {
			return artikelFac.sindArtikelgruppenEingeschraenkt(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return false;
		}
	}

	public boolean gibtEsEKStaffelnZuEinemArtikel(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.gibtEsEKStaffelnZuEinemArtikel(artikelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return false;
		}
	}

	public void removeArtikel(Integer iId) throws ExceptionLP {
		try {
			artikelFac.removeArtikel(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeArtikelspr(Integer artikelIId, String locale) throws ExceptionLP {
		try {
			artikelFac.removeArtikelspr(artikelIId, locale, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeArtgru(Integer iId) throws ExceptionLP {
		try {
			artikelFac.removeArtgru(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeVerleih(VerleihDto verleihDto) throws ExceptionLP {
		try {
			artikelFac.removeVerleih(verleihDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeArtkla(Integer iId) throws ExceptionLP {
		try {
			artikelFac.removeArtkla(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeKatalog(KatalogDto katalogDto) throws ExceptionLP {
		try {
			artikelFac.removeKatalog(katalogDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAllergen(AlergenDto alergenDto) throws ExceptionLP {
		try {
			artikelFac.removeAllergen(alergenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheArtikellieferanten(Integer iIdLieferant1, Integer iIdLieferant2) throws ExceptionLP {
		try {
			artikelFac.vertauscheArtikellieferanten(iIdLieferant1, iIdLieferant2, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheArtikelsperren(Integer iIdLieferant1, Integer iIdLieferant2) throws ExceptionLP {
		try {
			artikelFac.vertauscheArtikelsperren(iIdLieferant1, iIdLieferant2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheErsatztypen(Integer iIdLieferant1, Integer iIdLieferant2) throws ExceptionLP {
		try {
			artikelFac.vertauscheErsatztypen(iIdLieferant1, iIdLieferant2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheAlergen(Integer iId1, Integer iId2) throws ExceptionLP {
		try {
			artikelFac.vertauscheAlergen(iId1, iId2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeHersteller(Integer iId) throws ExceptionLP {
		try {
			artikelFac.removeHersteller(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeArtikellieferant(ArtikellieferantDto dto) throws ExceptionLP {
		try {
			artikelFac.removeArtikellieferant(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeFarbcode(FarbcodeDto dto) throws ExceptionLP {
		try {
			artikelFac.removeFarbcode(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeWebshop(WebshopDto dto) throws ExceptionLP {
		try {
			artikelFac.removeWebshop(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeArtikelshopgruppe(ArtikelshopgruppeDto dto) throws ExceptionLP {
		try {
			artikelFac.removeArtikelshopgruppe(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeArtikelallergen(ArtikelalergenDto dto) throws ExceptionLP {
		try {
			artikelFac.removeArtikelallergen(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeShopgruppe(Integer shopgruppeIId) throws ExceptionLP {
		try {
			artikelFac.removeShopgruppe(shopgruppeIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeErsatztypen(ErsatztypenDto dto) throws ExceptionLP {
		try {
			artikelFac.removeErsatztypen(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeReach(ReachDto dto) throws ExceptionLP {
		try {
			artikelFac.removeReach(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeLaseroberflaeche(LaseroberflaecheDto dto) throws ExceptionLP {
		try {
			artikelFac.removeLaseroberflaeche(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeVorzug(VorzugDto dto) throws ExceptionLP {
		try {
			artikelFac.removeVorzug(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeRohs(RohsDto dto) throws ExceptionLP {
		try {
			artikelFac.removeRohs(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeWerkzeug(WerkzeugDto dto) throws ExceptionLP {
		try {
			artikelFac.removeWerkzeug(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeVerschleissteil(VerschleissteilDto dto) throws ExceptionLP {
		try {
			artikelFac.removeVerschleissteil(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeVerschleissteilwerkzeug(VerschleissteilwerkzeugDto dto) throws ExceptionLP {
		try {
			artikelFac.removeVerschleissteilwerkzeug(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAutomotive(AutomotiveDto dto) throws ExceptionLP {
		try {
			artikelFac.removeAutomotive(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeMedical(MedicalDto dto) throws ExceptionLP {
		try {
			artikelFac.removeMedical(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeVerpackungsmittel(VerpackungsmittelDto dto) throws ExceptionLP {
		try {
			artikelFac.removeVerpackungsmittel(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeGebinde(GebindeDto dto) throws ExceptionLP {
		try {
			artikelFac.removeGebinde(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeShopgruppewebshop(ShopgruppewebshopDto shopgruppewebshopDto) throws ExceptionLP {
		try {
			artikelFac.removeShopgruppewebshop(shopgruppewebshopDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeVorschlagstext(VorschlagstextDto dto) throws ExceptionLP {
		try {
			artikelFac.removeVorschlagstext(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeSperren(SperrenDto dto) throws ExceptionLP {
		try {
			artikelFac.removeSperren(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeArtikelsperren(ArtikelsperrenDto dto) throws ExceptionLP {
		try {
			artikelFac.removeArtikelsperren(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeZugehoerige(ZugehoerigeDto dto) throws ExceptionLP {
		try {
			artikelFac.removeZugehoerige(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeArtikellieferantstaffel(ArtikellieferantstaffelDto dto) throws ExceptionLP {
		try {
			artikelFac.removeArtikellieferantstaffel(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeEinkaufsean(EinkaufseanDto dto) throws ExceptionLP {
		try {
			artikelFac.removeEinkaufsean(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtikel(ArtikelDto artikelDto) throws ExceptionLP {
		try {
			artikelFac.updateArtikel(artikelDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateKatalog(KatalogDto katalogDto) throws ExceptionLP {
		try {
			artikelFac.updateKatalog(katalogDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateVerleih(VerleihDto verleihDto) throws ExceptionLP {
		try {
			artikelFac.updateVerleih(verleihDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtikellieferant(ArtikellieferantDto artikellieferantDto) throws ExceptionLP {
		updateArtikellieferant(artikellieferantDto, false);
	}

	public void updateArtikellieferant(ArtikellieferantDto artikellieferantDto, boolean zuschnittsartikelNeuBerechnen)
			throws ExceptionLP {
		try {

			artikelFac.updateArtikellieferantImpl(artikellieferantDto, zuschnittsartikelNeuBerechnen,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtikellieferantstaffel(ArtikellieferantstaffelDto artikellieferantstaffelDto)
			throws ExceptionLP {
		try {
			artikelFac.updateArtikellieferantstaffel(artikellieferantstaffelDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtgru(ArtgruDto artgruDto) throws ExceptionLP {
		try {
			artikelFac.updateArtgru(artgruDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateEinkaufsean(EinkaufseanDto einkaufseanDto) throws ExceptionLP {
		try {
			artikelFac.updateEinkaufsean(einkaufseanDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateFarbcode(FarbcodeDto farbcodeDto) throws ExceptionLP {
		try {
			artikelFac.updateFarbcode(farbcodeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateWebshop(WebshopDto webshopDto) throws ExceptionLP {
		try {
			artikelFac.updateWebshop(webshopDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateVerpackungsmittel(VerpackungsmittelDto dto) throws ExceptionLP {
		try {
			artikelFac.updateVerpackungsmittel(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtikelshopgruppe(ArtikelshopgruppeDto dto) throws ExceptionLP {
		try {
			artikelFac.updateArtikelshopgruppe(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtikelallergen(ArtikelalergenDto dto) throws ExceptionLP {
		try {
			artikelFac.updateArtikelallergen(dto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateShopgruppe(ShopgruppeDto shopgruppeDto) throws ExceptionLP {
		try {
			artikelFac.updateShopgruppe(shopgruppeDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void erhoeheAlleStaffelnEinesArtikellieferant(Integer artikellieferantIId, java.sql.Date tGueltigab,
			BigDecimal nProzent) throws ExceptionLP {
		try {
			artikelFac.erhoeheAlleStaffelnEinesArtikellieferant(artikellieferantIId, tGueltigab, nProzent,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateReach(ReachDto dto) throws ExceptionLP {
		try {
			artikelFac.updateReach(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateLaseroberflaeche(LaseroberflaecheDto dto) throws ExceptionLP {
		try {
			artikelFac.updateLaseroberflaeche(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateVorzug(VorzugDto dto) throws ExceptionLP {
		try {
			artikelFac.updateVorzug(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAllergen(AlergenDto dto) throws ExceptionLP {
		try {
			artikelFac.updateAllergen(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAutomotive(AutomotiveDto dto) throws ExceptionLP {
		try {
			artikelFac.updateAutomotive(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateErsatztypen(ErsatztypenDto dto) throws ExceptionLP {
		try {
			artikelFac.updateErsatztypen(dto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateMedical(MedicalDto dto) throws ExceptionLP {
		try {
			artikelFac.updateMedical(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateGebinde(GebindeDto dto) throws ExceptionLP {
		try {
			artikelFac.updateGebinde(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateRohs(RohsDto dto) throws ExceptionLP {
		try {
			artikelFac.updateRohs(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	public void updateArtikelTruTops(ArtikelTruTopsDto dto) throws ExceptionLP {
		try {
			artikelFac.updateArtikelTruTops(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateWerkzeug(WerkzeugDto dto) throws ExceptionLP {
		try {
			artikelFac.updateWerkzeug(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtgrumandant(Integer artgruIId, Integer kontoIId) throws ExceptionLP {
		try {
			artikelFac.updateArtgrumandant(artgruIId, kontoIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateVerschleissteilwerkzeug(VerschleissteilwerkzeugDto dto) throws ExceptionLP {
		try {
			artikelFac.updateVerschleissteilwerkzeug(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateVerschleissteil(VerschleissteilDto dto) throws ExceptionLP {
		try {
			artikelFac.updateVerschleissteil(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateShopgruppewebshop(ShopgruppewebshopDto shopgruppewebshopDto) throws ExceptionLP {
		try {
			artikelFac.updateShopgruppewebshop(shopgruppewebshopDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateVorschlagstext(VorschlagstextDto vorschlagstextDto) throws ExceptionLP {
		try {
			artikelFac.updateVorschlagstext(vorschlagstextDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateSperren(SperrenDto sperrenDto) throws ExceptionLP {
		try {
			artikelFac.updateSperren(sperrenDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateZugehoerige(ZugehoerigeDto zugehoerigeDto) throws ExceptionLP {
		try {
			artikelFac.updateZugehoerige(zugehoerigeDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtikelsperren(ArtikelsperrenDto artikelsperrenDto) throws ExceptionLP {
		try {
			artikelFac.updateArtikelsperren(artikelsperrenDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateArtkla(ArtklaDto artklaDto) throws ExceptionLP {
		try {
			artikelFac.updateArtkla(artklaDto, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateHersteller(HerstellerDto herstellerDto) throws ExceptionLP {
		try {
			artikelFac.updateHersteller(herstellerDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ArtgruDto artgruFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.artgruFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtgruDto[] artgruFindAll() throws ExceptionLP {
		try {
			return artikelFac.artgruFindAll();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public HashMap getAllSperrenIcon() throws ExceptionLP {
		try {
			return artikelFac.getAllSperrenIcon(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public KatalogDto katalogFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.katalogFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ErsatztypenDto ersatztypenFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.ersatztypenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ErsatztypenDto[] ersatztypenFindByArtikelIId(Integer artikelId) throws ExceptionLP {
		try {
			return artikelFac.ersatztypenFindByArtikelIId(artikelId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ErsatztypenDto[] ersatztypenfindByArtikelIIdErsatz(Integer artikelIIdErsatz) throws ExceptionLP {
		try {
			return artikelFac.ersatztypenfindByArtikelIIdErsatz(artikelIIdErsatz);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer[] getBereitsVerwendeteShopgruppen(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.getBereitsVerwendeteShopgruppen(artikelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VorschlagstextDto vorschlagstextFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.vorschlagstextFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public EinkaufseanDto einkaufseanFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.einkaufseanFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VerleihDto verleihFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.verleihFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtklaDto artklaFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.artklaFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public FarbcodeDto farbcodeFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.farbcodeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelshopgruppeDto artikelshopgruppeFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.artikelshopgruppeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelalergenDto artikelallergenFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.artikelallergenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public WebshopDto webshopFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.webshopFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public WerkzeugDto werkzeugFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.werkzeugFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VerschleissteilwerkzeugDto verschleissteilwerkzeugFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.verschleissteilwerkzeugFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VerschleissteilDto verschleissteilFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.verschleissteilFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ShopgruppeDto shopgruppeFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.shopgruppeFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ReachDto reachFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.reachFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public LaseroberflaecheDto laseroberflaecheFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.laseroberflaecheFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VorzugDto vorzugFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.vorzugFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public AlergenDto allergenFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.allergenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public RohsDto rohsFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.rohsFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public AutomotiveDto automotiveFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.automotiveFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public MedicalDto medicalFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.medicalFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public GebindeDto gebindeFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.gebindeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ShopgruppewebshopDto shopgruppewebshopFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.shopgruppewebshopFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public SperrenDto sperrenFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.sperrenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelsperrenDto artikelsperrenFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.artikelsperrenFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VerpackungsmittelDto verpackungsmittelFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.verpackungsmittelFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ZugehoerigeDto zugehoerigeFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.zugehoerigeFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelsperrenDto[] artikelsperrenFindByArtikelIId(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.artikelsperrenFindByArtikelIId(artikelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void wandleHandeingabeInArtikelUm(Integer positionIId, int iArt, String neueArtikelnummer)
			throws ExceptionLP {
		try {
			artikelFac.wandleHandeingabeInArtikelUm(positionIId, iArt, neueArtikelnummer,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ArrayList<String> wandleHandeingabeInBestehendenArtikelUm(Integer positionIId, int iArt, Integer artikelIId)
			throws ExceptionLP {
		try {
			return artikelFac.wandleHandeingabeInBestehendenArtikelUm(positionIId, iArt, artikelIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public HerstellerDto herstellerFindBdPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.herstellerFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelDto artikelFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.artikelFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void preiseDerZuschnittsArtikelAnhandBasisartikelNeuBerechnen(Integer artikelIId,
			Integer artikellieferantIId) throws ExceptionLP {
		try {
			artikelFac.preiseDerZuschnittsArtikelAnhandBasisartikelNeuBerechnen(artikelIId, artikellieferantIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public int getLaengeArtikelBezeichnungen() throws ExceptionLP {
		try {
			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_ARTIKEL_LAENGE_BEZEICHNUNG,
							ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());

			return (Integer) parameter.getCWertAsObject();

		} catch (Throwable ex) {
			handleThrowable(ex);
			return 40;
		}

	}

	public ArtikelDto artikelFindByPrimaryKeySmallOhneExc(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.artikelFindByPrimaryKeySmallOhneExc(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void preiseAusAnfrageRueckpflegen(Integer anfrageIId, Integer anfragepositionlieferdatenIId,
			boolean bStaffelnLoeschen, boolean bLieferantVorreihen, boolean bAlsStaffelpreisRueckpflegen)
			throws ExceptionLP {
		try {
			artikelFac.preiseAusAnfrageRueckpflegen(anfrageIId, anfragepositionlieferdatenIId, bStaffelnLoeschen,
					bLieferantVorreihen, bAlsStaffelpreisRueckpflegen, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public ArtikellieferantDto artikellieferantFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.artikellieferantFindByPrimaryKey(iId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellieferantDto[] artikellieferantFindByLieferantIId(Integer lieferantIId) throws ExceptionLP {
		try {
			return artikelFac.artikellieferantFindByLieferantIId(lieferantIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String generiereNeueArtikelnummer(String beginnArtikelnummer) throws ExceptionLP {
		try {
			return artikelFac.generiereNeueArtikelnummer(beginnArtikelnummer, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(Integer artikelIId,
			Integer lieferantIId, String cWunschwaehrung) throws ExceptionLP {
		try {
			return artikelFac.artikellieferantFindByArtikellIIdLieferantIIdInWunschwaehrung(artikelIId, lieferantIId,
					cWunschwaehrung, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellieferantDto artikellieferantFindByIIdInWunschwaehrung(Integer artikellieferantIId,
			String cWunschwaehrung) throws ExceptionLP {
		try {
			return artikelFac.artikellieferantFindByIIdInWunschwaehrung(artikellieferantIId, cWunschwaehrung,
					LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void artikellieferantAlsErstesReihen(Integer artikelIId, Integer artikellieferantIId) throws ExceptionLP {
		try {
			artikelFac.artikellieferantAlsErstesReihen(artikelIId, artikellieferantIId);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

	public ArtikellieferantDto artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(Integer artikelIId,
			Integer lieferantIId, java.sql.Date tDatumPreisgueltigkeit, Integer gebindeIId) throws ExceptionLP {
		try {
			return artikelFac.artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(artikelIId,
					lieferantIId, tDatumPreisgueltigkeit, gebindeIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellieferantstaffelDto artikellieferantstaffelFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelFac.artikellieferantstaffelFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikellieferantstaffelDto[] artikellieferantstaffelFindByArtikellieferantIId(Integer artikellieferantIId)
			throws ExceptionLP {
		try {
			return artikelFac.artikellieferantstaffelFindByArtikellieferantIId(artikellieferantIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelDto artikelFindByCNr(String cNr) throws ExceptionLP {
		try {
			return artikelFac.artikelFindByCNr(cNr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelDto artikelFindByCNrOhneExc(String cNr) throws ExceptionLP {
		try {
			return artikelFac.artikelFindByCNrOhneExc(cNr, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelDto[] artikelFindSpezial(String bauteil, String bauform) throws ExceptionLP {
		try {
			return artikelFac.artikelFindSpecial(bauteil, bauform);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelDto getErsatzartikel(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.getErsatzartikel(artikelIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllSprArtikelarten() throws ExceptionLP {
		try {
			return artikelFac.getAllSprArtikelarten(LPMain.getInstance().getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllVerschleissteile(Integer werkzeugIId) throws ExceptionLP {
		try {
			return artikelFac.getAllVerschleissteile(werkzeugIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllSprArtgru() throws ExceptionLP {
		try {
			return artikelFac.getAllSprArtgru(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map getAlleGueltigenStaffelneinesLieferanten(Integer artikellieferantIId,
			String waehrungCNrGewuenschteWaehrung, java.sql.Date dDatum) throws ExceptionLP {
		try {
			return artikelFac.getAlleGueltigenStaffelneinesLieferanten(artikellieferantIId, dDatum,
					waehrungCNrGewuenschteWaehrung, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllVerleih() throws ExceptionLP {
		try {
			return artikelFac.getAllVerleih();
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Map<?, ?> getAllVorzug() throws ExceptionLP {
		try {
			return artikelFac.getAllVorzug(LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public ArtikelsprDto getDefaultArtikelbezeichnungen(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.getDefaultArtikelbezeichnungen(artikelIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	/**
	 * Einzeilige Artikelbezeichnung fuer die UI Darstellung zusammenbauen. <br>
	 * Wird beispielsweise in FLR Listen verwendet.
	 * 
	 * @param iIdArtikelI PK des Artikels
	 * @return String
	 * @throws ExceptionLP
	 */
	public String formatArtikelbezeichnungEinzeiligOhneExc(Integer iIdArtikelI) throws ExceptionLP {
		String cBezeichnung = null;
		try {
			cBezeichnung = artikelFac.formatArtikelbezeichnungEinzeiligOhneExc(iIdArtikelI,
					LPMain.getInstance().getTheClient().getLocUi());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return cBezeichnung;
	}

	public ArtikelDto artikelFindByEanFuerSchnellerfassung(String ean) throws ExceptionLP {
		ArtikelDto artikelDto = null;
		try {
			artikelDto = artikelFac.artikelFindByEanFuerSchnellerfassung(ean, LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return artikelDto;
	}

	public ArtikellieferantDto[] artikellieferantFindByArtikelIId(Integer artikelIId) throws ExceptionLP {
		ArtikellieferantDto[] aArtikellieferantDto = null;
		try {
			aArtikellieferantDto = artikelFac.artikellieferantFindByArtikelIId(artikelIId,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return aArtikellieferantDto;
	}

	public ArtikelsprDto artikelsprFindByArtikelIIdLocaleCNrOhneExc(Integer artikelIId, String localeCNr)
			throws ExceptionLP {
		ArtikelsprDto artikelsprDto = null;
		try {
			artikelsprDto = artikelFac.artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikelIId, localeCNr,
					LPMain.getInstance().getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return artikelsprDto;
	}

	public void vertauscheShopgruppen(Integer iIdShopgruppe1, Integer iIdShopgruppe2) throws ExceptionLP {
		try {
			artikelFac.vertauscheShopgruppen(iIdShopgruppe1, iIdShopgruppe2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public ArrayList<String> getVorgaengerArtikel(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.getVorgaengerArtikel(artikelIId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VendidataArticleExportResult exportiere4VendingArtikel(boolean checkOnly) throws ExceptionLP {
		try {
			return artikelFac.exportiere4VendingArtikel(checkOnly, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer generiere4VendingId(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.generiere4VendingId(artikelIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public void delete4VendingId(Integer artikelIId) throws ExceptionLP {
		try {
			artikelFac.delete4VendingId(artikelIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	public void resetArtikelTruTops(ArtikelTruTopsId artikelTruTopsId) throws ExceptionLP {
		try {
			artikelFac.resetArtikelTruTops(artikelTruTopsId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}
	
	public String generiereGTIN13Nummer(Integer artikelIId) throws ExceptionLP {
		try {
			return artikelFac.generiereGTIN13VerkaufseanNummer(artikelIId, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public Integer gibtEsBereitsEinenBevorzugtenArtikel(String artikelnummer) throws ExceptionLP {
		try {
			return artikelFac.gibtEsBereitsEinenBevorzugtenArtikel(artikelnummer, LPMain.getInstance().getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public Map<String, String> getAllSprWebshoparten() throws ExceptionLP {
		try {
			return artikelFac.getAllSprWebshoparten(LPMain.getTheClient().getLocUiAsString());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public WebabfrageArtikellieferantResult aktualisiereArtikellieferantByWebabfrage(
			WebabfrageArtikellieferantProperties webabfrageProperties) throws ExceptionLP {
		try {
			return artikelFac.aktualisiereArtikellieferantByWebabfrage(webabfrageProperties, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
}
