package com.lp.client.angebotstkl.webabfrage;

import java.util.List;

public abstract class SuchstringGenerator {
	
	static class SearchType {
		public static final Integer USERSTRING = 1;
		public static final Integer HERSTELLERARTIKELNUMMER = 2;
		public static final Integer LIEFERANTENARTIKELNUMMER = 3;
		public static final Integer SI_WERTE = 4;
		public static final Integer BEZEICHNUNG = 5;
	}
	
	protected SuchstringGenerator() {
	}
	
	public abstract List<String> generateSuchstring(IWebabfrageResult result) throws Throwable;
	
	public abstract Integer getVariante();
	
}
