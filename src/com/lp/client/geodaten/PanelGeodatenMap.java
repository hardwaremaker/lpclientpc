package com.lp.client.geodaten;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.maps.MapsPanelFX;

public class PanelGeodatenMap extends PanelBasis {
	private static final long serialVersionUID = -5810287456102763741L;

	private MapsPanelFX mapsPanel;
	
	public PanelGeodatenMap(InternalFrame internalFrameI, String addTitleI)
			throws Throwable {
		super(internalFrameI, addTitleI);
		jbInit();
		initComponents();
	}

	private void jbInit() {
		mapsPanel = new MapsPanelFX(getInternalFrame(), this);
		HvLayout layout = HvLayoutFactory.create(this, "ins 0", "fill,grow", "fill,grow");
		layout.add(mapsPanel);
		
		JSObjectFactory jsFactory = new JSObjectFactory();
		JSMap map = jsFactory.createMapWithDisabledDefaultUI("", new JSLatLng("47.869368", "13.127429"), 10);
		map.setName("map");
		myLogger.info("Loading maps browser...");
		mapsPanel.load(map, new BrowserLoadCallback() {
			public void succeeded() {
				myLogger.info("Maps browser loaded.");
				browserLoadingCompleted();
			}
			public void failed() {
			}
		});
	}

	@Override
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
//		super.eventYouAreSelected(bNeedNoYouAreSelectedI);
//		JSMapOptions mapOptions = getMapsBrowser().getMap().getMapOptions();
//		mapOptions.setZoom(5);
//		getMapsBrowser().setMapOptions(mapOptions);
	}
	
	private void browserLoadingCompleted() {
	}

	protected IMapsBrowser getMapsBrowser() {
		return mapsPanel.getMapsBrowser();
	}
	
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_GEODATENANZEIGE;
	}
	
	protected javax.swing.JComponent getFirstFocusableComponent() throws Exception {
		return mapsPanel;
	}
}
