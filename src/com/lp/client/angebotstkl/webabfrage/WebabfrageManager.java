package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebabfrageManager {

	private List<SuchstringGenerator> suchstringGeneratoren;
	private Map<Integer, Integer> idSearchTypeMap;

	public WebabfrageManager() {
		idSearchTypeMap = new HashMap<Integer, Integer>();
		initSuchstringGeneratoren();
	}

	private void initSuchstringGeneratoren() {
		suchstringGeneratoren = new ArrayList<SuchstringGenerator>();
		suchstringGeneratoren.add(new SuchstringGeneratorUserstring());
		suchstringGeneratoren.add(new SuchstringGeneratorHerstellernummer());
		suchstringGeneratoren.add(new SuchstringGeneratorLieferantenartikelnummer());
		suchstringGeneratoren.add(new SuchstringGeneratorSiWert());
		suchstringGeneratoren.add(new SuchstringGeneratorBezeichnung());
	}
	
	public Map<Integer, List<String>> getSuchstringsMap(List<IWebabfrageResult> positionen) throws Throwable {
		Map<Integer, List<String>> idSuchstringsMap = new HashMap<Integer, List<String>>();
		for (IWebabfrageResult position : positionen) {

			position.clearParts();
			SuchstringGenerator generator = getSuchstringGenerator(position);
			if (generator == null) continue;
			position.setGeneratedSearchStrings(generator.getVariante(), generator.generateSuchstring(position));
			idSearchTypeMap.put(position.getEinkaufsangebotpositionDto().getIId(), generator.getVariante());
			idSuchstringsMap.put(position.getEinkaufsangebotpositionDto().getIId(), 
					position.getGeneratedSearchStringsBySearchType(generator.getVariante()));
		}
		
		return idSuchstringsMap;
	}
	
	public Integer getSearchTypeForSearchId(Integer searchId) {
		return idSearchTypeMap.get(searchId);
	}

	private SuchstringGenerator getSuchstringGenerator(IWebabfrageResult result) throws Throwable {
		for (SuchstringGenerator generator : suchstringGeneratoren) {
			List<String> suchstrings = generator.generateSuchstring(result);
			if (suchstrings != null && !suchstrings.isEmpty()) {
				return generator;
			}
		}
		return null;
	}
}
