package com.lp.client.util;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Hilfsklasse um einfacher GridBagConstraints zu erstellen. Alle Parameter
 * k&ounl;nnen &uuml;ber setter Methoden geändert werden, die mit
 * Method-chaining verbunden werden k&ouml;nnen. <br>
 * Alle Felder haben sinnvolle default-werte. <br>
 * Der Builder kann f&uuml;r mehrere GridBagConstraints verwendet werden, indem
 * build() &ouml;fter aufgerufen wird.
 * 
 * @author Alexander Daum
 *
 */
public class GridBagConstraintBuilder implements Cloneable {

	private int gridx, gridy;
	private int gridwidth, gridheight;
	private double weightx, weighty;
	private int anchor;
	private int fill;
	private Insets insets;
	private int ipadx, ipady;

	public GridBagConstraintBuilder() {
		gridx = 0;
		gridy = 0;
		gridwidth = 1;
		gridheight = 1;
		weightx = 0.0;
		weighty = 0.0;
		anchor = GridBagConstraints.CENTER;
		fill = GridBagConstraints.HORIZONTAL;
		insets = new Insets(2, 2, 2, 2);
		ipadx = 0;
		ipady = 0;
	}

	public GridBagConstraints build() {
		return new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill,
				(Insets) insets.clone(), ipadx, ipady);
	}

	public int getX() {
		return gridx;
	}

	public GridBagConstraintBuilder x(int gridx) {
		this.gridx = gridx;
		return this;
	}

	public GridBagConstraintBuilder incX() {
		gridx++;
		return this;
	}

	public int getY() {
		return gridy;
	}

	public GridBagConstraintBuilder y(int gridy) {
		this.gridy = gridy;
		return this;
	}

	public GridBagConstraintBuilder incY() {
		gridy++;
		return this;
	}

	public int getWidth() {
		return gridwidth;
	}

	public GridBagConstraintBuilder width(int gridwidth) {
		this.gridwidth = gridwidth;
		return this;
	}

	public int getHeight() {
		return gridheight;
	}

	public GridBagConstraintBuilder height(int gridheight) {
		this.gridheight = gridheight;
		return this;
	}

	public double getWeightX() {
		return weightx;
	}

	public GridBagConstraintBuilder weightX(double weightx) {
		this.weightx = weightx;
		return this;
	}

	public double getWeightY() {
		return weighty;
	}

	public GridBagConstraintBuilder weightY(double weighty) {
		this.weighty = weighty;
		return this;
	}

	public GridBagConstraintBuilder weight(double weightX, double weightY) {
		this.weightx = weightX;
		this.weighty = weightY;
		return this;
	}

	public int getAnchor() {
		return anchor;
	}

	public GridBagConstraintBuilder anchor(int anchor) {
		this.anchor = anchor;
		return this;
	}

	public int getFill() {
		return fill;
	}

	public GridBagConstraintBuilder fill(int fill) {
		this.fill = fill;
		return this;
	}

	public Insets getInsets() {
		return insets;
	}

	public GridBagConstraintBuilder insets(Insets insets) {
		this.insets = insets;
		return this;
	}

	public GridBagConstraintBuilder insets(int top, int left, int bottom, int right) {
		this.insets = new Insets(top, left, bottom, right);
		return this;
	}
	public GridBagConstraintBuilder insets(int inset) {
		this.insets = new Insets(inset, inset, inset, inset);
		return this;
	}

	public int getPadX() {
		return ipadx;
	}

	public GridBagConstraintBuilder padX(int ipadx) {
		this.ipadx = ipadx;
		return this;
	}

	public int getPadY() {
		return ipady;
	}

	public GridBagConstraintBuilder padY(int ipady) {
		this.ipady = ipady;
		return this;
	}

	@Override
	public GridBagConstraintBuilder clone() {
		try {
			return (GridBagConstraintBuilder) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
