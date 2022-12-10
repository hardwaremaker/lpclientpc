package com.lp.client.frame;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class HvLayout {

	public enum Align {
		LEFT("align left"),
		RIGHT("align right"),
		CENTER("align center") ;

		private Align(String alignment) {
			value = alignment ;
		}

		private String value ;
	}

	private JPanel workingPanel = null;
	private JComponent lastComponent = null ;

	public HvLayout(JPanel panel) {
		this(panel, null, null, null);
	}

	public HvLayout(JPanel panel, String layoutConstraints) {
		this(panel, layoutConstraints, null, null);
	}

	public HvLayout(JPanel panel, String layoutConstraints, String columnConstraints, String rowConstraints) {
		panel.setLayout(new MigLayout(layoutConstraints, columnConstraints, rowConstraints));
		workingPanel = panel ;
	}

	public HvLayout addEmptyColumn() {
		workingPanel.add(new JLabel());
		return this ;
	}

	public HvLayout add(JComponent component) {
		workingPanel.add(component) ;
		lastComponent = component ;
		return this ;
	}

	public HvLayout add(JComponent component, String constraint) {
		workingPanel.add(component, constraint) ;
		lastComponent = component ;
		return this ;
	}

	public HvLayout add(JComponent component, Integer width) {
		applyWidth(component, width);
		workingPanel.add(component) ;
		lastComponent = component ;
		return this ;
	}

	public HvLayout add(JComponent component, String constraint, Integer width) {
		applyWidth(component, width);
		workingPanel.add(component, constraint) ;
		return this ;
	}

	public HvLayout addAligned(Align alignment, JComponent component) {
		add(component, alignment.value) ;
		return this ;
	}

	public HvLayout addAligned(Align alignment, JComponent component, Integer width) {
		applyWidth(component, width);
		add(component, alignment.value) ;
		return this ;
	}

	public HvLayout span(Integer span) {
		workingPanel.remove(lastComponent);
		workingPanel.add(lastComponent, "span " + String.valueOf(span));
		return this ;
	}

	public HvLayout split(Integer span) {
		workingPanel.remove(lastComponent);
		workingPanel.add(lastComponent, "split " + String.valueOf(span));
		return this ;
	}

	public HvLayout wrap() {
		workingPanel.remove(lastComponent);
		workingPanel.add(lastComponent, "wrap") ;
		return this ;
	}

	public HvLayout spanAndWrap(Integer span) {
		workingPanel.remove(lastComponent);
		workingPanel.add(lastComponent, "span " + String.valueOf(span) + ", wrap") ;
		return this ;
	}

	private void applyWidth(JComponent component, Integer width) {
		Dimension d = new Dimension(Defaults.sizeFactor(width), Defaults.getInstance().getControlHeight()) ;
		component.setMinimumSize(d);
		component.setPreferredSize(d);
	}
	
	public HvLayout skip(Integer skip) {
		workingPanel.remove(lastComponent);
		workingPanel.add(lastComponent, "skip " + String.valueOf(skip));
		return this;
	}
	
	public HvLayout skipAndWrap(Integer skip) {
		workingPanel.remove(lastComponent);
		workingPanel.add(lastComponent, "skip " + String.valueOf(skip) + ", wrap");
		return this;
	}
	
	public HvLayout skipAndSpan(Integer skip, Integer span) {
		workingPanel.remove(lastComponent);
		workingPanel.add(lastComponent, "skip " + String.valueOf(skip) + ", span " + String.valueOf(span));
		return this;
	}
}
