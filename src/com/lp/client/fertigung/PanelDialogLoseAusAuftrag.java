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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.ButtonAbstractAction;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.artikel.service.ArtikelsperrenDto;
import com.lp.server.fertigung.service.LosAusAuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

/*
 * <p><I>Dialog zur Nachtraeglichen Entnahme von Material auf ein Los</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>17. 10. 2005</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.8 $
 */
public class PanelDialogLoseAusAuftrag extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<LosAusAuftragDto> losDtos = null;

	private WrapperLabel[] wlaLagerstand = null;
	private WrapperLabel[] wlaVerfuegbar = null;
	private WrapperLabel[] wlaReserviert = null;
	private WrapperLabel[] wlaPosNr = null;
	private WrapperLabel[] wlaFehlmenge = null;
	private WrapperLabel[] wlaOffeneFtgMng = null;
	private WrapperDateField[] wlaBeginn = null;
	private WrapperDateField[] wlaEnde = null;
	private WrapperLabel[] wlaArtikelNummer = null;
	private WrapperLabel[] wlaArtikelBezeichnung = null;
	private WrapperNumberField[] wnfLosgroesse = null;
	private WrapperLabel[] wlaInABLos = null;
	private BigDecimal[] nVerfuegbar = null;
	private BigDecimal[] nLosgroesse = null;
	private WrapperLabel[] wlaISortReihenfolgenplanung = null;
	TreeMap tmVorhandenenNummern = null;
	private Integer auftragId = null;
	private boolean bReihenfolgenplanung = false;
	private boolean bAutomatischeErmittlungProduktionsende = false;
	
	private InternalFrameFertigung internalFrameFertigung = null;

	private JScrollPane jspScrollPane = new JScrollPane();

	private String ACTION_VERFUEGBAR_BERUECKSICHTIGEN = ALWAYSENABLED + "ACTION_VERFUEGBAR_BERUECKSICHTIGEN";

	public PanelDialogLoseAusAuftrag(InternalFrameFertigung internalFrameFertigung, Integer auftragIId, String title)
			throws Throwable {
		super(internalFrameFertigung, title);
		this.internalFrameFertigung = internalFrameFertigung;
		this.auftragId = auftragIId;

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_REIHENFOLGENPLANUNG, ParameterFac.KATEGORIE_FERTIGUNG,
						LPMain.getTheClient().getMandant());
		bReihenfolgenplanung = (Boolean) parameter.getCWertAsObject();

		
		ParametermandantDto parameterAutomatischeErmittlungLosEnde = (ParametermandantDto) DelegateFactory.getInstance()
				.getParameterDelegate().getParametermandant(ParameterFac.PARAMETER_AUTOMATISCHE_ERMITTLUNG_LOS_ENDE,
						ParameterFac.KATEGORIE_FERTIGUNG, LPMain.getTheClient().getMandant());

		bAutomatischeErmittlungProduktionsende = (Boolean) parameterAutomatischeErmittlungLosEnde.getCWertAsObject();
		
		init();
		setDefaults();
		initComponents();
	}

	/**
	 * Dialog initialisieren
	 * 
	 * @throws Throwable
	 */
	private void init() throws Throwable {

		losDtos = DelegateFactory.getInstance().getFertigungDelegate().vorschlagMitUnterlosenAusAuftrag(auftragId);

		if (bReihenfolgenplanung) {
			losDtos = DelegateFactory.getInstance().getFertigungDelegate()
					.vorschlagMitUnterlosenAusAuftragReihenUndBeginnEndeBerechnen(losDtos);

		}

		// Erledigte entfernen
		if (losDtos != null && losDtos.size() > 0) {
			ArrayList<LosAusAuftragDto> losDtosTemp = new ArrayList();
			for (int i = 0; i < losDtos.size(); i++) {
				if (!losDtos.get(i).isAuftragspositionIstErledigt()) {
					losDtosTemp.add(losDtos.get(i));
				}
			}

			losDtos = losDtosTemp;

		}

		// PJ18850
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_STUECKLISTENFREIGABE)) {

			boolean bDialogAngezeigt = false;
			boolean bTrotzdemProduzieren = false;

			ArrayList<LosAusAuftragDto> losDtosTemp = new ArrayList();

			if (losDtos != null && losDtos.size() > 0) {
				for (int i = 0; i < losDtos.size(); i++) {
					if (losDtos.get(i).getLosDto().getStuecklisteIId() != null) {
						StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
								.stuecklisteFindByPrimaryKey(losDtos.get(i).getLosDto().getStuecklisteIId());

						if (stklDto.getTFreigabe() == null) {

							if (bDialogAngezeigt == false) {

								bTrotzdemProduzieren = DialogFactory.showModalJaNeinDialog(getInternalFrame(), LPMain
										.getTextRespectUISPr("fert.error.losanlegen.auftrag.stklnichtfreigegeben"));
								bDialogAngezeigt = true;
							}

							if (bTrotzdemProduzieren == true) {
								losDtosTemp.add(losDtos.get(i));
							}

						} else {
							losDtosTemp.add(losDtos.get(i));
						}
					}

				}

				losDtos = losDtosTemp;

			}
		}

		if (losDtos != null && losDtos.size() > 0) {

			wlaLagerstand = new WrapperLabel[losDtos.size()];
			wlaFehlmenge = new WrapperLabel[losDtos.size()];
			wlaOffeneFtgMng = new WrapperLabel[losDtos.size()];
			wlaInABLos = new WrapperLabel[losDtos.size()];
			wlaReserviert = new WrapperLabel[losDtos.size()];
			wlaPosNr = new WrapperLabel[losDtos.size()];

			wlaArtikelBezeichnung = new WrapperLabel[losDtos.size()];

			wlaArtikelNummer = new WrapperLabel[losDtos.size()];
			wnfLosgroesse = new WrapperNumberField[losDtos.size()];
			wlaISortReihenfolgenplanung = new WrapperLabel[losDtos.size()];
			wlaVerfuegbar = new WrapperLabel[losDtos.size()];
			wlaBeginn = new WrapperDateField[losDtos.size()];
			wlaEnde = new WrapperDateField[losDtos.size()];
			nVerfuegbar = new BigDecimal[losDtos.size()];
			nLosgroesse = new BigDecimal[losDtos.size()];

			for (int i = 0; i < losDtos.size(); i++) {

				BigDecimal bdBereitsFuerAuftragInFertigung = BigDecimal.ZERO;

				LosAusAuftragDto laDto = losDtos.get(i);
				String sLabelVorhandene = "<html>";
				if (laDto.getBereitsVorhandeneLose() != null) {

					for (int k = 0; k < laDto.getBereitsVorhandeneLose().length; k++) {

						LosDto losDto = laDto.getBereitsVorhandeneLose()[k];

						if (!losDto.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)) {

							sLabelVorhandene += losDto.getCNr() + " " + losDto.getStatusCNr() + " "
									+ Helper.formatZahl(losDto.getNLosgroesse(), LPMain.getTheClient().getLocUi())
									+ "<br>";
							bdBereitsFuerAuftragInFertigung = bdBereitsFuerAuftragInFertigung
									.add(losDto.getNLosgroesse());
						}
					}

				}

				sLabelVorhandene += "</html>";

			

				// SP7026 Verfuegbar = Lagerstnd + offene FTGMenge - Reserviert - Felhmenge +
				// inABLos

				BigDecimal verfuegbar = new BigDecimal(0);

				if (laDto.getLagerstand() != null) {
					wlaLagerstand[i] = new WrapperLabel(Helper.formatZahlWennUngleichNull(laDto.getLagerstand(), 2,
							LPMain.getTheClient().getLocUi()));
					verfuegbar = laDto.getLagerstand();
				} else {
					wlaLagerstand[i] = new WrapperLabel("");
				}

				if (laDto.getReservierungen() != null) {
					wlaReserviert[i] = new WrapperLabel(Helper.formatZahlWennUngleichNull(laDto.getReservierungen(), 2,
							LPMain.getTheClient().getLocUi()));
					verfuegbar = verfuegbar.subtract(laDto.getReservierungen());
				} else {
					wlaReserviert[i] = new WrapperLabel("");
				}

				if (laDto.getFehlmengen() != null) {
					wlaFehlmenge[i] = new WrapperLabel(Helper.formatZahlWennUngleichNull(laDto.getFehlmengen(), 2,
							LPMain.getTheClient().getLocUi()));

					verfuegbar = verfuegbar.subtract(laDto.getFehlmengen());

				} else {
					wlaFehlmenge[i] = new WrapperLabel("");
				}
				if (laDto.getOffeneFertigungsmenge() != null) {
					wlaOffeneFtgMng[i] = new WrapperLabel(Helper.formatZahlWennUngleichNull(
							laDto.getOffeneFertigungsmenge(), 2, LPMain.getTheClient().getLocUi()));

					verfuegbar = verfuegbar.add(laDto.getOffeneFertigungsmenge());

				} else {
					wlaOffeneFtgMng[i] = new WrapperLabel("");
				}
				if (laDto.getAuftragspositionsnummer() != null) {
					wlaPosNr[i] = new WrapperLabel(laDto.getAuftragspositionsnummer() + "");
				} else {
					wlaPosNr[i] = new WrapperLabel("U");
				}

				verfuegbar = verfuegbar.add(bdBereitsFuerAuftragInFertigung);

				if (verfuegbar.doubleValue() < 0) {
					verfuegbar = BigDecimal.ZERO;

				}

				wlaVerfuegbar[i] = new WrapperLabel(
						Helper.formatZahlWennUngleichNull(verfuegbar, 2, LPMain.getTheClient().getLocUi()));

				nVerfuegbar[i] = verfuegbar;

				wlaArtikelNummer[i] = new WrapperLabel("Materialliste");
				wlaArtikelBezeichnung[i] = new WrapperLabel(losDtos.get(i).getLosDto().getCProjekt());

				if (losDtos.get(i).getLosDto().getLosUnternummerReihenfolgenplanung() != null) {
					wlaISortReihenfolgenplanung[i] = new WrapperLabel(
							losDtos.get(i).getLosDto().getLosUnternummerReihenfolgenplanung());
				} else {
					wlaISortReihenfolgenplanung[i] = new WrapperLabel("X");
				}

				if (losDtos.get(i).getLosDto().getStuecklisteIId() != null) {
					StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
							.stuecklisteFindByPrimaryKey(losDtos.get(i).getLosDto().getStuecklisteIId());
					wlaArtikelNummer[i] = new WrapperLabel(stklDto.getArtikelDto().getCNr());
					wlaArtikelBezeichnung[i] = new WrapperLabel(stklDto.getArtikelDto().formatBezeichnung());

				}

				wlaArtikelNummer[i].setHorizontalAlignment(SwingConstants.LEFT);
				wlaArtikelBezeichnung[i].setHorizontalAlignment(SwingConstants.LEFT);

				WrapperDateField wdf = new WrapperDateField();
				wdf.setDate(laDto.getLosDto().getTProduktionsbeginn());
				wdf.setMandatoryField(true);
				wlaBeginn[i] = wdf;

				wdf = new WrapperDateField();
				wdf.setDate(laDto.getLosDto().getTProduktionsende());
				wdf.setMandatoryField(true);
				wlaEnde[i] = wdf;

				wlaInABLos[i] = new WrapperLabel(Helper.formatZahlWennUngleichNull(bdBereitsFuerAuftragInFertigung, 2,
						LPMain.getTheClient().getLocUi()));

				WrapperNumberField wnf = new WrapperNumberField();
				wnf.setBigDecimal(laDto.getLosDto().getNLosgroesse().subtract(bdBereitsFuerAuftragInFertigung));
				wnf.setFractionDigits(Defaults.getInstance().getIUINachkommastellenLosgroesse());
				wnf.setMandatoryField(true);
				wnfLosgroesse[i] = wnf;

				nLosgroesse[i] = wnf.getBigDecimal();

				if (laDto.isBDatumVerschoben()) {
					wlaPosNr[i].setForeground(Color.RED);
					wlaArtikelNummer[i].setForeground(Color.RED);
					wlaISortReihenfolgenplanung[i].setForeground(Color.RED);
					wlaArtikelBezeichnung[i].setForeground(Color.RED);
				}

				wlaArtikelNummer[i].setToolTipText(sLabelVorhandene);

			}

		}

		getInternalFrame().addItemChangedListener(this);

		iZeile++;

		jpaWorkingOn.add(new WrapperLabel("PosNr"), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		if (bReihenfolgenplanung) {
			jpaWorkingOn.add(new WrapperLabel("LosNr"), new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20, 0));
		}
		WrapperLabel wlaStueckliste = new WrapperLabel(LPMain.getTextRespectUISPr("stkl.stueckliste"));

		wlaStueckliste.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaStueckliste, new GridBagConstraints(2, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel wlaBezeichnung = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bezeichnung"));

		wlaBezeichnung.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		WrapperLabel wlaSperren = new WrapperLabel(LPMain.getTextRespectUISPr("lp.sperren"));

		wlaSperren.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaSperren, new GridBagConstraints(4, iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		WrapperLabel wlaKommentare = new WrapperLabel(LPMain.getTextRespectUISPr("fert.loseausauftrag.kommentare"));

		wlaKommentare.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaKommentare, new GridBagConstraints(5, iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("lp.lagerstand")),
				new GridBagConstraints(7, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("fert.loseausauftrag.offenftgmng")),
				new GridBagConstraints(8, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("lp.reserviert")),
				new GridBagConstraints(9, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("fert.tab.oben.fehlmengen.title")),
				new GridBagConstraints(10, iZeile, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("lp.verfuegbar")),
				new GridBagConstraints(11, iZeile, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel wlaBeginnLbl = new WrapperLabel(LPMain.getTextRespectUISPr("lp.beginn"));
		wlaBeginnLbl.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaBeginnLbl, new GridBagConstraints(12, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel wlaEndeLbl = new WrapperLabel(LPMain.getTextRespectUISPr("lp.ende"));
		wlaEndeLbl.setHorizontalAlignment(SwingConstants.LEFT);
		
		if(bAutomatischeErmittlungProduktionsende==true) {
			wlaEndeLbl.setVisible(false);
		}
		
		
		jpaWorkingOn.add(wlaEndeLbl, new GridBagConstraints(13, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("fert.los.losausauftrag.inablos")),
				new GridBagConstraints(14, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel(LPMain.getTextRespectUISPr("label.losgroesse")),
				new GridBagConstraints(15, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		for (int i = 0; i < losDtos.size(); i++) {
			jpaWorkingOn.add(wlaPosNr[i], new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 20, 0));
			if (bReihenfolgenplanung) {
				jpaWorkingOn.add(wlaISortReihenfolgenplanung[i], new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 20, 0));
			}
			jpaWorkingOn.add(wlaArtikelNummer[i], new GridBagConstraints(2, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 40, 0));
			jpaWorkingOn.add(wlaArtikelBezeichnung[i], new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 90, 0));

			if (losDtos.get(i).isGesperrt()) {
				JLabel labelGesperrt = new JLabel();
				labelGesperrt.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/gesperrt.png")));

				jpaWorkingOn.add(labelGesperrt, new GridBagConstraints(4, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
			}

			if (losDtos.get(i).isKommentareVorhanden()) {
				JLabel labelKommentare = new JLabel();
				labelKommentare.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/notebook.png")));

				jpaWorkingOn.add(labelKommentare, new GridBagConstraints(5, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
			}
			
			
			if (losDtos.get(i).getLosDto().getStuecklisteIId() != null) {
				StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(losDtos.get(i).getLosDto().getStuecklisteIId());
				WrapperGotoButton wgo = new WrapperGotoButton(GotoHelper.GOTO_ARTIKEL_AUSWAHL);
				wgo.setOKey(stklDto.getArtikelIId());
				jpaWorkingOn.add(wgo.getWrapperButtonGoTo(), new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 15, 0));
			}
			
		

			jpaWorkingOn.add(wlaLagerstand[i], new GridBagConstraints(7, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
			jpaWorkingOn.add(wlaOffeneFtgMng[i], new GridBagConstraints(8, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 2, 1, 2), 0, 0));
			jpaWorkingOn.add(wlaReserviert[i], new GridBagConstraints(9, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
			jpaWorkingOn.add(wlaFehlmenge[i], new GridBagConstraints(10, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

			jpaWorkingOn.add(wlaVerfuegbar[i], new GridBagConstraints(11, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
			jpaWorkingOn.add(wlaBeginn[i], new GridBagConstraints(12, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
			
			
			
			jpaWorkingOn.add(wlaEnde[i], new GridBagConstraints(13, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
			
			if(bAutomatischeErmittlungProduktionsende==true) {
				wlaEnde[i].setVisible(false);
			}
			

			jpaWorkingOn.add(wlaInABLos[i], new GridBagConstraints(14, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

			BigDecimal bdBereitsFuerAuftragInFertigung = BigDecimal.ZERO;

			if (losDtos.get(i).getBereitsVorhandeneLose() != null) {
				for (int k = 0; k < losDtos.get(i).getBereitsVorhandeneLose().length; k++) {
					LosDto losDto = losDtos.get(i).getBereitsVorhandeneLose()[k];

					if (!losDto.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)) {
						bdBereitsFuerAuftragInFertigung = bdBereitsFuerAuftragInFertigung.add(losDto.getNLosgroesse());
					}
				}

			}

			if (wnfLosgroesse[i].getBigDecimal().doubleValue() < 0) {
				wnfLosgroesse[i].setBackground(Color.MAGENTA);
			} else if (bdBereitsFuerAuftragInFertigung.doubleValue() > 0
					&& wnfLosgroesse[i].getBigDecimal().doubleValue() > 0) {
				wnfLosgroesse[i].setBackground(Color.GREEN);
			}

			jpaWorkingOn.add(wnfLosgroesse[i], new GridBagConstraints(15, iZeile, 1, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), -120, 0));

			iZeile++;
			jpaWorkingOn.add(new JSeparator(JSeparator.HORIZONTAL), new GridBagConstraints(0, iZeile, 24, 1, 1.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 0, 2), 0, 0));

			iZeile++;

		}

		jspScrollPane.setViewportView(jpaWorkingOn);
		jspScrollPane.setAutoscrolls(true);

		this.add(jspScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		// PJ20184

		createAndSaveAndShowButton("/com/lp/client/res/data_down.png",
				LPMain.getTextRespectUISPr("fert.loseausauftrag.verfuegbarberuecksichtigen"),
				ACTION_VERFUEGBAR_BERUECKSICHTIGEN, null, null);

		getHmOfButtons().get(ACTION_VERFUEGBAR_BERUECKSICHTIGEN).getButton().setEnabled(true);

	}

	private String sucheNaechsteFreieNummer(String reihenfolge) {

		if (reihenfolge == null) {
			Iterator<String> it = tmVorhandenenNummern.descendingKeySet().iterator();
			while (it.hasNext()) {
				String nummer = it.next();
				Boolean bVerbraucht = (Boolean) tmVorhandenenNummern.get(nummer);

				if (bVerbraucht == false) {

					tmVorhandenenNummern.put(nummer, Boolean.TRUE);
					return nummer;
				}
			}
		} else {

			boolean bVerbraucht = true;

			while (bVerbraucht == true) {
				bVerbraucht = (Boolean) tmVorhandenenNummern.get(reihenfolge);

				if (bVerbraucht == false) {
					tmVorhandenenNummern.put(reihenfolge, Boolean.TRUE);
					return reihenfolge;
				} else {
					reihenfolge = (String) tmVorhandenenNummern.higherKey(reihenfolge);
				}

			}

		}

		return "";

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {

			if (allMandatoryFieldsSetDlg()) {
				for (int i = 0; i < losDtos.size(); i++) {
					losDtos.get(i).getLosDto().setNLosgroesse(wnfLosgroesse[i].getBigDecimal());
					losDtos.get(i).getLosDto().setTProduktionsbeginn(wlaBeginn[i].getDate());
					losDtos.get(i).getLosDto().setTProduktionsende(wlaEnde[i].getDate());
				}
				int i = DelegateFactory.getInstance().getFertigungDelegate().loseAusAuftragAnlegen(losDtos, auftragId);
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"), i + " Lose angelegt.");

				internalFrameFertigung.getTabbedPaneLos().getPanelQueryAuswahl(false).eventYouAreSelected(false);
				internalFrameFertigung.getTabbedPaneLos().setLosauswahlStatusFilterNachNeuerstellung(null);

				getInternalFrame().closePanelDialog();
			}

		} else if (e.getActionCommand().equals(ACTION_VERFUEGBAR_BERUECKSICHTIGEN)) {
			if (losDtos != null) {
				for (int i = 0; i < losDtos.size(); i++) {

					BigDecimal bdVorhanden = nLosgroesse[i];
					if (bdVorhanden != null && nVerfuegbar != null && nVerfuegbar[i].doubleValue() > 0) {
						if (bdVorhanden.subtract(nVerfuegbar[i]).doubleValue() < 0) {
							wnfLosgroesse[i].setBigDecimal(BigDecimal.ZERO);
						} else {
							wnfLosgroesse[i].setBigDecimal(bdVorhanden.subtract(nVerfuegbar[i]));
						}
						wnfLosgroesse[i].setFractionDigits(Defaults.getInstance().getIUINachkommastellenLosgroesse());

					}

				}
			}
		}
	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI EventObject
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {

		}
	}

	/**
	 * Dialogfenster zur Artikelauswahl.
	 * 
	 * @param e ActionEvent
	 * @throws Throwable
	 */

	private void setDefaults() throws Throwable {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}
}
