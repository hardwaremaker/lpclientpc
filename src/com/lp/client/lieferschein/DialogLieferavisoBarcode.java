package com.lp.client.lieferschein;

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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.PanelAdditiveVerpackungsmengen;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogLieferavisoBarcode extends JDialog implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelUrlaubsanspruch = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	WrapperLabel wlaLieferschein = new WrapperLabel();
	JTextField wtfLieferschein = new JTextField();
	JButton wbuFertig = new JButton();

	LieferscheinDto lieferscheinDto = null;

	public LieferscheinDto getLieferscheinDto() {
		return lieferscheinDto;
	}

	PanelQuery panelQueryLieferschein = null;

	public DialogLieferavisoBarcode(PanelQuery panelQueryLieferschein) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance().getTextRespectUISPr("ls.lieferaviso"), true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.panelQueryLieferschein = panelQueryLieferschein;
		jbInit();
		pack();
		wtfLieferschein.requestFocus();
	}

	public void dispose() {
		try {

			if (wtfLieferschein.getText() != null) {

				KeyEvent k = new KeyEvent(wtfLieferschein, 0, 0, 0, KeyEvent.VK_ENTER, ' ');

				keyPressed(k);
			}

		} catch (Throwable ex) {
			panelQueryLieferschein.handleException(ex, true);
		}

		super.dispose();
	}

	private void jbInit() throws Throwable {
		panelUrlaubsanspruch.setLayout(gridBagLayout1);

		wlaLieferschein.setText(LPMain.getInstance().getTextRespectUISPr("label.lieferschein"));

		wtfLieferschein.addKeyListener(this);

		wbuFertig.setText(LPMain.getInstance().getTextRespectUISPr("lp.beenden"));
		wbuFertig.addActionListener(new DialogLieferavisoBarcode_wbuFertig_actionAdapter(this));
		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(panelUrlaubsanspruch, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 250, 50));

		panelUrlaubsanspruch.add(wtfLieferschein, new GridBagConstraints(2, 1, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaLieferschein, new GridBagConstraints(0, 1, 1, 1, 0.15, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wbuFertig, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public void wbuFertig_actionPerformed(ActionEvent e) {

		this.dispose();
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == wtfLieferschein) {

				try {

					if (wtfLieferschein.getText() != null && wtfLieferschein.getText().length() > 0) {
						LieferscheinDto lsDto = DelegateFactory.getInstance().getLsDelegate()
								.lieferscheinFindByCNr(wtfLieferschein.getText());
						if (lsDto != null) {

							lieferscheinDto = lsDto;
							setVisible(false);
						} else {
							DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
									"Lieferschein " + wtfLieferschein.getText() + " konnte nicht gefunden werden.");

							wtfLieferschein.setText(null);

							wtfLieferschein.requestFocus();
						}
					}
				} catch (Throwable ex) {
					if (ex instanceof ExceptionLP) {
						ExceptionLP exLP = (ExceptionLP) ex;
						if (exLP.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
							DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
									"Lieferschein " + wtfLieferschein.getText() + " konnte nicht gefunden werden.");

							wtfLieferschein.setText(null);

							wtfLieferschein.requestFocus();
						} else {
							panelQueryLieferschein.handleException(ex, true);
						}
					}

					else {
						panelQueryLieferschein.handleException(ex, true);
					}

				}

			}

		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}

class DialogLieferavisoBarcode_wbuFertig_actionAdapter implements ActionListener {
	private DialogLieferavisoBarcode adaptee;

	DialogLieferavisoBarcode_wbuFertig_actionAdapter(DialogLieferavisoBarcode adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuFertig_actionPerformed(e);
	}
}
