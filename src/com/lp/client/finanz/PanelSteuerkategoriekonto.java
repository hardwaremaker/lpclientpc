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
package com.lp.client.finanz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

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
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.finanz.service.SteuerkategoriekontoDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")

public class PanelSteuerkategoriekonto extends PanelBasis implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;

	private String NEW = "NEW";
	private String DELETE = "DELETE";

	private SteuerkategoriekontoDto[] steuerkategoriekontoDtos = null;
//	private SteuerkategorieDto steuerkategorieDto = null;
	private Integer lengthButtons = null;
//	private Boolean firstTime = true;

	private final TabbedPaneFinanzamt tabbedPaneFinanzamt;

	private PanelQueryFLR panelQueryFLRKontoEk = null;
	private PanelQueryFLR panelQueryFLRKontoVk = null;
	private PanelQueryFLR panelQueryFLRKontoSkEk = null;
	private PanelQueryFLR panelQueryFLRKontoSkVk = null;
	private PanelQueryFLR panelQueryFLRkontoEFUst = null;

	static final public String ACTION_SPECIAL_KONTO_VK = "action_special_konto_vk";
	static final public String ACTION_SPECIAL_KONTO_EK = "action_special_konto_ek";
	static final public String ACTION_SPECIAL_KONTO_SKVK = "action_special_konto_skvk";
	static final public String ACTION_SPECIAL_KONTO_SKEK = "action_special_konto_skek";
	static final public String ACTION_SPECIAL_KONTO_EFUST = "action_special_konto_efust";

	private JButton wbuTagZurueck = new JButton();
	private JButton wbuNaechsterTag = new JButton();
	private WrapperLabel wlaGueltigZum = null;
	private WrapperDateField wdfGueltigZum = new WrapperDateField();
	private Timestamp datGueltigkeitZum = null;

	private Integer currentPosition = 0;

	// Formatierung des Panels
	private WrapperLabel[] wlaFormat = null;

	private WrapperLabel[] wlaKategoriename = null;
	private String[] kategorienameText = null;

	private WrapperDateField[] wdfGueltigAb = null;

	private WrapperButton[] wbuKontonummerVK = null;
	private WrapperTextField[] wtfKontonummerVK = null;
	private String[] kontonummerVKText = null;
	private String[] kontonummerVKTooltip = null;
	private boolean[] kontonummerVKistSteuerkonto = null;

	private WrapperButton[] wbuKontonummerEK = null;
	private WrapperTextField[] wtfKontonummerEK = null;
	private String[] kontonummerEKText = null;
	private String[] kontonummerEKTooltip = null;
	private boolean[] kontonummerEKistSteuerkonto = null;

	private WrapperButton[] wbuKontonummerSKVK = null;
	private WrapperTextField[] wtfKontonummerSKVK = null;
	private String[] kontonummerSKVKText = null;
	private String[] kontonummerSKVKTooltip = null;

	private WrapperButton[] wbuKontonummerSKEK = null;
	private WrapperTextField[] wtfKontonummerSKEK = null;
	private String[] kontonummerSKEKText = null;
	private String[] kontonummerSKEKTooltip = null;

	private WrapperButton[] wbuKontonummerEFUST = null;
	private WrapperTextField[] wtfKontonummerEFUST = null;
	private String[] kontonummerEFUSTText = null;
	private String[] kontonummerEFUSTTooltip = null;
	private boolean[] kontonummerEFUSTistSteuerkonto = null;

	private Timestamp[] kontonummerGueltigab = null;

	private JButton[] jbuNeu = null;

	private PanelSteuerkategorie panelSteuerkategorie = null;

	public PanelSteuerkategoriekonto(InternalFrame internalFrame, String add2TitleI, Object key,
			TabbedPaneFinanzamt tabbedPaneFinanzamt, PanelSteuerkategorie panelSteuerkategorie) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneFinanzamt = tabbedPaneFinanzamt;
		this.panelSteuerkategorie = panelSteuerkategorie;

		datGueltigkeitZum = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));

		initKontolisten();
		jbInit();
		initComponents();
	}

	protected SteuerkategorieDto getSteuerkategorieDto() throws ExceptionLP, Throwable {

		if (panelSteuerkategorie != null && panelSteuerkategorie.getKeyWhenDetailPanel() != null) {

			Integer steuerkategorieIId = (Integer) panelSteuerkategorie.getKeyWhenDetailPanel();
			return DelegateFactory.getInstance().getFinanzServiceDelegate()
					.steuerkategorieFindByPrimaryKey(steuerkategorieIId);
		}
		return null;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		try {
			if (evt.getSource() == wdfGueltigZum.getDisplay() && evt.getPropertyName().equals("date")) {

				// SP5423
				if (wdfGueltigZum.getDate() == null) {
					wdfGueltigZum.setDate(Helper.cutDate(new Date(System.currentTimeMillis())));
				}

				datGueltigkeitZum = wdfGueltigZum.getTimestamp();

				refreshPanel();
			}
		} catch (Throwable t) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("vkpf.error.preisgueltigkeitsanzeigeab"));
		}
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// ohne InnerBorder, den Platz brauche ich

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = new String[] {};

		// zusaetzliche buttons
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_VERKAUF)) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
					PanelBasis.ACTION_DISCARD };
		}

		enableToolsPanelButtons(aWhichButtonIUse);

		if (jPanelWorkingOn != null) {
			remove(jPanelWorkingOn);
		}

		wbuTagZurueck.setText("<");
		if (wbuTagZurueck.getListeners(ActionListener.class).length == 0) {
			wbuTagZurueck.addActionListener(new PanelSteuerkategoriekonto_wbuTagZurueck_actionAdapter(this));
		}
		wbuTagZurueck.setMaximumSize(new Dimension(25, 21));
		wbuTagZurueck.setMinimumSize(new Dimension(25, 21));
		wbuTagZurueck.setPreferredSize(new Dimension(25, 21));

		wbuNaechsterTag.setText(">");
		if (wbuNaechsterTag.getListeners(ActionListener.class).length == 0) {

			wbuNaechsterTag.addActionListener(new PanelSteuerkategoriekonto_wbuNaechsterTag_actionAdapter(this));
		}
		wbuNaechsterTag.setMaximumSize(new Dimension(25, 21));
		wbuNaechsterTag.setMinimumSize(new Dimension(25, 21));
		wbuNaechsterTag.setPreferredSize(new Dimension(25, 21));

		wlaGueltigZum = new WrapperLabel(LPMain.getTextRespectUISPr("fb.steuerkategoriekonto.gueltigzum"));
		wdfGueltigZum.setMandatoryField(true);

		wdfGueltigZum.setTimestamp(Helper.cut());

		wdfGueltigZum.getDisplay().addPropertyChangeListener(this);

		wdfGueltigZum.setActivatable(false);

		// Workingpanel
		zeichnePanel();
	}

	private void zeichnePanel() throws ExceptionLP, Throwable {

		if (jPanelWorkingOn != null) {
			remove(jPanelWorkingOn);
		}

		jPanelWorkingOn = new JPanel(
				new MigLayout("wrap 13", "[fill,16%,grow 200]" + "[fill,12%][fill,9%][fill,7%]1%[fill,9%][fill,7%]1%"
						+ "[fill,9%][fill,7%]1%[fill,9%][fill,7%]1%[fill,9%][fill,7%][fill,0%,grow 10]"));

		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		JPanel gueltigkeitsanzeigePanel = new JPanel(new GridBagLayout());
		gueltigkeitsanzeigePanel.add(wlaGueltigZum, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		gueltigkeitsanzeigePanel.add(wbuTagZurueck, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 20, 0));
		gueltigkeitsanzeigePanel.add(wdfGueltigZum, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		gueltigkeitsanzeigePanel.add(wbuNaechsterTag, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 20, 0));

		jPanelWorkingOn.add(gueltigkeitsanzeigePanel, "span");

		// diese Zeile formatiert das Panel

		wlaFormat = new WrapperLabel[7];
		for (int i = 0; i < wlaFormat.length; i++) {
			wlaFormat[i] = new WrapperLabel();
			wlaFormat[i].setHorizontalAlignment(SwingConstants.CENTER);
		}

		SteuerkategorieDto stk = getSteuerkategorieDto();
		ReversechargeartDto rcartOhneDto = DelegateFactory.getInstance().getFinanzServiceDelegate()
				.reversechargeartFindOhne();
		boolean reverseCharge = stk == null ? false : !rcartOhneDto.getIId().equals(stk.getReversechargeartId());

		wlaFormat[0].setText(LPMain.getInstance().getTextRespectUISPr("proj.projekt.label.kategorie"));
		wlaFormat[1].setText(LPMain.getInstance().getTextRespectUISPr("fb.steuerkategoriekonto.gueltigab"));
		wlaFormat[2].setText(LPMain.getInstance().getTextRespectUISPr("fb.steuerkategoriekonto.label.ustvk"));
		wlaFormat[3].setText(LPMain.getInstance().getTextRespectUISPr("fb.steuerkategoriekonto.label.skontovk"));
		wlaFormat[4].setText(LPMain.getInstance().getTextRespectUISPr("fb.steuerkategoriekonto.label.vstek"));
		wlaFormat[5].setText(LPMain.getInstance().getTextRespectUISPr("fb.steuerkategoriekonto.label.skontoek"));
		wlaFormat[6].setText(LPMain.getInstance().getTextRespectUISPr(
				reverseCharge ? "fb.steuerkategoriekonto.label.gegenust" : "fb.steuerkategoriekonto.label.einfuhrust"));

		lengthButtons = steuerkategoriekontoDtos == null ? 1 : steuerkategoriekontoDtos.length;

		wlaKategoriename = new WrapperLabel[lengthButtons];
		wdfGueltigAb = new WrapperDateField[lengthButtons];
		wbuKontonummerVK = new WrapperButton[lengthButtons];
		wtfKontonummerVK = new WrapperTextField[lengthButtons];
		wbuKontonummerSKVK = new WrapperButton[lengthButtons];
		wtfKontonummerSKVK = new WrapperTextField[lengthButtons];
		wbuKontonummerEK = new WrapperButton[lengthButtons];
		wtfKontonummerEK = new WrapperTextField[lengthButtons];
		wbuKontonummerSKEK = new WrapperButton[lengthButtons];
		wtfKontonummerSKEK = new WrapperTextField[lengthButtons];
		wbuKontonummerEFUST = new WrapperButton[lengthButtons];
		wtfKontonummerEFUST = new WrapperTextField[lengthButtons];
		jbuNeu = new JButton[lengthButtons];

		getInternalFrame().addItemChangedListener(this);

		for (int i = 0; i < lengthButtons; i++) {
			wlaKategoriename[i] = new WrapperLabel(kategorienameText != null ? kategorienameText[i] : "");

			wbuKontonummerVK[i] = new WrapperButton();
			wbuKontonummerVK[i].setActionCommand(PanelSteuerkategoriekonto.ACTION_SPECIAL_KONTO_VK + i);
			wbuKontonummerVK[i].addActionListener(this);
//			getInternalFrame().addItemChangedListener(this);
			wbuKontonummerVK[i].setText(LPMain.getInstance().getTextRespectUISPr("lp.konto"));
			wbuKontonummerVK[i].setEnabled(false);

			wtfKontonummerVK[i] = new WrapperTextField();
			wtfKontonummerVK[i].setText(kontonummerVKText[i]);
			wtfKontonummerVK[i].setToolTipText(kontonummerVKTooltip[i]);
			wtfKontonummerVK[i].setActivatable(false);
			wtfKontonummerVK[i].setToken("ustvk");

			wdfGueltigAb[i] = new WrapperDateField();
			wdfGueltigAb[i].setActivatable(true);
			wdfGueltigAb[i].setEnabled(false);
			wdfGueltigAb[i].setMandatoryField(true);
			wdfGueltigAb[i].setTimestamp(kontonummerGueltigab[i]);

			wbuKontonummerEK[i] = new WrapperButton();
			wbuKontonummerEK[i].setActionCommand(PanelSteuerkategoriekonto.ACTION_SPECIAL_KONTO_EK + i);
			wbuKontonummerEK[i].addActionListener(this);
//			getInternalFrame().addItemChangedListener(this);
			wbuKontonummerEK[i].setText(LPMain.getInstance().getTextRespectUISPr("lp.konto"));
			wbuKontonummerEK[i].setEnabled(false);

			wtfKontonummerEK[i] = new WrapperTextField();
			wtfKontonummerEK[i].setText(kontonummerEKText[i]);
			wtfKontonummerEK[i].setToolTipText(kontonummerEKTooltip[i]);
			wtfKontonummerEK[i].setActivatable(false);
			wtfKontonummerEK[i].setToken("vstek");

			wbuKontonummerSKVK[i] = new WrapperButton();
			wbuKontonummerSKVK[i].setActionCommand(PanelSteuerkategoriekonto.ACTION_SPECIAL_KONTO_SKVK + i);
			wbuKontonummerSKVK[i].addActionListener(this);
//			getInternalFrame().addItemChangedListener(this);
			wbuKontonummerSKVK[i].setText(LPMain.getInstance().getTextRespectUISPr("lp.konto"));
			wbuKontonummerSKVK[i].setEnabled(false);

			wtfKontonummerSKVK[i] = new WrapperTextField();
			wtfKontonummerSKVK[i].setText(kontonummerSKVKText[i]);
			wtfKontonummerSKVK[i].setToolTipText(kontonummerSKVKTooltip[i]);
			wtfKontonummerSKVK[i].setActivatable(false);
			wtfKontonummerSKVK[i].setToken("skontovk");

			wbuKontonummerSKEK[i] = new WrapperButton();
			wbuKontonummerSKEK[i].setActionCommand(PanelSteuerkategoriekonto.ACTION_SPECIAL_KONTO_SKEK + i);
			wbuKontonummerSKEK[i].addActionListener(this);
//			getInternalFrame().addItemChangedListener(this);
			wbuKontonummerSKEK[i].setText(LPMain.getInstance().getTextRespectUISPr("lp.konto"));
			wbuKontonummerSKEK[i].setEnabled(false);

			wtfKontonummerSKEK[i] = new WrapperTextField();
			wtfKontonummerSKEK[i].setText(kontonummerSKEKText[i]);
			wtfKontonummerSKEK[i].setToolTipText(kontonummerSKEKTooltip[i]);
			wtfKontonummerSKEK[i].setActivatable(false);
			wtfKontonummerSKEK[i].setToken("skontoek");

			wbuKontonummerEFUST[i] = new WrapperButton();
			wbuKontonummerEFUST[i].setActionCommand(PanelSteuerkategoriekonto.ACTION_SPECIAL_KONTO_EFUST + i);
			wbuKontonummerEFUST[i].addActionListener(this);
//			getInternalFrame().addItemChangedListener(this);
			wbuKontonummerEFUST[i].setText(LPMain.getInstance().getTextRespectUISPr("lp.konto"));
			wbuKontonummerEFUST[i].setEnabled(false);

			wtfKontonummerEFUST[i] = new WrapperTextField();
			wtfKontonummerEFUST[i].setText(kontonummerEFUSTText[i]);
			wtfKontonummerEFUST[i].setToolTipText(kontonummerEFUSTTooltip[i]);
			wtfKontonummerEFUST[i].setActivatable(false);
			wtfKontonummerEFUST[i].setToken("einfuhrust");

			jbuNeu[i] = new JButton();
			jbuNeu[i].setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/delete2.png")));
			jbuNeu[i].setActionCommand(DELETE + i);
			jbuNeu[i].addActionListener(this);
			jbuNeu[i].setToolTipText(
					LPMain.getInstance().getTextRespectUISPr("fb.steuerkategoriekonto.steuerkategorierkonto.loeschen"));

			jbuNeu[i].setMinimumSize(new Dimension(18, Defaults.getInstance().getControlHeight()));
			jbuNeu[i].setPreferredSize(new Dimension(18, Defaults.getInstance().getControlHeight()));

			wdfGueltigAb[i] = new WrapperDateField();
			wdfGueltigAb[i].setActivatable(true);
			wdfGueltigAb[i].setEnabled(false);
			wdfGueltigAb[i].setMandatoryField(true);
			wdfGueltigAb[i].setTimestamp(kontonummerGueltigab[i]);

		}

		jPanelWorkingOn.add(wlaFormat[0]);
		jPanelWorkingOn.add(wlaFormat[1]);
		for (int i = 2; i < wlaFormat.length; i++) {
			jPanelWorkingOn.add(wlaFormat[i], "span 2");
		}

		jPanelWorkingOn.add(new JLabel(""), "wrap");

		iZeile++;

		for (int i = 0; i < lengthButtons; i++) {
			jPanelWorkingOn.add(wlaKategoriename[i]);

			jPanelWorkingOn.add(wdfGueltigAb[i]);

			jPanelWorkingOn.add(wbuKontonummerVK[i]);

			jPanelWorkingOn.add(wtfKontonummerVK[i]);

			jPanelWorkingOn.add(wbuKontonummerSKVK[i]);

			jPanelWorkingOn.add(wtfKontonummerSKVK[i]);

			jPanelWorkingOn.add(wbuKontonummerEK[i]);

			jPanelWorkingOn.add(wtfKontonummerEK[i]);

			jPanelWorkingOn.add(wbuKontonummerSKEK[i]);

			jPanelWorkingOn.add(wtfKontonummerSKEK[i]);
			jPanelWorkingOn.add(wbuKontonummerEFUST[i]);

			jPanelWorkingOn.add(wtfKontonummerEFUST[i]);
			jPanelWorkingOn.add(jbuNeu[i], "wrap");
		}
		updateBackgroundColors();
	}

	public void refreshPanel() throws Throwable {
		initKontolisten();
		zeichnePanel();
	}

	/**
	 * F&auml;rbt Kontofelder von Vst-, Ust- und Einfuhrsteuerkonto rot wenn die
	 * Kontoart nicht richtig hinterlegt ist.
	 */
	protected void updateBackgroundColors() {
		for (int i = 0; i < lengthButtons; i++) {
			wtfKontonummerVK[i].setBackground(kontonummerVKistSteuerkonto[i] || kontonummerVKText[i].isEmpty()
					? HelperClient.getNotEditableColor()
					: Color.red);
			wtfKontonummerEK[i].setBackground(kontonummerEKistSteuerkonto[i] || kontonummerEKText[i].isEmpty()
					? HelperClient.getNotEditableColor()
					: Color.red);
			wtfKontonummerEFUST[i].setBackground(kontonummerEFUSTistSteuerkonto[i] || kontonummerEFUSTText[i].isEmpty()
					? HelperClient.getNotEditableColor()
					: Color.red);
		}
	}

	private void isortDtos() {
		boolean fertig = false;
		while (fertig == false) {
			fertig = true;
			for (int i = 0; i < steuerkategoriekontoDtos.length; i++) {
				if (i != (steuerkategoriekontoDtos.length - 1)) {
					if (steuerkategoriekontoDtos[i].getMwstsatzbezIId() > steuerkategoriekontoDtos[i + 1]
							.getMwstsatzbezIId()) {
						SteuerkategoriekontoDto temp = steuerkategoriekontoDtos[i];
						steuerkategoriekontoDtos[i] = steuerkategoriekontoDtos[i + 1];
						steuerkategoriekontoDtos[i + 1] = temp;
						fertig = false;
					}
				}
			}
			if (fertig == true) {
				return;
			}
		}
	}

	private void setGueltigkeitZum(boolean enabled) {
		wbuNaechsterTag.setEnabled(enabled);
		wbuTagZurueck.setEnabled(enabled);
		wdfGueltigZum.setEnabled(enabled);
	}

	private void initKontolisten() throws Throwable {
		if (panelSteuerkategorie != null && panelSteuerkategorie.getKeyWhenDetailPanel() != null) {

			Integer steuerkategorieIId = (Integer) panelSteuerkategorie.getKeyWhenDetailPanel();
			steuerkategoriekontoDtos = DelegateFactory.finanzservice()
					.steuerkategoriekontoAllZuDatum(steuerkategorieIId, datGueltigkeitZum);
			if (steuerkategoriekontoDtos.length == 0)
				steuerkategoriekontoDtos = DelegateFactory.getInstance().getFinanzServiceDelegate()
						.createDefaultSteuerkategoriekonto(steuerkategorieIId);
			isortDtos();
			kategorienameText = new String[steuerkategoriekontoDtos.length];
			kontonummerVKText = new String[steuerkategoriekontoDtos.length];
			kontonummerVKTooltip = new String[steuerkategoriekontoDtos.length];
			kontonummerVKistSteuerkonto = new boolean[steuerkategoriekontoDtos.length];
			kontonummerEKText = new String[steuerkategoriekontoDtos.length];
			kontonummerEKTooltip = new String[steuerkategoriekontoDtos.length];
			kontonummerEKistSteuerkonto = new boolean[steuerkategoriekontoDtos.length];
			kontonummerSKVKText = new String[steuerkategoriekontoDtos.length];
			kontonummerSKVKTooltip = new String[steuerkategoriekontoDtos.length];
			kontonummerSKEKText = new String[steuerkategoriekontoDtos.length];
			kontonummerSKEKTooltip = new String[steuerkategoriekontoDtos.length];
			kontonummerEFUSTText = new String[steuerkategoriekontoDtos.length];
			kontonummerEFUSTTooltip = new String[steuerkategoriekontoDtos.length];
			kontonummerEFUSTistSteuerkonto = new boolean[steuerkategoriekontoDtos.length];

			kontonummerGueltigab = new Timestamp[steuerkategoriekontoDtos.length];

			for (int i = 0; i < steuerkategoriekontoDtos.length; i++) {
				String mwstsatzbez = "";
				try {
					mwstsatzbez = (DelegateFactory.getInstance().getMandantDelegate()
							.mwstsatzbezFindByPrimaryKey(steuerkategoriekontoDtos[i].getMwstsatzbezIId()))
									.getCBezeichnung();
				} catch (Exception e) {
					//
				}
				if (steuerkategoriekontoDtos[i].getKontoIIdEk() != null) {
					KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
							.kontoFindByPrimaryKey(steuerkategoriekontoDtos[i].getKontoIIdEk());
					kontonummerEKText[i] = kontoDto.getCNr();
					kontonummerEKTooltip[i] = kontoDto.getCBez();
					kontonummerEKistSteuerkonto[i] = kontoDto.getKontoartCNr().equals(FinanzServiceFac.KONTOART_VST);
				} else {
					kontonummerEKText[i] = "";
					kontonummerEKTooltip[i] = null;
				}

				if (steuerkategoriekontoDtos[i].getKontoIIdSkontoEk() != null) {
					KontoDtoSmall kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
							.kontoFindByPrimaryKeySmall(steuerkategoriekontoDtos[i].getKontoIIdSkontoEk());
					kontonummerSKEKText[i] = kontoDto.getCNr();
					kontonummerSKEKTooltip[i] = kontoDto.getCBez();
				} else {
					kontonummerSKEKText[i] = "";
					kontonummerSKEKTooltip[i] = null;
				}

				if (steuerkategoriekontoDtos[i].getKontoIIdVk() != null) {
					KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
							.kontoFindByPrimaryKey(steuerkategoriekontoDtos[i].getKontoIIdVk());
					kontonummerVKText[i] = kontoDto.getCNr();
					kontonummerVKTooltip[i] = kontoDto.getCBez();
					kontonummerVKistSteuerkonto[i] = kontoDto.getKontoartCNr().equals(FinanzServiceFac.KONTOART_UST);
				} else {
					kontonummerVKText[i] = "";
					kontonummerVKTooltip[i] = null;
				}

				if (steuerkategoriekontoDtos[i].getKontoIIdSkontoVk() != null) {
					KontoDtoSmall kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
							.kontoFindByPrimaryKeySmall(steuerkategoriekontoDtos[i].getKontoIIdSkontoVk());
					kontonummerSKVKText[i] = kontoDto.getCNr();
					kontonummerSKVKTooltip[i] = kontoDto.getCBez();
				} else {
					kontonummerSKVKText[i] = "";
					kontonummerSKVKTooltip[i] = null;
				}

				if (steuerkategoriekontoDtos[i].getKontoIIdEinfuhrUst() != null) {
					KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
							.kontoFindByPrimaryKey(steuerkategoriekontoDtos[i].getKontoIIdEinfuhrUst());
					kontonummerEFUSTText[i] = kontoDto.getCNr();
					kontonummerEFUSTTooltip[i] = kontoDto.getCBez();
					kontonummerEFUSTistSteuerkonto[i] = kontoDto.getKontoartCNr().equals(FinanzServiceFac.KONTOART_UST)
							|| kontoDto.getKontoartCNr().equals(FinanzServiceFac.KONTOART_VST);
				} else {
					kontonummerEFUSTText[i] = "";
					kontonummerEFUSTTooltip[i] = null;
				}

				kategorienameText[i] = mwstsatzbez;

				kontonummerGueltigab[i] = steuerkategoriekontoDtos[i].getTGueltigAb();

			}
		}
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

		setGueltigkeitZum(true);
	}

	@Override
	public void updateButtons(LockStateValue lockstateValueI) throws Throwable {
		super.updateButtons(lockstateValueI);
		updateBackgroundColors();
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			for (int i = 0; i < lengthButtons; i++) {

				steuerkategoriekontoDtos[i].setTGueltigAb(wdfGueltigAb[i].getTimestamp());

				if (steuerkategoriekontoDtos[i].getId() == null) {
					DelegateFactory.getInstance().getFinanzServiceDelegate()
							.createSteuerkategoriekonto(steuerkategoriekontoDtos[i]);
				} else {
					DelegateFactory.getInstance().getFinanzServiceDelegate()
							.updateSteuerkategoriekonto(steuerkategoriekontoDtos[i]);
				}

			}
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wlaFormat[0];
	}

	@Override
	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI) throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		updateBackgroundColors();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);

		initKontolisten();
		Integer steuerkategorieIId = (Integer) panelSteuerkategorie.getKeyWhenDetailPanel();
		if (steuerkategorieIId != null) {
			refreshPanel();

			String title = getTabbedPaneFinanzamt().getFinanzamtTitle() + " | "
					+ getFinanzController().getReversechargeartDto().getCNr() + " | Steuerkategorie "
					+ getFinanzController().getSteuerkategorieDto().getCBez();

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, title);

		}
		setGueltigkeitZum(true);
	}

	private TabbedPaneFinanzamt getTabbedPaneFinanzamt() {
		return tabbedPaneFinanzamt;
	}

	private IFinanzamtController getFinanzController() {
		return getTabbedPaneFinanzamt().getFinanzController();
	}

	public void eventActionLock(ActionEvent e) throws Throwable {
		super.eventActionLock(e);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		for (int i = 0; i < lengthButtons; i++) {
			if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO_VK + i)) {
				String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
				QueryType[] qt = null;
				FilterKriterium[] filters = FinanzFilterFactory.getInstance()
						.createFKUstKontenInMwstSatzBez(steuerkategoriekontoDtos[i].getMwstsatzbezIId());
				panelQueryFLRKontoVk = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_FINANZKONTEN,
						aWhichButtonIUse, getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("finanz.liste.sachkonten"));
				FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
				FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
				FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance().createFKVKonto();
				panelQueryFLRKontoVk.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1, fkDirekt2, fkVersteckt);
				if (steuerkategoriekontoDtos != null) {
					panelQueryFLRKontoVk.setSelectedId(steuerkategoriekontoDtos[i].getKontoIIdVk());
				}
				currentPosition = i;
				new DialogQuery(panelQueryFLRKontoVk);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO_SKVK + i)) {
				String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
				QueryType[] qt = null;
				FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKSachkonten();
				panelQueryFLRKontoSkVk = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_FINANZKONTEN,
						aWhichButtonIUse, getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("finanz.liste.sachkonten"));
				FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
				FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
				FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance().createFKVKonto();
				panelQueryFLRKontoSkVk.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1, fkDirekt2,
						fkVersteckt);
				if (steuerkategoriekontoDtos != null) {
					panelQueryFLRKontoSkVk.setSelectedId(steuerkategoriekontoDtos[i].getKontoIIdSkontoVk());
				}
				currentPosition = i;
				new DialogQuery(panelQueryFLRKontoSkVk);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO_EK + i)) {
				String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
				QueryType[] qt = null;
				FilterKriterium[] filters = FinanzFilterFactory.getInstance()
						.createFKVstKontenInMwstSatzBez(steuerkategoriekontoDtos[i].getMwstsatzbezIId());
				panelQueryFLRKontoEk = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_FINANZKONTEN,
						aWhichButtonIUse, getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("finanz.liste.sachkonten"));
				FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
				FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
				FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance().createFKVKonto();
				panelQueryFLRKontoEk.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1, fkDirekt2, fkVersteckt);
				if (steuerkategoriekontoDtos != null) {
					panelQueryFLRKontoEk.setSelectedId(steuerkategoriekontoDtos[i].getKontoIIdEk());
				}
				currentPosition = i;
				new DialogQuery(panelQueryFLRKontoEk);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO_SKEK + i)) {
				String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
				QueryType[] qt = null;
				FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKSachkonten();
				panelQueryFLRKontoSkEk = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_FINANZKONTEN,
						aWhichButtonIUse, getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("finanz.liste.sachkonten"));
				FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
				FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
				FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance().createFKVKonto();
				panelQueryFLRKontoSkEk.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1, fkDirekt2,
						fkVersteckt);
				if (steuerkategoriekontoDtos != null) {
					panelQueryFLRKontoSkEk.setSelectedId(steuerkategoriekontoDtos[i].getKontoIIdSkontoEk());
				}
				currentPosition = i;
				new DialogQuery(panelQueryFLRKontoSkEk);
			} else if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO_EFUST + i)) {
				String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
				QueryType[] qt = null;
				FilterKriterium[] filters = FinanzFilterFactory.getInstance()
						.createFKSteuerkontenInMwstSatzBez(steuerkategoriekontoDtos[i].getMwstsatzbezIId());
				panelQueryFLRkontoEFUst = new PanelQueryFLR(qt, filters, QueryParameters.UC_ID_FINANZKONTEN,
						aWhichButtonIUse, getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("finanz.liste.sachkonten"));
				FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
				FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
				FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance().createFKVKonto();
				panelQueryFLRkontoEFUst.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1, fkDirekt2,
						fkVersteckt);
				if (steuerkategoriekontoDtos != null) {
					panelQueryFLRkontoEFUst.setSelectedId(steuerkategoriekontoDtos[i].getKontoIIdEinfuhrUst());
				}
				currentPosition = i;
				new DialogQuery(panelQueryFLRkontoEFUst);
			} else if (e.getActionCommand().equals(NEW + i)) {
				steuerkategoriekontoDtos[i].setId(null);
				wdfGueltigAb[i].setTimestamp(null);
			} else if (e.getActionCommand().equals(DELETE + i)) {

				DelegateFactory.getInstance().getFinanzServiceDelegate()
						.removeSteuerkategoriekonto(steuerkategoriekontoDtos[i].getId().id());
				refreshPanel();
			}
		}
	}

	void wbuTagZurueck_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfGueltigZum.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		wdfGueltigZum.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	}

	void wbuNaechsterTag_actionPerformed(ActionEvent e) {
		Calendar c = Calendar.getInstance();
		c.setTime(wdfGueltigZum.getDate());
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		wdfGueltigZum.setTimestamp(new java.sql.Timestamp(c.getTimeInMillis()));
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKontoVk) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				if (iId != null) {
					steuerkategoriekontoDtos[currentPosition].setKontoIIdVk(iId);
					KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey(iId);
					kontonummerVKText[currentPosition] = kontoDto.getCNr();
					kontonummerVKTooltip[currentPosition] = kontoDto.getCBez();
					kontonummerVKistSteuerkonto[currentPosition] = kontoDto.getKontoartCNr()
							.equals(FinanzServiceFac.KONTOART_UST);
					wtfKontonummerVK[currentPosition].setText(kontoDto.getCNr());
					wtfKontonummerVK[currentPosition].setToolTipText(kontoDto.getCBez());
				}
			} else if (e.getSource() == panelQueryFLRKontoSkVk) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				if (iId != null) {
					steuerkategoriekontoDtos[currentPosition].setKontoIIdSkontoVk(iId);
					KontoDtoSmall kontoDtoSmall = DelegateFactory.getInstance().getFinanzDelegate()
							.kontoFindByPrimaryKeySmall(iId);
					kontonummerSKVKText[currentPosition] = new String(kontoDtoSmall.getCNr());
					kontonummerSKVKTooltip[currentPosition] = kontoDtoSmall.getCBez();
					wtfKontonummerSKVK[currentPosition].setText(kontoDtoSmall.getCNr());
					wtfKontonummerSKVK[currentPosition].setToolTipText(kontoDtoSmall.getCBez());
				}
			} else if (e.getSource() == panelQueryFLRKontoEk) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				if (iId != null) {
					steuerkategoriekontoDtos[currentPosition].setKontoIIdEk(iId);
					KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey(iId);
					kontonummerEKText[currentPosition] = new String(kontoDto.getCNr());
					kontonummerEKTooltip[currentPosition] = kontoDto.getCBez();
					kontonummerEKistSteuerkonto[currentPosition] = kontoDto.getKontoartCNr()
							.equals(FinanzServiceFac.KONTOART_VST);
					wtfKontonummerEK[currentPosition].setText(kontoDto.getCNr());
					wtfKontonummerEK[currentPosition].setToolTipText(kontoDto.getCBez());
				}
			} else if (e.getSource() == panelQueryFLRKontoSkEk) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				if (iId != null) {
					steuerkategoriekontoDtos[currentPosition].setKontoIIdSkontoEk(iId);
					KontoDtoSmall kontoDtoSmall = DelegateFactory.getInstance().getFinanzDelegate()
							.kontoFindByPrimaryKeySmall(iId);
					kontonummerSKEKText[currentPosition] = new String(kontoDtoSmall.getCNr());
					kontonummerSKEKTooltip[currentPosition] = kontoDtoSmall.getCBez();
					wtfKontonummerSKEK[currentPosition].setText(kontoDtoSmall.getCNr());
					wtfKontonummerSKEK[currentPosition].setToolTipText(kontoDtoSmall.getCBez());
				}
			} else if (e.getSource() == panelQueryFLRkontoEFUst) {
				Integer iId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				if (iId != null) {
					steuerkategoriekontoDtos[currentPosition].setKontoIIdEinfuhrUst(iId);
					KontoDto kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey(iId);
					kontonummerEFUSTText[currentPosition] = new String(kontoDto.getCNr());
					kontonummerEFUSTTooltip[currentPosition] = kontoDto.getCBez();
					kontonummerEFUSTistSteuerkonto[currentPosition] = kontoDto.getKontoartCNr()
							.equals(FinanzServiceFac.KONTOART_UST)
							|| kontoDto.getKontoartCNr().equals(FinanzServiceFac.KONTOART_VST);
					wtfKontonummerEFUST[currentPosition].setText(kontoDto.getCNr());
					wtfKontonummerEFUST[currentPosition].setToolTipText(kontoDto.getCBez());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRKontoVk) {
				kontonummerVKText[currentPosition] = "";
				kontonummerVKTooltip[currentPosition] = "";
				wtfKontonummerVK[currentPosition].setText(null);
				wtfKontonummerVK[currentPosition].setToolTipText(null);
				steuerkategoriekontoDtos[currentPosition].setKontoIIdVk(null);
			} else if (e.getSource() == panelQueryFLRKontoSkVk) {
				kontonummerSKVKText[currentPosition] = new String("");
				wtfKontonummerSKVK[currentPosition].setText(null);
				steuerkategoriekontoDtos[currentPosition].setKontoIIdSkontoVk(null);
			} else if (e.getSource() == panelQueryFLRKontoEk) {
				kontonummerEKText[currentPosition] = new String("");
				wtfKontonummerEK[currentPosition].setText(null);
				steuerkategoriekontoDtos[currentPosition].setKontoIIdEk(null);
			} else if (e.getSource() == panelQueryFLRKontoSkEk) {
				kontonummerSKEKText[currentPosition] = new String("");
				wtfKontonummerSKEK[currentPosition].setText(null);
				steuerkategoriekontoDtos[currentPosition].setKontoIIdSkontoEk(null);
			} else if (e.getSource() == panelQueryFLRkontoEFUst) {
				kontonummerEFUSTText[currentPosition] = new String("");
				wtfKontonummerEFUST[currentPosition].setText(null);
				steuerkategoriekontoDtos[currentPosition].setKontoIIdEinfuhrUst(null);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
//			System.out.println("TEST\n\n");
		}
		updateBackgroundColors();
	}

	/**
	 * Es wird immer die gesamte Steuerkategorie gelockt.
	 * 
	 * @throws Exception
	 * @return LockMeDto
	 */
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_STEUERKATEGORIE;
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {

		eventActionLock(null);
		// Lockstate setzen und Buttons schalten.
		LockStateValue lockstateValue = getLockedstateDetailMainKey();
		lockstateValue.setIState(LOCK_IS_LOCKED_BY_ME);
		updateButtons(lockstateValue);
		setFirstFocusableComponentSaveOld();

		for (int i = 0; i < lengthButtons; i++) {
			wtfKontonummerEK[i].setEditable(false);
			wtfKontonummerVK[i].setEditable(false);
			wtfKontonummerSKEK[i].setEditable(false);
			wtfKontonummerSKVK[i].setEditable(false);
			wtfKontonummerEFUST[i].setEditable(false);

			jbuNeu[i].setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/document.png")));
			jbuNeu[i].setToolTipText(
					LPMain.getInstance().getTextRespectUISPr("fb.steuerkategoriekonto.steuerkategorierkonto.neu"));

			jbuNeu[i].setActionCommand(NEW + i);

		}
		updateBackgroundColors();
		setGueltigkeitZum(false);
	}

	public void setPanelsteuerkategorie(PanelSteuerkategorie panelsteuerkategorie) {
		this.panelSteuerkategorie = panelsteuerkategorie;
	}

	public PanelBasis getPanelsteuerkategorie() {
		return panelSteuerkategorie;
	}

}

class PanelSteuerkategoriekonto_wbuNaechsterTag_actionAdapter implements ActionListener {
	private PanelSteuerkategoriekonto adaptee;

	PanelSteuerkategoriekonto_wbuNaechsterTag_actionAdapter(PanelSteuerkategoriekonto adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuNaechsterTag_actionPerformed(e);
	}
}

class PanelSteuerkategoriekonto_wbuTagZurueck_actionAdapter implements ActionListener {
	private PanelSteuerkategoriekonto adaptee;

	PanelSteuerkategoriekonto_wbuTagZurueck_actionAdapter(PanelSteuerkategoriekonto adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuTagZurueck_actionPerformed(e);
	}
}
