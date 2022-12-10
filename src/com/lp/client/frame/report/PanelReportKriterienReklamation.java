package com.lp.client.frame.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.reklamation.ReportReklamationUnterartKundeLieferant;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.reklamation.service.ReklamationDto;

public class PanelReportKriterienReklamation extends PanelReportKriterien
		implements ActionListener {

	ReportReklamationUnterartKundeLieferant reportReklamationUnterartKundeLieferant = null;

	public PanelReportKriterienReklamation(
			InternalFrame internalFrame,
			ReportReklamationUnterartKundeLieferant reportReklamationUnterartKundeLieferant,
			String addTitleI, PartnerDto partnerDtoEmpfaenger,
			Integer ansprechpartnerIId, boolean bDirekt, boolean bMitEmailFax,
			boolean bNurVorschau) throws Throwable {
		super(internalFrame, reportReklamationUnterartKundeLieferant,
				addTitleI, partnerDtoEmpfaenger, ansprechpartnerIId, bDirekt,
				bMitEmailFax, bNurVorschau, true);

		this.reportReklamationUnterartKundeLieferant = reportReklamationUnterartKundeLieferant;

		reportReklamationUnterartKundeLieferant.wrbKunde
				.addActionListener(this);
		reportReklamationUnterartKundeLieferant.wrbLieferant
				.addActionListener(this);
		
		
		

	}

	
	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(
				reportReklamationUnterartKundeLieferant.wrbKunde)
				|| e.getSource().equals(
						reportReklamationUnterartKundeLieferant.wrbLieferant)) {
			setzeEmpfaengerAnhandSelektiertemRadioButton();

		}
		super.eventActionSpecial(e);
	}


	public void setzeEmpfaengerAnhandSelektiertemRadioButton()
			throws ExceptionLP, Throwable {
		if (reportReklamationUnterartKundeLieferant.getReklamationIId() != null) {

			ReklamationDto reklamationDto = DelegateFactory
					.getInstance()
					.getReklamationDelegate()
					.reklamationFindByPrimaryKey(
							reportReklamationUnterartKundeLieferant
									.getReklamationIId());

			PartnerDto partnerDto = null;
			Integer ansprechprtnerIId = null;
			if (reportReklamationUnterartKundeLieferant.wrbKunde
					.isSelected()) {

				partnerDto = DelegateFactory
						.getInstance()
						.getKundeDelegate()
						.kundeFindByPrimaryKey(reklamationDto.getKundeIId())
						.getPartnerDto();

				ansprechprtnerIId = reklamationDto.getAnsprechpartnerIId();

			}

			else if (reportReklamationUnterartKundeLieferant.wrbLieferant
					.isSelected()
					&& reklamationDto.getLieferantIId() != null) {
				partnerDto = DelegateFactory
						.getInstance()
						.getLieferantDelegate()
						.lieferantFindByPrimaryKey(
								reklamationDto.getLieferantIId())
						.getPartnerDto();

				ansprechprtnerIId = reklamationDto
						.getAnsprechpartnerIIdLieferant();

			}

			getOptions().setPartnerDtoEmpfaenger(partnerDto);
			getOptions().setAnsprechpartnerIId(ansprechprtnerIId);

			PanelVersandEmail panelVersandEmail = getPanelVersandEmail(false);
			if (panelVersandEmail != null) {
				panelVersandEmail.wtfEmpfaenger.setText(null);
				panelVersandEmail.setDefaultAbsender(partnerDto, ansprechprtnerIId);
			}

			PanelVersandFax panelVersandFax = getPanelVersandFax(false);
			if (panelVersandFax != null) {
				panelVersandFax.wtfEmpfaenger.setText(null);
				panelVersandFax.setDefaultAbsender(partnerDto, ansprechprtnerIId);
			}

		}
	}

}
