
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
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditor;
import com.lp.editor.util.TextBlockOverflowException;
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
public class PanelDialogAbrechnungsvorschlagUeberleitenER extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WrapperLabel wlaBetragAusPositionen = new WrapperLabel();
	private WrapperNumberField wnfBetragAusPositionen = new WrapperNumberField();

	private WrapperLabel wlaKommentar = new WrapperLabel();
	private LpEditor wefKommentar = new LpEditor(null);

	private WrapperLabel wlaVKPreis = new WrapperLabel();
	private WrapperNumberField wnfVKPreis = new WrapperNumberField();

	private WrapperLabel wlaBetragZuVerrechnen = new WrapperLabel();
	private WrapperNumberField wnfBetragZuVerrechnen = new WrapperNumberField();

	private WrapperIdentField wifArtikel = new WrapperIdentField(getInternalFrame(), null);

	private WrapperCheckBox wcbErledigt = new WrapperCheckBox();

	private BigDecimal bdBetragInRechnungswahrung;
	private String rechnungswaehrung;

	private WrapperLabel wlaAufschlag = new WrapperLabel();
	private WrapperNumberField wnfAufschlagProzent = new WrapperNumberField();
	private WrapperNumberField wnfAufschlagBetrag = new WrapperNumberField();

	private WrapperLabel wlaGesamtInclAufschlag = new WrapperLabel();
	private WrapperNumberField wnfGesamtInclAufschlag = new WrapperNumberField();

	private VerrechnungsmodellDto vmDto = null;
	private KundeDto kundeDto = null;

	private BestellvorschlagUeberleitungKriterienDto kritDto = null;

	PersonalDto personalDto = null;

	public PanelDialogAbrechnungsvorschlagUeberleitenER(Integer kundeIId, VerrechnungsmodellDto vmDto,
			BigDecimal bdBetragInRechnungswahrung, String rechnungswaehrung, InternalFrame oInternalFrameI, String title) throws Throwable {
		super(oInternalFrameI, title);
		this.bdBetragInRechnungswahrung = bdBetragInRechnungswahrung;
		this.rechnungswaehrung=rechnungswaehrung;
		this.vmDto = vmDto;
		this.kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(kundeIId);
		jbInit();
		setDefaults();
		initComponents();

		wefKommentar.requestFocus();
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);
		personalDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(LPMain.getTheClient().getIDPersonal());

		if (vmDto != null) {
			wifArtikel.setArtikelIId(vmDto.getArtikelIIdEr());
		}

		wlaBetragAusPositionen.setText(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.abrechnen.betragauspositionen"));

		wlaBetragZuVerrechnen.setText(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.abrechnen.betragverrechnen"));

		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr("lp.kommentar"));

		wcbErledigt.setText(LPMain.getInstance().getTextRespectUISPr("label.erledigt"));

		wlaVKPreis.setText(LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.ueberleiten.vkpreis"));

		wnfBetragAusPositionen.setEditable(false);
		
		wnfAufschlagBetrag.setEditable(false);
		wnfVKPreis.setEditable(false);
		wnfGesamtInclAufschlag.setEditable(false);

		Double dAufschlag = 0D;
		if (vmDto != null) {
			dAufschlag = vmDto.getFAufschlagEr();

			wnfAufschlagBetrag
					.setBigDecimal(Helper.getProzentWert(bdBetragInRechnungswahrung, new BigDecimal(dAufschlag), 2));

		} else {
			wnfAufschlagBetrag.setBigDecimal(BigDecimal.ZERO);
		}
		wlaAufschlag.setText(LPMain.getInstance()
				.getMessageTextRespectUISPr("rech.abrechnungsvorschlag.ueberleiten.er.aufschlag"));
		
		wnfAufschlagProzent.setDouble(dAufschlag);
		
		wlaGesamtInclAufschlag.setText(LPMain.getInstance()
				.getMessageTextRespectUISPr("rech.abrechnungsvorschlag.ueberleiten.er.gesamtinclaufschlag"));

		wnfBetragZuVerrechnen.addFocusListener(this);
		wnfAufschlagProzent.addFocusListener(this);

		wnfBetragZuVerrechnen.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wnfVKPreis.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());

		wefKommentar.setInternalFrame(getInternalFrame());
		
		iZeile++;
		jpaWorkingOn.add(
				new JLabel(kundeDto.getPartnerDto().formatAnrede() + " - " + kundeDto.getPartnerDto().formatAdresse()),
				new GridBagConstraints(0, iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 10, 2), 0, 0));

		wnfBetragAusPositionen.setBigDecimal(bdBetragInRechnungswahrung);
		wnfBetragZuVerrechnen.setBigDecimal(bdBetragInRechnungswahrung);
		wnfBetragZuVerrechnen.setMandatoryField(true);
		
		wnfAufschlagProzent.setMandatoryField(true);

		wnfGesamtInclAufschlag.setBigDecimal(wnfBetragZuVerrechnen.getBigDecimal().add(wnfAufschlagBetrag.getBigDecimal()));

		iZeile++;
		jpaWorkingOn.add(wlaBetragAusPositionen, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragAusPositionen, new GridBagConstraints(1, iZeile, 2, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new JLabel(rechnungswaehrung), new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBetragZuVerrechnen, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragZuVerrechnen, new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new JLabel(rechnungswaehrung), new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaAufschlag, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAufschlagProzent, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAufschlagBetrag, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new JLabel(rechnungswaehrung), new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaGesamtInclAufschlag, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGesamtInclAufschlag, new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new JLabel(rechnungswaehrung), new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(), new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		wcbErledigt.setSelected(true);

		wlaKommentar.setHorizontalAlignment(SwingConstants.LEFT);

		iZeile++;

		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbErledigt, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wefKommentar, new GridBagConstraints(0, iZeile, 5, 1, 0.0, 1.0, GridBagConstraints.CENTER,
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

	public void focusLost(FocusEvent e) {
		try {
			
			if(wnfAufschlagProzent.getBigDecimal()==null) {
				wnfAufschlagProzent.setBigDecimal(BigDecimal.ZERO);
			}
			
			if ((e.getSource().equals(wnfAufschlagProzent) || e.getSource().equals(wnfBetragZuVerrechnen)) && wnfBetragZuVerrechnen.getBigDecimal() != null
					&& wnfBetragAusPositionen.getBigDecimal() != null) {

				if (wnfBetragZuVerrechnen.getBigDecimal().doubleValue() >= wnfBetragAusPositionen.getBigDecimal()
						.doubleValue()) {
					wcbErledigt.setSelected(true);
				} else {
					wcbErledigt.setSelected(false);
				}

				if (vmDto != null) {
					wnfAufschlagBetrag.setBigDecimal(Helper.getProzentWert(wnfBetragZuVerrechnen.getBigDecimal(),
							wnfAufschlagProzent.getBigDecimal(),2));
				}
				wnfGesamtInclAufschlag
						.setBigDecimal(wnfBetragZuVerrechnen.getBigDecimal().add(wnfAufschlagBetrag.getBigDecimal()));

			}
		} catch (ExceptionLP e1) {
			handleException(e1, true);
		}
	}

	private void aktualisiereVKPreis() throws Throwable {
		if (wifArtikel.getArtikelDto() != null) {
			wifArtikel.setArtikelDto(DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(wifArtikel.getArtikelDto().getIId()));

			VerkaufspreisDto vkpreisDto = DelegateFactory.getInstance().getVkPreisfindungDelegate()
					.getVKPreisEinfachInZielwaehrungZuHeute(kundeDto.getIId(), wifArtikel.getArtikelDto().getIId(),
							BigDecimal.ZERO, rechnungswaehrung);
			if (vkpreisDto != null) {
				wnfVKPreis.setBigDecimal(vkpreisDto.getNettpreisOhneMaterialzuschlag());
			} else {
				wnfVKPreis.setBigDecimal(null);
			}
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == wifArtikel && wifArtikel.getArtikelDto() != null) {
				aktualisiereVKPreis();
			}
		}
	}

	public BigDecimal getBetragZuVerrechnen() throws ExceptionLP {
		return wnfBetragZuVerrechnen.getBigDecimal();
	}
	
	public BigDecimal getBetragInclAufschlagZuVerrechnen() throws ExceptionLP {
		return wnfGesamtInclAufschlag.getBigDecimal();
	}

	public Integer getArtikelIId() {
		return wifArtikel.getArtikelDto().getIId();
	}

	public String getKommentar() throws ExceptionLP, TextBlockOverflowException {
		return wefKommentar.getText();
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
