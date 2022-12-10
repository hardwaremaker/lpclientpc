package com.lp.client.system.automatik;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperEmailField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ServerDruckerFac;

public class PanelAutoJobMahnungsversand implements IPanelAutoJobDetails {

	private static final String VERSANDART_E_MAIL = "EMAIL";
	private static final String VERSANDART_FAX = "FAX";
	private static final String VERSANDART_KEIN_VERSAND = "KEIN";
	private static final String NICHT_DRUCKEN = ServerDruckerFac.NICHT_DRUCKEN;

	private AutoJobMahnungsversandCtrl panelCtrl;
	
	private WrapperLabel wlaAutoMahnDrucker = null;
	private WrapperComboBox wcbAutoMahnDrucker = null;
	private WrapperRadioButton wrbAutoMahnVersandKein = null;
	private WrapperRadioButton wrbAutoMahnVersandMail = null;
	private WrapperRadioButton wrbAutoMahnVersandFax = null;
	private ButtonGroup bgMahnVersandAuswahl = null;
	
	private WrapperLabel wlaEmail = null;
	private WrapperEmailField wtfEmail = null;
	
	public PanelAutoJobMahnungsversand(AutoJobMahnungsversandCtrl panelCtrl) {
		setPanelCtrl(panelCtrl);
	}
	
	public void setPanelCtrl(AutoJobMahnungsversandCtrl panelCtrl) {
		this.panelCtrl = panelCtrl;
	}
	
	public AutoJobMahnungsversandCtrl getPanelCtrl() {
		return panelCtrl;
	}
	
	private void initComponents()throws Throwable {
		wlaAutoMahnDrucker = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.drucker"));
		wcbAutoMahnDrucker = new WrapperComboBox();
		wrbAutoMahnVersandKein = new WrapperRadioButton(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.nichtsenden"));
		wrbAutoMahnVersandMail = new WrapperRadioButton(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.emailsenden"));
		wrbAutoMahnVersandFax = new WrapperRadioButton(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.faxsenden"));
		bgMahnVersandAuswahl = new ButtonGroup();
		bgMahnVersandAuswahl.add(wrbAutoMahnVersandKein);
		bgMahnVersandAuswahl.add(wrbAutoMahnVersandMail);
		bgMahnVersandAuswahl.add(wrbAutoMahnVersandFax);
		
		
		wlaEmail = new WrapperLabel();
		LPMain.getInstance();
		wlaEmail.setText(LPMain.getTextRespectUISPr("auto.mahnungsversand.email"));
		wlaEmail.setMinimumSize(new Dimension(60, Defaults.getInstance()
				.getControlHeight()));
		wlaEmail.setPreferredSize(new Dimension(60, Defaults.getInstance()
				.getControlHeight()));
		wtfEmail = new WrapperEmailField();
		wtfEmail.setColumnsMax(300);
		
	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();
		
		jPanelWorkingOn.add(wlaAutoMahnDrucker,
				new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		jPanelWorkingOn.add(wcbAutoMahnDrucker,
				new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wrbAutoMahnVersandKein,
				new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		jPanelWorkingOn.add(wrbAutoMahnVersandMail,
				new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		jPanelWorkingOn.add(wrbAutoMahnVersandFax,
				new GridBagConstraints(5, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		
		iZeile++;
		jPanelWorkingOn.add(wlaEmail,
				new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		jPanelWorkingOn.add(wtfEmail,
				new GridBagConstraints(3, iZeile, 3, 1, 0.0, 0.0,
						GridBagConstraints.NORTH,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
								2), 0, 0));
		
		return ++iZeile;
	}

	@Override
	public void loadDetails() throws Throwable {
		String[] printers = DelegateFactory.getInstance()
				.getServerDruckerDelegate().getServerDrucker();
		wcbAutoMahnDrucker.removeAllItems();
		for (int i = 0; i < printers.length; i++) {
			wcbAutoMahnDrucker.addItem(printers[i]);
		}
		wcbAutoMahnDrucker.addItem(NICHT_DRUCKEN);
		if (getPanelCtrl().getJobDetailsDto().getCDrucker() == null) {
			wcbAutoMahnDrucker.setSelectedItem(DelegateFactory
					.getInstance().getServerDruckerDelegate()
					.getServerStandarddrucker());
		} else {
			wcbAutoMahnDrucker.setSelectedItem(getPanelCtrl().getJobDetailsDto()
					.getCDrucker());
		}
		String sVersandart = getPanelCtrl().getJobDetailsDto().getCVersandart();
		if (sVersandart == null) {
			wrbAutoMahnVersandMail.setSelected(false);
			wrbAutoMahnVersandFax.setSelected(false);
			wrbAutoMahnVersandKein.setSelected(false);
		} else {
			if (sVersandart.equals(VERSANDART_KEIN_VERSAND)) {
				wrbAutoMahnVersandKein.setSelected(true);
			}
			if (sVersandart.equals(VERSANDART_E_MAIL)) {
				wrbAutoMahnVersandMail.setSelected(true);
			}
			if (sVersandart.equals(VERSANDART_FAX)) {
				wrbAutoMahnVersandFax.setSelected(true);
			}
		}
		wtfEmail.setText(getPanelCtrl().getJobDetailsDto().getCEmailZusaetzlich());
		
	}

	@Override
	public void saveDetails() throws Throwable {
		getPanelCtrl().getJobDetailsDto().setCDrucker((String) wcbAutoMahnDrucker
				.getSelectedItem());
		if (wrbAutoMahnVersandKein.isSelected()) {
			getPanelCtrl().getJobDetailsDto().setCVersandart(VERSANDART_KEIN_VERSAND);
		}
		if (wrbAutoMahnVersandMail.isSelected()) {
			getPanelCtrl().getJobDetailsDto().setCVersandart(VERSANDART_E_MAIL);
		}
		if (wrbAutoMahnVersandFax.isSelected()) {
			getPanelCtrl().getJobDetailsDto().setCVersandart(VERSANDART_FAX);
		}
		
		getPanelCtrl().getJobDetailsDto().setCEmailZusaetzlich(wtfEmail.getText());
		
		getPanelCtrl().save();
	}

}
