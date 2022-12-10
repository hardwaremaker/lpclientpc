package com.lp.client.geodaten;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class JSMarkerOptions {

	private JSVariable map;
	private JSLatLng position;
	
	/**
	 * Spezialisierter Serializer
	 * Hintergrund: Bei einem Namen mit ' (Beispiel: Alexandra's Blumenwiese)
	 * (und ja, sic! da gehoert eigentlich kein ' hin)
	 * wird bei einem Standard-Json-Stringserializer ein "Alexandra's Blumenwiese".
	 * Wir wollen aber 'Alexandra\'s Blumenwiese' haben.
	 * 
	 * Wenn wir nun also uns einen JavaString selbst zusammenbauen, der \' enthaelt,
	 * erkennt der Serializer das '\' und escaped das, womit wir nun serialized
	 * output \\' haben. Und genau das wollen wir ja nicht.
	 * 
	 * Ausserdem handelt es sich um einen JSON Serializier. Dort sind auch die
	 * property-namen in mit " zu umschliessen. Letztendlich brauchen wir aber
	 * Javascript properties, und die benoetigen die umschliessenden " nicht.
	 * 
	 */
    @JsonSerialize(using = SingleQuoteStringSerializer.class)   
	private String title;
	private Boolean visible;
	private String iconPath;
	
	public JSMarkerOptions(JSVariable map, JSLatLng position) {
		setMap(map);
		setPosition(position);
	}

	public void setMap(JSVariable map) {
		this.map = map;
	}
	
	@JsonIgnore
	public JSVariable getMap() {
		return map;
	}
	
	public void setPosition(JSLatLng position) {
		this.position = position;
	}
	
	public JSLatLng getPosition() {
		return position;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
		
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	
	public Boolean getVisible() {
		return visible;
	}
	
	@JsonProperty("map")
	public String getMapName() {
		return getMap().getName();
	}
	
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	@JsonIgnore
	public String getIconPath() {
		return iconPath;
	}

	@JsonProperty("icon")
	public String getIconPathJson() {
		return getIconPath() != null ? "'" + getIconPath() + "'" : null;
	}
}
