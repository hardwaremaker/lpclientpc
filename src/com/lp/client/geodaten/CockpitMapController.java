package com.lp.client.geodaten;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.pc.LPMain;

public class CockpitMapController implements IMapDataController {
	
	private IMapViewController mapViewController;
	private IMapEventHandler mapEventHandler;
	private PanelGeodatenMap panelMaps;
	private InternalFrame internalFrame;
	
	public CockpitMapController(InternalFrame internalFrame) throws Throwable {
		this.internalFrame = internalFrame;
		init();
	}

	private void init() throws Throwable {
		createPanelMap();
		createMapViewController();
	}

	private void createMapViewController() {
		mapEventHandler = null;
		mapViewController = new MapViewController(panelMaps.getMapsBrowser());
		mapViewController.setMapEventHandler(mapEventHandler);
	}

	private void createPanelMap() throws Throwable {
		panelMaps = new PanelGeodatenMap(getInternalFrame(), LPMain.getTextRespectUISPr("geodaten.tab.map"));
	}

	private InternalFrame getInternalFrame() {
		return internalFrame;
	}

	public void loadMap(MapPosition center, IMapData mapData) {
		loadMap(center);
		
		if (mapData == null) return;
		mapViewController.addMapData(mapData.getMapData());
	}

	@Override
	public void loadMap(MapPosition center) {
		mapViewController.removeAllMarkers();
		mapViewController.setCenter(center);
		mapViewController.addMarker(center);
	}

	public PanelGeodatenMap getPanelMaps() {
		return panelMaps;
	}
	
	@Override
	public void clearMap() {
		mapViewController.removeAllMarkers();
	}
}
