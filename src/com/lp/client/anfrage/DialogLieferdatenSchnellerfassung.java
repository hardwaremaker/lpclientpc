package com.lp.client.anfrage;

import java.awt.Color;

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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("static-access")
public class DialogLieferdatenSchnellerfassung extends JDialog
		implements ActionListener, ItemChangedListener, FocusListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel();

	private InternalFrame intFrame = null;

	private ArrayList<AnfragepositionDto> anfragepositionDtos = new ArrayList<AnfragepositionDto>();
	private ArrayList<AnfragepositionlieferdatenDto> anfragepositionlieferdatenDtos = new ArrayList<AnfragepositionlieferdatenDto>();

	private JScrollPane scrollpanePositionen;

	private JPanel panelLieferdaten = new JPanel();

	WrapperNumberField[] wnfAnliefermenge = null;
	WrapperNumberField[] wnfAnlieferpreis = null;
	WrapperNumberField[] wnfWBZ = null;

	JButton buttonWbz = new JButton();

	private Integer anfrageIId = null;

	private AnfrageDto anfrageDto = null;

	int iDefaultHeight = 680;

	private WrapperButton wbuSchliessen = new WrapperButton();

	public DialogLieferdatenSchnellerfassung(InternalFrame intFrame, Integer anfrageIId) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.intFrame = intFrame;
		this.anfrageIId = anfrageIId;

		AnfragepositionDto[] anfragepositionDtosTemp = DelegateFactory.getInstance().getAnfragepositionDelegate()
				.anfragepositionFindByAnfrage(anfrageIId);

		for (int i = 0; i < anfragepositionDtosTemp.length; i++) {
			if (anfragepositionDtosTemp[i].getArtikelIId() != null) {
				anfragepositionDtos.add(anfragepositionDtosTemp[i]);
			}
		}

		anfrageDto = DelegateFactory.getInstance().getAnfrageDelegate().anfrageFindByPrimaryKey(anfrageIId);

		try {
			jbInit();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}

		this.setSize(778, iDefaultHeight);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				actionPerformed(new ActionEvent(wbuSchliessen, 1, ""));
				dispose();
			}
		});

		String title = anfrageDto.getCNr();
		if (anfrageDto.getCBez() != null) {
			title += " " + anfrageDto.getCBez();
		}

		setTitle(title);

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

			} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

			}
		} catch (Throwable e1) {
			intFrame.handleException(e1, false);
		}
	}

	private void jbInit() throws Throwable {
		panel = new JPanel(new MigLayout("wrap 2", "[50%][50%]"));

		wbuSchliessen.setText(LPMain.getInstance().getTextRespectUISPr("anfr.lieferdaten.schnellerfassung.schliessen"));

		wbuSchliessen.addActionListener(this);

		intFrame.addItemChangedListener(this);

		int iZeile = 0;

		// KOPFDATEN

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT,
						ParameterFac.KATEGORIE_ARTIKEL, LPMain.getInstance().getTheClient().getMandant());

		String wbz = "WBZ (";

		if (parameter.getCWert() != null) {
			if (parameter.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				wbz += LPMain.getTextRespectUISPr("lp.kw");
			} else if (parameter.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				wbz += LPMain.getTextRespectUISPr("lp.tage");

			}
		}

		wbz += ")";

		buttonWbz.setText(wbz);
		buttonWbz.addActionListener(this);

		// TAETIGKEITEN

		iZeile++;

		panelLieferdaten.setLayout(new GridBagLayout());

		panelLieferdaten.add(new JLabel(LPMain.getTextRespectUISPr("anf.schnellerfassung.artikel")),
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 100, 00));
		panelLieferdaten.add(buttonWbz, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 00));
		panelLieferdaten.add(new JLabel(LPMain.getTextRespectUISPr("anf.schnellerfassung.anliefermenge")),
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 00));
		panelLieferdaten.add(new JLabel(LPMain.getTextRespectUISPr("anf.schnellerfassung.einheit")),
				new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 30, 00));
		panelLieferdaten.add(
				new JLabel(LPMain.getTextRespectUISPr("anf.schnellerfassung.anlieferpreis") + " "
						+ anfrageDto.getWaehrungCNr().trim()),
				new GridBagConstraints(4, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 00));

		wnfAnliefermenge = new WrapperNumberField[anfragepositionDtos.size()];
		wnfAnlieferpreis = new WrapperNumberField[anfragepositionDtos.size()];
		wnfWBZ = new WrapperNumberField[anfragepositionDtos.size()];

		AbstractAction action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof JComponent) {
					JComponent component = (JComponent) e.getSource();
					component.transferFocus();
				}
			}
		};

		for (int i = 0; i < anfragepositionDtos.size(); i++) {

			ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(anfragepositionDtos.get(i).getArtikelIId());

			AnfragepositionlieferdatenDto lieferdatenDto = DelegateFactory.getInstance().getAnfragepositionDelegate()
					.anfragepositionlieferdatenFindByAnfragepositionIId(anfragepositionDtos.get(i).getIId());

			anfragepositionlieferdatenDtos.add(lieferdatenDto);

			iZeile++;
			panelLieferdaten.add(new JLabel(aDto.formatArtikelbezeichnung()), new GridBagConstraints(0, iZeile, 1, 1, 0,
					0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 00));

			WrapperNumberField wnfWBZZeile = new WrapperNumberField();

			wnfWBZZeile.setFractionDigits(0);

			wnfWBZZeile.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "TransferFocus");
			wnfWBZZeile.getActionMap().put("TransferFocus", action);

			wnfWBZZeile.setInteger(lieferdatenDto.getIAnlieferzeit());
			wnfWBZZeile.addKeyListener(this);
			wnfWBZZeile.addFocusListener(this);

			wnfWBZ[i] = wnfWBZZeile;

			panelLieferdaten.add(wnfWBZZeile, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -50, 00));

			WrapperNumberField wnfAnliefermengeZeile = new WrapperNumberField();

			wnfAnliefermengeZeile.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());

			wnfAnliefermengeZeile.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "TransferFocus");
			wnfAnliefermengeZeile.getActionMap().put("TransferFocus", action);

			wnfAnliefermengeZeile.setBigDecimal(lieferdatenDto.getNAnliefermenge());

			if (lieferdatenDto.getNAnliefermenge() == null || lieferdatenDto.getNAnliefermenge().doubleValue() == 0) {
				wnfAnliefermengeZeile.setBigDecimal(anfragepositionDtos.get(i).getNMenge());
			}
			wnfAnliefermengeZeile.addKeyListener(this);
			wnfAnliefermengeZeile.addFocusListener(this);

			wnfAnliefermenge[i] = wnfAnliefermengeZeile;

			panelLieferdaten.add(wnfAnliefermengeZeile, new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -50, 00));
			panelLieferdaten.add(new JLabel(aDto.getEinheitCNr()), new GridBagConstraints(3, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 00));

			WrapperNumberField wnfAnlieferpreisZeile = new WrapperNumberField();

			wnfAnlieferpreisZeile.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());

			wnfAnlieferpreisZeile.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "TransferFocus");
			wnfAnlieferpreisZeile.getActionMap().put("TransferFocus", action);

			wnfAnlieferpreisZeile.setBigDecimal(lieferdatenDto.getNNettogesamtpreis());

			wnfAnlieferpreisZeile.addKeyListener(this);
			wnfAnlieferpreisZeile.addFocusListener(this);

			wnfAnlieferpreis[i] = wnfAnlieferpreisZeile;

			panelLieferdaten.add(wnfAnlieferpreisZeile, new GridBagConstraints(4, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -50, 00));
		}
		iZeile++;
		panelLieferdaten.add(new JLabel(""), new GridBagConstraints(0, iZeile, 1, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 00));

		scrollpanePositionen = new JScrollPane(panelLieferdaten);

		panel.add(scrollpanePositionen, "growx, height 00:600:600,  span 2");

		panel.add(wbuSchliessen, "width 100:100:100, span2, align center");

		getContentPane().add(panel);

		// Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				clearAndHide();
			}
		});

	}

	public void clearAndHide() {
		setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {

			if (e.getSource().equals(buttonWbz)) {

				String stringInput = JOptionPane.showInputDialog(
						LPMain.getInstance().getTextRespectUISPr("anfr.lieferdaten.schnellerfassung.wbz.werte.alle"));

				int number = Integer.parseInt(stringInput);

				for (int i = 0; i < wnfWBZ.length; i++) {
					wnfWBZ[i].setInteger(number);
					wnfWBZ[i].setForeground(Color.BLUE);
				}

			} else

			if (e.getSource().equals(wbuSchliessen)) {

				// PJ22009 Pruefen ob ungespeicherte Aenderungen
				boolean bUngespeicherteAenderungen = false;
				for (int i = 0; i < wnfAnliefermenge.length; i++) {

					if (wnfAnliefermenge[i].getForeground().equals(Color.BLUE)
							|| wnfAnlieferpreis[i].getForeground().equals(Color.BLUE)
							|| wnfWBZ[i].getForeground().equals(Color.BLUE)) {
						bUngespeicherteAenderungen = true;
					}

				}

				if (bUngespeicherteAenderungen == false) {
					clearAndHide();
				} else {

					int indexWaehrungSpeichern = 0;
					int indexWaehrungVerwerfen = 1;

					Object[] aOptionen = new Object[2];
					aOptionen[indexWaehrungVerwerfen] = LPMain.getInstance()
							.getTextRespectUISPr("anfr.lieferdaten.schnellerfassung.schliessen.frage.verwerfen");
					aOptionen[indexWaehrungSpeichern] = LPMain.getInstance()
							.getTextRespectUISPr("anfr.lieferdaten.schnellerfassung.schliessen.frage.speichern");

					int iAuswahl = DialogFactory.showModalDialog(intFrame,
							LPMain.getInstance()
									.getTextRespectUISPr("anfr.lieferdaten.schnellerfassung.schliessen.frage"),
							LPMain.getInstance().getTextRespectUISPr("lp.frage"), aOptionen, aOptionen[0]);

					if (iAuswahl == indexWaehrungVerwerfen) {
						clearAndHide();
					} else {

						for (int i = 0; i < wnfAnlieferpreis.length; i++) {

							if (wnfAnliefermenge[i].getForeground().equals(Color.BLUE)
									|| wnfAnlieferpreis[i].getForeground().equals(Color.BLUE)
									|| wnfWBZ[i].getForeground().equals(Color.BLUE)) {

								AnfragepositionlieferdatenDto lieferdatenDto = anfragepositionlieferdatenDtos.get(i);

								if (wnfAnliefermenge[i].getBigDecimal() != null) {
									lieferdatenDto.setNAnliefermenge(wnfAnliefermenge[i].getBigDecimal());
								}
								if (wnfAnlieferpreis[i].getBigDecimal() != null) {
									lieferdatenDto.setNNettogesamtpreis(wnfAnlieferpreis[i].getBigDecimal());
									lieferdatenDto.setNNettogesamtpreisminusrabatt(wnfAnlieferpreis[i].getBigDecimal());
								}

								if (wnfWBZ[i].getInteger() != null) {
									lieferdatenDto.setIAnlieferzeit(wnfWBZ[i].getInteger());
								}

								// PJ22394

								if (lieferdatenDto.getTPreisgueltigab() == null) {
									if (anfrageDto.getTPreisgueltigab() != null) {

										lieferdatenDto.setTPreisgueltigab(anfrageDto.getTPreisgueltigab());

									} else {

										lieferdatenDto.setTPreisgueltigab(Helper
												.cutTimestamp(new java.sql.Timestamp(new java.util.Date().getTime())));

									}
								}

								DelegateFactory.getInstance().getAnfragepositionDelegate()
										.updateAnfragepositionlieferdaten(lieferdatenDto);
							}

						}

						clearAndHide();

					}

				}

			}
		} catch (Throwable e1) {
			intFrame.handleException(e1, false);
		}

	}

	public void saveAndExit() throws Throwable {

		this.setVisible(false);
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		try {

			for (int i = 0; i < wnfAnliefermenge.length; i++) {

				if (e.getSource().equals(wnfAnliefermenge[i])) {
					if (wnfAnliefermenge[i].getBigDecimal() != null
							&& anfragepositionlieferdatenDtos.get(i).getNAnliefermenge() != null) {

						if (!wnfAnliefermenge[i].getBigDecimal()
								.equals(Helper.rundeKaufmaennisch(
										anfragepositionlieferdatenDtos.get(i).getNAnliefermenge(),
										Defaults.getInstance().getIUINachkommastellenMenge()))) {
							wnfAnliefermenge[i].setForeground(Color.BLUE);
						} else {
							wnfAnliefermenge[i].setForeground(Color.BLACK);
						}
					}
				}

				if (e.getSource().equals(wnfWBZ[i])) {
					if (wnfWBZ[i].getInteger() != null
							&& anfragepositionlieferdatenDtos.get(i).getIAnlieferzeit() != null) {

						if (!wnfWBZ[i].getInteger().equals(anfragepositionlieferdatenDtos.get(i).getIAnlieferzeit())) {
							wnfWBZ[i].setForeground(Color.BLUE);
						} else {
							wnfWBZ[i].setForeground(Color.BLACK);
						}
					} else if (wnfWBZ[i].getInteger() != null
							&& anfragepositionlieferdatenDtos.get(i).getIAnlieferzeit() == null) {
						wnfWBZ[i].setForeground(Color.BLUE);
					}

				}

				if (e.getSource().equals(wnfAnlieferpreis[i])) {
					if (wnfAnlieferpreis[i].getBigDecimal() != null
							&& anfragepositionlieferdatenDtos.get(i).getNAnliefermenge() != null) {

						if (!wnfAnlieferpreis[i].getBigDecimal()
								.equals(Helper.rundeKaufmaennisch(
										anfragepositionlieferdatenDtos.get(i).getNNettogesamtpreis(),
										Defaults.getInstance().getIUINachkommastellenPreiseEK()))) {
							wnfAnlieferpreis[i].setForeground(Color.BLUE);
						} else {
							wnfAnlieferpreis[i].setForeground(Color.BLACK);
						}
					}
				}

			}

		} catch (Throwable e1) {
			intFrame.handleException(e1, false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		int i = 0;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyChar() == '+') {
			// Speichern
			for (int i = 0; i < wnfAnlieferpreis.length; i++) {

				if (e.getSource().equals(wnfAnlieferpreis[i])
						|| ((e.getSource().equals(wnfAnlieferpreis[i]) || e.getSource().equals(wnfAnliefermenge[i])
								|| e.getSource().equals(wnfWBZ[i])) && e.getKeyChar() == '+')) {

					try {
						AnfragepositionlieferdatenDto lieferdatenDto = anfragepositionlieferdatenDtos.get(i);

						lieferdatenDto.setIAnlieferzeit(wnfWBZ[i].getInteger());

						if (wnfAnliefermenge[i].getBigDecimal() != null) {
							lieferdatenDto.setNAnliefermenge(wnfAnliefermenge[i].getBigDecimal());
						}
						if (wnfAnlieferpreis[i].getBigDecimal() != null) {
							lieferdatenDto.setNNettogesamtpreis(wnfAnlieferpreis[i].getBigDecimal());
							lieferdatenDto.setNNettogesamtpreisminusrabatt(wnfAnlieferpreis[i].getBigDecimal());
						}

						
						// PJ22394

						if (lieferdatenDto.getTPreisgueltigab() == null) {
							if (anfrageDto.getTPreisgueltigab() != null) {

								lieferdatenDto.setTPreisgueltigab(anfrageDto.getTPreisgueltigab());

							} else {

								lieferdatenDto.setTPreisgueltigab(Helper
										.cutTimestamp(new java.sql.Timestamp(new java.util.Date().getTime())));

							}
						}
						
						
						
						DelegateFactory.getInstance().getAnfragepositionDelegate()
								.updateAnfragepositionlieferdaten(lieferdatenDto);

						anfragepositionlieferdatenDtos.set(i, lieferdatenDto);

						wnfAnliefermenge[i].setForeground(Color.BLACK);
						wnfAnlieferpreis[i].setForeground(Color.BLACK);
						wnfWBZ[i].setForeground(Color.BLACK);

					} catch (Throwable e1) {
						intFrame.handleException(e1, false);
					}

					if (e.getKeyChar() == '+') {

						if (e.getSource().equals(wnfWBZ[i])) {
							if (wnfWBZ.length >= i) {
								wnfWBZ[i + 1].grabFocus();

							}
						}

						if (e.getSource().equals(wnfAnliefermenge[i])) {
							if (wnfAnliefermenge.length >= i) {
								wnfAnliefermenge[i + 1].grabFocus();
							}
						}

						if (e.getSource().equals(wnfAnlieferpreis[i])) {
							if (wnfAnlieferpreis.length >= i) {
								wnfAnlieferpreis[i + 1].grabFocus();
							}
						}
						e.consume();
					}

					break;

				}

			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
