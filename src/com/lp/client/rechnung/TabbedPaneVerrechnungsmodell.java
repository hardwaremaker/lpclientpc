package com.lp.client.rechnung;

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

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.ICopyPaste;
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
import com.lp.client.frame.component.WrapperMenuItem;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.InternalFramePersonal;
import com.lp.client.personal.PanelZeitmodell;
import com.lp.client.personal.PanelZeitmodelltag;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.rechnung.service.VerrechnungsmodelltagDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.BelegpositionDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class TabbedPaneVerrechnungsmodell extends TabbedPane implements ICopyPaste {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryVerrechnungsmodell = null;
	private PanelBasis panelDetailVerrechnungsmodelll = null;

	private PanelQuery panelQueryVerrechnungsmodelltage = null;
	private PanelBasis panelBottomVerrechnungsmodelltage = null;
	private PanelSplit panelSplitVerrechnungsmodelltage = null;

	private final static int IDX_PANEL_AUSWAHL = 0;
	private final static int IDX_PANEL_DETAIL = 1;
	private final static int IDX_PANEL_TAGE = 2;

	private static final String ACTION_SPECIAL_ZEITMODELLTAG_KOPIEREN = "ACTION_SPECIAL_ZEITMODELLTAG_KOPIEREN";

	private final String BUTTON_ZEITMODELLTAG_KOPIEREN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_ZEITMODELLTAG_KOPIEREN;

	private final static String MENUE_INFO_VERRECHNUNGSMODELL = "menu_info_verrechnungsmodell";

	public TabbedPaneVerrechnungsmodell(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("rech.verrechnungmodell"));

		jbInit();
		initComponents();
	}

	public PanelQuery getPanelQueryZeitmodell() {
		return panelQueryVerrechnungsmodell;
	}

	public void deleteVerrechnungsmodelltag() throws ExceptionLP, Throwable {
		if (panelQueryVerrechnungsmodelltage.getSelectedIds() == null)
			return;
		for (Object id : panelQueryVerrechnungsmodelltage.getSelectedIds()) {

			VerrechnungsmodelltagDto verrechnungsmodelltagDto = DelegateFactory.getInstance().getRechnungDelegate()
					.verrechnungsmodelltagFindByPrimaryKey((Integer) id);

			try {
				DelegateFactory.getInstance().getRechnungDelegate()
						.removeVerrechnungsmodelltag(verrechnungsmodelltagDto);
			} catch (Exception e) {
				//
			}
		}

	}

	public void copyHV() throws ExceptionLP, Throwable {
		int selectedPanelIndex = this.getSelectedIndex();

		if (selectedPanelIndex == IDX_PANEL_TAGE) {
			Object aoIIdPosition[] = panelQueryVerrechnungsmodelltage.getSelectedIds();

			if (aoIIdPosition != null && (aoIIdPosition.length > 0)) {
				VerrechnungsmodelltagDto[] dtos = new VerrechnungsmodelltagDto[aoIIdPosition.length];
				Integer aIIdPosition[] = new Integer[aoIIdPosition.length];
				for (int i = 0; i < aIIdPosition.length; i++) {
					aIIdPosition[i] = (Integer) aoIIdPosition[i];
					dtos[i] = DelegateFactory.getInstance().getRechnungDelegate()
							.verrechnungsmodelltagFindByPrimaryKey((Integer) aoIIdPosition[i]);
				}
				LPMain.getInstance().getPasteBuffer().writeObjectToPasteBuffer(dtos);
			}
		}
	}

	public void fillMustFields(BelegpositionDto belegposDtoI, int xalOfStklPosI) throws Throwable {

	}

	public void einfuegenHV() throws IOException, ParserConfigurationException, SAXException, Throwable {

		Object o = LPMain.getInstance().getPasteBuffer().readObjectFromPasteBuffer();

		int selectedPanelIndex = this.getSelectedIndex();
		if (o instanceof VerrechnungsmodelltagDto[]) {
			if (selectedPanelIndex == IDX_PANEL_TAGE) {

				VerrechnungsmodelltagDto[] dtos = (VerrechnungsmodelltagDto[]) o;

				Integer iId = null;

				for (int i = 0; i < dtos.length; i++) {
					VerrechnungsmodelltagDto positionDto = dtos[i];
					try {
						positionDto.setIId(null);
						positionDto.setVerrechnungsmodellIId(
								getInternalFrameRechnung().getVerrechnungsmodellDto().getIId());

						// wir legen eine neue position an
						iId = DelegateFactory.getInstance().getRechnungDelegate()
								.createVerrechnungsmodelltag(positionDto);

					} catch (Throwable t) {
						// nur loggen!
						myLogger.error(t.getMessage(), t);
					}
				}

				// den Datensatz in der Liste selektieren
				panelQueryVerrechnungsmodelltage.setSelectedId(iId);

				// die Liste neu aufbauen
				panelQueryVerrechnungsmodelltage.eventYouAreSelected(false);

				// im Detail den selektierten anzeigen
				panelSplitVerrechnungsmodelltage.eventYouAreSelected(false);

			}
		}

	}

	private void jbInit() throws Throwable {

		// Zeitmodellauswahlliste

		FilterKriterium[] kriterien = new FilterKriterium[1];
		kriterien[0] = new FilterKriterium("modell.mandant_c_nr", true,
				"'" + LPMain.getInstance().getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);

		String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };
		panelQueryVerrechnungsmodell = new PanelQuery(PersonalFilterFactory.getInstance().createQTZeitmodell(),
				kriterien, QueryParameters.UC_ID_VERRECHNUNGSMODELL, aWhichButtonIUse, getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("auft.title.panel.auswahl"), true);

		panelQueryVerrechnungsmodell.befuellePanelFilterkriterienDirektUndVersteckte(
				new FilterKriteriumDirekt("modell.c_bez", "", FilterKriterium.OPERATOR_LIKE,
						LPMain.getInstance().getTextRespectUISPr("label.bezeichnung"),
						FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
						true, true, Facade.MAX_UNBESCHRAENKT),
				null, new FilterKriterium("modell." + ZeiterfassungFac.FLR_ZEITMODELL_B_VERSTECKT, true, "(1)",
						FilterKriterium.OPERATOR_NOT_IN, false));

		addTab(LPMain.getInstance().getTextRespectUISPr("auft.title.panel.auswahl"), panelQueryVerrechnungsmodell);
		panelQueryVerrechnungsmodell.eventYouAreSelected(false);

		if ((Integer) panelQueryVerrechnungsmodell.getSelectedId() != null) {
			getInternalFrameRechnung().setVerrechnungsmodellDto(DelegateFactory.getInstance().getRechnungDelegate()
					.verrechnungsmodellFindByPrimaryKey((Integer) panelQueryVerrechnungsmodell.getSelectedId()));
		}

		// Zeitmodelldetail
		panelDetailVerrechnungsmodelll = new PanelVerrechnungsmodell(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("lp.detail"), panelQueryVerrechnungsmodell.getSelectedId());
		addTab(LPMain.getInstance().getTextRespectUISPr("lp.detail"), panelDetailVerrechnungsmodelll);

		// Zeitmodelltag

		String[] aWhichButtonIUseTage = { PanelBasis.ACTION_NEW, PanelBasis.ACTION_KOPIEREN,
				PanelBasis.ACTION_EINFUEGEN_LIKE_NEW };

		panelQueryVerrechnungsmodelltage = new PanelQuery(null,
				PersonalFilterFactory.getInstance()
						.createFKZeitmodelltag((Integer) panelQueryVerrechnungsmodell.getSelectedId()),
				QueryParameters.UC_ID_VERRECHNUNGSMODELLTAG, aWhichButtonIUseTage, getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("rech.verrechnungmodelltag"), true);

		panelQueryVerrechnungsmodelltage.createAndSaveAndShowButton("/com/lp/client/res/goto.png",
				LPMain.getInstance().getTextRespectUISPr("pers.zeitmodelltag.zeitmodelltagkopieren"),
				BUTTON_ZEITMODELLTAG_KOPIEREN, null);

		panelBottomVerrechnungsmodelltage = new PanelVerrechnungsmodelltag(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("rech.verrechnungmodelltag"), null);

		panelQueryVerrechnungsmodelltage.setFilterComboBox(
				DelegateFactory.getInstance().getZeiterfassungDelegate().getAllSprTagesarten(),
				new FilterKriterium("flrtagesart.i_id", true, "" + "", FilterKriterium.OPERATOR_EQUAL, false), false,
				null, false);
		panelQueryVerrechnungsmodelltage.setMultipleRowSelectionEnabled(true);

		panelSplitVerrechnungsmodelltage = new PanelSplit(getInternalFrame(), panelBottomVerrechnungsmodelltage,
				panelQueryVerrechnungsmodelltage, 300);
		addTab(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungmodelltag"), panelSplitVerrechnungsmodelltage);

		panelQueryVerrechnungsmodell.eventYouAreSelected(false);

		// Itemevents an MEIN Detailpanel senden kann.
		getInternalFrame().addItemChangedListener(this);
		refreshTitle();
		this.addChangeListener(this);

	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryVerrechnungsmodell.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	private void refreshTitle() {

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("lp.zeitmodell"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());
		if (getInternalFrameRechnung().getVerrechnungsmodellDto() != null) {
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameRechnung().getVerrechnungsmodellDto().getCBez());
		}

	}

	public InternalFrameRechnung getInternalFrameRechnung() {
		return (InternalFrameRechnung) getInternalFrame();
	}

	protected void lPActionEvent(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(MENUE_INFO_VERRECHNUNGSMODELL)) {

			if (panelQueryVerrechnungsmodell.getSelectedId() != null) {
				getInternalFrame().showReportKriterien(new ReportVerrechnungsmodell(getInternalFrameRechnung(),
						(Integer) panelQueryVerrechnungsmodell.getSelectedId(),
						LPMain.getTextRespectUISPr("rech.verrechnungmodell")));
			}

		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryVerrechnungsmodell) {
				Integer iId = (Integer) panelQueryVerrechnungsmodell.getSelectedId();
				if (iId != null) {
					setSelectedComponent(panelDetailVerrechnungsmodelll);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == panelBottomVerrechnungsmodelltage) {
				panelSplitVerrechnungsmodelltage.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (e.getSource() == panelBottomVerrechnungsmodelltage) {
				panelQueryVerrechnungsmodelltage.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

			if (e.getSource() == panelDetailVerrechnungsmodelll) {
				panelQueryVerrechnungsmodell.clearDirektFilter();
				Object oKey = panelDetailVerrechnungsmodelll.getKeyWhenDetailPanel();

				panelQueryVerrechnungsmodell.setSelectedId(oKey);
			}

			else if (e.getSource() == panelBottomVerrechnungsmodelltage) {

				panelQueryVerrechnungsmodelltage.setDefaultFilter(RechnungFilterFactory.getInstance()
						.createFKVerrechnungsmodelltag(getInternalFrameRechnung().getVerrechnungsmodellDto().getIId()));
				Object oKey = panelBottomVerrechnungsmodelltage.getKeyWhenDetailPanel();

				panelQueryVerrechnungsmodelltage.eventYouAreSelected(false);
				panelQueryVerrechnungsmodelltage.setSelectedId(oKey);
				panelSplitVerrechnungsmodelltage.eventYouAreSelected(false);

			}

		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryVerrechnungsmodell) {
				if (panelQueryVerrechnungsmodell.getSelectedId() != null) {
					getInternalFrameRechnung().setKeyWasForLockMe(panelQueryVerrechnungsmodell.getSelectedId() + "");

					// Dto-setzen

					getInternalFrameRechnung().setVerrechnungsmodellDto(
							DelegateFactory.getInstance().getRechnungDelegate().verrechnungsmodellFindByPrimaryKey(
									(Integer) panelQueryVerrechnungsmodell.getSelectedId()));
					getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
							getInternalFrameRechnung().getVerrechnungsmodellDto().getCBez());

					// Default- Filter vorbesetzten
					panelQueryVerrechnungsmodelltage
							.setDefaultFilter(RechnungFilterFactory.getInstance().createFKVerrechnungsmodelltag(
									getInternalFrameRechnung().getVerrechnungsmodellDto().getIId()));

					if (panelQueryVerrechnungsmodell.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
					}

				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
				}

			} else if (e.getSource() == panelQueryVerrechnungsmodelltage) {

				Integer iId = (Integer) panelQueryVerrechnungsmodelltage.getSelectedId();
				panelBottomVerrechnungsmodelltage.setKeyWhenDetailPanel(iId);
				panelBottomVerrechnungsmodelltage.eventYouAreSelected(false);
				panelQueryVerrechnungsmodelltage.updateButtons();
			}

		}

		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			// aktiviere ein QP ...
			if (e.getSource() == panelDetailVerrechnungsmodelll) {
				this.setSelectedComponent(panelQueryVerrechnungsmodell);
				setKeyWasForLockMe();
				panelQueryVerrechnungsmodell.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomVerrechnungsmodelltage) {
				setKeyWasForLockMe();
				if (panelBottomVerrechnungsmodelltage.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryVerrechnungsmodelltage.getId2SelectAfterDelete();
					panelQueryVerrechnungsmodelltage.setSelectedId(oNaechster);
				}
				panelSplitVerrechnungsmodelltage.eventYouAreSelected(false);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
			super.lPEventItemChanged(e);
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == panelQueryVerrechnungsmodell) {
				if (panelQueryVerrechnungsmodell.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelDetailVerrechnungsmodelll.eventActionNew(null, true, false);
				setSelectedComponent(panelDetailVerrechnungsmodelll);
			} else if (e.getSource() == panelQueryVerrechnungsmodelltage) {
				panelBottomVerrechnungsmodelltage.eventActionNew(e, true, false);
				panelBottomVerrechnungsmodelltage.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitVerrechnungsmodelltage);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.endsWith(ACTION_SPECIAL_ZEITMODELLTAG_KOPIEREN)) {
				if (panelQueryVerrechnungsmodelltage.getSelectedId() != null) {
					Integer zeitmodelltagIIdNeu = DelegateFactory.getInstance().getRechnungDelegate()
							.kopiereVerrechnungsmodelltag((Integer) panelQueryVerrechnungsmodelltage.getSelectedId());

					if (zeitmodelltagIIdNeu == null) {
						DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
								LPMain.getInstance()
										.getTextRespectUISPr("pers.zeitmodelltag.zeitmodelltagkopieren.keinfreiertag"));
					} else {
						panelQueryVerrechnungsmodelltage.eventYouAreSelected(false);
						panelQueryVerrechnungsmodelltage.setSelectedId(zeitmodelltagIIdNeu);
						panelSplitVerrechnungsmodelltage.eventYouAreSelected(false);
					}

				}
			} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)) {
				einfuegenHV();
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			copyHV();
		}

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			panelQueryVerrechnungsmodell.eventYouAreSelected(false);
			if (panelQueryVerrechnungsmodell.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
			}
			panelQueryVerrechnungsmodell.updateButtons();
		} else if (selectedIndex == IDX_PANEL_DETAIL) {
			panelDetailVerrechnungsmodelll.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_PANEL_TAGE) {
			if (getInternalFrameRechnung().getVerrechnungsmodellDto() != null) {
				panelQueryVerrechnungsmodelltage.setDefaultFilter(RechnungFilterFactory.getInstance()
						.createFKVerrechnungsmodelltag(getInternalFrameRechnung().getVerrechnungsmodellDto().getIId()));
			}

			panelSplitVerrechnungsmodelltage.eventYouAreSelected(false);
			panelQueryVerrechnungsmodelltage.updateButtons();
		}
		refreshTitle();

	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {

		WrapperMenuBar wrapperMenuBar = new WrapperMenuBar(this);

		JMenu menuInfo = new WrapperMenu("lp.info", this);

		JMenuItem menuInfoVerrechungsmodell = new JMenuItem(LPMain.getTextRespectUISPr("rech.verrechnungmodell"));
		menuInfoVerrechungsmodell.addActionListener(this);
		menuInfoVerrechungsmodell.setActionCommand(MENUE_INFO_VERRECHNUNGSMODELL);

		menuInfo.add(menuInfoVerrechungsmodell);
		wrapperMenuBar.add(menuInfo);

		return wrapperMenuBar;
	}

}
