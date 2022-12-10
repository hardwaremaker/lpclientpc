package com.lp.client.rechnung;

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
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.InternalFramePersonal;
import com.lp.server.personal.service.ZeitmodelltagDto;
import com.lp.server.rechnung.service.VerrechnungsmodelltagDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelVerrechnungsmodelltag extends PanelBasis {

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
	private InternalFrameRechnung internalFrameRechnung = null;

	private VerrechnungsmodelltagDto verrechnungsmodelltagDto = null;

	private WrapperLabel wlaTagesart = new WrapperLabel();
	private WrapperComboBox wcoTagesart = new WrapperComboBox();
	private WrapperTimeField wtfDauerAb = new WrapperTimeField();
	private WrapperTimeField wtfZeitraumVon = new WrapperTimeField();

	private WrapperLabel wlaZeitraumBis = new WrapperLabel();
	private WrapperTimeField wtfZeitraumBis = new WrapperTimeField();

	private WrapperRadioButton wrbDauer = new WrapperRadioButton();
	private WrapperRadioButton wrbZeitraum = new WrapperRadioButton();
	private ButtonGroup bg = new ButtonGroup();

	private WrapperIdentField wifGebucht = new WrapperIdentField(getInternalFrame(), this, false, true);
	private WrapperIdentField wifZuVerrechnen = new WrapperIdentField(getInternalFrame(), this, false, true);

	private WrapperCheckBox wcbEndeDesTages = new WrapperCheckBox();

	private JLabel wlaVKPreisGebucht = new JLabel();
	private JLabel wlaVKPreisZuVerrechnen = new JLabel();

	private JLabel wlaFaktor = new JLabel();

	public PanelVerrechnungsmodelltag(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameRechnung = (InternalFrameRechnung) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() throws Throwable {
		wcoTagesart.setMap(DelegateFactory.getInstance().getZeiterfassungDelegate().getAllSprTagesarten());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoTagesart;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		verrechnungsmodelltagDto = new VerrechnungsmodelltagDto();

		leereAlleFelder(this);
	}

	/**
	 * Hier kommen die events meiner speziellen Buttons an.
	 * 
	 * @param e ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wrbDauer) || e.getSource().equals(wrbZeitraum)) {

			if (wrbDauer.isSelected()) {
				wtfDauerAb.setMandatoryField(true);
				wtfZeitraumVon.setMandatoryField(false);
				wtfZeitraumBis.setMandatoryField(false);
			} else {
				wtfDauerAb.setMandatoryField(false);
				wtfZeitraumVon.setMandatoryField(true);
				wtfZeitraumBis.setMandatoryField(true);
			}
			if (wcbEndeDesTages.isSelected()) {
				wtfZeitraumBis.setEnabled(false);
			} else {
				wtfZeitraumBis.setEnabled(true);

			}
		}

		if (e.getSource().equals(wcbEndeDesTages)) {
			if (wcbEndeDesTages.isSelected()) {
				wtfZeitraumBis.setEnabled(false);
			} else {
				wtfZeitraumBis.setEnabled(true);

			}
		}

	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		internalFrameRechnung.getTabbedPaneVerrechnungsmodell().deleteVerrechnungsmodelltag();
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {
		verrechnungsmodelltagDto.setVerrechnungsmodellIId(internalFrameRechnung.getVerrechnungsmodellDto().getIId());

		verrechnungsmodelltagDto.setTagesartIId((Integer) wcoTagesart.getKeyOfSelectedItem());

		verrechnungsmodelltagDto.setArtikelIIdGebucht(wifGebucht.getArtikelDto().getIId());
		verrechnungsmodelltagDto.setArtikelIIdZuverrechnen(wifZuVerrechnen.getArtikelDto().getIId());

		if (wrbDauer.isSelected()) {
			verrechnungsmodelltagDto.setUZeitraumVon(null);
			verrechnungsmodelltagDto.setUZeitraumBis(null);
			verrechnungsmodelltagDto.setUDauerAb(wtfDauerAb.getTime());
		} else {
			verrechnungsmodelltagDto.setUZeitraumVon(wtfZeitraumVon.getTime());
			verrechnungsmodelltagDto.setUZeitraumBis(wtfZeitraumBis.getTime());
			verrechnungsmodelltagDto.setUDauerAb(null);
		}

		verrechnungsmodelltagDto.setBEndedestages(wcbEndeDesTages.getShort());

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		if (verrechnungsmodelltagDto.getUDauerAb() != null) {
			wrbDauer.setSelected(true);
			wtfZeitraumBis.setTime(null);

			wtfZeitraumVon.setTime(null);

			wtfDauerAb.setTime(verrechnungsmodelltagDto.getUDauerAb());
		} else {
			wrbZeitraum.setSelected(true);
			wtfZeitraumVon.setTime(verrechnungsmodelltagDto.getUZeitraumVon());

			wtfZeitraumBis.setTime(verrechnungsmodelltagDto.getUZeitraumBis());

			wtfDauerAb.setTime(null);
		}

		wcoTagesart.setKeyOfSelectedItem(verrechnungsmodelltagDto.getTagesartIId());

		wifGebucht.setArtikelDto(DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(verrechnungsmodelltagDto.getArtikelIIdGebucht()));
		wifZuVerrechnen.setArtikelDto(DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(verrechnungsmodelltagDto.getArtikelIIdZuverrechnen()));

		BigDecimal bdPreisbasisGebucht = DelegateFactory.getInstance().getVkPreisfindungDelegate().ermittlePreisbasis(
				wifGebucht.getArtikelIId(), new java.sql.Date(System.currentTimeMillis()), null,
				LPMain.getTheClient().getSMandantenwaehrung());

		BigDecimal bdPreisbasisZuVerrechnen = DelegateFactory.getInstance().getVkPreisfindungDelegate()
				.ermittlePreisbasis(wifZuVerrechnen.getArtikelIId(), new java.sql.Date(System.currentTimeMillis()),
						null, LPMain.getTheClient().getSMandantenwaehrung());

		if (bdPreisbasisGebucht != null) {
			wlaVKPreisGebucht.setText(Helper.formatBetrag(bdPreisbasisGebucht, LPMain.getTheClient().getLocUi()));
		}
		if (bdPreisbasisZuVerrechnen != null) {
			wlaVKPreisZuVerrechnen
					.setText(Helper.formatBetrag(bdPreisbasisZuVerrechnen, LPMain.getTheClient().getLocUi()));
		}

		if (bdPreisbasisGebucht != null && bdPreisbasisZuVerrechnen != null) {

			if (bdPreisbasisGebucht.doubleValue() != 0) {
				BigDecimal bdFaktor = bdPreisbasisZuVerrechnen.divide(bdPreisbasisGebucht, 2,
						BigDecimal.ROUND_HALF_EVEN);

				wlaVKPreisGebucht.setText(Helper.formatBetrag(bdPreisbasisGebucht, LPMain.getTheClient().getLocUi()));
				wlaVKPreisZuVerrechnen
						.setText(Helper.formatBetrag(bdPreisbasisZuVerrechnen, LPMain.getTheClient().getLocUi()));

				wlaFaktor.setText(LPMain.getInstance().getTextRespectUISPr("label.faktor") + ": "
						+ Helper.formatZahl(bdFaktor, 2, LPMain.getTheClient().getLocUi()));

			}

		}

		wcbEndeDesTages.setShort(verrechnungsmodelltagDto.getBEndedestages());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (verrechnungsmodelltagDto.getIId() == null) {

				verrechnungsmodelltagDto.setIId(DelegateFactory.getInstance().getRechnungDelegate()
						.createVerrechnungsmodelltag(verrechnungsmodelltagDto));
				setKeyWhenDetailPanel(verrechnungsmodelltagDto.getIId());
			} else {
				DelegateFactory.getInstance().getRechnungDelegate()
						.updateVerrechnungsmodelltag(verrechnungsmodelltagDto);
			}

			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(internalFrameRechnung.getVerrechnungsmodellDto().getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
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
		wlaTagesart.setText(LPMain.getInstance().getTextRespectUISPr("lp.tagesart"));
		wcoTagesart.setMandatoryField(true);

		wlaZeitraumBis.setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungmodelltag.zeitraumbis"));

		wrbDauer.setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungmodelltag.dauer"));
		wrbZeitraum.setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungmodelltag.zeitraum"));

		bg.add(wrbDauer);
		bg.add(wrbZeitraum);

		wrbDauer.addActionListener(this);
		wrbZeitraum.addActionListener(this);

		wcbEndeDesTages.addActionListener(this);

		wcbEndeDesTages.setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungsmodell.endedestages"));

		wifGebucht.getWtfIdent().setMandatoryField(true);

		wifGebucht.getWbuArtikel()
				.setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungmodelltag.artikelgebucht"));
		wifZuVerrechnen.getWbuArtikel()
				.setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungmodelltag.artikelzuverrechnen"));

		wifZuVerrechnen.getWtfIdent().setMandatoryField(true);

		wrbDauer.setSelected(true);

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

		int iZeile = 0;

		jpaWorkingOn.add(wlaTagesart, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wcoTagesart, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wifGebucht.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wifGebucht.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));

		jpaWorkingOn.add(wifGebucht.getWtfBezeichnung(), new GridBagConstraints(2, iZeile, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));

		jpaWorkingOn.add(wlaVKPreisGebucht, new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));

		jpaWorkingOn.add(wlaFaktor, new GridBagConstraints(6, iZeile, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));

		iZeile++;

		jpaWorkingOn.add(wifZuVerrechnen.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifZuVerrechnen.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifZuVerrechnen.getWtfBezeichnung(), new GridBagConstraints(2, iZeile, 3, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaVKPreisZuVerrechnen, new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));

		iZeile++;

		jpaWorkingOn.add(wrbDauer, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wtfDauerAb, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));

		WrapperLabel wlaUeberschrift1 = new WrapperLabel("hh:mm");
		wlaUeberschrift1.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaUeberschrift1, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30, 0));
		iZeile++;

		jpaWorkingOn.add(wrbZeitraum, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfZeitraumVon, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 70, 0));
		ImageIcon iicon = new ImageIcon(getClass().getResource("/com/lp/client/res/clock16x16.png"));
		WrapperLabel wlaUeberschrift0 = new WrapperLabel(iicon);
		wlaUeberschrift0.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaUeberschrift0, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));

		jpaWorkingOn.add(wlaZeitraumBis, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfZeitraumBis, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 60, 0));

		iicon = new ImageIcon(getClass().getResource("/com/lp/client/res/clock16x16.png"));
		WrapperLabel wlaUeberschrift3 = new WrapperLabel(iicon);
		wlaUeberschrift3.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaUeberschrift3, new GridBagConstraints(5, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));

		jpaWorkingOn.add(wcbEndeDesTages, new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_VERRECHNUNGSMODELL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		wlaVKPreisGebucht.setText("");
		wlaVKPreisZuVerrechnen.setText("");
		wlaFaktor.setText("");

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();

			if (wcbEndeDesTages.isSelected()) {
				wtfZeitraumBis.setEnabled(false);
			} else {
				if (key != null) {
					wtfZeitraumBis.setEnabled(true);
				}
			}

		} else {
			verrechnungsmodelltagDto = DelegateFactory.getInstance().getRechnungDelegate()
					.verrechnungsmodelltagFindByPrimaryKey((Integer) key);

			dto2Components();
		}
	}
}
