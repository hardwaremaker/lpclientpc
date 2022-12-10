package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.JobDetailsArbeitszeitstatusDto;

public class PanelAutoJobArbeitszeitstatus implements IPanelAutoJobDetails {

	private IAutoJobDetailsPanelCtrl<JobDetailsArbeitszeitstatusDto> ctrl;
	
	private WrapperLabel wlaPDFPath = null;
	private WrapperTextField wtfPDFPath = null;
	private WrapperLabel wlaTageBisStichtag = null;
	private WrapperNumberField wtfTageBisStichtag = null;
	private WrapperLabel wlaArchivierungstage = null;
	private WrapperNumberField wtfArchivierungstage = null;
	private WrapperLabel wlaEmail;
	private WrapperTextField wtfEmail;
	
	public PanelAutoJobArbeitszeitstatus(IAutoJobDetailsPanelCtrl<JobDetailsArbeitszeitstatusDto> ctrl) {
		setPanelCtrl(ctrl);
	}
	
	public void setPanelCtrl(IAutoJobDetailsPanelCtrl<JobDetailsArbeitszeitstatusDto> ctrl) {
		this.ctrl = ctrl;
	}
	
	public IAutoJobDetailsPanelCtrl<JobDetailsArbeitszeitstatusDto> getPanelCtrl() {
		return ctrl;
	}
	
	private void initComponents() throws ExceptionLP {
		wlaTageBisStichtag = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.tagebisstichtag"));
		wtfTageBisStichtag = new WrapperNumberField();
		wtfTageBisStichtag.setFractionDigits(0);
		wtfTageBisStichtag.setEditable(true);
		wtfTageBisStichtag.setHorizontalAlignment(SwingConstants.LEFT);
		
		wlaArchivierungstage = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.archivierungstage"));
		wtfArchivierungstage = new WrapperNumberField();
		wtfArchivierungstage.setFractionDigits(0);
		wtfArchivierungstage.setEditable(true);
		wtfArchivierungstage.setHorizontalAlignment(SwingConstants.LEFT);
		
		wlaPDFPath = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.pdfpfadpattern"));
		wtfPDFPath = new WrapperTextField();
		wtfPDFPath.setEditable(true);
		wtfPDFPath.setColumnsMax(400);
		
		wlaEmail = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.email"));
		wtfEmail = new WrapperTextField();
		wtfEmail.setEditable(true);
		wtfEmail.setColumnsMax(400);
	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();
		
		jPanelWorkingOn.add(wlaPDFPath,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfPDFPath,
				new GridBagConstraints(1, iZeile, 6, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));
		iZeile++;
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
		iZeile++;
		jPanelWorkingOn.add(wlaTageBisStichtag,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfTageBisStichtag,
				new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wlaArchivierungstage,
				new GridBagConstraints(4, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfArchivierungstage,
				new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));
		
		return ++iZeile;
	}
	
	@Override
	public void loadDetails() throws Throwable {
		wtfPDFPath.setText(getPanelCtrl().getJobDetailsDto().getCPfadPattern());
		wtfEmail.setText(getPanelCtrl().getJobDetailsDto().getCEmailEmpfaenger());
		wtfTageBisStichtag.setInteger(getPanelCtrl().getJobDetailsDto().getITageBisStichtag());
		wtfArchivierungstage.setInteger(getPanelCtrl().getJobDetailsDto().getIArchivierungstage());
	}

	@Override
	public void saveDetails() throws Throwable {
		getPanelCtrl().getJobDetailsDto().setCPfadPattern(wtfPDFPath.getText());
		getPanelCtrl().getJobDetailsDto().setCEmailEmpfaenger(wtfEmail.getText());
		getPanelCtrl().getJobDetailsDto().setITageBisStichtag(wtfTageBisStichtag.getInteger());
		getPanelCtrl().getJobDetailsDto().setIArchivierungstage(wtfArchivierungstage.getInteger());
		
		getPanelCtrl().save();
	}
}
