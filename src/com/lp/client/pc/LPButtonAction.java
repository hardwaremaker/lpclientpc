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

import javax.swing.JButton;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.benutzer.service.RechteFac;

public class LPButtonAction implements ILPButtonAction {
	private static final LpLogger myLogger = (LpLogger) LpLogger.getInstance(LPButtonAction.class);

	private boolean enable;
	private JButton button;
	private int iSort;
	private String recht;

	public LPButtonAction(JButton buttonI, String rechtCNr) {
		button = buttonI;
		setRecht(rechtCNr);
//		recht = rechtCNr;
//		HelperClient.setToolTipTextMitRechtToComponent(button, rechtCNr);
	}

	public boolean isEnabled() {
		return enable;
	}

	public void setEnabled(boolean enable) {
		this.enable = enable;
		button.setEnabled(enable);
	}

	public javax.swing.JButton getButton() {
		return button;
	}

	public int getISort() {
		return iSort;
	}

	public void setButton(javax.swing.JButton button) {
		this.button = button;
	}

	public String getRecht() {
		return recht;
	}

	public void setRecht(String rechtCnr) {
		this.recht = rechtCnr;
		HelperClient.setToolTipTextMitRechtToComponent(button, rechtCnr);
	}

	public boolean shouldBeEnabled() {
		return shouldBeEnabledTo(true) ;
	}

	public boolean shouldBeEnabledTo(boolean enable) {
		if (recht == null) {
			getButton().setEnabled(enable);
			return enable ;
		}

		// Keine wirklichen Rechte
		if(RechteFac.RECHT_MODULWEIT_READ.equals(recht) || RechteFac.RECHT_MODULWEIT_UPDATE.equals(recht)) {
			getButton().setEnabled(enable);
			return enable ;
		}

		try {
			boolean b = enable && DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(getRecht()) ;
//			boolean b = DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(getRecht()) ;
			getButton().setEnabled(b);
			return b ;
		} catch (Throwable e) {
			myLogger.warn("hatRecht(" + recht + "), button '"
					+ getButton().getActionCommand() + "' explizit false.", e) ;
		}

		getButton().setEnabled(false);
		return false ;
	}
}
