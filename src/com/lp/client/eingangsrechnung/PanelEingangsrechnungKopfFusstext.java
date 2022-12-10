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
package com.lp.client.eingangsrechnung;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.bestellung.BestellungFilterFactory;
import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldTextmodul;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ModulberechtigungDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich um die Kopfdaten der Eingangsrechnung</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.3 $
 */
public class PanelEingangsrechnungKopfFusstext extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private Border border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);

	protected WrapperLabel wlaKopftext = new WrapperLabel();
	protected WrapperLabel wlaFusstext = new WrapperLabel();

	protected WrapperEditorField wefKopftext = null;
	protected WrapperEditorField wefFusstext = null;

	public PanelEingangsrechnungKopfFusstext(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung)
			throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneEingangsrechnung = tabbedPaneEingangsrechnung;
		jbInit();

		initComponents();
	}

	private TabbedPaneEingangsrechnung getTabbedPane() {
		return tabbedPaneEingangsrechnung;
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setBorder(border1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wefFusstext = new WrapperEditorFieldTextmodul(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("label.fusstext"));
		wefKopftext = new WrapperEditorFieldTextmodul(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("label.kopftext"));
		wlaKopftext.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.kopftext"));
		HelperClient.setDefaultsToComponent(wlaKopftext, 110);
		wlaFusstext.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.fusstext"));

		// Actionpanel
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaKopftext, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefKopftext, new GridBagConstraints(1, iZeile, 1, 1,
				1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 200, 0));

		iZeile++;
		jpaWorkingOn.add(wlaFusstext, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefFusstext, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 200, 0));

	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_EINGANGSRECHNUNG;
	}

	/**
	 * Neue ER.
	 * 
	 * @param eventObject
	 *            EventObject
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, bLockMeI, bNeedNoNewI);
		getTabbedPane().setEingangsrechnungDto(null);
		getTabbedPane().setLieferantDto(null);

		this.leereAlleFelder(this);
		setDefaults();

		this.clearStatusbar();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		if (!bNeedNoYouAreSelectedI) {
			EingangsrechnungDto erDto = getTabbedPane()
					.getEingangsrechnungDto();
			if (erDto != null) {
				getTabbedPane().setEingangsrechnungDto(
						DelegateFactory
								.getInstance()
								.getEingangsrechnungDelegate()
								.eingangsrechnungFindByPrimaryKey(
										erDto.getIId()));
				dto2Components();
				getTabbedPane().enablePanels();
			}
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	/**
	 * Speichere ER.
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.updateKopfFusstextUebersteuert(
							getTabbedPane().getEingangsrechnungDto().getIId(),
							wefKopftext.getText(), wefFusstext.getText());

			EingangsrechnungDto erDto = DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungFindByPrimaryKey(
							getTabbedPane().getEingangsrechnungDto().getIId());

			this.setKeyWhenDetailPanel(erDto.getIId());
			getTabbedPane().setEingangsrechnungDto(erDto);

			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}

	}

	/**
	 * Stornieren einer ER.
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();
		if (erDto.getStatusCNr().equalsIgnoreCase(
				EingangsrechnungFac.STATUS_STORNIERT)) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint"),
					"Die Eingangsrechnung ist bereits storniert");
			return;
		}
		if (erDto.getStatusCNr().equalsIgnoreCase(
				EingangsrechnungFac.STATUS_ERLEDIGT)) {
			DialogFactory
					.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr("lp.hint"),
							"Die Eingangsrechnung ist bereits erledigt\nSie kann daher nicht storniert werden");
			return;
		}
		if (erDto.getStatusCNr().equalsIgnoreCase(
				EingangsrechnungFac.STATUS_TEILBEZAHLT)) {
			DialogFactory
					.showModalDialog(
							LPMain.getInstance().getTextRespectUISPr("lp.hint"),
							"Die Eingangsrechnung ist bereits teilweise bezahlt\nSie kann daher nicht storniert werden");
			return;
		}
		if (erDto.getStatusCNr().equals(EingangsrechnungFac.STATUS_ANGELEGT)) {
			boolean answer = (DialogFactory.showMeldung("Eingangsrechnung "
					+ erDto.getCNr() + " stornieren?", LPMain.getInstance()
					.getTextRespectUISPr("lp.frage"),
					javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
			if (!answer) {
				return;
			} else {
				DelegateFactory
						.getInstance()
						.getEingangsrechnungDelegate()
						.storniereEingangsrechnung(
								getTabbedPane().getEingangsrechnungDto()
										.getIId());
			}
		}
		super.eventActionDelete(e, false, false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
	}

	private void setDefaults() throws Throwable {

	}

	private void components2Dto() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();

		getTabbedPane().setEingangsrechnungDto(erDto);
	}

	private void dto2Components() throws Throwable {
		EingangsrechnungDto erDto = getTabbedPane().getEingangsrechnungDto();

		if (erDto.getCKopftextuebersteuert() != null) {
			wefKopftext.setText(erDto.getCKopftextuebersteuert());
		} else {
			wefKopftext.setText(DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungtextFindByMandantLocaleCNr(
							tabbedPaneEingangsrechnung.getLieferantDto()
									.getPartnerDto()
									.getLocaleCNrKommunikation(),
							MediaFac.MEDIAART_KOPFTEXT).getCTextinhalt());
		}

		if (erDto.getCFusstextuebersteuert() != null) {
			wefFusstext.setText(erDto.getCFusstextuebersteuert());
		} else {

			wefFusstext.setText(DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungtextFindByMandantLocaleCNr(
							tabbedPaneEingangsrechnung.getLieferantDto()
									.getPartnerDto()
									.getLocaleCNrKommunikation(),
							MediaFac.MEDIAART_FUSSTEXT).getCTextinhalt());

		}

		// Statusbar fuellen
		this.setStatusbarPersonalIIdAnlegen(erDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(erDto.getTAnlegen());
		this.setStatusbarPersonalIIdAendern(erDto.getPersonalIIdAendern());
		this.setStatusbarTAendern(erDto.getTAendern());
		this.setStatusbarStatusCNr(erDto.getStatusCNr());
		String status = DelegateFactory
				.getInstance()
				.getVersandDelegate()
				.getVersandstatus(LocaleFac.BELEGART_EINGANGSRECHNUNG,
						erDto.getIId());
		if (status != null) {
			status = LPMain.getInstance().getTextRespectUISPr(
					"lp.versandstatus")
					+ ": " + status;
		}
		setStatusbarSpalte5(status);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wefKopftext;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		getTabbedPane().enablePanels();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		getTabbedPane().enablePanels();
		getTabbedPane().setWareneingangDto(null);
		getTabbedPane().setInseratIIds(null);
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		getTabbedPane().print();
		eventYouAreSelected(false);
	}

	public void setMyComponents(EingangsrechnungDto eingangsrechnungDto)
			throws Throwable {

		// Statusbar fuellen
		this.setStatusbarPersonalIIdAnlegen(eingangsrechnungDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(eingangsrechnungDto.getTAnlegen());
		this.setStatusbarPersonalIIdAendern(eingangsrechnungDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(eingangsrechnungDto.getTAendern());
		this.setStatusbarStatusCNr(eingangsrechnungDto.getStatusCNr());
		String status = DelegateFactory
				.getInstance()
				.getVersandDelegate()
				.getVersandstatus(LocaleFac.BELEGART_EINGANGSRECHNUNG,
						eingangsrechnungDto.getIId());
		if (status != null) {
			status = LPMain.getInstance().getTextRespectUISPr(
					"lp.versandstatus")
					+ ": " + status;
		}
		setStatusbarSpalte5(status);
	}
}
