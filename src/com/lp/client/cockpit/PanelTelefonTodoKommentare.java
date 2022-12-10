
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
package com.lp.client.cockpit;

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
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.TelefonzeitenDto;

@SuppressWarnings("static-access")
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
 * @version $Revision: 1.3 $
 */
public class PanelTelefonTodoKommentare extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TelefonzeitenDto telefonzeitenDto = new TelefonzeitenDto();

	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperLabel wlaKommentarIntern = new WrapperLabel();
	private WrapperLabel wlaKommentarExtern = new WrapperLabel();

	private WrapperEditorField wefKommentarIntern = new WrapperEditorFieldKommentar(getInternalFrame(), "");
	private WrapperEditorField wefKommentarExtern = new WrapperEditorFieldKommentar(getInternalFrame(), "");

	public PanelTelefonTodoKommentare(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		initPanel();
		jbInit();
		initComponents();
	}

	// Intitialwerte von panel setzen (beim ersten laden)
	private void initPanel() {

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		leereAlleFelder(this);
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		super.eventActionDelete(e, false, false);
	}

	protected void dto2Components() {
		wefKommentarExtern.setText(telefonzeitenDto.getXKommentarext());
		wefKommentarIntern.setText(telefonzeitenDto.getXKommentarint());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			Integer iIdOrt = null;

			super.eventActionSave(e, true);

			eventYouAreSelected(false);
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		String[] aWhichButtonIUse = {};

		enableToolsPanelButtons(aWhichButtonIUse);

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		wlaKommentarExtern.setText(LPMain.getTextRespectUISPr("lp.externerkommentar"));
		wlaKommentarIntern.setText(LPMain.getTextRespectUISPr("lp.internerkommentar"));

		// jetzt meine felder
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jPanelWorkingOn.add(wlaKommentarExtern, new GridBagConstraints(0, 1, 1, 1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wefKommentarExtern, new GridBagConstraints(1, 1, 1, 1, 0.8, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaKommentarIntern, new GridBagConstraints(0, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wefKommentarIntern, new GridBagConstraints(1, 2, 1, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ORT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);

		Object key = getKeyWhenDetailPanel();
		leereAlleFelder(this);
		if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {

			
			clearStatusbar();
		} else {

			String sKey = (String) key;

			if (sKey.startsWith("T")) {

				telefonzeitenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.telefonzeitenFindByPrimaryKey(new Integer(sKey.substring(1)));
				setStatusbar();
				dto2Components();
			}
		}
	}

//hier erfolgt setting der statusbar (lt. felder in der db)
	protected void setStatusbar() throws Throwable {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wefKommentarExtern;
	}

}
