
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
package com.lp.client.frame.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.lp.client.angebotstkl.PanelPositionlieferantVergleich;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.frameposition.ClientPerspectiveManager;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 *
 * <p>
 * Diese Klasse kuemmert sich ein QP mit einem D-Panel.
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 *
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 *
 * @version not attributable Date $Date: 2009/08/11 13:34:37 $
 */
public class PanelSplit2 extends PanelBasis // implements ItemChangedListener UW
											// am 16.9.05 alle raus
{
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryUnten = null;
	private PanelQuery panelQueryOben = null;
	private JSplitPane paneSplit = null;
	private GridBagLayout gridBagLayoutAll = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private JPanel panelWorkingOn = null;
	private int dividerLocation = 200;
	private InternalFrame internalFrameI = null;

	private int OFFSET_USERDEFINED_1 = 100000;
	private int OFFSET_USERDEFINED_2 = 100001;
	private int OFFSET_USERDEFINED_3 = 100002;

	public PanelSplit2(InternalFrame internalFrame, PanelQuery panelQueryOben, PanelQuery panelQueryUnten,
			int dividerLocationI) throws Throwable {

		// titlp: das Toppanel gewinnt hier. ;-)
		super(internalFrame, panelQueryOben.getAdd2Title());

		internalFrame = internalFrameI;

		this.panelQueryUnten = panelQueryUnten;
		this.panelQueryOben = panelQueryOben;
		dividerLocation = dividerLocationI;

		jbInit();
		initComponents();
	}

	private void jbInit() throws Exception {
		// panelall
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// panelall.panelworking
		panelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		panelWorkingOn.setLayout(gridBagLayoutWorkingOn);

		this.add(panelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// panelall.panelworking.panesplit
		paneSplit = new JSplitPane();
		paneSplit.setDividerSize(3);
		paneSplit.setDividerLocation(dividerLocation);
		paneSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		// panelall.panelworking.panesplit
		panelWorkingOn.add(paneSplit, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// panelall.panelworking.panesplit.paneldetail
		paneSplit.setTopComponent(panelQueryOben);
		// panelall.panelworking.panesplit.panelquery
		paneSplit.setBottomComponent(panelQueryUnten);

		getPanelQuery().addSaveQueryViewActionListener(this);
		addComponentListener(new DividerLocationLoaderListener());
	}

	public void enableOneTouchExpandle(boolean enable) {
		if (!enable) {
			paneSplit.setDividerSize(3);
			paneSplit.setOneTouchExpandable(false);
		} else {
			paneSplit.setDividerSize(10);
			paneSplit.setOneTouchExpandable(true);
		}
	}

	public void beOneTouchExpandable() {
		enableOneTouchExpandle(true);
	}

	public JSplitPane getPanelSplit() {
		return paneSplit;
	}

	public void updateButtons(int iAspectI, LockStateValue lockstateValueI) throws Exception {
		// nothing here
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		// weiterrouten.
		panelQueryOben.eventYouAreSelected(false);

		// keyemp: hole key, key == null
		Object key = panelQueryOben.getSelectedId();
		panelQueryUnten.setKeyWhenDetailPanel(key);
		// key setzen wegen lockmeabfrage.
		setKeyWhenDetailPanel(key);
		panelQueryUnten.eventYouAreSelected(bNeedNoYouAreSelectedI);
	}

	protected PropertyVetoException eventActionVetoableChangeLP() throws Throwable {
		return null;
	}
	
	protected String getLockMeWer() throws Exception {
		return null;
	}
	
	public PanelQuery getPanelQuery() {
		return panelQueryOben;
	}

	public PanelBasis getPanelQueryUnten() {
		return panelQueryUnten;
	}

	public void eventActionEscape(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		// weiterrouten
		// panelDetail.eventActionEscape(e, bNeedNoSaveI);
	}

	/**
	 *
	 * @return int
	 * @throws Throwable
	 */
	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		// hier fix, damit er in's eventActionVetoableChangeLP geht.
		// return panelDetail.getLockedstateDetailMainKey();
		return new LockStateValue(null, null, LOCK_IS_LOCKED_BY_ME);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return panelQueryOben.getFirstFocusableComponent();
	}

	@Override
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (PanelQuery.LEAVEALONE_QUERYVIEW_SAVE.equals(e.getActionCommand())) {
			saveDividerLocation();
		}
	}

	/**
	 * Initiiert das Laden und Setzen der Divider Location Soll nur beim erstmaligen
	 * Anzeigen des Splitpanels geladen, darum entfernt sich dieser
	 * ComponentListener wieder selbst.
	 * 
	 * Ist so umstaendlich notwendig, da die Hoehen des Splitpanels und seinen
	 * Components bei Initialisierung noch nicht bekannt ist und die
	 * Divider-Location sich nach der Hoehe des Details richtet
	 * 
	 */
	private class DividerLocationLoaderListener extends ComponentAdapter {
		public void componentResized(ComponentEvent e) {
			loadDividerLocation();
			PanelSplit2.this.removeComponentListener(this);
		}

		public void componentShown(ComponentEvent e) {
			loadDividerLocation();
			PanelSplit2.this.removeComponentListener(this);
		}
	}

	private void loadDividerLocation() {
		if (getPanelQuery() == null)
			return;

		Integer detailHeight = ClientPerspectiveManager.getInstance()
				.loadDetailPanelHeight(getPanelQuery().getIdUsecase());
		if (detailHeight == null)
			return;

		// Berechnung der Divider-Location
		// aus SplitPane-Hoehe = Panel-Query-Hoehe + 1 + Divider-Hoehe + 1 +
		// Panel-Detail-Hoehe
		// zw. den jeweiligen Componenten ist ein Pixel Abgrenzung
		Integer loadedDivLoc = getPanelSplit().getHeight() - detailHeight - getPanelSplit().getDividerSize() - 1;
		// Miniumum 100 Pixel bei veraenderten Fenstergroessen
		getPanelSplit().setDividerLocation(Math.max(loadedDivLoc, 100));

	}

	private void saveDividerLocation() {
		if (getPanelQuery() == null || getPanelSplit() == null || getPanelSplit().getRightComponent() == null)
			return;
		// Speichern der Hoehe des Details
		Integer detailHeight = getPanelSplit().getRightComponent().getHeight();
		ClientPerspectiveManager.getInstance().saveDetailPanelHeight(getPanelQuery().getIdUsecase(), detailHeight);

		// SP7522
		if (getPanelQuery().getIdUsecase() == QueryParameters.UC_ID_POSITIONLIEFERANT) {
			if (getPanelSplit().getRightComponent() instanceof PanelPositionlieferantVergleich) {
				PanelPositionlieferantVergleich panel = (PanelPositionlieferantVergleich) getPanelSplit()
						.getRightComponent();

				ClientPerspectiveManager.getInstance().saveDetailPanelHeight(
						QueryParameters.UC_ID_POSITIONLIEFERANT + OFFSET_USERDEFINED_1,
						panel.getDividerLocationMitte());
				ClientPerspectiveManager.getInstance().saveDetailPanelHeight(
						QueryParameters.UC_ID_POSITIONLIEFERANT + OFFSET_USERDEFINED_2,
						panel.getDividerLocationLinks());
				ClientPerspectiveManager.getInstance().saveDetailPanelHeight(
						QueryParameters.UC_ID_POSITIONLIEFERANT + OFFSET_USERDEFINED_3,
						panel.getDividerLocationRechts());

			}
		}

	}
}
