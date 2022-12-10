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
package com.lp.client.frame.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.lp.client.angebotstkl.TabbedPaneEinkaufsangebot;
import com.lp.client.eingangsrechnung.TabbedPaneEingangsrechnung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.DialogExtraliste;
import com.lp.client.system.ReportExtraliste;
import com.lp.client.util.DokumentenlinkController;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.service.DokumentenlinkDto;
import com.lp.server.system.service.ExtralisteDto;
import com.lp.server.system.service.LocaleFac;

public class WrapperMenuBar extends JMenuBar implements ActionListener {
	protected final LpLogger myLogger = (LpLogger) com.lp.client.util.logger.LpLogger
			.getInstance(this.getClass());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MENU_MODUL = 0;
	public static final int MENU_BEARBEITEN = 1;
	public static final int MENU_JOURNAL = 2;
	private boolean bExtrasAdded = false;
	private TabbedPane tp = null;
	public static String DOKUMENTENLINK_ACTION = "DOKUMENTENLINK_ACTION";
	private String EXTRALISTE_ACTION = "EXTRALISTE_ACTION";

	public static final String ACTION_BEENDEN = "action_beenden";
	private DokumentenlinkController doklinkCtrl;

	public WrapperMenuBar(TabbedPane tabbedPane) throws Throwable {
		// 03.09.07 MB: das Menue erhaelt immer den Namen des Tabs
		JMenu menuModul = new JMenu(tabbedPane.getSAddTitle());
		// Namen des Menus wird auf "menu_"+name des tabs gesetz.
		if (Defaults.getInstance().isComponentNamingEnabled()) {
			menuModul.setName(HelperClient.COMP_PRAEFIX_MENU
					+ tabbedPane.getName().replaceFirst("tabbedPane", ""));
		}

		tp = tabbedPane;

		WrapperMenuItem menueItemBeenden = new WrapperMenuItem(
				LPMain.getTextRespectUISPr("lp.beenden"), null);
		menueItemBeenden.setActionCommand(ACTION_BEENDEN);
		menueItemBeenden.setToolTipText(LPMain
				.getTextRespectUISPr("lp.beenden"));
		menueItemBeenden.addActionListener(tabbedPane.getInternalFrame());
		menuModul.add(menueItemBeenden);

		JMenu menuBearbeiten = new WrapperMenu("lp.bearbeiten", tabbedPane);
		JMenu menuJournal = new WrapperMenu("lp.journal", tabbedPane);

		add(menuModul);

		addDokumentenlinks(menuModul);

		add(menuBearbeiten);
		add(menuJournal);

		// Wenn in LP_EXTRALISTE Eintraege vorhanden sind

		if (tabbedPane.getInternalFrame().getTabbedPaneRoot()
				.getSelectedIndex() == 0) {

			ExtralisteDto[] dtos = null;
			try {
				dtos = DelegateFactory
						.getInstance()
						.getSystemDelegate()
						.extralisteFindByBelegartCNr(
								tabbedPane.getInternalFrame().getBelegartCNr());
			} catch (Throwable ex) {
				/**
				 * @todo handle exception
				 */
				ex.printStackTrace();
			}

			if (dtos != null && dtos.length > 0) {

				JMenu menuExtras = new WrapperMenu("lp.extras", tabbedPane);

				for (int i = 0; i < dtos.length; i++) {
					JMenuItem menueItemExtras = new JMenuItem(dtos[i].getCBez());
					menueItemExtras.setActionCommand(EXTRALISTE_ACTION
							+ dtos[i].getIId());
					menueItemExtras.setToolTipText(dtos[i].getCBez());
					menueItemExtras.addActionListener(this);
					menuExtras.add(menueItemExtras);
				}
				add(menuExtras);
				bExtrasAdded = true;
			}
		}
	}

	private void addDokumentenlinks(JMenu menuModul) throws ExceptionLP, Throwable {
		if (!getDokumentenlinkCtrl().hasDokumentenlinks()) {
			return;
		}
		
		String textDoklink = LPMain.getTextRespectUISPr("system.dokumentenlink");
		JMenu menuDokumentenlink = new JMenu(textDoklink);
		menuDokumentenlink.setToolTipText(textDoklink);
		menuModul.add(menuDokumentenlink, 0);
		
		for (DokumentenlinkDto dokLink : getDokumentenlinkCtrl().getDokumentenlinkDtos()) {
			JMenuItem menuItemLink = new JMenuItem(dokLink.getCMenuetext());
			menuItemLink.setActionCommand(
					DOKUMENTENLINK_ACTION + dokLink.getIId());
			menuItemLink.setToolTipText(dokLink.getCMenuetext());
			menuItemLink.addActionListener(this);
			menuDokumentenlink.add(menuItemLink);
		}
	}
	
	private DokumentenlinkController getDokumentenlinkCtrl() {
		if (doklinkCtrl == null) {
			String belegartCnr = getBelegartCnr();
			doklinkCtrl = new DokumentenlinkController(tp, belegartCnr);
		}
		return doklinkCtrl;
	}
	
	private String getBelegartCnr() {
		if (tp instanceof TabbedPaneEingangsrechnung) {
			return ((TabbedPaneEingangsrechnung)tp).isBZusatzkosten() 
					? LocaleFac.BELEGART_ZUSATZKOSTEN 
					: tp.getInternalFrame().getBelegartCNr();
		} else if (tp instanceof TabbedPaneEinkaufsangebot) {
			return LocaleFac.BELEGART_EINKAUFSANGEBOT;
		}
		
		return tp.getInternalFrame().getBelegartCNr();
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().startsWith(DOKUMENTENLINK_ACTION)) {
				actionDokumentenlink(e.getActionCommand().substring(DOKUMENTENLINK_ACTION.length()));
				
			} else if (e.getActionCommand().startsWith(EXTRALISTE_ACTION)) {
				Integer extralisteIId = new Integer(e.getActionCommand()
						.replaceAll(EXTRALISTE_ACTION, ""));
				DialogExtraliste d = new DialogExtraliste(extralisteIId);

				if (d.bPrint == true) {
					tp.getInternalFrame().showReportKriterien(
							new ReportExtraliste(tp.getInternalFrame(), "",
									d.extralisteRueckgabeTabelleDto,
									extralisteIId));
				}
			}

		} catch (Throwable ex) {
			// Was tun?
			ex.printStackTrace();
			String message = "DOKUMENTENLINK_ACTION";
			LpLogger.getInstance(this.getClass()).warn(message, ex);
		}
	}

	private void actionDokumentenlink(String doklinkIdString) {
		try {
			Integer doklinkId = new Integer(doklinkIdString);
			getDokumentenlinkCtrl().actionDokumentenlink(doklinkId);
		} catch (NumberFormatException ex) {
			myLogger.error("NumberFormatException: String '" + doklinkIdString 
					+ "' aus " + DOKUMENTENLINK_ACTION + " Command ist keine Zahl.", ex);
		} catch (Throwable e) {
			new DialogError(LPMain.getInstance().getDesktop(), e, DialogError.TYPE_ERROR);
		}
	}

	/**
	 * Fuegt einen Menuepunkt zwischen dem Hilfemenue und dem vorletzten
	 * Menuepunkt ein.
	 * 
	 * @param jmenu
	 *            JMenu
	 */
	public void addJMenuItem(JMenu jmenu) {
		if (bExtrasAdded) {
			add(jmenu, getComponentCount() - 1);
		} else {
			add(jmenu, getComponentCount());
		}
	}
}
