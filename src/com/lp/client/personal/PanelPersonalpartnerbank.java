/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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


import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.partner.PanelPartnerBank;
import com.lp.server.partner.service.PartnerDto;


/**
 * <p> Diese Klasse kuemmert sich um Partner und deren Banken.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Josef Ornetsmueller; 24.03.05</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version $Revision: 1.3 $
 * Date $Date: 2008/08/11 09:55:50 $
 */


public class PanelPersonalpartnerbank
    extends PanelPartnerBank
{


  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public PanelPersonalpartnerbank(InternalFrame internalFrame,
                          String add2TitleI,
                          Object keyI)
      throws Throwable {
    super(internalFrame, add2TitleI, keyI);
  }


  protected PartnerDto getPartnerDto(){
    return ((InternalFramePersonal)getInternalFrame()).getPersonalDto().getPartnerDto();
  }


  protected String getLockMeWer()
      throws Exception {
    return HelperClient.LOCKME_PERSONAL;
  }

  protected void setDefaults()
      throws Throwable {
  }




}