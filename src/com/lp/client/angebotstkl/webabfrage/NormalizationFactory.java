package com.lp.client.angebotstkl.webabfrage;

import java.util.List;

import com.lp.service.findchips.FindchipsResponse;

public class NormalizationFactory {
	private FindChipsPartFactory findchipsFactory;
	
	public NormalizationFactory(WebabfrageBaseModel baseModel) {
		findchipsFactory = new FindChipsPartFactory(baseModel);
	}

	/**
	 * Normalisiert die gefundenen Bauteile aus der Response.
	 * 
	 * @param response Findchips-Response, die die gefundenenen Bauteile beinhaltet
	 * @return Liste der normalisierten Bauteile
	 */
	public List<NormalizedWebabfragePart> create(Integer searchId, FindchipsResponse response) {
		return findchipsFactory.normalize(searchId, response) ;
	}
	
	public NormalizedWebabfragePart create(OemSecretsResponse response) {
		return null ;
	}
}
