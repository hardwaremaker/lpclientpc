package com.lp.client.frame.filechooser;

public enum FileChooserConfigToken {

	ImportXlsArtikel,
	ImportXlsArtikelEigenschaften,
	ImportXlsForecastposition,
	ImportXlsForecastpositionVAT,
	ImportXlsForecastVMICC,
	ImportXlsLagerMinSoll,
	ImportXlsLos,
	ImportCsvLosKassendaten,
	ImportXlsPartner,
	ImportXlsStklArbeitsplan,
	ImportXlsEinkaufsangebotLieferant,
	ImportXlsStklMaterial,
	ImportXlsStklPruefkombination,
	ImportXlsStklPruefplan,
	ImportXlsVkMengenstaffel,
	ImportXlsWerkzeugVerschleissteil,
	ImportXmlEasydataStockMovements,
	EmailAttachment,
	ImportCsvInventurliste,
	IntelligenterImportAgstkl,
	IntelligenterImportBestellung,
	IntelligenterImportEinkaufsangebot,
	IntelligenterImportStkl,

	ImportXlsCreo,

	// Imports im Zuge Filechooser
	ImportCsvArtikel,
	ImportCsvAuftragPosition,
	ImportCsvAuftragSON,
	ImportCsvAuftragWooCommerce,
	ImportCsvForecastZeiss,
	ImportCsvAuftragShopify,
	ImportCsvAuftragAmazon,
	ImportCsvEkAgStklPositionen,
	ImportCsvFertigungIstMaterial,
	ImportCsvFertigungSollMaterial,
	ImportCsvFertigungGeraeteseriennummernAblieferung,
	ImportCsvFinanzOffenePosten,
	ImportCsvMonatsbestellung,
	ImportCsvStklPositionen,
	ImportCsvStklStrukturiertSiemens,
	
	ImportTxtFertigungTrace,
	
	ImportXlsAnfrageStuecklisten,
	ImportXlsArtikelPreispflege,
	ImportXlsAllergene,
	ImportXlsKundeSoko,
	ImportXlsSonderzeiten,
	ImportXlsStklStrukturiert,
	
	ImportXlsxLumiQuote,
	ImportXlsxAuftragVAT,
	
	ImportXmlEingangsrechnung4Vending,
	ImportXlsEingangsrechnung,
	ImportXmlFertigungProduktverbrauch4Vending,

	
	/**
	 * Verzeichnis fuer alles andere
	 */
	ImportLast,
	ImportLastCsv,
	ImportLastTxt,
	ImportLastXls,
	ImportLastDirectory,
	
	ExportLastDirectory,
	Export4Vending,
	ExportCsvExtraliste,
	ExportCsvLumiquote,
	ExportVcf,
	ExportDocEingangsrechnung
	;
	
	public String asToken() {
		return this.toString().toLowerCase();
	}
}
