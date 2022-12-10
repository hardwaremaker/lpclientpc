package com.lp.client.frame.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.geodaten.BrowserLoadCallback;
import com.lp.client.geodaten.IMapEventListener;
import com.lp.client.geodaten.IMapsBrowser;
import com.lp.client.geodaten.JSMap;
import com.lp.client.geodaten.JSMapOptions;
import com.lp.client.geodaten.JSMarker;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import netscape.javascript.JSObject;

public class MapsBrowser extends BrowserWrapper implements IMapsBrowser {

	private JSMap map;
	private ObjectMapper jsonMapper;
	private JSEventReceiver jsEventReceiver;
	private List<IMapEventListener> mapEventListener;

	private ScriptExecutor scriptExecutor = new ScriptExecutor();

	public MapsBrowser(InternalFrame internalFrame, JComponent wrappingComponent, JSMap map) {
		super(internalFrame, wrappingComponent);
		this.map = map;
		scriptExecutor.start();
	}

	@Override
	public JSMap getMap() {
		return map;
	}

	private List<IMapEventListener> getMapEventListener() {
		if (mapEventListener == null)
			mapEventListener = new ArrayList<IMapEventListener>();

		return mapEventListener;
	}

	@Override
	public void addMapEventListener(IMapEventListener listener) {
		getMapEventListener().add(listener);
	}

	@Override
	public void removeMapEventListener(IMapEventListener listener) {
		getMapEventListener().remove(listener);
	}

	public void loadContent(BrowserLoadCallback loadCallback) {
		setBrowserLoadCallback(loadCallback);

		Platform.runLater(new Runnable() {
			public void run() {
				String content;
				try {
					myLogger.info("Loading web content...");
					content = buildHtml();
					getWebEngine().getLoadWorker().stateProperty().addListener(MapsBrowser.this::mapLoaded);
					getWebEngine().loadContent(content);
					initCallback();
					myLogger.info("Html content:\n" + content);
				} catch (Throwable e) {
					myLogger.error("Error loading web content", e);
				}
			}

		});

	}

	private void mapLoaded(ObservableValue<? extends State> ov, State oldState, State newState) {
		if (newState == State.SUCCEEDED) {
			scriptExecutor.mapLoaded();
		}
	}

	private void initCallback() {
		JSObject window = (JSObject) getWebEngine().executeScript("window");
		jsEventReceiver = new JSEventReceiver();
		window.setMember("app", jsEventReceiver);
	}

	private String buildHtml() throws Throwable {
		HtmlBuilder htmlBuilder = new HtmlBuilder();
		return htmlBuilder.css(getCssStyle()).script().async().defer().source(getMapsApiUrl()).end().script()
				.javascriptType().content(getScriptSection()).end().bodyDivWrapper(getBodyDiv()).build();
	}

	private String getMapsApiUrl() throws Throwable {
		return "https://maps.googleapis.com/maps/api/js?key="
				+ DelegateFactory.getInstance().getParameterDelegate().getGoogleApiKey() + "&callback=initMap";
	}

	private String getBodyDiv() {
		return "map";
	}

	private String getScriptSection() throws JsonProcessingException {
		return "var map;\n" + "var markers = [];\n" + "function initMap() { \n" + asJavascriptValue(map) + "\n"
				+ "map.addListener('bounds_changed', function() {\n" + "var bounds = map.getBounds();\n"
				+ "app.onMapboundsChanged(JSON.stringify(bounds.getNorthEast()), JSON.stringify(bounds.getSouthWest()));\n"
				+ "});\n" + "app.initCompleted();" + "}\n" +

				"function addClickListener(clickedMarker, name) {\n"
				+ "	clickedMarker.addListener('click', function() {\n" + "app.onMarkerClick(name);" + "	});\n" + "}\n" +

				"function clearMarkers() {\n" + "for (var i = 0; i < markers.length; i++) {\n"
				+ "markers[i].setMap(null);\n" + "}\n" + "}\n" +

				"function removeMarkers() {\n" + "clearMarkers();\n" + "markers = [];\n" + "}";
	}

	private String getCssStyle() {
		return "html, body { " + "height: 100%; " + "margin: 0; " + "padding: 0; }" + "#" + getBodyDiv()
				+ " { height: 100% }";
	}

	private ObjectMapper getJsonMapper() {
		if (jsonMapper == null) {
			jsonMapper = new ObjectMapper();
			jsonMapper.setSerializationInclusion(Include.NON_NULL);
			jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		return jsonMapper;
	}

	private String asJavascriptValue(JSMap map) throws JsonProcessingException {
		StringBuilder builder = new StringBuilder();
		builder.append("").append(map.getName()).append(" = ").append("new google.maps.Map(document.getElementById('")
				.append(getBodyDiv()).append("'), ").append(jsonize(map.getMapOptions())).append(");");
		return builder.toString();
	}

	private String jsonize(Object object) throws JsonProcessingException {
		String json = getJsonMapper().writeValueAsString(object);
		return json.replaceAll("\"", "");
	}

	private String asJavascriptValues(List<JSMarker> list) {
		StringBuilder builder = new StringBuilder();
		for (JSMarker marker : list) {
			builder.append(asJavascriptValue(marker));
		}
		return builder.toString();
	}

	private String asJavascriptValue(JSMarker marker) {
		StringBuilder builder = new StringBuilder();
		try {
			builder.append("var ").append(marker.getName()).append(" = ").append("new google.maps.Marker(")
					.append(jsonize(marker.getMarkerOptions())).append(");");
		} catch (JsonProcessingException exc) {
			myLogger.error("JsonProcessingException", exc);
		}
		return builder.toString();
	}

	@Override
	protected void callbackLoadSucceeded() {
		// do nothing. call when map was initialized
	}

	@Override
	protected void finalize() throws Throwable {
		scriptExecutor.shutdown = true;
		super.finalize();
	}

	public class JSEventReceiver {

		public void onMarkerClick(final String name) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					myLogger.info("Marker " + name + " was clicked!");
					for (IMapEventListener listener : getMapEventListener()) {
						listener.onMarkerClick(name);
					}
				}
			});
		}

		public void initCompleted() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					getBrowserLoadCallback().succeeded();
				}
			});
		}

		public void onMapboundsChanged(final String northEastLatLng, final String southWestLatLng) {
//			SwingUtilities.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						JSLatLng northEast = getJsonMapper().readValue(northEastLatLng, JSLatLng.class);
//						JSLatLng southWest = getJsonMapper().readValue(southWestLatLng, JSLatLng.class);
//						myLogger.info("Bounds changed to northEast = " + northEast.getLat() + ", " + northEast.getLng() 
//						+ " and southWest = " + southWest.getLat() + ", " + southWest.getLng());
//					} catch (JsonParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (JsonMappingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			});
		}
	}

	@Override
	public void addMarker(final JSMarker marker) {
		Platform.runLater(new Runnable() {
			public void run() {
				addMarkerImpl(marker);
			}
		});
	}

	private void executeScript(String script) {
		scriptExecutor.scheduleExecute(script);
	}

	private void addMarkerImpl(JSMarker marker) {
		executeScript(createAddMarkerScript(marker));
		executeScript(createAddClickListener(marker));
	}

	private String createAddMarkerScript(JSMarker marker) {
		return asJavascriptValue(marker) + "markers.push(" + marker.getName() + ");";
	}

	private String createAddClickListener(JSMarker marker) {
		return "addClickListener(" + marker.getName() + ", '" + marker.getName() + "');";
	}

	@Override
	public void addMarker(final List<JSMarker> markers) {
		Platform.runLater(new Runnable() {
			public void run() {
				for (JSMarker current : markers) {
					addMarkerImpl(current);
				}
			}
		});
	}

	@Override
	public void removeAllMarkers() {
		Platform.runLater(new Runnable() {
			public void run() {
				executeScript("removeMarkers()");
			}
		});
	}

	@Override
	public void setMapOptions(final JSMapOptions mapOptions) {
		map.setMapOptions(mapOptions);
		Platform.runLater(new Runnable() {
			public void run() {
				try {
					executeScript("map.setOptions(" + jsonize(mapOptions) + ");");
				} catch (Exception e) {
					myLogger.error("Exception", e);
				}
			}
		});
	}

	private class ScriptExecutor extends Thread {
		private ConcurrentLinkedQueue<String> scriptQueue;
		private boolean loaded = false;
		private boolean shutdown = false;

		private static final int POLLING_INTERVAL = 1000;

		public ScriptExecutor() {
			scriptQueue = new ConcurrentLinkedQueue<String>();
		}

		public void scheduleExecute(String script) {
			scriptQueue.add(script);
			synchronized (this) {
				this.notify();
			}
		}

		public void mapLoaded() {
			loaded = true;
			synchronized (this) {
				this.notify();
			}
		}

		private void executeScript(String script) {
			try {
				// Wait until the map is loaded
				getWebEngine().executeScript(script);
			} catch (Exception e) {
				myLogger.error("Error during execution of script '" + script + "'", e);
			}
		}

		@Override
		public void run() {
			// Wait until loaded
			while (!loaded) {
				try {
					synchronized (this) {
						this.wait();
					}
				} catch (InterruptedException e) {
				}
			}
			while (!shutdown) {
				if (!scriptQueue.isEmpty()) {
					runScripts();
				}
				try {
					synchronized(this) {
						this.wait(POLLING_INTERVAL);
					}
				} catch (InterruptedException e) {
				}
			}
		}

		private void runScripts() {
			Platform.runLater(() -> {
				String script;
				while ((script = scriptQueue.poll()) != null) {
					executeScript(script);
				}
			});
		}
	}
}
