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
package com.lp.client.auftrag;

import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;

import com.lp.client.artikel.InternalFrameArtikel;
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
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.MaterialFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.ZeitplantypDto;
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
public class TabbedPaneZeitplantyp extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryZeitplantyp = null;
	private PanelQuery panelQueryZeitplantypdetail = null;
	private PanelBasis panelDetailZeitplantyp = null;
	private PanelBasis panelSplit = null;
	private PanelBasis panelDetailZeitplantypdetail = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_ZEITPLANTYPDETAIL = 2;

	public ZeitplantypDto zeitplantypDto = null;

	public TabbedPaneZeitplantyp(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"auft.zeitplantyp"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryMaterial() {
		return panelQueryZeitplantyp;
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryZeitplantyp == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryZeitplantyp = new PanelQuery(null, SystemFilterFactory
					.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_ZEITPLANTYP, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true);

			panelQueryZeitplantyp.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryZeitplantyp);

		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailZeitplantyp == null) {
			panelDetailZeitplantyp = new PanelZeitplantyp(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailZeitplantyp);
		}
	}

	private void createZeitplantypdetail(Integer key) throws Throwable {

		if (panelQueryZeitplantypdetail == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			FilterKriterium[] filters = AuftragFilterFactory.getInstance()
					.createFKZeitplantypdetail(key);

			panelQueryZeitplantypdetail = new PanelQuery(null, filters,
					QueryParameters.UC_ID_ZEITPLANTYPDETAIL, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.detail"), true);

			panelDetailZeitplantypdetail = new PanelZeitplantypdetail(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.detail"), null);

			panelSplit = new PanelSplit(getInternalFrame(),
					panelDetailZeitplantypdetail, panelQueryZeitplantypdetail,
					350);

			setComponentAt(IDX_PANEL_ZEITPLANTYPDETAIL, panelSplit);
		} else {
			// filter refreshen.
			panelQueryZeitplantypdetail.setDefaultFilter(AuftragFilterFactory
					.getInstance().createFKZeitplantypdetail(key));
		}
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		insertTab(LPMain.getInstance().getTextRespectUISPr("auft.zeitplantyp"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("auft.zeitplantyp"),
				IDX_PANEL_DETAIL);

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				IDX_PANEL_ZEITPLANTYPDETAIL);

		createAuswahl();

		panelQueryZeitplantyp.eventYouAreSelected(false);
		if ((Integer) panelQueryZeitplantyp.getSelectedId() != null) {
			zeitplantypDto = DelegateFactory
					.getInstance()
					.getAuftragServiceDelegate()
					.zeitplantypFindByPrimaryKey(
							(Integer) panelQueryZeitplantyp.getSelectedId());
		}
		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryZeitplantyp,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryZeitplantyp) {
				Integer iId = (Integer) panelQueryZeitplantyp.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailZeitplantyp);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelDetailZeitplantypdetail) {
				panelDetailZeitplantypdetail.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailZeitplantyp) {
				panelDetailZeitplantyp.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailZeitplantypdetail) {
				panelQueryZeitplantypdetail.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailZeitplantyp) {
				panelQueryZeitplantyp.clearDirektFilter();
				Object oKey = panelDetailZeitplantyp.getKeyWhenDetailPanel();
				panelQueryZeitplantyp.setSelectedId(oKey);
			}

			if (e.getSource() == panelDetailZeitplantypdetail) {
				Object oKey = panelDetailZeitplantypdetail
						.getKeyWhenDetailPanel();
				panelQueryZeitplantypdetail.eventYouAreSelected(false);
				panelQueryZeitplantypdetail.setSelectedId(oKey);
				panelSplit.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryZeitplantypdetail) {
				Integer iId = (Integer) panelQueryZeitplantypdetail
						.getSelectedId();
				panelDetailZeitplantypdetail.setKeyWhenDetailPanel(iId);
				panelDetailZeitplantypdetail.eventYouAreSelected(false);
				panelQueryZeitplantypdetail.updateButtons();
			} else if (e.getSource() == panelQueryZeitplantyp) {
				if (panelQueryZeitplantyp.getSelectedId() != null) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					getInternalFrame().setKeyWasForLockMe(key + "");

					zeitplantypDto = DelegateFactory.getInstance()
							.getAuftragServiceDelegate()
							.zeitplantypFindByPrimaryKey((Integer) key);

					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							LPMain.getInstance().getTextRespectUISPr(
									"label.bezeichnung")
									+ ": " + zeitplantypDto.getCBez());

					if (panelQueryZeitplantyp.getSelectedId() == null) {
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
			if (e.getSource() == panelDetailZeitplantyp) {
				getInternalFrame().enableAllPanelsExcept(true);
				this.setSelectedComponent(panelQueryZeitplantyp);
				setKeyWasForLockMe();
				panelQueryZeitplantyp.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailZeitplantypdetail) {
				setKeyWasForLockMe();
				if (panelDetailZeitplantypdetail.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryZeitplantypdetail
							.getId2SelectAfterDelete();
					panelQueryZeitplantypdetail.setSelectedId(oNaechster);
				}
				panelSplit.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryZeitplantyp) {

				createDetail((Integer) panelQueryZeitplantyp.getSelectedId());
				if (panelQueryZeitplantyp.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailZeitplantyp.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailZeitplantyp);
			} else if (e.getSource() == panelQueryZeitplantypdetail) {
				panelDetailZeitplantypdetail.eventActionNew(e, true, false);
				panelDetailZeitplantypdetail.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplit);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == panelQueryZeitplantypdetail) {

				int iPos = panelQueryZeitplantypdetail.getTable()
						.getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) panelQueryZeitplantypdetail
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) panelQueryZeitplantypdetail
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getAuftragServiceDelegate()
							.vertauscheZeitplantypdetail(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryZeitplantypdetail.setSelectedId(iIdPosition);
				}

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {

			if (e.getSource() == panelQueryZeitplantypdetail) {

				int iPos = panelQueryZeitplantypdetail.getTable()
						.getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < panelQueryZeitplantypdetail.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) panelQueryZeitplantypdetail
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) panelQueryZeitplantypdetail
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getAuftragServiceDelegate()
							.vertauscheZeitplantypdetail(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					panelQueryZeitplantypdetail.setSelectedId(iIdPosition);
				}

			}

		}

	}

	public InternalFrameAuftrag getInternalFrameAuftrag() {
		return (InternalFrameAuftrag) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryZeitplantyp.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("auft.zeitplantyp"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		if (zeitplantypDto != null) {

			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getInstance().getTextRespectUISPr(
							"label.bezeichnung")
							+ ": " + zeitplantypDto.getCBez());
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryZeitplantyp.eventYouAreSelected(false);
			if (panelQueryZeitplantyp.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

			panelQueryZeitplantyp.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_DETAIL) {
			Integer key = null;
			if (zeitplantypDto != null) {
				key = zeitplantypDto.getIId();
			}
			createDetail(key);
			panelDetailZeitplantyp.eventYouAreSelected(false);

		} else if (selectedIndex == IDX_PANEL_ZEITPLANTYPDETAIL) {
			createZeitplantypdetail(zeitplantypDto.getIId());
			panelSplit.eventYouAreSelected(false);
			panelQueryZeitplantypdetail.updateButtons();
		}
		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

}
