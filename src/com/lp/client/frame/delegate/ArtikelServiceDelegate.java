
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
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelServiceFac;
import com.lp.server.artikel.service.DateiverweisDto;
import com.lp.server.artikel.service.LumiQuoteArtikelDto;
import com.lp.server.artikel.service.WaffenausfuehrungDto;
import com.lp.server.artikel.service.WaffenkaliberDto;
import com.lp.server.artikel.service.WaffenkategorieDto;
import com.lp.server.artikel.service.WaffentypDto;
import com.lp.server.artikel.service.WaffentypFeinDto;
import com.lp.server.artikel.service.WaffenzusatzDto;
import com.lp.server.auftrag.service.AuftragseriennrnDto;
import com.lp.util.EJBExceptionLP;

public class ArtikelServiceDelegate extends Delegate {
	private Context context;
	private ArtikelServiceFac artikelServiceFac;

	public ArtikelServiceDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			artikelServiceFac = lookupFac(context, ArtikelServiceFac.class);
		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}

	}

	public void zusammenfuehrenArtikel(Integer artikelIId_Quelle, Integer artikelIId_Ziel) throws ExceptionLP {
		try {
			artikelServiceFac.zusammenfuehrenArtikel(artikelIId_Quelle, artikelIId_Ziel, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public List<AuftragseriennrnDto> zusammenfuehrenArtikelPruefeAuftragsnnr(Integer artikelIId_Quelle,
			Integer artikelIId_Ziel) throws ExceptionLP {
		try {
			return artikelServiceFac.zusammenfuehrenArtikelPruefeAuftragsnnr(artikelIId_Quelle, artikelIId_Ziel,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public void zusammenfuehrenHersteller(Integer herstellerIId_Quelle, Integer herstellerIId_Ziel) throws ExceptionLP {
		try {
			artikelServiceFac.zusammenfuehrenHersteller(herstellerIId_Quelle, herstellerIId_Ziel,
					LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void removeDateiverweis(DateiverweisDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.removeDateiverweis(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void updateDateiverweis(DateiverweisDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.updateDateiverweis(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public DateiverweisDto dateiverweisFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelServiceFac.dateiverweisFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Integer createDateiverweis(DateiverweisDto dto) throws ExceptionLP {
		try {
			return artikelServiceFac.createDateiverweis(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public void removeWaffenkaliber(WaffenkaliberDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.removeWaffenkaliber(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void updateWaffenkaliber(WaffenkaliberDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.updateWaffenkaliber(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public WaffenkaliberDto waffenkaliberFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelServiceFac.waffenkaliberFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Integer createWaffenkaliber(WaffenkaliberDto dto) throws ExceptionLP {
		try {
			return artikelServiceFac.createWaffenkaliber(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Integer createWaffentypFein(WaffentypFeinDto dto) throws ExceptionLP {
		try {
			return artikelServiceFac.createWaffentypFein(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Integer createWaffenkategorie(WaffenkategorieDto dto) throws ExceptionLP {
		try {
			return artikelServiceFac.createWaffenkategorie(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Integer createWaffentyp(WaffentypDto dto) throws ExceptionLP {
		try {
			return artikelServiceFac.createWaffentyp(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Integer createWaffenzusatz(WaffenzusatzDto dto) throws ExceptionLP {
		try {
			return artikelServiceFac.createWaffenzusatz(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Integer createWaffenausfuehrung(WaffenausfuehrungDto dto) throws ExceptionLP {
		try {
			return artikelServiceFac.createWaffenausfuehrung(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public void updateWaffenausfuehrung(WaffenausfuehrungDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.updateWaffenausfuehrung(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void updateWaffenkategorie(WaffenkategorieDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.updateWaffenkategorie(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void updateWaffentyp(WaffentypDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.updateWaffentyp(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void updateWaffentypFein(WaffentypFeinDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.updateWaffentypFein(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void updateWaffenzusatz(WaffenzusatzDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.updateWaffenzusatz(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public WaffenausfuehrungDto waffenausfuehrungFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelServiceFac.waffenausfuehrungFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public WaffenkategorieDto waffenkategorieFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelServiceFac.waffenkategorieFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public WaffentypFeinDto waffentypFeinFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelServiceFac.waffentypFeinFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public WaffentypDto waffentypFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelServiceFac.waffentypFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public WaffenzusatzDto waffenzusatzFindByPrimaryKey(Integer iId) throws ExceptionLP {
		try {
			return artikelServiceFac.waffenzusatzFindByPrimaryKey(iId);
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public void removeWaffenausfuehrung(WaffenausfuehrungDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.removeWaffenausfuehrung(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void removeWaffenkategorie(WaffenkategorieDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.removeWaffenkategorie(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void removeWaffentyp(WaffentypDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.removeWaffentyp(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void removeWaffentypFein(WaffentypFeinDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.removeWaffentypFein(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public void removeWaffenzusatz(WaffenzusatzDto dto) throws ExceptionLP {
		try {
			artikelServiceFac.removeWaffenzusatz(dto);
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}

	public Map getAllWaffenkaliber() throws ExceptionLP {
		try {
			return artikelServiceFac.getAllWaffenkaliber(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Map getAllWaffenausfuehrung() throws ExceptionLP {
		try {
			return artikelServiceFac.getAllWaffenausfuehrung(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Map getAllWaffentyp() throws ExceptionLP {
		try {
			return artikelServiceFac.getAllWaffentyp(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Map getAllWaffentypFein() throws ExceptionLP {
		try {
			return artikelServiceFac.getAllWaffentypFein(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Map getAllWaffenkategorie() throws ExceptionLP {
		try {
			return artikelServiceFac.getAllWaffenkategorie(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public Map getAllWaffenzusatz() throws ExceptionLP {
		try {
			return artikelServiceFac.getAllWaffenzusatz(LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	public void pflegeWiederbeschaffungszeiten(Integer artikelgruppeIId, Integer artikelklasseIId, String artiklenrVon,
			String artiklenrBis) throws ExceptionLP {
		try {
			artikelServiceFac.pflegeWiederbeschaffungszeiten(artikelgruppeIId, artikelklasseIId, artiklenrVon,
					artiklenrBis, LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);

		}

	}
	
	public ArrayList<LumiQuoteArtikelDto> getArtikelDatenForLumiquote() throws ExceptionLP {
		try {
			return artikelServiceFac.getArtikelDatenForLumiquote("6wtr", LPMain.getTheClient());
		} catch (Throwable ex) {
			handleThrowable(ex);
			return null;

		}

	}

	

}
