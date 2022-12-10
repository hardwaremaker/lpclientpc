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
package com.lp.client.artikel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ButtonAbstractAction;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperURLField;
import com.lp.client.frame.component.WrapperWebBauteilAbfrageButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.editor.PanelEditorPlainText;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.GebindeDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.WebabfrageArtikellieferantResult;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.inserat.service.InseraterDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.util.Helper;

public class PanelArtikellieferant extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArtikellieferantDto artikellieferantDto = null;
	private InternalFrameArtikel internalFrameArtikel = null;
	private JPanel jpaWorkingOn;
	private JPanel jpaButtonAction = null;
	private WrapperGotoButton wbuLieferant = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_LIEFERANT_AUSWAHL);
	private WrapperGotoButton wbuAnfragepositionlieferdaten = new WrapperGotoButton(
			com.lp.util.GotoHelper.GOTO_ANFRAGEPOSITIONLIEFERDATEN);
	private WrapperTextField wtfAnfragepositionlieferdaten = new WrapperTextField();
	private WrapperTextField wtfLieferant = new WrapperTextField();
	private WrapperCheckBox wcbHerstellerbezeichnung = new WrapperCheckBox();
	private WrapperCheckBox wcbWebshop = new WrapperCheckBox();
	private WrapperCheckBox wcbNichtLieferbar = new WrapperCheckBox();
	private WrapperButton wbuNichtLieferbarEdit = new WrapperButton();

	private JScrollPane staffeln = new JScrollPane();
	JList list = null;

	private WrapperComboBox wcoEinheitVpe = new WrapperComboBox();

	private WrapperLabel wlaArtikelnummerbeilieferant = new WrapperLabel();
	private WrapperLabel wlaEinzelpreis = new WrapperLabel();
	private WrapperLabel wlaNettopreis = new WrapperLabel();
	private WrapperLabel wlaMindestbestellmenge = new WrapperLabel();
	private WrapperLabel wlaFixkosten = new WrapperLabel();
	private WrapperLabel wlaGueltigab = new WrapperLabel();
	private WrapperLabel wlaWaehrungEinzelpreis = new WrapperLabel();
	private WrapperLabel wlaStandardmenge = new WrapperLabel();
	private WrapperLabel wlaVerpackungseinheit = new WrapperLabel();
	private WrapperLabel wlaRabattgruppe = new WrapperLabel();
	private WrapperNumberField wnfRabatt = new WrapperNumberField();
	private WrapperNumberField wnfStandardmenge = new WrapperNumberField();
	private WrapperNumberField wnfVerpackungseinheit = new WrapperNumberField();
	private WrapperTextField wtfRabattgruppe = new WrapperTextField();
	private WrapperDateField wdfgueltigab = new WrapperDateField();
	private WrapperNumberField wnfFixkosten = new WrapperNumberField();
	private WrapperLabel wlaInitialkosten = new WrapperLabel();
	private WrapperNumberField wnfInitialkosten = new WrapperNumberField();
	private WrapperNumberField wnfMindestbestellmenge = new WrapperNumberField();
	private WrapperNumberField wnfNettopreis = new WrapperNumberField();
	private WrapperNumberField wnfEinzelpreis = new WrapperNumberField();
	private WrapperTextField wtfArtikelnummer = new WrapperTextField();
	private PanelQueryFLR panelQueryFLRLieferant = null;
	private PanelQueryFLR panelQueryFLRGebinde = null;
	static final public String ACTION_SPECIAL_LIEFERANT_FROM_LISTE = "action_lieferant_from_liste";
	static final public String ACTION_SPECIAL_GEBINDE_FROM_LISTE = "action_gebinde_from_liste";

	private final String ACTION_SPECIAL_ZEILE_KOPIEREN = PanelBasis.ACTION_MY_OWN_NEW + "ZEILE_KOPIEREN";
	private WrapperGotoButton wbuZeileAndererMandant = null;

	private WrapperLabel wlaBezbeuilieferant = new WrapperLabel();
	private WrapperTextField wtfBezbeilieferant = new WrapperTextField();
	private WrapperLabel wlaRabatt = new WrapperLabel();
	private WrapperLabel wlaProzent = new WrapperLabel();
	private WrapperLabel wlaWaehrungNettopreis = new WrapperLabel();
	private WrapperLabel wlaEinheitMindestbestellmenge = new WrapperLabel();
	private WrapperLabel wlaEinheitStandardmenge = new WrapperLabel();
	private WrapperLabel wlaEinheitVerpackungseinheit = new WrapperLabel();
	private WrapperLabel wlaEinheitFixkosten = new WrapperLabel();
	protected WrapperURLField wtfWeblink = new WrapperURLField();

	private WrapperLabel wlaGebindemenge = new WrapperLabel();
	private WrapperNumberField wnfGebindemenge = new WrapperNumberField();

	private WrapperButton wbuGebinde = new WrapperButton();
	private WrapperTextField wtfGebinde = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);

	private WrapperLabel wlaWiederbeschaffungszeit = new WrapperLabel();
	private WrapperNumberField wnfWiederbeschaffungszeit = new WrapperNumberField();
	private WrapperLabel wlaWiederbeschaffungszeitEinheit = new WrapperLabel();

	private WrapperLabel wlaEinheitBestellmenge = new WrapperLabel();
	private WrapperLabel wlaEinheitArtikelmenge = new WrapperLabel();
	private WrapperNumberField wnfEinzelpreisBestellmenge = new WrapperNumberField();
	private WrapperNumberField wnfNettopreisBestellmenge = new WrapperNumberField();

	private WrapperLabel wlaMaterialzuschlag = new WrapperLabel();
	private WrapperLabel wlaArtikelMaterial = new WrapperLabel();
	private WrapperNumberField wnfZuschlag = new WrapperNumberField();
	private WrapperNumberField wnfEKPReis = new WrapperNumberField();

	private WrapperRadioButton wrbRabatt = new WrapperRadioButton();
	private WrapperRadioButton wrbNettopreis = new WrapperRadioButton();
	private ButtonGroup buttonGroup = new ButtonGroup();

	private WrapperLabel wlaGueltigbis = new WrapperLabel();
	private WrapperDateField wdfgueltigbis = new WrapperDateField();

	private WrapperLabel wlaAngebotsnummer = new WrapperLabel();
	private WrapperTextField wtfAngebotsnummer = new WrapperTextField();

	private WrapperSelectField wsfZertifikatart = null;

	private String eineheitWiederbeschaffung = null;

	private int iEinheitWiederbeschaffung = 1;

	private WrapperButton wbuFixkostenEdit = new WrapperButton();
	private PanelEditorPlainText panelEditorPlainTextFormel = null;
	private PanelEditorPlainText panelEditorPlainTextNichtLieferbar = null;
	private WrapperWebBauteilAbfrageButton webabfrageButton;
	private WrapperLabel wlaLetzteWebabfrage = new WrapperLabel();
	private WrapperDateField wdfLetzteWebabfrage = new WrapperDateField();
	private WrapperLabel wlaWebabfrageBestand = new WrapperLabel();
	private WrapperNumberField wnfWebabfrageBestand = new WrapperNumberField();
	private WrapperLabel wlaEinheitWebabfrageBestand = new WrapperLabel();

	

	public PanelArtikellieferant(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		Object key = getKeyWhenDetailPanel();
		if (key != null && !key.equals(LPMain.getLockMeForNew())) {

			artikellieferantDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikellieferantFindByPrimaryKey((Integer) key);

			if (artikellieferantDto != null && artikellieferantDto.getLieferantDto() != null && !artikellieferantDto
					.getLieferantDto().getMandantCNr().equals(LPMain.getTheClient().getMandant())) {

				if (!DelegateFactory.getInstance().getBenutzerServicesDelegate().hatRechtInZielmandant(
						RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF,
						artikellieferantDto.getLieferantDto().getMandantCNr())) {
					lockStateValue = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
				}

			}
		}

		return lockStateValue;

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfArtikelnummer;
	}

	void dialogQueryPartnerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLieferant = PartnerFilterFactory.getInstance().createPanelFLRLieferant(getInternalFrame(),
				artikellieferantDto.getLieferantIId(), true, false);
		new DialogQuery(panelQueryFLRLieferant);
	}

	protected void dto2Components() throws Throwable {

		wbuZeileAndererMandant.setOKey(null);
		wbuZeileAndererMandant.setEnabled(false);

		wcbHerstellerbezeichnung.setShort(artikellieferantDto.getBHerstellerbez());
		wcbWebshop.setShort(artikellieferantDto.getBWebshop());
		wcbNichtLieferbar.setShort(artikellieferantDto.getBNichtLieferbar());
		wdfgueltigab.setTimestamp(artikellieferantDto.getTPreisgueltigab());
		wdfgueltigbis.setTimestamp(artikellieferantDto.getTPreisgueltigbis());
		wnfMindestbestellmenge.setDouble(artikellieferantDto.getFMindestbestelmenge());

		wnfEinzelpreis.setBigDecimal(artikellieferantDto.getNEinzelpreis());
		wnfFixkosten.setBigDecimal(artikellieferantDto.getNFixkosten());
		wnfInitialkosten.setBigDecimal(artikellieferantDto.getNInitialkosten());
		wnfNettopreis.setBigDecimal(artikellieferantDto.getNNettopreis());

		// PJ 2496
		wnfEinzelpreis.setBigDecimal(artikellieferantDto.getNEinzelpreis());
		setTooltipMandantenwaehrung(wnfEinzelpreis, artikellieferantDto.getLieferantDto().getWaehrungCNr());

		wnfFixkosten.setBigDecimal(artikellieferantDto.getNFixkosten());
		setTooltipMandantenwaehrung(wnfFixkosten, artikellieferantDto.getLieferantDto().getWaehrungCNr());

		Integer iTage = DelegateFactory.getInstance().getBestellungDelegate()
				.getWiederbeschaffungsmoralEinesArtikels(artikellieferantDto.getArtikelIId(), artikellieferantDto.getLieferantDto().getIId());
		String durchschnitt = null;

		if (iTage != null) {
			durchschnitt = "\u00D8 WBZ:" + iTage / iEinheitWiederbeschaffung;
		} else {
			durchschnitt = "\u00D8 WBZ:?";
		}

		wlaWiederbeschaffungszeitEinheit.setText(eineheitWiederbeschaffung + " " + durchschnitt);

		wnfNettopreis.setBigDecimal(artikellieferantDto.getNNettopreis());
		setTooltipMandantenwaehrung(wnfNettopreis, artikellieferantDto.getLieferantDto().getWaehrungCNr());
		wtfWeblink.setText(artikellieferantDto.getCWeblink());

		wnfRabatt.setDouble(artikellieferantDto.getFRabatt());
		wnfStandardmenge.setDouble(artikellieferantDto.getFStandardmenge());

		wtfArtikelnummer.setText(artikellieferantDto.getCArtikelnrlieferant());
		wtfAngebotsnummer.setText(artikellieferantDto.getCAngebotnummer());
		wtfBezbeilieferant.setText(artikellieferantDto.getCBezbeilieferant());
		wbuLieferant.setOKey(artikellieferantDto.getLieferantDto().getIId());
		wtfLieferant.setText(artikellieferantDto.getLieferantDto().getPartnerDto().formatTitelAnrede());

		if (!artikellieferantDto.getLieferantDto().getMandantCNr().equals(LPMain.getTheClient().getMandant())) {
			wbuZeileAndererMandant.setEnabled(false);
			wbuZeileAndererMandant.setOKey(artikellieferantDto.getIId());
		}

		wlaWaehrungEinzelpreis.setText(artikellieferantDto.getLieferantDto().getWaehrungCNr().trim());
		wlaWaehrungNettopreis.setText(artikellieferantDto.getLieferantDto().getWaehrungCNr().trim());
		wlaEinheitFixkosten.setText(artikellieferantDto.getLieferantDto().getWaehrungCNr().trim());
		wcoEinheitVpe.setKeyOfSelectedItem(artikellieferantDto.getEinheitCNrVpe());

		if (Helper.short2boolean(artikellieferantDto.getBRabattbehalten()) == true) {
			wrbRabatt.setSelected(true);
		} else {
			wrbNettopreis.setSelected(true);
		}

		wtfRabattgruppe.setText(artikellieferantDto.getCRabattgruppe());
		wnfVerpackungseinheit.setBigDecimal(artikellieferantDto.getNVerpackungseinheit());
		wnfWiederbeschaffungszeit.setInteger(artikellieferantDto.getIWiederbeschaffungszeit());
		if (wnfEinzelpreis.getDouble() == null) {
			wnfRabatt.setDouble(null);
			wnfNettopreis.setDouble(null);
			wnfRabatt.setMandatoryField(false);
			wnfNettopreis.setMandatoryField(false);

		} else {
			wnfRabatt.setMandatoryField(true);
			wnfNettopreis.setMandatoryField(true);

		}

		wsfZertifikatart.setKey(artikellieferantDto.getZertifikatartIId());

		if (artikellieferantDto.getGebindeIId() != null) {
			GebindeDto dto = DelegateFactory.getInstance().getArtikelDelegate()
					.gebindeFindByPrimaryKey(artikellieferantDto.getGebindeIId());
			wtfGebinde.setText(dto.getCBez());
		} else {
			wtfGebinde.setText(null);
		}

		wnfGebindemenge.setBigDecimal(artikellieferantDto.getNGebindemenge());

		this.setStatusbarPersonalIIdAendern(artikellieferantDto.getPersonalIIdAendern());
		this.setStatusbarTAendern(artikellieferantDto.getTAendern());

		wnfWebabfrageBestand.setBigDecimal(artikellieferantDto.getNWebabfrageBestand());
		wdfLetzteWebabfrage.setTimestamp(artikellieferantDto.getTLetzteWebabfrage());

		wbuAnfragepositionlieferdaten.setOKey(artikellieferantDto.getAnfragepositionlieferdatenIId());
		if (artikellieferantDto.getAnfragepositionlieferdatenIId() != null) {
			AnfragepositionlieferdatenDto aplDto = DelegateFactory.getInstance().getAnfragepositionDelegate()
					.anfragepositionlieferdatenFindByPrimaryKey(artikellieferantDto.getAnfragepositionlieferdatenIId());
			AnfragepositionDto apDto = DelegateFactory.getInstance().getAnfragepositionDelegate()
					.anfragepositionFindByPrimaryKey(aplDto.getAnfragepositionIId());
			AnfrageDto afDto = DelegateFactory.getInstance().getAnfrageDelegate()
					.anfrageFindByPrimaryKey(apDto.getBelegIId());

			wtfAnfragepositionlieferdaten.setText(afDto.getCNr());
		} else {
			wtfAnfragepositionlieferdaten.setText(null);
		}

		ArrayList<String> staffeln = DelegateFactory.getInstance().getArtikelDelegate()
				.getEKStaffeln(artikellieferantDto.getIId());

		Object[] tempZeilen = new Object[staffeln.size() + 1];

		tempZeilen[0] = LPMain.getTextRespectUISPr("artikel.lieferant.staffeln");

		for (int i = 0; i < staffeln.size(); i++) {

			tempZeilen[i + 1] = staffeln.get(i);
		}

		list.setListData(tempZeilen);

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLieferant) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey((Integer) key);
				wbuLieferant.setOKey(lieferantDto.getIId());
				wtfLieferant.setText(lieferantDto.getPartnerDto().formatTitelAnrede());

				wlaWaehrungEinzelpreis.setText(lieferantDto.getWaehrungCNr().trim());
				wlaWaehrungNettopreis.setText(lieferantDto.getWaehrungCNr().trim());
				wlaEinheitFixkosten.setText(lieferantDto.getWaehrungCNr().trim());

				artikellieferantDto.setLieferantIId(lieferantDto.getIId());
				artikellieferantDto.setLieferantDto(lieferantDto);
			}

			else if (e.getSource() == panelQueryFLRGebinde) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				GebindeDto dto = DelegateFactory.getInstance().getArtikelDelegate()
						.gebindeFindByPrimaryKey((Integer) key);
				wtfGebinde.setText(dto.getCBez());
				artikellieferantDto.setGebindeIId(dto.getIId());
				wnfGebindemenge.setBigDecimal(dto.getNMenge());

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRGebinde) {
				wtfGebinde.setText(null);
				artikellieferantDto.setGebindeIId(null);
				wnfGebindemenge.setBigDecimal(null);
			}
		}
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		this.setLayout(new GridBagLayout());
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		wbuLieferant.setText(LPMain.getTextRespectUISPr("lp.lieferant") + "...");
		wbuLieferant.setActionCommand(PanelArtikellieferant.ACTION_SPECIAL_LIEFERANT_FROM_LISTE);
		wbuLieferant.addActionListener(this);

		wbuGebinde.setText(LPMain.getTextRespectUISPr("artikel.gebinde") + "...");
		wbuGebinde.setActionCommand(PanelArtikellieferant.ACTION_SPECIAL_GEBINDE_FROM_LISTE);
		wbuGebinde.addActionListener(this);

		// SP5018
		wnfVerpackungseinheit.setMinimumValue(new BigDecimal(0.01));

		wtfLieferant.setText("");
		wtfLieferant.setActivatable(false);
		wtfLieferant.setMandatoryField(true);
		wtfLieferant.setColumnsMax(500);
		wcbHerstellerbezeichnung.setText(LPMain.getTextRespectUISPr("artikel.herstellerbezeichnung"));
		wcbWebshop.setText(LPMain.getTextRespectUISPr("lp.webshop"));
		wcbNichtLieferbar.setText(LPMain.getTextRespectUISPr("artikel.artikellieferant.nichtlieferbar"));
		wlaArtikelnummerbeilieferant.setText(LPMain.getTextRespectUISPr("artikel.artikelnummerdeslieferanten"));
		wlaEinzelpreis.setText(LPMain.getTextRespectUISPr("label.einzelpreis"));
		wlaNettopreis.setText(LPMain.getTextRespectUISPr("label.nettopreis"));
		wlaMindestbestellmenge.setText(LPMain.getTextRespectUISPr("artikel.mindestbestellmenge"));
		wlaWiederbeschaffungszeit
				.setText(LPMain.getTextRespectUISPr("artikel.artikellieferant.wiederbeschaffungszeit"));

		wcoEinheitVpe.setMap(DelegateFactory.getInstance().getSystemDelegate().getAllEinheiten());

		wcoEinheitVpe.addActionListener(new PanelArtikellieferant_wcoEinheitVpe_actionAdapter(this));

		wtfAnfragepositionlieferdaten.setActivatable(false);

		wbuFixkostenEdit.setMinimumSize(new Dimension(23, 23));
		wbuFixkostenEdit.setPreferredSize(new Dimension(23, 23));
		wbuFixkostenEdit.setActionCommand(ACTION_TEXT);
		wbuFixkostenEdit.setToolTipText(LPMain.getTextRespectUISPr("artikel.artikellieferant.kommentarzufixkosten"));
		wbuFixkostenEdit.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/notebook.png")));
		wbuFixkostenEdit.addActionListener(this);
		wbuFixkostenEdit.getActionMap().put(ACTION_TEXT, new ButtonAbstractAction(this, ACTION_TEXT));

		wbuNichtLieferbarEdit.setMinimumSize(new Dimension(23, 23));
		wbuNichtLieferbarEdit.setPreferredSize(new Dimension(23, 23));
		wbuNichtLieferbarEdit.setMaximumSize(new Dimension(23, 23));
		wbuNichtLieferbarEdit.setActionCommand(ACTION_TEXT);
		wbuNichtLieferbarEdit
				.setToolTipText(LPMain.getTextRespectUISPr("artikel.artikellieferant.kommentarzunichtlieferbar"));
		wbuNichtLieferbarEdit.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/notebook.png")));
		wbuNichtLieferbarEdit.addActionListener(this);
		wbuNichtLieferbarEdit.getActionMap().put(ACTION_TEXT, new ButtonAbstractAction(this, ACTION_TEXT));

		wsfZertifikatart = new WrapperSelectField(WrapperSelectField.ZERTIFIKATART, getInternalFrame(), true);

		buttonGroup.add(wrbRabatt);
		buttonGroup.add(wrbNettopreis);

		wrbRabatt.setSelected(true);

		wrbRabatt.addActionListener(this);
		wrbNettopreis.addActionListener(this);

		wlaWiederbeschaffungszeitEinheit.setHorizontalAlignment(SwingConstants.LEFT);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT,
						ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null) {
			if (parameter.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				eineheitWiederbeschaffung = LPMain.getTextRespectUISPr("lp.kw");
				iEinheitWiederbeschaffung = 7;
			} else if (parameter.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				eineheitWiederbeschaffung = LPMain.getTextRespectUISPr("lp.tage");
			} else {
				eineheitWiederbeschaffung = "?";
			}
		}

		wtfGebinde.setActivatable(false);
		wlaWiederbeschaffungszeitEinheit.setText(eineheitWiederbeschaffung);

		wlaMaterialzuschlag.setText(LPMain.getTextRespectUISPr("label.zuschlag"));

		wlaFixkosten.setText(LPMain.getTextRespectUISPr("lp.fixkosten"));
		wlaGueltigab.setText(LPMain.getTextRespectUISPr("lp.gueltigab"));
		wlaAngebotsnummer.setText(LPMain.getTextRespectUISPr("anf.angebotnummer"));

		wlaGueltigbis.setText(LPMain.getTextRespectUISPr("lp.gueltigbis"));
		wlaWaehrungEinzelpreis.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungEinzelpreis.setText("");
		wlaStandardmenge.setText(LPMain.getTextRespectUISPr("artikel.standardmenge"));
		wlaVerpackungseinheit.setText(LPMain.getTextRespectUISPr("artikel.verpackungseinheit"));
		wlaRabattgruppe.setText(LPMain.getTextRespectUISPr("lp.rabattgruppe"));
		wtfRabattgruppe.setColumnsMax(5);
		wtfRabattgruppe.setText("");

		wtfWeblink.setColumnsMax(300);

		wbuAnfragepositionlieferdaten.setText(LPMain.getTextRespectUISPr("artikel.artikellieferant.anfrage"));

		wtfArtikelnummer.setSelectionStart(17);
		wtfArtikelnummer.setColumnsMax(ArtikelFac.MAX_ARTIKELLIEFERANT_ARTIKELNUMMERBEILIEFERANT);
		wtfArtikelnummer.setText("");
		wlaBezbeuilieferant.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));

		wlaGebindemenge.setText(LPMain.getTextRespectUISPr("artikel.gebindemenge"));

		wlaInitialkosten.setText("init.Kosten");

		wtfBezbeilieferant.setColumnsMax(ArtikelFac.MAX_ARTIKELLIEFERANT_BEZEICHNUNGBEILIEFERANT);
		wtfBezbeilieferant.setText("");
		wlaRabatt.setText(LPMain.getTextRespectUISPr("label.rabattsumme"));
		wlaProzent.setHorizontalAlignment(SwingConstants.LEFT);
		wlaProzent.setText("%");
		wlaWaehrungNettopreis.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungNettopreis.setText("");
		wlaEinheitMindestbestellmenge.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitMindestbestellmenge.setText("");
		wlaEinheitStandardmenge.setToolTipText("");
		wlaEinheitStandardmenge.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitStandardmenge.setText("");
		wlaEinheitFixkosten.setHorizontalAlignment(SwingConstants.LEFT);
		wlaEinheitFixkosten.setText("");
		wnfNettopreis.setDependenceField(true);
		wnfWiederbeschaffungszeit.setFractionDigits(0);
		wnfWiederbeschaffungszeit.setMinimumValue(0);
		wlaEinheitMindestbestellmenge.setText(internalFrameArtikel.getArtikelDto().getEinheitCNr().trim());
		wlaEinheitStandardmenge.setText(internalFrameArtikel.getArtikelDto().getEinheitCNr().trim());
		wlaEinheitVerpackungseinheit.setText(internalFrameArtikel.getArtikelDto().getEinheitCNr().trim());

		wlaEinheitArtikelmenge.setHorizontalAlignment(SwingConstants.CENTER);
		wlaEinheitBestellmenge.setHorizontalAlignment(SwingConstants.CENTER);

		wlaMaterialzuschlag.setHorizontalAlignment(SwingConstants.CENTER);
		wlaArtikelMaterial.setHorizontalAlignment(SwingConstants.CENTER);

		wnfNettopreis.addFocusListener(new PanelArtikellieferant_wnfNettopreis_focusAdapter(this));
		wnfRabatt.setDependenceField(true);

		wnfRabatt.addFocusListener(new PanelArtikellieferant_wnfRabatt_focusAdapter(this));
		wdfgueltigab.setMandatoryField(true);
		wnfEinzelpreis.addFocusListener(new PanelArtikellieferant_wnfEinzelpreis_focusAdapter(this));
		wnfEinzelpreisBestellmenge
				.addFocusListener(new PanelArtikellieferant_wnfEinzelpreisBestelleinheit_focusAdapter(this));
		wnfNettopreisBestellmenge
				.addFocusListener(new PanelArtikellieferant_wnfNettopreisBestelleinheit_focusAdapter(this));

		// Projekt 2619 Nachkommastellen
		int iNachkommastellen = Defaults.getInstance().getIUINachkommastellenPreiseEK();
		wnfEinzelpreis.setFractionDigits(iNachkommastellen);
		wnfNettopreis.setFractionDigits(iNachkommastellen);
		wnfEinzelpreisBestellmenge.setFractionDigits(iNachkommastellen);
		wnfNettopreisBestellmenge.setFractionDigits(iNachkommastellen);

		wnfFixkosten.setFractionDigits(iNachkommastellen);
		wnfInitialkosten.setFractionDigits(iNachkommastellen);

		wnfZuschlag.setFractionDigits(6);
		wnfZuschlag.setActivatable(false);
		wnfEKPReis.setFractionDigits(iNachkommastellen);
		wnfEKPReis.setActivatable(false);
		wlaLetzteWebabfrage.setText(LPMain.getTextRespectUISPr("artikel.artikellieferant.letztewebabfrage"));
		wdfLetzteWebabfrage.setActivatable(false);
		wlaWebabfrageBestand.setText(LPMain.getTextRespectUISPr("artikel.artikellieferant.bestand"));
		wnfWebabfrageBestand.setActivatable(false);
		wlaEinheitWebabfrageBestand.setText(internalFrameArtikel.getArtikelDto().getEinheitCNr().trim());
		wlaEinheitWebabfrageBestand.setHorizontalAlignment(SwingConstants.LEFT);

		// Goto Unterstueckliste
		wbuZeileAndererMandant = new WrapperGotoButton(LPMain.getTextRespectUISPr("artikel.lieferant.gotozielmandant"),
				com.lp.util.GotoHelper.GOTO_ARTIKELLIEFERANT_ANDERER_MANDANT);
		wbuZeileAndererMandant.setEnabled(false);
		wbuZeileAndererMandant.setActivatable(false);
		wbuZeileAndererMandant.getWrapperButton()
				.setToolTipText(LPMain.getTextRespectUISPr("artikel.lieferant.gotozielmandant"));
		wbuZeileAndererMandant.getWrapperButtonGoTo()
				.setToolTipText(LPMain.getTextRespectUISPr("artikel.lieferant.gotozielmandant"));
		wbuZeileAndererMandant.setMinimumSize(new Dimension(110, HelperClient.getToolsPanelButtonDimension().height));
		wbuZeileAndererMandant.setPreferredSize(new Dimension(110, HelperClient.getToolsPanelButtonDimension().height));

		jpaWorkingOn = new JPanel(new MigLayout("wrap 10",
				"[fill, 15%|fill,10%|fill,6%|fill,2%|fill,10%|fill,8%|fill,19%|fill, 8%|5%, fill, 0:0:9%|fill,12%]"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuLieferant);
		jpaWorkingOn.add(wtfLieferant, "span 3");
		jpaWorkingOn.add(wtfWeblink, "span");

		jpaWorkingOn.add(wlaArtikelnummerbeilieferant);
		jpaWorkingOn.add(wtfArtikelnummer, "span 3");
		jpaWorkingOn.add(wcbHerstellerbezeichnung, "span 2");
		jpaWorkingOn.add(wcbWebshop);
		jpaWorkingOn.add(wbuAnfragepositionlieferdaten, "span 2");
		jpaWorkingOn.add(wtfAnfragepositionlieferdaten);

		jpaWorkingOn.add(wlaBezbeuilieferant);
		jpaWorkingOn.add(wtfBezbeilieferant, "span 6");
		jpaWorkingOn.add(wcbNichtLieferbar, "span 2");
		jpaWorkingOn.add(wbuNichtLieferbarEdit);

		jpaWorkingOn.add(wlaEinheitArtikelmenge, "skip");
		jpaWorkingOn.add(wlaEinheitBestellmenge, "skip 2");
		jpaWorkingOn.add(wlaMaterialzuschlag, "wrap");

		jpaWorkingOn.add(wlaEinzelpreis);
		jpaWorkingOn.add(wnfEinzelpreis);
		jpaWorkingOn.add(wlaWaehrungEinzelpreis, "span 2");
		jpaWorkingOn.add(wnfEinzelpreisBestellmenge);
		jpaWorkingOn.add(wlaArtikelMaterial);
		jpaWorkingOn.add(wlaStandardmenge);
		jpaWorkingOn.add(wnfStandardmenge, "span 2");
		jpaWorkingOn.add(wlaEinheitStandardmenge);

		jpaWorkingOn.add(wlaRabatt);
		jpaWorkingOn.add(wnfRabatt);
		jpaWorkingOn.add(wlaProzent);
		jpaWorkingOn.add(wrbRabatt);
		jpaWorkingOn.add(wnfZuschlag, "skip");
		jpaWorkingOn.add(wlaMindestbestellmenge);
		jpaWorkingOn.add(wnfMindestbestellmenge, "span 2");
		jpaWorkingOn.add(wlaEinheitMindestbestellmenge);

		jpaWorkingOn.add(wlaNettopreis);
		jpaWorkingOn.add(wnfNettopreis);
		jpaWorkingOn.add(wlaWaehrungNettopreis);
		jpaWorkingOn.add(wrbNettopreis);
		jpaWorkingOn.add(wnfNettopreisBestellmenge);
		jpaWorkingOn.add(wnfEKPReis);
		jpaWorkingOn.add(wlaVerpackungseinheit);
		jpaWorkingOn.add(wnfVerpackungseinheit);
		jpaWorkingOn.add(wlaEinheitVerpackungseinheit);
		jpaWorkingOn.add(wcoEinheitVpe);

		jpaWorkingOn.add(wlaFixkosten);
		jpaWorkingOn.add(wnfFixkosten);

		jpaWorkingOn.add(wlaEinheitFixkosten);
		jpaWorkingOn.add(wbuFixkostenEdit);

		jpaWorkingOn.add(wlaInitialkosten);

		jpaWorkingOn.add(wnfInitialkosten);

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_DIMENSIONEN_BESTELLEN, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getTheClient().getMandant());
		boolean bDimensionenBestellen = (Boolean) parameter.getCWertAsObject();
		if (bDimensionenBestellen == false) {
			wlaInitialkosten.setVisible(false);
			wnfInitialkosten.setVisible(false);
		}

		jpaWorkingOn.add(wlaWiederbeschaffungszeit);
		jpaWorkingOn.add(wnfWiederbeschaffungszeit, "span 2");
		jpaWorkingOn.add(wlaWiederbeschaffungszeitEinheit);

		jpaWorkingOn.add(wlaGueltigab);
		jpaWorkingOn.add(wdfgueltigab);
		jpaWorkingOn.add(wlaRabattgruppe, "skip ,span 2");
		jpaWorkingOn.add(wtfRabattgruppe);
		jpaWorkingOn.add(wbuGebinde);
		jpaWorkingOn.add(wtfGebinde, "span 2,wrap");

		jpaWorkingOn.add(wlaGueltigbis);
		jpaWorkingOn.add(wdfgueltigbis);
		jpaWorkingOn.add(wlaAngebotsnummer, "skip ,span 2");
		jpaWorkingOn.add(wtfAngebotsnummer);
		jpaWorkingOn.add(wlaGebindemenge);
		jpaWorkingOn.add(wnfGebindemenge, "span 2,wrap");

		jpaWorkingOn.add(wlaLetzteWebabfrage, "skip 4");
		jpaWorkingOn.add(wdfLetzteWebabfrage);
		jpaWorkingOn.add(wlaWebabfrageBestand);
		jpaWorkingOn.add(wnfWebabfrageBestand, "span 2");
		jpaWorkingOn.add(wlaEinheitWebabfrageBestand);

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZERTIFIKATART)) {

			jpaWorkingOn.add(wsfZertifikatart.getWrapperButton());
			jpaWorkingOn.add(wsfZertifikatart.getWrapperTextField(), "span 9, wrap");

		}

		list = new JList();
		Color defaultCellForegroundColor = UIManager.getColor("Table.foreground");
		list.setSelectionForeground(defaultCellForegroundColor);
		// list.setFont(new java.awt.Font("monospaced", 0, 12));
		list.setFont(new java.awt.Font("monospaced", 0, 11));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);

		staffeln.getViewport().add(list);

		jpaWorkingOn.add(staffeln, "grow, height 10:300:500, span ");

		String[] aWhichButtonIUse = null;
		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF)) {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD,
					ACTION_PRINT, };
		} else {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD, ACTION_PRINT, };
		}

		enableToolsPanelButtons(aWhichButtonIUse);

		if (LPMain.getInstance().getDesktopController()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {

			createAndSaveAndShowButton("/com/lp/client/res/up_plus.png",
					LPMain.getTextRespectUISPr("artikel.lieferant.zeile.kopieren"), ACTION_SPECIAL_ZEILE_KOPIEREN,
					null);
			jpaButtonAction.add(wbuZeileAndererMandant);

		}

		installWebabfrageButton();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT_FROM_LISTE)) {
			dialogQueryPartnerFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_GEBINDE_FROM_LISTE)) {
			panelQueryFLRGebinde = ArtikelFilterFactory.getInstance().createPanelFLRGebinde(getInternalFrame(),
					artikellieferantDto.getGebindeIId(), true);
			new DialogQuery(panelQueryFLRGebinde);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ZEILE_KOPIEREN)) {

			if (artikellieferantDto != null && artikellieferantDto.getIId() != null) {

				ArtikellieferantDto artikellieferantDtoLocal = DelegateFactory.getInstance().getArtikelDelegate()
						.artikellieferantFindByPrimaryKey(artikellieferantDto.getIId());

				LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByiIdPartnercNrMandantOhneExc(
								artikellieferantDtoLocal.getLieferantDto().getPartnerIId(),
								LPMain.getTheClient().getMandant());
				if (lfDto != null) {
					artikellieferantDtoLocal.setLieferantIId(lfDto.getIId());
					artikellieferantDtoLocal.setIId(null);
					Integer artiellieferantIId = DelegateFactory.getInstance().getArtikelDelegate()
							.createArtikellieferant(artikellieferantDtoLocal);

					internalFrameArtikel.getTabbedPaneArtikel().getPanelQueryArtikellieferant().eventActionRefresh(null,
							false);
					internalFrameArtikel.getTabbedPaneArtikel().getPanelQueryArtikellieferant()
							.setSelectedId(artiellieferantIId);
					setKeyWhenDetailPanel(artiellieferantIId);
					eventYouAreSelected(false);
				} else {

					String meldung = LPMain.getMessageTextRespectUISPr("artikel.lieferant.zeile.kopieren.error",
							artikellieferantDtoLocal.getLieferantDto().getMandantCNr(),
							artikellieferantDtoLocal.getLieferantDto().getPartnerDto().formatFixName1Name2(),
							LPMain.getTheClient().getMandant());

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), meldung);
				}

			}

		}

		if (e.getSource().equals(wrbNettopreis) || e.getSource().equals(wrbRabatt)) {
			if (wrbNettopreis.isSelected()) {
				wnfRabatt.setEditable(false);
				wnfNettopreis.setEditable(true);
			} else {
				wnfNettopreis.setEditable(false);
				wnfRabatt.setEditable(true);

			}
		}

	}

	protected void setDefaults() {

		// darf Preise sehen
		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			wlaEinzelpreis.setVisible(false);
			wlaNettopreis.setVisible(false);
			wlaFixkosten.setVisible(false);
			wlaWaehrungEinzelpreis.setVisible(false);
			wlaRabattgruppe.setVisible(false);
			wnfRabatt.setVisible(false);
			wtfRabattgruppe.setVisible(false);
			wnfFixkosten.setVisible(false);
			wbuFixkostenEdit.setVisible(false);
			wnfNettopreis.setVisible(false);
			wnfEinzelpreis.setVisible(false);
			wlaRabatt.setVisible(false);
			wlaProzent.setVisible(false);
			wlaWaehrungNettopreis.setVisible(false);
			wlaEinheitFixkosten.setVisible(false);
			wrbRabatt.setVisible(false);
			wrbNettopreis.setVisible(false);
		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getArtikelDelegate().removeArtikellieferant(artikellieferantDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		artikellieferantDto = new ArtikellieferantDto();
		panelEditorPlainTextFormel = null;
		panelEditorPlainTextNichtLieferbar = null;
		wnfRabatt.setMandatoryField(false);
		wnfNettopreis.setMandatoryField(false);
	}

	protected void components2Dto() throws Throwable {

		if (wnfEinzelpreis.getDouble() == null) {
			wnfRabatt.setDouble(null);
			wnfEinzelpreis.setDouble(null);
		}
		artikellieferantDto.setBHerstellerbez(wcbHerstellerbezeichnung.getShort());
		artikellieferantDto.setBWebshop(wcbWebshop.getShort());
		artikellieferantDto.setBNichtLieferbar(wcbNichtLieferbar.getShort());
		artikellieferantDto.setCArtikelnrlieferant(wtfArtikelnummer.getText());
		artikellieferantDto.setCBezbeilieferant(wtfBezbeilieferant.getText());
		artikellieferantDto.setCRabattgruppe(wtfRabattgruppe.getText());
		artikellieferantDto.setTPreisgueltigab(wdfgueltigab.getTimestamp());
		artikellieferantDto.setTPreisgueltigbis(wdfgueltigbis.getTimestamp());
		artikellieferantDto.setFMindestbestelmenge(wnfMindestbestellmenge.getDouble());
		artikellieferantDto.setFRabatt(wnfRabatt.getDouble());
		artikellieferantDto.setFStandardmenge(wnfStandardmenge.getDouble());
		artikellieferantDto.setNVerpackungseinheit(wnfVerpackungseinheit.getBigDecimal());
		artikellieferantDto.setIWiederbeschaffungszeit(wnfWiederbeschaffungszeit.getInteger());
		artikellieferantDto.setNEinzelpreis(wnfEinzelpreis.getBigDecimal());
		artikellieferantDto.setNFixkosten(wnfFixkosten.getBigDecimal());
		artikellieferantDto.setNInitialkosten(wnfInitialkosten.getBigDecimal());
		artikellieferantDto.setNNettopreis(wnfNettopreis.getBigDecimal());
		artikellieferantDto.setCAngebotnummer(wtfAngebotsnummer.getText());
		artikellieferantDto.setZertifikatartIId(wsfZertifikatart.getIKey());
		artikellieferantDto.setCWeblink(wtfWeblink.getText());
		artikellieferantDto.setEinheitCNrVpe((String) wcoEinheitVpe.getKeyOfSelectedItem());

		artikellieferantDto.setNGebindemenge(wnfGebindemenge.getBigDecimal());

		if (wrbRabatt.isSelected() == true) {
			artikellieferantDto.setBRabattbehalten(Helper.boolean2Short(true));
		} else {
			artikellieferantDto.setBRabattbehalten(Helper.boolean2Short(false));
		}

		if (panelEditorPlainTextFormel != null) {
			artikellieferantDto.setXKommentarFixkosten(panelEditorPlainTextFormel.getPlainText());
		}

		if (panelEditorPlainTextNichtLieferbar != null) {
			artikellieferantDto.setXKommentarNichtLieferbar(panelEditorPlainTextNichtLieferbar.getPlainText());
		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		panelEditorPlainTextFormel = null;
		panelEditorPlainTextNichtLieferbar = null;
		eventActionSpecial(new ActionEvent(wrbNettopreis, 0, ""));

	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		String add2Title = LPMain.getTextRespectUISPr("artikel.report.lieferantenpreis");
		getInternalFrame().showReportKriterien(new ReportLieferantenpreis(internalFrameArtikel, add2Title));
	}

	private void setEditorButtonColor() {
		wbuFixkostenEdit.setIcon(artikellieferantDto != null && artikellieferantDto.getXKommentarFixkosten() != null
				&& artikellieferantDto.getXKommentarFixkosten().length() > 0 ? IconFactory.getCommentExist()
						: IconFactory.getEditorEdit());

		wbuNichtLieferbarEdit
				.setIcon(artikellieferantDto != null && artikellieferantDto.getXKommentarNichtLieferbar() != null
						&& artikellieferantDto.getXKommentarNichtLieferbar().length() > 0
								? IconFactory.getCommentExist()
								: IconFactory.getEditorEdit());

	}

	protected void eventActionText(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wbuFixkostenEdit)) {
			if (artikellieferantDto != null) {
				String plaintText = null;
				if (panelEditorPlainTextFormel != null) {
					plaintText = panelEditorPlainTextFormel.getPlainText();
				} else {
					plaintText = artikellieferantDto.getXKommentarFixkosten();
				}

				panelEditorPlainTextFormel = new PanelEditorPlainText(getInternalFrame(), new WrapperTextField(),
						getAdd2Title(), plaintText, getLockedstateDetailMainKey().getIState());

				getInternalFrame().showPanelDialog(panelEditorPlainTextFormel);
				getInternalFrame().getContentPane().validate();
			}
		}
		if (e.getSource().equals(wbuNichtLieferbarEdit)) {
			if (artikellieferantDto != null) {
				String plaintText = null;
				if (panelEditorPlainTextNichtLieferbar != null) {
					plaintText = panelEditorPlainTextNichtLieferbar.getPlainText();
				} else {
					plaintText = artikellieferantDto.getXKommentarNichtLieferbar();
				}

				panelEditorPlainTextNichtLieferbar = new PanelEditorPlainText(getInternalFrame(),
						new WrapperTextField(), getAdd2Title(), plaintText, getLockedstateDetailMainKey().getIState());

				getInternalFrame().showPanelDialog(panelEditorPlainTextNichtLieferbar);
				getInternalFrame().getContentPane().validate();
			}
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		if (wnfEinzelpreis.hasFocus()) {
			wnfEinzelpreis_focusLost(null);
		} else if (wnfRabatt.hasFocus()) {
			wnfRabatt_focusLost(null);
		} else if (wnfNettopreis.hasFocus()) {
			wnfNettopreis_focusLost(null);
		}

		if (allMandatoryFieldsSetDlg()) {

			// PJ 08/14231
			if (artikellieferantDto != null && artikellieferantDto.getIId() != null) {
				if (artikellieferantDto.getTPreisgueltigab() != null) {
					if (Helper.cutTimestamp(artikellieferantDto.getTPreisgueltigab()).getTime() == Helper
							.cutDate(wdfgueltigab.getDate()).getTime()) {
						boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
								LPMain.getTextRespectUISPr("artikel.error.ekpreisaenderungmitselbemdatum"));
						if (b == false) {
							return;
						}
					}
				}

			}

			components2Dto();

			if (artikellieferantDto.getIId() == null) {
				artikellieferantDto.setArtikelIId(internalFrameArtikel.getArtikelDto().getIId());
				artikellieferantDto.setMandantCNr(LPMain.getTheClient().getMandant());

				artikellieferantDto.setIId(
						DelegateFactory.getInstance().getArtikelDelegate().createArtikellieferant(artikellieferantDto));

				setKeyWhenDetailPanel(artikellieferantDto.getIId());

			} else {
				
				
				
				ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
						.getParametermandant(ParameterFac.PARAMETER_DIMENSIONEN_BESTELLEN, ParameterFac.KATEGORIE_ARTIKEL,
								LPMain.getTheClient().getMandant());
				boolean bDimensionenBestellen = (Boolean) parameter.getCWertAsObject();

				boolean zuschnittsartikelNeuBerechnen = false;

				if (bDimensionenBestellen && artikellieferantDto.getNNettopreis() != null) {

					int iArtikelart = DelegateFactory.getInstance().getArtikelDelegate().ichBinZuschittOderBasisArtikel(artikellieferantDto.getArtikelIId());

					if (iArtikelart == ArtikelFac.ZUSCHNITTSARTIKEL_BASISARTIKEL) {
						int iOption = DialogFactory.showModalJaNeinAbbrechenDialog(getInternalFrame(),
								LPMain.getInstance()
										.getTextRespectUISPr("artikel.lieferant.zuschnittsartikel.update.basis"),
								LPMain.getInstance().getTextRespectUISPr("lp.frage"));

						if (iOption == JOptionPane.YES_OPTION) {
							zuschnittsartikelNeuBerechnen = true;
						} else if (iOption == JOptionPane.CANCEL_OPTION) {
							return;
						}

					} else if (iArtikelart == ArtikelFac.ZUSCHNITTSARTIKEL_ZUSCHNITTARTIKEL) {
						int iOption = DialogFactory.showModalJaNeinAbbrechenDialog(getInternalFrame(),
								LPMain.getInstance().getTextRespectUISPr(
										"artikel.lieferant.zuschnittsartikel.update.basisundzuschnitt"),
								LPMain.getInstance().getTextRespectUISPr("lp.frage"));

						if (iOption == JOptionPane.YES_OPTION) {
							zuschnittsartikelNeuBerechnen = true;
						} else if (iOption == JOptionPane.CANCEL_OPTION) {
							return;
						}
					}

				}

				DelegateFactory.getInstance().getArtikelDelegate().updateArtikellieferant(artikellieferantDto, zuschnittsartikelNeuBerechnen);
				
			
			}

			

			// PJ 14400
			ArtikellieferantstaffelDto[] mengenstaffelDtos = DelegateFactory.getInstance().getArtikelDelegate()
					.artikellieferantstaffelFindByArtikellieferantIId(artikellieferantDto.getIId());

			if (mengenstaffelDtos != null && mengenstaffelDtos.length > 0) {
				boolean bMeldungMussangezeigtWerden = false;
				for (int i = 0; i < mengenstaffelDtos.length; i++) {
					if (Helper.short2boolean(mengenstaffelDtos[i].getBRabattbehalten()) == false) {
						bMeldungMussangezeigtWerden = true;
					}
				}

				if (bMeldungMussangezeigtWerden) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hinweis"),
							LPMain.getTextRespectUISPr("artikel.error.staffelpreiseaendern"));
				}

			}

			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(internalFrameArtikel.getArtikelDto().getIId().toString());
			}
			eventYouAreSelected(false);
		}

	}

	public void setBestellpreisVisible() throws Throwable {
		wnfVerpackungseinheit.setMandatoryField(false);
		if (internalFrameArtikel.getArtikelDto().getEinheitCNrBestellung() != null) {

			String s = internalFrameArtikel.getArtikelDto().getEinheitCNrBestellung().trim() + " (";

			if (internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor() != null) {

				boolean bInvers = Helper
						.short2boolean(internalFrameArtikel.getArtikelDto().getbBestellmengeneinheitInvers());

				if (bInvers) {
					s = Helper.formatZahl(internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor(), 2,
							LPMain.getTheClient().getLocUi()) + " "
							+ internalFrameArtikel.getArtikelDto().getEinheitCNr().trim() + "/ "
							+ internalFrameArtikel.getArtikelDto().getEinheitCNrBestellung().trim();
				} else {
					s += Helper.formatZahl(internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor(), 2,
							LPMain.getTheClient().getLocUi()) + " )";
				}

			}

			wlaEinheitBestellmenge.setText(s);

			wlaEinheitBestellmenge.setVisible(true);
			wlaEinheitArtikelmenge.setVisible(true);
			wnfEinzelpreisBestellmenge.setVisible(true);
			wnfNettopreisBestellmenge.setVisible(true);
			wcoEinheitVpe.setVisible(false);
			wcoEinheitVpe.setKeyOfSelectedItem(null);

		} else {

			wcoEinheitVpe.setVisible(true);

			if (wcoEinheitVpe.getKeyOfSelectedItem() == null) {
				wlaEinheitBestellmenge.setVisible(false);
				wlaEinheitArtikelmenge.setVisible(false);
				wnfEinzelpreisBestellmenge.setVisible(false);
				wnfNettopreisBestellmenge.setVisible(false);
			} else {
				String s = ((String) wcoEinheitVpe.getKeyOfSelectedItem()).trim() + " (";

				if (wnfVerpackungseinheit.getBigDecimal() != null) {
					s += Helper.formatZahl(wnfVerpackungseinheit.getBigDecimal(), 2, LPMain.getTheClient().getLocUi())
							+ " ";
				}

				wlaEinheitVerpackungseinheit.setText(internalFrameArtikel.getArtikelDto().getEinheitCNr().trim() + "/");

				s += internalFrameArtikel.getArtikelDto().getEinheitCNr().trim() + ")";
				wlaEinheitBestellmenge.setText(s);

				wlaEinheitBestellmenge.setVisible(true);
				wlaEinheitArtikelmenge.setVisible(true);
				wnfEinzelpreisBestellmenge.setVisible(true);
				wnfNettopreisBestellmenge.setVisible(true);
				wnfVerpackungseinheit.setMandatoryField(true);
			}

		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		list.removeAll();
		list.setListData(new Object[0]);

		wlaMaterialzuschlag.setForeground(Color.BLACK);

		wlaEinheitMindestbestellmenge.setText(internalFrameArtikel.getArtikelDto().getEinheitCNr().trim());
		wlaEinheitStandardmenge.setText(internalFrameArtikel.getArtikelDto().getEinheitCNr().trim());
		wlaEinheitVerpackungseinheit.setText(internalFrameArtikel.getArtikelDto().getEinheitCNr().trim());
		wlaEinheitArtikelmenge.setText(internalFrameArtikel.getArtikelDto().getEinheitCNr().trim());

		if (Helper.short2boolean(internalFrameArtikel.getArtikelDto().getBChargennrtragend())) {
			wnfGebindemenge.setVisible(true);
			wlaGebindemenge.setVisible(true);
			wbuGebinde.setVisible(true);
			wtfGebinde.setVisible(true);
		} else {
			wnfGebindemenge.setVisible(false);
			wlaGebindemenge.setVisible(false);
			wbuGebinde.setVisible(false);
			wtfGebinde.setVisible(false);
		}

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			wlaWaehrungEinzelpreis.setText("");
			wlaWaehrungNettopreis.setText("");
			wlaEinheitFixkosten.setText("");
			wlaEinheitBestellmenge.setText("");
			wdfgueltigab.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
			wlaWiederbeschaffungszeitEinheit.setText(eineheitWiederbeschaffung);

			eventActionSpecial(new ActionEvent(wrbNettopreis, 0, ""));

			setBestellpreisVisible();

			if (internalFrameArtikel.getArtikelDto().getMaterialIId() != null) {
				if (DelegateFactory.getInstance().getMaterialDelegate().materialzuschlagFindAktuellenzuschlag(
						internalFrameArtikel.getArtikelDto().getMaterialIId()) == null) {
					wlaArtikelMaterial.setVisible(false);
					wlaMaterialzuschlag.setVisible(false);
					wnfEKPReis.setVisible(false);
					wnfZuschlag.setVisible(false);
				} else {
					wlaArtikelMaterial.setVisible(true);
					wlaMaterialzuschlag.setVisible(true);
					wnfEKPReis.setVisible(true);
					wnfZuschlag.setVisible(true);
				}
			} else {
				wlaArtikelMaterial.setVisible(false);
				wlaMaterialzuschlag.setVisible(false);
				wnfEKPReis.setVisible(false);
				wnfZuschlag.setVisible(false);
			}
			clearStatusbar();
		} else {
			artikellieferantDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikellieferantFindByPrimaryKey((Integer) key);
			webabfrageButton.setArtikellieferantDto(artikellieferantDto);

			if (internalFrameArtikel.getArtikelDto().getMaterialIId() != null) {

				if (DelegateFactory.getInstance().getMaterialDelegate().materialzuschlagFindAktuellenzuschlag(
						internalFrameArtikel.getArtikelDto().getMaterialIId()) == null) {
					wlaArtikelMaterial.setVisible(false);
					wlaMaterialzuschlag.setVisible(false);
					wnfEKPReis.setVisible(false);
					wnfZuschlag.setVisible(false);
				} else {

					wlaArtikelMaterial.setVisible(true);
					wlaMaterialzuschlag.setVisible(true);
					wnfEKPReis.setVisible(true);
					wnfZuschlag.setVisible(true);

					MaterialDto mDto = DelegateFactory.getInstance().getMaterialDelegate()
							.materialFindByPrimaryKey(internalFrameArtikel.getArtikelDto().getMaterialIId());
					wlaArtikelMaterial.setText(mDto.getBezeichnung());

					BigDecimal zuschlag = DelegateFactory.getInstance().getMaterialDelegate()
							.getKupferzuschlagInLieferantenwaehrung(internalFrameArtikel.getArtikelDto().getIId(),
									artikellieferantDto.getLieferantIId());

					if (zuschlag == null) {
						wlaMaterialzuschlag.setForeground(Color.RED);
					}

					wnfZuschlag.setBigDecimal(zuschlag);
				}

			} else {
				wlaArtikelMaterial.setVisible(false);
				wlaMaterialzuschlag.setVisible(false);
				wnfEKPReis.setVisible(false);
				wnfZuschlag.setVisible(false);
			}

			dto2Components();

			setBestellpreisVisible();

			if (wnfEinzelpreis.getBigDecimal() != null && wnfNettopreis.getBigDecimal() != null) {

				berechneNettopreisMitZuschlag();

				BigDecimal faktor = internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor();

				boolean bInvers = Helper
						.short2boolean(internalFrameArtikel.getArtikelDto().getbBestellmengeneinheitInvers());
				if (faktor == null && wnfVerpackungseinheit.getBigDecimal() != null
						&& wcoEinheitVpe.getKeyOfSelectedItem() != null) {
					faktor = wnfVerpackungseinheit.getBigDecimal();
					bInvers = true;
				}

				if (faktor != null) {
					if (faktor.doubleValue() != 0) {
						if (bInvers) {
							wnfEinzelpreisBestellmenge.setBigDecimal(wnfEinzelpreis.getBigDecimal().multiply(faktor));
							setTooltipMandantenwaehrung(wnfEinzelpreisBestellmenge,
									artikellieferantDto.getLieferantDto().getWaehrungCNr());

							wnfNettopreisBestellmenge.setBigDecimal(wnfNettopreis.getBigDecimal().multiply(faktor));

							setTooltipMandantenwaehrung(wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto().getWaehrungCNr());
						} else {
							wnfEinzelpreisBestellmenge.setBigDecimal(wnfEinzelpreis.getBigDecimal().divide(faktor,
									Defaults.getInstance().getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN));
							setTooltipMandantenwaehrung(wnfEinzelpreisBestellmenge,
									artikellieferantDto.getLieferantDto().getWaehrungCNr());

							wnfNettopreisBestellmenge.setBigDecimal(wnfNettopreis.getBigDecimal().divide(faktor,
									Defaults.getInstance().getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN));

							setTooltipMandantenwaehrung(wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto().getWaehrungCNr());
						}
					} else {
						wnfNettopreisBestellmenge.setBigDecimal(new BigDecimal(0));
						wnfNettopreisBestellmenge.setToolTipText(null);
						wnfEinzelpreisBestellmenge.setBigDecimal(new BigDecimal(0));
						wnfEinzelpreisBestellmenge.setToolTipText(null);
					}
				}
			}
			pruefeMinVK();
		}

		wbuFixkostenEdit.setEnabled(true);
		wbuNichtLieferbarEdit.setEnabled(true);

		panelEditorPlainTextFormel = null;
		panelEditorPlainTextNichtLieferbar = null;
		setEditorButtonColor();
		setWebabfrageComponentsVisible();

		if (getHmOfButtons().containsKey(ACTION_SPECIAL_ZEILE_KOPIEREN) && artikellieferantDto != null
				&& artikellieferantDto.getLieferantDto() != null) {
			if (artikellieferantDto.getLieferantDto().getMandantCNr().equals(LPMain.getTheClient().getMandant())) {
				getHmOfButtons().get(ACTION_SPECIAL_ZEILE_KOPIEREN).setEnabled(false);
			}
		}

	}

	void wnfRabatt_focusLost(FocusEvent e) {
		if (wrbRabatt.isSelected()) {
			try {
				// z.b: Einzelpreis = 88, Rabatt = 22
				// Nettopreis: 68,64 = 88 - ( ( 88 / 100 ) * 22)
				if (wnfEinzelpreis.getBigDecimal() != null && wnfRabatt.getBigDecimal() != null) {
					BigDecimal einzelpreis = wnfEinzelpreis.getBigDecimal();
					BigDecimal rabattpreis = einzelpreis.multiply(wnfRabatt.getBigDecimal()).divide(new BigDecimal(100),
							BigDecimal.ROUND_HALF_EVEN);
					wnfNettopreis.setBigDecimal(einzelpreis.subtract(rabattpreis));

					if (wnfEinzelpreisBestellmenge.getBigDecimal() != null) {
						BigDecimal einzelpreisBestellmenge = wnfEinzelpreisBestellmenge.getBigDecimal();
						BigDecimal rabattpreistellmenge = einzelpreisBestellmenge.multiply(wnfRabatt.getBigDecimal())
								.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_EVEN);

						wnfNettopreisBestellmenge.setBigDecimal(einzelpreisBestellmenge.subtract(rabattpreistellmenge));
						if (artikellieferantDto.getLieferantDto() != null) {
							setTooltipMandantenwaehrung(wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto().getWaehrungCNr());
						}

					}

				} else {
					wnfNettopreis.setDouble(new Double(0));
				}
				pruefeMinVK();
				berechneNettopreisMitZuschlag();
			} catch (Throwable ex) {
				// nothing here
			}
		}
	}

	void wnfNettopreis_focusLost(FocusEvent e) {
		if (wrbNettopreis.isSelected()) {
			try {
				// z.b: Rabatt = 1 - ( Nettopreis / Einzelpreis )
				if (wnfEinzelpreis.getBigDecimal() != null && wnfNettopreis.getBigDecimal() != null
						&& wnfEinzelpreis.getBigDecimal().doubleValue() != 0) {
					BigDecimal rabattsatz = new BigDecimal(1).subtract(wnfNettopreis.getBigDecimal()
							.divide(wnfEinzelpreis.getBigDecimal(), 4, BigDecimal.ROUND_HALF_EVEN));
					wnfRabatt.setBigDecimal(rabattsatz.multiply(new BigDecimal(100)));

					BigDecimal faktor = internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor();

					if (faktor == null && wnfVerpackungseinheit.getBigDecimal() != null) {
						faktor = wnfVerpackungseinheit.getBigDecimal();
					}

					if (faktor != null && wnfEinzelpreisBestellmenge.getBigDecimal() != null
							&& wnfEinzelpreisBestellmenge.getBigDecimal().doubleValue() != 0) {

						wnfNettopreisBestellmenge.setBigDecimal(wnfEinzelpreisBestellmenge.getBigDecimal()
								.subtract(wnfEinzelpreisBestellmenge.getBigDecimal().multiply(rabattsatz)));
						if (artikellieferantDto.getLieferantDto() != null) {
							setTooltipMandantenwaehrung(wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto().getWaehrungCNr());
						}

					}

				} else {
					wnfRabatt.setDouble(new Double(0));

				}

				pruefeMinVK();
				berechneNettopreisMitZuschlag();
			} catch (Throwable ex) {
				// nothing here
			}
		}

	}

	private void setTooltipMandantenwaehrung(WrapperNumberField wnf, String lieferantenwaehrung) throws Throwable {
		if (wnf.getBigDecimal() != null) {
			BigDecimal umgerechnet = DelegateFactory.getInstance().getLocaleDelegate().rechneUmInAndereWaehrung(
					wnf.getBigDecimal(), lieferantenwaehrung, LPMain.getTheClient().getSMandantenwaehrung());
			wnf.setToolTipText(Helper.formatZahl(umgerechnet, wnf.getFractionDigits(), LPMain.getTheClient().getLocUi())
					+ " " + LPMain.getTheClient().getSMandantenwaehrung());
		} else {
			wnf.setToolTipText(null);
		}
	}

	public void wnfEinzelpreis_focusLost(FocusEvent e) {
		try {
			if (wnfEinzelpreis.getDouble() == null) {
				wnfRabatt.setDouble(null);
				wnfNettopreisBestellmenge.setDouble(null);
				wnfEinzelpreisBestellmenge.setDouble(null);
				wnfNettopreis.setDouble(null);
				wnfRabatt.setMandatoryField(false);
				wnfNettopreis.setMandatoryField(false);

			} else {

				wnfRabatt.setMandatoryField(true);
				wnfNettopreis.setMandatoryField(true);

				if (wnfRabatt.getBigDecimal() == null) {
					wnfRabatt.setBigDecimal(new java.math.BigDecimal(0));
					wnfNettopreis.setBigDecimal(wnfEinzelpreis.getBigDecimal());
					setTooltipMandantenwaehrung(wnfNettopreis, artikellieferantDto.getLieferantDto().getWaehrungCNr());
				}

				if (wnfEinzelpreis.getBigDecimal().doubleValue() != 0) {
					if (wrbNettopreis.isSelected()) {

						if (wnfNettopreis.getBigDecimal() != null) {
							// Rabatt neu berechnen
							BigDecimal rabattsatz = new BigDecimal(1).subtract(wnfNettopreis.getBigDecimal()
									.divide(wnfEinzelpreis.getBigDecimal(), 4, BigDecimal.ROUND_HALF_EVEN));
							wnfRabatt.setBigDecimal(rabattsatz.multiply(new BigDecimal(100)));
						}

					} else {
						if (wnfRabatt.getBigDecimal() != null) {
							// Nettopreis neu berechnen
							BigDecimal einzelpreis = wnfEinzelpreis.getBigDecimal();
							BigDecimal rabattpreis = einzelpreis.multiply(wnfRabatt.getBigDecimal())
									.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);
							wnfNettopreis.setBigDecimal(einzelpreis.subtract(rabattpreis));

							if (artikellieferantDto.getLieferantDto() != null) {
								setTooltipMandantenwaehrung(wnfNettopreis,
										artikellieferantDto.getLieferantDto().getWaehrungCNr());
							}
						}
					}

				} else {
					wnfRabatt.setBigDecimal(new java.math.BigDecimal(0));

					wnfNettopreis.setBigDecimal(wnfEinzelpreis.getBigDecimal());
					setTooltipMandantenwaehrung(wnfNettopreis, artikellieferantDto.getLieferantDto().getWaehrungCNr());
				}

				BigDecimal faktor = internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor();

				boolean bInvers = Helper
						.short2boolean(internalFrameArtikel.getArtikelDto().getbBestellmengeneinheitInvers());
				if (faktor == null && wnfVerpackungseinheit.getBigDecimal() != null
						&& wcoEinheitVpe.getKeyOfSelectedItem() != null) {
					faktor = wnfVerpackungseinheit.getBigDecimal();
					bInvers = true;
				}

				if (faktor != null) {
					if (faktor.doubleValue() != 0) {

						if (bInvers) {
							wnfEinzelpreisBestellmenge.setBigDecimal(wnfEinzelpreis.getBigDecimal().multiply(faktor));
							setTooltipMandantenwaehrung(wnfEinzelpreisBestellmenge,
									artikellieferantDto.getLieferantDto().getWaehrungCNr());

							wnfNettopreisBestellmenge.setBigDecimal(wnfNettopreis.getBigDecimal().multiply(faktor));
							setTooltipMandantenwaehrung(wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto().getWaehrungCNr());

						} else {

							wnfEinzelpreisBestellmenge.setBigDecimal(wnfEinzelpreis.getBigDecimal().divide(faktor,
									Defaults.getInstance().getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN));
							setTooltipMandantenwaehrung(wnfEinzelpreisBestellmenge,
									artikellieferantDto.getLieferantDto().getWaehrungCNr());

							wnfNettopreisBestellmenge.setBigDecimal(wnfNettopreis.getBigDecimal().divide(faktor,
									Defaults.getInstance().getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN));
							setTooltipMandantenwaehrung(wnfNettopreisBestellmenge,
									artikellieferantDto.getLieferantDto().getWaehrungCNr());
						}
					} else {
						wnfEinzelpreisBestellmenge.setBigDecimal(new java.math.BigDecimal(0));
						wnfEinzelpreisBestellmenge.setToolTipText(null);
						wnfNettopreisBestellmenge.setBigDecimal(new java.math.BigDecimal(0));
						wnfNettopreisBestellmenge.setToolTipText(null);
					}
				}
				pruefeMinVK();
			}
			berechneNettopreisMitZuschlag();
		} catch (Throwable ex) {
			// nothing here
			ex.printStackTrace();
		}
	}

	private void berechneNettopreisMitZuschlag() throws ExceptionLP {
		if (wnfZuschlag.getBigDecimal() != null) {
			wnfEKPReis.setBigDecimal(wnfNettopreis.getBigDecimal().add(wnfZuschlag.getBigDecimal()));
		} else {
			wnfEKPReis.setBigDecimal(null);
		}
	}

	private void pruefeMinVK() throws Throwable {
		wnfNettopreis.setForeground(Color.BLACK);
		wnfNettopreis.setToolTipText(null);
		if (wnfNettopreis.getBigDecimal() != null) {

			BigDecimal gestpreis = DelegateFactory.getInstance().getLagerDelegate()
					.getGemittelterGestehungspreisDesHauptlagers(internalFrameArtikel.getArtikelDto().getIId());

			if (gestpreis != null) {

				BigDecimal mindestpreis = new BigDecimal(gestpreis.doubleValue() * (1
						+ ((internalFrameArtikel.getArtikelDto().getFMindestdeckungsbeitrag().doubleValue() / 100))));

				if (mindestpreis.doubleValue() < wnfNettopreis.getBigDecimal().doubleValue()) {
					wnfNettopreis.setForeground(Color.RED);
					wnfNettopreis.setToolTipText(LPMain.getTextRespectUISPr("artikel.ekpreisueberminvk"));
				}
			}

		}
	}

	public void wnfEinzelpreisBestelleinheit_focusLost(FocusEvent e) {
		try {
			BigDecimal faktor = internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor();

			boolean bInvers = Helper
					.short2boolean(internalFrameArtikel.getArtikelDto().getbBestellmengeneinheitInvers());

			if (faktor == null && wnfVerpackungseinheit.getBigDecimal() != null
					&& wcoEinheitVpe.getKeyOfSelectedItem() != null) {
				faktor = wnfVerpackungseinheit.getBigDecimal();
				bInvers = true;
			}

			if (faktor != null && wnfEinzelpreisBestellmenge.getBigDecimal() != null) {
				if (bInvers) {
					wnfEinzelpreis.setBigDecimal(wnfEinzelpreisBestellmenge.getBigDecimal().divide(faktor,
							Defaults.getInstance().getIUINachkommastellenPreiseEK(), BigDecimal.ROUND_HALF_EVEN));

				} else {
					wnfEinzelpreis.setBigDecimal(wnfEinzelpreisBestellmenge.getBigDecimal().multiply(faktor));

				}

				setTooltipMandantenwaehrung(wnfEinzelpreis, artikellieferantDto.getLieferantDto().getWaehrungCNr());

				wnfEinzelpreis_focusLost(null);

			}
			berechneNettopreisMitZuschlag();
		} catch (Throwable ex) {
			// nix
		}
	}

	public void wnfNettopreisBestelleinheit_focusLost(FocusEvent e) {
		try {
			BigDecimal faktor = internalFrameArtikel.getArtikelDto().getNUmrechnungsfaktor();

			if (faktor == null && wnfVerpackungseinheit.getBigDecimal() != null) {
				faktor = wnfVerpackungseinheit.getBigDecimal();
			}

			if (faktor != null && wnfEinzelpreisBestellmenge.getBigDecimal() != null
					&& wnfNettopreisBestellmenge.getBigDecimal() != null
					&& wnfEinzelpreisBestellmenge.getBigDecimal().doubleValue() != 0) {

				BigDecimal rabattsatz = new BigDecimal(1).subtract(wnfNettopreisBestellmenge.getBigDecimal()
						.divide(wnfEinzelpreisBestellmenge.getBigDecimal(), 4, BigDecimal.ROUND_HALF_EVEN));
				wnfRabatt.setBigDecimal(rabattsatz.multiply(new BigDecimal(100)));

				if (wnfEinzelpreis.getBigDecimal() != null && wnfNettopreis.getBigDecimal() != null
						&& wnfEinzelpreis.getBigDecimal().doubleValue() != 0) {

					wnfNettopreis.setBigDecimal(wnfEinzelpreis.getBigDecimal()
							.subtract(wnfEinzelpreis.getBigDecimal().multiply(rabattsatz)));

					setTooltipMandantenwaehrung(wnfNettopreis, artikellieferantDto.getLieferantDto().getWaehrungCNr());

				}

				pruefeMinVK();
				berechneNettopreisMitZuschlag();
			}
		} catch (Throwable ex) {
			// nix
		}
	}

	public void wcoEinheitVpe_actionPerformed(ActionEvent e) {

		try {
			setBestellpreisVisible();
		} catch (Throwable e1) {
			getInternalFrame().handleException(e1, true);
		}
	}

	private boolean isWeblieferant() {
		if (!LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_WEB_BAUTEIL_ANFRAGE)
				|| artikellieferantDto == null) {
			return false;
		}

		try {
			return DelegateFactory.getInstance().getLieferantDelegate()
					.isWeblieferant(artikellieferantDto.getLieferantIId());
		} catch (Throwable e) {
		}
		return false;
	}

	private void setWebabfrageComponentsVisible() {
		boolean isWeblieferant = isWeblieferant();
		wlaWebabfrageBestand.setVisible(isWeblieferant);
		wlaEinheitWebabfrageBestand.setVisible(isWeblieferant);
		wlaLetzteWebabfrage.setVisible(isWeblieferant);
		wdfLetzteWebabfrage.setVisible(isWeblieferant);
		wnfWebabfrageBestand.setVisible(isWeblieferant);
		webabfrageButton.setVisible(isWeblieferant);
	}

	private void installWebabfrageButton() {
		webabfrageButton = new WrapperWebBauteilAbfrageButton(artikellieferantDto,
				new FarnellWebAbfrageCtrl(new IWebabfrageArtikellieferantCallback() {
					@Override
					public void doneExc(Throwable e) {
						myLogger.error("Fehler waehrend Webabfrage", e);
						handleException(e, true);
					}

					@Override
					public void done(WebabfrageArtikellieferantResult result) {
						try {
							Integer artikellieferantIId = result != null && result.getArtikellieferantDto() != null
									? result.getArtikellieferantDto().getIId()
									: null;
							internalFrameArtikel.getTabbedPaneArtikel().getPanelQueryArtikellieferant()
									.refreshMe(artikellieferantIId);
							setKeyWhenDetailPanel(artikellieferantIId);
							eventYouAreSelected(false);
						} catch (Throwable e) {
							myLogger.error("Fehler waehrend Webabfrage", e);
							handleException(e, true);
						}
					}
				}));
		addButtonToToolpanel(webabfrageButton);
		getToolBar().getHmOfButtons().put(WrapperWebBauteilAbfrageButton.ActionCommand,
				new LPButtonAction(webabfrageButton, RechteFac.RECHT_LP_DARF_PREISE_AENDERN_EINKAUF));
	}
}

class PanelArtikellieferant_wnfEinzelpreis_focusAdapter extends FocusAdapter {
	private PanelArtikellieferant adaptee;

	PanelArtikellieferant_wnfEinzelpreis_focusAdapter(PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfEinzelpreis_focusLost(e);
	}
}

class PanelArtikellieferant_wcoEinheitVpe_actionAdapter implements ActionListener {
	private PanelArtikellieferant adaptee;

	PanelArtikellieferant_wcoEinheitVpe_actionAdapter(PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoEinheitVpe_actionPerformed(e);
	}
}

class PanelArtikellieferant_wnfRabatt_focusAdapter extends java.awt.event.FocusAdapter {
	PanelArtikellieferant adaptee;

	PanelArtikellieferant_wnfRabatt_focusAdapter(PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfRabatt_focusLost(e);
	}
}

class PanelArtikellieferant_wnfNettopreis_focusAdapter extends java.awt.event.FocusAdapter {
	PanelArtikellieferant adaptee;

	PanelArtikellieferant_wnfNettopreis_focusAdapter(PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfNettopreis_focusLost(e);
	}
}

class PanelArtikellieferant_wnfEinzelpreisBestelleinheit_focusAdapter extends java.awt.event.FocusAdapter {
	PanelArtikellieferant adaptee;

	PanelArtikellieferant_wnfEinzelpreisBestelleinheit_focusAdapter(PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfEinzelpreisBestelleinheit_focusLost(e);
	}
}

class PanelArtikellieferant_wnfNettopreisBestelleinheit_focusAdapter extends java.awt.event.FocusAdapter {
	PanelArtikellieferant adaptee;

	PanelArtikellieferant_wnfNettopreisBestelleinheit_focusAdapter(PanelArtikellieferant adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfNettopreisBestelleinheit_focusLost(e);
	}
}
