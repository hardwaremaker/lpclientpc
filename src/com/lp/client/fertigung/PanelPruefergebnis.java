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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerschleissteilDto;
import com.lp.server.artikel.service.WerkzeugDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LospruefplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.PruefergebnisDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.util.Helper;

public class PanelPruefergebnis extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel panelButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	ButtonGroup buttonGroup1 = new ButtonGroup();
	Border border1 = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	private TabbedPaneLos tabbedPaneLos = null;

	private Integer losablieferungIId = null;

	private ArrayList<LospruefplanDto> alPruefplan = null;

	private HashMap<Integer, WrapperNumberField> hmCrimphoehe = new HashMap();
	private HashMap<Integer, WrapperNumberField> hmCrimphoeheIsolation = new HashMap();
	private HashMap<Integer, WrapperNumberField> hmCrimpbreite = new HashMap();
	private HashMap<Integer, WrapperNumberField> hmCrimpbreiteIsolation = new HashMap();

	private HashMap<Integer, WrapperNumberField> hmAbzugskraft = new HashMap();
	private HashMap<Integer, WrapperNumberField> hmAbzugskraft2 = new HashMap();

	private HashMap<Integer, WrapperNumberField> hmNWert = new HashMap();
	private HashMap<Integer, ButtonGroup> hmBWert = new HashMap();
	private Map<Integer, WrapperTextField> hmCWert = new HashMap<Integer, WrapperTextField>();

	private JScrollPane jspScrollPane = new JScrollPane();

	public PanelPruefergebnis(InternalFrame internalFrame, String add2TitleI,
			TabbedPaneLos tabbedPaneLos, Integer losablieferungIId)
			throws Throwable {

		super(internalFrame, add2TitleI, null);

		setKeyWhenDetailPanel(losablieferungIId);
		this.tabbedPaneLos = tabbedPaneLos;
		this.losablieferungIId = losablieferungIId;

		jbInit();
		setDefaults();
		initComponents();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return NO_VALUE_THATS_OK_JCOMPONENT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		leereAlleFelder(this);

	}

	private void jbInit() throws Throwable {

		alPruefplan = DelegateFactory.getInstance()
				.getFertigungServiceDelegate()
				.lospruefplanFindyByLosIId(tabbedPaneLos.getLosDto().getIId());

		ArrayList<PruefergebnisDto> alPruefergebnis = DelegateFactory
				.getInstance().getFertigungServiceDelegate()
				.pruefergebnisFindByLosablieferungIId(losablieferungIId);

		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder

		JPanel jpaWorkingOn = new JPanel(new GridBagLayout());

		MessageFormat mf = new MessageFormat(
				LPMain.getTextRespectUISPr("fert.ablieferung.pruefergebnis.title"));
		mf.setLocale(LPMain.getTheClient().getLocUi());

		LosablieferungDto laDto = DelegateFactory.getInstance()
				.getFertigungDelegate()
				.losablieferungFindByPrimaryKey(losablieferungIId, false);

		Object pattern[] = new Object[] {
				tabbedPaneLos.getLosDto().getCNr(),
				Helper.formatZahl(laDto.getNMenge(), 0, LPMain.getTheClient()
						.getLocUi()),
				Helper.formatDatumZeit(laDto.getTAendern(), LPMain
						.getTheClient().getLocUi()) };

		iZeile++;
		jpaWorkingOn.add(new JLabel("<html><b><u>" + mf.format(pattern)
				+ "</u></b></html>"), new GridBagConstraints(0, iZeile, 3, 1,
				1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 20, 2), 80, 0));

		iZeile++;
		jpaWorkingOn.add(
				new JLabel(LPMain.getTextRespectUISPr("stkl.pruefart")),
				new GridBagConstraints(0, iZeile, 1, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						80, 0));
		jpaWorkingOn
				.add(new JLabel(
						LPMain.getTextRespectUISPr("stk.pruefergebins.kontaktartikel")),
						new GridBagConstraints(1, iZeile, 1, 1, 1, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 100, 0));
		jpaWorkingOn.add(
				new JLabel(LPMain
						.getTextRespectUISPr("stk.pruefkombination.litze")),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						100, 0));

		jpaWorkingOn
				.add(new JLabel(
						LPMain.getTextRespectUISPr("los.pruefplan.werkzeugverschleissteil")),
						new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));
		jpaWorkingOn
				.add(new JLabel(
						LPMain.getTextRespectUISPr("stk.pruefkombination.crimphoehedraht")
								+ " (mm)"), new GridBagConstraints(4, iZeile,
						1, 1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn
				.add(new JLabel(
						LPMain.getTextRespectUISPr("stk.pruefkombination.crimphoeheisolation")
								+ " (mm)"), new GridBagConstraints(5, iZeile,
						1, 1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn
				.add(new JLabel(
						LPMain.getTextRespectUISPr("stk.pruefkombination.crimpbreitedraht")
								+ " (mm)"), new GridBagConstraints(6, iZeile,
						1, 1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn
				.add(new JLabel(
						LPMain.getTextRespectUISPr("stk.pruefkombination.crimpbreiteisolation")
								+ " (mm)"), new GridBagConstraints(7, iZeile,
						1, 1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(
				new JLabel(LPMain
						.getTextRespectUISPr("stk.pruefkombination.wert")
						+ " (N/mm)"), new GridBagConstraints(8, iZeile, 1, 1,
						1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn
				.add(new JLabel(
						LPMain.getTextRespectUISPr("stk.pruefkombination.abzugskraft")
								+ " (N)"), new GridBagConstraints(9, iZeile, 1,
						1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn
				.add(new JLabel(
						LPMain.getTextRespectUISPr("stk.pruefkombination.abzugskraft")
								+ " 2" + " (N)"), new GridBagConstraints(10,
						iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn
				.add(new JLabel(
						LPMain.getTextRespectUISPr("stk.pruefkombination.durchgefuehrt")),
						new GridBagConstraints(11, iZeile, 1, 1, 1, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));

		jpaWorkingOn
				.add(new JLabel(
						LPMain.getTextRespectUISPr("stk.pruefergebnis.wert.text")),
						new GridBagConstraints(12, iZeile, 1, 1, 1, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 80, 0));

		for (LospruefplanDto dto : alPruefplan) {
			iZeile++;

			String kontakt = "";
			Integer artikelIIdKontakt = null;

			if (dto.getLossollmaterialIIdKontakt() != null) {

				LossollmaterialDto sollMatDtoKontakt = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(
								dto.getLossollmaterialIIdKontakt());
				ArtikelDto aDtoKontakt = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								sollMatDtoKontakt.getArtikelIId());

				artikelIIdKontakt = aDtoKontakt.getIId();

				kontakt = "<html><b>" + aDtoKontakt.getCNr() + "</b> "
						+ aDtoKontakt.formatBezeichnung() + "</html>";

			}

			String pruefart = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.pruefartFindByPrimaryKey(dto.getPruefartIId()).getCNr();

			jpaWorkingOn.add(new JLabel(pruefart),
					new GridBagConstraints(0, iZeile, 1, 1, 1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			jpaWorkingOn.add(new JLabel(kontakt),
					new GridBagConstraints(1, iZeile, 1, 1, 1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			String litze = "";
			Integer artikelIIdLitze = null;
			if (dto.getLossollmaterialIIdLitze() != null) {

				LossollmaterialDto sollMatDtoLitze = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(
								dto.getLossollmaterialIIdLitze());
				ArtikelDto aDtoLitze = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								sollMatDtoLitze.getArtikelIId());
				artikelIIdLitze = aDtoLitze.getIId();

				litze = "<html><b>" + aDtoLitze.getCNr() + "</b> "
						+ aDtoLitze.formatBezeichnung() + "</html>";

			}

			jpaWorkingOn.add(new JLabel(litze),
					new GridBagConstraints(2, iZeile, 1, 1, 1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			String litze2 = "";
			Integer artikelIIdLitze2 = null;
			if (dto.getLossollmaterialIIdLitze2() != null) {

				LossollmaterialDto sollMatDtoLitze = DelegateFactory
						.getInstance()
						.getFertigungDelegate()
						.lossollmaterialFindByPrimaryKey(
								dto.getLossollmaterialIIdLitze());
				ArtikelDto aDtoLitze = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								sollMatDtoLitze.getArtikelIId());
				artikelIIdLitze2 = aDtoLitze.getIId();

				litze2 = "<html><b>" + aDtoLitze.getCNr() + "</b> "
						+ aDtoLitze.formatBezeichnung() + "</html>";

			}

			jpaWorkingOn.add(new JLabel(litze2),
					new GridBagConstraints(2, iZeile, 1, 1, 1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			String werkzeug = "";
			Integer werkzeugIId = null;
			if (dto.getWerkzeugIId() != null) {

				WerkzeugDto wDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.werkzeugFindByPrimaryKey(dto.getWerkzeugIId());

				VerschleissteilDto vDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.verschleissteilFindByPrimaryKey(
								dto.getVerschleissteilIId());

				werkzeug = wDto.getBezeichnung() + " (" + vDto.getBezeichnung()
						+ ")";

			}
			jpaWorkingOn.add(new JLabel(werkzeug),
					new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			Integer pruefkombinationIId = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.pruefeObPruefplanInPruefkombinationVorhanden(null,
							dto.getPruefartIId(), artikelIIdKontakt,
							artikelIIdLitze, artikelIIdLitze2,
							dto.getVerschleissteilIId(),
							dto.getPruefkombinationId());
			PruefkombinationDto pkDto = null;
			if (pruefkombinationIId != null) {
				pkDto = DelegateFactory.getInstance().getStuecklisteDelegate()
						.pruefkombinationFindByPrimaryKey(pruefkombinationIId);
			}

			if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
					|| pruefart
							.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {

				// SollWerte
				JLabel sollCrimphoeheDraht = new JLabel();
				JLabel sollCrimphoeheIsolation = new JLabel();
				JLabel sollCrimpbreiteDraht = new JLabel();
				JLabel sollCrimpbreiteIsolation = new JLabel();

				if (pkDto != null) {

					sollCrimphoeheDraht.setText(Helper.formatZahl(pkDto
							.getNCrimphoeheDraht(), 2, LPMain.getTheClient()
							.getLocUi()));

					sollCrimpbreiteDraht.setText(Helper.formatZahl(pkDto
							.getNCrimpbreitDraht(), 2, LPMain.getTheClient()
							.getLocUi()));

					if (pruefart
							.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)) {
						sollCrimphoeheIsolation.setText(Helper.formatZahl(pkDto
								.getNCrimphoeheIsolation(), 2, LPMain
								.getTheClient().getLocUi()));

						sollCrimpbreiteIsolation.setText(Helper.formatZahl(
								pkDto.getNCrimpbreiteIsolation(), 2, LPMain
										.getTheClient().getLocUi()));
					}

				}

				WrapperNumberField wnfCrimphoehe = new WrapperNumberField();
				wnfCrimphoehe.setMandatoryField(true);
				WrapperNumberField wnfCrimphoeheIsolation = new WrapperNumberField();
				wnfCrimphoeheIsolation.setMandatoryField(true);
				WrapperNumberField wnfCrimpBreite = new WrapperNumberField();
				wnfCrimpBreite.setMandatoryField(true);
				WrapperNumberField wnfCrimpBreiteIsolation = new WrapperNumberField();
				wnfCrimpBreiteIsolation.setMandatoryField(true);

				WrapperNumberField wnfAbzugskraft = new WrapperNumberField();
				WrapperNumberField wnfAbzugskraft2 = new WrapperNumberField();

				for (PruefergebnisDto ergebnisDto : alPruefergebnis) {

					if (ergebnisDto.getLospruefplanIId().equals(dto.getIId())) {
						wnfCrimphoehe.setBigDecimal(ergebnisDto
								.getNCrimphoeheDraht());
						wnfCrimphoeheIsolation.setBigDecimal(ergebnisDto
								.getNCrimphoeheIsolation());
						wnfCrimpBreite.setBigDecimal(ergebnisDto
								.getNCrimpbreitDraht());
						wnfCrimpBreiteIsolation.setBigDecimal(ergebnisDto
								.getNCrimpbreiteIsolation());
						wnfAbzugskraft.setBigDecimal(ergebnisDto
								.getNAbzugskraftLitze());
						wnfAbzugskraft2.setBigDecimal(ergebnisDto
								.getNAbzugskraftLitze2());
					}

					setStatusbarPersonalIIdAnlegen(ergebnisDto
							.getPersonalIIdAnlegen());
					setStatusbarPersonalIIdAendern(ergebnisDto
							.getPersonalIIdAendern());

					setStatusbarTAnlegen(ergebnisDto.getTAnlegen());
					setStatusbarTAendern(ergebnisDto.getTAendern());

				}

				hmCrimphoehe.put(dto.getIId(), wnfCrimphoehe);
				hmCrimphoeheIsolation.put(dto.getIId(), wnfCrimphoeheIsolation);
				hmCrimpbreite.put(dto.getIId(), wnfCrimpBreite);
				hmCrimpbreiteIsolation.put(dto.getIId(),
						wnfCrimpBreiteIsolation);

				hmAbzugskraft.put(dto.getIId(), wnfAbzugskraft);
				hmAbzugskraft2.put(dto.getIId(), wnfAbzugskraft2);

				jpaWorkingOn.add(wnfCrimphoehe, new GridBagConstraints(4,
						iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 40, 2, 2),
						-50, 0));

				jpaWorkingOn.add(sollCrimphoeheDraht, new GridBagConstraints(4,
						iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						30, 0));

				if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)) {

					jpaWorkingOn.add(wnfCrimphoeheIsolation,
							new GridBagConstraints(5, iZeile, 1, 1, 1, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 40, 2, 2), -50, 0));
					jpaWorkingOn.add(sollCrimphoeheIsolation,
							new GridBagConstraints(5, iZeile, 1, 1, 1, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 2), 30, 0));
				}

				jpaWorkingOn.add(wnfCrimpBreite, new GridBagConstraints(6,
						iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 40, 2, 2),
						-50, 0));

				jpaWorkingOn.add(sollCrimpbreiteDraht, new GridBagConstraints(
						6, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						30, 0));

				if (pruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)) {
					jpaWorkingOn.add(wnfCrimpBreiteIsolation,
							new GridBagConstraints(7, iZeile, 1, 1, 1, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 40, 2, 2), -50, 0));
					jpaWorkingOn.add(sollCrimpbreiteIsolation,
							new GridBagConstraints(7, iZeile, 1, 1, 1, 0.0,
									GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 2), 30, 0));
				}

				jpaWorkingOn.add(wnfAbzugskraft, new GridBagConstraints(9,
						iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 40, 2, 2),
						-50, 0));
				if (Helper.short2boolean(dto.getBDoppelanschlag())) {

					jpaWorkingOn.add(wnfAbzugskraft2, new GridBagConstraints(
							10, iZeile, 1, 1, 1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 40, 2,
									2), -50, 0));
				}

			} else if (Helper.isOneOf(pruefart, 
					StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG, 
					StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG,
					StuecklisteFac.PRUEFART_FREIE_PRUEFUNG)) {

				ButtonGroup bgJaNein = new ButtonGroup();

				WrapperRadioButton wcbJa = new WrapperRadioButton("Ja");
				WrapperRadioButton wcbNein = new WrapperRadioButton("Nein");

				bgJaNein.add(wcbJa);
				bgJaNein.add(wcbNein);

				for (PruefergebnisDto ergebnisDto : alPruefergebnis) {

					if (ergebnisDto.getLospruefplanIId().equals(dto.getIId())
							&& ergebnisDto.getBWert() != null) {

						if (Helper.short2boolean(ergebnisDto.getBWert())) {
							wcbJa.setSelected(true);
						} else {
							wcbNein.setSelected(true);
						}

					}
				}

				jpaWorkingOn.add(wcbJa,
						new GridBagConstraints(11, iZeile, 1, 1, 1, 0.0,
								GridBagConstraints.WEST,
								GridBagConstraints.NONE,
								new Insets(2, 2, 2, 20), 10, 0));

				jpaWorkingOn.add(wcbNein,
						new GridBagConstraints(11, iZeile, 1, 1, 1, 0.0,
								GridBagConstraints.EAST,
								GridBagConstraints.NONE,
								new Insets(2, 50, 2, 2), 30, 0));
				hmBWert.put(dto.getIId(), bgJaNein);
			} else if (pruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)
					|| pruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {

				WrapperNumberField wnfWert = new WrapperNumberField();
				wnfWert.setMandatoryField(true);

				for (PruefergebnisDto ergebnisDto : alPruefergebnis) {

					if (ergebnisDto.getLospruefplanIId().equals(dto.getIId())) {
						wnfWert.setBigDecimal(ergebnisDto.getNWert());
					}
				}

				JLabel sollWert = new JLabel();

				if (pkDto != null && pkDto.getNWert() != null) {
					sollWert.setText(Helper.formatZahl(pkDto.getNWert(), 2,
							LPMain.getTheClient().getLocUi()));
				}

				jpaWorkingOn.add(sollWert, new GridBagConstraints(8, iZeile, 1,
						1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						30, 0));

				jpaWorkingOn.add(wnfWert, new GridBagConstraints(8, iZeile, 1,
						1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL,
						new Insets(2, 40, 2, 20), -50, 0));

				String l = "N";
				if (pruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {
					l = "mm";
				}

				jpaWorkingOn.add(new WrapperLabel(l), new GridBagConstraints(8,
						iZeile, 1, 1, 1, 0.0, GridBagConstraints.EAST,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

				hmNWert.put(dto.getIId(), wnfWert);
				
			} else if (StuecklisteFac.PRUEFART_MATERIALSTATUS.equals(pruefart)) {
				WrapperTextField wtfWert = new WrapperTextField(80);
				wtfWert.setMandatoryField(true);
				
				for (PruefergebnisDto ergebnisDto : alPruefergebnis) {
					if (ergebnisDto.getLospruefplanIId().equals(dto.getIId())) {
						wtfWert.setText(ergebnisDto.getCWert());
					}
				}
				
				jpaWorkingOn.add(wtfWert, new GridBagConstraints(12,
						iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
				hmCWert.put(dto.getIId(), wtfWert);
			}
		}

		jspScrollPane.setViewportView(jpaWorkingOn);
		jspScrollPane.setAutoscrolls(true);

		this.add(jspScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		super.eventActionDiscard(e);

		tabbedPaneLos.setSelectedIndex(tabbedPaneLos.IDX_ABLIEFERUNG);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getFertigungServiceDelegate()
				.removePruefergebnisse(losablieferungIId);
		this.leereAlleFelder(this);
		super.eventActionDelete(e, false, false);
		tabbedPaneLos.setSelectedIndex(tabbedPaneLos.IDX_ABLIEFERUNG);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LOS;
	}

	protected void setDefaults() throws Throwable {
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			ArrayList<PruefergebnisDto> alErgebnisse = new ArrayList<PruefergebnisDto>();

			for (LospruefplanDto dto : alPruefplan) {

				PruefergebnisDto ergDto = new PruefergebnisDto();

				ergDto.setLospruefplanIId(dto.getIId());
				ergDto.setLosablieferungIId(losablieferungIId);

				if (hmCrimpbreite.containsKey(dto.getIId())) {
					ergDto.setNCrimpbreitDraht(hmCrimpbreite.get(dto.getIId())
							.getBigDecimal());
				}
				if (hmAbzugskraft.containsKey(dto.getIId())) {
					ergDto.setNAbzugskraftLitze(hmAbzugskraft.get(dto.getIId())
							.getBigDecimal());
				}
				if (hmAbzugskraft2.containsKey(dto.getIId())) {
					ergDto.setNAbzugskraftLitze2(hmAbzugskraft2.get(
							dto.getIId()).getBigDecimal());
				}
				if (hmCrimphoehe.containsKey(dto.getIId())) {
					ergDto.setNCrimphoeheDraht(hmCrimphoehe.get(dto.getIId())
							.getBigDecimal());
				}
				if (hmCrimphoehe.containsKey(dto.getIId())) {
					ergDto.setNCrimphoeheDraht(hmCrimphoehe.get(dto.getIId())
							.getBigDecimal());
				}
				if (hmCrimphoeheIsolation.containsKey(dto.getIId())) {
					ergDto.setNCrimphoeheIsolation(hmCrimphoeheIsolation.get(
							dto.getIId()).getBigDecimal());
				}
				if (hmCrimpbreiteIsolation.containsKey(dto.getIId())) {
					ergDto.setNCrimpbreiteIsolation(hmCrimpbreiteIsolation.get(
							dto.getIId()).getBigDecimal());
				}

				if (hmNWert.containsKey(dto.getIId())) {
					ergDto.setNWert(hmNWert.get(dto.getIId()).getBigDecimal());
				}

				if (hmBWert.containsKey(dto.getIId())) {

					ButtonGroup bg = hmBWert.get(dto.getIId());

					Enumeration buttons = bg.getElements();

					WrapperRadioButton bJa = (WrapperRadioButton) buttons
							.nextElement();
					WrapperRadioButton bNein = (WrapperRadioButton) buttons
							.nextElement();

					if (bJa.isSelected() || bNein.isSelected()) {

						Short b = 0;

						if (bJa.isSelected()) {
							b = 1;
						}

						ergDto.setBWert(b);
					} else {
						showDialogPflichtfelderAusfuellen();
						return;
					}

				}
				
				WrapperTextField tf = null;
				if ( (tf = hmCWert.get(dto.getIId()) ) != null ) {
					ergDto.setCWert(tf.getText());
				}
				
				alErgebnisse.add(ergDto);
			}

			DelegateFactory.getInstance().getFertigungServiceDelegate()
					.updatePruefergebnisse(alErgebnisse, losablieferungIId);

			super.eventActionSave(e, true);
			eventYouAreSelected(false);

		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
		}
	}

}
