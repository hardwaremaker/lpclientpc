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
package com.lp.client.artikel;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.ExceptionLP;
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
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dynamisch.PanelDynamisch;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
/**
 * <p>Tabbed pane fuer Komponente Preislisten.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2004-10-28</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class TabbedPaneWarenbewegung extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PanelQuery warenbewegungenTop = null;
	public PanelDynamisch warenbewegungenBottom = null;
	public PanelSplit warenbewegungen = null; // FLR 1:n Liste

	int iDivider = 170;

	private Integer i_id_buchung_Zwischenablage = null;

	private final static int IDX_PANEL_WARENBEWEGUNGEN = 0;

	private final String MENUE_ACTION_PRINT_CHARGENEIGENSCHAFTEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_PRINT_CHARGENEIGENSCHAFTEN";

	private final String MENUE_ACTION_KOPIERE_CHARGENEIGENSCHAFTEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_KOPIERE_CHARGENEIGENSCHAFTEN";
	private final String MENUE_ACTION_EINFUEGEN_CHARGENEIGENSCHAFTEN = PanelBasis.ACTION_MY_OWN_NEW
			+ "MENUE_ACTION_EINFUEGEN_CHARGENEIGENSCHAFTEN";

	private VkpfartikelpreislisteDto vkpfartikelpreislisteDto = null;

	public TabbedPaneWarenbewegung(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"artikel.warenbewegungen"));

		jbInit();
		initComponents();
	}

	public VkpfartikelpreislisteDto getVkpfartikelpreislisteDto() {
		return vkpfartikelpreislisteDto;
	}

	public void setVkpfartikelpreislisteDto(
			VkpfartikelpreislisteDto vkpfartikelpreislisteDtoI) {
		vkpfartikelpreislisteDto = vkpfartikelpreislisteDtoI;
	}

	private void jbInit() throws Throwable {
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.warenbewegungen"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.warenbewegungen"), IDX_PANEL_WARENBEWEGUNGEN);

		refreshPanelDynamisch();
		setSelectedComponent(warenbewegungen);
		warenbewegungenTop.eventYouAreSelected(false);
		setKeyWasForLockMe();
		setTitlePreisliste(LPMain.getInstance()
				.getTextRespectUISPr("lp.detail"));
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	private void refreshPanelDynamischBottom(Integer i_id_buchung)
			throws Throwable {

		iDivider = warenbewegungen.getPanelSplit().getDividerLocation();

		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, };

		if (i_id_buchung != null) {

			LagerbewegungDto[] lDtos = DelegateFactory.getInstance()
					.getLagerDelegate()
					.lagerbewegungFindByIIdBuchung(i_id_buchung);

			if (lDtos != null && lDtos.length > 0) {

				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(lDtos[0].getArtikelIId());

				if (artikelDto.istArtikelSnrOderchargentragend()) {

					Integer artikelsnrchnrIId = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.updateArtikelsnrchnr(lDtos[0].getArtikelIId(),
									lDtos[0].getCSeriennrchargennr());

					if (DelegateFactory
							.getInstance()
							.getPanelDelegate()
							.panelbeschreibungVorhanden(
									PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
									artikelDto.getArtgruIId())
							|| DelegateFactory
									.getInstance()
									.getPanelDelegate()
									.panelbeschreibungVorhanden(
											PanelFac.PANEL_CHARGENEIGENSCHAFTEN)) {

						warenbewegungenBottom = new PanelDynamisch(
								getInternalFrame(),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.eigenschaften"),
								warenbewegungenTop,
								PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
								HelperClient.LOCKME_ARTIKEL, aWhichButtonIUse,
								artikelDto.getArtgruIId(), artikelsnrchnrIId
										+ "");
						warenbewegungenBottom.eventYouAreSelected(false);

						warenbewegungen.getPanelSplit().setBottomComponent(
								warenbewegungenBottom);
						warenbewegungenBottom.updateButtons();

					} else {

						warenbewegungenBottom = new PanelDynamisch(
								getInternalFrame(),
								LPMain.getInstance().getTextRespectUISPr(
										"lp.eigenschaften"),
								warenbewegungenTop,
								PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
								HelperClient.LOCKME_ARTIKEL, aWhichButtonIUse,
								9999, artikelsnrchnrIId + "");
						warenbewegungenBottom.eventYouAreSelected(false);

						warenbewegungen.getPanelSplit().setBottomComponent(
								warenbewegungenBottom);
						warenbewegungenBottom.updateButtons();
					}

				} else {

					// Eigentlich Panel entfernen
					warenbewegungenBottom = new PanelDynamisch(
							getInternalFrame(), LPMain.getInstance()
									.getTextRespectUISPr("lp.eigenschaften"),
							warenbewegungenTop,
							PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
							HelperClient.LOCKME_ARTIKEL, aWhichButtonIUse,
							9999, null);
					warenbewegungenBottom.eventYouAreSelected(false);

					warenbewegungen.getPanelSplit().setBottomComponent(
							warenbewegungenBottom);
					warenbewegungenBottom.updateButtons();

				}
			}
		}
		warenbewegungen.getPanelSplit().setDividerLocation(iDivider);
	}

	private void refreshPanelDynamisch() throws Throwable {

		FilterKriterium[] filters = ArtikelFilterFactory.getInstance()
				.createFKWarenbewegungen();

		String[] aWhichButtonIUseTop = null;

		boolean bChargeneigenschaftenVorhanden = DelegateFactory
				.getInstance()
				.getPanelDelegate()
				.panelbeschreibungVorhanden(PanelFac.PANEL_CHARGENEIGENSCHAFTEN);

		if (bChargeneigenschaftenVorhanden) {
			aWhichButtonIUseTop = new String[] { PanelBasis.ACTION_KOPIEREN,
					PanelBasis.ACTION_EINFUEGEN_LIKE_NEW, };
		}

		warenbewegungenTop = new PanelQuery(null, filters,
				QueryParameters.UC_ID_WARENBEWEGUNGEN, aWhichButtonIUseTop,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"vkpf.preisliste.title.panel.preisliste"), true);

		if (bChargeneigenschaftenVorhanden) {
			warenbewegungenTop
					.getHmOfButtons()
					.get(PanelBasis.ACTION_KOPIEREN)
					.getButton()
					.setToolTipText(
							LPMain.getInstance()
									.getTextRespectUISPr(
											"artikel.warenbewegung.chargeneigenschaften.kopieren"));
			warenbewegungenTop
					.getHmOfButtons()
					.get(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)
					.getButton()
					.setToolTipText(
							LPMain.getInstance()
									.getTextRespectUISPr(
											"artikel.warenbewegung.chargeneigenschaften.einfuegen"));
		}

		warenbewegungenTop
				.befuellePanelFilterkriterienDirekt(
						ArtikelFilterFactory.getInstance()
								.createFKDWarenbewegungArtikelnummer(
										getInternalFrame()),
						ArtikelFilterFactory.getInstance()
								.createFKDSnrChargennummer());

		warenbewegungenTop.createAndSaveAndShowButton(
				"/com/lp/client/res/printer.png",
				LPMain.getInstance().getTextRespectUISPr(
						"artikel.chargeneigenschaften.drucken"),
				MENUE_ACTION_PRINT_CHARGENEIGENSCHAFTEN, null);

		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD, };
		warenbewegungenBottom = new PanelDynamisch(getInternalFrame(), LPMain
				.getInstance().getTextRespectUISPr("lp.eigenschaften"),
				warenbewegungenTop, PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
				HelperClient.LOCKME_ARTIKEL, aWhichButtonIUse);
		warenbewegungen = new PanelSplit(getInternalFrame(),
				warenbewegungenBottom, warenbewegungenTop, 200);

		setComponentAt(IDX_PANEL_WARENBEWEGUNGEN, warenbewegungen);

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_WARENBEWEGUNGEN:

			// das Panel existiert auf jeden Fall
			warenbewegungen.eventYouAreSelected(false);
			warenbewegungenTop.updateButtons(warenbewegungenBottom
					.getLockedstateDetailMainKey());
			break;
		}
	}

	private void initializeDtos(Integer iIdPreislisteI) throws Throwable {
		if (iIdPreislisteI != null) {
			vkpfartikelpreislisteDto = DelegateFactory.getInstance()
					.getVkPreisfindungDelegate()
					.vkpfartikelpreislisteFindByPrimaryKey(iIdPreislisteI);
		}
	}

	public void resetDtos() {
		vkpfartikelpreislisteDto = new VkpfartikelpreislisteDto();
	}

	public void setTitlePreisliste(String sTitleOhrwaschloben) throws Throwable {
		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getInstance().getTextRespectUISPr(
						"vkpf.preislisten.title.tab"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				sTitleOhrwaschloben);

		// TITLE_IDX_AS_I_LIKE setzen
		String sPreisliste = "";

		if (vkpfartikelpreislisteDto == null
				|| vkpfartikelpreislisteDto.getIId() == null) {
			sPreisliste = LPMain.getInstance().getTextRespectUISPr(
					"vkpf.neuepreisliste");
		} else {
			sPreisliste = vkpfartikelpreislisteDto.getCNr();
		}

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
				sPreisliste);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) { // Zeile in Tabelle
															// gewaehlt
			if (e.getSource() == warenbewegungenTop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				warenbewegungenBottom.setKeyWhenDetailPanel(key);
				warenbewegungenBottom.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				warenbewegungenTop.updateButtons(warenbewegungenBottom
						.getLockedstateDetailMainKey());

				refreshPanelDynamischBottom((Integer) key);

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (e.getSource() == warenbewegungenBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				warenbewegungenTop.updateButtons(new LockStateValue(
						PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			setTitlePreisliste(LPMain.getInstance().getTextRespectUISPr(
					"lp.detail"));
			warenbewegungen.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (e.getSource() == warenbewegungenTop
					&& sAspectInfo
							.endsWith(MENUE_ACTION_PRINT_CHARGENEIGENSCHAFTEN)) {
				String add2Title = LPMain.getInstance().getTextRespectUISPr(
						"artikel.chargeneigenschaften");
				getInternalFrame().showReportKriterien(
						new ReportChargeneigenschaften(getInternalFrame(),
								(Integer) warenbewegungenTop.getSelectedId(),
								add2Title));
			} else if (sAspectInfo.equals(PanelBasis.ACTION_EINFUEGEN_LIKE_NEW)
					&& i_id_buchung_Zwischenablage != null
					&& (Integer) warenbewegungenTop.getSelectedId() != null) {

				boolean b = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getInstance()
										.getTextRespectUISPr(
												"artikel.warenbewegung.kopierechargeneigenschaften.auszwischenablage.info"));
				if (b == true) {
					DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.kopiereChargeneigenschaften(
									i_id_buchung_Zwischenablage,
									(Integer) warenbewegungenTop
											.getSelectedId());
				}

				warenbewegungenTop.eventYouAreSelected(false);

			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == warenbewegungenTop) {
				warenbewegungenBottom.eventActionNew(e, true, false); // keyForLockMe
																		// setzen
				warenbewegungenBottom.eventYouAreSelected(false);
				setSelectedComponent(warenbewegungen); // ui
			}
		} else

		// wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
		if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == warenbewegungenBottom) {
				setKeyWasForLockMe();
				warenbewegungen.eventYouAreSelected(false);
			}
		}

		// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
		// Discard
		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == warenbewegungenBottom) {
				warenbewegungen.eventYouAreSelected(false); // das 1:n Panel neu
															// aufbauen
			}
		}

		// markierenachsave: 0 Wir landen hier nach Button Save
		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == warenbewegungenBottom) {
				// markierenachsave: 1 den Key des Datensatzes merken
				Object oKey = warenbewegungenBottom.getKeyWhenDetailPanel();

				// markierenachsave: 2 die Liste neu aufbauen
				warenbewegungenTop.eventYouAreSelected(false);

				// markierenachsave: 3 den Datensatz in der Liste selektieren
				warenbewegungenTop.setSelectedId(oKey);

				// markierenachsave: 4 im Detail den selektierten anzeigen
				warenbewegungen.eventYouAreSelected(false);
			}
		} else

		// posreihung: 4 Einer der Knoepfe zur Reihung der Positionen auf einem
		// PanelQuery wurde gedrueckt
		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == warenbewegungenTop) {
				int iPos = warenbewegungenTop.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) warenbewegungenTop
							.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) warenbewegungenTop
							.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory
							.getInstance()
							.getVkPreisfindungDelegate()
							.vertauscheVkpfartikelpreisliste(iIdPosition,
									iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					warenbewegungenTop.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == warenbewegungenTop) {
				int iPos = warenbewegungenTop.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < warenbewegungenTop.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) warenbewegungenTop
							.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) warenbewegungenTop
							.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory
							.getInstance()
							.getVkPreisfindungDelegate()
							.vertauscheVkpfartikelpreisliste(iIdPosition,
									iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					warenbewegungenTop.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			if (e.getSource() == warenbewegungenTop) {
				warenbewegungenBottom.eventActionNew(e, true, false);
				warenbewegungenBottom.eventYouAreSelected(false); // Buttons
																	// schalten
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_KOPIEREN) {
			if (e.getSource() == warenbewegungenTop) {
				i_id_buchung_Zwischenablage = (Integer) warenbewegungenTop
						.getSelectedId();
			}
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
		e.getActionCommand();
	}

	public PanelQuery getPreislistennameTop() {
		return this.warenbewegungenTop;
	}

	/**
	 * Diese Methode setzt ide aktuelle Preisliste als den zu lockenden Auftrag.
	 */
	public void setKeyWasForLockMe() {
		Object oKey = warenbewegungenTop.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		case EJBExceptionLP.FEHLER_VKPF_MAXIMALZEHNPREISLISTEN:
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
					.getTextRespectUISPr("vkpf.hint.maximalzehnpreislisten"));

			try {
				warenbewegungen.eventYouAreSelected(false); // @JO hier sitzt
															// ein Lock drauf???
			} catch (Throwable t) {
				super.handleException(t, true);
			}

			break;

		default:
			bErrorErkannt = false;
		}

		return bErrorErkannt;
	}

	protected JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

	public Object getDto() {
		return vkpfartikelpreislisteDto;
	}
}
