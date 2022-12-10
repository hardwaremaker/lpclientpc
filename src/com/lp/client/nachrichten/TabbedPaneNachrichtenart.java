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
package com.lp.client.nachrichten;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.PanelMaterialzuschlag;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenu;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.ReportEntitylog;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.MaterialFac;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */
public class TabbedPaneNachrichtenart extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryMaterial = null;
	private PanelQuery panelQueryNachrichtenabo = null;
	private PanelBasis panelDetailNachrichtenart = null;
	private PanelBasis panelSplit = null;
	private PanelBasis panelDetailNachrichtenabo = null;

	private WrapperMenuBar wrapperManuBar = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_ABO = 2;

	public TabbedPaneNachrichtenart(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"label.material"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryMaterial() {
		return panelQueryMaterial;
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryMaterial == null) {
			// Artikelauswahlliste
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryMaterial = new PanelQuery(null, null,
					QueryParameters.UC_ID_NACHRICHTENART, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true);

			panelQueryMaterial.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDKennung(),
					SystemFilterFactory.getInstance()
							.createFKDSprTabelleBezeichnung(
									"nachrichtenartsprset"));

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryMaterial);

		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailNachrichtenart == null) {
			panelDetailNachrichtenart = new PanelNachrichtenart(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailNachrichtenart);
		}
	}

	private void createAbo(Integer key) throws Throwable {

		
		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("nachrichtenart_i_id", true,
				key + "", FilterKriterium.OPERATOR_EQUAL, false);
		
		if (panelQueryNachrichtenabo == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			

			panelQueryNachrichtenabo = new PanelQuery(null, kriterien,
					QueryParameters.UC_ID_NACHRICHTENABO, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.nachrichtenabo"), true);

			panelDetailNachrichtenabo = new PanelNachrichtenabo(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.nachrichtenabo"), null);

			panelSplit = new PanelSplit(getInternalFrame(),
					panelDetailNachrichtenabo, panelQueryNachrichtenabo,
					350);

			setComponentAt(IDX_PANEL_ABO, panelSplit);
		} else {
			// filter refreshen.
			panelQueryNachrichtenabo.setDefaultFilter(kriterien);
		}
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				IDX_PANEL_DETAIL);

		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.nachrichtenabo"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.nachrichtenabo"),
				IDX_PANEL_ABO);

		createAuswahl();

		panelQueryMaterial.eventYouAreSelected(false);
		if ((Integer) panelQueryMaterial.getSelectedId() != null) {
			getInternalFrameNachrichten().setNachrichtenartDto(
					DelegateFactory
							.getInstance()
							.getNachrichtenDelegate()
							.nachrichtenartFindByPrimaryKey(
									(Integer) panelQueryMaterial
											.getSelectedId()));
		}
		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryMaterial,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryMaterial) {
				Integer iId = (Integer) panelQueryMaterial.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailNachrichtenart);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelDetailNachrichtenabo) {
				panelDetailNachrichtenabo.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailNachrichtenart) {
				panelDetailNachrichtenart.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailNachrichtenabo) {
				panelQueryNachrichtenabo.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailNachrichtenart) {
				panelQueryMaterial.clearDirektFilter();
				Object oKey = panelDetailNachrichtenart.getKeyWhenDetailPanel();
				panelQueryMaterial.setSelectedId(oKey);
			}

			if (e.getSource() == panelDetailNachrichtenabo) {
				Object oKey = panelDetailNachrichtenabo
						.getKeyWhenDetailPanel();
				panelQueryNachrichtenabo.eventYouAreSelected(false);
				panelQueryNachrichtenabo.setSelectedId(oKey);
				panelSplit.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryNachrichtenabo) {
				Integer iId = (Integer) panelQueryNachrichtenabo
						.getSelectedId();
				panelDetailNachrichtenabo.setKeyWhenDetailPanel(iId);
				panelDetailNachrichtenabo.eventYouAreSelected(false);
				panelQueryNachrichtenabo.updateButtons();
			} else if (e.getSource() == panelQueryMaterial) {
				if (panelQueryMaterial.getSelectedId() != null) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					getInternalFrameNachrichten().setKeyWasForLockMe(key + "");

					getInternalFrameNachrichten().setNachrichtenartDto(
							DelegateFactory
									.getInstance()
									.getNachrichtenDelegate()
									.nachrichtenartFindByPrimaryKey(
											(Integer) key));
					String sBezeichnung = "";
					if (getInternalFrameNachrichten().getNachrichtenartDto()
							.getNachrichtenartsprDto() != null
							&& getInternalFrameNachrichten()
									.getNachrichtenartDto()
									.getNachrichtenartsprDto().getCBez() != null) {
						sBezeichnung = getInternalFrameNachrichten()
								.getNachrichtenartDto()
								.getNachrichtenartsprDto().getCBez()
								+ "";
					}
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							LPMain.getInstance().getTextRespectUISPr(
									"pers.nachrichtenart")
									+ ": "
									+ getInternalFrameNachrichten()
											.getNachrichtenartDto().getCNr()
									+ ", "
									+ LPMain.getInstance().getTextRespectUISPr(
											"label.bezeichnung")
									+ ": "
									+ sBezeichnung);

					if (panelQueryMaterial.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, true);
					}
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailNachrichtenart) {
				getInternalFrame().enableAllPanelsExcept(true);
				this.setSelectedComponent(panelQueryMaterial);
				setKeyWasForLockMe();
				panelQueryMaterial.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailNachrichtenabo) {
				setKeyWasForLockMe();
				if (panelDetailNachrichtenabo.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryNachrichtenabo
							.getId2SelectAfterDelete();
					panelQueryNachrichtenabo.setSelectedId(oNaechster);
				}
				panelSplit.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryMaterial) {

				createDetail((Integer) panelQueryMaterial.getSelectedId());
				if (panelQueryMaterial.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailNachrichtenart.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailNachrichtenart);
			} else if (e.getSource() == panelQueryNachrichtenabo) {
				panelDetailNachrichtenabo.eventActionNew(e, true, false);
				panelDetailNachrichtenabo.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplit);
			}
		}

	}

	public InternalFrameNachrichten getInternalFrameNachrichten() {
		return (InternalFrameNachrichten) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryMaterial.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame()
				.setLpTitle(
						InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
						LPMain.getInstance().getTextRespectUISPr(
								"pers.nachrichtenart"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		String sBezeichnung = "";
		if (getInternalFrameNachrichten().getNachrichtenartDto() != null) {
			if (getInternalFrameNachrichten().getNachrichtenartDto()
					.getNachrichtenartsprDto() != null
					&& getInternalFrameNachrichten().getNachrichtenartDto()
							.getNachrichtenartsprDto().getCBez() != null) {
				sBezeichnung = getInternalFrameNachrichten()
						.getNachrichtenartDto().getNachrichtenartsprDto()
						.getCBez();
			}
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getInstance().getTextRespectUISPr(
							"pers.nachrichtenart")
							+ ": "
							+ getInternalFrameNachrichten()
									.getNachrichtenartDto().getCNr()
							+ ", "
							+ LPMain.getInstance().getTextRespectUISPr(
									"label.bezeichnung") + ": " + sBezeichnung);
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryMaterial.eventYouAreSelected(false);
			if (panelQueryMaterial.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

			panelQueryMaterial.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_DETAIL) {
			Integer key = null;
			if (getInternalFrameNachrichten().getNachrichtenartDto() != null) {
				key = getInternalFrameNachrichten().getNachrichtenartDto()
						.getIId();
			}
			createDetail(key);
			panelDetailNachrichtenart.eventYouAreSelected(false);

		} else if (selectedIndex == IDX_PANEL_ABO) {
			createAbo(getInternalFrameNachrichten()
					.getNachrichtenartDto().getIId());
			panelSplit.eventYouAreSelected(false);
			panelQueryNachrichtenabo.updateButtons();
		}
		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {
			wrapperManuBar = new WrapperMenuBar(this);

			/*
			 * JMenu menuInfo = new WrapperMenu("lp.info", this);
			 * 
			 * wrapperManuBar.addJMenuItem(menuInfo);
			 */

		}

		return wrapperManuBar;
	}

}
