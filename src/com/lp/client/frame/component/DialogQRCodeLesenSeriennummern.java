package com.lp.client.frame.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import com.lp.client.fertigung.PanelLosAblieferung;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dialog.DialogQRCodeLesen;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.util.EJBExceptionLP;

public class DialogQRCodeLesenSeriennummern extends JDialog {

	private String barcode = "";

	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	JButton jButtonUebernehmen = new JButton();

	private DialogSerienChargenauswahlSeriennummern dialogSerienChargenauswahlSeriennummern = null;

	private PanelLosAblieferung panelLosAblieferung = null;

	protected InternalFrame internalFrame = null;

	public DialogQRCodeLesenSeriennummern(
			DialogSerienChargenauswahlSeriennummern dialogSerienChargenauswahlSeriennummern,
			PanelLosAblieferung panelLosAblieferung, InternalFrame internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getTextRespectUISPr("er.erausqr.scan"), true);

		this.internalFrame = internalFrame;
		this.dialogSerienChargenauswahlSeriennummern = dialogSerienChargenauswahlSeriennummern;
		this.panelLosAblieferung = panelLosAblieferung;

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.setSize(250, 100);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addEscapeListener(this);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		jButtonUebernehmen.grabFocus();
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

	public void dispose() {
		// Beim expliziten Close die moeglicherweise erfassten Artikel verwerfen

		super.dispose();
	}

	public void actionPerformed(ActionEvent e) {
		try {

		} catch (Throwable e1) {
			internalFrame.handleException(e1, true);
		}

	}

	public String getBarCode() {
		return barcode;
	}

	protected void setBarCode(String s) {
		barcode = s;
	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);
		jButtonUebernehmen.setText(LPMain.getTextRespectUISPr("er.erausqr.beenden"));

		add(panel1);

		jButtonUebernehmen.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// your code is scanned and you can access it using frame.getBarCode()
					// now clean the bar code so the next one can be read
					// setBarCode(new String());

					if (getBarCode().length() > 0) {

						// Typ herausfinden
						String seriennummer = null;
						String barcodeRAW = getBarCode();

						if (getBarCode().startsWith("[)>06")) {
							// Typ T1
							char gs = 0x1D;
							int iStartSNR = -1;
							int iStopSNR = -1;
							for (int i = 5; i < getBarCode().length() - 1; i++) {
								if (getBarCode().charAt(i) == gs && getBarCode().charAt(i + 1) == 'S') {
									iStartSNR = i + 2;
								}

								if (iStartSNR > -1 && i > iStartSNR && getBarCode().charAt(i) == gs) {
									iStopSNR = i;
									break;
								}

							}

							if (iStartSNR > 0 && iStopSNR > iStartSNR) {
								seriennummer = getBarCode().substring(iStartSNR, iStopSNR);
							}

						} else if (getBarCode().length() == 44) {
							// Typ T2
							seriennummer = getBarCode().substring(13, 26);
						} else if (getBarCode().startsWith("@")) {
							// Typ T3
							String[] s = getBarCode().split("@");
							if (s.length > 1) {
								seriennummer = s[2];
							}
						}

						try {

							if (seriennummer != null) {
								if (dialogSerienChargenauswahlSeriennummern != null) {

									dialogSerienChargenauswahlSeriennummern.add2List(
											new SeriennrChargennrMitMengeDto(seriennummer, null, new BigDecimal(1)));
								} else if (panelLosAblieferung != null) {
									// SNR und MENGE setzen
									setBarCode("");

									// vorher Status pruefen
									if (panelLosAblieferung.getTabbedPaneLos().getLosDto() == null) {
										throw new ExceptionLP(EJBExceptionLP.FEHLER, new Exception("losDto = null"));
									} else {
										if (panelLosAblieferung.getTabbedPaneLos().getLosDto().getStatusCNr()
												.equals(FertigungFac.STATUS_STORNIERT)) {
											throw new ExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT,
													new Exception(
															panelLosAblieferung.getTabbedPaneLos().getLosDto().getCNr()
																	+ " ist storniert"));
										}
										if (panelLosAblieferung.getTabbedPaneLos().getLosDto().getStatusCNr()
												.equals(FertigungFac.STATUS_ANGELEGT)) {
											throw new ExceptionLP(
													EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN,
													new Exception(
															panelLosAblieferung.getTabbedPaneLos().getLosDto().getCNr()
																	+ " ist noch nicht ausgegeben"));
										}
										if (panelLosAblieferung.getTabbedPaneLos().getLosDto().getStatusCNr()
												.equals(FertigungFac.STATUS_GESTOPPT)) {
											throw new ExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT,
													new Exception(
															panelLosAblieferung.getTabbedPaneLos().getLosDto().getCNr()
																	+ " ist gestoppt"));
										}
									}
									// alles ok -> weiter gehts
									panelLosAblieferung.eventActionNew(e, true, false);
									panelLosAblieferung.eventYouAreSelected(false);

									setVisible(false);
									panelLosAblieferung.eventActionSave(null, false, seriennummer,barcodeRAW);
									
									
								}
							} else {

								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
										LPMain.getTextRespectUISPr("artikel.snrdialog.barcode.nichterkannt"));
							}
							setBarCode("");

						} catch (Throwable e1) {
							internalFrame.handleException(e1, false);
						}

					} else {
						setVisible(false);
					}

				} else {
					// some character has been read, append it to your "barcode cache"

					int ascii = (int) e.getKeyChar();
					if (ascii < 256) {
						setBarCode(getBarCode() + e.getKeyChar());
					}

				}
			}

		});

		jButtonUebernehmen.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				setVisible(false);

			}

		});

		panel1.add(jButtonUebernehmen, new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}

	public void jButtonUebernehmen_actionPerformed(ActionEvent e) {

		setVisible(false);
	}

}

class DialogQRCodeLesen_jButtonUebernehmen_actionAdapter implements ActionListener {
	private DialogQRCodeLesen adaptee;

	DialogQRCodeLesen_jButtonUebernehmen_actionAdapter(DialogQRCodeLesen adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonUebernehmen_actionPerformed(e);
	}
}