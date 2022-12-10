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
package com.lp.client.finanz;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.component.IPanelQueryTauscheIdsDelegate;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.IPartnerDtoService;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p><I>Diese Klasse kuemmert sich um die Finanzaemter</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>13.01.05</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.11 $
 */
public class TabbedPaneFinanzamt extends TabbedPane implements IPartnerDtoService {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private FinanzamtDto finanzamtDto = null;
	private PanelQuery panelQueryFinanzamt = null;
	private PanelFinanzFinanzamtKopfdaten panelFinanzamtKopfdaten = null;

	private WrapperMenuBar wrapperManuBar = null;

	public int IDX_FINANZAEMTER = -1;
	public int IDX_KOPFDATEN = -1;
	public int IDX_REVERSECHARGEART = -1 ;
	public int IDX_STEUERKATEGORIE = -1;
	private int IDX_STEUERKATEGORIEKONTO = -1;
	private int IDX_BUCHUNGSPARAMETER = -1;

	private PanelQuery panelReversechargeartQP1 = null;
	private PanelReversechargeart panelReversechargeartBottomD1 = null;
	private PanelSplit panelReversechargeartSP1 = null;
	
	private PanelQuery panelSteuerkategorieQP1 = null;
	private PanelSteuerkategorie panelSteuerkategorieBottomD1 = null;
	private PanelSplit panelSteuerkategorieSP1 = null;

	private PanelQuery panelSteuerkategoriekontoQP1 = null;
	private PanelSteuerkategoriekonto panelSteuerkategorieBottomkontoD1 = null;

	private PanelBuchungsparameter panelBuchungsparameterD1 = null;

	private IFinanzamtController finanzamtController = null;
	private Integer previousFinanzamtIId = null;
	private Integer previousSteuerkategoriekontoIId = null;
	private Integer previousReversechargeartId = null ;
	private InternalFrameFinanz internalFrameFinanz = null;

	private static final String MENUE_JOURNAL_STEUERKATEGORIE = "MENUE_JOURNAL_STEUERKATEGORIE";
	private static final String MENU_ACTION_BEARBEITEN_UVAFORMULARPFLEGE = "MENU_ACTION_BEARBEITEN_UVAFORMULARPFLEGE";

	public TabbedPaneFinanzamt(InternalFrameFinanz internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain
				.getTextRespectUISPr("finanz.tab.unten.finanzamt.title"));
		internalFrameFinanz = internalFrameI;
		finanzamtController = new FinanzamtController(LPMain.getTheClient());

		jbInit();
		initComponents();
	}

	public IFinanzamtController getFinanzController() {
		return finanzamtController;
	}

	/**
	 * jbInit.
	 *
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		//

		int index = 0;
		IDX_FINANZAEMTER = index;
		insertTab(LPMain.getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getTextRespectUISPr("lp.auswahl"), IDX_FINANZAEMTER);

		IDX_KOPFDATEN = ++index;
		// Tab 2: Kopfdaten
		insertTab(LPMain.getTextRespectUISPr("lp.kopfdaten"), null, null,
				LPMain.getTextRespectUISPr("lp.kopfdaten"), IDX_KOPFDATEN);
		
		IDX_REVERSECHARGEART = ++index ;
		insertTab(LPMain.getTextRespectUISPr("fb.reversechargeart"), null, null,
				LPMain.getTextRespectUISPr("fb.reversechargeart"), IDX_REVERSECHARGEART);
		
		if (internalFrameFinanz.getBVollversion()) {
			IDX_STEUERKATEGORIE = ++index;
			// Tab 3: Steuerkategorie
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"fb.steuerkategorie"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"fb.steuerkategorie"), IDX_STEUERKATEGORIE);

			IDX_STEUERKATEGORIEKONTO = ++index;
			// Tab 4: Steuerkategoriekonto
			insertTab(
					LPMain.getInstance().getTextRespectUISPr(
							"fb.steuerkategoriekonto"),
					null,
					null,
					LPMain.getInstance().getTextRespectUISPr(
							"fb.steuerkategoriekonto"),
					IDX_STEUERKATEGORIEKONTO);
		}

		IDX_BUCHUNGSPARAMETER = ++index;
		insertTab(
				LPMain.getInstance()
						.getTextRespectUISPr("fb.buchungsparameter"), null,
				null,
				LPMain.getInstance()
						.getTextRespectUISPr("fb.buchungsparameter"),
				IDX_BUCHUNGSPARAMETER);

		setSelectedComponent(getPanelQueryFinanzamt(true));
		// refresh
		getPanelQueryFinanzamt(true).eventYouAreSelected(false);
		// damit gleich eine selektiert ist
		ItemChangedEvent it = new ItemChangedEvent(
				getPanelQueryFinanzamt(true), ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// da will jemand die Buchungen ansehen
			if (e.getSource() == getPanelQueryFinanzamt(false)) {
				Object key = getPanelQueryFinanzamt(true).getSelectedId();
				getFinanzController().setFinanzamtIId((Integer) key);

				holeFinanzamtDto(key);
				// nur wechseln wenns auch einen gibt
				if (key != null) {
					setSelectedComponent(getPanelDetailFinanzamtKopfdaten(true));
					getPanelDetailFinanzamtKopfdaten(true).eventYouAreSelected(
							false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelQueryFinanzamt(false)) {
				Object key = getPanelQueryFinanzamt(true).getSelectedId();
				getPanelQueryFinanzamt(true).setKeyWhenDetailPanel(key);
				getFinanzController().setFinanzamtIId((Integer) key);

				// updateDetailKeyForFinanzamt() ;
				holeFinanzamtDto(key);
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_FINANZAEMTER, key != null);

				getPanelQueryFinanzamt(true).updateButtons();

//				MandantDto mDto = DelegateFactory
//						.getInstance()
//						.getMandantDelegate()
//						.mandantFindByPrimaryKey(
//								LPMain.getTheClient().getMandant());
//
//				if (mDto.getPartnerIIdFinanzamt() != null
//						&& mDto.getPartnerIIdFinanzamt().equals((Integer) key)) {
//					setEnabledAt(IDX_BUCHUNGSPARAMETER, true);
//				} else {
//					setEnabledAt(IDX_BUCHUNGSPARAMETER, false);
//				}
			} else if (e.getSource() == panelReversechargeartQP1) {
				Integer iId = (Integer) panelReversechargeartQP1.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelReversechargeartBottomD1.setKeyWhenDetailPanel(iId);
				getFinanzController().setReversechargeartIId(iId);
				
				panelReversechargeartBottomD1.eventYouAreSelected(false);
				panelReversechargeartQP1.updateButtons();
			} else if (e.getSource() == panelSteuerkategorieQP1) {
				Integer iId = (Integer) panelSteuerkategorieQP1.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelSteuerkategorieBottomD1.setKeyWhenDetailPanel(iId);
				getFinanzController().setSteuerkategorieIId(iId);

				panelSteuerkategorieBottomD1.eventYouAreSelected(false);
				panelSteuerkategorieQP1.updateButtons();
			} else if (e.getSource() == panelSteuerkategoriekontoQP1) {
				Integer iId = (Integer) panelSteuerkategoriekontoQP1
						.getSelectedId();
				getInternalFrame().setKeyWasForLockMe(iId + "");
				panelSteuerkategorieBottomkontoD1.setKeyWhenDetailPanel(iId);
				panelSteuerkategorieBottomkontoD1.eventYouAreSelected(false);
				panelSteuerkategoriekontoQP1.updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelQueryFinanzamt(false)) {
				if (getPanelQueryFinanzamt(true).getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				getPanelDetailFinanzamtKopfdaten(true).eventActionNew(null,
						true, false);
				setSelectedComponent(getPanelDetailFinanzamtKopfdaten(true));
			} else if(e.getSource() == panelReversechargeartQP1) {
				panelReversechargeartBottomD1.eventActionNew(e, true, false);
				panelReversechargeartBottomD1.eventYouAreSelected(false);
				setSelectedComponent(panelReversechargeartSP1);
			} else if (e.getSource() == panelSteuerkategorieQP1) {
				// Steuerkategorien duerfen nicht angelegt werden
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelDetailFinanzamtKopfdaten(false)) {
				setFinanzamtDto(null);
				getPanelQueryFinanzamt(true).eventYouAreSelected(false);
				this.setSelectedComponent(getPanelQueryFinanzamt(true));
			} else if (e.getSource() == panelSteuerkategorieBottomD1) {
				panelSteuerkategorieSP1.eventYouAreSelected(false);
				// }else if (e.getSource() == panelSteuerkategorieBottomkontoD1)
				// {
				// panelSteuerkategoriekontoSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelReversechargeartBottomD1) {
				if(panelReversechargeartBottomD1.getKeyWhenDetailPanel() == null) {
					Object o = panelReversechargeartQP1.getId2SelectAfterDelete() ;
					panelReversechargeartQP1.setSelectedId(o);
				}
				panelReversechargeartSP1.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD 
				|| e.getID() == ItemChangedEvent.ACTION_DELETE) {
			if(e.getSource() == panelReversechargeartBottomD1) {
				panelReversechargeartBottomD1.eventYouAreSelected(false);				
			} else if (e.getSource() == panelSteuerkategorieBottomD1) {
				panelSteuerkategorieBottomD1.eventYouAreSelected(false);
			} else if (e.getSource() == panelSteuerkategorieBottomkontoD1) {
				panelSteuerkategorieBottomkontoD1.eventYouAreSelected(false);
			} else if (e.getSource() == panelBuchungsparameterD1) {
				panelBuchungsparameterD1.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == getPanelDetailFinanzamtKopfdaten(false)) {
				getPanelQueryFinanzamt(true).clearDirektFilter();
				Object key = getPanelDetailFinanzamtKopfdaten(true)
						.getKeyWhenDetailPanel();

				getPanelQueryFinanzamt(true).eventActionRefresh(null, false);

				getPanelQueryFinanzamt(true).setSelectedId(key);
				getFinanzController().setFinanzamtIId((Integer) key);
				getPanelQueryFinanzamt(true).eventYouAreSelected(false);
			} else if (e.getSource() == panelBuchungsparameterD1) {
				Object oKey = panelBuchungsparameterD1.getKeyWhenDetailPanel();
			} else if (e.getSource() == panelReversechargeartBottomD1) {
				Object oKey = panelReversechargeartBottomD1
						.getKeyWhenDetailPanel();
				panelReversechargeartQP1.setSelectedId(oKey);
				panelReversechargeartSP1.eventYouAreSelected(false);				
			} else if (e.getSource() == panelSteuerkategorieBottomD1) {
				Object oKey = panelSteuerkategorieBottomD1
						.getKeyWhenDetailPanel();
				// panelSteuerkategorieQP1.eventYouAreSelected(false);
				panelSteuerkategorieQP1.setSelectedId(oKey);
				panelSteuerkategorieSP1.eventYouAreSelected(false);
			} else if (e.getSource() == panelSteuerkategorieBottomkontoD1) {
				Object oKey = panelSteuerkategorieBottomkontoD1
						.getKeyWhenDetailPanel();
				panelSteuerkategoriekontoQP1.setSelectedId(oKey);
				panelSteuerkategoriekontoQP1.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if(e.getSource() == panelReversechargeartQP1) {
				panelReversechargeartQP1.tauscheMinus(new IPanelQueryTauscheIdsDelegate() {				
					@Override
					public void tausche(Integer actualId, Integer otherId) throws Throwable {
						DelegateFactory.getInstance()
							.getFinanzServiceDelegate().vertauscheReversechargeart(actualId, otherId);
					}
				});
//				Integer nextId = getPreviousRowId(panelReversechargeartQP1.getTable()) ;
//				if(nextId != null) {
//					Integer actualId = (Integer) panelReversechargeartQP1.getSelectedId();
//					DelegateFactory.getInstance().getFinanzServiceDelegate().vertauscheReversechargeart(actualId, nextId);
//					panelReversechargeartQP1.setSelectedId(actualId);
//				}				
			} else if (e.getSource() == panelSteuerkategorieQP1) {
				panelSteuerkategorieQP1.tauscheMinus(new IPanelQueryTauscheIdsDelegate() {					
					@Override
					public void tausche(Integer actualId, Integer otherId) throws Throwable {
						DelegateFactory.getInstance()
							.getFinanzServiceDelegate().vertauscheSteuerkategorie(actualId, otherId);
					}
				});
//				
//				int iPos = panelSteuerkategorieQP1.getTable().getSelectedRow();
//
//				// wenn die Position nicht die erste ist
//				if (iPos > 0) {
//					Integer iIdPosition = (Integer) panelSteuerkategorieQP1
//							.getSelectedId();
//
//					Integer iIdPositionMinus1 = (Integer) panelSteuerkategorieQP1
//							.getTable().getValueAt(iPos - 1, 0);
//					DelegateFactory
//							.getInstance()
//							.getFinanzServiceDelegate()
//							.vertauscheSteuerkategorie(iIdPosition,
//									iIdPositionMinus1);
//
//					// die Liste neu anzeigen und den richtigen Datensatz
//					// markieren
//					panelSteuerkategorieQP1.setSelectedId(iIdPosition);
//				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if(e.getSource() == panelReversechargeartQP1) {
				panelReversechargeartQP1.tauschePlus(new IPanelQueryTauscheIdsDelegate() {
					@Override
					public void tausche(Integer actualId, Integer otherId) throws Throwable {
						DelegateFactory.getInstance()
							.getFinanzServiceDelegate().vertauscheReversechargeart(actualId, otherId);
					}
				});
			} else if (e.getSource() == panelSteuerkategorieQP1) {
				panelSteuerkategorieQP1.tauschePlus(new IPanelQueryTauscheIdsDelegate() {			
					@Override
					public void tausche(Integer actualId, Integer otherId) throws Throwable {
						DelegateFactory.getInstance()
							.getFinanzServiceDelegate().vertauscheSteuerkategorie(actualId, otherId);						
					}
				});
//				Integer nextId = getNextRowId(panelSteuerkategorieQP1.getTable()) ;
//				if(nextId != null) {
//					Integer actualId = (Integer) panelSteuerkategorieQP1.getSelectedId();
//					DelegateFactory.getInstance().getFinanzServiceDelegate().vertauscheSteuerkategorie(actualId, nextId);
//					panelSteuerkategorieQP1.setSelectedId(actualId);
//				}
//				
//				int iPos = panelSteuerkategorieQP1.getTable().getSelectedRow();
//
//				// wenn die Position nicht die letzte ist
//				if (iPos < panelSteuerkategorieQP1.getTable().getRowCount() - 1) {
//					Integer iIdPosition = (Integer) panelSteuerkategorieQP1
//							.getSelectedId();
//
//					Integer iIdPositionPlus1 = (Integer) panelSteuerkategorieQP1
//							.getTable().getValueAt(iPos + 1, 0);
//					DelegateFactory
//							.getInstance()
//							.getFinanzServiceDelegate()
//							.vertauscheSteuerkategorie(iIdPosition,
//									iIdPositionPlus1);
//
//					// die Liste neu anzeigen und den richtigen Datensatz
//					// markieren
//					panelSteuerkategorieQP1.setSelectedId(iIdPosition);
//				}
			}
		}
	}
	
	private Integer getNextRowId(WrapperTable wrapperTable) {
		Integer index = wrapperTable.getSelectedRow() ;
		if(index < wrapperTable.getRowCount() - 1) {
			return (Integer) wrapperTable.getValueAt(index + 1,  0) ;
		}
		
		return null ;
	}

	private Integer getPreviousRowId(WrapperTable wrapperTable) {
		Integer index = wrapperTable.getSelectedRow() ;
		if(index > 0 ) {
			return (Integer) wrapperTable.getValueAt(index - 1,  0) ;
		}
		
		return null ;
	}

	private FilterKriterium[] getFilterForReversechargeart() throws Throwable {
		FilterKriterium[] aFilterKriterium = new FilterKriterium[1];

		aFilterKriterium[0] = SystemFilterFactory.getInstance()
				.createFKMandantCNr()[0];

		return aFilterKriterium;
	}


	private boolean refreshReversechargeart() throws Throwable {
		if (null == getFinanzController().getFinanzamtIId())
			return false;

		FilterKriterium[] aFilterKriterium = getFilterForReversechargeart();

		if (null == panelReversechargeartQP1) {
			String[] aWhichButtonIUse = LPMain.getInstance().isLPAdmin() 
				? new String[] {
					PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 }
				: new String[] {
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 } ;

			panelReversechargeartQP1 = new PanelQuery(null, aFilterKriterium,
					QueryParameters.UC_ID_REVERSECHARGEART, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("fb.reversechargeart"), true);
		} else {
			panelReversechargeartQP1.setDefaultFilter(aFilterKriterium);
		}

		if (null == panelReversechargeartBottomD1) {
			panelReversechargeartBottomD1 = new PanelReversechargeart(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("fb.reversechargeart"), null,
					this);
		}

		if (null == panelReversechargeartSP1) {
			panelReversechargeartSP1 = new PanelSplit(getInternalFrame(),
					panelReversechargeartBottomD1, panelReversechargeartQP1, 200);
			setComponentAt(IDX_REVERSECHARGEART, panelReversechargeartSP1);
		}

		// liste soll sofort angezeigt werden
		panelReversechargeartQP1.eventYouAreSelected(true);
		return true;
	}
	
	private FilterKriterium[] getFilterForSteuerkategorie() throws Throwable {
		FilterKriterium[] aFilterKriterium = new FilterKriterium[3];

		aFilterKriterium[0] = SystemFilterFactory.getInstance()
				.createFKMandantCNr()[0];

		aFilterKriterium[1] = new FilterKriterium("finanzamt_i_id", true, "("
				+ getFinanzController().getFinanzamtIId() + ")",
				FilterKriterium.OPERATOR_EQUAL, false);

		aFilterKriterium[2] = new FilterKriterium("reversechargeart_i_id", true, "("
				+ getFinanzController().getReversechargeartIId() + ")", 
				FilterKriterium.OPERATOR_EQUAL, false) ;
		
		return aFilterKriterium;
	}

	
	private boolean refreshSteuerkategorie() throws Throwable {
		if (null == getFinanzController().getFinanzamtIId())
			return false;
		if(null == getFinanzController().getReversechargeartIId()) {
			return false ;
		}

		FilterKriterium[] aFilterKriterium = getFilterForSteuerkategorie();

		if (null == panelSteuerkategorieQP1) {
			String[] aWhichButtonIUse = new String[] {
					PanelBasis.ACTION_POSITION_VONNNACHNMINUS1,
					PanelBasis.ACTION_POSITION_VONNNACHNPLUS1 };

			panelSteuerkategorieQP1 = new PanelQuery(null, aFilterKriterium,
					QueryParameters.UC_ID_STEUERKATEGORIE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("fb.steuerkategorie"), true);
		} else {
			panelSteuerkategorieQP1.setDefaultFilter(aFilterKriterium);
		}

		if (null == panelSteuerkategorieBottomD1) {
			panelSteuerkategorieBottomD1 = new PanelSteuerkategorie(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("fb.steuerkategorie"), null,
					this);
		}

		if (null == panelSteuerkategorieSP1) {
			panelSteuerkategorieSP1 = new PanelSplit(getInternalFrame(),
					panelSteuerkategorieBottomD1, panelSteuerkategorieQP1, 200);
			setComponentAt(IDX_STEUERKATEGORIE, panelSteuerkategorieSP1);
		}

		// liste soll sofort angezeigt werden
		panelSteuerkategorieQP1.eventYouAreSelected(true);
		return true;
	}

	private boolean refreshSteuerkategoriekonto() throws Throwable {
		if (null == getFinanzController().getSteuerkategorieIId())
			return false;
		if (null == panelSteuerkategorieBottomD1)
			return false;

		if (null == panelSteuerkategoriekontoQP1) {
			panelSteuerkategoriekontoQP1 = new PanelQuery(null,
					SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_STEUERKATEGORIE, null,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("fb.steuerkategoriekonto"),
					true);
		}

		if (null == panelSteuerkategorieBottomkontoD1) {
			panelSteuerkategorieBottomkontoD1 = new PanelSteuerkategoriekonto(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("fb.steuerkategoriekonto"),
					null, this, panelSteuerkategorieBottomD1);
			setComponentAt(IDX_STEUERKATEGORIEKONTO,
					panelSteuerkategorieBottomkontoD1);
		}

		panelSteuerkategorieBottomkontoD1.refreshPanel();

		// liste soll sofort angezeigt werden
		panelSteuerkategoriekontoQP1.eventYouAreSelected(true);
		return true;
	}

	// private void updateDetailKeyForFinanzamt() throws Throwable {
	// Integer key = getFinanzController().getFinanzamtIId() ;
	//
	// if(null == key) return ;
	// getInternalFrame().setKeyWasForLockMe(key.toString());
	// getPanelDetailFinanzamtKopfdaten(true).setKeyWhenDetailPanel(key);
	//
	// PartnerDto partnerDto =
	// getFinanzController().getFinanzamtDto().getPartnerDto();
	// String sTitle = partnerDto == null ? "Neues Finanzamt" :
	// partnerDto.formatFixTitelName1Name2() ;
	// getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sTitle);
	// }

	/**
	 * hole FinanzamtDto.
	 *
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeFinanzamtDto(Object key) throws Throwable {
		if (key != null) {
			FinanzamtDto finanzamtDto = DelegateFactory
					.getInstance()
					.getFinanzDelegate()
					.finanzamtFindByPrimaryKey((Integer) key,
							LPMain.getInstance().getTheClient().getMandant());
			setFinanzamtDto(finanzamtDto);
			getInternalFrame().setKeyWasForLockMe(key.toString());
			getPanelDetailFinanzamtKopfdaten(true).setKeyWhenDetailPanel(key);
		}
	}

	/**
	 * getPanelQueryFinanzamt mit extrem lazy loading.
	 *
	 * @return PanelQuery
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @throws Throwable
	 */
	private PanelQuery getPanelQueryFinanzamt(boolean bNeedInstantiationIfNull)
			throws Throwable {
		if (panelQueryFinanzamt == null && bNeedInstantiationIfNull) {
			String[] aWhichButtonIUseFinanzamt = { PanelBasis.ACTION_NEW };
			QueryType[] qtKassenbuch = null;
			FilterKriterium[] filtersFinanzamt = SystemFilterFactory
					.getInstance().createFKMandantCNr();

			panelQueryFinanzamt = new PanelQuery(qtKassenbuch,
					filtersFinanzamt, QueryParameters.UC_ID_FINANZAMT,
					aWhichButtonIUseFinanzamt, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr("lp.auswahl"),
					true);
			setComponentAt(IDX_FINANZAEMTER, panelQueryFinanzamt);
		}
		return panelQueryFinanzamt;
	}

	/**
	 * getPanelDetailKassenbuchKopfdaten mit extrem lazy loading.
	 *
	 * @param bNeedInstantiationIfNull
	 *            boolean
	 * @return PanelFinanzKassenbuchKopfdaten
	 * @throws Throwable
	 */
	private PanelFinanzFinanzamtKopfdaten getPanelDetailFinanzamtKopfdaten(
			boolean bNeedInstantiationIfNull) throws Throwable {
		if (panelFinanzamtKopfdaten == null && bNeedInstantiationIfNull) {
//			panelFinanzamtKopfdaten = new PanelFinanzFinanzamtKopfdaten(
//					getInternalFrame(), LPMain.getInstance()
//							.getTextRespectUISPr("lp.kopfdaten"), null, this);
			panelFinanzamtKopfdaten = new PanelFinanzFinanzamtKopfdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kopfdaten"), null, this, this);
			this.setComponentAt(IDX_KOPFDATEN, panelFinanzamtKopfdaten);
		}
		return panelFinanzamtKopfdaten;
	}

	/**
	 * eventMenueAction.
	 *
	 * @param e
	 *            ActionEvent
	 */
	protected void lPActionEvent(ActionEvent e) throws Throwable {
		String cmd = e.getActionCommand();
		if (MENUE_JOURNAL_STEUERKATEGORIE.equals(cmd)) {
			String add2Title = LPMain.getInstance().getTextRespectUISPr(
					"finanz.report.steuerkategorien");
			getInternalFrame().showReportKriterien(
					new ReportSteuerkategorie(internalFrameFinanz, add2Title));
		} else if (MENU_ACTION_BEARBEITEN_UVAFORMULARPFLEGE.equals(cmd)) {
			DelegateFactory.getInstance().getFinanzServiceDelegate().pflegeUvaFormulare();
		}
	}

	/**
	 * eventStateChanged.
	 *
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		// lazy loading und titel setzen
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_FINANZAEMTER) {
			enableTabSteuerkategorieKonto(false);
			getPanelQueryFinanzamt(true).eventYouAreSelected(false);
		} else if (selectedIndex == IDX_KOPFDATEN) {
			enableTabSteuerkategorieKonto(true);
			getPanelDetailFinanzamtKopfdaten(true).eventYouAreSelected(false);
		} else if (selectedIndex == IDX_REVERSECHARGEART) {
			changeTabToReversechargeart();
			enableTabSteuerkategorie(true); 			
		} else if (selectedIndex == IDX_STEUERKATEGORIE) {
			changeTabToSteuerkategorie();
			enableTabSteuerkategorieKonto(true);
		} else if (selectedIndex == IDX_STEUERKATEGORIEKONTO) {
			changeTabToSteuerkategoriekonto();
		} else if (selectedIndex == IDX_BUCHUNGSPARAMETER) {
			refreshBuchungsparameter();
			panelBuchungsparameterD1.eventYouAreSelected(false);
		}
	}

	private void refreshBuchungsparameter() throws Throwable {
		if (panelBuchungsparameterD1 == null) {
			panelBuchungsparameterD1 = new PanelBuchungsparameter(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("fb.buchungsparameter"),
					panelQueryFinanzamt.getSelectedId(), this);
			setComponentAt(IDX_BUCHUNGSPARAMETER, panelBuchungsparameterD1);
		}
	}

	void enableTabSteuerkategorie(boolean enabled) {
		if(IDX_STEUERKATEGORIE != -1) {
			Component tabKategorieKonto = getComponentAt(IDX_STEUERKATEGORIE);
			if (null != tabKategorieKonto) {
				tabKategorieKonto.setEnabled(enabled);
			}			
		}
	}
	
	void enableTabSteuerkategorieKonto(boolean enabled) {
		if (IDX_STEUERKATEGORIEKONTO != -1) {
			Component tabKategorieKonto = getComponentAt(IDX_STEUERKATEGORIEKONTO);
			if (null != tabKategorieKonto) {
				tabKategorieKonto.setEnabled(enabled);
			}
		}
	}

	protected void changeTabToReversechargeart() throws Throwable {
		if (changedFinanzamt()) {
			if (createDefaultReversechargeart()) {
				setFinanzamt(getFinanzController().getFinanzamtIId());
			}
		}

		if (refreshReversechargeart() != true) {
			JOptionPane.showOptionDialog(this,
					LPMain.getTextRespectUISPr("lp.finanzamt.achtung"), "",
					JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE, null,
					null, null);
		} else {
			panelReversechargeartSP1.eventYouAreSelected(false);
		}		
	}
	
	protected void changeTabToSteuerkategorie() throws Throwable {
		createDefaultSteuerkategorien() ;
		
		if (changedFinanzamt()) {
			if (createDefaultSteuerkategorien()) {
				setFinanzamt(getFinanzController().getFinanzamtIId());
			}
		}

		// panelSteuerkategorieSP1 = null;
		if (refreshSteuerkategorie() != true) {
			String token = getFinanzController().getFinanzamtIId() == null 
					? "lp.finanzamt.achtung" : "fb.reversechargeart.achtung" ; 
			JOptionPane.showOptionDialog(this,
					LPMain.getTextRespectUISPr(token), "",
					JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE, null,
					null, null);
		} else {
			panelSteuerkategorieSP1.eventYouAreSelected(false);
		}
	}

	protected void changeTabToSteuerkategoriekonto() throws Throwable {
		if (changedSteuerkategorie()) {
			if (createDefaultSteuerkategoriekonto()) {
				setSteuerkategorie(getFinanzController()
						.getSteuerkategorieIId());
			}
		}

		if (refreshSteuerkategoriekonto()) {
			panelSteuerkategorieBottomkontoD1.eventYouAreSelected(false);
		} else {
			JOptionPane.showOptionDialog(this,
					LPMain.getTextRespectUISPr("lp.steuerkategorie.achtung"),
					"", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE,
					null, null, null);
		}
	}

	public FinanzamtDto getFinanzamtDto() {
		return finanzamtDto;
	}

	public void setFinanzamtDto(FinanzamtDto finanzamtDto) {
		this.finanzamtDto = finanzamtDto;
		if (getFinanzamtDto() != null) {
			PartnerDto partnerDto = finanzamtDto.getPartnerDto();
			String sTitle = null;
			if (partnerDto != null) {
				sTitle = partnerDto.formatFixTitelName1Name2();
			} else {
				sTitle = "Neues Finanzamt";
			}
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					sTitle);
		}
	}

	public String getFinanzamtTitle() {
		if(getFinanzamtDto() == null) return "" ;
		
		return getFinanzamtDto().getPartnerDto().getCName1nachnamefirmazeile1() ; 
	}
	
	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperManuBar == null) {

			wrapperManuBar = new WrapperMenuBar(this);

			if (LPMain
					.getInstance()
					.getDesktop()
					.darfAnwenderAufModulZugreifen(
							LocaleFac.BELEGART_FINANZBUCHHALTUNG)) {

				JMenu journal = (JMenu) wrapperManuBar
						.getComponent(WrapperMenuBar.MENU_JOURNAL);

				JMenuItem menuItemSteuerkategorien = new JMenuItem(LPMain
						.getInstance().getTextRespectUISPr(
								"finanz.report.steuerkategorien"));
				menuItemSteuerkategorien.addActionListener(this);
				menuItemSteuerkategorien
						.setActionCommand(MENUE_JOURNAL_STEUERKATEGORIE);
				journal.add(menuItemSteuerkategorien);
				
				JMenu jmDatei = (JMenu) wrapperManuBar
						.getComponent(WrapperMenuBar.MENU_MODUL);
				addPflegeUvaformularMenuItem(jmDatei);
			}

		}

		return wrapperManuBar;
	}

	private void addPflegeUvaformularMenuItem(JMenu menu) throws Throwable {
		WrapperMenuItem menueItem = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("finanz.pflegeuvaformular"),
				RechteFac.RECHT_FB_CHEFBUCHHALTER);
		menueItem.addActionListener(this);
		menueItem.setActionCommand(MENU_ACTION_BEARBEITEN_UVAFORMULARPFLEGE);
		menu.add(menueItem, 0);
	}
	
	private boolean changedFinanzamt() {
		return !getFinanzController().getFinanzamtIId().equals(
				previousFinanzamtIId);
		// return previousFinanzamtIId !=
		// getFinanzController().getFinanzamtIId() ;
	}

	private void setFinanzamt(Integer finanzamtIId) {
		previousFinanzamtIId = finanzamtIId;
	}

	private boolean changedReversechargeart() {
		return !getFinanzController().getReversechargeartIId().equals(previousReversechargeartId) ;
	}
	
	private void setReversechargeeartId(Integer reversechargeartId) {
		previousReversechargeartId = reversechargeartId ;
	}
	
	private boolean createDefaultReversechargeart() {		
		//TODO: Default Reverse charge arten erzeugen
		return true ;
	}
	
	private boolean createDefaultSteuerkategorien() {
		return getFinanzController().createDefaultSteuerkategorie();
	}

	private boolean changedSteuerkategorie() {
		if (null == previousSteuerkategoriekontoIId) {
			return null != getFinanzController().getSteuerkategorieIId();
		}

		return !previousSteuerkategoriekontoIId.equals(getFinanzController()
				.getSteuerkategorieIId());
		// return
		// !getFinanzController().getSteuerkategorieIId().equals(previousSteuerkategoriekontoIId)
		// ;
	}

	private void setSteuerkategorie(Integer steuerkategoriekontoIId) {
		previousSteuerkategoriekontoIId = steuerkategoriekontoIId;
	}

	private boolean createDefaultSteuerkategoriekonto() {
		return getFinanzController().createDefaultSteuerkategoriekonto();
	}

	@Override
	public void setServicePartnerDto(PartnerDto partnerDto) {
		getFinanzamtDto().setPartnerDto(partnerDto);
	}

	@Override
	public PartnerDto getServicePartnerDto() {
		if(getFinanzamtDto() == null) {
			return new PartnerDto();
		}
		return getFinanzamtDto().getPartnerDto();
	}

}
