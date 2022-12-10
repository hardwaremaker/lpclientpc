/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.lieferschein;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.PanelAdditiveVerpackungsmengen;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogPositionenSchnellerfassung extends JDialog implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel panelUrlaubsanspruch = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	WrapperLabel wlaArtikelnummer = new WrapperLabel();
	JTextField wtfArtikelnummer = new JTextField();
	WrapperLabel wlaBezeichnung = new WrapperLabel();
	JTextField wtfBezeichnung = new JTextField();
	WrapperLabel wlaSerienChargennummer = new WrapperLabel();
	JTextField wtfSerienChargennummer = new JTextField();
	WrapperLabel wlaMenge = new WrapperLabel();
	WrapperNumberField wnfMenge = new WrapperNumberField();
	JButton wbuFertig = new JButton();

	ArtikelDto letzter_gebuchter_artikelDto = null;
	ArtikelDto artikelDto = null;
	LieferscheinDto lieferscheinDto = null;
	RechnungDto rechnungDto = null;
	PanelSplit panelPositionen = null;

	boolean suchNachEan = false;

	PanelAdditiveVerpackungsmengen pa = null;

	public DialogPositionenSchnellerfassung(PanelSplit panelSplit, LieferscheinDto lieferscheinDto) throws Throwable {
		super(LPMain.getInstance().getDesktop(),
				LPMain.getInstance().getTextRespectUISPr("auftrag.positionen.schnelleingabe"), true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.panelPositionen = panelSplit;
		this.lieferscheinDto = lieferscheinDto;
		jbInit();
		pack();
		wtfArtikelnummer.requestFocus();
	}

	public DialogPositionenSchnellerfassung(PanelSplit panelSplit, RechnungDto rechnungDto) throws Throwable {
		super(LPMain.getInstance().getDesktop(),
				LPMain.getInstance().getTextRespectUISPr("auftrag.positionen.schnelleingabe"), true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.panelPositionen = panelSplit;
		this.rechnungDto = rechnungDto;
		jbInit();
		pack();
		wtfArtikelnummer.requestFocus();
	}

	public void dispose() {
		try {

			if (wtfArtikelnummer.getText() != null && wtfArtikelnummer.getText().length() > 0 && artikelDto != null
					&& wnfMenge.getBigDecimal() != null) {

				KeyEvent k = new KeyEvent(wnfMenge, 0, 0, 0, KeyEvent.VK_ENTER, ' ');

				keyPressed(k);
			}

		} catch (Throwable ex) {
			panelPositionen.handleException(ex, true);
		}

		super.dispose();
	}

	private void jbInit() throws Throwable {
		panelUrlaubsanspruch.setLayout(gridBagLayout1);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_SCHNELLANLAGE_SUCHE_ZUERST_NACH_EAN,
						ParameterFac.KATEGORIE_ALLGEMEIN, LPMain.getTheClient().getMandant());
		suchNachEan = (Boolean) parameter.getCWertAsObject();

		if (suchNachEan) {
			wlaArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr("lp.lsreschnellerfassung.suchenachean"));

		} else {
			wlaArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr("artikel.artikelnummer"));
		}

		wlaSerienChargennummer.setText(
				LPMain.getInstance().getTextRespectUISPr("artikel.handlagerbewegung.seriennrchargennreinzeln"));
		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung.setEnabled(false);
		wtfSerienChargennummer.setEnabled(false);

		wtfArtikelnummer.addKeyListener(this);

		wtfSerienChargennummer.addKeyListener(this);
		wnfMenge.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());
		wnfMenge.addKeyListener(this);

		wbuFertig.setText(LPMain.getInstance().getTextRespectUISPr("lp.beenden"));
		wbuFertig.addActionListener(new DialogLieferscheinpositionenSchnellerfassung_wbuFertig_actionAdapter(this));
		this.getContentPane().setLayout(gridBagLayout2);

		this.getContentPane().add(panelUrlaubsanspruch, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 250, 50));

		panelUrlaubsanspruch.add(wtfArtikelnummer, new GridBagConstraints(2, 1, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaArtikelnummer, new GridBagConstraints(0, 1, 1, 1, 0.15, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wtfBezeichnung, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaBezeichnung, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelUrlaubsanspruch.add(wlaSerienChargennummer, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wtfSerienChargennummer, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelUrlaubsanspruch.add(wlaMenge, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		panelUrlaubsanspruch.add(wnfMenge, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		pa = new PanelAdditiveVerpackungsmengen(panelPositionen.getInternalFrame(), wnfMenge);

		pa.getWnfEinzelmenge().addKeyListener(this);
		pa.getWnfVerpackungen().addKeyListener(this);

		parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getInstance().getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_VERPACKUNGSMENGEN_EINGABE);
		int bVerpackungsmengeneingabe = (Integer) parameter.getCWertAsObject();

		if (bVerpackungsmengeneingabe > 0) {
			panelUrlaubsanspruch.add(pa, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}

		panelUrlaubsanspruch.add(wbuFertig, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public void wbuFertig_actionPerformed(ActionEvent e) {

		this.dispose();
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == wtfArtikelnummer) {
				artikelDto=null;
				try {

					if (suchNachEan) {
						artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByEanFuerSchnellerfassung(wtfArtikelnummer.getText());
					}

					if (artikelDto == null) {
						artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByCNr(wtfArtikelnummer.getText());
					}
					pa.setVerpackungsmenge(artikelDto.getFVerpackungsmenge());
				} catch (Throwable ex) {
					if (ex instanceof ExceptionLP) {
						ExceptionLP exLP = (ExceptionLP) ex;
						if (exLP.getICode() == EJBExceptionLP.FEHLER_BEI_FIND) {
							DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
									"Artikel konnte nicht gefunden werden.");
							artikelDto = null;
							wtfArtikelnummer.setText(null);
							wtfBezeichnung.setText(null);
							wtfArtikelnummer.requestFocus();
						} else {
							panelPositionen.handleException(ex, true);
						}
					}

					else {
						panelPositionen.handleException(ex, true);
					}

				}

				if (artikelDto != null) {
					wtfBezeichnung.setText(artikelDto.formatArtikelbezeichnung());

					if (Helper.short2boolean(artikelDto.getBChargennrtragend())
							|| Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
						wtfSerienChargennummer.setEnabled(true);
						wtfSerienChargennummer.setText(null);
						wtfSerienChargennummer.requestFocus();
						if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
							try {
								wnfMenge.setDouble(new Double(1));
							} catch (ExceptionLP ex2) {
								// wird hoffentlich gehen
							}
						}

					} else {
						wtfSerienChargennummer.setText(null);
						wtfSerienChargennummer.setEnabled(false);
						try {
							wnfMenge.setDouble(null);
						} catch (ExceptionLP ex2) {
							// wird hoffentlich gehen
						}

						wnfMenge.requestFocus();
					}
				}
			} else if (e.getSource() == wtfSerienChargennummer) {
				if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
					wtfArtikelnummer.requestFocus();

				} else if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
					wnfMenge.requestFocus();
				}
			} else if (e.getSource() == wnfMenge || e.getSource() == pa.getWnfEinzelmenge()
					|| e.getSource() == pa.getWnfVerpackungen()) {

				// Auftragsposition speichern
				if (e.getSource() == pa.getWnfEinzelmenge() || e.getSource() == pa.getWnfVerpackungen()) {
					pa.focusLost(new FocusEvent(pa.getWnfEinzelmenge(), -1));
				}
				try {
					if (artikelDto != null && wnfMenge.getBigDecimal() != null) {

						BigDecimal bdVerpackungsmengenMultiplikator=BigDecimal.ONE;
						if(suchNachEan && wtfArtikelnummer.getText().equals(artikelDto.getCVerpackungseannr()) && artikelDto.getFVerpackungsmenge()!=null) {
							bdVerpackungsmengenMultiplikator=new BigDecimal(artikelDto.getFVerpackungsmenge());
						}
						
						if (lieferscheinDto != null) {

							LieferscheinpositionDto aDto = new LieferscheinpositionDto();
							aDto.setBelegIId(lieferscheinDto.getIId());
							aDto.setArtikelIId(artikelDto.getIId());
							aDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
							aDto.setNMenge(wnfMenge.getBigDecimal());
							aDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
							aDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
							aDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
							aDto.setEinheitCNr(artikelDto.getEinheitCNr());
							aDto.setNMenge(wnfMenge.getBigDecimal().multiply(bdVerpackungsmengenMultiplikator));

							if (artikelDto.istArtikelSnrOderchargentragend()
									&& wtfSerienChargennummer.getText() != null) {
								aDto.setSeriennrChargennrMitMenge(
										SeriennrChargennrMitMengeDto.erstelleDtoAusEinerChargennummer(
												wtfSerienChargennummer.getText(), wnfMenge.getBigDecimal()));
							}
							DelegateFactory.getInstance().getLieferscheinpositionDelegate()
									.createLieferscheinposition(aDto, true);
						} else if (rechnungDto != null) {
							RechnungPositionDto aDto = new RechnungPositionDto();
							aDto.setBelegIId(rechnungDto.getIId());
							aDto.setArtikelIId(artikelDto.getIId());
							aDto.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
							aDto.setNMenge(wnfMenge.getBigDecimal());
							aDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
							aDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
							aDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
							aDto.setEinheitCNr(artikelDto.getEinheitCNr());
							aDto.setNMenge(wnfMenge.getBigDecimal().multiply(bdVerpackungsmengenMultiplikator));

							if (artikelDto.istArtikelSnrOderchargentragend()
									&& wtfSerienChargennummer.getText() != null) {
								aDto.setSeriennrChargennrMitMenge(
										SeriennrChargennrMitMengeDto.erstelleDtoAusEinerChargennummer(
												wtfSerienChargennummer.getText(), wnfMenge.getBigDecimal()));
							}
							DelegateFactory.getInstance().getRechnungDelegate().createRechnungposition(aDto,
									rechnungDto.getLagerIId());
						}

						wtfArtikelnummer.setText(null);
						wtfBezeichnung.setText(null);
						wtfSerienChargennummer.setText(null);

						wnfMenge.setDouble(null);
						pa.setVerpackungsmenge(null);
					}
				} catch (Throwable ex) {
					panelPositionen.handleException(ex, true);
				}
				wtfArtikelnummer.requestFocus();

			}

		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}

class DialogLieferscheinpositionenSchnellerfassung_wbuFertig_actionAdapter implements ActionListener {
	private DialogPositionenSchnellerfassung adaptee;

	DialogLieferscheinpositionenSchnellerfassung_wbuFertig_actionAdapter(DialogPositionenSchnellerfassung adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wbuFertig_actionPerformed(e);
	}
}
