
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
package com.lp.client.stueckliste;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.SystemServicesFac;

@SuppressWarnings("static-access")
public class DialogMaschineErsetzen extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	
	private WrapperSelectField wsfMaschineAlt = null;
	private WrapperSelectField wsfMaschineNeu = null;
	private JButton btnOK = new JButton();
	private JButton btnAbbrechen = new JButton();

	InternalFrameStueckliste internalFrame = null;

	public DialogMaschineErsetzen(InternalFrameStueckliste internalFrame) throws Throwable {
		super(LPMain.getInstance().getDesktop(),
				LPMain.getInstance().getTextRespectUISPr("stkl.maschine.ersetzendurch"), true);
		this.internalFrame = internalFrame;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		// Gespeicherte Werte setzen
		setKeyValueDtos(DelegateFactory.getInstance().getSystemDelegate()
				.keyvalueFindyByCGruppe(SystemServicesFac.KEYVALUE_ARTIKEL_KOPIEREN));
		this.setSize(370, 200);

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

		if (e.getSource().equals(btnAbbrechen)) {
			this.setVisible(false);
		} else {

			// Artikel ersetzen

			if (wsfMaschineAlt.getIKey() == null || wsfMaschineNeu.getIKey() == null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
				return;
			}
			try {
				DelegateFactory.getInstance().getStuecklisteDelegate().maschineErsetzen(
						wsfMaschineAlt.getIKey(), wsfMaschineNeu.getIKey());
			} catch (Throwable e1) {
				internalFrame.getTabbedPaneStueckliste().handleException(e1, true);
			}
			this.setVisible(false);
		}

	}

	private void setKeyValueDtos(KeyvalueDto[] dtos) throws Throwable {

		for (int z = 0; z < dtos.length; z++) {

			for (int i = 0; i < this.getContentPane().getComponents().length; ++i) {

				{

					if (this.getContentPane().getComponents()[i].getName() != null
							&& this.getContentPane().getComponents()[i].getName().equals(dtos[z].getCKey())) {
						if (this.getContentPane().getComponents()[i] instanceof WrapperCheckBox) {

							WrapperCheckBox wcb = (WrapperCheckBox) this.getContentPane().getComponents()[i];
							wcb.setShort(new Short(dtos[z].getCValue()));

						}
					}
				}
			}
		}

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		btnOK.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));
		btnAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr("lp.abbrechen"));
		btnOK.addActionListener(this);
		btnAbbrechen.addActionListener(this);

		wsfMaschineAlt = new WrapperSelectField(WrapperSelectField.MASCHINE,internalFrame, false);
		wsfMaschineNeu = new WrapperSelectField(WrapperSelectField.MASCHINE,internalFrame, false);

		wsfMaschineAlt.setMandatoryField(true);
		wsfMaschineNeu.setMandatoryField(true);

		this.addEscapeListener(this);

		this.getContentPane().setLayout(gridBagLayout2);

		int iZeile = 0;
		this.getContentPane().add(
				new JLabel(LPMain.getInstance().getTextRespectUISPr("stkl.maschine.ersetzendurch.quelle")),
				new GridBagConstraints(0, iZeile, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 2, 5, 2), 100, 0));
		iZeile++;
		this.getContentPane().add(wsfMaschineAlt.getWrapperButton(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 2, 5, 2), 80, 0));
		this.getContentPane().add(wsfMaschineAlt.getWrapperTextField(), new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 2, 5, 2), 150, 0));
		
		iZeile++;
		this.getContentPane().add(
				new JLabel(LPMain.getInstance().getTextRespectUISPr("stkl.artikel.ersetztendurch.ziel")),
				new GridBagConstraints(0, iZeile, 2, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(5, 2, 5, 2), 0, 0));

		iZeile++;

		this.getContentPane().add(wsfMaschineNeu.getWrapperButton(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 2, 5, 2), 0, 0));
		this.getContentPane().add(wsfMaschineNeu.getWrapperTextField(), new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 2, 5, 2), 0, 0));
		

		iZeile++;

		this.getContentPane().add(btnOK, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(5, 2, 5, 2), 0, 0));
		this.getContentPane().add(btnAbbrechen, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(5, 2, 5, 2), 0, 0));

	}

}
