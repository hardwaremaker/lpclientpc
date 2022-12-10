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
package com.lp.client.frame.report;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.ReportJournalKriterienDto;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich ...</I>
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellungsdatum <I>22.03.05</I>
 * </p>
 *
 * <p>
 * </p>
 *
 * @author Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public abstract class PanelReportJournalVerkauf extends PanelReportJournal {

	protected final static String ACTION_SPECIAL_SORTIERUNG_PROVISIONSEMPFAENGER = "action_special_sortierung_provisionsempfaenger";
	protected final static String ACTION_SPECIAL_PROVISIONSEMPFAENGER_AUSWAHL = "action_special_provisionsempfaenger_auswahl";
	protected final static String ACTION_SPECIAL_PROVISIONSEMPFAENGER_ALLE = "action_special_provisionsempfaenger_alle";
	protected final static String ACTION_SPECIAL_PROVISIONSEMPFAENGER_EINER = "action_special_provisionsempfaenger_einer";

	protected WrapperRadioButton wrbSortierungProvisionsempfaenger = new WrapperRadioButton();
	protected WrapperButton wbuProvisionsempfaenger = null;
	protected WrapperTextField wtfProvisionsempfaenger = null;
	protected ButtonGroup jbgProvisionsempfaenger = new ButtonGroup();
	protected WrapperRadioButton wrbProvisionsempfaengerAlle = new WrapperRadioButton();
	protected WrapperRadioButton wrbProvisionsempfaengerEiner = new WrapperRadioButton();

	protected JPanel jPanelProvisionsempfaenger = null;
	protected PanelQueryFLR panelQueryFLRProvisionsempfaenger = null;
	protected PersonalDto provisionsempfaengerDto = null;

	private static final long serialVersionUID = 1L;

	private PanelQueryFLR panelQueryFLRKunde = null;

	protected KundeDto kundeDto = null;

	public PanelReportJournalVerkauf(InternalFrame internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInitVerkauf();
		initComponents();
	}

	/**
	 * jbInit. MUSS private bleiben, zerstoert sonst subklassen.
	 *
	 * @throws Throwable
	 */
	private final void jbInitVerkauf() throws Throwable {
		wrbSortierungPartner.setText(LPMain.getTextRespectUISPr("button.kunden"));
		wbuPartner.setText(LPMain.getTextRespectUISPr("button.kunde"));
		wbuPartner.setToolTipText(LPMain.getTextRespectUISPr("button.kunde.tooltip"));

	}

	protected void befuelleKriterien(ReportJournalKriterienDto krit) {

		super.befuelleKriterien(krit);
		if (wrbPartnerEiner.isSelected()) {
			krit.kundeIId = kundeDto.getIId();
		}

		if (wrbSortierungProvisionsempfaenger.isSelected()) {
			krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_PROVISIONSEMPFAENGER;
		}
		if (wrbProvisionsempfaengerEiner.isSelected()) {

			if (wtfProvisionsempfaenger != null && wtfProvisionsempfaenger.isVisible()
					&& provisionsempfaengerDto != null) {
				krit.provisionsempfaengerIId = provisionsempfaengerDto.getIId();
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER_EINER)) {
			// wenn noch keiner gewaehlt
			// SP8514 und Dialog bereits gestartet
			if (kundeDto == null && isInitialized()) {
				wbuPartner.doClick();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER_AUSWAHL)) {
			dialogQueryKunde(e);
		}
		

		if (e.getActionCommand().equals(ACTION_SPECIAL_PROVISIONSEMPFAENGER_EINER)) {
			// wenn noch keiner gewaehlt ist, dann geht der Dialog auf
			if (provisionsempfaengerDto.getIId() == null && isInitialized()) {
				wbuProvisionsempfaenger.doClick();
			}

			wbuProvisionsempfaenger.setVisible(true);
			wtfProvisionsempfaenger.setVisible(true);
			wtfProvisionsempfaenger.setMandatoryField(true);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PROVISIONSEMPFAENGER_AUSWAHL)) {
			dialogQueryProvisionsempfaenger(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PROVISIONSEMPFAENGER_ALLE)) {
			wbuProvisionsempfaenger.setVisible(false);
			wtfProvisionsempfaenger.setVisible(false);
			wtfProvisionsempfaenger.setMandatoryField(false);

			provisionsempfaengerDto = new PersonalDto();
		}
		
	}

	private void dialogQueryProvisionsempfaenger(ActionEvent e) throws Throwable {
		panelQueryFLRProvisionsempfaenger = PersonalFilterFactory.getInstance()
				.createPanelFLRPersonal(getInternalFrame(), true, false, provisionsempfaengerDto.getIId());

		new DialogQuery(panelQueryFLRProvisionsempfaenger);
	}

	
	/**
	 * Dialogfenster zur Kundenauswahl.
	 *
	 * @param e ActionEvent
	 * @throws Throwable
	 */
	private void dialogQueryKunde(ActionEvent e) throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(getInternalFrame(), true, true);
		if (kundeDto != null) {
			panelQueryFLRKunde.setSelectedId(kundeDto.getIId());
		}
		new DialogQuery(panelQueryFLRKunde);
	}

	protected void addProvisionsempfaenger() {
		wbuProvisionsempfaenger = new WrapperButton(
				LPMain.getTextRespectUISPr("part.kunde.button.provisionsempfaenger"));
		wbuProvisionsempfaenger.setActionCommand(ACTION_SPECIAL_PROVISIONSEMPFAENGER_AUSWAHL);
		wbuProvisionsempfaenger.addActionListener(this);

		wbuProvisionsempfaenger
				.setMinimumSize(new Dimension(BREITE_BUTTONS, Defaults.getInstance().getControlHeight()));
		wbuProvisionsempfaenger
				.setPreferredSize(new Dimension(BREITE_BUTTONS, Defaults.getInstance().getControlHeight()));
		wtfProvisionsempfaenger = new WrapperTextField();
		wtfProvisionsempfaenger.setEditable(false);
		wtfProvisionsempfaenger.setMinimumSize(new Dimension(50, Defaults.getInstance().getControlHeight()));
		wtfProvisionsempfaenger.setPreferredSize(new Dimension(50, Defaults.getInstance().getControlHeight()));

		wrbSortierungProvisionsempfaenger = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("part.kunde.provisionsempfaenger"));
		wrbSortierungProvisionsempfaenger.setActionCommand(ACTION_SPECIAL_SORTIERUNG_PROVISIONSEMPFAENGER);
		wrbSortierungProvisionsempfaenger.addActionListener(this);
		buttonGroupSortierung.add(wrbSortierungProvisionsempfaenger);

		wrbProvisionsempfaengerAlle.setText(LPMain.getTextRespectUISPr("label.alle"));
		wrbProvisionsempfaengerAlle.setActionCommand(ACTION_SPECIAL_PROVISIONSEMPFAENGER_ALLE);
		wrbProvisionsempfaengerAlle.addActionListener(this);

		wrbProvisionsempfaengerEiner.setText(LPMain.getTextRespectUISPr("label.einer"));
		wrbProvisionsempfaengerEiner.setActionCommand(ACTION_SPECIAL_PROVISIONSEMPFAENGER_EINER);
		wrbProvisionsempfaengerEiner.addActionListener(this);
		jbgProvisionsempfaenger = new ButtonGroup();
		jbgProvisionsempfaenger.add(wrbProvisionsempfaengerAlle);
		jbgProvisionsempfaenger.add(wrbProvisionsempfaengerEiner);

		provisionsempfaengerDto = new PersonalDto();
		wrbProvisionsempfaengerAlle.setSelected(true);
		wbuProvisionsempfaenger.setVisible(false);
		wtfProvisionsempfaenger.setVisible(false);

		iZeile++;
		jpaWorkingOn.add(wrbSortierungProvisionsempfaenger, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbProvisionsempfaengerAlle, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wrbProvisionsempfaengerEiner, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(getPanelProvisionsempfaenger(), new GridBagConstraints(2, iZeile, 4, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	}

	protected JPanel getPanelProvisionsempfaenger() {
		if (jPanelProvisionsempfaenger == null) {
			jPanelProvisionsempfaenger = new JPanel(new GridBagLayout());

			jPanelProvisionsempfaenger.add(wbuProvisionsempfaenger, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jPanelProvisionsempfaenger.add(wtfProvisionsempfaenger, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}

		return jPanelProvisionsempfaenger;
	}

	/**
	 * eventItemchanged.
	 *
	 * @param eI EventObject
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKunde) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKunde((Integer) key);
			}
			if (e.getSource() == panelQueryFLRProvisionsempfaenger) {

				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				if (key != null) {
					provisionsempfaengerDto = DelegateFactory.getInstance().getPersonalDelegate()
							.personalFindByPrimaryKey((Integer) key);

					wtfProvisionsempfaenger.setText(provisionsempfaengerDto.getPartnerDto().formatFixName2Name1());
				}
			}

		}
	}

	/**
	 * Die Kundendaten in die Felder schreiben
	 *
	 * @throws Throwable
	 */
	private void dto2ComponentsKunde() throws Throwable {
		if (kundeDto != null) {
			this.wtfPartner.setText(kundeDto.getPartnerDto().formatFixTitelName1Name2());
		} else {
			this.wtfPartner.setText(null);
		}
	}

	private void holeKunde(Integer key) throws Throwable {
		if (key != null) {
			kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(key);
			dto2ComponentsKunde();
		}
	}

}
