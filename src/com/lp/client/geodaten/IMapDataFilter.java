package com.lp.client.geodaten;

import com.lp.server.util.HvOptional;

public interface IMapDataFilter {

	HvOptional<IMapData> getMapDataController();
	
	void loadGeodaten();
	
	boolean mustLoadGeodaten();
	
	void registerGeodatenFilterListener(IGeodatenFilterListener listener);
}
