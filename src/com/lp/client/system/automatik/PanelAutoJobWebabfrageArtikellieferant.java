package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.JobDetailsWebabfrageArtikellieferantDto;

public class PanelAutoJobWebabfrageArtikellieferant implements IPanelAutoJobDetails {

	private IAutoJobDetailsPanelCtrl<JobDetailsWebabfrageArtikellieferantDto> panelCtrl;
	private WrapperLabel wlaEmail;
	private WrapperTextField wtfEmail;
	
	public PanelAutoJobWebabfrageArtikellieferant(IAutoJobDetailsPanelCtrl<JobDetailsWebabfrageArtikellieferantDto> panelCtrl) {
		setPanelCtrl(panelCtrl);
	}

	public void setPanelCtrl(IAutoJobDetailsPanelCtrl<JobDetailsWebabfrageArtikellieferantDto> panelCtrl) {
		this.panelCtrl = panelCtrl;
	}
	
	public IAutoJobDetailsPanelCtrl<JobDetailsWebabfrageArtikellieferantDto> getPanelCtrl() {
		return panelCtrl;
	}
	
	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		wlaEmail = new WrapperLabel(LPMain.getTextRespectUISPr("lp.system.automatik.details.webabfrageartlief.email"));
		wtfEmail = new WrapperTextField(80);
		wtfEmail.setEditable(true);
		
		jPanelWorkingOn.add(wlaEmail,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfEmail,
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		
		return ++iZeile;
	}
	
	private JobDetailsWebabfrageArtikellieferantDto detailsDto() throws Throwable {
		return getPanelCtrl().getJobDetailsDto();
	}

	@Override
	public void loadDetails() throws Throwable {
		wtfEmail.setText(detailsDto().getCEmailErfolgreich());
	}

	@Override
	public void saveDetails() throws Throwable {
		detailsDto().setCEmailErfolgreich(wtfEmail.getText());
		getPanelCtrl().save();
	}

}
