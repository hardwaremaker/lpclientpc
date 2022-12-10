package com.lp.client.rechnung;

import java.awt.Cursor;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogOffeneLieferscheineVerrechnen extends JDialog implements ActionListener, ItemChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private InternalFrameRechnung intFrame = null;

	private WrapperLabel wlaBisLSDatum = new WrapperLabel();
	private WrapperDateField wdfBisLSDatum = new WrapperDateField();

	private static final String ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG = "action_special_rechnungsadresse_auftrag";

	private WrapperButton jButtonRechnungsadresse = null;
	private PanelQueryFLR panelQueryFLRRechnungsadresse = null;
	private WrapperTextField wtfKundeRechnungsadresse = null;

	private WrapperButton wbuOk = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	private List<Integer> kundeIId_Rechnungsadresse = null;

	public DialogOffeneLieferscheineVerrechnen(InternalFrameRechnung intFrame) {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.intFrame = intFrame;
		try {
			jbInit();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		this.setSize(700, 120);

		setTitle(LPMain.getInstance().getTextRespectUISPr("rechnung.menu.extras.offenelsverrechnen"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		this.addEscapeListener(this);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), "test");
		getRootPane().getActionMap().put("test", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					saveAndExit();
				} catch (Throwable e1) {
					e1.printStackTrace();
				}

			}
		});

	}

	public static void addEscapeListener(final JDialog dialog) {
		ActionListener escListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		};

		dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	public void changed(EventObject eI) {
		try {
			ItemChangedEvent e = (ItemChangedEvent) eI;
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
				if (e.getSource() == panelQueryFLRRechnungsadresse) {

					kundeIId_Rechnungsadresse = panelQueryFLRRechnungsadresse.getSelectedIdsAsInteger();
					String sAdresse = "";
					if (kundeIId_Rechnungsadresse != null) {
						Iterator it = kundeIId_Rechnungsadresse.iterator();

						while (it.hasNext()) {
							Integer kundeIId = (Integer) it.next();

							KundeDto kundeRechnungsadresseDto = DelegateFactory.getInstance().getKundeDelegate()
									.kundeFindByPrimaryKey(kundeIId);

							DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(kundeIId,
									LocaleFac.BELEGART_RECHNUNG, intFrame);

							sAdresse += kundeRechnungsadresseDto.getPartnerDto().formatTitelAnrede();
							if (kundeRechnungsadresseDto.getPartnerDto().getCKbez() != null) {
								sAdresse = sAdresse + "  /  " + kundeRechnungsadresseDto.getPartnerDto().getCKbez();
							}
							sAdresse += "; ";

						}

					}
					if (panelQueryFLRRechnungsadresse.getDialog() != null) {
						panelQueryFLRRechnungsadresse.getDialog().setVisible(false);
					}

					wtfKundeRechnungsadresse.setText(sAdresse);
				}

			} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
				if (e.getSource() == panelQueryFLRRechnungsadresse) {

					kundeIId_Rechnungsadresse = null;
					wtfKundeRechnungsadresse.setText(null);
				}
			}
		} catch (Throwable e1) {
			intFrame.handleException(e1, false);
		}
	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);

		wbuOk.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));
		wbuAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		wbuOk.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		jButtonRechnungsadresse = new WrapperButton();
		jButtonRechnungsadresse.setText(LPMain.getTextRespectUISPr("button.rechnungsadresse"));
		jButtonRechnungsadresse.setActionCommand(ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG);
		jButtonRechnungsadresse.addActionListener(this);

		wtfKundeRechnungsadresse = new WrapperTextField();
		wtfKundeRechnungsadresse.setActivatable(false);
		wtfKundeRechnungsadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wdfBisLSDatum.setMandatoryField(true);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -2);
		wdfBisLSDatum.setDate(Helper.cutDate(c.getTime()));

		intFrame.addItemChangedListener(this);

		wlaBisLSDatum.setText(LPMain.getTextRespectUISPr("rechnung.menu.extras.offenelsverrechnen.bislsdatum"));

		int iZeile = 0;

		panel1.add(wlaBisLSDatum, new GridBagConstraints(0, iZeile, 1, 1, .5, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wdfBisLSDatum, new GridBagConstraints(1, iZeile, 3, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(jButtonRechnungsadresse, new GridBagConstraints(0, iZeile, 1, 1, .5, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wtfKundeRechnungsadresse, new GridBagConstraints(1, iZeile, 3, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		panel1.add(wbuOk, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));

		panel1.add(wbuAbbrechen, new GridBagConstraints(2, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));

		getContentPane().add(panel1);

		// Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				/*
				 * Instead of directly closing the window, we're going to change the
				 * JOptionPane's value property.
				 */
				setVisible(false);
			}
		});

	}

	public void clearAndHide() {
		setVisible(false);
	}

	private void dialogQueryRechnungsadresse(ActionEvent e) throws Throwable {
		panelQueryFLRRechnungsadresse = PartnerFilterFactory.getInstance().createPanelFLRKunde(intFrame, true, true,
				null);

		panelQueryFLRRechnungsadresse.setMultipleRowSelectionEnabled(true);
		panelQueryFLRRechnungsadresse.addButtonAuswahlBestaetigen(null);

		String in = "(";
		List<Integer> kundeIIds = DelegateFactory.getInstance().getLsDelegate()
				.getRechnungsadressenGelieferterLieferscheine(wdfBisLSDatum.getDate());

		Iterator it = kundeIIds.iterator();

		if (it.hasNext() == false) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getMessageTextRespectUISPr("rechnung.menu.extras.offenelsverrechnen.keinegeliefertels",
							Helper.formatDatum(wdfBisLSDatum.getDate(), LPMain.getTheClient().getLocUi())));
			return;
		}

		while (it.hasNext()) {
			in += it.next();

			if (it.hasNext()) {
				in += ",";
			}
		}

		in += ")";

		FilterKriterium[] krit = new FilterKriterium[1];
		krit[0] = new FilterKriterium("i_id", true, in + "", FilterKriterium.OPERATOR_IN, false);

		panelQueryFLRRechnungsadresse.setDefaultFilter(krit);
		panelQueryFLRRechnungsadresse.eventActionRefresh(null, false);

		new DialogQuery(panelQueryFLRRechnungsadresse);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(ACTION_SPECIAL_RECHNUNGSADRESSE_AUFTRAG)) {
				dialogQueryRechnungsadresse(e);
			}

			else if (e.getSource().equals(wbuOk)) {

				saveAndExit();
			} else if (e.getSource().equals(wbuAbbrechen)) {
				this.setVisible(false);
			}

		} catch (Throwable e1) {
			intFrame.handleException(e1, false);
		}

	}

	public void saveAndExit() throws Throwable {
		try {

			setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));

			DelegateFactory.getInstance().getLsDelegate().verrechneGelieferteLieferscheine(kundeIId_Rechnungsadresse,
					wdfBisLSDatum.getDate(), intFrame.getNeuDatum());

			intFrame.getTabbedPaneRechnung().getPanelQueryRechnung().eventYouAreSelected(false);

		} finally {

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		if (kundeIId_Rechnungsadresse == null) {
			List<Integer> kundeIIds = DelegateFactory.getInstance().getLsDelegate()
					.getRechnungsadressenGelieferterLieferscheine(wdfBisLSDatum.getDate());

			Iterator it = kundeIIds.iterator();
			while (it.hasNext()) {
				Integer kundeIId = (Integer) it.next();
				DelegateFactory.getInstance().getKundeDelegate().pruefeKunde(kundeIId, LocaleFac.BELEGART_RECHNUNG,
						intFrame);
			}
		}

		this.setVisible(false);
	}
}
