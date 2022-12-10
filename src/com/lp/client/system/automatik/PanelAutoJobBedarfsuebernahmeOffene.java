package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.JobDetailsBedarfsuebernahmeOffeneDto;

public class PanelAutoJobBedarfsuebernahmeOffene implements IPanelAutoJobDetails {

	private IAutoJobDetailsPanelCtrl<JobDetailsBedarfsuebernahmeOffeneDto> ctrl;
	
	private WrapperLabel wlaEmail;
	private WrapperTextField wtfEmail;

	public PanelAutoJobBedarfsuebernahmeOffene(IAutoJobDetailsPanelCtrl<JobDetailsBedarfsuebernahmeOffeneDto> ctrl) {
		this.ctrl = ctrl;
	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();
		
		jPanelWorkingOn.add(wlaEmail,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfEmail,
				new GridBagConstraints(1, iZeile, 6, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));
		
		return ++iZeile;
	}

	private void initComponents() {
		wlaEmail = new WrapperLabel(LPMain.getTextRespectUISPr("lp.system.automatik.details.email"));
		wtfEmail = new WrapperTextField();
		wtfEmail.setEditable(true);
		wtfEmail.setColumnsMax(400);
	}
	@Override
	public void loadDetails() throws Throwable {
		wtfEmail.setText(ctrl.getJobDetailsDto().getCEmailEmpfaenger());
	}

	@Override
	public void saveDetails() throws Throwable {
		ctrl.getJobDetailsDto().setCEmailEmpfaenger(wtfEmail.getText());
		
		ctrl.save();
	}

}
