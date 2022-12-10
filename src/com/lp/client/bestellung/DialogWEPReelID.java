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
package com.lp.client.bestellung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.client.rechnung.ReportRechnung;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.RueckgabeWEPMitReelIDDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.util.Facade;

@SuppressWarnings("static-access")
public class DialogWEPReelID extends JDialog implements ActionListener, ItemChangedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel1 = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperNumberField wnfMenge = null;
	private WrapperButton wbuHinzufuegen = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();
	private TabbedPaneBestellung tpBestellung = null;

	private WrapperTextField wtfDatecode = new WrapperTextField();
	private WrapperTextField wtfBatch1 = new WrapperTextField();

	private WrapperCheckBox wcbEtikettDrucken = new WrapperCheckBox();

	private WrapperCheckBox wcbLagerplatzRueckpflegen = new WrapperCheckBox();

	private WrapperButton wbuLagerplatz = null;
	private WrapperTextField wtfLagerplatz = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	private PanelQueryFLR panelQueryFLRLagerplatz = null;

	private Integer lagerplatzIId = null;

	static final public String ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE = "action_lagerplatz_from_liste";

	private Integer bestellpositionIId = null;

	public DialogWEPReelID(TabbedPaneBestellung tpBestellung, Integer bestellpositionIId) {
		super(LPMain.getInstance().getDesktop(), "", true);

		this.tpBestellung = tpBestellung;
		this.bestellpositionIId = bestellpositionIId;
		try {
			jbInit();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		this.setSize(350, 230);
		addEscapeListener(this);
		setTitle(LPMain.getInstance().getTextRespectUISPr("bes.wep.button.reelid"));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

	}

	void dialogQueryLagerplatzFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLagerplatz = ArtikelFilterFactory.getInstance()
				.createPanelFLRLagerplatz(tpBestellung.getInternalFrame(), lagerplatzIId, true);

		new DialogQuery(panelQueryFLRLagerplatz);
	}

	public void changed(EventObject eI) {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLagerplatz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				try {
					LagerplatzDto lagerplatzDto = DelegateFactory.getInstance().getLagerDelegate()
							.lagerplatzFindByPrimaryKey((Integer) key);
					wtfLagerplatz.setText(lagerplatzDto.getCLagerplatz());
					lagerplatzIId = lagerplatzDto.getIId();

				} catch (Throwable e1) {
					tpBestellung.handleException(e1, true);
				}

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLagerplatz) {
				wtfLagerplatz.setText(null);
				lagerplatzIId = null;
			}
		}
	}

	public static void addEscapeListener(final JDialog dialog) {
		ActionListener escListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		};

		dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuHinzufuegen)) {
			try {

				if (wnfMenge.getBigDecimal() == null) {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				RueckgabeWEPMitReelIDDto rueckgabeWEPMitReelIDDto = DelegateFactory.getInstance()
						.getWareneingangDelegate().wareneingangspositionMitReelIDBuchen(
								tpBestellung.getWareneingangDto().getIId(), bestellpositionIId,
								wnfMenge.getBigDecimal(), wtfDatecode.getText(), wtfBatch1.getText());

				wnfMenge.setBigDecimal(null);

				BestellpositionDto bsPosDto = DelegateFactory.getInstance().getBestellungDelegate()
						.bestellpositionFindByPrimaryKey(bestellpositionIId);

				if (wcbLagerplatzRueckpflegen.isSelected() && lagerplatzIId != null) {
					DelegateFactory.getInstance().getLagerDelegate().artikellagerplatzCRUD(bsPosDto.getArtikelIId(),
							tpBestellung.getWareneingangDto().getLagerIId(), lagerplatzIId);
				}

				if (wcbEtikettDrucken.isSelected()) {

					WareneingangspositionDto weposDto = DelegateFactory.getInstance().getWareneingangDelegate()
							.wareneingangspositionFindByPrimaryKey(rueckgabeWEPMitReelIDDto.getWeposIId());

					ReportWepEtikett2 reportEtikett = new ReportWepEtikett2(tpBestellung.getInternalFrame(),
							tpBestellung.getWareneingangDto(), weposDto, bsPosDto,
							DelegateFactory.getInstance().getArtikelDelegate()
									.artikelFindByPrimaryKey(bsPosDto.getArtikelIId()),
							DelegateFactory.getInstance().getArtikelDelegate()
									.getArtikelEinkaufspreisDesBevorzugtenLieferanten(bsPosDto.getArtikelIId(),
											BigDecimal.ONE, tpBestellung.getBesDto().getWaehrungCNr()),
							null, rueckgabeWEPMitReelIDDto.getChargennummer(),lagerplatzIId);
					reportEtikett.eventYouAreSelected(false);
					reportEtikett.setKeyWhenDetailPanel(tpBestellung.getWareneingangspositionenDto().getIId());

					PanelReportKriterien krit = new PanelReportKriterien(tpBestellung.getInternalFrame(), reportEtikett,
							"", null, null, false, false, false);
					krit.druckeArchiviereUndSetzeVersandstatusEinesBelegs();
					wnfMenge.requestFocus();
				}

			} catch (Throwable e1) {
				tpBestellung.handleException(e1, true);
			}
		} else if (e.getSource().equals(wbuAbbrechen)) {
			this.setVisible(false);
		} else if (e.getSource().equals(wbuLagerplatz)) {
			try {
				dialogQueryLagerplatzFromListe(null);
			} catch (Throwable e1) {
				tpBestellung.handleException(e1, true);
			}
		}

	}

	private void jbInit() throws Throwable {
		panel1.setLayout(gridBagLayout1);
		wnfMenge = new WrapperNumberField();
		wnfMenge.setMinimumValue(0);
		wnfMenge.setMandatoryField(true);
		wnfMenge.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());

		wbuHinzufuegen.setText(LPMain.getInstance().getTextRespectUISPr("bes.wep.reelid.hinzufuegen"));
		wbuAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		WrapperLabel wlaMenge = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.menge"));
		WrapperLabel wlaDatecode = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("bes.wep.reelid.datecode"));
		WrapperLabel wlaBatch1 = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("bes.wep.reelid.batch1"));

		wbuLagerplatz = new WrapperButton();
		wbuLagerplatz.setText(LPMain.getInstance().getTextRespectUISPr("lp.lagerplatz"));
		wbuLagerplatz.setActionCommand(ACTION_SPECIAL_LAGERPLATZ_FROM_LISTE);
		wbuLagerplatz.addActionListener(this);

		tpBestellung.getInternalFrame().addItemChangedListener(this);

		wcbLagerplatzRueckpflegen
				.setText(LPMain.getInstance().getTextRespectUISPr("bes.wep.reelid.lagerplatzrueckpflegen"));

		wcbEtikettDrucken.setText(LPMain.getInstance().getTextRespectUISPr("bes.wep.reelid.etikettdrucken"));
		wcbEtikettDrucken.setSelected(true);

		wbuHinzufuegen.addActionListener(this);
		wbuAbbrechen.addActionListener(this);

		wtfLagerplatz.setActivatable(false);

		BestellpositionDto bsPosDto = DelegateFactory.getInstance().getBestellungDelegate()
				.bestellpositionFindByPrimaryKey(bestellpositionIId);

		ArtikellagerplaetzeDto dto = DelegateFactory.getInstance().getLagerDelegate()
				.artikellagerplaetzeFindByArtikelIIdLagerIId(bsPosDto.getArtikelIId(),
						tpBestellung.getWareneingangDto().getLagerIId());
		if (dto != null) {
			wtfLagerplatz.setText(dto.getLagerplatzDto().getCLagerplatz());
		}

		add(panel1);
		panel1.add(wlaMenge, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wnfMenge, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wlaDatecode, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wtfDatecode, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wlaBatch1, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wtfBatch1, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wbuLagerplatz, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panel1.add(wtfLagerplatz, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wcbLagerplatzRueckpflegen, new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wcbEtikettDrucken, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panel1.add(wbuHinzufuegen, new GridBagConstraints(0, 6, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 100, 0));
		panel1.add(wbuAbbrechen, new GridBagConstraints(1, 6, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		setContentPane(panel1);

		// Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				/*
				 * Instead of directly closing the window, we're going to change the
				 * JOptionPane's value property.
				 */
				setVisible(false);
			}
		});

	}

	public void clearAndHide() {
		setVisible(false);
	}

}
