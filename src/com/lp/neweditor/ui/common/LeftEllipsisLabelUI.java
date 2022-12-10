package com.lp.neweditor.ui.common;

import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

import com.lp.util.Helper;

public class LeftEllipsisLabelUI extends BasicLabelUI {
	@Override
	protected String layoutCL(JLabel label, FontMetrics fontMetrics, String text, Icon icon, Rectangle viewR,
			Rectangle iconR, Rectangle textR) {
		return Helper.reverseString(
				super.layoutCL(label, fontMetrics, Helper.reverseString(text), icon, viewR, iconR, textR));
	}
}