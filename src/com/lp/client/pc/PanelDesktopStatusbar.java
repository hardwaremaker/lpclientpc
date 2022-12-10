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
package com.lp.client.pc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.lp.client.fertigung.DialogIstmaterialAendern;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.nachrichten.DialogNachrichten;
import com.lp.client.system.InternalFrameSystem;
import com.lp.client.system.TabbedPaneNachrichtarchiv;
import com.lp.client.util.IconFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.personal.service.NachrichtenempfaengerDto;
import com.lp.server.system.jms.service.LPMessage;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParameteranwenderDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * 
 * <p> Diese Klasse kuemmert sich um die Desktopstatusbar.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Josef Ornetsmueller; 28.06.06</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2011/03/03 13:35:15 $
 */
public class PanelDesktopStatusbar extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jpaWorkingOn = new JPanel();

	private WrapperLabel wlaSpalte0 = null;
	private WrapperLabel wlaSpalte1 = null;
	private WrapperLabel wlaSpalte4 = null;
	private WrapperLabel wlaSpalte3 = null;
	private WrapperLabel wlaSpalte5 = null;
	private WrapperLabel wlaSpalte6 = null;
	private WrapperLabel wlaSpalte2 = null;

	private GridBagLayout gridBagLayoutWorkingOn = new GridBagLayout();
	private GridBagLayout gridBagLayoutStatusbar = new GridBagLayout();
	private final static int iHeight = 18;

	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	private JFrame frameNotification = new JFrame();

	private static final int I_QUEUE = 2;
	private static final int I_TOPIC = 3;
	private static final int I_INFO_TOPIC = 4;
	private static final int I_NEUE_NACHRICHTEN = 5;
	private static final int I_BILD_MANDANT = 6;

	private static final int I_MAX = 6;

	private DialogNachrichten dialogNachrichten = null;

	private Timestamp tLetzteUngeleseneNachricht = null;

	public PanelDesktopStatusbar() throws Throwable {
		jbInit();

		Timer timer = new Timer(60000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					showPopupWennNeueNachrichtenVerfuegbar(false);
				} catch (Exception e1) {
					//
				}
			}
		});

		timer.start();

	}

	private void jbInit() throws Throwable {
		Color borderColor = Defaults.getInstance().getDesktopStatusbarBorderColor();
		Border borderStatusbarFeld = borderColor != null ? BorderFactory.createLineBorder(borderColor, 1)
				: BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.white,
						new Color(115, 114, 105), /**
													 * @todo JO 18.03.06 ->lp.properties PJ 5380
													 */
						new Color(165, 163, 151));

		jpaWorkingOn = new JPanel();
		wlaSpalte0 = new WrapperLabel();
		wlaSpalte1 = new WrapperLabel();
		wlaSpalte2 = new WrapperLabel();
		wlaSpalte3 = new WrapperLabel();
		wlaSpalte4 = new WrapperLabel();
		wlaSpalte5 = new WrapperLabel();
		wlaSpalte6 = new WrapperLabel();

		gridBagLayoutWorkingOn = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingOn);
		jpaWorkingOn.setBorder(BorderFactory.createEmptyBorder());

		this.setLayout(gridBagLayoutStatusbar);

		wlaSpalte0.setMaximumSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));
		wlaSpalte0.setMinimumSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));
		wlaSpalte0.setPreferredSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));

		wlaSpalte1.setMaximumSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));
		wlaSpalte1.setMinimumSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));
		wlaSpalte1.setPreferredSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));

		wlaSpalte2.setMaximumSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));
		wlaSpalte2.setMinimumSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));
		wlaSpalte2.setPreferredSize(new Dimension(Helper.getBreiteInPixel(4), iHeight));
		wlaSpalte2.addMouseListener(new PanelDesktopStatusbar_wlaSpalte2_mouseAdapter(this));

		wlaSpalte3.setMaximumSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));
		wlaSpalte3.setMinimumSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));
		wlaSpalte3.setPreferredSize(new Dimension(Helper.getBreiteInPixel(10), iHeight));
		wlaSpalte3.addMouseListener(new PanelDesktopStatusbar_wlaSpalte3_mouseAdapter(this));

		wlaSpalte4.setMaximumSize(new Dimension(10000, iHeight));
		wlaSpalte4.setMinimumSize(new Dimension(10, iHeight));
		wlaSpalte4.setPreferredSize(new Dimension(10, iHeight));
		wlaSpalte4.addMouseListener(new PanelDesktopStatusbar_wlaSpalte4_mouseAdapter(this));

		wlaSpalte5.setMaximumSize(new Dimension(10000, iHeight));
		wlaSpalte5.setMinimumSize(new Dimension(10, iHeight));
		wlaSpalte5.setPreferredSize(new Dimension(10, iHeight));
		wlaSpalte5.addMouseListener(new PanelDesktopStatusbar_wlaSpalte5_mouseAdapter(this));

		wlaSpalte6.setMaximumSize(new Dimension(10000, iHeight));
		wlaSpalte6.setMinimumSize(new Dimension(10, iHeight));
		wlaSpalte6.setPreferredSize(new Dimension(10, iHeight));

		wlaSpalte2.setToolTipText(LPMain.getInstance().getTextRespectUISPr("lp.queue"));

		wlaSpalte0.setBorder(borderStatusbarFeld);
		wlaSpalte1.setBorder(borderStatusbarFeld);
		wlaSpalte2.setBorder(borderStatusbarFeld);
		wlaSpalte3.setBorder(borderStatusbarFeld);
		wlaSpalte4.setBorder(borderStatusbarFeld);
		wlaSpalte5.setBorder(borderStatusbarFeld);
		wlaSpalte6.setBorder(borderStatusbarFeld);

		Font defaultFont = HelperClient.getDefaultFont();
		// ca. 90% der standardgroesse
		Font statusbarFont = new Font(defaultFont.getFontName(), defaultFont.getStyle(),
				(defaultFont.getSize() * 9) / 10);
		wlaSpalte0.setFont(statusbarFont);
		wlaSpalte1.setFont(statusbarFont);
		wlaSpalte2.setFont(statusbarFont);
		wlaSpalte3.setFont(statusbarFont);
		wlaSpalte4.setFont(statusbarFont);
		wlaSpalte5.setFont(statusbarFont);
		wlaSpalte6.setFont(statusbarFont);

		wlaSpalte0.setHorizontalAlignment(SwingConstants.CENTER);
		wlaSpalte1.setHorizontalAlignment(SwingConstants.CENTER);
		wlaSpalte2.setHorizontalAlignment(SwingConstants.CENTER);
		wlaSpalte3.setHorizontalAlignment(SwingConstants.CENTER);
		wlaSpalte4.setHorizontalAlignment(SwingConstants.LEFT);
		wlaSpalte5.setHorizontalAlignment(SwingConstants.LEFT);
		wlaSpalte6.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaSpalte0, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte3, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte4, new GridBagConstraints(4, 0, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte5, new GridBagConstraints(5, 0, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte6, new GridBagConstraints(6, 0, 1, 1, 0.4, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * Einen Wert in ein Label setzen.
	 * 
	 * @param iWhichI   int
	 * @param valueLblI Object
	 */
	private void setValueAt(int iWhichI, Object valueLblI) {
		String sText = null;
		if (valueLblI == null) {
			sText = "";
		} else if (valueLblI instanceof String) {
			sText = (String) valueLblI;
		} else if (valueLblI instanceof java.util.Date) {
			java.util.Date dDatum = (java.util.Date) valueLblI;
			sText = Helper.formatDatum(dDatum, Defaults.getInstance().getLocUI());
		} else if (valueLblI instanceof java.sql.Timestamp) {
			java.util.Date dDatum = new java.sql.Timestamp(((java.util.Date) valueLblI).getTime());
			sText = Helper.formatDatum(dDatum, Defaults.getInstance().getLocUI());
		} else {
			sText = valueLblI.toString();
		}

		switch (iWhichI) {
		case 0:
			wlaSpalte0.setText(sText);
			break;
		case 1:
			wlaSpalte1.setText(sText);
			break;
		case 2:
			wlaSpalte2.setText(sText);
			break;
		case 3:
			wlaSpalte3.setText(sText);
			break;
		case 4:
			wlaSpalte4.setText(sText);
			break;
		case 5:
			if (sText != null) {
				sText = "<html><b>" + sText;

			}

			wlaSpalte5.setText(sText);
			break;
		case 6:
			wlaSpalte6.setText(sText);
			break;
		default:
			wlaSpalte0.setText("0 >" + iWhichI + "> 5 sorry");
		}
	}

	/**
	 * Leere die StatusBar.
	 */
	public void clearStatusbar() {
		for (int i = 0; i <= I_MAX; i++) {
			this.setValueAt(i, null);
		}
	}

	public void setLPQueue(Object o) {
		this.setValueAt(I_QUEUE, o);
	}

	public void setLPTopic(Object o) {
		this.setValueAt(I_TOPIC, o);
	}

	public void setLPInfoTopic(Object o) {
		this.setValueAt(I_INFO_TOPIC, o);
	}

	public void setNeueNachrichten(Object o) {
		this.setValueAt(I_NEUE_NACHRICHTEN, o);
	}

	public void setBildMandant(Icon o) {
		if (o == null) {
			wlaSpalte6.setIcon(null);
		} else {
			wlaSpalte6.setIcon(o);
		}

	}

	protected Border getStatusbarBorder() {
		Color borderColor = Defaults.getInstance().getDesktopStatusbarBorderColor();
		return borderColor != null ? BorderFactory.createLineBorder(borderColor, 1)
				: BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.white,
						new Color(115, 114, 105), /** @todo JO 18.03.06 ->lp.properties PJ 5380 */
						new Color(165, 163, 151));
	}

	public void redraw() {
		Border border = getStatusbarBorder();
		wlaSpalte0.setBorder(border);
		wlaSpalte1.setBorder(border);
		wlaSpalte2.setBorder(border);
		wlaSpalte3.setBorder(border);
		wlaSpalte4.setBorder(border);
		wlaSpalte5.setBorder(border);
		wlaSpalte6.setBorder(border);

		this.invalidate();
		this.validate();
		this.repaint();
	}

	public void wlaSpalte2_mouseClicked(MouseEvent e) {
		JOptionPane pane = InternalFrame.getNarrowOptionPane(80);
		pane.setMessageType(JOptionPane.INFORMATION_MESSAGE);

		ArrayList<?> al = null;
		String msg = null;
		try {
			al = DelegateFactory.getInstance().getLPAsynchSubscriber().browse();

			MessageFormat mf = new MessageFormat(LPMain.getInstance().getTextRespectUISPr("lp.queue.jobs"));
			mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());
			Object pattern[] = { al.size() + "" };
			msg = mf.format(pattern) + Helper.LINE_SEPARATOR;

			if (DelegateFactory.getInstance().getLPAsynchSubscriber().getMsg() != null) {
				msg += DelegateFactory.getInstance().getLPAsynchSubscriber().getLpmLast().getBbJobPositivProcessed()
						+ ": "
						+ new Time(DelegateFactory.getInstance().getLPAsynchSubscriber().getMsg().getJMSTimestamp())
						+ " | "
						+ DelegateFactory.getInstance().getLPAsynchSubscriber().getLpmLast()
								.getSSender2stelligesModulKuerzel()
						+ " | " + DelegateFactory.getInstance().getLPAsynchSubscriber().getLpmLast().getSUser() + " | "
						+ DelegateFactory.getInstance().getLPAsynchSubscriber().getLpmLast().getIiWhatIWant()
						+ Helper.LINE_SEPARATOR;
			}

			for (int i = 0; i < al.size(); i++) {
				ObjectMessage mess = (ObjectMessage) al.get(i);
				LPMessage lpm = (LPMessage) mess.getObject();

				msg += i + 1 + ". | " + lpm.getSSender2stelligesModulKuerzel() + " | " + lpm.getSUser() + " | "
						+ lpm.getIiWhatIWant() + " | " + mess.getJMSPriority() + " | "
						+ ((System.currentTimeMillis() - mess.getJMSTimestamp()) / 1000) + " [s] | "
						+ Helper.LINE_SEPARATOR;
			}
			pane.setMessage(msg);
		} catch (Throwable ex) {
			// Wir brechen hier nicht ab.
			myLogger.error(ex.getMessage());
		}
		JDialog dialog = pane.createDialog(this, LPMain.getInstance().getTextRespectUISPr("lp.queue.title"));
		dialog.setVisible(true);

	}

	public void showPopupWennNeueNachrichtenVerfuegbar(boolean bClientStart) {

		setNeueNachrichten("");

		try {

			ArrayList<Map<Integer, String>> mNachrichten = DelegateFactory.getInstance().getNachrichtenDelegate()
					.getListeDerNachrichtenFuerEinePerson(LPMain.getTheClient().getIDPersonal(), true, 999,null,null, true);

			if (mNachrichten != null && mNachrichten.size() > 0) {

				zeigeNotificationAn(mNachrichten, bClientStart);

			}

		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void zeigeNotificationAn(ArrayList<Map<Integer, String>> mNachrichten, boolean bClientStart)
			throws Throwable {

		setzeAnzahlDerUngelesenenNachrichten(mNachrichten);

		int iArt = 1;
		ArbeitsplatzparameterDto paramDto = DelegateFactory.getInstance().getParameterDelegate()
				.holeArbeitsplatzparameter(ParameterFac.ARBEITSPLATZPARAMETER_PUSH_BENACHRICHTIGUNG);
		if (paramDto != null) {
			iArt = new Integer(paramDto.getCWert());
		}

		if (iArt == 1 || iArt == 2) {

			if (mNachrichten != null && mNachrichten.size() > 0) {
				Map<Integer, String> m = mNachrichten.get(0);

				Integer nachrichtenermpfaengerIId = m.keySet().iterator().next();

				Timestamp anlageDatumDerLetztenUngelesenenNachricht = DelegateFactory.getInstance()
						.getNachrichtenDelegate().getAnlageDatumDerNachricht(nachrichtenermpfaengerIId);

				if (tLetzteUngeleseneNachricht == null
						|| tLetzteUngeleseneNachricht.getTime() < anlageDatumDerLetztenUngelesenenNachricht.getTime()) {

					tLetzteUngeleseneNachricht=anlageDatumDerLetztenUngelesenenNachricht;
					
					String betreff = DelegateFactory.getInstance().getNachrichtenDelegate()
							.getBetreff(nachrichtenermpfaengerIId);

					String header = "";// "Neueste Nachricht:";
					String message = m.values().iterator().next();
					if (bClientStart) {
						if (mNachrichten.size() == 1) {
							header = "Es ist 1 Nachricht vorhanden";
						} else {
							header = "Es sind " + mNachrichten.size() + " Nachrichten vorhanden";
						}

					}

					if (iArt == 1) {

						header = "<html><u><b>" + header;

						if (frameNotification != null) {
							frameNotification.setVisible(false);
							frameNotification.dispose();
						}
						frameNotification = new JFrame();
						if (bClientStart) {
							frameNotification.setSize(350, 30);
						} else {
							frameNotification.setSize(350, 50);
						}
						frameNotification.setLayout(new GridBagLayout());

						frameNotification.addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent me) {
								try {
									zeigeNachrichtenDialogAn();
									frameNotification.dispose();
								} catch (Throwable e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});

						GridBagConstraints constraints = new GridBagConstraints();
						constraints.gridx = 0;
						constraints.gridy = 0;
						constraints.weightx = 1.0f;
						constraints.weighty = 1.0f;
						constraints.insets = new Insets(5, 5, 5, 5);
						constraints.fill = GridBagConstraints.BOTH;
						JLabel headingLabel = new JLabel(header);
						// headingLabel.setIcon(IconFactory.getHeliumv()); // --- use image icon you
						// want to be as
						// heading image.
						headingLabel.setOpaque(false);
						frameNotification.add(headingLabel, constraints);
						constraints.gridx++;
						constraints.weightx = 0f;
						constraints.weighty = 0f;
						constraints.fill = GridBagConstraints.NONE;
						constraints.anchor = GridBagConstraints.NORTH;

						JButton cloesButton = new JButton(new AbstractAction("x") {
							@Override
							public void actionPerformed(final ActionEvent e) {
								frameNotification.dispose();
							}
						});

						cloesButton.setMargin(new Insets(1, 4, 1, 4));
						cloesButton.setFocusable(false);
						frameNotification.add(cloesButton, constraints);
						constraints.gridx = 0;
						constraints.gridy++;
						constraints.weightx = 1.0f;
						constraints.weighty = 1.0f;
						constraints.insets = new Insets(5, 5, 5, 5);
						constraints.fill = GridBagConstraints.BOTH;

						String html = m.values().iterator().next();
						if (bClientStart) {
							html = "";

						} else {
							header = html.split("->")[0];
							headingLabel.setText(header);

							html = "<html><b>" + betreff;
						}

						JLabel messageLabel = new JLabel(html);
						frameNotification.add(messageLabel, constraints);
						frameNotification.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
						frameNotification.setUndecorated(true);
						frameNotification.setLocationRelativeTo(LPMain.getInstance().getDesktop());
						frameNotification.setAlwaysOnTop(true);

						int x = (int) LPMain.getInstance().getDesktop().getLocationOnScreen().getX()
								+ LPMain.getInstance().getDesktop().getWidth() - frameNotification.getWidth();

						int y = (int) LPMain.getInstance().getDesktop().getLocationOnScreen().getY()
								+ LPMain.getInstance().getDesktop().getHeight() - frameNotification.getHeight();

						frameNotification.setLocation(x - 10, y - 30);
						frameNotification.setVisible(true);

						new Thread() {
							@Override
							public void run() {
								try {
									Thread.sleep(10000); // time after which pop up will be disappeared.
									if (frameNotification != null) {
										frameNotification.dispose();
									}
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							};
						}.start();

					} else if (iArt == 2) {

						if (SystemTray.isSupported()) {

							SystemTray tray = SystemTray.getSystemTray();
							Image image = IconFactory.getHeliumv().getImage();
							TrayIcon trayIcon = new TrayIcon(image, "Helium 5");
							trayIcon.setImageAutoSize(true);
							trayIcon.setToolTip("Helium 5");
							tray.add(trayIcon);

							if (bClientStart) {
								if (mNachrichten.size() == 1) {
									header = "Es ist 1 Nachricht vorhanden";
								} else {
									header = "Es sind " + mNachrichten.size() + " Nachrichten vorhanden";
								}
								trayIcon.displayMessage("Helium 5 Benachrichtigung", header, MessageType.INFO);
							} else {
								trayIcon.displayMessage("Helium 5 Benachrichtigung",
										Helper.strippHTML(message).replaceAll("->", "\n"), MessageType.INFO);
							}

						}
					}
				}
			}

		}

	}

	public void setzeAnzahlDerUngelesenenNachrichten(ArrayList<Map<Integer, String>> mNachrichten) throws Throwable {

		if (mNachrichten == null) {
			mNachrichten = DelegateFactory.getInstance().getNachrichtenDelegate()
					.getListeDerNachrichtenFuerEinePerson(LPMain.getTheClient().getIDPersonal(), true, 999,null,null,true);
		}

		if (mNachrichten != null && mNachrichten.size() > 0) {

			if (mNachrichten.size() == 1) {

				setNeueNachrichten(LPMain.getInstance().getTextRespectUISPr("lp.eineneuenachricht"));

			} else {
				setNeueNachrichten(
						LPMain.getInstance().getMessageTextRespectUISPr("lp.neuenachrichten", mNachrichten.size()));
			}
		} else {
			setNeueNachrichten(null);
		}
	}

	public void zeigeNachrichtenDialogAn() throws Throwable {

		if (dialogNachrichten == null) {
			dialogNachrichten = new DialogNachrichten(null, this);
		} else {
			dialogNachrichten.refreshList();
		}

		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dialogNachrichten);
		dialogNachrichten.setVisible(true);

	}

	public void wlaSpalte3_mouseClicked(MouseEvent e) {
		try {

			if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_SYSTEM)) {
				InternalFrameSystem ifSystem = (InternalFrameSystem) LPMain.getInstance().getDesktop()
						.holeModul(LocaleFac.BELEGART_SYSTEM);
				ifSystem.geheZu(InternalFrameSystem.IDX_PANE_NACHRICHTENARCHIV,
						TabbedPaneNachrichtarchiv.IDX_PANEL_NACHRICHTARCHIV, null, null, null);
			}

		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void wlaSpalte4_mouseClicked(MouseEvent e) {
		try {
			String meldung = JOptionPane.showInputDialog("Meldung eingeben:");
			if (meldung.length() > 0)
				LPMain.getInstance().getInfoTopic().send2AllUser(meldung);
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void wlaSpalte5_mouseClicked(MouseEvent e) {
		try {
			/*
			 * if (e.getButton() == MouseEvent.BUTTON3) { ArrayList<Map<Integer, String>>
			 * mNachrichten = DelegateFactory.getInstance().getNachrichtenDelegate()
			 * .getListeDerNachrichtenFuerEinePerson(LPMain.getTheClient().getIDPersonal(),
			 * true, 999);
			 * 
			 * if (mNachrichten != null && mNachrichten.size() > 0) {
			 * zeigeNotificationAn(mNachrichten, false); } } else {
			 */

			zeigeNachrichtenDialogAn();
			// }

		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}

class PanelDesktopStatusbar_wlaSpalte2_mouseAdapter extends MouseAdapter {
	private PanelDesktopStatusbar adaptee;

	PanelDesktopStatusbar_wlaSpalte2_mouseAdapter(PanelDesktopStatusbar adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.wlaSpalte2_mouseClicked(e);
	}
}

class PanelDesktopStatusbar_wlaSpalte3_mouseAdapter extends MouseAdapter {
	private PanelDesktopStatusbar adaptee;

	PanelDesktopStatusbar_wlaSpalte3_mouseAdapter(PanelDesktopStatusbar adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.wlaSpalte3_mouseClicked(e);
	}
}

class PanelDesktopStatusbar_wlaSpalte4_mouseAdapter extends MouseAdapter {
	private PanelDesktopStatusbar adaptee;

	PanelDesktopStatusbar_wlaSpalte4_mouseAdapter(PanelDesktopStatusbar adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.wlaSpalte4_mouseClicked(e);
	}
}

class PanelDesktopStatusbar_wlaSpalte5_mouseAdapter extends MouseAdapter {
	private PanelDesktopStatusbar adaptee;

	PanelDesktopStatusbar_wlaSpalte5_mouseAdapter(PanelDesktopStatusbar adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.wlaSpalte5_mouseClicked(e);
	}
}
