package com.lp.client.rechnung;

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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JLabel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditor;
import com.lp.editor.util.TextBlockOverflowException;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.bestellung.service.BestellvorschlagUeberleitungKriterienDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.VerrechnungsmodellDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>
 * <I>Dialog zur Eingabe der Kriterien fuer die Ueberleitung des
 * Bestellvorschlags in Belege.</I>
 * </p>
 * <p>
 * Dieser Dialog wird aus den folgenden Modulen aufgerufen:
 * </p>
 * <ul>
 * <li>Bestellung/Bestellvorschlag
 * <li>Anfrage/Anfragevorschlag
 * </ul>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>22.11.05</I>
 * </p>
 * <p>
 * </p>
 * 
 * @version $Revision: 1.6 $
 */
public class PanelDialogAbrechnungsvorschlagUeberleitenReise extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WrapperLabel wlaBetragAusPositionen = new WrapperLabel();
	private WrapperNumberField wnfBetragAusPositionen = new WrapperNumberField();

	private LpEditor wefKommentarBetrag = new LpEditor(null);

	private LpEditor wefKommentarKilometer = new LpEditor(null);
	
	private LpEditor wefKommentarSpesen = new LpEditor(null);

	private JLabel wlaArtikelBetrag = new JLabel();
	private JLabel wlaArtikelVKPReisKilometer = new JLabel();

	private WrapperLabel wlaBetragZuVerrechnen = new WrapperLabel();
	private WrapperNumberField wnfBetragZuVerrechnen = new WrapperNumberField();

	private WrapperLabel wlaKilometerAusPositionen = new WrapperLabel();
	private WrapperNumberField wnfKilometerAusPositionen = new WrapperNumberField();
	private WrapperLabel wlaKilometerZuVerrechnen = new WrapperLabel();
	private WrapperNumberField wnfKilometerZuVerrechnen = new WrapperNumberField();
	
	private JLabel wlaArtikelSpesen = new JLabel();
	private WrapperLabel wlaSpesenAusPositionen = new WrapperLabel();
	private WrapperNumberField wnfSpesenAusPositionen = new WrapperNumberField();
	private WrapperLabel wlaSpesenZuVerrechnen = new WrapperLabel();
	private WrapperNumberField wnfSpesenZuVerrechnen = new WrapperNumberField();

	private WrapperCheckBox wcbErledigt = new WrapperCheckBox();

	private BigDecimal bdBetragInRechnungswahrung;
	private BigDecimal bdKilometer;
	private BigDecimal bdSpesenInRechnungswaehrung;
	
	private String rechnungswaehrung;

	private VerrechnungsmodellDto vmDto;
	private KundeDto kundeDto = null;

	private BestellvorschlagUeberleitungKriterienDto kritDto = null;

	PersonalDto personalDto = null;

	public PanelDialogAbrechnungsvorschlagUeberleitenReise(Integer kundeIId, VerrechnungsmodellDto vmDto,
			BigDecimal bdBetragInRechnungswahrung, BigDecimal bdKilometer, BigDecimal bdSpesenInRechnungswaehrung, String rechnungswaehrung, InternalFrame oInternalFrameI, String title)
			throws Throwable {
		super(oInternalFrameI, title);
		this.bdBetragInRechnungswahrung = bdBetragInRechnungswahrung;
		this.bdSpesenInRechnungswaehrung = bdSpesenInRechnungswaehrung;
		this.rechnungswaehrung=rechnungswaehrung;
		this.bdKilometer = bdKilometer;
		this.vmDto = vmDto;
		this.kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(kundeIId);
		jbInit();
		setDefaults();
		initComponents();

		wefKommentarBetrag.requestFocus();
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);
		personalDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());

		wlaBetragAusPositionen.setText(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.abrechnen.betragauspositionen"));

		wlaBetragZuVerrechnen.setText(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.abrechnen.betragverrechnen"));

		wlaKilometerAusPositionen.setText(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.abrechnen.kilometerauspositionen"));
		wlaKilometerZuVerrechnen.setText(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.abrechnen.kilometerverrechnen"));

		wlaSpesenAusPositionen.setText(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.abrechnen.spesenauspositionen"));
		wlaSpesenZuVerrechnen.setText(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.abrechnen.spesenverrechnen"));
		
	
		
		wcbErledigt.setText(LPMain.getInstance().getTextRespectUISPr("label.erledigt"));

		wnfBetragAusPositionen.setEditable(false);

		wnfKilometerAusPositionen.setEditable(false);
		
		wnfSpesenAusPositionen.setEditable(false);

		ArtikelDto aBetragDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(vmDto.getArtikelIIdReisespesen());
		wlaArtikelBetrag.setText(
				LPMain.getInstance().getMessageTextRespectUISPr("rech.abrechnungsvorschlag.reise.artikelbetrag",
						aBetragDto.formatArtikelbezeichnung(), vmDto.getCBez()));

		
		ArtikelDto aSpesenDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(vmDto.getArtikelIIdEr());
		wlaArtikelSpesen.setText(
				LPMain.getInstance().getMessageTextRespectUISPr("rech.abrechnungsvorschlag.reise.artikelspesen",
						aSpesenDto.formatArtikelbezeichnung(), vmDto.getCBez()));
		
		
		
		
		ArtikelDto aKilometerDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(vmDto.getArtikelIIdReisekilometer());

		VerkaufspreisDto vkpfDto = DelegateFactory.getInstance().getVkPreisfindungDelegate()
				.getVKPreisEinfachInZielwaehrungZuHeute(kundeDto.getIId(), aKilometerDto.getIId(), BigDecimal.ONE, rechnungswaehrung);

		if (vkpfDto != null && vkpfDto.getNettpreisOhneMaterialzuschlag() != null) {
			wlaArtikelVKPReisKilometer.setText(
					LPMain.getInstance().getMessageTextRespectUISPr("rech.abrechnungsvorschlag.reise.artikelkilometer",
							aKilometerDto.formatArtikelbezeichnung(), vmDto.getCBez(),
							Helper.formatZahl(vkpfDto.getNettpreisOhneMaterialzuschlag(),
									Defaults.getInstance().getIUINachkommastellenPreiseVK(),
									LPMain.getInstance().getUISprLocale())));

		} else {
			wlaArtikelVKPReisKilometer.setText(
					LPMain.getInstance().getMessageTextRespectUISPr("rech.abrechnungsvorschlag.reise.artikelkilometer",
							aKilometerDto.formatArtikelbezeichnung(), vmDto.getCBez(), "KEIN"));

		}
		wnfBetragZuVerrechnen.addKeyListener(this);
		wnfKilometerZuVerrechnen.addKeyListener(this);
		wnfSpesenZuVerrechnen.addKeyListener(this);

		
		
		wnfBetragZuVerrechnen.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		
		wnfSpesenZuVerrechnen.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());

		iZeile++;
		jpaWorkingOn.add(
				new JLabel(kundeDto.getPartnerDto().formatAnrede() + " - " + kundeDto.getPartnerDto().formatAdresse()),
				new GridBagConstraints(0, iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 10, 2), 0, 0));

		wnfBetragAusPositionen.setBigDecimal(bdBetragInRechnungswahrung);
		wnfBetragZuVerrechnen.setBigDecimal(bdBetragInRechnungswahrung);
		wnfBetragZuVerrechnen.setMandatoryField(true);

		wnfKilometerAusPositionen.setBigDecimal(bdKilometer);
		wnfKilometerZuVerrechnen.setBigDecimal(bdKilometer);

		wnfKilometerZuVerrechnen.setMandatoryField(true);
		wefKommentarBetrag.setInternalFrame(getInternalFrame());
		wefKommentarKilometer.setInternalFrame(getInternalFrame());
		wefKommentarSpesen.setInternalFrame(getInternalFrame());
		
		
		
		wnfSpesenAusPositionen.setBigDecimal(bdSpesenInRechnungswaehrung);
		wnfSpesenZuVerrechnen.setBigDecimal(bdSpesenInRechnungswaehrung);
		wnfSpesenZuVerrechnen.setMandatoryField(true);
		
		
		
		iZeile++;
		jpaWorkingOn.add(wlaBetragAusPositionen, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragAusPositionen, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new JLabel(rechnungswaehrung), new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBetragZuVerrechnen, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragZuVerrechnen, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new JLabel(rechnungswaehrung), new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaArtikelBetrag, new GridBagConstraints(0, iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wefKommentarBetrag, new GridBagConstraints(0, iZeile, 5, 1, 0.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaKilometerAusPositionen, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKilometerAusPositionen, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaKilometerZuVerrechnen, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKilometerZuVerrechnen, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaArtikelVKPReisKilometer, new GridBagConstraints(0, iZeile, 3, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wefKommentarKilometer, new GridBagConstraints(0, iZeile, 5, 1, 0.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaSpesenAusPositionen, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSpesenAusPositionen, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaSpesenZuVerrechnen, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSpesenZuVerrechnen, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaArtikelSpesen, new GridBagConstraints(0, iZeile, 3, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wefKommentarSpesen, new GridBagConstraints(0, iZeile, 5, 1, 0.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		wcbErledigt.setSelected(true);

		jpaWorkingOn.add(wcbErledigt, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	private void setDefaults() throws Throwable {

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			if (allMandatoryFieldsSetDlg()) {
				buildBenutzerKriterien();

				super.eventActionSpecial(e);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			super.eventActionSpecial(e);
		}
	}

	protected void eventKeyTyped(KeyEvent e) throws Throwable {

	}

	protected void eventKeyPressed(KeyEvent e) throws Throwable {

	}

	protected void eventKeyReleased(KeyEvent e) throws Throwable {
		// berechneGesamt
		wcbErledigt.setSelected(false);
		boolean bBetragErledigt = false;
		if (wnfBetragZuVerrechnen.getBigDecimal() != null && wnfBetragAusPositionen.getBigDecimal() != null) {
			if (wnfBetragZuVerrechnen.getBigDecimal().doubleValue() >= wnfBetragAusPositionen.getBigDecimal()
					.doubleValue()) {
				bBetragErledigt = true;
			}
		}

		boolean bKilometerErledigt = false;
		if (wnfKilometerZuVerrechnen.getBigDecimal() != null && wnfKilometerAusPositionen.getBigDecimal() != null) {
			if (wnfKilometerZuVerrechnen.getBigDecimal().doubleValue() >= wnfKilometerAusPositionen.getBigDecimal()
					.doubleValue()) {
				bKilometerErledigt = true;
			}
		}
		
		boolean bSpesenErledigt = false;
		if (wnfSpesenZuVerrechnen.getBigDecimal() != null && wnfSpesenAusPositionen.getBigDecimal() != null) {
			if (wnfSpesenZuVerrechnen.getBigDecimal().doubleValue() >= wnfSpesenAusPositionen.getBigDecimal()
					.doubleValue()) {
				bKilometerErledigt = true;
			}
		}

		if (bBetragErledigt == true && bKilometerErledigt == true && bSpesenErledigt == true) {
			wcbErledigt.setSelected(true);
		}

	}


	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

	public BigDecimal getSpesenZuVerrechnen() throws ExceptionLP {
		return wnfSpesenZuVerrechnen.getBigDecimal();
	}
	
	public BigDecimal getBetragZuVerrechnen() throws ExceptionLP {
		return wnfBetragZuVerrechnen.getBigDecimal();
	}
	
	public BigDecimal getKilometerZuVerrechnen() throws ExceptionLP {
		return wnfKilometerZuVerrechnen.getBigDecimal();
	}

	public Integer getArtikelIIdReisespesen() {
		return vmDto.getArtikelIIdReisespesen();
	}
	
	public Integer getArtikelIIdKilometer() {
		return vmDto.getArtikelIIdReisekilometer();
	}
	
	public Integer getArtikelIIdSpesen() {
		return vmDto.getArtikelIIdEr();
	}

	public String getKommentar() throws ExceptionLP, TextBlockOverflowException {
		return wefKommentarBetrag.getText();
	}
	
	public String getKommentarKilometer() throws ExceptionLP, TextBlockOverflowException {
		return wefKommentarKilometer.getText();
	}
	
	public String getKommentarSpesen() throws ExceptionLP, TextBlockOverflowException {
		return wefKommentarSpesen.getText();
	}

	public boolean isErledigt() throws ExceptionLP, TextBlockOverflowException {
		return wcbErledigt.isSelected();
	}

	private BestellvorschlagUeberleitungKriterienDto buildBenutzerKriterien() {
		kritDto = new BestellvorschlagUeberleitungKriterienDto();

		return kritDto;
	}

	public BestellvorschlagUeberleitungKriterienDto getBestellvorschlagUeberleitungKriterienDto() {
		return kritDto;
	}
}
