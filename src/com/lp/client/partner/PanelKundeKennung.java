
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
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.InternalFrameSystem;
import com.lp.server.partner.service.KundeKennungDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LieferartsprDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;

@SuppressWarnings("static-access")
/**
 * <p>
 * Diese Klasse kuemmert sich um das KostenstellenCRUD.
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellung: Josef Ornetsmueller; 13.05.05
 * </p>
 *
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 *
 * @version $Revision: 1.4 $ Date $Date: 2009/11/13 10:12:50 $
 */
public class PanelKundeKennung extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = null;
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperSelectField wsfKennung = new WrapperSelectField(WrapperSelectField.KENNUNG, getInternalFrame(),
			false);
	private KundeKennungDto kundeKennungDto = null;

	private WrapperLabel wlaWert = new WrapperLabel();
	private WrapperTextField wtfWert = new WrapperTextField();

	public PanelKundeKennung(InternalFrame internalFrame, String add2TitleI, Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		initComponents();
		initPanel();
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
		if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	private void jbInit() throws Exception, Throwable {
		// von hier ...
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();

		add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		// jetzt meine Felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		getInternalFrame().addItemChangedListener(this);

		wlaWert = new WrapperLabel(textFromToken("lp.wert"));
		wtfWert.setMandatoryField(true);

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wsfKennung.getWrapperButton(), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfKennung.getWrapperTextField(), new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaWert, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfWert, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	protected void dto2Components() throws Throwable {
		wtfWert.setText(kundeKennungDto.getWert());
		wsfKennung.setKey(kundeKennungDto.getKennungIId());
	}

	protected void components2Dto() throws Throwable {
		kundeKennungDto.setWert(wtfWert.getText());
		kundeKennungDto.setKundeIId(getInternalFrameKunde().getKundeDto().getIId());
		kundeKennungDto.setKennungIId(wsfKennung.getIKey());
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		DelegateFactory.getInstance().getKundeDelegate().removeKundeKennung(kundeKennungDto);

		super.eventActionDelete(e, false, false);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		kundeKennungDto = new KundeKennungDto();

		setDefaults();

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);

		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();
			if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
				leereAlleFelder(this);
				clearStatusbar();
				setDefaults();
			} else {
				kundeKennungDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundekennungFindByPrimaryKey((Integer) key);
				dto2Components();
				setStatusbar();

			}

			initPanel();
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		if (!bNeedNoSaveI) {
			if (allMandatoryFieldsSetDlg()) {
				components2Dto();
				if (kundeKennungDto.getIId() == null) {
					// create
					Integer key = DelegateFactory.getInstance().getKundeDelegate().createKundeKennung(kundeKennungDto);
					setKeyWhenDetailPanel(key);
					kundeKennungDto.setIId(key);
				} else {
					// update
					DelegateFactory.getInstance().getKundeDelegate().updateKundeKennung(kundeKennungDto);
				}
				super.eventActionSave(e, true);
				eventYouAreSelected(false);
			}
		} else {
			super.eventActionSave(e, true);
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERART;
	}

	protected void setStatusbar() throws Throwable {

	}

	protected void setDefaults() throws Throwable {

	}

	private void initPanel() {

	}

	private InternalFrameKunde getInternalFrameKunde() {
		return (InternalFrameKunde) getInternalFrame();
	}

	protected void eventActionRefresh(ActionEvent aE, boolean bNeedNoRefreshI) throws Throwable {

		super.eventActionRefresh(aE, bNeedNoRefreshI);

	}

}
