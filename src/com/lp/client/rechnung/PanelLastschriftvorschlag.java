package com.lp.client.rechnung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.finanz.TabbedPaneMahnwesen;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.MahnlaufDto;
import com.lp.server.rechnung.service.LastschriftvorschlagDto;
import com.lp.server.rechnung.service.LastschriftvorschlagKomplettDto;
import com.lp.util.GotoHelper;

public class PanelLastschriftvorschlag extends PanelBasis {
	private static final long serialVersionUID = 4247543336194752548L;

	private TabbedPaneMahnwesen tpMahnwesen;
	private LastschriftvorschlagKomplettDto lastschriftvorschlagDto;
	
	private JPanel jPanelWorkingOn;
	
	private WrapperLabel wlaRechnung;
	private WrapperGotoButton wbuGotoRechnung;
	private WrapperTextField wtfRechnung;
	
	private WrapperLabel wlaKunde;
	private WrapperGotoButton wbuGotoKunde;
	private WrapperTextField wtfKunde;
	private WrapperTextField wtfKundeAdresse;
	
	private WrapperLabel wlaFaelligAm;
	private WrapperDateField wdfFaelligAm;
	private WrapperLabel wlaRechnungdatum;
	private WrapperDateField wdfRechnungdatum;
	
	private WrapperKeyValueField wkvBank;
	private WrapperKeyValueField wkvIban;
	private WrapperKeyValueField wkvBic;
	
	private WrapperLabel wlaReBrutto;
	private WrapperNumberField wnfReBrutto;
	private WrapperLabel wlaReBruttoOffen;
	private WrapperNumberField wnfReBruttoOffen;
	private WrapperLabel wlaLastschriftbetrag;
	private WrapperNumberField wnfLastschriftbetrag;
	
	private WrapperLabel wlaExportiertAm;
	private WrapperDateField wdfExportiertAm;
	private WrapperLabel wlaGesamtwert;
	private WrapperNumberField wnfGesamtwert;
	
	private WrapperLabel wlaWaehrungBrutto;
	private WrapperLabel wlaWaehrungBruttoOffen;
	private WrapperLabel wlaWaehrungLastschrift;
	private WrapperLabel wlaWaehrungGesamtwert;
	
	private WrapperLabel wlaVerwendungszweck;
	private WrapperTextField wtfVerwendungszweck;
	
	public PanelLastschriftvorschlag(InternalFrame internalFrameI,
			String addTitleI, Object keyWhenDetailPanelI, TabbedPaneMahnwesen tbMahnwesen) throws Throwable {
		super(internalFrameI, addTitleI, keyWhenDetailPanelI);
		setTabbedPaneMahnwesen(tbMahnwesen);
		jbInit();
	}

	public void setTabbedPaneMahnwesen(TabbedPaneMahnwesen tpMahnwesen) {
		this.tpMahnwesen = tpMahnwesen;
	}
	
	public TabbedPaneMahnwesen getTabbedPaneMahnwesen() {
		return tpMahnwesen;
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_FINANZ_MAHNLAUF;
	}
	
	public LastschriftvorschlagKomplettDto getLastschriftvorschlagDto() {
		return lastschriftvorschlagDto;
	}
	
	public void setLastschriftvorschlagDto(
			LastschriftvorschlagKomplettDto lastschriftvorschlagDto) {
		this.lastschriftvorschlagDto = lastschriftvorschlagDto;
	}
	
	private void jbInit() throws Throwable {
		wlaRechnung = new WrapperLabel(LPMain.getTextRespectUISPr("rech.rechnung"));
		wbuGotoRechnung = new WrapperGotoButton(GotoHelper.GOTO_RECHNUNG_AUSWAHL);
		wbuGotoRechnung.getWrapperButton().setVisible(false);
		wtfRechnung = new WrapperTextField();
		wtfRechnung.setActivatable(false);
		
		wlaKunde = new WrapperLabel(LPMain.getTextRespectUISPr("label.kunde"));
		wtfKunde = new WrapperTextField();
		wtfKunde.setColumns(100);
		wtfKunde.setActivatable(false);
		wbuGotoKunde = new WrapperGotoButton(GotoHelper.GOTO_KUNDE_AUSWAHL);
		wbuGotoKunde.getWrapperButton().setVisible(false);
		wtfKundeAdresse = new WrapperTextField();
		wtfKundeAdresse.setColumnsMax(100);
		wtfKundeAdresse.setActivatable(false);
		
		wlaFaelligAm = new WrapperLabel(LPMain.getTextRespectUISPr("label.faelligam"));
		wdfFaelligAm = new WrapperDateField();
		wdfFaelligAm.setActivatable(false);
		wlaRechnungdatum = new WrapperLabel(LPMain.getTextRespectUISPr("fb.mahnung.redatum"));
		wdfRechnungdatum = new WrapperDateField();
		wdfRechnungdatum.setActivatable(false);
		
		wkvBank = new WrapperKeyValueField(92);
		wkvBank.setKey(LPMain.getTextRespectUISPr("part.kund.banken"));
		wkvIban = new WrapperKeyValueField(92);
		wkvIban.setKey(LPMain.getTextRespectUISPr("lp.iban"));
		wkvBic = new WrapperKeyValueField(92);
		wkvBic.setKey(LPMain.getTextRespectUISPr("lp.bic"));
		
		wlaReBrutto = new WrapperLabel(LPMain.getTextRespectUISPr("label.bruttobetrag"));
		wnfReBrutto = new WrapperNumberField();
		wnfReBrutto.setActivatable(false);
		wlaReBruttoOffen = new WrapperLabel(LPMain.getTextRespectUISPr("rech.mahnung.bruttooffen"));
		wnfReBruttoOffen = new WrapperNumberField();
		wnfReBruttoOffen.setActivatable(false);
		wlaLastschriftbetrag = new WrapperLabel(LPMain.getTextRespectUISPr(
				"rechnung.lastschriftvorschlag.lastschriftbetrag"));
		wnfLastschriftbetrag = new WrapperNumberField();
		wnfLastschriftbetrag.setMandatoryFieldDB(true);
		
		wlaExportiertAm = new WrapperLabel();
		wlaExportiertAm.setVisible(false);
		wdfExportiertAm = new WrapperDateField();
		wdfExportiertAm.setActivatable(false);
		wdfExportiertAm.setVisible(false);
		wlaGesamtwert = new WrapperLabel(LPMain.getTextRespectUISPr("lp.gesamtwert"));
		wnfGesamtwert = new WrapperNumberField();
		wnfGesamtwert.setActivatable(false);
		
		wlaWaehrungBrutto = new WrapperLabel();
		wlaWaehrungBrutto.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungBruttoOffen = new WrapperLabel();
		wlaWaehrungBruttoOffen.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungLastschrift = new WrapperLabel();
		wlaWaehrungLastschrift.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungGesamtwert = new WrapperLabel(LPMain.getTheClient().getSMandantenwaehrung());
		wlaWaehrungGesamtwert.setHorizontalAlignment(SwingConstants.LEFT);
		
		wlaVerwendungszweck = new WrapperLabel(LPMain.getTextRespectUISPr("rechnung.lastschriftvorschlag.verwendungszweck"));
		wtfVerwendungszweck = new WrapperTextField(140);
		wtfVerwendungszweck.setActivatable(true);
		
		jPanelWorkingOn = new JPanel();
		initLayout();
	}

	private void initLayout() throws Throwable {
		JPanel kundePanel = new JPanel(new MigLayout("insets 0, align right", "[fill,130:130:130|fill]"));
		kundePanel.add(wlaKunde);
		kundePanel.add(wbuGotoKunde);
				
		JPanel rePanel = new JPanel(new MigLayout("insets 0 0 15 0, align right", "[fill,130:130:130|fill]"));
		rePanel.add(wlaRechnung);
		rePanel.add(wbuGotoRechnung);
		
		JPanel erstelltGespeichertPanel = new JPanel();
		HvLayout erstelltGespeichertLayout = HvLayoutFactory.create(erstelltGespeichertPanel,
				"ins 0", "[fill,200|fill,100|fill,100|fill,grow|fill,200|fill,30:30:30]", "");
		erstelltGespeichertLayout
			.add(wlaExportiertAm).add(wdfExportiertAm)
			.add(wlaGesamtwert, "skip 1").add(wnfGesamtwert).add(wlaWaehrungGesamtwert);
		
		HvLayout layout = HvLayoutFactory.create(jPanelWorkingOn, 
				"wrap 6", "[fill,15%|fill,40%|fill,140:140:140|fill,30%|fill,30:30:30]", "");
		layout.add(rePanel).add(wtfRechnung, "gapbottom 15, wrap")
			.add(kundePanel).add(wtfKunde)
			.add(wkvBank).spanAndWrap(2)
			.add(wtfKundeAdresse, "skip")
			.add(wkvIban).spanAndWrap(2)
			
			.add(wkvBic, "skip 2, span 2, wrap")
			
			.add(wlaRechnungdatum).add(wdfRechnungdatum).wrap()
			.add(wlaFaelligAm).add(wdfFaelligAm).add(wlaReBrutto).add(wnfReBrutto).add(wlaWaehrungBrutto).wrap()
			.add(wlaReBruttoOffen).skip(2).add(wnfReBruttoOffen).add(wlaWaehrungBruttoOffen).wrap()
			.add(wlaVerwendungszweck).add(wtfVerwendungszweck)
				.add(wlaLastschriftbetrag).add(wnfLastschriftbetrag).add(wlaWaehrungLastschrift).wrap()			
			.add(erstelltGespeichertPanel, "span, pushy, aligny bottom");
		
		this.setLayout(new GridBagLayout());
		// Actionpanel von Oberklasse holen und anhaengen.
		getInternalFrame().addItemChangedListener(this);
		this.add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);
	}
	
	private void holeLastschriftvorschlagDto(Integer iId) throws Throwable {
		if (iId == null) return;
		
		setLastschriftvorschlagDto(DelegateFactory.getInstance()
				.getMahnwesenDelegate().lastschriftvorschlagKomplettFindByPrimaryKey(iId));
	}
	
	private void dto2Components() throws Throwable {
		if (getLastschriftvorschlagDto() == null) return;
		
		dto2ComponentsImpl();
	}

	private void dto2ComponentsImpl() throws Throwable {
		if (getLastschriftvorschlagDto() == null) return;
		
		wbuGotoRechnung.setOKey(getLastschriftvorschlagDto().getRechnungDto().getIId());
		wtfRechnung.setText(getLastschriftvorschlagDto().getRechnungDto().getCNr());
		
		wbuGotoKunde.setOKey(getLastschriftvorschlagDto().getKundeDto().getIId());
		wtfKunde.setText(getLastschriftvorschlagDto().getKundeDto()
				.getPartnerDto().formatFixTitelName1Name2());
		wtfKundeAdresse.setText(getLastschriftvorschlagDto().getKundeDto()
				.getPartnerDto().formatAdresse());
		
		wdfRechnungdatum.setDate(getLastschriftvorschlagDto().getRechnungDto().getTBelegdatum());
		wdfFaelligAm.setDate(DelegateFactory.getInstance().getMandantDelegate()
				.berechneZielDatumFuerBelegdatum(
						new Date(getLastschriftvorschlagDto().getRechnungDto().getTBelegdatum().getTime()), 
						getLastschriftvorschlagDto().getRechnungDto().getZahlungszielIId()));
		wnfReBrutto.setBigDecimal(getLastschriftvorschlagDto().getNRechnungsbetrag());
		wnfReBruttoOffen.setBigDecimal(getLastschriftvorschlagDto().getNRechnungsbetrag()
				.subtract(getLastschriftvorschlagDto().getNBereitsBezahlt()));
		wnfLastschriftbetrag.setBigDecimal(getLastschriftvorschlagDto().getNZahlbetrag());
		
		wlaWaehrungBrutto.setText(getLastschriftvorschlagDto().getRechnungDto().getWaehrungCNr());
		wlaWaehrungBruttoOffen.setText(getLastschriftvorschlagDto().getRechnungDto().getWaehrungCNr());
		wlaWaehrungLastschrift.setText(getLastschriftvorschlagDto().getRechnungDto().getWaehrungCNr());
		wlaWaehrungGesamtwert.setText(getLastschriftvorschlagDto().getRechnungDto().getWaehrungCNr());
		
		wtfVerwendungszweck.setText(getLastschriftvorschlagDto().getCVerwendungszweck());
		
		if (getLastschriftvorschlagDto().getPartnerbankDto() != null) {
			wkvIban.setValue(getLastschriftvorschlagDto().getPartnerbankDto().getCIban());
			wkvBank.setValue(getLastschriftvorschlagDto().getBankDto()
					.getPartnerDto().getCName1nachnamefirmazeile1());
			wkvBic.setValue(getLastschriftvorschlagDto().getBankDto().getCBic());
		}
		
		if (getLastschriftvorschlagDto().getTGespeichert() != null) {
			wdfExportiertAm.setVisible(true);
			wdfExportiertAm.setDate(getLastschriftvorschlagDto().getTGespeichert());
			String who = getLastschriftvorschlagDto().getPersonalIIdGespeichert() != null ?
					Defaults.getInstance().getPersonalKurzzeichen(
					getLastschriftvorschlagDto().getPersonalIIdGespeichert()) : "null";
			wlaExportiertAm.setText(LPMain.getMessageTextRespectUISPr(
				"rechnung.lastschriftvorschlag.exportiertvonam", who));
			wlaExportiertAm.setVisible(true);
		} else {
			wlaExportiertAm.setVisible(false);
			wdfExportiertAm.setVisible(false);
		}
		this.setStatusbarPersonalIIdAnlegen(getMahnlaufDto().getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(getMahnlaufDto().getTAnlegen());
		this.setStatusbarPersonalIIdAendern(getLastschriftvorschlagDto().getPersonalIIdAendern());
		this.setStatusbarTAendern(getLastschriftvorschlagDto().getTAendern());
	}
	
	private void components2Dto() throws ExceptionLP {
		getLastschriftvorschlagDto().setNZahlbetrag(wnfLastschriftbetrag.getBigDecimal());
		getLastschriftvorschlagDto().setCVerwendungszweck(wtfVerwendungszweck.getText());
	}
	
	private MahnlaufDto getMahnlaufDto() {
		return getTabbedPaneMahnwesen().getMahnlaufDto();
	}
	
	public void updateGesamtwert() throws Throwable {
		if (getMahnlaufDto() == null) return;
		
		wnfGesamtwert.setBigDecimal(DelegateFactory.getInstance().getMahnwesenDelegate()
				.getGesamtwertEinesLastschriftvorschlaglaufs(getMahnlaufDto().getIId()));
	}
	
	@Override
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (!allMandatoryFieldsSetDlg() || getLastschriftvorschlagDto() == null) return;
		
		components2Dto();
		LastschriftvorschlagDto lvDto = DelegateFactory.getInstance().getMahnwesenDelegate()
			.updateLastschriftvorschlag(getLastschriftvorschlagDto());
		setKeyWhenDetailPanel(lvDto.getIId());
		holeLastschriftvorschlagDto(lvDto.getIId());
		updateGesamtwert();
		super.eventActionSave(e, false);
		eventYouAreSelected(false);
	}
	
	@Override
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(bNeedNoYouAreSelectedI);
		Integer key = (Integer) getKeyWhenDetailPanel();
		if (key == null) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			holeLastschriftvorschlagDto(key);
			dto2Components();
		}
	}
	
	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfLastschriftbetrag;
	};
	
	@Override
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		Integer selectedId = (Integer)getTabbedPaneMahnwesen()
				.getPanelQueryLastschriftvorschlag().getSelectedId();
		
		if (isLockedDlg()) return;
		
		DelegateFactory.getInstance().getMahnwesenDelegate().removeLastschriftvorschlag(selectedId);
		setLastschriftvorschlagDto(null);
		leereAlleFelder(this);
		super.eventActionDelete(e, bAdministrateLockKeyI, bNeedNoDeleteI);
	}
}
