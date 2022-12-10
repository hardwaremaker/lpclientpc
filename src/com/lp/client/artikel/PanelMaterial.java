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

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.MaterialsprDto;

@SuppressWarnings("static-access")
public class PanelMaterial extends PanelBasis {

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
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();

	private WrapperLabel wlaSpezGew = new WrapperLabel();
	private WrapperNumberField wnfSpezGew = new WrapperNumberField();

	public PanelMaterial(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		internalFrameArtikel.materialDto = new MaterialDto();
		leereAlleFelder(this);
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getMaterialDelegate().removeMaterial(internalFrameArtikel.materialDto.getIId());
		super.eventActionDelete(e, false, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		Object key = null;

		if (internalFrameArtikel.materialDto != null) {
			key = internalFrameArtikel.materialDto.getIId();
		}

		if (key == null) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			internalFrameArtikel.materialDto = DelegateFactory.getInstance().getMaterialDelegate()
					.materialFindByPrimaryKey(internalFrameArtikel.materialDto.getIId());
			wtfKennung.setText(internalFrameArtikel.materialDto.getCNr());

			wnfSpezGew.setBigDecimal(internalFrameArtikel.materialDto.getNGewichtInKG());
			
			if (internalFrameArtikel.materialDto.getMaterialsprDto() != null) {
				wtfBezeichnung.setText(internalFrameArtikel.materialDto.getMaterialsprDto().getCBez());
			}
			String sBezeichnung = "";
			if (internalFrameArtikel.materialDto.getMaterialsprDto() != null) {
				sBezeichnung = internalFrameArtikel.materialDto.getMaterialsprDto().getCBez() + "";
			}
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getInstance().getTextRespectUISPr("label.material") + ": "
							+ internalFrameArtikel.materialDto.getCNr() + ", "
							+ LPMain.getInstance().getTextRespectUISPr("label.bezeichnung") + ": " + sBezeichnung);
		}
	}

	protected void eventItemchanged(EventObject eI) {
		eI = (ItemChangedEvent) eI;
	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaKennung.setText(LPMain.getInstance().getTextRespectUISPr("lp.material"));
		wtfKennung.setColumnsMax(ArtikelFac.MAX_MATERIAL_NAME);
		wtfKennung.setText("");
		wtfKennung.setMandatoryField(true);
		getInternalFrame().addItemChangedListener(this);

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung.setColumnsMax(ArtikelFac.MAX_MATERIAL_BEZEICHNUNG);
		wtfBezeichnung.setText("");

		wlaSpezGew.setText(LPMain.getInstance().getTextRespectUISPr("artikel.material.spezifischesgewicht"));

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
		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, 0, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(2, 0, 1, 1, 0.09, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(3, 0, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaSpezGew, new GridBagConstraints(2, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSpezGew, new GridBagConstraints(3, 1, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MATERIAL;
	}

	protected void components2Dto() throws Exception {
		internalFrameArtikel.materialDto.setCNr(wtfKennung.getText());
		if (internalFrameArtikel.materialDto.getMaterialsprDto() == null) {
			internalFrameArtikel.materialDto.setMaterialsprDto(new MaterialsprDto());
		}
		internalFrameArtikel.materialDto.getMaterialsprDto().setCBez(wtfBezeichnung.getText());
		internalFrameArtikel.materialDto.setNGewichtInKG(wnfSpezGew.getBigDecimal());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (internalFrameArtikel.materialDto.getIId() == null) {
				// Create
				internalFrameArtikel.materialDto.setIId(DelegateFactory.getInstance().getMaterialDelegate()
						.createMaterial(internalFrameArtikel.materialDto));
				internalFrameArtikel.materialDto = internalFrameArtikel.materialDto;
				// diesem panel den key setzen.
				setKeyWhenDetailPanel(internalFrameArtikel.materialDto.getIId());
			} else {
				DelegateFactory.getInstance().getMaterialDelegate().updateMaterial(internalFrameArtikel.materialDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(internalFrameArtikel.materialDto.getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

}
