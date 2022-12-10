
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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.lp.client.angebotstkl.AngebotstklFilterFactory;
import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>
 * <I>Dialog zur Eingabe der Kriterien fuer den Bestellvorschlag.</I>
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
 * @author Uli Walch
 * @version $Revision: 1.9 $
 */
public class PanelDialogKriterienBestellvorschlagAnhandEkag extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WrapperLabel wlaVorlaufzeit = null;
	private WrapperNumberField wnfVorlaufzeit = null;

	private PanelQueryFLR panelQueryFLREkag = null;

	private Integer ekagIId = null;

	private WrapperButton wbuEkag = new WrapperButton();
	private WrapperTextField wtfEkag = new WrapperTextField();

	private WrapperLabel wlaFertigungsterminGeplant = new WrapperLabel();
	private WrapperDateField wdfFertigungsterminGeplant = new WrapperDateField();

	private WrapperComboBox wcbMengen = new WrapperComboBox();
	private WrapperLabel wlaMengen = new WrapperLabel();

	private static final String ACTION_SPECIAL_LEEREN = "action_special_leeren";
	static final public String ACTION_SPECIAL_EKAG_FROM_LISTE = "action_ekag_from _liste";

	public PanelDialogKriterienBestellvorschlagAnhandEkag(InternalFrame oInternalFrameI, String title)
			throws HeadlessException, Throwable {
		super(oInternalFrameI, title);

		try {
			jbInit();
			setDefaults();
			initComponents();
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	}

	private void jbInit() throws Throwable {

		wlaVorlaufzeit = new WrapperLabel();
		wlaVorlaufzeit.setText(LPMain.getInstance().getTextRespectUISPr("bes.bestellvorschlag.anhandekag.vorlaufzeit"));

		wbuEkag.setText(LPMain.getInstance().getTextRespectUISPr("bes.bestellvorschlag.anhandekag.ekag"));

		wlaFertigungsterminGeplant = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebot.fertigungstermin.geplant"));
		wdfFertigungsterminGeplant.setMandatoryField(true);
		wtfEkag.setMandatoryField(true);
		wnfVorlaufzeit = new WrapperNumberField();
		wnfVorlaufzeit.setMandatoryField(true);
		wnfVorlaufzeit.setFractionDigits(0);

		wbuEkag.addActionListener(this);
		wbuEkag.setActionCommand(ACTION_SPECIAL_EKAG_FROM_LISTE);
		wtfEkag.setActivatable(false);

		wcbMengen.setMandatoryField(true);

		wlaMengen.setText(LPMain.getInstance().getTextRespectUISPr("agstkl.einkaufsangebot.bestellvorschlag.menge"));

		iZeile++;
		jpaWorkingOn.add(wbuEkag, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 150, 0));

		jpaWorkingOn.add(wtfEkag, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 300, 0));
		jpaWorkingOn.add(new WrapperLabel(""), new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMengen, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbMengen, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 80, 0));
		iZeile++;
		jpaWorkingOn.add(wlaFertigungsterminGeplant, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfFertigungsterminGeplant, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaVorlaufzeit, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wnfVorlaufzeit, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -50, 0));
		iZeile++;

	}

	private void setDefaults() throws Throwable {
		ParametermandantDto parametermandantDto = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getInstance().getTheClient().getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_AUFTRAG);
		wnfVorlaufzeit.setInteger((Integer) parametermandantDto
				.getCWertAsObject());
	}

	public Integer getEkagIId() {
		return ekagIId;
	}

	public Integer getMengenstaffel() {
		return (Integer) wcbMengen.getKeyOfSelectedItem();
	}

	public Integer getVorlaufzeit() throws Throwable {
		return wnfVorlaufzeit.getInteger();
	}

	public Timestamp getGeplantenFertigungstermin() throws Throwable {
		return wdfFertigungsterminGeplant.getTimestamp();
	}

	void dialogQueryEkagFromListe(ActionEvent e) throws Throwable {

		FilterKriterium fkSchnellansicht = new FilterKriterium("i_id", true,
				"(SELECT distinct poslief.flreinkaufsangebotposition.einkaufsangebot_i_id FROM FLRPositionlieferant poslief WHERE (poslief.b_menge1_bestellen=1 OR poslief.b_menge2_bestellen=1 OR poslief.b_menge3_bestellen=1 OR poslief.b_menge4_bestellen=1 OR poslief.b_menge5_bestellen=1) )",
				FilterKriterium.OPERATOR_IN, false);

		panelQueryFLREkag =

				new PanelQueryFLR(null, SystemFilterFactory.getInstance().createFKMandantCNr(),
						QueryParameters.UC_ID_EINKAUFSANGEBOT, null, getInternalFrame(),
						LPMain.getInstance().getTextRespectUISPr("auft.title.panel.auswahl"));

		panelQueryFLREkag.befuelleFilterkriteriumSchnellansicht(new FilterKriterium[] { fkSchnellansicht });

		panelQueryFLREkag.getCbSchnellansicht()
				.setText(LPMain.getInstance().getTextRespectUISPr("bes.bestellvorschlag.anhandekag.nurbestellt"));

		panelQueryFLREkag.befuellePanelFilterkriterienDirekt(
				AngebotstklFilterFactory.getInstance().createFKDEinakufsangebotbelegnumer(),
				AngebotstklFilterFactory.getInstance().createFKDEinkaufsangebotKundeName());

		panelQueryFLREkag.eventActionRefresh(null, true);

		new DialogQuery(panelQueryFLREkag);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_EKAG_FROM_LISTE)) {
			dialogQueryEkagFromListe(e);
		}

		if (e.getActionCommand().equals(ACTION_SPECIAL_LEEREN)) {

		} else {
			super.eventActionSpecial(e);
		}

	}

	public void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLREkag) {
				Integer ekagIId = (Integer) panelQueryFLREkag.getSelectedId();

				EinkaufsangebotDto ekagDto = DelegateFactory.getInstance().getAngebotstklDelegate()
						.einkaufsangebotFindByPrimaryKey(ekagIId);

				Map m = new LinkedHashMap();

				if (ekagDto.getNMenge1() != null && ekagDto.getNMenge1().doubleValue() > 0) {
					m.put(AngebotstklFac.MENGE_1,
							Helper.formatZahl(ekagDto.getNMenge1(), 2, LPMain.getTheClient().getLocUi()));
				}
				if (ekagDto.getNMenge2() != null && ekagDto.getNMenge2().doubleValue() > 0) {
					m.put(AngebotstklFac.MENGE_2,
							Helper.formatZahl(ekagDto.getNMenge2(), 2, LPMain.getTheClient().getLocUi()));
				}
				if (ekagDto.getNMenge3() != null && ekagDto.getNMenge3().doubleValue() > 0) {
					m.put(AngebotstklFac.MENGE_3,
							Helper.formatZahl(ekagDto.getNMenge3(), 2, LPMain.getTheClient().getLocUi()));
				}
				if (ekagDto.getNMenge4() != null && ekagDto.getNMenge4().doubleValue() > 0) {
					m.put(AngebotstklFac.MENGE_4,
							Helper.formatZahl(ekagDto.getNMenge4(), 2, LPMain.getTheClient().getLocUi()));
				}
				if (ekagDto.getNMenge5() != null && ekagDto.getNMenge5().doubleValue() > 0) {
					m.put(AngebotstklFac.MENGE_5,
							Helper.formatZahl(ekagDto.getNMenge5(), 2, LPMain.getTheClient().getLocUi()));
				}

				wcbMengen.setMap(m);

				this.ekagIId = ekagIId;

				String s = ekagDto.getCNr();
				if (ekagDto.getCProjekt() != null) {
					s += " " + ekagDto.getCProjekt();
				}

				wtfEkag.setText(s);
				wdfFertigungsterminGeplant.setDate(ekagDto.getTFertigungstermin());
				if (panelQueryFLREkag.dialog != null) {
					panelQueryFLREkag.dialog.setVisible(false);
				}
			}
		}
	}

}
