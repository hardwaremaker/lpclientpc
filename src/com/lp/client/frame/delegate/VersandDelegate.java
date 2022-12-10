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

/**
 * <p><I>Diese Klasse kuemmert sich um den Versand</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>09.03.05</I></p>
 *
 * <p> </p>
 *
 * @author  Martin Bluehweis
 * @version $Revision: 1.8 $
 */
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.system.jcr.service.FehlerVersandauftraegeDto;
import com.lp.server.system.mail.service.MailTestMessage;
import com.lp.server.system.mail.service.MailTestMessageResult;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class VersandDelegate extends Delegate {
	private VersandFac versandFac;
	private Context context;

	public VersandDelegate() throws Exception {
		context = new InitialContext();
		versandFac = lookupFac(context, VersandFac.class);

	}

	public VersandauftragDto updateVersandauftrag(
			VersandauftragDto versandauftragDto, boolean bDokumenteanhangen)
			throws ExceptionLP {
		try {
			if (versandauftragDto.getIId() == null) {
				return versandFac
						.createVersandauftrag(versandauftragDto,
								bDokumenteanhangen, LPMain.getTheClient());
			} else {
				return versandFac.updateVersandauftrag(versandauftragDto,
						LPMain.getTheClient());
			}
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public VersandanhangDto createVersandanhang(
			VersandanhangDto versandanhangDto) throws ExceptionLP {
		int tries = 1;
		do {
			try {
				return versandFac.createVersandanhang(versandanhangDto, 
						LPMain.getTheClient());
			} catch (Throwable ex) {
				myLogger.error("Throwable", ex);
//				if(!(ex.getCause() instanceof javax.transaction.RollbackException)) {
//					handleThrowable(ex);
//					return null;										
//				}
			}			
		} while(--tries >= 0);
		return null;
	}
	
	public VersandauftragDto createVersandauftrag(
			VersandauftragDto versandAuftragDto, 
			List<VersandanhangDto> versandAnhaenge,
			boolean dokumenteAnhaengen) throws ExceptionLP {
		try {
			return versandFac.createVersandauftrag(versandAuftragDto,
					versandAnhaenge, dokumenteAnhaengen, LPMain.getTheClient());
		} catch(Throwable t) {
			handleThrowable(t);
			return null;
		}
	}
	
	public VersandanhangDto createVersandanhang0(
			VersandanhangDto versandanhangDto) throws ExceptionLP {
		try {		
			return versandFac.createVersandanhang(versandanhangDto, 
					LPMain.getTheClient());
		} catch (Throwable ex) {
			myLogger.error("Throwable", ex);
			handleThrowable(ex);
			return null;
		}			
	}


	public void createVersandanhaenge(List<VersandanhangDto> alAnhaenge)
			throws ExceptionLP {
		try {
			versandFac.createVersandanhaenge(alAnhaenge, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public VersandanhangDto[] VersandanhangFindByVersandauftragIID(
			Integer versandauftragIID) throws RemoteException, EJBExceptionLP {
		return versandFac
				.VersandanhangFindByVersandauftragIID(versandauftragIID);
	}

	public VersandauftragDto versandauftragFindByPrimaryKey(Integer iId)
			throws ExceptionLP {
		try {
			return versandFac.versandauftragFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public Integer versandauftragFindFehlgeschlagenen() throws ExceptionLP {
		try {
			return versandFac.versandauftragFindFehlgeschlagenen(
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void sendeVersandauftragErneut(Integer versandauftragIId,
			Timestamp tWunschSendeZeitpunkt) throws ExceptionLP {
		try {
			versandFac.sendeVersandauftragErneut(versandauftragIId,
					tWunschSendeZeitpunkt, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void storniereVersandauftrag(Integer versandauftragIId)
			throws ExceptionLP {
		try {
			versandFac.storniereVersandauftrag(versandauftragIId, 
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeVersandauftrag(Integer versandauftragIId)
			throws ExceptionLP {
		try {
			versandFac.removeVersandauftrag(versandauftragIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String getDefaultTextForBelegEmail(MailtextDto mailtextDto)
			throws ExceptionLP {
		try {
			return versandFac.getDefaultTextForBelegEmail(mailtextDto, LPMain
					.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getDefaultTextForBelegHtmlEmail(MailtextDto mailtextDto)
			throws ExceptionLP {
		try {
			return versandFac.getDefaultTextForBelegHtmlEmail(mailtextDto,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getDefaultDateinameForBelegEmail(String beleggartCNr,
			Integer belegIId) throws ExceptionLP {
		try {
			return versandFac.getDefaultDateinameForBelegEmail(beleggartCNr,
					belegIId, LPMain.getTheClient().getLocUi(),
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	public String getDefaultDateinameForBelegEmail(String beleggartCNr,
			Integer belegIId, String locale) throws ExceptionLP {
		try {
			return versandFac.getDefaultDateinameForBelegEmail(beleggartCNr,
					belegIId,Helper.string2Locale(locale),
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getDefaultBetreffForBelegEmail(MailtextDto mailtextDto,
			String beleggartCNr, Integer belegIId) throws ExceptionLP {
		try {
			return versandFac.getDefaultBetreffForBelegEmail(mailtextDto,
					beleggartCNr, belegIId, LPMain.getTheClient().getLocUi(), 
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public String getUebersteuertenAbsender(MailtextDto mailtextDto) throws ExceptionLP {
		try {
			return versandFac.getUebersteuertenAbsenderFuerBelegEmail(
					mailtextDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public String getDefaultBetreffForBelegEmail(MailtextDto mailtextDto,
			String beleggartCNr, Integer belegIId, String locale) throws ExceptionLP {
		try {
			return versandFac.getDefaultBetreffForBelegEmail(mailtextDto,
					beleggartCNr, belegIId,Helper.string2Locale(locale), LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void sendeVersandauftragSofort(Integer versandauftragIId)
			throws ExceptionLP {
		try {
			versandFac.sendeVersandauftragSofort(versandauftragIId, 
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public String getVersandstatus(String belegartCNr, Integer i_belegIId)
			throws ExceptionLP {
		try {
			return versandFac.getVersandstatus(belegartCNr, i_belegIId, 
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public String getFormattedVersandstatus(String belegartCNr,
			Integer i_belegIId) throws ExceptionLP {
		try {
			return versandFac.getFormattedVersandstatus(belegartCNr,
					i_belegIId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return null;
	}

	public String getDefaultAnhangForBelegEmail(MailtextDto mailtextDto,
			String beleggartCNr, Integer belegIId) throws ExceptionLP {
		try {
			return versandFac.getDefaultAnhangForBelegEmail(mailtextDto,
					beleggartCNr, belegIId, LPMain.getTheClient()
							.getLocUi(), LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getUebersteuertenDateinamenForBeleg(MailtextDto mailtextDto,
			String beleggartCNr, Integer belegIId) throws ExceptionLP {
		try {
			return versandFac.getUebersteuertenDateinamenForBeleg(mailtextDto,
					beleggartCNr, belegIId, LPMain.getTheClient()
							.getLocUi(), LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public String getDefaultAnhangForBelegEmail(MailtextDto mailtextDto, 
			String beleggartCNr, Integer belegIId, String locale) throws ExceptionLP {
		try {
			return versandFac.getDefaultAnhangForBelegEmail(mailtextDto,
					beleggartCNr, belegIId, Helper.string2Locale(locale), 
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}
	
	public MailTestMessageResult testMailConfiguration(MailTestMessage testMessage) throws ExceptionLP {
		try {
			return versandFac.testMailConfiguration(testMessage, LPMain.getTheClient());
		} catch (Throwable e) {
			handleThrowable(e);
			return null;
		}
	}
	
	public FehlerVersandauftraegeDto getOffeneUndFehlgeschlageneAuftraege() throws ExceptionLP {
		try {
			return versandFac.getOffeneUndFehlgeschlageneAuftraege(LPMain.getTheClient());
		} catch (Throwable e) {
			handleThrowable(e);
			return null;
		}
	}
	
}
