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
package com.lp.client.finanz.sepaimportassistent;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.DialogError;
import com.lp.client.frame.assistent.view.AssistentPageView;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.pc.LPMain;

public class SepaImportPage5View extends AssistentPageView {

	private static final long serialVersionUID = 1L;
	
	private SepaImportPage5Ctrl controller;
	private WrapperTextArea wtaClientInfo;
	private JProgressBar progressBar;

	public SepaImportPage5View(SepaImportPage5Ctrl controller,
			InternalFrame iFrame) {
		super(controller, iFrame);
		this.controller = controller;
	}

	@Override
	public void dataUpdated() {
		progressBar.setModel(getController().getProgressModel());
		progressBar.setVisible(getController().isBusyImporting());

		if (getController().getImportThrowable() != null) {
//			getInternalFrame().handleException(getController().getImportThrowable(), false);
			new DialogError(LPMain.getInstance().getDesktop(), getController().getImportThrowable(),
					DialogError.TYPE_ERROR);
		}

		wtaClientInfo.setText(getClientInfoText(getController().getClientInfo()));
	}

	private String getClientInfoText(List<String> clientInfo) {
		StringBuilder builder = new StringBuilder();
		for (String line : clientInfo) {
			builder.append(line);
			builder.append("\n");
		}
		return builder.toString();
	}

	@Override
	public SepaImportPage5Ctrl getController() {
		return controller;
	}

	@Override
	public String getTitle() {
		return LPMain.getTextRespectUISPr("fb.sepa.import.sepakontoauszug");
	}

	@Override
	protected void initViewImpl() {
		progressBar = new JProgressBar(getController().getProgressModel());
		
		wtaClientInfo = new WrapperTextArea();
		wtaClientInfo.setEditable(false);
		wtaClientInfo.setBorder(BorderFactory.createEtchedBorder());
		wtaClientInfo.setBackground(Color.WHITE);
		
		setLayout(new MigLayout("wrap 3, hidemode 0", 
				"[15%,fill|70%,fill|15%,fill]", 
				"[fill|fill,grow]"));
		add(progressBar, "span");
		add(wtaClientInfo, "skip 1");
	}

}
