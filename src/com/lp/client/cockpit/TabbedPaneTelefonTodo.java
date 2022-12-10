package com.lp.client.cockpit;

import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;

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

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import com.lp.client.anfrage.AnfrageFilterFactory;
import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.ReportChargeneigenschaften;
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
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dynamisch.PanelDynamisch;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.client.system.PanelOrt;
import com.lp.server.anfrage.service.AnfrageFac;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
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
public class TabbedPaneTelefonTodo extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PanelQuery telefonzeitenTodoTop = null;
	public PanelBasis telefonzeitenTodoBottom = null;
	public PanelSplit telefonzeitenTodoSplit = null;

	private final static int IDX_PANEL_TELEFONZEITEN_TODO = 0;

	static final public String GOTO_TELEFONZEIT = PanelBasis.ACTION_MY_OWN_NEW + "GOTO_TELEFONZEIT";
	WrapperGotoButton wbuGotoTelefonzeit = new WrapperGotoButton(
			com.lp.util.GotoHelper.GOTO_ZEITERFASSUNG_TELEFONZEITEN);

	public TabbedPaneTelefonTodo(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr("cp.cockpit.telefontodo"));

		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		insertTab(LPMain.getInstance().getTextRespectUISPr("cp.cockpit.telefontodo"), null, null,
				LPMain.getInstance().getTextRespectUISPr("cp.cockpit.telefontodo"), IDX_PANEL_TELEFONZEITEN_TODO);

		refreshPanelQuery();
		setSelectedComponent(telefonzeitenTodoSplit);
		telefonzeitenTodoTop.eventYouAreSelected(false);
		setKeyWasForLockMe();

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	private void refreshPanelQuery() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("telefonzeiten.mandant_c_nr", true,
				"'" + LPMain.getTheClient().getMandant() + "'", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		String[] aWhichButtonIUseTop = null;

		telefonzeitenTodoTop = new PanelQuery(null, kriterien, QueryParameters.UC_ID_TELEFONZEITENTODO,
				aWhichButtonIUseTop, getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("cp.cockpit.telefontodo"), true);

		Map m = new LinkedHashMap();
		m.put(ZeiterfassungFacAll.TELEFONZEITEN_TODO_OPTION_OFFENE_WIEDERVORLAGEN,
				LPMain.getTextRespectUISPr("cp.telefonzeitentodo.option.offenewiedervorlagen"));
		m.put(ZeiterfassungFacAll.TELEFONZEITEN_TODO_OPTION_ALLE_WIEDERVORLAGEN,
				LPMain.getTextRespectUISPr("cp.telefonzeitentodo.option.allewiedervorlagen"));
		m.put(ZeiterfassungFacAll.TELEFONZEITEN_TODO_OPTION_ALLE_TELEFONZEITEN,
				LPMain.getTextRespectUISPr("cp.telefonzeitentodo.option.alletelefonzeiten"));

		telefonzeitenTodoTop.setFilterComboBox(m, new FilterKriterium("COMBO", true, "", "", false), true, "", false);

		/*
		 * FilterKriterium[] filters = new FilterKriterium[1]; filters[0] = new
		 * FilterKriterium("telefonzeiten.t_wiedervorlage_erledigt", true, "",
		 * FilterKriterium.OPERATOR_IS + " " + FilterKriterium.OPERATOR_NULL, false);
		 * 
		 * telefonzeitenTodoTop.befuelleFilterkriteriumSchnellansicht(filters);
		 */

		FilterKriteriumDirekt fkdPartner = new FilterKriteriumDirekt(
				"telefonzeiten.flrpartner." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance().getTextRespectUISPr("part.partner"),
				FilterKriteriumDirekt.PROZENT_BOTH, true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);

		FilterKriteriumDirekt fkdAnsprechpartner = new FilterKriteriumDirekt(
				ZeiterfassungFac.TELEFONZEITEN_TODO_ANSPRECHPARTNER, "", FilterKriterium.OPERATOR_LIKE,
				LPMain.getInstance().getTextRespectUISPr("label.ansprechpartner"), FilterKriteriumDirekt.PROZENT_BOTH,
				true, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);

		telefonzeitenTodoTop.befuellePanelFilterkriterienDirekt(fkdPartner, fkdAnsprechpartner);

		FilterKriteriumDirekt fkdText = new FilterKriteriumDirekt(ZeiterfassungFac.TELEFONZEITEN_TODO_TEXT, "",
				FilterKriterium.OPERATOR_LIKE, LPMain.getInstance().getTextRespectUISPr("lp.textsuche"),
				FilterKriteriumDirekt.EXTENDED_SEARCH, false, // wrapWithSingleQuotes
				true, Facade.MAX_UNBESCHRAENKT);
		telefonzeitenTodoTop.addDirektFilter(fkdText);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_KURZZEICHEN_STATT_NAME_IN_AUSWAHLLISTE,
						ParameterFac.KATEGORIE_PROJEKT, LPMain.getTheClient().getMandant());
		boolean bKurzzeichenStattName = (Boolean) parameter.getCWertAsObject();

		FilterKriteriumDirekt fPerson = null;
		if (bKurzzeichenStattName) {

			fPerson = new FilterKriteriumDirekt(
					"telefonzeiten.flrpersonal_zugewiesener." + PersonalFac.FLR_PERSONAL_C_KURZZEICHEN, "",
					FilterKriterium.OPERATOR_LIKE,
					LPMain.getInstance().getTextRespectUISPr("pers.telefonzeiten.zugewiesener"),
					FilterKriteriumDirekt.PROZENT_TRAILING, true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT);

		} else {

			fPerson = new FilterKriteriumDirekt(
					"telefonzeiten.flrpersonal_zugewiesener.flrpartner."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE,
					LPMain.getInstance().getTextRespectUISPr("pers.telefonzeiten.zugewiesener"),
					FilterKriteriumDirekt.PROZENT_TRAILING, true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT);

		}
		telefonzeitenTodoTop.addDirektFilter(fPerson);

		telefonzeitenTodoTop.getToolBar().addButtonLeft("/com/lp/client/res/data_into.png",
				LPMain.getTextRespectUISPr("proj.projektverlauf.gehezubeleg"), GOTO_TELEFONZEIT,
				KeyStroke.getKeyStroke('G', java.awt.event.InputEvent.CTRL_MASK), null);

		telefonzeitenTodoBottom = new PanelTelefonTodoKommentare(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("lp.ort"), null);

		telefonzeitenTodoSplit = new PanelSplit(getInternalFrame(), telefonzeitenTodoBottom, telefonzeitenTodoTop, 300);

		setComponentAt(IDX_PANEL_TELEFONZEITEN_TODO, telefonzeitenTodoSplit);

	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);

		int selectedIndex = getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_TELEFONZEITEN_TODO:

			// das Panel existiert auf jeden Fall

			telefonzeitenTodoTop.updateButtons(telefonzeitenTodoTop.getLockedstateDetailMainKey());
			break;
		}
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) { // Zeile in Tabelle

			if (e.getSource() == telefonzeitenTodoTop) {
				String c_nr = (String) telefonzeitenTodoTop.getSelectedId();
				// key 1; IF
				getInternalFrame().setKeyWasForLockMe(c_nr);
				// key2; PBottom
				telefonzeitenTodoBottom.setKeyWhenDetailPanel(c_nr);
				telefonzeitenTodoBottom.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				telefonzeitenTodoTop.updateButtons(telefonzeitenTodoBottom.getLockedstateDetailMainKey());

			}

		} else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.

		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {

		} else if (e.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.equals(GOTO_TELEFONZEIT)) {
				if (e.getSource() == telefonzeitenTodoTop) {

					Object key = telefonzeitenTodoTop.getSelectedId();

					if (key != null) {

						String sKey = (String) key;

						Integer iKey = new Integer(sKey.substring(1));
						wbuGotoTelefonzeit.setOKey(iKey);
						if (sKey.startsWith("T")) {

							wbuGotoTelefonzeit.setWhereToGo(com.lp.util.GotoHelper.GOTO_ZEITERFASSUNG_TELEFONZEITEN);

							wbuGotoTelefonzeit.actionPerformed(
									new ActionEvent(wbuGotoTelefonzeit, 0, WrapperGotoButton.ACTION_GOTO));
						} else {

							wbuGotoTelefonzeit.setWhereToGo(com.lp.util.GotoHelper.GOTO_ANGEBOT_AUSWAHL);
							wbuGotoTelefonzeit.actionPerformed(
									new ActionEvent(wbuGotoTelefonzeit, 0, WrapperGotoButton.ACTION_GOTO));
						}

					}

				}
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == telefonzeitenTodoTop) {

			}
		} else

		// wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
		if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {

		}

		// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
		// Discard
		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {

		}

		// markierenachsave: 0 Wir landen hier nach Button Save
		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {

		} else

		// posreihung: 4 Einer der Knoepfe zur Reihung der Positionen auf einem
		// PanelQuery wurde gedrueckt
		if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {
			if (e.getSource() == telefonzeitenTodoTop) {
				int iPos = telefonzeitenTodoTop.getTable().getSelectedRow();

				// wenn die Position nicht die erste ist
				if (iPos > 0) {
					Integer iIdPosition = (Integer) telefonzeitenTodoTop.getSelectedId();

					Integer iIdPositionMinus1 = (Integer) telefonzeitenTodoTop.getTable().getValueAt(iPos - 1, 0);

					DelegateFactory.getInstance().getVkPreisfindungDelegate()
							.vertauscheVkpfartikelpreisliste(iIdPosition, iIdPositionMinus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					telefonzeitenTodoTop.setSelectedId(iIdPosition);
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {
			if (e.getSource() == telefonzeitenTodoTop) {
				int iPos = telefonzeitenTodoTop.getTable().getSelectedRow();

				// wenn die Position nicht die letzte ist
				if (iPos < telefonzeitenTodoTop.getTable().getRowCount() - 1) {
					Integer iIdPosition = (Integer) telefonzeitenTodoTop.getSelectedId();

					Integer iIdPositionPlus1 = (Integer) telefonzeitenTodoTop.getTable().getValueAt(iPos + 1, 0);

					DelegateFactory.getInstance().getVkPreisfindungDelegate()
							.vertauscheVkpfartikelpreisliste(iIdPosition, iIdPositionPlus1);

					// die Liste neu anzeigen und den richtigen Datensatz
					// markieren
					telefonzeitenTodoTop.setSelectedId(iIdPosition);
				}
			}
		}
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) {
		e.getActionCommand();
	}

	/**
	 * Diese Methode setzt ide aktuelle Preisliste als den zu lockenden Auftrag.
	 */
	public void setKeyWasForLockMe() {
		Object oKey = telefonzeitenTodoTop.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

}
