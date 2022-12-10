package com.lp.client.frame.maps;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.geodaten.BrowserLoadCallback;
import com.lp.client.geodaten.IMapsBrowser;
import com.lp.client.geodaten.JSMap;
import com.lp.client.util.logger.LpLogger;

public class MapsPanelFX extends JFXPanel {
	private static final long serialVersionUID = -1709853483843128505L;
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	private InternalFrame internalFrame;
	private WebPanelInstaller webPanelInstaller;
	private IMapsBrowser browser;
	private JFXPanel webPanel;
	private JComponent parentComponent;
	
	public MapsPanelFX(InternalFrame internalFrame, JComponent wrappingComponent) {
		this.internalFrame = internalFrame;
		this.parentComponent = wrappingComponent;
		webPanel = this;
		parentComponent.addComponentListener(new ComponentListener() {			
			@Override
			public void componentShown(ComponentEvent arg0) {
			}
			@Override
			public void componentResized(ComponentEvent e) {
				Component c = e.getComponent() ;
				myLogger.info("componentresized to width: " + c.getWidth() + " height " + c.getHeight() + ".") ;
				
				if(webPanelInstaller != null) {
					webPanelInstaller.resizeTo(c.getWidth(), c.getHeight());
				}
			}
			@Override
			public void componentMoved(ComponentEvent arg0) {
			}
			@Override
			public void componentHidden(ComponentEvent arg0) {
			}
		});
	}
	
	public InternalFrame getInternalFrame() {
		return internalFrame;
	}

	private MapsBrowser createMapsBrowser(JSMap map) {
		return new MapsBrowser(getInternalFrame(), parentComponent, map);
//		return (MapsBrowser) new BrowserMock(getInternalFrame(), parentComponent);
	}
	
	public void load(JSMap map, BrowserLoadCallback callback) {
		webPanelInstaller = new WebPanelInstaller(map, callback);
		webPanelInstaller.run();
	}
	
	public IMapsBrowser getMapsBrowser() {
		if (webPanelInstaller == null) return null;
		return webPanelInstaller.getMapsBrowser();
	}
	
	private class WebPanelInstaller implements Runnable {
		private CountDownLatch latch = null ;
		private JSMap map;
		private BrowserLoadCallback loadCallback;

		public WebPanelInstaller(JSMap map, BrowserLoadCallback callback) {
			this.map = map;
			this.loadCallback = callback;
			latch = new CountDownLatch(1);
		}
		
		public void resizeTo(int width, int height) {
			if(webPanel != null) {
				myLogger.info("MapsPanel field size: width " + webPanel.getWidth() + " height: " + webPanel.getHeight() + ".");
				myLogger.info("new jfxpanel size width " + width + " height " + height + ".");
				Scene s = webPanel.getScene();
				if(s != null) {
					myLogger.info("actual scene size width " + s.getWidth() + " height " + s.getHeight() + ".");
					webPanel.setSize(width, height);
//					myLogger.info("new scene size width " + s.getWidth() + " height " + s.getHeight() + ".");
				}
			}
		}

		@Override
		public void run() {
			installWebPanel();
//			parentComponent.add(webPanel);
		}
		
		public IMapsBrowser getMapsBrowser() {
			if (browser == null) {
				//Schleife damit andere Interrupt-Quellen ignoriert werden.
				while(latch.getCount() != 0) {
					try {
						latch.await();
					} catch (InterruptedException e) {
					}
				}
			}
			return browser;
		}
		
		private void installWebPanel() {
			Platform.setImplicitExit(false);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					myLogger.info("Installing web panel...");
					final MapsBrowser b = createMapsBrowser(map);
					Scene scene = new Scene((Parent) b);
					webPanel.setScene(scene);
//					scene.widthProperty().addListener(new ChangeListener() {
//						@Override
//						public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//	                        Double w = (Double)newValue;
//	    					myLogger.info("Scene changelistener width: " + oldValue  + " newValue: " + w) ;
//	                        browser.getWebView().setPrefWidth(w);
//	                        browser.adjustHeight();
//						}
//					});
					
					b.loadContent(loadCallback);
					browser = b;
					myLogger.info("Web panel installed.");
					latch.countDown();
				}
			});
		}
		
	}
}
