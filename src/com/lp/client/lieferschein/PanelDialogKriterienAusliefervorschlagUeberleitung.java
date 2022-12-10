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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ButtonGroup;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.bestellung.service.BestellvorschlagUeberleitungKriterienDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access")
/**
 * <p><I>Dialog zur Eingabe der Kriterien fuer die Ueberleitung des Bestellvorschlags in Belege.</I></p>
 * <p>Dieser Dialog wird aus den folgenden Modulen aufgerufen:</p>
 * <ul>
 * <li>Bestellung/Bestellvorschlag
 * <li>Anfrage/Anfragevorschlag
 * </ul>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>22.11.05</I></p>
 * <p> </p>
 * @version $Revision: 1.6 $
 */
public class PanelDialogKriterienAusliefervorschlagUeberleitung extends
		PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ButtonGroup jbgKriterien = null;

	private WrapperRadioButton wrbAlle = null;
	private WrapperRadioButton wrbEinKunde = null;
	private WrapperRadioButton wrbEinKundeLieferadrese = null;

	private WrapperLabel wlaEmpty = null; // fuer die Formatierung
	private WrapperButton wbuKunde2 = null;
	private WrapperTextField wtfKunde2 = null;

	private WrapperButton wbuLieferadresse = null;
	private WrapperTextField wtfLieferadresse = null;
	private KundeDto kundeDtoLieferadresse = null;

	private WrapperButton wbuEinKunde = null;
	private WrapperTextField wtfEinKunde = null;
	private KundeDto kundeDto = null;

	private PanelQueryFLR panelQueryLieferadresse = null;
	private PanelQueryFLR panelQueryKunde = null;

	private static final String ACTION_SPECIAL_FLR_LIEFERADRESSE = "action_special_flr_lieferadresse";
	private static final String ACTION_SPECIAL_FLR_KUNDE = "action_special_flr_einkunde";

	private BestellvorschlagUeberleitungKriterienDto kritDto = null;

	private Set<Integer> setIIds = null;

	public Set<Integer> getSetIIds() {
		return setIIds;
	}

	public PanelDialogKriterienAusliefervorschlagUeberleitung(
			InternalFrame oInternalFrameI, String title, Set<Integer> setIIds)
			throws Throwable {
		super(oInternalFrameI, title);
		this.setIIds = setIIds;
		jbInit();
		setDefaults();
		initComponents();
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		jbgKriterien = new ButtonGroup();

		wrbAlle = new WrapperRadioButton(LPMain.getInstance()
				.getTextRespectUISPr(
						"ls.ausliefervorschlag.ueberleiten.allekunden"));

		wrbAlle.addActionListener(this);
		wrbAlle.setSelected(true);

		wrbEinKunde = new WrapperRadioButton(LPMain.getInstance()
				.getTextRespectUISPr(
						"ls.ausliefervorschlag.ueberleiten.einkunde"));

		wrbEinKunde.addActionListener(this);

		wrbEinKundeLieferadrese = new WrapperRadioButton(
				LPMain.getInstance()
						.getTextRespectUISPr(
								"ls.ausliefervorschlag.ueberleiten.einkundelieferadresse"));

		wrbEinKundeLieferadrese.addActionListener(this);

		wlaEmpty = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaEmpty, 15);
		wbuKunde2 = new WrapperButton(LPMain.getInstance().getTextRespectUISPr(
				"button.kunde"));
		wbuKunde2.setActionCommand(ACTION_SPECIAL_FLR_KUNDE);
		wbuKunde2.addActionListener(this);
		wtfKunde2 = new WrapperTextField();
		wtfKunde2.setEditable(false);
		wtfKunde2.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuLieferadresse = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("ls.ausliefervorschlag.lieferadresse")
				+ "...");
		wbuLieferadresse.setActionCommand(ACTION_SPECIAL_FLR_LIEFERADRESSE);
		wbuLieferadresse.addActionListener(this);
		wtfLieferadresse = new WrapperTextField();
		wtfLieferadresse.setEditable(false);
		wtfLieferadresse.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuEinKunde = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.kunde"));
		wbuEinKunde.setActionCommand(ACTION_SPECIAL_FLR_KUNDE);
		wbuEinKunde.addActionListener(this);
		HelperClient.setDefaultsToComponent(wbuEinKunde, 120);
		wtfEinKunde = new WrapperTextField();
		wtfEinKunde.setMandatoryField(false);
		wtfEinKunde.setEditable(false);

		jbgKriterien.add(wrbAlle);
		jbgKriterien.add(wrbEinKunde);
		jbgKriterien.add(wrbEinKundeLieferadrese);

		jpaWorkingOn.add(wrbAlle, new GridBagConstraints(0, iZeile, 3, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbEinKunde, new GridBagConstraints(0, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuEinKunde, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfEinKunde, new GridBagConstraints(2, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(10, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbEinKundeLieferadrese, new GridBagConstraints(0,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaEmpty, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuKunde2, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKunde2, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuLieferadresse, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferadresse, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

	}

	private void setDefaults() throws Throwable {
		kundeDto = new KundeDto();
		kundeDtoLieferadresse = new KundeDto();

		wbuEinKunde.setEnabled(false);
		wbuKunde2.setEnabled(false);
		wbuLieferadresse.setEnabled(false);
		wtfKunde2.setEditable(false);
		wtfLieferadresse.setEditable(false);
		wtfEinKunde.setEditable(false);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_KUNDE)) {
			panelQueryKunde = PartnerFilterFactory.getInstance()
					.createPanelFLRKunde(getInternalFrame(), false, false,
							kundeDto.getIId());
			FilterKriterium[] krit = new FilterKriterium[2];
			krit[0] = new FilterKriterium("mandant_c_nr", true, "'"
					+ LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_LIKE, false);

			Set<Integer> kundenIIds = DelegateFactory.getInstance()
					.getAusliefervorschlagDelegate()
					.getKundenIIsEinesAusliefervorschlages(setIIds, false);

			String sKrit = "";

			Iterator it = kundenIIds.iterator();
			while (it.hasNext()) {
				sKrit += it.next();

				if (it.hasNext()) {
					sKrit += ",";
				}
			}

			krit[1] = new FilterKriterium("i_id", true, "(" + sKrit + ")",
					FilterKriterium.OPERATOR_IN, false);

			panelQueryKunde.setDefaultFilter(krit);
			panelQueryKunde.eventActionRefresh(null, true);
			new DialogQuery(panelQueryKunde);
		}
		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_LIEFERADRESSE)) {
			panelQueryLieferadresse = PartnerFilterFactory.getInstance()
					.createPanelFLRKunde(getInternalFrame(), false, false,
							kundeDtoLieferadresse.getIId());
			FilterKriterium[] krit = new FilterKriterium[2];
			krit[0] = new FilterKriterium("mandant_c_nr", true, "'"
					+ LPMain.getTheClient().getMandant() + "'",
					FilterKriterium.OPERATOR_LIKE, false);

			Set<Integer> kundenIIds = DelegateFactory.getInstance()
					.getAusliefervorschlagDelegate()
					.getKundenIIsEinesAusliefervorschlages(setIIds, true);

			String sKrit = "";

			Iterator it = kundenIIds.iterator();
			while (it.hasNext()) {
				sKrit += it.next();

				if (it.hasNext()) {
					sKrit += ",";
				}
			}

			krit[1] = new FilterKriterium("i_id", true, "(" + sKrit + ")",
					FilterKriterium.OPERATOR_IN, false);

			panelQueryLieferadresse.setDefaultFilter(krit);
			panelQueryLieferadresse.eventActionRefresh(null, true);
			new DialogQuery(panelQueryLieferadresse);
		} else if (e.getSource().equals(wrbAlle)) {
			wbuEinKunde.setEnabled(false);
			wbuKunde2.setEnabled(false);
			wbuLieferadresse.setEnabled(false);
			wtfKunde2.setEditable(false);
			wtfLieferadresse.setEditable(false);
			wtfEinKunde.setEditable(false);
			wtfKunde2.setMandatoryField(false);
			wtfLieferadresse.setMandatoryField(false);
			wtfEinKunde.setMandatoryField(false);
			kundeDto = new KundeDto();
			kundeDtoLieferadresse = new KundeDto();

			wtfKunde2.setText("");
			wtfLieferadresse.setText("");
			wtfEinKunde.setText("");

		} else if (e.getSource().equals(wrbEinKunde)) {
			wbuEinKunde.setEnabled(true);
			wbuKunde2.setEnabled(false);
			wbuLieferadresse.setEnabled(false);
			wtfKunde2.setEditable(false);
			wtfKunde2.setMandatoryField(false);
			wtfLieferadresse.setMandatoryField(false);
			wtfEinKunde.setMandatoryField(true);
			wtfLieferadresse.setEditable(false);
			wtfEinKunde.setEditable(false);

			kundeDtoLieferadresse = new KundeDto();

			wtfKunde2.setText("");
			wtfLieferadresse.setText("");

		} else if (e.getSource().equals(wrbEinKundeLieferadrese)) {
			wbuEinKunde.setEnabled(false);
			wbuKunde2.setEnabled(true);
			wbuLieferadresse.setEnabled(true);
			wtfKunde2.setEditable(false);
			wtfLieferadresse.setEditable(false);
			wtfEinKunde.setEditable(false);

			wtfKunde2.setMandatoryField(true);
			wtfLieferadresse.setMandatoryField(true);
			wtfEinKunde.setMandatoryField(false);

			wtfKunde2.setText("");
			wtfLieferadresse.setText("");
			wtfEinKunde.setText("");

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			if (allMandatoryFieldsSetDlg()) {
				buildBenutzerKriterien();

				super.eventActionSpecial(e);
			}
		} else if (e.getActionCommand()
				.equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			super.eventActionSpecial(e);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryKunde) {
				Integer id = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(id);

				if (wrbEinKunde.isSelected()) {

					wtfEinKunde.setText(kundeDto.getPartnerDto()
							.formatFixName1Name2());
				} else {
					wtfKunde2.setText(kundeDto.getPartnerDto()
							.formatFixName1Name2());
				}
			} else if (e.getSource() == panelQueryLieferadresse) {
				Integer id = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				kundeDtoLieferadresse = DelegateFactory.getInstance()
						.getKundeDelegate().kundeFindByPrimaryKey(id);

				wtfLieferadresse.setText(kundeDtoLieferadresse.getPartnerDto()
						.formatFixName1Name2());

			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	private BestellvorschlagUeberleitungKriterienDto buildBenutzerKriterien() {
		kritDto = new BestellvorschlagUeberleitungKriterienDto();

		kritDto.setBBelegprolieferantprotermin(wrbAlle.isSelected());
		kritDto.setBBelegprolieferant(wrbEinKunde.isSelected());
		kritDto.setBBelegeinlieferanteintermin(wrbEinKundeLieferadrese
				.isSelected());

		kritDto.setKostenstelleIId(kundeDto.getIId());

		return kritDto;
	}

	public Integer getKundeIId() {
		return kundeDto.getIId();
	}

	public Integer getKundeIIdLieferadresse() {
		return kundeDtoLieferadresse.getIId();
	}
}
