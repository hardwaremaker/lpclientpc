package com.lp.client.frame;

import javax.swing.JPanel;

public class HvLayoutFactory {
	public static HvLayout create(JPanel panel) {
		return new HvLayout(panel) ;
	}

	public static HvLayout create(JPanel panel, String layoutConstraints) {
		return new HvLayout(panel, layoutConstraints) ;
	}

	public static HvLayout create(JPanel panel, String layoutConstraints, String columnConstraints, String rowConstraints) {
		return new HvLayout(panel, layoutConstraints, columnConstraints, rowConstraints) ;
	}

	public static HvLayout createLeftInset(JPanel panel) {
		return create(panel, "insets 0 10 0 0") ;
	}

	public static HvLayout createWithoutInset(JPanel panel) {
		return create(panel, "insets 0 0 0 0") ;
	}
}
