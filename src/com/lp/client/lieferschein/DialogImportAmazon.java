package com.lp.client.lieferschein;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.lp.client.frame.DialogImportAllgemein;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.auftrag.service.ImportAmazonCsvDto;
import com.lp.server.system.service.ImportRueckgabeDto;

public class DialogImportAmazon extends DialogImportAllgemein {

	LinkedHashMap<String, ArrayList<ImportAmazonCsvDto>> hm = null;
	Integer lagerIIdAbbuchungslager = null;

	public DialogImportAmazon(LinkedHashMap<String, ArrayList<ImportAmazonCsvDto>> hmNachBestellnummerGruppiert,
			Integer lagerIIdAbbuchungslager, TabbedPane tabbedpane) throws Throwable {
		super(tabbedpane);
		this.hm = hmNachBestellnummerGruppiert;
		this.lagerIIdAbbuchungslager = lagerIIdAbbuchungslager;
		aktuaisierePruefergebnis();

	}

	@Override
	public ImportRueckgabeDto pruefen() throws Throwable {
		// TODO Auto-generated method stub
		return DelegateFactory.getInstance().getLsDelegate().importiereAmazon_CSV(hm, lagerIIdAbbuchungslager, false);
	}

	@Override
	public void importieren() throws Throwable {
		DelegateFactory.getInstance().getLsDelegate().importiereAmazon_CSV(hm, lagerIIdAbbuchungslager, true);
	}

}
