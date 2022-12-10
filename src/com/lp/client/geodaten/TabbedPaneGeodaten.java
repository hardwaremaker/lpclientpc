package com.lp.client.geodaten;

import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.InternalFrameLieferant;
import com.lp.client.partner.InternalFramePartner;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.partner.TabbedPaneKunde;
import com.lp.client.partner.TabbedPaneLieferant;
import com.lp.client.partner.TabbedPanePartner;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LocaleFac;

public class TabbedPaneGeodaten extends TabbedPane implements IMapDataController {
	private static final long serialVersionUID = -5654705766319133065L;

	public class TabIndex {
		public static final int FILTER = 0;
		public static final int MAPS = 1;
	}
	private WrapperMenuBar wrapperMenuBar;
	private PanelGeodatenMap panelMaps;
	private PanelBasis panelFilter;
	private IMapEventHandler mapEventHandler;
	private IMapViewController mapViewController;

	public TabbedPaneGeodaten(InternalFrameMaps internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("geodaten.modulname"));
		jbInit();
		initComponents();
	}
	
	@Override
	public InternalFrameMaps getInternalFrame() {
		return (InternalFrameMaps) super.getInternalFrame();
	}
	
	private void jbInit() throws Throwable {
		insertTab(LPMain.getTextRespectUISPr("geodaten.tab.daten"), null, null, 
				LPMain.getTextRespectUISPr("geodaten.tab.daten"), TabIndex.FILTER);
		insertTab(LPMain.getTextRespectUISPr("geodaten.tab.map"), null, null, 
				LPMain.getTextRespectUISPr("geodaten.tab.map"), TabIndex.MAPS);
		
		createPanelFilter();
		createPanelMap();
		createMapViewController();

		addChangeListener(this);
	}

	private void createMapViewController() {
		mapEventHandler = new MapEventHandler();
		mapViewController = new MapViewController(panelMaps.getMapsBrowser());
		mapViewController.setMapEventHandler(mapEventHandler);
	}

	private void createPanelMap() throws Throwable {
		panelMaps = new PanelGeodatenMap(getInternalFrame(), LPMain.getTextRespectUISPr("geodaten.tab.map"));
		setComponentAt(TabIndex.MAPS, panelMaps);
//		setEnabledAt(TabIndex.MAPS, false);
	}

	private void createPanelFilter() throws Throwable {
		panelFilter = new PanelGeodatenDaten(getInternalFrame(), LPMain.getTextRespectUISPr("geodaten.tab.daten"), this);
		setComponentAt(TabIndex.FILTER, panelFilter);
	}
	
	@Override
	protected JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
		}
		return wrapperMenuBar;
	}

	public void lPEventObjectChanged(javax.swing.event.ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		
		if (TabIndex.FILTER == getSelectedIndex()) {
			//panelFilter.eventYouAreSelected(false);
		} else if (TabIndex.MAPS == getSelectedIndex()) {
			panelMaps.eventYouAreSelected(false);
		}
	}

	@Override
	protected void lPActionEvent(ActionEvent e) throws Throwable {
	}

	public void loadMap(MapPosition center, IMapData mapData) {
		loadMap(center);
		
		if (mapData == null) return;
		mapViewController.addMapData(mapData.getMapData());
	}

	@Override
	public void loadMap(MapPosition center) {
		setSelectedIndex(TabIndex.MAPS);
		mapViewController.removeAllMarkers();
		mapViewController.setCenter(center);
	}
	
	@Override
	public void clearMap() {
		mapViewController.removeAllMarkers();
	}
	
	public class MapEventHandler implements IMapEventHandler {

		@Override
		public void onKundeClick(Integer id) {
			try {
				if (LPMain.getInstance().getDesktop()
						.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_KUNDE)) {
					InternalFrameKunde ifKunde = (InternalFrameKunde) LPMain
							.getInstance().getDesktop().holeModul(LocaleFac.BELEGART_KUNDE);
					ifKunde.geheZu(InternalFrameKunde.IDX_PANE_KUNDE,
							TabbedPaneKunde.IDX_PANE_KUNDE, id, null,
							PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) id));
				}
			} catch (Throwable t) {
				handleException(t, false);
			}
		}

		@Override
		public void onInteressentClick(Integer id) {
			onKundeClick(id);
		}

		@Override
		public void onLieferantClick(Integer id) {
			try {
				if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_LIEFERANT)) {
					InternalFrameLieferant ifLieferant = (InternalFrameLieferant)LPMain.getInstance()
							.getDesktop().holeModul(LocaleFac.BELEGART_LIEFERANT);
					ifLieferant.geheZu(InternalFrameLieferant.IDX_PANE_LIEFERANT, 
							TabbedPaneLieferant.IDX_PANE_LIEFERANT, id, null, 
							PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) id));
				}
			} catch (Throwable t) {
				handleException(t, false);
			}
		}

		@Override
		public void onPartnerClick(Integer id) {
			try {
				if (LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_PARTNER)) {
					InternalFramePartner ifPartner = (InternalFramePartner)LPMain.getInstance()
							.getDesktop().holeModul(LocaleFac.BELEGART_PARTNER);
					ifPartner.geheZu(InternalFramePartner.IDX_PANE_PARTNER, 
							TabbedPanePartner.IDX_PANEL_QP, id, null, 
							PartnerFilterFactory.getInstance().createFKPartnerKey((Integer) id));
				}
			} catch (Throwable t) {
				handleException(t, false);
			}
		}

		@Override
		public void onPositionClick(String title) {
		}
		
	}
}
