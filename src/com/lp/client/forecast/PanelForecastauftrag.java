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


import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelForecastauftrag extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ForecastauftragDto forecastauftragDto = null;

	public void setForecastauftragDto(ForecastauftragDto forecastauftragDto) {
		this.forecastauftragDto = forecastauftragDto;
	}

	public final static String MY_OWN_NEW_TOGGLE_FREIGABE = PanelBasis.ACTION_MY_OWN_NEW
			+ "MY_OWN_NEW_FREIGABE";

	private InternalFrameForecast internalFrameForecast = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperTextField wtfBemerkung = new WrapperTextField();

	private WrapperTextField wtfKennung = new WrapperTextField(
			Facade.MAX_UNBESCHRAENKT);
	private WrapperTextField wtfKunde = new WrapperTextField(
			Facade.MAX_UNBESCHRAENKT);
	private WrapperTextField wtfLieferadresse = new WrapperTextField(
			Facade.MAX_UNBESCHRAENKT);
	private WrapperTextField wtfProjekt = new WrapperTextField(
			Facade.MAX_UNBESCHRAENKT);

	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperEditorField wefKommentar = null;

	private JLabel wlaFreigabe = new JLabel();

	public InternalFrameForecast getInternalFrameForecast() {
		return internalFrameForecast;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBemerkung;
	}

	public PanelForecastauftrag(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameForecast = (InternalFrameForecast) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();
		wlaFreigabe.setText(null);
		if (key != null && !key.equals(LPMain.getLockMeForNew())) {

			forecastauftragDto = DelegateFactory.getInstance()
					.getForecastDelegate()
					.forecastauftragFindByPrimaryKey((Integer) key);

			dto2Components();
			getInternalFrameForecast().getTpForecast().refreshTitle(false);

		} else {
			leereAlleFelder(this);

			Calendar cal = Calendar.getInstance();

		}

		wtfKennung.setText(internalFrameForecast.getTpForecast()
				.getForecastDto().getCNr());
		wtfProjekt.setText(internalFrameForecast.getTpForecast()
				.getForecastDto().getCProjekt());
		KundeDto kundeDto = DelegateFactory
				.getInstance()
				.getKundeDelegate()
				.kundeFindByPrimaryKey(
						internalFrameForecast.getTpForecast().getForecastDto()
								.getKundeIId());

		wtfKunde.setText(internalFrameForecast.getTpForecast().formatKunde(
				kundeDto));

		if (internalFrameForecast.getTpForecast().getPanelQueryLieferadresse()
				.getSelectedId() != null) {

			FclieferadresseDto fclDto = DelegateFactory
					.getInstance()
					.getForecastDelegate()
					.fclieferadresseFindByPrimaryKey(
							(Integer) internalFrameForecast.getTpForecast()
									.getPanelQueryLieferadresse()
									.getSelectedId());

			KundeDto kundeLieferadresseDto = DelegateFactory.getInstance()
					.getKundeDelegate()
					.kundeFindByPrimaryKey(fclDto.getKundeIIdLieferadresse());

			wtfLieferadresse.setText(internalFrameForecast.getTpForecast()
					.formatKunde(kundeLieferadresseDto));
		} else {
			wtfLieferadresse.setText(null);
		}

		LPButtonAction toggleFreigabe = getHmOfButtons().get(
				MY_OWN_NEW_TOGGLE_FREIGABE);
		// Wenn vorhanden
		if (toggleFreigabe != null) {
			toggleFreigabe.getButton().setEnabled(true);
		}
	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	protected void dto2Components() throws Throwable {

		wtfBemerkung.setText(forecastauftragDto.getCBemerkung());

		wefKommentar.setText(forecastauftragDto.getXKommentar());

		String text = "";

		if (forecastauftragDto.getTFreigabe() != null) {
			text = LPMain.getTextRespectUISPr("fc.freigegebenam")
					+ " "
					+ Helper.formatDatumZeit(forecastauftragDto.getTFreigabe(),
							LPMain.getTheClient().getLocUi());
		}
		if (forecastauftragDto.getPersonalIIdFreigabe() != null) {
			text += "("
					+ DelegateFactory
							.getInstance()
							.getPersonalDelegate()
							.personalFindByPrimaryKey(
									forecastauftragDto.getPersonalIIdFreigabe())
							.getCKurzzeichen() + ")";
		}

		wlaFreigabe.setText(text);

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

		wlaBemerkung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bemerkung"));

		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kommentar"));

		wefKommentar = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.kommentar"));
		wefKommentar.getLpEditor().getTextBlockAttributes(-1).capacity = SystemFac.MAX_LAENGE_EDITORTEXT_WENN_NTEXT;

		wtfKennung.setActivatable(false);
		wtfProjekt.setActivatable(false);
		wtfLieferadresse.setActivatable(false);
		wtfKunde.setActivatable(false);

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

		jpaWorkingOn.add(new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.kennung")), new GridBagConstraints(
				0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.kunde")), new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("part.adressart.lieferadresse")),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wtfLieferadresse, new GridBagConstraints(1, iZeile, 3,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.projekt")), new GridBagConstraints(
				0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 15, 2), 0, 0));
		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 3, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 15, 2), 0, 0));

		iZeile++;

		iZeile++;

		jpaWorkingOn.add(wlaBemerkung, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBemerkung, new GridBagConstraints(1, iZeile, 3, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wefKommentar, new GridBagConstraints(1, iZeile, 3, 1,
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 50));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT };

		getToolBar().addButtonCenter("/com/lp/client/res/check2.png",
				LPMain.getTextRespectUISPr("fc.freigeben"),
				MY_OWN_NEW_TOGGLE_FREIGABE, null,
				RechteFac.RECHT_FC_FORECAST_CUD);

		getToolBar().getToolsPanelCenter().add(wlaFreigabe);

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		String add2Title = LPMain.getTextRespectUISPr("fc.beschaffung");
		getInternalFrame().showReportKriterien(
				new ReportBeschaffung(getInternalFrame(), forecastauftragDto
						.getIId(), add2Title));
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		forecastauftragDto = new ForecastauftragDto();
		forecastauftragDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

		leereAlleFelder(this);

	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (getKeyWhenDetailPanel() != null
				&& getKeyWhenDetailPanel() instanceof Integer) {

			forecastauftragDto = DelegateFactory
					.getInstance()
					.getForecastDelegate()
					.forecastauftragFindByPrimaryKey(
							(Integer) getKeyWhenDetailPanel());

			boolean b = DelegateFactory
					.getInstance()
					.getForecastDelegate()
					.sindBereitsPositionenVorhanden(
							(Integer) getKeyWhenDetailPanel());
			if (b == true || forecastauftragDto.getTFreigabe() != null) {
				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}

		}

		return lockStateValue;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MY_OWN_NEW_TOGGLE_FREIGABE)) {
			// PJ 17558
			if (forecastauftragDto != null
					&& forecastauftragDto.getIId() != null) {
				TreeMap<String, BigDecimal> alMengeUnterwegsKleinerNull = DelegateFactory
						.getInstance().getForecastDelegate()
						.toggleFreigabe(forecastauftragDto.getIId());
				forecastauftragDto = DelegateFactory
						.getInstance()
						.getForecastDelegate()
						.forecastauftragFindByPrimaryKey(
								forecastauftragDto.getIId());

				internalFrameForecast.getTpForecast()
						.getPanelQueryForecastauftrag()
						.eventYouAreSelected(false);
				eventYouAreSelected(false);

				if (alMengeUnterwegsKleinerNull != null
						&& alMengeUnterwegsKleinerNull.size() > 0) {
					
					String msg = "";

					Iterator<String> it = alMengeUnterwegsKleinerNull.keySet()
							.iterator();
					while (it.hasNext()) {

						String artikelnr = it.next();
						BigDecimal bdMengeUnterwegs = alMengeUnterwegsKleinerNull
								.get(artikelnr);

						msg += Helper.fitString2Length(artikelnr, 25, ' ')
								+ Helper.fitString2LengthAlignRight(Helper
										.formatZahl(bdMengeUnterwegs, 0, LPMain
												.getTheClient().getLocUi()),
										10, ' ') +"\r\n";

					}

					

				      // create a JTextArea
				      JTextArea textArea = new JTextArea(30, 50);
				      textArea.setFont(Font.decode(Font.MONOSPACED));
				      textArea.setText(msg);
				      textArea.setEditable(false);
				      
				      // wrap a scrollpane around it
				      JScrollPane scrollPane = new JScrollPane(textArea);
				      
				      // display them in a message dialog
				      JOptionPane.showMessageDialog(getInternalFrame(), scrollPane, LPMain.getInstance()
								.getTextRespectUISPr("fc.freigabe.negativemengenunterwegs"),JOptionPane.INFORMATION_MESSAGE);
				}

			}
		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_FORECAST;
	}

	protected void setDefaults() throws Throwable {
		DateFormatSymbols symbols = new DateFormatSymbols(LPMain.getInstance()
				.getUISprLocale());
		String[] defaultMonths = symbols.getMonths();
		Map<Integer, String> m = new TreeMap<Integer, String>();
		for (int i = 0; i < defaultMonths.length - 1; i++) {
			m.put(new Integer(i), defaultMonths[i]);

		}

	}

	protected void components2Dto() throws Throwable {

		forecastauftragDto
				.setFclieferadresseIId((Integer) getInternalFrameForecast()
						.getTpForecast().getPanelQueryLieferadresse()
						.getSelectedId());

		forecastauftragDto.setCBemerkung(wtfBemerkung.getText());
		forecastauftragDto.setXKommentar(wefKommentar.getText());

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getForecastDelegate()
				.removeForecastauftrag(forecastauftragDto);

		forecastauftragDto = new ForecastauftragDto();
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, true, true);

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (forecastauftragDto.getIId() == null) {
				forecastauftragDto.setIId(DelegateFactory.getInstance()
						.getForecastDelegate()
						.createForecastauftrag(forecastauftragDto));
				setKeyWhenDetailPanel(forecastauftragDto.getIId());
				forecastauftragDto = DelegateFactory
						.getInstance()
						.getForecastDelegate()
						.forecastauftragFindByPrimaryKey(
								forecastauftragDto.getIId());

			} else {
				DelegateFactory.getInstance().getForecastDelegate()
						.updateForecastauftrag(forecastauftragDto);
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
