package com.lp.client.geodaten;

public class JSMapOptions {

	private Integer zoom;
	private Boolean zoomScrollwheel;
	private Boolean zoomControl;
	private Integer maxZoom;
	private Integer minZoom;
	private JSLatLng center;
	private Boolean fullscreenControl;
	private Boolean streetViewControl;
	private Boolean disableDefaultUI;
	
	public JSMapOptions(JSLatLng center, Integer zoom) {
		setCenter(center);
		setZoom(zoom);
	}

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}

	public Boolean getZoomScrollwheel() {
		return zoomScrollwheel;
	}

	public void setZoomScrollwheel(Boolean zoomScrollwheel) {
		this.zoomScrollwheel = zoomScrollwheel;
	}

	public Boolean getZoomControl() {
		return zoomControl;
	}

	public void setZoomControl(Boolean zoomControl) {
		this.zoomControl = zoomControl;
	}

	public Integer getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(Integer maxZoom) {
		this.maxZoom = maxZoom;
	}

	public Integer getMinZoom() {
		return minZoom;
	}

	public void setMinZoom(Integer minZoom) {
		this.minZoom = minZoom;
	}

	public JSLatLng getCenter() {
		return center;
	}

	public void setCenter(JSLatLng center) {
		this.center = center;
	}

	public Boolean getFullscreenControl() {
		return fullscreenControl;
	}

	public void setFullscreenControl(Boolean fullscreenControl) {
		this.fullscreenControl = fullscreenControl;
	}

	public Boolean getStreetViewControl() {
		return streetViewControl;
	}

	public void setStreetViewControl(Boolean streetViewControl) {
		this.streetViewControl = streetViewControl;
	}

	public Boolean getDisableDefaultUI() {
		return disableDefaultUI;
	}
	
	public void setDisableDefaultUI(Boolean disableDefaultUI) {
		this.disableDefaultUI = disableDefaultUI;
	}
}
