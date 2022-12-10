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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperTristateCheckbox;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;

public class StklImportPage3ViewEinkauf extends StklImportPage3View {

	private static final long serialVersionUID = -5595252273539094883L;

	private WrapperTristateCheckbox wtcPreisuebernahme = null;

	public StklImportPage3ViewEinkauf(StklImportPage3CtrlEinkauf controller,
			InternalFrame iFrame) {
		super(controller, iFrame);
		ListenerEinkauf lv = new ListenerEinkauf();
		cellEditorChooser.addItemListener(lv);
		setAndAddListener(lv);
	}

	@Override
	public void dataUpdated() {
		super.dataUpdated();

		int status = ((StklImportPage3CtrlEinkauf)getController()).getPreisuebernahmeStatus();
		if(wtcPreisuebernahme != null) {
			if(status != WrapperTristateCheckbox.DISABLE) {
				wtcPreisuebernahme.setVisible(!getController().isBusyImporting());
				wtcPreisuebernahme.setSelection(status);
			} else {
				wtcPreisuebernahme.setVisible(false);
			}
		}
	}

	@Override
	protected void initViewImpl() {
		super.initViewImpl();
		
		wtcPreisuebernahme = new WrapperTristateCheckbox(
				LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.preisuebernahme.tristatecheckbox"));
		wtcPreisuebernahme.addActionListener(l);
		wtcPreisuebernahme.setSelected(false);
		setLayout(new MigLayout("wrap 6, hidemode 0, fill", "[fill, grow]push[fill, grow|fill, grow|align right|align right]", "[fill|fill|fill, grow]"));
		add(progressBar, "span");
		add(zusammengefasst);
		add(mitHandartikelBefuellen);
		add(wtcPreisuebernahme, "skip, width 140");
		add(wtcMappingUpdate, "width 140");
		add(new JScrollPane(table), "newline, span");
	}

	protected class ListenerEinkauf extends Listener implements ItemListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
		
			if(wtcPreisuebernahme != null && e.getSource() == wtcPreisuebernahme) {
				((StklImportPage3CtrlEinkauf)getController()).selektierePreisuebernahme(wtcPreisuebernahme.getSelection());
			}
		}
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getSource() == cellEditorChooser && e.getStateChange() == ItemEvent.SELECTED) {
				ArtikelDto selectedDto = (ArtikelDto) e.getItem();
				if(selectedDto == null)
					return;
				
				if(selectedDto != StklImportPage3Ctrl.HANDARTIKEL 
						&& selectedDto != StklImportPage3Ctrl.FLR_LISTE 
						&& selectedDto != StklImportPage3Ctrl.ZUVIELE_ARTIKEL_FLR_LISTE) {
					((StklImportPage3CtrlEinkauf)getController()).fetchLiefPreisIfExist(
							cellEditorChooser.getResult().getSelectedArtikelDto().getIId(), 
							cellEditorChooser.getResult());
					((StklImportPage3CtrlEinkauf)getController()).setPreisuebernahmeStatusAusStklImportResults();
				}
			}
			
		}
		
	}
}
