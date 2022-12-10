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
import com.lp.server.system.service.JobDetailsAuslieferlisteDto;

public class PanelAutoJobAuslieferliste implements IPanelAutoJobDetails {

	private IAutoJobDetailsPanelCtrl<JobDetailsAuslieferlisteDto> ctrl;

	private WrapperLabel wlaPDFPath = null;
	private WrapperTextField wtfPDFPath = null;
	private WrapperLabel wlaStichtag = null;
	private WrapperNumberField wtfTageBisStichtag = null;
	private WrapperLabel wlaArchivierungstage = null;
	private WrapperNumberField wtfArchivierungstage = null;
	private WrapperCheckBox wcbNurNachLosEndeSortiert = new WrapperCheckBox();

	public PanelAutoJobAuslieferliste(IAutoJobDetailsPanelCtrl<JobDetailsAuslieferlisteDto> ctrl) {
		setPanelCtrl(ctrl);
	}

	public void setPanelCtrl(IAutoJobDetailsPanelCtrl<JobDetailsAuslieferlisteDto> ctrl) {
		this.ctrl = ctrl;
	}

	public IAutoJobDetailsPanelCtrl<JobDetailsAuslieferlisteDto> getPanelCtrl() {
		return ctrl;
	}

	private void initComponents() throws Throwable {
		wlaStichtag = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.system.automatik.details.auslieferliste.stichtag"));
		wtfTageBisStichtag = new WrapperNumberField();
		wtfTageBisStichtag.setFractionDigits(0);
		wtfTageBisStichtag.setEditable(true);
		wtfTageBisStichtag.setHorizontalAlignment(SwingConstants.LEFT);

		wlaArchivierungstage = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.system.automatik.details.auslieferliste.archivierungstage"));
		wtfArchivierungstage = new WrapperNumberField();
		wtfArchivierungstage.setFractionDigits(0);
		wtfArchivierungstage.setEditable(true);
		wtfArchivierungstage.setHorizontalAlignment(SwingConstants.LEFT);

		wlaPDFPath = new WrapperLabel(LPMain.getTextRespectUISPr("lp.system.automatik.details.auslieferliste.pdfpfad"));
		wtfPDFPath = new WrapperTextField();
		wtfPDFPath.setEditable(true);
		wtfPDFPath.setColumnsMax(400);

		wcbNurNachLosEndeSortiert
				.setText(LPMain.getInstance().getTextRespectUISPr("fert.auslieferliste.nurlosenachendeterminsortiert"));

	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();

		jPanelWorkingOn.add(wlaPDFPath, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfPDFPath, new GridBagConstraints(1, iZeile, 6, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaStichtag, new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfTageBisStichtag, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaArchivierungstage, new GridBagConstraints(4, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfArchivierungstage, new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wcbNurNachLosEndeSortiert, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		return ++iZeile;
	}

	@Override
	public void loadDetails() throws Throwable {
		wtfPDFPath.setText(getPanelCtrl().getJobDetailsDto().getcPfadPattern());
		wtfArchivierungstage.setText(getPanelCtrl().getJobDetailsDto().getiArchivierungstage() == null ? ""
				: getPanelCtrl().getJobDetailsDto().getiArchivierungstage().toString());
		wtfTageBisStichtag.setText(getPanelCtrl().getJobDetailsDto().getiTageBisStichtag() == null ? ""
				: getPanelCtrl().getJobDetailsDto().getiTageBisStichtag().toString());
		wcbNurNachLosEndeSortiert
				.setShort(getPanelCtrl().getJobDetailsDto().getBNurLoseNachEndetermin() == null ? new Short((short)0)
						: getPanelCtrl().getJobDetailsDto().getBNurLoseNachEndetermin());
	}

	@Override
	public void saveDetails() throws Throwable {
		getPanelCtrl().getJobDetailsDto().setcPfadPattern(wtfPDFPath.getText());
		getPanelCtrl().getJobDetailsDto().setiArchivierungstage(wtfArchivierungstage.getInteger());
		getPanelCtrl().getJobDetailsDto().setiTageBisStichtag(wtfTageBisStichtag.getInteger());
		getPanelCtrl().getJobDetailsDto().setBNurLoseNachEndetermin(wcbNurNachLosEndeSortiert.getShort());

		getPanelCtrl().save();
	}

}
