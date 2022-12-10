
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
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.projekt.TabbedPaneProjekt;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LostechnikerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ProjektzeitenDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.projekt.service.ProjekttechnikerDto;

@SuppressWarnings("static-access")
/*
 * <p>Panel zum Bearbeiten der Klassen eines Loses</p> <p>Copyright Logistik Pur
 * Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum <I>24. 10. 2005</I></p>
 * <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.4 $
 */
public class PanelZeitnachweis extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneZeitnachweis tabbedPaneProjekt = null;

	private ProjektzeitenDto projektzeitenDto = null;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private Border border1;

	private WrapperLabel wlaKommentar = null;
	private WrapperLabel wlaKommentarIntern = null;

	private WrapperEditorField wefKommentar = new WrapperEditorFieldKommentar(getInternalFrame(),
			LPMain.getTextRespectUISPr("lp.bemerkung"));

	private WrapperEditorField wefKommentarIntern = new WrapperEditorFieldKommentar(getInternalFrame(),
			LPMain.getTextRespectUISPr("lp.bemerkung"));
	private WrapperLabel wlaDauerUebersteuert = null;
	private WrapperNumberField wnfDauerUebersteuert = null;

	public PanelZeitnachweis(InternalFrame internalFrame, String add2TitleI, Object key,
			TabbedPaneZeitnachweis tabbedPaneLos) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneProjekt = tabbedPaneLos;
		jbInit();
		initComponents();
	}

	private TabbedPaneZeitnachweis getTabbedPaneProjekt() {
		return tabbedPaneProjekt;
	}

	private void jbInit() throws Throwable {
		String[] aWhichButtonIUse = null;

		aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD };

		this.enableToolsPanelButtons(aWhichButtonIUse);

		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(gridBagLayout1);

		jpaWorkingOn.setBorder(border1);
		jpaWorkingOn.setLayout(gridBagLayout3);
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		// controls
		wlaDauerUebersteuert = new WrapperLabel(LPMain.getTextRespectUISPr("rech.zeitnachweis.dauer.uebersteuert"));
		wnfDauerUebersteuert = new WrapperNumberField();

		wlaKommentar = new WrapperLabel(LPMain.getTextRespectUISPr("lp.externerkommentar"));
		wlaKommentarIntern = new WrapperLabel(LPMain.getTextRespectUISPr("lp.internerkommentar"));

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		// statusbarneu: 1 an den unteren rand des panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaDauerUebersteuert, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfDauerUebersteuert, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 30, 0));
		iZeile++;
		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefKommentar, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaKommentarIntern, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefKommentarIntern, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			if (projektzeitenDto.getZeitdatenIId() != null) {

				ZeitdatenDto zdDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.zeitdatenFindByPrimaryKey(projektzeitenDto.getZeitdatenIId());

				zdDto.setFDauerUebersteuert(wnfDauerUebersteuert.getDouble());
				zdDto.setXKommentar(wefKommentar.getText());
				zdDto.setXKommentarIntern(wefKommentarIntern.getText());

				DelegateFactory.getInstance().getZeiterfassungDelegate().updateZeitdaten(zdDto);

			} else if (projektzeitenDto.getTelefonzeitenIId() != null) {

				TelefonzeitenDto tzDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.telefonzeitenFindByPrimaryKey(projektzeitenDto.getTelefonzeitenIId());

				tzDto.setFDauerUebersteuert(wnfDauerUebersteuert.getDouble());
				tzDto.setXKommentarext(wefKommentar.getText());
				tzDto.setXKommentarint(wefKommentarIntern.getText());

				DelegateFactory.getInstance().getZeiterfassungDelegate().updateTelefonzeiten(tzDto);
			}

			setKeyWhenDetailPanel(projektzeitenDto.getIId());
			super.eventActionSave(e, true);
			// jetz den anzeigen
			eventYouAreSelected(false);

		}
	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		if (projektzeitenDto != null) {

			if (projektzeitenDto.getZeitdatenIId() != null) {

				ZeitdatenDto zdDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.zeitdatenFindByPrimaryKey(projektzeitenDto.getZeitdatenIId());

				wnfDauerUebersteuert.setDouble(zdDto.getFDauerUebersteuert());

				wefKommentar.setText(zdDto.getXKommentar());
				wefKommentarIntern.setText(zdDto.getXKommentarIntern());
			}else if (projektzeitenDto.getTelefonzeitenIId() != null) {

				TelefonzeitenDto tzDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.telefonzeitenFindByPrimaryKey(projektzeitenDto.getTelefonzeitenIId());

				wnfDauerUebersteuert.setDouble(tzDto.getFDauerUebersteuert());

				wefKommentar.setText(tzDto.getXKommentarext());
				wefKommentarIntern.setText(tzDto.getXKommentarint());
			}
		} else {
			wnfDauerUebersteuert.setDouble(null);
			wefKommentar.setText(null);
			wefKommentarIntern.setText(null);
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bChangeKeyLockMeI, boolean bNeedNoNewI)
			throws Throwable {
		super.eventActionNew(eventObject, false, false);

		this.leereAlleFelder(this);
	}

	/**
	 * Stornieren einer Rechnung bzw Gutschrift
	 * 
	 * @param e                     ActionEvent
	 * @param bAdministrateLockKeyI boolean
	 * @param bNeedNoDeleteI        boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_ZEITDATEN;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI EventObject
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();
			if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
				// einen neuen Eintrag anlegen oder die letzte Position wurde
				// geloescht.
				leereAlleFelder(this);
				clearStatusbar();
			} else {
				// einen alten Eintrag laden.
				projektzeitenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.projektzeitenFindByPrimaryKey((Integer) key);
				dto2Components();
			}
		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		return false;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfDauerUebersteuert;
	}
}
