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
package com.lp.client.finanz;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.SwingWorker;

import com.lp.client.finanz.sepaimportassistent.TabbedPaneBelegZahlung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.view.IDataUpdateListener;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.HelperTimestamp;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungKompakt;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.Iso20022BuchungdetailDto;
import com.lp.server.finanz.service.Iso20022StandardEnum;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.SepaBetrag;
import com.lp.server.finanz.service.SepaHabenBetrag;
import com.lp.server.finanz.service.SepaSollBetrag;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.util.Helper;

public class FinanzSepaBankbuchungController implements IFinanzBankbuchungController, IFinanzBankbuchungViewController, ItemChangedListener {

	private TabbedPaneBelegZahlung tabbedPane;
	private FinanzBankbuchungModel bankbuchungModel;
	private List<IGegenkontoListener> gegenkontoListener;
	private List<IKostenstelleListener> kostenstelleListener;
	private List<IBetragListener> buchungsbetragListener;
	private List<IMwstListener> mwstListener;
	private List<IDatumListener> datumListener;
	private List<IBankkontoListener> bankkontoListener;
	private List<IResetListener> resetListener;
	private List<IWaehrungListener> waehrungListener;
	private List<IDataUpdateListener> buchungDataUpdateListener;
	private List<IInfoListener> infoListener;
	private List<IBelegnummerListener> belegnummerListener;
	private List<ITextListener> textListener;
	private List<IBuchungsartListener> buchungsartListener;
	
	private PanelQueryFLR panelQueryFLRGegenkonto;
	private PanelQueryFLR panelQueryFLRKostenstelle;
	private BuchungKompakt buchungToEdit;
	
	public FinanzSepaBankbuchungController(TabbedPaneBelegZahlung tabbedPane) throws Throwable {
		this.tabbedPane = tabbedPane;
		initListener();

		tabbedPane.getInternalFrameFinanz().addItemChangedListener(this);
	}
	
	private void initListener() {
		gegenkontoListener = new ArrayList<IGegenkontoListener>();
		kostenstelleListener = new ArrayList<IKostenstelleListener>();
		buchungsbetragListener = new ArrayList<IBetragListener>();
		mwstListener = new ArrayList<IMwstListener>();
		datumListener = new ArrayList<IDatumListener>();
		bankkontoListener = new ArrayList<IBankkontoListener>();
		resetListener = new ArrayList<IResetListener>();
		waehrungListener = new ArrayList<IWaehrungListener>();
		buchungDataUpdateListener = new ArrayList<IDataUpdateListener>();
		infoListener = new ArrayList<IInfoListener>();
		belegnummerListener = new ArrayList<IBelegnummerListener>();
		textListener = new ArrayList<ITextListener>();
		buchungsartListener = new ArrayList<IBuchungsartListener>();
	}
	
	public void actionYouAreSelected() throws Throwable {
		buchungToEdit = null;
		model().setAuszugnummer(getAuszugsnummer(tabbedPane.getController().getZahldatum()));
		updateViewData();
		checkAndSetMwstSatz(true);
	}
	
	public void actionYouAreSelectedWithRefresh() throws Throwable {
		resetSepaBankbuchungModel();
		actionYouAreSelected();
	}
	
	public void actionYouAreSelected(BuchungKompakt buchung) throws Throwable {
//		refreshSepaBankbuchungModel();
		buchungToEdit = buchung;
		setBuchung2Model(buchung);
		updateViewData();
		checkAndSetMwstSatz(false);
	}

	private void setBuchung2Model(BuchungKompakt buchung) throws Throwable {
		model().setBelegnummer(buchung.getBuchungDto().getCBelegnummer());
		model().setText(buchung.getBuchungDto().getCText());
		model().setBuchungsdatum(buchung.getBuchungDto().getDBuchungsdatum());
		KostenstelleDto kostenstelleDto = DelegateFactory.getInstance()
				.getSystemDelegate().kostenstelleFindByPrimaryKey(buchung.getBuchungDto().getKostenstelleIId());
		model().setKostenstelleDto(kostenstelleDto);
		
		BuchungdetailDto detailBankkonto = getBuchungdetailBankkonto(buchung.getBuchungdetailList());
		model().setKommentar(detailBankkonto.getCKommentar());
		KontoDto gegenkontoDto = DelegateFactory.getInstance()
				.getFinanzDelegate().kontoFindByPrimaryKey(detailBankkonto.getKontoIIdGegenkonto());
		model().setGegenkontoDto(gegenkontoDto);
		model().setBuchungsart(detailBankkonto.getBuchungdetailartCNr());
		model().setAuszugnummer(detailBankkonto.getIAuszug());
		
		int indexUstBetrag = buchung.getBuchungdetailList().get(0).getNUst().signum() != 0 ? 0 : 
			buchung.getBuchungdetailList().get(1).getNUst().signum() == 0 ? 0 : 1;
		BuchungdetailDto detailGegenkonto = buchung.getBuchungdetailList().get(indexUstBetrag);
		model().setBetrag(detailGegenkonto.getNBetrag());
		model().setMwstBetrag(detailGegenkonto.getNUst());
		MwstsatzDto mwstSatzDto = DelegateFactory.getInstance().getMandantDelegate().getMwstSatzVonBruttoBetragUndUst(
				new Timestamp(buchung.getBuchungDto().getDBuchungsdatum().getTime()), model().getBetrag(), model().getMwstBetrag());
		model().setMwstsatzDto(mwstSatzDto);
		model().setKontotypGegenkonto(gegenkontoDto.getKontotypCNr());
		model().setAuszugnummerGegenkonto(detailBankkonto.getIAuszug().equals(detailGegenkonto.getIAuszug()) ?
				null : detailGegenkonto.getIAuszug());
		model().setGegenkontoBankkonto(model().getAuszugnummerGegenkonto() == null ? false : true);
	}

	private BuchungdetailDto getBuchungdetailBankkonto(List<Iso20022BuchungdetailDto> list) {
		if (bankkonto().getIId().equals(list.get(0).getKontoIId())) {
			return list.get(0);
		} else {
			return list.get(1);
		}
	}
	
	private FinanzBankbuchungModel model() {
		if (bankbuchungModel == null) {
			
			try {
				KontoDto bankkontoDto = DelegateFactory.getInstance().getFinanzDelegate()
						.kontoFindByPrimaryKey(tabbedPane.getController().getBankkontoIId());
				bankbuchungModel = new FinanzBankbuchungModel(bankkontoDto, tabbedPane.getController().getAuszugsnummer());
				KostenstelleDto defaultKostenstelleDto = tabbedPane.getInternalFrameFinanz().getDefaultKostenstelle();
				bankbuchungModel.setKostenstelleDto(defaultKostenstelleDto);
				bankbuchungModel.setWaehrung(tabbedPane.getController().getWaehrung());
			} catch (ExceptionLP e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bankbuchungModel;
	}
	
	public Integer getAuszugsnummer(Date zahldatum) {
		return Iso20022StandardEnum.SWISS.equals(tabbedPane.getController().getIso20022Standard())
				? getSwissAuszugsnummer(zahldatum)
				: tabbedPane.getController().getAuszugsnummer();
	}

	private Integer getSwissAuszugsnummer(Date zahldatum) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String swissAuszug = dateFormat.format(zahldatum);
		return Integer.parseInt(swissAuszug);
	}

	private void resetSepaBankbuchungModel() throws Throwable {
		model().setBuchungsdatum(tabbedPane.getController().getZahldatum());
		model().setBetrag(tabbedPane.getNochVerfuegbarerZahlbetrag2().getWert());
		WechselkursDto kursDto = DelegateFactory.getInstance().getLocaleDelegate().
				getKursZuDatum(model().getWaehrung(), 
						LPMain.getTheClient().getSMandantenwaehrung(), model().getBuchungsdatum());
		model().setKursDto(kursDto);
		model().setBuchungsart(tabbedPane.getNochVerfuegbarerZahlbetrag2().isSoll() ?
				BuchenFac.SollBuchung : BuchenFac.HabenBuchung);
		model().setGegenkontoDto(null);
		model().setText(null);
		model().setKontotypGegenkonto(FinanzServiceFac.KONTOTYP_SACHKONTO);
		model().setBelegnummer(null);
		model().setKommentar(null);
		model().setGegenkontoBankkonto(false);
		gegenkontoChanged();
	}
	
	private void updateViewData() throws Throwable {
		//notifyResetListener();
		notifyBankkontoListener();
		notifyBetragListener();
		notifyBuchungDataUpdateListener();
		notifyDatumListener();
		notifyGegenkontoListener();
		notifyKontotypGegenkontoListener();
		notifyKostenstelleListener();
		notifyKursListener();
		notifyWaehrungEnabledListener(false);
		notifyWaehrungListener();
		notifyBelegnummerListener();
		notifyTextListener();
		notifyBuchungsartListener();
	}

	@Override
	public InternalFrame getInternalFrame() {
		return tabbedPane.getInternalFrameFinanz();
	}

	@Override
	public Integer getAktuellesGeschaeftsjahr() throws Throwable {
		return tabbedPane.getInternalFrameFinanz().getIAktuellesGeschaeftsjahr();
	}

	@Override
	public void actionUpdateGegenkontoKontotyp(String kontotyp) throws Throwable {
		if (kontotyp != null && kontotyp.equals(model().getKontotypGegenkonto())) return;
		
		model().setKontotypGegenkonto(kontotyp);
		model().setGegenkontoDto(null);
		gegenkontoChanged();
//		notifyGegenkontoListener();
//		
//		checkAndSetMwstSatz(true);
	}

	private void checkAndSetMwstSatz(Boolean bSteuerfrei) throws Throwable {
		Boolean bSteuerzulaessig = false;
		
		if (bankkonto() != null && gegenkonto() != null) {
			String bankSkCnr = bankkonto().getSteuerkategorieCnr();
			String gegenSkCnr = gegenkonto().getSteuerkategorieCnr();
			
			bSteuerzulaessig = ((bankSkCnr == null) != (gegenSkCnr == null)) 
					|| (bankSkCnr != null && gegenSkCnr != null && bankSkCnr.equals(gegenSkCnr));
		}
		notifyMwstSatzEnabled(bSteuerzulaessig);
		
		if (!bSteuerzulaessig || bSteuerfrei) {
			MwstsatzbezDto mwstbezDto = DelegateFactory.getInstance().getMandantDelegate().getMwstsatzbezSteuerfrei();
			if (mwstbezDto != null && (model().getMwstsatzDto() == null 
					|| !model().getMwstsatzDto().getIIMwstsatzbezId().equals(mwstbezDto.getIId()))) {
				Timestamp ts = HelperTimestamp.cutOrToday(model().getBuchungsdatum());
				MwstsatzDto mwstsatzDto = DelegateFactory.mandant()
						.mwstsatzFindZuDatum(mwstbezDto.getIId(), ts);
/*				
				MwstsatzDto mwstsatzDto = DelegateFactory.getInstance().getMandantDelegate()
						.mwstsatzFindByMwstsatzbezIIdAktuellster(mwstbezDto.getIId());
*/						
				model().setMwstsatzDto(mwstsatzDto);
				notifyMwstSatzListener();
			}
		}
//		notifyMwstListener();
	}

	@Override
	public void actionChooseGegenkonto() throws Throwable {
		panelQueryFLRGegenkonto = FinanzFilterFactory.getInstance().createPanelFLRKonten(
				getInternalFrame(), model().getKontotypGegenkonto());
		
		new DialogQuery(panelQueryFLRGegenkonto);
	}

	@Override
	public void actionUpdateGegenkonto(String cNr) {
		if (cNr == null) return;
		
		try {
			KontoDto gegenkontoDto = DelegateFactory.getInstance()
					.getFinanzDelegate().kontoFindByCnrKontotypMandantOhneExc(
							cNr, model().getKontotypGegenkonto(), LPMain.getTheClient().getMandant());
		
			if (gegenkontoDto == null) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getMessageTextRespectUISPr("fb.error.kontomitnummernichtgefunden",
								model().getKontotypGegenkonto(), cNr));
			} else if (gegenkontoDto.getIId().equals(bankkonto().getIId())) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("finanz.error.verschiedenekonten"));
				gegenkontoDto = null;
			}
			
			model().setGegenkontoDto(gegenkontoDto);
			gegenkontoChanged();
		} catch (ExceptionLP e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionChooseKostenstelle() throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, false);
		if (model().getKostenstelleDto() != null) {
			panelQueryFLRKostenstelle.setSelectedId(model().getKostenstelleDto().getIId());
		}
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	@Override
	public void actionUpdateBetrag(BigDecimal betrag) {
		// TODO check betrag mit noch verfügbaren
		
		model().setBetrag(betrag);
		calcAndSetMwst();
		notifyKursListener();
	}

	private void calcAndSetMwst() {
		MwstsatzDto mwstsatzDto = model().getMwstsatzDto();
		BigDecimal mwstBetrag = null;
		if (mwstsatzDto != null) {
			Double dMwstSatz = mwstsatzDto.getFMwstsatz();
			mwstBetrag = Helper.getMehrwertsteuerBetrag(model().getBetrag(), dMwstSatz);
		}
		model().setMwstBetrag(mwstBetrag);
		notifyMwstListener();
	}

	@Override
	public void actionUpdateMwstSatz(Integer iId) throws Throwable {
		Timestamp ts = HelperTimestamp.cutOrToday(model().getBuchungsdatum());
		model().setMwstsatzDto(DelegateFactory.mandant()
				.mwstsatzFindZuDatum(iId,ts));
/*				
		if (model().getBuchungsdatum() != null) {
			model().setMwstsatzDto(DelegateFactory.getInstance()
					.getMandantDelegate().mwstsatzFindZuDatum(iId, new Timestamp(model().getBuchungsdatum().getTime())));
		} else {
			model().setMwstsatzDto(DelegateFactory.getInstance()
					.getMandantDelegate().mwstsatzFindByMwstsatzbezIIdAktuellster(iId));
		}
*/		
		calcAndSetMwst();
	}

	@Override
	public void actionUpdateBelegnummerHandeingabe(String belegnummer) {
		model().setBelegnummerModi(FinanzBankbuchungModel.BELEGNUMMER_HAND);
		model().setBelegnummer(belegnummer);
	}

	@Override
	public void actionUpdateAutomatischeBelegnummer() {
		model().setBelegnummerModi(FinanzBankbuchungModel.BELEGNUMMER_AUTO);
		model().setBelegnummer(null);
	}

	@Override
	public void actionUpdateBuchungsart(String buchungsart) {
		model().setBuchungsart(buchungsart);
		notifyBuchungsartListener();
	}

	@Override
	public void actionUpdateText(String text) {
		model().setText(text);
	}

	@Override
	public void actionUpdateKommentar(String kommentar) {
		model().setKommentar(kommentar);
	}

	@Override
	public void changed(EventObject eI) {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		try {
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL ) {
				if (panelQueryFLRGegenkonto == e.getSource()) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();
					KontoDto gegenkontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKey((Integer) key);
					model().setGegenkontoDto(gegenkontoDto);
					gegenkontoChanged();
				} else if (panelQueryFLRKostenstelle == e.getSource()) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();
					KostenstelleDto kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate().kostenstelleFindByPrimaryKey((Integer) key);
					model().setKostenstelleDto(kostenstelleDto);
					
					notifyKostenstelleListener();
				}
			}
		} catch (Throwable t) {
			//TODO do something
		}
	}
	
	private void gegenkontoChanged() throws Throwable {
		if (gegenkonto() != null && 
				DelegateFactory.getInstance().getFinanzDelegate()
					.bankverbindungFindByKontoIIdOhneExc(gegenkonto().getIId()) != null) {
			model().setGegenkontoBankkonto(true);
		} else {
			model().setGegenkontoBankkonto(false);
		}
		
		notifyGegenkontoListener();
		checkAndSetMwstSatz(false);
		notifyMwstListener();
	}

	@Override
	public void registerGegenkontoListener(IGegenkontoListener listener) {
		gegenkontoListener.add(listener);
	}

	@Override
	public void registerKostenstelleListener(IKostenstelleListener listener) {
		kostenstelleListener.add(listener);
	}

	@Override
	public void registerBuchungsbetragListener(IBetragListener listener) {
		buchungsbetragListener.add(listener);
	}

	@Override
	public void registerMwstListener(IMwstListener listener) {
		mwstListener.add(listener);
	}

	@Override
	public void registerDatumListener(IDatumListener listener) {
		datumListener.add(listener);
	}

	@Override
	public void registerBankkontoListener(IBankkontoListener listener) {
		bankkontoListener.add(listener);
	}

	@Override
	public void registerResetListener(IResetListener listener) {
		resetListener.add(listener);
	}

	@Override
	public void registerWaehrungListener(IWaehrungListener listener) {
		waehrungListener.add(listener);
	}
	
	public void registerBuchungDataUpdateListener(IDataUpdateListener listener) {
		buchungDataUpdateListener.add(listener);
	}
	
	@Override
	public void registerInfoListener(IInfoListener listener) {
		infoListener.add(listener);
	}

	@Override
	public void registerTextListener(ITextListener listener) {
		textListener.add(listener);
	}

	@Override
	public void registerBelegnummerListener(IBelegnummerListener listener) {
		belegnummerListener.add(listener);
	}

	@Override
	public void registerBuchungsartListener(IBuchungsartListener listener) {
		buchungsartListener.add(listener);
	}

	private void notifyGegenkontoListener() {
		for (IGegenkontoListener listener : gegenkontoListener) {
			listener.updateGegenkonto(gegenkonto());
			listener.updateAuszugnummerGegenkonto(model().getAuszugnummerGegenkonto());
			listener.enableAuszugnummerGegenkonto(model().isGegenkontoBankkonto());
		}
	}
	
	private void notifyKontotypGegenkontoListener() {
		for (IGegenkontoListener listener : gegenkontoListener) {
			listener.updateKontotypGegenkonto(model().getKontotypGegenkonto());
		}
	}
	
	private void notifyKostenstelleListener() {
		for (IKostenstelleListener listener : kostenstelleListener) {
			listener.updateKostenstelle(model().getKostenstelleDto());
		}
	}
	
	private void notifyBetragListener() {
		for (IBetragListener listener : buchungsbetragListener) {
			listener.updateBuchungsbetrag(model().getBetrag());
		}
	}
	
	private void notifyKursListener() {
		for (IBetragListener listener : buchungsbetragListener) {
			listener.updateKurs(model().getKursDto());
		}
	}
	
	private void notifyMwstListener() {
		for (IMwstListener listener : mwstListener) {
			listener.updateMwstBetrag(model().getMwstBetrag());
		}
	}
	
	private void notifyMwstSatzListener() {
		for (IMwstListener listener : mwstListener) {
			listener.updateMwstSatz(model().getMwstsatzDto().getIIMwstsatzbezId());
		}
	}

	private void notifyMwstSatzEnabled(Boolean value) {
		for (IMwstListener listener : mwstListener) {
			listener.enableMwstSatz(value);
		}
	}
	
	private void notifyDatumListener() {
		for (IDatumListener listener : datumListener) {
			listener.updateDatum(model().getBuchungsdatum(), false);
		}
	}
	
	private void notifyBankkontoListener() {
		for (IBankkontoListener listener : bankkontoListener) {
			listener.updateBankkonto(bankkonto());
			listener.updateAuszugnummer(model().getAuszugnummer());
			listener.enableAuszugnummer(false);
		}
	}
	
	private void notifyResetListener() {
		for (IResetListener listener : resetListener) {
			listener.reset();
		}
	}
	
	private void notifyWaehrungListener() {
		for (IWaehrungListener listener : waehrungListener) {
			listener.updateWaehrung(model().getWaehrung());
		}
	}
	
	private void notifyWaehrungEnabledListener(Boolean value) {
		for (IWaehrungListener listener : waehrungListener) {
			listener.enableWaehrung(value);
		}
	}
	
	private void notifyBuchungDataUpdateListener() {
		for (IDataUpdateListener listener : buchungDataUpdateListener) {
			listener.dataUpdated();
		}
	}
	
	private void notifyInfoListener(String text) {
		for (IInfoListener listener : infoListener) {
			listener.updateInfo(text);
		}
	}
	
	private void notifyTextListener() {
		for (ITextListener listener : textListener) {
			listener.updateText(model().getText());
		}
	}
	
	private void notifyBelegnummerListener() {
		for (IBelegnummerListener listener : belegnummerListener) {
			if (FinanzBankbuchungModel.BELEGNUMMER_HAND == model().getBelegnummerModi()) {
				listener.updateBelegnummerHand(model().getBelegnummer());
			} else {
				listener.updateBelegnummerAuto();
			}
		}
	}
	
	private void notifyBuchungsartListener() {
		for (IBuchungsartListener listener : buchungsartListener) {
			listener.updateBuchungsartBankkonto(model().getBuchungsart());
			listener.updateBuchungsartGegenkonto(BuchenFac.SollBuchung.equals(
					model().getBuchungsart()) ? BuchenFac.HabenBuchung : BuchenFac.SollBuchung ); 
		}
	}

	private BuchungKompakt updateBuchung(BuchungKompakt buchung) throws Throwable {
		createBuchungImpl(buchung);
		return buchung;
	}
	
	private BuchungKompakt createBuchung() throws Throwable {
		BuchungKompakt buchung = new BuchungKompakt();
		createBuchungImpl(buchung);
		return buchung;
	}
	
	
	private void createBuchungImpl(BuchungKompakt buchung) throws Throwable {
		BuchungDto buchungDto = new BuchungDto();
		buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BANKBUCHUNG);
		buchungDto.setCBelegnummer(model().getBelegnummer());
		buchungDto.setCText(model().getText());
		buchungDto.setDBuchungsdatum(model().getBuchungsdatum());
		buchungDto.setKostenstelleIId(model().getKostenstelleDto().getIId());
		buchungDto.setIGeschaeftsjahr(getAktuellesGeschaeftsjahr());
		
		Integer auszugsnr = model().getAuszugnummer();
		Iso20022BuchungdetailDto detailBankkonto = new Iso20022BuchungdetailDto();
		detailBankkonto.setCKommentar(model().getKommentar());
		detailBankkonto.setKontoIId(bankkonto().getIId());
		detailBankkonto.setKontoIIdGegenkonto(gegenkonto().getIId());
		detailBankkonto.setBuchungdetailartCNr(model().getBuchungsart());
		detailBankkonto.setIAuszug(auszugsnr);
		
		Iso20022BuchungdetailDto detailGegenkonto = new Iso20022BuchungdetailDto();
		detailGegenkonto.setKontoIId(gegenkonto().getIId());
		detailGegenkonto.setKontoIIdGegenkonto(bankkonto().getIId());
		detailGegenkonto.setBuchungdetailartCNr(BuchenFac.HabenBuchung.equals(detailBankkonto.getBuchungdetailartCNr()) ? 
				BuchenFac.SollBuchung : BuchenFac.HabenBuchung);
		detailGegenkonto.setCKommentar(model().getKommentar());
		detailGegenkonto.setIAuszug(model().getAuszugnummerGegenkonto() != null ? 
				model().getAuszugnummerGegenkonto() : auszugsnr);
		
		List<Iso20022BuchungdetailDto> details = new ArrayList<Iso20022BuchungdetailDto>();
		if (BuchenFac.SollBuchung.equals(detailBankkonto.getBuchungdetailartCNr())) {
			details.add(detailBankkonto);
			details.add(detailGegenkonto);
		} else {
			details.add(detailGegenkonto);
			details.add(detailBankkonto);
		}

		boolean bIsSteuerSoll = isSteuerSoll();
		if (bIsSteuerSoll) {
			setBetraege(details.get(0), details.get(1));
		} else {
			setBetraege(details.get(1), details.get(0));
		}
		
		Integer ustKontoIId = getUstKontoIId();
		if (ustKontoIId != null) {
			Iso20022BuchungdetailDto detailMwst = new Iso20022BuchungdetailDto();
			detailMwst.setKontoIId(ustKontoIId);
			detailMwst.setKontoIIdGegenkonto(details.get(0).getKontoIId());
			detailMwst.setBuchungdetailartCNr(bIsSteuerSoll ? BuchenFac.SollBuchung : BuchenFac.HabenBuchung);
			detailMwst.setCKommentar(model().getKommentar());
			detailMwst.setIAuszug(auszugsnr);
			detailMwst.setNBetrag(model().getBetragMwstMandantenWaehrung());
			detailMwst.setNUst(BigDecimal.ZERO);
			detailMwst.setNBetragKontowaehrung(model().getMwstBetrag());
			details.add(detailMwst);
		}
		
		buchung.setBuchungDto(buchungDto);
		buchung.setBuchungdetailList(details);
	}
	
	private boolean isSteuerSoll() {
		KontoDto sollKonto = BuchenFac.SollBuchung.equals(model().getBuchungsart()) ? 
				bankkonto() : gegenkonto();
		KontoDto habenKonto = BuchenFac.SollBuchung.equals(model().getBuchungsart()) ? 
				gegenkonto() : bankkonto();
		KontoDto besteuertesKonto;
		boolean bSteuerSoll = false;
		
		if (sollKonto.getSteuerkategorieCnr() != null) {
			bSteuerSoll = false;
			besteuertesKonto = sollKonto;
		} else if (habenKonto.getSteuerkategorieCnr() != null) {
			bSteuerSoll = true;
			besteuertesKonto = habenKonto;
		} else {
			return bSteuerSoll;
		}
		
		if (FinanzServiceFac.KONTOTYP_SACHKONTO.equals(besteuertesKonto.getKontotypCNr()) &&
				(FinanzServiceFac.STEUERART_UST.equals(besteuertesKonto.getcSteuerart()) ||
						FinanzServiceFac.STEUERART_VST.equals(besteuertesKonto.getcSteuerart()))) {
			bSteuerSoll = !bSteuerSoll;
		}
		
		return bSteuerSoll;
	}

	private KontoDto gegenkonto() {
		return model().getGegenkontoDto();
	}

	private KontoDto bankkonto() {
		return model().getBankkontoDto();
	}

	private void setBetraege(Iso20022BuchungdetailDto detailBesteuert, Iso20022BuchungdetailDto detailNichtbesteuert) throws Throwable {
		detailBesteuert.setNBetrag(model().getBetragNettoMandantenWaehrung());
		detailBesteuert.setNBetragKontowaehrung(model().getBetragNetto());
		detailBesteuert.setNUst(BigDecimal.ZERO);
		
		detailNichtbesteuert.setNBetrag(model().getBetragBruttoMandantenWaehrung());
		detailNichtbesteuert.setNBetragKontowaehrung(model().getBetrag());
		detailNichtbesteuert.setNUst(model().getBetragMwstMandantenWaehrung());
	}
	
	private Integer getUstKontoIId() throws ExceptionLP, Throwable {
		if (!model().hasMwstBetrag()
				|| gegenkonto().getSteuerkategorieCnr() == null) {
			return null;
		}
		
		if (FinanzServiceFac.KONTOTYP_DEBITOR.equals(gegenkonto().getKontotypCNr())
				|| FinanzServiceFac.STEUERART_UST.equals(gegenkonto().getcSteuerart())) {
			return DelegateFactory.getInstance().getFinanzServiceDelegate()
							.getUstKontoFuerSteuerkategorie(
									gegenkonto().getSteuerkategorieCnr(),
									gegenkonto().getFinanzamtIId(),
									model().getMwstsatzDto().getIIMwstsatzbezId(),
									Helper.asTimestamp(model().getBuchungsdatum()));
		} else {
			return DelegateFactory.getInstance().getFinanzServiceDelegate()
						.getVstKontoFuerSteuerkategorie(
								gegenkonto().getSteuerkategorieCnr(),
								gegenkonto().getFinanzamtIId(),
								model().getMwstsatzDto().getIIMwstsatzbezId(),
								Helper.asTimestamp(model().getBuchungsdatum()));
		}
	}

	@Override
	public void actionSave() throws Throwable {
		if (!checkBuchung()) return;
		
		if (buchungToEdit != null) {
			updateBuchung(buchungToEdit);
			tabbedPane.actionBuchungEdited();
			buchungToEdit = null;
			showInfoText(LPMain.getTextRespectUISPr("fb.sepa.import.buchungeditiert"), 2000);
		} else {
			BuchungKompakt buchung = createBuchung();
			tabbedPane.getController().getResultWaitingForManuelleAuswahl().getManualBookings().add(buchung);
			showInfoText(LPMain.getTextRespectUISPr("fb.sepa.import.buchunggespeichert"), 2000);
		}
		tabbedPane.updateNochVerfuegbarerZahlbetrag();
		tabbedPane.updateButtonUebernehmen();
		notifyBuchungDataUpdateListener();
		actionYouAreSelected();
//		getBankbuchungModel().setDefaults();
//		updateViewData();
//		checkAndSetMwstSatz(false);
		
	}

	private boolean checkBuchung() {
		if (BigDecimal.ZERO.compareTo(model().getBetrag()) != -1) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("fb.sepa.import.error.betrag"));
			return false;
		} 		
		
		SepaBetrag sepaVerfuegbar = tabbedPane.getNochVerfuegbarerZahlbetrag2();
		if (buchungToEdit != null) {
			BuchungdetailDto detailBankkonto = getBuchungdetailBankkonto(buchungToEdit.getBuchungdetailList());
			sepaVerfuegbar = sepaVerfuegbar.add(BuchenFac.SollBuchung.equals(detailBankkonto.getBuchungdetailartCNr()) ?
					new SepaSollBetrag(detailBankkonto.getNBetrag()) : new SepaHabenBetrag(detailBankkonto.getNBetrag()));
		}
		SepaBetrag sepaZahlbetrag;
		if (BuchenFac.SollBuchung.equals(model().getBuchungsart())) {
			sepaZahlbetrag = new SepaSollBetrag(model().getBetrag());
		} else {
			sepaZahlbetrag = new SepaHabenBetrag(model().getBetrag());
		}
		SepaBetrag newSepaVerfuegbar = sepaVerfuegbar.subtract(sepaZahlbetrag);
		if (BigDecimal.ZERO.compareTo(newSepaVerfuegbar.getWert()) != 0 
				&& sepaVerfuegbar.getPlusMinusWert().signum() != newSepaVerfuegbar.getPlusMinusWert().signum()) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getMessageTextRespectUISPr("fb.sepa.import.error.manuellebuchung.betragzugross",
					Helper.formatZahl(sepaZahlbetrag.getWert(), 2, Defaults.getInstance().getLocUI()),
					Helper.formatZahl(sepaVerfuegbar.getWert(), 2, Defaults.getInstance().getLocUI())));
			model().setBetrag(sepaVerfuegbar.getWert());
			notifyBetragListener();
			return false;
		}

		return true;
	}

	private void showInfoText(String text, final long milliseconds) {
		notifyInfoListener(text);
		
		SwingWorker<Void, Integer> infoWorker = new SwingWorker<Void, Integer>() {

			@Override
			protected Void doInBackground() throws Exception {
				Thread.sleep(milliseconds);
				notifyInfoListener(null);
				return null;
			}
			
		};
		infoWorker.execute();
	}

	@Override
	public void actionUpdateAuszugnummerGegenkonto(Integer auszugnummer) {
		model().setAuszugnummerGegenkonto(auszugnummer);
	}

	@Override
	public void actionReset() throws Throwable {
		actionYouAreSelectedWithRefresh();
	}
}
