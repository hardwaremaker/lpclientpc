package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.pc.LPMain;

public class PanelAutoJobMahnen implements IPanelAutoJobDetails {

	private AutoJobMahnenCtrl panelCtrl;

	private WrapperRadioButton wrbAutoMahnAB = null;
	private WrapperRadioButton wrbAutoMahnLiefer = null;
	private WrapperRadioButton wrbAutoMahnABundLiefer = null;
	private ButtonGroup bgAutoMahnArt = null;
	
	public PanelAutoJobMahnen(AutoJobMahnenCtrl panelCtrl) {
		setPanelCtrl(panelCtrl);
	}
	
	public void setPanelCtrl(AutoJobMahnenCtrl panelCtrl) {
		this.panelCtrl = panelCtrl;
	}
	
	public AutoJobMahnenCtrl getPanelCtrl() {
		return panelCtrl;
	}

	private void initComponents() {
		wrbAutoMahnAB = new WrapperRadioButton(LPMain.getTextRespectUISPr(
				"bes.mahnwesen.radiobutton2"));
		wrbAutoMahnLiefer = new WrapperRadioButton(LPMain.getTextRespectUISPr(
				"bes.mahnwesen.radiobutton1"));
		wrbAutoMahnABundLiefer = new WrapperRadioButton(LPMain.getTextRespectUISPr(
				"bes.mahnwesen.radiobutton3"));
		bgAutoMahnArt = new ButtonGroup();
		bgAutoMahnArt.add(wrbAutoMahnLiefer);
		bgAutoMahnArt.add(wrbAutoMahnAB);
		bgAutoMahnArt.add(wrbAutoMahnABundLiefer);
	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();
		
		jPanelWorkingOn.add(wrbAutoMahnLiefer,
				new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		jPanelWorkingOn.add(wrbAutoMahnAB,
				new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		jPanelWorkingOn.add(wrbAutoMahnABundLiefer,
				new GridBagConstraints(5, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		return ++iZeile;
	}

	@Override
	public void loadDetails() throws Throwable {
		if(getPanelCtrl().getJobDetailsDto().getBAbMahnen() == null){
			getPanelCtrl().getJobDetailsDto().setBAbMahnen(false);
		}
		if(getPanelCtrl().getJobDetailsDto().getBLieferMahnen() == null){
			getPanelCtrl().getJobDetailsDto().setBLieferMahnen(false);
		}
		if(getPanelCtrl().getJobDetailsDto().getBAbMahnen()){
			if(getPanelCtrl().getJobDetailsDto().getBLieferMahnen()){
				wrbAutoMahnABundLiefer.setSelected(true);
			} else {
				wrbAutoMahnAB.setSelected(true);
			}
		} else {
			if(getPanelCtrl().getJobDetailsDto().getBLieferMahnen()){
				wrbAutoMahnLiefer.setSelected(true);
			}
		}
	}

	@Override
	public void saveDetails() throws Throwable {
		if(wrbAutoMahnAB.isSelected()){
			getPanelCtrl().getJobDetailsDto().setBAbMahnen(true);
			getPanelCtrl().getJobDetailsDto().setBLieferMahnen(false);
		}
		else if(wrbAutoMahnABundLiefer.isSelected()){
			getPanelCtrl().getJobDetailsDto().setBAbMahnen(true);
			getPanelCtrl().getJobDetailsDto().setBLieferMahnen(true);
		}
		else if(wrbAutoMahnLiefer.isSelected()){
			getPanelCtrl().getJobDetailsDto().setBAbMahnen(false);
			getPanelCtrl().getJobDetailsDto().setBLieferMahnen(true);
		}
		
		getPanelCtrl().save();
	}

}
