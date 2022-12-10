package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.partner.service.LieferantDto;

public class PanelLieferantWebabfrage extends PanelBasis implements ActionListener {
	private static final long serialVersionUID = 3134054128089479521L;

	private HvLayout layoutAll;
	private JPanel panelAll;
	private WrapperLabel labelWebabfrage;
	private WrapperComboBox comboWebabfrage;
	
	public PanelLieferantWebabfrage(InternalFrameLieferant internalFrame, String title, Object key)
			throws Throwable {
		super(internalFrame, title, key);
		jbInit();
	}

	private LieferantDto lieferantDto() {
		return ((InternalFrameLieferant)getInternalFrame()).getLieferantDto();
	}
	
	private IPanelLieferantWeblieferant getSelectedDetailPanel() {
		return ((ComboWebabfrageItem)comboWebabfrage.getSelectedItem()).getPanelProperties();
	}
	
	private void initComboWebabfrage() {
		labelWebabfrage = new WrapperLabel(LPMain.getTextRespectUISPr("lieferant.weblieferant.webabfrage"));
		labelWebabfrage.setHorizontalAlignment(SwingConstants.RIGHT);
		comboWebabfrage = new WrapperComboBox();
		comboWebabfrage.setMandatoryFieldDB(false);
		ComboWebabfrageItem emptyItem = new ComboWebabfrageItem(0, 
				LPMain.getTextRespectUISPr("wrappercombobox.emptyentry"), 
				new PanelLieferantWebabfrageLeer());
		comboWebabfrage.setEmptyEntry(emptyItem);
		ComboWebabfrageItem comboItem = new ComboWebabfrageItem(AngebotstklFac.WebabfrageTyp.FARNELL, 
				LPMain.getTextRespectUISPr("lieferant.weblieferant.abfragetyp.farnell"),
				new PanelLieferantWebabfrageFarnell());
		Map<Integer, ComboWebabfrageItem> mapItems = new HashMap<Integer, ComboWebabfrageItem>();
		mapItems.put(comboItem.getKey(), comboItem);
		comboWebabfrage.setMap(mapItems);
		comboWebabfrage.addActionListener(this);
	}
	
	private void jbInit() throws Throwable {
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		initComboWebabfrage();
		
		panelAll = new JPanel();
		setLayout(new GridBagLayout());
		add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		add(panelAll, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		layoutAll = HvLayoutFactory.create(panelAll, "wrap 2", "100[20%,fill|50%,fill]30%", "20[]10[||||||]");
		layoutAll.add(labelWebabfrage, "gap ")
			.add(comboWebabfrage);

		String[] aWhichButtons = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtons);
	}
	
	private void reloadPanelDetail() {
		panelAll.removeAll();
		layoutAll.add(labelWebabfrage)
			.add(comboWebabfrage);
		
		JComponent[][] components = getSelectedDetailPanel().getPanelComponents();
		for (JComponent[] row : components) {
			layoutAll.add(row[0]).add(row[1]);
		}
	}
	
	private IWebpartnerDto loadWeblieferant() throws ExceptionLP, Throwable {
		IWebpartnerDto weblieferant = DelegateFactory.getInstance()
				.getLieferantDelegate().weblieferantFindByLieferantIIdOhneExc(lieferantDto().getIId());
		return weblieferant;
	}
	
	@Override
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(bNeedNoYouAreSelectedI);
		
		IWebpartnerDto weblieferant = loadWeblieferant();
		if (weblieferant == null) {
			comboWebabfrage.setKeyOfSelectedItem(null);
		} else {
			comboWebabfrage.setKeyOfSelectedItem(weblieferant.getWebabfrageIId());
		}
		
		getSelectedDetailPanel().setWebpartnerDto(weblieferant, lieferantDto());
		reloadPanelDetail();
	}
	
	@Override
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (!allMandatoryFieldsSetDlg()) {
			return;
		}
		getSelectedDetailPanel().update();
		super.eventActionSave(e, bNeedNoSaveI);
	}
	
	@Override
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		getSelectedDetailPanel().remove();
		super.eventActionDelete(e, bAdministrateLockKeyI, bNeedNoDeleteI);
	}
	
	@Override
	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		getSelectedDetailPanel().setWebpartnerDto(loadWeblieferant(), lieferantDto());
		super.eventActionDiscard(e);
	}
	
	@Override
	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}
	
	@Override
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource() == comboWebabfrage) {
			getSelectedDetailPanel().setWebpartnerDto(loadWeblieferant(), lieferantDto());
			reloadPanelDetail();
		}
	}
	
	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERANT;
	}
	
	private class ComboWebabfrageItem {
		private Integer key;
		private String comboText;
		private IPanelLieferantWeblieferant panelProperties;
		
		public ComboWebabfrageItem(Integer key, String comboText, IPanelLieferantWeblieferant panelProperties) {
			this.key = key;
			this.comboText = comboText;
			this.panelProperties = panelProperties;
		}
		
		public Integer getKey() {
			return key;
		}
		
		public IPanelLieferantWeblieferant getPanelProperties() {
			return panelProperties;
		}
		
		@Override
		public String toString() {
			return comboText;
		}
	}
}
