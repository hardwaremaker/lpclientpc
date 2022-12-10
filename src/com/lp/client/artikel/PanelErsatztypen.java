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
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelshopgruppeDto;
import com.lp.server.artikel.service.ErsatztypenDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.util.GotoHelper;

public class PanelErsatztypen extends PanelBasis {

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
	private InternalFrameArtikel internalFrameArtikel = null;
	private ErsatztypenDto ersatztypenDto = null;

	private PanelQueryFLR panelQueryFLRArtikel = null;
	private WrapperGotoButton wbuArtikel = new WrapperGotoButton(GotoHelper.GOTO_ARTIKEL_AUSWAHL);
	private WrapperTextField wtfArtikel = new WrapperTextField();

	static final public String ACTION_SPECIAL_ARTIKEL_FROM_LISTE = "action_artikel_from_liste";

	public PanelErsatztypen(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuArtikel;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		ersatztypenDto = new ErsatztypenDto();
	}

	protected void dto2Components() throws Throwable {

		wbuArtikel.setOKey(ersatztypenDto.getArtikelIIdErsatz());
		
		if (ersatztypenDto.getArtikelIIdErsatz() != null) {
			wtfArtikel.setText(DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							ersatztypenDto.getArtikelIIdErsatz())
					.formatArtikelbezeichnung());
		} else {
			wtfArtikel.setText(null);
		}

	}

	protected void components2Dto() {
		ersatztypenDto.setArtikelIId(internalFrameArtikel.getArtikelDto()
				.getIId());
		
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRArtikel) {
		        Object key = ( (ISourceEvent) e.getSource()).getIdSelected();
		        ArtikelDto artikelTempDto = DelegateFactory.getInstance().getArtikelDelegate().
		            artikelFindByPrimaryKey( (Integer)
		                                    key);
		        if (artikelTempDto.getIId().equals(internalFrameArtikel.getArtikelDto().
		                                           getIId())) {
		          DialogFactory.showModalDialog(LPMain.getTextRespectUISPr(
		              "lp.error"),
		                                               LPMain.getTextRespectUISPr(
		              "artikel.error.kannnichtselbstzugeordnetwerden"));
		        }
		        else {
		          wtfArtikel.setText(artikelTempDto.
		              formatArtikelbezeichnung());
		          ersatztypenDto.setArtikelIIdErsatz(artikelTempDto.getIId());
		        }
		      }
		}
	}

	void dialogQueryArtikelFromListe(ActionEvent e) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
				PanelBasis.ACTION_LEEREN };
		panelQueryFLRArtikel = new PanelQueryFLR(
				null,
				ArtikelFilterFactory.getInstance().createFKArtikelliste(),
				com.lp.server.util.fastlanereader.service.query.QueryParameters.UC_ID_ARTIKELLISTE,
				aWhichButtonIUse, getInternalFrame(), LPMain
						.getTextRespectUISPr("title.artikelauswahlliste"),
				ArtikelFilterFactory.getInstance().createFKVArtikel(), null);

		panelQueryFLRArtikel.setFilterComboBox(DelegateFactory.getInstance()
				.getArtikelDelegate().getAllSprArtgru(), new FilterKriterium(
				"ag.i_id", true, "" + "", FilterKriterium.OPERATOR_IN, false),
				false, LPMain.getTextRespectUISPr("lp.alle"), false);

		panelQueryFLRArtikel.befuellePanelFilterkriterienDirekt(
				ArtikelFilterFactory.getInstance().createFKDArtikelnummer(
						internalFrameArtikel), ArtikelFilterFactory
						.getInstance().createFKDVolltextsuche());
		panelQueryFLRArtikel.setSelectedId(ersatztypenDto
				.getArtikelIIdErsatz());
		new DialogQuery(panelQueryFLRArtikel);
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

		wbuArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.artikel"));

		wbuArtikel.setActionCommand(ACTION_SPECIAL_ARTIKEL_FROM_LISTE);
		wbuArtikel.addActionListener(this);

		wtfArtikel.setMandatoryField(true);
		wtfArtikel.setActivatable(false);
		wtfArtikel.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);

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
		jpaWorkingOn.add(wbuArtikel, new GridBagConstraints(0, 0, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(1, 0, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKEL_FROM_LISTE)) {
		      dialogQueryArtikelFromListe(e);
		    }
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable, Throwable {
		DelegateFactory.getInstance().getArtikelDelegate()
				.removeErsatztypen(ersatztypenDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (internalFrameArtikel.getTabbedPaneArtikel().bArtikelfreigabe == true) {
			// Wenn Fregegeben, dann nicht mehr aenderbar
			if (internalFrameArtikel.getArtikelDto() != null && internalFrameArtikel.getArtikelDto().getTFreigabe() != null) {
				lockStateValue = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			ersatztypenDto = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.ersatztypenFindByPrimaryKey((Integer) key);
			dto2Components();
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (ersatztypenDto.getIId() == null) {
				ersatztypenDto.setIId(DelegateFactory.getInstance()
						.getArtikelDelegate()
						.createErsatztypen(ersatztypenDto));
				setKeyWhenDetailPanel(ersatztypenDto.getIId());
			} else {
				DelegateFactory.getInstance().getArtikelDelegate()
						.updateErsatztypen(ersatztypenDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameArtikel.getArtikelDto().getIId()
								.toString());
			}
			eventYouAreSelected(false);
		}
	}
}
