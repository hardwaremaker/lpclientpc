package com.lp.client.lieferschein;

import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperBelegDateField;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.system.service.BelegDatumClient;

public class PanelAuslieferdatum extends PanelBasis {
	private static final long serialVersionUID = -3051782703376698789L;
	private static final String ACTION_SPECIAL_AUSLIEFERDATUM_JETZT = "action_special_auslieferdatum_jetzt" ;

	private WrapperLabel wlaAuslieferdatum = null;
	private WrapperDateField wdfAuslieferdatum = null;
	

	public PanelAuslieferdatum(InternalFrame internalFrame) throws Throwable {
		this(internalFrame, "") ;
	}
	
	public PanelAuslieferdatum(InternalFrame internalFrame, String title) throws Throwable {
		super(internalFrame, title) ;
		
		jbInitPanel() ;
		setDefaults() ;
		initComponents() ;
	}
	
	private InternalFrameLieferschein getInternalFrameLs() {
		return (InternalFrameLieferschein) getInternalFrame() ;
	}
	
	
	private LieferscheinDto getLieferscheinDto() {
		return getInternalFrameLs().getTabbedPaneLieferschein().getLieferscheinDto() ;
	}
	
	private void jbInitPanel() throws Throwable {
		wlaAuslieferdatum = new WrapperLabel(LPMain.getTextRespectUISPr("lp.auslieferdatum"));
		wdfAuslieferdatum = new WrapperBelegDateField(new BelegDatumClient(LPMain
				.getTheClient().getMandant(), new GregorianCalendar(), true));
		wdfAuslieferdatum.setMandatoryField(false);
		wdfAuslieferdatum.setShowRubber(false);
		wdfAuslieferdatum.setActivatable(false);
		
		createAndSaveAndShowButton("/com/lp/client/res/truck_red16x16.png",
				LPMain.getTextRespectUISPr("ls.auslieferdatum.jetzt"), 
				ACTION_SPECIAL_AUSLIEFERDATUM_JETZT, null, RechteFac.RECHT_LS_LIEFERSCHEIN_VERSAND) ;		
		
		JPanel datumPanel = new JPanel(new MigLayout("", "[fill, 20%][fill, 30%][fill]")) ;
		datumPanel.add(getWlaAuslieferdatum()) ;
		datumPanel.add(getWdfAuslieferdatum()) ;
		datumPanel.add(getBtnAuslieferdatumJetzt());
		
		add(datumPanel) ;
	} 

	public WrapperLabel getWlaAuslieferdatum() {
		return wlaAuslieferdatum ;
	}
	
	public WrapperDateField getWdfAuslieferdatum() {
		return wdfAuslieferdatum ;
	}
	
	public JButton getBtnAuslieferdatumJetzt() {
		return getHmOfButtons().get(ACTION_SPECIAL_AUSLIEFERDATUM_JETZT).getButton() ;
	}
	
	public void setDefaults() {
		dto2Components();
	}

	public void dto2Components() {
		wdfAuslieferdatum.setDate(getLieferscheinDto()
				.getTLiefertermin());	
		enableComponentsInAbhaengigkeitZuStatus();
	}
	
	public void components2Dto() {
		if (wdfAuslieferdatum.getDate() != null) {
			getLieferscheinDto().setTLiefertermin(
					new Timestamp(wdfAuslieferdatum.getDate().getTime()));
		} else {
			getLieferscheinDto().setTLiefertermin(null);
		}
	}

	protected void enableEingabeImpl() {
		wdfAuslieferdatum.setActivatable(true);
		wdfAuslieferdatum.setEnabled(true);		
	}
	
	/**
	 * Das Auslieferdatum kann eingegeben werden
	 */
	public void enableForcedEingabe()  {
		enableEingabeImpl(); 
	}
	
	/**
	 * Das Auslieferdatum kann nur eingegeben werden, wenn der Beleg bereits gedruckt wurde
	 */
	public void enableEingabe() {
		if(getLieferscheinDto().getTGedruckt() != null) {
			enableEingabeImpl() ;
		}
	}
	
	private void enableComponentsInAbhaengigkeitZuStatus() {
		LPButtonAction btnAction = getHmOfButtons().get(ACTION_SPECIAL_AUSLIEFERDATUM_JETZT) ;
		btnAction.setEnabled(getLieferscheinDto().getTGedruckt() != null) ; 
		
		wdfAuslieferdatum.setActivatable(false);
		wdfAuslieferdatum.setEnabled(false);
	}	
	
	@Override
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if(e.getActionCommand().equals(ACTION_SPECIAL_AUSLIEFERDATUM_JETZT)) {
			/*
			 * Das Auslieferdatum explizit zu setzen ist eine "Abkuerzung" 
			 * fuer den Fall, dass das Lieferdatum direkt ueber den Server 
			 * gesetzt wird. Dann muss fuer die aktuelle UI nicht das 
			 * komplette LieferscheinDto neu geladen werden. Sehr schoen 
			 * ist das allerdings nicht. (ghp, 15.03.2016)
			 */
			wdfAuslieferdatum.setDate(new Timestamp(System.currentTimeMillis())) ;				
			if(!wdfAuslieferdatum.isEnabled()) {
				DelegateFactory.getInstance()
					.getLsDelegate().setzeAuslieferdatumAufJetzt(
							getLieferscheinDto().getIId());
			}
		}
	}
}
