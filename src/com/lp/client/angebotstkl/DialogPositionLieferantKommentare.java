
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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.PositionlieferantDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.util.EJBExceptionLP;

@SuppressWarnings("static-access")
public class DialogPositionLieferantKommentare extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelUrlaubsanspruch = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	WrapperLabel wlaBemerkungLF = new WrapperLabel();
	WrapperLabel wlaBemerkungIntern = new WrapperLabel();
	WrapperLabel wlaBemerkungVK = new WrapperLabel();
	WrapperTextField wtfBemerkungLF = new WrapperTextField();
	WrapperTextField wtfBemerkungIntern = new WrapperTextField();
	WrapperTextField wtfBemerkungVK = new WrapperTextField();

	JButton wbuSpeichern = new JButton();

	private PositionlieferantDto lfDto = null;

	private InternalFrame frame = null;

	public boolean bSpeichen = false;
	private boolean nurLesend = false;

	public DialogPositionLieferantKommentare(Integer positionslieferantIId, boolean nurLesend, InternalFrame frame)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.bemerkung.bearbeiten"), true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		this.frame = frame;
		this.nurLesend = nurLesend;

		lfDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.positionlieferantFindByPrimaryKey(positionslieferantIId);

		jbInit();
		pack();
		wtfBemerkungLF.requestFocus();
		this.setSize(800, 150);

	}

	private void jbInit() throws Throwable {

		panelUrlaubsanspruch.setLayout(gridBagLayout1);

		wlaBemerkungLF.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.bemerkung"));
		wlaBemerkungIntern.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.bemerkungintern"));
		wlaBemerkungVK.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.bemerkungverkauf"));

		wtfBemerkungLF.setColumnsMax(300);
		wtfBemerkungIntern.setColumnsMax(300);
		wtfBemerkungVK.setColumnsMax(300);

		wtfBemerkungLF.setText(lfDto.getCBemerkung());
		wtfBemerkungIntern.setText(lfDto.getCBemerkungIntern());
		wtfBemerkungVK.setText(lfDto.getCBemerkungVerkauf());

		
		if(nurLesend) {
			wtfBemerkungLF.setEditable(false);
			wtfBemerkungIntern.setEditable(false);
			wtfBemerkungVK.setEditable(false);
		}
		
		wbuSpeichern.setText(LPMain.getInstance().getTextRespectUISPr("lp.speichern"));

		wbuSpeichern.addActionListener(this);
		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(panelUrlaubsanspruch, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 250, 50));

		panelUrlaubsanspruch.add(wlaBemerkungLF, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wtfBemerkungLF, new GridBagConstraints(1, 0, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wlaBemerkungIntern, new GridBagConstraints(0, 1, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wtfBemerkungIntern, new GridBagConstraints(1, 1, 1, 1, 0.3, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wlaBemerkungVK, new GridBagConstraints(0, 2, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wtfBemerkungVK, new GridBagConstraints(1, 2, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wbuSpeichern, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(wbuSpeichern)) {
			try {
				lfDto.setCBemerkung(wtfBemerkungLF.getText());
				lfDto.setCBemerkungIntern(wtfBemerkungIntern.getText());
				lfDto.setCBemerkungVerkauf(wtfBemerkungVK.getText());

				DelegateFactory.getInstance().getAngebotstklDelegate().updatePositionlieferant(lfDto);
				bSpeichen = true;
			} catch (Throwable ex) {
				frame.handleException(ex, false);
			}

			this.setVisible(false);

		}
	}

}
