package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ServerDruckerFac;

public class PanelAutoJobBestellvorschlag implements IPanelAutoJobDetails {

	private AutoJobBestellvorschlagCtrl panelCtrl;

	private WrapperLabel wlaAutoBestDrucker = null;
	private WrapperComboBox wcbAutoBestDrucker = null;
	
	public PanelAutoJobBestellvorschlag(AutoJobBestellvorschlagCtrl panelCtrl) {
		setPanelCtrl(panelCtrl);
	}

	public void setPanelCtrl(AutoJobBestellvorschlagCtrl panelCtrl) {
		this.panelCtrl = panelCtrl;
	}
	
	public AutoJobBestellvorschlagCtrl getPanelCtrl() {
		return panelCtrl;
	}
	
	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();
		
		jPanelWorkingOn.add(wlaAutoBestDrucker,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		jPanelWorkingOn.add(wcbAutoBestDrucker,
				new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));

		return ++iZeile;
	}

	private void initComponents() {
		wlaAutoBestDrucker = new WrapperLabel(LPMain.getTextRespectUISPr("label.drucker"));
		wcbAutoBestDrucker = new WrapperComboBox();
	}

	@Override
	public void loadDetails() throws Throwable {
		String[] printers = DelegateFactory.getInstance()
				.getServerDruckerDelegate().getServerDrucker();
		wcbAutoBestDrucker.removeAllItems();
		for (int i = 0; i < printers.length; i++) {
			wcbAutoBestDrucker.addItem(printers[i]);
		}
		wcbAutoBestDrucker.addItem(ServerDruckerFac.NICHT_DRUCKEN);
		if (getPanelCtrl().getJobDetailsDto().getCDrucker() == null) {
			wcbAutoBestDrucker.setSelectedItem(DelegateFactory
					.getInstance().getServerDruckerDelegate()
					.getServerStandarddrucker());
		} else {
			wcbAutoBestDrucker.setSelectedItem(getPanelCtrl().getJobDetailsDto()
					.getCDrucker());
		}
	}

	@Override
	public void saveDetails() throws Throwable {
		getPanelCtrl().getJobDetailsDto().setCDrucker((String) wcbAutoBestDrucker.getSelectedItem());
		getPanelCtrl().save();
	}

}
