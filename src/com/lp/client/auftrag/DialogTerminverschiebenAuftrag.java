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
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;
import java.util.GregorianCalendar;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.util.IZwsPosition;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access")
public class DialogTerminverschiebenAuftrag extends JDialog
		implements ActionListener, PropertyChangeListener, ItemChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();
	private TabbedPaneAuftrag tpAuftrag = null;

	private WrapperLabel wlaLiefertermin = null;
	private WrapperDateField wdfLiefertermin = null;
	private WrapperDateField wdfLieferterminNeu = null;
	private WrapperLabel wlaWunschtermin = null;
	private WrapperDateField wdfWunschtermin = null;
	private WrapperDateField wdfWunschterminNeu = null;
	private WrapperLabel jLabelFinaltermin = null;
	private WrapperDateField wdfFinaltermin = null;
	private WrapperDateField wdfFinalterminNeu = null;

	private JButton wbuUp = null;
	private JButton wbuDown = null;

	private JRadioButton wrbSelektierterAuftrag = null;
	private JRadioButton wrbMehrereAuftraege = null;

	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperTextField wtfAuftrag = new WrapperTextField();
	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_auftrag_liste";
	private ArrayList<Integer> auftragIIds = new ArrayList<Integer>();
	private PanelQueryFLR panelQueryFLRAuftrag = null;

	public DialogTerminverschiebenAuftrag(TabbedPaneAuftrag tpLos) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);

		this.tpAuftrag = tpLos;
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		this.setSize(Defaults.sizeFactor(500), Defaults.sizeFactor(220));

		setTitle(LPMain.getInstance().getTextRespectUISPr("auft.menu.termineverschieben"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	private void datumUmEinenTagVeraendern(WrapperDateField wdf, boolean erhoehen) {

		if (wdf.getDate() != null) {
			GregorianCalendar gcNeu = new GregorianCalendar();
			gcNeu.setTime(wdf.getDate());

			if (erhoehen == true) {
				gcNeu.add(Calendar.DAY_OF_MONTH, 1);
			} else {
				gcNeu.add(Calendar.DAY_OF_MONTH, -1);
			}

			wdf.setDate(new java.sql.Date(gcNeu.getTimeInMillis()));

		}

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {
			try {
				if (wdfLieferterminNeu.getDate() == null || wdfFinalterminNeu.getDate() == null) {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				if (wrbSelektierterAuftrag.isSelected()) {

					if (tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_OFFEN)
							|| tpAuftrag.getAuftragDto().getStatusCNr()
									.equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)) {

						ArrayList<Integer> auftragIIds = new ArrayList<Integer>();
						auftragIIds.add(tpAuftrag.getAuftragDto().getIId());
						
						
						DelegateFactory.getInstance().getAuftragDelegate().terminVerschieben(
								auftragIIds, wdfLieferterminNeu.getTimestamp(),
								wdfFinalterminNeu.getTimestamp(), wdfWunschterminNeu.getTimestamp(),false);
					} else {

						tpAuftrag.showStatusMessage("lp.warning", "auft.warning.terminkannnichtverschobenwerden");

					}
				} else {
					DelegateFactory.getInstance().getAuftragDelegate().terminVerschieben(auftragIIds, wdfLieferterminNeu.getTimestamp(),
							wdfFinalterminNeu.getTimestamp(), wdfWunschterminNeu.getTimestamp(),true);
				}

				setVisible(false);

			} catch (Throwable e1) {
				tpAuftrag.handleException(e1, true);
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			this.setVisible(false);

		} else if (e.getSource().equals(wbuUp)) {
			datumUmEinenTagVeraendern(wdfFinalterminNeu, true);
			datumUmEinenTagVeraendern(wdfLieferterminNeu, true);
		} else if (e.getSource().equals(wbuDown)) {
			datumUmEinenTagVeraendern(wdfFinalterminNeu, false);
			datumUmEinenTagVeraendern(wdfLieferterminNeu, false);
		}

		if (e.getSource().equals(wrbSelektierterAuftrag) || e.getSource().equals(wrbMehrereAuftraege)) {

			aktualisiereKomponenten();

		}

		if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {
			try {
				FilterKriterium[] fk = LieferscheinFilterFactory.getInstance()
						.createFKPanelQueryFLRAuftragAuswahlNurAngelegte(false);
				panelQueryFLRAuftrag = AuftragFilterFactory.getInstance()
						.createPanelFLRAuftrag(tpAuftrag.getInternalFrame(), true, true, fk);
				panelQueryFLRAuftrag.setMultipleRowSelectionEnabled(true);

				panelQueryFLRAuftrag.addButtonAuswahlBestaetigen(null);

				new DialogQuery(panelQueryFLRAuftrag);
			} catch (Throwable e1) {
				tpAuftrag.handleException(e1, true);
			}
		}

	}

	private void aktualisiereKomponenten() {
		if (wrbSelektierterAuftrag.isSelected()) {
			wbuAuftrag.setEnabled(false);
			wtfAuftrag.setMandatoryField(false);

			wdfLiefertermin.setDate(tpAuftrag.getAuftragDto().getDLiefertermin());
			wdfFinaltermin.setDate(tpAuftrag.getAuftragDto().getDFinaltermin());
			
			
			wdfLieferterminNeu.setDate(tpAuftrag.getAuftragDto().getDLiefertermin());
			wdfFinalterminNeu.setDate(tpAuftrag.getAuftragDto().getDFinaltermin());
			

		} else {
			wbuAuftrag.setEnabled(true);
			wtfAuftrag.setMandatoryField(true);

			wdfLiefertermin.setDate(null);
			wdfFinaltermin.setDate(null);
			wdfLieferterminNeu.setDate(null);
			wdfFinalterminNeu.setDate(null);

		}
	}

	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getSource() == wdfLieferterminNeu.getDisplay() && evt.getPropertyName().equals("date")) {
			wdfFinalterminNeu.setMinimumValue(wdfLieferterminNeu.getDate());

			if (wdfFinalterminNeu.getDate() != null && wdfLieferterminNeu.getDate() != null) {
				if (wdfFinalterminNeu.getDate().before(wdfLieferterminNeu.getDate())) {
					wdfFinalterminNeu.setDate(wdfLieferterminNeu.getDate());
				}
			}
		}
	}

	public void changed(EventObject eI) {
		try {
			ItemChangedEvent e = (ItemChangedEvent) eI;
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
				if (e.getSource() == panelQueryFLRAuftrag) {
					Object[] o = panelQueryFLRAuftrag.getSelectedIds();

					String auftraege = "";

					if (wtfAuftrag.getText() != null) {
						auftraege = wtfAuftrag.getText();
					}
					for (int i = 0; i < o.length; i++) {
						AuftragDto losDto = DelegateFactory.getInstance().getAuftragDelegate()
								.auftragFindByPrimaryKey((Integer) o[i]);
						auftraege += losDto.getCNr() + ", ";

						auftragIIds.add(losDto.getIId());

					}
					wtfAuftrag.setText(auftraege);
					wtfAuftrag.setToolTipText(auftraege);
				}
				if (panelQueryFLRAuftrag.dialog != null) {
					panelQueryFLRAuftrag.dialog.setVisible(false);
				}
			} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
				if (e.getSource() == panelQueryFLRAuftrag) {
					auftragIIds = new ArrayList<Integer>();
					wtfAuftrag.setText("");
					wtfAuftrag.setToolTipText("");
				}
			}
		} catch (Throwable e1) {
			tpAuftrag.getInternalFrame().handleException(e1, false);
		}
	}

	private void jbInit() throws Exception {
		panel1.setLayout(gridBagLayout1);

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr("lp.report.save"));
		wbuAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		wlaLiefertermin = new WrapperLabel();
		wlaLiefertermin.setText(LPMain.getTextRespectUISPr("label.liefertermin"));

		wdfLiefertermin = new WrapperDateField();
		wdfLiefertermin.setEnabled(false);

		wdfLieferterminNeu = new WrapperDateField();
		wdfLieferterminNeu.setMandatoryField(true);

		wdfLiefertermin.setDate(tpAuftrag.getAuftragDto().getDLiefertermin());
		wdfLieferterminNeu.setMandatoryField(true);
		wdfLieferterminNeu.setDate(tpAuftrag.getAuftragDto().getDLiefertermin());
		wdfLieferterminNeu.getDateEditor().addPropertyChangeListener("date", this);

		jLabelFinaltermin = new WrapperLabel();
		jLabelFinaltermin.setText(LPMain.getTextRespectUISPr("label.finaltermin"));

		wdfFinaltermin = new WrapperDateField();
		wdfFinaltermin.setEnabled(false);
		wdfFinaltermin.setDate(tpAuftrag.getAuftragDto().getDFinaltermin());

		wdfFinalterminNeu = new WrapperDateField();
		wdfFinalterminNeu.setMandatoryField(true);
		wdfFinalterminNeu.setDate(tpAuftrag.getAuftragDto().getDFinaltermin());

		wlaWunschtermin = new WrapperLabel();
		wlaWunschtermin.setText(LPMain.getTextRespectUISPr("label.wunschtermin"));
		wdfWunschtermin = new WrapperDateField();
		wdfWunschtermin.setEnabled(false);
		wdfWunschterminNeu = new WrapperDateField();
		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		wdfWunschtermin.setDate(tpAuftrag.getAuftragDto().getTWunschtermin());
		wdfWunschterminNeu.setDate(tpAuftrag.getAuftragDto().getTWunschtermin());

		add(panel1);

		wbuUp = new JButton();
		wbuDown = new JButton();

		wbuUp.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/navigate_open.png")));
		wbuDown.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/navigate_close.png")));

		wbuUp.addActionListener(this);
		wbuDown.addActionListener(this);

		tpAuftrag.getInternalFrame().addItemChangedListener(this);

		int iZeile = 0;

		WrapperLabel wlaBisher = new WrapperLabel(LPMain.getTextRespectUISPr("auft.menu.termineverschieben.bisher"));
		wlaBisher.setHorizontalAlignment(SwingConstants.LEFT);

		wrbSelektierterAuftrag = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("auft.terminverschieben.selektierterauftrag")
						+ tpAuftrag.getAuftragDto().getCNr());

		wrbSelektierterAuftrag.setSelected(true);

		wrbMehrereAuftraege = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("auft.terminverschieben.mehrereauftraege"));

		wrbSelektierterAuftrag.addActionListener(this);
		wrbMehrereAuftraege.addActionListener(this);

		ButtonGroup bg = new ButtonGroup();
		bg.add(wrbSelektierterAuftrag);
		bg.add(wrbMehrereAuftraege);

		wbuAuftrag.setText(LPMain.getInstance().getTextRespectUISPr("auft.modulname.tooltip") + "...");

		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuAuftrag.addActionListener(this);
		// wtfLos.setActivatable(false);
		wtfAuftrag.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfAuftrag.setEditable(false);

		aktualisiereKomponenten();

		panel1.add(wrbSelektierterAuftrag, new GridBagConstraints(0, iZeile, 3, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));

		iZeile++;

		panel1.add(wrbMehrereAuftraege, new GridBagConstraints(0, iZeile, 3, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));

		iZeile++;

		panel1.add(wbuAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
		panel1.add(wtfAuftrag, new GridBagConstraints(1, iZeile, 3, 1, 1.0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;

		panel1.add(wlaBisher, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		WrapperLabel wlaAktuell = new WrapperLabel(LPMain.getTextRespectUISPr("auft.menu.termineverschieben.aktuell"));
		wlaAktuell.setHorizontalAlignment(SwingConstants.LEFT);

		panel1.add(wlaAktuell, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 20), 0, 0));
		iZeile++;
		panel1.add(wlaLiefertermin, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 150, 0));
		panel1.add(wdfLiefertermin, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wdfLieferterminNeu, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wbuUp, new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		panel1.add(jLabelFinaltermin, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
		panel1.add(wdfFinaltermin, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wdfFinalterminNeu, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		panel1.add(wbuDown, new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		panel1.add(wlaWunschtermin, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
		panel1.add(wdfWunschtermin, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wdfWunschterminNeu, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		panel1.add(wbuSpeichern, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));
		panel1.add(wbuAbbrechen, new GridBagConstraints(2, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));

		setContentPane(panel1);

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

}
