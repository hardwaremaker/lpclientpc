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
package com.lp.client.bestellung;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelPositionenArtikelEinkauf;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.GebindeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Panel fuer Artikelpositionen in der Bestellung.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 22.07.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Josef Erlinger
 * @version $Revision: 1.15 $
 */
public class PanelPositionenArtikelBestellung extends PanelPositionenArtikelEinkauf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public WrapperLabel wlaGewichtbestellmenge = null;
	public WrapperNumberField wnfGewichtbestellmenge = null;
	public WrapperLabel wlaGewichtmengeEinheit = null;

	public WrapperLabel wlaGewichtPreis = null;
	public WrapperNumberField wnfGewichtPreis = null;
	public WrapperLabel wlaGewichtWaehrung = null;

	private WrapperButton buttonGebinde = new WrapperButton();
	public String ACTION_GEBINDE = "ACTION_GEBINDE";

	/**
	 * @todo MB weg damit
	 */
	private TabbedPaneBestellung tPBes = null;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame    der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI       der default Titel des Panels
	 * @param key              PK der Position
	 * @param sLockMeWer       String
	 * @param iSpaltenbreite1I die Breite der ersten Spalte
	 * @throws Throwable Ausnahme
	 */
	public PanelPositionenArtikelBestellung(InternalFrame internalFrame, String add2TitleI, Object key,
			String sLockMeWer, int iSpaltenbreite1I) throws Throwable {

		super(internalFrame, add2TitleI, key, sLockMeWer, iSpaltenbreite1I, null);
		tPBes = ((InternalFrameBestellung) internalFrame).getTabbedPaneBestellung();
		wcoEinheit.setActivatable(false);
		jbInitPanel();
		initPanel();
	}

	private void jbInitPanel() throws Throwable {
		wlaGewichtbestellmenge = new WrapperLabel(LPMain.getTextRespectUISPr("bes.alternative.bestellmengeneinheit"));
		// wlaGewichtbestellmenge.addMouseListener(new
		// MouseAdapterChangeMenge(this));
		wnfGewichtbestellmenge = new WrapperNumberField();
		wlaGewichtmengeEinheit = new WrapperLabel();
		wlaGewichtmengeEinheit.setHorizontalAlignment(SwingConstants.LEADING);
		wlaGewichtPreis = new WrapperLabel(LPMain.getTextRespectUISPr("lp.preis"));
		wnfGewichtPreis = new WrapperNumberField();
		wnfGewichtPreis.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseEK());
		wlaGewichtWaehrung = new WrapperLabel();
		HelperClient.setDefaultsToComponent(wlaGewichtWaehrung, 20);
		wlaGewichtWaehrung.setHorizontalAlignment(SwingConstants.LEADING);
		iZeile++;
		iZeile = 13;
		add(wlaGewichtbestellmenge, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		add(wnfGewichtbestellmenge, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		add(wlaGewichtmengeEinheit, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		add(wnfGewichtPreis, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		add(wlaGewichtWaehrung, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		wnfGewichtbestellmenge.addFocusListener(new FocusAdapterGewichtArtikel(this));
		wnfGewichtPreis.addFocusListener(new FocusAdapterGewichtPreisArtikel(this));

		buttonGebinde.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/cookies.png")));
		buttonGebinde.setActionCommand(ACTION_GEBINDE);
		buttonGebinde.addActionListener(this);

		add(buttonGebinde, new GridBagConstraints(0, iZeileMenge, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 10, 0));

	}

	private void initPanel() {
		// Wenn der Benutzer keine Preise sehen darf, sind einige Felder nicht
		// sichtbar.
		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			wlaGewichtPreis.setVisible(false);
			wnfGewichtPreis.setVisible(false);
			wlaGewichtWaehrung.setVisible(false);
		}
	}

	/*
	 * public void dto2Components() throws Throwable {
	 * 
	 * besPosDto = getInternalFrameBestellung().getBestellpositionDto();
	 * 
	 * if (besPosDto != null) { if (getArtikelDto().getEinheitCNrBestellung() !=
	 * null) { wnfGewichtbestellmenge.setBigDecimal(besPosDto.getNMenge().multiply
	 * (getArtikelDto(). getNUmrechnungsfaktor()));
	 * wlaGewichtWaehrung.setText(getTPBes().getBesDto().
	 * getWaehrungCNrBestellungswaehrung()); if (besPosDto.getNNettoeinzelpreis() !=
	 * null) { BigDecimal nPreisPerEinheit =
	 * (besPosDto.getNNettoeinzelpreis().divide(
	 * wnfGewichtbestellmenge.getBigDecimal(),
	 * getTPBes().getIPreiseUINachkommastellen(),
	 * BigDecimal.ROUND_HALF_UP)).multiply( wnfMenge.getBigDecimal());
	 * wnfGewichtPreis.setBigDecimal(nPreisPerEinheit); } wlaGewichtmengeEinheit.
	 * setText(getArtikelDto().getEinheitCNrBestellung().trim() + "  ?"); } }
	 * 
	 * }
	 * 
	 * protected void setDefaults() throws Throwable {
	 * 
	 * }
	 */

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_GEBINDE)) {
			positionsmengeueberGebindeBerechnen();
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == wifArtikelauswahl) {
				if (wifArtikelauswahl.getArtikelDto() != null) {

					// PJ20563
					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance()
							.getParameterDelegate().getParametermandant(ParameterFac.PARAMETER_DIMENSIONEN_BESTELLEN,
									ParameterFac.KATEGORIE_ARTIKEL, LPMain.getTheClient().getMandant());
					boolean b = (Boolean) parameter.getCWertAsObject();

					if (b == true && wifArtikelauswahl.getArtikelDto() != null
							&& wifArtikelauswahl.getArtikelDto().getEinheitCNr() != null) {
						EinheitDto einheitDto = DelegateFactory.getInstance().getSystemDelegate()
								.einheitFindByPrimaryKey(wifArtikelauswahl.getArtikelDto().getEinheitCNr());

						if (einheitDto.getIDimension() > 0) {
							DialogDimensionenBestellen d = new DialogDimensionenBestellen(
									wifArtikelauswahl.getArtikelDto().getIId(), getInternalFrameBestellung()
											.getTabbedPaneBestellung().getBesDto().getLieferantIIdBestelladresse(),
									getInternalFrame());
							LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
							d.setVisible(true);

							if (d.getArtikelDtoNeuErstellt() != null) {

								wnfMenge.setBigDecimal(d.getBdNeuePositionsmenge());
								wifArtikelauswahl.setArtikelDto(d.getArtikelDtoNeuErstellt());
							}

						}
					}

					setArtikelEingabefelderEditable(true);
					setArtikelDto(wifArtikelauswahl.getArtikelDto());
					pruefeObFremdgefertigteStueckliste();
					handleArtikelSelektiert();
					artikelDto2components();
					handleArtikelSelektiert();

					if (Helper.short2boolean(wifArtikelauswahl.getArtikelDto().getBKeineLagerzubuchung())) {
						// PJ19999 Hinweis, wenn in BS und BV verwendet

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("artikel.keinelagerzubuchung.hinweis.bs"));
					}

					// SP4058
					// positionsmengeueberGebindeBerechnen();
					if (pa != null) {
						pa.clearMengenUndSetzeFocusAufKarton();
					}

				}
			}
		}
	}

	private void positionsmengeueberGebindeBerechnen() throws Throwable {

		// Gebindeauswahl
		ArrayList<GebindeDto> alGebinde = DelegateFactory.getInstance().getArtikelDelegate()
				.getGebindeEinesArtikelsUndEinesLieferanten(getArtikelDto().getIId(), getIIdLieferant(),
						getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getDBelegdatum());

		if (alGebinde.size() > 0) {

			InternalFrameBestellung intFrameBestellung = (InternalFrameBestellung) getInternalFrame();

			DialogGebindeUmrechnung dGebinde = new DialogGebindeUmrechnung(getArtikelDto().getIId(), getIIdLieferant(),
					alGebinde, wnfMenge.getBigDecimal(), intFrameBestellung.getTabbedPaneBestellung()
							.getPanelBestellungPositionenBottomD3().getBestellpositionDto().getGebindeIId());
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dGebinde);
			dGebinde.setVisible(true);

			if (dGebinde.getBdNeuePositionsmenge() != null && dGebinde.getGebindeDtoSelektiert() != null
					|| dGebinde.isKeinGebinde()) {
				// Menge setzen

				if (dGebinde.isKeinGebinde()) {

					intFrameBestellung.getTabbedPaneBestellung().getPanelBestellungPositionenBottomD3()
							.getBestellpositionDto().setGebindeIId(null);
					intFrameBestellung.getTabbedPaneBestellung().getPanelBestellungPositionenBottomD3()
							.getBestellpositionDto().setNAnzahlgebinde(null);
				} else {
					wnfMenge.setBigDecimal(dGebinde.getBdNeuePositionsmenge());

					intFrameBestellung.getTabbedPaneBestellung().getPanelBestellungPositionenBottomD3()
							.getBestellpositionDto().setGebindeIId(dGebinde.getGebindeDtoSelektiert().getIId());
					intFrameBestellung.getTabbedPaneBestellung().getPanelBestellungPositionenBottomD3()
							.getBestellpositionDto().setNAnzahlgebinde(dGebinde.getBdAnzahlGebinde());
				}

				try {
					ArtikellieferantDto artikellieferantDto = DelegateFactory.getInstance().getArtikelDelegate()
							.getArtikelEinkaufspreisMitOptionGebinde(getArtikelDto().getIId(), getIIdLieferant(),
									dGebinde.getBdNeuePositionsmenge(),
									getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getWaehrungCNr(),
									getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getDBelegdatum(),
									getInternalFrameBestellung().getTabbedPaneBestellung()
											.getPanelBestellungPositionenBottomD3().getBestellpositionDto()
											.getGebindeIId());

					setArtikellieferantDto(artikellieferantDto);

				} catch (ExceptionLP eDummy) {
					// nothing here
				}

				berechnePreisdaten(false);
				TabbedPaneBestellung tpBestellung = intFrameBestellung.getTabbedPaneBestellung();
				PanelBestellungPositionen panelBestellungPos = tpBestellung.getPanelBestellungPositionenBottomD3();

				panelBestellungPos.artLiefData2Components(getArtikelDto(), getArtikellieferantDto(),false);
			}

		}

	}

	private void handleArtikelSelektiert() throws ExceptionLP, Throwable, Throwable {
		if (getArtikelDto() != null) {
			// Artikel gefunden.
			InternalFrameBestellung intFrameBestellung = (InternalFrameBestellung) getInternalFrame();
			TabbedPaneBestellung tpBestellung = intFrameBestellung.getTabbedPaneBestellung();
			PanelBestellungPositionen panelBestellungPos = tpBestellung.getPanelBestellungPositionenBottomD3();

			String sWahrungBS = getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getWaehrungCNr();

			ArtikellieferantDto artliefDto = null;
			try {
				artliefDto = HelperBestellung.getArtikellieferantDtoEinkaufspreis(getArtikelDto().getIId(),
						getIIdLieferant(), BigDecimal.ONE, sWahrungBS,
						getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getDBelegdatum(), null);
			} catch (ExceptionLP elp) {
				handleException(elp, false);
			}

			if (artliefDto != null) {
				setArtikellieferantDto(artliefDto);
				panelBestellungPos.wtfLieferantArtikelBezeichnung.setText(artliefDto.getCBezbeilieferant());
			} else {
				setArtikellieferantDto(null);
			}

			BigDecimal umrechnungsfaktor = getArtikelDto().getNUmrechnungsfaktor();
			boolean bInvers = Helper.short2boolean(getArtikelDto().getbBestellmengeneinheitInvers());

			if (umrechnungsfaktor == null) {

				if (getArtikellieferantDto() != null && getArtikellieferantDto().getNVerpackungseinheit() != null
						&& getArtikellieferantDto().getEinheitCNrVpe() != null) {
					umrechnungsfaktor = getArtikellieferantDto().getNVerpackungseinheit();
					bInvers = true;
				}

			}

			if (wnfMenge.getBigDecimal() != null && umrechnungsfaktor != null && umrechnungsfaktor.doubleValue() != 0) {
				if (bInvers) {
					wnfGewichtbestellmenge.setBigDecimal(
							wnfMenge.getBigDecimal().divide(umrechnungsfaktor, 2, BigDecimal.ROUND_HALF_UP));
					BigDecimal nPreisPerEinheit = (wnfNettopreis.getBigDecimal().multiply(umrechnungsfaktor));
					wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
				} else {
					wnfGewichtbestellmenge.setBigDecimal(wnfMenge.getBigDecimal().multiply(umrechnungsfaktor));
					BigDecimal nPreisPerEinheit = (wnfEinzelpreis.getBigDecimal().divide(umrechnungsfaktor,
							Defaults.getInstance().getIUINachkommastellenPreiseEK(), BigDecimal.ROUND_HALF_EVEN));
					wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
				}

			} else {
				wnfGewichtbestellmenge.setBigDecimal(umrechnungsfaktor);
			}
			artikelDto2components(true);

			panelBestellungPos.artLiefData2Components(getArtikelDto(), artliefDto, true);

			// SP2771
			if (artliefDto == null) {
				ArtikellieferantDto alDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(getArtikelDto().getIId(),
								getIIdLieferant(),
								getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getDBelegdatum(),
								null);
				if (alDto != null) {
					panelBestellungPos.artLiefData2ComponentsWennKeinEinzelpreisDefiniert(getArtikelDto(), alDto, true);
				}

			}
		}
	}

	private void workoutException(ExceptionLP elpI) throws ExceptionLP {

		if (elpI.getICode() == EJBExceptionLP.FEHLER_KEIN_WECHSELKURS_HINTERLEGT) {
			MessageFormat mf = new MessageFormat(LPMain.getTextRespectUISPr("bes.wahrung.exit"));
			String sWaehrungBS = getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getWaehrungCNr();
			String sWaehrungMandant = null;
			try {
				sWaehrungMandant = LPMain.getTheClient().getSMandantenwaehrung();
				mf.setLocale(LPMain.getTheClient().getLocUi());
			} catch (Throwable exDummy) {
				// nothing here
			}
			Object pattern[] = { sWaehrungBS + " " + sWaehrungMandant };
			String sMsg = mf.format(pattern);

			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"), sMsg);
			throw elpI;
		} else {
			throw elpI;
		}
	}

	public void artikelDto2components() throws Throwable {
		artikelDto2components(false);
	}

	public void artikelDto2components(boolean bArtikelNeuAusgewaehlt) throws Throwable {

		super.artikelDto2components();

		if (getArtikellieferantDto() != null && getArtikellieferantDto().getFStandardmenge() != null) {
			wnfMenge.setDouble(getArtikellieferantDto().getFStandardmenge());
		} else if (getArtikellieferantDto() != null && getArtikellieferantDto().getFMindestbestelmenge() != null) {
			wnfMenge.setDouble(getArtikellieferantDto().getFMindestbestelmenge());
		}

		if (pa != null) {
			if (getArtikellieferantDto() != null && getArtikellieferantDto().getNVerpackungseinheit() != null) {

				pa.setVerpackungsmenge(getArtikellieferantDto().getNVerpackungseinheit().doubleValue());
			} else {

				if (getArtikelDto() != null && getArtikelDto().getFVerpackungsmenge() != null) {
					pa.setVerpackungsmenge(getArtikelDto().getFVerpackungsmenge());
				} else {
					pa.setVerpackungsmenge(null);
				}

			}
		}

		berechnePreisdaten(bArtikelNeuAusgewaehlt);

		BigDecimal umrechnungsfaktor = getArtikelDto().getNUmrechnungsfaktor();
		boolean bInvers = Helper.short2boolean(getArtikelDto().getbBestellmengeneinheitInvers());

		if (umrechnungsfaktor == null) {

			if (getArtikellieferantDto() != null && getArtikellieferantDto().getNVerpackungseinheit() != null
					&& getArtikellieferantDto().getEinheitCNrVpe() != null) {
				umrechnungsfaktor = getArtikellieferantDto().getNVerpackungseinheit();
				bInvers = true;
			}

		}

		if (umrechnungsfaktor != null) {
			if (bInvers) {

				if (umrechnungsfaktor.doubleValue() != 0) {
					wnfGewichtbestellmenge.setBigDecimal(
							wnfMenge.getBigDecimal().divide(umrechnungsfaktor, 4, BigDecimal.ROUND_HALF_EVEN));
				} else {
					wnfGewichtbestellmenge.setBigDecimal(new BigDecimal(0));
				}
			} else {
				wnfGewichtbestellmenge.setBigDecimal(wnfMenge.getBigDecimal().multiply(umrechnungsfaktor));
			}

			if (getArtikelDto().getEinheitCNrBestellung() != null) {
				wlaGewichtmengeEinheit.setText(getArtikelDto().getEinheitCNrBestellung().trim());
			} else if (getArtikellieferantDto() != null) {
				wlaGewichtmengeEinheit.setText(getArtikellieferantDto().getEinheitCNrVpe().trim());
			}

			if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
				wlaGewichtmengeEinheit.setText(wlaGewichtmengeEinheit.getText() + "  \u00E0");
			}
			wlaGewichtWaehrung.setText(tPBes.getBesDto().getWaehrungCNr());
		}
	}

	/**
	 * Wenn dieses Feld verlassen wird, muessen die Preisfelder neu berechnet
	 * werden.
	 * 
	 * @param e Ereignis
	 * @throws java.lang.Throwable Ausnahme
	 */
	protected void wnfEinzelpreis_focusLost(FocusEvent e) throws Throwable {
		super.wnfEinzelpreis_focusLost(e);

		BigDecimal umrechnungsfaktor = getArtikelDto().getNUmrechnungsfaktor();
		boolean bInvers = Helper.short2boolean(getArtikelDto().getbBestellmengeneinheitInvers());

		if (umrechnungsfaktor == null) {

			if (getArtikellieferantDto() != null && getArtikellieferantDto().getNVerpackungseinheit() != null
					&& getArtikellieferantDto().getEinheitCNrVpe() != null) {
				umrechnungsfaktor = getArtikellieferantDto().getNVerpackungseinheit();
				bInvers = true;
			}

		}

		if (wnfEinzelpreis.getBigDecimal() != null && umrechnungsfaktor != null) {
			if (umrechnungsfaktor.compareTo(new BigDecimal(0)) != 0) {

				if (bInvers) {

					BigDecimal nPreisPerEinheit = wnfEinzelpreis.getBigDecimal().multiply(umrechnungsfaktor);

					wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
				} else {
					BigDecimal nPreisPerEinheit = (wnfEinzelpreis.getBigDecimal().divide(umrechnungsfaktor,
							Defaults.getInstance().getIUINachkommastellenPreiseEK(), BigDecimal.ROUND_HALF_EVEN));

					wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
				}
			}
			if (getArtikelDto().getEinheitCNrBestellung() != null) {
				wlaGewichtmengeEinheit.setText(getArtikelDto().getEinheitCNrBestellung().trim() + "  \u00E0");
			} else if (getArtikellieferantDto() != null && getArtikellieferantDto().getEinheitCNrVpe() != null) {
				wlaGewichtmengeEinheit.setText(getArtikellieferantDto().getEinheitCNrVpe().trim() + "  \u00E0");
			}

		}
	}

	private void pruefeObFremdgefertigteStueckliste() throws Throwable {
		// pruefen, ob Artikel ein Stuecklistenartikel ist und fremdgefertigt
		// wird
		StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(getArtikelDto().getIId());
		if (stklDto != null && !Helper.short2boolean(stklDto.getBFremdfertigung())
				&& !stklDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getTextRespectUISPr("bes.warning.stuecklistenichtfremdgefertigt"));
		}
	}

	/**
	 * Die Preisdaten vorbelgen.
	 * 
	 * @throws Throwable Ausnahme
	 */
	protected void berechnePreisdaten(boolean bArtikelNeuAusgewaehlt) throws Throwable {

		BigDecimal bdEinzelpreis = null;
		ArtikellieferantDto artikellieferantDto = null;
		Integer iIdartikel = getArtikelDto().getIId();
		BigDecimal bdMenge = new BigDecimal(wnfMenge.getDouble().doubleValue());

		// PJ21210
		BigDecimal bdMengeVorhanden = BigDecimal.ZERO;

		if (Helper.short2boolean(getArtikelDto().getBSummeInBestellung())) {

			bdMengeVorhanden = DelegateFactory.getInstance().getBestellungDelegate()
					.getSummeMengeGleicherArtikelInBestellung(
							getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getIId(),
							getInternalFrameBestellung().getTabbedPaneBestellung()
									.getPanelBestellungPositionenBottomD3().getBestellpositionDto().getIId(),
							iIdartikel);
		}

		try {
			artikellieferantDto = DelegateFactory.getInstance().getArtikelDelegate()
					.getArtikelEinkaufspreisMitOptionGebinde(iIdartikel, getIIdLieferant(),
							bdMengeVorhanden.add(bdMenge),
							getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getWaehrungCNr(),
							getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getDBelegdatum(),
							getInternalFrameBestellung().getTabbedPaneBestellung()
									.getPanelBestellungPositionenBottomD3().getBestellpositionDto().getGebindeIId());
		} catch (ExceptionLP eDummy) {
			// nothing here
		}

		if (artikellieferantDto != null) {
			// wir haben einen artikeleinkaufspreis
			bdEinzelpreis = artikellieferantDto.getNEinzelpreis();
			wnfEinzelpreis.setBigDecimal(bdEinzelpreis);

			Double dRabatt = (artikellieferantDto.getFRabatt() != null) ? artikellieferantDto.getFRabatt()
					: new Double(0);

			getWnfRabattsatz().setDouble(dRabatt);

			BigDecimal nettopreis = null;
			BigDecimal rabattsumme = null;
			if (Helper.short2boolean(artikellieferantDto.getBRabattbehalten())) {
				wnfRabattsumme.getWrbFixNumber().setSelected(true);
				nettopreis = bdEinzelpreis
						.subtract(bdEinzelpreis.multiply(new BigDecimal(dRabatt.doubleValue()).movePointLeft(2)));

				rabattsumme = bdEinzelpreis.multiply(new BigDecimal(dRabatt.doubleValue()).movePointLeft(2));

			} else {
				wnfNettopreis.getWrbFixNumber().setSelected(true);
				nettopreis = artikellieferantDto.getNNettopreis();
				rabattsumme = bdEinzelpreis.subtract(nettopreis);
			}

			if (wnfMaterialzuschlag != null && wnfMaterialzuschlag.getBigDecimal() != null) {
				nettopreis = nettopreis.add(wnfMaterialzuschlag.getBigDecimal());
			}

			wnfNettopreis.setBigDecimal(nettopreis);

			wnfRabattsumme.setBigDecimal(rabattsumme);
		} else {
			// wir haben keinen artikeleinkaufspreis
			wnfEinzelpreis.setBigDecimal(new BigDecimal(0.));
			getWnfRabattsatz().setDouble(new Double(0.));
		}
	}

	protected InternalFrameBestellung getInternalFrameBestellung() {
		return (InternalFrameBestellung) getInternalFrame();
	}

	public void setArtikelDto(ArtikelDto artikelDto) throws Throwable {
		super.setArtikelDto(artikelDto);
		boolean bGewichtVisible = false;

		buttonGebinde.setVisible(false);

		ArrayList<GebindeDto> alGebinde = DelegateFactory.getInstance().getArtikelDelegate()
				.getGebindeEinesArtikelsUndEinesLieferanten(getArtikelDto().getIId(), getIIdLieferant(),
						getInternalFrameBestellung().getTabbedPaneBestellung().getBesDto().getDBelegdatum());

		if (alGebinde.size() > 0) {
			buttonGebinde.setVisible(true);

			Integer gebindeIId = getInternalFrameBestellung().getTabbedPaneBestellung()
					.getPanelBestellungPositionenBottomD3().getBestellpositionDto().getGebindeIId();
			BigDecimal bgAnzahlGebinde = getInternalFrameBestellung().getTabbedPaneBestellung()
					.getPanelBestellungPositionenBottomD3().getBestellpositionDto().getNAnzahlgebinde();

			String toolTip = "";

			if (gebindeIId != null && bgAnzahlGebinde != null && bgAnzahlGebinde.doubleValue() != 0
					&& getInternalFrameBestellung().getTabbedPaneBestellung().getPanelBestellungPositionenBottomD3()
							.getBestellpositionDto().getNMenge() != null) {
				String gebinde = DelegateFactory.getInstance().getArtikelDelegate().gebindeFindByPrimaryKey(gebindeIId)
						.getCBez();

				BigDecimal bdGebindemenge = getInternalFrameBestellung().getTabbedPaneBestellung()
						.getPanelBestellungPositionenBottomD3().getBestellpositionDto().getNMenge()
						.divide(bgAnzahlGebinde, 6, BigDecimal.ROUND_HALF_EVEN);

				toolTip = gebinde + " (" + Helper.formatZahl(bdGebindemenge,
						Defaults.getInstance().getIUINachkommastellenMenge(), LPMain.getTheClient().getLocUi()) + " "
						+ getArtikelDto().getEinheitCNr().trim() + ")";

			}

			buttonGebinde.setToolTipText(toolTip);
		}

		BigDecimal umrechnungsfaktor = getArtikelDto().getNUmrechnungsfaktor();
		boolean bInvers = Helper.short2boolean(getArtikelDto().getbBestellmengeneinheitInvers());

		if (umrechnungsfaktor == null) {

			if (getArtikellieferantDto() != null && getArtikellieferantDto().getNVerpackungseinheit() != null
					&& getArtikellieferantDto().getEinheitCNrVpe() != null) {
				umrechnungsfaktor = getArtikellieferantDto().getNVerpackungseinheit();
				bInvers = true;
			}

		}

		if (artikelDto != null && umrechnungsfaktor != null && umrechnungsfaktor.doubleValue() != 0) {
			bGewichtVisible = true;
			if (wnfMenge.getBigDecimal() != null) {

				if (bInvers) {

					wnfGewichtbestellmenge.setBigDecimal(wnfMenge.getBigDecimal().divide(umrechnungsfaktor,
							Defaults.getInstance().getIUINachkommastellenPreiseEK(), BigDecimal.ROUND_HALF_EVEN));
					BigDecimal nPreisPerEinheit = (wnfNettopreis.getBigDecimal().multiply(umrechnungsfaktor));
					wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
				} else {
					wnfGewichtbestellmenge.setBigDecimal(wnfMenge.getBigDecimal().multiply(umrechnungsfaktor));
					BigDecimal nPreisPerEinheit = (wnfNettopreis.getBigDecimal().divide(umrechnungsfaktor,
							Defaults.getInstance().getIUINachkommastellenPreiseEK(), BigDecimal.ROUND_HALF_EVEN));
					wnfGewichtPreis.setBigDecimal(nPreisPerEinheit);
				}
			} else {
				wnfGewichtbestellmenge.setBigDecimal(umrechnungsfaktor);

				wnfGewichtPreis.setBigDecimal(null);
			}

			if (getArtikelDto().getEinheitCNrBestellung() != null) {
				wlaGewichtmengeEinheit.setText(getArtikelDto().getEinheitCNrBestellung().trim());
			} else if (getArtikellieferantDto() != null) {
				wlaGewichtmengeEinheit.setText(getArtikellieferantDto().getEinheitCNrVpe().trim());
			}
			if (getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
				wlaGewichtmengeEinheit.setText(wlaGewichtmengeEinheit.getText() + "  \u00E0");
			}
			wlaGewichtWaehrung.setText(tPBes.getBesDto().getWaehrungCNr());

		} else {
			wnfGewichtbestellmenge.setBigDecimal(null);
			wnfGewichtPreis.setBigDecimal(null);
		}
		wlaGewichtbestellmenge.setVisible(bGewichtVisible);
		wnfGewichtbestellmenge.setVisible(bGewichtVisible);
		wlaGewichtmengeEinheit.setVisible(bGewichtVisible);

		// nur Anzeigen wenn auch Recht fuer Preise sehen
		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			wlaGewichtPreis.setVisible(false);
			wnfGewichtPreis.setVisible(false);
			wlaGewichtWaehrung.setVisible(false);
		} else {
			wlaGewichtPreis.setVisible(bGewichtVisible);
			wnfGewichtPreis.setVisible(bGewichtVisible);
			wlaGewichtWaehrung.setVisible(bGewichtVisible);
		}

	}
}

class FocusAdapterGewichtPreisArtikel implements FocusListener {
	private PanelPositionenArtikelBestellung adaptee = null;

	FocusAdapterGewichtPreisArtikel(PanelPositionenArtikelBestellung adaptee) {
		this.adaptee = adaptee;
	}

	public void focusGained(FocusEvent e) {
		// nothing here
	}

	public void focusLost(FocusEvent e) {
		try {

			if (adaptee.wnfGewichtPreis.getBigDecimal() != null) {

				BigDecimal umrechnungsfaktor = adaptee.getArtikelDto().getNUmrechnungsfaktor();
				boolean bInvers = Helper.short2boolean(adaptee.getArtikelDto().getbBestellmengeneinheitInvers());

				if (umrechnungsfaktor == null) {

					if (adaptee.getArtikellieferantDto() != null
							&& adaptee.getArtikellieferantDto().getNVerpackungseinheit() != null
							&& adaptee.getArtikellieferantDto().getEinheitCNrVpe() != null) {
						umrechnungsfaktor = adaptee.getArtikellieferantDto().getNVerpackungseinheit();
						bInvers = true;
					}

				}

				if (umrechnungsfaktor != null) {
					BigDecimal umrechnung = new BigDecimal(0);
					BigDecimal preis = adaptee.wnfGewichtPreis.getBigDecimal();

					if (preis != null || umrechnungsfaktor != null) {
						if (bInvers) {
							umrechnung = preis.divide(umrechnungsfaktor,
									Defaults.getInstance().getIUINachkommastellenPreiseEK(),
									BigDecimal.ROUND_HALF_EVEN);
						} else {
							umrechnung = preis.multiply(umrechnungsfaktor);
						}

						adaptee.wnfEinzelpreis.setBigDecimal(umrechnung);
						// noch ein focuslost auf den Einzelpreis erzeugen,
						// damit sich der nettopreis mit aendert
						adaptee.wnfEinzelpreis.requestFocusInWindow();
						adaptee.wtfBezeichnung.requestFocusInWindow();
						// den Focus auf den Einzelpreis setzen
						adaptee.wnfEinzelpreis.requestFocusInWindow();
					}
				}
			}

		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}

class FocusAdapterGewichtArtikel implements FocusListener {
	private PanelPositionenArtikelBestellung adaptee = null;

	FocusAdapterGewichtArtikel(PanelPositionenArtikelBestellung adaptee) {
		this.adaptee = adaptee;
	}

	public void focusGained(FocusEvent e) {
		// nothing here
	}

	public void focusLost(FocusEvent e) {
		try {

			if (adaptee.wnfGewichtbestellmenge.getBigDecimal() != null) {

				BigDecimal umrechnungsfaktor = adaptee.getArtikelDto().getNUmrechnungsfaktor();
				boolean bInvers = Helper.short2boolean(adaptee.getArtikelDto().getbBestellmengeneinheitInvers());

				if (umrechnungsfaktor == null) {

					if (adaptee.getArtikellieferantDto() != null
							&& adaptee.getArtikellieferantDto().getNVerpackungseinheit() != null
							&& adaptee.getArtikellieferantDto().getEinheitCNrVpe() != null) {
						umrechnungsfaktor = adaptee.getArtikellieferantDto().getNVerpackungseinheit();
						bInvers = true;
					}

				}

				if (umrechnungsfaktor != null) {
					BigDecimal umrechnung = new BigDecimal(0);
					BigDecimal gewicht = adaptee.wnfGewichtbestellmenge.getBigDecimal();

					if (gewicht != null || umrechnungsfaktor != null) {

						if (bInvers) {

							umrechnung = gewicht.multiply(umrechnungsfaktor);
						} else {
							umrechnung = gewicht.divide(umrechnungsfaktor, 4, BigDecimal.ROUND_HALF_EVEN);
						}
						adaptee.wnfMenge.setBigDecimal(umrechnung);
					}
				}
			}

		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}
