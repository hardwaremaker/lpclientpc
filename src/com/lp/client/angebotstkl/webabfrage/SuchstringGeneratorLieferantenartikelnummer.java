package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.ArtikellieferantDto;

public class SuchstringGeneratorLieferantenartikelnummer extends SuchstringGenerator {

	public SuchstringGeneratorLieferantenartikelnummer() {
	}

	@Override
	public List<String> generateSuchstring(IWebabfrageResult result) throws Throwable {
		List<String> list = new ArrayList<String>();
		if (result == null || !result.hasArtikellieferantDto()) {
			return list;
		}
		
		for (ArtikellieferantDto dto : result.getArtikellieferantDtos()) {
			if (dto.getCArtikelnrlieferant() != null) {
				list.add(dto.getCArtikelnrlieferant());
			}
		}
		
		return list;
	}

	@Override
	public Integer getVariante() {
		return SearchType.LIEFERANTENARTIKELNUMMER;
	}

}
