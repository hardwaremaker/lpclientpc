package com.lp.client.rechnung;

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

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditor;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.AbrechnungsvorschlagUeberleitenDto;
import com.lp.server.rechnung.service.ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>
 * <I>Dialog zur Eingabe der Kriterien fuer die Ueberleitung des
 * Bestellvorschlags in Belege.</I>
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
 * @version $Revision: 1.6 $
 */
public class PanelDialogAbrechnungsvorschlagUeberleitenPersonalUndMaschinenzeiten extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel[] wlaArtikel = null;

	private JLabel[] wlaBezeichnung = null;
	private JLabel[] wlaZusatzBezeichnung = null;
	private WrapperLabel[] wlaDauerOffen = null;
	private WrapperLabel[] wlaVKPreisProStunde = null;
	private WrapperNumberField[] wnfZuVerrechnen = null;
	private LpEditor[] wefKommentar = null;
	private Integer[] artikelIIds = null;

	private BigDecimal[] bdVKPreiseFuerAbrechnung = null;

	WrapperNumberField wnfZuVerrechnenGesamt = new WrapperNumberField();

	private WrapperCheckBox wcbErledigt = new WrapperCheckBox();

	private BigDecimal bdStunden;
	private BigDecimal bdOffenGesamt = BigDecimal.ZERO;
	private String rechnungswaehrung;

	private KundeDto kundeDto = null;

	private JScrollPane jspScrollPane = new JScrollPane();
	private AbrechnungsvorschlagUeberleitenDto ueberleitungsvorschlagDto = null;

	Map<String, ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto> zeilenVerdichtet = null;

	public PanelDialogAbrechnungsvorschlagUeberleitenPersonalUndMaschinenzeiten(Integer kundeIId,
			AbrechnungsvorschlagUeberleitenDto ueberleitungsvorschlagDto, InternalFrame oInternalFrameI, String title, String rechnungswaehrung)
			throws Throwable {
		super(oInternalFrameI, title);
		this.ueberleitungsvorschlagDto = ueberleitungsvorschlagDto;
		this.kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(kundeIId);
		this.rechnungswaehrung=rechnungswaehrung;
		jbInit();
		setDefaults();
		initComponents();

	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		// Ueberschrift

		wcbErledigt.setText(LPMain.getInstance().getTextRespectUISPr("label.erledigt"));

		wcbErledigt.setSelected(true);

		iZeile++;
		jpaWorkingOn.add(new WrapperLabel("Offen gesamt selektiert"), new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		WrapperNumberField wnfOffenGesamt = new WrapperNumberField();

		wnfOffenGesamt.setFractionDigits(3);
		wnfOffenGesamt.setMandatoryField(true);
		wnfOffenGesamt.setEditable(false);

		jpaWorkingOn.add(wnfOffenGesamt, new GridBagConstraints(7, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(new JLabel("Artikel"), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(new JLabel("Bezeichnung"), new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new JLabel("Zusatzbez."), new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel("Verrechenbar"), new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel("VKPreis/h"), new GridBagConstraints(6, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(new WrapperLabel("zu verrechnen"), new GridBagConstraints(7, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		// Nun pro Artikel verdichten

		zeilenVerdichtet = ueberleitungsvorschlagDto.getZeilenVerdichtetNachArtikelOderAuftragsposition();

		if (zeilenVerdichtet != null && zeilenVerdichtet.size() > 0) {

			wlaArtikel = new JLabel[zeilenVerdichtet.size()];

			wlaBezeichnung = new JLabel[zeilenVerdichtet.size()];
			wlaZusatzBezeichnung = new JLabel[zeilenVerdichtet.size()];
			wlaDauerOffen = new WrapperLabel[zeilenVerdichtet.size()];

			wlaVKPreisProStunde = new WrapperLabel[zeilenVerdichtet.size()];
			bdVKPreiseFuerAbrechnung = new BigDecimal[zeilenVerdichtet.size()];
			wnfZuVerrechnen = new WrapperNumberField[zeilenVerdichtet.size()];
			wefKommentar = new LpEditor[zeilenVerdichtet.size()];
			artikelIIds = new Integer[zeilenVerdichtet.size()];

			Iterator it = zeilenVerdichtet.keySet().iterator();
			int i = 0;
			while (it.hasNext()) {

				String  key = (String) it.next();
				
				ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto zeile = zeilenVerdichtet
						.get(key);

				
				Integer artikelIId = zeile.getArtikelIId();

				artikelIIds[i] = artikelIId;

				

				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(artikelIId);

				wlaArtikel[i] = new JLabel(artikelDto.getCNr());
				wlaBezeichnung[i] = new JLabel(artikelDto.getCBezAusSpr());
				wlaZusatzBezeichnung[i] = new JLabel(artikelDto.getCZBezAusSpr());

				wlaDauerOffen[i] = new WrapperLabel(
						Helper.formatZahlWennUngleichNull(zeile.getBdOffen(), 2, LPMain.getTheClient().getLocUi()));

				bdOffenGesamt = bdOffenGesamt.add(zeile.getBdOffen());

				VerkaufspreisDto vkpreisDto = DelegateFactory.getInstance().getVkPreisfindungDelegate()
						.getVKPreisEinfachInZielwaehrungZuHeute(kundeDto.getIId(), artikelIId, zeile.getBdOffen(),rechnungswaehrung);

				// Preisberechnung
				BigDecimal bdVKPreis = BigDecimal.ZERO;

				if (vkpreisDto != null) {

					wlaVKPreisProStunde[i] = new WrapperLabel(Helper.formatZahlWennUngleichNull(
							vkpreisDto.getNettpreisOhneMaterialzuschlag(), 2, LPMain.getTheClient().getLocUi()));

					if (vkpreisDto.getNettpreisOhneMaterialzuschlag() != null) {
						bdVKPreis = vkpreisDto.getNettpreisOhneMaterialzuschlag();
					}

				} else {
					wlaVKPreisProStunde[i] = new WrapperLabel("kein VKPreis");
				}

				if (zeile.getAuftragspositionIId() != null) {
					AuftragpositionDto aufPosDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
							.auftragpositionFindByPrimaryKey(zeile.getAuftragspositionIId());

					if (aufPosDto.getArtikelIId() != null
							&& aufPosDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() != null) {
						
						//SP9093
						
						ArtikelDto artikelDtoAufPos = DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByPrimaryKey(aufPosDto.getArtikelIId());
						
						if(Helper.short2boolean(artikelDtoAufPos.getBKalkulatorisch())&& aufPosDto.getPositionIIdZugehoerig()!=null){
							aufPosDto=DelegateFactory.getInstance().getAuftragpositionDelegate()
									.auftragpositionFindByPrimaryKey(aufPosDto.getPositionIIdZugehoerig());

						}
						
						VerkaufspreisDto vkpreisDtoAuftrag = DelegateFactory.getInstance().getVkPreisfindungDelegate()
								.getVKPreisEinfachInZielwaehrungZuHeute(kundeDto.getIId(), aufPosDto.getArtikelIId(),
										zeile.getBdOffen(),rechnungswaehrung);
						if (vkpreisDtoAuftrag != null && vkpreisDtoAuftrag.getNettpreisOhneMaterialzuschlag() != null
								&& vkpreisDtoAuftrag.getNettpreisOhneMaterialzuschlag().doubleValue() != 0) {
							// Faktor bestimmen

							BigDecimal faktor = bdVKPreis.divide(vkpreisDtoAuftrag.getNettpreisOhneMaterialzuschlag(),
									4, BigDecimal.ROUND_HALF_EVEN);

							BigDecimal bdPreis = aufPosDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
									.multiply(faktor);

							wlaVKPreisProStunde[i] = new WrapperLabel(
									Helper.formatZahlWennUngleichNull(bdPreis, 2, LPMain.getTheClient().getLocUi()));

							bdVKPreiseFuerAbrechnung[i] = bdPreis;

						}
					}
					
					if(aufPosDto.getCBez()!=null) {
						wlaBezeichnung[i].setText(aufPosDto.getCBez());
					}
				
					if(aufPosDto.getCZusatzbez()!=null) {
						wlaZusatzBezeichnung[i].setText(aufPosDto.getCZusatzbez());
					}

				}

				WrapperNumberField wnf = new WrapperNumberField();
				
				wnf.setFractionDigits(3);
				wnf.setBigDecimal(zeile.getBdOffen());
				wnf.setMandatoryField(true);
				wnf.addKeyListener(this);
				wnfZuVerrechnen[i] = wnf;

				LpEditor lpe = new LpEditor(null);
				lpe.setInternalFrame(getInternalFrame());

				wefKommentar[i] = lpe;

				iZeile++;

				jpaWorkingOn.add(wlaArtikel[i], new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

				jpaWorkingOn.add(wlaBezeichnung[i], new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
				jpaWorkingOn.add(wlaZusatzBezeichnung[i], new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
				jpaWorkingOn.add(wlaDauerOffen[i], new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

				jpaWorkingOn.add(wlaVKPreisProStunde[i], new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
				jpaWorkingOn.add(wnfZuVerrechnen[i], new GridBagConstraints(7, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
				iZeile++;
				jpaWorkingOn.add(wefKommentar[i],
						new GridBagConstraints(0, iZeile, 8, 1, 0.0, 0.0, GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -1000, -1000));
				i++;
			}

		}
		iZeile++;

		jpaWorkingOn.add(new WrapperLabel("Zu verrechen Gesamt"), new GridBagConstraints(6, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		wnfZuVerrechnenGesamt.setFractionDigits(3);
		wnfZuVerrechnenGesamt.setMandatoryField(true);
		wnfZuVerrechnenGesamt.setEditable(false);
		wnfZuVerrechnenGesamt.setBigDecimal(bdOffenGesamt);

		jpaWorkingOn.add(wnfZuVerrechnenGesamt, new GridBagConstraints(7, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbErledigt, new GridBagConstraints(7, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		wnfOffenGesamt.setBigDecimal(bdOffenGesamt);

		jspScrollPane.setViewportView(jpaWorkingOn);
		jspScrollPane.setAutoscrolls(true);

		this.add(jspScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	}

	private void setDefaults() throws Throwable {

	}

	protected void eventKeyTyped(KeyEvent e) throws Throwable {

	}

	protected void eventKeyPressed(KeyEvent e) throws Throwable {

	}

	protected void eventKeyReleased(KeyEvent e) throws Throwable {
		// berechneGesamt
		BigDecimal bdGesamt = BigDecimal.ZERO;
		for (int i = 0; i < wnfZuVerrechnen.length; i++) {
			if (wnfZuVerrechnen[i].getBigDecimal() != null) {
				bdGesamt = bdGesamt.add(wnfZuVerrechnen[i].getBigDecimal());
			}
		}
		wnfZuVerrechnenGesamt.setBigDecimal(bdGesamt);
		if (bdGesamt.doubleValue() < bdOffenGesamt.doubleValue()) {
			wcbErledigt.setSelected(false);
		} else {
			wcbErledigt.setSelected(true);
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			if (allMandatoryFieldsSetDlg()) {

				
				Iterator it = zeilenVerdichtet.keySet().iterator();
				int i = 0;
				while (it.hasNext()) {

					String key = (String) it.next();

					ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto zeile = zeilenVerdichtet
							.get(key);

					zeile.setBdZuverrechnenVomClient(wnfZuVerrechnen[i].getBigDecimal());
					zeile.setKommentarVomClient(wefKommentar[i].getText());
					zeile.setBdPreisVomClient(bdVKPreiseFuerAbrechnung[i]);
					zeilenVerdichtet.put(key, zeile);
					i++;
				}

				DelegateFactory.getInstance().getRechnungDelegate().abrechnungsvorschlagUeberleitenZeitdaten(zeilenVerdichtet,
						wcbErledigt.isSelected(), ((InternalFrameRechnung) getInternalFrame()).neuDatum);

				super.eventActionSpecial(e);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			super.eventActionSpecial(e);
		}
	}

	public void focusLost(FocusEvent e) {
		/*
		 * try { if (e.getSource().equals(wnfBetragZuVerrechnen) &&
		 * wnfBetragZuVerrechnen.getBigDecimal() != null &&
		 * wnfBetragAusPositionen.getBigDecimal() != null) {
		 * 
		 * if (wnfBetragZuVerrechnen.getBigDecimal().doubleValue() >=
		 * wnfBetragAusPositionen.getBigDecimal() .doubleValue()) {
		 * wcbErledigt.setSelected(true); } else { wcbErledigt.setSelected(false); }
		 * 
		 * } else if (e.getSource().equals(wnfStundenZuVerrechnen) &&
		 * wnfStundenZuVerrechnen.getBigDecimal() != null &&
		 * wnfStundenAusPositionen.getBigDecimal() != null) {
		 * 
		 * if (wnfStundenZuVerrechnen.getBigDecimal().doubleValue() >=
		 * wnfStundenAusPositionen.getBigDecimal() .doubleValue()) {
		 * wcbErledigt.setSelected(true); } else { wcbErledigt.setSelected(false); }
		 * 
		 * } } catch (ExceptionLP e1) { handleException(e1, true); }
		 */
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

}
