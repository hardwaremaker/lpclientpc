
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
package com.lp.client.artikel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.InventurlisteDto;
import com.lp.server.artikel.service.InventurprotokollDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.system.service.LocaleFac;

public class DialogEinstandspreisInventur extends JDialog implements ActionListener, FocusListener {

	private static final long serialVersionUID = -5704551591990693731L;

	private JPanel panel = null;
	private WrapperButton wbuSpeichern = null;
	private WrapperButton wbuAbbrechen = null;

	private WrapperLabel wlaPreis = new WrapperLabel();
	private WrapperNumberField wnfPreis = new WrapperNumberField();

	private HvLayout hvLayout = null;

	public BigDecimal bdPreis = null;

	private InventurprotokollDto inventurprotokollDto = null;
	private InventurlisteDto inventurlisteDto = null;
	private LagerbewegungDto lBewDto = null;

	private InternalFrame intFrame = null;

	public DialogEinstandspreisInventur(InventurprotokollDto inventurprotokollDto, InternalFrame intFrame)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.inventurprotokollDto = inventurprotokollDto;
		this.intFrame = intFrame;
		jbInit();
		setContentPane(panel);

		Dimension d = new Dimension(Defaults.sizeFactor(380), Defaults.sizeFactor(160));
		setSize(d.width, d.height);

		setTitle(LPMain.getTextRespectUISPr("lp.preis"));

		addEscapeListener(this);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		focusLost(new FocusEvent(wnfPreis, -1));

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

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSpeichern)) {
			try {
				if (wnfPreis.getBigDecimal() == null) {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				if (wnfPreis.getBigDecimal() != null) {

					DelegateFactory.getInstance().getInventurDelegate().eintandspreisEinerInventurbuchungAendern(
							inventurlisteDto.getIId(), wnfPreis.getBigDecimal());

				}

				setVisible(false);

			} catch (Throwable e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			bdPreis = null;
			setVisible(false);
		}

	}

	private void jbInit() throws Throwable {

		panel = new JPanel();

		wbuSpeichern = new WrapperButton();
		wbuAbbrechen = new WrapperButton();

		wbuSpeichern.setText(LPMain.getTextRespectUISPr("button.ok"));
		wbuAbbrechen.setText(LPMain.getTextRespectUISPr("Cancel"));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		wlaPreis.setText(LPMain.getTextRespectUISPr("bes.einstandspreis") + " ("
				+ LPMain.getTheClient().getSMandantenwaehrung() + ")");

		wnfPreis.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());

		wnfPreis.setMandatoryField(true);

		inventurlisteDto = DelegateFactory.getInstance().getInventurDelegate()
				.inventurlisteFindByPrimaryKey(inventurprotokollDto.getInventurlisteIId());

		lBewDto = DelegateFactory.getInstance().getLagerDelegate().getLetzteintrag(LocaleFac.BELEGART_INVENTUR,
				inventurprotokollDto.getInventurlisteIId(), inventurlisteDto.getCSeriennrchargennr());

		wnfPreis.setBigDecimal(lBewDto.getNEinstandspreis());

		wnfPreis.addFocusListener(this);

		add(panel);

		hvLayout = new HvLayout(panel, null, "[right, grow]", null);
		hvLayout.add(wlaPreis, 150).add(wnfPreis, 150).wrap().add(wbuSpeichern, "center", 90).add(wbuAbbrechen, 90)
				.wrap();

	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {

	}

}
