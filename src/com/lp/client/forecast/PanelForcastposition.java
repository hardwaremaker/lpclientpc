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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.SystemFac;
import com.lp.util.Helper;

/**
 * <p>
 * Panel zum Bearbeiten der Auftragszuordnungen einer ER
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>20. 02. 2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.7 $
 */
public class PanelForcastposition extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneForecast tabbedPaneForecast = null;
	private ForecastpositionDto forecastpositionDto = null;

	private Border border = null;
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private JPanel jpaWorkingOn = new JPanel();
	private WrapperIdentField wifArtikel = null;

	static final private String ACTION_SPECIAL_POSITIONMANUELLERLEDIGEN =LEAVEALONE+ "action_special_positionmanuellerledigen";

	private WrapperLabel wlaForeastauftrag = new WrapperLabel("");

	private WrapperNumberField wnfMenge = new WrapperNumberField();
	
	private WrapperLabel wlaErfassteMenge = new WrapperLabel("");

	private Integer artikelIId_Zuletztgewaehlt = null;

	ForecastauftragDto forecastauftragDto = null;

	private JLabel wlaLinienabrufe = new JLabel();

	private WrapperGotoButton wbuUnterstueckliste = null;
	private WrapperGotoButton wbuUnterstuecklisteAndererMandant = null;

	private WrapperLabel wlaBestellnummer = new WrapperLabel();
	private WrapperTextField wtfBestellnummer = null;

	private WrapperLabel wlaTermin = new WrapperLabel();
	private WrapperDateField wdfTermin = new WrapperDateField();

	private WrapperLabel wlaStatus = new WrapperLabel();
	private WrapperComboBox wcbStatus = new WrapperComboBox();

	private WrapperLabel wlaForecastartManuell = new WrapperLabel();
	private WrapperComboBox wcbForecastartManuell = new WrapperComboBox();

	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperEditorField wefKommentar = null;

	public PanelForcastposition(InternalFrame internalFrame, String add2TitleI,
			Object key, TabbedPaneForecast tabbedPaneForecast) throws Throwable {
		super(internalFrame, add2TitleI);
		this.tabbedPaneForecast = tabbedPaneForecast;
		jbInit();
		initComponents();
	}

	private TabbedPaneForecast getTabbedPane() {
		return tabbedPaneForecast;
	}

	/**
	 * Die Klasse initialisieren.
	 * 
	 * @throws Throwable
	 */
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

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);
		wifArtikel.getWtfIdent().setMandatoryField(true);

		wlaKommentar.setText(LPMain.getTextRespectUISPr("lp.kommentar"));

		wefKommentar = new WrapperEditorFieldKommentar(getInternalFrame(),
				LPMain.getTextRespectUISPr("lp.kommentar"));
		wefKommentar.getLpEditor().getTextBlockAttributes(-1).capacity = SystemFac.MAX_LAENGE_EDITORTEXT_WENN_NTEXT;

		wlaBestellnummer = new WrapperLabel();
		wlaBestellnummer.setText(LPMain
				.getTextRespectUISPr("label.bestellnummer"));

		wlaTermin.setText(LPMain.getTextRespectUISPr("label.termin"));
		wlaStatus.setText(LPMain.getTextRespectUISPr("lp.status"));

		wlaForecastartManuell.setText(LPMain
				.getTextRespectUISPr("fc.forcastposition.facmanuell"));

		wcbStatus.setMandatoryField(true);
		wcbStatus.setMap(getStatusMap());

		wdfTermin.setMandatoryField(true);
		wnfMenge.setMandatoryField(true);
		wnfMenge.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());

		Map mFC = new LinkedHashMap();

		mFC.put(ForecastFac.FORECASTART_CALL_OFF_TAG,
				DelegateFactory
						.getInstance()
						.getForecastDelegate()
						.forecastartFindByPrimaryKey(
								ForecastFac.FORECASTART_CALL_OFF_TAG)
						.getBezeichnung());
		mFC.put(ForecastFac.FORECASTART_CALL_OFF_WOCHE,
				DelegateFactory
						.getInstance()
						.getForecastDelegate()
						.forecastartFindByPrimaryKey(
								ForecastFac.FORECASTART_CALL_OFF_WOCHE)
						.getBezeichnung());
		mFC.put(ForecastFac.FORECASTART_FORECASTAUFTRAG,
				DelegateFactory
						.getInstance()
						.getForecastDelegate()
						.forecastartFindByPrimaryKey(
								ForecastFac.FORECASTART_FORECASTAUFTRAG)
						.getBezeichnung());
		wcbForecastartManuell.setMap(mFC);

		wtfBestellnummer = new WrapperTextField();

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

		jpaWorkingOn.add(wlaBestellnummer, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBestellnummer, new GridBagConstraints(1, iZeile, 2,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		iZeile++;

		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("lp.menge")),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		
		jpaWorkingOn.add(wlaLinienabrufe, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		
		wlaErfassteMenge.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaErfassteMenge, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 20, 2, 2), 0, 0));
	
		

		iZeile++;

		jpaWorkingOn.add(wlaTermin, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfTermin, new GridBagConstraints(1, iZeile, 2, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaStatus, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbStatus, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaForecastartManuell, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbForecastartManuell, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wefKommentar, new GridBagConstraints(1, iZeile, 2, 1,
				0, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		JPanel jpaOffset = new JPanel();
		jpaOffset.setLayout(new GridBagLayout());

		// Goto Unterstueckliste
		wbuUnterstuecklisteAndererMandant = new WrapperGotoButton(
				LPMain.getTextRespectUISPr("button.stueckliste"),
				com.lp.util.GotoHelper.GOTO_STUECKLISTE_ANDERER_MANDANT_AUSWAHL);
		wbuUnterstuecklisteAndererMandant.setEnabled(false);
		wbuUnterstuecklisteAndererMandant.setActivatable(false);
		wbuUnterstuecklisteAndererMandant.getWrapperButtonGoTo()
				.setToolTipText("GOTO_ANDERER_MANDANT");
		wbuUnterstuecklisteAndererMandant.setMinimumSize(new Dimension(110,
				HelperClient.getToolsPanelButtonDimension().height));
		wbuUnterstuecklisteAndererMandant.setPreferredSize(new Dimension(110,
				HelperClient.getToolsPanelButtonDimension().height));

		// Goto Unterstueckliste
		wbuUnterstueckliste = new WrapperGotoButton(
				LPMain.getTextRespectUISPr("button.stueckliste"),
				com.lp.util.GotoHelper.GOTO_STUECKLISTE_AUSWAHL);
		wbuUnterstueckliste.setEnabled(false);
		wbuUnterstueckliste.setActivatable(false);
		wbuUnterstueckliste.getWrapperButtonGoTo().setToolTipText(
				LPMain.getTextRespectUISPr("lp.goto"));
		wbuUnterstueckliste.setMinimumSize(new Dimension(110, HelperClient
				.getToolsPanelButtonDimension().height));
		wbuUnterstueckliste.setPreferredSize(new Dimension(110, HelperClient
				.getToolsPanelButtonDimension().height));

		if (LPMain
				.getInstance()
				.getDesktopController()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {

			getToolBar().getToolsPanelCenter().add(
					wbuUnterstuecklisteAndererMandant);

		} else {
			getToolBar().getToolsPanelCenter().add(wbuUnterstueckliste);
		}
		wlaForeastauftrag.setHorizontalAlignment(SwingConstants.LEFT);
		wlaForeastauftrag.setMinimumSize(new Dimension(400, 23));
		wlaForeastauftrag.setPreferredSize(new Dimension(400, 23));
		getToolBar().getToolsPanelCenter().add(wlaForeastauftrag);
		wlaForeastauftrag.setPreferredSize(new Dimension(400, 23));

		this.createAndSaveAndShowButton(
				"/com/lp/client/res/box_preferences.png",
				LPMain.getTextRespectUISPr("fcpos.tooltip.manuellerledigen"),
				ACTION_SPECIAL_POSITIONMANUELLERLEDIGEN,
				RechteFac.RECHT_FC_FORECAST_CUD);

	}

	private Map<String, String> getStatusMap() throws ExceptionLP, Throwable {
		List<String> stati = DelegateFactory.getInstance().getForecastDelegate().getStatiEinerForecastposition();
		Map<String, String> m = new LinkedHashMap<String, String>();
		for (String status : stati) {
			m.put(status, status);
		}
		return m;
	}

	public String getLockMeWer() {
		return HelperClient.LOCKME_FORECAST;
	}

	public void eventActionNew(EventObject e, boolean bChangeKeyLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		// locknew: 1 hier muss dafuer gesorgt werden, dass der Framework lockt

		super.eventActionNew(e, true, false);
		clearStatusbar();
		forecastpositionDto = new ForecastpositionDto();

		leereAlleFelder(this);

	}

	/**
	 * Loeschen einer Rechnungsposition
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

		if (!isLockedDlg()) {
			Object[] o = tabbedPaneForecast.getPanelQueryPositionen()
					.getSelectedIds();
			if (o != null) {
				for (int i = 0; i < o.length; i++) {

					ForecastpositionDto forecastpositionDtoLocal = DelegateFactory
							.getInstance().getForecastDelegate()
							.forecastpositionFindByPrimaryKey((Integer) o[i]);

					DelegateFactory.getInstance().getForecastDelegate()
							.removeForecastposition(forecastpositionDtoLocal);
				}
			}
			this.forecastpositionDto = null;
			// selectafterdelete: key null setzen
			this.setKeyWhenDetailPanel(null);
			this.leereAlleFelder(this);
			super.eventActionDelete(e, false, false);
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		wifArtikel.validate();
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (forecastpositionDto.getIId() == null) {

				forecastpositionDto.setIId(DelegateFactory.getInstance()
						.getForecastDelegate()
						.createForecastposition(forecastpositionDto));

				setKeyWhenDetailPanel(forecastpositionDto.getIId());
			} else {
				DelegateFactory.getInstance().getForecastDelegate()
						.updateForecastposition(forecastpositionDto);

			}

			super.eventActionSave(e, true);

			eventYouAreSelected(false);

		}
	}

	private void components2Dto() throws Throwable {
		forecastpositionDto.setForecastauftragIId((Integer) getTabbedPane()
				.getPanelQueryForecastauftrag().getSelectedId());

		forecastpositionDto.setArtikelIId(wifArtikel.getArtikelDto().getIId());
		forecastpositionDto.setCBestellnummer(wtfBestellnummer.getText());
		forecastpositionDto.setTTermin(wdfTermin.getTimestamp());
		forecastpositionDto.setNMenge(wnfMenge.getBigDecimal());
		forecastpositionDto.setStatusCNr((String) wcbStatus
				.getKeyOfSelectedItem());
		forecastpositionDto.setForecastartCNr((String) wcbForecastartManuell
				.getKeyOfSelectedItem());
		forecastpositionDto.setXKommentar(wefKommentar.getText());
	}

	private void dto2Components() throws Throwable {

		ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(forecastpositionDto.getArtikelIId());

		wifArtikel.setArtikelDto(aDto);

		wefKommentar.setText(forecastpositionDto.getXKommentar());
		wnfMenge.setBigDecimal(forecastpositionDto.getNMenge());
		wdfTermin.setTimestamp(forecastpositionDto.getTTermin());
		wcbStatus.setKeyOfSelectedItem(forecastpositionDto.getStatusCNr());

		wcbForecastartManuell.setKeyOfSelectedItem(forecastpositionDto
				.getForecastartCNr());

		wlaLinienabrufe.setForeground(Color.BLACK);
		wlaLinienabrufe.setText(null);
		wlaLinienabrufe.setToolTipText(null);

		BigDecimal bdSummeLinienabrufe = DelegateFactory.getInstance()
				.getForecastDelegate()
				.getSummeLinienabrufe(forecastpositionDto.getIId());

		if (bdSummeLinienabrufe != null) {

			wlaLinienabrufe.setText("L");

			wlaLinienabrufe.setToolTipText(LPMain
					.getTextRespectUISPr("fc.linienabruf.summeabrufe")
					+ " "
					+ Helper.formatZahl(bdSummeLinienabrufe, 3, LPMain
							.getTheClient().getLocUi()));

			if (Helper.rundeKaufmaennisch(bdSummeLinienabrufe, 3).doubleValue() != Helper
					.rundeKaufmaennisch(wnfMenge.getBigDecimal(), 3)
					.doubleValue()) {
				wlaLinienabrufe.setForeground(Color.RED);

			}

		}

		
		wlaErfassteMenge.setText(LPMain
				.getTextRespectUISPr("fc.position.mengeerfasst")
				+ " "
				+ Helper.formatZahl(forecastpositionDto.getNMengeErfasst(), 3, LPMain
						.getTheClient().getLocUi()));
		
		wbuUnterstuecklisteAndererMandant.setOKey(null);
		wbuUnterstuecklisteAndererMandant.setEnabled(false);

		StuecklisteDto stuecklisteDto = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
						forecastpositionDto.getArtikelIId());
		if (stuecklisteDto != null) {
			wbuUnterstueckliste.setEnabled(false);
			wbuUnterstueckliste.setOKey(stuecklisteDto.getIId());
		} else {
			wbuUnterstueckliste.setOKey(null);
			wbuUnterstueckliste.setEnabled(false);
		}

		wbuUnterstuecklisteAndererMandant.setEnabled(false);
		wbuUnterstuecklisteAndererMandant.setOKey(forecastpositionDto
				.getArtikelIId());

		wtfBestellnummer.setText(forecastpositionDto.getCBestellnummer());

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		Integer fcposIId = (Integer) getTabbedPane().getPanelQueryPositionen()
				.getSelectedId();
		if (fcposIId != null) {
			if (e.getActionCommand().equals(
					ACTION_SPECIAL_POSITIONMANUELLERLEDIGEN)) {
				// PJ20065

				if (forecastpositionDto.getStatusCNr().equals(
						LocaleFac.STATUS_ANGELEGT)) {
					forecastpositionDto.setStatusCNr(LocaleFac.STATUS_ERLEDIGT);
				} else {
					forecastpositionDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
				}

				DelegateFactory.getInstance().getForecastDelegate()
						.updateForecastposition(forecastpositionDto);
				
				 getTabbedPane().getPanelQueryPositionen().eventYouAreSelected(false);
			

			}
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource().equals(wifArtikel.getPanelQueryFLRArtikel())) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				artikelIId_Zuletztgewaehlt = (Integer) key;
			}

		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		wlaForeastauftrag.setText("");
		wlaLinienabrufe.setText("");
		wlaErfassteMenge.setText("");

		if (getTabbedPane().getPanelQueryForecastauftrag().getSelectedId() != null) {
			ForecastauftragDto faDto = DelegateFactory
					.getInstance()
					.getForecastDelegate()
					.forecastauftragFindByPrimaryKey(
							(Integer) getTabbedPane()
									.getPanelQueryForecastauftrag()
									.getSelectedId());

			FclieferadresseDto fclDto = DelegateFactory
					.getInstance()
					.getForecastDelegate()
					.fclieferadresseFindByPrimaryKey(
							faDto.getFclieferadresseIId());

			ForecastDto fcDto = DelegateFactory.getInstance()
					.getForecastDelegate()
					.forecastFindByPrimaryKey(fclDto.getForecastIId());

			String kennung = fcDto.getCNr();
			String projekt = "";
			if (fcDto.getCProjekt() != null) {
				projekt = fcDto.getCProjekt();
			}
			String bemerkung = "";
			if (faDto.getCBemerkung() != null) {
				bemerkung = faDto.getCBemerkung();
			}

			String msg = LPMain.getMessageTextRespectUISPr(
					"fc.position.infosausfc", new Object[] { kennung, projekt,
							bemerkung });

			wlaForeastauftrag.setText(msg);

		}

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// einen neuen Eintrag anlegen oder die letzte Position wurde
			// geloescht.

			leereAlleFelder(this);

			clearStatusbar();

			if (key != null && key.equals(LPMain.getLockMeForNew())
					&& artikelIId_Zuletztgewaehlt != null) {
				ArtikelDto aDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(artikelIId_Zuletztgewaehlt);

				wifArtikel.setArtikelDto(aDto);
			}

		} else {
			// einen alten Eintrag laden.
			forecastpositionDto = DelegateFactory.getInstance()
					.getForecastDelegate()
					.forecastpositionFindByPrimaryKey((Integer) key);
			dto2Components();

		}

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wifArtikel.getWbuArtikel();
	}

	public static FocusTraversalPolicy getFocusTraversal(
			final JComponent order[]) {
		FocusTraversalPolicy policy = new FocusTraversalPolicy() {
			java.util.List<JComponent> list = java.util.Arrays.asList(order);

			public java.awt.Component getFirstComponent(
					java.awt.Container focusCycleRoot) {
				return order[0];
			}

			public java.awt.Component getLastComponent(
					java.awt.Container focusCycleRoot) {
				return order[order.length - 1];
			}

			public java.awt.Component getComponentAfter(
					java.awt.Container focusCycleRoot,
					java.awt.Component aComponent) {
				int index = 0, x = -1;
				index = list.indexOf(aComponent);
				index++; // automatisch erhoeht, sodasz er unten nichts
							// wegzeiehn
							// musz
				// er geht rein entweder wenn es disabled ist oder wenn es nicht
				// angezeigt wird
				if (!order[index % order.length].isEnabled()
						|| !order[index % order.length].isVisible()) {
					x = index;
					index = -1;
					// zuerst die Schleife nach hinten
					for (; x != order.length; x++) {
						if (order[x].isEnabled() && order[x].isVisible()) {
							index = x;
							break;
						}
					}
					if (index == -1) {
						x = list.indexOf(aComponent);
						for (int y = 0; y <= x; y++) {
							if (order[y].isEnabled() && order[x].isVisible()) {
								index = y;
								break;
							}
						}
					}
				}
				return order[index % order.length];
			}

			public java.awt.Component getComponentBefore(
					java.awt.Container focusCycleRoot,
					java.awt.Component aComponent) {
				int index = list.indexOf(aComponent), x = -1;
				index--;
				if (!order[(index + order.length) % order.length].isEnabled()
						|| !order[(index + order.length) % order.length]
								.isVisible()) {
					x = index;
					index = -1;
					for (; x >= 0; x--) {
						if (order[x].isEnabled() && order[x].isVisible()) {
							index = x;
							break;
						}
					}
					// wenn sich nix getan hat
					if (index == -1) {
						x = list.indexOf(aComponent);
						for (int y = order.length - 1; y >= x; y--) {
							if (order[y].isEnabled() && order[x].isVisible()) {
								index = y;
								break;
							}
						}
					}

				}
				return order[(index + order.length) % order.length];
			}

			public java.awt.Component getDefaultComponent(
					java.awt.Container focusCycleRoot) {
				return order[0];
			}

			public java.awt.Component getInitialComponent(java.awt.Window window) {
				return order[0];
			}
		};
		return policy;
	}
}
