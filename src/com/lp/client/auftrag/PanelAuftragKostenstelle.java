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
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

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
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragkostenstelleDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * In diesem Fenster werden Auftragteilnehmer erfasst.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 2004-09-29
 * </p>
 * <p>
 * </p>
 * 
 * @author uli walch
 * @version $Revision: 1.4 $
 */
public class PanelAuftragKostenstelle extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAuftrag intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAuftrag tpAuftrag = null;

	/** Aktueller Teilnehmer. */
	private AuftragkostenstelleDto auftragkostenstelleDto = null;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private Border innerBorder = null;

	private static final String ACTION_SPECIAL_KOSTENSTELLE = "action_special_kostenstelle";

	private WrapperButton wbuKostenstelle = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;
	private WrapperTextField wtfKostenstelle = null;

	public PanelAuftragKostenstelle(InternalFrame internalFrame,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameAuftrag) internalFrame;
		tpAuftrag = intFrame.getTabbedPaneAuftrag();

		jbInit();
		setDefaults();
		initComponents();
	}

	void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setBorder(innerBorder);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD,
				PanelBasis.ACTION_DELETE };
		enableToolsPanelButtons(aWhichButtonIUse);

		wbuKostenstelle = new WrapperButton(
				LPMain.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.setMandatoryField(true);
		wbuKostenstelle.addActionListener(this);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);

		wtfKostenstelle = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
		wtfKostenstelle.setActivatable(false);
		wtfKostenstelle.setMandatoryField(true);

		// Workingpanel
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		jPanelWorkingOn.add(wbuKostenstelle, new GridBagConstraints(0, 0, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfKostenstelle, new GridBagConstraints(1, 0, 4, 1,
				0.6, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	private void setDefaults() throws Throwable {
		leereAlleFelder(this);

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKostenstelle) {
				Integer iIdKostenstelle = (Integer) (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();
				KostenstelleDto kostenstelleDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.kostenstelleFindByPrimaryKey(iIdKostenstelle);
				wtfKostenstelle.setText(kostenstelleDto
						.formatKostenstellenbezeichnung());
				auftragkostenstelleDto.setKostenstelleIId(kostenstelleDto
						.getIId());

			}
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (auftragkostenstelleDto.getIId() == null) {

				Integer pk = DelegateFactory.getInstance()
						.getAuftragServiceDelegate()
						.createAuftragkostenstelle(auftragkostenstelleDto);
				auftragkostenstelleDto = DelegateFactory.getInstance()
						.getAuftragServiceDelegate()
						.auftragkostenstelleFindByPrimaryKey(pk);
				this.setKeyWhenDetailPanel(pk);

			} else {
				DelegateFactory.getInstance().getAuftragServiceDelegate()
						.updateAuftragkostenstelle(this.auftragkostenstelleDto);
			}

			// buttons schalten
			super.eventActionSave(e, true);

			eventYouAreSelected(false);

		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		if (tpAuftrag.istAktualisierenAuftragErlaubt()) {

			super.eventActionNew(eventObject, true, false);

			this.resetPanel();

		} else {
			tpAuftrag.getAuftragKostenstelleTop().updateButtons(
					tpAuftrag.getAuftragKostenstelleBottom()
							.getLockedstateDetailMainKey());
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (tpAuftrag.istAktualisierenAuftragErlaubt()) {
			super.eventActionUpdate(aE, false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (tpAuftrag.istAktualisierenAuftragErlaubt()) {
			DelegateFactory.getInstance().getAuftragServiceDelegate()
					.removeAuftragkostenstelle(auftragkostenstelleDto);
			this.setKeyWhenDetailPanel(null);
			super.eventActionDelete(e, false, false); // keyWasForLockMe nicht
														// ueberschreiben
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		}
	}

	private void dialogQueryKostenstelle(ActionEvent e) throws Throwable {
		
		
		FilterKriterium filter[] = new FilterKriterium[2];
		filter[0] = new FilterKriterium("mandant_c_nr", true, "'" + tpAuftrag.getAuftragDto().getMandantCNr()
				+ "'", FilterKriterium.OPERATOR_EQUAL, false);
		
		filter[1] = new FilterKriterium("i_id", true,
				"("+tpAuftrag.getAuftragDto().getKostIId()+")",
				FilterKriterium.OPERATOR_NOT + " "
						+ FilterKriterium.OPERATOR_IN, false);
		
		
		
		panelQueryFLRKostenstelle = new PanelQueryFLR(
			 SystemFilterFactory.getInstance().createQTKostenstelle(), filter,
				QueryParameters.UC_ID_KOSTENSTELLE, null,
				getInternalFrame(), LPMain.getTextRespectUISPr(
						"title.kostenstelleauswahlliste"));

		panelQueryFLRKostenstelle
				.befuellePanelFilterkriterienDirektUndVersteckte(
						 SystemFilterFactory.getInstance().createFKDKostenstelleNummer(),
						 SystemFilterFactory.getInstance().createFKDKostenstelleBezeichnung(),
						 SystemFilterFactory.getInstance().createFKVKostenstelle());
		

		new DialogQuery(panelQueryFLRKostenstelle);
	}

	private void resetPanel() throws Throwable {
		auftragkostenstelleDto = new AuftragkostenstelleDto();

		leereAlleFelder(this);
		setDefaults();
	}

	private void dto2Components() throws Throwable {

		KostenstelleDto kostenstelleDto = DelegateFactory
				.getInstance()
				.getSystemDelegate()
				.kostenstelleFindByPrimaryKey(
						auftragkostenstelleDto.getKostenstelleIId());
		wtfKostenstelle.setText(kostenstelleDto
				.formatKostenstellenbezeichnung());

	}

	private void components2Dto() throws ExceptionLP {
		if (auftragkostenstelleDto.getIId() == null) {
			auftragkostenstelleDto.setAuftragIId(tpAuftrag.getAuftragDto()
					.getIId());

		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// teilnehmer neu einlesen, ausloeser war ev. ein refresh
		Object oKey = getKeyWhenDetailPanel();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			auftragkostenstelleDto = DelegateFactory.getInstance()
					.getAuftragServiceDelegate()
					.auftragkostenstelleFindByPrimaryKey((Integer) oKey);
			dto2Components();

		}

		tpAuftrag.setTitleAuftrag(LPMain
				.getTextRespectUISPr("auft.kostenstellen"));

		aktualisiereStatusbar();
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAuftrag.getAuftragDto()
				.getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAuftrag.getAuftragDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAuftrag.getAuftragDto()
				.getPersonalIIdAendern());
		setStatusbarTAendern(tpAuftrag.getAuftragDto().getTAendern());
		setStatusbarStatusCNr(tpAuftrag.getAuftragStatus());
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_AUFTRAG;
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockStateValue lsv = super.getLockedstateDetailMainKey();

		if (tpAuftrag.getAuftragDto().getIId() != null) {
			if (tpAuftrag.getAuftragDto().getStatusCNr()
					.equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)
					|| tpAuftrag
							.getAuftragDto()
							.getStatusCNr()
							.equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)
					|| tpAuftrag.getAuftragDto().getStatusCNr()
							.equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				lsv = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lsv;
	}
}
