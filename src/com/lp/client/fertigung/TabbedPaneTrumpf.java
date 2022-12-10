
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
package com.lp.client.fertigung;

import javax.swing.JMenuBar;
import javax.swing.event.ChangeEvent;

import com.lp.client.artikel.PanelTrumpf;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Tabbed pane fuer Komponente Preislisten.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 2004-10-28
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class TabbedPaneTrumpf extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PanelQuery trumpfTop = null;
	public PanelTrumpf trumpfBottom = null;
	public PanelSplit trumpf = null; // FLR 1:n Liste

	int iDivider = 170;

	private final static int IDX_PANEL_TRUMPF = 0;

	private VkpfartikelpreislisteDto vkpfartikelpreislisteDto = null;

	public TabbedPaneTrumpf(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("artikel.trumpf"));

		jbInit();
		initComponents();
	}

	public VkpfartikelpreislisteDto getVkpfartikelpreislisteDto() {
		return vkpfartikelpreislisteDto;
	}

	public void setVkpfartikelpreislisteDto(VkpfartikelpreislisteDto vkpfartikelpreislisteDtoI) {
		vkpfartikelpreislisteDto = vkpfartikelpreislisteDtoI;
	}

	private void jbInit() throws Throwable {
		insertTab(LPMain.getTextRespectUISPr("artikel.trumpf"), null, null,
				LPMain.getTextRespectUISPr("artikel.trumpf"), IDX_PANEL_TRUMPF);

		refreshPanelTrumpf();
		setSelectedComponent(trumpf);
		trumpfTop.eventYouAreSelected(false);
		setKeyWasForLockMe();
		setTitlePreisliste(LPMain.getTextRespectUISPr("lp.detail"));
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}


	private void refreshPanelTrumpf() throws Throwable {

		String[] aWhichButtonIUseTop = null;

		FilterKriterium[] filters = new FilterKriterium[3];
		filters[0] = new FilterKriterium("flrlos.mandant_c_nr", true, "'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		filters[1] = new FilterKriterium("t_export_beginn", true, "",
				FilterKriterium.OPERATOR_IS + " " + FilterKriterium.OPERATOR_NOT_NULL, false);
		filters[2] = new FilterKriterium("flrartikel.flrartikelklasse.b_tops", true, "'" + LPMain.getTheClient().getMandant() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);

		trumpfTop = new PanelQuery(null, filters, QueryParameters.UC_ID_LOSSOLLMATERIAL_TRUMPF, aWhichButtonIUseTop,
				getInternalFrame(), LPMain.getTextRespectUISPr("vkpf.preisliste.title.panel.preisliste"),
				true);

		FilterKriterium[] filtersSchnellansicht = new FilterKriterium[1];
		filtersSchnellansicht[0] = new FilterKriterium("flrlos.status_c_nr", true, "('" + FertigungFac.STATUS_ANGELEGT
				+ "','" + FertigungFac.STATUS_STORNIERT + "','" + FertigungFac.STATUS_ERLEDIGT + "')",
				FilterKriterium.OPERATOR_NOT_IN, false);

		trumpfTop.befuelleFilterkriteriumSchnellansicht(filtersSchnellansicht);

		trumpfTop.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("flrartikel.c_nr", "", FilterKriterium.OPERATOR_LIKE,
						LPMain.getTextRespectUISPr("artikel.artikelnummer"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true, 99),
				new FilterKriteriumDirekt("flrlos." + FertigungFac.FLR_LOS_C_NR, "",
						FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("fert.los.direktfilter.losnr"),
						FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
						true, // wrapWithSingleQuotes
						false, Facade.MAX_UNBESCHRAENKT));

		trumpfTop.addDirektFilter(new FilterKriteriumDirekt(
				"flrlos." + FertigungFac.FLR_LOSREPORT_FLRAUFTRAG + ".flrkunde.flrpartner.c_name1nachnamefirmazeile1",
				"", FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("part.firma_nachname"),
				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT));

		
		trumpfBottom = new PanelTrumpf(getInternalFrame(),
				LPMain.getTextRespectUISPr("artikel.trumpf"),null);
		trumpf = new PanelSplit(getInternalFrame(), trumpfBottom, trumpfTop, 220);

		setComponentAt(IDX_PANEL_TRUMPF, trumpf);

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_TRUMPF:

			// das Panel existiert auf jeden Fall
			trumpf.eventYouAreSelected(false);
			trumpfTop.updateButtons(trumpfBottom.getLockedstateDetailMainKey());
			break;
		}
	}

	public void resetDtos() {
		vkpfartikelpreislisteDto = new VkpfartikelpreislisteDto();
	}

	public void setTitlePreisliste(String sTitleOhrwaschloben) throws Throwable {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getTextRespectUISPr("vkpf.preislisten.title.tab"));
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, sTitleOhrwaschloben);

		// TITLE_IDX_AS_I_LIKE setzen
		String sPreisliste = "";

		if (vkpfartikelpreislisteDto == null || vkpfartikelpreislisteDto.getIId() == null) {
			sPreisliste = LPMain.getTextRespectUISPr("vkpf.neuepreisliste");
		} else {
			sPreisliste = vkpfartikelpreislisteDto.getCNr();
		}

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, sPreisliste);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) { // Zeile in Tabelle
															// gewaehlt
			if (e.getSource() == trumpfTop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				trumpfBottom.setKeyWhenDetailPanel(key);
				trumpfBottom.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				trumpfTop.updateButtons(trumpfBottom.getLockedstateDetailMainKey());

			

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (e.getSource() == trumpfBottom) {
				// im QP die Buttons in den Zustand neu setzen.
				trumpfTop.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			setTitlePreisliste(LPMain.getTextRespectUISPr("lp.detail"));
			trumpf.eventYouAreSelected(false);
		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();

		}

		else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == trumpfTop) {
				trumpfBottom.eventActionNew(e, true, false); // keyForLockMe
																// setzen
				trumpfBottom.eventYouAreSelected(false);
				setSelectedComponent(trumpf); // ui
			}
		} else

		// wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
		if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == trumpfBottom) {
				setKeyWasForLockMe();
				trumpf.eventYouAreSelected(false);
			}
		}

		// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
		// Discard
		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (e.getSource() == trumpfBottom) {
				trumpf.eventYouAreSelected(false); // das 1:n Panel neu
													// aufbauen
			}
		}

		// markierenachsave: 0 Wir landen hier nach Button Save
		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == trumpfBottom) {
				// markierenachsave: 1 den Key des Datensatzes merken
				Object oKey = trumpfBottom.getKeyWhenDetailPanel();

				// markierenachsave: 2 die Liste neu aufbauen
				trumpfTop.eventYouAreSelected(false);

				// markierenachsave: 3 den Datensatz in der Liste selektieren
				trumpfTop.setSelectedId(oKey);

				// markierenachsave: 4 im Detail den selektierten anzeigen
				trumpf.eventYouAreSelected(false);
			}
		}

	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
		e.getActionCommand();
	}

	public PanelQuery getPreislistennameTop() {
		return this.trumpfTop;
	}

	/**
	 * Diese Methode setzt ide aktuelle Preisliste als den zu lockenden Auftrag.
	 */
	public void setKeyWasForLockMe() {
		Object oKey = trumpfTop.getSelectedId();

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
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("vkpf.hint.maximalzehnpreislisten"));

			try {
				trumpf.eventYouAreSelected(false); // @JO hier sitzt
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
