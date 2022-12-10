
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
package com.lp.client.personal;


import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.lp.client.frame.*;
import com.lp.client.frame.component.*;
import com.lp.client.pc.*;
import com.lp.server.personal.service.*;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.util.Helper;
import com.lp.client.frame.dialog.*;


public class PanelPassivereise
    extends PanelBasis
{

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//von hier ...
  private GridBagLayout gridBagLayoutAll = null;
  private JPanel jpaWorkingOn = new JPanel();
  private JPanel jpaButtonAction = null;
  private Border border = null;
  private GridBagLayout gridBagLayoutWorkingPanel = null;
  private InternalFramePersonal internalFramePersonal = null;

  private PassivereiseDto passivereiseDto = null;

  private WrapperLabel wlaTagesart = new WrapperLabel();
  private WrapperComboBox wcoTagesart = new WrapperComboBox();
  private WrapperLabel wlaBis = new WrapperLabel();
  private WrapperTimeField wtfBis = new WrapperTimeField();
  private WrapperLabel wlaAb = new WrapperLabel();
  private WrapperTimeField wtfAb = new WrapperTimeField();
  private WrapperCheckBox wcbRestdestages = new WrapperCheckBox();
 
  private WrapperLabel wla0000 = new WrapperLabel();
  private WrapperLabel wla2400 = new WrapperLabel();


  public PanelPassivereise(InternalFrame internalFrame, String add2TitleI,
                             Object pk)
      throws Throwable {
    super(internalFrame, add2TitleI, pk);
    internalFramePersonal = (InternalFramePersonal) internalFrame;
    jbInit();
    setDefaults();
    initComponents();
    enableAllComponents(this, false);
  }


  protected void setDefaults()
      throws Throwable {
    wcoTagesart.setMap(DelegateFactory.getInstance().getZeiterfassungDelegate().
                       getAllSprTagesarten());
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wcoTagesart;
  }


  public void eventActionNew(EventObject eventObject, boolean bLockMeI,
                             boolean bNeedNoNewI)
      throws Throwable {
    super.eventActionNew(eventObject, true, false);
    passivereiseDto = new PassivereiseDto();
    leereAlleFelder(this);

  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
  }


  protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
                                   boolean bNeedNoDeleteI)
      throws Throwable {
    DelegateFactory.getInstance().getPersonalDelegate().removePassivereise(
        passivereiseDto);
    this.setKeyWhenDetailPanel(null);
    super.eventActionDelete(e, false, false);
  }


  protected void components2Dto()
      throws ExceptionLP {
    passivereiseDto.setKollektivIId(internalFramePersonal.getKollektivDto().getIId());
    passivereiseDto.setTagesartIId( (Integer) wcoTagesart.getKeyOfSelectedItem());
    passivereiseDto.setUBis(wtfBis.getTime());
    passivereiseDto.setUAb(wtfAb.getTime());
    passivereiseDto.setBRestdestages(wcbRestdestages.getShort());
   
  }


  protected void dto2Components()
      throws ExceptionLP, Throwable {
    wcbRestdestages.setShort(passivereiseDto.getBRestdestages());
 
    wtfBis.setTime(passivereiseDto.getUBis());
    wtfAb.setTime(passivereiseDto.getUAb());
    wcoTagesart.setKeyOfSelectedItem(passivereiseDto.getTagesartIId());

    if (Helper.short2Boolean(passivereiseDto.getBRestdestages()) == false) {
     wtfBis.setTime(passivereiseDto.getUBis());
     wtfBis.setActivatable(true);
   }
   else {
     wtfBis.setTime(null);
     wtfBis.setActivatable(false);
   }



  }


  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
      throws Throwable {
    if (allMandatoryFieldsSetDlg()) {
      if (wtfBis.getTime().before(wtfAb.getTime()) || Helper.short2boolean(wcbRestdestages.getShort())==true) {
        components2Dto();
        if (passivereiseDto.getIId() == null) {
          passivereiseDto.setIId(DelegateFactory.getInstance().
                                       getPersonalDelegate().
                                       createPassivereise(passivereiseDto));
          setKeyWhenDetailPanel(passivereiseDto.getIId());
        }
        else {
          DelegateFactory.getInstance().getPersonalDelegate().
              updatePassivereise(
                  passivereiseDto);
        }

        super.eventActionSave(e, true);

        if (getInternalFrame().getKeyWasForLockMe() == null) {
          getInternalFrame().setKeyWasForLockMe(internalFramePersonal.getZeitmodellDto().
                                                getIId() + "");
        }
        eventYouAreSelected(false);
      }
      else {
        DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr(
            "lp.error"),
                                             LPMain.getInstance().getTextRespectUISPr(
                                                 "lp.error.beginnvorende"));
      }
    }

  }


  protected void eventItemchanged(EventObject eI)
      throws Throwable {
    ItemChangedEvent e = (ItemChangedEvent) eI;
  }


  private void jbInit()
      throws Throwable {
    //von hier ...
    border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    setBorder(border);
    //das Aussenpanel hat immer das Gridbaglayout.
    gridBagLayoutAll = new GridBagLayout();
    this.setLayout(gridBagLayoutAll);

    //Actionpanel von Oberklasse holen und anhaengen.
    jpaButtonAction = getToolsPanel();
    this.setActionMap(null);

    getInternalFrame().addItemChangedListener(this);
    wlaTagesart.setText(LPMain.getInstance().getTextRespectUISPr("lp.tagesart"));
    wcoTagesart.setMandatoryField(true);
    wlaBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));
    wlaAb.setText(LPMain.getInstance().getTextRespectUISPr("lp.ab"));
    wtfAb.setMandatoryField(true);
    wtfBis.setMandatoryField(true);

    wla0000.setText("00:00 "+LPMain.getInstance().getTextRespectUISPr("lp.uhr") +" -");
    wla2400.setText(" - 24:00 "+LPMain.getInstance().getTextRespectUISPr("lp.uhr"));

    Dimension labelDimension = HelperClient.getSizeFactoredDimension(100);
    HelperClient.setMinimumAndPreferredSize(wcoTagesart, labelDimension);
    HelperClient.setMinimumAndPreferredSize(wla0000, labelDimension);
    HelperClient.setMinimumAndPreferredSize(wla2400, labelDimension);
    HelperClient.setMinimumAndPreferredSize(wlaBis, labelDimension);
    HelperClient.setMinimumAndPreferredSize(wlaAb, labelDimension);
    HelperClient.setMinimumAndPreferredSize(wcbRestdestages, labelDimension);
  
    
    Dimension timeFieldDimension = HelperClient.getSizeFactoredDimension(75);
    HelperClient.setMinimumAndPreferredSize(wtfBis, timeFieldDimension);
    HelperClient.setMinimumAndPreferredSize(wtfAb, timeFieldDimension);
   
    
    wcbRestdestages.setText(LPMain.getInstance().getTextRespectUISPr(
        "pers.zutritt.restdestages"));
  
    wcbRestdestages.setMnemonic('R');
    wcbRestdestages.addActionListener(new
    		PanelPassivereise_wcbRestdestages_actionAdapter(this));

    this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(0, 0, 0, 0), 0, 0));

    //jetzt meine felder
    jpaWorkingOn = new JPanel();
    gridBagLayoutWorkingPanel = new GridBagLayout();
    jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                                  , GridBagConstraints.SOUTHEAST,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.add(wlaTagesart, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        0, 0));
    jpaWorkingOn.add(wcoTagesart, new GridBagConstraints(2, 0, 5, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0,
        0));




        jpaWorkingOn.add(wla0000, new GridBagConstraints(0, 1, 1, 1, 0, 0.0
            , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
          0, 0));
    jpaWorkingOn.add(wlaBis, new GridBagConstraints(1, 1, 1, 1, 0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
        0, 0));
    jpaWorkingOn.add(wtfBis, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0),
        0, 0));
    jpaWorkingOn.add(wlaAb, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
       0, 0));
    jpaWorkingOn.add(wtfAb, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 0),
        0, 0));

    jpaWorkingOn.add(wcbRestdestages, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
       0, 0));

    jpaWorkingOn.add(wla2400, new GridBagConstraints(6, 1, 1, 1, 0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
          0, 0));
  


    String[] aWhichButtonIUse = {
        ACTION_UPDATE,
        ACTION_SAVE,
        ACTION_DELETE,
        ACTION_DISCARD,
    };

    enableToolsPanelButtons(aWhichButtonIUse);

  }


  public void wcbRestdestages_actionPerformed(ActionEvent e) {
    if (wcbRestdestages.isSelected()) {
      wtfBis.setEnabled(false);
    }
    else {
      wtfBis.setEnabled(true);

    }

  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_KOLLEKTIV;
  }


  public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
      throws Throwable {

    super.eventYouAreSelected(false);
    Object key = getKeyWhenDetailPanel();
    if (key == null
        || (key.equals(LPMain.getLockMeForNew()))) {
      leereAlleFelder(this);
      clearStatusbar();
      if (wcbRestdestages.isSelected()) {
        wtfBis.setEnabled(false);
      }
      else {
        if (key != null) {
          wtfBis.setEnabled(true);
        }
      }
    }
    else {
      passivereiseDto = DelegateFactory.getInstance().getPersonalDelegate().
    		  passivereiseFindByPrimaryKey( (Integer) key);
      dto2Components();


    }

  }
}


class PanelPassivereise_wcbRestdestages_actionAdapter
    implements ActionListener
{
  private PanelPassivereise adaptee;
  PanelPassivereise_wcbRestdestages_actionAdapter(PanelPassivereise adaptee) {
    this.adaptee = adaptee;
  }


  public void actionPerformed(ActionEvent e) {
    adaptee.wcbRestdestages_actionPerformed(e);
  }
}
