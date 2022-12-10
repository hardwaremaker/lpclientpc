package com.lp.client.lieferschein;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelKonditionen;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.lieferschein.service.BegruendungDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.system.service.SystemFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * In diesem Fenster werden Konditionen zum Lieferschein erfasst.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 2005-04-10
 * </p>
 * <p>
 * </p>
 * 
 * @author uli walch
 */
public class PanelLieferscheinVersandinfos extends PanelBasis implements PropertyChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Cache for convenience. */
	private TabbedPaneLieferschein tpLieferschein = null;

	private JPanel jPanelWorkingOn = new JPanel();

	private WrapperLabel wlaPakete = null;

	private WrapperNumberField wnfPakete = null;

	private WrapperLabel wlaGewicht = null;

	private WrapperNumberField wnfGewicht = null;

	private WrapperLabel wlaEinheit = null;

	private WrapperLabel wlaVersandnummer = null;
	private WrapperTextField wtfVersandnummer = null;

	private WrapperLabel wlaVersandnummer2 = null;
	private WrapperTextField wtfVersandnummer2 = null;

	public PanelLieferscheinVersandinfos(InternalFrame internalFrame, String add2TitleI, Object key) throws Throwable {

		super(internalFrame, add2TitleI, key);
		tpLieferschein = ((InternalFrameLieferschein) getInternalFrame()).getTabbedPaneLieferschein();
		jbInit();
		initComponents();

	}

	private void jbInit() throws Throwable {

		wlaPakete = new WrapperLabel(LPMain.getTextRespectUISPr("ls.pakete"));
		wnfPakete = new WrapperNumberField();
		wnfPakete.setFractionDigits(0);
		wnfPakete.setMinimumValue(0);
		wnfPakete.setMandatoryField(true);

		wlaGewicht = new WrapperLabel(LPMain.getTextRespectUISPr("lp.gewicht"));
		wnfGewicht = new WrapperNumberField();
		wnfGewicht.setMandatoryField(true);
		wlaEinheit = new WrapperLabel(SystemFac.EINHEIT_KILOGRAMM.trim());
		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		HelperClient.setDefaultsToComponent(wlaEinheit, 25);

		wlaVersandnummer = new WrapperLabel(LPMain.getTextRespectUISPr("ls.versandnummer"));
		wtfVersandnummer = new WrapperTextField();
		wtfVersandnummer.setColumnsMax(80);

		wlaVersandnummer2 = new WrapperLabel(LPMain.getTextRespectUISPr("ls.versandnummer2"));
		wtfVersandnummer2 = new WrapperTextField();
		wtfVersandnummer2.setColumnsMax(80);

		this.setLayout(new GridBagLayout());

		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		jPanelWorkingOn.setLayout(new GridBagLayout());

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile = 0; // mein eigener Teil beginnt nach den ersten drei Zeilen des
					// BasisPanel

		jPanelWorkingOn.add(wlaPakete, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jPanelWorkingOn.add(wnfPakete, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaGewicht, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfGewicht, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaEinheit, new GridBagConstraints(2, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaVersandnummer, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfVersandnummer, new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaVersandnummer2, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfVersandnummer2, new GridBagConstraints(1, iZeile, 5, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(new WrapperLabel(""), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	/**
	 * Default Werte im Panel setzen.
	 * 
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		leereAlleFelder(this);

		wnfPakete.setInteger(0);
		wnfGewicht.setDouble(new Double(0));

	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_LIEFERSCHEIN;
	}

	/**
	 * Einen existierenden Lieferschein zur Anzeige bringen.
	 * 
	 * @throws Throwable
	 */
	public void dto2Components() throws Throwable {
		wnfPakete.setDouble(new Double(tpLieferschein.getLieferscheinDto().getIAnzahlPakete().doubleValue()));
		wnfGewicht.setDouble(tpLieferschein.getLieferscheinDto().getFGewichtLieferung());
		wtfVersandnummer.setText(tpLieferschein.getLieferscheinDto().getCVersandnummer());
		wtfVersandnummer2.setText(tpLieferschein.getLieferscheinDto().getCVersandnummer2());

		aktualisiereStatusbar();
	}

	private void components2Dto() throws Throwable {

		tpLieferschein.getLieferscheinDto().setCVersandnummer(wtfVersandnummer.getText());
		tpLieferschein.getLieferscheinDto().setCVersandnummer2(wtfVersandnummer2.getText());
		tpLieferschein.getLieferscheinDto().setIAnzahlPakete(new Integer(wnfPakete.getDouble().intValue()));
		tpLieferschein.getLieferscheinDto().setFGewichtLieferung(wnfGewicht.getDouble());

	
	}

	public void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);

	}

	public void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			DelegateFactory.getInstance().getLsDelegate().updateLieferscheinVersandinfos(tpLieferschein.getLieferscheinDto());

			super.eventActionSave(e, false); // buttons schalten

			eventYouAreSelected(false);
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);

		// neu einlesen, ausloeser war ev. ein refresh oder discard
		Object oKey = tpLieferschein.getLieferscheinDto().getIId();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		tpLieferschein.setLieferscheinDto(
				DelegateFactory.getInstance().getLsDelegate().lieferscheinFindByPrimaryKey((Integer) oKey));

		dto2Components();

		tpLieferschein.setTitleLieferschein(LPMain.getTextRespectUISPr("ls.title.panel.konditionen"));

		tpLieferschein.enablePanels(tpLieferschein.getLieferscheinDto(), true);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, false); // Buttons schalten

	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpLieferschein.getLieferscheinDto().getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpLieferschein.getLieferscheinDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpLieferschein.getLieferscheinDto().getPersonalIIdAendern());
		setStatusbarTAendern(tpLieferschein.getLieferscheinDto().getTAendern());
		setStatusbarStatusCNr(tpLieferschein.getLieferscheinStatus());
	}

	/**
	 * Wenn in Liefertermin oder Finaltermin ein neues Datum gewaehlt wurde, dann
	 * landet man hier.
	 * 
	 * @param evt Ereignis
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		/*
		 * if (evt.getSource() == wdfLiefertermin.getDisplay() &&
		 * evt.getPropertyName().equals("value")) {
		 * wdfRueckgabetermin.setMinimumValue(wdfLiefertermin.getDate());
		 * 
		 * if (wdfRueckgabetermin.getDate() != null && wdfLiefertermin.getDate() !=
		 * null) { if (wdfRueckgabetermin.getDate().before(wdfLiefertermin.getDate())) {
		 * wdfRueckgabetermin.setDate(wdfLiefertermin.getDate()); } } } else if
		 * (evt.getSource() == wdfRueckgabetermin.getDisplay() &&
		 * evt.getPropertyName().equals("value")) {
		 * wdfLiefertermin.setMaximumValue(wdfRueckgabetermin.getDate()); }
		 */
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_FORMAT_NUMBER:
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.error.belegwerte"));
			break;

		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}
}
