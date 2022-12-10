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
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.lp.client.angebotstkl.PanelPositionlieferantVergleich;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.frameposition.ClientPerspectiveManager;
import com.lp.client.pc.LPMain;
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
public class PanelSplit extends PanelBasis // implements ItemChangedListener UW
											// am 16.9.05 alle raus
{
	private static final long serialVersionUID = 1L;
	private PanelBasis panelHead = null;
	private PanelQuery panelQuery = null;
	private PanelBasis panelDetail = null;
	private JSplitPane paneSplit = null;
	private JSplitPane splitPaneHeader = null;
	private GridBagLayout gridBagLayoutAll = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private JPanel panelWorkingOn = null;
	private int dividerLocation = 200;
	private int dividerLocationHead = 50;
	private InternalFrame internalFrameI = null;

	private int OFFSET_USERDEFINED_1 = 100000;
	private int OFFSET_USERDEFINED_2 = 100001;
	private int OFFSET_USERDEFINED_3 = 100002;

	public static int OFFSET_PANEL_HEAD = 1000000;

	public PanelSplit(InternalFrame internalFrame, PanelBasis panelDetailI, PanelQuery panelQueryI,
			int dividerLocationI) throws Throwable {

		// titlp: das Toppanel gewinnt hier. ;-)
		super(internalFrame, panelQueryI.getAdd2Title());

		internalFrame = internalFrameI;

		panelDetail = panelDetailI;
		panelQuery = panelQueryI;
		dividerLocation = dividerLocationI;

		jbInit();
		initComponents();
	}

	public PanelSplit(InternalFrame internalFrame, PanelBasis panelHeadI, int dividerLocationHeadI,
			PanelBasis panelDetailI, PanelQuery panelQueryI, int dividerLocationdetailSplit) throws Throwable {

		// titlp: das Toppanel gewinnt hier. ;-)
		super(internalFrame, panelQueryI.getAdd2Title());

		internalFrame = internalFrameI;

		panelDetail = panelDetailI;
		panelQuery = panelQueryI;
		panelHead = panelHeadI;
		dividerLocation = dividerLocationdetailSplit;
		dividerLocationHead = dividerLocationHeadI;

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

		if (panelHead != null) {

			JScrollPane jspScrollPane = new JScrollPane();

			// determine when the horizontal scrollbar appears in the scrollpane
			int horizontalPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
			// int horizontalPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
			// int horizontalPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;

			// determine when the vertical scrollbar appears in the scrollpane
			int vericalPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
			// int vericalPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
			// int vericalPolicy = JScrollPane.VERTICAL_SCROLLBAR_NEVER;

			jspScrollPane.setHorizontalScrollBarPolicy(horizontalPolicy);
			jspScrollPane.setVerticalScrollBarPolicy(vericalPolicy);

			jspScrollPane.getViewport().add(panelHead, null);

			splitPaneHeader = new JSplitPane();
			splitPaneHeader.setDividerSize(3);
			
			splitPaneHeader.setOrientation(JSplitPane.VERTICAL_SPLIT);

			splitPaneHeader.setTopComponent(jspScrollPane);
			splitPaneHeader.setBottomComponent(paneSplit);
			
			splitPaneHeader.setDividerLocation(dividerLocationHead);
			
			panelWorkingOn.add(splitPaneHeader, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		} else {
			panelWorkingOn.add(paneSplit, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
					GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

		// panelall.panelworking.panesplit.paneldetail
		paneSplit.setTopComponent(panelQuery);
		// panelall.panelworking.panesplit.panelquery
		paneSplit.setBottomComponent(panelDetail);

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
		panelQuery.eventYouAreSelected(false);

		updateDetailAndHead(bNeedNoYouAreSelectedI);
	}

	private void updateDetailAndHead(boolean bNeedNoYouAreSelectedI) throws Throwable {
		// keyemp: hole key, key == null
		Object key = panelQuery.getSelectedId();
		panelDetail.setKeyWhenDetailPanel(key);
		// key setzen wegen lockmeabfrage.
		setKeyWhenDetailPanel(key);
		panelDetail.eventYouAreSelected(bNeedNoYouAreSelectedI);

		if (panelHead != null) {
			panelHead.eventYouAreSelected(bNeedNoYouAreSelectedI);
			PanelBasis.enableAllComponents(panelHead, false);
		}
	}
	
	/**
	 * Lade Daten neu aus DB und waehle einen Eintrag aus
	 * @param newKey
	 * @throws Throwable
	 */
	public void updateDataAfterSave(Object newKey) throws Throwable {
		//Pruefen, ob key ein echter key ist
		if(!newKey.equals(LPMain.getLockMeForNew())) {
			//Daten aktualisieren & korrekten Datensatz selektieren
			panelQuery.eventActionRefreshUpdateDb(null, newKey);
			//Auch Buttons updaten und eventuell aktivieren.
			panelQuery.updateButtons();
		}
		updateDetailAndHead(false);
	}

	protected String getLockMeWer() throws Exception {
		return panelDetail.getLockMeWer();
	}

	public PanelQuery getPanelQuery() {
		return panelQuery;
	}

	public PanelBasis getPanelDetail() {
		return panelDetail;
	}

	/**
	 * btnsave: 0 behandle das ereignis save.
	 *
	 * @param e            ActionEvent der Event.
	 eventActionRefreshUpdateDb* @param bNeedNoSaveI boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		// weiterrouten
		panelDetail.eventActionSave(e, bNeedNoSaveI);
	}

	public void eventActionEscape(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		// weiterrouten
		// panelDetail.eventActionEscape(e, bNeedNoSaveI);
	}

	/**
	 * evtvet: Event "Vetoable Window close"; wird null zurueckgegeben, } * so wird
	 * das Modul via dicard beendet, wird ein PropertyVetoException zurueckgegeben,
	 * bleibt das Modul "erhalten".
	 *
	 * @return PropertyVetoException
	 * @throws Throwable
	 */
	protected PropertyVetoException eventActionVetoableChangeLP() throws Throwable {
		return panelDetail.eventActionVetoableChangeLP();
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
		return panelQuery.getFirstFocusableComponent();
	}

	public PanelSplit setSelectedIdFromDetailPanel() throws Throwable {
		Object oKey = panelDetail.getKeyWhenDetailPanel();
		panelQuery.setSelectedId(oKey);
		return this;
	}

	public PanelSplit youAreSelectedFromDetailPanel() throws Throwable {
		return youAreSelectedFromDetailPanel(false);
	}

	public PanelSplit youAreSelectedFromDetailPanel(boolean noNeedYouAreSelected) throws Throwable {
		setSelectedIdFromDetailPanel();
		eventYouAreSelected(noNeedYouAreSelected);
		return this;
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
			PanelSplit.this.removeComponentListener(this);
		}

		public void componentShown(ComponentEvent e) {
			loadDividerLocation();
			PanelSplit.this.removeComponentListener(this);
		}
	}

	private void loadDividerLocation() {
		if (getPanelQuery() == null)
			return;

		Integer headerHeight =0;
		if (splitPaneHeader != null) {

			Integer  headerHeightTemp = ClientPerspectiveManager.getInstance()
					.loadDetailPanelHeight(getPanelQuery().getIdUsecase() + OFFSET_PANEL_HEAD);
			if (headerHeightTemp != null) {
				splitPaneHeader.setDividerLocation(headerHeightTemp);
				headerHeight=headerHeightTemp;
			}

		}

		
		Integer detailHeight = ClientPerspectiveManager.getInstance()
				.loadDetailPanelHeight(getPanelQuery().getIdUsecase());
		if (detailHeight == null)
			return;

		// Berechnung der Divider-Location
		// aus SplitPane-Hoehe = Panel-Query-Hoehe + 1 + Divider-Hoehe + 1 +
		// Panel-Detail-Hoehe
		// zw. den jeweiligen Componenten ist ein Pixel Abgrenzung
		Integer loadedDivLoc = getPanelSplit().getHeight() - detailHeight- headerHeight - getPanelSplit().getDividerSize() - 1;
		// Miniumum 100 Pixel bei veraenderten Fenstergroessen
		getPanelSplit().setDividerLocation(Math.max(loadedDivLoc, 100));

		// SP7522
		if (getPanelQuery().getIdUsecase() == QueryParameters.UC_ID_POSITIONLIEFERANT) {
			if (getPanelSplit().getRightComponent() instanceof PanelPositionlieferantVergleich) {
				PanelPositionlieferantVergleich panel = (PanelPositionlieferantVergleich) getPanelSplit()
						.getRightComponent();

				Integer detailHeight_1 = ClientPerspectiveManager.getInstance()
						.loadDetailPanelHeight(QueryParameters.UC_ID_POSITIONLIEFERANT + OFFSET_USERDEFINED_1);
				Integer detailHeight_2 = ClientPerspectiveManager.getInstance()
						.loadDetailPanelHeight(QueryParameters.UC_ID_POSITIONLIEFERANT + OFFSET_USERDEFINED_2);
				Integer detailHeight_3 = ClientPerspectiveManager.getInstance()
						.loadDetailPanelHeight(QueryParameters.UC_ID_POSITIONLIEFERANT + OFFSET_USERDEFINED_3);

				try {
					panel.setDividerLocations(detailHeight_1, detailHeight_2, detailHeight_3);
				} catch (Throwable e) {
					//
				}

			}
		}

	
		
	}

	private void saveDividerLocation() {
		if (getPanelQuery() == null || getPanelSplit() == null || getPanelSplit().getRightComponent() == null)
			return;
		// Speichern der Hoehe des Details
		Integer detailHeight = getPanelSplit().getRightComponent().getHeight();
		ClientPerspectiveManager.getInstance().saveDetailPanelHeight(getPanelQuery().getIdUsecase(), detailHeight);

		if (splitPaneHeader != null) {
			
			Integer headerHeight = splitPaneHeader.getLeftComponent().getHeight();
			ClientPerspectiveManager.getInstance()
					.saveDetailPanelHeight(getPanelQuery().getIdUsecase() + OFFSET_PANEL_HEAD, headerHeight);
		}

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
