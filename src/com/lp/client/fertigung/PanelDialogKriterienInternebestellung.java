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
package com.lp.client.fertigung;

import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access")
/**
 * <p>
 * <I>Dialog zur Eingabe der Kriterien fuer die Interne Bestellung.</I>
 * </p>
 * <p>
 * Dieser Dialog wird aus den folgenden Modulen aufgerufen:
 * </p>
 * <ul>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>22.11.05</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class PanelDialogKriterienInternebestellung extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaVorlaufZeitAuftrag = null;
	private WrapperNumberField wnfVorlaufZeitAuftrag = null;

	private WrapperLabel wlaVorlaufZeitUnterlose = null;
	private WrapperNumberField wnfVorlaufUnterlose = null;

	private WrapperLabel wlaToleranz = null;
	private WrapperNumberField wnfToleranz = null;

	private WrapperLabel wlaLieferterminfuerArtikelOhneReservierung = null;
	private WrapperDateField wdfLieferterminfuerArtikelOhneReservierung = null;

	private WrapperCheckBox wcbVorhandeneInterneBestellungEintrageLoeschen = null;
	private WrapperCheckBox wcbVerdichten = null;

	private WrapperCheckBox wcbExakterAuftragsbezug = null;

	private WrapperCheckBox wcbMitNichtFreigegebeneAuftraegen = null;

	private WrapperLabel wlaVerdichtungstage = null;
	private WrapperNumberField wnfVerdichtungstage = null;

	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperTextField wtfAuftrag = new WrapperTextField();
	private PanelQueryFLR panelQueryFLRAuftrag = null;

	private WrapperButton wbuLos = new WrapperButton();
	private WrapperTextField wtfLos = new WrapperTextField();

	private ArrayList<Integer> losIId = new ArrayList<Integer>();

	private WrapperButton wbuFertigungsgruppe = new WrapperButton();
	private WrapperTextField wtfFertigungsgruppe = new WrapperTextField();

	private Integer fertigungsgruppeIId = null;
	private ArrayList<Integer> auftragIId = new ArrayList<Integer>();

	static final public String ACTION_SPECIAL_LOS_FROM_LISTE = "action_auftrag_los_liste";
	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_auftrag_liste";

	private PanelQueryFLR panelQueryFLRLos = null;

	static final public String ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE = "ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE";

	private PanelQueryFLR panelQueryFLRFertigungsgruppe = null;

	public PanelDialogKriterienInternebestellung(InternalFrame oInternalFrameI, String title)
			throws HeadlessException, Throwable {
		super(oInternalFrameI, title);
		jbInitPanel();
		setDefaults();
		initComponents();
	}

	private void jbInitPanel() throws Throwable {
		wlaVorlaufZeitAuftrag = new WrapperLabel(LPMain.getTextRespectUISPr("bes.vorlaufzeiten"));

		wlaVorlaufZeitUnterlose = new WrapperLabel(LPMain.getTextRespectUISPr("bes.vorlaufzeiten.unterlose"));

		wlaToleranz = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("bes.toleranz"));
		wnfVorlaufZeitAuftrag = new WrapperNumberField();
		wnfVorlaufZeitAuftrag.setMandatoryField(true);
		wnfVorlaufZeitAuftrag.setMaximumIntegerDigits(3);
		wnfVorlaufZeitAuftrag.setFractionDigits(0);
		wnfVorlaufZeitAuftrag.setMinimumValue(1);

		wnfVorlaufUnterlose = new WrapperNumberField();
		wnfVorlaufUnterlose.setMandatoryField(true);
		wnfVorlaufUnterlose.setMaximumIntegerDigits(3);
		wnfVorlaufUnterlose.setFractionDigits(0);
		wnfVorlaufUnterlose.setMinimumValue(0);

		wlaVerdichtungstage = new WrapperLabel(LPMain.getTextRespectUISPr("lp.tage"));
		wnfVerdichtungstage = new WrapperNumberField();
		wnfVerdichtungstage.setMaximumIntegerDigits(3);
		wnfVerdichtungstage.setFractionDigits(0);
		wnfVerdichtungstage.setMinimumValue(1);

		wnfToleranz = new WrapperNumberField();
		wnfToleranz.setMandatoryField(true);
		wnfToleranz.setMaximumIntegerDigits(3);
		wnfToleranz.setFractionDigits(0);
		wnfToleranz.setMinimumValue(0);
		HelperClient.setDefaultsToComponent(wnfToleranz, 120);

		HelperClient.setDefaultsToComponent(wnfVorlaufZeitAuftrag, 120);

		wlaLieferterminfuerArtikelOhneReservierung = new WrapperLabel();
		wlaLieferterminfuerArtikelOhneReservierung
				.setText(LPMain.getTextRespectUISPr("bes.lieferdatumfuerartikelohnereservierung"));
		wdfLieferterminfuerArtikelOhneReservierung = new WrapperDateField();
		wdfLieferterminfuerArtikelOhneReservierung.setMandatoryField(true);

		wcbVorhandeneInterneBestellungEintrageLoeschen = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("fert.internebestellungloeschen"));

		wcbMitNichtFreigegebeneAuftraegen = new WrapperCheckBox(
				LPMain.getInstance().getTextRespectUISPr("bes.bestellvorschlag.nichtfreigegebene"));

		wcbVerdichten = new WrapperCheckBox(LPMain.getTextRespectUISPr("lp.verdichten"));

		wcbExakterAuftragsbezug = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("fert.internebestellung.exakterauftragsbezug"));
		wcbExakterAuftragsbezug.setSelected(true);

		wbuAuftrag.setText(LPMain.getInstance().getTextRespectUISPr("auft.modulname.tooltip") + "...");

		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuAuftrag.addActionListener(this);
		wtfAuftrag.setActivatable(false);
		wtfAuftrag.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);

		wbuFertigungsgruppe.setText(LPMain.getInstance().getTextRespectUISPr("stkl.fertigungsgruppe") + "...");
		wbuFertigungsgruppe.addActionListener(this);
		wbuFertigungsgruppe.setActionCommand(this.ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE);
		wtfFertigungsgruppe.setActivatable(false);

		wbuLos.setText(LPMain.getInstance().getTextRespectUISPr("auft.title.panel.lose") + "...");

		wbuLos.setActionCommand(ACTION_SPECIAL_LOS_FROM_LISTE);
		wbuLos.addActionListener(this);
		// wtfLos.setActivatable(false);
		wtfLos.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfLos.setActivatable(false);

		iZeile++;
		jpaWorkingOn.add(wlaVorlaufZeitAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfVorlaufZeitAuftrag, new GridBagConstraints(1, iZeile, 1, 1, 2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVorlaufZeitUnterlose, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfVorlaufUnterlose, new GridBagConstraints(1, iZeile, 1, 1, 2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaToleranz, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfToleranz, new GridBagConstraints(1, iZeile, 1, 1, 2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaLieferterminfuerArtikelOhneReservierung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfLieferterminfuerArtikelOhneReservierung, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbExakterAuftragsbezug, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 220, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbVorhandeneInterneBestellungEintrageLoeschen, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbVerdichten, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVerdichtungstage, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfVerdichtungstage, new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLos, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfLos, new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtfAuftrag, new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_AUFTRAG,
				ParameterFac.PARAMETER_AUFTRAGSFREIGABE);
		boolean auftragsfreigabe = (Boolean) parameter.getCWertAsObject();
		if (auftragsfreigabe) {
			iZeile++;
			jpaWorkingOn.add(wcbMitNichtFreigegebeneAuftraegen, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		}

	/*	parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_INTERNE_BESTELLUNG_ANGELEGTE_ENTFERNEN);
		boolean angelegteEntfernen = (Boolean) parameter.getCWertAsObject();
		if (angelegteEntfernen) {
			iZeile++;
			jpaWorkingOn.add(new JLabel(LPMain.getTextRespectUISPr("fert.internebestellung.angelegte.loeschen")),
					new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
							new Insets(2, 2, 2, 80), 50, 0));

			jpaWorkingOn.add(wbuFertigungsgruppe, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
			jpaWorkingOn.add(wtfFertigungsgruppe, new GridBagConstraints(1, iZeile, 1, 1, 0.5, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		}*/
	}

	void dialogQueryLosFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLos = FertigungFilterFactory.getInstance().createPanelFLRBebuchbareLose(getInternalFrame(), false,
				true, true, null, true);
		
		panelQueryFLRLos.setMultipleRowSelectionEnabled(true);
		panelQueryFLRLos.addButtonAuswahlBestaetigen(null);
		

		new DialogQuery(panelQueryFLRLos);

	}

	private void setDefaults() throws Throwable {
		// den Vorschlagswert fuer die Auftragsvorlaufdauer bestimmen
		ParametermandantDto parameterVorlaufzeit = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_INTERNEBESTELLUNG);
		wnfVorlaufZeitAuftrag.setInteger((Integer) parameterVorlaufzeit.getCWertAsObject());

		ParametermandantDto parameterVorlaufzeitUnterlose = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_DEFAULT_VORLAUFZEIT_INTERNEBESTELLUNG_UNTERLOSE);
		wnfVorlaufUnterlose.setInteger((Integer) parameterVorlaufzeitUnterlose.getCWertAsObject());

		wdfLieferterminfuerArtikelOhneReservierung.setDate(new Date(System.currentTimeMillis()));

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
				ParameterFac.PARAMETER_DEFAULT_INTERNEBESTELLUNG_LOESCHEN);
		boolean bloeschen = (Boolean) parameter.getCWertAsObject();

		wcbVorhandeneInterneBestellungEintrageLoeschen.setSelected(bloeschen);

		// den Vorschlagswert fuer die Verdichtungstage bestimmen
		ParametermandantDto parameterVerdichtungstage = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_INTERNEBESTELLUNG_VERDICHTUNGSZEITRAUM);
		wnfVerdichtungstage.setInteger((Integer) parameterVerdichtungstage.getCWertAsObject());
		ParametermandantDto parameterToleranz = DelegateFactory.getInstance().getParameterDelegate()
				.getMandantparameter(LPMain.getInstance().getTheClient().getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.TOLERANZFRIST_INTERNE_BESTELLUNG);
		wnfToleranz.setInteger((Integer) parameterToleranz.getCWertAsObject());

	}

	public Integer getAuftragsvorlaufzeit() throws Throwable {
		return wnfVorlaufZeitAuftrag.getInteger();
	}

	public Integer getVorlaufzeitUnterlose() throws Throwable {
		return wnfVorlaufUnterlose.getInteger();
	}

	public Integer getVerdichtungstage() throws Throwable {
		return wnfVerdichtungstage.getInteger();
	}

	public Date getLieferterminFuerArtikelOhneReservierung() {
		return wdfLieferterminfuerArtikelOhneReservierung.getDate();
	}

	public boolean getVorhandeneLoeschen() {
		return wcbVorhandeneInterneBestellungEintrageLoeschen.isSelected();
	}

	public boolean getBVerdichten() {
		return wcbVerdichten.isSelected();
	}

	public boolean getBExakterAuftragsbezug() {
		return wcbExakterAuftragsbezug.isSelected();
	}

	public boolean getBMitNichtFreigegebeneAuftraegen() {
		return wcbMitNichtFreigegebeneAuftraegen.isSelected();
	}

	public Integer getToleranz() throws ExceptionLP {
		return wnfToleranz.getInteger();
	}

	public ArrayList<Integer>  getLosIIds() {
		return losIId;
	}

	public Integer getFertigungsgruppeIId() {
		return fertigungsgruppeIId;
	}

	public ArrayList<Integer> getAuftragIIds() {
		return auftragIId;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfVorlaufZeitAuftrag;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_LOS_FROM_LISTE)) {
			dialogQueryLosFromListe(e);
		}

		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			if (!allMandatoryFieldsSetDlg()) {
				return;
			}
		}

		if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {
			FilterKriterium[] fk = LieferscheinFilterFactory.getInstance().createFKPanelQueryFLRAuftragAuswahl(false);
			panelQueryFLRAuftrag = AuftragFilterFactory.getInstance().createPanelFLRAuftrag(getInternalFrame(), true,
					true, fk);
			panelQueryFLRAuftrag.setMultipleRowSelectionEnabled(true);
			panelQueryFLRAuftrag.addButtonAuswahlBestaetigen(null);
			new DialogQuery(panelQueryFLRAuftrag);

		}

		if (e.getActionCommand().equals(ACTION_SPECIAL_FERTIGUNGSGRUPPE_FROM_LISTE)) {
			panelQueryFLRFertigungsgruppe = StuecklisteFilterFactory.getInstance()
					.createPanelFLRFertigungsgruppe(getInternalFrame(), fertigungsgruppeIId, false);
			new DialogQuery(panelQueryFLRFertigungsgruppe);
		}

		// den allgemeine Behandlung.
		super.eventActionSpecial(e);
	}

	public void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLos) {
				Object[] o = panelQueryFLRLos.getSelectedIds();

				String lose = "";

				if (wtfLos.getText() != null) {
					lose = wtfLos.getText();
				}
				for (int i = 0; i < o.length; i++) {
					LosDto losDto = DelegateFactory.getInstance().getFertigungDelegate()
							.losFindByPrimaryKey((Integer) o[i]);
					lose += losDto.getCNr() + ", ";

					losIId.add(losDto.getIId());

				}
				wtfLos.setText(lose);
				if (panelQueryFLRLos != null && panelQueryFLRLos.getDialog() != null) {
					panelQueryFLRLos.getDialog().setVisible(false);
				}

			} else if (e.getSource() == panelQueryFLRAuftrag) {
				Object[] o = panelQueryFLRAuftrag.getSelectedIds();

				String auftraege = "";

				if (wtfAuftrag.getText() != null) {
					auftraege = wtfAuftrag.getText();
				}
				for (int i = 0; i < o.length; i++) {
					AuftragDto losDto = DelegateFactory.getInstance().getAuftragDelegate()
							.auftragFindByPrimaryKey((Integer) o[i]);
					auftraege += losDto.getCNr() + ", ";

					auftragIId.add(losDto.getIId());

				}
				wtfAuftrag.setText(auftraege);
				if (panelQueryFLRAuftrag != null && panelQueryFLRAuftrag.getDialog() != null) {
					panelQueryFLRAuftrag.getDialog().setVisible(false);
				}

			} else if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
				FertigungsgruppeDto fertigungsgruppeDto = DelegateFactory.getInstance().getStuecklisteDelegate()
						.fertigungsgruppeFindByPrimaryKey(key);
				fertigungsgruppeIId = fertigungsgruppeDto.getIId();
				wtfFertigungsgruppe.setText(fertigungsgruppeDto.getCBez());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLos) {
				losIId = new ArrayList<Integer>();
				wtfLos.setText(null);
			} else if (e.getSource() == panelQueryFLRAuftrag) {
				auftragIId = new ArrayList<Integer>();
				wtfAuftrag.setText("");
			} else if (e.getSource() == panelQueryFLRFertigungsgruppe) {
				fertigungsgruppeIId = null;
				wtfFertigungsgruppe.setText("");
			}
		}

	}

}
