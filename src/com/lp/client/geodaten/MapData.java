package com.lp.client.geodaten;

import java.util.List;

public class MapData {
	
	private List<MapPosition> positions;
	
	public MapData(List<MapPosition> positions) {
		setPositions(positions);
	}
	
	public List<MapPosition> getPositions() {
		return positions;
	}
	
	public void setPositions(List<MapPosition> positions) {
		this.positions = positions;
	}
}
