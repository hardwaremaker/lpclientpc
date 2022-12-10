package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.List;

public class SuchstringGeneratorHerstellernummer extends SuchstringGenerator {

	public SuchstringGeneratorHerstellernummer() {
	}

	@Override
	public List<String> generateSuchstring(IWebabfrageResult result) throws Throwable {
		List<String> list = new ArrayList<String>();
		if (result == null) {
			return list;
		}
		
		if (result.getEinkaufsangebotpositionDto().getCArtikelnrhersteller() != null) {
			list.add(result.getEinkaufsangebotpositionDto().getCArtikelnrhersteller());
		}
		
		return list;
	}

	@Override
	public Integer getVariante() {
		return SearchType.HERSTELLERARTIKELNUMMER;
	}
}
