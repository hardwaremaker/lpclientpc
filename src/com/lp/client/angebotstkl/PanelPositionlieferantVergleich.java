
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.EkaglieferantDto;
import com.lp.server.angebotstkl.service.EkgruppelieferantDto;
import com.lp.server.angebotstkl.service.PositionlieferantDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

public class PanelPositionlieferantVergleich extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOnGewaehlt = new JPanel();
	private JPanel jpaWorkingOnBilligster = new JPanel();
	private JPanel jpaWorkingOnSchnellster = new JPanel();

	private JSplitPane sp2 = null;
	private JPanel jpaArtikel = null;

	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameAngebotstkl internalFramePersonal = null;

	private PositionlieferantDto positionlieferantDto = null;

	WrapperLabel lblLieferantAktuell = new WrapperLabel();
	WrapperLabel lblLieferantBilligster = new WrapperLabel();
	WrapperLabel lblLieferantSchnellster = new WrapperLabel();
	WrapperLabel lblLieferantBestellt = new WrapperLabel();

	private JCheckBox[] wcbBestellen = new JCheckBox[5];

	private int lastDividerLocationSp2 = 400;
	private int lastDividerLocationSpLinks = 200;
	private int lastDividerLocationSpRechts = 200;

	public PanelPositionlieferantVergleich(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
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
		return null;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		positionlieferantDto = new PositionlieferantDto();
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wcbBestellen[0])) {
			positionlieferantDto.setBMenge1Bestellen(
					Helper.boolean2Short(!Helper.short2boolean(positionlieferantDto.getBMenge1Bestellen())));
		} else if (e.getSource().equals(wcbBestellen[1])) {
			positionlieferantDto.setBMenge2Bestellen(
					Helper.boolean2Short(!Helper.short2boolean(positionlieferantDto.getBMenge2Bestellen())));
		} else if (e.getSource().equals(wcbBestellen[2])) {
			positionlieferantDto.setBMenge3Bestellen(
					Helper.boolean2Short(!Helper.short2boolean(positionlieferantDto.getBMenge3Bestellen())));
		} else if (e.getSource().equals(wcbBestellen[3])) {
			positionlieferantDto.setBMenge4Bestellen(
					Helper.boolean2Short(!Helper.short2boolean(positionlieferantDto.getBMenge4Bestellen())));
		} else if (e.getSource().equals(wcbBestellen[4])) {
			positionlieferantDto.setBMenge5Bestellen(
					Helper.boolean2Short(!Helper.short2boolean(positionlieferantDto.getBMenge5Bestellen())));
		}

		DelegateFactory.getInstance().getAngebotstklDelegate().updatePositionlieferant(positionlieferantDto);

		erzeugeUndBefuellePanel();
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getAngebotstklDelegate().removePositionlieferant(positionlieferantDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		erzeugeUndBefuellePanel();

		EinkaufsangebotpositionDto einkaufsangebotpositionDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.einkaufsangebotpositionFindByPrimaryKey(positionlieferantDto.getEinkaufsangebotpositionIId());
		EinkaufsangebotDto einkaufsangebotDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.einkaufsangebotFindByPrimaryKey(einkaufsangebotpositionDto.getBelegIId());

		EkaglieferantDto ekaglieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.ekaglieferantFindByPrimaryKey(positionlieferantDto.getEgaklieferantIId());
		LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
				.lieferantFindByPrimaryKey(ekaglieferantDto.getLieferantIId());

		lblLieferantAktuell.setText(LPMain.getTextRespectUISPr("agstkl.lieferant.positionenlfvergleich.lf.gewaehlt")
				+ lfDto.getPartnerDto().getCKbez());

		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferantVergleich()
				.uebersteuereSpaltenUeberschrift(8,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge1(), 0) + "/"
								+ ekaglieferantDto.getWaehrungCNr());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferantVergleich()
				.uebersteuereSpaltenUeberschrift(9,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge2(), 0) + "/"
								+ ekaglieferantDto.getWaehrungCNr());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferantVergleich()
				.uebersteuereSpaltenUeberschrift(10,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge3(), 0) + "/"
								+ ekaglieferantDto.getWaehrungCNr());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferantVergleich()
				.uebersteuereSpaltenUeberschrift(11,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge4(), 0) + "/"
								+ ekaglieferantDto.getWaehrungCNr());
		internalFramePersonal.getTabbedPaneEinkaufsangebot().getPanelQueryPositionlieferantVergleich()
				.uebersteuereSpaltenUeberschrift(12,
						LPMain.getTextRespectUISPr("lp.preis") + " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto.getNMenge5(), 0) + "/"
								+ ekaglieferantDto.getWaehrungCNr());

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

		int iNachkommastellenMenge = Defaults.getInstance().getIUINachkommastellenMenge();

		int iNachkommastellenPreis = Defaults.getInstance().getIUINachkommastellenPreiseEK();

		lblLieferantAktuell.setText(LPMain.getTextRespectUISPr("agstkl.lieferant.positionenlfvergleich.lf.gewaehlt"));
		lblLieferantBilligster
				.setText(LPMain.getTextRespectUISPr("agstkl.lieferant.positionenlfvergleich.lf.billigster"));
		lblLieferantSchnellster
				.setText(LPMain.getTextRespectUISPr("agstkl.lieferant.positionenlfvergleich.lf.schnellster"));

		lblLieferantBestellt.setText("bestellt");

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = {};

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public int getDividerLocationLinks() {
		return ((JSplitPane) sp2.getLeftComponent()).getDividerLocation();
	}

	public int getDividerLocationRechts() {
		return ((JSplitPane) sp2.getRightComponent()).getDividerLocation();
	}

	public int getDividerLocationMitte() {
		return sp2.getDividerLocation();
	}

	public void setDividerLocations(int mitte, int links, int rechts) throws Throwable {
		if (sp2 != null) {
			((JSplitPane) sp2.getLeftComponent()).setDividerLocation(links);
			((JSplitPane) sp2.getRightComponent()).setDividerLocation(rechts);
			sp2.setDividerLocation(mitte);
			erzeugeUndBefuellePanel();

		}
	}

	private void erzeugeUndBefuellePanel() throws Throwable {

		if (sp2 != null) {

			lastDividerLocationSp2 = sp2.getDividerLocation();

			lastDividerLocationSpLinks = ((JSplitPane) sp2.getLeftComponent()).getDividerLocation();
			lastDividerLocationSpRechts = ((JSplitPane) sp2.getRightComponent()).getDividerLocation();

			this.remove(sp2);

		}

		if (jpaArtikel != null) {
			this.remove(jpaArtikel);
		}

		this.revalidate();

		// jetzt meine felder

		jpaWorkingOnGewaehlt = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOnGewaehlt.setLayout(gridBagLayoutWorkingPanel);

		jpaWorkingOnBilligster = new JPanel();
		GridBagLayout gridBagLayoutWorkingPanelBilligster = new GridBagLayout();
		jpaWorkingOnBilligster.setLayout(gridBagLayoutWorkingPanelBilligster);

		jpaWorkingOnSchnellster = new JPanel();
		GridBagLayout gridBagLayoutWorkingPanelSchnellster = new GridBagLayout();
		jpaWorkingOnSchnellster.setLayout(gridBagLayoutWorkingPanelSchnellster);

		JPanel jpaWorkingOnBestellt = new JPanel();
		GridBagLayout gridBagLayoutWorkingPanelBestellt = new GridBagLayout();
		jpaWorkingOnBestellt.setLayout(gridBagLayoutWorkingPanelBestellt);

		JSplitPane spLlinks = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpaWorkingOnGewaehlt, jpaWorkingOnBilligster);

		JSplitPane spRechts = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jpaWorkingOnSchnellster,
				jpaWorkingOnBestellt);

		sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spLlinks, spRechts);

		spLlinks.setBorder(BorderFactory.createEmptyBorder());
		spRechts.setBorder(BorderFactory.createEmptyBorder());
		sp2.setBorder(BorderFactory.createEmptyBorder());
		sp2.setDividerLocation(lastDividerLocationSp2);
		spLlinks.setDividerLocation(lastDividerLocationSpLinks);
		spLlinks.setResizeWeight(1);
		spRechts.setDividerLocation(lastDividerLocationSpRechts);

		int iNachkommastellenMenge = Defaults.getInstance().getIUINachkommastellenMenge();

		int iNachkommastellenPreis = Defaults.getInstance().getIUINachkommastellenPreiseEK();

		EinkaufsangebotpositionDto einkaufsangebotpositionDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.einkaufsangebotpositionFindByPrimaryKey(positionlieferantDto.getEinkaufsangebotpositionIId());
		EinkaufsangebotDto einkaufsangebotDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.einkaufsangebotFindByPrimaryKey(einkaufsangebotpositionDto.getBelegIId());

		String artikelnummer = " ";
		String bezeichnung = " ";
		Integer artikelIId = null;
		if (einkaufsangebotpositionDto.getArtikelIId() != null) {
			ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(einkaufsangebotpositionDto.getArtikelIId());
			artikelnummer = aDto.getCNr();
			bezeichnung = aDto.formatBezeichnung();
			artikelIId = aDto.getIId();
		}

		EkaglieferantDto ekaglieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.ekaglieferantFindByPrimaryKey(positionlieferantDto.getEgaklieferantIId());
		LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
				.lieferantFindByPrimaryKey(ekaglieferantDto.getLieferantIId());

		lblLieferantAktuell.setText(LPMain.getTextRespectUISPr("agstkl.lieferant.positionenlfvergleich.lf.gewaehlt")
				+ lfDto.getPartnerDto().getCKbez());

		jpaArtikel = new JPanel();
		GridBagLayout gridBagLayoutjpaArtikel = new GridBagLayout();
		jpaArtikel.setLayout(gridBagLayoutjpaArtikel);

		WrapperGotoButton wbuArtikel = new WrapperGotoButton(GotoHelper.GOTO_ARTIKEL_AUSWAHL);
		wbuArtikel.setText("Artikel...");
		wbuArtikel.setOKey(artikelIId);

		jpaArtikel.add(wbuArtikel, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 50, 2, 0), 100, 0));
		if (einkaufsangebotpositionDto.getArtikelIId() == null) {
			wbuArtikel.setVisible(false);
		}

		jpaArtikel.add(new JLabel(artikelnummer), new GridBagConstraints(1, iZeile, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 0), 0, 0));

		jpaArtikel.add(new JLabel(bezeichnung), new GridBagConstraints(2, iZeile, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 2, 2, 0), 0, 0));
		jpaArtikel.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("label.waehrung") + ": "
						+ LPMain.getTheClient().getSMandantenwaehrung()),
				new GridBagConstraints(3, iZeile, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 2, 2, 0), 100, 0));

		this.add(jpaArtikel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		this.add(sp2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		iZeile++;

		jpaWorkingOnGewaehlt.add(lblLieferantAktuell, new GridBagConstraints(0, iZeile, 5, 1, 0, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOnBilligster.add(lblLieferantBilligster, new GridBagConstraints(0, iZeile, 4, 1, 0.1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOnSchnellster.add(lblLieferantSchnellster, new GridBagConstraints(0, iZeile, 4, 1, 0.1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOnBestellt.add(lblLieferantBestellt, new GridBagConstraints(0, iZeile, 4, 1, 0.1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;
		jpaWorkingOnGewaehlt.add(new JLabel("bestellen"), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnGewaehlt.add(new WrapperLabel("Menge"), new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnGewaehlt.add(new WrapperLabel("Preis"), new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnGewaehlt.add(new WrapperLabel("Lieferzeit"), new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnGewaehlt.add(new WrapperLabel("VPE"), new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOnBilligster.add(new WrapperLabel("Preis"), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnBilligster.add(new WrapperLabel("Lieferzeit"), new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnBilligster.add(new WrapperLabel("VPE"), new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnBilligster.add(new WrapperLabel("Kbez"), new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOnSchnellster.add(new WrapperLabel("Preis"), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnSchnellster.add(new WrapperLabel("Lieferzeit"), new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnSchnellster.add(new WrapperLabel("VPE"), new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnSchnellster.add(new WrapperLabel("Kbez"), new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		jpaWorkingOnBestellt.add(new WrapperLabel("Preis"), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnBestellt.add(new WrapperLabel("Lieferzeit"), new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnBestellt.add(new WrapperLabel("VPE"), new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
		jpaWorkingOnBestellt.add(new WrapperLabel("Kbez"), new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

		iZeile++;

		for (int i = 0; i < 5; i++) {

			JCheckBox jcb = new JCheckBox();

			BigDecimal bdMengeGesamt = null;
			BigDecimal bdPreisGewaehlterLieferant = null;

			int iMenge = 0;

			boolean bestellen = false;

			if (i == 0) {

				bdMengeGesamt = einkaufsangebotDto.getNMenge1().multiply(einkaufsangebotpositionDto.getNMenge());
				bdPreisGewaehlterLieferant = positionlieferantDto.getNPreisMenge1();
				iMenge = AngebotstklFac.MENGE_1;

				bestellen = Helper.short2boolean(positionlieferantDto.getBMenge1Bestellen());

			}
			if (i == 1) {

				if (einkaufsangebotDto.getNMenge2().doubleValue() <= 0) {
					continue;
				}

				bdMengeGesamt = einkaufsangebotDto.getNMenge2().multiply(einkaufsangebotpositionDto.getNMenge());
				bdPreisGewaehlterLieferant = positionlieferantDto.getNPreisMenge2();
				iMenge = AngebotstklFac.MENGE_2;

				bestellen = Helper.short2boolean(positionlieferantDto.getBMenge2Bestellen());
			}
			if (i == 2) {

				if (einkaufsangebotDto.getNMenge2().doubleValue() <= 0) {
					continue;
				}
				bdMengeGesamt = einkaufsangebotDto.getNMenge3().multiply(einkaufsangebotpositionDto.getNMenge());
				bdPreisGewaehlterLieferant = positionlieferantDto.getNPreisMenge3();
				iMenge = AngebotstklFac.MENGE_3;

				bestellen = Helper.short2boolean(positionlieferantDto.getBMenge3Bestellen());
			}
			if (i == 3) {

				if (einkaufsangebotDto.getNMenge4().doubleValue() <= 0) {
					continue;
				}

				bdMengeGesamt = einkaufsangebotDto.getNMenge4().multiply(einkaufsangebotpositionDto.getNMenge());
				bdPreisGewaehlterLieferant = positionlieferantDto.getNPreisMenge4();
				iMenge = AngebotstklFac.MENGE_4;

				bestellen = Helper.short2boolean(positionlieferantDto.getBMenge4Bestellen());
			}
			if (i == 4) {

				if (einkaufsangebotDto.getNMenge5().doubleValue() <= 0) {
					continue;
				}

				bdMengeGesamt = einkaufsangebotDto.getNMenge5().multiply(einkaufsangebotpositionDto.getNMenge());
				bdPreisGewaehlterLieferant = positionlieferantDto.getNPreisMenge5();
				iMenge = AngebotstklFac.MENGE_5;

				bestellen = Helper.short2boolean(positionlieferantDto.getBMenge5Bestellen());
			}

			jcb.setSelected(bestellen);

			jcb.addActionListener(this);

			wcbBestellen[i] = jcb;

			jpaWorkingOnGewaehlt.add(wcbBestellen[i], new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 20, 0));

			WrapperLabel wlaMenge = new WrapperLabel(
					Helper.formatZahl(bdMengeGesamt, iNachkommastellenMenge, LPMain.getTheClient().getLocUi()));

			jpaWorkingOnGewaehlt.add(wlaMenge, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

			if (bdPreisGewaehlterLieferant != null) {
				WrapperLabel wlaPreisGewaehlterLieferant = new WrapperLabel(Helper.formatZahl(
						bdPreisGewaehlterLieferant, iNachkommastellenPreis, LPMain.getTheClient().getLocUi()));
				jpaWorkingOnGewaehlt.add(wlaPreisGewaehlterLieferant, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
			}

			if (positionlieferantDto.getILieferzeitinkw() != null) {
				WrapperLabel wlaLieferzeitGewaehlterLieferant = new WrapperLabel(Helper
						.formatZahl(positionlieferantDto.getILieferzeitinkw(), 0, LPMain.getTheClient().getLocUi()));
				jpaWorkingOnGewaehlt.add(wlaLieferzeitGewaehlterLieferant, new GridBagConstraints(3, iZeile, 1, 1, 0.1,
						0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
			}

			if (positionlieferantDto.getNVerpackungseinheit() != null) {
				WrapperLabel wlaVPEGewaehlterLieferant = new WrapperLabel(Helper.formatZahl(
						positionlieferantDto.getNVerpackungseinheit(), 0, LPMain.getTheClient().getLocUi()));
				jpaWorkingOnGewaehlt.add(wlaVPEGewaehlterLieferant, new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
			}

			// Billigster

			PositionlieferantDto positionlieferantDtoBilligster = DelegateFactory.getInstance().getAngebotstklDelegate()
					.getGuenstigstenLieferant(positionlieferantDto.getEinkaufsangebotpositionIId(), iMenge);

			BigDecimal bdPreisBilligsterLieferant = null;

			if (positionlieferantDtoBilligster != null) {
				if (i == 0) {
					bdPreisBilligsterLieferant = positionlieferantDtoBilligster.getNPreisMenge1();

				}
				if (i == 1) {
					bdPreisBilligsterLieferant = positionlieferantDtoBilligster.getNPreisMenge2();

				}
				if (i == 2) {
					bdPreisBilligsterLieferant = positionlieferantDtoBilligster.getNPreisMenge3();

				}
				if (i == 3) {
					bdPreisBilligsterLieferant = positionlieferantDtoBilligster.getNPreisMenge4();

				}
				if (i == 4) {
					bdPreisBilligsterLieferant = positionlieferantDtoBilligster.getNPreisMenge5();

				}
			}
			if (bdPreisBilligsterLieferant != null) {
				WrapperLabel wlaPreisBilligsterLieferant = new WrapperLabel(Helper.formatZahl(
						bdPreisBilligsterLieferant, iNachkommastellenPreis, LPMain.getTheClient().getLocUi()));

				jpaWorkingOnBilligster.add(wlaPreisBilligsterLieferant, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
						0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
			}

			if (positionlieferantDtoBilligster != null && positionlieferantDtoBilligster.getILieferzeitinkw() != null) {
				WrapperLabel wlaLieferzeitBilligsterLieferant = new WrapperLabel(Helper.formatZahl(
						positionlieferantDtoBilligster.getILieferzeitinkw(), 0, LPMain.getTheClient().getLocUi()));
				jpaWorkingOnBilligster.add(wlaLieferzeitBilligsterLieferant,
						new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
			}
			if (positionlieferantDtoBilligster != null
					&& positionlieferantDtoBilligster.getNVerpackungseinheit() != null) {
				WrapperLabel wlaVPEBilligsterLieferant = new WrapperLabel(Helper.formatZahl(
						positionlieferantDtoBilligster.getNVerpackungseinheit(), 0, LPMain.getTheClient().getLocUi()));
				jpaWorkingOnBilligster.add(wlaVPEBilligsterLieferant, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
			}

			WrapperLabel wlaKbezBilligsterLieferant = new WrapperLabel(" ");

			if (positionlieferantDtoBilligster != null) {
				EkaglieferantDto ekaglieferantDtoBilligster = DelegateFactory.getInstance().getAngebotstklDelegate()
						.ekaglieferantFindByPrimaryKey(positionlieferantDtoBilligster.getEgaklieferantIId());
				LieferantDto lfDtoBilligster = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(ekaglieferantDtoBilligster.getLieferantIId());
				wlaKbezBilligsterLieferant = new WrapperLabel(lfDtoBilligster.getPartnerDto().getCKbez());
			}

			jpaWorkingOnBilligster.add(wlaKbezBilligsterLieferant, new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

			// Schnellster

			PositionlieferantDto positionlieferantDtoSchnellster = DelegateFactory.getInstance()
					.getAngebotstklDelegate()
					.getSchnellstenLieferant(positionlieferantDto.getEinkaufsangebotpositionIId());

			BigDecimal bdPreisSchnellsterLieferant = null;

			if (positionlieferantDtoSchnellster != null) {

				if (i == 0) {
					bdPreisSchnellsterLieferant = positionlieferantDtoSchnellster.getNPreisMenge1();

				}
				if (i == 1) {
					bdPreisSchnellsterLieferant = positionlieferantDtoSchnellster.getNPreisMenge2();

				}
				if (i == 2) {
					bdPreisSchnellsterLieferant = positionlieferantDtoSchnellster.getNPreisMenge3();

				}
				if (i == 3) {
					bdPreisSchnellsterLieferant = positionlieferantDtoSchnellster.getNPreisMenge4();

				}
				if (i == 4) {
					bdPreisSchnellsterLieferant = positionlieferantDtoSchnellster.getNPreisMenge5();

				}
			}

			if (bdPreisSchnellsterLieferant != null) {
				WrapperLabel wlaPreisSchnellsterLieferant = new WrapperLabel(Helper.formatZahl(
						bdPreisSchnellsterLieferant, iNachkommastellenPreis, LPMain.getTheClient().getLocUi()));
				jpaWorkingOnSchnellster.add(wlaPreisSchnellsterLieferant, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
						0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

				if (positionlieferantDtoSchnellster != null
						&& positionlieferantDtoSchnellster.getILieferzeitinkw() != null) {
					WrapperLabel wlaLieferzeitSchnellsterLieferant = new WrapperLabel(Helper.formatZahl(
							positionlieferantDtoSchnellster.getILieferzeitinkw(), 0, LPMain.getTheClient().getLocUi()));
					jpaWorkingOnSchnellster.add(wlaLieferzeitSchnellsterLieferant,
							new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
				}

				if (positionlieferantDtoSchnellster != null
						&& positionlieferantDtoSchnellster.getNVerpackungseinheit() != null) {
					WrapperLabel wlaVPESchnellsterLieferant = new WrapperLabel(
							Helper.formatZahl(positionlieferantDtoSchnellster.getNVerpackungseinheit(), 0,
									LPMain.getTheClient().getLocUi()));
					jpaWorkingOnSchnellster.add(wlaVPESchnellsterLieferant,
							new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
				}

				if (positionlieferantDtoSchnellster != null) {

					EkaglieferantDto ekaglieferantDtoSchnellster = DelegateFactory.getInstance()
							.getAngebotstklDelegate()
							.ekaglieferantFindByPrimaryKey(positionlieferantDtoSchnellster.getEgaklieferantIId());
					LieferantDto lfDtoSchnellster = DelegateFactory.getInstance().getLieferantDelegate()
							.lieferantFindByPrimaryKey(ekaglieferantDtoSchnellster.getLieferantIId());

					WrapperLabel wlaKbezSchnellsterLieferant = new WrapperLabel(
							lfDtoSchnellster.getPartnerDto().getCKbez());
					jpaWorkingOnSchnellster.add(wlaKbezSchnellsterLieferant,
							new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
				}

			} else {
				jpaWorkingOnSchnellster.add(new WrapperLabel(""), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
			}

			// Bestellt

			PositionlieferantDto positionlieferantDtoBestellt = DelegateFactory.getInstance().getAngebotstklDelegate()
					.getBestelltLieferant(positionlieferantDto.getIId(), iMenge);

			if (positionlieferantDtoBestellt != null) {

				BigDecimal bdPreisBestelltLieferant = null;

				if (i == 0) {
					bdPreisBestelltLieferant = positionlieferantDtoBestellt.getNPreisMenge1();

				}
				if (i == 1) {
					bdPreisBestelltLieferant = positionlieferantDtoBestellt.getNPreisMenge2();

				}
				if (i == 2) {
					bdPreisBestelltLieferant = positionlieferantDtoBestellt.getNPreisMenge3();

				}
				if (i == 3) {
					bdPreisBestelltLieferant = positionlieferantDtoBestellt.getNPreisMenge4();

				}
				if (i == 4) {
					bdPreisBestelltLieferant = positionlieferantDtoBestellt.getNPreisMenge5();

				}

				if (bdPreisBestelltLieferant != null) {
					WrapperLabel wlaPreisBestelltLieferant = new WrapperLabel(Helper.formatZahl(
							bdPreisBestelltLieferant, iNachkommastellenPreis, LPMain.getTheClient().getLocUi()));
					jpaWorkingOnBestellt.add(wlaPreisBestelltLieferant,
							new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

				} else {

				}

				if (positionlieferantDtoBestellt.getILieferzeitinkw() != null) {
					WrapperLabel wlaLieferzeitBestelltLieferant = new WrapperLabel(Helper.formatZahl(
							positionlieferantDtoBestellt.getILieferzeitinkw(), 0, LPMain.getTheClient().getLocUi()));
					jpaWorkingOnBestellt.add(wlaLieferzeitBestelltLieferant,
							new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
				}

				if (positionlieferantDtoBestellt.getNVerpackungseinheit() != null) {
					WrapperLabel wlaVPESchnellsterLieferant = new WrapperLabel(
							Helper.formatZahl(positionlieferantDtoBestellt.getNVerpackungseinheit(), 0,
									LPMain.getTheClient().getLocUi()));
					jpaWorkingOnBestellt.add(wlaVPESchnellsterLieferant,
							new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
				}

				EkaglieferantDto ekaglieferantDtoBestellt = DelegateFactory.getInstance().getAngebotstklDelegate()
						.ekaglieferantFindByPrimaryKey(positionlieferantDtoBestellt.getEgaklieferantIId());
				LieferantDto lfDtoBestellt = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(ekaglieferantDtoBestellt.getLieferantIId());

				WrapperLabel wlaKbezBestelltLieferant = new WrapperLabel(lfDtoBestellt.getPartnerDto().getCKbez());
				jpaWorkingOnBestellt.add(wlaKbezBestelltLieferant, new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));

			} else {
				jpaWorkingOnBestellt.add(new WrapperLabel(" "), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 0), 0, 0));
			}
			iZeile++;
		}

		this.revalidate();

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
					.positionlieferantFindByPrimaryKeyInZielWaehrung((Integer) key,
							LPMain.getTheClient().getSMandantenwaehrung());
			dto2Components();

		}

	}
}
