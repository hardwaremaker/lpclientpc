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
package com.lp.client.stueckliste;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
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
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerschleissteilDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.StklpruefplanDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class PanelStklpruefplan extends PanelBasis {

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
	private StklpruefplanDto stklpruefplanDto = null;

	private InternalFrameStueckliste internalFrameStueckliste = null;

	private WrapperComboBox wcoPruefart = new WrapperComboBox();

	private WrapperButton wbuKontakt = new WrapperButton();
	private WrapperTextField wtfKontakt = new WrapperTextField();

	private WrapperButton wbuLitze = new WrapperButton();
	private WrapperTextField wtfLitze = new WrapperTextField();

	private WrapperButton wbuLitze2 = new WrapperButton();
	private WrapperTextField wtfLitze2 = new WrapperTextField();

	static final public String ACTION_SPECIAL_SOLLMATERIAL_KONTAKT_FROM_LISTE = "action_sollmaterial_kontakt_from_liste";
	private PanelQueryFLR panelQueryFLRStuecklistepositionKontakt = null;

	static final public String ACTION_SPECIAL_SOLLMATERIAL_LITZE_FROM_LISTE = "action_sollmaterial_litze_from_liste";
	private PanelQueryFLR panelQueryFLRStuecklistepositionLitze = null;

	static final public String ACTION_SPECIAL_SOLLMATERIAL_LITZE2_FROM_LISTE = "action_sollmaterial_litze2_from_liste";
	private PanelQueryFLR panelQueryFLRStuecklistepositionLitze2 = null;

	private WrapperButton wbuPruefkombination = new WrapperButton();
	private WrapperTextField wtfPruefkombination = new WrapperTextField();

	static final public String ACTION_SPECIAL_PRUEFKOMBINATION_FROM_LISTE = "action_sollmaterial_pruefkombination_from_liste";
	private PanelQueryFLR panelQueryFLRPruefkombination = null;

	private WrapperButton wbuVerschleissteil = new WrapperButton();
	private WrapperTextField wtfVerschleissteil = new WrapperTextField();

	static final public String ACTION_SPECIAL_VERSCHLEISSTEIL_FROM_LISTE = "action_verschleissteil_litze_from_liste";
	private PanelQueryFLR panelQueryFLRVerschleissteil = null;

	private WrapperCheckBox wcbDoppelanschlag = new WrapperCheckBox();

	public PanelStklpruefplan(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameStueckliste = (InternalFrameStueckliste) internalFrame;
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
		stklpruefplanDto = new StklpruefplanDto();
	}

	protected void dto2Components() throws Throwable {

		if (stklpruefplanDto.getVerschleissteilIId() != null) {
			VerschleissteilDto vDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.verschleissteilFindByPrimaryKey(
							stklpruefplanDto.getVerschleissteilIId());

			wtfVerschleissteil.setText(vDto.getBezeichnung());

		} else {
			wtfVerschleissteil.setText(null);
		}

		if (stklpruefplanDto.getStuecklistepositionIIdKontakt() != null) {

			StuecklistepositionDto stuecklistepositionDtoKontakt = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.stuecklistepositionFindByPrimaryKey(
							stklpruefplanDto.getStuecklistepositionIIdKontakt());

			ArtikelDto artikelDtoKontakt = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							stuecklistepositionDtoKontakt.getArtikelIId());
			wtfKontakt.setText(artikelDtoKontakt.formatArtikelbezeichnung());
		} else {
			wtfKontakt.setText(null);
		}

		if (stklpruefplanDto.getStuecklistepositionIIdLitze() != null) {

			StuecklistepositionDto stuecklistepositionDtoLitze = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.stuecklistepositionFindByPrimaryKey(
							stklpruefplanDto.getStuecklistepositionIIdLitze());

			ArtikelDto artikelDtoLitze = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							stuecklistepositionDtoLitze.getArtikelIId());
			wtfLitze.setText(artikelDtoLitze.formatArtikelbezeichnung());

		} else {
			wtfLitze.setText(null);
		}

		if (stklpruefplanDto.getStuecklistepositionIIdLitze2() != null) {

			StuecklistepositionDto stuecklistepositionDtoLitze2 = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.stuecklistepositionFindByPrimaryKey(
							stklpruefplanDto.getStuecklistepositionIIdLitze2());

			ArtikelDto artikelDtoLitze = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							stuecklistepositionDtoLitze2.getArtikelIId());
			wtfLitze2.setText(artikelDtoLitze.formatArtikelbezeichnung());

		} else {
			wtfLitze2.setText(null);
		}

		wtfPruefkombination.setText(null);
		if (stklpruefplanDto.getPruefkombinationId() != null) {

			PruefkombinationDto pruefkombinationDto = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.pruefkombinationFindByPrimaryKey(
							stklpruefplanDto.getPruefkombinationId());
			if (pruefkombinationDto.getPruefkombinationsprDto() != null
					&& pruefkombinationDto.getPruefkombinationsprDto()
							.getCBez() != null) {

				wtfPruefkombination.setText(pruefkombinationDto
						.getPruefkombinationsprDto().getCBez());
			} else {
				wtfPruefkombination.setText("SELEKTIERT");
			}

		}

		wcbDoppelanschlag.setShort(stklpruefplanDto.getBDoppelanschlag());

		wcoPruefart.setKeyOfSelectedItem(stklpruefplanDto.getPruefartIId());

		setStatusbarPersonalIIdAendern(stklpruefplanDto.getPersonalIIdAendern());
		setStatusbarPersonalIIdAnlegen(stklpruefplanDto.getPersonalIIdAnlegen());

		setStatusbarTAendern(stklpruefplanDto.getTAendern());
		setStatusbarTAnlegen(stklpruefplanDto.getTAnlegen());

	}

	void dialogQueryStuecklistepositionKontaktFromListe() throws Throwable {

		panelQueryFLRStuecklistepositionKontakt = StuecklisteFilterFactory
				.getInstance().createPanelFLRStuecklisteposition(
						getInternalFrame(),
						internalFrameStueckliste.getStuecklisteDto().getIId(),
						stklpruefplanDto.getStuecklistepositionIIdKontakt());
		new DialogQuery(panelQueryFLRStuecklistepositionKontakt);

	}

	void dialogQueryStuecklistepositionLitzeFromListe() throws Throwable {

		panelQueryFLRStuecklistepositionLitze = StuecklisteFilterFactory
				.getInstance().createPanelFLRStuecklisteposition(
						getInternalFrame(),
						internalFrameStueckliste.getStuecklisteDto().getIId(),
						stklpruefplanDto.getStuecklistepositionIIdLitze());
		new DialogQuery(panelQueryFLRStuecklistepositionLitze);

	}

	void dialogQueryStuecklistepositionLitze2FromListe() throws Throwable {

		panelQueryFLRStuecklistepositionLitze2 = StuecklisteFilterFactory
				.getInstance().createPanelFLRStuecklisteposition(
						getInternalFrame(),
						internalFrameStueckliste.getStuecklisteDto().getIId(),
						stklpruefplanDto.getStuecklistepositionIIdLitze2());
		new DialogQuery(panelQueryFLRStuecklistepositionLitze2);

	}

	void dialogQueryVerschleissteilFromListe() throws Throwable {

		String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH };

		FilterKriterium[] kriterien = null;

		Integer artikelIIdKontakt = null;
		Integer artikelIIdLitze = null;
		Integer artikelIIdLitze2 = null;

		if (stklpruefplanDto.getStuecklistepositionIIdKontakt() != null) {
			StuecklistepositionDto stuecklistepositionDtoKontakt = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.stuecklistepositionFindByPrimaryKey(
							stklpruefplanDto.getStuecklistepositionIIdKontakt());
			artikelIIdKontakt = stuecklistepositionDtoKontakt.getArtikelIId();
		}

		if (stklpruefplanDto.getStuecklistepositionIIdLitze() != null) {
			StuecklistepositionDto stuecklistepositionDtoLitze = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.stuecklistepositionFindByPrimaryKey(
							stklpruefplanDto.getStuecklistepositionIIdLitze());
			artikelIIdLitze = stuecklistepositionDtoLitze.getArtikelIId();
		}
		if (stklpruefplanDto.getStuecklistepositionIIdLitze2() != null) {
			StuecklistepositionDto stuecklistepositionDtoLitz2 = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.stuecklistepositionFindByPrimaryKey(
							stklpruefplanDto.getStuecklistepositionIIdLitze2());
			artikelIIdLitze2 = stuecklistepositionDtoLitz2.getArtikelIId();
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

		panelQueryFLRVerschleissteil.setSelectedId(stklpruefplanDto
				.getVerschleissteilIId());

		new DialogQuery(panelQueryFLRVerschleissteil);

	}

	void dialogQueryPruefkombinationFromListe() throws Throwable {

		String sPruefart = DelegateFactory.stkl().pruefartFindByPrimaryKey(
				(Integer) wcoPruefart.getKeyOfSelectedItem()).getCNr();

		List<FilterKriterium> kriterien = new ArrayList<FilterKriterium>();
		kriterien.add(FilterDslBuilder
				.create("flrpruefart.i_id")
				.equal((Integer) wcoPruefart.getKeyOfSelectedItem()).build());
		
		if (sPruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {
			kriterien.add(FilterDslBuilder
					.create("artikel_i_id_litze")
					.isNull().build());
			
		} else if (!StuecklisteFac.PRUEFART_FREIE_PRUEFUNG.equals(sPruefart)) {
			kriterien.add(FilterDslBuilder
					.create("artikel_i_id_kontakt")
					.isNull().build());
		}

		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRPruefkombination = new PanelQueryFLR(null, kriterien.toArray(new FilterKriterium[] {}),
				QueryParameters.UC_ID_PRUEFKOMBINATION, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("stk.pruefkombination"));

		new DialogQuery(panelQueryFLRPruefkombination);

	}

	protected void components2Dto() throws Throwable {

		stklpruefplanDto.setStuecklisteId(internalFrameStueckliste
				.getStuecklisteDto().getIId());

		stklpruefplanDto.setPruefartIId((Integer) wcoPruefart
				.getKeyOfSelectedItem());

		stklpruefplanDto.setBDoppelanschlag(wcbDoppelanschlag.getShort());
		if(wcbDoppelanschlag.isSelected()==false){
			stklpruefplanDto.setStuecklistepositionIIdLitze2(null);
		}
		

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRStuecklistepositionKontakt) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				StuecklistepositionDto stuecklistepositionDto = DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.stuecklistepositionFindByPrimaryKey(key);

				stklpruefplanDto.setStuecklistepositionIIdKontakt(key);

				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								stuecklistepositionDto.getArtikelIId());
				wtfKontakt.setText(artikelDto.formatArtikelbezeichnung());
			} else if (e.getSource() == panelQueryFLRStuecklistepositionLitze) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				StuecklistepositionDto stuecklistepositionDto = DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.stuecklistepositionFindByPrimaryKey(key);

				stklpruefplanDto.setStuecklistepositionIIdLitze(key);

				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								stuecklistepositionDto.getArtikelIId());
				wtfLitze.setText(artikelDto.formatArtikelbezeichnung());
			} else if (e.getSource() == panelQueryFLRStuecklistepositionLitze2) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				StuecklistepositionDto stuecklistepositionDto = DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.stuecklistepositionFindByPrimaryKey(key);

				stklpruefplanDto.setStuecklistepositionIIdLitze2(key);

				ArtikelDto artikelDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey(
								stuecklistepositionDto.getArtikelIId());
				wtfLitze2.setText(artikelDto.formatArtikelbezeichnung());
			} else if (e.getSource() == panelQueryFLRVerschleissteil) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				VerschleissteilDto vDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.verschleissteilFindByPrimaryKey(key);

				stklpruefplanDto.setVerschleissteilIId(key);

				wtfVerschleissteil.setText(vDto.getBezeichnung());
			} else if (e.getSource() == panelQueryFLRPruefkombination) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();

				wtfPruefkombination.setText(null);

				PruefkombinationDto pruefkombinationDto = DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.pruefkombinationFindByPrimaryKey(key);

				stklpruefplanDto.setPruefkombinationId(key);

				if (pruefkombinationDto.getPruefkombinationsprDto() != null
						&& pruefkombinationDto.getPruefkombinationsprDto()
								.getCBez() != null) {
					wtfPruefkombination.setText(pruefkombinationDto
							.getPruefkombinationsprDto().getCBez());
				} else {
					wtfPruefkombination.setText("SELEKTIERT");
				}

				stklpruefplanDto.setStuecklistepositionIIdKontakt(null);
				stklpruefplanDto.setStuecklistepositionIIdLitze(null);
				stklpruefplanDto.setStuecklistepositionIIdLitze2(null);
				wtfKontakt.setText(null);
				wtfLitze.setText(null);
				wtfLitze2.setText(null);

			}
		}
		if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRPruefkombination) {
				stklpruefplanDto.setPruefkombinationId(null);
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
		// wtfLitze.setMandatoryField(true);
		// wsfWerkzeug.setMandatoryField(true);

		wbuKontakt.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.kontakt") + "...");
		wbuLitze.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.litze") + "...");
		wbuPruefkombination.setText(LPMain
				.getTextRespectUISPr("stk.oderpruefkombination") + "...");

		wbuLitze2.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.litze") + "2...");

		wbuVerschleissteil.setText(LPMain
				.getTextRespectUISPr("artikel.werkzeug.verschleissteil")
				+ "...");
		wtfVerschleissteil.setActivatable(false);

		wcoPruefart
				.addActionListener(new PanelStklpruefplan_wcbPruefart_actionAdapter(
						this));

		// wtfVerschleissteil.setMandatoryField(true);

		wtfKontakt.setActivatable(false);
		wtfKontakt.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfLitze.setActivatable(false);
		wtfLitze.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wtfPruefkombination.setActivatable(false);
		wtfPruefkombination.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuKontakt.addActionListener(this);
		wbuKontakt
				.setActionCommand(ACTION_SPECIAL_SOLLMATERIAL_KONTAKT_FROM_LISTE);
		wbuLitze.addActionListener(this);
		wbuLitze.setActionCommand(ACTION_SPECIAL_SOLLMATERIAL_LITZE_FROM_LISTE);

		wbuLitze2.addActionListener(this);
		wbuLitze2
				.setActionCommand(ACTION_SPECIAL_SOLLMATERIAL_LITZE2_FROM_LISTE);

		wbuVerschleissteil.addActionListener(this);
		wbuVerschleissteil
				.setActionCommand(ACTION_SPECIAL_VERSCHLEISSTEIL_FROM_LISTE);

		wbuPruefkombination.addActionListener(this);
		wbuPruefkombination
				.setActionCommand(ACTION_SPECIAL_PRUEFKOMBINATION_FROM_LISTE);

		wcbDoppelanschlag.setText(LPMain
				.getTextRespectUISPr("stk.pruefkombination.doppelanschlag"));
		wcbDoppelanschlag.addActionListener(this);

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
		jpaWorkingOn.add(wtfKontakt, new GridBagConstraints(1, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuLitze, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLitze, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLitze2, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLitze2, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuPruefkombination, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPruefkombination, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuVerschleissteil, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVerschleissteil, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wcbDoppelanschlag, new GridBagConstraints(0, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_STUECKLISTE;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(
				ACTION_SPECIAL_SOLLMATERIAL_KONTAKT_FROM_LISTE)) {
			dialogQueryStuecklistepositionKontaktFromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_SOLLMATERIAL_LITZE_FROM_LISTE)) {
			dialogQueryStuecklistepositionLitzeFromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_SOLLMATERIAL_LITZE2_FROM_LISTE)) {
			dialogQueryStuecklistepositionLitze2FromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PRUEFKOMBINATION_FROM_LISTE)) {
			dialogQueryPruefkombinationFromListe();
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_VERSCHLEISSTEIL_FROM_LISTE)) {
			dialogQueryVerschleissteilFromListe();
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
		DelegateFactory.getInstance().getStuecklisteDelegate()
				.removeStklpruefplan(stklpruefplanDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	public void pruefplanVorbesetzen(StuecklistepositionDto sktlposDtoKontakt,
			StuecklistepositionDto sktlposDtoLitze, PruefkombinationDto pkDto)
			throws Throwable {

		wcoPruefart.setKeyOfSelectedItem(pkDto.getPruefartIId());

		String pruefart = DelegateFactory.getInstance()
				.getStuecklisteDelegate()
				.pruefartFindByPrimaryKey(pkDto.getPruefartIId()).getCNr();

		if (pruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)
				&& sktlposDtoLitze == null) {
			sktlposDtoLitze = sktlposDtoKontakt;
			sktlposDtoKontakt = null;
		}

		if (sktlposDtoKontakt != null) {

			ArtikelDto artikelDtoKontakt = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(sktlposDtoKontakt.getArtikelIId());
			wtfKontakt.setText(artikelDtoKontakt.formatArtikelbezeichnung());

			stklpruefplanDto.setStuecklistepositionIIdKontakt(sktlposDtoKontakt
					.getIId());
		} else {
			stklpruefplanDto.setStuecklistepositionIIdKontakt(null);
		}

		if (sktlposDtoLitze != null) {
			ArtikelDto artikelDtoLitze = DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(sktlposDtoLitze.getArtikelIId());
			wtfLitze.setText(artikelDtoLitze.formatArtikelbezeichnung());

			stklpruefplanDto.setStuecklistepositionIIdLitze(sktlposDtoLitze
					.getIId());
		} else {
			stklpruefplanDto.setStuecklistepositionIIdLitze(null);
		}

		if (pkDto.getVerschleissteilIId() != null) {
			VerschleissteilDto vDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.verschleissteilFindByPrimaryKey(
							pkDto.getVerschleissteilIId());

			wtfVerschleissteil.setText(vDto.getBezeichnung());

		} else {
			wtfVerschleissteil.setText(null);
		}

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
			stklpruefplanDto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.stklpruefplanFindByPrimaryKey((Integer) key);
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

			wtfLitze2.setMandatoryField(false);
			wbuLitze2.setVisible(false);
			wtfLitze2.setVisible(false);

			wbuPruefkombination.setVisible(false);
			wtfPruefkombination.setVisible(false);

			wbuVerschleissteil.setVisible(false);
			wtfVerschleissteil.setVisible(false);
			wtfVerschleissteil.setMandatoryField(false);

			String sPruefart = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.pruefartFindByPrimaryKey(pruefartIId).getCNr();

			if (sPruefart.equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
					|| sPruefart
							.equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {

				wtfKontakt.setMandatoryField(true);
				wbuKontakt.setVisible(true);
				wtfKontakt.setVisible(true);

				wcbDoppelanschlag.setVisible(true);

				wtfLitze.setMandatoryField(true);
				wbuLitze.setVisible(true);
				wtfLitze.setVisible(true);

				wtfLitze2.setMandatoryField(true);
				wbuLitze2.setVisible(true);
				wtfLitze2.setVisible(true);

				wtfLitze2.setVisible(wcbDoppelanschlag.isSelected());
				wbuLitze2.setVisible(wcbDoppelanschlag.isSelected());
				wtfLitze2.setMandatoryField(wcbDoppelanschlag.isSelected());

				wbuVerschleissteil.setVisible(true);
				wtfVerschleissteil.setVisible(true);
				wtfVerschleissteil.setMandatoryField(true);
			}

			if (sPruefart.equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {
				wtfKontakt.setMandatoryField(true);
				wbuKontakt.setVisible(true);
				wtfKontakt.setVisible(true);

				wtfLitze.setMandatoryField(true);
				wbuLitze.setVisible(true);
				wtfLitze.setVisible(true);
			}
			if (sPruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {

				// wtfLitze.setMandatoryField(true);
				wbuLitze.setVisible(true);
				wtfLitze.setVisible(true);

				wbuPruefkombination.setVisible(true);
				wtfPruefkombination.setVisible(true);

			}

			if (sPruefart.equals(StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG)
					|| sPruefart
							.equals(StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG)) {
				// wtfKontakt.setMandatoryField(true);
				wbuKontakt.setVisible(true);
				wtfKontakt.setVisible(true);

				wbuPruefkombination.setVisible(true);
				wtfPruefkombination.setVisible(true);

				wbuKontakt.setText(LPMain
						.getTextRespectUISPr("stk.pruefkombination.artikel")
						+ "...");

			}
			
			if (StuecklisteFac.PRUEFART_FREIE_PRUEFUNG.equals(sPruefart)) {
				wbuPruefkombination.setVisible(true);
				wtfPruefkombination.setVisible(true);
				wtfPruefkombination.setMandatoryField(true);
				wbuPruefkombination.setText(LPMain.getTextRespectUISPr("stk.pruefkombination"));
			} else {
				wbuPruefkombination.setText(
						LPMain.getTextRespectUISPr("stk.oderpruefkombination") + "...");
				wtfPruefkombination.setMandatoryField(false);
			}

		} catch (Throwable e1) {
			handleException(e1, true);
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			String sPruefart = DelegateFactory
					.getInstance()
					.getStuecklisteDelegate()
					.pruefartFindByPrimaryKey(
							(Integer) wcoPruefart.getKeyOfSelectedItem())
					.getCNr();

			if (sPruefart.equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {

				if (stklpruefplanDto.getPruefkombinationId() == null
						&& stklpruefplanDto.getStuecklistepositionIIdLitze() == null) {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("stk.pruefplan.error"));
					return;
				}

			}

			if (sPruefart.equals(StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG)
					|| sPruefart
							.equals(StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG)) {
				if (stklpruefplanDto.getPruefkombinationId() == null
						&& stklpruefplanDto.getStuecklistepositionIIdKontakt() == null) {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("stk.pruefplan.error"));

					return;
				}

			}

			if (stklpruefplanDto.getIId() == null) {

				stklpruefplanDto.setIId(DelegateFactory.getInstance()
						.getStuecklisteDelegate()
						.createStklpruefplan(stklpruefplanDto));
				setKeyWhenDetailPanel(stklpruefplanDto.getIId());
			} else {
				DelegateFactory.getInstance().getStuecklisteDelegate()
						.updateStklpruefplan(stklpruefplanDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						stklpruefplanDto.getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

	class PanelStklpruefplan_wcbPruefart_actionAdapter implements
			ActionListener {
		private PanelStklpruefplan adaptee;

		PanelStklpruefplan_wcbPruefart_actionAdapter(PanelStklpruefplan adaptee) {
			this.adaptee = adaptee;
		}

		public void actionPerformed(ActionEvent e) {
			adaptee.wcbPruefart_actionPerformed(e);
		}
	}

}
