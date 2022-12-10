package com.lp.client.geodaten;


public interface IMapViewController {

	void addMarker(MapPosition mapPosition);
	void addMarker(MapPartner mapPartner);
	void addMarker(MapKunde mapKunde);
	void addMarker(MapLieferant mapLieferant);

	void setCenter(MapPartner mapPartner);
	void setCenter(MapKunde mapKunde);
	void setCenter(MapLieferant mapLieferant);
	void setCenter(MapPosition mapPosition);
	
	void removeAllMarkers();
	
	void addMapData(MapData mapData);
	
	void setMapEventHandler(IMapEventHandler eventHandler);
}
