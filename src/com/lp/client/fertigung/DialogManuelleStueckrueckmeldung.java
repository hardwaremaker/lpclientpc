
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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.common.base.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.util.Facade;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;
import com.lp.util.KeyValue;

@SuppressWarnings("static-access")
public class DialogManuelleStueckrueckmeldung extends JDialog
		implements ActionListener, ItemChangedListener, ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperGotoButton wbuLos = new WrapperGotoButton(GotoHelper.GOTO_FERTIGUNG_AUSWAHL);
	private WrapperTextField wtfLos = new WrapperTextField();
	private LosDto losDto = null;
	private PersonalDto personalDto = null;

	private JLabel wlaAuftragKundeStueckliste = new JLabel();
	private JLabel wlaProjektKommentar = new JLabel();

	private JLabel wlaPersonal = new JLabel();

	private JLabel wlaLosgroesse = new JLabel();

	private JButton btnSpeichern = new JButton();
	private JButton btnZurueck = new JButton();
	private InternalFrame internalFrame;

	private WrapperLabel wlaGut = new WrapperLabel();
	private WrapperNumberField wnfGut = new WrapperNumberField(5);
	private WrapperLabel wlaSchlecht = new WrapperLabel();
	private WrapperNumberField wnfSchlecht = new WrapperNumberField(5);

	private WrapperLabel wlaArbeitsgang = new WrapperLabel();
	private WrapperNumberField wnfArbeitsgang = new WrapperNumberField(3);

	private WrapperLabel wlaZeitpunkt = new WrapperLabel();
	private WrapperTimestampField wtfZeitpunkt = new WrapperTimestampField();

	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperTextField wtfKommentar = new WrapperTextField();

	private WrapperLabel wlaAusweis = new WrapperLabel();
	private WrapperTextField wtfAusweis = new WrapperTextField();

	private WrapperCheckBox wcbFertig = new WrapperCheckBox();

	private JList list = null;
	private ArrayList<KeyValue> mArbeitsgaenge = new ArrayList<KeyValue>();

	private PanelQueryFLR panelQueryFLRLos = null;
	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_los_from_liste";

	private WrapperGotoButton buttonGoto = null;

	public DialogManuelleStueckrueckmeldung(InternalFrame internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", false);
		this.internalFrame = internalFrame;

		try {
			jbInit();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		this.setSize(700, 600);

		setTitle(LPMain.getInstance().getTextRespectUISPr("fert.menu.manuellestueckrueckmeldung"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				wtfLos.requestFocusInWindow();
			}
		});

	}

	private void jbInit() throws Throwable {

		this.getContentPane().setLayout(new GridBagLayout());

		internalFrame.addItemChangedListener(this);

		// das Aussenpanel hat immer das Gridbaglayout.
		this.getContentPane().setLayout(new GridBagLayout());

		buttonGoto = new WrapperGotoButton(GotoHelper.GOTO_LOS_GUT_SCHLECHT);

		wtfLos.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfLos.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (wtfLos.getText() != null && wtfLos.getText().length() > 0) {

						String losnummer;

						if (wtfLos.getText().startsWith("$L")) {
							losnummer = wtfLos.getText().substring(2);
						} else {
							losnummer = wtfLos.getText();
						}

						try {
							LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
									.losFindByCNrMandantCNrOhneExc(losnummer, LPMain.getTheClient().getMandant());

							if (losDto != null) {
								holeLosUndArbeitsgaenge(losDto.getIId());
							} else {

								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getMessageTextRespectUISPr(
												"fert.menu.manuellestueckrueckmeldung.los.nichtgefunden", losnummer));

								holeLosUndArbeitsgaenge(null);

								wtfLos.setText(null);
								wtfLos.requestFocusInWindow();
							}

						} catch (Throwable e1) {
							internalFrame.handleException(e1, false);
						}

					}
				}

			}
			// }

		});

		wtfAusweis.setMandatoryField(true);

		wtfAusweis.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (wtfAusweis.getText() != null && wtfLos.getText().length() > 0
							&& wtfAusweis.getText().startsWith("$P")) {

						String ausweis;

						if (wtfAusweis.getText().startsWith("$P")) {
							ausweis = wtfAusweis.getText().substring(2);
						} else {
							ausweis = wtfAusweis.getText();
						}

						try {
							personalDto = DelegateFactory.getInstance().getPersonalDelegate()
									.personalFindByCAusweis(ausweis);

							if (personalDto != null) {

								personalDto = DelegateFactory.getInstance().getPersonalDelegate()
										.personalFindByPrimaryKey(personalDto.getIId());

								wlaPersonal.setText(personalDto.formatFixName1Name2());
								wtfAusweis.setText(ausweis);
								btnSpeichern.requestFocusInWindow();
							} else {
								wlaPersonal.setText("NICHT GEFUNDEN");
								Toolkit.getDefaultToolkit().beep();
								wtfAusweis.setText(null);
							}

						} catch (Throwable e1) {
							internalFrame.handleException(e1, false);
						}

					}
				}

			}
			// }

		});

		wbuLos.setText(LPMain.getTextRespectUISPr("fert.tab.unten.los.title") + "...");
		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);

		wlaKommentar.setText(LPMain.getTextRespectUISPr("lp.kommentar"));
		wtfKommentar.setColumnsMax(300);

		wlaZeitpunkt.setText(LPMain.getTextRespectUISPr("fert.menu.manuellestueckrueckmeldung.zeitpunkt"));

		wlaArbeitsgang.setText(LPMain.getTextRespectUISPr("stkl.arbeitsplan.arbeitsgang"));
		wnfArbeitsgang.setFractionDigits(0);
		wnfArbeitsgang.setEditable(false);

		wlaAuftragKundeStueckliste.setText("Auftrag/ Kunde / Stueckliste");
		wlaProjektKommentar.setText("Projekt / Kommentar");

		wtfZeitpunkt.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

		wlaAusweis.setText(LPMain.getTextRespectUISPr("pers.personal.ausweis"));

		wcbFertig.setText(LPMain.getTextRespectUISPr("lp.fertig"));

		wcbFertig.setMnemonic(KeyEvent.VK_F);

		wlaGut.setText(LPMain.getTextRespectUISPr("zeiterfassung.zeitdaten.mengegut"));

		wlaGut.setDisplayedMnemonic(KeyEvent.VK_G);
		wlaGut.setLabelFor(wnfGut);

		wnfGut.setFractionDigits(com.lp.client.frame.Defaults.getInstance().getIUINachkommastellenMenge());
		wnfSchlecht.setFractionDigits(com.lp.client.frame.Defaults.getInstance().getIUINachkommastellenMenge());

		wbuLos.setMnemonic(KeyEvent.VK_L);
		wnfGut.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					wtfKommentar.requestFocusInWindow();
				}

			}
			// }

		});

		wtfKommentar.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (wtfKommentar.getText() != null && wtfKommentar.getText().length() > 0) {

						String ausweis;

						if (wtfKommentar.getText().startsWith("$P")) {
							ausweis = wtfKommentar.getText().substring(2);

							try {
								personalDto = DelegateFactory.getInstance().getPersonalDelegate()
										.personalFindByCAusweis(ausweis);

								if (personalDto != null) {

									personalDto = DelegateFactory.getInstance().getPersonalDelegate()
											.personalFindByPrimaryKey(personalDto.getIId());

									wlaPersonal.setText(personalDto.formatFixName1Name2());
									wtfAusweis.setText(ausweis);
									wtfKommentar.setText(null);
									btnSpeichern.requestFocusInWindow();
								} else {
									wlaPersonal.setText("NICHT GEFUNDEN");
									Toolkit.getDefaultToolkit().beep();
									wtfAusweis.setText(null);
								}

							} catch (Throwable e1) {
								internalFrame.handleException(e1, false);
							}

						}

						/*
						 * String losnummer;
						 * 
						 * if (wtfKommentar.getText().startsWith("$L")) { losnummer =
						 * wtfKommentar.getText().substring(2); } else { losnummer =
						 * wtfKommentar.getText(); }
						 * 
						 * try { LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
						 * .losFindByCNrMandantCNr(losnummer, LPMain.getTheClient().getMandant());
						 * 
						 * if (losDto != null) { holeLosUndArbeitsgaenge(losDto.getIId()); }
						 * 
						 * } catch (Throwable e1) { internalFrame.handleException(e1, false); }
						 */

					}
				}

			}
			// }

		});

		wlaKommentar.setDisplayedMnemonic(KeyEvent.VK_K);
		wlaKommentar.setLabelFor(wtfKommentar);

		wlaSchlecht.setText(LPMain.getTextRespectUISPr("zeiterfassung.zeitdaten.mengeschlecht"));

		btnSpeichern
				.setText(LPMain.getInstance().getTextRespectUISPr("fert.menu.manuellestueckrueckmeldung.speichern"));
		btnZurueck.setText(LPMain.getInstance().getTextRespectUISPr("fert.menu.manuellestueckrueckmeldung.zurueck"));

		btnSpeichern.addActionListener(this);

		btnSpeichern.setMnemonic(KeyEvent.VK_S);

		btnZurueck.addActionListener(this);

		int iZeile = 0;

		JPanel panelLos = new JPanel();

		GridBagLayout gbl2 = new GridBagLayout();
		panelLos.setLayout(gbl2);

		int iZeilePanel = 0;

		panelLos.add(wbuLos, new GridBagConstraints(0, iZeilePanel, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelLos.add(wtfLos, new GridBagConstraints(1, iZeilePanel, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelLos.add(wlaLosgroesse, new GridBagConstraints(3, iZeilePanel, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeilePanel++;
		panelLos.add(wlaAuftragKundeStueckliste, new GridBagConstraints(0, iZeilePanel, 5, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeilePanel++;
		panelLos.add(wlaProjektKommentar, new GridBagConstraints(0, iZeilePanel, 5, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeilePanel++;

		list = new JList();

		Color defaultCellForegroundColor = UIManager.getColor("Table.foreground");

		list.setSelectionBackground(Color.LIGHT_GRAY);

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

		panelLos.add(listScroller, new GridBagConstraints(0, iZeilePanel, 6, 1, 1, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 140));

		iZeilePanel++;
		panelLos.add(wlaArbeitsgang, new GridBagConstraints(0, iZeilePanel, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 25), 0, 0));

		panelLos.add(buttonGoto.getWrapperButtonGoTo(), new GridBagConstraints(0, iZeilePanel, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 10, 0));

		panelLos.add(wnfArbeitsgang, new GridBagConstraints(1, iZeilePanel, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelLos.add(wlaZeitpunkt, new GridBagConstraints(2, iZeilePanel, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		panelLos.add(wtfZeitpunkt, new GridBagConstraints(3, iZeilePanel, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 220, 0));

		iZeilePanel++;

		panelLos.add(wlaGut, new GridBagConstraints(0, iZeilePanel, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelLos.add(wnfGut, new GridBagConstraints(1, iZeilePanel, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelLos.add(wlaSchlecht, new GridBagConstraints(2, iZeilePanel, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelLos.add(wnfSchlecht, new GridBagConstraints(3, iZeilePanel, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		panelLos.add(wcbFertig, new GridBagConstraints(4, iZeilePanel, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		iZeilePanel++;

		panelLos.add(wlaKommentar, new GridBagConstraints(0, iZeilePanel, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		panelLos.add(wtfKommentar, new GridBagConstraints(1, iZeilePanel, 5, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeilePanel++;

		panelLos.add(wlaAusweis, new GridBagConstraints(0, iZeilePanel, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelLos.add(wtfAusweis, new GridBagConstraints(1, iZeilePanel, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelLos.add(wlaPersonal, new GridBagConstraints(3, iZeilePanel, 3, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		this.getContentPane().add(panelLos, new GridBagConstraints(0, iZeile, 3, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		this.getContentPane().add(btnSpeichern, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(5, 100, 5, 5), 0, 0));
		this.getContentPane().add(btnZurueck, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

	}

	public class SelectedListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (isSelected) {
				c.setBackground(Color.RED);
			}
			return c;
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			list.setSelectedIndex(list.getSelectedIndex());

			int i = list.getSelectedIndex();

			if (i >= 0) {

				KeyValue map = mArbeitsgaenge.get(i);
				try {

					wnfGut.setBigDecimal(null);
					wnfArbeitsgang.setBigDecimal(null);
					wnfSchlecht.setBigDecimal(null);
					wnfArbeitsgang.setInteger(null);

					wtfKommentar.setText(null);
					personalDto = null;
					wtfAusweis.setText(null);

					wlaPersonal.setText(null);

					LossollarbeitsplanDto saDto = (LossollarbeitsplanDto) map.getOKey();
					if (saDto != null) {

						wcbFertig.setShort(saDto.getBFertig());

						wnfArbeitsgang.setInteger(saDto.getIArbeitsgangnummer());
						buttonGoto.setOKey(saDto.getIId());
					} else {
						wcbFertig.setShort(Helper.boolean2Short(false));

					}

					wnfGut.requestFocusInWindow();

				} catch (Throwable e1) {
					internalFrame.handleException(e1, true);
				}
			}

		}
	}

	private void holeLosUndArbeitsgaenge(Integer losIId) throws Throwable {
		if (losIId != null) {
			losDto = DelegateFactory.getInstance().getFertigungDelegate().losFindByPrimaryKey(losIId);

			wtfLos.setText(losDto.getCNr());

			wbuLos.setOKey(losIId);

			wlaAuftragKundeStueckliste.setText(" ");

			String auftrag = "";
			String kunde = "";

			String stueckliste = "";

			if (losDto.getAuftragIId() != null) {

				AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(losDto.getAuftragIId());
				KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse());
				kunde = kundeDto.getPartnerDto().formatFixName1Name2();

			} else if (losDto.getKundeIId() != null) {
				KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(losDto.getKundeIId());
				kunde = kundeDto.getPartnerDto().formatFixName1Name2();
			}

			String lgr = LPMain.getTextRespectUISPr("label.losgroesse") + " "
					+ Helper.formatZahl(losDto.getNLosgroesse(), LPMain.getInstance().getTheClient().getLocUi());

			if (losDto.getStuecklisteIId() != null) {

				ArtikelDto aDto = DelegateFactory.getInstance().getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId()).getArtikelDto();

				stueckliste = aDto.formatArtikelbezeichnung();
				
				lgr+=" "+aDto.getEinheitCNr().trim();
				
			}

			wlaAuftragKundeStueckliste.setText(LPMain.getMessageTextRespectUISPr(
					"fert.menu.manuellestueckrueckmeldung.auftragkundestueckliste", auftrag, kunde, stueckliste));

			wlaLosgroesse.setText(lgr);

			String projekt = "";

			if (losDto.getCProjekt() != null) {
				projekt = losDto.getCProjekt();
			}

			String kommentar = "";

			if (losDto.getCKommentar() != null) {
				kommentar = losDto.getCKommentar();
			}

			wlaProjektKommentar.setText(LPMain.getMessageTextRespectUISPr(
					"fert.menu.manuellestueckrueckmeldung.projektkommentar", projekt, kommentar));

			mArbeitsgaenge = DelegateFactory.getInstance().getFertigungDelegate().getListeDerArbeitsgaenge(losIId);
			list.removeAll();

			Object[] tempZeilen = new Object[mArbeitsgaenge.size()];

			int iZeileErsterAGNichtFertig = -1;

			for (int i = 0; i < mArbeitsgaenge.size(); i++) {
				KeyValue m = mArbeitsgaenge.get(i);

				LossollarbeitsplanDto saDto = (LossollarbeitsplanDto) m.getOKey();
				if (saDto != null && Helper.short2boolean(saDto.getBFertig()) == false) {

					if (iZeileErsterAGNichtFertig == -1) {
						iZeileErsterAGNichtFertig = i;
					}

					buttonGoto.setOKey(saDto.getIId());
				}

				tempZeilen[i] = m.getOValue();
			}

			//Wenn Alle Fertig, dann esten selektieren
			if(iZeileErsterAGNichtFertig==-1 && mArbeitsgaenge.size()>2) {
				KeyValue m = mArbeitsgaenge.get(2);

				LossollarbeitsplanDto saDto = (LossollarbeitsplanDto) m.getOKey();
				buttonGoto.setOKey(saDto.getIId());
				iZeileErsterAGNichtFertig=2;
			}
			
			list.setListData(tempZeilen);

			list.setSelectedIndex(iZeileErsterAGNichtFertig);

			wtfZeitpunkt.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));

			wnfGut.requestFocusInWindow();

		} else {
			losDto = null;
			wtfLos.setText(null);
			wlaAuftragKundeStueckliste.setText(" ");
			wlaProjektKommentar.setText(" ");
			wtfLos.requestFocusInWindow();
			wlaPersonal.setText(null);
			list.removeAll();
			Object[] tempZeilen = new Object[mArbeitsgaenge.size()];
			list.setListData(tempZeilen);
			wnfArbeitsgang.setBigDecimal(null);
			wcbFertig.setSelected(false);
			wtfKommentar.setText(null);
			wnfGut.setBigDecimal(null);
			wnfSchlecht.setBigDecimal(null);

			wbuLos.setOKey(null);
			buttonGoto.setOKey(null);
			wlaLosgroesse.setText(null);

		}
	}

	public void changed(EventObject eI) {
		try {
			ItemChangedEvent e = (ItemChangedEvent) eI;
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
				if (e.getSource() == panelQueryFLRLos) {
					Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

					holeLosUndArbeitsgaenge(key);

				}
			}
		} catch (Throwable e) {
			internalFrame.handleException(e, false);
		}
	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRLos = FertigungFilterFactory.getInstance().createPanelFLRBebuchbareLose(internalFrame, false, true,
				false, null, false);
		new DialogQuery(panelQueryFLRLos);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnZurueck)) {

			this.setVisible(false);
			internalFrame.removeItemChangedListener(this);
		} else if (e.getSource().equals(wbuLos.getWrapperButton())) {
			try {
				dialogQueryLosFromListe(e);
			} catch (Throwable e1) {
				internalFrame.handleException(e1, false);
			}

		} else if (e.getSource().equals(btnSpeichern)) {

			if (losDto == null || personalDto == null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
				return;
			}

			if (losDto != null && personalDto != null) {

				int iBisherSelektiert = list.getSelectedIndex();

				if (iBisherSelektiert >= 2) {

					KeyValue map = mArbeitsgaenge.get(iBisherSelektiert);

					LossollarbeitsplanDto saDto = (LossollarbeitsplanDto) map.getOKey();
					if (saDto != null) {
						LosgutschlechtDto losgutschlechtDto = new LosgutschlechtDto();
						losgutschlechtDto.setLossollarbeitsplanIId(saDto.getIId());

						try {

							BigDecimal bdGut = BigDecimal.ZERO;

							if (wnfGut.getBigDecimal() != null) {
								bdGut = wnfGut.getBigDecimal();
							}

							BigDecimal bdSchlecht = BigDecimal.ZERO;
							if (wnfSchlecht.getBigDecimal() != null) {
								bdSchlecht = wnfSchlecht.getBigDecimal();
							}

							if (bdGut.doubleValue() != 0 || bdSchlecht.doubleValue() != 0) {

								BigDecimal bdInArbeit = BigDecimal.ZERO;

								losgutschlechtDto.setNGut(bdGut);
								losgutschlechtDto.setNSchlecht(bdSchlecht);
								losgutschlechtDto.setNInarbeit(bdInArbeit);

								losgutschlechtDto.setCKommentar(wtfKommentar.getText());
								losgutschlechtDto.setTZeitpunkt(wtfZeitpunkt.getTimestamp());
								losgutschlechtDto.setPersonalIIdErfasst(personalDto.getIId());

								DelegateFactory.getInstance().getFertigungDelegate()
										.createLosgutschlecht(losgutschlechtDto);

							}
							
							if (saDto.getBFertig() != wcbFertig.getShort()) {
								saDto.setBFertig(wcbFertig.getShort());
								DelegateFactory.getInstance().getFertigungDelegate().updateLossollarbeitsplan(saDto);
							}

							holeLosUndArbeitsgaenge(null);
							wtfLos.requestFocusInWindow();
							wtfAusweis.setText(null);
							personalDto=null;
						} catch (Throwable e1) {
							internalFrame.handleException(e1, true);
						}
					}

				}

			}

		}

	}

	public void clearAndHide() {
		setVisible(false);
	}

}
