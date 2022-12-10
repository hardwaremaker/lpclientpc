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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperTableEditable;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.pc.PanelDesktopStatusbar;
import com.lp.server.personal.service.NachrichtenDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.GotoHelper;

@SuppressWarnings("static-access")
public class DialogNachrichten extends JDialog
		implements ListSelectionListener, KeyListener, MouseListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JList list = null;
	public ArrayList<Map<Integer, String>> mNachrichten = null;
	private WrapperTextField betreff = new WrapperTextField();
	private WrapperTextField suche = new WrapperTextField();

	private WrapperCheckBox wcbNurUngelesene = new WrapperCheckBox();
	private WrapperCheckBox wcbNurEmpfangene = new WrapperCheckBox();

	private WrapperComboBox wcbPersonal = new WrapperComboBox();

	private WrapperComboBox wcbPersonalFilter = new WrapperComboBox();

	WrapperGotoButton wrapperGotoButton = new WrapperGotoButton(
			LPMain.getInstance().getTextRespectUISPr("label.identnummer"), com.lp.util.GotoHelper.GOTO_ARTIKEL_AUSWAHL);

	private InternalFrame internalFrame = null;

	private PanelDesktopStatusbar pds = null;

	public DialogNachrichten() {
		// nothing here
	}

	public DialogNachrichten(InternalFrame internalFrame, PanelDesktopStatusbar pds) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance().getTextRespectUISPr("pers.nachrichtenverlauf"),
				false);
		this.pds = pds;
		this.internalFrame = internalFrame;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		jbInit();
		pack();
		setSize(850, 200);
		refreshList();
		setAlwaysOnTop(false);

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {

			this.dispose();
		}
	}

	public void refreshList() throws Throwable {

		// Zuletzt selektiert
		Integer nachrichtenempaengerIIdZuletztSelektiert = null;

		if (list.getSelectedIndex() >= 0) {

			Map<Integer, String> m = (Map<Integer, String>) mNachrichten.get(list.getSelectedIndex());
			nachrichtenempaengerIIdZuletztSelektiert = (Integer) m.keySet().iterator().next();
		}

		mNachrichten = DelegateFactory.getInstance().getNachrichtenDelegate().getListeDerNachrichtenFuerEinePerson(
				LPMain.getTheClient().getIDPersonal(), wcbNurUngelesene.isSelected(), 100,
				(Integer) wcbPersonalFilter.getKeyOfSelectedItem(), suche.getText(), wcbNurEmpfangene.isSelected());
		list.removeAll();

		Object[] tempZeilen = new Object[mNachrichten.size()];

		int iIndexSelektiert = 0;
		for (int i = 0; i < mNachrichten.size(); i++) {
			Map<Integer, String> m = mNachrichten.get(i);
			tempZeilen[i] = m.values().iterator().next();

			Integer nachrichtenempaengerIIdZeile = (Integer) m.keySet().iterator().next();

			if (nachrichtenempaengerIIdZuletztSelektiert != null
					&& nachrichtenempaengerIIdZuletztSelektiert.equals(nachrichtenempaengerIIdZeile)) {
				iIndexSelektiert = i;
			}

		}

		list.setListData(tempZeilen);

		if (list.getSize().height > 0) {
			// list.ensureIndexIsVisible(0);
		}

		pds.setzeAnzahlDerUngelesenenNachrichten(null);

		list.setSelectedIndex(iIndexSelektiert);

	}

	private void jbInit() throws Throwable {
		// setUndecorated(true);

		JPanel panel = new JPanel(new GridBagLayout());

		this.getContentPane().setLayout(gridBagLayout2);

		list = new JList() {
			@Override
			public int locationToIndex(Point location) {
				int index = super.locationToIndex(location);
				if (index != -1 && !getCellBounds(index, index).contains(location)) {
					return -1;
				} else {
					return index;
				}
			}
		};
		;

		Color defaultCellForegroundColor = UIManager.getColor("Table.foreground");

		list.setSelectionForeground(defaultCellForegroundColor);
		// list.setFont(new java.awt.Font("monospaced", 0, 12));
		list.setFont(new java.awt.Font("monospaced", 0, 11));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		// listScroller.setMinimumSize(new Dimension(250, 80));
		// listScroller.setPreferredSize(new Dimension(300, 80));
		list.addListSelectionListener(this);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		list.addMouseListener(this);

		betreff.addKeyListener(this);
		betreff.setColumnsMax(80);

		wcbNurEmpfangene.setSelected(true);
		wcbNurUngelesene.setSelected(true);

		suche.addKeyListener(this);

		wcbPersonal.setMandatoryField(true);
		Map m = DelegateFactory.getInstance().getNachrichtenDelegate().getMoeglicheNachrichtenempfaenger();
		wcbPersonal.setMap(m, true);

		wcbPersonalFilter.setMap(m, true);

		wcbPersonalFilter.addActionListener(this);

		wcbNurEmpfangene.setText("nur empfangene");
		wcbNurUngelesene.addActionListener(this);

		wcbNurUngelesene.setText("nur ungelesene");
		wcbNurEmpfangene.addActionListener(this);

		panel.add(wcbNurEmpfangene, new GridBagConstraints(0, 0, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		panel.add(wcbNurUngelesene, new GridBagConstraints(1, 0, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		panel.add(suche, new GridBagConstraints(2, 0, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		panel.add(new JLabel(new ImageIcon(getClass().getResource("/com/lp/client/res/branch_view.png"))),
				new GridBagConstraints(3, 0, 1, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0), 0, 0));

		panel.add(wcbPersonalFilter, new GridBagConstraints(4, 0, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		panel.add(listScroller, new GridBagConstraints(0, 1, 5, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		panel.add(betreff, new GridBagConstraints(0, 3, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		panel.add(wcbPersonal, new GridBagConstraints(4, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 100, 0));

		this.getContentPane().add(panel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	}

	public void valueChanged(ListSelectionEvent e) {

	}

	public void nachrichtSenden() throws Throwable {

		DelegateFactory.getInstance().getNachrichtenDelegate().nachrichtAnEinzelpersonErstellen(betreff.getText(), "",
				(Integer) wcbPersonal.getKeyOfSelectedItem());

		betreff.setText("");

		refreshList();
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == betreff) {

				try {
					nachrichtSenden();
				} catch (Throwable e1) {
					e1.printStackTrace();
				}

				betreff.requestFocus();
			}

			if (e.getSource() == suche) {

				try {
					refreshList();
				} catch (Throwable e1) {
					e1.printStackTrace();
				}

				suche.requestFocus();
			}

		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		int index = list.locationToIndex(e.getPoint());

		if (index >= 0) {
			Map<Integer, String> m = mNachrichten.get(index);

			if (m != null) {

				Integer nachrichtenempaengerIId = (Integer) m.keySet().iterator().next();
				try {
					Integer nachrichtenIId = DelegateFactory.getInstance().getNachrichtenDelegate()
							.nachrichtenempfaengerFindByPrimaryKey(nachrichtenempaengerIId).getNachrichtenIId();

					NachrichtenDto nachrichtenDto = DelegateFactory.getInstance().getNachrichtenDelegate()
							.nachrichtenFindByPrimaryKey(nachrichtenIId);

					if (e.getButton() == MouseEvent.BUTTON3) {
						DelegateFactory.getInstance().getNachrichtenDelegate()
								.nachrichtenempfaengerAlsGelesenMarkieren(nachrichtenempaengerIId, false);

						refreshList();
					}

					else if (e.getClickCount() == 2) {

						if (nachrichtenDto.getBelegIId() != null && nachrichtenDto.getCBelegartnr() != null) {
							wrapperGotoButton.setWhereToGo(-1);

							wrapperGotoButton.setOKey(nachrichtenDto.getBelegIId());

							if (nachrichtenDto.getCBelegartnr().equals(LocaleFac.BELEGART_PROJEKT)) {
								wrapperGotoButton.setWhereToGo(GotoHelper.GOTO_PROJEKT_AUSWAHL);
							}

							if (wrapperGotoButton.getiWhereToGo() > -1) {
								ActionEvent action = new ActionEvent(this, 0, "ACTION_GOTO");
								wrapperGotoButton.actionPerformed(action);
								this.setVisible(false);
							}

						}
					} else {
						DelegateFactory.getInstance().getNachrichtenDelegate()
								.nachrichtenempfaengerAlsGelesenMarkieren(nachrichtenempaengerIId, true);
						refreshList();
					}

				} catch (Throwable e1) {
					e1.printStackTrace();
				}

			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wcbPersonalFilter) || e.getSource().equals(wcbNurEmpfangene)
				|| e.getSource().equals(wcbNurUngelesene)) {
			try {
				refreshList();
			} catch (Throwable e1) {
				internalFrame.handleException(e1, true);
			}
		}

	}
}
