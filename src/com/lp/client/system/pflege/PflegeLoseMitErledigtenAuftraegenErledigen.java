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
package com.lp.client.system.pflege;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.util.logger.LpLogger;

public class PflegeLoseMitErledigtenAuftraegenErledigen implements
		IPflegefunktion, ActionListener, ListSelectionListener, FocusListener {

	private static Logger logger = LpLogger
			.getLogger(PflegeLoseMitErledigtenAuftraegenErledigen.class);

	private Map<Component, Action> actionMap;

	private JButton exportieren;

	private JLabel statisticsLabel;

	private JPanel panelMain;

	private JTextArea textResults;
	private JScrollPane scrollResults;

	@Override
	public String getKategorie() {
		return KATEGORIE_FERTIGUNG;
	}

	@Override
	public String getName() {
		return "Lose mit erledigten Aufträgen erledigen";
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public String getBeschreibung() {
		return "Alle Lose mit erledigten Auftraegen erledigen";
	}

	@Override
	public JPanel getPanel() {
		if (panelMain != null)
			return panelMain;

		panelMain = new JPanel(new MigLayout("fill"));

		exportieren = new JButton("Lose mit erledigten Auftraegen erledigen");
		statisticsLabel = new JLabel();

		exportieren.addActionListener(this);

		textResults = new JTextArea();

		scrollResults = new JScrollPane(textResults,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelMain.add(scrollResults, "grow, push, span");

		panelMain.add(statisticsLabel, "span 2");
		panelMain.add(exportieren);

		actionMap = new HashMap<Component, PflegeLoseMitErledigtenAuftraegenErledigen.Action>();

		actionMap.put(exportieren, new ActionExport());

		HelperClient.setComponentNames(this);
		return panelMain;
	}

	private void delegateAction(EventObject event) {
		Action a = actionMap.get(event.getSource());
		if (a != null)
			a.doAction(event);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		delegateAction(event);
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		delegateAction(arg0);
	}

	private class ActionExport implements Action {
		@Override
		public void doAction(EventObject event) {

			try {
				ArrayList<String> results = DelegateFactory.getInstance()
						.getPflegeDelegate()
						.loseMitErledigtenAuftraegenErledigen();

				String listString = "";

				for (String s : results) {
					listString += s + "\r\n";
				}

				textResults.setText(listString);

				DialogFactory.showModalDialog("Info",
						"Aktion erfolgreich durchgefuehrt");
			} catch (Throwable e) {
				JOptionPane.showMessageDialog(panelMain, e.getMessage());
				logger.error("Fehler");
				return;
			}

		}
	}

	private interface Action {
		void doAction(EventObject event);
	}

	@Override
	public boolean isStartable() {
		return false;
	}

	@Override
	public void run() {
	}

	@Override
	public void cancel() {
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public void init() {
	}

	@Override
	public void eventYouAreSelected() {

	}

	@Override
	public boolean supportsProgressBar() {
		return false;
	}

	@Override
	public void addPflegeEventListener(PflegeEventListener listener) {
	}

	@Override
	public void removeAllPflegeEventListeners() {
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}
}
