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
package com.lp.client.forecast;

import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.stammdatencrud.PanelStammdatenCRUD;
import com.lp.client.lieferschein.PanelBegruendung;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * <p><I>Diese Klasse kuemmert sich um die Wartung der Grunddaten des
 * Auftrags.</I> </p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>19.07.05</I></p>
 * 
 * <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.5 $
 */
public class TabbedPaneForecastGrunddaten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelForecastartTopQP;
	private PanelSplit panelForecastartSP;
	private PanelStammdatenCRUD panelForecastartBottomD;

	private PanelQuery panelImportdefTopQP;
	private PanelSplit panelImportdefSP;
	private PanelStammdatenCRUD panelImportdefBottomD;

	private PanelQuery panelKommdruckerTopQP;
	private PanelSplit panelKommdruckerSP;
	private PanelKommdrucker panelKommdruckerBottomD;

	private static final int IDX_PANEL_FORECASTART = 0;
	private static final int IDX_PANEL_IMPORTDEF = 1;
	private static final int IDX_PANEL_KOMMDRUCKER = 2;

	public TabbedPaneForecastGrunddaten(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.grunddaten"));
		jbInit();
		initComponents();
	}

	public InternalFrameForecast getInternalFrameForecast() {
		return (InternalFrameForecast) getInternalFrame();
	}

	private void jbInit() throws Throwable {
		insertTab(LPMain.getInstance().getTextRespectUISPr("fc.forecastart"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("fc.forecastart"),
				IDX_PANEL_FORECASTART);

		insertTab(LPMain.getInstance().getTextRespectUISPr("fc.importdef"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("fc.importdef"),
				IDX_PANEL_IMPORTDEF);
		insertTab(LPMain.getInstance().getTextRespectUISPr("fc.kommdrucker"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("fc.kommdrucker"),
				IDX_PANEL_KOMMDRUCKER);

		// default
		refreshPanelAuftragtext();

		setSelectedComponent(panelForecastartSP);

		panelForecastartTopQP.eventYouAreSelected(false);

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelForecastartTopQP) {
				String cNr = (String) panelForecastartTopQP.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr + "");
				panelForecastartBottomD.setKeyWhenDetailPanel(cNr);
				panelForecastartBottomD.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelForecastartTopQP.updateButtons(panelForecastartBottomD
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelImportdefTopQP) {
				String cNr = (String) panelImportdefTopQP.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(cNr + "");
				panelImportdefBottomD.setKeyWhenDetailPanel(cNr);
				panelImportdefBottomD.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				panelImportdefTopQP.updateButtons(panelImportdefBottomD
						.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelKommdruckerTopQP) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				panelKommdruckerBottomD.setKeyWhenDetailPanel(key);
				panelKommdruckerBottomD.eventYouAreSelected(false);
				panelKommdruckerTopQP.updateButtons();
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == panelForecastartBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelForecastartTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));

			} else if (eI.getSource() == panelImportdefBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelImportdefTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));

			} else if (eI.getSource() == panelKommdruckerBottomD) {
				// im QP die Buttons in den Zustand neu setzen.
				panelKommdruckerTopQP.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelForecastartBottomD) {
				panelForecastartSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelImportdefBottomD) {
				panelImportdefTopQP.eventYouAreSelected(false);
			} else if (e.getSource() == panelKommdruckerBottomD) {
				panelKommdruckerTopQP.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelForecastartBottomD) {
				Object oKey = panelForecastartBottomD.getKeyWhenDetailPanel();
				panelForecastartTopQP.eventYouAreSelected(false);
				panelForecastartTopQP.setSelectedId(oKey);
				panelForecastartSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelImportdefBottomD) {
				Object oKey = panelImportdefBottomD.getKeyWhenDetailPanel();
				panelImportdefTopQP.eventYouAreSelected(false);
				panelImportdefTopQP.setSelectedId(oKey);
				panelImportdefSP.eventYouAreSelected(false);
			} else if (e.getSource() == panelKommdruckerBottomD) {
				Object oKey = panelKommdruckerBottomD.getKeyWhenDetailPanel();
				panelKommdruckerTopQP.eventYouAreSelected(false);
				panelKommdruckerTopQP.setSelectedId(oKey);
				panelKommdruckerSP.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelForecastartBottomD) {
				panelForecastartSP.eventYouAreSelected(false); // refresh auf
				// das gesamte
				// 1:n panel
			} else if (e.getSource() == panelImportdefBottomD) {
				panelImportdefSP.eventYouAreSelected(false); // refresh auf
				// das gesamte
				// 1:n panel
			} else if (e.getSource() == panelKommdruckerBottomD) {
				panelKommdruckerSP.eventYouAreSelected(false); // refresh auf
				// das gesamte
				// 1:n panel
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelForecastartTopQP) {

				panelForecastartBottomD.eventActionNew(eI, true, false);
				panelForecastartBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelForecastartSP);
			} else if (eI.getSource() == panelImportdefTopQP) {

				panelImportdefBottomD.eventActionNew(eI, true, false);
				panelImportdefBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelImportdefSP);
			} else if (eI.getSource() == panelKommdruckerTopQP) {

				panelKommdruckerBottomD.eventActionNew(eI, true, false);
				panelKommdruckerBottomD.eventYouAreSelected(false);
				setSelectedComponent(panelKommdruckerSP);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {

		}
		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {

		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_FORECASTART: {
			refreshPanelAuftragtext();

			panelForecastartTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr("lp.auftragtext"));

			// im QP die Buttons setzen.
			panelForecastartTopQP.updateButtons(panelForecastartBottomD
					.getLockedstateDetailMainKey());

			break;
		}
		case IDX_PANEL_IMPORTDEF: {
			refreshPanelImportdef();

			panelImportdefTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr("fc.importdef"));

			// im QP die Buttons setzen.
			panelImportdefTopQP.updateButtons(panelForecastartBottomD
					.getLockedstateDetailMainKey());

			break;
		}
		case IDX_PANEL_KOMMDRUCKER: {
			refreshPanelKommdrucker();

			panelKommdruckerTopQP.eventYouAreSelected(false);
			setTitle(LPMain.getInstance().getTextRespectUISPr("fc.kommdrucker"));

			// im QP die Buttons setzen.
			panelKommdruckerTopQP.updateButtons(panelKommdruckerBottomD
					.getLockedstateDetailMainKey());

			break;
		}

		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
	}

	private void refreshPanelAuftragtext() throws Throwable {
		if (panelForecastartSP == null) {
			String[] aWhichStandardButtonIUse = null;

			panelForecastartTopQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_FORECASTART,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain
							.getInstance()
							.getTextRespectUISPr("fc.forecastart"), true);

			panelForecastartBottomD = new PanelStammdatenCRUD(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("fc.forecastart"), null,
					HelperClient.SCRUD_FORECASTART_FILE,
					getInternalFrameForecast(), HelperClient.LOCKME_FORECASTART);

			panelForecastartSP = new PanelSplit(getInternalFrame(),
					panelForecastartBottomD, panelForecastartTopQP,
					400 - ((PanelStammdatenCRUD) panelForecastartBottomD)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_FORECASTART, panelForecastartSP);
		}
	}

	private void refreshPanelImportdef() throws Throwable {
		if (panelImportdefSP == null) {
			String[] aWhichStandardButtonIUse = null;

			panelImportdefTopQP = new PanelQuery(null, null,
					QueryParameters.UC_ID_IMPORTDEF, aWhichStandardButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("fc.forecastart"), true);

			panelImportdefBottomD = new PanelStammdatenCRUD(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("fc.forecastart"),
					null, HelperClient.SCRUD_IMPORTDEF_FILE,
					getInternalFrameForecast(), HelperClient.LOCKME_IMPORTDEF);

			panelImportdefSP = new PanelSplit(getInternalFrame(),
					panelImportdefBottomD, panelImportdefTopQP,
					400 - ((PanelStammdatenCRUD) panelImportdefBottomD)
							.getAnzahlControls() * 30);

			setComponentAt(IDX_PANEL_IMPORTDEF, panelImportdefSP);
		}
	}

	private void refreshPanelKommdrucker() throws Throwable {
		if (panelKommdruckerSP == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
			};

			panelKommdruckerTopQP = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_KOMMDRUCKER,
					aWhichButtonIUse, getInternalFrame(), LPMain
							.getInstance()
							.getTextRespectUISPr("fc.kommdrucker"), true);

			panelKommdruckerBottomD = new PanelKommdrucker(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("fc.kommdrucker"),
					null);

			panelKommdruckerSP = new PanelSplit(getInternalFrame(),
					panelKommdruckerBottomD, panelKommdruckerTopQP, 300);

			setComponentAt(IDX_PANEL_KOMMDRUCKER, panelKommdruckerSP);
		}
	}

	private void setTitle(String cTitleI) {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				cTitleI);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}
}
