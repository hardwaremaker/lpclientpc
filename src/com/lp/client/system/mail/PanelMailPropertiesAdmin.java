package com.lp.client.system.mail;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.SystemDelegate;
import com.lp.client.pc.LPMain;
import com.lp.client.system.InternalFrameSystem;
import com.lp.server.system.mail.service.MailPropertyDto;

public class PanelMailPropertiesAdmin extends PanelBasis {
	private static final long serialVersionUID = 3778101013604126999L;

	private JPanel jPanelWorkingOn;
	private WrapperLabel wlaMailProperty;
	private WrapperTextField wtfMailProperty;
	private WrapperLabel wlaWert;
	private WrapperTextField wtfWert;
	
	private MailPropertyDto mailPropertyDto;
	
	public PanelMailPropertiesAdmin(InternalFrameSystem internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		
		jbInit(); 
		initComponents();
	}
	
	private void jbInit() throws Throwable {
		wlaMailProperty = new WrapperLabel(LPMain.getTextRespectUISPr("lp.system.mailproperty"));
		wtfMailProperty = new WrapperTextField(160);
		wtfMailProperty.setActivatable(false);
		wtfMailProperty.setMandatoryField(true);
		
		wlaWert = new WrapperLabel(LPMain.getTextRespectUISPr("lp.wert"));
		wtfWert = new WrapperTextField(160);
		wtfWert.setActivatable(true);
		
		jPanelWorkingOn = new JPanel();
		HvLayout layout = HvLayoutFactory.create(jPanelWorkingOn, "ins 0", "[10%|fill,120|fill,grow|15%]", "20[|]");
		layout.add(wlaMailProperty).skip(1).add(wtfMailProperty).wrap()
			.add(wlaWert).skip(1).add(wtfWert);
		
		this.setLayout(new GridBagLayout());
		this.add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);
	}
	
	@Override
	public InternalFrameSystem getInternalFrame() {
		return (InternalFrameSystem) super.getInternalFrame();
	}
	
	@Override
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(bNeedNoYouAreSelectedI);
		
		getInternalFrame().getTabbedPaneParameter().disableParametermandantgueltigab();
		String cnr = (String) getKeyWhenDetailPanel();
		if (cnr == null || cnr.equals(LPMain.getLockMeForNew())) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			loadMailPropertyDto(cnr);
			dto2Components();
		}
	}
	
	private void dto2Components() throws Throwable {
		if (mailProperty() == null) return;
		
		wtfMailProperty.setText(mailProperty().getCNr());
		wtfWert.setText(mailProperty().getCWert());
		
		setStatusbarPersonalIIdAendern(mailProperty().getPersonalIIdAendern());
		setStatusbarTAendern(mailProperty().getTAendern());
	}

	private void components2Dto() {
		mailProperty().setCNr(wtfMailProperty.getText());
		mailProperty().setCWert(wtfWert.getText());
	}
	
	private void loadMailPropertyDto(String cnr) throws ExceptionLP, Throwable {
		if (cnr == null) return;
		
		setMailProperty(systemDelegate().mailpropertyFindByCnr(cnr));
	}
	
	private MailPropertyDto mailProperty() {
		return mailPropertyDto;
	}
	
	private void setMailProperty(MailPropertyDto mailPropertyDto) {
		this.mailPropertyDto = mailPropertyDto;
	}
	
	private SystemDelegate systemDelegate() throws Throwable {
		return DelegateFactory.getInstance().getSystemDelegate();
	}

	@Override
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MAILPROPERTIES;
	}
	
	@Override
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (mailProperty() == null
				|| isLockedDlg()) {
			return;
		}
		
		systemDelegate().resetMailProperty(mailProperty().getCNr());
		loadMailPropertyDto(mailProperty().getCNr());
		dto2Components();
		
		super.eventActionDelete(e, bAdministrateLockKeyI, bNeedNoDeleteI);
	}

	@Override
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (!allMandatoryFieldsSetDlg())
			return;
		
		boolean isNewProperty = mailProperty().getCNr() == null;
		components2Dto();
		mailProperty().setMandantCNr(LPMain.getTheClient().getMandant());
		setMailProperty(isNewProperty 
				? systemDelegate().createMailProperty(mailProperty())
				: systemDelegate().updateMailProperty(mailProperty()));
		
		setKeyWhenDetailPanel(mailProperty().getCNr());
		super.eventActionSave(e, bNeedNoSaveI);
		eventYouAreSelected(false);
	}
	
	@Override
	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		wtfMailProperty.setActivatable(false);
	}
	
	@Override
	public void eventActionNew(EventObject eventObject, boolean bAdministrateLockKeyI, boolean bNeedNoNewI)
			throws Throwable {
		super.eventActionNew(eventObject, bAdministrateLockKeyI, bNeedNoNewI);
		leereAlleFelder(this);
		wtfMailProperty.setActivatable(true);
		setMailProperty(new MailPropertyDto());
	}
}
