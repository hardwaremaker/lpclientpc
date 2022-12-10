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
package com.lp.client.personal;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperPasswordField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.ExcFactory;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.VersandFac;
import com.lp.util.Helper;

//@SuppressWarnings("static-access")
public class PanelPersonalparameter extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PersonalDto personalDto = null;
	private InternalFramePersonal internalFramePersonal = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperCheckBox wcbKeineAnzeigeAmTerminal = new WrapperCheckBox();
	private WrapperCheckBox wcbKeineAnzeigeInAnwesenheitsliste = new WrapperCheckBox();
	private WrapperCheckBox wcbProjektStartMitMeinenOffenen = new WrapperCheckBox();
	private WrapperCheckBox wcbBekommtUeberstundenausbezahlt = new WrapperCheckBox();
	private WrapperCheckBox wcbAnwesenheitslisteTermial = new WrapperCheckBox();
	private WrapperCheckBox wcbAnwesenheitslisteAlleTermial = new WrapperCheckBox();
	private WrapperCheckBox wcbKommtAmTerminal = new WrapperCheckBox();

	private WrapperCheckBox wcbTelefonzeitStarten = null;

	private WrapperCheckBox wcbWEPInfo = new WrapperCheckBox();

	private WrapperLabel wlaImapBenutzer = new WrapperLabel();
	private WrapperTextField wtfImapBenutzer = new WrapperTextField();

	private WrapperLabel wlaImapKennwort = new WrapperLabel();
	private WrapperPasswordField wtfImapKennwort = new WrapperPasswordField();

	private WrapperLabel wlaVersandKennwort = new WrapperLabel();
	private WrapperPasswordField wtfVersandKennwort = new WrapperPasswordField();

	private WrapperLabel wlaImapInboxFolder = new WrapperLabel();
	private WrapperTextField wtfImapInboxFolder = new WrapperTextField();

	private WrapperLabel wlaBccEmpfaenger = new WrapperLabel();
	private WrapperTextField wtfBccEmpfaenger = new WrapperTextField(VersandFac.MAX_BCCEMPFAENGER);

	private WrapperCheckBox wcbSynchronisiereAlleKontakte = new WrapperCheckBox();

	public PanelPersonalparameter(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFramePersonal) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_PERSONAL;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcbKeineAnzeigeInAnwesenheitsliste;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		leereAlleFelder(this);
		super.eventYouAreSelected(false);
		personalDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(((InternalFramePersonal) getInternalFrame()).getPersonalDto().getIId());
		dto2Components();
	}

	private void jbInit() throws Throwable {
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wcbKeineAnzeigeInAnwesenheitsliste
				.setText(LPMain.getTextRespectUISPr("pers.personaldaten.keineanzeigeinanwesenheitsliste"));

		wcbKeineAnzeigeAmTerminal.setText(LPMain.getTextRespectUISPr("pers.personaldaten.keineanzeigeamterminal"));

		wcbWEPInfo.setText(LPMain.getTextRespectUISPr("pers.wepinfo.generell"));

		wcbProjektStartMitMeinenOffenen
				.setText(LPMain.getTextRespectUISPr("pers.personaldaten.projektstart.mitmeinenoffenen"));

		wcbKommtAmTerminal.setText(LPMain.getTextRespectUISPr("pers.personaldaten.kommtamterminal"));

		wcbBekommtUeberstundenausbezahlt
				.setText(LPMain.getTextRespectUISPr("pers.personaldaten.keineueberstundenauszahlung"));
		wcbAnwesenheitslisteTermial.setText(LPMain.getTextRespectUISPr("pers.personaldaten.anwesenheitslisteterminal"));
		wcbAnwesenheitslisteAlleTermial
				.setText(LPMain.getTextRespectUISPr("pers.personaldaten.anwesenheitslistealleterminal"));

		wtfImapBenutzer.setColumnsMax(80);

		wcbTelefonzeitStarten = new WrapperCheckBox(LPMain.getTextRespectUISPr("pers.telefonzeitstarten"));

		wlaImapBenutzer.setText(LPMain.getTextRespectUISPr("pers.imapuser"));
		wlaImapKennwort.setText(LPMain.getTextRespectUISPr("pers.imapkennwort"));

		wlaVersandKennwort.setText(LPMain.getTextRespectUISPr("pers.versandkennwort"));

		wlaImapInboxFolder.setText(LPMain.getTextRespectUISPr("pers.imapinboxfolder"));
		wtfImapInboxFolder.setColumnsMax(80);
		wlaBccEmpfaenger.setText(LPMain.getTextRespectUISPr("pers.personaldaten.bccempfaenger"));

		wcbSynchronisiereAlleKontakte.setText(LPMain.getTextRespectUISPr("pers.personaldaten.synchallekontakte"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile = 0;
		jpaWorkingOn.add(wcbKeineAnzeigeInAnwesenheitsliste, new GridBagConstraints(0, iZeile, 2, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaImapBenutzer, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfImapBenutzer, new GridBagConstraints(3, iZeile, 2, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbKeineAnzeigeAmTerminal, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaImapKennwort, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfImapKennwort, new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbBekommtUeberstundenausbezahlt, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaImapInboxFolder, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfImapInboxFolder, new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		++iZeile;
		jpaWorkingOn.add(wcbTelefonzeitStarten, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaVersandKennwort, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVersandKennwort, new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbProjektStartMitMeinenOffenen, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 20, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbAnwesenheitslisteAlleTermial, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBccEmpfaenger, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBccEmpfaenger, new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbAnwesenheitslisteTermial, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbKommtAmTerminal, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbWEPInfo, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbSynchronisiereAlleKontakte, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		// iZeile++;
		// jpaWorkingOn.add(wcbTelefonzeitStarten, new GridBagConstraints(0,
		// iZeile, 2,
		// 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
		// new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void setDefaults() throws Throwable {

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		wcbBekommtUeberstundenausbezahlt.setShort(personalDto.getBUeberstundenausbezahlt());

		if (personalDto.getBAnwesenheitsliste().intValue() == 0) {
			wcbKeineAnzeigeInAnwesenheitsliste.setShort(new Short((short) 1));
		} else {
			wcbKeineAnzeigeInAnwesenheitsliste.setShort(new Short((short) 0));
		}
		wcbKeineAnzeigeAmTerminal.setShort(personalDto.getBKeineAnzeigeAmTerminal());

		wcbWEPInfo.setShort(personalDto.getBWepInfo());

		wcbTelefonzeitStarten.setShort(personalDto.getBTelefonzeitstarten());

		wcbProjektStartMitMeinenOffenen.setShort(personalDto.getBStartMitMeinenOffenenProjekten());
		wcbKommtAmTerminal.setShort(personalDto.getBKommtAmTerminal());

		wcbAnwesenheitslisteTermial.setShort(personalDto.getBAnwesenheitTerminal());
		wcbAnwesenheitslisteAlleTermial.setShort(personalDto.getBAnwesenheitalleterminal());
		wtfBccEmpfaenger.setText(personalDto.getCBccempfaenger());

		wtfImapBenutzer.setText(personalDto.getCImapbenutzer());

		wtfImapKennwort.setText("**********");

		if (personalDto.getCVersandkennwort() != null) {
			wtfVersandKennwort.setText("**********");
		}

		if (personalDto.getCImapInboxFolder() != null) {
			wtfImapInboxFolder.setText(personalDto.getCImapInboxFolder());
		} else {
			wtfImapInboxFolder.setText("");
		}

		wcbSynchronisiereAlleKontakte.setShort(personalDto.getbSynchronisiereAlleKontakte());

		this.setStatusbarPersonalIIdAendern(personalDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(personalDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(personalDto.getTAnlegen());
		this.setStatusbarTAendern(personalDto.getTAendern());
	}

	protected void components2Dto() throws Throwable {

		personalDto.setBUeberstundenausbezahlt(wcbBekommtUeberstundenausbezahlt.getShort());

		personalDto.setCImapbenutzer(wtfImapBenutzer.getText());
		if (!new String(wtfImapKennwort.getPassword()).equals("**********")) {
			personalDto.setCImapkennwort(new String(wtfImapKennwort.getPassword()));

		}

		if (!new String(wtfVersandKennwort.getPassword()).equals("**********")) {
			
			String passwort=new String(wtfVersandKennwort.getPassword());
			
			if(passwort==null || passwort.length()==0) {
				personalDto.setCVersandkennwort(null);
			}else {
				personalDto.setCVersandkennwort(new String(wtfVersandKennwort.getPassword()));
			}
			
			
		}

		if (wcbKeineAnzeigeInAnwesenheitsliste.getShort().intValue() == 0) {
			personalDto.setBAnwesenheitsliste(new Short((short) 1));
		} else {
			personalDto.setBAnwesenheitsliste(new Short((short) 0));
		}

		personalDto.setBWepInfo(wcbWEPInfo.getShort());

		personalDto.setBKeineAnzeigeAmTerminal(wcbKeineAnzeigeAmTerminal.getShort());

		personalDto.setBAnwesenheitTerminal(wcbAnwesenheitslisteTermial.getShort());
		personalDto.setBAnwesenheitalleterminal(wcbAnwesenheitslisteAlleTermial.getShort());

		personalDto.setBTelefonzeitstarten(wcbTelefonzeitStarten.getShort());

		personalDto.setBStartMitMeinenOffenenProjekten(wcbProjektStartMitMeinenOffenen.getShort());
		personalDto.setBKommtAmTerminal(wcbKommtAmTerminal.getShort());
		personalDto.setCBccempfaenger(wtfBccEmpfaenger.getText());

		// Kommunikationsdaten

		personalDto.setCImapInboxFolder(wtfImapInboxFolder.getText());

		personalDto.setbSynchronisiereAlleKontakte(wcbSynchronisiereAlleKontakte.getShort());
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (!super.allMandatoryFieldsSetDlg())
			return;

		if (!Helper.isStringEmpty(wtfBccEmpfaenger.getText())
				&& !Helper.validateEmailadresse(wtfBccEmpfaenger.getText())) {
			throw ExcFactory.ungueltigeEmailadresse(wtfBccEmpfaenger.getText());
		}

		components2Dto();
		DelegateFactory.getInstance().getPersonalDelegate().updatePersonal(personalDto);
		super.eventActionSave(e, true);
	}

}
