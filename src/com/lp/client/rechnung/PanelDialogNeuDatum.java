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
package com.lp.client.rechnung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.inserat.InternalFrameInserat;


/**
 * <p><I>Dialog zur Auswahl des Neu-Datums fuer eine Rechnung</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>09. 09. 2004</I></p>
 * @todo Gueltigen Datumsbereich ueberpruefen  PJ 4930
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.3 $
 */
public class PanelDialogNeuDatum
    extends PanelDialogKriterien {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private WrapperDateField wdfDatum = new WrapperDateField();
  private WrapperLabel wlaNeuDatum = new WrapperLabel();
  private java.sql.Date neuDatum;
  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  public PanelDialogNeuDatum(InternalFrame p0,String title,  java.sql.Date neuDatum) throws Throwable {
    super(p0, title);
    this.neuDatum=neuDatum;
    jbInit();
    initComponents();
  }


  /**
   * Dialog initialisieren
   *
   * @throws Throwable
   */
  private void jbInit() throws Throwable {
    wlaNeuDatum.setText("Neu-Datum");
    jpaWorkingOn.setLayout(gridBagLayout1);
    this.add(jpaWorkingOn,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0, 0));
    jpaWorkingOn.add(wdfDatum,    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaNeuDatum,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    // Default auf heute setzen
    if(neuDatum==null) {
      wdfDatum.setDate(new java.sql.Date(System.currentTimeMillis()));
    }
    else {
      wdfDatum.setDate(neuDatum);
    }
  }


  /**
   * Datum auslesen.
   * @return Date
   */
  public java.sql.Date getDate() {
    return wdfDatum.getDate();
  }


  /**
   *
   * @param e ActionEvent
   * @throws Throwable
   * @todo vom internalframe das dautm abholen  PJ 4940
   */
  protected void eventActionSpecial(ActionEvent e) throws Throwable {
    if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
    	
    	if(getInternalFrame()instanceof InternalFrameRechnung){
    		((InternalFrameRechnung)getInternalFrame()).neuDatum=getDate();
    	}else if(getInternalFrame()instanceof InternalFrameInserat){
    		((InternalFrameInserat)getInternalFrame()).getTabbedPaneInserat().neuDatum=getDate();
    	}
    	
    }
    super.eventActionSpecial(e);
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wdfDatum;
  }
}
