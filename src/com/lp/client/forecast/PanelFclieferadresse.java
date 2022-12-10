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
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.open.DirectoryFile;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.ForecastImportCallOff;
import com.lp.server.system.service.ForecastImportLinienabruf;
import com.lp.server.system.service.IForecastImportFile;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;

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
public class PanelFclieferadresse extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneForecast tabbedPaneForecast = null;

	private Border border = null;
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private JPanel jpaWorkingOn = new JPanel();

	FclieferadresseDto fclieferadresseDto = null;

	private PanelQueryFLR panelQueryFLRKundeLieferadresse = null;

	private final static String ACTION_SPECIAL_PFAD = "ACTION_SPECIAL_PFAD";

	private WrapperLabel wlaPfad = new WrapperLabel();
	private WrapperTextField wtfPfad = new WrapperTextField(300);
	private WrapperButton wbuPfad = new WrapperButton(
			new ImageIcon(getClass().getResource("/com/lp/client/res/folder.png")));
	private WrapperComboBox wcoImportdefPfad = new WrapperComboBox();

	private WrapperCheckBox wcoZusammenziehen = new WrapperCheckBox();

	private WrapperCheckBox wcoLiefermengenBeruecksichtigen = new WrapperCheckBox();

	private WrapperGotoButton wbuKundeLieferadresse = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_KUNDE_AUSWAHL);
	private WrapperTextField wtfKundeLieferadresse = new WrapperTextField();
	private final static String ACTION_SPECIAL_KUNDELIEFERADRESSE_FROM_LISTE = "action_kundelieferadresse_from_liste";
	private final static String ACTION_SPECIAL_IMPORT = LEAVEALONE + "action_special_import_forecastfiles";
	private WrapperButton wbuImport = new WrapperButton(IconFactory.getImport());

	private WrapperCheckBox wcoKommissionieren = new WrapperCheckBox();

	private WrapperLabel wlaRundungPositionen = new WrapperLabel();
	private WrapperLabel wlaRundungLinienabruf = new WrapperLabel();

	private ButtonGroup bgPositionen = new ButtonGroup();
	private ButtonGroup bgRundung = new ButtonGroup();

	WrapperRadioButton wrbRundungPositionenKeine = new WrapperRadioButton();
	WrapperRadioButton wrbRundungPositionenVerpackungsmenge = new WrapperRadioButton();
	WrapperRadioButton wrbRundungPositionenVerpackungsmittelmenge = new WrapperRadioButton();

	WrapperRadioButton wrbRundungLinienabrufKeine = new WrapperRadioButton();
	WrapperRadioButton wrbRundungLinienabrufVerpackungsmenge = new WrapperRadioButton();
	WrapperRadioButton wrbRundungLinienabrufVerpackungsmittelmenge = new WrapperRadioButton();

	private WrapperLabel wlaKommdrucker = new WrapperLabel();
	private WrapperComboBox wcoKommdrucker = new WrapperComboBox();

	public PanelFclieferadresse(InternalFrame internalFrame, String add2TitleI, Object key,
			TabbedPaneForecast tabbedPaneForecast) throws Throwable {
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

		wbuKundeLieferadresse.setText(LPMain.getTextRespectUISPr("button.lieferadresse"));

		wcoZusammenziehen.setText(LPMain.getTextRespectUISPr("fc.lieferadressen.zusammenziehen"));

		wcoLiefermengenBeruecksichtigen
				.setText(LPMain.getTextRespectUISPr("fc.lieferadressen.liefermengenberuecksichtigen"));

		wcoKommissionieren.setText(LPMain.getTextRespectUISPr("artikel.kommissionieren"));

		wlaPfad.setText(LPMain.getTextRespectUISPr("fc.lieferadresse.importpfad"));

		wlaRundungPositionen.setText(LPMain.getTextRespectUISPr("fc.lieferadresse.rundung.positionen"));
		wlaRundungLinienabruf.setText(LPMain.getTextRespectUISPr("fc.lieferadresse.rundung.linienabrufe"));

		wrbRundungPositionenKeine.setText(LPMain.getTextRespectUISPr("fc.lieferadresse.rundung.keinerundung"));
		wrbRundungLinienabrufKeine.setText(LPMain.getTextRespectUISPr("fc.lieferadresse.rundung.keinerundung"));

		wrbRundungPositionenVerpackungsmittelmenge
				.setText(LPMain.getTextRespectUISPr("artikel.verpackungsmittelmenge"));
		wrbRundungLinienabrufVerpackungsmittelmenge
				.setText(LPMain.getTextRespectUISPr("artikel.verpackungsmittelmenge"));

		wrbRundungPositionenVerpackungsmenge.setText(LPMain.getTextRespectUISPr("artikel.sonstiges.verpackungsmenge"));
		wrbRundungLinienabrufVerpackungsmenge.setText(LPMain.getTextRespectUISPr("artikel.sonstiges.verpackungsmenge"));

		wlaKommdrucker.setText(LPMain.getTextRespectUISPr("fc.kommdrucker"));

		wcoImportdefPfad.setMap(DelegateFactory.getInstance().getForecastDelegate().getAllImportdef());
		wcoKommdrucker.setMap(DelegateFactory.getInstance().getForecastDelegate().getAllKommdrucker());

		wtfKundeLieferadresse.setMandatoryField(true);
		wtfKundeLieferadresse.setActivatable(false);
		wtfKundeLieferadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuKundeLieferadresse.setActionCommand(ACTION_SPECIAL_KUNDELIEFERADRESSE_FROM_LISTE);
		wbuKundeLieferadresse.addActionListener(this);

		wbuPfad.setActionCommand(ACTION_SPECIAL_PFAD);
		wbuPfad.addActionListener(this);

		wbuImport.setActionCommand(ACTION_SPECIAL_IMPORT);
		wbuImport.addActionListener(this);

		wlaRundungPositionen.setHorizontalAlignment(SwingConstants.LEFT);
		wlaRundungLinienabruf.setHorizontalAlignment(SwingConstants.LEFT);

		bgPositionen.add(wrbRundungPositionenKeine);
		bgPositionen.add(wrbRundungPositionenVerpackungsmenge);
		bgPositionen.add(wrbRundungPositionenVerpackungsmittelmenge);
		wrbRundungPositionenKeine.setSelected(true);

		bgRundung.add(wrbRundungLinienabrufKeine);
		bgRundung.add(wrbRundungLinienabrufVerpackungsmenge);
		bgRundung.add(wrbRundungLinienabrufVerpackungsmittelmenge);
		wrbRundungLinienabrufKeine.setSelected(true);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuKundeLieferadresse, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));

		jpaWorkingOn.add(wtfKundeLieferadresse, new GridBagConstraints(1, iZeile, 4, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaRundungPositionen, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaRundungLinienabruf, new GridBagConstraints(3, iZeile, 2, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcoKommissionieren, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200, 0));
		jpaWorkingOn.add(wrbRundungPositionenKeine, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbRundungLinienabrufKeine, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcoZusammenziehen, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbRundungPositionenVerpackungsmittelmenge, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbRundungLinienabrufVerpackungsmittelmenge, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		
		jpaWorkingOn.add(wcoLiefermengenBeruecksichtigen, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wrbRundungPositionenVerpackungsmenge, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbRundungLinienabrufVerpackungsmenge, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaPfad, new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfPfad, new GridBagConstraints(1, iZeile, 2, 1, 0.8, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuPfad, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wcoImportdefPfad, new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;

		jpaWorkingOn.add(wlaKommdrucker, new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcoKommdrucker, new GridBagConstraints(1, iZeile, 2, 1, 0.8, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);
		getToolBar().addButtonLeft("/com/lp/client/res/import1.png", LPMain.getTextRespectUISPr("fc.forecast.import"),
				ACTION_SPECIAL_IMPORT, null, RechteFac.RECHT_FC_FORECAST_CUD);

		JPanel jpaOffset = new JPanel();
		jpaOffset.setLayout(new GridBagLayout());

	}

	public String getLockMeWer() {
		return HelperClient.LOCKME_FORECAST;
	}

	public void eventActionNew(EventObject e, boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {
		// locknew: 1 hier muss dafuer gesorgt werden, dass der Framework lockt

		super.eventActionNew(e, true, false);
		clearStatusbar();
		fclieferadresseDto = new FclieferadresseDto();

		leereAlleFelder(this);

	}

	/**
	 * Loeschen einer Rechnungsposition
	 * 
	 * @param e                     ActionEvent
	 * @param bAdministrateLockKeyI boolean
	 * @param bNeedNoDeleteI        boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!isLockedDlg()) {
			DelegateFactory.getInstance().getForecastDelegate().removeFclieferadresse(fclieferadresseDto);
			this.fclieferadresseDto = null;
			// selectafterdelete: key null setzen
			this.setKeyWhenDetailPanel(null);
			this.leereAlleFelder(this);
			super.eventActionDelete(e, false, false);
		}

	}

	private void importpfadDeaktivieren() throws Throwable {

		if (getTabbedPane().getPanelQueryForecast().getSelectedId() != null) {
			ForecastDto fcDto = DelegateFactory.getInstance().getForecastDelegate()
					.forecastFindByPrimaryKey((Integer) getTabbedPane().getPanelQueryForecast().getSelectedId());

			if (fcDto.getCPfadForecastauftrag() != null) {
				wtfPfad.setEditable(false);
				wcoImportdefPfad.setEnabled(false);
				wbuPfad.setEnabled(false);
			}

		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, false);
		importpfadDeaktivieren();

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (fclieferadresseDto.getiId() == null) {

				fclieferadresseDto.setIId(
						DelegateFactory.getInstance().getForecastDelegate().createFclieferadresse(fclieferadresseDto));

				setKeyWhenDetailPanel(fclieferadresseDto.getiId());
			} else {
				DelegateFactory.getInstance().getForecastDelegate().updateFclieferadresse(fclieferadresseDto);

			}

			super.eventActionSave(e, true);

			eventYouAreSelected(false);
		}
	}

	private void components2Dto() throws Throwable {
		fclieferadresseDto.setForecastIId((Integer) getTabbedPane().getPanelQueryForecast().getSelectedId());
		fclieferadresseDto.setCPfadImport(wtfPfad.getText());
		fclieferadresseDto.setImportdefCNrPfad((String) wcoImportdefPfad.getKeyOfSelectedItem());
		fclieferadresseDto.setKommdruckerIId((Integer) wcoKommdrucker.getKeyOfSelectedItem());
		fclieferadresseDto.setBZusammenziehen(wcoZusammenziehen.getShort());
		fclieferadresseDto.setBKommissionieren(wcoKommissionieren.getShort());
		fclieferadresseDto.setBLiefermengenberuecksichtigen(wcoLiefermengenBeruecksichtigen.getShort());

		int iTypRundungLinienabruf = ForecastFac.FORECAST_TYP_RUNDUNG_KEINE;

		if (wrbRundungLinienabrufVerpackungsmenge.isSelected()) {
			iTypRundungLinienabruf = ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMENGE;
		} else if (wrbRundungLinienabrufVerpackungsmittelmenge.isSelected()) {
			iTypRundungLinienabruf = ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMITTELMENGE;
		}
		fclieferadresseDto.setITypRundungLinienabruf(iTypRundungLinienabruf);

		int iTypRundungPosition = ForecastFac.FORECAST_TYP_RUNDUNG_KEINE;

		if (wrbRundungPositionenVerpackungsmenge.isSelected()) {
			iTypRundungPosition = ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMENGE;
		} else if (wrbRundungPositionenVerpackungsmittelmenge.isSelected()) {
			iTypRundungPosition = ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMITTELMENGE;
		}
		fclieferadresseDto.setITypRundungPosition(iTypRundungPosition);

	}

	private void dto2Components() throws Throwable {
		KundeDto kundeLieferadresseDto = DelegateFactory.getInstance().getKundeDelegate()
				.kundeFindByPrimaryKey(fclieferadresseDto.getKundeIIdLieferadresse());

		wtfKundeLieferadresse.setText(tabbedPaneForecast.formatKunde(kundeLieferadresseDto));

		wbuKundeLieferadresse.setOKey(fclieferadresseDto.getKundeIIdLieferadresse());

		wcoImportdefPfad.setKeyOfSelectedItem(fclieferadresseDto.getImportdefCNrPfad());
		wcoKommdrucker.setKeyOfSelectedItem(fclieferadresseDto.getKommdruckerIId());

		wcoKommissionieren.setShort(fclieferadresseDto.getBKommissionieren());

		wcoZusammenziehen.setShort(fclieferadresseDto.getBZusammenziehen());

		wcoLiefermengenBeruecksichtigen.setShort(fclieferadresseDto.getBLiefermengenberuecksichtigen());

		wtfPfad.setText(fclieferadresseDto.getCPfadImport());
		setHmButtonEnabled(ACTION_SPECIAL_IMPORT, wtfPfad.getText() != null);

		if (fclieferadresseDto.getITypRundungLinienabruf().intValue() == ForecastFac.FORECAST_TYP_RUNDUNG_KEINE) {
			wrbRundungLinienabrufKeine.setSelected(true);
		} else if (fclieferadresseDto.getITypRundungLinienabruf()
				.intValue() == ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMENGE) {
			wrbRundungLinienabrufVerpackungsmenge.setSelected(true);
		} else if (fclieferadresseDto.getITypRundungLinienabruf()
				.intValue() == ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMITTELMENGE) {
			wrbRundungLinienabrufVerpackungsmittelmenge.setSelected(true);
		}

		if (fclieferadresseDto.getITypRundungPosition().intValue() == ForecastFac.FORECAST_TYP_RUNDUNG_KEINE) {
			wrbRundungPositionenKeine.setSelected(true);
		} else if (fclieferadresseDto.getITypRundungPosition()
				.intValue() == ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMENGE) {
			wrbRundungPositionenVerpackungsmenge.setSelected(true);
		} else if (fclieferadresseDto.getITypRundungPosition()
				.intValue() == ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMITTELMENGE) {
			wrbRundungPositionenVerpackungsmittelmenge.setSelected(true);
		}

	}

	void dialogQueryKundeLieferadresseFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRKundeLieferadresse = PartnerFilterFactory.getInstance().createPanelFLRKunde(getInternalFrame(),
				true, false, fclieferadresseDto.getKundeIIdLieferadresse());
		new DialogQuery(panelQueryFLRKundeLieferadresse);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		String cmd = e.getActionCommand();
		if (ACTION_SPECIAL_KUNDELIEFERADRESSE_FROM_LISTE.equals(cmd)) {
			dialogQueryKundeLieferadresseFromListe(e);
		}

		if (ACTION_SPECIAL_PFAD.equals(cmd)) {
			String baseDirectory = wtfPfad.getText() != null ? wtfPfad.getText()
					: LPMain.getInstance().getLastImportDirectory();
			HvOptional<DirectoryFile> f = HelperClient.chooseDirectoryNew(this, baseDirectory);
			if(f.isPresent()) {
				wtfPfad.setText(f.get().getDirectory().getAbsolutePath());				
			}
/*			
			File f = HelperClient.chooseDirectory(this, baseDirectory);
			if (f != null) {
				wtfPfad.setText(f.getAbsolutePath());
			}
*/			
		}

		// if (ACTION_SPECIAL_PFAD.equals(cmd)) {
		// File[] files = HelperClient.chooseFile(this,
		// HelperClient.FILE_FILTER_EDI, false);
		// if (files != null && files.length > 0) {
		// wtfPfad.setText(files[0].getAbsolutePath());
		// }
		// }

		if (ACTION_SPECIAL_IMPORT.equals(cmd)) {
			importForecastFiles();
		}
	}

	private void importForecastFiles() throws IOException {
		File f = new File(wtfPfad.getText());
		File[] localFiles = f.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				String s = pathname.getName().toLowerCase();
				if (s.endsWith(".txt"))
					return true;
				if (s.endsWith(".xls"))
					return true;
				return false;
			}
		});
		myLogger.info("Found '" + (localFiles == null ? -1 : localFiles.length) + "' files to import forecastcalloff.");

		if (localFiles == null || localFiles.length == 0) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), LPMain.getMessageTextRespectUISPr(
					"fc.forecast.import.keinedateien", f.getAbsolutePath(), "*.txt, *.xls"));
			return;
		}

		List<IForecastImportFile<?>> importFiles = new ArrayList<IForecastImportFile<?>>();
		for (File localFile : localFiles) {
			if (localFile.getName().toLowerCase().endsWith(".txt")) {
				importFiles.add(createLinienabrufImport(localFile));
				continue;
			}

			if (localFile.getName().toLowerCase().endsWith(".xls")) {
				importFiles.add(createCallOffImport(localFile));
				continue;
			}
		}

		DialogCallOffImportController importController = new DialogCallOffImportController(fclieferadresseDto.getiId(),
				importFiles, localFiles);
		DialogCallOffImport dialog = new DialogCallOffImport(LPMain.getInstance().getDesktop(),
				LPMain.getTextRespectUISPr("fc.forecast.calloffimport.title"), true, importController);
		dialog.setVisible(true);
	}

	private IForecastImportFile<?> createLinienabrufImport(File f) throws FileNotFoundException, IOException {
		List<String> content = readAsciiFile(f);
		return new ForecastImportLinienabruf(f.getName(), content, new java.sql.Date(System.currentTimeMillis()));
	}

	private IForecastImportFile<?> createCallOffImport(File f) throws FileNotFoundException, IOException {
		byte[] content = readBinaryFile(f);
		return new ForecastImportCallOff(f.getName(), content);
	}

	private List<String> readAsciiFile(File file) throws FileNotFoundException, IOException {
		BufferedReader reader = null;

		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			List<String> inputs = new ArrayList<String>();

			String line = reader.readLine();
			while (line != null) {
				inputs.add(line);
				line = reader.readLine();
			}

			return inputs;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	private byte[] readBinaryFile(File file) throws IOException {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException ex) {
			}

			try {
				if (ios != null)
					ios.close();
			} catch (IOException ex) {
			}
		}

		return ous.toByteArray();
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKundeLieferadresse) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey((Integer) key);

				wtfKundeLieferadresse.setText(tabbedPaneForecast.formatKunde(kundeDto));
				fclieferadresseDto.setKundeIIdLieferadresse(kundeDto.getIId());

			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// einen neuen Eintrag anlegen oder die letzte Position wurde
			// geloescht.

			leereAlleFelder(this);

			clearStatusbar();
			
			if(key != null && key.equals(LPMain.getLockMeForNew())) {
				wcoLiefermengenBeruecksichtigen.setSelected(true);
			}
		} else {
			// einen alten Eintrag laden.
			fclieferadresseDto = DelegateFactory.getInstance().getForecastDelegate()
					.fclieferadresseFindByPrimaryKey((Integer) key);
			dto2Components();

		}
		importpfadDeaktivieren();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuKundeLieferadresse;
	}
}
