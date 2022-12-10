package com.lp.client.finanz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.ReversechargeartDto;

public class PanelSelectReversechargeart extends PanelBasis {
	private static final long serialVersionUID = -5229506979160465267L;
	private static final String ACTION_SPECIAL_REVERSECHARGEART = "action_special_rechnung_reversechargeart";

	private WrapperLabel wlaReversechargeart ;
	private WrapperComboBox wcoReversechargeart ;
	
	private boolean mehrfachKontierung ;
	
	public PanelSelectReversechargeart(InternalFrame internalFrame, String title) throws Throwable {
		super(internalFrame, title) ;
		
		jbInitPanel() ;
		setDefaults() ;
		initComponents() ;
	}
	
	private void jbInitPanel() {
		wlaReversechargeart = new WrapperLabel(LPMain.getTextRespectUISPr("lp.reversecharge")) ; 
		wlaReversechargeart.setHorizontalAlignment(SwingConstants.RIGHT); 
		
		wcoReversechargeart = new WrapperComboBox() ;
		wcoReversechargeart.setMandatoryFieldDB(true);
		wcoReversechargeart.addActionListener(this);
		wcoReversechargeart.setActionCommand(ACTION_SPECIAL_REVERSECHARGEART);

		setLayout(new BorderLayout()) ;
		setPreferredSize(new Dimension(300, 60));
		setMaximumSize(new Dimension(500, 60)); 
		JPanel  panel = new JPanel(new FlowLayout()) ;
		panel.setPreferredSize(new Dimension(300, 60));
		panel.setMaximumSize(new Dimension(500, 60)); 
		panel.add(wlaReversechargeart) ;
		panel.add(wcoReversechargeart) ;
		add(panel, BorderLayout.CENTER) ;
		
//		HvLayout hvLayout = HvLayoutFactory.create(this, "nogrid"); 
//		HvLayout hvLayout = HvLayoutFactory.create(this, "fill", "[][grow]", null); 
// 		hvLayout
// 			.add(wlaReversechargeart)
//			.add(wcoReversechargeart, "span, grow") ;
	}
	
	public void setDefaults() throws Throwable {
		mehrfachKontierung = false ;
		prepareReversechargeart();
	}
	
	private void prepareReversechargeart() throws Throwable {
		if(!wcoReversechargeart.isMapSet()) {
			wcoReversechargeart.setMap(DelegateFactory.getInstance().getFinanzServiceDelegate().getAllReversechargeartAllowed());
		}
		wcoReversechargeart.setSelectedIndex(0);		
	}	
	
	public Integer getReversechargeartId() {
		return (Integer) wcoReversechargeart.getKeyOfSelectedItem() ;
	}
	
	public void setReversechargeartId(Integer reversechargeartId) {
		if(!mehrfachKontierung) {
			wcoReversechargeart.setKeyOfSelectedItem(reversechargeartId);
		}
	}
	
	public WrapperLabel getWlaReversechargeart() {
		return wlaReversechargeart ;
	}
	
	public WrapperComboBox getWcoReversechargeart() {
		return wcoReversechargeart ;
	}
	
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}
	

	/**
	 * In der Combobox den "Ohne Reversecharge" Eintrag selektieren
	 */
	public void setOhneAsSelected() {
		if(!mehrfachKontierung) {
			wcoReversechargeart.setSelectedIndex(0);
		}
	}

	/**
	 * Ist in der Combobox der Eintrag "Ohne Reversecharge" der selectierte?
	 * @return true wenn der Eintrag "Ohne Reversecharge" der selektierte ist
	 */
	public boolean isOhneSelected() {
		return mehrfachKontierung ? true : wcoReversechargeart.getSelectedIndex() <= 0 ;
	}	
	
	/**
	 * Ist eine der Reversecharge-Arten selektiert?
	 * @return true wenn eine der Reversecharge-Arten ausgew&auml;hlt wurde
	 */
	public boolean hatReversecharge() {
		return !isOhneSelected();
	}
	
	public void setReversechargeEnabled(boolean enabled) {
		wcoReversechargeart.setEnabled(enabled);
	}
	
	/**
	 * Die Anzeige der Kombobox so ab&auml;ndern, dass "Siehe kontierung" angezeigt wird
	 */
	public void setReversechargeKontierung() throws Throwable {
		mehrfachKontierung = true ;

		Map<Integer, String> map = new HashMap<Integer, String>();
		ReversechargeartDto rcartOhneDto = DelegateFactory
				.getInstance().getFinanzServiceDelegate().reversechargeartFindOhne() ;
		map.put(rcartOhneDto.getIId(), LPMain.getTextRespectUISPr("er.siehekontierung"));
		wcoReversechargeart.setMap(map);
		wcoReversechargeart.setSelectedIndex(0);
	}
	
	public void reload() throws Throwable {
		mehrfachKontierung = false ;

		wcoReversechargeart.setMap(DelegateFactory.getInstance().getFinanzServiceDelegate().getAllReversechargeartAllowed());
		wcoReversechargeart.setSelectedIndex(0);
	}
}
