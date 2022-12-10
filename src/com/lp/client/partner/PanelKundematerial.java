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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.MaterialzuschlagDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.KundematerialDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerkommunikationDto;

@SuppressWarnings("static-access")
/**
 *
 * <p> Diese Klasse kuemmert sich die Partnerkommunikationen.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; dd.mm.05</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2012/08/29 14:29:29 $
 */
public class PanelKundematerial extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneKunde tpKunde = null;

	private JPanel jpaWorking = null;
	private GridBagLayout gblWorkingPanel = null;
	private Border border = null;
	private GridBagLayout gblAussen = null;

	private WrapperSelectField wsfMaterial = new WrapperSelectField(
			WrapperSelectField.MATERIAL, getInternalFrame(), false);
	private WrapperSelectField wsfMaterialNotierung = new WrapperSelectField(
			WrapperSelectField.MATERIAL, getInternalFrame(), false);

	private WrapperLabel wlaMaterialbsasis = new WrapperLabel();
	private WrapperNumberField wnfMaterialbasis = new WrapperNumberField();

	private WrapperCheckBox wcbMaterialInklusive = new WrapperCheckBox();

	private WrapperLabel wlaAufschlag = new WrapperLabel();
	private WrapperLabel wlaAufschlagWaehrung = new WrapperLabel();
	private WrapperLabel wlaEinheitBasis = new WrapperLabel();

	private WrapperNumberField wnfAufschlagBetrag = new WrapperNumberField();
	private WrapperNumberField wnfAufschlagProzent = new WrapperNumberField();

	private KundematerialDto kundematerialDto = null;

	public PanelKundematerial(InternalFrame internalFrame, String add2TitleI,
			Object keyI, TabbedPaneKunde tpKunde) throws Throwable {

		super(internalFrame, add2TitleI, keyI);
		this.tpKunde = tpKunde;
		jbInit();
		initComponents();
		initPanel();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		// buttons.
		resetToolsPanel();

		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DELETE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		// das aussenpanel hat immer das gridbaglayout.
		gblAussen = new GridBagLayout();
		setLayout(gblAussen);

		// actionpanel von Oberklasse holen und anhaengen.
		add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		wsfMaterial.setMandatoryField(true);
		wsfMaterialNotierung.getWrapperButton().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"part.material.notierung")
						+ "...");
		wsfMaterialNotierung.setMandatoryField(true);
		wlaMaterialbsasis.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.material.basis"));
		wlaAufschlag.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.technik.aufschlagmaterial"));
		wcbMaterialInklusive.setText(LPMain.getInstance().getTextRespectUISPr(
				"part.material.materialinklusive"));
		wlaAufschlagWaehrung.setHorizontalAlignment(SwingConstants.LEFT);

		wlaEinheitBasis.setHorizontalAlignment(SwingConstants.LEFT);
		
		wnfMaterialbasis.setFractionDigits(6);
		wnfMaterialbasis.setMandatoryField(true);
		// Workingpanel
		jpaWorking = new JPanel();
		gblWorkingPanel = new GridBagLayout();
		jpaWorking.setLayout(gblWorkingPanel);
		add(jpaWorking, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		jpaWorking.add(wsfMaterial.getWrapperButton(), new GridBagConstraints(
				0, iZeile, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wsfMaterial.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 4, 1, 0.5, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jpaWorking.add(wsfMaterialNotierung.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wsfMaterialNotierung.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 4, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorking.add(wlaMaterialbsasis, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wnfMaterialbasis, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wlaEinheitBasis, new GridBagConstraints(2, iZeile, 2,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		
		
		iZeile++;
		jpaWorking.add(wcbMaterialInklusive, new GridBagConstraints(1, iZeile,
				2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorking.add(wlaAufschlag, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorking.add(wnfAufschlagBetrag, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wlaAufschlagWaehrung, new GridBagConstraints(2, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
		jpaWorking.add(wnfAufschlagProzent, new GridBagConstraints(3, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel wlaProz = new WrapperLabel("%");
		wlaProz.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorking.add(wlaProz, new GridBagConstraints(4, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 30, 0));

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// Neu.
			leereAlleFelder(this);

			clearStatusbar();

		} else {
			kundematerialDto = DelegateFactory.getInstance()
					.getMaterialDelegate()
					.kundematerialFindByPrimaryKey((Integer) key);

			dto2Components();
		}

		String sTitel = getTabbedPaneKunde().getKundeDto().getPartnerDto()
				.formatFixTitelName1Name2();

		wlaAufschlagWaehrung.setText(getTabbedPaneKunde().getKundeDto()
				.getCWaehrung());
		wlaEinheitBasis.setText(getTabbedPaneKunde().getKundeDto().getCWaehrung() + "/1kg");

		getInternalFrame()
				.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitel);

	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		wsfMaterial.setKey(kundematerialDto.getMaterialIId());
		wsfMaterialNotierung.setKey(kundematerialDto.getMaterialIIdNotierung());
		wnfMaterialbasis.setBigDecimal(kundematerialDto.getNMaterialbasis());
		wnfAufschlagBetrag.setBigDecimal(kundematerialDto.getNAufschlagBetrag());
		wnfAufschlagProzent.setDouble(kundematerialDto.getFAufschlagProzent());
		wcbMaterialInklusive.setShort(kundematerialDto.getBMaterialInklusive());
	}

	/**
	 * Behandle Ereignis Save.
	 * 
	 * @param e
	 *            Ereignis
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			checkLockedDlg();

			components2Dto();
			if (kundematerialDto.getIId() == null) {
				// Create.
				kundematerialDto.setIId(DelegateFactory.getInstance()
						.getMaterialDelegate()
						.createKundematerial(kundematerialDto));
				setKeyWhenDetailPanel(kundematerialDto.getIId());
			} else {
				// Update.
				DelegateFactory.getInstance().getMaterialDelegate()
						.updateKundematerial(kundematerialDto);
			}
			super.eventActionSave(e, false);
			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!isLockedDlg()) {
			DelegateFactory.getInstance().getMaterialDelegate()
					.removeKundematerial(kundematerialDto);

			kundematerialDto = new KundematerialDto();
			this.setKeyWhenDetailPanel(null);

			super.eventActionDelete(e, false, false);
		}
	}

	private void components2Dto() throws Throwable {
		kundematerialDto.setKundeIId(getTabbedPaneKunde().getKundeDto().getIId());
		kundematerialDto.setMaterialIId(wsfMaterial.getIKey());
		kundematerialDto.setMaterialIIdNotierung(wsfMaterialNotierung.getIKey());
		kundematerialDto.setNMaterialbasis(wnfMaterialbasis.getBigDecimal());
		kundematerialDto.setBMaterialInklusive(wcbMaterialInklusive.getShort());
		kundematerialDto.setNAufschlagBetrag(wnfAufschlagBetrag.getBigDecimal());
		kundematerialDto.setFAufschlagProzent(wnfAufschlagProzent.getDouble());
	}

	/**
	 * Fuelle die Helpercontrols, jene aus denen ausgewaehlt werden kann.
	 * 
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PARTNER;
	}

	private TabbedPaneKunde getTabbedPaneKunde() {
		return tpKunde;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		if (!bNeedNoNewI) {
			kundematerialDto = new KundematerialDto();

			setDefaults();
		}
	}

	/**
	 * setDefaults
	 * 
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wsfMaterial.getWrapperButton();
	}

}
