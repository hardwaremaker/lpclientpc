
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
package com.lp.client.frame.delegate;



import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.system.service.AutoMahnungsversandDto;
import com.lp.server.system.service.AutoMahnungsversandFac;
import com.lp.server.system.service.AutoMonatsabrechnungversandDto;
import com.lp.server.system.service.AutoMonatsabrechnungversandFac;


public class AutoMonatsabrechnungversandDelegate
    extends Delegate
{

  private Context context;
  private AutoMonatsabrechnungversandFac autoMonatsabrechnungsversandFac;

  public AutoMonatsabrechnungversandDelegate()
      throws Throwable {
	  context = new InitialContext();
	  autoMonatsabrechnungsversandFac = lookupFac(context, AutoMonatsabrechnungversandFac.class);
  }



  public AutoMonatsabrechnungversandDto autoMonatsabrechnungversandFindByMandantCNr(String MandantCNr)
      throws RemoteException, FinderException, EJBException {
    return autoMonatsabrechnungsversandFac.autoMonatsabrechnungversandFindByMandantCNr(MandantCNr);
  }
  public AutoMonatsabrechnungversandDto autoMonatsabrechnungversandAbteilungenfindByMandantCNr(String MandantCNr)
	      throws RemoteException, FinderException, EJBException {
	    return autoMonatsabrechnungsversandFac.autoMonatsabrechnungversandAbteilungenfindByMandantCNr(MandantCNr);
	  }


  public AutoMonatsabrechnungversandDto autoMonatsabrechnungversandFindByPrimaryKey(Integer iId)
      throws RemoteException, FinderException, EJBException {
    return autoMonatsabrechnungsversandFac.autoMonatsabrechnungversandFindByPrimaryKey(iId);
  }


  public void createAutoMonatsabrechnungversand(AutoMonatsabrechnungversandDto dto)
      throws RemoteException, EJBException {
    autoMonatsabrechnungsversandFac.createAutoMonatsabrechnungversand(dto);
  }
  public void createAutoMonatsabrechnungversandAbteilungen(AutoMonatsabrechnungversandDto dto)
	      throws RemoteException, EJBException {
	    autoMonatsabrechnungsversandFac.createAutoMonatsabrechnungversandAbteilungen(dto);
	  }

  public void removeAutoMonatsabrechnungversand(AutoMonatsabrechnungversandDto dto)
      throws RemoteException, EJBException {
    autoMonatsabrechnungsversandFac.removeAutoMonatsabrechnungversand(dto);
  }


  public void removeAutoMonatsabrechnungversand(Integer iId)
      throws RemoteException, EJBException {
    autoMonatsabrechnungsversandFac.removeAutoMonatsabrechnungversand(iId);
  }


  public void updateAutoMonatsabrechnungversand(AutoMonatsabrechnungversandDto dto)
      throws RemoteException, EJBException {
    autoMonatsabrechnungsversandFac.updateAutoMonatsabrechnungversand(dto);
  }public void updateAutoMonatsabrechnungversandAbteilungen(AutoMonatsabrechnungversandDto dto)
	      throws RemoteException, EJBException {
	    autoMonatsabrechnungsversandFac.updateAutoMonatsabrechnungversandAbteilungen(dto);
	  }


  public void updateAutoMonatsabrechnungversands(AutoMonatsabrechnungversandDto[] dtos)
      throws RemoteException, EJBException {
    autoMonatsabrechnungsversandFac.updateAutoMonatsabrechnungversands(dtos);
  }
}
