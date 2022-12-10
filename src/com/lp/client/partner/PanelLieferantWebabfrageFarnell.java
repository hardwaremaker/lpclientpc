package com.lp.client.partner;

import javax.swing.JComponent;

import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.partner.service.WeblieferantFarnellDto;

public class PanelLieferantWebabfrageFarnell extends PanelLieferantWebabfrageDetailBase<WeblieferantFarnellDto> {
	private static final long serialVersionUID = 6363348452307111445L;

	private WrapperLabel labelApiKey;
	private WrapperTextField textApiKey;
	private WrapperLabel labelUrl;
	private WrapperTextField textUrl;
	private WrapperLabel labelCustomerId;
	private WrapperTextField textCustomerId;
	private WrapperLabel labelCustomerKey;
	private WrapperTextField textCustomerKey;
	private WrapperLabel labelStore;
	private WrapperTextField textStore;
	private JComponent[][] panelComponents;

	public PanelLieferantWebabfrageFarnell() {
		initPanelComponents();
	}
	
	private void initPanelComponents() {
		labelApiKey = new WrapperLabel(LPMain.getTextRespectUISPr("lieferant.weblieferant.farnell.apikey"));
		textApiKey = new WrapperTextField();
		initTextField(textApiKey, true);
		
		labelUrl = new WrapperLabel(LPMain.getTextRespectUISPr("lieferant.weblieferant.farnell.url"));
		textUrl = new WrapperTextField();
		initTextField(textUrl, true);
		
		labelCustomerId = new WrapperLabel(LPMain.getTextRespectUISPr("lieferant.weblieferant.farnell.customerid"));
		textCustomerId = new WrapperTextField();
		initTextField(textCustomerId, false);
		
		labelCustomerKey = new WrapperLabel(LPMain.getTextRespectUISPr("lieferant.weblieferant.farnell.customerkey"));
		textCustomerKey = new WrapperTextField();
		initTextField(textCustomerKey, false);

		labelStore = new WrapperLabel(LPMain.getTextRespectUISPr("lieferant.weblieferant.farnell.store"));
		textStore = new WrapperTextField();
		initTextField(textStore, true);
		
		panelComponents = new JComponent[5][2];
		panelComponents[0][0] = labelUrl;
		panelComponents[0][1] = textUrl;
		panelComponents[1][0] = labelApiKey;
		panelComponents[1][1] = textApiKey;
		panelComponents[2][0] = labelStore;
		panelComponents[2][1] = textStore;
		panelComponents[3][0] = labelCustomerId;
		panelComponents[3][1] = textCustomerId;
		panelComponents[4][0] = labelCustomerKey;
		panelComponents[4][1] = textCustomerKey;
	}
	
	private void initTextField(WrapperTextField field, boolean mandatory) {
		field.setColumnsMax(100);
		field.setMandatoryField(mandatory);
		field.setActivatable(true);
	}
	
	@Override
	public JComponent[][] getPanelComponents() {
		if (panelComponents == null) {
			initPanelComponents();
		}
		return panelComponents;
	}

	@Override
	protected void components2Dto() {
		getWebpartnerDto().setCUrl(textUrl.getText());
		getWebpartnerDto().setCApiKey(textApiKey.getText());
		getWebpartnerDto().setCStore(textStore.getText());
		getWebpartnerDto().setCCustomerId(textCustomerId.getText());
		getWebpartnerDto().setCCustomerKey(textCustomerKey.getText());
	}

	@Override
	protected void dto2components() {
		textUrl.setText(getWebpartnerDto().getCUrl());
		textApiKey.setText(getWebpartnerDto().getCApiKey());
		textStore.setText(getWebpartnerDto().getCStore());
		textCustomerId.setText(getWebpartnerDto().getCCustomerId());
		textCustomerKey.setText(getWebpartnerDto().getCCustomerKey());
	}

	@Override
	protected WeblieferantFarnellDto createWebPartnerDto() {
		WeblieferantFarnellDto farnellDto = new WeblieferantFarnellDto();
		farnellDto.setWebabfrageIId(AngebotstklFac.WebabfrageTyp.FARNELL);
		return farnellDto;
	}
}
