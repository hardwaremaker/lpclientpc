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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;


public class PanelTransportkostenEinesWEs extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;

	private WrapperLabel wlaTransportkosten = new WrapperLabel();
	private WrapperLabel wlaTransportkostenwaehrung = new WrapperLabel();
	public WrapperNumberField wnfTransportkosten = new WrapperNumberField();
	private WrapperLabel wlaZollkosten = new WrapperLabel();
	private WrapperLabel wlaZollkostenwaehrung = new WrapperLabel();
	public WrapperNumberField wnfZollkosten = new WrapperNumberField();
	private WrapperLabel wlaBankspesen = new WrapperLabel();
	private WrapperLabel wlaBankspesenwaehrung = new WrapperLabel();
	public WrapperNumberField wnfBankspesen = new WrapperNumberField();
	private WrapperLabel wlaSonstigespesen = new WrapperLabel();
	private WrapperLabel wlaSonstigespesenwaehrung = new WrapperLabel();
	public WrapperNumberField wnfSonstigespesen = new WrapperNumberField();

	private WrapperLabel wlaRabattsatz = new WrapperLabel();
	private WrapperNumberField wnfRabattsatz = new WrapperNumberField();

	private WrapperLabel wlaGemeinKostenFaktor = new WrapperLabel();
	private WrapperNumberField wnfGemeinKostenFaktor = new WrapperNumberField();
	private WrapperLabel wlaProzent = new WrapperLabel();
	private WrapperLabel wlaProzent1 = new WrapperLabel();

	private GridBagLayout gridBagLayoutWorkingPanel = null;

	WareneingangDto weDto = null;

	PanelQuery panelQuery = null;

	JDialog dialogParent = null;

	public PanelTransportkostenEinesWEs(InternalFrame internalFrame,
			String add2TitleI, PanelQuery panelQuery, JDialog dialogParent)
			throws Throwable {
		super(internalFrame, add2TitleI);

		this.panelQuery = panelQuery;
		this.dialogParent = dialogParent;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BESTELLUNG;
	}

	protected void dto2Components() throws ExceptionLP {
		wnfGemeinKostenFaktor.setDouble(weDto.getDGemeinkostenfaktor());
		wnfTransportkosten.setBigDecimal(weDto.getNTransportkosten());

		wnfZollkosten.setBigDecimal(weDto.getNZollkosten());
		wnfBankspesen.setBigDecimal(weDto.getNBankspesen());
		wnfSonstigespesen.setBigDecimal(weDto.getNSonstigespesen());

		wnfRabattsatz.setDouble(weDto.getFRabattsatz());
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() instanceof PanelQuery) {

				Integer wareneingangIId = (Integer) ((PanelQuery) e.getSource())
						.getSelectedId();

				if (wareneingangIId != null) {
					weDto = DelegateFactory
							.getInstance()
							.getWareneingangDelegate()
							.wareneingangFindByPrimaryKeyOhneExc(
									wareneingangIId);
				}
				if (weDto != null) {

					dto2Components();
					enableAllComponents(this, false);
					setzeButton(PanelBasis.ACTION_SAVE, false, false);
					setzeButton(PanelBasis.ACTION_DISCARD, false, false);
					setzeButton(PanelBasis.ACTION_REFRESH, true, false);
					setzeButton(PanelBasis.ACTION_UPDATE, true, false);
					setzeButton(PanelBasis.ACTION_REFRESH, true, true);
					// setzeButton(PanelBasis.ACTION_PRINT, true, true);

				} else {
					weDto = null;
					setzeButton(PanelBasis.ACTION_SAVE, false, false);
					setzeButton(PanelBasis.ACTION_DISCARD, false, false);
					setzeButton(PanelBasis.ACTION_REFRESH, false, false);
					setzeButton(PanelBasis.ACTION_UPDATE, false, false);
					// setzeButton(PanelBasis.ACTION_PRINT, true, true);

				}

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_ESCAPE) {
			getInternalFrame().removeItemChangedListener(this);
			dialogParent.dispose();
		}
	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wlaTransportkosten.setText(LPMain
				.getTextRespectUISPr("bes.transportkosten"));
		wnfTransportkosten.setMinimumValue(0);
		wnfTransportkosten.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseWE());

		wlaZollkosten.setText(LPMain.getTextRespectUISPr("bes.zollkosten"));
		wnfZollkosten.setMinimumValue(0);
		wnfZollkosten.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseWE());
		wlaZollkostenwaehrung.setHorizontalAlignment(SwingConstants.LEADING);

		wlaSonstigespesen.setText(LPMain
				.getTextRespectUISPr("bes.sonstigespesen"));
		wnfSonstigespesen.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseWE());
		wlaSonstigespesenwaehrung
				.setHorizontalAlignment(SwingConstants.LEADING);

		wlaRabattsatz.setText(LPMain.getTextRespectUISPr("label.rabattsatz"));
		wnfRabattsatz.setColumns(QueryParameters.FLR_BREITE_XS);
		wnfRabattsatz.setMandatoryFieldDB(true);

		wlaBankspesen.setText(LPMain.getTextRespectUISPr("bes.bankspesen"));
		wnfBankspesen.setMinimumValue(0);
		wnfBankspesen.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseWE());
		wlaBankspesenwaehrung.setHorizontalAlignment(SwingConstants.LEADING);

		wlaGemeinKostenFaktor.setText(LPMain
				.getTextRespectUISPr("bes.gemeinkostenfaktor"));
		wnfGemeinKostenFaktor.setMinimumValue(0);
		wnfGemeinKostenFaktor.setMaximumValue(100);
		wnfGemeinKostenFaktor.setActivatable(false);

		wlaProzent1.setText(LPMain.getTextRespectUISPr("label.prozent"));
		wlaProzent1.setHorizontalAlignment(SwingConstants.LEADING);

		wlaProzent.setText(LPMain.getTextRespectUISPr("label.prozent"));
		wlaProzent.setHorizontalAlignment(SwingConstants.LEADING);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;

		jPanelWorkingOn.add(wlaTransportkosten, new GridBagConstraints(0,
				iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfTransportkosten, new GridBagConstraints(1,
				iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaTransportkostenwaehrung, new GridBagConstraints(
				2, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaZollkosten, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfZollkosten, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaZollkostenwaehrung, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaBankspesen, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfBankspesen, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaBankspesenwaehrung, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaSonstigespesen, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfSonstigespesen, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaSonstigespesenwaehrung, new GridBagConstraints(
				2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaGemeinKostenFaktor, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfGemeinKostenFaktor, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaRabattsatz, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfRabattsatz, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent1, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void eventActionLock(ActionEvent e) throws Throwable {

	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void setDefaults() throws Throwable {

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		super.eventActionDelete(e, false, false);
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
	}

	protected void components2Dto() throws Throwable {

		ParametermandantDto pm = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_GEMEINKOSTENFAKTOR,
						weDto.getTWareneingangsdatum());

		Double gkdouble = new Double(pm.getCWert());

		if (wnfTransportkosten.getBigDecimal() == null) {
			weDto.setNTransportkosten(new BigDecimal(0));
		} else {
			weDto.setNTransportkosten(wnfTransportkosten.getBigDecimal());
		}
		if (wnfBankspesen.getBigDecimal() == null) {
			weDto.setNBankspesen(new BigDecimal(0));
		} else {
			weDto.setNBankspesen(wnfBankspesen.getBigDecimal());
		}
		if (wnfZollkosten.getBigDecimal() == null) {
			weDto.setNZollkosten(new BigDecimal(0));
		} else {
			weDto.setNZollkosten(wnfZollkosten.getBigDecimal());
		}
		if (wnfSonstigespesen.getBigDecimal() == null) {
			weDto.setNSonstigespesen(new BigDecimal(0));
		} else {
			weDto.setNSonstigespesen(wnfSonstigespesen.getBigDecimal());
		}

		weDto.setDGemeinkostenfaktor(gkdouble);
		weDto.setFRabattsatz(wnfRabattsatz.getDouble());
	}

	private void setzeButton(String button, boolean bEnabled,
			boolean bPanelQuery) {
		Collection<?> buttons = getHmOfButtons().values();
		if (bPanelQuery == true) {
			buttons = panelQuery.getHmOfButtons().values();
		}
		for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
			LPButtonAction item = (LPButtonAction) iter.next();
			if (item.getButton().getActionCommand().equals(button)) {
				item.getButton().setEnabled(bEnabled);
			}

		}
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {

	}

	protected void eventActionEscape(ActionEvent e) throws Throwable {
		int u = 0;
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		eventYouAreSelected(false);
		enableAllComponents(this, false);

		setzeButton(PanelBasis.ACTION_SAVE, false, false);
		setzeButton(PanelBasis.ACTION_DISCARD, false, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, false);
		setzeButton(PanelBasis.ACTION_UPDATE, true, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, true);
		// setzeButton(PanelBasis.ACTION_PRINT, true, true);

	}

	protected void eventActionUpdate(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		// super.eventActionUpdate(e, false);

		if (panelQuery.getSelectedId() != null) {
			enableAllComponents(this, true);
			setzeButton(PanelBasis.ACTION_SAVE, true, false);
			setzeButton(PanelBasis.ACTION_DISCARD, true, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, false);
			setzeButton(PanelBasis.ACTION_UPDATE, false, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, true);
			// setzeButton(PanelBasis.ACTION_PRINT, false, true);
		} else {
			enableAllComponents(this, false);
			setzeButton(PanelBasis.ACTION_SAVE, false, false);
			setzeButton(PanelBasis.ACTION_DISCARD, false, false);
			setzeButton(PanelBasis.ACTION_REFRESH, false, false);
			setzeButton(PanelBasis.ACTION_UPDATE, false, false);
			// setzeButton(PanelBasis.ACTION_PRINT, true, true);

		}
		// AxD: Falls hier wieder super.eventActionUpdate aufgerufen wird, diesen call
		// zu normalem setFirstFocusableComponent aendern, sonst stimmt der alte Fokus
		// nicht mehr!
		setFirstFocusableComponentSaveOld();
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		components2Dto();
		if (allMandatoryFieldsSetDlg()) {

			BigDecimal spesen = weDto.getNTransportkosten().add(
					weDto.getNBankspesen().add(
							weDto.getNZollkosten().add(
									weDto.getNSonstigespesen())));

			BigDecimal wertsumme = DelegateFactory.getInstance()
					.getWareneingangDelegate().getWareneingangWertsumme(weDto);

			if (wertsumme == null) {
				wertsumme = new BigDecimal(0);
			}
			if (wertsumme.add(spesen).doubleValue() < 0) {

				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hinweis"),
								LPMain.getTextRespectUISPr("bes.wareneingang.error.wertsumme"));
				return;
			}

			DelegateFactory.getInstance().getWareneingangDelegate()
					.updateWareneingang(weDto);
			panelQuery.eventYouAreSelected(false);

		}

		enableAllComponents(this, false);
		setzeButton(PanelBasis.ACTION_SAVE, false, false);
		setzeButton(PanelBasis.ACTION_DISCARD, false, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, false);
		setzeButton(PanelBasis.ACTION_UPDATE, true, false);
		setzeButton(PanelBasis.ACTION_REFRESH, true, true);
		setzeButton(PanelBasis.ACTION_PRINT, true, true);

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = panelQuery.getSelectedId();

		weDto = null;

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();

		} else {

			Integer wareneingangIId = (Integer) key;
			weDto = DelegateFactory.getInstance().getWareneingangDelegate()
					.wareneingangFindByPrimaryKey(wareneingangIId);

		}
	}

}
