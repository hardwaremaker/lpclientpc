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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.InventurDto;
import com.lp.server.artikel.service.InventurFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelInventurAbschliessen extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameArtikel internalFrameArtikel = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperLabel wlaInventurdatum = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperDateField wdfInventurdatum = new WrapperDateField();

	private WrapperLabel wlaLager = new WrapperLabel();
	private WrapperTextField wtfLager = new WrapperTextField();
	private WrapperLabel wlaInventurdurchgefuehrt = new WrapperLabel();
	private WrapperLabel wlaAbwertungdurchgefuehrt = new WrapperLabel();

	private WrapperCheckBox wcbNichtInventierteArtikelAufNullSetzen = new WrapperCheckBox();
	private WrapperCheckBox wcbNichtInventierteSnrChnrArtikelAufNullSetzen = new WrapperCheckBox();
	static final public String ACTION_SPECIAL_INVENTUR_DURCHFUEHREN = "action_inventur_durchfuehren";

	public PanelInventurAbschliessen(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	protected void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		internalFrameArtikel.setInventurDto(new InventurDto());
		leereAlleFelder(this);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		super.eventActionDelete(e, false, false);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = null;

		if (internalFrameArtikel.getInventurDto() != null) {
			key = internalFrameArtikel.getInventurDto().getIId();
		}

		if (key == null) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			internalFrameArtikel.setInventurDto(DelegateFactory
					.getInstance()
					.getInventurDelegate()
					.inventurFindByPrimaryKey(
							internalFrameArtikel.getInventurDto().getIId()));
			dto2Components();
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getInstance()
							.getTextRespectUISPr("artikel.inventur")
							+ ": "
							+ internalFrameArtikel.getInventurDto().getCBez());
			if (Helper.short2boolean(internalFrameArtikel.getInventurDto()
					.getBInventurdurchgefuehrt())) {
				getHmOfButtons().get(ACTION_SPECIAL_INVENTUR_DURCHFUEHREN)
						.getButton().setEnabled(false);
			} else {
				getHmOfButtons().get(ACTION_SPECIAL_INVENTUR_DURCHFUEHREN)
						.getButton().setEnabled(true);
			}
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		if (Helper.short2boolean(internalFrameArtikel.getInventurDto()
				.getBInventurdurchgefuehrt())) {
			wdfInventurdatum.setEnabled(false);
			wcbNichtInventierteArtikelAufNullSetzen.setEnabled(false);
			wcbNichtInventierteSnrChnrArtikelAufNullSetzen.setEnabled(false);

		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == internalFrameArtikel.getTabbedPaneInventur()
					.getPanelQueryInventur()) {
				if (Helper.short2boolean(internalFrameArtikel.getInventurDto()
						.getBInventurdurchgefuehrt())) {
					getHmOfButtons().get(ACTION_SPECIAL_INVENTUR_DURCHFUEHREN)
							.getButton().setEnabled(false);
				} else {
					getHmOfButtons().get(ACTION_SPECIAL_INVENTUR_DURCHFUEHREN)
							.getButton().setEnabled(false);
				}
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);
		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wlaInventurdatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.inventurdatum"));
		wtfBezeichnung.setColumnsMax(InventurFac.MAX_INVENTUR_BEZEICHNUNG);
		wtfBezeichnung.setText("");
		wtfBezeichnung.setMandatoryField(true);
		wdfInventurdatum.setMandatoryField(true);

		wtfBezeichnung.setActivatable(false);
		wdfInventurdatum.setActivatable(false);

		wlaLager.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.lager"));

		wlaInventurdurchgefuehrt.setHorizontalAlignment(SwingConstants.LEFT);
		wlaAbwertungdurchgefuehrt.setHorizontalAlignment(SwingConstants.LEFT);

		wtfLager.setActivatable(false);

		wcbNichtInventierteArtikelAufNullSetzen
				.setText(LPMain
						.getInstance()
						.getTextRespectUISPr(
								"artikel.inventur.nichtinventierteartikelaufnullsetzen"));

		wcbNichtInventierteSnrChnrArtikelAufNullSetzen
				.setText(LPMain
						.getInstance()
						.getTextRespectUISPr(
								"artikel.inventur.nichtinventiertesnrshnrartikelaufnullsetzen"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaInventurdatum, new GridBagConstraints(0, 0, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfInventurdatum, new GridBagConstraints(1, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 1, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 1, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaLager, new GridBagConstraints(0, 2, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, 2, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbNichtInventierteArtikelAufNullSetzen,
				new GridBagConstraints(1, 3, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wcbNichtInventierteSnrChnrArtikelAufNullSetzen,
				new GridBagConstraints(1, 4, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wlaInventurdurchgefuehrt, new GridBagConstraints(1, 6,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(20, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaAbwertungdurchgefuehrt, new GridBagConstraints(1,
				7, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(20, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		getToolBar().addButtonLeft("/com/lp/client/res/check2.png",
				LPMain.getTextRespectUISPr("artikel.inventur.durchfuehren"),
				ACTION_SPECIAL_INVENTUR_DURCHFUEHREN, null, null);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_INVENTUR;
	}

	protected void components2Dto() {
		internalFrameArtikel.getInventurDto().setCBez(wtfBezeichnung.getText());
		internalFrameArtikel.getInventurDto().setTInventurdatum(
				wdfInventurdatum.getTimestamp());
		internalFrameArtikel.getInventurDto()
				.setBNichtinventierteartikelabbuchen(
						wcbNichtInventierteArtikelAufNullSetzen.getShort());
		internalFrameArtikel.getInventurDto()
				.setBNichtinventierteSnrchnrArtikelabbuchen(
						wcbNichtInventierteSnrChnrArtikelAufNullSetzen
								.getShort());
	}

	protected void dto2Components() throws Throwable {
		wtfBezeichnung.setText(internalFrameArtikel.getInventurDto().getCBez());
		wdfInventurdatum.setTimestamp(internalFrameArtikel.getInventurDto()
				.getTInventurdatum());
		wcbNichtInventierteArtikelAufNullSetzen.setShort(internalFrameArtikel
				.getInventurDto().getBNichtinventierteartikelabbuchen());
		wcbNichtInventierteSnrChnrArtikelAufNullSetzen
				.setShort(internalFrameArtikel.getInventurDto()
						.getBNichtinventierteSnrchnrArtikelabbuchen());

		if (internalFrameArtikel.getInventurDto().getLagerIId() != null) {
			LagerDto lagerDto = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.lagerFindByPrimaryKey(
							internalFrameArtikel.getInventurDto().getLagerIId());
			wtfLager.setText(lagerDto.getCNr());
		} else {
			wtfLager.setText(null);
		}

		if (Helper.short2boolean(internalFrameArtikel.getInventurDto()
				.getBInventurdurchgefuehrt()) == true) {

			String s = "";
			if (internalFrameArtikel.getInventurDto()
					.getPersonalIIdInventurdurchgefuehrt() != null) {
				PersonalDto personalDto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								internalFrameArtikel.getInventurDto()
										.getPersonalIIdInventurdurchgefuehrt());

				s += personalDto.formatAnrede() + " hat am ";
				s += Helper.formatTimestamp(internalFrameArtikel
						.getInventurDto().getTInventurdurchgefuehrt(), LPMain
						.getTheClient().getLocUi());

				s += " die Inventur durchgef\u00FChrt";

			}

			wlaInventurdurchgefuehrt.setText(s);
		} else {
			wlaInventurdurchgefuehrt.setText(null);
		}

		if (Helper.short2boolean(internalFrameArtikel.getInventurDto()
				.getBAbwertungdurchgefuehrt()) == true) {

			String s = "";
			if (internalFrameArtikel.getInventurDto()
					.getPersonalIIdAbwertungdurchgefuehrt() != null) {
				PersonalDto personalDto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								internalFrameArtikel.getInventurDto()
										.getPersonalIIdAbwertungdurchgefuehrt());

				s += personalDto.formatAnrede() + " hat am ";
				s += Helper.formatTimestamp(internalFrameArtikel
						.getInventurDto().getTAbwertungdurchgefuehrt(), LPMain
						.getTheClient().getLocUi());

				s += " die Preise abgewertet";

			}

			wlaAbwertungdurchgefuehrt.setText(s);
		} else {
			wlaAbwertungdurchgefuehrt.setText(null);
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_INVENTUR_DURCHFUEHREN)) {

			boolean bDurchfuehren = DialogFactory.showModalJaNeinDialog(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"artikel.warning.inventurdurchfuehren"));

			if (bDurchfuehren == true) {

				if (wcbNichtInventierteArtikelAufNullSetzen.isSelected()
						|| wcbNichtInventierteSnrChnrArtikelAufNullSetzen
								.isSelected()) {

					String sMeldung = null;

					if (wcbNichtInventierteArtikelAufNullSetzen.isSelected()
							&& wcbNichtInventierteSnrChnrArtikelAufNullSetzen
									.isSelected()) {

						sMeldung = LPMain
								.getInstance()
								.getTextRespectUISPr(
										"artikel.inventur.warnung.nichtinventiertealleartikelaufnullsetzen");
					} else {
						if (wcbNichtInventierteArtikelAufNullSetzen
								.isSelected()) {

							sMeldung = LPMain
									.getInstance()
									.getTextRespectUISPr(
											"artikel.inventur.warnung.nichtinventierteartikelaufnullsetzen");
						} else if (wcbNichtInventierteSnrChnrArtikelAufNullSetzen
								.isSelected()) {

							sMeldung = LPMain
									.getInstance()
									.getTextRespectUISPr(
											"artikel.inventur.warnung.nichtinventiertesnrchnrartikelaufnullsetzen");
						}
					}

					boolean b = DialogFactory.showModalJaNeinDialog(
							getInternalFrame(), sMeldung);

					if (b == false) {
						return;
					}
				}

				DelegateFactory
						.getInstance()
						.getInventurDelegate()
						.inventurDurchfuehren(
								internalFrameArtikel.getInventurDto().getIId());
				DialogFactory.showModalDialog("Info",
						"Inventur durchgef\u00FChrt!");
				eventYouAreSelected(false);

				getHmOfButtons().get(ACTION_SPECIAL_INVENTUR_DURCHFUEHREN)
						.getButton().setEnabled(false);
			}

		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (internalFrameArtikel.getInventurDto().getIId() == null) {
				// Create
				internalFrameArtikel
						.getInventurDto()
						.setBAbwertungdurchgefuehrt(Helper.boolean2Short(false));
				internalFrameArtikel.getInventurDto()
						.setBInventurdurchgefuehrt(Helper.boolean2Short(false));

				internalFrameArtikel.getInventurDto().setIId(
						DelegateFactory
								.getInstance()
								.getInventurDelegate()
								.createInventur(
										internalFrameArtikel.getInventurDto()));
				internalFrameArtikel.setInventurDto(internalFrameArtikel
						.getInventurDto());

				InventurDto[] dtos = DelegateFactory
						.getInstance()
						.getInventurDelegate()
						.inventurFindInventurenNachDatum(
								wdfInventurdatum.getTimestamp());

				if (dtos != null && dtos.length > 0) {

					DialogFactory
							.showModalDialog(LPMain.getInstance()
									.getTextRespectUISPr("lp.error"),
									"Es gibt bereits Inventuren nach dem angegebenen Inventurdatum");
				}
				// diesem panel den key setzen.
				setKeyWhenDetailPanel(internalFrameArtikel.getInventurDto()
						.getIId());
			} else {
				DelegateFactory.getInstance().getInventurDelegate()
						.updateInventur(internalFrameArtikel.getInventurDto());
			}

			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameArtikel.getInventurDto().getIId()
								.toString());
			}
			eventYouAreSelected(false);

		}
	}

}
