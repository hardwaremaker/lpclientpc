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

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

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
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.WerkzeugDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelWerkzeug extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperDateField wdfDatum = new WrapperDateField();

	private WrapperLabel wlaKaufpreis = new WrapperLabel();
	private WrapperNumberField wnfKaufpreis = new WrapperNumberField();

	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperEditorField wtfKommentar = new WrapperEditorField(
			getInternalFrame(), "");

	private WerkzeugDto werkzeugDto = null;

	private WrapperButton wbuMandantStandort = new WrapperButton();

	private WrapperTextField wtfMandantStandort = new WrapperTextField();

	private WrapperSelectField wsfLieferant = new WrapperSelectField(
			WrapperSelectField.LIEFERANT, getInternalFrame(), true);
	private WrapperSelectField wsfLagerplatz = new WrapperSelectField(
			WrapperSelectField.LAGERPLATZ, getInternalFrame(), true);

	private static final String ACTION_SPECIAL_STANDORT = "action_special_standort";

	private PanelQueryFLR panelQueryFLRStandort = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();

	public PanelWerkzeug(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);

		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfDatum;
	}

	private void dialogQueryStandort(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelQueryFLRStandort = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_MANDANT, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("title.mandantauswahlliste"));
		if (panelQueryFLRStandort != null) {

			panelQueryFLRStandort.setSelectedId(werkzeugDto
					.getMandantCNrStandort());

		}
		new DialogQuery(panelQueryFLRStandort);
	}

	protected void setDefaults() throws Throwable {

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		werkzeugDto = new WerkzeugDto();
		// getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_STANDORT)) {
			dialogQueryStandort(e);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getArtikelDelegate()
				.removeWerkzeug(werkzeugDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		werkzeugDto.setTKaufdatum(wdfDatum.getTimestamp());
		werkzeugDto.setCBez(wtfBezeichnung.getText());
		werkzeugDto.setCNr(wtfKennung.getText());
		werkzeugDto.setLieferantIId(wsfLieferant.getIKey());
		werkzeugDto.setLagerplatzIId(wsfLagerplatz.getIKey());
		werkzeugDto.setNKaufpreis(wnfKaufpreis.getBigDecimal());
		werkzeugDto.setXKommentar(wtfKommentar.getText());
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (getKeyWhenDetailPanel() != null
				&& getKeyWhenDetailPanel() instanceof Integer) {

			WerkzeugDto wDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.werkzeugFindByPrimaryKey((Integer) getKeyWhenDetailPanel());

			if (!LPMain.getTheClient().getMandant()
					.equals(wDto.getMandantCNrStandort())) {

				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	protected void dto2Components() throws Throwable {
		wdfDatum.setTimestamp(werkzeugDto.getTKaufdatum());
		wtfBezeichnung.setText(werkzeugDto.getCBez());
		wtfKennung.setText(werkzeugDto.getCNr());

		MandantDto mDto = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(werkzeugDto.getMandantCNrStandort());

		wtfMandantStandort.setText(mDto.getCNr() + " " + mDto.getCKbez());

		wsfLieferant.setKey(werkzeugDto.getLieferantIId());
		wsfLagerplatz.setKey(werkzeugDto.getLagerplatzIId());
		wnfKaufpreis.setBigDecimal(werkzeugDto.getNKaufpreis());
		wtfKommentar.setText(werkzeugDto.getXKommentar());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (werkzeugDto.getIId() == null) {
				werkzeugDto.setIId(DelegateFactory.getInstance()
						.getArtikelDelegate().createWerkzeug(werkzeugDto));
				setKeyWhenDetailPanel(werkzeugDto.getIId());

			} else {
				DelegateFactory.getInstance().getArtikelDelegate()
						.updateWerkzeug(werkzeugDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame()
						.setKeyWasForLockMe(werkzeugDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRStandort) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				if (!key.equals(LPMain.getTheClient().getMandant())) {
					wsfLieferant.setKey(null);
					wsfLagerplatz.setKey(null);
				}

				MandantDto mDto = DelegateFactory.getInstance()
						.getMandantDelegate()
						.mandantFindByPrimaryKey((String) key);

				wtfMandantStandort.setText(mDto.getCNr() + " "
						+ mDto.getCKbez());

				werkzeugDto.setMandantCNrStandort(mDto.getCNr());
			}

		}
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

		wlaDatum.setText(LPMain.getInstance().getTextRespectUISPr(
				"zeiterfassung.kaufdatum"));
		wlaKaufpreis.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.werkzeug.kaufpreis"));

		getInternalFrame().addItemChangedListener(this);
		wbuMandantStandort.setText(LPMain.getInstance().getTextRespectUISPr(
				"system.standort")
				+ "...");
		wbuMandantStandort.setActionCommand(ACTION_SPECIAL_STANDORT);
		wbuMandantStandort.addActionListener(this);

		wtfMandantStandort.setActivatable(false);
		wtfMandantStandort.setText("");
		wtfMandantStandort.setMandatoryField(true);

		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kommentar"));

		wlaKennung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.kennung"));
		wtfKennung.setMandatoryField(true);

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfBezeichnung.setColumnsMax(80);
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

		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, iZeile, 1, 1,
				0.4, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuMandantStandort, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfMandantStandort, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfLieferant.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfLieferant.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfLagerplatz.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfLagerplatz.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfDatum, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaKaufpreis, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKaufpreis, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;

		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKommentar, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_WERKZEUG;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();

			if (key != null) {
				wtfMandantStandort.setText(LPMain.getTheClient().getMandant());
				werkzeugDto.setMandantCNrStandort(LPMain.getTheClient()
						.getMandant());
			}

		} else {
			werkzeugDto = DelegateFactory.getInstance().getArtikelDelegate()
					.werkzeugFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

}
