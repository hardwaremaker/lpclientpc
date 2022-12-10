package com.lp.client.geodaten;

import java.util.EventObject;

import javax.swing.JTabbedPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.util.IconFactory;
import com.lp.server.system.service.LocaleFac;

public class InternalFrameMaps extends InternalFrame {
	private static final long serialVersionUID = -752594583018208284L;
	
	public class TabIndex {
		public static final int MAP = 0;
	}
	private TabbedPaneGeodaten tabbedPaneMap;
	
	public InternalFrameMaps(String sAddTitleI, String sRechtModulweitI) throws Throwable {
		super(sAddTitleI, LocaleFac.BELEGART_GEODATENANZEIGE, sRechtModulweitI);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		tabbedPaneRoot.insertTab("Maps", null, null, "Maps", TabIndex.MAP);
		createTabbedPaneMap();
		tabbedPaneMap.lPEventObjectChanged(null);
		tabbedPaneRoot.setSelectedComponent(tabbedPaneMap);
		
		setFrameIcon(IconFactory.getModuleMap());
	}

	private void createTabbedPaneMap() throws Throwable {
		tabbedPaneMap = new TabbedPaneGeodaten(this);
		tabbedPaneRoot.setComponentAt(TabIndex.MAP, tabbedPaneMap);
		initComponents();
	}

	@Override
	public void lPStateChanged(EventObject e) throws Throwable {
		int selectedTabIndex = ((JTabbedPane)e.getSource()).getSelectedIndex();
		
		if (TabIndex.MAP == selectedTabIndex) {
			tabbedPaneMap.lPEventObjectChanged(null);
		}
	}
	
	protected TabbedPaneGeodaten getTabbedPaneMap() {
		return tabbedPaneMap;
	}

}
