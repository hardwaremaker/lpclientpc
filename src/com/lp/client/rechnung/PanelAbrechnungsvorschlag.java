
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
package com.lp.client.rechnung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.service.AbrechnungsvorschlagDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich ...</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>18.02.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author josef erlinger
 * @version $Revision: 1.19 $
 */
public class PanelAbrechnungsvorschlag extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// wird benoetigt in der eventActionSave initialisiert wird es im
	// Konstruktor
	private AbrechnungsvorschlagDto abrechnungsvorschlagDto = null;

	// von hier ...
	private JPanel jpaWorkingOn = null;
	private JPanel panelButtonAction = null;

	private WrapperGotoButton wbuTelefonzeitenGoto = new WrapperGotoButton(GotoHelper.GOTO_ZEITERFASSUNG_TELEFONZEITEN);
	private WrapperTextField wtfTelefonzeiten = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	private WrapperGotoButton wbuReiseGoto = new WrapperGotoButton(GotoHelper.GOTO_ZEITERFASSUNG_REISE);
	private WrapperTextField wtfReise = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	private WrapperGotoButton wbuMaschinenzeitdatenGoto = new WrapperGotoButton(GotoHelper.GOTO_MASCHINEN_ZEITDATEN);
	private WrapperTextField wtfMaschinenzeitdaten = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	private WrapperGotoButton wbuAuftragGoto = new WrapperGotoButton(GotoHelper.GOTO_AUFTRAG_AUSWAHL);
	private WrapperTextField wtfAuftrag = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	private WrapperGotoButton wbuLosGoto = new WrapperGotoButton(GotoHelper.GOTO_FERTIGUNG_AUSWAHL);
	private WrapperTextField wtfLos = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	private WrapperGotoButton wbuZeitdatenGoto = new WrapperGotoButton(GotoHelper.GOTO_ZEITERFASSUNG_ZEITDATEN);
	private WrapperTextField wtfZeitdaten = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
	
	private WrapperGotoButton wbuKundeGoto = new WrapperGotoButton(GotoHelper.GOTO_KUNDE_AUSWAHL);
	private WrapperTextField wtfKunde = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	private WrapperGotoButton wbuProjektGoto = new WrapperGotoButton(GotoHelper.GOTO_PROJEKT_AUSWAHL);
	private WrapperTextField wtfProjekt = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	public PanelAbrechnungsvorschlag(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		// initialisieren von TabbedPaneBestellung
		jbInit();
		initComponents();
	}

	private AbrechnungsvorschlagDto getAbrechnungsvorschlagDto() {
		return abrechnungsvorschlagDto;
	}

	private void setAbrechnungsvorschlagDto(AbrechnungsvorschlagDto bestellpositionDto) {
		this.abrechnungsvorschlagDto = bestellpositionDto;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected PropertyVetoException eventActionVetoableChangeLP() throws Throwable {
		PropertyVetoException pve = super.eventActionVetoableChangeLP();
		DelegateFactory.getInstance().getRechnungDelegate().removeLockDesAbrechungsvorschlagesWennIchIhnSperre();
		return pve;
	}
	
	private InternalFrameRechnung getInternalFrameRechnung() {
		return (InternalFrameRechnung) getInternalFrame();
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI) throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
	}

	/**
	 * Verwerfen der aktuelle Usereingabe und zurueckgehen auf den bestehenden
	 * Datensatz, wenn einer existiert.
	 * 
	 * @param e Ereignis
	 * @throws Throwable
	 */
	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		super.eventActionDelete(e, true, false);

	}

	protected void components2Dto() throws Throwable {

	}

	protected void dto2Components() throws Throwable {

		if (abrechnungsvorschlagDto.getAuftragIId() != null) {
			AuftragDto aDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey(abrechnungsvorschlagDto.getAuftragIId());
			wbuAuftragGoto.setOKey(abrechnungsvorschlagDto.getAuftragIId());
			wtfAuftrag.setText(aDto.getCNr());
		}
		if (abrechnungsvorschlagDto.getLosIId() != null) {
			LosDto lDto = DelegateFactory.getInstance().getFertigungDelegate()
					.losFindByPrimaryKey(abrechnungsvorschlagDto.getLosIId());
			wbuLosGoto.setOKey(abrechnungsvorschlagDto.getLosIId());
			wtfLos.setText(lDto.getCNr());
		}
		
		if (abrechnungsvorschlagDto.getKundeIId() != null) {
			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
					.kundeFindByPrimaryKey(abrechnungsvorschlagDto.getKundeIId());
			wbuKundeGoto.setOKey(abrechnungsvorschlagDto.getKundeIId());
			wtfKunde.setText(kundeDto.getPartnerDto().formatFixName1Name2());
		}

		if (abrechnungsvorschlagDto.getProjektIId() != null) {
			ProjektDto pjDto = DelegateFactory.getInstance().getProjektDelegate()
					.projektFindByPrimaryKey(abrechnungsvorschlagDto.getProjektIId());
			wbuProjektGoto.setOKey(abrechnungsvorschlagDto.getProjektIId());
			wtfProjekt.setText(pjDto.getCNr());
		}

		if (abrechnungsvorschlagDto.getZeitdatenIId() != null) {
			ZeitdatenDto zDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.zeitdatenFindByPrimaryKey(abrechnungsvorschlagDto.getZeitdatenIId());
			wbuZeitdatenGoto.setOKey(abrechnungsvorschlagDto.getZeitdatenIId());
			wtfZeitdaten.setText(Helper.formatTimestamp(zDto.getTZeit(), LPMain.getTheClient().getLocUi()));

		}

		if (abrechnungsvorschlagDto.getTelefonzeitenIId() != null) {
			TelefonzeitenDto zDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.telefonzeitenFindByPrimaryKey(abrechnungsvorschlagDto.getTelefonzeitenIId());
			wbuTelefonzeitenGoto.setOKey(abrechnungsvorschlagDto.getTelefonzeitenIId());
			wtfTelefonzeiten.setText(Helper.formatTimestamp(zDto.getTVon(), LPMain.getTheClient().getLocUi()));
		}

		if (abrechnungsvorschlagDto.getMaschinenzeitdatenIId() != null) {
			MaschinenzeitdatenDto zDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.maschinenzeitdatenFindByPrimaryKey(abrechnungsvorschlagDto.getMaschinenzeitdatenIId());
			wbuMaschinenzeitdatenGoto.setOKey(abrechnungsvorschlagDto.getMaschinenzeitdatenIId());
			wtfMaschinenzeitdaten.setText(Helper.formatTimestamp(zDto.getTVon(), LPMain.getTheClient().getLocUi()));
		}

		if (abrechnungsvorschlagDto.getReiseIId() != null) {
			ReiseDto zDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.reiseFindByPrimaryKey(abrechnungsvorschlagDto.getReiseIId());
			wbuReiseGoto.setOKey(abrechnungsvorschlagDto.getReiseIId());
			wtfReise.setText(Helper.formatTimestamp(zDto.getTZeit(), LPMain.getTheClient().getLocUi()));
		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
		
			
			

			super.eventActionSave(e, true);
			eventYouAreSelected(false);

		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		// nothing here
	}

	private void jbInit() throws Throwable {

		wbuTelefonzeitenGoto.setText(LPMain.getTextRespectUISPr("pers.telefonzeiten"));

		wbuAuftragGoto.setText(LPMain.getTextRespectUISPr("auft.auftrag"));
		wbuAuftragGoto.setActivatable(false);

		wbuLosGoto.setText(LPMain.getTextRespectUISPr("fert.tab.unten.los.title"));
		wbuLosGoto.setActivatable(false);

		
		wbuKundeGoto.setText(LPMain.getTextRespectUISPr("label.kunde"));
		wbuKundeGoto.setActivatable(false);

		wbuZeitdatenGoto.setText(LPMain.getTextRespectUISPr("zeiterfassung.title.tab.zeitdaten"));
		wbuZeitdatenGoto.setActivatable(false);

		wbuProjektGoto.setText(LPMain.getTextRespectUISPr("label.projekt"));
		wbuProjektGoto.setActivatable(false);

		wbuReiseGoto.setText(LPMain.getTextRespectUISPr("pers.reisezeiten"));
		wbuReiseGoto.setActivatable(false);

		wbuMaschinenzeitdatenGoto.setText(LPMain.getTextRespectUISPr("pers.maschinenzeit"));
		wbuMaschinenzeitdatenGoto.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);

		//String[] aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD, };

		//enableToolsPanelButtons(aWhichButtonIUse);

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// das Aussenpanel hat immer das Gridbaglayout.
		setLayout(new GridBagLayout());

		// Actionpanel von Oberklasse holen und anhaengen.

		/*panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
*/
		wbuTelefonzeitenGoto.setMinimumSize(new Dimension(120, Defaults.getInstance().getControlHeight()));
		wbuTelefonzeitenGoto.setPreferredSize(new Dimension(120, Defaults.getInstance().getControlHeight()));
		wbuTelefonzeitenGoto.setMinimumSize(new Dimension(120, Defaults.getInstance().getControlHeight()));

		wtfTelefonzeiten.setActivatable(false);
		wtfAuftrag.setActivatable(false);
		wtfLos.setActivatable(false);
		wtfProjekt.setActivatable(false);
		wtfZeitdaten.setActivatable(false);
		wtfTelefonzeiten.setActivatable(false);
		wtfReise.setActivatable(false);
		wtfMaschinenzeitdaten.setActivatable(false);

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		jpaWorkingOn.setLayout(new GridBagLayout());
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		// ab hier einhaengen.
		// Zeile.
		jpaWorkingOn.add(wbuAuftragGoto, new GridBagConstraints(0, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAuftrag, new GridBagConstraints(1, iZeile, 1, 1, 2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuLosGoto, new GridBagConstraints(2, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLos, new GridBagConstraints(3, iZeile, 1, 1, 2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuProjektGoto, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfProjekt, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuKundeGoto, new GridBagConstraints(2, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde, new GridBagConstraints(3, iZeile, 1, 1, 2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuZeitdatenGoto, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfZeitdaten, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuTelefonzeitenGoto, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfTelefonzeiten, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuReiseGoto, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfReise, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuMaschinenzeitdatenGoto, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfMaschinenzeitdaten, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ABRECHNUNGSVORSCHLAG;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		leereAlleFelder(this);
		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();

		if (key != null && !key.equals(LPMain.getLockMeForNew())) {
			setAbrechnungsvorschlagDto(DelegateFactory.getInstance().getRechnungDelegate()
					.abrechnungsvorschlagFindByPrimaryKey((Integer) key));

			dto2Components();
			setStatusbar();

		/*	if (getAbrechnungsvorschlagDto().getZeitdatenIId() != null) {

				ZeitdatenDto zDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.zeitdatenFindByPrimaryKey(getAbrechnungsvorschlagDto().getZeitdatenIId());

				if (zDto.getCBelegartnr() != null) {

					if (zDto.getCBelegartnr().equals(LocaleFac.BELEGART_AUFTRAG)) {
						wbuAuftragGoto.setActivatable(true);
					} else if (zDto.getCBelegartnr().equals(LocaleFac.BELEGART_LOS)) {
						wbuLosGoto.setActivatable(true);
					} else if (zDto.getCBelegartnr().equals(LocaleFac.BELEGART_PROJEKT)) {
						wbuProjektGoto.setActivatable(true);
					}

				}

			} else {
				getHmOfButtons().get(PanelBasis.ACTION_UPDATE).getButton().setEnabled(false);
			}*/

		}

	}

	// hier erfolgt setting der statusbar (lt. felder in der db)
	protected void setStatusbar() throws Throwable {

		setStatusbarTAnlegen(abrechnungsvorschlagDto.getTAnlegen());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuAuftragGoto;
	}
}
