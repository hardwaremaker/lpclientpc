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
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.MeilensteinDto;
import com.lp.server.auftrag.service.ZahlungsplanDto;
import com.lp.server.auftrag.service.ZahlungsplanmeilensteinDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>In diesem Fenster werden Auftragteilnehmer erfasst.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-09-29</p>
 * <p> </p>
 * @author uli walch
 * @version $Revision: 1.4 $
 */
public class PanelZahlungsplan extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAuftrag intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAuftrag tpAuftrag = null;

	private ZahlungsplanDto zahlungsplanDto = null;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private Border innerBorder = null;

	WrapperLabel wlaTermin = new WrapperLabel();
	WrapperDateField wdfTermin = new WrapperDateField();

	WrapperLabel wlaBetrag = new WrapperLabel();
	WrapperNumberField wnfBetrag = new WrapperNumberField();

	WrapperLabel wlaBetragUrsprung = new WrapperLabel();
	WrapperNumberField wnfBetragUrsprung = new WrapperNumberField();
	WrapperLabel wlaWaehrungBetrag = new WrapperLabel();

	static final public String ACTION_SPECIAL_MEILENSTEIN = "action_special_meilenstein";

	static final public String ACTION_SPECIAL_TOGGLE_ERLEDIGT = "action_special_toggle_erledigt";

	WrapperButton wbuMeilenstein = new WrapperButton();
	WrapperTextField wtfMeilenstein = new WrapperTextField();

	WrapperLabel wlaKommentar = new WrapperLabel();
	WrapperTextField wtfKommentar = new WrapperTextField();

	WrapperLabel wlaErledigt = new WrapperLabel();

	WrapperLabel wlaKommentarLang = new WrapperLabel();
	WrapperEditorField wefKommentarLang = new WrapperEditorField(
			getInternalFrame(), "");

	private PanelQueryFLR panelQueryFLRMeilenstein = null;

	void dialogQueryMeilenstein(ActionEvent e) throws Throwable {
		QueryType[] qt = null;
		FilterKriterium[] fk = SystemFilterFactory.getInstance()
				.createFKMandantCNr();

		Integer meilensteinIId = null;
		if (zahlungsplanDto.getZahlungsplanmeilensteinDto() != null) {
			meilensteinIId = zahlungsplanDto.getZahlungsplanmeilensteinDto()
					.getMeilensteinId();
		}

		panelQueryFLRMeilenstein = new PanelQueryFLR(qt, fk,
				QueryParameters.UC_ID_MEILENSTEIN, null, intFrame, 
				LPMain.getTextRespectUISPr("auft.meilenstein"),
				meilensteinIId);

		new DialogQuery(panelQueryFLRMeilenstein);
	}

	public PanelZahlungsplan(InternalFrame internalFrame, String add2TitleI,
			Object key) throws Throwable {
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
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

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

		wbuMeilenstein.setText(LPMain.getTextRespectUISPr("auft.meilenstein"));
		wlaKommentar.setText(LPMain.getTextRespectUISPr("auft.zeitplan.kommentar"));

		wlaKommentarLang.setText(LPMain.getTextRespectUISPr(
				"auft.zeitplan.kommentar"));

		wlaErledigt.setHorizontalAlignment(SwingConstants.LEFT);

		wtfKommentar.setColumnsMax(300);

		wtfMeilenstein.setMandatoryField(true);
		wtfMeilenstein.setActivatable(false);
		wtfMeilenstein.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuMeilenstein.addActionListener(this);
		wbuMeilenstein.setActionCommand(ACTION_SPECIAL_MEILENSTEIN);

		wlaTermin.setText(LPMain.getTextRespectUISPr("auft.zahlungsplan.termin"));
		wlaBetrag.setText(LPMain.getTextRespectUISPr("auft.zahlungsplan.betrag"));

		wlaBetragUrsprung.setText(LPMain.getTextRespectUISPr("auft.zahlungsplan.betrag.ursprung"));

		wdfTermin.setMandatoryField(true);

		wnfBetrag.setMandatoryField(true);
		if (!DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						com.lp.server.benutzer.service.RechteFac.RECHT_AUFT_DARF_AUFTRAG_ERLEDIGEN)) {
			wnfBetragUrsprung.setActivatable(false);
		}

		wlaWaehrungBetrag.setHorizontalAlignment(SwingConstants.LEFT);

		int iZeile = 0;

		jPanelWorkingOn.add(wlaTermin, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 150, 0));
		jPanelWorkingOn.add(wdfTermin, new GridBagConstraints(1, iZeile, 4, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaBetrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfBetrag, new GridBagConstraints(1, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaWaehrungBetrag, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 40, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaBetragUrsprung, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfBetragUrsprung, new GridBagConstraints(1,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wbuMeilenstein, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfMeilenstein, new GridBagConstraints(1, iZeile,
				1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfKommentar, new GridBagConstraints(1, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaKommentarLang, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wefKommentarLang, new GridBagConstraints(1, iZeile,
				1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaErledigt, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		createAndSaveAndShowButton(
				"/com/lp/client/res/document_check16x16.png",
				LPMain.getTextRespectUISPr("auft.zahlungsplanmeilenstein.toggleerledigt"),
				ACTION_SPECIAL_TOGGLE_ERLEDIGT, null);

	}

	private void setDefaults() throws Throwable {
		leereAlleFelder(this);

		wdfTermin.setMaximumValue(tpAuftrag.getAuftragDto().getDFinaltermin());
		wlaWaehrungBetrag.setText(LPMain.getTheClient().getSMandantenwaehrung());
		wlaErledigt.setText("");

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfTermin.getDateEditor().getUiComponent();
	}
	
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRMeilenstein) {
				Integer meilensteinIId = (Integer) ((ISourceEvent) e
						.getSource()).getIdSelected();

				MeilensteinDto meilensteinDto = DelegateFactory.getInstance()
						.getAuftragServiceDelegate()
						.meilensteinFindByPrimaryKey(meilensteinIId);

				wtfMeilenstein.setText(meilensteinDto.getBezeichnung());

				if (zahlungsplanDto.getZahlungsplanmeilensteinDto() == null) {
					zahlungsplanDto
							.setZahlungsplanmeilensteinDto(new ZahlungsplanmeilensteinDto());
				}

				zahlungsplanDto.getZahlungsplanmeilensteinDto()
						.setMeilensteinId(meilensteinIId);
			}
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			if (zahlungsplanDto.getIId() == null) {

				Integer pkTeilnehmer = DelegateFactory.getInstance()
						.getAuftragServiceDelegate()
						.createZahlungsplan(zahlungsplanDto);
				zahlungsplanDto = DelegateFactory.getInstance()
						.getAuftragServiceDelegate()
						.zahlungsplanFindByPrimaryKey(pkTeilnehmer);
				this.setKeyWhenDetailPanel(pkTeilnehmer);

			} else {
				DelegateFactory.getInstance().getAuftragServiceDelegate()
						.updateZahlungsplan(this.zahlungsplanDto);
			}

			// buttons schalten
			super.eventActionSave(e, true);

			eventYouAreSelected(false);

		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		this.resetPanel();

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		super.eventActionUpdate(aE, false);

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		DelegateFactory.getInstance().getAuftragServiceDelegate()
				.removeZahlungsplan(zahlungsplanDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_MEILENSTEIN)) {
			dialogQueryMeilenstein(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_TOGGLE_ERLEDIGT)) {
			if (zahlungsplanDto.getZahlungsplanmeilensteinDto() != null
					&& zahlungsplanDto.getZahlungsplanmeilensteinDto().getIId() != null)
				DelegateFactory
						.getInstance()
						.getAuftragServiceDelegate()
						.toggleZahlungplanmeilensteinErledigt(
								zahlungsplanDto.getZahlungsplanmeilensteinDto()
										.getIId());

			tpAuftrag.getAuftragZahlungsplanTop().eventYouAreSelected(false);

			eventYouAreSelected(false);
		}
	}

	private void erledigenDeaktivierenWennNoetig() throws Throwable {

		boolean bEnable = false;
		if (zahlungsplanDto != null && zahlungsplanDto.getIId() != null) {
			ZahlungsplanmeilensteinDto[] dtos = DelegateFactory
					.getInstance()
					.getAuftragServiceDelegate()
					.zahlungsplanmeilensteinFindByZahlungplanIIdOrderByTErledigt(
							zahlungsplanDto.getIId());

			if (dtos.length == 1) {
				bEnable = true;
			}

		}

		if (this.getHmOfButtons().get(ACTION_SPECIAL_TOGGLE_ERLEDIGT) != null) {

			if (bEnable == true) {
				this.getHmOfButtons().get(ACTION_SPECIAL_TOGGLE_ERLEDIGT)
						.getButton().setEnabled(true);
			} else {
				this.getHmOfButtons().get(ACTION_SPECIAL_TOGGLE_ERLEDIGT)
						.getButton().setEnabled(false);
			}
		}

	}

	private void resetPanel() throws Throwable {
		zahlungsplanDto = new ZahlungsplanDto();
		zahlungsplanDto
				.setZahlungsplanmeilensteinDto(new ZahlungsplanmeilensteinDto());

		leereAlleFelder(this);
		setDefaults();
	}

	private void dto2Components() throws Throwable {
		wlaErledigt.setText("");
		wnfBetrag.setBigDecimal(zahlungsplanDto.getNBetrag());
		wnfBetragUrsprung.setBigDecimal(zahlungsplanDto.getNBetragUrsprung());

		wdfTermin
				.setDate(zahlungsplanDto
						.getTTermin());

		if (zahlungsplanDto.getZahlungsplanmeilensteinDto() != null
				&& zahlungsplanDto.getZahlungsplanmeilensteinDto()
						.getMeilensteinId() != null) {

			MeilensteinDto meilensteinDto = DelegateFactory
					.getInstance()
					.getAuftragServiceDelegate()
					.meilensteinFindByPrimaryKey(
							zahlungsplanDto.getZahlungsplanmeilensteinDto()
									.getMeilensteinId());

			wtfMeilenstein.setText(meilensteinDto.getBezeichnung());

			wtfKommentar.setText(zahlungsplanDto
					.getZahlungsplanmeilensteinDto().getCKommentar());
			wefKommentarLang.setText(zahlungsplanDto
					.getZahlungsplanmeilensteinDto().getXText());

			ZahlungsplanmeilensteinDto[] dtos = DelegateFactory
					.getInstance()
					.getAuftragServiceDelegate()
					.zahlungsplanmeilensteinFindByZahlungplanIIdOrderByTErledigt(
							zahlungsplanDto.getIId());

			if (dtos.length > 0) {

				for (int i = 0; i < dtos.length; i++) {
					if (dtos[i].getTErledigt() != null
							&& dtos[i].getPersonalIIdErledigt() != null) {
						PersonalDto personalDtoVerrechnen = DelegateFactory
								.getInstance()
								.getPersonalDelegate()
								.personalFindByPrimaryKey(
										dtos[i].getPersonalIIdErledigt());

						wlaErledigt
								.setText(LPMain
										.getTextRespectUISPr("auft.zahlungsplanmeilenstein.erledigtam")
										+ " "
										+ Helper.formatTimestamp(dtos[i]
												.getTErledigt(), LPMain
												.getTheClient().getLocUi())
										+ ", "
										+ personalDtoVerrechnen.formatAnrede());
						break;
					}
				}

			}
		}

	}

	private void components2Dto() throws Throwable {
		if (zahlungsplanDto.getIId() == null) {
			zahlungsplanDto.setAuftragIId(tpAuftrag.getAuftragDto().getIId());

		}

		zahlungsplanDto.setTTermin(wdfTermin.getTimestamp());

		zahlungsplanDto.setNBetrag(wnfBetrag.getBigDecimal());
		zahlungsplanDto.setNBetragUrsprung(wnfBetragUrsprung.getBigDecimal());

		if (zahlungsplanDto.getZahlungsplanmeilensteinDto() == null) {
			zahlungsplanDto
					.setZahlungsplanmeilensteinDto(new ZahlungsplanmeilensteinDto());
		}

		zahlungsplanDto.getZahlungsplanmeilensteinDto().setZahlungsplaniId(
				zahlungsplanDto.getIId());
		zahlungsplanDto.getZahlungsplanmeilensteinDto().setCKommentar(
				wtfKommentar.getText());
		zahlungsplanDto.getZahlungsplanmeilensteinDto().setXText(
				wefKommentarLang.getText());

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		// teilnehmer neu einlesen, ausloeser war ev. ein refresh
		Object oKey = getKeyWhenDetailPanel();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			zahlungsplanDto = DelegateFactory.getInstance()
					.getAuftragServiceDelegate()
					.zahlungsplanFindByPrimaryKey((Integer) oKey);
			dto2Components();

		}

		tpAuftrag.setTitleAuftrag(LPMain.getTextRespectUISPr("auft.title.panel.teilnehmer"));

		aktualisiereStatusbar();

		erledigenDeaktivierenWennNoetig();

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
					|| tpAuftrag.getAuftragDto().getStatusCNr()
							.equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				lsv = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lsv;
	}
}
