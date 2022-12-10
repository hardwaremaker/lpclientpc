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
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerschleissteilDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LospruefplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.inserat.service.InseraterDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class PanelLospruefplan extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private LospruefplanDto lospruefplanDto = null;

	private InternalFrameFertigung internalFrameFertigung = null;

	private WrapperComboBox wcoPruefart = new WrapperComboBox();

	private WrapperButton wbuKontakt = new WrapperButton();
	private WrapperTextField wtfKontakt = new WrapperTextField();

	private WrapperButton wbuLitze = new WrapperButton();
	private WrapperTextField wtfLitze = new WrapperTextField();

	private WrapperButton wbuLitze2 = new WrapperButton();
	private WrapperTextField wtfLitze2 = new WrapperTextField();

	private WrapperCheckBox wcbDoppelanschlag = new WrapperCheckBox();

	private WrapperButton wbuPruefkombination = new WrapperButton();
	private WrapperTextField wtfPruefkombination = new WrapperTextField();

	static final public String ACTION_SPECIAL_PRUEFKOMBINATION_FROM_LISTE = "action_sollmaterial_pruefkombination_from_liste";
	private PanelQueryFLR panelQueryFLRPruefkombination = null;

	static final public String ACTION_SPECIAL_SOLLMATERIAL_KONTAKT_FROM_LISTE = "action_sollmaterial_kontakt_from_liste";
	private PanelQueryFLR panelQueryFLRSollmaterialKontakt = null;

	static final public String ACTION_SPECIAL_SOLLMATERIAL_LITZE_FROM_LISTE = "action_sollmaterial_litze_from_liste";
	private PanelQueryFLR panelQueryFLRSollmaterialLitze = null;

	static final public String ACTION_SPECIAL_SOLLMATERIAL_LITZE2_FROM_LISTE = "action_sollmaterial_litze2_from_liste";
	private PanelQueryFLR panelQueryFLRSollmaterialLitze2 = null;

	private WrapperSelectField wsfWerkzeug = new WrapperSelectField(
			WrapperSelectField.WERKZEUG, getInternalFrame(), false);

	private WrapperLabel wlaAbzugskraft1 = new WrapperLabel();
	private WrapperLabel wlaAbzugskraft2 = new WrapperLabel();

	private WrapperNumberField wnfAbzugskraft1 = new WrapperNumberField();
	private WrapperNumberField wnfAbzugskraft2 = new WrapperNumberField();

	private WrapperButton wbuVerschleissteil = new WrapperButton();
	private WrapperTextField wtfVerschleissteil = new WrapperTextField();

	static final public String ACTION_SPECIAL_VERSCHLEISSTEIL_FROM_LISTE = "action_verschleissteil_litze_from_liste";
	private PanelQueryFLR panelQueryFLRVerschleissteil = null;

	private WrapperLabel wlaCrimphoeheDraht = new WrapperLabel();
	private WrapperLabel wlaCrimphoeheInsolation = new WrapperLabel();
	private WrapperLabel wlaCrimpBreiteDraht = new WrapperLabel();
	private WrapperLabel wlaCrimpBreiteInsolation = new WrapperLabel();

	private WrapperNumberField wnfCrimphoeheDraht = new WrapperNumberField();
	private WrapperNumberField wnfCrimphoeheInsolation = new WrapperNumberField();
	private WrapperNumberField wnfCrimpBreiteDraht = new WrapperNumberField();
	private WrapperNumberField wnfCrimpBreiteInsolation = new WrapperNumberField();

	private WrapperLabel wlaToleranz1 = new WrapperLabel();
	private WrapperLabel wlaToleranz2 = new WrapperLabel();

	private WrapperLabel wlaEinheitWert = new WrapperLabel();
	private WrapperLabel wlaWert = new WrapperLabel();

	private WrapperLabel wlaEinheitMM1 = new WrapperLabel("mm");
	private WrapperLabel wlaEinheitMM2 = new WrapperLabel("mm");
	private WrapperLabel wlaEinheitMM3 = new WrapperLabel("mm");
	private WrapperLabel wlaEinheitMM4 = new WrapperLabel("mm");

	private WrapperNumberField wnfToleranzCrimphoeheDraht = new WrapperNumberField();
	private WrapperNumberField wnfToleranzCrimphoeheInsolation = new WrapperNumberField();
	private WrapperNumberField wnfToleranzCrimpBreiteDraht = new WrapperNumberField();
	private WrapperNumberField wnfToleranzCrimpBreiteInsolation = new WrapperNumberField();

	private WrapperNumberField wnfWert = new WrapperNumberField();
	private WrapperNumberField wnfToleranzWert = new WrapperNumberField();

	public PanelLospruefplan(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameFertigung = (InternalFrameFertigung) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() throws Throwable {
		wcoPruefart.setMandatoryField(true);
		Map<?, ?> m = DelegateFactory.getInstance().getStuecklisteDelegate()
				.getAllPruefart();
		wcoPruefart.setMap(m);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuKontakt;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		lospruefplanDto = new LospruefplanDto();
	}

	protected void dto2Components() throws Throwable {

		wsfWerkzeug.setKey(lospruefplanDto.getWerkzeugIId());

		Integer artikelIIdKontakt = null;

		if (lospruefplanDto.getLossollmaterialIIdKontakt() != null) {

			LossollmaterialDto lossollmaterialDtoKontakt = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.lossollmaterialFindByPrimaryKey(
							lospruefplanDto.getLossollmaterialIIdKontakt());
			ArtikelDto artikelDtoKontakt = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							lossollmaterialDtoKontakt.getArtikelIId());

			artikelIIdKontakt = artikelDtoKontakt.getIId();

			wtfKontakt.setText(artikelDtoKontakt.formatArtikelbezeichnung());
		} else {
			wtfKontakt.setText(null);
		}
		Integer artikelIIdLitze = null;

		if (lospruefplanDto.getLossollmaterialIIdLitze() != null) {

			LossollmaterialDto lossollmaterialDtoLitze = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.lossollmaterialFindByPrimaryKey(
							lospruefplanDto.getLossollmaterialIIdLitze());
			ArtikelDto artikelDtoLitze = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							lossollmaterialDtoLitze.getArtikelIId());

			artikelIIdLitze = artikelDtoLitze.getIId();

			wtfLitze.setText(artikelDtoLitze.formatArtikelbezeichnung());
		} else {
			wtfLitze.setText(null);
		}

		Integer artikelIIdLitze2 = null;

		if (lospruefplanDto.getLossollmaterialIIdLitze2() != null) {

			LossollmaterialDto lossollmaterialDtoLitze = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.lossollmaterialFindByPrimaryKey(
							lospruefplanDto.getLossollmaterialIIdLitze2());
			ArtikelDto artikelDtoLitze = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							lossollmaterialDtoLitze.getArtikelIId());

			artikelIIdLitze2 = artikelDtoLitze.getIId();

			wtfLitze2.setText(artikelDtoLitze.formatArtikelbezeichnung());
		} else {
			wtfLitze2.setText(null);
		}

		if (lospruefplanDto.getVerschleissteilIId() != null) {
			VerschleissteilDto verschleissteilDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.verschleissteilFindByPrimaryKey(
							lospruefplanDto.getVerschleissteilIId());

			wtfVerschleissteil.setText(verschleissteilDto.getBezeichnung());
		} else {
			wtfVerschleissteil.setText(null);
		}

		wtfPruefkombination.setText(null);
		if (lospruefplanDto.getPruefkombinationId() != null) {

			PruefkombinationDto pruefkombinationDto = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.pruefkombinationFindByPrimaryKey(
							lospruefplanDto.getPruefkombinationId());
			if (pruefkombinationDto.getPruefkombinationsprDto() != null
					&& pruefkombinationDto.getPruefkombinationsprDto()
							.getCBez() != null) {

				wtfPruefkombination.setText(pruefkombinationDto
						.getPruefkombinationsprDto().getCBez());
			} else {
				wtfPruefkombination.setText("SELEKTIERT");
			}

		}

		wcbDoppelanschlag.setShort(lospruefplanDto.getBDoppelanschlag());

		wcoPruefart.setKeyOfSelectedItem(lospruefplanDto.getPruefartIId());

		setStatusbarPersonalIIdAendern(lospruefplanDto.getPersonalIIdAendern());
		setStatusbarPersonalIIdAnlegen(lospruefplanDto.getPersonalIIdAnlegen());

		setStatusbarTAendern(lospruefplanDto.getTAendern());
		setStatusbarTAnlegen(lospruefplanDto.getTAnlegen());

		// Werte

		Integer pruefkombinationIId = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.pruefeObPruefplanInPruefkombinationVorhanden(null,
						lospruefplanDto.getPruefartIId(), artikelIIdKontakt,
						artikelIIdLitze, artikelIIdLitze2,
						lospruefplanDto.getVerschleissteilIId(),
						lospruefplanDto.getPruefkombinationId());
		if (pruefkombinationIId != null) {

			PruefkombinationDto pkDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.pruefkombinationFindByPrimaryKey(pruefkombinationIId);

			wnfCrimpBreiteDraht.setBigDecimal(pkDto.getNCrimpbreitDraht());
			wnfCrimpBreiteInsolation.setBigDecimal(pkDto
					.getNCrimpbreiteIsolation());
			wnfCrimphoeheDraht.setBigDecimal(pkDto.getNCrimphoeheDraht());
			wnfCrimphoeheInsolation.setBigDecimal(pkDto
					.getNCrimphoeheIsolation());

			wnfToleranzCrimpBreiteDraht.setBigDecimal(pkDto
					.getNToleranzCrimpbreitDraht());
			wnfToleranzCrimpBreiteInsolation.setBigDecimal(pkDto
					.getNToleranzCrimpbreiteIsolation());
			wnfToleranzCrimphoeheDraht.setBigDecimal(pkDto
					.getNToleranzCrimphoeheDraht());
			wnfToleranzCrimphoeheInsolation.setBigDecimal(pkDto
					.getNToleranzCrimphoeheIsolation());

			wnfWert.setBigDecimal(pkDto.getNWert());
			wnfToleranzWert.setBigDecimal(pkDto.getNToleranzWert());

			wnfAbzugskraft1.setBigDecimal(pkDto.getNAbzugskraftLitze());
			wnfAbzugskraft2.setBigDecimal(pkDto.getNAbzugskraftLitze2());

		} else {
			wnfCrimpBreiteDraht.setBigDecimal(null);
			wnfCrimpBreiteInsolation.setBigDecimal(null);
			wnfCrimphoeheDraht.setBigDecimal(null);
			wnfCrimphoeheInsolation.setBigDecimal(null);

			wnfToleranzCrimpBreiteDraht.setBigDecimal(null);
			wnfToleranzCrimpBreiteInsolation.setBigDecimal(null);
			wnfToleranzCrimphoeheDraht.setBigDecimal(null);
			wnfToleranzCrimphoeheInsolation.setBigDecimal(null);

			wnfWert.setBigDecimal(null);
			wnfToleranzWert.setBigDecimal(null);

		}

	}

	void dialogQueryPruefkombinationFromListe() throws Throwable {

		String sPruefart = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.pruefartFindByPrimaryKey(
						(Integer) wcoPruefart.getKeyOfSelectedItem()).getCNr();

		FilterKriterium[] krit = new FilterKriterium[2];

		FilterKriterium krit1 = new FilterKriterium("flrpruefart.i_id", true,
				(Integer) wcoPruefart.getKeyOfSelectedItem() + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		krit[0] = krit1;

		if (sPruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {
			FilterKriterium krit2 = new FilterKriterium("artikel_i_id_litze",
					true, "", FilterKriterium.OPERATOR_IS + " "
							+ FilterKriterium.OPERATOR_NULL, false);
			krit[1] = krit2;
		} else {
			FilterKriterium krit2 = new FilterKriterium("artikel_i_id_kontakt",
					true, "", FilterKriterium.OPERATOR_IS + " "
							+ FilterKriterium.OPERATOR_NULL, false);
			krit[1] = krit2;
		}

		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRPruefkombination = new PanelQueryFLR(null, krit,
				QueryParameters.UC_ID_PRUEFKOMBINATION, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("stk.pruefkombination"));

		new DialogQuery(panelQueryFLRPruefkombination);

	}

	void dialogQuerySollmaterialKontaktFromListe() throws Throwable {

		panelQueryFLRSollmaterialKontakt = FertigungFilterFactory.getInstance()
				.createPanelFLRLossollmaterial(
						getInternalFrame(),
						internalFrameFertigung.getTabbedPaneLos().getLosDto()
								.getIId(),
						lospruefplanDto.getLossollmaterialIIdKontakt(), false);
		new DialogQuery(panelQueryFLRSollmaterialKontakt);

	}

	void dialogQueryVerschleissteilFromListe() throws Throwable {

		String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };

		FilterKriterium[] kriterien = null;

		Integer artikelIIdKontakt = null;
		Integer artikelIIdLitze = null;
		Integer artikelIIdLitze2 = null;

		if (lospruefplanDto.getLossollmaterialIIdKontakt() != null) {
			LossollmaterialDto lossollmaterialDto = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.lossollmaterialFindByPrimaryKey(
							lospruefplanDto.getLossollmaterialIIdKontakt());

			artikelIIdKontakt = lossollmaterialDto.getArtikelIId();
		}

		if (lospruefplanDto.getLossollmaterialIIdLitze() != null) {
			LossollmaterialDto lossollmaterialDto = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.lossollmaterialFindByPrimaryKey(
							lospruefplanDto.getLossollmaterialIIdLitze());
			artikelIIdLitze = lossollmaterialDto.getArtikelIId();
		}
		if (lospruefplanDto.getLossollmaterialIIdLitze2() != null) {
			LossollmaterialDto lossollmaterialDto = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.lossollmaterialFindByPrimaryKey(
							lospruefplanDto.getLossollmaterialIIdLitze2());
			artikelIIdLitze2 = lossollmaterialDto.getArtikelIId();
		}

		ArrayList<Integer> iids = DelegateFactory
				.getInstance()
				.getStuecklisteDelegate()
				.getVorgeschlageneVerschleissteile(artikelIIdKontakt,
						artikelIIdLitze, artikelIIdLitze2,
						wcbDoppelanschlag.isSelected());

		kriterien = new FilterKriterium[1];

		String in = "(-99)";

		if (iids != null && iids.size() > 0) {
			in = "(";

			Iterator it = iids.iterator();
			while (it.hasNext()) {
				in += it.next();

				if (it.hasNext()) {
					in += ",";
				}

			}
			in += ")";
		}

		kriterien[0] = new FilterKriterium("i_id", true, in,
				FilterKriterium.OPERATOR_IN, false);

		panelQueryFLRVerschleissteil = new PanelQueryFLR(null, kriterien,
				QueryParameters.UC_ID_VERSCHLEISSTEIL, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("artikel.werkzeug.verschleissteil"));

		panelQueryFLRVerschleissteil.befuellePanelFilterkriterienDirekt(
				SystemFilterFactory.getInstance().createFKDKennung(), null);

		panelQueryFLRVerschleissteil.setSelectedId(lospruefplanDto
				.getVerschleissteilIId());

		new DialogQuery(panelQueryFLRVerschleissteil);

	}

	void dialogQuerySollmaterialLitzeFromListe() throws Throwable {

		panelQueryFLRSollmaterialLitze = FertigungFilterFactory.getInstance()
				.createPanelFLRLossollmaterial(
						getInternalFrame(),
						internalFrameFertigung.getTabbedPaneLos().getLosDto()
								.getIId(),
						lospruefplanDto.getLossollmaterialIIdLitze(), false);
		new DialogQuery(panelQueryFLRSollmaterialLitze);

	}

	void dialogQuerySollmaterialLitze2FromListe() throws Throwable {

		panelQueryFLRSollmaterialLitze2 = FertigungFilterFactory.getInstance()
				.createPanelFLRLossollmaterial(
						getInternalFrame(),
						internalFrameFertigung.getTabbedPaneLos().getLosDto()
								.getIId(),
						lospruefplanDto.getLossollmaterialIIdLitze2(), false);
		new DialogQuery(panelQueryFLRSollmaterialLitze2);

	}

	protected void components2Dto() throws Throwable {

		lospruefplanDto.setWerkzeugIId(wsfWerkzeug.getIKey());
		lospruefplanDto.setLosIId(internalFrameFertigung.getTabbedPaneLos()
				.getLosDto().getIId());

		lospruefplanDto.setPruefartIId((Integer) wcoPruefart
				.getKeyOfSelectedItem());

		lospruefplanDto.setBDoppelanschlag(wcbDoppelanschlag.getShort());
		
		if(wcbDoppelanschlag.isSelected()==false){
			lospruefplanDto.setLossollmaterialIIdLitze2(null);
		}
		

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRSollmaterialKontakt) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				LossollmaterialDto lossollmaterialDto = DelegateFactory
						.getInstance().getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(key);

				lospruefplanDto.setLossollmaterialIIdKontakt(key);

				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								lossollmaterialDto.getArtikelIId());
				wtfKontakt.setText(artikelDto.formatArtikelbezeichnung());
			} else if (e.getSource() == panelQueryFLRSollmaterialLitze) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				LossollmaterialDto lossollmaterialDto = DelegateFactory
						.getInstance().getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(key);

				lospruefplanDto.setLossollmaterialIIdLitze(key);

				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								lossollmaterialDto.getArtikelIId());
				wtfLitze.setText(artikelDto.formatArtikelbezeichnung());
			} else if (e.getSource() == panelQueryFLRSollmaterialLitze2) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				LossollmaterialDto lossollmaterialDto = DelegateFactory
						.getInstance().getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(key);

				lospruefplanDto.setLossollmaterialIIdLitze2(key);

				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								lossollmaterialDto.getArtikelIId());
				wtfLitze2.setText(artikelDto.formatArtikelbezeichnung());
			} else if (e.getSource() == panelQueryFLRVerschleissteil) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				VerschleissteilDto verschleissteilDto = DelegateFactory
						.getInstance().getArtikelDelegate()
						.verschleissteilFindByPrimaryKey(key);

				lospruefplanDto.setVerschleissteilIId(verschleissteilDto
						.getIId());

				wtfVerschleissteil.setText(verschleissteilDto.getBezeichnung());
			} else if (e.getSource() == panelQueryFLRPruefkombination) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				wtfPruefkombination.setText(null);

				PruefkombinationDto pruefkombinationDto = DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.pruefkombinationFindByPrimaryKey(key);

				lospruefplanDto.setPruefkombinationId(key);

				if (pruefkombinationDto.getPruefkombinationsprDto() != null
						&& pruefkombinationDto.getPruefkombinationsprDto()
								.getCBez() != null) {
					wtfPruefkombination.setText(pruefkombinationDto
							.getPruefkombinationsprDto().getCBez());
				} else {
					wtfPruefkombination.setText("SELEKTIERT");
				}

				lospruefplanDto.setLossollmaterialIIdKontakt(null);
				lospruefplanDto.setLossollmaterialIIdLitze(null);
				lospruefplanDto.setLossollmaterialIIdLitze2(null);
				wtfKontakt.setText(null);
				wtfLitze.setText(null);
				wtfLitze2.setText(null);

			}
		}

		if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRPruefkombination) {
				lospruefplanDto.setPruefkombinationId(null);
				wtfPruefkombination.setText(null);
			}
		}

	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wtfKontakt.setMandatoryField(true);
		wtfLitze.setMandatoryField(true);
		// wsfWerkzeug.setMandatoryField(true);

		wbuKontakt.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.kontakt") + "...");
		wbuLitze.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.litze") + "...");

		wbuPruefkombination.setText(LPMain
				.getTextRespectUISPr("stk.oderpruefkombination") + "...");

		wbuLitze2.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.litze") + "2...");

		wbuLitze2.addActionListener(this);
		wbuLitze2
				.setActionCommand(ACTION_SPECIAL_SOLLMATERIAL_LITZE2_FROM_LISTE);

		wlaAbzugskraft1
				.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.abzugskraft")
						+ " 1");
		wlaAbzugskraft2
				.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.abzugskraft")
						+ " 2");

		wbuPruefkombination.addActionListener(this);
		wbuPruefkombination
				.setActionCommand(ACTION_SPECIAL_PRUEFKOMBINATION_FROM_LISTE);

		wcbDoppelanschlag.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.doppelanschlag"));
		wcbDoppelanschlag.addActionListener(this);

		wlaCrimpBreiteDraht.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.crimpbreitedraht"));
		wlaCrimphoeheDraht.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.crimphoehedraht"));
		wlaCrimphoeheInsolation
				.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.crimphoeheisolation"));
		wlaCrimpBreiteInsolation
				.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.crimpbreiteisolation"));

		wlaToleranz1.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.toleranz"));
		wlaToleranz2.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.toleranz"));

		wlaWert.setText(LPMain.getTextRespectUISPr("stk.pruefkombination.wert"));

		wcoPruefart
				.addActionListener(new PanelLospruefplan_wcbPruefart_actionAdapter(
						this));

		wlaEinheitWert.setHorizontalAlignment(SwingConstants.LEFT);

		wnfCrimpBreiteDraht.setActivatable(false);
		wnfCrimphoeheDraht.setActivatable(false);
		wnfCrimpBreiteInsolation.setActivatable(false);
		wnfCrimphoeheInsolation.setActivatable(false);

		wnfToleranzCrimpBreiteDraht.setActivatable(false);
		wnfToleranzCrimphoeheDraht.setActivatable(false);
		wnfToleranzCrimpBreiteInsolation.setActivatable(false);
		wnfToleranzCrimphoeheInsolation.setActivatable(false);

		wnfAbzugskraft1.setActivatable(false);
		wnfAbzugskraft2.setActivatable(false);

		wnfWert.setActivatable(false);
		wnfToleranzWert.setActivatable(false);

		wtfKontakt.setActivatable(false);
		wtfKontakt.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfLitze.setActivatable(false);
		wtfLitze.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuKontakt.addActionListener(this);
		wbuKontakt
				.setActionCommand(ACTION_SPECIAL_SOLLMATERIAL_KONTAKT_FROM_LISTE);
		wbuLitze.addActionListener(this);
		wbuLitze.setActionCommand(ACTION_SPECIAL_SOLLMATERIAL_LITZE_FROM_LISTE);

		wbuVerschleissteil.setText(LPMain
				.getTextRespectUISPr("artikel.werkzeug.verschleissteil")
				+ "...");

		wbuVerschleissteil.addActionListener(this);
		wbuVerschleissteil
				.setActionCommand(ACTION_SPECIAL_VERSCHLEISSTEIL_FROM_LISTE);

		wtfVerschleissteil.setActivatable(false);
		wtfVerschleissteil.setMandatoryField(true);

		getInternalFrame().addItemChangedListener(this);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("stkl.pruefart")),
				new GridBagConstraints(0, iZeile, 1, 1, 0.05, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wcoPruefart, new GridBagConstraints(1, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuKontakt, new GridBagConstraints(0, iZeile, 1, 1,
				0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontakt, new GridBagConstraints(1, iZeile, 5, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuLitze, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLitze, new GridBagConstraints(1, iZeile, 5, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLitze2, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLitze2, new GridBagConstraints(1, iZeile, 5, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuPruefkombination, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPruefkombination, new GridBagConstraints(1, iZeile,
				5, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuVerschleissteil, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVerschleissteil, new GridBagConstraints(1, iZeile,
				5, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wsfWerkzeug.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wsfWerkzeug.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 5, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbDoppelanschlag, new GridBagConstraints(0, iZeile,
				2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaToleranz1, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaToleranz2, new GridBagConstraints(6, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaCrimphoeheDraht, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wnfCrimphoeheDraht, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfToleranzCrimphoeheDraht, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wlaEinheitMM1, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wlaCrimphoeheInsolation, new GridBagConstraints(4,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfCrimphoeheInsolation, new GridBagConstraints(5,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfToleranzCrimphoeheInsolation,
				new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						50, 0));
		jpaWorkingOn.add(wlaEinheitMM2, new GridBagConstraints(7, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 30, 0));
		iZeile++;
		jpaWorkingOn.add(wlaCrimpBreiteDraht, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wnfCrimpBreiteDraht, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfToleranzCrimpBreiteDraht, new GridBagConstraints(2,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEinheitMM3, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wlaCrimpBreiteInsolation, new GridBagConstraints(4,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfCrimpBreiteInsolation, new GridBagConstraints(5,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfToleranzCrimpBreiteInsolation,
				new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wlaEinheitMM4, new GridBagConstraints(7, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 30, 0));

		iZeile++;
		jpaWorkingOn.add(wlaWert, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWert, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wnfToleranzWert, new GridBagConstraints(2, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(wlaEinheitWert, new GridBagConstraints(3, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 30, 0));

		iZeile++;
		jpaWorkingOn.add(wlaAbzugskraft1, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(wnfAbzugskraft1, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wlaAbzugskraft2, new GridBagConstraints(4, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfAbzugskraft2, new GridBagConstraints(5, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LOS;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_SOLLMATERIAL_KONTAKT_FROM_LISTE)) {
			dialogQuerySollmaterialKontaktFromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_SOLLMATERIAL_LITZE_FROM_LISTE)) {
			dialogQuerySollmaterialLitzeFromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_SOLLMATERIAL_LITZE2_FROM_LISTE)) {
			dialogQuerySollmaterialLitze2FromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_VERSCHLEISSTEIL_FROM_LISTE)) {

			dialogQueryVerschleissteilFromListe();

		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PRUEFKOMBINATION_FROM_LISTE)) {
			dialogQueryPruefkombinationFromListe();
		}

		if (e.getSource().equals(wcbDoppelanschlag)) {
			if (wcbDoppelanschlag.isSelected()) {
				wtfLitze2.setVisible(true);
				wbuLitze2.setVisible(true);
				wtfLitze2.setMandatoryField(true);
				wtfLitze2.setEditable(false);

			} else {
				wtfLitze2.setVisible(false);
				wbuLitze2.setVisible(false);
				wtfLitze2.setMandatoryField(false);

			}
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable, Throwable {
		DelegateFactory.getInstance().getFertigungServiceDelegate()
				.removeLospruefplan(lospruefplanDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();

			if (wcbDoppelanschlag.isSelected()) {
				wtfLitze2.setVisible(true);
				wbuLitze2.setVisible(true);
				wtfLitze2.setMandatoryField(true);
				wtfLitze2.setEditable(false);

			} else {
				wtfLitze2.setVisible(false);
				wbuLitze2.setVisible(false);
				wtfLitze2.setMandatoryField(false);

			}

		} else {
			lospruefplanDto = DelegateFactory.getInstance()
					.getFertigungServiceDelegate()
					.lospruefplanFindByPrimaryKey((Integer) key);
			dto2Components();
		}

	}

	public void wcbPruefart_actionPerformed(ActionEvent e) {
		Integer pruefartIId = (Integer) wcoPruefart.getKeyOfSelectedItem();

		try {

			// Zuerst alle unsichtbar und nicht Mandantory

			wbuKontakt.setText(LPMain
					.getTextRespectUISPr("stk.pruefkombination.kontakt")
					+ "...");
			wtfKontakt.setMandatoryField(false);
			wbuKontakt.setVisible(false);
			wtfKontakt.setVisible(false);

			wcbDoppelanschlag.setVisible(false);

			wtfLitze.setMandatoryField(false);
			wbuLitze.setVisible(false);
			wtfLitze.setVisible(false);

			wlaAbzugskraft1.setVisible(false);
			wnfAbzugskraft1.setVisible(false);

			wlaAbzugskraft2.setVisible(false);
			wnfAbzugskraft2.setVisible(false);

			wtfLitze2.setMandatoryField(false);
			wbuLitze2.setVisible(false);
			wtfLitze2.setVisible(false);

			wbuPruefkombination.setVisible(false);
			wtfPruefkombination.setVisible(false);

			wsfWerkzeug.getWrapperButton().setVisible(false);
			wsfWerkzeug.getWrapperTextField().setVisible(false);
			// wsfWerkzeug.setMandatoryField(false);

			wbuVerschleissteil.setVisible(false);
			wtfVerschleissteil.setVisible(false);
			wtfVerschleissteil.setMandatoryField(false);

			wlaEinheitMM1.setVisible(false);
			wlaEinheitMM2.setVisible(false);
			wlaEinheitMM3.setVisible(false);
			wlaEinheitMM4.setVisible(false);

			wnfCrimpBreiteDraht.setVisible(false);
			wnfCrimpBreiteInsolation.setVisible(false);
			wnfCrimphoeheDraht.setVisible(false);
			wnfCrimphoeheInsolation.setVisible(false);

			wnfToleranzCrimpBreiteDraht.setVisible(false);
			wnfToleranzCrimpBreiteInsolation.setVisible(false);
			wnfToleranzCrimphoeheDraht.setVisible(false);
			wnfToleranzCrimphoeheInsolation.setVisible(false);

			wlaCrimpBreiteDraht.setVisible(false);
			wlaCrimpBreiteInsolation.setVisible(false);
			wlaCrimphoeheDraht.setVisible(false);
			wlaCrimphoeheInsolation.setVisible(false);

			wlaWert.setVisible(false);
			wnfWert.setVisible(false);

			wnfToleranzWert.setVisible(false);
			wlaEinheitWert.setVisible(false);

			wlaToleranz1.setVisible(false);
			wlaToleranz2.setVisible(false);

			String sPruefart = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.pruefartFindByPrimaryKey(pruefartIId).getCNr();

			if (sPruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
					|| sPruefart
							.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {
				wtfKontakt.setMandatoryField(true);
				wbuKontakt.setVisible(true);
				wtfKontakt.setVisible(true);

				wtfLitze.setMandatoryField(true);
				wbuLitze.setVisible(true);
				wtfLitze.setVisible(true);

				wsfWerkzeug.getWrapperButton().setVisible(true);
				wsfWerkzeug.getWrapperTextField().setVisible(true);
				// wsfWerkzeug.setMandatoryField(true);

				wbuVerschleissteil.setVisible(true);
				wtfVerschleissteil.setVisible(true);
				wtfVerschleissteil.setMandatoryField(true);

				wcbDoppelanschlag.setVisible(true);
				wtfLitze2.setMandatoryField(true);
				wbuLitze2.setVisible(true);
				wtfLitze2.setVisible(true);

				wtfLitze2.setVisible(wcbDoppelanschlag.isSelected());
				wbuLitze2.setVisible(wcbDoppelanschlag.isSelected());
				wtfLitze2.setMandatoryField(wcbDoppelanschlag.isSelected());

				wlaAbzugskraft1.setVisible(true);
				wlaAbzugskraft2.setVisible(true);
				wnfAbzugskraft1.setVisible(true);
				wnfAbzugskraft2.setVisible(true);

			}

			if (sPruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)) {
				wlaEinheitMM1.setVisible(true);
				wlaEinheitMM2.setVisible(true);
				wlaEinheitMM3.setVisible(true);
				wlaEinheitMM4.setVisible(true);

				wnfCrimpBreiteDraht.setVisible(true);
				wnfCrimpBreiteInsolation.setVisible(true);
				wnfCrimphoeheDraht.setVisible(true);
				wnfCrimphoeheInsolation.setVisible(true);

				wnfToleranzCrimpBreiteDraht.setVisible(true);
				wnfToleranzCrimpBreiteInsolation.setVisible(true);
				wnfToleranzCrimphoeheDraht.setVisible(true);
				wnfToleranzCrimphoeheInsolation.setVisible(true);

				wlaCrimpBreiteDraht.setVisible(true);
				wlaCrimpBreiteInsolation.setVisible(true);
				wlaCrimphoeheDraht.setVisible(true);
				wlaCrimphoeheInsolation.setVisible(true);

				wlaToleranz1.setVisible(true);
				wlaToleranz2.setVisible(true);
			} else if (sPruefart
					.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {
				wlaEinheitMM1.setVisible(true);
				wlaEinheitMM3.setVisible(true);

				wnfCrimpBreiteDraht.setVisible(true);

				wnfCrimphoeheDraht.setVisible(true);

				wnfToleranzCrimpBreiteDraht.setVisible(true);

				wnfToleranzCrimphoeheDraht.setVisible(true);

				wlaCrimpBreiteDraht.setVisible(true);

				wlaCrimphoeheDraht.setVisible(true);

				wlaToleranz1.setVisible(true);

			} else if (sPruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)
					|| sPruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {

				if (sPruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {

					// wtfLitze.setMandatoryField(true);
					wbuLitze.setVisible(true);
					wtfLitze.setVisible(true);

				} else {
					wtfKontakt.setMandatoryField(true);
					wbuKontakt.setVisible(true);
					wtfKontakt.setVisible(true);

					wtfLitze.setMandatoryField(true);
					wbuLitze.setVisible(true);
					wtfLitze.setVisible(true);
				}

				wnfToleranzWert.setVisible(true);
				wlaEinheitWert.setVisible(true);
				wlaToleranz1.setVisible(true);

				if (sPruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {
					wlaEinheitWert.setText("mm");
				} else {

					wlaWert.setVisible(true);
					wnfWert.setVisible(true);
					wlaEinheitWert.setText("N");

				}

			} else if (sPruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {
				wtfKontakt.setMandatoryField(false);
				wbuKontakt.setVisible(false);
				wtfKontakt.setVisible(false);

				wtfLitze.setMandatoryField(false);
				wbuLitze.setVisible(false);
				wtfLitze.setVisible(false);
				wbuPruefkombination.setVisible(true);
				wtfPruefkombination.setVisible(true);

			} else if (sPruefart
					.equals(StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG)
					|| sPruefart
							.equals(StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG)) {
				// wtfKontakt.setMandatoryField(true);
				wbuKontakt.setVisible(true);
				wtfKontakt.setVisible(true);

				wbuKontakt.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.artikel")
						+ "...");

				wbuPruefkombination.setVisible(true);
				wtfPruefkombination.setVisible(true);

			}
		} catch (Throwable e1) {
			handleException(e1, true);
		}

	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (internalFrameFertigung.getTabbedPaneLos().getLosDto() != null) {
			String status = DelegateFactory
					.getInstance()
					.getFertigungDelegate()
					.losFindByPrimaryKey(
							internalFrameFertigung.getTabbedPaneLos()
									.getLosDto().getIId()).getStatusCNr();
			if (status.equals(FertigungFac.STATUS_AUSGEGEBEN)
					|| status.equals(FertigungFac.STATUS_IN_PRODUKTION)
					|| status.equals(FertigungFac.STATUS_TEILERLEDIGT)
					|| status.equals(FertigungFac.STATUS_ERLEDIGT)) {

				lockStateValue = new LockStateValue(
						PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);

			}
		}

		return lockStateValue;
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (lospruefplanDto.getIId() == null) {

				lospruefplanDto.setIId(DelegateFactory.getInstance()
						.getFertigungServiceDelegate()
						.createLospruefplan(lospruefplanDto));
				setKeyWhenDetailPanel(lospruefplanDto.getIId());
			} else {
				DelegateFactory.getInstance().getFertigungServiceDelegate()
						.updateLospruefplan(lospruefplanDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						lospruefplanDto.getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

	class PanelLospruefplan_wcbPruefart_actionAdapter implements ActionListener {
		private PanelLospruefplan adaptee;

		PanelLospruefplan_wcbPruefart_actionAdapter(PanelLospruefplan adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.wcbPruefart_actionPerformed(e);
		}
	}

}
