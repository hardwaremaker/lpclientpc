package com.lp.client.angebotstkl.webabfrage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;

public class CondensedWebabfrageResultItem implements IWebabfrageResult, Serializable {
	private static final long serialVersionUID = 3003770016211231608L;
	private List<IWebabfrageResult> list;
	
	public CondensedWebabfrageResultItem(List<IWebabfrageResult> list) {
		if (list == null) throw new NullPointerException("list == null");
		if (list.size() == 0) throw new IllegalArgumentException("list.size() == 0");
		
		setList(new ArrayList<IWebabfrageResult>(list));
	}

	public CondensedWebabfrageResultItem(IWebabfrageResult result) {
		this(Arrays.asList(result));
	}

	public List<IWebabfrageResult> getList() {
		return list;
	}

	public void setList(List<IWebabfrageResult> list) {
		this.list = list;
	}

	private IWebabfrageResult getFirstItem() {
		return list.get(0);
	}

	@Override
	public String getName() {
		return getFirstItem().getName();
	}

	@Override
	public BigDecimal getMenge() {
		BigDecimal gesamtMenge = new BigDecimal(0);
		for (IWebabfrageResult result : list) {
			gesamtMenge = gesamtMenge.add(result.getMenge());
		}
		return gesamtMenge;
	}

	@Override
	public void setFoundItems(List<NormalizedWebabfragePart> items) {
		for (IWebabfrageResult result : list) {
			result.setFoundItems(new ArrayList<NormalizedWebabfragePart>(items));
		}
	}

	@Override
	public List<NormalizedWebabfragePart> getFoundItems() {
		return getFirstItem().getFoundItems();
	}

	@Override
	public void setSelectedPart(NormalizedWebabfragePart part) {
		for (IWebabfrageResult result : list) {
			result.setSelectedPart(part);
		}
	}

	@Override
	public NormalizedWebabfragePart getSelectedPart() {
		return getFirstItem().getSelectedPart();
	}

	@Override
	public void setGeneratedSearchStrings(Integer searchType, List<String> searchStrings) {
		for (IWebabfrageResult result : list) {
			result.setGeneratedSearchStrings(searchType, searchStrings);
		}
	}

	@Override
	public List<String> getGeneratedSearchStringsBySearchType(Integer searchType) {
		return getFirstItem().getGeneratedSearchStringsBySearchType(searchType);
	}

	@Override
	public void setUserString(String searchString) {
		for (IWebabfrageResult result : list) {
			result.setUserString(searchString);
		}
	}

	@Override
	public String getUserString() {
		return getFirstItem().getUserString();
	}

	@Override
	public void setEinkaufsangebotpositionDto(EinkaufsangebotpositionDto positionDto) {
	}

	@Override
	public EinkaufsangebotpositionDto getEinkaufsangebotpositionDto() {
		return getFirstItem().getEinkaufsangebotpositionDto();
	}

	@Override
	public void setArtikelDto(ArtikelDto artikelDto) {
		
	}

	@Override
	public ArtikelDto getArtikelDto() {
		return getFirstItem().getArtikelDto();
	}

	@Override
	public Boolean hasArtikelDto() {
		return getFirstItem().hasArtikelDto();
	}

	@Override
	public void setArtikellieferantDtos(List<ArtikellieferantDto> artikellieferantDto) {

	}

	@Override
	public List<ArtikellieferantDto> getArtikellieferantDtos() {
		return getFirstItem().getArtikellieferantDtos();
	}

	@Override
	public Boolean hasArtikellieferantDto() {
		return getFirstItem().hasArtikellieferantDto();
	}

	@Override
	public void addPart(NormalizedWebabfragePart part) {
		for (IWebabfrageResult result : list) {
			result.addPart(part);
		}
	}

	@Override
	public void setFoundParts(Integer searchType, List<NormalizedWebabfragePart> parts) {
		for (IWebabfrageResult result : list) {
			result.setFoundParts(searchType, parts);
		}
	}

	@Override
	public void addPartBySearchType(Integer searchType, NormalizedWebabfragePart part) {
		for (IWebabfrageResult result : list) {
			result.addPartBySearchType(searchType, part);
		}
	}

	@Override
	public List<NormalizedWebabfragePart> getFoundPartsBySearchType(Integer searchType) {
		return getFirstItem().getFoundPartsBySearchType(searchType);
	}

	@Override
	public void setState(IColorState state) {
		for (IWebabfrageResult result : list) {
			result.setState(state);
		}
	}

	@Override
	public IColorState getState() {
		return getFirstItem().getState();
	}

	@Override
	public void clearParts() {
		for (IWebabfrageResult result : list) {
			result.clearParts();
		}
	}

}
