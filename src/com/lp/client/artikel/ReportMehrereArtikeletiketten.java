package com.lp.client.artikel;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.ArtikelReportFac.ReportMehrereArtikeletikettenSortierung;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.report.JasperPrintLP;

import net.miginfocom.swing.MigLayout;
import net.miginfocom.swing.SwingComponentWrapper;

public class ReportMehrereArtikeletiketten extends PanelBasis implements PanelReportIfJRDS {

	private static final long serialVersionUID = 1L;

	private WrapperIdentField wifArtikelVon = null;
	private WrapperIdentField wifArtikelBis = null;
	private WrapperComboBox wcbArtikelgruppe = null;
	private WrapperSelectField wsfArtikelklasse = null;
	private WrapperSelectField wsfLager = null;
	private WrapperSelectField wsfLagerplatzVon = null;
	private WrapperSelectField wsfLagerplatzBis = null;
	private WrapperSelectField wsfShopgruppe = null;

	private WrapperLabel wlaArtikelgruppe = new WrapperLabel();

	private WrapperLabel wlaSortierung = new WrapperLabel();

	private ButtonGroup btnGroupSortierung = new ButtonGroup();
	private WrapperRadioButton wrbSortAsc = new WrapperRadioButton();
	private WrapperRadioButton wrbSortDesc = new WrapperRadioButton();
	private ButtonGroup btnGroupSortOder = new ButtonGroup();
	private WrapperCheckBox wcbEtikettProLagerplatz = new WrapperCheckBox();

	private final boolean hatMehrfachlagerplatz;

	public ReportMehrereArtikeletiketten(InternalFrameArtikel internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		hatMehrfachlagerplatz = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MEHRFACH_LAGERPLATZ_JE_LAGER);
		jbInit();
	}

	private void jbInit() throws Throwable {

		JPanel contentPane = new JPanel();

		wifArtikelVon = new WrapperIdentField(getInternalFrame(), this);
		wifArtikelBis = new WrapperIdentField(getInternalFrame(), this);
		wsfLager = new WrapperSelectField(WrapperSelectField.LAGER, getInternalFrame(), true);
		wsfLagerplatzVon = new WrapperSelectField(WrapperSelectField.LAGERPLATZ, getInternalFrame(), true);
		wsfLagerplatzBis = new WrapperSelectField(WrapperSelectField.LAGERPLATZ, getInternalFrame(), true);
		wcbArtikelgruppe = new WrapperComboBox();
		wsfArtikelklasse = new WrapperSelectField(WrapperSelectField.ARTIKELKLASSE, getInternalFrame(), true);
		wsfShopgruppe = new WrapperSelectField(WrapperSelectField.SHOPGRUPPE, getInternalFrame(), true);

		wifArtikelVon.getWbuArtikel().setText(LPMain.getTextRespectUISPr("artikel.report.multietikett.artikelvon"));
		wifArtikelBis.getWbuArtikel().setText(LPMain.getTextRespectUISPr("artikel.report.multietikett.artikelbis"));
		wlaArtikelgruppe.setText(LPMain.getTextRespectUISPr("lp.artikelgruppe"));
		wsfArtikelklasse.setText(LPMain.getTextRespectUISPr("lp.artikelklasse"));
		wsfLager.setText(LPMain.getTextRespectUISPr("label.lager"));
		wsfLagerplatzVon.setText(LPMain.getTextRespectUISPr("artikel.report.multietikett.lagerplatzvon"));
		wsfLagerplatzBis.setText(LPMain.getTextRespectUISPr("artikel.report.multietikett.lagerplatzbis"));
		wsfShopgruppe.setText(LPMain.getTextRespectUISPr("lp.shopgruppe"));

		wcbArtikelgruppe.setEmptyEntry(LPMain.getTextRespectUISPr("lp.alle"));
		wcbEtikettProLagerplatz.setText(LPMain.getTextRespectUISPr("artikel.report.multietikett.etikettprolager"));
		wcbEtikettProLagerplatz.setSelected(true);

		try {
			wcbArtikelgruppe.setMap(DelegateFactory.getInstance().getArtikelDelegate().getAllSprArtgru(), false);
		} catch (ExceptionLP e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		wifArtikelVon.getWbuArtikel().setPreferredSize(HelperClient.getSizeFactoredDimension(120));
		wifArtikelBis.getWbuArtikel().setPreferredSize(HelperClient.getSizeFactoredDimension(120));
		wifArtikelVon.getWbuArtikel().getWrapperButtonGoTo().setVisible(false);
		wifArtikelBis.getWbuArtikel().getWrapperButtonGoTo().setVisible(false);

		wifArtikelBis.getWtfIdent().setMandatoryField(false);
		wifArtikelVon.getWtfIdent().setMandatoryField(false);

		wlaSortierung.setText("Sortierung");

		wrbSortAsc.setText(LPMain.getTextRespectUISPr("artikel.report.multietikett.order.asc"));
		wrbSortDesc.setText(LPMain.getTextRespectUISPr("artikel.report.multietikett.order.desc"));
		wrbSortAsc.setHorizontalAlignment(SwingConstants.RIGHT);
		wrbSortDesc.setHorizontalAlignment(SwingConstants.RIGHT);
		btnGroupSortOder.add(wrbSortAsc);
		btnGroupSortOder.add(wrbSortDesc);

		wrbSortAsc.setSelected(true);

		JPanel sortPanel = createSortierBtns();
		wifArtikelVon.getWtfIdent().setPreferredSize(sortPanel.getPreferredSize());
		contentPane.setLayout(new MigLayout("fillx", "[grow 20][grow 100][grow 20][grow 100]"));
		contentPane.add(wifArtikelVon.getWbuArtikel(), "growx");
		contentPane.add(wifArtikelVon.getWtfIdent(), "growx");
		contentPane.add(wifArtikelBis.getWbuArtikel(), "growx");
		contentPane.add(wifArtikelBis.getWtfIdent(), "growx,wrap");
		contentPane.add(wlaArtikelgruppe, "growx");
		contentPane.add(wcbArtikelgruppe, "growx");
		contentPane.add(wlaSortierung, "growx, align right");
		contentPane.add(sortPanel, "grow, span 2 4, wrap");
		contentPane.add(wsfArtikelklasse, "growx");
		contentPane.add(wsfArtikelklasse.getWrapperTextField(), "growx");
		contentPane.add(wrbSortAsc, "alignx center, growx, wrap");
		contentPane.add(wsfShopgruppe, "growx");
		contentPane.add(wsfShopgruppe.getWrapperTextField(), "growx");
		contentPane.add(wrbSortDesc, "alignx center, growx, wrap");
		contentPane.add(wsfLager, "growx");
		contentPane.add(wsfLager.getWrapperTextField(), "growx, wrap");
		contentPane.add(wsfLagerplatzVon, "growx");
		contentPane.add(wsfLagerplatzVon.getWrapperTextField(), "growx");
		contentPane.add(wsfLagerplatzBis, "growx");
		contentPane.add(wsfLagerplatzBis.getWrapperTextField(), "growx,wrap");

		if (hatMehrfachlagerplatz) {
			contentPane.add(wcbEtikettProLagerplatz, "span, growx");
		}

		setLayout(new BorderLayout());
		add(contentPane, BorderLayout.CENTER);

	}

	private JPanel createSortierBtns() {
		WrapperRadioButton wrbSortArtikelNr = new WrapperRadioButton();
		WrapperRadioButton wrbSortArtikelgruppe = new WrapperRadioButton();
		WrapperRadioButton wrbSortArtikelklasse = new WrapperRadioButton();
		WrapperRadioButton wrbSortLagerort = new WrapperRadioButton();
		WrapperRadioButton wrbSortArtikelbezeichnung = new WrapperRadioButton();
		WrapperRadioButton wrbSortArtikelkurzbezeichnung = new WrapperRadioButton();
		WrapperRadioButton wrbSortShopgruppe = new WrapperRadioButton();
		WrapperRadioButton wrbSortReferenznummer = new WrapperRadioButton();

		wrbSortArtikelNr.setText(LPMain.getTextRespectUISPr("artikel.artikelnummer"));
		wrbSortArtikelNr.setActionCommand(ReportMehrereArtikeletikettenSortierung.SORT_ARTIKELNUMMER.name());
		wrbSortArtikelgruppe.setText(LPMain.getTextRespectUISPr("lp.artikelgruppe"));
		wrbSortArtikelgruppe.setActionCommand(ReportMehrereArtikeletikettenSortierung.SORT_ARTIKELGRUPPE.name());
		wrbSortArtikelklasse.setText(LPMain.getTextRespectUISPr("lp.artikelklasse"));
		wrbSortArtikelklasse.setActionCommand(ReportMehrereArtikeletikettenSortierung.SORT_ARTIKELKLASSE.name());
		wrbSortLagerort.setText(LPMain.getTextRespectUISPr("artikel.lagerort"));
		wrbSortLagerort.setActionCommand(ReportMehrereArtikeletikettenSortierung.SORT_LAGERORT.name());
		wrbSortArtikelbezeichnung.setText(LPMain.getTextRespectUISPr("artikel.report.multietikett.sort.artikelbez"));
		wrbSortArtikelbezeichnung
				.setActionCommand(ReportMehrereArtikeletikettenSortierung.SORT_ARTIKELBEZEICHNUNG.name());
		wrbSortArtikelkurzbezeichnung.setText(LPMain.getTextRespectUISPr("lp.kurzbezeichnung"));
		wrbSortArtikelkurzbezeichnung
				.setActionCommand(ReportMehrereArtikeletikettenSortierung.SORT_ARTIKELKURZBEZEICHNUNG.name());
		wrbSortShopgruppe.setText(LPMain.getTextRespectUISPr("lp.shopgruppe"));
		wrbSortShopgruppe.setActionCommand(ReportMehrereArtikeletikettenSortierung.SORT_SHOPGRUPPE.name());
		wrbSortReferenznummer.setText(LPMain.getTextRespectUISPr("lp.referenznummer"));
		wrbSortReferenznummer.setActionCommand(ReportMehrereArtikeletikettenSortierung.SORT_REFERENZNUMMER.name());
		btnGroupSortierung.add(wrbSortArtikelNr);
		btnGroupSortierung.add(wrbSortArtikelgruppe);
		btnGroupSortierung.add(wrbSortArtikelklasse);
		btnGroupSortierung.add(wrbSortLagerort);
		btnGroupSortierung.add(wrbSortArtikelbezeichnung);
		btnGroupSortierung.add(wrbSortArtikelkurzbezeichnung);
		btnGroupSortierung.add(wrbSortShopgruppe);
		btnGroupSortierung.add(wrbSortReferenznummer);

		wrbSortArtikelNr.setSelected(true);
		JPanel sortPanel = new JPanel();

		sortPanel.setLayout(new GridLayout(0, 2));
		sortPanel.add(wrbSortArtikelNr);
		sortPanel.add(wrbSortArtikelbezeichnung);
		sortPanel.add(wrbSortArtikelgruppe);
		sortPanel.add(wrbSortArtikelkurzbezeichnung);
		sortPanel.add(wrbSortArtikelklasse);
		sortPanel.add(wrbSortShopgruppe);
		sortPanel.add(wrbSortLagerort);
		sortPanel.add(wrbSortReferenznummer);
		return sortPanel;
	}

	@Override
	public String getModul() {
		return ArtikelReportFac.REPORT_MODUL;
	}

	@Override
	public String getReportname() {
		return ArtikelReportFac.REPORT_ARTIKELETIKETT;
	}

	@Override
	public boolean getBErstelleReportSofort() {
		return false;
	}

	@Override
	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	@Override
	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		String artikelgruppeFilt = null;
		if (!wcbArtikelgruppe.isSelectedItemEmpty()) {
			artikelgruppeFilt = wcbArtikelgruppe.getKeyOfSelectedItem().toString();
		}
		Integer artikelklasseId = wsfArtikelklasse.getIKey();
		Integer artikelVonId = wifArtikelVon.getArtikelIId();
		Integer artikelBisId = wifArtikelBis.getArtikelIId();
		Integer lagerId = wsfLager.getIKey();
		Integer lagerplatzVonId = wsfLagerplatzVon.getIKey();
		Integer lagerplatzBisId = wsfLagerplatzBis.getIKey();
		Integer shopgruppeId = wsfShopgruppe.getIKey();

		boolean sortOrder = wrbSortAsc.isSelected();

		boolean etikettProLagerplatz;
		if (hatMehrfachlagerplatz) {
			etikettProLagerplatz = wcbEtikettProLagerplatz.isSelected();
		} else {
			// Ist eigentlich egal, weil ja hier nur 1 Lagerplatz pro Lager sein kann
			etikettProLagerplatz = true;
		}

		ReportMehrereArtikeletikettenSortierung sortierung = ReportMehrereArtikeletikettenSortierung
				.valueOf(btnGroupSortierung.getSelection().getActionCommand());

		return DelegateFactory.getInstance().getArtikelReportDelegate().printMehrereArtikeletiketten(artikelgruppeFilt,
				artikelklasseId, artikelVonId, artikelBisId, lagerId, lagerplatzVonId, lagerplatzBisId, shopgruppeId,
				sortierung, sortOrder, etikettProLagerplatz);
	}

}
