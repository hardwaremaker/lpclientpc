
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
package com.lp.client.angebotstkl;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDtoFuerOptimieren;
import com.lp.server.angebotstkl.service.EkagLieferantoptimierenDto;
import com.lp.server.angebotstkl.service.EkaglieferantDto;
import com.lp.server.angebotstkl.service.PositionlieferantDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

public class PanelEkaglieferantOptimieren extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;

	private JPanel jpaKriterien = new JPanel();

	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaLieferzeitInKW = new WrapperLabel();
	private WrapperNumberField wnfLieferzeitInKW = new WrapperNumberField();

	private WrapperComboBox wcbMengen = new WrapperComboBox();

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperComboBox wcbSortierung = new WrapperComboBox();

	private WrapperLabel wlaSucheArtikelnummer = new WrapperLabel();
	private WrapperLabel wlaSucheBezeichnung = new WrapperLabel();
	private WrapperTextField wtfSucheArtikelnummer = new WrapperTextField();
	private WrapperTextField wtfSucheBezeichnung = new WrapperTextField();

	private WrapperCheckBox wcbMinBest = new WrapperCheckBox();
	private WrapperCheckBox wcbVerpackungeinheit = new WrapperCheckBox();

	private JScrollPane jspScrollPane = new JScrollPane();

	private JTable tableInhalt = new JTable();

	static final int iSpaltePositionsmenge = 0;
	static final int iSpaltePositionseinheit = 1;
	static final int iSpalteArtikel = 2;
	static final int iSpalteBezeichnung = 3;
	static final int iSpalteZusatzbezeichnung = 4;
	static final int iSpalteHerstellernummer = 5;

	static final int iSpalteKommentareVorhanden = 6;
	static final int iSpalteVerfuegbar = 7;

	static final int iSpalteMenge1 = 8;
	static final int iSpalteGuenstPreis1 = 9;
	static final int iSpalteLiefKbez1 = 10;
	static final int iSpalteLieferzeit1 = 11;
	static final int iSpalteVPE1 = 12;
	static final int iSpalteUebstPreis1 = 13;
	static final int iSpalteLieferzeitUebst1 = 14;
	static final int iSpalteVPEUebst1 = 15;
	static final int iSpalteLFUebst1 = 16;

	static final int iSpalteMenge2 = 17;
	static final int iSpalteGuenstPreis2 = 18;
	static final int iSpalteLiefKbez2 = 19;
	static final int iSpalteLieferzeit2 = 20;
	static final int iSpalteVPE2 = 22;
	static final int iSpalteUebstPreis2 = 22;
	static final int iSpalteLieferzeitUebst2 = 23;
	static final int iSpalteVPEUebst2 = 24;
	static final int iSpalteLFUebst2 = 25;

	static final int iSpalteMenge3 = 26;
	static final int iSpalteGuenstPreis3 = 27;
	static final int iSpalteLiefKbez3 = 28;
	static final int iSpalteLieferzeit3 = 29;
	static final int iSpalteVPE3 = 30;
	static final int iSpalteUebstPreis3 = 31;
	static final int iSpalteLieferzeitUebst3 = 32;
	static final int iSpalteVPEUebst3 = 33;
	static final int iSpalteLFUebst3 = 34;

	static final int iSpalteMenge4 = 35;
	static final int iSpalteGuenstPreis4 = 36;
	static final int iSpalteLiefKbez4 = 37;
	static final int iSpalteLieferzeit4 = 38;
	static final int iSpalteVPE4 = 39;
	static final int iSpalteUebstPreis4 = 40;
	static final int iSpalteLieferzeitUebst4 = 41;
	static final int iSpalteVPEUebst4 = 42;
	static final int iSpalteLFUebst4 = 43;

	static final int iSpalteMenge5 = 44;
	static final int iSpalteGuenstPreis5 = 45;
	static final int iSpalteLiefKbez5 = 46;
	static final int iSpalteLieferzeit5 = 47;
	static final int iSpalteVPE5 = 48;
	static final int iSpalteUebstPreis5 = 49;
	static final int iSpalteLieferzeitUebst5 = 50;
	static final int iSpalteVPEUebst5 = 51;
	static final int iSpalteLFUebst5 = 52;

	static final int iAnzahlSpalten = 53;

	public static final int ACTION_LIEFERANT_UEBERSTEUERN = 1;

	private JLabel wlaBestellt = new JLabel();
	private static final String ACTION_SPECIAL_BESTELLEN = PanelBasis.ACTION_MY_OWN_NEW + "ACTION_SPECIAL_BESTELLEN";

	private static final String ACTION_SPECIAL_ARTIKELNUMMERLF_SPEICHERN = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_ARTIKELNUMMERLF_SPEICHERN";

	private static final String ACTION_SPECIAL_GOTOEKAGLIEFERANTPOSITION = PanelBasis.ACTION_MY_OWN_NEW
			+ "ACTION_SPECIAL_GOTOEKAGLIEFERANTPOSITION";

	private PanelQueryFLR panelQueryFLRPositionslieferant = null;

	EkagLieferantoptimierenDto optDto = null;

	public PanelEkaglieferantOptimieren(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, true);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_EINKAUFSANGEBOT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);

		enableAllComponents(this, true);

		aktualisiereInhalt();

	}

	private boolean istBereitsBestellt(int iMenge) throws Throwable {

		for (int i = 0; i < optDto.getPos().size(); i++) {
			EinkaufsangebotpositionDtoFuerOptimieren ekagPosDto = optDto.getPos().get(i);

			// 1

			if (iMenge == AngebotstklFac.MENGE_1) {

				if (ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge1() != null) {
					PositionlieferantDto posliefDtoUest = DelegateFactory.getInstance().getAngebotstklDelegate()
							.positionlieferantFindByPrimaryKey(ekagPosDto.getEinkaufsangebotpositionDto()
									.getPositionlieferantIIdUebersteuertMenge1());

					if (Helper.short2boolean(posliefDtoUest.getBMenge1Bestellen()) == true) {
						return true;
					}

				} else {
					if (ekagPosDto.getGuenstigsterLieferantMenge1() != null && Helper
							.short2boolean(ekagPosDto.getGuenstigsterLieferantMenge1().getBMenge1Bestellen()) == true) {

						return true;
					}
				}
			}
			// 2
			if (iMenge == AngebotstklFac.MENGE_2) {
				if (ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge2() != null) {
					PositionlieferantDto posliefDtoUest = DelegateFactory.getInstance().getAngebotstklDelegate()
							.positionlieferantFindByPrimaryKey(ekagPosDto.getEinkaufsangebotpositionDto()
									.getPositionlieferantIIdUebersteuertMenge2());

					if (Helper.short2boolean(posliefDtoUest.getBMenge2Bestellen()) == true) {
						return true;
					}

				} else {
					if (ekagPosDto.getGuenstigsterLieferantMenge2() != null && Helper
							.short2boolean(ekagPosDto.getGuenstigsterLieferantMenge2().getBMenge2Bestellen()) == true) {

						return true;
					}
				}
			}
			if (iMenge == AngebotstklFac.MENGE_3) {

				// 3
				if (ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge3() != null) {
					PositionlieferantDto posliefDtoUest = DelegateFactory.getInstance().getAngebotstklDelegate()
							.positionlieferantFindByPrimaryKey(ekagPosDto.getEinkaufsangebotpositionDto()
									.getPositionlieferantIIdUebersteuertMenge3());

					if (Helper.short2boolean(posliefDtoUest.getBMenge3Bestellen()) == true) {
						return true;
					}

				} else {
					if (ekagPosDto.getGuenstigsterLieferantMenge3() != null && Helper
							.short2boolean(ekagPosDto.getGuenstigsterLieferantMenge3().getBMenge3Bestellen()) == true) {

						return true;
					}
				}
			}
			if (iMenge == AngebotstklFac.MENGE_4) {
				// 4
				if (ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge4() != null) {
					PositionlieferantDto posliefDtoUest = DelegateFactory.getInstance().getAngebotstklDelegate()
							.positionlieferantFindByPrimaryKey(ekagPosDto.getEinkaufsangebotpositionDto()
									.getPositionlieferantIIdUebersteuertMenge4());

					if (Helper.short2boolean(posliefDtoUest.getBMenge4Bestellen()) == true) {
						return true;
					}

				} else {
					if (ekagPosDto.getGuenstigsterLieferantMenge4() != null && Helper
							.short2boolean(ekagPosDto.getGuenstigsterLieferantMenge4().getBMenge4Bestellen()) == true) {

						return true;
					}
				}
			}
			if (iMenge == AngebotstklFac.MENGE_5) {

				// 5
				if (ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge5() != null) {
					PositionlieferantDto posliefDtoUest = DelegateFactory.getInstance().getAngebotstklDelegate()
							.positionlieferantFindByPrimaryKey(ekagPosDto.getEinkaufsangebotpositionDto()
									.getPositionlieferantIIdUebersteuertMenge5());

					if (Helper.short2boolean(posliefDtoUest.getBMenge5Bestellen()) == true) {
						return true;
					}

				} else {
					if (ekagPosDto.getGuenstigsterLieferantMenge5() != null && Helper
							.short2boolean(ekagPosDto.getGuenstigsterLieferantMenge5().getBMenge5Bestellen()) == true) {

						return true;
					}
				}
			}
		}

		return false;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI) throws Throwable {
		enableAllComponents(this, true);

		// Filter speichern

		Integer einkaufsangebotIId = ((InternalFrameAngebotstkl) getInternalFrame()).getTabbedPaneEinkaufsangebot()
				.getEinkaufsangebotDto().getIId();

		EinkaufsangebotDto ekagDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.einkaufsangebotFindByPrimaryKey(einkaufsangebotIId);
		ekagDto.setIOptimierenLieferzeit(wnfLieferzeitInKW.getInteger());
		ekagDto.setIOptimierenMenge((Integer) wcbMengen.getKeyOfSelectedItem());
		ekagDto.setISortierung((Integer) wcbSortierung.getKeyOfSelectedItem());
		ekagDto.setBOptimierenMinmenge(Helper.boolean2Short(wcbMinBest.isSelected()));
		ekagDto.setBOptimierenVerpackungseinheit(Helper.boolean2Short(wcbVerpackungeinheit.isSelected()));
		DelegateFactory.getInstance().getAngebotstklDelegate().updateEinkaufsangebot(ekagDto);

		aktualisiereInhalt();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	private PartnerDto getLieferant(Integer positionslieferantIId) throws Throwable {
		PositionlieferantDto posliefDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.positionlieferantFindByPrimaryKey(positionslieferantIId);
		EkaglieferantDto ekaglieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.ekaglieferantFindByPrimaryKey(posliefDto.getEgaklieferantIId());

		return DelegateFactory.getInstance().getLieferantDelegate()
				.lieferantFindByPrimaryKey(ekaglieferantDto.getLieferantIId()).getPartnerDto();

	}

	private String[] getPartnerInfo(Integer positionlieferantIIdUebersteuert) throws Throwable {

		String[] sText = new String[2];
		if (positionlieferantIIdUebersteuert != null) {

			PartnerDto pDto = getLieferant(positionlieferantIIdUebersteuert);
			sText[1] = pDto.formatFixName1Name2();

			String[] bezeichnungenBtn = Helper.intelligenteWorttrennung(new int[] { 8 }, pDto.getCKbez());

			sText[0] = bezeichnungenBtn[0];

		}

		return sText;
	}

	private HashMap<Integer, String> addTooltip(PositionlieferantDto posliefDto, HashMap<Integer, String> hmInput,
			LieferantDto lfDto, boolean guenstigster) throws Throwable {

		if (!hmInput.containsKey(posliefDto.getIId())) {
			if (lfDto == null) {
				EkaglieferantDto ekaglieferantDto = DelegateFactory.getInstance().getAngebotstklDelegate()
						.ekaglieferantFindByPrimaryKey(posliefDto.getEgaklieferantIId());
				lfDto = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(ekaglieferantDto.getLieferantIId());
			}

			String bemerkungen = "";

			if (posliefDto.getCBemerkung() != null && posliefDto.getCBemerkung().length() > 0) {

				bemerkungen += " <b>LF:</b> " + posliefDto.getCBemerkung();

			}

			if (posliefDto.getCBemerkungIntern() != null && posliefDto.getCBemerkungIntern().length() > 0) {

				bemerkungen += " <b>INTERN:</b> " + posliefDto.getCBemerkungIntern();

			}

			if (posliefDto.getCBemerkungVerkauf() != null && posliefDto.getCBemerkungVerkauf().length() > 0) {

				bemerkungen += " <b>VERKAUF:</b> " + posliefDto.getCBemerkungVerkauf();

			}

			if (bemerkungen.length() > 0) {
				hmInput.put(posliefDto.getIId(), "<u>" + lfDto.getPartnerDto().getCKbez() + "</u>: " + bemerkungen);
			}

		}

		return hmInput;
	}

	private String hakerlBestellt(int iMenge) throws Throwable {
		boolean bMenge1Bestellt = istBereitsBestellt(iMenge);
		if (bMenge1Bestellt == true) {
			return " (fix.)";
		} else {
			return "";
		}
	}

	private void aktualisiereInhalt() throws Throwable {

		jpaWorkingOn.revalidate();
		// Ueberschrift

		LPButtonAction btn = getHmOfButtons().get(ACTION_REFRESH);

		btn.getButton().setBackground(UIManager.getColor("Button.background"));

		String[] colNames = new String[iAnzahlSpalten];

		colNames[iSpaltePositionsmenge] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.menge");
		colNames[iSpaltePositionseinheit] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.einheit");
		colNames[iSpalteArtikel] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.artikel");
		colNames[iSpalteBezeichnung] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.bez");
		colNames[iSpalteZusatzbezeichnung] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.zbez");
		colNames[iSpalteHerstellernummer] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.herstellernr");

		colNames[iSpalteKommentareVorhanden] = "K";
		colNames[iSpalteVerfuegbar] = LPMain.getTextRespectUISPr("lp.verfuegbar");

		colNames[iSpalteMenge1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.gesmenge") + " 1";
		colNames[iSpalteGuenstPreis1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.guenstpreis") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		colNames[iSpalteUebstPreis1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstpreis") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		colNames[iSpalteLieferzeitUebst1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeituebst");
		colNames[iSpalteLiefKbez1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.liefkbez");
		colNames[iSpalteLieferzeit1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeit");
		colNames[iSpalteVPE1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.vpe");
		colNames[iSpalteLFUebst1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uest");
		colNames[iSpalteVPEUebst1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstvpe");

		colNames[iSpalteMenge2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.gesmenge") + " 2";
		colNames[iSpalteGuenstPreis2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.guenstpreis") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		colNames[iSpalteUebstPreis2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstpreis") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		colNames[iSpalteLieferzeitUebst2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeituebst");
		colNames[iSpalteLiefKbez2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.liefkbez");
		colNames[iSpalteLieferzeit2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeit");
		colNames[iSpalteVPE2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.vpe");
		colNames[iSpalteVPEUebst2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstvpe");
		colNames[iSpalteLFUebst2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uest");

		colNames[iSpalteMenge3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.gesmenge") + " 3";
		colNames[iSpalteGuenstPreis3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.guenstpreis") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		colNames[iSpalteUebstPreis3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstpreis") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		colNames[iSpalteLieferzeitUebst3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeituebst");
		colNames[iSpalteLiefKbez3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.liefkbez");
		colNames[iSpalteLieferzeit3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeit");
		colNames[iSpalteVPE3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.vpe");
		colNames[iSpalteVPEUebst3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstvpe");
		colNames[iSpalteLFUebst3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uest");

		colNames[iSpalteMenge4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.gesmenge") + " 4";
		colNames[iSpalteGuenstPreis4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.guenstpreis") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		colNames[iSpalteUebstPreis4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstpreis") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		colNames[iSpalteLieferzeitUebst4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeituebst");
		colNames[iSpalteLiefKbez4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.liefkbez");
		colNames[iSpalteLieferzeit4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeit");
		colNames[iSpalteVPE4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.vpe");
		colNames[iSpalteLFUebst4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uest");
		colNames[iSpalteVPEUebst4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstvpe");

		colNames[iSpalteMenge5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.gesmenge") + " 5";
		colNames[iSpalteGuenstPreis5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.guenstpreis") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		colNames[iSpalteUebstPreis5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstpreis") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		colNames[iSpalteLieferzeitUebst5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeituebst");
		colNames[iSpalteLiefKbez5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.liefkbez");
		colNames[iSpalteLieferzeit5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeit");
		colNames[iSpalteVPE5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.vpe");
		colNames[iSpalteVPEUebst5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstvpe");
		colNames[iSpalteLFUebst5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uest");

		final String[] columnToolTips = new String[iAnzahlSpalten];

		columnToolTips[iSpaltePositionsmenge] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.menge");
		columnToolTips[iSpaltePositionseinheit] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.einheit");
		columnToolTips[iSpalteArtikel] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.artikel");
		columnToolTips[iSpalteBezeichnung] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.bez");
		columnToolTips[iSpalteZusatzbezeichnung] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.zbez");
		columnToolTips[iSpalteHerstellernummer] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.herstellernr");

		columnToolTips[iSpalteKommentareVorhanden] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.kommentare");
		columnToolTips[iSpalteVerfuegbar] = LPMain.getTextRespectUISPr("lp.verfuegbar");

		columnToolTips[iSpalteMenge1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.gesmenge") + " 1";
		columnToolTips[iSpalteGuenstPreis1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.guenstpreis.lang") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		columnToolTips[iSpalteUebstPreis1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstpreis.lang") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		columnToolTips[iSpalteLieferzeitUebst1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeituebst.lang");
		columnToolTips[iSpalteLiefKbez1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.liefkbez.lang");
		columnToolTips[iSpalteLieferzeit1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeit.lang");
		columnToolTips[iSpalteVPE1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.vpe");
		columnToolTips[iSpalteVPEUebst1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstvpe");

		columnToolTips[iSpalteLFUebst1] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uest.lang");

		columnToolTips[iSpalteMenge2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.gesmenge") + " 2";
		columnToolTips[iSpalteGuenstPreis2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.guenstpreis.lang") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		columnToolTips[iSpalteUebstPreis2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstpreis.lang") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		columnToolTips[iSpalteLieferzeitUebst2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeituebst.lang");
		columnToolTips[iSpalteLiefKbez2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.liefkbez.lang");
		columnToolTips[iSpalteLieferzeit2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeit.lang");
		columnToolTips[iSpalteVPE2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.vpe");
		columnToolTips[iSpalteVPEUebst2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstvpe");
		columnToolTips[iSpalteLFUebst2] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uest.lang");

		columnToolTips[iSpalteMenge3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.gesmenge") + " 3";
		columnToolTips[iSpalteGuenstPreis3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.guenstpreis.lang") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		columnToolTips[iSpalteUebstPreis3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstpreis.lang") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		columnToolTips[iSpalteLieferzeitUebst3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeituebst.lang");
		columnToolTips[iSpalteLiefKbez3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.liefkbez.lang");
		columnToolTips[iSpalteLieferzeit3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeit.lang");
		columnToolTips[iSpalteVPE3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.vpe");
		columnToolTips[iSpalteVPEUebst3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstvpe");
		columnToolTips[iSpalteLFUebst3] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uest.lang");

		columnToolTips[iSpalteMenge4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.gesmenge") + " 4";
		columnToolTips[iSpalteGuenstPreis4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.guenstpreis.lang") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		columnToolTips[iSpalteUebstPreis4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstpreis.lang") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		columnToolTips[iSpalteLieferzeitUebst4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeituebst.lang");
		columnToolTips[iSpalteLiefKbez4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.liefkbez.lang");
		columnToolTips[iSpalteLieferzeit4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeit.lang");
		columnToolTips[iSpalteVPE4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.vpe");
		columnToolTips[iSpalteVPEUebst4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstvpe");
		columnToolTips[iSpalteLFUebst4] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uest.lang");

		columnToolTips[iSpalteMenge5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.gesmenge") + " 5";
		columnToolTips[iSpalteGuenstPreis5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.guenstpreis.lang") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		columnToolTips[iSpalteUebstPreis5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstpreis.lang") + "/"
				+ LPMain.getTheClient().getSMandantenwaehrung();
		columnToolTips[iSpalteLieferzeitUebst5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeituebst.lang");
		columnToolTips[iSpalteLiefKbez5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.liefkbez.lang");
		columnToolTips[iSpalteLieferzeit5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.lieferzeit.lang");
		columnToolTips[iSpalteVPE5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.vpe");
		columnToolTips[iSpalteVPEUebst5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uebstvpe");
		columnToolTips[iSpalteLFUebst5] = LPMain
				.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.uest.lang");

		JLabel ueberschrift = new JLabel();
		ueberschrift.setOpaque(true);

		ueberschrift.setBackground(Color.WHITE);

		Integer einkaufsangebotIId = ((InternalFrameAngebotstkl) getInternalFrame()).getTabbedPaneEinkaufsangebot()
				.getEinkaufsangebotDto().getIId();

		EinkaufsangebotDto ekagDto = DelegateFactory.getInstance().getAngebotstklDelegate()
				.einkaufsangebotFindByPrimaryKey(einkaufsangebotIId);

		Map mSort = new LinkedHashMap();
		mSort.put(AngebotstklFac.SORTIERUNG_WIE_ERFASST,
				LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.wieerfasst"));
		mSort.put(AngebotstklFac.SORTIERUNG_ARTIKELNUMMER,
				LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.artikelnummer"));
		mSort.put(AngebotstklFac.SORTIERUNG_BEZEICHNUNG,
				LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.bezeichnung"));
		mSort.put(AngebotstklFac.SORTIERUNG_HERSTELLERNUMER,
				LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.herstellernr"));

		if (ekagDto.getNMenge1() != null && ekagDto.getNMenge1().doubleValue() > 0) {
			mSort.put(AngebotstklFac.SORTIERUNG_GUENST_PREIS1,
					LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.guenstpreis") + " 1");
		}
		if (ekagDto.getNMenge2() != null && ekagDto.getNMenge2().doubleValue() > 0) {
			mSort.put(AngebotstklFac.SORTIERUNG_GUENST_PREIS2,
					LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.guenstpreis") + " 2");
		}
		if (ekagDto.getNMenge3() != null && ekagDto.getNMenge3().doubleValue() > 0) {
			mSort.put(AngebotstklFac.SORTIERUNG_GUENST_PREIS3,
					LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.guenstpreis") + " 3");
		}
		if (ekagDto.getNMenge4() != null && ekagDto.getNMenge4().doubleValue() > 0) {
			mSort.put(AngebotstklFac.SORTIERUNG_GUENST_PREIS4,
					LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.guenstpreis") + " 4");
		}
		if (ekagDto.getNMenge5() != null && ekagDto.getNMenge5().doubleValue() > 0) {
			mSort.put(AngebotstklFac.SORTIERUNG_GUENST_PREIS5,
					LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.guenstpreis") + " 5");
		}

		wcbSortierung.setMap(mSort);

		wcbSortierung.setKeyOfSelectedItem(ekagDto.getISortierung());

		Map m = new LinkedHashMap();

		if (ekagDto.getNMenge1() != null && ekagDto.getNMenge1().doubleValue() > 0) {
			m.put(AngebotstklFac.MENGE_1, Helper.formatZahl(ekagDto.getNMenge1(), 2, LPMain.getTheClient().getLocUi()));
		}
		if (ekagDto.getNMenge2() != null && ekagDto.getNMenge2().doubleValue() > 0) {
			m.put(AngebotstklFac.MENGE_2, Helper.formatZahl(ekagDto.getNMenge2(), 2, LPMain.getTheClient().getLocUi()));
		}
		if (ekagDto.getNMenge3() != null && ekagDto.getNMenge3().doubleValue() > 0) {
			m.put(AngebotstklFac.MENGE_3, Helper.formatZahl(ekagDto.getNMenge3(), 2, LPMain.getTheClient().getLocUi()));
		}
		if (ekagDto.getNMenge4() != null && ekagDto.getNMenge4().doubleValue() > 0) {
			m.put(AngebotstklFac.MENGE_4, Helper.formatZahl(ekagDto.getNMenge4(), 2, LPMain.getTheClient().getLocUi()));
		}
		if (ekagDto.getNMenge5() != null && ekagDto.getNMenge5().doubleValue() > 0) {
			m.put(AngebotstklFac.MENGE_5, Helper.formatZahl(ekagDto.getNMenge5(), 2, LPMain.getTheClient().getLocUi()));
		}

		wcbMengen.removeActionListener(this);
		wcbMengen.setMap(m);

		wnfLieferzeitInKW.setInteger(ekagDto.getIOptimierenLieferzeit());
		wcbMengen.setKeyOfSelectedItem(ekagDto.getIOptimierenMenge());

		wcbMengen.addActionListener(this);

		wcbMinBest.setSelected(Helper.short2boolean(ekagDto.getBOptimierenMinmenge()));
		wcbVerpackungeinheit.setSelected(Helper.short2boolean(ekagDto.getBOptimierenVerpackungseinheit()));

		// Inhalte

		int iNachkommastellenMenge = Defaults.getInstance().getIUINachkommastellenMenge();
		int iNachkommastellenPreis = Defaults.getInstance().getIUINachkommastellenPreiseEK();

		optDto = DelegateFactory.getInstance().getAngebotstklDelegate().getEkagLieferantoptimierenDto(
				einkaufsangebotIId, wnfLieferzeitInKW.getInteger(), (Integer) wcbMengen.getKeyOfSelectedItem(),
				wcbMinBest.isSelected(), wcbVerpackungeinheit.isSelected(),
				(Integer) wcbSortierung.getKeyOfSelectedItem(), wtfSucheArtikelnummer.getText(),
				wtfSucheBezeichnung.getText());

		colNames[iSpalteMenge1] += hakerlBestellt(AngebotstklFac.MENGE_1);
		colNames[iSpalteMenge2] += hakerlBestellt(AngebotstklFac.MENGE_2);
		colNames[iSpalteMenge3] += hakerlBestellt(AngebotstklFac.MENGE_3);
		colNames[iSpalteMenge4] += hakerlBestellt(AngebotstklFac.MENGE_4);
		colNames[iSpalteMenge5] += hakerlBestellt(AngebotstklFac.MENGE_5);

		ArrayList<Object[]> alInhalt = new ArrayList<Object[]>();

		iZeile = 1;

		final String[] tooltips = new String[optDto.getPos().size()];

		for (int i = 0; i < optDto.getPos().size(); i++) {
			EinkaufsangebotpositionDtoFuerOptimieren ekagPosDto = optDto.getPos().get(i);

			Object[] zeile = new Object[iAnzahlSpalten];

			zeile[iSpaltePositionsmenge] = Helper.formatZahl(ekagPosDto.getEinkaufsangebotpositionDto().getNMenge(),
					iNachkommastellenMenge, LPMain.getTheClient().getLocUi());

			zeile[iSpaltePositionseinheit] = ekagPosDto.getEinkaufsangebotpositionDto().getEinheitCNr().trim();

			String artikel = "";
			String artikelBez = "";
			String artikelZbez = "";

			BigDecimal verfuegbar = null;
			
			String herstellernrAusArtikel="";

			if (ekagPosDto.getEinkaufsangebotpositionDto().getPositionsartCNr()
					.equals(AngebotstklFac.POSITIONSART_AGSTKL_HANDEINGABE)) {

				artikelBez = ekagPosDto.getEinkaufsangebotpositionDto().getCBez();
				artikelZbez = ekagPosDto.getEinkaufsangebotpositionDto().getCZusatzbez();

			} else {

				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(ekagPosDto.getEinkaufsangebotpositionDto().getArtikelIId());

				artikel = artikelDto.getCNr();
				artikelBez = artikelDto.getCBezAusSpr();
				artikelZbez = artikelDto.getCZBezAusSpr();

				BigDecimal lagerstand = DelegateFactory.getInstance().getLagerDelegate()
						.getLagerstandAllerLagerEinesMandanten(artikelDto.getIId(), false);

				BigDecimal fehlmengen = DelegateFactory.getInstance().getFehlmengeDelegate()
						.getAnzahlFehlmengeEinesArtikels(artikelDto.getIId());
				BigDecimal reservierungen = DelegateFactory.getInstance().getReservierungDelegate()
						.getAnzahlReservierungen(artikelDto.getIId());

				verfuegbar = lagerstand.subtract(reservierungen).subtract(fehlmengen);

				zeile[iSpalteVerfuegbar] = Helper.formatZahl(verfuegbar, iNachkommastellenMenge,
						LPMain.getTheClient().getLocUi());
				
				if(artikelDto.getCArtikelnrhersteller()!=null) {
					herstellernrAusArtikel = artikelDto.getCArtikelnrhersteller();
				}

			}

			zeile[iSpalteArtikel] = artikel;
			zeile[iSpalteBezeichnung] = artikelBez;
			zeile[iSpalteZusatzbezeichnung] = artikelZbez;

			if(ekagPosDto.getEinkaufsangebotpositionDto().getCArtikelnrhersteller()!=null) {
				zeile[iSpalteHerstellernummer] = ekagPosDto.getEinkaufsangebotpositionDto().getCArtikelnrhersteller();
			}else {
				zeile[iSpalteHerstellernummer] = herstellernrAusArtikel;
			}

			HashMap<Integer, String> tooltip = new HashMap<Integer, String>();

			// 1
			if (ekagDto.getNMenge1().doubleValue() != 0) {
				zeile[iSpalteMenge1] = Helper.formatZahl(
						ekagDto.getNMenge1().multiply(ekagPosDto.getEinkaufsangebotpositionDto().getNMenge()),
						iNachkommastellenMenge, LPMain.getTheClient().getLocUi());
			}
			if (ekagPosDto.getGuenstigsterLieferantMenge1() != null) {

				PositionlieferantDto posliefDto = ekagPosDto.getGuenstigsterLieferantMenge1();

				zeile[iSpalteGuenstPreis1] = Helper.formatZahl(posliefDto.getNPreisMenge1(), iNachkommastellenPreis,
						LPMain.getTheClient().getLocUi());

				EkaglieferantDto ekaglieferantDtoBilligster = DelegateFactory.getInstance().getAngebotstklDelegate()
						.ekaglieferantFindByPrimaryKey(posliefDto.getEgaklieferantIId());
				LieferantDto lfDtoBilligster = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(ekaglieferantDtoBilligster.getLieferantIId());

				String[] bezeichnungen = Helper.intelligenteWorttrennung(new int[] { 8 },
						lfDtoBilligster.getPartnerDto().getCKbez());

				zeile[iSpalteLiefKbez1] = bezeichnungen[0];

				tooltip = addTooltip(posliefDto, tooltip, lfDtoBilligster, true);

				zeile[iSpalteLieferzeit1] = posliefDto.getILieferzeitinkw();
				if (posliefDto.getNVerpackungseinheit() != null) {
					zeile[iSpalteVPE1] = Helper.formatZahl(posliefDto.getNVerpackungseinheit(), iNachkommastellenMenge,
							LPMain.getTheClient().getLocUi());
				}

			}

			if (ekagDto.getNMenge1().doubleValue() > 0) {
				zeile[iSpalteLFUebst1] = getPartnerInfo(
						ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge1());
				if (ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge1() != null) {
					PositionlieferantDto posliefDto = DelegateFactory.getInstance().getAngebotstklDelegate()
							.positionlieferantFindByPrimaryKeyInZielWaehrung(
									ekagPosDto.getEinkaufsangebotpositionDto()
											.getPositionlieferantIIdUebersteuertMenge1(),
									LPMain.getTheClient().getSMandantenwaehrung());
					if (posliefDto.getNPreisMenge1() != null) {
						zeile[iSpalteUebstPreis1] = Helper.formatZahl(posliefDto.getNPreisMenge1(),
								iNachkommastellenPreis, LPMain.getTheClient().getLocUi());
					}
					zeile[iSpalteLieferzeitUebst1] = posliefDto.getILieferzeitinkw();

					
					if (posliefDto.getNVerpackungseinheit() != null) {
						zeile[iSpalteVPEUebst1] = Helper.formatZahl(posliefDto.getNVerpackungseinheit(), iNachkommastellenMenge,
								LPMain.getTheClient().getLocUi());
					}
					
					tooltip = addTooltip(posliefDto, tooltip, null, false);

				}

			}

			// 2
			if (ekagDto.getNMenge2().doubleValue() != 0) {
				zeile[iSpalteMenge2] = Helper.formatZahl(
						ekagDto.getNMenge2().multiply(ekagPosDto.getEinkaufsangebotpositionDto().getNMenge()),
						iNachkommastellenMenge, LPMain.getTheClient().getLocUi());
			}
			if (ekagPosDto.getGuenstigsterLieferantMenge2() != null) {

				PositionlieferantDto posliefDto = ekagPosDto.getGuenstigsterLieferantMenge2();

				zeile[iSpalteGuenstPreis2] = Helper.formatZahl(posliefDto.getNPreisMenge2(), iNachkommastellenPreis,
						LPMain.getTheClient().getLocUi());

				EkaglieferantDto ekaglieferantDtoBilligster = DelegateFactory.getInstance().getAngebotstklDelegate()
						.ekaglieferantFindByPrimaryKey(posliefDto.getEgaklieferantIId());
				LieferantDto lfDtoBilligster = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(ekaglieferantDtoBilligster.getLieferantIId());

				String[] bezeichnungen = Helper.intelligenteWorttrennung(new int[] { 8 },
						lfDtoBilligster.getPartnerDto().getCKbez());

				zeile[iSpalteLiefKbez2] = bezeichnungen[0];

				tooltip = addTooltip(posliefDto, tooltip, lfDtoBilligster, true);

				zeile[iSpalteLieferzeit2] = posliefDto.getILieferzeitinkw();
				if (posliefDto.getNVerpackungseinheit() != null) {
					zeile[iSpalteVPE2] = Helper.formatZahl(posliefDto.getNVerpackungseinheit(), iNachkommastellenMenge,
							LPMain.getTheClient().getLocUi());
				}

			}

			if (ekagDto.getNMenge2().doubleValue() > 0) {
				zeile[iSpalteLFUebst2] = getPartnerInfo(
						ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge2());

				if (ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge2() != null) {
					PositionlieferantDto posliefDto = DelegateFactory.getInstance().getAngebotstklDelegate()
							.positionlieferantFindByPrimaryKey(ekagPosDto.getEinkaufsangebotpositionDto()
									.getPositionlieferantIIdUebersteuertMenge2());
					if (posliefDto.getNPreisMenge2() != null) {
						zeile[iSpalteUebstPreis2] = Helper.formatZahl(posliefDto.getNPreisMenge2(),
								iNachkommastellenPreis, LPMain.getTheClient().getLocUi());
					}
					zeile[iSpalteLieferzeitUebst2] = posliefDto.getILieferzeitinkw();

					if (posliefDto.getNVerpackungseinheit() != null) {
						zeile[iSpalteVPEUebst2] = Helper.formatZahl(posliefDto.getNVerpackungseinheit(), iNachkommastellenMenge,
								LPMain.getTheClient().getLocUi());
					}
					
					tooltip = addTooltip(posliefDto, tooltip, null, false);
				}

			}

			// 3
			if (ekagDto.getNMenge3().doubleValue() != 0) {
				zeile[iSpalteMenge3] = Helper.formatZahl(
						ekagDto.getNMenge3().multiply(ekagPosDto.getEinkaufsangebotpositionDto().getNMenge()),
						iNachkommastellenMenge, LPMain.getTheClient().getLocUi());
			}
			if (ekagPosDto.getGuenstigsterLieferantMenge3() != null) {

				PositionlieferantDto posliefDto = ekagPosDto.getGuenstigsterLieferantMenge3();

				zeile[iSpalteGuenstPreis3] = Helper.formatZahl(posliefDto.getNPreisMenge3(), iNachkommastellenPreis,
						LPMain.getTheClient().getLocUi());

				EkaglieferantDto ekaglieferantDtoBilligster = DelegateFactory.getInstance().getAngebotstklDelegate()
						.ekaglieferantFindByPrimaryKey(posliefDto.getEgaklieferantIId());
				LieferantDto lfDtoBilligster = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(ekaglieferantDtoBilligster.getLieferantIId());

				String[] bezeichnungen = Helper.intelligenteWorttrennung(new int[] { 8 },
						lfDtoBilligster.getPartnerDto().getCKbez());

				zeile[iSpalteLiefKbez3] = bezeichnungen[0];

				tooltip = addTooltip(posliefDto, tooltip, lfDtoBilligster, true);

				zeile[iSpalteLieferzeit3] = posliefDto.getILieferzeitinkw();
				if (posliefDto.getNVerpackungseinheit() != null) {
					zeile[iSpalteVPE3] = Helper.formatZahl(posliefDto.getNVerpackungseinheit(), iNachkommastellenMenge,
							LPMain.getTheClient().getLocUi());
				}

			}
			if (ekagDto.getNMenge3().doubleValue() > 0) {
				zeile[iSpalteLFUebst3] = getPartnerInfo(
						ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge3());
				if (ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge3() != null) {
					PositionlieferantDto posliefDto = DelegateFactory.getInstance().getAngebotstklDelegate()
							.positionlieferantFindByPrimaryKey(ekagPosDto.getEinkaufsangebotpositionDto()
									.getPositionlieferantIIdUebersteuertMenge3());
					if (posliefDto.getNPreisMenge3() != null) {
						zeile[iSpalteUebstPreis3] = Helper.formatZahl(posliefDto.getNPreisMenge3(),
								iNachkommastellenPreis, LPMain.getTheClient().getLocUi());
					}
					zeile[iSpalteLieferzeitUebst3] = posliefDto.getILieferzeitinkw();

					if (posliefDto.getNVerpackungseinheit() != null) {
						zeile[iSpalteVPEUebst3] = Helper.formatZahl(posliefDto.getNVerpackungseinheit(), iNachkommastellenMenge,
								LPMain.getTheClient().getLocUi());
					}
					
					tooltip = addTooltip(posliefDto, tooltip, null, false);
				}
			}
			// 4
			if (ekagDto.getNMenge4().doubleValue() != 0) {
				zeile[iSpalteMenge4] = Helper.formatZahl(
						ekagDto.getNMenge4().multiply(ekagPosDto.getEinkaufsangebotpositionDto().getNMenge()),
						iNachkommastellenMenge, LPMain.getTheClient().getLocUi());
			}
			if (ekagPosDto.getGuenstigsterLieferantMenge4() != null) {

				PositionlieferantDto posliefDto = ekagPosDto.getGuenstigsterLieferantMenge4();

				zeile[iSpalteGuenstPreis4] = Helper.formatZahl(posliefDto.getNPreisMenge4(), iNachkommastellenPreis,
						LPMain.getTheClient().getLocUi());

				EkaglieferantDto ekaglieferantDtoBilligster = DelegateFactory.getInstance().getAngebotstklDelegate()
						.ekaglieferantFindByPrimaryKey(posliefDto.getEgaklieferantIId());
				LieferantDto lfDtoBilligster = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(ekaglieferantDtoBilligster.getLieferantIId());

				String[] bezeichnungen = Helper.intelligenteWorttrennung(new int[] { 8 },
						lfDtoBilligster.getPartnerDto().getCKbez());

				zeile[iSpalteLiefKbez4] = bezeichnungen[0];

				tooltip = addTooltip(posliefDto, tooltip, lfDtoBilligster, true);

				zeile[iSpalteLieferzeit4] = posliefDto.getILieferzeitinkw();
				if (posliefDto.getNVerpackungseinheit() != null) {
					zeile[iSpalteVPE4] = Helper.formatZahl(posliefDto.getNVerpackungseinheit(), iNachkommastellenMenge,
							LPMain.getTheClient().getLocUi());
				}

			}
			if (ekagDto.getNMenge4().doubleValue() > 0) {
				zeile[iSpalteLFUebst4] = getPartnerInfo(
						ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge4());

				if (ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge4() != null) {
					PositionlieferantDto posliefDto = DelegateFactory.getInstance().getAngebotstklDelegate()
							.positionlieferantFindByPrimaryKey(ekagPosDto.getEinkaufsangebotpositionDto()
									.getPositionlieferantIIdUebersteuertMenge4());
					if (posliefDto.getNPreisMenge4() != null) {
						zeile[iSpalteUebstPreis4] = Helper.formatZahl(posliefDto.getNPreisMenge4(),
								iNachkommastellenPreis, LPMain.getTheClient().getLocUi());
					}
					zeile[iSpalteLieferzeitUebst4] = posliefDto.getILieferzeitinkw();

					if (posliefDto.getNVerpackungseinheit() != null) {
						zeile[iSpalteVPEUebst4] = Helper.formatZahl(posliefDto.getNVerpackungseinheit(), iNachkommastellenMenge,
								LPMain.getTheClient().getLocUi());
					}
					
					tooltip = addTooltip(posliefDto, tooltip, null, false);
				}
			}
			// 5
			if (ekagDto.getNMenge5().doubleValue() != 0) {
				zeile[iSpalteMenge5] = Helper.formatZahl(
						ekagDto.getNMenge5().multiply(ekagPosDto.getEinkaufsangebotpositionDto().getNMenge()),
						iNachkommastellenMenge, LPMain.getTheClient().getLocUi());
			}
			if (ekagPosDto.getGuenstigsterLieferantMenge5() != null) {

				PositionlieferantDto posliefDto = ekagPosDto.getGuenstigsterLieferantMenge5();

				zeile[iSpalteGuenstPreis5] = Helper.formatZahl(posliefDto.getNPreisMenge5(), iNachkommastellenPreis,
						LPMain.getTheClient().getLocUi());

				EkaglieferantDto ekaglieferantDtoBilligster = DelegateFactory.getInstance().getAngebotstklDelegate()
						.ekaglieferantFindByPrimaryKey(posliefDto.getEgaklieferantIId());
				LieferantDto lfDtoBilligster = DelegateFactory.getInstance().getLieferantDelegate()
						.lieferantFindByPrimaryKey(ekaglieferantDtoBilligster.getLieferantIId());

				String[] bezeichnungen = Helper.intelligenteWorttrennung(new int[] { 8 },
						lfDtoBilligster.getPartnerDto().getCKbez());

				zeile[iSpalteLiefKbez5] = bezeichnungen[0];

				tooltip = addTooltip(posliefDto, tooltip, lfDtoBilligster, true);

				zeile[iSpalteLieferzeit5] = posliefDto.getILieferzeitinkw();
				if (posliefDto.getNVerpackungseinheit() != null) {
					zeile[iSpalteVPE5] = Helper.formatZahl(posliefDto.getNVerpackungseinheit(), iNachkommastellenMenge,
							LPMain.getTheClient().getLocUi());
				}

			}
			if (ekagDto.getNMenge5().doubleValue() > 0) {
				zeile[iSpalteLFUebst5] = getPartnerInfo(
						ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge5());
				if (ekagPosDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge5() != null) {
					PositionlieferantDto posliefDto = DelegateFactory.getInstance().getAngebotstklDelegate()
							.positionlieferantFindByPrimaryKey(ekagPosDto.getEinkaufsangebotpositionDto()
									.getPositionlieferantIIdUebersteuertMenge5());
					if (posliefDto.getNPreisMenge5() != null) {
						zeile[iSpalteUebstPreis5] = Helper.formatZahl(posliefDto.getNPreisMenge5(),
								iNachkommastellenPreis, LPMain.getTheClient().getLocUi());
					}
					zeile[iSpalteLieferzeitUebst5] = posliefDto.getILieferzeitinkw();

					if (posliefDto.getNVerpackungseinheit() != null) {
						zeile[iSpalteVPEUebst5] = Helper.formatZahl(posliefDto.getNVerpackungseinheit(), iNachkommastellenMenge,
								LPMain.getTheClient().getLocUi());
					}
					
					tooltip = addTooltip(posliefDto, tooltip, null, false);
				}
			}

			if (tooltip.size() > 0) {
				zeile[iSpalteKommentareVorhanden] = "K";
			}

			alInhalt.add(zeile);

			if (tooltip.size() > 0) {

				String toolTip = "";
				Iterator itTool = tooltip.keySet().iterator();
				while (itTool.hasNext()) {

					String text = tooltip.get(itTool.next());
					toolTip += text + "<br>";
				}
				tooltips[i] = "<html>" + toolTip + "</html>";
			}

			iZeile++;
		}

		int vorherigeZeile = 0;

		if (tableInhalt != null) {
			vorherigeZeile = tableInhalt.getSelectedRow();
		}

		final boolean bIstBestellt = istBereitsBestellt((Integer) wcbMengen.getKeyOfSelectedItem());

		MyTableModel1 model = new MyTableModel1(alInhalt.toArray(new Object[][] {}), colNames, bIstBestellt);

		tableInhalt = new JTable(model) {

			public String getToolTipText(MouseEvent e) {
				String tip = null;
				java.awt.Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				int colIndex = columnAtPoint(p);
				int realColumnIndex = convertColumnIndexToModel(colIndex);

				return tooltips[rowIndex];

			}

			// Implement table header tool tips.
			protected JTableHeader createDefaultTableHeader() {
				return new JTableHeader(columnModel) {
					public String getToolTipText(MouseEvent e) {
						String tip = null;
						java.awt.Point p = e.getPoint();
						int index = columnModel.getColumnIndexAtX(p.x);
						int realIndex = columnModel.getColumn(index).getModelIndex();
						return columnToolTips[realIndex];
					}
				};
			}

		};

		tableInhalt.registerKeyboardAction(new CopyAction(),
				KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_FOCUSED);

		tableInhalt.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if (e.getButton() == MouseEvent.BUTTON3) {

					JTable source = (JTable) e.getSource();
					int row = source.rowAtPoint(e.getPoint());
					int column = source.columnAtPoint(e.getPoint());

					Integer positionlieferantIId = null;

					EinkaufsangebotpositionDtoFuerOptimieren posDto = optDto.getPos().get(row);

					// Guenstigster Lieferant
					if (column == iSpalteLiefKbez1 && posDto.getGuenstigsterLieferantMenge1() != null) {
						positionlieferantIId = posDto.getGuenstigsterLieferantMenge1().getIId();
					}
					if (column == iSpalteLiefKbez2 && posDto.getGuenstigsterLieferantMenge2() != null) {
						positionlieferantIId = posDto.getGuenstigsterLieferantMenge2().getIId();
					}
					if (column == iSpalteLiefKbez3 && posDto.getGuenstigsterLieferantMenge3() != null) {
						positionlieferantIId = posDto.getGuenstigsterLieferantMenge3().getIId();
					}
					if (column == iSpalteLiefKbez4 && posDto.getGuenstigsterLieferantMenge4() != null) {
						positionlieferantIId = posDto.getGuenstigsterLieferantMenge4().getIId();
					}
					if (column == iSpalteLiefKbez5 && posDto.getGuenstigsterLieferantMenge5() != null) {
						positionlieferantIId = posDto.getGuenstigsterLieferantMenge5().getIId();
					}

					// Uebersteuerter Lieferant
					if (column == iSpalteLFUebst1 && posDto.getEinkaufsangebotpositionDto()
							.getPositionlieferantIIdUebersteuertMenge1() != null) {
						positionlieferantIId = posDto.getEinkaufsangebotpositionDto()
								.getPositionlieferantIIdUebersteuertMenge1();
					}
					if (column == iSpalteLFUebst2 && posDto.getEinkaufsangebotpositionDto()
							.getPositionlieferantIIdUebersteuertMenge2() != null) {
						positionlieferantIId = posDto.getEinkaufsangebotpositionDto()
								.getPositionlieferantIIdUebersteuertMenge2();
					}
					if (column == iSpalteLFUebst3 && posDto.getEinkaufsangebotpositionDto()
							.getPositionlieferantIIdUebersteuertMenge3() != null) {
						positionlieferantIId = posDto.getEinkaufsangebotpositionDto()
								.getPositionlieferantIIdUebersteuertMenge3();
					}
					if (column == iSpalteLFUebst4 && posDto.getEinkaufsangebotpositionDto()
							.getPositionlieferantIIdUebersteuertMenge4() != null) {
						positionlieferantIId = posDto.getEinkaufsangebotpositionDto()
								.getPositionlieferantIIdUebersteuertMenge4();
					}
					if (column == iSpalteLFUebst5 && posDto.getEinkaufsangebotpositionDto()
							.getPositionlieferantIIdUebersteuertMenge5() != null) {
						positionlieferantIId = posDto.getEinkaufsangebotpositionDto()
								.getPositionlieferantIIdUebersteuertMenge5();
					}
					try {
						if (positionlieferantIId != null) {

							DialogPositionLieferantKommentare d = new DialogPositionLieferantKommentare(
									positionlieferantIId, bIstBestellt, getInternalFrame());

							LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);

							d.setVisible(true);

							if (d.bSpeichen == true) {
								aktualisiereInhalt();
							}

						}
					} catch (Throwable e1) {
						e1.printStackTrace();
						handleException(e1, true);
					}

				}

			}
		});

		tableInhalt.setDefaultRenderer(BigDecimal.class, new MyBigDecimalCellRenderer(optDto));

		tableInhalt.setDefaultRenderer(Integer.class, new MyIntegerCellRenderer(optDto));
		tableInhalt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tableInhalt.setSelectionForeground(Color.BLACK);

		tableInhalt.setDefaultRenderer(Object.class, new TableCellRenderer() {
			private DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {

				System.out.println(row + ":" + column);
				Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);

				EinkaufsangebotpositionDtoFuerOptimieren ekagPosDto = optDto.getPos().get(row);
				if (ekagPosDto.getGuenstigsterLieferantMenge1() == null
						&& optDto.getEkagDto().getNMenge1().doubleValue() != 0
						&& column == PanelEkaglieferantOptimieren.iSpalteLiefKbez1) {
					c.setBackground(Color.RED);
				} else if (ekagPosDto.getGuenstigsterLieferantMenge2() == null
						&& optDto.getEkagDto().getNMenge2().doubleValue() != 0
						&& column == PanelEkaglieferantOptimieren.iSpalteLiefKbez2) {
					c.setBackground(Color.RED);
				} else if (ekagPosDto.getGuenstigsterLieferantMenge3() == null
						&& optDto.getEkagDto().getNMenge3().doubleValue() != 0
						&& column == PanelEkaglieferantOptimieren.iSpalteLiefKbez3) {
					c.setBackground(Color.RED);
				} else if (ekagPosDto.getGuenstigsterLieferantMenge4() == null
						&& optDto.getEkagDto().getNMenge4().doubleValue() != 0
						&& column == PanelEkaglieferantOptimieren.iSpalteLiefKbez4) {
					c.setBackground(Color.RED);
				} else if (ekagPosDto.getGuenstigsterLieferantMenge5() == null
						&& optDto.getEkagDto().getNMenge5().doubleValue() != 0
						&& column == PanelEkaglieferantOptimieren.iSpalteLiefKbez5) {
					c.setBackground(Color.RED);
				} else {
					if (row % 2 == 0) {
						c.setBackground(Color.WHITE);
					} else {
						c.setBackground(Color.LIGHT_GRAY);
					}
				}

				if (isSelected) {
					c.setBackground(new Color(187, 240, 236));
				}

				return c;
			}

		});

		tableInhalt.setRowHeight(17);

		Action delete = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				JTable table = (JTable) e.getSource();

				String rowCol = e.getActionCommand();

				String[] teile = rowCol.split("x");

				int modelRow = Integer.valueOf(teile[0]);

				Integer modelCol = Integer.valueOf(teile[1]);

				int iZeilen = optDto.getPos().size();

				EinkaufsangebotpositionDtoFuerOptimieren posDto = optDto.getPos().get(modelRow);

				try {

					if (e.getID() == ACTION_LIEFERANT_UEBERSTEUERN) {

						if (modelCol == iSpalteLFUebst1) {
							dialogQueryPositionlieferantFromListe(rowCol,
									posDto.getEinkaufsangebotpositionDto().getIId(),
									posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge1(),
									AngebotstklFac.MENGE_1);
						}

						else if (modelCol == iSpalteLFUebst2) {
							dialogQueryPositionlieferantFromListe(rowCol,
									posDto.getEinkaufsangebotpositionDto().getIId(),
									posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge2(),
									AngebotstklFac.MENGE_2);
						} else if (modelCol == iSpalteLFUebst3) {
							dialogQueryPositionlieferantFromListe(rowCol,
									posDto.getEinkaufsangebotpositionDto().getIId(),
									posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge3(),
									AngebotstklFac.MENGE_3);
						} else if (modelCol == iSpalteLFUebst4) {
							dialogQueryPositionlieferantFromListe(rowCol,
									posDto.getEinkaufsangebotpositionDto().getIId(),
									posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge4(),
									AngebotstklFac.MENGE_4);
						} else if (modelCol == iSpalteLFUebst5) {
							dialogQueryPositionlieferantFromListe(rowCol,
									posDto.getEinkaufsangebotpositionDto().getIId(),
									posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge5(),
									AngebotstklFac.MENGE_5);
						}
					}

				} catch (Throwable e1) {
					e1.printStackTrace();
					handleException(e1, true);
				}

			}
		};
		if (ekagDto.getNMenge1().doubleValue() > 0) {
			ButtonColumn buttonColumn = new ButtonColumn(tableInhalt, delete, iSpalteLFUebst1);
		}
		if (ekagDto.getNMenge2().doubleValue() > 0) {
			ButtonColumn buttonColumn2 = new ButtonColumn(tableInhalt, delete, iSpalteLFUebst2);
		}
		if (ekagDto.getNMenge3().doubleValue() > 0) {
			ButtonColumn buttonColumn3 = new ButtonColumn(tableInhalt, delete, iSpalteLFUebst3);
		}
		if (ekagDto.getNMenge4().doubleValue() > 0) {
			ButtonColumn buttonColumn4 = new ButtonColumn(tableInhalt, delete, iSpalteLFUebst4);
		}

		if (ekagDto.getNMenge5().doubleValue() > 0) {
			ButtonColumn buttonColumn5 = new ButtonColumn(tableInhalt, delete, iSpalteLFUebst5);
		}

		tableInhalt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableInhalt.getColumnModel().getColumn(iSpaltePositionsmenge).setPreferredWidth(100);
		tableInhalt.getColumnModel().getColumn(iSpaltePositionseinheit).setPreferredWidth(30);
		tableInhalt.getColumnModel().getColumn(iSpalteArtikel).setPreferredWidth(200);
		tableInhalt.getColumnModel().getColumn(iSpalteBezeichnung).setPreferredWidth(250);
		tableInhalt.getColumnModel().getColumn(iSpalteZusatzbezeichnung).setPreferredWidth(250);
		tableInhalt.getColumnModel().getColumn(iSpalteHerstellernummer).setPreferredWidth(100);
		tableInhalt.getColumnModel().getColumn(iSpalteKommentareVorhanden).setPreferredWidth(20);
		tableInhalt.getColumnModel().getColumn(iSpalteVerfuegbar).setPreferredWidth(90);
		tableInhalt.getColumnModel().getColumn(iSpalteMenge1).setPreferredWidth(90);
		tableInhalt.getColumnModel().getColumn(iSpalteGuenstPreis1).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteUebstPreis1).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteLieferzeitUebst1).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteLiefKbez1).setPreferredWidth(50);
		tableInhalt.getColumnModel().getColumn(iSpalteLieferzeit1).setPreferredWidth(35);
		tableInhalt.getColumnModel().getColumn(iSpalteVPE1).setPreferredWidth(55);
		tableInhalt.getColumnModel().getColumn(iSpalteLFUebst1).setPreferredWidth(80);
		tableInhalt.getColumnModel().getColumn(iSpalteVPEUebst1).setPreferredWidth(55);

		tableInhalt.getColumnModel().getColumn(iSpalteMenge2).setPreferredWidth(90);
		tableInhalt.getColumnModel().getColumn(iSpalteGuenstPreis2).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteUebstPreis2).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteLieferzeitUebst2).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteLiefKbez2).setPreferredWidth(50);
		tableInhalt.getColumnModel().getColumn(iSpalteLieferzeit2).setPreferredWidth(35);
		tableInhalt.getColumnModel().getColumn(iSpalteVPE2).setPreferredWidth(55);
		tableInhalt.getColumnModel().getColumn(iSpalteLFUebst2).setPreferredWidth(80);
		tableInhalt.getColumnModel().getColumn(iSpalteVPEUebst2).setPreferredWidth(55);

		tableInhalt.getColumnModel().getColumn(iSpalteMenge3).setPreferredWidth(90);
		tableInhalt.getColumnModel().getColumn(iSpalteGuenstPreis3).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteUebstPreis3).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteLieferzeitUebst3).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteLiefKbez3).setPreferredWidth(50);
		tableInhalt.getColumnModel().getColumn(iSpalteLieferzeit3).setPreferredWidth(35);
		tableInhalt.getColumnModel().getColumn(iSpalteVPE3).setPreferredWidth(55);
		tableInhalt.getColumnModel().getColumn(iSpalteLFUebst3).setPreferredWidth(80);
		tableInhalt.getColumnModel().getColumn(iSpalteVPEUebst3).setPreferredWidth(55);

		tableInhalt.getColumnModel().getColumn(iSpalteMenge4).setPreferredWidth(90);
		tableInhalt.getColumnModel().getColumn(iSpalteGuenstPreis4).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteUebstPreis4).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteLieferzeitUebst4).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteLiefKbez4).setPreferredWidth(50);
		tableInhalt.getColumnModel().getColumn(iSpalteLieferzeit4).setPreferredWidth(35);
		tableInhalt.getColumnModel().getColumn(iSpalteVPE4).setPreferredWidth(55);
		tableInhalt.getColumnModel().getColumn(iSpalteLFUebst4).setPreferredWidth(80);
		tableInhalt.getColumnModel().getColumn(iSpalteVPEUebst4).setPreferredWidth(55);

		tableInhalt.getColumnModel().getColumn(iSpalteMenge5).setPreferredWidth(90);
		tableInhalt.getColumnModel().getColumn(iSpalteGuenstPreis5).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteUebstPreis5).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteLieferzeitUebst5).setPreferredWidth(70);
		tableInhalt.getColumnModel().getColumn(iSpalteLiefKbez5).setPreferredWidth(50);
		tableInhalt.getColumnModel().getColumn(iSpalteLieferzeit5).setPreferredWidth(35);
		tableInhalt.getColumnModel().getColumn(iSpalteVPE5).setPreferredWidth(55);
		tableInhalt.getColumnModel().getColumn(iSpalteLFUebst5).setPreferredWidth(80);
		tableInhalt.getColumnModel().getColumn(iSpalteVPEUebst5).setPreferredWidth(55);

		jspScrollPane.setViewportView(tableInhalt);

		// jspScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		if (bIstBestellt) {
			wlaBestellt.setText(" "
					+ LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.bestellen.bestellt"));
			wnfLieferzeitInKW.setEnabled(false);
			wcbMinBest.setEnabled(false);
			wcbVerpackungeinheit.setEnabled(false);
		} else {
			wlaBestellt.setText(" "
					+ LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.bestellen.nochnicht"));
			wnfLieferzeitInKW.setEnabled(true);
			wcbMinBest.setEnabled(true);
			wcbVerpackungeinheit.setEnabled(true);
		}

		jpaWorkingOn.revalidate();

	}

	protected void eventKeyTyped(KeyEvent e) throws Throwable {
		refreshBlau();
	}

	protected void eventKeyPressed(KeyEvent e) throws Throwable {

	}

	protected void eventKeyReleased(KeyEvent e) throws Throwable {

	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		jpaKriterien = new JPanel(new GridBagLayout());

		wlaLieferzeitInKW.setText(LPMain.getTextRespectUISPr("agstkl.positionlieferant.lieferzeitinkw"));
		wnfLieferzeitInKW.setFractionDigits(0);
		wcbMengen.setMandatoryField(true);
		wcbMengen.addActionListener(this);

		wlaSortierung.setText(LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.sort"));
		wcbSortierung.setMandatoryField(true);

		wcbMinBest.setText(LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.minbest"));

		wcbVerpackungeinheit.setText(LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.vpe"));

		jspScrollPane.setViewportView(tableInhalt);
		jspScrollPane.setAutoscrolls(true);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		wlaSucheArtikelnummer.setText(LPMain.getTextRespectUISPr("artikel.artikelnummer") + " (Aa|*A*)");
		wlaSucheBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung") + " (Aa|*A*)");
		jpaKriterien.add(wlaSucheArtikelnummer, new GridBagConstraints(0, iZeile, 1, 1, .65, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaKriterien.add(wtfSucheArtikelnummer, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaKriterien.add(wlaSucheBezeichnung, new GridBagConstraints(2, iZeile, 1, 1, .6, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaKriterien.add(wtfSucheBezeichnung, new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaKriterien.add(wlaSortierung, new GridBagConstraints(4, iZeile, 1, 1, .4, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaKriterien.add(wcbSortierung, new GridBagConstraints(5, iZeile, 1, 1, .4, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaKriterien.add(wlaLieferzeitInKW, new GridBagConstraints(0, iZeile, 1, 1, .5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaKriterien.add(wnfLieferzeitInKW, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaKriterien.add(wcbMengen, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaKriterien.add(wcbMinBest, new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaKriterien.add(wcbVerpackungeinheit, new GridBagConstraints(4, iZeile, 2, 1, 1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		this.add(jpaKriterien, new GridBagConstraints(0, 1, 1, 1, 1.0, 0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jspScrollPane, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		wcbVerpackungeinheit.addActionListener(this);
		wcbMinBest.addActionListener(this);
		wnfLieferzeitInKW.addKeyListener(this);
		wtfSucheArtikelnummer.addKeyListener(this);
		wtfSucheBezeichnung.addKeyListener(this);

		/*
		 * wcbSortierung.addItemListener(new ItemListener() { public void
		 * itemStateChanged(ItemEvent evt) { if (evt.getStateChange() ==
		 * ItemEvent.SELECTED) { refreshBlau(); } } });
		 */

		aktualisiereInhalt();

		String[] aWhichButtonIUse = {};

		getToolBar().addButtonCenter("/com/lp/client/res/data_into.png",
				LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.goto.ekaglieferant"),
				ACTION_SPECIAL_GOTOEKAGLIEFERANTPOSITION, null, null);
		enableToolsPanelButtons(true, ACTION_SPECIAL_GOTOEKAGLIEFERANTPOSITION);

		getToolBar().addButtonCenter("/com/lp/client/res/cube_green.png",
				LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.artikelnummerlf.speichern"),
				ACTION_SPECIAL_ARTIKELNUMMERLF_SPEICHERN, null, null);
		enableToolsPanelButtons(true, ACTION_SPECIAL_ARTIKELNUMMERLF_SPEICHERN);

		getToolBar().addButtonCenter("/com/lp/client/res/shoppingcart_full16x16.png",
				LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.bestellen"),
				ACTION_SPECIAL_BESTELLEN, null, null);
		enableToolsPanelButtons(true, ACTION_SPECIAL_BESTELLEN);
		wlaBestellt.setText(
				LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.bestellen.nochnicht"));

		getToolBar().getToolsPanelCenter().add(wlaBestellt);

		enableToolsPanelButtons(aWhichButtonIUse);
	}

	protected void setDefaults() {
	}

	protected void dto2Components() throws Throwable {

	}

	protected void components2Dto() throws Throwable {

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wcbMengen) && wcbMengen.getKeyOfSelectedItem() != null && optDto != null) {
			eventActionRefresh(null, true);
		}

		if (e.getSource().equals(wcbMinBest) || e.getSource().equals(wcbVerpackungeinheit)) {

			refreshBlau();

		} else {

			if (e.getActionCommand().equals(ACTION_SPECIAL_BESTELLEN)) {

				if (istBereitsBestellt((Integer) wcbMengen.getKeyOfSelectedItem())) {
					// zuruecknehmen

					boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(), LPMain.getTextRespectUISPr(
							"agstkl.einkaufsangebot.lieferantenoptimieren.bestellen.frage.zuruecknehmen"));
					if (b == true) {
						DelegateFactory.getInstance().getAngebotstklDelegate().lieferantenOptimierenbestellen(optDto,
								(Integer) wcbMengen.getKeyOfSelectedItem(), true);
					}

				} else {
					DelegateFactory.getInstance().getAngebotstklDelegate().lieferantenOptimierenbestellen(optDto,
							(Integer) wcbMengen.getKeyOfSelectedItem(), false);

				}

				aktualisiereInhalt();

			}
			if (e.getActionCommand().equals(ACTION_SPECIAL_ARTIKELNUMMERLF_SPEICHERN)) {

				if (istBereitsBestellt((Integer) wcbMengen.getKeyOfSelectedItem())) {

					Integer einkaufsangebotIId = ((InternalFrameAngebotstkl) getInternalFrame())
							.getTabbedPaneEinkaufsangebot().getEinkaufsangebotDto().getIId();

					DelegateFactory.getInstance().getAngebotstklDelegate()
							.lieferantenOptimierenArtikelnummerLFSpeichern(einkaufsangebotIId);
				} else {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), LPMain.getTextRespectUISPr(
							"agstkl.einkaufsangebot.optimieren.artikelnummerlfspeichern.nurbeipreisefixiert"));
				}

			}
			if (e.getActionCommand().equals(ACTION_SPECIAL_GOTOEKAGLIEFERANTPOSITION)) {
				Integer positionlieferantIId = null;

				int row = tableInhalt.getSelectedRow();
				int column = tableInhalt.getSelectedColumn();

				if (row > -1 && column > -1) {
					EinkaufsangebotpositionDtoFuerOptimieren zeile = optDto.getPos().get(row);
					// Wenn man eine Preisspalte selektiert hat

					if (column == iSpalteGuenstPreis1) {
						if (zeile.getGuenstigsterLieferantMenge1() != null) {
							positionlieferantIId = zeile.getGuenstigsterLieferantMenge1().getIId();
						}
					} else if (column == iSpalteGuenstPreis2) {
						if (zeile.getGuenstigsterLieferantMenge2() != null) {
							positionlieferantIId = zeile.getGuenstigsterLieferantMenge2().getIId();
						}
					} else if (column == iSpalteGuenstPreis3) {
						if (zeile.getGuenstigsterLieferantMenge3() != null) {
							positionlieferantIId = zeile.getGuenstigsterLieferantMenge3().getIId();
						}
					} else if (column == iSpalteGuenstPreis4) {
						if (zeile.getGuenstigsterLieferantMenge4() != null) {
							positionlieferantIId = zeile.getGuenstigsterLieferantMenge4().getIId();
						}
					} else if (column == iSpalteGuenstPreis5) {
						if (zeile.getGuenstigsterLieferantMenge5() != null) {
							positionlieferantIId = zeile.getGuenstigsterLieferantMenge5().getIId();
						}
					}

					if (column == iSpalteUebstPreis1) {

						positionlieferantIId = zeile.getEinkaufsangebotpositionDto()
								.getPositionlieferantIIdUebersteuertMenge1();

					} else if (column == iSpalteUebstPreis2) {

						positionlieferantIId = zeile.getEinkaufsangebotpositionDto()
								.getPositionlieferantIIdUebersteuertMenge2();

					} else if (column == iSpalteUebstPreis3) {

						positionlieferantIId = zeile.getEinkaufsangebotpositionDto()
								.getPositionlieferantIIdUebersteuertMenge3();

					} else if (column == iSpalteUebstPreis4) {

						positionlieferantIId = zeile.getEinkaufsangebotpositionDto()
								.getPositionlieferantIIdUebersteuertMenge4();

					} else if (column == iSpalteUebstPreis5) {

						positionlieferantIId = zeile.getEinkaufsangebotpositionDto()
								.getPositionlieferantIIdUebersteuertMenge5();

					}
				}

				if (positionlieferantIId != null) {
					((InternalFrameAngebotstkl) getInternalFrame()).getTabbedPaneEinkaufsangebot()
							.geheZuPositionsliefeferant(positionlieferantIId);
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
							LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.optimieren.goto.error"));
				}
			}
		}
	}

	private void refreshBlau() {
		LPButtonAction btn = getHmOfButtons().get(ACTION_REFRESH);
		btn.getButton().setOpaque(true);
		btn.getButton().setBackground(Color.BLUE);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getSource() == panelQueryFLRPositionslieferant) {

			if (panelQueryFLRPositionslieferant.getSource() != null) {

				String[] teile = ((String) panelQueryFLRPositionslieferant.getSource()).split("x");

				int modelRow = Integer.valueOf(teile[0]);

				Integer modelCol = Integer.valueOf(teile[1]);

				EinkaufsangebotpositionDtoFuerOptimieren posDto = optDto.getPos().get(modelRow);
				EinkaufsangebotpositionDto ekagposDto = posDto.getEinkaufsangebotpositionDto();

				Integer positionslieferantIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

					if (modelCol.equals(iSpalteLFUebst1)) {
						ekagposDto.setPositionlieferantIIdUebersteuertMenge1(positionslieferantIId);
					} else if (modelCol.equals(iSpalteLFUebst2)) {
						ekagposDto.setPositionlieferantIIdUebersteuertMenge2(positionslieferantIId);
					} else if (modelCol.equals(iSpalteLFUebst3)) {
						ekagposDto.setPositionlieferantIIdUebersteuertMenge3(positionslieferantIId);
					} else if (modelCol.equals(iSpalteLFUebst4)) {
						ekagposDto.setPositionlieferantIIdUebersteuertMenge4(positionslieferantIId);
					} else if (modelCol.equals(iSpalteLFUebst5)) {
						ekagposDto.setPositionlieferantIIdUebersteuertMenge5(positionslieferantIId);
					}

				} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
					if (modelCol.equals(iSpalteLFUebst1)) {
						ekagposDto.setPositionlieferantIIdUebersteuertMenge1(null);
					} else if (modelCol.equals(iSpalteLFUebst2)) {
						ekagposDto.setPositionlieferantIIdUebersteuertMenge2(null);
					} else if (modelCol.equals(iSpalteLFUebst3)) {
						ekagposDto.setPositionlieferantIIdUebersteuertMenge3(null);
					} else if (modelCol.equals(iSpalteLFUebst4)) {
						ekagposDto.setPositionlieferantIIdUebersteuertMenge4(null);
					} else if (modelCol.equals(iSpalteLFUebst5)) {
						ekagposDto.setPositionlieferantIIdUebersteuertMenge5(null);
					}
				}

				DelegateFactory.getInstance().getAngebotstklDelegate().updateEinkaufsangebotposition(ekagposDto);
			}

			aktualisiereInhalt();

			if (istBereitsBestellt((Integer) wcbMengen.getKeyOfSelectedItem())) {
				DelegateFactory.getInstance().getAngebotstklDelegate().lieferantenOptimierenbestellen(optDto,
						(Integer) wcbMengen.getKeyOfSelectedItem(), false);
			}

		}

	}

	private void dialogQueryPositionlieferantFromListe(Object source, Integer einkaufsangebotpositionIId,
			Integer selectedId, int iMenge) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

		FilterKriterium[] kriterien = new FilterKriterium[2];

		FilterKriterium krit1 = new FilterKriterium("einkaufsangebotposition_i_id", true,
				einkaufsangebotpositionIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		FilterKriterium krit2 = new FilterKriterium("MENGE", true, iMenge + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[1] = krit2;

		panelQueryFLRPositionslieferant = new PanelQueryFLR(null, kriterien,
				QueryParameters.UC_ID_POSITIONLIEFERANT_OPTIMIEREN, aWhichButtonIUse, getInternalFrame(),
				LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.lieferantauswaehlen"));
		panelQueryFLRPositionslieferant.setSource(source);
		panelQueryFLRPositionslieferant.setSelectedId(selectedId);
		panelQueryFLRPositionslieferant.uebersteuereSpaltenUeberschrift(10,
				LPMain.getTextRespectUISPr("lp.preis") + " "
						+ LPMain.getTextRespectUISPr("agstkl.einkaufsangebot.lieferantenoptimieren.ueberschrift.menge")
						+ " " + iMenge + " / " + LPMain.getTheClient().getSMandantenwaehrung());

		new DialogQuery(panelQueryFLRPositionslieferant);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (super.allMandatoryFieldsSetDlg()) {
			components2Dto();

		}
		super.eventActionSave(e, true);
	}
}

class ButtonColumn extends AbstractCellEditor
		implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener {
	private JTable table;
	private Action action;
	private int mnemonic;
	private Border originalBorder;
	private Border focusBorder;

	private JButton renderButton;
	private JButton editButton;
	private Object editorValue;
	private boolean isButtonColumnEditor;

	/**
	 * Create the ButtonColumn to be used as a renderer and editor. The renderer and
	 * editor will automatically be installed on the TableColumn of the specified
	 * column.
	 *
	 * @param table  the table containing the button renderer/editor
	 * @param action the Action to be invoked when the button is invoked
	 * @param column the column to which the button renderer/editor is added
	 */
	public ButtonColumn(JTable table, Action action, int column) {
		this.table = table;
		this.action = action;

		renderButton = new JButton();
		editButton = new JButton();
		editButton.setFocusPainted(false);
		editButton.addActionListener(this);
		editButton.addMouseListener(this);

		originalBorder = editButton.getBorder();
		// setFocusBorder(new LineBorder(Color.BLUE));

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(column).setCellRenderer(this);
		columnModel.getColumn(column).setCellEditor(this);

	}

	/**
	 * Get foreground color of the button when the cell has focus
	 *
	 * @return the foreground color
	 */
	public Border getFocusBorder() {
		return focusBorder;
	}

	/**
	 * The foreground color of the button when the cell has focus
	 *
	 * @param focusBorder the foreground color
	 */
	public void setFocusBorder(Border focusBorder) {
		this.focusBorder = focusBorder;
		editButton.setBorder(focusBorder);
	}

	public int getMnemonic() {
		return mnemonic;
	}

	public void setValueAt(Object value, int row, int col) {
		int i = 0;
	}

	/**
	 * The mnemonic to activate the button when the cell has focus
	 *
	 * @param mnemonic the mnemonic
	 */
	public void setMnemonic(int mnemonic) {
		this.mnemonic = mnemonic;
		renderButton.setMnemonic(mnemonic);
		editButton.setMnemonic(mnemonic);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value == null) {
			editButton.setText("");
			editButton.setIcon(null);
		} else if (value instanceof Icon) {
			editButton.setText("");
			editButton.setIcon((Icon) value);
		} else {
			// editButton.setText(value.toString());
			editButton.setIcon(null);
		}

		this.editorValue = value;
		return editButton;
	}

	@Override
	public Object getCellEditorValue() {
		return editorValue;
	}

//
//Implement TableCellRenderer interface
//
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (isSelected) {
			renderButton.setForeground(table.getSelectionForeground());
			renderButton.setBackground(new Color(187, 240, 236));
		} else {
			renderButton.setForeground(table.getForeground());
			renderButton.setBackground(UIManager.getColor("Button.background"));
		}

//	renderButton.setText( (value == null) ? "" : value.toString() );
		if (value == null) {
			renderButton.setText("");
			renderButton.setIcon(null);
		} else if (value instanceof Icon) {
			renderButton.setText("");
			renderButton.setIcon((Icon) value);
		} else {

			if (((String[]) value)[0] != null) {
				renderButton.setBackground(Color.GREEN);
				renderButton.setOpaque(true);
			}

			renderButton.setText(((String[]) value)[0]);
			renderButton.setToolTipText(((String[]) value)[1]);
			renderButton.setIcon(null);
		}

		return renderButton;
	}

//
//Implement ActionListener interface
//
	/*
	 * The button has been pressed. Stop editing and invoke the custom Action
	 */
	public void actionPerformed(ActionEvent e) {
		int row = table.convertRowIndexToModel(table.getEditingRow());

		int col = table.convertRowIndexToModel(table.getEditingColumn());

		fireEditingStopped();

		// Invoke the Action

		ActionEvent event = new ActionEvent(table, PanelEkaglieferantOptimieren.ACTION_LIEFERANT_UEBERSTEUERN,
				"" + row + "x" + col);
		action.actionPerformed(event);
	}

//
//Implement MouseListener interface
//
	/*
	 * When the mouse is pressed the editor is invoked. If you then then drag the
	 * mouse to another cell before releasing it, the editor is still active. Make
	 * sure editing is stopped when the mouse is released.
	 */
	public void mousePressed(MouseEvent e) {
		if (table.isEditing() && table.getCellEditor() == this)
			isButtonColumnEditor = true;
	}

	public void mouseReleased(MouseEvent e) {
		if (isButtonColumnEditor && table.isEditing())
			table.getCellEditor().stopCellEditing();

		isButtonColumnEditor = false;

	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {

			JTable source = (JTable) e.getSource();
			int row = source.rowAtPoint(e.getPoint());
			int column = source.columnAtPoint(e.getPoint());

			if (!source.isRowSelected(row)) {
				int z = 0;
			}

			/*
			 * int row = table.rowAtPoint(e.getPoint()); int col =
			 * table.columnAtPoint(e.getPoint());
			 * 
			 * ActionEvent event = new ActionEvent(table,
			 * PanelEkaglieferantOptimieren2.ACTION_LIEFERANT_KOMMENTAR_EDITIEREN, "" + row
			 * + "x" + col); action.actionPerformed(event);
			 */

		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {

	}
}

class MyBigDecimalCellRenderer extends DefaultTableCellRenderer {

	EkagLieferantoptimierenDto optDto = null;

	public MyBigDecimalCellRenderer(EkagLieferantoptimierenDto optDto) {
		this.optDto = optDto;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		EinkaufsangebotpositionDtoFuerOptimieren ekagPosDto = optDto.getPos().get(row);
		if (ekagPosDto.getGuenstigsterLieferantMenge1() == null && optDto.getEkagDto().getNMenge1().doubleValue() != 0
				&& (column == PanelEkaglieferantOptimieren.iSpalteGuenstPreis1)) {
			c.setBackground(Color.RED);
		} else if (ekagPosDto.getGuenstigsterLieferantMenge2() == null
				&& optDto.getEkagDto().getNMenge2().doubleValue() != 0
				&& (column == PanelEkaglieferantOptimieren.iSpalteGuenstPreis2)) {
			c.setBackground(Color.RED);
		} else if (ekagPosDto.getGuenstigsterLieferantMenge3() == null
				&& optDto.getEkagDto().getNMenge3().doubleValue() != 0
				&& (column == PanelEkaglieferantOptimieren.iSpalteGuenstPreis3)) {
			c.setBackground(Color.RED);
		} else if (ekagPosDto.getGuenstigsterLieferantMenge4() == null
				&& optDto.getEkagDto().getNMenge4().doubleValue() != 0
				&& (column == PanelEkaglieferantOptimieren.iSpalteGuenstPreis4)) {
			c.setBackground(Color.RED);
		} else if (ekagPosDto.getGuenstigsterLieferantMenge5() == null
				&& optDto.getEkagDto().getNMenge5().doubleValue() != 0
				&& (column == PanelEkaglieferantOptimieren.iSpalteGuenstPreis5)) {
			c.setBackground(Color.RED);
		} else {

			if (row % 2 == 0) {
				c.setBackground(Color.WHITE);
			} else {

				c.setBackground(Color.LIGHT_GRAY);
			}
		}

		if (isSelected) {
			c.setBackground(new Color(187, 240, 236));
		}

		return c;

	}

	@Override
	public int getHorizontalAlignment() {
		return SwingConstants.RIGHT;
	}

}

class MyIntegerCellRenderer extends DefaultTableCellRenderer {

	EkagLieferantoptimierenDto optDto = null;

	public MyIntegerCellRenderer(EkagLieferantoptimierenDto optDto) {
		this.optDto = optDto;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		EinkaufsangebotpositionDtoFuerOptimieren ekagPosDto = optDto.getPos().get(row);

		if (row % 2 == 0) {
			c.setBackground(Color.WHITE);
		} else {

			c.setBackground(Color.LIGHT_GRAY);
		}

		if (isSelected) {
			c.setBackground(new Color(187, 240, 236));
		}

		return c;

	}

	@Override
	public int getHorizontalAlignment() {
		return SwingConstants.RIGHT;
	}

}

class CopyAction extends AbstractAction {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final JTable tbl = (JTable) e.getSource();
		final int row = tbl.getSelectedRow();
		final int col = tbl.getSelectedColumn();
		if (row >= 0 && col >= 0) {
			final TableCellRenderer renderer = tbl.getCellRenderer(row, col);
			final Component comp = tbl.prepareRenderer(renderer, row, col);
			if (comp instanceof JLabel) {
				final String toCopy = ((JLabel) comp).getText();
				final StringSelection selection = new StringSelection(toCopy);
				final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
			}
		}
	}

}

class MyTableModel1 extends DefaultTableModel {
	private static final long serialVersionUID = -6311652299531724223L;
	private Object[] columnNames = null;
	private Object[][] data = null;
	boolean bIstBestellt = false;

	public MyTableModel1(Object rowData[][], Object columnNames[], boolean bIstBestellt) {
		super(rowData, columnNames);
		this.columnNames = columnNames;
		this.data = data;
		this.bIstBestellt = bIstBestellt;

	}

	public Class<?> getColumnClass(int c) {
		if (c == PanelEkaglieferantOptimieren.iSpalteVerfuegbar
				|| c == PanelEkaglieferantOptimieren.iSpaltePositionsmenge
				|| c == PanelEkaglieferantOptimieren.iSpalteMenge1
				|| c == PanelEkaglieferantOptimieren.iSpalteGuenstPreis1
				|| c == PanelEkaglieferantOptimieren.iSpalteUebstPreis1
				|| c == PanelEkaglieferantOptimieren.iSpalteMenge2
				|| c == PanelEkaglieferantOptimieren.iSpalteGuenstPreis2
				|| c == PanelEkaglieferantOptimieren.iSpalteUebstPreis2
				|| c == PanelEkaglieferantOptimieren.iSpalteMenge3
				|| c == PanelEkaglieferantOptimieren.iSpalteGuenstPreis3
				|| c == PanelEkaglieferantOptimieren.iSpalteUebstPreis3
				|| c == PanelEkaglieferantOptimieren.iSpalteMenge4
				|| c == PanelEkaglieferantOptimieren.iSpalteGuenstPreis4
				|| c == PanelEkaglieferantOptimieren.iSpalteUebstPreis4
				|| c == PanelEkaglieferantOptimieren.iSpalteMenge5
				|| c == PanelEkaglieferantOptimieren.iSpalteGuenstPreis5
				|| c == PanelEkaglieferantOptimieren.iSpalteUebstPreis5 || c == PanelEkaglieferantOptimieren.iSpalteVPE1
				|| c == PanelEkaglieferantOptimieren.iSpalteVPE2 || c == PanelEkaglieferantOptimieren.iSpalteVPE3
				|| c == PanelEkaglieferantOptimieren.iSpalteVPE4 || c == PanelEkaglieferantOptimieren.iSpalteVPE5
				|| c == PanelEkaglieferantOptimieren.iSpalteVPEUebst1
				|| c == PanelEkaglieferantOptimieren.iSpalteVPEUebst2
				|| c == PanelEkaglieferantOptimieren.iSpalteVPEUebst3
				|| c == PanelEkaglieferantOptimieren.iSpalteVPEUebst4
				|| c == PanelEkaglieferantOptimieren.iSpalteVPEUebst5) {
			return BigDecimal.class;
		} else if (c == PanelEkaglieferantOptimieren.iSpalteLieferzeitUebst1
				|| c == PanelEkaglieferantOptimieren.iSpalteLieferzeitUebst2
				|| c == PanelEkaglieferantOptimieren.iSpalteLieferzeitUebst3
				|| c == PanelEkaglieferantOptimieren.iSpalteLieferzeitUebst4
				|| c == PanelEkaglieferantOptimieren.iSpalteLieferzeitUebst5) {
			return Integer.class;
		} else {
			return String.class;
		}
	}

	public boolean isCellEditable(int row, int col) {

		if (bIstBestellt) {
			return false;
		}
		if (col == PanelEkaglieferantOptimieren.iSpalteLFUebst1 || col == PanelEkaglieferantOptimieren.iSpalteLFUebst2
				|| col == PanelEkaglieferantOptimieren.iSpalteLFUebst3
				|| col == PanelEkaglieferantOptimieren.iSpalteLFUebst4
				|| col == PanelEkaglieferantOptimieren.iSpalteLFUebst5) {
			return true;
		} else {
			return false;
		}

	}

}
