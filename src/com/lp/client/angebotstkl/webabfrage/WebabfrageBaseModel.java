package com.lp.client.angebotstkl.webabfrage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lp.client.frame.Defaults;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EkweblieferantDto;
import com.lp.server.angebotstkl.service.WebabfrageDto;
import com.lp.server.angebotstkl.service.WebabfragepositionDto;

public class WebabfrageBaseModel {

	private List<WebabfrageDto> webabfrageTypen;
	private Integer selectedWebabfrageTyp;
	private EinkaufsangebotDto einkaufsangebotDto;
	private Map<Integer,BigDecimal> mengenstaffel;
	private Integer selectedMenge;
	private List<WebabfragepositionDto> positionen;
	private List<IWebabfrageResult> results;
	private IWebabfrageResult selectedResult;
	private Map<Integer, EkweblieferantDto> ekweblieferanten;
	
	public WebabfrageBaseModel(EinkaufsangebotDto einkaufsangebotDto, List<WebabfragepositionDto> positionen) {
		webabfrageTypen = new ArrayList<WebabfrageDto>();
		selectedWebabfrageTyp = AngebotstklFac.WebabfrageTyp.FINDCHIPS;
		ekweblieferanten = Collections.synchronizedMap(new HashMap<Integer, EkweblieferantDto>());
		setEinkaufsangebotDto(einkaufsangebotDto);
		setPositionen(positionen);
		initMengenstaffel();
	}

	private void initMengenstaffel() {
		mengenstaffel = new HashMap<Integer,BigDecimal>();
		if (getEinkaufsangebotDto().getNMenge1() != null && 
				BigDecimal.ZERO.compareTo(getEinkaufsangebotDto().getNMenge1()) != 0) {
			mengenstaffel.put(1, getEinkaufsangebotDto().getNMenge1());
		}
		if (getEinkaufsangebotDto().getNMenge2() != null && 
				BigDecimal.ZERO.compareTo(getEinkaufsangebotDto().getNMenge2()) != 0) {
			mengenstaffel.put(2, getEinkaufsangebotDto().getNMenge2());
		}
		if (getEinkaufsangebotDto().getNMenge3() != null && 
				BigDecimal.ZERO.compareTo(getEinkaufsangebotDto().getNMenge3()) != 0) {
			mengenstaffel.put(3, getEinkaufsangebotDto().getNMenge3());
		}
		if (getEinkaufsangebotDto().getNMenge4() != null && 
				BigDecimal.ZERO.compareTo(getEinkaufsangebotDto().getNMenge4()) != 0) {
			mengenstaffel.put(4, getEinkaufsangebotDto().getNMenge4());
		}
		if (getEinkaufsangebotDto().getNMenge5() != null && 
				BigDecimal.ZERO.compareTo(getEinkaufsangebotDto().getNMenge5()) != 0) {
			mengenstaffel.put(5, getEinkaufsangebotDto().getNMenge5());
		}
		selectedMenge = getSmallestMengenstaffelKey();
	}

	private Integer getSmallestMengenstaffelKey() {
		Integer key = null;
		for (Entry<Integer, BigDecimal> entry : mengenstaffel.entrySet()) {
			if (key == null || entry.getKey() < key) {
				key = entry.getKey();
			}
		}
		
		return key;
	}

	public List<WebabfrageDto> getWebabfrageTypen() {
		return webabfrageTypen;
	}

	public void setWebabfrageTypen(List<WebabfrageDto> webabfrageTypen) {
		this.webabfrageTypen = webabfrageTypen;
	}

	public Integer getSelectedWebabfrageTyp() {
		return selectedWebabfrageTyp;
	}

	public void setSelectedWebabfrageTyp(Integer selectedWebabfrageTyp) {
		this.selectedWebabfrageTyp = selectedWebabfrageTyp;
	}

	public EinkaufsangebotDto getEinkaufsangebotDto() {
		return einkaufsangebotDto;
	}

	private void setEinkaufsangebotDto(EinkaufsangebotDto einkaufsangebotDto) {
		this.einkaufsangebotDto = einkaufsangebotDto;
	}

	public Integer getEinkaufsangebotIId() {
		return getEinkaufsangebotDto().getIId();
	}

	public List<WebabfragepositionDto> getPositionen() {
		return positionen;
	}

	public void setPositionen(List<WebabfragepositionDto> positionen) {
		this.positionen = positionen;
	}

	public List<IWebabfrageResult> getResults() {
		return results;
	}

	public void setResults(List<IWebabfrageResult> results) {
		this.results = results;
	}
	
	public List<IWebabfrageResult> getResultsAsNormalList() {
		List<IWebabfrageResult> normalList = new ArrayList<IWebabfrageResult>();
		
		for (IWebabfrageResult result : getResults()) {
			if (result instanceof CondensedWebabfrageResultItem) {
				normalList.addAll(((CondensedWebabfrageResultItem)result).getList());
			} else {
				normalList.add(result);
			}
		}
		
		return normalList;
	}
	
	public IWebabfrageResult getWebabfrageResultByEinkaufsangebotpositionIId(Integer iId) {
		for (IWebabfrageResult result : getResults()) {
			if (result.getEinkaufsangebotpositionDto().getIId().equals(iId)) {
				return result;
			}
		}
		return null;
	}

	public void setEkweblieferanten(List<EkweblieferantDto> ekweblieferantenList) {
		synchronized(ekweblieferanten) {
			ekweblieferanten.clear();
			for (EkweblieferantDto dto : ekweblieferantenList) {
				ekweblieferanten.put(dto.getWebpartnerIId(), dto);
			}
		}
	}

	public Map<Integer, EkweblieferantDto> getEkweblieferanten() {
		return ekweblieferanten;
	}

	public Map<Integer, BigDecimal> getEkMengen() {
		return mengenstaffel;
	}

	public Integer getSelectedMengeIndex() {
		return selectedMenge;
	}
	
	public BigDecimal getSelectedMenge() {
		return getEkMengen().get(getSelectedMengeIndex());
	}

	public void setSelectedMengeIndex(Integer selectedMenge) {
		this.selectedMenge = selectedMenge;
	}

	public IWebabfrageResult getSelectedResult() {
		return selectedResult;
	}

	public void setSelectedResult(IWebabfrageResult selectedResult) {
		this.selectedResult = selectedResult;
	}
	
	public IWebabfrageResult getResultById(Integer id) {
		for (IWebabfrageResult result : results) {
			if (result.getEinkaufsangebotpositionDto().getIId().equals(id)) {
				return result;
			}
		}
		return null;
	}
	
	public static int getIUINachkommastellenPreiseEK() {
		try {
			return Defaults.getInstance().getIUINachkommastellenPreiseEK();
		} catch (Throwable e) {
			return 4;
		}
	}
	
	public static int getIUINachkommastellenMenge() {
		try {
			return Defaults.getInstance().getIUINachkommastellenMenge();
		} catch (Throwable e) {
			return 4;
		}
	}
}
