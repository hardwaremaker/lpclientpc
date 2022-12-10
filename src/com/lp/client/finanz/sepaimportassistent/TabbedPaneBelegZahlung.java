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
package com.lp.client.finanz.sepaimportassistent;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lp.client.finanz.FinanzSepaBankbuchungController;
import com.lp.client.finanz.InternalFrameFinanz;
import com.lp.client.finanz.PanelFinanzBankbuchung;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.view.IDataUpdateListener;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.BuchungKompakt;
import com.lp.server.finanz.service.ISepaImportResult;
import com.lp.server.finanz.service.SepaBetrag;
import com.lp.server.finanz.service.SepaHabenBetrag;
import com.lp.server.finanz.service.SepaSollBetrag;
import com.lp.server.util.BelegAdapter;
import com.lp.server.util.BelegZahlungAdapter;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

import net.miginfocom.swing.MigLayout;

public class TabbedPaneBelegZahlung extends JTabbedPane implements ChangeListener, INochVerfuegbarerZahlbetragUpdater {
	
	private static final long serialVersionUID = 1L;

	public static final int IDX_UEBERSICHT = 0;
	public static final int IDX_EINGANGSRECHNUNG = 1;
	public static final int IDX_AUSGANGSRECHNUNG = 2;
	public static final int IDX_MANUELLEBUCHUNG = 3;
	
	protected static final String BUTTON_ACTION_ZAHLUNG_UEBERNEHMEN = 
			"button_action_zahlung_uebernehmen";
	protected static final String BUTTON_ACTION_ZURUECK_AUS_MANUELLER_AUSWAHL = 
			"button_action_zurueck_aus_manueller_auswahl";
	protected static final String ACTION_ADD_ON_AR = "_add_on_ar";
	protected static final String ACTION_ADD_ON_ER = "_add_on_er";
	
	public SimpleDateFormat dateFormat = new SimpleDateFormat(SepaImportModel.DATE_FORMAT);
	
	private PanelManuelleAuswahlUebersicht panelUebersicht;
	private SepaAuswahlZahlungenPanel panelEingangsrechnung;
	private SepaAuswahlZahlungenPanel panelAusgangsrechnung;
	private PanelFinanzBankbuchung panelBankbuchung;
	private PanelRahmenManuelleAuswahl panelRahmenUebersicht;
	private PanelRahmenManuelleAuswahl panelRahmenEingangsrechnung;
	private PanelRahmenManuelleAuswahl panelRahmenAusgangsrechnung;
	private PanelRahmenManuelleAuswahl panelRahmenBankbuchung;
	
	private List<IManuelleAuswahlRahmenInfoUpdater> panelRahmenListener;
	private List<IDataUpdateListener> manuelleAuswahlUpdateListener;
	private ActionListener actionListener;
	private IManuelleAuswahlController controller;
	private InternalFrameFinanz internalFrameFinanz;
	private FinanzSepaBankbuchungController bankbuchungController;

	public TabbedPaneBelegZahlung(InternalFrameFinanz internalFrameFinanz, ActionListener listener, 
			IManuelleAuswahlController controller) throws Throwable {
		actionListener = listener;
		this.controller = controller;
		this.internalFrameFinanz = internalFrameFinanz;
		bankbuchungController = new FinanzSepaBankbuchungController(this);
		panelRahmenListener = new ArrayList<IManuelleAuswahlRahmenInfoUpdater>();
		manuelleAuswahlUpdateListener = new ArrayList<IDataUpdateListener>();
		
		jbInit();
	}
	
	private void jbInit() throws Throwable {
		panelUebersicht = new PanelManuelleAuswahlUebersicht(new ManuelleAuswahlUebersichtController(controller, this));
		panelRahmenUebersicht = new PanelRahmenManuelleAuswahl(panelUebersicht);
		bankbuchungController.registerBuchungDataUpdateListener(panelUebersicht);
		registerRahmenInfoUpdateListener(panelRahmenUebersicht);
		registerManuelleAuswahlUpdateListener(panelUebersicht);
		insertTab(LPMain.getTextRespectUISPr("lp.uebersicht"), 
				null, panelRahmenUebersicht, 
				LPMain.getTextRespectUISPr("lp.uebersicht"), 
				IDX_UEBERSICHT);
		
		panelEingangsrechnung = new SepaAuswahlERZahlungenPanel(this);
		panelEingangsrechnung.updateTableData();
		registerManuelleAuswahlUpdateListener(panelEingangsrechnung);
		panelRahmenEingangsrechnung = new PanelRahmenManuelleAuswahl(panelEingangsrechnung);
		registerRahmenInfoUpdateListener(panelRahmenEingangsrechnung);
		insertTab(LPMain.getTextRespectUISPr("fb.sepa.import.tab.eingangsrechnung"), 
				null, panelRahmenEingangsrechnung, 
				LPMain.getTextRespectUISPr("fb.sepa.import.tab.eingangsrechnung.tooltip"), 
				IDX_EINGANGSRECHNUNG);

		panelAusgangsrechnung = new SepaAuswahlARZahlungenPanel(this);
		panelAusgangsrechnung.updateTableData();
		registerManuelleAuswahlUpdateListener(panelAusgangsrechnung);
		panelRahmenAusgangsrechnung = new PanelRahmenManuelleAuswahl(panelAusgangsrechnung);
		registerRahmenInfoUpdateListener(panelRahmenAusgangsrechnung);
		insertTab(LPMain.getTextRespectUISPr("fb.sepa.import.tab.rechnung"), 
				null, panelRahmenAusgangsrechnung, 
				LPMain.getTextRespectUISPr("fb.sepa.import.tab.rechnung.tooltip"), 
				IDX_AUSGANGSRECHNUNG);
		
		if (getController().hasFibu()) {
			panelBankbuchung = new PanelFinanzBankbuchung(bankbuchungController, bankbuchungController);
			panelRahmenBankbuchung = new PanelRahmenManuelleAuswahl(panelBankbuchung);
			registerRahmenInfoUpdateListener(panelRahmenBankbuchung);
			insertTab(LPMain.getTextRespectUISPr("fb.sepa.import.tab.manuellebuchung"), 
					null, panelRahmenBankbuchung, 
					LPMain.getTextRespectUISPr("fb.sepa.import.tab.manuellebuchung.tooltip"), 
					IDX_MANUELLEBUCHUNG);
		}
		this.addChangeListener(this);
	}
	
	public void registerManuelleAuswahlUpdateListener(IDataUpdateListener listener) {
		manuelleAuswahlUpdateListener.add(listener);
	}
	
	protected void notifyManuelleAuswahlUpdateListener() {
		for (IDataUpdateListener listener : manuelleAuswahlUpdateListener) {
			listener.dataUpdated();
		}
	}
	
	public void registerRahmenInfoUpdateListener(IManuelleAuswahlRahmenInfoUpdater listener) {
		panelRahmenListener.add(listener);
	}
	
	private void notifyNochVerfuegbarerZahlbetragChanged() {
		SepaBetrag nochVerfuegbar = getNochVerfuegbarerZahlbetrag2();
		for (IManuelleAuswahlRahmenInfoUpdater listener : panelRahmenListener) {
			try {
				listener.updateBetragNochVerfuegbar(nochVerfuegbar);
			} catch (ExceptionLP e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void notifyZahlungsinfoChanged() {
		for (IManuelleAuswahlRahmenInfoUpdater listener : panelRahmenListener) {
			listener.updateZahlungsinfo(getZahlungsinfo());
		}
	}

	private void notifyEnableUebernehmen(Boolean enable) {
		for (IManuelleAuswahlRahmenInfoUpdater listener : panelRahmenListener) {
			listener.enableUebernehmen(enable);
		}
	}

	public BelegAdapter getSelectedBeleg(int index) {
		if (IDX_EINGANGSRECHNUNG == index) {
			return panelEingangsrechnung.getSelectedBeleg();
		} else if (IDX_AUSGANGSRECHNUNG == index) {
			return panelAusgangsrechnung.getSelectedBeleg();
		}
		
		return null;
	}

	protected void manuelleAuswahlSelected() {
		notifyManuelleAuswahlUpdateListener();
		notifyZahlungsinfoChanged();
		updateNochVerfuegbarerZahlbetrag();
		updateButtonUebernehmen();
		enableAllTabs();
		panelAusgangsrechnung.clearRowFilter();
		panelEingangsrechnung.clearRowFilter();
	}
	
	public IManuelleAuswahlController getController() {
		return controller;
	}
	public InternalFrameFinanz getInternalFrameFinanz() {
		return internalFrameFinanz;
	}
	
	private ISepaImportResult getResultWaitingForAuswahl() {
		return controller.getResultWaitingForManuelleAuswahl();
	}
	
	public SepaBetrag getNochVerfuegbarerZahlbetrag2() {
		ISepaImportResult result = getResultWaitingForAuswahl();
		SepaBetrag bdNochverfuegbar = result.getPayment().getBetrag();
		
		for (BelegZahlungAdapter belegZahlung : result.getManualPayments()) {
			SepaBetrag toAdd = null;
			if (belegZahlung.isRechnungzahlungAdapter()) {
				toAdd = BigDecimal.ZERO.compareTo(belegZahlung.getNBetragfw()) < 1 ? 
						new SepaSollBetrag(belegZahlung.getNBetragfw()) : new SepaHabenBetrag(belegZahlung.getNBetragfw().negate());
			} else {
				toAdd = BigDecimal.ZERO.compareTo(belegZahlung.getNBetragfw()) < 1 ? 
						new SepaHabenBetrag(belegZahlung.getNBetragfw()) : new SepaSollBetrag(belegZahlung.getNBetragfw().negate());
			}
			bdNochverfuegbar = bdNochverfuegbar.subtract(toAdd);
		}
		
		for (BuchungKompakt buchung : result.getManualBookings()) {
			int idxBuchungdetail = 
					buchung.getBuchungdetailList().get(0).getKontoIId().equals(getController().getBankkontoIId()) ? 0 : 1;
			SepaBetrag toAdd = null;
			if (buchung.getBuchungdetailList().get(idxBuchungdetail).isHabenBuchung()) {
				toAdd = new SepaHabenBetrag(buchung.getBuchungdetailList().get(idxBuchungdetail).getNBetragKontowaehrung());
			} else {
				toAdd = new SepaSollBetrag(buchung.getBuchungdetailList().get(idxBuchungdetail).getNBetragKontowaehrung());
			}
			bdNochverfuegbar = bdNochverfuegbar.subtract(toAdd);
		}
		
		return bdNochverfuegbar;
	}

	public void updateNochVerfuegbarerZahlbetrag() {
		notifyNochVerfuegbarerZahlbetragChanged();
	}
	
	public void updateButtonUebernehmen() {
		if (isNochBetragVerfuegbar()) {
			notifyEnableUebernehmen(true);
		} else {
			notifyEnableUebernehmen(false);
		}
	}
	
	public boolean isNochBetragVerfuegbar() {
		BigDecimal bdNochVerfuegbar = getNochVerfuegbarerZahlbetrag2().getWert();
		return BigDecimal.ZERO.compareTo(bdNochVerfuegbar) == 0 ||
				BigDecimal.ZERO.compareTo(bdNochVerfuegbar) > 0;
	}
	
	@Override
	public void dataChanged() {
		updateNochVerfuegbarerZahlbetrag();
		updateButtonUebernehmen();
	}

	@Override
	public void actionEdit(BelegZahlungAdapter belegZahlungAdapter) {
		if (belegZahlungAdapter.isEingangsrechnungzahlungAdapter()) {
			setSelectedIndex(IDX_EINGANGSRECHNUNG);
			panelEingangsrechnung.setSelection(belegZahlungAdapter);
		} else if (belegZahlungAdapter.isRechnungzahlungAdapter()) {
			setSelectedIndex(IDX_AUSGANGSRECHNUNG);
			panelAusgangsrechnung.setSelection(belegZahlungAdapter);
		}
	}

	@Override
	public void actionEdit(BuchungKompakt buchung) {
		setSelectedIndex(IDX_MANUELLEBUCHUNG);
		enableTabsExceptCurrent(false);
		try {
			bankbuchungController.actionYouAreSelected(buchung);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void enableTabsExceptCurrent(boolean enable) {
		int paneIndex = getSelectedIndex();
		for (int idx=0; idx < getTabCount(); idx++) {
			if (idx != paneIndex) setEnabledAt(idx, enable);
		}
	}
	
	private void enableAllTabs() {
		for (int idx=0; idx < getTabCount(); idx++) {
			setEnabledAt(idx, true);
		}
	}

	public String getZahlungsinfo() {
		return getController().getInfoBuchungszeile(getResultWaitingForAuswahl());
//		SepaZahlung zahlung = getResultWaitingForAuswahl().getPayment();
//		StringBuilder sb = new StringBuilder();
//		
//		Date buchungsdatum = getController().getZahldatum();
//		sb.append(dateFormat.format(buchungsdatum));
//		sb.append(" | ");
//		sb.append(zahlung.getBeteiligter().getName());
//		sb.append(" | Betrag: ");
//		sb.append(zahlung.getBetrag().getPlusMinusWert());
//		sb.append(" | Zweck/Referenz: ");
//		if (zahlung.isSammler()) {
//			sb.append(LPMain.getTextRespectUISPr("fb.sepa.import.sammlerbuchung") 
//						+ "("+ String.valueOf(zahlung.getEnthalteneBuchungen()) + ")");
//		} else {
//			sb.append(zahlung.getZahlungsreferenz() != null ? zahlung.getZahlungsreferenz() : zahlung.getVerwendungszweck());
//		}
//		return sb.toString();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		try {
			if (e.getSource() instanceof TabbedPaneBelegZahlung) {
				if (IDX_EINGANGSRECHNUNG == this.getSelectedIndex()) {
//					tableEingangsrechnung.actionTabChanged();
				} else if (IDX_AUSGANGSRECHNUNG == this.getSelectedIndex()) {
//					tableAusgangsrechnung.actionTabChanged();
				} else if (IDX_MANUELLEBUCHUNG == this.getSelectedIndex()) {
					bankbuchungController.actionYouAreSelectedWithRefresh();
				}
			}
		} catch (ExceptionLP e1) {
			throw new EJBExceptionLP(e1);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
		
	}

	public void actionBuchungEdited() {
		enableAllTabs();
	}

	public class PanelRahmenManuelleAuswahl extends JPanel implements IManuelleAuswahlRahmenInfoUpdater {
		
		private static final long serialVersionUID = 8511644489688765775L;

		private WrapperLabel wlZahlungsinfo;
		private WrapperTextField wtZahlungsinfo;
		private WrapperButton wbUebernehmen;
		private WrapperButton wbZurueck;
		private WrapperLabel wlNochVerfuegbar;
		private WrapperNumberField wnfNochVerfuegbar;
		private WrapperLabel wlWaehrung;
		private JPanel panelDetail;
		
		public PanelRahmenManuelleAuswahl() throws ExceptionLP {
			this(new JPanel());
		}
		
		public PanelRahmenManuelleAuswahl(JPanel detail) throws ExceptionLP {
			setDetailPanel(detail);
			jbInit();
		}
		
		private void jbInit() throws ExceptionLP {
			wlZahlungsinfo = new WrapperLabel();
			wlZahlungsinfo.setText(LPMain.getTextRespectUISPr("fb.sepa.import.head.zahlung"));
			wtZahlungsinfo = new WrapperTextField(Facade.MAX_UNBESCHRAENKT);
			wtZahlungsinfo.setEditable(false);
			
			wbUebernehmen = new WrapperButton();
			wbUebernehmen.setText(LPMain.getTextRespectUISPr("fb.sepa.import.head.uebernehmen"));
			wbUebernehmen.addActionListener(actionListener);
			wbUebernehmen.setActionCommand(
					TabbedPaneBelegZahlung.BUTTON_ACTION_ZAHLUNG_UEBERNEHMEN);
			wbUebernehmen.setEnabled(false);
			
			wbZurueck = new WrapperButton();
			wbZurueck.setText(LPMain.getTextRespectUISPr("lp.zurueck"));
			wbZurueck.addActionListener(actionListener);
			wbZurueck.setActionCommand(
					TabbedPaneBelegZahlung.BUTTON_ACTION_ZURUECK_AUS_MANUELLER_AUSWAHL);
			
			wlNochVerfuegbar = new WrapperLabel();
			wlNochVerfuegbar.setText(LPMain.getTextRespectUISPr("fb.sepa.import.nochverfuegbar"));
			wnfNochVerfuegbar = new WrapperNumberField();
			wnfNochVerfuegbar.setEditable(false);
			wnfNochVerfuegbar.setHorizontalAlignment(SwingConstants.RIGHT);
			wlWaehrung = new WrapperLabel();
			wlWaehrung.setText(controller.getWaehrung());
			wlWaehrung.setHorizontalAlignment(SwingConstants.LEFT);
			
			panelDetail.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			
			setLayout(new MigLayout("wrap 7, hidemode 2, fill", "[10%,fill|10%,fill|20%,fill|20%,fill|20%,fill|20%,fill|30px,fill]", "[fill|fill, grow|fill]"));
			
			add(wlZahlungsinfo, "align left");
			add(wtZahlungsinfo, "span");
			
			add(panelDetail, "span");
			
			add(wlNochVerfuegbar, "skip 4, al right");
			add(wnfNochVerfuegbar);
			add(wlWaehrung);
			
			add(wbZurueck, "span 2");
			add(wbUebernehmen);
		}
		
		private void setTextNochVerfuegbar(String text) {
			StringBuilder builder = new StringBuilder(LPMain.getTextRespectUISPr("fb.sepa.import.nochverfuegbar"));
			builder.append(" (");
			builder.append(text);
			builder.append(")");
			wlNochVerfuegbar.setText(builder.toString());
		}
		
		@Override
		public void enableUebernehmen(Boolean enable) {
			wbUebernehmen.setEnabled(enable);
		}
		
		@Override
		public void updateBetragNochVerfuegbar(BigDecimal betrag)
				throws ExceptionLP {
			wnfNochVerfuegbar.setBigDecimal(betrag);
		}

		@Override
		public void updateBetragNochVerfuegbar(SepaBetrag betrag) throws ExceptionLP {
			if (betrag.isSoll()) {
				setTextNochVerfuegbar(LPMain.getTextRespectUISPr("finanz.soll"));
			} else {
				setTextNochVerfuegbar(LPMain.getTextRespectUISPr("finanz.haben"));
			}
			wnfNochVerfuegbar.setBigDecimal(betrag.getWert());
		}

		@Override
		public void updateZahlungsinfo(String zahlungsinfo) {
			wtZahlungsinfo.setText(zahlungsinfo);
		}

		public void setDetailPanel(JPanel detail) {
			panelDetail = detail;
		}

	}
	
	public interface IManuelleAuswahlRahmenInfoUpdater {
		public void enableUebernehmen(Boolean enable);
		
		public void updateBetragNochVerfuegbar(BigDecimal betrag) throws ExceptionLP;
		
		public void updateBetragNochVerfuegbar(SepaBetrag betrag) throws ExceptionLP;
		
		public void updateZahlungsinfo(String zahlungsinfo);
	}

}
