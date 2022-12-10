package com.lp.client.angebot;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;

public class AGSchnellerfassungPositionData implements Serializable {
	
	private static final long serialVersionUID = 8549692800870893290L;
	
	private Dimension size;
	private Point location;


	double weightMaterial;
	public double getWeightMaterial() {
		return weightMaterial;
	}

	public void setWeightMaterial(double weightMaterial) {
		this.weightMaterial = weightMaterial;
	}

	public double getWeightTaetigkeit() {
		return weightTaetigkeit;
	}

	public void setWeightTaetigkeit(double weightTaetigkeit) {
		this.weightTaetigkeit = weightTaetigkeit;
	}

	public double getWeightPosition() {
		return weightPosition;
	}

	public void setWeightPosition(double weightPosition) {
		this.weightPosition = weightPosition;
	}

	public double getWeightMengenstaffel() {
		return weightMengenstaffel;
	}

	public void setWeightMengenstaffel(double weightMengenstaffel) {
		this.weightMengenstaffel = weightMengenstaffel;
	}

	public int getDividerLocation() {
		return dividerLocation;
	}

	public void setDividerLocation(int dividerLocation) {
		this.dividerLocation = dividerLocation;
	}

	double weightTaetigkeit;
	double weightPosition;
	double weightMengenstaffel;
	
	int dividerLocation;
	
	public AGSchnellerfassungPositionData() {
		
	}

	public AGSchnellerfassungPositionData(DialogAngebotpositionSchnellerfassung dialog) {
		
		setSize(dialog.getBounds().getSize());
		setLocation(dialog.getBounds().getLocation());
	}
	
	
	
	
	
	public void applyTo(DialogAngebotpositionSchnellerfassung dialog) {
		dialog.setSize(getSize());
		dialog.setLocation(getLocation());
		
	}
	
	// Getters
	public Dimension getSize() {
		return size;
	}
	
	public Point getLocation() {
		return location;
	}
	

	
	// Setters
	public void setSize(Dimension size) {
		this.size = size;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	
	
	
}
