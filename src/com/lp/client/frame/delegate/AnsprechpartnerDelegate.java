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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AngebotstklpositionFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.util.Helper;

public class AnsprechpartnerDelegate extends Delegate {
	private Context context;
	private AnsprechpartnerFac ansprechpartnerFac;

	public AnsprechpartnerDelegate() throws Exception {
		context = new InitialContext();
		ansprechpartnerFac = lookupFac(context, AnsprechpartnerFac.class);
	}

	public Integer createAnsprechpartner(AnsprechpartnerDto ansprechpartnerDto) throws ExceptionLP {

		Integer iId = null;
		try {
			iId = ansprechpartnerFac.createAnsprechpartner(ansprechpartnerDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public Integer getMaxISort(Integer iIdPartnerI) throws ExceptionLP {

		Integer iId = null;
		try {
			iId = ansprechpartnerFac.getMaxISort(iIdPartnerI);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return iId;
	}

	public String getUebersteuerteEmpfaenger(PartnerDto partnerDto, String reportname, boolean bEmail)
			throws ExceptionLP {
		try {
			return ansprechpartnerFac.getUebersteuerteEmpfaenger(partnerDto, reportname, bEmail, LPMain.getTheClient());
		} catch (Throwable ex) {
			return null;
		}
	}

	public void removeAnsprechpartner(AnsprechpartnerDto ansprechpartnerDto) throws ExceptionLP {

		try {
			ansprechpartnerFac.removeAnsprechpartner(ansprechpartnerDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAnsprechpartner(AnsprechpartnerDto ansprechpartnerDto) throws ExceptionLP {

		try {
			ansprechpartnerFac.updateAnsprechpartner(ansprechpartnerDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void pruefeObAnsprechpartnerVersteckt(Integer... ansprechpartnerIIds) throws ExceptionLP {
		// SP6638

		if (ansprechpartnerIIds != null) {

			Set<Integer> s = new LinkedHashSet<Integer>();

			for (int i = 0; i < ansprechpartnerIIds.length; i++) {
				if (ansprechpartnerIIds[i] != null) {
					s.add(ansprechpartnerIIds[i]);
				}
			}

			try {

				Iterator<Integer> it = s.iterator();

				while (it.hasNext()) {
					Integer ansprechpartnerIId = it.next();
					AnsprechpartnerDto anspDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
							.ansprechpartnerFindByPrimaryKey(ansprechpartnerIId);

					if (Helper.short2boolean(anspDto.getBVersteckt())) {
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr("lp.hinweis.ansprechpartner.versteckt"),
								LPMain.getMessageTextRespectUISPr("lp.hinweis.ansprechpartner.versteckt.detail",
										anspDto.getPartnerDto().formatFixTitelName1Name2()));
					}
				}

			} catch (Throwable ex) {
				handleThrowable(ex);
			}
		}

	}

	public void vertauscheAnsprechpartner(Integer ansprechpartner1, Integer ansprechpartner2) throws ExceptionLP {

		try {
			ansprechpartnerFac.vertauscheAnsprechpartner(ansprechpartner1, ansprechpartner2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void vertauscheAnsprechpartner(Integer iSort1, Integer iSort2, Integer ansprechpartner1,
			Integer ansprechpartner2) throws ExceptionLP {

		try {
			ansprechpartnerFac.vertauscheAnsprechpartner(iSort1, iSort2, ansprechpartner1, ansprechpartner2);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void renumberISortAnsprechpartner(Integer partnerId) throws ExceptionLP {

		try {
			ansprechpartnerFac.renumberISortAnsprechpartner(partnerId);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public AnsprechpartnerDto ansprechpartnerFindByPrimaryKey(Integer iId) throws ExceptionLP {
		AnsprechpartnerDto ansprechpartnerDto = null;
		try {
			ansprechpartnerDto = ansprechpartnerFac.ansprechpartnerFindByPrimaryKey(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ansprechpartnerDto;
	}

	public AnsprechpartnerDto[] ansprechpartnerFindByAnsprechpartnerIId(Integer iId) throws ExceptionLP {
		AnsprechpartnerDto[] ansprechpartnerDtos = null;
		try {
			ansprechpartnerDtos = ansprechpartnerFac.ansprechpartnerFindByAnsprechpartnerIId(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ansprechpartnerDtos;
	}

	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIIdOhneExc(Integer iId) throws ExceptionLP {
		AnsprechpartnerDto[] ansprechpartnerDtos = null;
		try {
			ansprechpartnerDtos = ansprechpartnerFac.ansprechpartnerFindByPartnerIIdOhneExc(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ansprechpartnerDtos;
	}

	public Integer createAnsprechpartnerfunktion(AnsprechpartnerfunktionDto ansprechpartnerfunktionDto)
			throws ExceptionLP {

		Integer a = null;
		try {
			a = ansprechpartnerFac.createAnsprechpartnerfunktion(ansprechpartnerfunktionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return a;
	}

	public void removeAnsprechpartnerfunktion(Integer iId) throws ExceptionLP {
		try {
			ansprechpartnerFac.removeAnsprechpartnerfunktion(iId, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void removeAnsprechpartnerfunktion(AnsprechpartnerfunktionDto ansprechpartnerfunktionDto)
			throws ExceptionLP {

		try {
			ansprechpartnerFac.removeAnsprechpartnerfunktion(ansprechpartnerfunktionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void updateAnsprechpartnerfunktion(AnsprechpartnerfunktionDto ansprechpartnerfunktionDto)
			throws ExceptionLP {

		try {
			ansprechpartnerFac.updateAnsprechpartnerfunktion(ansprechpartnerfunktionDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public AnsprechpartnerfunktionDto ansprechpartnerfunktionFindByPrimaryKey(Integer iId) throws ExceptionLP {

		AnsprechpartnerfunktionDto ansprechpartnerfunktionDto = null;
		try {
			ansprechpartnerfunktionDto = ansprechpartnerFac.ansprechpartnerfunktionFindByPrimaryKey(iId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ansprechpartnerfunktionDto;
	}

	public AnsprechpartnerfunktionDto[] ansprechpartnerfunktionFindAll() throws ExceptionLP {

		AnsprechpartnerfunktionDto[] ansprechpartnerfunktionDto = null;
		try {
			ansprechpartnerfunktionDto = ansprechpartnerFac.ansprechpartnerfunktionFindAll(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ansprechpartnerfunktionDto;
	}

	public ArrayList<?> getAllAnsprechpartner(Integer iIdPartneransprechpartnerI) throws ExceptionLP {

		ArrayList<?> anprechMap = null;
		try {
			anprechMap = ansprechpartnerFac.getAllAnsprechpartner(iIdPartneransprechpartnerI, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return anprechMap;
	}

	public AnsprechpartnerDto ansprechpartnerFindErstenEinesPartnersOhneExc(Integer partnerIId) throws ExceptionLP {
		AnsprechpartnerDto ansprechpartnerDto = null;
		try {
			ansprechpartnerDto = ansprechpartnerFac.ansprechpartnerFindErstenEinesPartnersOhneExc(partnerIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
		return ansprechpartnerDto;
	}

	public void zusammenfuehrenAnsprechpartner(AnsprechpartnerDto ansprechpartnerZielDto,
			int ansprechpartnerQuellDtoIid, PartnerDto partnerDto) throws ExceptionLP {

		try {
			ansprechpartnerFac.zusammenfuehrenAnsprechpartner(ansprechpartnerZielDto, ansprechpartnerQuellDtoIid,
					partnerDto, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public AnsprechpartnerDto[] ansprechpartnerFindByEmail(String email) throws ExceptionLP {
		try {
			return ansprechpartnerFac.ansprechpartnerFindByEmail(email, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;
		}
	}

	public void pruefeAnsprechpartnerZugehoerigkeit(Integer partnerIId, Integer ansprechpartnerIId) throws ExceptionLP {
		try {
			ansprechpartnerFac.pruefeAnsprechpartnerZugehoerigkeit(partnerIId, ansprechpartnerIId,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}
	}

}
