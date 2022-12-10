package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.JobDetailsKpiReportDto;
import com.lp.util.Helper;

public class PanelAutoJobKpiReport implements IPanelAutoJobDetails {

	private IAutoJobDetailsPanelCtrl<JobDetailsKpiReportDto> ctrl;
	
	private WrapperLabel wlaPDFPath = null;
	private WrapperTextField wtfPDFPath = null;
	private WrapperLabel wlaStichtagHinweis = null;
	private WrapperLabel wlaStichtag = null;
	private WrapperNumberField wtfTage = null;
	private WrapperLabel wlaArchivierungstage = null;
	private WrapperNumberField wtfArchivierungstage = null;
	private WrapperLabel wlaEmailEmpfaenger = null;
	private WrapperTextField wtfEmailEmpfaenger = null;
	
	public PanelAutoJobKpiReport(IAutoJobDetailsPanelCtrl<JobDetailsKpiReportDto> ctrl) {
		setPanelCtrl(ctrl);
	}
	
	public void setPanelCtrl(IAutoJobDetailsPanelCtrl<JobDetailsKpiReportDto> ctrl) {
		this.ctrl = ctrl;
	}

	public IAutoJobDetailsPanelCtrl<JobDetailsKpiReportDto> getPanelCtrl() {
		return ctrl;
	}
	
	private void initComponents() throws Throwable {
		wlaStichtagHinweis = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.kpireport.tagehinweis"));
		wlaStichtag = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.kpireport.tage"));
		wtfTage = new WrapperNumberField();
		wtfTage.setFractionDigits(0);
		wtfTage.setEditable(true);
		wtfTage.setHorizontalAlignment(SwingConstants.LEFT);
		wtfTage.setMandatoryField(true);
		
		wlaArchivierungstage = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.kpireport.archivierungstage"));
		wtfArchivierungstage = new WrapperNumberField();
		wtfArchivierungstage.setFractionDigits(0);
		wtfArchivierungstage.setEditable(true);
		wtfArchivierungstage.setHorizontalAlignment(SwingConstants.LEFT);
		wtfArchivierungstage.setMandatoryField(true);
		
		wlaPDFPath = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.kpireport.pdfpfad"));
		wtfPDFPath = new WrapperTextField();
		wtfPDFPath.setEditable(true);
		wtfPDFPath.setColumnsMax(400);
		wtfPDFPath.setMandatoryField(true);
		
		wlaEmailEmpfaenger = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.detail.kpireport.emailempfaenger"));
		wtfEmailEmpfaenger = new WrapperTextField();
		wtfEmailEmpfaenger.setEditable(true);
		wtfEmailEmpfaenger.setColumnsMax(300);
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
		jPanelWorkingOn.add(wlaStichtagHinweis,
				new GridBagConstraints(0, iZeile, 4, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaStichtag,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfTage,
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
		iZeile++;
		jPanelWorkingOn.add(wlaEmailEmpfaenger,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfEmailEmpfaenger,
				new GridBagConstraints(2, iZeile, 5, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, 
						new Insets(2, 2, 2,	2), 0, 0));

		return ++iZeile;
	}

	@Override
	public void loadDetails() throws Throwable {
		wtfPDFPath.setText(getPanelCtrl().getJobDetailsDto().getcPfadPattern());
		wtfArchivierungstage.setText(getPanelCtrl().getJobDetailsDto().getiArchivierungstage() == null ? "" : 
			getPanelCtrl().getJobDetailsDto().getiArchivierungstage().toString());
		wtfTage.setText(getPanelCtrl().getJobDetailsDto().getiTage() == null ? "" :
			getPanelCtrl().getJobDetailsDto().getiTage().toString());
		wtfEmailEmpfaenger.setText(Helper.emptyString(getPanelCtrl().getJobDetailsDto().getcEmailEmpfaenger()));
	}

	@Override
	public void saveDetails() throws Throwable {
		getPanelCtrl().getJobDetailsDto().setcPfadPattern(wtfPDFPath.getText());
		getPanelCtrl().getJobDetailsDto().setiArchivierungstage(wtfArchivierungstage.getInteger());
		getPanelCtrl().getJobDetailsDto().setiTage(wtfTage.getInteger());
		getPanelCtrl().getJobDetailsDto().setcEmailEmpfaenger(wtfEmailEmpfaenger.getText());
		getPanelCtrl().save();
	}

}
