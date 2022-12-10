package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.List;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.SiWertParser;
import com.lp.util.siprefixparser.BigDecimalSI;

public class SuchstringGeneratorSiWert extends SuchstringGenerator {
	
	public SuchstringGeneratorSiWert() {
	}

	@Override
	public List<String> generateSuchstring(IWebabfrageResult result) throws Throwable {
		List<String> list = new ArrayList<String>();
		if (result == null) {
			return list;
		}

		SiWertParser parser = new SiWertParser(getSiOhneEinheitenFromParam(), getSiEinheitenFromParam());
		BigDecimalSI siWert = parser.berechneSiWertAusBezeichnung(result.getEinkaufsangebotpositionDto().getCBez());
		if (siWert != null) {
			list.add(siWert.toSIString());
		}
		return list;
	}

	@Override
	public Integer getVariante() {
		return SearchType.SI_WERTE;
	}

	private Boolean getSiOhneEinheitenFromParam() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_SI_OHNE_EINHEIT,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		return (Boolean) parameter.getCWertAsObject();
	}

	private String getSiEinheitenFromParam() throws Throwable {
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_SI_EINHEITEN,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		return parameter.getCWert();
	}
}
