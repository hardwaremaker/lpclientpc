package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.List;

public class SuchstringGeneratorUserstring extends SuchstringGenerator {

	public SuchstringGeneratorUserstring() {
	}

	@Override
	public List<String> generateSuchstring(IWebabfrageResult result) throws Throwable {
		List<String> list = new ArrayList<String>();
		if (result != null && result.getUserString() != null && !result.getUserString().isEmpty()) {
			list.add(result.getUserString());
		}
		
		return list;
	}

	@Override
	public Integer getVariante() {
		return SearchType.USERSTRING;
	}

}
