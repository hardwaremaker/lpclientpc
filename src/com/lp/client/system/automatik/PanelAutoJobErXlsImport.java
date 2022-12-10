package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.JobDetailsErImportDto;

public class PanelAutoJobErXlsImport implements IPanelAutoJobDetails {

	private IAutoJobDetailsPanelCtrl<JobDetailsErImportDto> panelCtrl;
	
	private WrapperLabel wlaErImportDirPath;
	private WrapperTextField wtfErImportDirPath;
	private WrapperLabel wlaErImportEmailFehler;
	private WrapperTextField wtfErImportEmailFehler;
	private WrapperLabel wlaErImportEmailErfolgreich;
	private WrapperTextField wtfErImportEmailErfolgreich;
	
	public PanelAutoJobErXlsImport(IAutoJobDetailsPanelCtrl<JobDetailsErImportDto> ctrl) {
		setPanelCtrl(ctrl);
	}

	public IAutoJobDetailsPanelCtrl<JobDetailsErImportDto> getPanelCtrl() {
		return panelCtrl;
	}
	
	public void setPanelCtrl(IAutoJobDetailsPanelCtrl<JobDetailsErImportDto> ctrl) {
		this.panelCtrl = ctrl;
	}
	
	private void initComponents() {
		wlaErImportDirPath = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.erxlsimport.importpfad"));
		wlaErImportEmailErfolgreich = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.erxlsimport.emailerfolgreich"));
		wlaErImportEmailFehler = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.erxlsimport.emailfehler"));
		
		wtfErImportDirPath = createTextField(400);
		wtfErImportEmailErfolgreich = createTextField(80);
		wtfErImportEmailFehler = createTextField(80);
	}
	
	private WrapperTextField createTextField(int maxColumns) {
		WrapperTextField wtf = new WrapperTextField();
		wtf.setEditable(true);
		wtf.setColumnsMax(maxColumns);
		return wtf;
	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();
		
		jPanelWorkingOn.add(wlaErImportDirPath,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfErImportDirPath,
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaErImportEmailErfolgreich,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfErImportEmailErfolgreich,
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaErImportEmailFehler,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfErImportEmailFehler,
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		
		return ++iZeile;
	}

	@Override
	public void loadDetails() throws Throwable {
		wtfErImportDirPath.setText(getPanelCtrl().getJobDetailsDto().getCImportPfad());
		wtfErImportEmailErfolgreich.setText(getPanelCtrl().getJobDetailsDto().getCEmailErfolgreich());
		wtfErImportEmailFehler.setText(getPanelCtrl().getJobDetailsDto().getCEmailFehler());
	}
	
	@Override
	public void saveDetails() throws Throwable {
		getPanelCtrl().getJobDetailsDto().setCImportPfad(wtfErImportDirPath.getText());
		getPanelCtrl().getJobDetailsDto().setCEmailErfolgreich(wtfErImportEmailErfolgreich.getText());
		getPanelCtrl().getJobDetailsDto().setCEmailFehler(wtfErImportEmailFehler.getText());

		getPanelCtrl().save();
	}
}
