package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ServerDruckerFac;

public class PanelAutoJobFehlmengendruck implements IPanelAutoJobDetails {

	private AutoJobFehlmengendruckCtrl panelCtrl;
	
	private WrapperLabel wlaAutoFehlmengen = null;
	private WrapperComboBox wcbAutoFehlmengen = null;
	
	public PanelAutoJobFehlmengendruck(AutoJobFehlmengendruckCtrl panelCtrl) {
		setPanelCtrl(panelCtrl);
	}
	
	public AutoJobFehlmengendruckCtrl getPanelCtrl() {
		return panelCtrl;
	}
	
	public void setPanelCtrl(AutoJobFehlmengendruckCtrl panelCtrl) {
		this.panelCtrl = panelCtrl;
	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();
		
		jPanelWorkingOn.add(wlaAutoFehlmengen,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		jPanelWorkingOn.add(wcbAutoFehlmengen,
				new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		return ++iZeile;
	}

	private void initComponents() {
		wlaAutoFehlmengen = new WrapperLabel(LPMain.getTextRespectUISPr("label.drucker"));
		wcbAutoFehlmengen = new WrapperComboBox();
	}
	
	@Override
	public void loadDetails() throws Throwable {
		String[] printers = DelegateFactory.getInstance()
				.getServerDruckerDelegate().getServerDrucker();
		wcbAutoFehlmengen.removeAllItems();
		for (int i = 0; i < printers.length; i++) {
			wcbAutoFehlmengen.addItem(printers[i]);
		}
		wcbAutoFehlmengen.addItem(ServerDruckerFac.NICHT_DRUCKEN);
		if (getPanelCtrl().getJobDetailsDto().getCDrucker() == null) {
			wcbAutoFehlmengen.setSelectedItem(DelegateFactory.getInstance()
					.getServerDruckerDelegate().getServerStandarddrucker());
		} else {
			wcbAutoFehlmengen.setSelectedItem(getPanelCtrl().getJobDetailsDto()
					.getCDrucker());
		}
	}

	@Override
	public void saveDetails() throws Throwable {
		getPanelCtrl().getJobDetailsDto().setCDrucker((String) wcbAutoFehlmengen
				.getSelectedItem());
		getPanelCtrl().save();
	}

}
