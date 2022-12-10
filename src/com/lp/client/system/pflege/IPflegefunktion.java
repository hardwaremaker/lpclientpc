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
package com.lp.client.system.pflege;

import javax.swing.JPanel;

public interface IPflegefunktion {
	
	public static final String KATEGORIE_DOKUMENTENABLAGE = "Dokumentenablage";
	public static final String KATEGORIE_RECHNUNG         = "Rechnung";
	public static final String KATEGORIE_LIEFERSCHEIN     = "Lieferschein";
	public static final String KATEGORIE_ALLGEMEIN        = "Allgemein";
	public static final String KATEGORIE_FERTIGUNG        = "Fertigung";
	public static final String KATEGORIE_AUFTRAG          = "Auftrag";
	
	public String getKategorie();
	
	public String getName();

	public String getBeschreibung();
	
	public JPanel getPanel();
	
	public boolean supportsProgressBar();
	
	public boolean isStartable();
	
	public void run();
	
	public void cancel();
	
	public boolean isRunning();
	
	public void addPflegeEventListener(PflegeEventListener listener);
	
	public void removeAllPflegeEventListeners();
	
	public abstract String toString();
	
	public void init();
	
	public void eventYouAreSelected();

}
