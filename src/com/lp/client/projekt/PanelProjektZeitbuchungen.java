
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
package com.lp.client.projekt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.KeyStroke;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditor;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;

/**
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/05/09 09:29:31 $
 */
public class PanelProjektZeitbuchungen extends PanelBasis implements PropertyChangeListener {

	private static final long serialVersionUID = 5243100664274910691L;

	private ZeitdatenDto zeitdatenDto = null;

	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperTextField wtfBemerkung = new WrapperTextField();

	private WrapperLabel wlaKommentarIntern = new WrapperLabel();
	private LpEditor wefKommentarIntern = new LpEditor(null);

	private WrapperLabel wlaKommentarExtern = new WrapperLabel();
	private LpEditor wefKommentarExtern = new LpEditor(null);

	private IProjektDtoService iProjektDtoService = null;

	static final public String GOTO_ZEITBUCHUNG = LEAVEALONE + "GOTO_BELEG";

	WrapperGotoButton wbuZeitbuchung = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_ZEITERFASSUNG_ZEITDATEN);

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI    der default Titel des Panels
	 * @param key           PK des Projektes
	 * @throws java.lang.Throwable Ausnahme
	 */
	public PanelProjektZeitbuchungen(InternalFrame internalFrame, IProjektDtoService iProjektDtoService,
			String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.iProjektDtoService = iProjektDtoService;
		jbInitPanel();
		initComponents();
	}

	private void jbInitPanel() throws Throwable {

		// enableToolsPanelButtons(toolbarButtonsPlain);

		// das Aussenpanel hat immer das Gridbaglayout und einen Rahmen nach
		// innen
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 10, 3));

		getInternalFrame().addItemChangedListener(this);

		wlaBemerkung.setText(LPMain.getTextRespectUISPr("lp.bemerkung"));

		wtfBemerkung.setColumnsMax(ZeiterfassungFac.MAX_ZEITDATEN_BEMERKUNG);

		wlaKommentarIntern.setText(LPMain.getTextRespectUISPr("lp.internerkommentar"));
		wlaKommentarExtern.setText(LPMain.getTextRespectUISPr("lp.externerkommentar"));

		// Zeile - die Toolbar
		add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		this.add(wlaBemerkung, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 0, 2, 2), 100, 0));
		this.add(wtfBemerkung, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 0, 2, 0), 0, 0));
		iZeile++;
		this.add(wlaKommentarExtern, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 0, 2, 2), 0, 0));
		this.add(wefKommentarExtern, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 0, 2, 0), 0, 0));

		iZeile++;
		this.add(wlaKommentarIntern, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 0, 2, 2), 0, 0));
		this.add(wefKommentarIntern, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 0, 2, 0), 0, 0));

		iZeile++;

		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		this.add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		getToolBar().addButtonLeft("/com/lp/client/res/data_into.png",
				LPMain.getTextRespectUISPr("projekt.gehezuzeitbuchung"), GOTO_ZEITBUCHUNG,
				KeyStroke.getKeyStroke('G', java.awt.event.InputEvent.CTRL_MASK), null);
		enableToolsPanelButtons(true, GOTO_ZEITBUCHUNG);

	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_PROJEKT;
	}

	private void setDefaults() throws Throwable {
		zeitdatenDto = new ZeitdatenDto();
		leereAlleFelder(this);

	}

	private void dto2Components() throws Throwable {

		wtfBemerkung.setText(zeitdatenDto.getCBemerkungZuBelegart());
		wefKommentarExtern.setText(zeitdatenDto.getXKommentar());
		wefKommentarIntern.setText(zeitdatenDto.getXKommentarIntern());

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		setDefaults();
		clearStatusbar();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(GOTO_ZEITBUCHUNG)) {
			wbuZeitbuchung.actionPerformed(new ActionEvent(wbuZeitbuchung, 0, WrapperGotoButton.ACTION_GOTO));
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || key.equals(LPMain.getLockMeForNew())) {
			// Neu.
			leereAlleFelder(this);

			clearStatusbar();
			wefKommentarIntern.setText(null);
			wefKommentarExtern.setText(null);
			wbuZeitbuchung.setOKey(null);
			enableToolsPanelButtons(false, GOTO_ZEITBUCHUNG);
		} else {
			// Update.
			zeitdatenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
					.zeitdatenFindByPrimaryKey((Integer) key);

			wbuZeitbuchung.setOKey(zeitdatenDto.getIId());
			enableToolsPanelButtons(true, GOTO_ZEITBUCHUNG);
			dto2Components();

		}
		StringBuffer projektTitel = new StringBuffer("");
		if (iProjektDtoService == null || iProjektDtoService.getProjektDto().getIId() == null) {
			projektTitel.append(LPMain.getTextRespectUISPr("proj.projekt.neu"));
		} else {
			PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(iProjektDtoService.getProjektDto().getPartnerIId());
			projektTitel
					.append(DelegateFactory.getInstance().getProjektServiceDelegate()
							.bereichFindByPrimaryKey(iProjektDtoService.getProjektDto().getBereichIId()).getCBez())
					.append(" ")
					.append(iProjektDtoService.getProjektDto().getCNr() + "|"
							+ partnerDto.getCName1nachnamefirmazeile1() + "|"
							+ iProjektDtoService.getProjektDto().getCTitel());
		}

		// dem Panel die Titel setzen
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				LPMain.getTextRespectUISPr("proj.projekt.details"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, projektTitel.toString());
	}

	public void propertyChange(PropertyChangeEvent evt) {
	}

}
