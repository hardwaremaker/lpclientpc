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

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvCreatingCachingProvider;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheJudgeFac;

public class TheJudgeDelegate extends Delegate {
	private Context context;
	private TheJudgeFac theJudgeFac;
	// private HvCachingProvider<String, Boolean> cache;
	private TheJudgeCache cache;

	public TheJudgeDelegate() throws Exception {
		context = new InitialContext();
		theJudgeFac = lookupFac(context, TheJudgeFac.class);

		cache = new TheJudgeCache();
	}

	private class TheJudgeCache extends
			HvCreatingCachingProvider<String, Boolean> {

		@Override
		protected Boolean provideValue(String key, String transformedKey)
				throws ExceptionLP {
			try {
				return theJudgeFac.hatRecht(key, LPMain.getTheClient());
			} catch (Throwable t) {
				handleThrowable(t);
				return Boolean.FALSE;
			}
		}

		@Override
		protected String transformKey(String key) throws ExceptionLP {
			try {
				return LPMain.getTheClient().getIDUser() + "." + key;
			} catch (Throwable t) {
				handleThrowable(t);
				return null;
			}
		}

		public boolean hatRecht(String rechtCnr) throws ExceptionLP {
			return getValueOfKey(rechtCnr);
		}

		public boolean hatRechtOder(String rechtCnr, String oderRechtCnr)
				throws ExceptionLP {
			// TODO: Das ist suboptimal, sollte auf Bean hatRechtOder() gehen
			return getValueOfKey(rechtCnr) || getValueOfKey(oderRechtCnr);
		}
	}

	public void removeLockedObject(LockMeDto lockMeDto) throws ExceptionLP {

		try {
			if (lockMeDto.getPersonalIIdLocker() == null) {
				// alle bis auf SCRUD-Lockme
				lockMeDto.setPersonalIIdLocker(LPMain.getTheClient()
						.getIDPersonal());
			}

			theJudgeFac.removeLockedObject(lockMeDto);
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public LockMeDto lockmeFindByPrimaryKey(LockMeDto lockMeDtoI)
			throws ExceptionLP {

		LockMeDto lockMeDto = null;
		try {
			lockMeDto = theJudgeFac.theJudgeFindByPrimaryKey(
					lockMeDtoI.getCWer(), lockMeDtoI.getCWas(),
					lockMeDtoI.getPersonalIIdLocker(), lockMeDtoI.getCUsernr());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return lockMeDto;
	}

	public LockMeDto[] theJudgeFindByWerWas(LockMeDto lockMeDtoI)
			throws ExceptionLP {

		LockMeDto[] lockMeDto = null;
		try {
			lockMeDto = theJudgeFac.findByWerWas(lockMeDtoI.getCWer(),
					lockMeDtoI.getCWas());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return lockMeDto;
	}

	public LockMeDto[] findMyLocks() throws ExceptionLP {
		LockMeDto[] lockMeDto = null;
		try {
			lockMeDto = theJudgeFac.findMyLocks(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return lockMeDto;
	}

	public void removeMyLocks(TheClientDto theClientDto) throws ExceptionLP {
		try {
			theJudgeFac.removeMyLocks(theClientDto);
		} catch (Throwable ex) {
			handleThrowable(ex);
		}
	}

	public void addLockedObject(LockMeDto lockMeDto) throws ExceptionLP {
		try {
			lockMeDto.setPersonalIIdLocker(LPMain.getTheClient()
					.getIDPersonal());
			theJudgeFac.addLockedObject(lockMeDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	public boolean isLocked(LockMeDto lockMeDto) throws ExceptionLP {
		boolean b = false;
		try {
			b = theJudgeFac.isLocked(lockMeDto, LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return b;
	}

	public boolean hatRecht(String rechtCNr) throws ExceptionLP {
		return cache.hatRecht(rechtCNr);
	}

	/**
	 * Hat der Anwender entweder recht1 oder recht2?
	 * 
	 * @param recht1CNr
	 * @param oderRecht2Cnr
	 * @return true wenn der Anwender mindestens eines der angegebenen Rechte
	 *         hat
	 * @throws ExceptionLP
	 */
	public boolean hatRechtOder(String recht1CNr, String oderRecht2Cnr)
			throws ExceptionLP {
		return cache.hatRechtOder(recht1CNr, oderRecht2Cnr);
	}

	// public boolean hatRecht(String rechtCNr) throws ExceptionLP {
	//
	// boolean b = false;
	// try {
	// return theJudgeFac.hatRecht(rechtCNr, LPMain.getTheClient());
	// } catch (Throwable t) {
	// handleThrowable(t);
	// }
	// return b;
	// }

	public Integer getSystemrolleIId() throws ExceptionLP {

		Integer b = null;
		try {
			return theJudgeFac.getSystemrolleIId(LPMain.getTheClient());
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return b;
	}

	public TheJudgeCache getTheJudgeCache() {
		return cache;
	}

}
