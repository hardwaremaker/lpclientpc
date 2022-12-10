package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ServerDruckerFac;

public class PanelAutoJobRahmendetailbedarf implements IPanelAutoJobDetails {

	private AutoJobRahmendetailbedarfCtrl panelCtrl;

	private WrapperLabel wlaAutoRahmendetrailbedarfDrucker = null;
	private WrapperLabel wlaAutoRahmendetailbedarfSortierung = null;
	private WrapperComboBox wcbAutoRahmendetrailbedarfDrucker = null;
	private WrapperRadioButton wrbAutoRahmendetailbedarfdruckSortArtikel = null;
	private WrapperRadioButton wrbAutoRahmendetailbedarfdruckSortLieferant = null;
	private ButtonGroup bgAutoRahmendetailbedarfdruckSort = null;
	
	public PanelAutoJobRahmendetailbedarf(AutoJobRahmendetailbedarfCtrl panelCtrl) {
		setPanelCtrl(panelCtrl);
	}
	
	public AutoJobRahmendetailbedarfCtrl getPanelCtrl() {
		return panelCtrl;
	}
	
	public void setPanelCtrl(AutoJobRahmendetailbedarfCtrl panelCtrl) {
		this.panelCtrl = panelCtrl;
	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();
		
		jPanelWorkingOn.add(wlaAutoRahmendetrailbedarfDrucker,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		jPanelWorkingOn.add(wcbAutoRahmendetrailbedarfDrucker,
				new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaAutoRahmendetailbedarfSortierung,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wrbAutoRahmendetailbedarfdruckSortArtikel,
				new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		jPanelWorkingOn.add(wrbAutoRahmendetailbedarfdruckSortLieferant,
				new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));

		return ++iZeile;
	}

	private void initComponents() {
		wlaAutoRahmendetrailbedarfDrucker = new WrapperLabel(LPMain.getTextRespectUISPr(
				"label.drucker"));
		wlaAutoRahmendetailbedarfSortierung = new WrapperLabel(LPMain.getTextRespectUISPr(
				"label.sortierung"));
		wcbAutoRahmendetrailbedarfDrucker = new WrapperComboBox();
		wrbAutoRahmendetailbedarfdruckSortArtikel = new WrapperRadioButton(LPMain.getTextRespectUISPr(
				"artikel.artikelnummer"));
		wrbAutoRahmendetailbedarfdruckSortLieferant = new WrapperRadioButton(LPMain.getTextRespectUISPr(
				"label.lieferant"));
		bgAutoRahmendetailbedarfdruckSort = new ButtonGroup();
		bgAutoRahmendetailbedarfdruckSort.add(wrbAutoRahmendetailbedarfdruckSortArtikel);
		bgAutoRahmendetailbedarfdruckSort.add(wrbAutoRahmendetailbedarfdruckSortLieferant);
	}

	@Override
	public void loadDetails() throws Throwable {
		String[] printers = DelegateFactory.getInstance().getServerDruckerDelegate().getServerDrucker();
		wcbAutoRahmendetrailbedarfDrucker.removeAllItems();
		for(int i=0;i<printers.length;i++){
			wcbAutoRahmendetrailbedarfDrucker.addItem(printers[i]);
		}
		wcbAutoRahmendetrailbedarfDrucker.addItem(ServerDruckerFac.NICHT_DRUCKEN);
		if(getPanelCtrl().getJobDetailsDto().getCDrucker()==null){
			wcbAutoRahmendetrailbedarfDrucker.setSelectedItem(DelegateFactory.getInstance().getServerDruckerDelegate().getServerStandarddrucker());
		} else {
			wcbAutoRahmendetrailbedarfDrucker.setSelectedItem(getPanelCtrl().getJobDetailsDto().getCDrucker());
		}
		if(getPanelCtrl().getJobDetailsDto().getBSortiertnachArtikel()==null){
			getPanelCtrl().getJobDetailsDto().setBSortiertnachArtikel(true);
		}
		if(getPanelCtrl().getJobDetailsDto().getBSortiertnachArtikel()){
			wrbAutoRahmendetailbedarfdruckSortArtikel.setSelected(true);
		} else {
			wrbAutoRahmendetailbedarfdruckSortLieferant.setSelected(true);
		}
	}

	@Override
	public void saveDetails() throws Throwable {
		getPanelCtrl().getJobDetailsDto().setCDrucker((String) wcbAutoRahmendetrailbedarfDrucker.getSelectedItem());
		if(wrbAutoRahmendetailbedarfdruckSortArtikel.isSelected()){
			getPanelCtrl().getJobDetailsDto().setBSortiertnachArtikel(true);
		} else {
			getPanelCtrl().getJobDetailsDto().setBSortiertnachArtikel(false);
		}
	}

}
