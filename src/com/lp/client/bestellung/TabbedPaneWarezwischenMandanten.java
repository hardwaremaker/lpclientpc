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
package com.lp.client.bestellung;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;

import com.lp.client.fertigung.InternalFrameFertigung;
import com.lp.client.fertigung.ReportMaschineUndMaterial;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>
 * &UUml;berschrift:
 * </p>
 * <p>
 * Beschreibung:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation:
 * </p>
 * 
 * @author Christian Kollmann
 * @version $Revision: 1.5 $
 */
public class TabbedPaneWarezwischenMandanten extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryWarezwischenmandanten = null;

	private final String ACTION_SPECIAL_WE_BUCHEN = PanelBasis.ALWAYSENABLED + "action_special_we_buchen";

	private final String ACTION_SPECIAL_SELEKTIERTE_ZEILEN_BUCHEN = PanelBasis.ALWAYSENABLED
			+ "action_special_selektierte_zeilen_buchen";

	private final static int IDX_PANEL_AUSWAHL = 0;

	private WrapperMenuBar wrapperMenuBar = null;

	public TabbedPaneWarezwischenMandanten(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("bes.warezwischenmandanten"));
		jbInit();
		initComponents();

	}

	public PanelQuery getPanelQueryWiederholende() {
		return panelQueryWarezwischenmandanten;
	}

	private void jbInit() throws Throwable {

		insertTab(LPMain.getInstance().getTextRespectUISPr("bes.warezwischenmandanten"), null, null,
				LPMain.getInstance().getTextRespectUISPr("bes.warezwischenmandanten"), IDX_PANEL_AUSWAHL);

		createAuswahl();

		panelQueryWarezwischenmandanten.eventYouAreSelected(false);

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelQueryWarezwischenmandanten, ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		this.addChangeListener(this);
		this.getInternalFrame().addItemChangedListener(this);

	}

	private void createAuswahl() throws Throwable {
		if (panelQueryWarezwischenmandanten == null) {

			// Filter

			FilterKriterium fkv = new FilterKriterium("NUR_UNTERWEGS", true, "(1)", // wenn
					// das
					// Kriterium
					// verwendet
					// wird,
					// sollen
					// die
					// versteckten
					// nicht
					// mitangezeigt
					// werden
					FilterKriterium.OPERATOR_NOT_IN, false);

			FilterKriterium[] kriterien = new FilterKriterium[1];
			kriterien[0] = new FilterKriterium("flrbestellposition.flrbestellung.mandant_c_nr", true,
					"'" + LPMain.getInstance().getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL,
					false);

			panelQueryWarezwischenmandanten = new PanelQuery(null, kriterien,
					QueryParameters.UC_ID_WAREZWISCHENMANDANTEN, null, getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("bes.warezwischenmandanten"), true, fkv,
					LPMain.getInstance().getTextRespectUISPr("bes.wareunterwegs.nurunterwegs"));

			FilterKriteriumDirekt fkDirekt1 = BestellungFilterFactory.getInstance()
					.createFKDWarewischenMandantenArtikelnummer();

			FilterKriteriumDirekt fkDirekt2 = new FilterKriteriumDirekt("flrlieferscheinposition.flrlieferschein.c_nr",
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("label.lieferschein"),
					FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX%'
					true, // wrapWithSingleQuotes
					false, Facade.MAX_UNBESCHRAENKT);

			panelQueryWarezwischenmandanten.befuellePanelFilterkriterienDirekt(fkDirekt1, fkDirekt2);

			panelQueryWarezwischenmandanten.addDirektFilter(new FilterKriteriumDirekt(
					"flrbestellposition.flrbestellung.c_nr", "", FilterKriterium.OPERATOR_LIKE,
					LPMain.getTextRespectUISPr("label.bestellnummer"), FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung
																												// als
																												// '%XX'
					true, // wrapWithSingleQuotes
					false, Facade.MAX_UNBESCHRAENKT));

			panelQueryWarezwischenmandanten.createAndSaveAndShowButton("/com/lp/client/res/document_check16x16.png",
					LPMain.getTextRespectUISPr("bes.warezwischenmandanten.webuchen"), ACTION_SPECIAL_WE_BUCHEN,
					RechteFac.RECHT_BES_WARENEINGANG_CUD);

			panelQueryWarezwischenmandanten.createAndSaveAndShowButton("/com/lp/client/res/document_ok.png",
					LPMain.getTextRespectUISPr("bes.warezwischenmandanten.nurselektiertezeilen"),
					ACTION_SPECIAL_SELEKTIERTE_ZEILEN_BUCHEN, RechteFac.RECHT_BES_WARENEINGANG_CUD);

			panelQueryWarezwischenmandanten.setMultipleRowSelectionEnabled(true);

			MandantDto[] mandantenDtos = DelegateFactory.getInstance().getMandantDelegate().mandantFindAll();

			Map m = new LinkedHashMap();

			for (int i = 0; i < mandantenDtos.length; i++) {
				if (!mandantenDtos[i].getCNr().equals(LPMain.getTheClient().getMandant())) {
					m.put(mandantenDtos[i].getCNr(), mandantenDtos[i].getCNr());
				}
			}

			panelQueryWarezwischenmandanten.setFilterComboBox(m,
					new FilterKriterium("flrauftragposition.flrauftrag.mandant_c_nr", true, "" + "",
							FilterKriterium.OPERATOR_EQUAL, false, true),
					DelegateFactory.getInstance().getArtikelDelegate().sindArtikelgruppenEingeschraenkt(),
					LPMain.getTextRespectUISPr("lp.alle"), false);

			setComponentAt(IDX_PANEL_AUSWAHL, panelQueryWarezwischenmandanten);

		}
	}

	public InternalFrameBestellung getInternalFrameBestellung() {
		return (InternalFrameBestellung) getInternalFrame();
	}

	public void setKeyWasForLockMe() {
		Object oKey = panelQueryWarezwischenmandanten.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryWarezwischenmandanten) {
				if (panelQueryWarezwischenmandanten.getSelectedId() != null) {
					getInternalFrameBestellung()
							.setKeyWasForLockMe(panelQueryWarezwischenmandanten.getSelectedId() + "");

					if (panelQueryWarezwischenmandanten.getSelectedId() == null) {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
					} else {
						getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, true);
					}
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
				}

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			refreshTitle();
		}

		else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {

			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.equals(ACTION_SPECIAL_WE_BUCHEN)) {

				if (panelQueryWarezwischenmandanten.getSelectedId() != null) {

					if (panelQueryWarezwischenmandanten.getSelectedIds().length == 1) {

						Integer lieferscheinIId = DelegateFactory.getInstance().getWareneingangDelegate()
								.getLieferscheinIIdAusWareUnterwegs(
										(String) panelQueryWarezwischenmandanten.getSelectedId());

						if (lieferscheinIId != null) {

							String msg = LPMain.getMessageTextRespectUISPr("bes.warezwischenmandanten.webuchen.frage",
									DelegateFactory.getInstance().getLsDelegate()
											.lieferscheinFindByPrimaryKey(lieferscheinIId).getCNr());

							if (DialogFactory.showModalJaNeinDialog(getInternalFrame(), msg)) {

								DelegateFactory.getInstance().getWareneingangDelegate()
										.wareUnterwegsZubuchen(lieferscheinIId, null);
								panelQueryWarezwischenmandanten.eventYouAreSelected(false);
							}
						}
					}  else {
						
						DialogFactory.showModalDialog(LPMain
							.getMessageTextRespectUISPr("lp.error"), LPMain
							.getMessageTextRespectUISPr("bes.warezwischenmandanten.webuchen.error.mehrere.zeilen"));
					}
				}

			} else if (sAspectInfo.equals(ACTION_SPECIAL_SELEKTIERTE_ZEILEN_BUCHEN)) {

				if (panelQueryWarezwischenmandanten.getSelectedIdsAsString() != null
						&& panelQueryWarezwischenmandanten.getSelectedIdsAsString().size() > 0) {

					String msg = LPMain
							.getMessageTextRespectUISPr("bes.warezwischenmandanten.webuchen.frage.nurselektierte");

					if (DialogFactory.showModalJaNeinDialog(getInternalFrame(), msg)) {

						DelegateFactory.getInstance().getWareneingangDelegate().wareUnterwegsZubuchen(null,
								panelQueryWarezwischenmandanten.getSelectedIdsAsString());
						panelQueryWarezwischenmandanten.eventYouAreSelected(false);
					}

				}

			}
		} else if ((e.getID() == ItemChangedEvent.ACTION_NEW) || (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW)) {
			// btnnew: einen neuen machen.

			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

		}

		else if (e.getID() == ItemChangedEvent.ACTION_TABLE_SELECTION_CHANGED) {

		}

	}

	private void refreshTitle() {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr("bes.warezwischenmandanten"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				((PanelBasis) this.getSelectedComponent()).getAdd2Title());

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		if (selectedIndex == IDX_PANEL_AUSWAHL) {
			createAuswahl();
			panelQueryWarezwischenmandanten.eventYouAreSelected(false);
			if (panelQueryWarezwischenmandanten.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_AUSWAHL, false);
			}
			panelQueryWarezwischenmandanten.updateButtons();
		}

		refreshTitle();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

	}

	public javax.swing.JMenuBar getJMenuBar() throws Throwable {
		setName("TabbedPaneWarezwischenMandanten");
		if (wrapperMenuBar == null) {

			wrapperMenuBar = new WrapperMenuBar(this);

		}

		return wrapperMenuBar;

	}

}
