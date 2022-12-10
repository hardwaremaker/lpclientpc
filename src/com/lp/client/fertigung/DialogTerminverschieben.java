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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.ButtonFactory;
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
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogTerminverschieben extends JDialog
		implements ActionListener, PropertyChangeListener, ItemChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();
	private TabbedPaneLos tpLos = null;
	private boolean bGesperrt = false;
	private boolean bEventLaueft = false;

	private JButton jbuLock = null;

	private WrapperLabel wlaEnde = new WrapperLabel();
	private WrapperLabel wlaBeginn = new WrapperLabel();

	private WrapperDateField wdfBeginn = new WrapperDateField();
	private WrapperDateField wdfEnde = new WrapperDateField();

	private int iAutomatischeErmittlungAGBeginn = 0;
	private boolean bReihenfolgenplanung = false;

	private boolean bAutomatischeErmittlungProduktionsende = false;

	private PanelQueryFLR panelQueryFLRLos = null;
	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_auftrag_los_liste";
	private ArrayList<Integer> losIId = new ArrayList<Integer>();
	private WrapperButton wbuLos = new WrapperButton();
	private WrapperTextField wtfLos = new WrapperTextField();

	public DialogTerminverschieben(TabbedPaneLos tpLos) {
		super(LPMain.getInstance().getDesktop(), "", true);

		this.tpLos = tpLos;
		try {
			jbInit();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}

		this.setSize(Defaults.sizeFactor(300), Defaults.sizeFactor(180));

		setTitle(LPMain.getInstance().getTextRespectUISPr("fert.menu.terminverschieben"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	public void propertyChange(PropertyChangeEvent e) {
		if (bGesperrt && wdfBeginn.getDate() != null && wdfEnde.getDate() != null) {
			if (e.getSource() == wdfBeginn && e.getNewValue() instanceof Date && e.getPropertyName().equals("date")
					&& bEventLaueft == false) {
				int i = Helper.getDifferenzInTagen((Date) e.getOldValue(), (Date) e.getNewValue());
				bEventLaueft = true;
				wdfEnde.setDate(Helper.addiereTageZuDatum(wdfEnde.getDate(), i));
				bEventLaueft = false;

			}
			if (e.getSource() == wdfEnde && e.getNewValue() instanceof Date && e.getPropertyName().equals("date")
					&& bEventLaueft == false) {
				int i = Helper.getDifferenzInTagen((Date) e.getOldValue(), (Date) e.getNewValue());
				bEventLaueft = true;
				wdfBeginn.setDate(Helper.addiereTageZuDatum(wdfBeginn.getDate(), i));
				bEventLaueft = false;
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource().equals(wbuSpeichern)) {

				if (wdfBeginn.getTimestamp() == null || wdfEnde.getTimestamp() == null) {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				if (iAutomatischeErmittlungAGBeginn > 0 && bReihenfolgenplanung == false) {
					// SP9205
				} else if (bAutomatischeErmittlungProduktionsende == true) {

				} else {
					if (wdfEnde.getTimestamp().before(wdfBeginn.getTimestamp())) {
						DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
								LPMain.getInstance().getTextRespectUISPr("lp.error.beginnvorende"));
						return;
					}

				}

				for (int i = 0; i < losIId.size(); i++) {
					DelegateFactory.getInstance().getFertigungDelegate().terminVerschieben(losIId.get(i),
							Helper.cutTimestamp(wdfBeginn.getTimestamp()), Helper.cutTimestamp(wdfEnde.getTimestamp()));
				}

				tpLos.getLosDto().setTProduktionsbeginn(Helper.cutDate(wdfBeginn.getDate()));
				tpLos.getLosDto().setTProduktionsende(Helper.cutDate(wdfEnde.getDate()));

				setVisible(false);

			} else if (e.getSource().equals(wbuAbbrechen)) {
				this.setVisible(false);
			} else if (e.getSource().equals(jbuLock)) {
				bGesperrt = !bGesperrt;

				if (bGesperrt) {

					jbuLock.setIcon(IconFactory.getLock());
				} else {

					jbuLock.setIcon(IconFactory.getLockOpen());
				}

			}

			if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
				panelQueryFLRLos = FertigungFilterFactory.getInstance()
						.createPanelFLRBebuchbareLose(tpLos.getInternalFrame(), false, true, true, null, true);

				panelQueryFLRLos.setMultipleRowSelectionEnabled(true);
				panelQueryFLRLos.addButtonAuswahlBestaetigen(null);

				new DialogQuery(panelQueryFLRLos);
			}
		} catch (

		Throwable e1) {
			tpLos.handleException(e1, true);
		}
	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);

		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr("lp.report.save"));
		wbuAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		WrapperLabel wlaNeueTermine = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("fert.terminverschieben.neuetermine"));
		wlaNeueTermine.setHorizontalAlignment(SwingConstants.LEFT);
		wdfBeginn.setMandatoryField(true);
		wdfEnde.setMandatoryField(true);

		wdfBeginn.setDate(tpLos.getLosDto().getTProduktionsbeginn());
		wdfBeginn.addPropertyChangeListener(this);
		wdfEnde.setDate(tpLos.getLosDto().getTProduktionsende());
		wdfEnde.addPropertyChangeListener(this);

		wlaBeginn.setText(LPMain.getInstance().getTextRespectUISPr("lp.beginn"));
		wlaEnde.setText(LPMain.getInstance().getTextRespectUISPr("lp.ende"));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		jbuLock = ButtonFactory.createJButton(IconFactory.getLockOpen(), "");
		jbuLock.addActionListener(this);

		wbuLos.setText(LPMain.getInstance().getTextRespectUISPr("auft.title.panel.lose") + "...");
		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);
		// wtfLos.setActivatable(false);
		wtfLos.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfLos.setActivatable(false);

		tpLos.getInternalFrame().addItemChangedListener(this);

		HelperClient.setMinimumAndPreferredSize(jbuLock,
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));

		add(panel1);

		ParametermandantDto parameterAGBeginn = (ParametermandantDto) DelegateFactory.getInstance()
				.getParameterDelegate().getParametermandant(ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_AG_BEGINN,
						ParameterFac.KATEGORIE_FERTIGUNG, LPMain.getTheClient().getMandant());

		iAutomatischeErmittlungAGBeginn = (Integer) parameterAGBeginn.getCWertAsObject();

		ParametermandantDto parameterReihenfolgenplanung = (ParametermandantDto) DelegateFactory.getInstance()
				.getParameterDelegate().getParametermandant(ParameterFac.PARAMETER_REIHENFOLGENPLANUNG,
						ParameterFac.KATEGORIE_FERTIGUNG, LPMain.getTheClient().getMandant());

		bReihenfolgenplanung = (Boolean) parameterReihenfolgenplanung.getCWertAsObject();

		ParametermandantDto parameterAutomatischeErmittlungLosEnde = (ParametermandantDto) DelegateFactory.getInstance()
				.getParameterDelegate().getParametermandant(ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_LOS_ENDE,
						ParameterFac.KATEGORIE_FERTIGUNG, LPMain.getTheClient().getMandant());

		bAutomatischeErmittlungProduktionsende = (Boolean) parameterAutomatischeErmittlungLosEnde.getCWertAsObject();

		if (bAutomatischeErmittlungProduktionsende) {
			wlaEnde.setVisible(false);
			wdfEnde.setVisible(false);

			jbuLock.setVisible(false);
		} else {

			if (iAutomatischeErmittlungAGBeginn > 0 && bReihenfolgenplanung == false) {
				wlaBeginn.setVisible(false);
				wdfBeginn.setVisible(false);

				jbuLock.setVisible(false);
			}
		}

		losIId.add(tpLos.getLosDto().getIId());
		wtfLos.setText(tpLos.getLosDto().getCNr() + ", ");

		if (panelQueryFLRLos != null && panelQueryFLRLos.getDialog() != null) {
			panelQueryFLRLos.getDialog().setVisible(false);
		}

		panel1.add(wlaNeueTermine, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 5, 0, 0), 0, 0));
		panel1.add(wlaBeginn, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 150, 0));
		panel1.add(wlaEnde, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wdfBeginn, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		panel1.add(wdfEnde, new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(jbuLock, new GridBagConstraints(2, 3, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wbuLos, new GridBagConstraints(0, 5, 3, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wtfLos, new GridBagConstraints(0, 6, 3, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		panel1.add(wbuSpeichern, new GridBagConstraints(0, 7, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		panel1.add(wbuAbbrechen, new GridBagConstraints(1, 7, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

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

	public void changed(EventObject eI) {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLos) {
				Object[] o = panelQueryFLRLos.getSelectedIds();

				String lose = "";

				if (wtfLos.getText() != null) {
					lose = wtfLos.getText();
				}
				try {
					for (int i = 0; i < o.length; i++) {
						LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
								.losFindByPrimaryKey((Integer) o[i]);
						lose += losDto.getCNr() + ", ";

						losIId.add(losDto.getIId());

					}

				} catch (Throwable e1) {
					tpLos.handleException(e1, true);
				}

				wtfLos.setText(lose);
				if (panelQueryFLRLos != null && panelQueryFLRLos.getDialog() != null) {
					panelQueryFLRLos.getDialog().setVisible(false);
				}

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLos) {
				losIId = new ArrayList<Integer>();
				wtfLos.setText(null);
			}
		}
	}

}
