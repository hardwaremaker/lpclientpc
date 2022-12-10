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
package com.lp.client.stueckliste.importassistent;

import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.pc.LPMain;

public class StklImportPage3ViewVerkauf extends StklImportPage3View {

	private static final long serialVersionUID = -3177127401698503141L;

	protected WrapperCheckBox cbUpdateArtikel;
	
	public StklImportPage3ViewVerkauf(StklImportPage3CtrlVerkauf controller,
			InternalFrame iFrame) {
		super(controller, iFrame);
		ListenerVerkauf le = new ListenerVerkauf();
		setAndAddListener(le);
	}

	@Override
	public void dataUpdated() {
		super.dataUpdated();

		cbUpdateArtikel.setVisible(!getController().isBusyImporting());
		cbUpdateArtikel.setEnabled(((StklImportPage3CtrlVerkauf)getController()).darfArtikelUpdaten());
	}

	@Override
	protected void initViewImpl() {
		super.initViewImpl();

		cbUpdateArtikel = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.artikelupdaten"));
		cbUpdateArtikel.addActionListener(l);

		setLayout(new MigLayout("wrap 5, hidemode 2, fill", "[fill, grow]push[fill, grow|fill, grow|align right|align right]", "[fill|fill|fill, grow]"));
		add(progressBar, "span");
		add(zusammengefasst);
		add(mitHandartikelBefuellen);
		if (getController().isDemo()) {
			add(mitArtikelErzeugen);
			add(cbUpdateArtikel, "width 110");
		} else {
			add(cbUpdateArtikel, "skip, width 110");
		}
		
		add(wtcMappingUpdate, "width 120");
		add(new JScrollPane(table), "newline, span");
	}

	protected class ListenerVerkauf extends Listener {

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			if(e.getSource() == cbUpdateArtikel) {
				getController().setUpdateArtikel(cbUpdateArtikel.isSelected());
			}
		}
	}
}
