package com.lp.client.geodaten;

public interface IMapDataController {

	void loadMap(MapPosition center, IMapData mapData);

	void loadMap(MapPosition center);
	
	void clearMap();
}
