package com.lp.client.frame.maps;

import java.util.List;

import javafx.application.Platform;

import javax.swing.JComponent;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.geodaten.BrowserLoadCallback;
import com.lp.client.geodaten.JSMap;
import com.lp.client.geodaten.JSMarker;

public class BrowserMock extends MapsBrowser {

	public BrowserMock(InternalFrame internalFrame, JComponent wrappingComponent) {
		super(internalFrame, wrappingComponent, null);
	}

	public void loadContent(BrowserLoadCallback loadCallback) {
		setBrowserLoadCallback(loadCallback);
		Platform.runLater(new Runnable() {
			public void run() {
				try {
					myLogger.info("Loading web content...");
					String content = buildTextHtml("Hello World");
					getWebEngine().loadContent(content);
					myLogger.info("Html content\n" + content);
//					getWebEngine().load("http://www.oracle.com/products/index.html");
				} catch (Throwable e) {
					myLogger.error("Error loading web content", e);
				}
			}
		});
	}

	private String buildTextHtml(String text) {
		HtmlBuilder htmlBuilder = new HtmlBuilder();
		return htmlBuilder.css(getCssStyleText())
				.body(text)
//				.bodyDivWrapper(getBodyDiv())
				.build();
	}
	
	private String getCssStyleText() {
		return "html, body { "
				+ "height: 100%; "
				+ "margin: 0; "
				+ "padding: 0; "
				+ "background-color: #dddddd }";
//				+ "#" + getBodyDiv() + " { height: 100% }";
	}

	@Override
	public void addMarker(JSMarker marker) {
	}

	@Override
	public void addMarker(List<JSMarker> marker) {
	}

	@Override
	public void removeAllMarkers() {
	}

	@Override
	public JSMap getMap() {
		return null;
	}

}
