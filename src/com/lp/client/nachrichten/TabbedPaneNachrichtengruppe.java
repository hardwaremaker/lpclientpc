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
import com.lp.client.artikel.PanelMaterial;
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
public class TabbedPaneNachrichtengruppe extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryNachrichtengruppe = null;
	private PanelBasis panelDetailNachrichtengruppe = null;
	
	private PanelQuery panelQueryTeilnehmer = null;
	private PanelBasis panelSplit = null;
	private PanelBasis panelDetailTeilnehmer = null;

	private WrapperMenuBar wrapperManuBar = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_TEILNEHMER = 2;

	public TabbedPaneNachrichtengruppe(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.nachrichtengruppen"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryMaterial() {
		return panelQueryNachrichtengruppe;
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryNachrichtengruppe == null) {
			// Artikelauswahlliste
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryNachrichtengruppe = new PanelQuery(null, null,
					QueryParameters.UC_ID_NACHRICHTENGRUPPE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true);

			panelQueryNachrichtengruppe.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryNachrichtengruppe);

		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailNachrichtengruppe == null) {
			panelDetailNachrichtengruppe = new PanelNachrichtengruppe(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailNachrichtengruppe);
		}
	}

	private void createTeilnehmer(Integer key) throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("nachrichtengruppe_i_id", true,
				key + "", FilterKriterium.OPERATOR_EQUAL, false);
		
		if (panelQueryTeilnehmer == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };


			panelQueryTeilnehmer = new PanelQuery(null, kriterien,
					QueryParameters.UC_ID_NACHRICHTENGRUPPETEILNEHMER, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.nachrichtengruppeteilnehmer"), true);

			panelDetailTeilnehmer = new PanelNachrichtengruppeteilnehmer(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("pers.nachrichtengruppeteilnehmer"), null);

			panelSplit = new PanelSplit(getInternalFrame(),
					panelDetailTeilnehmer, panelQueryTeilnehmer,
					350);

			setComponentAt(IDX_PANEL_TEILNEHMER, panelSplit);
		} else {
			// filter refreshen.
			panelQueryTeilnehmer.setDefaultFilter(kriterien);
		}
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
				IDX_PANEL_AUSWAHL);

		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.detail"),
				IDX_PANEL_DETAIL);

		insertTab(LPMain.getInstance().getTextRespectUISPr("pers.nachrichtengruppeteilnehmer"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("pers.nachrichtengruppeteilnehmer"),
				IDX_PANEL_TEILNEHMER);

		createAuswahl();

		panelQueryNachrichtengruppe.eventYouAreSelected(false);
		if ((Integer) panelQueryNachrichtengruppe.getSelectedId() != null) {
			getInternalFrameNachrichten().setNachrichtengruppeDto(
					DelegateFactory
							.getInstance()
							.getNachrichtenDelegate()
							.nachrichtengruppeFindByPrimaryKey(
									(Integer) panelQueryNachrichtengruppe
											.getSelectedId()));
		}
		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryNachrichtengruppe,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryNachrichtengruppe) {
				Integer iId = (Integer) panelQueryNachrichtengruppe.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailNachrichtengruppe);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelDetailTeilnehmer) {
				panelDetailTeilnehmer.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailNachrichtengruppe) {
				panelDetailNachrichtengruppe.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailTeilnehmer) {
				panelQueryTeilnehmer.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailNachrichtengruppe) {
				panelQueryNachrichtengruppe.clearDirektFilter();
				Object oKey = panelDetailNachrichtengruppe.getKeyWhenDetailPanel();
				panelQueryNachrichtengruppe.setSelectedId(oKey);
			}

			if (e.getSource() == panelDetailTeilnehmer) {
				Object oKey = panelDetailTeilnehmer
						.getKeyWhenDetailPanel();
				panelQueryTeilnehmer.eventYouAreSelected(false);
				panelQueryTeilnehmer.setSelectedId(oKey);
				panelSplit.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryTeilnehmer) {
				Integer iId = (Integer) panelQueryTeilnehmer
						.getSelectedId();
				panelDetailTeilnehmer.setKeyWhenDetailPanel(iId);
				panelDetailTeilnehmer.eventYouAreSelected(false);
				panelQueryTeilnehmer.updateButtons();
			} else if (e.getSource() == panelQueryNachrichtengruppe) {
				if (panelQueryNachrichtengruppe.getSelectedId() != null) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					getInternalFrameNachrichten().setKeyWasForLockMe(key + "");

					getInternalFrameNachrichten().setNachrichtengruppeDto(
							DelegateFactory
									.getInstance()
									.getNachrichtenDelegate()
									.nachrichtengruppeFindByPrimaryKey(
											(Integer) key));

					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							LPMain.getInstance().getTextRespectUISPr(
									"pers.nachrichtengruppe")
									+ ": "
									+ getInternalFrameNachrichten()
											.getNachrichtengruppeDto()
											.getCBez());

					if (panelQueryNachrichtengruppe.getSelectedId() == null) {
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
			if (e.getSource() == panelDetailNachrichtengruppe) {
				getInternalFrame().enableAllPanelsExcept(true);
				this.setSelectedComponent(panelQueryNachrichtengruppe);
				setKeyWasForLockMe();
				panelQueryNachrichtengruppe.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailTeilnehmer) {
				setKeyWasForLockMe();
				if (panelDetailTeilnehmer.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryTeilnehmer
							.getId2SelectAfterDelete();
					panelQueryTeilnehmer.setSelectedId(oNaechster);
				}
				panelSplit.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryNachrichtengruppe) {

				createDetail((Integer) panelQueryNachrichtengruppe.getSelectedId());
				if (panelQueryNachrichtengruppe.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailNachrichtengruppe.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailNachrichtengruppe);
			} else if (e.getSource() == panelQueryTeilnehmer) {
				panelDetailTeilnehmer.eventActionNew(e, true, false);
				panelDetailTeilnehmer.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplit);
			}
		}

	}

	public InternalFrameNachrichten getInternalFrameNachrichten() {
		return (InternalFrameNachrichten) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryNachrichtengruppe.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("pers.nachrichten"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		if (getInternalFrameNachrichten().getNachrichtengruppeDto() != null
				&& getInternalFrameNachrichten().getNachrichtengruppeDto()
						.getCBez() != null) {

			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getInstance().getTextRespectUISPr(
							"pers.nachrichtengruppe")
							+ ": "
							+ getInternalFrameNachrichten()
									.getNachrichtengruppeDto().getCBez());
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryNachrichtengruppe.eventYouAreSelected(false);
			if (panelQueryNachrichtengruppe.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

			panelQueryNachrichtengruppe.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_DETAIL) {
			Integer key = null;
			if (getInternalFrameNachrichten().getNachrichtengruppeDto() != null) {
				key = getInternalFrameNachrichten().getNachrichtengruppeDto()
						.getIId();
			}
			createDetail(key);
			panelDetailNachrichtengruppe.eventYouAreSelected(false);

		} else if (selectedIndex == IDX_PANEL_TEILNEHMER) {
			createTeilnehmer(getInternalFrameNachrichten()
					.getNachrichtengruppeDto().getIId());
			panelSplit.eventYouAreSelected(false);
			panelQueryTeilnehmer.updateButtons();
		}
		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {
			wrapperManuBar = new WrapperMenuBar(this);

		}

		return wrapperManuBar;
	}

}
