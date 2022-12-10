package com.lp.client.angebotstkl.webabfrage;

import java.util.List;

public interface IPartNormalizationFactory<T extends INormalizationResponse> {

	public List<NormalizedWebabfragePart> normalize(Integer searchId, T part);
}
