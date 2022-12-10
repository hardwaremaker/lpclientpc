package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.AutomatikjobtypeFac;
import com.lp.server.system.service.AutomatiktimerFac;

public class PanelAutoJobHead implements IPanelAutoJobDetails {

	private IAutoJobHeadPanelCtrl headPanelCtrl;
	
	private WrapperLabel wlaName = null;
	private WrapperTextField wtfName = null;
	private WrapperCheckBox wcbActive = null;
	private WrapperCheckBox wcbNonWorkingDay = null;
	private WrapperCheckBox wcbMonthJob = null;
	private WrapperLabel wlaIntervall = null;
	private WrapperNumberField wtfIntervall = null;
	private WrapperLabel wlaBeschreibung = null;
	private WrapperTextField wtfBeschreibung = null;

	public PanelAutoJobHead(IAutoJobHeadPanelCtrl headPanelCtrl) {
		setHeadPanelCtrl(headPanelCtrl);
	}	

	public void setHeadPanelCtrl(IAutoJobHeadPanelCtrl headPanelCtrl) {
		this.headPanelCtrl = headPanelCtrl;
	}
	
	public IAutoJobHeadPanelCtrl getHeadPanelCtrl() {
		return headPanelCtrl;
	}
	
	private void initComponents() throws Throwable {
		wlaName = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.name"));
		wtfName = new WrapperTextField();
		wtfName.setEditable(false);
		wtfName.setActivatable(false);
		wcbActive = new WrapperCheckBox(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.aktiv"));
		wcbNonWorkingDay = new WrapperCheckBox(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.vorfeiertagenausfuehren"));
		wcbMonthJob = new WrapperCheckBox(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.monatsendeausfuehren"));
		wlaIntervall = new WrapperLabel(getIntervallText());
		wtfIntervall = new WrapperNumberField();
		wtfIntervall.setFractionDigits(0);
		wtfIntervall.setHorizontalAlignment(SwingConstants.LEFT);
		wlaBeschreibung = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.beschreibung"));
		wtfBeschreibung = new WrapperTextField();
		wtfBeschreibung.setEditable(false);
		wtfBeschreibung.setActivatable(false);
		wtfBeschreibung.setColumnsMax(1000);
	}

	private String getIntervallText() throws Throwable {
		if (AutomatiktimerFac.Scheduler.AUTOMATIKJOB.equals(
				getHeadPanelCtrl().getJobDto().getIScheduler())) {
			return LPMain.getTextRespectUISPr(
					"lp.system.automatik.details.intervalltage");
		}
		return LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.intervallminuten");
	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();
		
		jPanelWorkingOn.add(wlaName, new GridBagConstraints(0, iZeile, 1, 1,
				0.7, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfName, new GridBagConstraints(2, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(1, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaIntervall, new GridBagConstraints(4, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfIntervall, new GridBagConstraints(6, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaBeschreibung, new GridBagConstraints(0, iZeile,
				1, 1, 0.7, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfBeschreibung, new GridBagConstraints(2, iZeile,
				6, 1, 2.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wcbActive, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcbNonWorkingDay, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcbMonthJob, new GridBagConstraints(6, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		return ++iZeile;
	}
	
	@Override
	public void loadDetails() throws Throwable {
		wtfName.setText(getHeadPanelCtrl().getJobDto().getCName());
		wtfIntervall.setInteger(getHeadPanelCtrl().getJobDto().getIIntervall());
		wtfBeschreibung.setText(getHeadPanelCtrl().getJobDto().getCBeschreibung());
		wcbActive.setSelected(getHeadPanelCtrl().getJobDto().getBActive() == 1);
		if (getHeadPanelCtrl().getJobTypeDto().getCJobtype().equals(AutomatiktimerFac.JOBTYPE_MAILVERSAND_TYPE) ||
				getHeadPanelCtrl().getJobTypeDto().getCJobtype().equals(AutomatiktimerFac.JOBTYPE_MAILIMAPABLAGE_TYPE)) {
			// keine Optionen
			wcbMonthJob.setVisible(false);
			wcbNonWorkingDay.setVisible(false);
		}
		wcbMonthJob.setSelected(getHeadPanelCtrl().getJobDto().getBMonthjob() == 1);
		wcbNonWorkingDay.setSelected(getHeadPanelCtrl().getJobDto().getBPerformOnNonWOrkingDays() == 1);
	}
	
	@Override
	public void saveDetails() throws Throwable {
		getHeadPanelCtrl().getJobDto().setIIntervall(wtfIntervall.getInteger());
		Integer bActive = wcbActive.isSelected() ? 1 : 0;
		getHeadPanelCtrl().getJobDto().setBActive(bActive);

		Integer bMonthjob = wcbMonthJob.isSelected() ? 1 : 0;
		getHeadPanelCtrl().getJobDto().setBMonthjob(bMonthjob);
		
		Integer bPerformOnNonWorkingDays = wcbNonWorkingDay.isSelected() ? 1 : 0;
		getHeadPanelCtrl().getJobDto().setBPerformOnNonWOrkingDays(bPerformOnNonWorkingDays);
		
		getHeadPanelCtrl().save();
	}

}
