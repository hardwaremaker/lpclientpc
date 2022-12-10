package com.lp.client.personal;

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

import com.lp.client.artikel.ReportLieferantenpreis;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.partner.service.BeauskunftungDto;
import com.lp.server.partner.service.IdentifikationDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ReligionDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelBeauskunftung extends PanelBasis {

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
	private WrapperButton wbuIdentifikation = new WrapperButton();
	private WrapperTextField wtfIdentifikation = new WrapperTextField();

	private WrapperCheckBox wcbKostenpflichtig = new WrapperCheckBox();

	private BeauskunftungDto beauskunftungDto = null;

	private PanelQueryFLR panelQueryFLRIdentifikation = null;

	static final public String ACTION_SPECIAL_IDENTIFIKATION_FROM_LISTE = "action_identifikation_from_liste";

	private TabbedPaneDSGVO tpDSGVO = null;

	public PanelBeauskunftung(InternalFrame internalFrame,
			TabbedPaneDSGVO tpDSGVO, String add2TitleI, Object pk)
			throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.tpDSGVO = tpDSGVO;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuIdentifikation;
	}

	void dialogQueryIdentifikationFromListe(ActionEvent e) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRIdentifikation = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_IDENTIFIKATION, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"pers.identifikation"));
		panelQueryFLRIdentifikation.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("if.c_nr", "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getInstance()
								.getTextRespectUISPr("label.kennung"),
						FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
						true, true, Facade.MAX_UNBESCHRAENKT), null);
		if (beauskunftungDto.getIdentifikationIId() != null) {
			panelQueryFLRIdentifikation.setSelectedId(beauskunftungDto
					.getIdentifikationIId());
		}

		new DialogQuery(panelQueryFLRIdentifikation);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		beauskunftungDto = new BeauskunftungDto();

		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_IDENTIFIKATION_FROM_LISTE)) {
			dialogQueryIdentifikationFromListe(e);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRIdentifikation) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				IdentifikationDto identifikationDto = DelegateFactory
						.getInstance().getPartnerServicesDelegate()
						.identifikationFindByPrimaryKey((Integer) key);
				wtfIdentifikation
						.setText(identifikationDto.formatBezeichnung());
				beauskunftungDto.setIdentifikationIId(identifikationDto
						.getIId());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRIdentifikation) {
				wtfIdentifikation.setText(null);
				beauskunftungDto.setIdentifikationIId(null);
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

		wbuIdentifikation.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.identifikation"));
		wcbKostenpflichtig.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.beauskunftung.kostenpflichtig"));

		wbuIdentifikation
				.setActionCommand(ACTION_SPECIAL_IDENTIFIKATION_FROM_LISTE);
		wbuIdentifikation.addActionListener(this);
		wtfIdentifikation.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);
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
		jpaWorkingOn.add(wbuIdentifikation, new GridBagConstraints(0, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfIdentifikation, new GridBagConstraints(1, 0, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wcbKostenpflichtig, new GridBagConstraints(1, 1, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD,ACTION_PRINT };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BEAUSKUNFTUNG;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPartnerServicesDelegate()
				.removeBeauskunftung(beauskunftungDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		beauskunftungDto.setPartnerIId((Integer) tpDSGVO.getPanelQueryPArtner()
				.getSelectedId());
		beauskunftungDto.setBKostenpflichtig(wcbKostenpflichtig.getShort());

	}

	protected void dto2Components() throws Throwable {

		if (beauskunftungDto.getIdentifikationIId() != null) {

			wtfIdentifikation.setText(DelegateFactory
					.getInstance()
					.getPartnerServicesDelegate()
					.identifikationFindByPrimaryKey(
							beauskunftungDto.getIdentifikationIId())
					.formatBezeichnung());
		} else {
			wtfIdentifikation.setText(null);
		}
		wcbKostenpflichtig.setShort(beauskunftungDto.getBKostenpflichtig());

	}

	
	protected void eventActionPrint(ActionEvent e) throws Throwable {
		String add2Title = LPMain
				.getTextRespectUISPr("pers.dsgvo.beauskunftung");
		getInternalFrame().showReportKriterien(
				new ReportBeauskunftung(tpDSGVO,beauskunftungDto.getIId(), add2Title));
	}
	
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (beauskunftungDto.getIId() == null) {
				beauskunftungDto.setIId(DelegateFactory.getInstance()
						.getPartnerServicesDelegate()
						.createBeauskunftung(beauskunftungDto));
				setKeyWhenDetailPanel(beauskunftungDto.getIId());
			} else {
				DelegateFactory.getInstance().getPartnerServicesDelegate()
						.updateBeauskunftung(beauskunftungDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						beauskunftungDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			beauskunftungDto = DelegateFactory.getInstance()
					.getPartnerServicesDelegate()
					.beauskunftungFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}
}
