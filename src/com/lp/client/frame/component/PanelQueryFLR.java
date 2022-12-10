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

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.artikel.PanelArtikel;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.frameposition.ClientPerspectiveManager;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.InternalFrameLieferant;
import com.lp.client.partner.PanelKundekopfdaten;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.pc.LPModul;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

/**
 * 
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/12/11 08:37:04 $
 */
public class PanelQueryFLR extends PanelQuery {

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	private Object source = null;

	private static final long serialVersionUID = 1L;
	public Dialog dialog = null;

	public PanelQueryFLR(QueryType[] typesI, FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI);
		jbInitPanel();
		// Eine Aenderung der Auswahl loest kein ItemChangedEvent aus
		fireItemChangedEventAfterChange = false;
		loadPositionData();
	}

	public PanelQueryFLR(QueryType[] typesI, FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI, FilterKriterium kritVersteckteFelderNichtAnzeigenI,
			String labelText, SortierKriterium sortierkriterium, String textSortierkriterium) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI, false,
				kritVersteckteFelderNichtAnzeigenI, labelText, sortierkriterium, textSortierkriterium);
		// Eine Aenderung der Auswahl loest kein ItemChangedEvent aus
		jbInitPanel();
		fireItemChangedEventAfterChange = false;
		loadPositionData();
	}
	public PanelQueryFLR(QueryType[] typesI, FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI,boolean refreshWhenYouAreSelectedI, FilterKriterium kritVersteckteFelderNichtAnzeigenI,
			String labelText) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI, refreshWhenYouAreSelectedI,
				kritVersteckteFelderNichtAnzeigenI, labelText, null, null);
		jbInitPanel();
		// Eine Aenderung der Auswahl loest kein ItemChangedEvent aus
		fireItemChangedEventAfterChange = false;
		loadPositionData();
	}
	public PanelQueryFLR(QueryType[] typesI, FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI, FilterKriterium kritVersteckteFelderNichtAnzeigenI,
			String labelText) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI, false,
				kritVersteckteFelderNichtAnzeigenI, labelText, null, null);
		jbInitPanel();
		// Eine Aenderung der Auswahl loest kein ItemChangedEvent aus
		fireItemChangedEventAfterChange = false;
		loadPositionData();
	}

	protected void loadPositionData() {
	}

	private void jbInitPanel() throws Throwable {

		if (getIdUsecase() == QueryParameters.UC_ID_KUNDE2 || getIdUsecase() == QueryParameters.UC_ID_ARTIKELLISTE

				|| getIdUsecase() == QueryParameters.UC_ID_LIEFERANTEN) {

			String[] aButton = { PanelBasis.ACTION_NEW, };
			enableToolsPanelButtons(aButton);

		}

	}

	/**
	 * Konstruktor fuer eine FLR Liste, in der ein bestimmter Datensatz selektiert
	 * ist. <br>
	 * Der Key dieses Datensatzes wird als Parameter uebergeben.
	 * 
	 * @param typesI            die UI Filterkriterien fuer den Benutzer
	 * @param filtersI          die default Filterkriterien, die fuer den Benutzer
	 *                          nicht sichtbar sind
	 * @param idUsecaseI        die ID des gewuenschten UseCase
	 * @param aWhichButtonIUseI welche Buttons sind auf dem Panel sichtbar
	 * @param internalFrameI    den InternalFrame als Kontext setzen
	 * @param add2TitleI        der Titel dieses Panels
	 * @param oSelectedIdI      der Datensatz mit diesem Key soll selektiert werden
	 * @throws Throwable
	 */
	public PanelQueryFLR(QueryType[] typesI, FilterKriterium[] filtersI, int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI, Object oSelectedIdI) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI, oSelectedIdI);
		jbInitPanel();
		// Eine Aenderung der Auswahl loest kein ItemChangedEvent aus
		fireItemChangedEventAfterChange = false;
	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

	public void closeMe() {
		getInternalFrame().removeItemChangedListener(this);
		dialog.setVisible(false);
		dialog.dispose();
		dialog = null;
	}

	private void holeModulUndPruefe(String belegartCNr, int iUntererReiter) throws Throwable {

		if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(belegartCNr)) {

			InternalFrame internalFrame = (InternalFrame) LPMain.getInstance().getDesktop().holeModul(belegartCNr);
			if (istAuswertungGeoeffnet(internalFrame) == false) {

				if (istDatenatzGelockt(internalFrame) == false) {

					internalFrame.getTabbedPaneRoot().setSelectedIndex(iUntererReiter);
					if (internalFrame.getTabbedPaneRoot().getSelectedComponent() instanceof TabbedPane) {
						TabbedPane tpSelected = (TabbedPane) internalFrame.getTabbedPaneRoot().getSelectedComponent();
						tpSelected.setSelectedIndex(0);

						ItemChangedEvent ev = new ItemChangedEvent(tpSelected.getSelectedComponent(),
								ItemChangedEvent.ACTION_NEW);

						tpSelected.lPEventItemChanged(ev);

						if (tpSelected.getSelectedComponent() instanceof PanelBasis) {
							PanelBasis panelBasis = (PanelBasis) tpSelected.getSelectedComponent();

							panelBasis.setPanelQueryFLRForCallback(this);

						}

					}

				}
			}
		}

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		this.getDialog().setVisible(false);
		if (getIdUsecase() == QueryParameters.UC_ID_KUNDE2) {

			holeModulUndPruefe(LocaleFac.BELEGART_KUNDE, InternalFrameKunde.IDX_PANE_KUNDE);

		} else if (getIdUsecase() == QueryParameters.UC_ID_ARTIKELLISTE) {
			holeModulUndPruefe(LocaleFac.BELEGART_ARTIKEL, InternalFrameArtikel.IDX_TABBED_PANE_ARTIKEL);
		} else if (getIdUsecase() == QueryParameters.UC_ID_LIEFERANTEN) {
			holeModulUndPruefe(LocaleFac.BELEGART_LIEFERANT, InternalFrameLieferant.IDX_PANE_LIEFERANT);
		} else {
			super.eventActionNew(eventObject, bLockMeI, bNeedNoNewI);
			closeMe();
		}

	}

	public boolean istAuswertungGeoeffnet(InternalFrame iframe) {
		// Wenn ein Report offen, dann vorher fragen, ob dieser geschlossen
		// werden soll

		if (iframe.getStack().size() > 1) {

			boolean b = DialogFactory.showModalJaNeinDialog(iframe,
					LPMain.getTextRespectUISPr("lp.goto.reportgeoeffnet"));

			if (b == true) {

				iframe.enableAllAndRemoveComponent(false);

				return false;

			} else {
				return true;
			}

		} else {
			return false;
		}
	}

	public boolean istDatenatzGelockt(InternalFrame iframe) throws Throwable {
		// Wenn ein Report offen, dann vorher fragen, ob dieser geschlossen
		// werden soll

		if (iframe.getTabbedPaneRoot().getSelectedComponent() instanceof TabbedPane) {

			TabbedPane tp = (TabbedPane) iframe.getTabbedPaneRoot().getSelectedComponent();

			if (tp.getSelectedComponent() instanceof PanelBasis) {
				PanelBasis pb = (PanelBasis) tp.getSelectedComponent();
				LockStateValue lv = pb.getLockedstateDetailMainKey();
				if (lv.getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
					int i = DialogFactory.showModalJaNeinAbbrechenDialog(iframe,
							LPMain.getTextRespectUISPr("lp.goto.datensatzgesperrt"),
							LPMain.getTextRespectUISPr("lp.frage"));
					if (i == JOptionPane.YES_OPTION) {
						pb.setPanelQueryFLRForCallback(null);
						pb.eventActionSave(null, true);
						pb.setPanelQueryFLRForCallback(this);
						return false;

					}
					if (i == JOptionPane.NO_OPTION) {
						pb.discard();
						return false;
					} else {
						return true;
					}
				}
			}
		}

		return false;
	}

	public void neuenEintragUebernehmen(Object neueIId) throws Throwable {

		clearAllFilters();
		eventActionRefresh0(null, true);
		setSelectedId(neueIId);
		fireItemChangedEvent_GOTO_DETAIL_PANEL();
		getInternalFrame().moveToFront();
		closeMe();

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	/**
	 * Hier sind immer alle buttons enabled.
	 * 
	 * @param iAspectI        int
	 * @param lockstateValueI LockStateValue
	 * @throws Exception
	 */
	public void updateButtons(int iAspectI, LockStateValue lockstateValueI) throws Exception {
		// kein super!
		Collection<?> buttons = getHmOfButtons().values();

		for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
			LPButtonAction item = (LPButtonAction) iter.next();

			item.getButton().setEnabled(true);
		}
	}

	protected void eventActionLeeren(ActionEvent e) throws Throwable {
		getInternalFrame().fireItemChanged(this, ItemChangedEvent.ACTION_LEEREN);
		closeMe();
	}

	protected void eventActionEscape(ActionEvent e) throws Throwable {
		super.eventActionEscape(e);
		closeMe();
	}

	/**
	 * 
	 * @param e MouseEvent
	 * @throws ExceptionLP
	 */
	protected void eventMouseClicked(MouseEvent e) throws ExceptionLP {
		if (e.getSource().getClass() == WrapperTable.class) {
			if (e.getClickCount() == 2) {
				// Doppelklick in die Tabelle -> Dialog schliessen
				closeMe();
				super.eventMouseClicked(e);
				return;

				// getInternalFrame().removeItemChangedListener(this);
				// dialog.dispose();
				// super.eventMouseClicked(e);
				// cleanup();
				// return;
			}
		}
		super.eventMouseClicked(e);
	}

	/**
	 * //JO 24.4.06 opt. kein evt wenn key up.
	 * 
	 * @param e KeyEvent
	 * @throws Throwable
	 */
	// protected void eventKeyPressed(KeyEvent e) throws Throwable {
	// boolean bFilterChangedOri = getBFilterHasChanged();
	// // Event an die Superklasse weiterleiten
	// super.eventKeyPressed(e);
	// if (e.getKeyChar() == KeyEvent.VK_ENTER) {
	// // wenn Enter und die Filter sich nicht geaendert haben, dann den
	// // Dialog schliessen
	// // und auch ein Datensatz gewaehlt wurde
	// if (!bFilterChangedOri && this.getSelectedId() != null) {
	// getInternalFrame().removeItemChangedListener(this);
	// dialog.dispose();
	// cleanup();
	// }
	// }
	// }

	protected void eventKeyPressed(KeyEvent e) throws Throwable {
		// Event an die Superklasse weiterleiten
		super.eventKeyPressed(e);
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			// wenn Enter und die Filter sich nicht geaendert haben, dann den
			// Dialog schliessen
			// und auch ein Datensatz gewaehlt wurde
			boolean bFilterChangedOri = getBFilterHasChanged();
			if (!bFilterChangedOri && this.getSelectedId() != null) {
				closeMe();
			}
		}
	}

	public Dialog getDialog() {
		return dialog;
	}

	@Override
	protected void saveFramePosition() {
		ClientPerspectiveManager.getInstance().saveQueryFLRPosition(this);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		if (!getHmDirektFilter().isEmpty()) {
			return super.getFirstFocusableComponent();
		}

		return getTable();
	}
}
