
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
package com.lp.client.angebotstkl;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.EkaglieferantDto;
import com.lp.server.angebotstkl.service.EkgruppelieferantDto;
import com.lp.server.angebotstkl.service.PositionlieferantDto;
import com.lp.server.artikel.service.ArtikelbestelltFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.util.Helper;

public class PanelPositionlieferant extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameAngebotstkl internalFramePersonal = null;

	private PositionlieferantDto positionlieferantDto = null;

	private WrapperLabel wlaArtikelNrLieferant = new WrapperLabel();
	private WrapperTextField wtfArtikelNrLieferant = new WrapperTextField();

	private WrapperLabel wlaLieferzeitInKW = new WrapperLabel();
	private WrapperNumberField wnfLieferzeitInKW = new WrapperNumberField();

	private WrapperLabel wlaLagerstand = new WrapperLabel();
	private WrapperNumberField wnfLagerstand = new WrapperNumberField();

	private WrapperLabel wlaVerpackungseinheit = new WrapperLabel();
	private WrapperNumberField wnfVerpackungseinheit = new WrapperNumberField();

	private WrapperLabel wlaTransportkosten = new WrapperLabel();
	private WrapperNumberField wnfTransportkosten = new WrapperNumberField();

	private WrapperLabel wlaMindestbestellmenge = new WrapperLabel();
	private WrapperNumberField wnfMindestbestellmenge = new WrapperNumberField();

	private WrapperLabel wlaPreisMenge1 = new WrapperLabel();
	private WrapperNumberField wnfPreisMenge1 = new WrapperNumberField();

	private WrapperLabel wlaPreisMenge2 = new WrapperLabel();
	private WrapperNumberField wnfPreisMenge2 = new WrapperNumberField();

	private WrapperLabel wlaPreisMenge3 = new WrapperLabel();
	private WrapperNumberField wnfPreisMenge3 = new WrapperNumberField();

	private WrapperLabel wlaPreisMenge4 = new WrapperLabel();
	private WrapperNumberField wnfPreisMenge4 = new WrapperNumberField();

	private WrapperLabel wlaPreisMenge5 = new WrapperLabel();
	private WrapperNumberField wnfPreisMenge5 = new WrapperNumberField();

	private JLabel wlaWaehrungPreis1 = new JLabel();
	private JLabel wlaWaehrungPreis2 = new JLabel();
	private JLabel wlaWaehrungPreis3 = new JLabel();
	private JLabel wlaWaehrungPreis4 = new JLabel();
	private JLabel wlaWaehrungPreis5 = new JLabel();

	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperTextField wtfBemerkung = new WrapperTextField();

	private WrapperLabel wlaBemerkungIntern = new WrapperLabel();
	private WrapperTextField wtfBemerkungIntern = new WrapperTextField();

	private WrapperLabel wlaBemerkungVerkauf = new WrapperLabel();
	private WrapperTextField wtfBemerkungVerkauf = new WrapperTextField();

	private WrapperLabel wlaBestellt = new WrapperLabel();
	private WrapperNumberField wnfBestellt = new WrapperNumberField();

	private WrapperLabel wlaRahmenbestellt = new WrapperLabel();
	private WrapperNumberField wnfRahmenbestellt = new WrapperNumberField();

	private WrapperLabel wlaVerfuegbar = new WrapperLabel();
	private WrapperNumberField wnfVerfuegbar = new WrapperNumberField();

	private WrapperLabel wlaLetzterEK = new WrapperLabel();

	public PanelPositionlieferant(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFrameAngebotstkl) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() throws Throwable {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfArtikelNrLieferant;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI, Integer ekagIId)
			throws Throwable {
		super.eventActionNew(eventObject, true, false);
		positionlieferantDto = new PositionlieferantDto();
		positionlieferantDto.setEinkaufsangebotpositionIId(ekagIId);
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getAngebotstklDelegate().removePositionlieferant(positionlieferantDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {

		positionlieferantDto.setEgaklieferantIId((Integer) internalFramePersonal.getTabbedPaneEinkaufsangebot()
				.getPanelQueryEkaglieferant().getSelectedId());
		positionlieferantDto.setCArtikelnrlieferant(wtfArtikelNrLieferant.getText());
		positionlieferantDto.setNPreisMenge1(wnfPreisMenge1.getBigDecimal());
		positionlieferantDto.setNPreisMenge2(wnfPreisMenge2.getBigDecimal());
		positionlieferantDto.setNPreisMenge3(wnfPreisMenge3.getBigDecimal());
		positionlieferantDto.setNPreisMenge4(wnfPreisMenge4.getBigDecimal());
		positionlieferantDto.setNPreisMenge5(wnfPreisMenge5.getBigDecimal());

		positionlieferantDto.setILieferzeitinkw(wnfLieferzeitInKW.getInteger());

		positionlieferantDto.setCBemerkung(wtfBemerkung.getText());
		positionlieferantDto.setCBemerkungIntern(wtfBemerkungIntern.getText());
		positionlieferantDto.setCBemerkungVerkauf(wtfBemerkungVerkauf.getText());

		positionlieferantDto.setNLagerstand(wnfLagerstand.getBigDecimal());
		positionlieferantDto.setNMindestbestellmenge(wnfMindestbestellmenge.getBigDecimal());
		positionlieferantDto.setNTransportkosten(wnfTransportkosten.getBigDecimal());
		positionlieferantDto.setNVerpackungseinheit(wnfVerpackungseinheit.getBigDecimal());

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		wtfArtikelNrLieferant.setText(positionlieferantDto.getCArtikelnrlieferant());

		wnfPreisMenge1.setBigDecimal(positionlieferantDto.getNPreisMenge1());
		wnfPreisMenge2.setBigDecimal(positionlieferantDto.getNPreisMenge2());
		wnfPreisMenge3.setBigDecimal(positionlieferantDto.getNPreisMenge3());
		wnfPreisMenge4.setBigDecimal(positionlieferantDto.getNPreisMenge4());
		wnfPreisMenge5.setBigDecimal(positionlieferantDto.getNPreisMenge5());

		wnfLieferzeitInKW.setInteger(positionlieferantDto.getILieferzeitinkw());

		wnfLagerstand.setBigDecimal(positionlieferantDto.getNLagerstand());
		wnfMindestbestellmenge.setBigDecimal(positionlieferantDto.getNMindestbestellmenge());
		wnfTransportkosten.setBigDecimal(positionlieferantDto.getNTransportkosten());
		wnfVerpackungseinheit.setBigDecimal(positionlieferantDto.getNVerpackungseinheit());

		wtfBemerkung.setText(positionlieferantDto.getCBemerkung());
		wtfBemerkungIntern.setText(positionlieferantDto.getCBemerkungIntern());
		wtfBemerkungVerkauf.setText(positionlieferantDto.getCBemerkungVerkauf());

		EinkaufsangebotpositionDto einkaufsangebotpositionDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.einkaufsangebotpositionFindByPrimaryKey(positionlieferantDto.getEinkaufsangebotpositionIId());
		EinkaufsangebotDto einkaufsangebotDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.einkaufsangebotFindByPrimaryKey(einkaufsangebotpositionDto.getBelegIId());

		EkaglieferantDto ekaglieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.ekaglieferantFindByPrimaryKey(positionlieferantDto.getEgaklieferantIId());

		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferant()
				.uebersteuereSpaltenUeberschrift(8,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge1(), 0) + "/"
								+ ekaglieferantDto.getWaehrungCNr());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferant()
				.uebersteuereSpaltenUeberschrift(9,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge2(), 0) + "/"
								+ ekaglieferantDto.getWaehrungCNr());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferant()
				.uebersteuereSpaltenUeberschrift(10,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge3(), 0) + "/"
								+ ekaglieferantDto.getWaehrungCNr());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferant()
				.uebersteuereSpaltenUeberschrift(11,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge4(), 0) + "/"
								+ ekaglieferantDto.getWaehrungCNr());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferant()
				.uebersteuereSpaltenUeberschrift(12,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge5(), 0) + "/"
								+ ekaglieferantDto.getWaehrungCNr());

		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferant()
				.uebersteuereSpaltenUeberschrift(13,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge1(), 0) + "/"
								+ LPMain.getTheClient().getSMandantenwaehrung());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferant()
				.uebersteuereSpaltenUeberschrift(14,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge2(), 0) + "/"
								+ LPMain.getTheClient().getSMandantenwaehrung());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferant()
				.uebersteuereSpaltenUeberschrift(15,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge3(), 0) + "/"
								+ LPMain.getTheClient().getSMandantenwaehrung());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferant()
				.uebersteuereSpaltenUeberschrift(16,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge4(), 0) + "/"
								+ LPMain.getTheClient().getSMandantenwaehrung());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferant()
				.uebersteuereSpaltenUeberschrift(17,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge5(), 0) + "/"
								+ LPMain.getTheClient().getSMandantenwaehrung());

		wlaLetzterEK.setText(LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.positionlieferant.letzterwe.kein"));
		wlaLetzterEK.setToolTipText("");

		if (einkaufsangebotpositionDto.getArtikelIId() != null) {
			BigDecimal bestellt = DelegateFactory.getInstance().getArtikelbestelltDelegate()
					.getAnzahlBestellt(einkaufsangebotpositionDto.getArtikelIId());
			wnfBestellt.setBigDecimal(bestellt);

			BigDecimal rahmenbestellt = null;
			Hashtable<?, ?> htAnzahlRahmenbestellt = DelegateFactory.getInstance().getArtikelbestelltDelegate()
					.getAnzahlRahmenbestellt(einkaufsangebotpositionDto.getArtikelIId());
			if (htAnzahlRahmenbestellt.containsKey(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL)) {
				rahmenbestellt = (BigDecimal) htAnzahlRahmenbestellt.get(ArtikelbestelltFac.KEY_RAHMENBESTELLT_ANZAHL);
				wnfRahmenbestellt.setBigDecimal(rahmenbestellt);
			}

			BigDecimal lagerstand = DelegateFactory.getInstance().getLagerDelegate()
					.getLagerstandAllerLagerEinesMandanten(einkaufsangebotpositionDto.getArtikelIId(), false);

			BigDecimal fehlmengen = DelegateFactory.getInstance().getFehlmengeDelegate()
					.getAnzahlFehlmengeEinesArtikels(einkaufsangebotpositionDto.getArtikelIId());
			BigDecimal reservierungen = DelegateFactory.getInstance().getReservierungDelegate()
					.getAnzahlReservierungen(einkaufsangebotpositionDto.getArtikelIId());

			BigDecimal verfuegbar = lagerstand.subtract(reservierungen).subtract(fehlmengen);

			if (verfuegbar.doubleValue() < 0) {
				wlaVerfuegbar.setForeground(Color.RED);
			} else {
				wlaVerfuegbar.setForeground(Color.BLACK);
			}

			wnfVerfuegbar.setBigDecimal(verfuegbar);

			Integer wepIId = DelegateFactory.getInstance().getLagerDelegate()
					.getLetzteWEP_IID(einkaufsangebotpositionDto.getArtikelIId());
			if (wepIId != null) {
				WareneingangspositionDto weposDto = DelegateFactory.getInstance().getWareneingangDelegate()
						.wareneingangspositionFindByPrimaryKeyOhneExc(wepIId);
				if (weposDto != null) {

					WareneingangDto weDto = DelegateFactory.getInstance().getWareneingangDelegate()
							.wareneingangFindByPrimaryKey(weposDto.getWareneingangIId());
					BestellungDto bsDto = DelegateFactory.getInstance().getBestellungDelegate()
							.bestellungFindByPrimaryKey(weDto.getBestellungIId());
					LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
							.lieferantFindByPrimaryKey(bsDto.getLieferantIIdBestelladresse());

					String letzterPreis = Helper.formatZahl(weposDto.getNGelieferterpreis(),
							Defaults.getInstance().getIUINachkommastellenPreiseEK(), LPMain.getTheClient().getLocUi())
							+ " " + bsDto.getWaehrungCNr();

					String letztesWEDatum = Helper.formatDatum(weDto.getTWareneingangsdatum(),
							LPMain.getTheClient().getLocUi());

					String text = LPMain.getMessageTextRespectUISPr(
							"agstkl.einkaufsangebot.positionlieferant.letzterwe", letzterPreis, bsDto.getCNr(),
							lfDto.getPartnerDto().formatFixName1Name2(), letztesWEDatum);

					wlaLetzterEK.setText(text);
					wlaLetzterEK.setToolTipText(text);

				}
			}
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();
			if (positionlieferantDto.getIId() == null) {
				positionlieferantDto.setIId(DelegateFactory.getInstance().getAngebotstklDelegate()
						.createPositionlieferant(positionlieferantDto));
				setKeyWhenDetailPanel(positionlieferantDto.getIId());
			} else {
				DelegateFactory.getInstance().getAngebotstklDelegate().updatePositionlieferant(positionlieferantDto);
			}

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(internalFramePersonal.getEinkaufsangebotDto().getIId() + "");
			}
			eventYouAreSelected(false);

		}

	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);

		wlaArtikelNrLieferant.setText(LPMain.getTextRespectUISPr("agstkl.lieferant.positionen.artikelnrlieferant"));
		wlaLagerstand.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.lagerstand"));
		wlaLieferzeitInKW.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.lieferzeitinkw"));
		wlaTransportkosten.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.transportkosten"));
		wlaVerpackungseinheit.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.verpackungseinheit"));
		wlaBemerkung.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.bemerkung"));
		wlaBemerkungIntern.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.bemerkungintern"));
		wlaBemerkungVerkauf.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.bemerkungverkauf"));
		wlaMindestbestellmenge.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.mindestbestellmenge"));
		wlaPreisMenge1.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.preis.menge1"));
		wlaPreisMenge2.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.preis.menge2"));
		wlaPreisMenge3.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.preis.menge3"));
		wlaPreisMenge4.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.preis.menge4"));
		wlaPreisMenge5.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.preis.menge5"));

		wnfLieferzeitInKW.setFractionDigits(0);
		wtfBemerkung.setColumnsMax(300);
		wtfBemerkungIntern.setColumnsMax(300);
		wtfBemerkungVerkauf.setColumnsMax(300);

		wlaRahmenbestellt.setText(LPMain.getTextRespectUISPr("lp.rahmenbestellt"));
		wlaBestellt.setText(LPMain.getTextRespectUISPr("lp.bestellt"));
		wlaVerfuegbar.setText(LPMain.getTextRespectUISPr("lp.verfuegbar"));

		wnfRahmenbestellt.setActivatable(false);
		wnfBestellt.setActivatable(false);
		wnfVerfuegbar.setActivatable(false);

		int iNachkommastellenMenge = Defaults.getInstance().getIUINachkommastellenMenge();

		int iNachkommastellenPreis = Defaults.getInstance().getIUINachkommastellenPreiseEK();

		wnfPreisMenge1.setFractionDigits(iNachkommastellenPreis);
		wnfPreisMenge2.setFractionDigits(iNachkommastellenPreis);
		wnfPreisMenge3.setFractionDigits(iNachkommastellenPreis);
		wnfPreisMenge4.setFractionDigits(iNachkommastellenPreis);
		wnfPreisMenge5.setFractionDigits(iNachkommastellenPreis);

		wnfTransportkosten.setFractionDigits(iNachkommastellenPreis);

		wnfLagerstand.setFractionDigits(iNachkommastellenMenge);
		wnfMindestbestellmenge.setFractionDigits(iNachkommastellenMenge);
		wnfVerpackungseinheit.setFractionDigits(iNachkommastellenMenge);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaArtikelNrLieferant, new GridBagConstraints(1, iZeile, 1, 1, 0.15, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wtfArtikelNrLieferant, new GridBagConstraints(2, iZeile, 1, 1, 0.15, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wlaLagerstand, new GridBagConstraints(4, iZeile, 1, 1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfLagerstand, new GridBagConstraints(5, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaLieferzeitInKW, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfLieferzeitInKW, new GridBagConstraints(2, iZeile, 1, 1, 0.15, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wlaVerpackungseinheit, new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfVerpackungseinheit, new GridBagConstraints(5, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMindestbestellmenge, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfMindestbestellmenge, new GridBagConstraints(2, iZeile, 1, 1, 0.15, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wlaTransportkosten, new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfTransportkosten, new GridBagConstraints(5, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaPreisMenge1, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfPreisMenge1, new GridBagConstraints(2, iZeile, 1, 1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wlaWaehrungPreis1, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wlaPreisMenge2, new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfPreisMenge2, new GridBagConstraints(5, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wlaWaehrungPreis2, new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaPreisMenge3, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfPreisMenge3, new GridBagConstraints(2, iZeile, 1, 1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wlaWaehrungPreis3, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 30, 0));

		jpaWorkingOn.add(wlaPreisMenge4, new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfPreisMenge4, new GridBagConstraints(5, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wlaWaehrungPreis4, new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaPreisMenge5, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfPreisMenge5, new GridBagConstraints(2, iZeile, 1, 1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wlaWaehrungPreis5, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 30, 0));

		iZeile++;
		jpaWorkingOn.add(wlaBemerkung, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wtfBemerkung, new GridBagConstraints(2, iZeile, 3, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaBemerkungIntern, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wtfBemerkungIntern, new GridBagConstraints(2, iZeile, 3, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaBemerkungVerkauf, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wtfBemerkungVerkauf, new GridBagConstraints(2, iZeile, 3, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaVerfuegbar, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfVerfuegbar, new GridBagConstraints(2, iZeile, 1, 1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOn.add(wlaBestellt, new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfBestellt, new GridBagConstraints(5, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaRahmenbestellt, new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOn.add(wnfRahmenbestellt, new GridBagConstraints(5, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;

		wlaLetzterEK.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaLetzterEK, new GridBagConstraints(1, iZeile, 5, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_EINKAUFSANGEBOT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();

		} else {
			positionlieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
					.positionlieferantFindByPrimaryKey((Integer) key);
			dto2Components();

		}
		Integer ekaglieferantIId = (Integer) internalFramePersonal.getTabbedPaneEinkaufsangebot()
				.getPanelQueryEkaglieferant().getSelectedId();

		if (ekaglieferantIId != null) {

			EkaglieferantDto ekaglieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
					.ekaglieferantFindByPrimaryKey(ekaglieferantIId);

			wlaWaehrungPreis1.setText(ekaglieferantDto.getWaehrungCNr());
			wlaWaehrungPreis2.setText(ekaglieferantDto.getWaehrungCNr());
			wlaWaehrungPreis3.setText(ekaglieferantDto.getWaehrungCNr());
			wlaWaehrungPreis4.setText(ekaglieferantDto.getWaehrungCNr());
			wlaWaehrungPreis5.setText(ekaglieferantDto.getWaehrungCNr());
		} else {
			wlaWaehrungPreis1.setText("");
			wlaWaehrungPreis2.setText("");
			wlaWaehrungPreis3.setText("");
			wlaWaehrungPreis4.setText("");
			wlaWaehrungPreis5.setText("");
		}

	}
}
