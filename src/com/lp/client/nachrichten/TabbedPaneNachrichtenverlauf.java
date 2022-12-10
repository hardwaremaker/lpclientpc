package com.lp.client.nachrichten;


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



import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.pc.LPMain;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author Christian Kollmann
 * @version $Revision: 1.3 $
 */
public class TabbedPaneNachrichtenverlauf
    extends TabbedPane
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private PanelQuery panelQueryVerlauf = null;

  private final static int IDX_PANEL_VERLAUF = 0;

 
  private WrapperMenuBar wrapperMenuBar = null;

  public TabbedPaneNachrichtenverlauf(InternalFrame internalFrameI)
      throws Throwable {
    super(internalFrameI,
          LPMain.getInstance().getTextRespectUISPr("pers.nachrichtenverlauf"));

    jbInit();
    initComponents();
  }


  public InternalFrameNachrichten getInternalFrameNachrichten() {
    return (InternalFrameNachrichten) getInternalFrame();
  }


  private void jbInit()
      throws Throwable {
    //Kollektiv

    //1 tab oben: QP1 PartnerFLR; lazy loading
    insertTab(
        LPMain.getInstance().getTextRespectUISPr("pers.nachrichtenverlauf"),
        null,
        null,
        LPMain.getInstance().getTextRespectUISPr("pers.nachrichtenverlauf"),
        IDX_PANEL_VERLAUF);

    panelQueryVerlauf = new PanelQuery(
        null,
       null,
        QueryParameters.UC_ID_NACHRICHTENVERLAUF,
        null,
        getInternalFrame(),
        LPMain.getInstance().getTextRespectUISPr(
            "pers.nachrichtenverlauf"), true);

   

    setComponentAt(IDX_PANEL_VERLAUF, panelQueryVerlauf);

 
    // Itemevents an MEIN Detailpanel senden kann.
    this.addChangeListener(this);
    getInternalFrame().addItemChangedListener(this);

  }


  public void lPEventItemChanged(ItemChangedEvent e)
      throws Throwable {
/*
    if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_UPDATE) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
    }
    else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {

    }
*/
  }


  public void lPEventObjectChanged(ChangeEvent e)
      throws Throwable {
    super.lPEventObjectChanged(e);
    int selectedIndex = this.getSelectedIndex();

    if (selectedIndex == IDX_PANEL_VERLAUF) {
      panelQueryVerlauf.eventYouAreSelected(false);
      if (panelQueryVerlauf.getSelectedId() == null) {
        getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_VERLAUF, false);
      }
      panelQueryVerlauf.updateButtons();

    }

  }
  protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
   
  }


  public javax.swing.JMenuBar getJMenuBar()
      throws Throwable {
    if (wrapperMenuBar == null) {

      wrapperMenuBar = new WrapperMenuBar(this);

   

    }
    return wrapperMenuBar;

  }

}
