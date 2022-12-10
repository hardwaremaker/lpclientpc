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
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.MmzDto;

@SuppressWarnings("static-access")
/**
 * <p>
 * Diese Klasse kuemmert sich um die Wechselkurse
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellung: Martin Bluehweis; 22.06.05
 * </p>
 *
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 *
 * @version not attributable Date $Date: 2012/08/06 07:42:38 $
 */
public class PanelMmz extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaBisWert = new WrapperLabel();
	private WrapperNumberField wnfBisWert = new WrapperNumberField();

	private WrapperLabel wlaZuschlag = new WrapperLabel();
	private WrapperNumberField wnfZuschlag = new WrapperNumberField();

	private WrapperSelectField wsfLAnd = new WrapperSelectField(WrapperSelectField.LAND, getInternalFrame(), true);

	private MmzDto mmzDto = null;

	private WrapperIdentField wifArtikelauswahl = null;

	public PanelMmz(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
	}

	private void setDefaults() throws Throwable {

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		mmzDto = new MmzDto();
		leereAlleFelder(this);
		setDefaults();
		clearStatusbar();

	}

	protected void dto2Components() throws Throwable {
		if (mmzDto != null) {
			wnfBisWert.setBigDecimal(mmzDto.getNBisWert());
			wnfZuschlag.setBigDecimal(mmzDto.getNZuschlag());
			wifArtikelauswahl.setArtikelIId(mmzDto.getArtikelIId());
			wsfLAnd.setKey(mmzDto.getLandIId());

		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();

		} else {
			mmzDto = DelegateFactory.getInstance().getRechnungServiceDelegate().mmzFindByPrimaryKey((Integer) key);
			dto2Components();
		}

	}

	protected void components2Dto() throws Throwable {
		mmzDto.setMandantCNr(LPMain.getTheClient().getMandant());
		mmzDto.setArtikelIId(wifArtikelauswahl.getArtikelIId());
		mmzDto.setNBisWert(wnfBisWert.getBigDecimal());
		mmzDto.setNZuschlag(wnfZuschlag.getBigDecimal());
		mmzDto.setLandIId(wsfLAnd.getIKey());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (mmzDto.getIId() == null) {
				mmzDto.setIId(DelegateFactory.getInstance().getRechnungServiceDelegate().createMmz(mmzDto));
				setKeyWhenDetailPanel(mmzDto.getIId());
			} else {
				DelegateFactory.getInstance().getRechnungServiceDelegate().updateMmz(mmzDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(mmzDto.getIId() + "");
			}

			eventYouAreSelected(false);

		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getRechnungServiceDelegate().removeMmz(mmzDto);
		super.eventActionDelete(e, false, false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);

		wnfBisWert.setMandatoryField(true);
		wnfZuschlag.setMandatoryField(true);

		wifArtikelauswahl = new WrapperIdentField(getInternalFrame(), this);

		wlaBisWert.setText(LPMain.getInstance().getTextRespectUISPr("rechnung.mindermengenzuschlag.biswert") + " ("
				+ LPMain.getTheClient().getSMandantenwaehrung() + ")");
		wlaZuschlag.setText(LPMain.getInstance().getTextRespectUISPr("rechnung.mindermengenzuschlag.zuschlag") + " ("
				+ LPMain.getTheClient().getSMandantenwaehrung() + ")");
		int iPreiseUINachkommastellenVK = Defaults.getInstance().getIUINachkommastellenPreiseVK();

		wnfBisWert.setFractionDigits(iPreiseUINachkommastellenVK);
		wnfZuschlag.setFractionDigits(iPreiseUINachkommastellenVK);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaBisWert, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jPanelWorkingOn.add(wnfBisWert, new GridBagConstraints(1, iZeile, 1, 1, 0.4, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wifArtikelauswahl.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wifArtikelauswahl.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wifArtikelauswahl.getWtfBezeichnung(), new GridBagConstraints(2, iZeile, 1, 1, 0.8, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaZuschlag, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfZuschlag, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wsfLAnd.getWrapperButton(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wsfLAnd.getWrapperTextField(), new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_WAEHRUNG;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}
}
