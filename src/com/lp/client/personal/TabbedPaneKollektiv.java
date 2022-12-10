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
package com.lp.client.personal;

import javax.swing.event.ChangeEvent;

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
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.personal.service.PersonalFac;
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
public class TabbedPaneKollektiv extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryKollektiv = null;
	private PanelBasis panelDetailKollektiv = null;

	private PanelQuery panelQueryKollektivuestd = null;
	private PanelBasis panelSplitKollektivuestd = null;
	private PanelBasis panelDetailKollektivuestd = null;

	private PanelQuery panelQueryKollektivuestd50 = null;
	private PanelBasis panelSplitKollektivuestd50 = null;
	private PanelBasis panelDetailKollektivuestd50 = null;

	private PanelQuery panelQueryKollektivuestdBVA = null;
	private PanelBasis panelSplitKollektivuestdBVA = null;
	private PanelBasis panelDetailKollektivuestdBVA = null;
	
	private PanelQuery panelQueryPassivereise = null;
	private PanelBasis panelSplitPassivereise = null;
	private PanelBasis panelDetailPassivereise = null;

	private  static int IDX_PANEL_AUSWAHL = -1;
	private  static int IDX_PANEL_DETAIL = -1;
	private  static int IDX_PANEL_UESTD = -1;
	private  static int IDX_PANEL_UESTD50 = -1;
	private  static int IDX_PANEL_PASSIVEREISE = -1;

	private  static int IDX_PANEL_UESTD_BVA = -1;

	public TabbedPaneKollektiv(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"pers.personaldaten.kollektiv"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryMaterial() {
		return panelQueryKollektiv;
	}

	private void createAuswahl() throws Throwable {
		if (panelQueryKollektiv == null) {
			// Artikelauswahlliste
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryKollektiv = new PanelQuery(null, null,
					QueryParameters.UC_ID_KOLLEKTIV, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.auswahl"), true);

			panelQueryKollektiv.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDBezeichnung(),
					null);

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryKollektiv);

		}
	}

	private void createDetail(Integer key) throws Throwable {
		if (panelDetailKollektiv == null) {
			panelDetailKollektiv = new PanelKollektiv(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.detail"), key);
			setComponentAt(IDX_PANEL_DETAIL, panelDetailKollektiv);
		}
	}

	private void createKollektivuestd(Integer key) throws Throwable {

		if (panelQueryKollektivuestd == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = PersonalFilterFactory.getInstance()
					.createFKKollektivuestd(key);

			panelQueryKollektivuestd = new PanelQuery(null, filters,
					QueryParameters.UC_ID_KOLLEKTIVUESTD, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.personaldaten.kollektivuestd100"),
					true);

			panelDetailKollektivuestd = new PanelKollektivuestd(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.personaldaten.kollektivuestd100"),
					null);

			panelSplitKollektivuestd = new PanelSplit(getInternalFrame(),
					panelDetailKollektivuestd, panelQueryKollektivuestd, 350);

			setComponentAt(IDX_PANEL_UESTD, panelSplitKollektivuestd);
		} else {
			// filter refreshen.
			panelQueryKollektivuestd.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKKollektivuestd(key));
		}
	}
	private void createPassivereise(Integer key) throws Throwable {

		if (panelQueryPassivereise == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = PersonalFilterFactory.getInstance()
					.createFKPassivereise(key);

			panelQueryPassivereise = new PanelQuery(null, filters,
					QueryParameters.UC_ID_PASSIVEREISE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.kollektiv.passivereise"),
					true);

			panelDetailPassivereise = new PanelPassivereise(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.kollektiv.passivereise"),
					null);

			panelSplitPassivereise = new PanelSplit(getInternalFrame(),
					panelDetailPassivereise, panelQueryPassivereise, 350);

			setComponentAt(IDX_PANEL_PASSIVEREISE, panelSplitPassivereise);
		} else {
			// filter refreshen.
			panelQueryPassivereise.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKPassivereise(key));
		}
	}

	private void createKollektivuestdBVA(Integer key) throws Throwable {

		if (panelQueryKollektivuestdBVA == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = PersonalFilterFactory.getInstance()
					.createFKKollektivuestd(key);

			panelQueryKollektivuestdBVA = new PanelQuery(null, filters,
					QueryParameters.UC_ID_KOLLEKTIVUESTDBVA, aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"pers.kollektiv.uest.betriebsvereinbarungA"), true);

			panelDetailKollektivuestdBVA = new PanelKollektivUestdBVA(
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr(
							"pers.kollektiv.uest.betriebsvereinbarungA"), null);

			panelSplitKollektivuestdBVA = new PanelSplit(getInternalFrame(),
					panelDetailKollektivuestdBVA, panelQueryKollektivuestdBVA,
					350);

			setComponentAt(IDX_PANEL_UESTD_BVA, panelSplitKollektivuestdBVA);
		} else {
			// filter refreshen.
			panelQueryKollektivuestdBVA.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKKollektivuestd(key));
		}
	}

	private void createKollektivuestd50(Integer key) throws Throwable {

		if (panelQueryKollektivuestd50 == null) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			FilterKriterium[] filters = PersonalFilterFactory.getInstance()
					.createFKKollektivuestd(key);

			panelQueryKollektivuestd50 = new PanelQuery(null, filters,
					QueryParameters.UC_ID_KOLLEKTIVUESTD50, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.personaldaten.kollektivuestd50"),
					true);

			panelDetailKollektivuestd50 = new PanelKollektivuestd50(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr(
									"pers.personaldaten.kollektivuestd50"),
					null);

			panelSplitKollektivuestd50 = new PanelSplit(getInternalFrame(),
					panelDetailKollektivuestd50, panelQueryKollektivuestd50,
					350);

			setComponentAt(IDX_PANEL_UESTD50, panelSplitKollektivuestd50);
		} else {
			// filter refreshen.
			panelQueryKollektivuestd50.setDefaultFilter(PersonalFilterFactory
					.getInstance().createFKKollektivuestd(key));
		}
	}

	private void jbInit() throws Throwable {

		
		IDX_PANEL_AUSWAHL = reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.auswahl"), null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.auswahl"));
		
		

		IDX_PANEL_DETAIL =reiterHinzufuegen(LPMain.getInstance().getTextRespectUISPr("lp.detail"), null,
				null, LPMain.getInstance().getTextRespectUISPr("lp.detail"));
		IDX_PANEL_UESTD =reiterHinzufuegen(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.personaldaten.kollektivuestd100"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.personaldaten.kollektivuestd100"));
		IDX_PANEL_UESTD50 =reiterHinzufuegen(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.personaldaten.kollektivuestd50"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.personaldaten.kollektivuestd50"));
		IDX_PANEL_PASSIVEREISE =reiterHinzufuegen(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.kollektiv.passivereise"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.kollektiv.passivereise"));
		IDX_PANEL_UESTD_BVA =	reiterHinzufuegen(
				LPMain.getInstance().getTextRespectUISPr(
						"pers.kollektiv.uest.betriebsvereinbarungA"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.kollektiv.uest.betriebsvereinbarungA"));

		createAuswahl();

		panelQueryKollektiv.eventYouAreSelected(false);
		if ((Integer) panelQueryKollektiv.getSelectedId() != null) {
			getInternalFramePersonal().setKollektivDto(
					DelegateFactory
							.getInstance()
							.getPersonalDelegate()
							.kollektivFindByPrimaryKey(
									(Integer) panelQueryKollektiv
											.getSelectedId()));
		}
		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryKollektiv,
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	public void disableUest100() {
		if (getInternalFramePersonal().getKollektivDto() != null
				&& getInternalFramePersonal().getKollektivDto()
						.getIBerechnungsbasis() != null
				&& getInternalFramePersonal().getKollektivDto()
						.getIBerechnungsbasis() != PersonalFac.KOLLEKTIV_BERECHNUNGSBASIS_UHRZEIT) {
			setEnabledAt(IDX_PANEL_UESTD, false);
		}
	}

	public void disableBetriebsvereinbarung() {
		if (getInternalFramePersonal().getKollektivDto() != null
				&& getInternalFramePersonal().getKollektivDto()
						.getCAbrechungsart() != null
				&& getInternalFramePersonal().getKollektivDto()
						.getCAbrechungsart()
						.equals(PersonalFac.KOLLEKTIV_ABRECHNUNGSART_STANDARD)) {
			setEnabledAt(IDX_PANEL_UESTD_BVA, false);
		}
		if (getInternalFramePersonal().getKollektivDto() != null
				&& getInternalFramePersonal().getKollektivDto()
						.getCAbrechungsart() != null
				&& getInternalFramePersonal()
						.getKollektivDto()
						.getCAbrechungsart()
						.equals(PersonalFac.KOLLEKTIV_ABRECHNUNGSART_BETRIEBSVEREINBARUNG_A)) {
			setEnabledAt(IDX_PANEL_UESTD50, false);
			setEnabledAt(IDX_PANEL_UESTD, false);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryKollektiv) {
				Integer iId = (Integer) panelQueryKollektiv.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailKollektiv);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelDetailKollektivuestd) {
				panelSplitKollektivuestd.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKollektivuestd50) {
				panelSplitKollektivuestd50.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKollektiv) {
				panelDetailKollektiv.eventYouAreSelected(false);

			}  else if (e.getSource() == panelDetailPassivereise) {
				panelSplitPassivereise.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailKollektivuestdBVA) {
				panelSplitKollektivuestdBVA.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelDetailKollektivuestd) {
				panelQueryKollektivuestd.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailKollektivuestd50) {
				panelQueryKollektivuestd50.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			} else if (e.getSource() == panelDetailKollektivuestdBVA) {
				panelQueryKollektivuestdBVA.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}else if (e.getSource() == panelDetailPassivereise) {
				panelQueryPassivereise.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailKollektiv) {
				panelQueryKollektiv.clearDirektFilter();
				Object oKey = panelDetailKollektiv.getKeyWhenDetailPanel();
				panelQueryKollektiv.setSelectedId(oKey);
			} else if (e.getSource() == panelDetailKollektivuestd) {
				Object oKey = panelDetailKollektivuestd.getKeyWhenDetailPanel();
				panelQueryKollektivuestd.eventYouAreSelected(false);
				panelQueryKollektivuestd.setSelectedId(oKey);
				panelSplitKollektivuestd.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailPassivereise) {
				Object oKey = panelDetailPassivereise.getKeyWhenDetailPanel();
				panelQueryPassivereise.eventYouAreSelected(false);
				panelQueryPassivereise.setSelectedId(oKey);
				panelSplitPassivereise.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailKollektivuestd50) {
				Object oKey = panelDetailKollektivuestd50
						.getKeyWhenDetailPanel();
				panelQueryKollektivuestd50.eventYouAreSelected(false);
				panelQueryKollektivuestd50.setSelectedId(oKey);
				panelSplitKollektivuestd50.eventYouAreSelected(false);

			} else if (e.getSource() == panelDetailKollektivuestdBVA) {
				Object oKey = panelDetailKollektivuestdBVA
						.getKeyWhenDetailPanel();
				panelQueryKollektivuestdBVA.eventYouAreSelected(false);
				panelQueryKollektivuestdBVA.setSelectedId(oKey);
				panelSplitKollektivuestdBVA.eventYouAreSelected(false);

			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryKollektivuestd) {
				Integer iId = (Integer) panelQueryKollektivuestd
						.getSelectedId();
				panelDetailKollektivuestd.setKeyWhenDetailPanel(iId);
				panelDetailKollektivuestd.eventYouAreSelected(false);
				panelQueryKollektivuestd.updateButtons();
			} else if (e.getSource() == panelQueryKollektivuestd50) {
				Integer iId = (Integer) panelQueryKollektivuestd50
						.getSelectedId();
				panelDetailKollektivuestd50.setKeyWhenDetailPanel(iId);
				panelDetailKollektivuestd50.eventYouAreSelected(false);
				panelQueryKollektivuestd50.updateButtons();
			} else if (e.getSource() == panelQueryKollektivuestdBVA) {
				Integer iId = (Integer) panelQueryKollektivuestdBVA
						.getSelectedId();
				panelDetailKollektivuestdBVA.setKeyWhenDetailPanel(iId);
				panelDetailKollektivuestdBVA.eventYouAreSelected(false);
				panelQueryKollektivuestdBVA.updateButtons();
			} else if (e.getSource() == panelQueryPassivereise) {
				Integer iId = (Integer) panelQueryPassivereise
						.getSelectedId();
				panelDetailPassivereise.setKeyWhenDetailPanel(iId);
				panelDetailPassivereise.eventYouAreSelected(false);
				panelQueryPassivereise.updateButtons();
			} else if (e.getSource() == panelQueryKollektiv) {
				if (panelQueryKollektiv.getSelectedId() != null) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();

					getInternalFramePersonal().setKeyWasForLockMe(key + "");

					getInternalFramePersonal().setKollektivDto(
							DelegateFactory.getInstance().getPersonalDelegate()
									.kollektivFindByPrimaryKey((Integer) key));
					String sBezeichnung = "";
					getInternalFrame().setLpTitle(
							InternalFrame.TITLE_IDX_AS_I_LIKE,
							LPMain.getInstance().getTextRespectUISPr(
									"pers.personaldaten.kollektiv")
									+ ": "
									+ getInternalFramePersonal()
											.getKollektivDto().getCBez());

					if (panelQueryKollektiv.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this,
								IDX_PANEL_AUSWAHL, true);
					}

					disableUest100();
					disableBetriebsvereinbarung();

				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_PANEL_AUSWAHL, false);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailKollektiv) {
				getInternalFrame().enableAllPanelsExcept(true);
				this.setSelectedComponent(panelQueryKollektiv);
				setKeyWasForLockMe();
				panelQueryKollektiv.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKollektivuestd) {
				setKeyWasForLockMe();
				if (panelDetailKollektivuestd.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryKollektivuestd
							.getId2SelectAfterDelete();
					panelQueryKollektivuestd.setSelectedId(oNaechster);
				}
				panelSplitKollektivuestd.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKollektivuestd50) {
				setKeyWasForLockMe();
				if (panelDetailKollektivuestd50.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryKollektivuestd50
							.getId2SelectAfterDelete();
					panelQueryKollektivuestd50.setSelectedId(oNaechster);
				}
				panelSplitKollektivuestd50.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailPassivereise) {
				setKeyWasForLockMe();
				if (panelDetailPassivereise.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryPassivereise
							.getId2SelectAfterDelete();
					panelQueryPassivereise.setSelectedId(oNaechster);
				}
				panelSplitPassivereise.eventYouAreSelected(false);
			} else if (e.getSource() == panelDetailKollektivuestdBVA) {
				setKeyWasForLockMe();
				if (panelDetailKollektivuestdBVA.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryKollektivuestdBVA
							.getId2SelectAfterDelete();
					panelQueryKollektivuestdBVA.setSelectedId(oNaechster);
				}
				panelSplitKollektivuestdBVA.eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryKollektiv) {

				createDetail((Integer) panelQueryKollektiv.getSelectedId());
				if (panelQueryKollektiv.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailKollektiv.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailKollektiv);
			} else if (e.getSource() == panelQueryKollektivuestd) {
				panelDetailKollektivuestd.eventActionNew(e, true, false);
				panelDetailKollektivuestd.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitKollektivuestd);
			} else if (e.getSource() == panelQueryKollektivuestd50) {
				panelDetailKollektivuestd50.eventActionNew(e, true, false);
				panelDetailKollektivuestd50.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitKollektivuestd50);
			} else if (e.getSource() == panelQueryKollektivuestdBVA) {
				panelDetailKollektivuestdBVA.eventActionNew(e, true, false);
				panelDetailKollektivuestdBVA.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitKollektivuestdBVA);
			} else if (e.getSource() == panelQueryPassivereise) {
				panelDetailPassivereise.eventActionNew(e, true, false);
				panelDetailPassivereise.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitPassivereise);
			}
		}

	}

	public InternalFramePersonal getInternalFramePersonal() {
		return (InternalFramePersonal) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryKollektiv.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr(
						"pers.personaldaten.kollektiv"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

		String sBezeichnung = "";
		if (getInternalFramePersonal().getKollektivDto() != null) {
			getInternalFrame().setLpTitle(
					InternalFrame.TITLE_IDX_AS_I_LIKE,
					LPMain.getInstance().getTextRespectUISPr(
							"pers.personaldaten.kollektiv")
							+ ": "
							+ getInternalFramePersonal().getKollektivDto()
									.getCBez());
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryKollektiv.eventYouAreSelected(false);
			if (panelQueryKollektiv.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this,
						IDX_PANEL_AUSWAHL, false);
			}

			panelQueryKollektiv.updateButtons();
		}

		else if (selectedIndex == IDX_PANEL_DETAIL) {
			Integer key = null;
			if (getInternalFramePersonal().getKollektivDto() != null) {
				key = getInternalFramePersonal().getKollektivDto().getIId();
			}
			createDetail(key);
			panelDetailKollektiv.eventYouAreSelected(false);

		} else if (selectedIndex == IDX_PANEL_UESTD) {
			createKollektivuestd(getInternalFramePersonal().getKollektivDto()
					.getIId());
			panelSplitKollektivuestd.eventYouAreSelected(false);
			panelQueryKollektivuestd.updateButtons();
		} else if (selectedIndex == IDX_PANEL_PASSIVEREISE) {
			createPassivereise(getInternalFramePersonal().getKollektivDto()
					.getIId());
			panelSplitPassivereise.eventYouAreSelected(false);
			panelQueryPassivereise.updateButtons();
		} else if (selectedIndex == IDX_PANEL_UESTD50) {
			createKollektivuestd50(getInternalFramePersonal().getKollektivDto()
					.getIId());
			panelSplitKollektivuestd50.eventYouAreSelected(false);
			panelQueryKollektivuestd50.updateButtons();
		} else if (selectedIndex == IDX_PANEL_UESTD_BVA) {
			createKollektivuestdBVA(getInternalFramePersonal()
					.getKollektivDto().getIId());
			panelSplitKollektivuestdBVA.eventYouAreSelected(false);
			panelQueryKollektivuestdBVA.updateButtons();
		}
		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

}
