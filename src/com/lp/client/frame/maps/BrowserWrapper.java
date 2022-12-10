package com.lp.client.frame.maps;

import static javafx.concurrent.Worker.State.FAILED;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.geodaten.BrowserLoadCallback;
import com.lp.client.util.logger.LpLogger;

public abstract class BrowserWrapper extends Region {
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	private WebView webView = getWebView();
	private WebEngine webEngine = getWebEngine();
	private InternalFrame hvInternalFrame;
	private JComponent hvWrappingComponent;
	private BrowserLoadCallback browserLoadCallback;

	public BrowserWrapper(InternalFrame internalFrame, JComponent wrappingComponent) {
		hvInternalFrame = internalFrame;
		hvWrappingComponent = wrappingComponent;
		setDefaults();
		getChildren().add(getWebView());
	}
	
	public void setBrowserLoadCallback(BrowserLoadCallback browserLoadCallback) {
		this.browserLoadCallback = browserLoadCallback;
	}
	
	protected BrowserLoadCallback getBrowserLoadCallback() {
		return browserLoadCallback;
	}
	
	protected JComponent getHvWrappingComponent() {
		return hvWrappingComponent;
	}
	
	protected InternalFrame getHvInternalFrame() {
		return hvInternalFrame;
	}

	private void setDefaults() {
		getWebEngine().setJavaScriptEnabled(true);
		addExceptionPropertyErrorDialog();
		addStatePropertyAdjustHeight();
	}
	
	private void addStatePropertyAdjustHeight() {
 		getWebEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
		    @Override
		    public void changed(ObservableValue<? extends State> arg0, State oldState, State newState) {
		    	myLogger.info("ChangeListener: oldState = '" + oldState + "', newState = '" + newState + "'");
		        if (newState == State.SUCCEEDED) {
//		            adjustHeight();
		        	callbackLoadSucceeded();
		        } else if (newState == State.FAILED) {
		        	callbackLoadFailed();
		        }
		    }
		});
	}

	protected void callbackLoadFailed() {
		if (getBrowserLoadCallback() == null) return;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getBrowserLoadCallback().failed();
			}
		});
	}

	protected void callbackLoadSucceeded() {
		if (getBrowserLoadCallback() == null) return;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getBrowserLoadCallback().succeeded();
			}
		});
	}

	private void addExceptionPropertyErrorDialog() {
		getWebEngine().getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {

			public void changed(ObservableValue<? extends Throwable> o,
					Throwable old, final Throwable value) {
				myLogger.error("WebEngine loadworker exception", value);
				if (webEngine.getLoadWorker().getState() == FAILED) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JOptionPane.showMessageDialog(hvInternalFrame,
									(value != null) ? webEngine.getLocation()
													+ "\n" + value.getMessage()
													: webEngine.getLocation()
															+ "\nUnexpected error.",
											"Loading error...",
											JOptionPane.ERROR_MESSAGE);
						}
					});
				}
			}
		});
	}
	
	protected WebEngine getWebEngine() {
		if (webEngine == null) 
			webEngine = getWebView().getEngine();
		
		return webEngine;
	}

	protected WebView getWebView() {
		if (webView == null)
			webView = new WebView();
		
		return webView;
	}

	protected void adjustHeight() {
	    Platform.runLater(new Runnable(){
	        @Override                                
	        public void run() {
	            try {
	                Object result = getWebEngine().executeScript(
	                    "document.getElementById('map').offsetHeight");
	                myLogger.info("adjustHeight: Got result " + result) ;
	                if(result instanceof Integer) {
	                    Integer i = (Integer) result;
	                    double height = new Double(i);
	                    height = height + 20;
	                    getWebView().setPrefHeight(height);
	                }
	            } catch (Exception e) {
	                // not important
	            } catch(Throwable t) {
	                myLogger.info("adjustHeight: Throwable", t) ;
	            }
	        }               
	    });
	}

	@Override
	protected void layoutChildren() {
		double w = getWidth();
		double h = getHeight();
		layoutInArea(getWebView(), 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
	}

	@Override
	protected double computePrefWidth(double height) {
		return 650;
	}

	@Override
	protected double computePrefHeight(double width) {
		return 400;
	}
	
	public abstract void loadContent(BrowserLoadCallback loadCallback);
}
