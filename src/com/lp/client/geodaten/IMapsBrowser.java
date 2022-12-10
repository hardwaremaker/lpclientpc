package com.lp.client.geodaten;

import java.util.List;

public interface IMapsBrowser {

	void addMarker(JSMarker marker);
	
	void addMarker(List<JSMarker> marker);
	
	void removeAllMarkers();

	JSMap getMap();

	void setMapOptions(JSMapOptions mapOptions);

	void addMapEventListener(IMapEventListener listener);

	void removeMapEventListener(IMapEventListener listener);
}
