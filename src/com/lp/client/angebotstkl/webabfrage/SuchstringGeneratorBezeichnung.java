package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuchstringGeneratorBezeichnung extends SuchstringGenerator {

	@Override
	public List<String> generateSuchstring(IWebabfrageResult result) throws Throwable {
		String bezeichnung = result.getName();
		String[] bezParts = bezeichnung.split(" ");
		
		return bezParts != null ? Arrays.asList(bezParts) : new ArrayList<String>();
	}

	@Override
	public Integer getVariante() {
		return SearchType.BEZEICHNUNG;
	}

}
