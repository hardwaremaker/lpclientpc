
package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.JobDetailsLumiquoteDto;

public class PanelAutoJobLumiquote implements IPanelAutoJobDetails {

	private IAutoJobDetailsPanelCtrl<JobDetailsLumiquoteDto> ctrl;

	private WrapperLabel wlaPath = null;
	private WrapperTextField wtfPath = null;
	
	private WrapperLabel wlaArtikelFilter = null;
	private WrapperTextField wtfArtikelFilter = null;
	
	public PanelAutoJobLumiquote(IAutoJobDetailsPanelCtrl<JobDetailsLumiquoteDto> ctrl) {
		setPanelCtrl(ctrl);
	}

	public void setPanelCtrl(IAutoJobDetailsPanelCtrl<JobDetailsLumiquoteDto> ctrl) {
		this.ctrl = ctrl;
	}

	public IAutoJobDetailsPanelCtrl<JobDetailsLumiquoteDto> getPanelCtrl() {
		return ctrl;
	}

	private void initComponents() throws Throwable {
	

		wlaPath = new WrapperLabel(LPMain.getTextRespectUISPr("lp.system.automatik.details.lumiquote.pfad"));
		wtfPath = new WrapperTextField();
		wtfPath.setEditable(true);
		wtfPath.setColumnsMax(400);
		
		wlaArtikelFilter= new WrapperLabel(LPMain.getTextRespectUISPr("lp.system.automatik.details.lumiquote.artikelfilter"));
		wtfArtikelFilter = new WrapperTextField();
		wtfArtikelFilter.setEditable(true);
		wtfArtikelFilter.setColumnsMax(400);

	

	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();

		jPanelWorkingOn.add(wlaPath, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfPath, new GridBagConstraints(1, iZeile, 6, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		
		
		jPanelWorkingOn.add(wlaArtikelFilter, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfArtikelFilter, new GridBagConstraints(1, iZeile, 6, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		return ++iZeile;
	}

	@Override
	public void loadDetails() throws Throwable {
		wtfPath.setText(getPanelCtrl().getJobDetailsDto().getCPfad());
		wtfArtikelFilter.setText(getPanelCtrl().getJobDetailsDto().getCArtikelfilter());
		
	}

	@Override
	public void saveDetails() throws Throwable {
		getPanelCtrl().getJobDetailsDto().setCPfad(wtfPath.getText());
		getPanelCtrl().getJobDetailsDto().setCArtikelfilter(wtfArtikelFilter.getText());

		getPanelCtrl().save();
	}

}
