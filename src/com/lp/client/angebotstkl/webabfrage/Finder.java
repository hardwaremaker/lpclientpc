package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.angebotstkl.service.EkweblieferantDto;

public class Finder {
	
	public List<EkweblieferantDto> ekweblieferanten = new ArrayList<EkweblieferantDto>();
	public float step;
	public Map<Integer, Float> ekwebliefPercentage = new HashMap<Integer, Float>();

	public Finder() {
	}
	
	public void rateParts(IWebabfrageResult result, List<NormalizedWebabfragePart> parts) {
		
		for (NormalizedWebabfragePart part : parts) {
			Integer webpartnerIId = part.getDistributor().getIId();
			if (webpartnerIId != null && ekwebliefPercentage.containsKey(webpartnerIId)) {
				part.setRate(ekwebliefPercentage.get(webpartnerIId));
			} else {
				part.setRate(0);
			}
		}
	}

	public void setEkweblieferanten(List<EkweblieferantDto> ekweblieferanten) {
		this.ekweblieferanten = new ArrayList<EkweblieferantDto>(ekweblieferanten);
		Collections.sort(this.ekweblieferanten, new Comparator<EkweblieferantDto>() {

			@Override
			public int compare(EkweblieferantDto o1, EkweblieferantDto o2) {
				return o1.getISort().compareTo(o2.getISort()) * -1;
			}
		});
		
		initEkwebliefPercentage();
	}

	private void initEkwebliefPercentage() {
		step = 100 / (ekweblieferanten.size() + 1);
		
		ekwebliefPercentage.clear();
		float rate = step;
		for (EkweblieferantDto dto : ekweblieferanten) {
			ekwebliefPercentage.put(dto.getWebpartnerIId(), rate);
			rate += step;
		}
	}
}
