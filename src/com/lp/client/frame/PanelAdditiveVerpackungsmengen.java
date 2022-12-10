package com.lp.client.frame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;

import javax.swing.JPanel;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;

public class PanelAdditiveVerpackungsmengen extends JPanel implements
		FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	WrapperLabel wlaEinzelmenge = new WrapperLabel();
	public WrapperLabel wlaVerpackung = new WrapperLabel();

	public WrapperLabel getWlaEinzelmenge() {
		return wlaEinzelmenge;
	}

	public WrapperLabel getWlaVerpackung() {
		return wlaVerpackung;
	}

	WrapperNumberField wnfEinzelmenge = new WrapperNumberField();

	public WrapperNumberField getWnfEinzelmenge() {
		return wnfEinzelmenge;
	}

	public WrapperNumberField getWnfVerpackungen() {
		return wnfVerpackungen;
	}

	public void clearMengenUndSetzeFocusAufKarton() throws Throwable {
		wnfEinzelmenge.setBigDecimal(null);
		wnfVerpackungen.setBigDecimal(null);
		wnfVerpackungen.requestFocus();
	}

	WrapperNumberField wnfVerpackungen = new WrapperNumberField();

	WrapperNumberField wnfMengeAusBeleg = null;

	InternalFrame internalFrame = null;

	Double fVerpackungsmenge = null;

	public PanelAdditiveVerpackungsmengen(InternalFrame internalFrame,
			WrapperNumberField wnfMengeAusBeleg) throws ExceptionLP {
		this.wnfMengeAusBeleg = wnfMengeAusBeleg;
		this.internalFrame = internalFrame;
		wnfEinzelmenge.setFractionDigits(0);
		wnfVerpackungen.setFractionDigits(0);

		wlaEinzelmenge.setText(LPMain
				.getTextRespectUISPr("artikel.einzelmenge"));

		wlaVerpackung.setText(LPMain
				.getTextRespectUISPr("artikel.verpackungsmenge.karton"));

		// Listener
		wnfEinzelmenge.addFocusListener(this);
		wnfVerpackungen.addFocusListener(this);
		wnfMengeAusBeleg.addFocusListener(this);

		this.setLayout(new GridBagLayout());

		this.add(wlaVerpackung, new GridBagConstraints(0, 0, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		this.add(wnfVerpackungen, new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), -100, 0));

		this.add(wlaEinzelmenge, new GridBagConstraints(0, 1, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		this.add(wnfEinzelmenge, new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), -100, 0));

		wnfEinzelmenge.setVisible(false);
		wnfVerpackungen.setVisible(false);
		wlaEinzelmenge.setVisible(false);
		wlaVerpackung.setVisible(false);

	}

	public void setVisible(boolean b){
		super.setVisible(b);
		wnfEinzelmenge.setVisible(b);
		wnfVerpackungen.setVisible(b);
		wlaEinzelmenge.setVisible(b);
		wlaVerpackung.setVisible(b);
	}
	
	public void setVerpackungsmenge(Double fVerpackungsmenge) throws Throwable {
		this.fVerpackungsmenge = fVerpackungsmenge;

		if (fVerpackungsmenge != null && fVerpackungsmenge != 0) {
			wnfEinzelmenge.setVisible(true);
			wnfVerpackungen.setVisible(true);
			wlaEinzelmenge.setVisible(true);
			wlaVerpackung.setVisible(true);

			if (wnfMengeAusBeleg.getBigDecimal() != null) {

				wnfEinzelmenge.setBigDecimal(new BigDecimal(wnfMengeAusBeleg
						.getBigDecimal().doubleValue() % fVerpackungsmenge));
				wnfVerpackungen.setBigDecimal(new BigDecimal(new BigDecimal(
						wnfMengeAusBeleg.getBigDecimal().doubleValue()
								/ fVerpackungsmenge).intValue()));

			} else {
				wnfEinzelmenge.setBigDecimal(null);
				wnfVerpackungen.setBigDecimal(null);
			}

			wlaVerpackung.setText(LPMain
					.getTextRespectUISPr("artikel.verpackungsmenge.karton")
					+ " "
					+ Helper.formatZahl(fVerpackungsmenge, 2, LPMain
							.getTheClient().getLocUi()));

		} else {
			wnfEinzelmenge.setVisible(false);
			wnfVerpackungen.setVisible(false);
			wlaEinzelmenge.setVisible(false);
			wlaVerpackung.setVisible(false);
			wnfEinzelmenge.setBigDecimal(null);
			wnfVerpackungen.setBigDecimal(null);
		}

	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		try {

			if (fVerpackungsmenge != null && fVerpackungsmenge != 0) {

				if (e.getSource().equals(wnfMengeAusBeleg)) {
					if (wnfMengeAusBeleg.getBigDecimal() != null) {

						if (wnfMengeAusBeleg.getBigDecimal() != null) {

							wnfEinzelmenge
									.setBigDecimal(new BigDecimal(
											wnfMengeAusBeleg.getBigDecimal()
													.doubleValue()
													% fVerpackungsmenge));
							wnfVerpackungen.setBigDecimal(new BigDecimal(
									new BigDecimal(wnfMengeAusBeleg
											.getBigDecimal().doubleValue()
											/ fVerpackungsmenge).intValue()));

						}

					}
				} else if (e.getSource().equals(wnfEinzelmenge)
						|| e.getSource().equals(wnfVerpackungen)) {

					BigDecimal bdMengeFuerBeleg = BigDecimal.ZERO;

					if (wnfEinzelmenge.getBigDecimal() != null) {
						bdMengeFuerBeleg = bdMengeFuerBeleg.add(wnfEinzelmenge
								.getBigDecimal());
					}

					if (wnfVerpackungen.getBigDecimal() != null) {

						bdMengeFuerBeleg = bdMengeFuerBeleg.add(wnfVerpackungen
								.getBigDecimal().multiply(
										new BigDecimal(fVerpackungsmenge)));

					}
					wnfMengeAusBeleg.setBigDecimal(bdMengeFuerBeleg);
				}
			}
		} catch (ExceptionLP e1) {
			internalFrame.handleException(e1, true);
		}

	}

}
