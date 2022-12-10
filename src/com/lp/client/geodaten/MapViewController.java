package com.lp.client.geodaten;

import com.lp.client.util.logger.LpLogger;

public class MapViewController implements IMapViewController {
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	private IMapsBrowser mapsBrowser;
	private JSObjectFactory jsFactory;
	private IMapEventHandler mapEventHandler;
	private enum MarkerNamePrefix {
		KD, LF, PA, PO, IN;
	}
	
	public MapViewController(IMapsBrowser mapsBrowser) {
		setMapsBrowser(mapsBrowser);
	}
	
	public void setMapsBrowser(IMapsBrowser mapsBrowser) {
		this.mapsBrowser = mapsBrowser;
		mapsBrowser.addMapEventListener(new MapEventListener());
	}
	
	public IMapsBrowser getMapsBrowser() {
		return mapsBrowser;
	}
	
	private JSObjectFactory getJsFactory() {
		if (jsFactory == null) {
			jsFactory = new JSObjectFactory();
		}
		return jsFactory;
	}

	private String createIconPath(String markerColor) {
		return "https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + markerColor;
	}
	
	@Override
	public void addMarker(MapPartner mapPartner) {
		getMapsBrowser().addMarker(createMarker(mapPartner));
	}
	
	private JSMarker createMarker(MapPartner mapPartner) {
		JSMarker marker = setupMarker(mapPartner);
		marker.setName(MarkerNamePrefix.PA.name() + mapPartner.getId());
		marker.getMarkerOptions().setIconPath(createIconPath("0091d0"));
		return marker;
	}
	
	@Override
	public void addMarker(MapKunde mapKunde) {
		getMapsBrowser().addMarker(createMarker(mapKunde));
	}

	private JSMarker createMarker(MapKunde mapKunde) {
		JSMarker marker = setupMarker(mapKunde);
		marker.setName(MarkerNamePrefix.KD.name() + mapKunde.getId());
		marker.getMarkerOptions().setIconPath(createIconPath("ff8b00"));
		return marker;
	}
	
	@Override
	public void addMarker(MapLieferant mapLieferant) {
		getMapsBrowser().addMarker(createMarker(mapLieferant));
	}
	
	private JSMarker createMarker(MapLieferant mapLieferant) {
		JSMarker marker = setupMarker(mapLieferant);
		marker.setName(MarkerNamePrefix.LF.name() + mapLieferant.getId());
		marker.getMarkerOptions().setIconPath(createIconPath("08d11c"));
		return marker;
	}
	
	public void addMarker(MapInteressent mapInteressent) {
		getMapsBrowser().addMarker(createMarker(mapInteressent));
	}
	
	private JSMarker createMarker(MapInteressent mapInteressent) {
		JSMarker marker = setupMarker(mapInteressent);
		marker.setName(MarkerNamePrefix.IN.name() + mapInteressent.getId());
		marker.getMarkerOptions().setIconPath(createIconPath("fcf100"));
		return marker;
	}
	
	public void addMarker(MapPosition mapPosition) {
		getMapsBrowser().addMarker(createMarker(mapPosition));
	}
	
	private JSMarker createMarker(MapPosition mapPosition) {
		JSMarker marker = setupMarker(mapPosition);
		marker.setName(MarkerNamePrefix.PO.name() + Integer.toString(System.identityHashCode(mapPosition)));
		marker.getMarkerOptions().setIconPath(createIconPath("aaaaaa"));
		return marker;
	}
	
	private JSMarker setupMarker(MapPosition mapPosition) {
		JSLatLng latLng = transformGeodaten(mapPosition);
		JSMarker marker = getJsFactory().createMarker(getMapsBrowser().getMap(), latLng);
		marker.getMarkerOptions().setTitle(mapPosition.getTitle());	
		return marker;
	}
	
	@Override
	public void setCenter(MapPosition mapPosition) {
		setCenterImpl(mapPosition, createMarker(mapPosition));
	}
	
	private JSLatLng transformGeodaten(MapPosition mapPosition) {
		return getJsFactory().createLatLng(mapPosition.getGeodatenDto().getBreitengrad(), 
				mapPosition.getGeodatenDto().getLaengengrad());
	}
	
	private void setCenterImpl(MapPosition mapPosition, JSMarker marker) {
		JSMapOptions mapOptions = getMapsBrowser().getMap().getMapOptions();
		mapOptions.setCenter(transformGeodaten(mapPosition));
		getMapsBrowser().setMapOptions(mapOptions);

		marker.getMarkerOptions().setIconPath(createIconPath("eeeeee"));
		getMapsBrowser().addMarker(marker);
	}
	
	@Override
	public void setCenter(MapPartner mapPartner) {
		setCenterImpl(mapPartner, createMarker(mapPartner));
	}

	@Override
	public void setCenter(MapKunde mapKunde) {
		setCenterImpl(mapKunde, createMarker(mapKunde));
	}
	
	@Override
	public void setCenter(MapLieferant mapLieferant) {
		setCenterImpl(mapLieferant, createMarker(mapLieferant));
	}
	
	@Override
	public void removeAllMarkers() {
		getMapsBrowser().removeAllMarkers();
	}

	protected class MapEventListener implements IMapEventListener {
		@Override
		public void onMarkerClick(String name) {
			if(mapEventHandler == null) {
				return;
			}
			if (name.startsWith(MarkerNamePrefix.PO.name())) {
				mapEventHandler.onPositionClick(name.substring(2));
				return;
			}
			
			Integer id = parseMarkerName(name);
			if (id == null) return;
			
			if (name.startsWith(MarkerNamePrefix.KD.name())) {
				mapEventHandler.onKundeClick(id);
			} else if (name.startsWith(MarkerNamePrefix.IN.name())) {
				mapEventHandler.onInteressentClick(id);
			} else if (name.startsWith(MarkerNamePrefix.LF.name())) {
				mapEventHandler.onLieferantClick(id);
			} else if (name.startsWith(MarkerNamePrefix.PA.name())) {
				mapEventHandler.onPartnerClick(id);
			}
		}
	}
	
	private Integer parseMarkerName(String name) {
		try {
			return Integer.parseInt(name.substring(2));
		} catch (NumberFormatException ex) {
			myLogger.warn("Could not parse id from name '" + name + "' from startIndex 2", ex);
		}
		return null;
	}

	@Override
	public void addMapData(MapData mapData) {
		for (MapPosition mapPosition : mapData.getPositions()) {
			System.out.println(mapPosition.asString());
			if (mapPosition instanceof MapInteressent) {
				addMarker((MapInteressent)mapPosition);
			} else if (mapPosition instanceof MapKunde) {
				addMarker((MapKunde)mapPosition);
			} else if (mapPosition instanceof MapLieferant) {
				addMarker((MapLieferant)mapPosition);
			} else if (mapPosition instanceof MapPartner) {
				addMarker((MapPartner)mapPosition);
			} else if (mapPosition instanceof MapPosition) {
				addMarker(mapPosition);
			}
		}
	}
	
	@Override
	public void setMapEventHandler(IMapEventHandler eventHandler) {
		mapEventHandler = eventHandler;
	}
}
