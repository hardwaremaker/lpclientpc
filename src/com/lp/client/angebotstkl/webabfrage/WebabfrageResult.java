package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;

public class WebabfrageResult implements IWebabfrageResult {

	private EinkaufsangebotpositionDto einkaufsangebotpositionDto;
	private ArtikelDto artikelDto;
	private List<ArtikellieferantDto> artikellieferantDtos;
	private List<NormalizedWebabfragePart> foundItems;
	private Map<Integer, List<NormalizedWebabfragePart>> normalizedPartsByType;
	private NormalizedWebabfragePart selectedPart;
	private Map<Integer, List<String>> searchStringsByType;
	private String userString;
	private IColorState state;
	
	public WebabfrageResult() {
		searchStringsByType = new HashMap<Integer, List<String>>();
		normalizedPartsByType = new TreeMap<Integer, List<NormalizedWebabfragePart>>();
		setState(new ColorStartState());
	}

	@Override
	public String getName() {
		if (hasArtikelDto() && getArtikelDto().getArtikelsprDto() != null) {
			return getPositionsbezeichnung(getArtikelDto().getArtikelsprDto().getCBez(), getArtikelDto().getArtikelsprDto().getCZbez());
		}
		
		return getEinkaufsangebotpositionDto() != null ? 
				getPositionsbezeichnung(getEinkaufsangebotpositionDto().getCBez(), 
						getEinkaufsangebotpositionDto().getCZusatzbez()) : null;
	}

	private String getPositionsbezeichnung(String bez, String zBez) {
		StringBuilder builder = new StringBuilder();
		
		if (bez != null && !bez.isEmpty()) {
			builder.append(bez);
			builder.append(" ");
		}
		if (zBez != null && !zBez.isEmpty()) {
			builder.append(zBez);
			builder.append(" ");
		}
		
		return builder.toString();
	}

	@Override
	public BigDecimal getMenge() {
		return getEinkaufsangebotpositionDto() != null ? getEinkaufsangebotpositionDto().getNMenge() : null;
	}

	@Override
	public void setFoundItems(List<NormalizedWebabfragePart> items) {
		foundItems = items;
		setState(new ColorFoundState());
	}

	@Override
	public List<NormalizedWebabfragePart> getFoundItems() {
//		if (foundItems == null) {
//			foundItems = Collections.synchronizedList(new ArrayList<NormalizedWebabfragePart>());
//		}
//		return foundItems;
		List<NormalizedWebabfragePart> foundParts = new ArrayList<NormalizedWebabfragePart>();
		synchronized (normalizedPartsByType) {
			for (Entry<Integer, List<NormalizedWebabfragePart>> entry : normalizedPartsByType.entrySet()) {
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					foundParts.addAll(entry.getValue());
					return foundParts;
				}
			}
		}
		return foundParts;
	}

	@Override
	public void addPart(NormalizedWebabfragePart part) {
		getFoundItems().add(part);
		setState(new ColorFoundState());
	}

	@Override
	public void setSelectedPart(NormalizedWebabfragePart part) {
		selectedPart = part;
		setState(part != null ? new ColorSelectedState() : new ColorFoundState());
	}

	@Override
	public NormalizedWebabfragePart getSelectedPart() {
		return selectedPart;
	}

	@Override
	public void setGeneratedSearchStrings(Integer searchType, List<String> searchStrings) {
		if (searchType == null || searchStrings == null) return;
		
		searchStringsByType.put(searchType, searchStrings);
	}

	@Override
	public List<String> getGeneratedSearchStringsBySearchType(Integer searchType) {
		if (searchType == null || searchStringsByType.get(searchType) == null) {
			return new ArrayList<String>();
		}
		
		return searchStringsByType.get(searchType);
	}

	@Override
	public void setUserString(String searchString) {
		userString = searchString;
	}

	@Override
	public String getUserString() {
		return userString;
	}

	@Override
	public void setFoundParts(Integer searchType, List<NormalizedWebabfragePart> parts) {
		if (searchType == null || parts == null) return;
		
		normalizedPartsByType.put(searchType, parts);
		setState(new ColorFoundState());
	}

	@Override
	public void addPartBySearchType(Integer searchType,	NormalizedWebabfragePart part) {
		synchronized(normalizedPartsByType) {
			List<NormalizedWebabfragePart> list = normalizedPartsByType.get(searchType);
			if (list == null) {
				list = new ArrayList<NormalizedWebabfragePart>();
			}
			list.add(part);
			normalizedPartsByType.put(searchType, list);
		}
		setState(new ColorFoundState());
	}

	@Override
	public List<NormalizedWebabfragePart> getFoundPartsBySearchType(Integer searchType) {
		if (searchType == null || normalizedPartsByType.get(searchType) == null) {
			return new ArrayList<NormalizedWebabfragePart>();
		}

		return normalizedPartsByType.get(searchType);
	}

	@Override
	public void setEinkaufsangebotpositionDto(EinkaufsangebotpositionDto positionDto) {
		this.einkaufsangebotpositionDto = positionDto;
	}

	@Override
	public EinkaufsangebotpositionDto getEinkaufsangebotpositionDto() {
		return einkaufsangebotpositionDto;
	}

	@Override
	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	@Override
	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	@Override
	public Boolean hasArtikelDto() {
		return getArtikelDto() != null;
	}

	@Override
	public void setArtikellieferantDtos(List<ArtikellieferantDto> artikellieferantDto) {
		this.artikellieferantDtos = artikellieferantDto;
	}

	@Override
	public List<ArtikellieferantDto> getArtikellieferantDtos() {
		if (artikellieferantDtos == null) {
			artikellieferantDtos = new ArrayList<ArtikellieferantDto>();
		}
		
		return artikellieferantDtos;
	}

	@Override
	public Boolean hasArtikellieferantDto() {
		return !getArtikellieferantDtos().isEmpty();
	}

	@Override
	public void setState(IColorState state) {
		this.state = state;
	}

	@Override
	public IColorState getState() {
		return state;
	}

	@Override
	public void clearParts() {
		synchronized (normalizedPartsByType) {
			for (Entry<Integer, List<NormalizedWebabfragePart>> entry : normalizedPartsByType.entrySet()) {
				if (entry.getValue() != null) {
					entry.getValue().clear();
				}
			}
		}
	}

}
