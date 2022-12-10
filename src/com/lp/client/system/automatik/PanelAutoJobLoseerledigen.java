package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.pc.LPMain;

public class PanelAutoJobLoseerledigen implements IPanelAutoJobDetails {

	private AutoJobLoseerledigenCtrl panelCtrl;

	private WrapperLabel wlaArbeitstage = null;
	private WrapperNumberField wnfArbeitstage = null;

	public PanelAutoJobLoseerledigen(AutoJobLoseerledigenCtrl panelCtrl) {
		setPanelCtrl(panelCtrl);
	}

	public void setPanelCtrl(AutoJobLoseerledigenCtrl panelCtrl) {
		this.panelCtrl = panelCtrl;
	}

	public AutoJobLoseerledigenCtrl getPanelCtrl() {
		return panelCtrl;
	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();

		jPanelWorkingOn.add(wlaArbeitstage, new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfArbeitstage, new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		return ++iZeile;
	}

	private void initComponents() throws Throwable {
		wlaArbeitstage = new WrapperLabel(LPMain.getTextRespectUISPr("auto.loseerledigen.arbeitstage"));
		wnfArbeitstage = new WrapperNumberField();
		wnfArbeitstage.setFractionDigits(0);
	}

	@Override
	public void loadDetails() throws Throwable {

		wnfArbeitstage.setInteger(getPanelCtrl().getJobDetailsDto().getIArbeitstage());

	}

	@Override
	public void saveDetails() throws Throwable {
		getPanelCtrl().getJobDetailsDto().setIArbeitstage(wnfArbeitstage.getInteger());
		getPanelCtrl().save();
	}

}
