package com.lp.client.frame.report;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.pc.LPMain;
import com.lp.client.util.ClientConfiguration;

public class PanelDateRange extends JPanel {

	private static final long serialVersionUID = 7418934400479958056L;

	private static final int I_BREITE = 100;

	private JLabel wlaVon = null;
	private JLabel wlaBis = null;
	private WrapperDateField wdfVon = null;
	private WrapperDateField wdfBis = null;
	private WrapperDateRangeController wdrc = null;


	public PanelDateRange() {
		jbInit();
	}

	private void jbInit() {

		wlaVon = new JLabel();
		wlaBis = new JLabel();
		wlaVon.setText(LPMain.getTextRespectUISPr("lp.von"));
		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));

		wdfVon = new WrapperDateField();
		wdfBis = new WrapperDateField();
		wdfVon.setMandatoryField(true);
		wdfBis.setMandatoryField(true);

		wdrc = new WrapperDateRangeController(wdfVon, wdfBis);

		HvLayout hvLayout = HvLayoutFactory.createWithoutInset(this) ;

		hvLayout
			.add(wlaVon)
			.add(wdfVon, I_BREITE)
			.add(wlaBis)
			.add(wdfBis, I_BREITE)
			.add(wdrc, ClientConfiguration.getUiControlHeight());

	}
	public WrapperDateField getWdfVon() {
		return wdfVon;
	}

	public WrapperDateField getWdfBis() {
		return wdfBis;
	}

	public WrapperDateRangeController getWdrc() {
		return wdrc;
	}

}
