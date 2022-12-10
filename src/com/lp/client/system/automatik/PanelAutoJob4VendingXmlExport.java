package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayout.Align;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.JobDetails4VendingExportDto;

public class PanelAutoJob4VendingXmlExport implements IPanelAutoJobDetails {

	private IAutoJobDetailsPanelCtrl<JobDetails4VendingExportDto> panelCtrl;
	
	private WrapperLabel wlaKundenPath;
	private WrapperLabel wlaArtikelPath;
	private WrapperLabel wlaLieferantenPath;
	private WrapperCheckBox wcbKunden;
	private WrapperCheckBox wcbArtikel;
	private WrapperCheckBox wcbLieferanten;
	private WrapperTextField wtfKundenPath;
	private WrapperTextField wtfArtikelPath;
	private WrapperTextField wtfLieferantenPath;
	private WrapperLabel wlaEmailFehler;
	private WrapperLabel wlaEmailErfolgreich;
	private WrapperTextField wtfEmailFehler;
	private WrapperTextField wtfEmailErfolgreich;

	public PanelAutoJob4VendingXmlExport(IAutoJobDetailsPanelCtrl<JobDetails4VendingExportDto> panelCtrl) {
		setPanelCtrl(panelCtrl);
	}

	public void setPanelCtrl(IAutoJobDetailsPanelCtrl<JobDetails4VendingExportDto> panelCtrl) {
		this.panelCtrl = panelCtrl;
	}
	
	public IAutoJobDetailsPanelCtrl<JobDetails4VendingExportDto> getPanelCtrl() {
		return panelCtrl;
	}
	
	private void initComponents() {
		wlaKundenPath = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.4vendingexport.kundenpfad"));
		wlaLieferantenPath = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.4vendingexport.lieferantenpfad"));
		wlaArtikelPath = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.4vendingexport.artikelpfad"));
		
		wcbKunden = new WrapperCheckBox(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.4vendingexport.kunden"));
		wcbLieferanten = new WrapperCheckBox(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.4vendingexport.lieferanten"));
		wcbArtikel = new WrapperCheckBox(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.4vendingexport.artikel"));
		
		wtfKundenPath = createTextField(400);
		wtfLieferantenPath = createTextField(400);
		wtfArtikelPath = createTextField(400);
		
		wlaEmailErfolgreich = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.4vendingexport.emailerfolgreich"));
		wtfEmailErfolgreich = createTextField(80);
		wlaEmailFehler = new WrapperLabel(LPMain.getTextRespectUISPr(
				"lp.system.automatik.details.4vendingexport.emailfehler"));
		wtfEmailFehler = createTextField(80);
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
		
		jPanelWorkingOn.add(wlaEmailErfolgreich,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfEmailErfolgreich,
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaEmailFehler,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		jPanelWorkingOn.add(wtfEmailFehler,
				new GridBagConstraints(1, iZeile, 3, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		iZeile++;
		JPanel vendingPanel = new JPanel();
		HvLayout hvlayout = HvLayoutFactory.create(vendingPanel, "ins 0", "[100px,fill|100px,fill|fill,grow]", "");
		hvlayout.addAligned(Align.LEFT,wcbArtikel).add(wlaArtikelPath).add(wtfArtikelPath).wrap();
		hvlayout.addAligned(Align.LEFT,wcbKunden).add(wlaKundenPath).add(wtfKundenPath).wrap();
		hvlayout.addAligned(Align.LEFT,wcbLieferanten).add(wlaLieferantenPath).add(wtfLieferantenPath).wrap();
		jPanelWorkingOn.add(vendingPanel,
			new GridBagConstraints(1, iZeile, 6, 1, 0.0, 0.0,
					GridBagConstraints.NORTH,
					GridBagConstraints.HORIZONTAL, 
					new Insets(2, 2, 2,	2), 0, 0));
		
		return ++iZeile;
	}

	@Override
	public void loadDetails() throws Throwable {
		wcbArtikel.setSelected(getPanelCtrl().getJobDetailsDto().getBArtikel() == null 
				? false : getPanelCtrl().getJobDetailsDto().getBArtikel());
		wcbKunden.setSelected(getPanelCtrl().getJobDetailsDto().getBKunden() == null 
				? false : getPanelCtrl().getJobDetailsDto().getBKunden());
		wcbLieferanten.setSelected(getPanelCtrl().getJobDetailsDto().getBLieferanten() == null 
				? false : getPanelCtrl().getJobDetailsDto().getBLieferanten());
		
		wtfArtikelPath.setText(getPanelCtrl().getJobDetailsDto().getCPfadPatternArtikel());
		wtfKundenPath.setText(getPanelCtrl().getJobDetailsDto().getCPfadPatternKunden());
		wtfLieferantenPath.setText(getPanelCtrl().getJobDetailsDto().getCPfadPatternLieferanten());
		
		wtfEmailFehler.setText(getPanelCtrl().getJobDetailsDto().getCEmailFehler());
		wtfEmailErfolgreich.setText(getPanelCtrl().getJobDetailsDto().getCEmailErfolgreich());
	}

	@Override
	public void saveDetails() throws Throwable {
		getPanelCtrl().getJobDetailsDto().setBArtikel(wcbArtikel.isSelected());
		getPanelCtrl().getJobDetailsDto().setBKunden(wcbKunden.isSelected());
		getPanelCtrl().getJobDetailsDto().setBLieferanten(wcbLieferanten.isSelected());
		
		getPanelCtrl().getJobDetailsDto().setCPfadPatternArtikel(wtfArtikelPath.getText());
		getPanelCtrl().getJobDetailsDto().setCPfadPatternKunden(wtfKundenPath.getText());
		getPanelCtrl().getJobDetailsDto().setCPfadPatternLieferanten(wtfLieferantenPath.getText());
		
		getPanelCtrl().getJobDetailsDto().setCEmailErfolgreich(wtfEmailErfolgreich.getText());
		getPanelCtrl().getJobDetailsDto().setCEmailFehler(wtfEmailFehler.getText());
		
		getPanelCtrl().save();
	}

}
