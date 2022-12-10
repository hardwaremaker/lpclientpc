package com.lp.client.frame.filechooser.open;

import java.awt.Component;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.server.util.HvOptional;

public class FileOpenerFactory {

	public static XlsFileOpener artikelImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsArtikel);
	}

	public static XlsFileOpener eingangsrechnungImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsEingangsrechnung);
	}

	public static HvOptional<CsvFile> artikelImportOpenCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvArtikel).selectSingle();
	}
	
	public static HvOptional<XlsFile> allergeneImportOpenXls(Component parent) {
		return new XlsFileOpenerNew(parent, FileChooserConfigToken.ImportXlsAllergene).selectSingle();
	}
	
	public static HvOptional<XlsFile> artikelImportPreispflegeOpenXls(Component parent) {
		return new XlsFileOpenerNew(parent, FileChooserConfigToken.ImportXlsArtikelPreispflege).selectSingle();
	}

	public static XlsFileOpener stklArbeitsplanImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsStklArbeitsplan);
	}
	public static XlsFileOpener einkaufsangebotLieferantImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsEinkaufsangebotLieferant);
	}
	
	public static XlsFileOpener losImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsLos);
	}
	
	public static XlsFileOpener stklPruefkombinationImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsStklPruefkombination);
	}
	
	public static XlsFileOpener stklCreoImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsCreo);
	}
	
	public static XlsFileOpener stklPruefplanImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsStklPruefplan);
	}
	
	public static XlsFileOpener stklMaterialImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsStklMaterial);
	}
	public static XlsFileOpener forecastVMICCstklMaterialImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsForecastVMICC);
	}
	public static XlsFileOpener lagerminsollImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsLagerMinSoll);
	}
	
	public static XlsFileOpener artikelEigenschaftenImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsArtikelEigenschaften);
	}
	
	public static XlsFileOpener verschleissteilImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsWerkzeugVerschleissteil);
	}
	
	public static XlsFileOpener forecastpositionImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsForecastposition);
	}
	
	public static XlsFileOpener forecastpositionImportOpenXlsVAT(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsForecastpositionVAT);
	}
	
	public static XlsFileOpener vkMengenstaffelImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpener(internalFrame, FileChooserConfigToken.ImportXlsVkMengenstaffel);
	}
	
	public static HvOptional<XlsFile> partnerImportOpenXls(InternalFrame internalFrame) {
		return new XlsFileOpenerNew(internalFrame, FileChooserConfigToken.ImportXlsPartner).selectSingle();
	}
	
	public static CsvXlsFileOpener intelligenterStklImport(InternalFrame internalFrame, FileChooserConfigToken configToken) {
		return new CsvXlsFileOpener(internalFrame, configToken);
	}
	
	public static HvOptional<CsvFile> einkaufsangebotStklPositionenImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvEkAgStklPositionen).selectSingle();
	}
	
	public static HvOptional<XmlFile> eingangsrechnungImport4VendingXml(Component parent) {
		return new XmlFileOpener(parent, FileChooserConfigToken.ImportXmlEingangsrechnung4Vending).selectSingle();
	}
	
	public static HvOptional<CsvFile> monatsbestellungImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvMonatsbestellung).selectSingle();
	}
	
	public static HvOptional<XlsFile> sonderzeitenImportXls(Component parent) {
		return new XlsFileOpenerNew(parent, FileChooserConfigToken.ImportXlsSonderzeiten).selectSingle();
	}
	
	public static HvOptional<CsvFile> gestehungspreiseImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportLastCsv).selectSingle();
	}
	
	public static HvOptional<XlsFile> stklStrukturiertImportXls(Component parent) {
		return new XlsFileOpenerNew(parent, FileChooserConfigToken.ImportXlsStklStrukturiert).selectSingle();
	}
	
	
	public static HvOptional<XlsxFile> lumiQuoteXlsx(Component parent) {
		return new XlsxFileOpener(parent, FileChooserConfigToken.ImportXlsxLumiQuote).selectSingle();
	}
	public static HvOptional<CsvFile> stklStrukturiertImportSiemensCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvStklStrukturiertSiemens).selectSingle();
	}
	
	public static HvOptional<CsvFile> stklPositionenImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvStklPositionen).selectSingle();
	}
	
	public static HvOptional<CsvFile> partnerImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportLastCsv).selectSingle();
	}
	
	public static HvOptional<CsvFile> lieferantImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportLastCsv).selectSingle();
	}
	
	public static HvOptional<XlsFile> kundeSokoImportXls(Component parent) {
		return new XlsFileOpenerNew(parent, FileChooserConfigToken.ImportXlsKundeSoko).selectSingle();
	}
	
	public static HvOptional<CsvFile> finanzKontenImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportLastCsv).selectSingle();
	}
	
	public static HvOptional<CsvFile> finanzOffenePostenImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvFinanzOffenePosten).selectSingle();
	}
	
	public static HvOptional<TxtFile> fertigungTraceImportTxt(Component parent) {
		return new TxtFileOpener(parent, FileChooserConfigToken.ImportTxtFertigungTrace).selectSingle();
	}
	
	public static HvOptional<CsvFile> fertigungIstMaterialImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvFertigungIstMaterial).selectSingle();
	}
	public static HvOptional<CsvFile> fertigungSollMaterialImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvFertigungSollMaterial).selectSingle();
	}
	
	public static HvOptional<CsvFile> fertigungGeraeteseriennummernAblieferungImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvFertigungGeraeteseriennummernAblieferung).selectSingle();		
	}
	
	public static HvOptional<XmlFile> fertigungProduktverbrauch4VendingImportXml(Component parent) {
		return new XmlFileOpener(parent, FileChooserConfigToken
				.ImportXmlFertigungProduktverbrauch4Vending).selectSingle();
	}
	
	public static HvOptional<XlsxFile> auftragVATImportXlsx(Component parent) {
		return new XlsxFileOpener(parent, FileChooserConfigToken.ImportXlsxAuftragVAT).selectSingle();
	}
	
	public static HvOptional<CsvFile> auftragPositionImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvAuftragPosition).selectSingle();
	}
	
	public static HvOptional<CsvFile> auftragSONImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvAuftragSON).selectSingle();
	}
	public static HvOptional<CsvFile> auftragWooCommerceImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvAuftragWooCommerce).selectSingle();
	}
	public static HvOptional<CsvFile> forecastZeissImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvForecastZeiss).selectSingle();
	}
	public static HvOptional<CsvFile> auftragShopifyImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvAuftragShopify).selectSingle();
	}
	public static HvOptional<CsvFile> auftragAmazonImportCsv(Component parent) {
		return new CsvFileOpener(parent, FileChooserConfigToken.ImportCsvAuftragAmazon).selectSingle();
	}
	
	public static HvOptional<XlsFile> anfrageStuecklistenImportCsv(Component parent) {
		return new XlsFileOpenerNew(parent, FileChooserConfigToken.ImportXlsAnfrageStuecklisten).selectSingle();
	}
	
	public static HvOptional<WrapperFile> artikelDateiverweisOpen(Component parent) {
		return new AnyFileOpener(parent, 
				FileChooserConfigToken.ImportLastDirectory).selectSingleUnchecked();
	}
	
	public static HvOptional<TxtFile> finanzWarenverkehrsnummernImportTxt(Component parent) {
		return new TxtFileOpener(parent, FileChooserConfigToken.ImportLastTxt)
				.selectSingle();
	}
}
