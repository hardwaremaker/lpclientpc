package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;
import java.util.List;

import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;

public interface IWebabfrageResult {

	public void setEinkaufsangebotpositionDto(EinkaufsangebotpositionDto positionDto);
	
	public EinkaufsangebotpositionDto getEinkaufsangebotpositionDto();
	
	public void setArtikelDto(ArtikelDto artikelDto);
	
	public ArtikelDto getArtikelDto();
	
	public Boolean hasArtikelDto();
	
	public void setArtikellieferantDtos(List<ArtikellieferantDto> artikellieferantDto);
	
	public List<ArtikellieferantDto> getArtikellieferantDtos();
	
	public Boolean hasArtikellieferantDto();
	
	public String getName();
	
	public BigDecimal getMenge();
	
	public void setFoundItems(List<NormalizedWebabfragePart> items);
	
	public List<NormalizedWebabfragePart> getFoundItems();
	
	public void clearParts();
	
	public void addPart(NormalizedWebabfragePart part);
	
	public NormalizedWebabfragePart getSelectedPart();
	
	public void setSelectedPart(NormalizedWebabfragePart part);
	
	public void setGeneratedSearchStrings(Integer searchType, List<String> searchStrings);
	
	public List<String> getGeneratedSearchStringsBySearchType(Integer searchType);
	
	public void setUserString(String searchString);
	
	public String getUserString();
	
	public void setFoundParts(Integer searchType, List<NormalizedWebabfragePart> parts);
	
	public void addPartBySearchType(Integer searchType, NormalizedWebabfragePart part);
	
	public List<NormalizedWebabfragePart> getFoundPartsBySearchType(Integer searchType);
	
	public void setState(IColorState state);
	
	public IColorState getState();
}
