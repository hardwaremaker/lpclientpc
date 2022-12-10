package com.lp.client.angebot;

import java.awt.Color;
import java.awt.Dimension;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Query;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperMaterialField;
import com.lp.client.frame.component.WrapperMediaControl;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.frameposition.ClientPerspectiveManager;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.zeiterfassung.ZeiterfassungFilterFactory;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.ejb.Agstklarbeitsplan;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.angebotstkl.service.AgstklmaterialDto;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelSchnellerfassungDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AngebotstklServiceFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogAngebotpositionSchnellerfassung extends JDialog
		implements ActionListener, ItemChangedListener, FocusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelKopfdaten = new JPanel();

	private JSplitPane splitPane = null;

	private InternalFrame intFrame = null;

	private WrapperLabel wlaBezeichnung = null;
	private WrapperTextField wtfBezeichnung = null;

	private WrapperLabel wlaZeichnungsnummer = new WrapperLabel();
	private WrapperTextField wtfZeichnungsnummer = new WrapperTextField(30);

	private WrapperLabel wlaBild = null;
	private WrapperMediaControl wmcMedia = null;

	private BigDecimal[] bdGewichtPreisCache = null;

	private PanelQueryFLR panelQueryFLRArtikel = null;

	private static final String ACTION_SPECIAL_MASCHINE_FROM_LISTE = "action_special_maschine_from_liste";

	private ArrayList<WrapperTextField> wtfMaterialBezeichnung = new ArrayList<WrapperTextField>();
	private ArrayList<WrapperNumberField> wnfMaterialDimension1 = new ArrayList<WrapperNumberField>();
	private ArrayList<WrapperNumberField> wnfMaterialDimension2 = new ArrayList<WrapperNumberField>();
	private ArrayList<WrapperNumberField> wnfMaterialDimension3 = new ArrayList<WrapperNumberField>();

	private ArrayList<WrapperComboBox> wcbMaterialMaterialtyp = new ArrayList<WrapperComboBox>();
	private ArrayList<WrapperNumberField> wnfMaterialGewicht = new ArrayList<WrapperNumberField>();
	private ArrayList<WrapperNumberField> wnfMaterialGewichtPreis = new ArrayList<WrapperNumberField>();

	private PanelQueryFLR[] panelQueryFLRMaschine = null;

	private AgstklDto agstklDto = null;
	private ArrayList<AgstklpositionDto> agstklpositionDtos = new ArrayList<AgstklpositionDto>();
	private ArrayList<AgstklarbeitsplanDto> agstklarbeitsplanDtos = new ArrayList<AgstklarbeitsplanDto>();
	private ArrayList<AgstklmengenstaffelSchnellerfassungDto> agstklmengenstaffelDtos = new ArrayList<AgstklmengenstaffelSchnellerfassungDto>();

	private ArrayList<AgstklmaterialDto> agstklmaterialDtos = new ArrayList<AgstklmaterialDto>();

	private AngebotpositionDto agposDtoVorhanden = null;

	private JScrollPane scrollpanePositionen;
	private JScrollPane scrollpaneArbeitsplan;
	private JScrollPane scrollpaneMengenstaffeln;

	private JScrollPane scrollpaneMaterial;

	private JLabel[] lblMaschinen = null;

	private WrapperLabel wlaPreisAusAngebotposition = new WrapperLabel();

	private WrapperNumberField[] wnfRuestzeit = null;
	private WrapperNumberField[] wnfStueckzeit = null;
	private WrapperCheckBox[] wcbNurMaschine = null;
	private WrapperNumberField[] wnfPreisTaetigkeit = null;

	private WrapperNumberField[] wnfStundesatzMann = null;
	private WrapperNumberField[] wnfStundesatzMaschine = null;

	WrapperLabel wlaSummeGesamtOhneInitialkosten = new WrapperLabel();
	BigDecimal bdInitialkosten = BigDecimal.ZERO;

	private WrapperCheckBox[] wcbIntitialkostenPosition = null;
	private WrapperCheckBox[] wcbIntitialkostenArbeitsplan = null;

	WrapperNumberField[] wnfMaterialMenge = null;
	WrapperNumberField[] wnfMaterialPreis = null;
	WrapperNumberField[] wnfMaterialPreisGesamt = null;
	ArtikelDto[] materialArtikelDto = null;
	WrapperCheckBox[] wcbMaterialRuestmenge = null;

	private ButtonGroup bgMengenstaffel = new ButtonGroup();
	WrapperRadioButton[] wrbMengenstaffelGewaehlt = null;

	private BigDecimal[] materialpreisProKG = null;

	private BigDecimal[] materialgewichtInKG = null;

	private AngebotDto angebotDto = null;
	private Integer angebotpositionIId = null;

	private boolean bMaschinenzeiterfassung = false;

	WrapperLabel wlaSummeRuestzeit = new WrapperLabel();
	WrapperLabel wlaSummeStueckzeit = new WrapperLabel();

	WrapperLabel wlaSummeRuestkosten = new WrapperLabel();
	WrapperLabel wlaSummeStueckkosten = new WrapperLabel();

	public Integer getAngebotpositionIId() {
		return angebotpositionIId;
	}

	int iDefaultHeight = 660;

	WrapperNumberField[] wnfMengenstaffelMenge = null;

	WrapperNumberField[] wnfMengenstaffelAufschlagAZ = null;
	WrapperNumberField[] wnfMengenstaffelAufschlagMaterial = null;
	WrapperNumberField[] wnfMengenstaffelSummeMaterialMitAufschlag = null;
	WrapperNumberField[] wnfMengenstaffelSummeAZ = null;
	WrapperNumberField[] wnfMengenstaffelSummeMaterial = null;
	WrapperNumberField[] wnfMengenstaffelSummeAZMitAufschlag = null;

	WrapperNumberField[] wnfMengenstaffelPreisGesamtBerechnet = null;
	WrapperNumberField[] wnfMengenstaffelPreisGesamtBerechnetProEinheit = null;

	WrapperButton[] wbuPositionEntfernen = null;

	WrapperButton[] wbuMaschine = null;

	private WrapperButton[] wbuArbeitsplanEntfernen = null;
	WrapperButton[] wbuMaterialEntfernen = null;

	private WrapperNumberField wnfSummeAZ = new WrapperNumberField();
	private WrapperNumberField wnfSummePositionen = new WrapperNumberField();
	private WrapperNumberField wnfSummeMaterial = new WrapperNumberField();

	private WrapperNumberField wnfSummeGesamt = new WrapperNumberField();

	private WrapperButton wbuUebernehmen = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	WrapperMaterialField wifNeuMaterial = null;

	WrapperIdentField wifNeuTaetigkeit = null;

	WrapperIdentField wifNeuPosition = null;

	WrapperNumberField wnfMengeHinzufuegen = new WrapperNumberField();
	WrapperButton wbuMengenstaffelAdd = new WrapperButton("x");

	WrapperButton[] wbuMengenstaffelEntfernen = null;

	public DialogAngebotpositionSchnellerfassung(InternalFrame intFrame, AgstklDto agstklDto, Integer angebotIId,
			AngebotpositionDto agposDtoVorhanden) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.intFrame = intFrame;
		this.agstklDto = agstklDto;

		this.angebotDto = DelegateFactory.getInstance().getAngebotDelegate().angebotFindByPrimaryKey(angebotIId);

		this.agposDtoVorhanden = agposDtoVorhanden;

		// PJ22554
		if (agposDtoVorhanden == null) {
			agstklDto.setTBelegdatum(angebotDto.getTBelegdatum());
		}

		AgstklarbeitsplanDto[] agstklarbeitsplanDtosArray = DelegateFactory.getInstance().getAngebotstklDelegate()
				.agstklarbeitsplanFindByAgstklIId(agstklDto.getIId());

		for (int i = 0; i < agstklarbeitsplanDtosArray.length; i++) {
			agstklarbeitsplanDtos.add(agstklarbeitsplanDtosArray[i]);
		}

		AgstklpositionDto[] agstklpositionDtosTemp = DelegateFactory.getInstance().getAngebotstklpositionDelegate()
				.agstklpositionFindByAgstklIIdMengeNotNullOhneExc(agstklDto.getIId());

		for (AgstklpositionDto temp : agstklpositionDtosTemp) {

			// PJ22554
			if (agposDtoVorhanden == null) {

				if (temp.getAgstklpositionsartCNr().equals(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT)) {
					ArtikellieferantDto alDto = DelegateFactory.getInstance().getArtikelDelegate()
							.getArtikelEinkaufspreisDesBevorzugtenLieferanten(temp.getArtikelIId(), BigDecimal.ONE,
									agstklDto.getWaehrungCNr());

					if (alDto != null && alDto.getNNettopreis() != null) {
						temp.setNNettoeinzelpreis(alDto.getNNettopreis());
					}

				}

			}

			agstklpositionDtos.add(temp);
		}

		AgstklmengenstaffelSchnellerfassungDto[] dtosTemp = DelegateFactory.getInstance().getAngebotstklDelegate()
				.agstklmengenstaffelSchnellerfassungFindByAgstklIId(agstklDto.getIId());

		for (AgstklmengenstaffelSchnellerfassungDto temp : dtosTemp) {
			agstklmengenstaffelDtos.add(temp);
		}

		if (dtosTemp.length == 0) {

			AgstklmengenstaffelSchnellerfassungDto zeilDefault = new AgstklmengenstaffelSchnellerfassungDto();

			zeilDefault.setNMenge(BigDecimal.ONE);

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_DEFAULT_ARTIKEL_DECKUNGSBEITRAG,
							ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());

			Double dDefaultAufschlag = 0D;
			if (parameter != null) {
				dDefaultAufschlag = (Double) parameter.getCWertAsObject();
			}

			zeilDefault.setNAufschlagAz(new BigDecimal(dDefaultAufschlag));
			zeilDefault.setNAufschlagMaterial(new BigDecimal(dDefaultAufschlag));

			agstklmengenstaffelDtos.add(zeilDefault);
		}

		agstklmaterialDtos = DelegateFactory.getInstance().getAngebotstklpositionDelegate()
				.agstklmaterialFindByAgstklIId(agstklDto.getIId());

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_MASCHINENZEITERFASSUNG)) {
			bMaschinenzeiterfassung = true;
		}

		try {
			jbInit();

			// PJ22554
			if (agposDtoVorhanden == null) {
				berechneGewicht();
				berechneAlles();
			}

		} catch (Throwable ex) {
			ex.printStackTrace();
		}

		if (agstklDto.getIHoeheDialog() != null && agstklDto.getIHoeheDialog() > 100) {
			iDefaultHeight = agstklDto.getIHoeheDialog();
		}

		String titel = agstklDto.getCNr();
		if (agstklDto.getCBez() != null) {
			titel += " " + agstklDto.getCBez();
		}

		if (Helper.short2boolean(agstklDto.getBVorlage())) {
			setTitle(LPMain.getInstance().getTextRespectUISPr("angb.schnellerfassung.titel") + titel);
		} else {
			setTitle(LPMain.getInstance().getTextRespectUISPr("angb.schnellerfassung.titel2") + titel);
		}

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		this.addEscapeListener(this);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK), "test");
		getRootPane().getActionMap().put("test", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					actionPerformedForStrgS(new ActionEvent(wbuUebernehmen, -1, ""));
				} catch (Throwable e1) {
					e1.printStackTrace();
				}

			}
		});
		hoeheUndBreiteAnpassen();
	}

	private void hoeheUndBreiteAnpassen() {

		AGSchnellerfassungPositionData posData = ClientPerspectiveManager.getInstance()
				.readAGSchnellerfassungPositionData();

		if (posData == null) {
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int screenHeight = screenSize.height - 50;
			int screenWidth = screenSize.width - (screenSize.width / 3);

			this.setSize(screenWidth, screenHeight);
		} else {

			this.setSize(posData.getSize());
			this.setLocation(posData.getLocation());

			splitPane.setDividerLocation(posData.getDividerLocation());

			GridBagLayout gbl = (GridBagLayout) panelKopfdaten.getLayout();
			GridBagConstraints gc = gbl.getConstraints(scrollpaneMaterial);
			gc.weighty = posData.weightMaterial;
			gbl.setConstraints(scrollpaneMaterial, gc);

			gc = gbl.getConstraints(scrollpaneArbeitsplan);
			gc.weighty = posData.weightTaetigkeit;
			gbl.setConstraints(scrollpaneArbeitsplan, gc);

			gc = gbl.getConstraints(scrollpanePositionen);
			gc.weighty = posData.weightPosition;
			gbl.setConstraints(scrollpanePositionen, gc);

			gc = gbl.getConstraints(scrollpaneMengenstaffeln);
			gc.weighty = posData.weightMengenstaffel;
			gbl.setConstraints(scrollpaneMengenstaffeln, gc);

			panelKopfdaten.revalidate();
			panelKopfdaten.repaint();

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

	public void changed(EventObject eI) {
		try {
			ItemChangedEvent e = (ItemChangedEvent) eI;
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
				if (e.getSource() == panelQueryFLRArtikel) {

					Integer artikelIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

					ArtikelDto artikelTempDto = DelegateFactory.getInstance().getArtikelDelegate()
							.artikelFindByPrimaryKey(artikelIId);

					ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelkommentarDelegate()
							.pruefeArtikel(artikelTempDto, null, intFrame);
					if (artikelDto != null) {

						AgstklpositionDto pos = new AgstklpositionDto();
						pos.setAgstklpositionsartCNr(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT);
						pos.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
						pos.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
						pos.setBDrucken(Helper.boolean2Short(false));
						pos.setArtikelIId(artikelDto.getIId());
						pos.setNMenge(BigDecimal.ONE);
						pos.setNNettoeinzelpreis(BigDecimal.ZERO);
						pos.setFRabattsatz(0D);
						pos.setNAufschlag(BigDecimal.ZERO);
						pos.setNNettogesamtpreis(BigDecimal.ZERO);
						pos.setNNettogesamtmitaufschlag(BigDecimal.ZERO);

						agstklpositionDtos.add(pos);

						scrollpanePositionen.setViewportView(createPanelPositionen());
						berechneAlles();

					}

				} else if (e.getSource().equals(wifNeuTaetigkeit)) {
					// ADD ZEILE TAETIGKEIT

					Integer artikelIId = null;
					if (e.getSource().equals(wifNeuTaetigkeit)) {
						artikelIId = ((WrapperIdentField) wifNeuTaetigkeit).getArtikelIId();
					} else {
						artikelIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
					}

					AgstklarbeitsplanDto apTemp = new AgstklarbeitsplanDto();
					apTemp.setArtikelIId(artikelIId);
					apTemp.setLRuestzeit(0L);
					apTemp.setLStueckzeit(0L);
					apTemp.setBNurmaschinenzeit(Helper.boolean2Short(false));

					Integer i = DelegateFactory.getInstance().getAngebotstklDelegate()
							.getNextArbeitsgang(agstklDto.getIId());

					if (i != null) {
						apTemp.setIArbeitsgang(i);
					} else {
						apTemp.setIArbeitsgang(new Integer(10));
					}

					agstklarbeitsplanDtos.add(apTemp);

					scrollpaneArbeitsplan.setViewportView(createPanelTaetigkeiten());
					berechneAlles();

					wifNeuTaetigkeit.setArtikelDto(null);
					wifNeuTaetigkeit.setArtikelIId(null);

					JScrollBar sb = scrollpaneArbeitsplan.getVerticalScrollBar();
					sb.setValue(sb.getMaximum());

				} else if (e.getSource().equals(wifNeuPosition)) {
					// ADD ZEILE POSITION

					Integer artikelIId = null;
					if (e.getSource().equals(wifNeuPosition)) {
						artikelIId = ((WrapperIdentField) wifNeuPosition).getArtikelIId();
					} else {
						artikelIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
					}

					AgstklpositionDto apTemp = new AgstklpositionDto();
					apTemp.setArtikelIId(artikelIId);
					apTemp.setAgstklpositionsartCNr(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT);
					if (artikelIId != null) {
						ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByPrimaryKey(artikelIId);
						apTemp.setEinheitCNr(artikelDto.getEinheitCNr());

						if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
							apTemp.setAgstklpositionsartCNr(AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE);
							apTemp.setNNettoeinzelpreis(BigDecimal.ZERO);
							apTemp.setNNettogesamtpreis(BigDecimal.ZERO);
							apTemp.setNNettogesamtmitaufschlag(BigDecimal.ZERO);
						} else {
							ArtikellieferantDto alDto = DelegateFactory.getInstance().getArtikelDelegate()
									.getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikelIId, BigDecimal.ONE,
											agstklDto.getWaehrungCNr());
							if (alDto != null && alDto.getNNettopreis() != null) {
								apTemp.setNNettoeinzelpreis(alDto.getNNettopreis());
								apTemp.setNNettogesamtpreis(alDto.getNNettopreis());
								apTemp.setNNettogesamtmitaufschlag(alDto.getNNettopreis());
							} else {
								apTemp.setNNettoeinzelpreis(BigDecimal.ZERO);
								apTemp.setNNettogesamtpreis(BigDecimal.ZERO);
								apTemp.setNNettogesamtmitaufschlag(BigDecimal.ZERO);
							}
						}

					}

					apTemp.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
					apTemp.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
					apTemp.setBDrucken(Helper.boolean2Short(false));
					apTemp.setFRabattsatz(0D);
					apTemp.setFZusatzrabattsatz(0D);
					//apTemp.setNMenge(BigDecimal.ONE);

					agstklpositionDtos.add(apTemp);

					scrollpanePositionen.setViewportView(createPanelPositionen());
					berechneAlles();

					wifNeuPosition.setArtikelDto(null);
					wifNeuPosition.getWtfIdent().setText("");

					JScrollBar sb = scrollpanePositionen.getVerticalScrollBar();
					sb.setValue(sb.getMaximum());

				} else if (e.getSource().equals(wifNeuMaterial)) {
					// ADD ZEILE MATERIAL

					Integer materialIId = ((WrapperMaterialField) wifNeuMaterial).getMaterialIId();

					for (int i = 0; i < agstklmaterialDtos.size(); i++) {
						agstklmaterialDtos.get(i).setNDimension1(wnfMaterialDimension1.get(i).getBigDecimal());
						agstklmaterialDtos.get(i).setNDimension2(wnfMaterialDimension2.get(i).getBigDecimal());
						agstklmaterialDtos.get(i).setNDimension3(wnfMaterialDimension3.get(i).getBigDecimal());
						agstklmaterialDtos.get(i)
								.setCMaterialtyp((String) wcbMaterialMaterialtyp.get(i).getKeyOfSelectedItem());
						agstklmaterialDtos.get(i).setCBez(wtfMaterialBezeichnung.get(i).getText());

						agstklmaterialDtos.get(i).setNGewicht(wnfMaterialGewicht.get(i).getBigDecimal());

						agstklmaterialDtos.get(i).setNGewichtpreis(wnfMaterialGewichtPreis.get(i).getBigDecimal());

						agstklmaterialDtos.get(i).setCBez(wtfMaterialBezeichnung.get(i).getText());

					}

					AgstklmaterialDto apTemp = new AgstklmaterialDto();
					apTemp.setMaterialIId(materialIId);
					apTemp.setCMaterialtyp(AngebotstklFac.MATERIALTYP_QUADER);
					apTemp.setNGewichtpreis(BigDecimal.ZERO);

					agstklmaterialDtos.add(apTemp);

					scrollpaneMaterial.setViewportView(createPanelMaterial());
					berechneAlles();

					wifNeuMaterial.setMaterialDto(null);
					wifNeuMaterial.getWtfKennung().setText("");

					JScrollBar sb = scrollpaneMaterial.getVerticalScrollBar();
					sb.setValue(sb.getMaximum());

				} else if (e.getSource().equals(wmcMedia)) {
					if (wmcMedia.getDateiname() != null) {
						if (wmcMedia.getDateiname().contains(".")) {
							wtfZeichnungsnummer.setText(
									wmcMedia.getDateiname().substring(0, wmcMedia.getDateiname().lastIndexOf('.')));
						} else {
							wtfZeichnungsnummer.setText(wmcMedia.getDateiname());
						}
					}

				} else {
					for (int x = 0; x < panelQueryFLRMaschine.length; x++) {
						if (e.getSource().equals(panelQueryFLRMaschine[x])) {
							Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
							MaschineDto maschineDto = null;
							maschineDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
									.maschineFindByPrimaryKey(key);
							agstklarbeitsplanDtos.get(x).setMaschineIId(maschineDto.getIId());
							lblMaschinen[x].setText(maschineDto.getBezeichnung());

							BigDecimal bdKostenMaschine = DelegateFactory.getInstance().getZeiterfassungDelegate()
									.getMaschinenKostenZumZeitpunkt(maschineDto.getIId(),
											LocaleFac.BELEGART_AGSTUECKLISTE, agstklarbeitsplanDtos.get(x).getIId());

							wnfStundesatzMaschine[x].setBigDecimal(bdKostenMaschine);

							// PJ22553

							if (agstklarbeitsplanDtos.get(x).getMaschineIId() != null
									&& agstklarbeitsplanDtos.get(x).getAgartCNr() == null
									&& agstklarbeitsplanDtos.get(x).getIArbeitsgang() != 0) {
								ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
										.artikelFindByPrimaryKeySmallOhneExc(
												agstklarbeitsplanDtos.get(x).getArtikelIId());
								if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {
									for (int y = x + 1; y < agstklarbeitsplanDtos.size(); y++) {

										if (!agstklarbeitsplanDtos.get(x).getIId()
												.equals(agstklarbeitsplanDtos.get(y).getIId())) {

											if (agstklarbeitsplanDtos.get(x).getIArbeitsgang()
													.equals(agstklarbeitsplanDtos.get(y).getIArbeitsgang())) {

												ArtikelDto artikelDtoPos = DelegateFactory.getInstance()
														.getArtikelDelegate().artikelFindByPrimaryKeySmallOhneExc(
																agstklarbeitsplanDtos.get(y).getArtikelIId());
												if (Helper.short2boolean(artikelDtoPos.getbReinemannzeit()) == false) {
													agstklarbeitsplanDtos.get(y).setMaschineIId(
															agstklarbeitsplanDtos.get(x).getMaschineIId());
													lblMaschinen[y].setText(maschineDto.getBezeichnung());
													wnfStundesatzMaschine[y].setBigDecimal(bdKostenMaschine);

												}
											}
										}

									}

								}
							}

							berechneAlles();

						}
					}
				}

			} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

				for (int x = 0; x < panelQueryFLRMaschine.length; x++) {
					if (e.getSource().equals(panelQueryFLRMaschine[x])) {

						agstklarbeitsplanDtos.get(x).setMaschineIId(null);
						lblMaschinen[x].setText("");
					}
				}

			}
		} catch (Throwable e1) {
			intFrame.handleException(e1, false);
		}
	}

	private void jbInit() throws Throwable {
		// panelKopfdaten = new JPanel(new MigLayout("wrap 10",
		// "[12%][15%][10%][6%][6%][6%][10%][8%][3%][100%]"));

		panelKopfdaten = new JPanel(new GridBagLayout());

		wbuAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr("Cancel"));

		wbuAbbrechen.addActionListener(this);

		wbuUebernehmen.setText(LPMain.getInstance().getTextRespectUISPr("angb.schnellerfassung.uebernehmen"));
		wbuUebernehmen.addActionListener(this);

		if (!AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT.equals(angebotDto.getStatusCNr())) {
			wbuUebernehmen.setEnabled(false);
		}

		intFrame.addItemChangedListener(this);

		wlaBezeichnung = new WrapperLabel();
		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("agstkl.projekt.bezeichnung"));

		wlaZeichnungsnummer.setText(LPMain.getInstance().getTextRespectUISPr("agstkl.projekt.zeichnungsnummer"));

		wlaBild = new WrapperLabel();
		wlaBild.setText(LPMain.getTextRespectUISPr("lp.bild"));

		wmcMedia = new WrapperMediaControl(intFrame, "");

		wtfBezeichnung = new WrapperTextField(AuftragFac.MAX_AUFT_AUFTRAG_PROJEKTBEZEICHNUNG);

		wtfBezeichnung.setMandatoryField(true);

		wbuMengenstaffelAdd.setText("+ Mengenstaffel...");
		wbuMengenstaffelAdd.addActionListener(this);

		int iZeile = 0;

		int iWidthScrollpane = Defaults.sizeFactor(780) + 100;

		// KOPFDATEN

		panelKopfdaten.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelKopfdaten.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 1, 1, 0.4, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelKopfdaten.add(wlaZeichnungsnummer, new GridBagConstraints(2, iZeile, 1, 1, 0.2, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelKopfdaten.add(wtfZeichnungsnummer, new GridBagConstraints(3, iZeile, 1, 1, 0.4, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		JLabel lbl = new JLabel("");

		panelKopfdaten.add(lbl, new GridBagConstraints(4, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 40, 0));

		JPanel panelEditor = new JPanel(new GridBagLayout());
		panelEditor.add(wmcMedia, new GridBagConstraints(0, iZeile, 1, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -550, 0));

		// Material

		wifNeuMaterial = new WrapperMaterialField(intFrame, null);
		wifNeuMaterial.getWtfKennung().setMandatoryField(false);
		iZeile++;

		scrollpaneMaterial = new JScrollPane(createPanelMaterial());
		scrollpaneMaterial.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		scrollpaneMaterial.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelKopfdaten.add(scrollpaneMaterial, new GridBagConstraints(0, iZeile, 5, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		wnfSummeMaterial.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wnfSummeMaterial.setDependenceField(true);
		wnfSummeMaterial.setEditable(false);
		iZeile++;

		wifNeuMaterial.getWbuMaterial().setText("+ " + LPMain.getTextRespectUISPr("as.material") + "...");

		panelKopfdaten.add(wifNeuMaterial.getWbuMaterial(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelKopfdaten.add(wifNeuMaterial.getWtfKennung(), new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelKopfdaten.add(new WrapperLabel("Summe Material"), new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelKopfdaten.add(wnfSummeMaterial, new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// TAETIGKEITEN

		wifNeuTaetigkeit = new WrapperIdentField(intFrame, null, false, true);
		wifNeuTaetigkeit.getWtfIdent().setMandatoryField(false);
		iZeile++;

		scrollpaneArbeitsplan = new JScrollPane(createPanelTaetigkeiten());
		scrollpaneArbeitsplan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		scrollpaneArbeitsplan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelKopfdaten.add(scrollpaneArbeitsplan, new GridBagConstraints(0, iZeile, 5, 1, 0, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		wnfSummeAZ.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wnfSummeAZ.setDependenceField(true);
		wnfSummeAZ.setEditable(false);
		iZeile++;

		wifNeuTaetigkeit.getWbuArtikel().setText("+ " + LPMain.getTextRespectUISPr("button.taetigkeit"));
		wifNeuTaetigkeit.getWbuArtikel().getWrapperButtonGoTo().setVisible(false);

		panelKopfdaten.add(wifNeuTaetigkeit.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelKopfdaten.add(wifNeuTaetigkeit.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelKopfdaten.add(new WrapperLabel("Summe AZ"), new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelKopfdaten.add(wnfSummeAZ, new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		scrollpanePositionen = new JScrollPane(createPanelPositionen());
		scrollpanePositionen.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		scrollpanePositionen.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelKopfdaten.add(scrollpanePositionen, new GridBagConstraints(0, iZeile, 5, 1, 0, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		wnfSummePositionen.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wnfSummePositionen.setDependenceField(true);
		wnfSummePositionen.setEditable(false);
		iZeile++;

		wifNeuPosition = new WrapperIdentField(intFrame, null, true, false);
		wifNeuPosition.getWbuArtikel().setText("+ Position...");
		wifNeuPosition.getWbuArtikel().getWrapperButtonGoTo().setVisible(false);
		wifNeuPosition.getWtfIdent().setMandatoryField(false);

		panelKopfdaten.add(wifNeuPosition.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelKopfdaten.add(wifNeuPosition.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelKopfdaten.add(new WrapperLabel("Summe Positionen"), new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelKopfdaten.add(wnfSummePositionen, new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		// panelKopfdaten.add(new JLabel(""), " width 38:38:38, wrap");

		wnfSummeGesamt.setDependenceField(true);
		wnfSummeGesamt.setEditable(false);

		iZeile++;

		wlaSummeGesamtOhneInitialkosten.setText("Summe Gesamt");

		panelKopfdaten.add(wlaSummeGesamtOhneInitialkosten, new GridBagConstraints(1, iZeile, 2, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelKopfdaten.add(wnfSummeGesamt, new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		// panelKopfdaten.add(new JLabel(""), " width 38:38:38, wrap");

		// MENGENSTAFFELN

		iZeile++;

		scrollpaneMengenstaffeln = new JScrollPane(createPanelMengenstaffel());
		scrollpaneMengenstaffeln.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		scrollpaneMengenstaffeln.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelKopfdaten.add(scrollpaneMengenstaffeln, new GridBagConstraints(0, iZeile, 5, 1, 0, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		wnfMengeHinzufuegen.addFocusListener(this);

		iZeile++;

		panelKopfdaten.add(wbuMengenstaffelAdd, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelKopfdaten.add(wnfMengeHinzufuegen, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		panelKopfdaten.add(wlaPreisAusAngebotposition, new GridBagConstraints(3, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (agposDtoVorhanden != null && agposDtoVorhanden.getNNettoeinzelpreis() != null) {
			wlaPreisAusAngebotposition.setText(LPMain.getTextRespectUISPr("agstkl.schnellefassung.preisausagpos")
					+ Helper.formatZahl(agposDtoVorhanden.getNNettoeinzelpreis(),
							Defaults.getInstance().getIUINachkommastellenPreiseVK(),
							LPMain.getInstance().getTheClient().getLocUi()));

		}

		iZeile++;
		panelKopfdaten.add(wbuUebernehmen, new GridBagConstraints(0, iZeile, 2, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		panelKopfdaten.add(wbuAbbrechen, new GridBagConstraints(2, iZeile, 2, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelKopfdaten, panelEditor);
		splitPane.setDividerLocation(iWidthScrollpane);

		getContentPane().add(splitPane);

		wmcMedia.setMimeType(agstklDto.getDatenformatCNr());
		wmcMedia.setOMedia(agstklDto.getOMedia());
		wmcMedia.setDateiname(agstklDto.getCDateiname());

		wtfZeichnungsnummer.setText(agstklDto.getCZeichnungsnummer());

		// berechneGewicht();

		if (agposDtoVorhanden != null) {

			wtfBezeichnung.setText(agstklDto.getCBez());

		}

		berechneAlles();

		// Aufschlag rueckrechnen

		if (agposDtoVorhanden != null && agposDtoVorhanden.getNMenge() != null) {
			for (int x = 0; x < wnfMengenstaffelPreisGesamtBerechnetProEinheit.length; x++) {

				if (wnfMengenstaffelMenge[x].getBigDecimal() != null && wnfMengenstaffelMenge[x].getBigDecimal()
						.doubleValue() == agposDtoVorhanden.getNMenge().doubleValue()) {

					wnfMengenstaffelPreisGesamtBerechnetProEinheit[x].setBigDecimal(
							agposDtoVorhanden.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

					wrbMengenstaffelGewaehlt[x].setSelected(true);
				}

			}
		}

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

	private JPanel createPanelMaterial() throws ExceptionLP, Throwable {

		int iZeile = 0;

		wtfMaterialBezeichnung = new ArrayList<WrapperTextField>();
		wnfMaterialDimension1 = new ArrayList<WrapperNumberField>();
		wnfMaterialDimension2 = new ArrayList<WrapperNumberField>();
		wnfMaterialDimension3 = new ArrayList<WrapperNumberField>();

		wcbMaterialMaterialtyp = new ArrayList<WrapperComboBox>();
		wnfMaterialGewicht = new ArrayList<WrapperNumberField>();
		wnfMaterialGewichtPreis = new ArrayList<WrapperNumberField>();

		Map m = new LinkedHashMap();
		m.put(AngebotstklFac.MATERIALTYP_QUADER, AngebotstklFac.MATERIALTYP_QUADER.trim() + " "
				+ LPMain.getTextRespectUISPr("angb.schnellerfassung.materialtyp.quader"));
		m.put(AngebotstklFac.MATERIALTYP_RUND, AngebotstklFac.MATERIALTYP_RUND.trim() + " "
				+ LPMain.getTextRespectUISPr("angb.schnellerfassung.materialtyp.rund"));
		m.put(AngebotstklFac.MATERIALTYP_ROHR, AngebotstklFac.MATERIALTYP_ROHR.trim() + " "
				+ LPMain.getTextRespectUISPr("angb.schnellerfassung.materialtyp.rohr"));

		JPanel panelMaterial = new JPanel();

		panelMaterial.setLayout(new GridBagLayout());

		panelMaterial.add(new JLabel(LPMain.getTextRespectUISPr("lp.bezeichnung")), new GridBagConstraints(0, iZeile, 1,
				1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 50, 00));

		panelMaterial.add(new JLabel(LPMain.getTextRespectUISPr("agstkl.material")), new GridBagConstraints(1, iZeile,
				1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 50, 00));

		panelMaterial.add(new WrapperLabel(agstklDto.getWaehrungCNr() + "/kg"), new GridBagConstraints(2, iZeile, 1, 1,
				0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 50, 00));

		panelMaterial.add(new JLabel(LPMain.getTextRespectUISPr("agstkl.materialtyp")),
				new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 50, 00));

		panelMaterial.add(new JLabel("Dim1"), new GridBagConstraints(4, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 00, 00));
		panelMaterial.add(new JLabel("Dim2"), new GridBagConstraints(5, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 00, 00));
		panelMaterial.add(new JLabel("Dim3"), new GridBagConstraints(6, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 00, 00));
		panelMaterial.add(new JLabel("Gewicht [kg]"), new GridBagConstraints(7, iZeile, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 00));

		panelMaterial.add(
				new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.preiszeile") + " "
						+ agstklDto.getWaehrungCNr().trim()),
				new GridBagConstraints(8, iZeile, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 00));

		AGSchnellerfassungHeightController hcMaterial = new AGSchnellerfassungHeightController(panelKopfdaten);

		panelMaterial.add(hcMaterial, new GridBagConstraints(9, iZeile, 1, 1, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 20));

		bdGewichtPreisCache = new BigDecimal[agstklmaterialDtos.size()];
		materialpreisProKG = new BigDecimal[agstklmaterialDtos.size()];
		materialgewichtInKG = new BigDecimal[agstklmaterialDtos.size()];

		wbuMaterialEntfernen = new WrapperButton[agstklmaterialDtos.size()];

		for (int i = 0; i < agstklmaterialDtos.size(); i++) {

			AgstklmaterialDto matDto = agstklmaterialDtos.get(i);

			iZeile++;

			MaterialDto materialDto = DelegateFactory.getInstance().getMaterialDelegate()
					.materialFindByPrimaryKey(matDto.getMaterialIId());

			materialpreisProKG[i] = DelegateFactory.getInstance().getMaterialDelegate().getMaterialpreisInZielwaehrung(
					materialDto.getIId(), agstklDto.getTBelegdatum(), agstklDto.getWaehrungCNr());

			if (materialDto.getNGewichtInKG() != null) {
				materialgewichtInKG[i] = materialDto.getNGewichtInKG();
			} else {
				materialgewichtInKG[i] = BigDecimal.ZERO;
			}

			WrapperTextField wtfMaterialBezeichnungLocal = new WrapperTextField(80);
			wtfMaterialBezeichnungLocal.setText(matDto.getCBez());
			wtfMaterialBezeichnung.add(wtfMaterialBezeichnungLocal);

			WrapperComboBox wcbMaterialtypLocal = new WrapperComboBox();

			wcbMaterialtypLocal.setMap(m, false);
			wcbMaterialtypLocal.setKeyOfSelectedItem(agstklmaterialDtos.get(i).getCMaterialtyp());
			wcbMaterialtypLocal.addActionListener(this);
			wcbMaterialMaterialtyp.add(wcbMaterialtypLocal);

			panelMaterial.add(wtfMaterialBezeichnungLocal, new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			JLabel materialBezeichnung = new JLabel(materialDto.getBezeichnung());

			Dimension d = new Dimension(100, Defaults.getInstance().getControlHeight());

			materialBezeichnung.setPreferredSize(d);
			materialBezeichnung.setMaximumSize(d);

			panelMaterial.add(materialBezeichnung, new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			panelMaterial.add(
					new WrapperLabel(Helper.formatZahl(materialgewichtInKG[i],
							Defaults.getInstance().getIUINachkommastellenPreiseEK(), LPMain.getTheClient().getLocUi())),
					new GridBagConstraints(2, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 0, 0));

			panelMaterial.add(wcbMaterialMaterialtyp.get(i), new GridBagConstraints(3, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			WrapperNumberField wnfDim1 = new WrapperNumberField(4);
			wnfDim1.setBigDecimal(matDto.getNDimension1());
			wnfDim1.addFocusListener(this);
			wnfMaterialDimension1.add(wnfDim1);

			panelMaterial.add(wnfMaterialDimension1.get(i), new GridBagConstraints(4, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -20, 0));

			WrapperNumberField wnfDim2 = new WrapperNumberField(4);
			wnfDim2.setBigDecimal(matDto.getNDimension2());
			wnfDim2.addFocusListener(this);
			wnfMaterialDimension2.add(wnfDim2);

			panelMaterial.add(wnfMaterialDimension2.get(i), new GridBagConstraints(5, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -20, 0));

			WrapperNumberField wnfDim3 = new WrapperNumberField(4);
			wnfDim3.setBigDecimal(matDto.getNDimension3());
			wnfDim3.addFocusListener(this);
			wnfMaterialDimension3.add(wnfDim3);

			if (wcbMaterialtypLocal.getKeyOfSelectedItem() != null) {

				if (wcbMaterialtypLocal.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_RUND)) {
					wnfMaterialDimension3.get(i).setEditable(false);
					wnfMaterialDimension3.get(i).setBigDecimal(null);

				}

			}

			panelMaterial.add(wnfMaterialDimension3.get(i), new GridBagConstraints(6, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -20, 0));

			WrapperNumberField wnfGewicht = new WrapperNumberField(3);
			wnfGewicht.setBigDecimal(matDto.getNGewicht());
			wnfGewicht.addFocusListener(this);
			wnfMaterialGewicht.add(wnfGewicht);

			panelMaterial.add(wnfMaterialGewicht.get(i), new GridBagConstraints(7, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			WrapperNumberField wnfGewichtPreis = new WrapperNumberField(6);
			wnfGewichtPreis.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
			wnfGewichtPreis.setBigDecimal(matDto.getNGewichtpreis());
			wnfGewichtPreis.addFocusListener(this);

			wnfMaterialGewichtPreis.add(wnfGewichtPreis);

			panelMaterial.add(wnfMaterialGewichtPreis.get(i), new GridBagConstraints(8, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			wbuMaterialEntfernen[i] = new WrapperButton("X");
			wbuMaterialEntfernen[i].addActionListener(this);
			wbuMaterialEntfernen[i].setForeground(Color.RED);

			panelMaterial.add(wbuMaterialEntfernen[i], new GridBagConstraints(9, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 10, 0));

			// berechneGewicht(i);

		}

		iZeile++;
		panelMaterial.add(new JLabel(""), new GridBagConstraints(1, iZeile, 1, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		return panelMaterial;
	}

	private JPanel createPanelTaetigkeiten() throws ExceptionLP, Throwable {
		JPanel panelArbeitsplan = new JPanel();
		panelArbeitsplan.setLayout(new GridBagLayout());

		wnfRuestzeit = new WrapperNumberField[agstklarbeitsplanDtos.size()];
		wnfStueckzeit = new WrapperNumberField[agstklarbeitsplanDtos.size()];
		wcbNurMaschine = new WrapperCheckBox[agstklarbeitsplanDtos.size()];
		wcbIntitialkostenArbeitsplan = new WrapperCheckBox[agstklarbeitsplanDtos.size()];

		wnfPreisTaetigkeit = new WrapperNumberField[agstklarbeitsplanDtos.size()];
		wnfStundesatzMann = new WrapperNumberField[agstklarbeitsplanDtos.size()];
		wnfStundesatzMaschine = new WrapperNumberField[agstklarbeitsplanDtos.size()];

		wbuMaschine = new WrapperButton[agstklarbeitsplanDtos.size()];
		panelQueryFLRMaschine = new PanelQueryFLR[agstklarbeitsplanDtos.size()];

		lblMaschinen = new JLabel[agstklarbeitsplanDtos.size()];

		wbuArbeitsplanEntfernen = new WrapperButton[agstklarbeitsplanDtos.size()];

		int iZeileLocal = 0;

		JLabel lblInitialkosten = new JLabel("In");
		lblInitialkosten.setToolTipText(LPMain.getTextRespectUISPr("stkl.positionen.initialkosten.tooltip"));

		panelArbeitsplan.add(lblInitialkosten, new GridBagConstraints(0, iZeileLocal, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 00));

		panelArbeitsplan.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.taetigkeit")),
				new GridBagConstraints(1, iZeileLocal, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 00));

		panelArbeitsplan.add(new WrapperLabel(agstklDto.getWaehrungCNr() + "/h T"),
				new GridBagConstraints(2, iZeileLocal, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 00));

		if (bMaschinenzeiterfassung == true) {

			panelArbeitsplan.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.maschine")),
					new GridBagConstraints(3, iZeileLocal, 1, 1, 0, 0, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 50, 00));
			panelArbeitsplan.add(new WrapperLabel(agstklDto.getWaehrungCNr() + "/h M"),
					new GridBagConstraints(4, iZeileLocal, 1, 1, 0, 0, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 00));
		}
		panelArbeitsplan.add(new WrapperLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.ruestzeit")),
				new GridBagConstraints(5, iZeileLocal, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 50, 00));
		panelArbeitsplan.add(new WrapperLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.stueckzeit")),
				new GridBagConstraints(6, iZeileLocal, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 50, 00));

		if (bMaschinenzeiterfassung == true) {
			panelArbeitsplan.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.nurmaschine")),
					new GridBagConstraints(7, iZeileLocal, 1, 1, 0, 0, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 20, 00));
		}

		panelArbeitsplan.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.preiszeile") + " "
						+ agstklDto.getWaehrungCNr().trim()),
				new GridBagConstraints(8, iZeileLocal, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 80, 00));

		AGSchnellerfassungHeightController hc = new AGSchnellerfassungHeightController(panelKopfdaten);
		panelArbeitsplan.add(hc, new GridBagConstraints(9, iZeileLocal, 1, 1, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 20));

		for (int i = 0; i < agstklarbeitsplanDtos.size(); i++) {

			AgstklarbeitsplanDto apDto = agstklarbeitsplanDtos.get(i);

			wnfRuestzeit[i] = new WrapperNumberField(4);
			wnfStueckzeit[i] = new WrapperNumberField(4);
			wcbNurMaschine[i] = new WrapperCheckBox();
			wcbIntitialkostenArbeitsplan[i] = new WrapperCheckBox();

			wnfPreisTaetigkeit[i] = new WrapperNumberField(5);

			wnfRuestzeit[i].setMandatoryField(true);
			wnfStueckzeit[i].setMandatoryField(true);

			wnfRuestzeit[i].addFocusListener(this);
			wnfStueckzeit[i].addFocusListener(this);

			wnfRuestzeit[i].setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());
			wnfStueckzeit[i].setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());

			wnfPreisTaetigkeit[i].setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
			wnfPreisTaetigkeit[i].setDependenceField(true);
			wnfPreisTaetigkeit[i].setEditable(false);

			wnfRuestzeit[i].setBigDecimal(Helper.long2StundenBigDecimal(apDto.getLRuestzeit()));
			wnfStueckzeit[i].setBigDecimal(Helper.long2MinutenBigDecimal(apDto.getLStueckzeit()));

			wcbNurMaschine[i].setShort(apDto.getBNurmaschinenzeit());
			wcbNurMaschine[i].addActionListener(this);

			wcbIntitialkostenArbeitsplan[i].setShort(apDto.getBInitial());
			wcbIntitialkostenArbeitsplan[i].addActionListener(this);

			wbuArbeitsplanEntfernen[i] = new WrapperButton("X");
			wbuArbeitsplanEntfernen[i].addActionListener(this);
			wbuArbeitsplanEntfernen[i].setForeground(Color.RED);

			wnfStundesatzMann[i] = new WrapperNumberField(4);
			wnfStundesatzMann[i].setMandatoryField(true);
			wnfStundesatzMaschine[i] = new WrapperNumberField(4);
			wnfStundesatzMaschine[i].setMandatoryField(true);

			wnfStundesatzMann[i].addFocusListener(this);
			wnfStundesatzMaschine[i].addFocusListener(this);

			iZeileLocal++;

			ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(apDto.getArtikelIId());

			BigDecimal preisPersonal = DelegateFactory.getInstance().getLagerDelegate()
					.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
							agstklarbeitsplanDtos.get(i).getArtikelIId());

			if (preisPersonal == null) {
				preisPersonal = new BigDecimal(0);
			}

			if (agstklarbeitsplanDtos.get(i).getNStundensatzMann() != null) {
				preisPersonal = agstklarbeitsplanDtos.get(i).getNStundensatzMann();
			}

			wnfStundesatzMann[i].setBigDecimal(preisPersonal);

			BigDecimal bdKostenMaschine = BigDecimal.ZERO;
			if (agstklarbeitsplanDtos.get(i).getMaschineIId() != null) {
				bdKostenMaschine = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.getMaschinenKostenZumZeitpunkt(agstklarbeitsplanDtos.get(i).getMaschineIId(),
								LocaleFac.BELEGART_AGSTUECKLISTE, agstklarbeitsplanDtos.get(i).getIId());
			}

			if (agstklarbeitsplanDtos.get(i).getNStundensatzMaschine() != null) {
				bdKostenMaschine = agstklarbeitsplanDtos.get(i).getNStundensatzMaschine();
			}

			wnfStundesatzMaschine[i].setBigDecimal(bdKostenMaschine);

			JLabel lblTaetigkeit = new JLabel(artikelDto.formatArtikelbezeichnung());

			Dimension d = new Dimension(200, Defaults.getInstance().getControlHeight());

			lblTaetigkeit.setPreferredSize(d);
			lblTaetigkeit.setMaximumSize(d);

			panelArbeitsplan.add(wcbIntitialkostenArbeitsplan[i], new GridBagConstraints(0, iZeileLocal, 1, 1, 0, 0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 10, 0));

			panelArbeitsplan.add(lblTaetigkeit, new GridBagConstraints(1, iZeileLocal, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			panelArbeitsplan.add(wnfStundesatzMann[i], new GridBagConstraints(2, iZeileLocal, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -20, 0));

			wbuMaschine[i] = new WrapperButton("M");
			wbuMaschine[i].addActionListener(this);
			wbuMaschine[i].setActionCommand(ACTION_SPECIAL_MASCHINE_FROM_LISTE);

			lblMaschinen[i] = new JLabel("");

			Dimension d2 = new Dimension(120, Defaults.getInstance().getControlHeight());

			lblMaschinen[i].setPreferredSize(d2);
			lblMaschinen[i].setMaximumSize(d2);

			if (apDto.getMaschineIId() != null) {
				MaschineDto maschineDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.maschineFindByPrimaryKey(apDto.getMaschineIId());
				lblMaschinen[i] = new JLabel(maschineDto.getBezeichnung());

			}
			if (bMaschinenzeiterfassung == true) {
				panelArbeitsplan.add(wbuMaschine[i], new GridBagConstraints(3, iZeileLocal, 1, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 0, 2, 2), 10, 0));
				panelArbeitsplan.add(lblMaschinen[i], new GridBagConstraints(3, iZeileLocal, 1, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 25, 2, 2), 0, 0));
				panelArbeitsplan.add(wnfStundesatzMaschine[i], new GridBagConstraints(4, iZeileLocal, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -20, 0));
			}

			panelArbeitsplan.add(wnfRuestzeit[i], new GridBagConstraints(5, iZeileLocal, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -30, 0));
			panelArbeitsplan.add(wnfStueckzeit[i], new GridBagConstraints(6, iZeileLocal, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -30, 0));

			if (bMaschinenzeiterfassung == true) {
				panelArbeitsplan.add(wcbNurMaschine[i], new GridBagConstraints(7, iZeileLocal, 1, 1, 0, 0,
						GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 20, 0));
			}
			panelArbeitsplan.add(wnfPreisTaetigkeit[i], new GridBagConstraints(8, iZeileLocal, 1, 1, 0, 0,
					GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
			panelArbeitsplan.add(wbuArbeitsplanEntfernen[i], new GridBagConstraints(9, iZeileLocal, 1, 1, 0, 0,
					GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 10, 0));

		}

		if (agstklarbeitsplanDtos.size() > 0) {
			iZeileLocal++;
			panelArbeitsplan.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.preiszeile")),
					new GridBagConstraints(4, iZeileLocal, 1, 1, 0, 0, GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
			panelArbeitsplan.add(wlaSummeRuestzeit, new GridBagConstraints(5, iZeileLocal, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 5), 0, 0));
			panelArbeitsplan.add(wlaSummeStueckzeit, new GridBagConstraints(6, iZeileLocal, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 5), 0, 0));

			iZeileLocal++;
			panelArbeitsplan.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.kosten")),
					new GridBagConstraints(4, iZeileLocal, 1, 1, 0, 0, GridBagConstraints.CENTER,
							GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
			panelArbeitsplan.add(wlaSummeRuestkosten, new GridBagConstraints(5, iZeileLocal, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 5), 0, 0));
			panelArbeitsplan.add(wlaSummeStueckkosten, new GridBagConstraints(6, iZeileLocal, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 5), 0, 0));

		}

		iZeileLocal++;
		panelArbeitsplan.add(new JLabel(""), new GridBagConstraints(2, iZeileLocal, 1, 1, 0, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 0), 0, 0));

		return panelArbeitsplan;
	}

	private JPanel createPanelPositionen() throws ExceptionLP, Throwable {
		// POSITIONEN
		int iZeile = 0;
		JPanel panelPositionen = new JPanel();
		panelPositionen.setLayout(new GridBagLayout());

		wnfMaterialMenge = new WrapperNumberField[agstklpositionDtos.size()];
		wnfMaterialPreis = new WrapperNumberField[agstklpositionDtos.size()];
		wnfMaterialPreisGesamt = new WrapperNumberField[agstklpositionDtos.size()];
		wbuPositionEntfernen = new WrapperButton[agstklpositionDtos.size()];
		materialArtikelDto = new ArtikelDto[agstklpositionDtos.size()];
		wcbMaterialRuestmenge = new WrapperCheckBox[agstklpositionDtos.size()];
		wcbIntitialkostenPosition = new WrapperCheckBox[agstklpositionDtos.size()];

		JLabel lblInitialkosten = new JLabel("In");
		lblInitialkosten.setToolTipText(LPMain.getTextRespectUISPr("stkl.positionen.initialkosten.tooltip"));

		panelPositionen.add(lblInitialkosten, new GridBagConstraints(0, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 00));
		panelPositionen.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.positionen")),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		panelPositionen.add(new WrapperLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.menge")),
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 100, 0));
		panelPositionen.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.einheit")),
				new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 50, 0));

		panelPositionen.add(new JLabel(LPMain.getTextRespectUISPr("agstkl.positionen.schnellerfassung.ruestmenge")),
				new GridBagConstraints(4, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 10, 0));

		panelPositionen.add(new WrapperLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.preiseinheit")),
				new GridBagConstraints(5, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 00, 0));
		panelPositionen.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.preiszeile") + " "
						+ agstklDto.getWaehrungCNr().trim()),
				new GridBagConstraints(7, iZeile, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 00, 0));

		AGSchnellerfassungHeightController hc = new AGSchnellerfassungHeightController(panelKopfdaten);
		panelPositionen.add(hc, new GridBagConstraints(8, iZeile, 1, 1, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 20));

		if (agstklpositionDtos.size() > 0) {

			for (int i = 0; i < agstklpositionDtos.size(); i++) {

				AgstklpositionDto posDto = agstklpositionDtos.get(i);

				wnfMaterialMenge[i] = new WrapperNumberField(6);
				wnfMaterialPreis[i] = new WrapperNumberField(6);
				wnfMaterialPreisGesamt[i] = new WrapperNumberField(6);

				wnfMaterialMenge[i].setMandatoryField(true);
				wnfMaterialPreis[i].setMandatoryField(true);

				wnfMaterialMenge[i].addFocusListener(this);
				wnfMaterialPreis[i].addFocusListener(this);

				wnfMaterialMenge[i].setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());
				wnfMaterialPreis[i].setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
				wnfMaterialPreisGesamt[i].setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());

				wnfMaterialPreisGesamt[i].addFocusListener(this);

				wnfMaterialMenge[i].setBigDecimal(agstklpositionDtos.get(i).getNMenge());
				wnfMaterialPreis[i].setBigDecimal(agstklpositionDtos.get(i).getNNettoeinzelpreis());

				wbuPositionEntfernen[i] = new WrapperButton();
				wbuPositionEntfernen[i].setText("X");
				wbuPositionEntfernen[i].setForeground(Color.RED);
				wbuPositionEntfernen[i].addActionListener(this);

				wcbMaterialRuestmenge[i] = new WrapperCheckBox();

				wcbMaterialRuestmenge[i].addActionListener(this);

				wcbMaterialRuestmenge[i].setShort(agstklpositionDtos.get(i).getBRuestmenge());

				wcbIntitialkostenPosition[i] = new WrapperCheckBox();

				wcbIntitialkostenPosition[i].addActionListener(this);

				wcbIntitialkostenPosition[i].setShort(agstklpositionDtos.get(i).getBInitial());

				iZeile++;

				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(posDto.getArtikelIId());

				materialArtikelDto[i] = artikelDto;

				JLabel lblArtikel = new JLabel(artikelDto.formatArtikelbezeichnung());

				Dimension d = new Dimension(270, Defaults.getInstance().getControlHeight());

				lblArtikel.setPreferredSize(d);
				lblArtikel.setMaximumSize(d);

				panelPositionen.add(wcbIntitialkostenPosition[i], new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 10, 0));

				panelPositionen.add(lblArtikel, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
						GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

				panelPositionen.add(wnfMaterialMenge[i], new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

				if (artikelDto.getEinheitCNr().equals(SystemFac.EINHEIT_METER)) {
					panelPositionen.add(new JLabel(SystemFac.EINHEIT_MILLIMETER), new GridBagConstraints(3, iZeile, 1,
							1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

					if (agstklpositionDtos.get(i).getNMenge() != null) {
						wnfMaterialMenge[i]
								.setBigDecimal(agstklpositionDtos.get(i).getNMenge().multiply(new BigDecimal(1000)));
					} else {
						wnfMaterialMenge[i].setBigDecimal(BigDecimal.ZERO);
					}

				} else {
					panelPositionen.add(new JLabel(artikelDto.getEinheitCNr()), new GridBagConstraints(3, iZeile, 1, 1,
							0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
				}
				panelPositionen.add(wcbMaterialRuestmenge[i], new GridBagConstraints(4, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 10, 0));
				panelPositionen.add(wnfMaterialPreis[i], new GridBagConstraints(5, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

				panelPositionen.add(new JLabel(artikelDto.getEinheitCNr()), new GridBagConstraints(6, iZeile, 1, 1, 0,
						0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

				panelPositionen.add(wnfMaterialPreisGesamt[i], new GridBagConstraints(7, iZeile, 1, 1, 0, 0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

				panelPositionen.add(wbuPositionEntfernen[i], new GridBagConstraints(8, iZeile, 1, 1, 0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), 10, 0));

			}
		}
		iZeile++;

		panelPositionen.add(new JLabel(""), new GridBagConstraints(1, iZeile, 1, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		return panelPositionen;
	}

	private JPanel createPanelMengenstaffel() throws ExceptionLP, Throwable {
		JPanel panelMengenstaffel = new JPanel();
		panelMengenstaffel.setLayout(new GridBagLayout());

		int iZeile = 0;

		wnfMengenstaffelMenge = new WrapperNumberField[agstklmengenstaffelDtos.size()];

		wnfMengenstaffelAufschlagAZ = new WrapperNumberField[agstklmengenstaffelDtos.size()];
		wnfMengenstaffelAufschlagMaterial = new WrapperNumberField[agstklmengenstaffelDtos.size()];
		wnfMengenstaffelSummeMaterialMitAufschlag = new WrapperNumberField[agstklmengenstaffelDtos.size()];
		wnfMengenstaffelSummeAZ = new WrapperNumberField[agstklmengenstaffelDtos.size()];
		wnfMengenstaffelSummeMaterial = new WrapperNumberField[agstklmengenstaffelDtos.size()];
		wnfMengenstaffelSummeAZMitAufschlag = new WrapperNumberField[agstklmengenstaffelDtos.size()];

		wnfMengenstaffelPreisGesamtBerechnet = new WrapperNumberField[agstklmengenstaffelDtos.size()];
		wnfMengenstaffelPreisGesamtBerechnetProEinheit = new WrapperNumberField[agstklmengenstaffelDtos.size()];

		int iMengenstaffelSelektiert = -1;

		if (wrbMengenstaffelGewaehlt != null) {
			for (int x = 0; x < wrbMengenstaffelGewaehlt.length; x++) {
				if (wrbMengenstaffelGewaehlt[x].isSelected()) {
					iMengenstaffelSelektiert = x;
				}
			}
		}

		wrbMengenstaffelGewaehlt = new WrapperRadioButton[agstklmengenstaffelDtos.size()];
		wbuMengenstaffelEntfernen = new WrapperButton[agstklmengenstaffelDtos.size()];
		bgMengenstaffel = new ButtonGroup();

		panelMengenstaffel.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.mengenstaffel")),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		panelMengenstaffel.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.summematerial")),
				new GridBagConstraints(2, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		panelMengenstaffel.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.aufschlag")),
				new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		panelMengenstaffel.add(new JLabel("="), new GridBagConstraints(4, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		panelMengenstaffel.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.summearbeitszeit")),
				new GridBagConstraints(5, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		panelMengenstaffel.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.aufschlag")),
				new GridBagConstraints(6, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		panelMengenstaffel.add(new JLabel("="), new GridBagConstraints(7, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		panelMengenstaffel.add(
				new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.wert") + " "
						+ agstklDto.getWaehrungCNr().trim()),
				new GridBagConstraints(8, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		panelMengenstaffel.add(new JLabel(LPMain.getTextRespectUISPr("angb.schnellerfassung.preis")),
				new GridBagConstraints(9, iZeile, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		AGSchnellerfassungHeightController hc = new AGSchnellerfassungHeightController(panelKopfdaten);
		panelMengenstaffel.add(hc, new GridBagConstraints(10, iZeile, 1, 1, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 20));

		for (int i = 0; i < agstklmengenstaffelDtos.size(); i++) {
			iZeile++;

			wbuMengenstaffelEntfernen[i] = new WrapperButton();
			wbuMengenstaffelEntfernen[i].setText("X");
			wbuMengenstaffelEntfernen[i].setForeground(Color.RED);
			wbuMengenstaffelEntfernen[i].addActionListener(this);

			wrbMengenstaffelGewaehlt[i] = new WrapperRadioButton("");

			wnfMengenstaffelMenge[i] = new WrapperNumberField(4);
			wnfMengenstaffelMenge[i].setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());
			wnfMengenstaffelMenge[i].setMandatoryField(true);
			wnfMengenstaffelMenge[i].addFocusListener(this);

			wnfMengenstaffelMenge[i].setBigDecimal(agstklmengenstaffelDtos.get(i).getNMenge());

			wnfMengenstaffelSummeMaterial[i] = new WrapperNumberField(6);
			wnfMengenstaffelSummeMaterial[i].setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
			wnfMengenstaffelSummeMaterial[i].setDependenceField(true);
			wnfMengenstaffelSummeMaterial[i].setEditable(false);

			wnfMengenstaffelSummeAZ[i] = new WrapperNumberField(6);
			wnfMengenstaffelSummeAZ[i].setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
			wnfMengenstaffelSummeAZ[i].setDependenceField(true);
			wnfMengenstaffelSummeAZ[i].setEditable(false);

			wnfMengenstaffelAufschlagAZ[i] = new WrapperNumberField(3);
			wnfMengenstaffelAufschlagAZ[i].setMandatoryField(true);
			wnfMengenstaffelAufschlagAZ[i].addFocusListener(this);
			wnfMengenstaffelAufschlagAZ[i].setBigDecimal(agstklmengenstaffelDtos.get(i).getNAufschlagAz());

			wnfMengenstaffelAufschlagMaterial[i] = new WrapperNumberField(3);
			wnfMengenstaffelAufschlagMaterial[i].setMandatoryField(true);
			wnfMengenstaffelAufschlagMaterial[i].addFocusListener(this);
			wnfMengenstaffelAufschlagMaterial[i].setBigDecimal(agstklmengenstaffelDtos.get(i).getNAufschlagMaterial());

			wnfMengenstaffelSummeMaterialMitAufschlag[i] = new WrapperNumberField(6);
			wnfMengenstaffelSummeMaterialMitAufschlag[i]
					.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
			wnfMengenstaffelSummeMaterialMitAufschlag[i].setDependenceField(true);
			wnfMengenstaffelSummeMaterialMitAufschlag[i].setEditable(false);

			wnfMengenstaffelSummeAZMitAufschlag[i] = new WrapperNumberField(6);
			wnfMengenstaffelSummeAZMitAufschlag[i]
					.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
			wnfMengenstaffelSummeAZMitAufschlag[i].setDependenceField(true);
			wnfMengenstaffelSummeAZMitAufschlag[i].setEditable(false);

			wnfMengenstaffelPreisGesamtBerechnet[i] = new WrapperNumberField(8);

			wnfMengenstaffelPreisGesamtBerechnet[i].setDependenceField(true);
			wnfMengenstaffelPreisGesamtBerechnet[i].setEditable(false);

			wnfMengenstaffelPreisGesamtBerechnetProEinheit[i] = new WrapperNumberField(8);

			wnfMengenstaffelPreisGesamtBerechnetProEinheit[i].addFocusListener(this);

			panelMengenstaffel.add(wrbMengenstaffelGewaehlt[i], new GridBagConstraints(0, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), 20, 0));
			bgMengenstaffel.add(wrbMengenstaffelGewaehlt[i]);

			panelMengenstaffel.add(wnfMengenstaffelMenge[i], new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), 0, 0));
			panelMengenstaffel.add(wnfMengenstaffelSummeMaterial[i], new GridBagConstraints(2, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), -20, 0));

			panelMengenstaffel.add(wnfMengenstaffelAufschlagMaterial[i], new GridBagConstraints(3, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), 0, 0));
			panelMengenstaffel.add(wnfMengenstaffelSummeMaterialMitAufschlag[i], new GridBagConstraints(4, iZeile, 1, 1,
					0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), -20, 0));
			panelMengenstaffel.add(wnfMengenstaffelSummeAZ[i], new GridBagConstraints(5, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), -20, 0));
			panelMengenstaffel.add(wnfMengenstaffelAufschlagAZ[i], new GridBagConstraints(6, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), 0, 0));
			panelMengenstaffel.add(wnfMengenstaffelSummeAZMitAufschlag[i], new GridBagConstraints(7, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), -20, 0));

			panelMengenstaffel.add(wnfMengenstaffelPreisGesamtBerechnet[i], new GridBagConstraints(8, iZeile, 1, 1, 0,
					0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), -20, 0));
			panelMengenstaffel.add(wnfMengenstaffelPreisGesamtBerechnetProEinheit[i],
					new GridBagConstraints(9, iZeile, 1, 1, 1, 0, GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));

			panelMengenstaffel.add(wbuMengenstaffelEntfernen[i], new GridBagConstraints(10, iZeile, 1, 1, 0, 0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 2, 2, 2), 10, 0));

		}

		if (iMengenstaffelSelektiert >= 0 && wrbMengenstaffelGewaehlt.length > iMengenstaffelSelektiert) {
			wrbMengenstaffelGewaehlt[iMengenstaffelSelektiert].setSelected(true);
		}

		iZeile++;

		panelMengenstaffel.add(new JLabel(""), new GridBagConstraints(1, iZeile, 1, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		return panelMengenstaffel;
	}

	private void berechneGewicht() throws Throwable {
		for (int i = 0; i < agstklmaterialDtos.size(); i++) {
			berechneGewicht(i);
		}
	}

	private void berechneGewicht(int iZeile) throws Throwable {

		WrapperNumberField wnfGewicht = wnfMaterialGewicht.get(iZeile);
		WrapperNumberField wnfGewichtPreis = wnfMaterialGewichtPreis.get(iZeile);

		WrapperNumberField wnfDimension1 = wnfMaterialDimension1.get(iZeile);
		WrapperNumberField wnfDimension2 = wnfMaterialDimension2.get(iZeile);
		WrapperNumberField wnfDimension3 = wnfMaterialDimension3.get(iZeile);

		WrapperComboBox wcbMaterialtyp = wcbMaterialMaterialtyp.get(iZeile);

		wnfGewicht.setBigDecimal(null);
		wnfGewicht.setBackground(Color.RED);

		wnfGewichtPreis.setBigDecimal(null);
		wnfGewichtPreis.setBackground(Color.RED);

		AgstklmaterialDto matDto = agstklmaterialDtos.get(iZeile);

		if (materialpreisProKG[iZeile] != null && wcbMaterialtyp.getKeyOfSelectedItem() != null) {

			if (wcbMaterialtyp.getKeyOfSelectedItem() != null) {

				BigDecimal gewicht = BigDecimal.ZERO;

				if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_QUADER)) {
					double b = 0;
					double h = 0;
					double t = 0;
					if (wnfDimension1.getBigDecimal() != null) {
						b = wnfDimension1.getBigDecimal().doubleValue() / 100;
					}
					if (wnfDimension3.getBigDecimal() != null) {
						h = wnfDimension3.getBigDecimal().doubleValue() / 100;
					}
					if (wnfDimension2.getBigDecimal() != null) {
						t = wnfDimension2.getBigDecimal().doubleValue() / 100;
					}

					gewicht = new BigDecimal(b * h * t).multiply(materialgewichtInKG[iZeile]);
				} else if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_RUND)) {
					double radius = 0;
					double laenge = 0;
					if (wnfDimension1.getBigDecimal() != null) {
						radius = wnfDimension1.getBigDecimal().doubleValue() / 2 / 100;
					}
					if (wnfDimension2.getBigDecimal() != null) {
						laenge = wnfDimension2.getBigDecimal().doubleValue() / 100;
					}

					gewicht = new BigDecimal(radius * radius * Math.PI * laenge).multiply(materialgewichtInKG[iZeile]);
				} else if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_ROHR)) {
					double radiusAussen = 0;

					double radiusInnen = 0;

					double laenge = 0;
					if (wnfDimension1.getBigDecimal() != null) {
						radiusAussen = wnfDimension1.getBigDecimal().doubleValue() / 2 / 100;
					}

					if (wnfDimension2.getBigDecimal() != null) {
						radiusInnen = wnfDimension2.getBigDecimal().doubleValue() / 2 / 100;
					}
					if (wnfDimension3.getBigDecimal() != null) {
						laenge = wnfDimension3.getBigDecimal().doubleValue() / 100;
					}

					if (radiusInnen > radiusAussen) {
						gewicht = BigDecimal.ZERO;
					} else {
						BigDecimal bdZylinderAussen = new BigDecimal(radiusAussen * radiusAussen * Math.PI * laenge);
						BigDecimal bdZylinderInnen = new BigDecimal(radiusInnen * radiusInnen * Math.PI * laenge);

						gewicht = bdZylinderAussen.subtract(bdZylinderInnen).multiply(materialgewichtInKG[iZeile]);
					}

				}

				wnfGewicht.setBigDecimal(gewicht);
				wnfGewicht.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());
			}

			berechneGewichtPreis(iZeile);

		} else {
			wnfGewicht.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());
			wnfGewichtPreis.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());
		}

	}

	private void berechneGewichtPreis(int iZeile) throws ExceptionLP {

		WrapperNumberField wnfGewicht = wnfMaterialGewicht.get(iZeile);
		WrapperNumberField wnfGewichtPreis = wnfMaterialGewichtPreis.get(iZeile);

		if (materialpreisProKG[iZeile] != null && wnfGewicht.getBigDecimal() != null) {

			BigDecimal preis = wnfGewicht.getBigDecimal().multiply(materialpreisProKG[iZeile]);
			wnfGewichtPreis.setBigDecimal(preis);
			wnfGewichtPreis.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());

		}
	}

	private BigDecimal berechneZeileTaetigkeiten(int iZeile, BigDecimal bdMengenstaffel, boolean bNurRuestkosten)
			throws Throwable {
		if (wnfRuestzeit[iZeile].getBigDecimal() != null && wnfStueckzeit[iZeile].getBigDecimal() != null) {

			BigDecimal bdKosten = BigDecimal.ZERO;

			BigDecimal rustzeitInStunden = wnfRuestzeit[iZeile].getBigDecimal();

			BigDecimal stueckzeitInMinuten = wnfStueckzeit[iZeile].getBigDecimal().divide(new BigDecimal(60), 4,
					BigDecimal.ROUND_HALF_EVEN);
			if (bdMengenstaffel != null) {
				stueckzeitInMinuten = stueckzeitInMinuten.multiply(bdMengenstaffel);
			}
			BigDecimal bdGesamteit = null;
			if (bNurRuestkosten) {
				bdGesamteit = rustzeitInStunden;
			} else {
				bdGesamteit = rustzeitInStunden.add(stueckzeitInMinuten);
			}

			if (wcbNurMaschine[iZeile].isSelected() == false && wnfStundesatzMann[iZeile].getBigDecimal() != null) {
				bdKosten = bdGesamteit.multiply(wnfStundesatzMann[iZeile].getBigDecimal());

				BigDecimal preisPersonal = DelegateFactory.getInstance().getLagerDelegate()
						.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
								agstklarbeitsplanDtos.get(iZeile).getArtikelIId());
				if (preisPersonal.doubleValue() > wnfStundesatzMann[iZeile].getBigDecimal().doubleValue()) {
					wnfStundesatzMann[iZeile].setForeground(Color.RED);
				}

			}

			if (agstklarbeitsplanDtos.get(iZeile).getMaschineIId() != null
					&& wnfStundesatzMaschine[iZeile].getBigDecimal() != null) {
				bdKosten = bdKosten.add(bdGesamteit.multiply(wnfStundesatzMaschine[iZeile].getBigDecimal()));

				BigDecimal bdKostenMaschineBerechnet = DelegateFactory.getInstance().getZeiterfassungDelegate()
						.getMaschinenKostenZumZeitpunkt(agstklarbeitsplanDtos.get(iZeile).getMaschineIId(),
								LocaleFac.BELEGART_AGSTUECKLISTE, agstklarbeitsplanDtos.get(iZeile).getIId());

				if (bdKostenMaschineBerechnet.doubleValue() > wnfStundesatzMaschine[iZeile].getBigDecimal()
						.doubleValue()) {
					wnfStundesatzMaschine[iZeile].setForeground(Color.RED);
				}

			}

			if (bdMengenstaffel == null) {
				wnfPreisTaetigkeit[iZeile].setBigDecimal(bdKosten);
			}
			return bdKosten;
		} else {
			return null;
		}
	}

	private BigDecimal berechneZeilePosition(int iZeile, BigDecimal bdMengenstaffel) throws Throwable {
		if (wnfMaterialMenge[iZeile].getBigDecimal() != null && wnfMaterialPreis[iZeile].getBigDecimal() != null) {

			BigDecimal bdMenge = wnfMaterialMenge[iZeile].getBigDecimal();

			if (bdMengenstaffel != null) {

				if (!wcbMaterialRuestmenge[iZeile].isSelected()) {
					bdMenge = bdMenge.multiply(bdMengenstaffel);
				}
			}

			if (materialArtikelDto[iZeile].getEinheitCNr().equals(SystemFac.EINHEIT_METER)) {
				bdMenge = bdMenge.divide(new BigDecimal(1000), Defaults.getInstance().getIUINachkommastellenMenge(),
						BigDecimal.ROUND_HALF_EVEN);
			}

			BigDecimal bdGesamtpreis = bdMenge.multiply(wnfMaterialPreis[iZeile].getBigDecimal());

			if (bdMengenstaffel != null && bdMengenstaffel.doubleValue() > 1) {

				ArtikellieferantDto alDto = DelegateFactory.getInstance().getArtikelDelegate().getArtikelEinkaufspreis(
						materialArtikelDto[iZeile].getIId(), null, bdMenge, agstklDto.getWaehrungCNr(),
						new java.sql.Date(agstklDto.getTBelegdatum().getTime()));

				if (alDto!=null && alDto.getNNettopreis() != null) {
					bdGesamtpreis = bdMenge.multiply(alDto.getNNettopreis());
				}

			}

			if (bdMengenstaffel == null) {
				wnfMaterialPreisGesamt[iZeile].setBigDecimal(bdGesamtpreis);
			}

			ArtikellieferantDto alDto = DelegateFactory.getInstance().getArtikelDelegate()
					.getArtikelEinkaufspreisDesBevorzugtenLieferanten(materialArtikelDto[iZeile].getIId(),
							BigDecimal.ONE, agstklDto.getWaehrungCNr());

			if (alDto != null && alDto.getNNettopreis() != null
					&& alDto.getNNettopreis().doubleValue() > wnfMaterialPreis[iZeile].getBigDecimal().doubleValue()) {

				wnfMaterialPreis[iZeile].setForeground(Color.RED);

			}

			return bdGesamtpreis;
		} else {
			return null;
		}
	}

	private BigDecimal berechneZeileMaterial(int iZeile, BigDecimal bdMengenstaffel) throws Throwable {

		wnfSummeMaterial.setBigDecimal(null);
		wnfSummeMaterial.setBackground(Color.RED);

		BigDecimal bdSummeMaterial = BigDecimal.ZERO;

		BigDecimal bdkostenZeile = wnfMaterialGewichtPreis.get(iZeile).getBigDecimal();
		if (bdkostenZeile == null) {
			return null;
		} else {
			bdSummeMaterial = bdSummeMaterial.add(bdkostenZeile);
		}

		if (bdMengenstaffel == null) {
			wnfSummeMaterial.setBigDecimal(bdSummeMaterial);

			wnfSummeMaterial.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());
		}

		return bdSummeMaterial;

	}

	private BigDecimal berechneSummeTaetigkeiten(BigDecimal bdMengenstaffel) throws Throwable {
		wnfSummeAZ.setBigDecimal(null);
		wnfSummeAZ.setBackground(Color.RED);

		BigDecimal bdSummeAZ = BigDecimal.ZERO;

		BigDecimal bdSummeRuestkostenFuerEinStueck = BigDecimal.ZERO;
		BigDecimal bdSummeStueckkostenFuerEinStueck = BigDecimal.ZERO;
		for (int i = 0; i < agstklarbeitsplanDtos.size(); i++) {

			BigDecimal bdkostenZeile = berechneZeileTaetigkeiten(i, bdMengenstaffel, false);

			if (bdkostenZeile == null) {
				return null;
			} else {

				if (wcbIntitialkostenArbeitsplan[i].isSelected()) {
					bdInitialkosten = bdInitialkosten.add(bdkostenZeile);
				} else {
					bdSummeAZ = bdSummeAZ.add(bdkostenZeile);

				}

			}

			BigDecimal bdRuestkostenZeile = berechneZeileTaetigkeiten(i, BigDecimal.ONE, true);
			bdSummeRuestkostenFuerEinStueck = bdSummeRuestkostenFuerEinStueck.add(bdRuestkostenZeile);

			BigDecimal bdStueckkostenZeile = berechneZeileTaetigkeiten(i, BigDecimal.ONE, false);
			bdStueckkostenZeile = bdStueckkostenZeile.subtract(bdRuestkostenZeile);

			bdSummeStueckkostenFuerEinStueck = bdSummeStueckkostenFuerEinStueck.add(bdStueckkostenZeile);

		}

		if (bdMengenstaffel == null) {
			wnfSummeAZ.setBigDecimal(bdSummeAZ);

			wnfSummeAZ.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());

			if (wnfSummeAZ.isDependenceField()) {
				wnfSummeAZ.setDependenceField(true);
			}
		}

		BigDecimal bdSummeRuestzeit = BigDecimal.ZERO;
		BigDecimal bdSummeStueckzeit = BigDecimal.ZERO;
		for (int i = 0; i < agstklarbeitsplanDtos.size(); i++) {
			if (wnfRuestzeit[i].getBigDecimal() != null) {
				bdSummeRuestzeit = bdSummeRuestzeit.add(wnfRuestzeit[i].getBigDecimal());
			}
			if (wnfStueckzeit[i].getBigDecimal() != null) {
				bdSummeStueckzeit = bdSummeStueckzeit.add(wnfStueckzeit[i].getBigDecimal());
			}
		}

		wlaSummeRuestzeit.setText(Helper.formatZahl(bdSummeRuestzeit,
				Defaults.getInstance().getIUINachkommastellenMenge(), LPMain.getTheClient().getLocUi()));
		wlaSummeStueckzeit.setText(Helper.formatZahl(bdSummeStueckzeit,
				Defaults.getInstance().getIUINachkommastellenMenge(), LPMain.getTheClient().getLocUi()));

		wlaSummeRuestkosten.setText(Helper.formatZahl(bdSummeRuestkostenFuerEinStueck,
				Defaults.getInstance().getIUINachkommastellenMenge(), LPMain.getTheClient().getLocUi()));
		wlaSummeStueckkosten.setText(Helper.formatZahl(bdSummeStueckkostenFuerEinStueck,
				Defaults.getInstance().getIUINachkommastellenMenge(), LPMain.getTheClient().getLocUi()));

		return bdSummeAZ;

	}

	private BigDecimal berechneTaetigkeitenUndMaterial(BigDecimal bdMengenstaffel) throws Throwable {

		wnfSummeGesamt.setBigDecimal(null);
		wnfSummeGesamt.setBackground(Color.RED);

		bdInitialkosten = BigDecimal.ZERO;

		BigDecimal bdMaterial = berechneSummeMaterial(bdMengenstaffel);

		BigDecimal bdAZ = berechneSummeTaetigkeiten(bdMengenstaffel);

		BigDecimal bdPositionen = berechneSummePositionen(bdMengenstaffel);

		if (bdAZ != null && bdPositionen != null && bdMaterial != null) {

			BigDecimal bdSummeAZUndMaterial = bdAZ.add(bdPositionen).add(bdMaterial);

			if (bdMengenstaffel == null) {
				wnfSummeGesamt.setBigDecimal(bdSummeAZUndMaterial);

				wlaSummeGesamtOhneInitialkosten.setText("Summe Gesamt (ohne Initialkosten von "
						+ Helper.formatZahl(bdInitialkosten, 2, LPMain.getTheClient().getLocUi()) + " )");

				wnfSummeGesamt.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());

				if (wnfSummeGesamt.isDependenceField()) {
					wnfSummeGesamt.setDependenceField(true);
				}

			}

			return bdSummeAZUndMaterial;
		} else {
			return null;
		}

	}

	private BigDecimal berechneSummePositionen(BigDecimal bdMengenstaffel) throws Throwable {
		wnfSummePositionen.setBigDecimal(null);
		wnfSummePositionen.setBackground(Color.RED);

		BigDecimal bdSummeMaterial = BigDecimal.ZERO;

		for (int i = 0; i < agstklpositionDtos.size(); i++) {

			BigDecimal bdkostenZeile = berechneZeilePosition(i, bdMengenstaffel);
			if (bdkostenZeile == null) {
				return null;
			} else {

				if (wcbIntitialkostenPosition[i].isSelected()) {
					bdInitialkosten = bdInitialkosten.add(bdkostenZeile);
				} else {
					bdSummeMaterial = bdSummeMaterial.add(bdkostenZeile);
				}

			}

		}

		if (bdMengenstaffel == null) {
			wnfSummePositionen.setBigDecimal(bdSummeMaterial);

			wnfSummePositionen.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());
			if (wnfSummePositionen.isDependenceField()) {
				wnfSummePositionen.setDependenceField(true);
			}
		}

		return bdSummeMaterial;

	}

	private BigDecimal berechneSummeMaterial(BigDecimal bdMengenstaffel) throws Throwable {
		wnfSummeMaterial.setBigDecimal(null);
		wnfSummeMaterial.setBackground(Color.RED);

		BigDecimal bdSummeMaterial = BigDecimal.ZERO;

		for (int i = 0; i < agstklmaterialDtos.size(); i++) {

			BigDecimal bdkostenZeile = berechneZeileMaterial(i, bdMengenstaffel);
			if (bdkostenZeile == null) {
				return null;
			} else {

				if (bdMengenstaffel == null) {
					bdSummeMaterial = bdSummeMaterial.add(bdkostenZeile);
				} else {
					bdSummeMaterial = bdSummeMaterial.add(bdkostenZeile.multiply(bdMengenstaffel));
				}

			}

		}

		if (bdMengenstaffel == null) {
			wnfSummeMaterial.setBigDecimal(bdSummeMaterial);

			wnfSummeMaterial.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());
			if (wnfSummeMaterial.isDependenceField()) {
				wnfSummeMaterial.setDependenceField(true);
			}
		}

		return bdSummeMaterial;

	}

	public void clearAndHide() {
		setVisible(false);
	}

	void dialogQueryArtikelFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel = ArtikelFilterFactory.getInstance().createPanelFLRArtikelOhneArbeitszeit(intFrame, null,
				false);
		new DialogQuery(panelQueryFLRArtikel);
	}

	public void actionPerformedForStrgS(ActionEvent e) {
		actionPerformed(e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(ACTION_SPECIAL_MASCHINE_FROM_LISTE)) {
				for (int x = 0; x < wbuMaschine.length; x++) {
					if (e.getSource().equals(wbuMaschine[x])) {
						panelQueryFLRMaschine[x] = ZeiterfassungFilterFactory.getInstance()
								.createPanelFLRMaschinen(intFrame, agstklarbeitsplanDtos.get(x).getMaschineIId());

						new DialogQuery(panelQueryFLRMaschine[x]);
					}
				}
			} else if (e.getSource().equals(wbuAbbrechen)) {
				this.setVisible(false);
			} else if (e.getSource().equals(wbuMengenstaffelAdd)) {

				for (int i = 0; i < agstklmengenstaffelDtos.size(); i++) {
					agstklmengenstaffelDtos.get(i).setNMenge(wnfMengenstaffelMenge[i].getBigDecimal());

					agstklmengenstaffelDtos.get(i).setNAufschlagAz(wnfMengenstaffelAufschlagAZ[i].getBigDecimal());

					agstklmengenstaffelDtos.get(i)
							.setNAufschlagMaterial(wnfMengenstaffelAufschlagMaterial[i].getBigDecimal());
					agstklmengenstaffelDtos.get(i).setNWertMaterial(wnfMengenstaffelSummeMaterial[i].getBigDecimal());
					agstklmengenstaffelDtos.get(i).setNWertAz(wnfMengenstaffelSummeAZ[i].getBigDecimal());
					agstklmengenstaffelDtos.get(i)
							.setNPreisEinheit(wnfMengenstaffelPreisGesamtBerechnetProEinheit[i].getBigDecimal());

				}

				AgstklmengenstaffelSchnellerfassungDto staffelDto = new AgstklmengenstaffelSchnellerfassungDto();

				staffelDto.setNMenge(wnfMengeHinzufuegen.getBigDecimal());
				staffelDto.setNAufschlagAz(BigDecimal.ZERO);
				staffelDto.setNAufschlagMaterial(BigDecimal.ZERO);

				if (agstklmengenstaffelDtos.size() > 0) {
					staffelDto.setNAufschlagAz(
							agstklmengenstaffelDtos.get(agstklmengenstaffelDtos.size() - 1).getNAufschlagAz());
					staffelDto.setNAufschlagMaterial(
							agstklmengenstaffelDtos.get(agstklmengenstaffelDtos.size() - 1).getNAufschlagMaterial());
				}

				agstklmengenstaffelDtos.add(staffelDto);

				scrollpaneMengenstaffeln.setViewportView(createPanelMengenstaffel());
				berechneAlles();

				if (wnfMengeHinzufuegen.getBigDecimal() == null && agstklmengenstaffelDtos.size() > 0) {
					wnfMengenstaffelMenge[agstklmengenstaffelDtos.size() - 1].requestFocus();
				}
				wnfMengeHinzufuegen.setBigDecimal(null);

				JScrollBar sb = scrollpaneMengenstaffeln.getVerticalScrollBar();
				sb.setValue(sb.getMaximum());

			} else {
				if (e.getSource().equals(wbuUebernehmen)) {

					int iMengenstaffelSelektiert = -1;

					for (int x = 0; x < wrbMengenstaffelGewaehlt.length; x++) {
						if (wrbMengenstaffelGewaehlt[x].isSelected()) {
							iMengenstaffelSelektiert = x;
						}
					}

					if (iMengenstaffelSelektiert >= 0) {

						if (wnfMengenstaffelMenge[iMengenstaffelSelektiert].getBigDecimal() != null
								&& wnfMengenstaffelPreisGesamtBerechnetProEinheit[iMengenstaffelSelektiert]
										.getBigDecimal() != null) {

							if (wtfBezeichnung.getText() == null) {
								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
								return;
							}

							agstklDto.setCBez(wtfBezeichnung.getText());
							agstklDto.setCZeichnungsnummer(wtfZeichnungsnummer.getText());
							agstklDto.setNInitialkosten(bdInitialkosten);

							if (wmcMedia.getMimeType() != null && !wmcMedia.getMimeType()
									.equals(Defaults.getInstance().getSComboboxEmptyEntry())) {
								agstklDto.setDatenformatCNr(wmcMedia.getMimeType());
								agstklDto.setOMedia(wmcMedia.getOMediaImage());
							} else {
								agstklDto.setDatenformatCNr(null);
								agstklDto.setOMedia(null);
							}

							for (int i = 0; i < agstklmaterialDtos.size(); i++) {
								agstklmaterialDtos.get(i).setNDimension1(wnfMaterialDimension1.get(i).getBigDecimal());
								agstklmaterialDtos.get(i).setNDimension2(wnfMaterialDimension2.get(i).getBigDecimal());
								agstklmaterialDtos.get(i).setNDimension3(wnfMaterialDimension3.get(i).getBigDecimal());
								agstklmaterialDtos.get(i)
										.setCMaterialtyp((String) wcbMaterialMaterialtyp.get(i).getKeyOfSelectedItem());
								agstklmaterialDtos.get(i).setCBez(wtfMaterialBezeichnung.get(i).getText());

								agstklmaterialDtos.get(i).setNGewicht(wnfMaterialGewicht.get(i).getBigDecimal());

								agstklmaterialDtos.get(i)
										.setNGewichtpreis(wnfMaterialGewichtPreis.get(i).getBigDecimal());

								agstklmaterialDtos.get(i).setCBez(wtfMaterialBezeichnung.get(i).getText());

							}

							for (int i = 0; i < agstklpositionDtos.size(); i++) {

								BigDecimal bdMenge = wnfMaterialMenge[i].getBigDecimal();
								if (materialArtikelDto[i].getEinheitCNr().equals(SystemFac.EINHEIT_METER)) {
									bdMenge = bdMenge.divide(new BigDecimal(1000),
											Defaults.getInstance().getIUINachkommastellenMenge(),
											BigDecimal.ROUND_HALF_EVEN);
								}

								agstklpositionDtos.get(i).setNMenge(bdMenge);
								agstklpositionDtos.get(i).setNNettoeinzelpreis(wnfMaterialPreis[i].getBigDecimal());

								agstklpositionDtos.get(i).setBRuestmenge(wcbMaterialRuestmenge[i].getShort());

								wcbMaterialRuestmenge[i].setShort(agstklpositionDtos.get(i).getBRuestmenge());

								agstklpositionDtos.get(i).setBInitial(wcbIntitialkostenPosition[i].getShort());
								wcbIntitialkostenPosition[i].setShort(agstklpositionDtos.get(i).getBInitial());

							}

							for (int i = 0; i < agstklarbeitsplanDtos.size(); i++) {
								agstklarbeitsplanDtos.get(i).setBNurmaschinenzeit(wcbNurMaschine[i].getShort());
								agstklarbeitsplanDtos.get(i).setLRuestzeit((long) wnfRuestzeit[i].getBigDecimal()
										.multiply(new BigDecimal(3600000)).doubleValue());
								agstklarbeitsplanDtos.get(i).setLStueckzeit((long) wnfStueckzeit[i].getBigDecimal()
										.multiply(Helper.MINUTE_IN_MS).doubleValue());

								agstklarbeitsplanDtos.get(i).setNStundensatzMann(wnfStundesatzMann[i].getBigDecimal());
								agstklarbeitsplanDtos.get(i)
										.setNStundensatzMaschine(wnfStundesatzMaschine[i].getBigDecimal());

								agstklarbeitsplanDtos.get(i).setBInitial(wcbIntitialkostenArbeitsplan[i].getShort());

							}

							for (int i = 0; i < agstklmengenstaffelDtos.size(); i++) {
								agstklmengenstaffelDtos.get(i).setNMenge(wnfMengenstaffelMenge[i].getBigDecimal());

								agstklmengenstaffelDtos.get(i)
										.setNAufschlagAz(wnfMengenstaffelAufschlagAZ[i].getBigDecimal());

								agstklmengenstaffelDtos.get(i)
										.setNAufschlagMaterial(wnfMengenstaffelAufschlagMaterial[i].getBigDecimal());

								agstklmengenstaffelDtos.get(i)
										.setNWertMaterial(wnfMengenstaffelSummeMaterial[i].getBigDecimal());
								agstklmengenstaffelDtos.get(i).setNWertAz(wnfMengenstaffelSummeAZ[i].getBigDecimal());
								agstklmengenstaffelDtos.get(i).setNPreisEinheit(
										wnfMengenstaffelPreisGesamtBerechnetProEinheit[i].getBigDecimal());

							}

							// AGSTKL erzeugen
							angebotpositionIId = DelegateFactory.getInstance().getAngebotstklDelegate()
									.createAngebotstuecklisteAusSchnellerfassungUndErzeugeAngebotsposition(agstklDto,
											agstklarbeitsplanDtos, agstklpositionDtos, agstklmengenstaffelDtos,
											agstklmaterialDtos, angebotDto.getIId(),
											wnfMengenstaffelMenge[iMengenstaffelSelektiert].getBigDecimal(),
											wnfMengenstaffelPreisGesamtBerechnetProEinheit[iMengenstaffelSelektiert]
													.getBigDecimal(),
											this.getHeight(), agposDtoVorhanden);
							saveAndExit();
							return;

						} else {
							if (wtfBezeichnung.getText() == null) {
								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
								return;
							}
							return;
						}

					} else {
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("agstkl.schnelllerfassung.keinemengenstaffel.ausgewaehlt"));
					}

				}

				for (int x = 0; x < wbuMengenstaffelEntfernen.length; x++) {
					if (e.getSource().equals(wbuMengenstaffelEntfernen[x])) {
						agstklmengenstaffelDtos.remove(x);
						scrollpaneMengenstaffeln.setViewportView(createPanelMengenstaffel());
						break;
					}
				}

				for (int x = 0; x < wbuPositionEntfernen.length; x++) {
					if (e.getSource().equals(wbuPositionEntfernen[x])) {
						agstklpositionDtos.remove(x);
						scrollpanePositionen.setViewportView(createPanelPositionen());
						break;
					}
				}

				for (int x = 0; x < wbuArbeitsplanEntfernen.length; x++) {
					if (e.getSource().equals(wbuArbeitsplanEntfernen[x])) {
						agstklarbeitsplanDtos.remove(x);
						scrollpaneArbeitsplan.setViewportView(createPanelTaetigkeiten());
						break;
					}
				}

				for (int x = 0; x < wbuMaterialEntfernen.length; x++) {
					if (e.getSource().equals(wbuMaterialEntfernen[x])) {
						for (int i = 0; i < agstklmaterialDtos.size(); i++) {
							agstklmaterialDtos.get(i).setNDimension1(wnfMaterialDimension1.get(i).getBigDecimal());
							agstklmaterialDtos.get(i).setNDimension2(wnfMaterialDimension2.get(i).getBigDecimal());
							agstklmaterialDtos.get(i).setNDimension3(wnfMaterialDimension3.get(i).getBigDecimal());
							agstklmaterialDtos.get(i)
									.setCMaterialtyp((String) wcbMaterialMaterialtyp.get(i).getKeyOfSelectedItem());
							agstklmaterialDtos.get(i).setCBez(wtfMaterialBezeichnung.get(i).getText());

							agstklmaterialDtos.get(i).setNGewicht(wnfMaterialGewicht.get(i).getBigDecimal());

							agstklmaterialDtos.get(i).setNGewichtpreis(wnfMaterialGewichtPreis.get(i).getBigDecimal());

							agstklmaterialDtos.get(i).setCBez(wtfMaterialBezeichnung.get(i).getText());

						}
						agstklmaterialDtos.remove(x);

						scrollpaneMaterial.setViewportView(createPanelMaterial());
						break;
					}
				}

				berechneAlles();
			}

			for (int i = 0; i < agstklmaterialDtos.size(); i++) {

				WrapperComboBox wcbMaterialtyp = wcbMaterialMaterialtyp.get(i);

				if (e.getSource().equals(wcbMaterialtyp)) {

					WrapperNumberField wnfDimension1 = wnfMaterialDimension1.get(i);
					WrapperNumberField wnfDimension2 = wnfMaterialDimension2.get(i);
					WrapperNumberField wnfDimension3 = wnfMaterialDimension3.get(i);

					wnfDimension1.setEditable(false);
					wnfDimension2.setEditable(false);
					wnfDimension3.setEditable(false);

					if (wcbMaterialtyp.getKeyOfSelectedItem() != null) {
						if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_QUADER)
								|| wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_ROHR)) {
							wnfDimension1.setEditable(true);
							wnfDimension2.setEditable(true);
							wnfDimension3.setEditable(true);
						}
						if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_RUND)) {
							wnfDimension1.setEditable(true);
							wnfDimension2.setEditable(true);
							wnfDimension3.setBigDecimal(null);
						}

					}

					// berechneGewicht(i);

					berechneAlles();

				}
			}

			for (int i = 0; i < agstklpositionDtos.size(); i++) {

				WrapperCheckBox wcbMaterialtyp = wcbMaterialRuestmenge[i];

				if (e.getSource().equals(wcbMaterialtyp)) {
					berechneAlles();
				}
			}

		} catch (Throwable e1) {
			intFrame.handleException(e1, false);
		}

	}

	public void saveAndExit() throws Throwable {

		AGSchnellerfassungPositionData posData = new AGSchnellerfassungPositionData(this);
		posData.setDividerLocation(splitPane.getDividerLocation());

		GridBagLayout gbl = (GridBagLayout) panelKopfdaten.getLayout();

		posData.setWeightMaterial(gbl.getConstraints(scrollpaneMaterial).weighty);
		posData.setWeightMengenstaffel(gbl.getConstraints(scrollpaneMengenstaffeln).weighty);
		posData.setWeightPosition(gbl.getConstraints(scrollpanePositionen).weighty);
		posData.setWeightTaetigkeit(gbl.getConstraints(scrollpaneArbeitsplan).weighty);

		ClientPerspectiveManager.getInstance().persistAGSchnellerfassungPositionData(posData);

		this.setVisible(false);
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		try {

			if (e.getSource().equals(wnfMengeHinzufuegen)) {

				if (e.getOppositeComponent() != null && e.getOppositeComponent() == wbuMengenstaffelAdd) {
					return;
				}

				if (wnfMengeHinzufuegen.getBigDecimal() != null) {
					actionPerformed(new ActionEvent(wbuMengenstaffelAdd, -1, ""));
				}
			}

			for (int i = 0; i < agstklmaterialDtos.size(); i++) {

				WrapperNumberField wnfDimension1 = wnfMaterialDimension1.get(i);
				WrapperNumberField wnfDimension2 = wnfMaterialDimension2.get(i);
				WrapperNumberField wnfDimension3 = wnfMaterialDimension3.get(i);

				WrapperNumberField wnfGewicht = wnfMaterialGewicht.get(i);
				WrapperNumberField wnfGewichtPreis = wnfMaterialGewichtPreis.get(i);

				if (e.getSource().equals(wnfDimension1) || e.getSource().equals(wnfDimension3)
						|| e.getSource().equals(wnfDimension2)) {

					if (wnfDimension1.getBigDecimal() != null && wnfDimension2.getBigDecimal() != null) {
						bdGewichtPreisCache[i] = null;
						berechneGewicht(i);
					}

				}

				if (e.getSource().equals(wnfGewicht)) {

					berechneGewichtPreis(i);

				}

				if (e.getSource().equals(wnfGewichtPreis) && wnfGewichtPreis.getBigDecimal() != null) {

					if (bdGewichtPreisCache[i] != null
							&& bdGewichtPreisCache[i].equals(wnfGewichtPreis.getBigDecimal())) {

					} else {
						bdGewichtPreisCache[i] = wnfGewichtPreis.getBigDecimal();
						wnfGewichtPreis.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());

					}
				}

				if (e.getSource().equals(wnfGewicht)) {
					if (materialpreisProKG != null && wnfGewicht.getBigDecimal() != null) {

						BigDecimal preis = wnfGewicht.getBigDecimal().multiply(materialpreisProKG[i]);
						wnfGewichtPreis.setBigDecimal(preis);
						wnfGewichtPreis.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());

					}
				}

			}

			for (int x = 0; x < wnfMaterialMenge.length; x++) {
				if (e.getSource().equals(wnfMaterialMenge[x])) {
					agstklpositionDtos.get(x).setNMenge(wnfMaterialMenge[x].getBigDecimal());

				}
			}

			for (int x = 0; x < wnfMaterialPreis.length; x++) {
				if (e.getSource().equals(wnfMaterialPreis[x])) {

					agstklpositionDtos.get(x).setNNettoeinzelpreis(wnfMaterialPreis[x].getBigDecimal());
					agstklpositionDtos.get(x).setNNettogesamtpreis(wnfMaterialPreis[x].getBigDecimal());

				}
			}

			for (int x = 0; x < wnfMaterialPreisGesamt.length; x++) {
				if (e.getSource().equals(wnfMaterialPreisGesamt[x])) {

					if (wnfMaterialMenge[x].getBigDecimal() != null
							&& wnfMaterialMenge[x].getBigDecimal().doubleValue() != 0) {

						BigDecimal preisNeu = wnfMaterialPreisGesamt[x].getBigDecimal().divide(
								wnfMaterialMenge[x].getBigDecimal(),
								Defaults.getInstance().getIUINachkommastellenPreiseEK(), BigDecimal.ROUND_HALF_EVEN);

						if (materialArtikelDto[x].getEinheitCNr().equals(SystemFac.EINHEIT_METER)) {
							preisNeu = preisNeu.multiply(new BigDecimal(1000));
						}

						wnfMaterialPreis[x].setBigDecimal(preisNeu);

						agstklpositionDtos.get(x).setNNettoeinzelpreis(preisNeu);
					}

				}
			}

			for (int x = 0; x < wnfMengenstaffelPreisGesamtBerechnetProEinheit.length; x++) {
				if (e.getSource().equals(wnfMengenstaffelPreisGesamtBerechnetProEinheit[x])
						&& wnfMengenstaffelPreisGesamtBerechnetProEinheit[x].getBigDecimal() != null) {

					if (agstklmengenstaffelDtos.get(x).getNPreisEinheit() != null && agstklmengenstaffelDtos.get(x)
							.getNPreisEinheit().doubleValue() != wnfMengenstaffelPreisGesamtBerechnetProEinheit[x]
									.getBigDecimal().doubleValue()) {

						if (wnfMengenstaffelMenge[x].getBigDecimal() != null) {

							// Aufschlag berechnen
							BigDecimal bdPreisGesamtNeu = wnfMengenstaffelPreisGesamtBerechnetProEinheit[x]
									.getBigDecimal().multiply(wnfMengenstaffelMenge[x].getBigDecimal());

							if (bdPreisGesamtNeu.doubleValue() <= wnfMengenstaffelSummeMaterialMitAufschlag[x]
									.getBigDecimal().doubleValue()) {
								wnfMengenstaffelSummeAZ[x].setBigDecimal(BigDecimal.ZERO);
								wnfMengenstaffelAufschlagAZ[x].setBigDecimal(BigDecimal.ZERO);
								wnfMengenstaffelSummeAZMitAufschlag[x].setBigDecimal(BigDecimal.ZERO);
								wnfMengenstaffelPreisGesamtBerechnetProEinheit[x]
										.setBigDecimal(wnfMengenstaffelSummeMaterialMitAufschlag[x].getBigDecimal());

							} else {

								BigDecimal diff = bdPreisGesamtNeu
										.subtract(wnfMengenstaffelSummeMaterialMitAufschlag[x].getBigDecimal());
								wnfMengenstaffelSummeAZMitAufschlag[x].setBigDecimal(diff);

								BigDecimal bdAufschlag = Helper.getProzentsatzBD(
										wnfMengenstaffelSummeAZ[x].getBigDecimal(),
										wnfMengenstaffelSummeAZMitAufschlag[x].getBigDecimal(), 2);

								wnfMengenstaffelAufschlagAZ[x].setBigDecimal(bdAufschlag.subtract(new BigDecimal(100)));

							}

							BigDecimal bdSummeMitAufschlaegen = BigDecimal.ZERO;
							if (wnfMengenstaffelSummeAZMitAufschlag[x].getBigDecimal() != null) {
								bdSummeMitAufschlaegen = wnfMengenstaffelSummeAZMitAufschlag[x].getBigDecimal();
							}

							if (wnfMengenstaffelSummeMaterialMitAufschlag[x].getBigDecimal() != null) {
								bdSummeMitAufschlaegen = bdSummeMitAufschlaegen
										.add(wnfMengenstaffelSummeMaterialMitAufschlag[x].getBigDecimal());

							}

							wnfMengenstaffelPreisGesamtBerechnet[x].setBigDecimal(bdSummeMitAufschlaegen);

						}
					}
					return;

				}
			}

			berechneAlles();
		} catch (Throwable e1) {
			intFrame.handleException(e1, false);
		}
	}

	private void berechneAlles() throws ExceptionLP, Throwable {

		bdInitialkosten = BigDecimal.ZERO;

		for (int i = 0; i < wnfMengenstaffelMenge.length; i++) {

			wnfMengenstaffelPreisGesamtBerechnetProEinheit[i].setBigDecimal(null);

			if (wnfMengenstaffelMenge[i].getBigDecimal() != null) {

				BigDecimal bdMaterial = berechneSummeMaterial(wnfMengenstaffelMenge[i].getBigDecimal());

				BigDecimal bdAZ = berechneSummeTaetigkeiten(wnfMengenstaffelMenge[i].getBigDecimal());

				BigDecimal bdPositionen = berechneSummePositionen(wnfMengenstaffelMenge[i].getBigDecimal());

				BigDecimal bdMaterialGesamt = bdMaterial;
				
				if(bdPositionen!=null) {
					bdMaterialGesamt = bdMaterial.add(bdPositionen);
				}
				

				wnfMengenstaffelSummeAZ[i].setBigDecimal(bdAZ);
				wnfMengenstaffelSummeMaterial[i].setBigDecimal(bdMaterialGesamt);

				// Aufschlaege berechnen

				// AZ
				if (wnfMengenstaffelAufschlagAZ[i].getBigDecimal() != null) {
					wnfMengenstaffelSummeAZMitAufschlag[i].setBigDecimal(
							bdAZ.add(Helper.getProzentWert(bdAZ, wnfMengenstaffelAufschlagAZ[i].getBigDecimal(), 5)));
				} else {
					wnfMengenstaffelSummeAZMitAufschlag[i].setBigDecimal(bdAZ);
				}

				// MATERIAL

				if (wnfMengenstaffelAufschlagMaterial[i].getBigDecimal() != null) {
					wnfMengenstaffelSummeMaterialMitAufschlag[i]
							.setBigDecimal(bdMaterialGesamt.add(Helper.getProzentWert(bdMaterialGesamt,
									wnfMengenstaffelAufschlagMaterial[i].getBigDecimal(), 5)));
				} else {
					wnfMengenstaffelSummeMaterialMitAufschlag[i].setBigDecimal(bdMaterialGesamt);
				}

				BigDecimal bdSummeMitAufschlaegen = BigDecimal.ZERO;
				if (wnfMengenstaffelSummeAZMitAufschlag[i].getBigDecimal() != null) {
					bdSummeMitAufschlaegen = wnfMengenstaffelSummeAZMitAufschlag[i].getBigDecimal();
				}

				if (wnfMengenstaffelSummeMaterialMitAufschlag[i].getBigDecimal() != null) {
					bdSummeMitAufschlaegen = bdSummeMitAufschlaegen
							.add(wnfMengenstaffelSummeMaterialMitAufschlag[i].getBigDecimal());

				}

				wnfMengenstaffelPreisGesamtBerechnet[i].setBigDecimal(bdSummeMitAufschlaegen);

				wnfMengenstaffelPreisGesamtBerechnetProEinheit[i]
						.setBigDecimal(wnfMengenstaffelPreisGesamtBerechnet[i].getBigDecimal()
								.divide(wnfMengenstaffelMenge[i].getBigDecimal(), 2, BigDecimal.ROUND_HALF_EVEN));

			}

		}

		berechneTaetigkeitenUndMaterial(null);
	}
}
