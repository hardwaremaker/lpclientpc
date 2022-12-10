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
package com.lp.client.forecast;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelLinienabruf extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinienabrufDto linienabrufDto = null;

	private InternalFrameForecast internalFrameForecast = null;
	TabbedPaneForecast tabbedPaneForecast = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private WrapperLabel wlaArtikelPositionstermin = null;
	private WrapperLabel wlaArtikelPositionstermin2 = new WrapperLabel();

	private WrapperLabel wlaLinie = new WrapperLabel();
	private WrapperTextField wtfLinie = new WrapperTextField();

	private WrapperLabel wlaBereich = new WrapperLabel();
	private WrapperTextField wtfBereich = new WrapperTextField();

	private WrapperLabel wlaBereichBez = new WrapperLabel();
	private WrapperTextField wtfBereichBez = new WrapperTextField();

	private WrapperLabel wlaBestellnummer = new WrapperLabel();
	private WrapperTextField wtfBestellnummer = new WrapperTextField();

	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();

	private WrapperLabel wlaProduktionstermin = new WrapperLabel();

	private WrapperDateField wdfProduktionstermin = new WrapperDateField();

	private JLabel wlaPositionsmenge = new JLabel();

	public InternalFrameForecast getInternalFrameForecast() {
		return internalFrameForecast;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfProduktionstermin;
	}

	public PanelLinienabruf(InternalFrame internalFrame, String add2TitleI,
			Object pk, TabbedPaneForecast tabbedPaneForecast) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameForecast = (InternalFrameForecast) internalFrame;
		this.tabbedPaneForecast = tabbedPaneForecast;
		jbInit();
		setDefaults();
		initComponents();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (tabbedPaneForecast.getPanelQueryPositionen().getSelectedId() != null) {

			ForecastpositionDto fpDto = DelegateFactory
					.getInstance()
					.getForecastDelegate()
					.forecastpositionFindByPrimaryKey(
							(Integer) tabbedPaneForecast
									.getPanelQueryPositionen().getSelectedId());
			ArtikelDto aDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(fpDto.getArtikelIId());

			wlaArtikelPositionstermin2.setText(aDto.getCNr()
					+ " / "
					+ Helper.formatDatum(fpDto.getTTermin(), LPMain
							.getTheClient().getLocUi()));

			wlaPositionsmenge.setText(LPMain.getInstance().getTextRespectUISPr(
					"fc.lininenabruf.positionsmenge")
					+ " "
					+ Helper.formatZahl(fpDto.getNMenge(), Defaults
							.getInstance().getIUINachkommastellenMenge(),
							LPMain.getTheClient().getLocUi()) );

			
			
			
		} else {
			wlaArtikelPositionstermin2.setText(null);
			wlaPositionsmenge.setText("");
		}

		if (key != null && !key.equals(LPMain.getLockMeForNew())) {

			linienabrufDto = DelegateFactory.getInstance()
					.getForecastDelegate()
					.linienabrufFindByPrimaryKey((Integer) key);

			dto2Components();
			getInternalFrameForecast().getTpForecast().refreshTitle(true);

		} else {
			leereAlleFelder(this);

		}

	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	protected void dto2Components() throws Throwable {

		// wlaArtikelPositionstermin2.setKeyOfSelectedItem(linienabrufDto.getForecastoffsetIId());
		wtfLinie.setText(linienabrufDto.getCLinie());

		wtfBereich.setText(linienabrufDto.getCBereichNr());
		wtfBereichBez.setText(linienabrufDto.getCBereichBez());
		wtfBestellnummer.setText(linienabrufDto.getCBestellnummer());

		wnfMenge.setBigDecimal(linienabrufDto.getNMenge());

		wdfProduktionstermin.setTimestamp(linienabrufDto
				.getTProduktionstermin());
		
		
		wlaPositionsmenge.setText(wlaPositionsmenge.getText() +" / "+ LPMain.getInstance().getTextRespectUISPr(
					"fc.position.mengeerfasst.linienabruf") + Helper.formatZahl(linienabrufDto.getNMengeErfasst(), Defaults
							.getInstance().getIUINachkommastellenMenge(),
							LPMain.getTheClient().getLocUi()));
		

	}

	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wlaProduktionstermin.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.linienabruf.produktionsdatum"));

		wdfProduktionstermin.setMandatoryField(true);

		wlaLinie.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.linienabruf.linie"));

		wlaArtikelPositionstermin = new WrapperLabel(
				LPMain.getTextRespectUISPr("fc.linienabruf.artikeltermin"));
		HelperClient.setDefaultsToComponent(wlaArtikelPositionstermin, 115);

		wlaBereich.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.linienabruf.bereich"));
		wlaBereichBez.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.linienabruf.bereichbez"));

		wlaBestellnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"fc.linienabruf.bestellnummer"));

		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));

		wlaArtikelPositionstermin2.setHorizontalAlignment(SwingConstants.LEFT);

		wtfBereich.setMandatoryField(true);
		wtfBereichBez.setMandatoryField(true);
		wtfBestellnummer.setMandatoryField(true);
		wtfLinie.setMandatoryField(true);
		wnfMenge.setMandatoryField(true);
		wnfMenge.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaArtikelPositionstermin, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaArtikelPositionstermin2, new GridBagConstraints(1,
				iZeile, 3, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 150, 0));
		iZeile++;

		jpaWorkingOn.add(wlaProduktionstermin, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfProduktionstermin, new GridBagConstraints(1,
				iZeile, 3, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 70, 0));

		iZeile++;

		jpaWorkingOn.add(wlaLinie, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfLinie, new GridBagConstraints(1, iZeile, 3, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaBereich, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBereich, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaBereichBez, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBereichBez, new GridBagConstraints(1, iZeile, 3, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaBestellnummer, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBestellnummer, new GridBagConstraints(1, iZeile, 3,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaPositionsmenge, new GridBagConstraints(2, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		String add2Title = LPMain.getTextRespectUISPr("fc.beschaffung");
		getInternalFrame().showReportKriterien(
				new ReportBeschaffung(getInternalFrame(), linienabrufDto
						.getIId(), add2Title));
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		linienabrufDto = new LinienabrufDto();

		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wlaArtikelPositionstermin2)) {

		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_FORECAST;
	}

	protected void setDefaults() throws Throwable {

		wdfProduktionstermin.setDate(new java.util.Date());

	}

	protected void components2Dto() throws ExceptionLP {

		linienabrufDto.setForecastpositionIId((Integer) tabbedPaneForecast
				.getPanelQueryPositionen().getSelectedId());

		linienabrufDto.setCLinie(wtfLinie.getText());

		linienabrufDto.setCBereichNr(wtfBereich.getText());
		linienabrufDto.setCBereichBez(wtfBereichBez.getText());
		linienabrufDto.setCBestellnummer(wtfBestellnummer.getText());

		linienabrufDto.setNMenge(wnfMenge.getBigDecimal());

		linienabrufDto.setTProduktionstermin(wdfProduktionstermin
				.getTimestamp());

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getForecastDelegate()
				.removeLinienabruf(linienabrufDto);

		linienabrufDto = new LinienabrufDto();
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, true, true);

	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (tabbedPaneForecast.getPanelQueryForecastauftrag().getSelectedId() != null) {

			ForecastauftragDto fcaDto = DelegateFactory
					.getInstance()
					.getForecastDelegate()
					.forecastauftragFindByPrimaryKey(
							(Integer) tabbedPaneForecast
									.getPanelQueryForecastauftrag()
									.getSelectedId());

			if (fcaDto.getStatusCNr().equals(LocaleFac.STATUS_FREIGEGEBEN)) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}

		}

		return lockStateValue;
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			Integer forecastpositionIId = (Integer) tabbedPaneForecast
					.getPanelQueryPositionen().getSelectedId();
			ForecastpositionDto fpDto = DelegateFactory.getInstance()
					.getForecastDelegate()
					.forecastpositionFindByPrimaryKey(forecastpositionIId);

			// PJ19843
			ArtikelDto aDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(fpDto.getArtikelIId());

			if (Helper.short2boolean(aDto.getBKommissionieren())) {
				boolean b = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getInstance()
										.getTextRespectUISPr(
												"artikel.kommissionieren.lininenabruf.warning"));

				if (b == false) {
					return;
				}
			}

			if (linienabrufDto.getIId() == null) {
				linienabrufDto.setIId(DelegateFactory.getInstance()
						.getForecastDelegate()
						.createLinienabruf(linienabrufDto));
				setKeyWhenDetailPanel(linienabrufDto.getIId());
				linienabrufDto = DelegateFactory.getInstance()
						.getForecastDelegate()
						.linienabrufFindByPrimaryKey(linienabrufDto.getIId());

			} else {
				DelegateFactory.getInstance().getForecastDelegate()
						.updateLinienabruf(linienabrufDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						getInternalFrameForecast().getTpForecast()
								.getForecastDto().getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

}
