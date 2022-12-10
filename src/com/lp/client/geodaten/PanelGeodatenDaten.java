package com.lp.client.geodaten;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.EnumSet;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.IHvValueHolderListener;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.GeodatenDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.util.HvOptional;
import com.lp.service.google.geocoding.GeodatenFinder;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

public class PanelGeodatenDaten extends PanelBasis implements IGeodatenFilterListener {
	private static final long serialVersionUID = -546425044921835396L;

	private class Action {
		public static final String AUSWAHL_ADRESSE = "action_special_AUSWAHL_ADRESSE";
		public static final String DIALOG_KUNDE = "action_special_DIALOG_KUNDE";
		public static final String DIALOG_LIEFERANT = "action_special_DIALOG_LIEFERANT";
		public static final String DIALOG_PARTNER = "action_special_DIALOG_PARTNER";
		public static final String LADE_KARTE = "action_special_LADE_KARTE";
		public static final String LADE_GEODATEN = "action_special_LADE_GEODATEN";
	}

	private WrapperLabel wlaAuswahlAdresse;
	private WrapperComboBox wcbAuswahlAdresse;
	private JPanel panelAuswahlAdresse;
	private JPanel panelAuswahlTyp;
	private JPanel panelTop;
	private JPanel panelBottom;
	private JPanel panelFilterSerienbrief;
	
	private JPanel panelBottomButtons;
	private WrapperButton wrbKarteLaden;
	private WrapperButton wrbGeodatenLaden;
	
	private PanelGotoKundeAuswahl panelKundeAuswahl;
	private PanelGotoLieferantAuswahl panelLieferantAuswahl;
	private PanelGotoPartnerAuswahl panelPartnerAuswahl;
	private PanelManuelleAuswahl panelManuelleAuswahl;
	private IMapPositionHolder selectedAuswahlPanel;
	
	private IMapDataController mapDataController;
	private IMapDataFilter currentFilterController;
	private GeodatenFinder geodatenFinder;
	
	public PanelGeodatenDaten(InternalFrameMaps internalFrameI, String addTitleI, IMapDataController mapDataController) throws Throwable {
		super(internalFrameI, addTitleI);
		jbInit();
		initComponents();
		this.mapDataController = mapDataController;
		this.geodatenFinder = new GeodatenFinder();
	}

	@Override
	public InternalFrameMaps getInternalFrame() {
		return (InternalFrameMaps) super.getInternalFrame();
	}
	
	private void jbInit() throws Throwable {
		wlaAuswahlAdresse = new WrapperLabel(LPMain.getTextRespectUISPr("lp.typ"));
		wcbAuswahlAdresse = new WrapperComboBox();
		wcbAuswahlAdresse.setMandatoryField(true);
		wcbAuswahlAdresse.setMap(createMapAuswahlAdresse());
		wcbAuswahlAdresse.setActionCommand(Action.AUSWAHL_ADRESSE);
		wcbAuswahlAdresse.addActionListener(this);
		wcbAuswahlAdresse.setMandatoryField(false);

		wrbKarteLaden = new WrapperButton(LPMain.getTextRespectUISPr("geodaten.button.ladekarte"));
		wrbKarteLaden.setActionCommand(Action.LADE_KARTE);
		wrbKarteLaden.addActionListener(this);
		wrbKarteLaden.setEnabled(false);

		wrbGeodatenLaden = new WrapperButton(LPMain.getTextRespectUISPr("geodaten.button.ladegeodaten"));
		wrbGeodatenLaden.setActionCommand(Action.LADE_GEODATEN);
		wrbGeodatenLaden.addActionListener(this);
		wrbGeodatenLaden.setEnabled(false);
	
		panelKundeAuswahl = new PanelGotoKundeAuswahl(getInternalFrame());
		panelLieferantAuswahl = new PanelGotoLieferantAuswahl(getInternalFrame());
		panelPartnerAuswahl = new PanelGotoPartnerAuswahl(getInternalFrame());
		panelManuelleAuswahl = new PanelManuelleAuswahl();
		
		panelAuswahlTyp = new JPanel();
		HvLayout layoutAuswahlTyp = HvLayoutFactory.create(panelAuswahlTyp, "ins 0", "fill,grow", "");
		
		panelAuswahlAdresse = new JPanel();
		panelAuswahlAdresse.setBorder(BorderFactory.createTitledBorder("Adressauswahl"));
		HvLayout layoutAdressauswahl = HvLayoutFactory.create(panelAuswahlAdresse, "ins 0", "10[fill,200px|fill,300px|fill,grow]10", "5[]5[]0[]5");
		layoutAdressauswahl.add(wlaAuswahlAdresse).add(wcbAuswahlAdresse).wrap()
			.add(panelAuswahlTyp).spanAndWrap(3);
		
		actionAuswahlAdresstyp();
		
		panelTop = new JPanel();
		HvLayout layoutTop = HvLayoutFactory.create(panelTop, "", "fill,grow", "");
		layoutTop.add(panelAuswahlAdresse);

		panelFilterSerienbrief = new FilterSerienbriefPanel(getInternalFrame());
		currentFilterController = (IMapDataFilter) panelFilterSerienbrief;
		currentFilterController.registerGeodatenFilterListener(this);
		panelBottom = new JPanel();
		panelFilterSerienbrief.setBorder(BorderFactory.createTitledBorder("Datenfilter"));
		HvLayout layoutBottom = HvLayoutFactory.create(panelBottom, "", "10[fill,grow]10", "fill,grow");
		layoutBottom.add(panelFilterSerienbrief);

		initPanelBottomButtons();
		
		HvLayout layoutAll = HvLayoutFactory.create(this, "", "[fill,grow|fill]", "[|fill,grow|]");
		layoutAll.add(panelTop).spanAndWrap(2)
			.add(panelBottom).spanAndWrap(2)
			.add(panelBottomButtons, "skip 1, gapright 10, pushx");
	}
	
	private void initPanelBottomButtons() {
		panelBottomButtons = new JPanel();
		HvLayout layoutButtons = HvLayoutFactory.create(panelBottomButtons, "ins 0", "[fill,150|fill,150]", "");
		layoutButtons.add(wrbGeodatenLaden).add(wrbKarteLaden);
	}

	private Map<AdressauswahlEnum, String> createMapAuswahlAdresse() {
		Map<AdressauswahlEnum, String> map = new LinkedHashMap<AdressauswahlEnum, String>();
		for (AdressauswahlEnum choice : EnumSet.allOf(AdressauswahlEnum.class)) {
			map.put(choice, choice.getValue());
		}
		return map;
	}

	protected void eventActionSpecial(ActionEvent event) throws Throwable {
		if (Action.AUSWAHL_ADRESSE.equals(event.getActionCommand())) {
			actionAuswahlAdresstyp();
		} else if (Action.LADE_KARTE.equals(event.getActionCommand())) {
			actionLadeKarte();
		} else if (Action.LADE_GEODATEN.equals(event.getActionCommand())) {
			actionLadeGeodaten();
		}
	}
	
	private void actionLadeGeodaten() {
		currentFilterController.loadGeodaten();
	}

	private void actionLadeKarte() {
		if (!selectedAuswahlPanel.isSet()) {
			return;
		}
		
		MapPosition center = selectedAuswahlPanel.get();
		if (center == null) return; // TODO
		
		if (currentFilterController.getMapDataController().isPresent()) {
			mapDataController.loadMap(center, currentFilterController.getMapDataController().get());
		} else {
			mapDataController.loadMap(center);
		}
	}

	private void actionAuswahlAdresstyp() {
		AdressauswahlEnum typeSelected = (AdressauswahlEnum) wcbAuswahlAdresse.getKeyOfSelectedItem();
		if (AdressauswahlEnum.MANUELL.equals(typeSelected)) {
			adresstypManuellSelected();
		} else if (AdressauswahlEnum.KUNDE.equals(typeSelected)) {
			adresstypKundeSelected();
		} else if (AdressauswahlEnum.LIEFERANT.equals(typeSelected)) {
			adresstypLieferantSelected();
		} else if (AdressauswahlEnum.PARTNER.equals(typeSelected)) {
			adresstypPartnerSelected();
		}
	}

	private void adresstypPartnerSelected() {
		adresstypDialogAuswahlSelected(panelPartnerAuswahl);
	}

	private void adresstypLieferantSelected() {
		adresstypDialogAuswahlSelected(panelLieferantAuswahl);
	}

	private void adresstypKundeSelected() {
		adresstypDialogAuswahlSelected(panelKundeAuswahl);
	}
	
	private void adresstypDialogAuswahlSelected(PanelDialogAuswahl panelDialogAuswahl) {
		panelDialogAuswahl.setDefaults();
		panelAuswahlTyp.removeAll();
		panelAuswahlTyp.add(panelDialogAuswahl);
		selectedAuswahlPanel = panelDialogAuswahl;
		addressChanged();
	}

	private void adresstypManuellSelected() {
		panelManuelleAuswahl.setDefaults();
		panelAuswahlTyp.removeAll();
		panelAuswahlTyp.add(panelManuelleAuswahl);
		selectedAuswahlPanel = panelManuelleAuswahl;
		addressChanged();
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_GEODATENANZEIGE;
	}

	@Override
	public void filterChanged() {
		wrbGeodatenLaden.setEnabled(currentFilterController.mustLoadGeodaten());
	}
	
	public void addressChanged() {
		wrbKarteLaden.setEnabled(selectedAuswahlPanel.isSet());
	}
	
	private abstract class PanelDialogAuswahl extends PanelBasis implements IMapPositionHolder {
		private static final long serialVersionUID = 6514023567994257088L;

		private WrapperGotoButton wbuGoto;
		private WrapperTextField wtfName;
		private WrapperTextField wtfAdresse;
		protected PanelQueryFLR panelQueryFLR;
		
		public PanelDialogAuswahl(InternalFrameMaps internalFrame) throws Throwable {
			super(internalFrame, "");
			initPanel();
		}
		
		protected abstract WrapperGotoButton createGotoButton();
		
		private void initPanel() {
			// wegen Dialogauswahl auf FLR events hoeren
			getInternalFrame().addItemChangedListener(this);

			wbuGoto = createGotoButton();
			
			wtfName = new WrapperTextField(400);
			wtfName.setEditable(false);
			wtfAdresse = new WrapperTextField(400);
			wtfAdresse.setEditable(false);
			wtfAdresse.setMandatoryField(true);
			wtfAdresse.addValueChangedListener(new IHvValueHolderListener() {
				public void valueChanged(Component reference, Object oldValue, Object newValue) {
					addressChanged();
				}
			});

			HvLayout layoutKundeAuswahl = HvLayoutFactory.create(this, "ins 0", "[fill,200px|fill,grow]", "[]5[]");
			layoutKundeAuswahl.add(wbuGoto).add(wtfName).wrap()
				.add(wtfAdresse).skip(1);
		}
		
		@Override
		protected void eventActionSpecial(ActionEvent event) throws Throwable {
			if (event == null || event.getActionCommand() == null) return;
			if (event.getActionCommand().equals(wbuGoto.getActionCommand())) {
				actionDialogAuswahl();
			}
		}

		protected abstract void actionDialogAuswahl() throws Throwable;

		@Override
		protected void eventItemchanged(EventObject eo) throws Throwable {
			ItemChangedEvent e = (ItemChangedEvent) eo;
			if (e.getID() != ItemChangedEvent.GOTO_DETAIL_PANEL) return; 
			
			if (e.getSource() == panelQueryFLR) {
				actionPartnerSelected((Integer) ((ISourceEvent) e.getSource()).getIdSelected());
			}
		}
		
		private void actionPartnerSelected(Integer selectedIId) throws Throwable {
			setPartnerDto(selectedIId);
			wbuGoto.setOKey(selectedIId);
			wtfName.setText(getPartnerDto().formatFixName1Name2());
			setAddress(getPartnerDto().formatAdresse());
		}

		protected abstract PartnerDto setPartnerDto(Integer selectedIId) throws Throwable;
		
		protected abstract PartnerDto getPartnerDto();
		
		public void setDefaults() {
			wtfName.setText(null);
			setAddress(null);;
		}
		
		protected void setAddress(String address) {
			wtfAdresse.setText(address);
//			addressChanged();
		}
		
		@Override
		public boolean isSet() {
			return getPartnerDto() != null && !Helper.isStringEmpty(getPartnerDto().formatAdresse());
		}
		
		protected void updateGeodaten(GeodatenDto geodatenDto) {
			try {
				DelegateFactory.getInstance().getPartnerDelegate().createGeodaten(geodatenDto);
			} catch (ExceptionLP e) {
				myLogger.error("Geodaten konnten nicht upgedated werden", e);
			}
		}
		
		@Override
		public MapPosition get() {
			if (!isSet()) return null;
			
			GeodatenDto geodatenDto = getPartnerDto().getGeodatenDto();
			if (geodatenDto == null) {
				geodatenDto = geodatenFinder.find(getPartnerDto().formatAdresse());
				if (geodatenDto == null) return null;
				
				geodatenDto.setPartnerIId(getPartnerDto().getIId());
				getPartnerDto().setGeodatenDto(geodatenDto);
				updateGeodaten(geodatenDto);
			}
			
			return createMapPosition();
		}

		protected abstract MapPosition createMapPosition();
		
	}
	
	private class PanelGotoPartnerAuswahl extends PanelDialogAuswahl {
		private static final long serialVersionUID = -4939804757463720825L;
		private PartnerDto partnerDto;

		public PanelGotoPartnerAuswahl(InternalFrameMaps internalFrame) throws Throwable {
			super(internalFrame);
		}

		@Override
		protected WrapperGotoButton createGotoButton() {
			WrapperGotoButton wbuPartner = new WrapperGotoButton(
					LPMain.getTextRespectUISPr("button.partner"), GotoHelper.GOTO_PARTNER_AUSWAHL);
			wbuPartner.setActionCommand(Action.DIALOG_PARTNER);
			wbuPartner.addActionListener(this);
			return wbuPartner;
		}

		@Override
		protected void actionDialogAuswahl() throws Throwable {
			panelQueryFLR = PartnerFilterFactory.getInstance()
					.createPanelFLRPartner(getInternalFrame());
			new DialogQuery(panelQueryFLR);
		}

		@Override
		protected PartnerDto setPartnerDto(Integer selectedIId) throws Throwable {
			partnerDto = DelegateFactory.getInstance().getPartnerDelegate().partnerFindByPrimaryKey(selectedIId);
			return partnerDto;
		}
		
		@Override
		protected PartnerDto getPartnerDto() {
			return partnerDto;
		}
		
		@Override
		protected MapPosition createMapPosition() {
			return new MapPartner(getPartnerDto());
		}
		
		public void setDefaults() {
			super.setDefaults();
			partnerDto = null;
		}
	}
	
	private class PanelGotoKundeAuswahl extends PanelDialogAuswahl {
		private static final long serialVersionUID = -2320644680194064365L;
		private KundeDto kundeDto;

		public PanelGotoKundeAuswahl(InternalFrameMaps internalFrame) throws Throwable {
			super(internalFrame);
		}

		@Override
		protected WrapperGotoButton createGotoButton() {
			WrapperGotoButton wbuKunde = new WrapperGotoButton(
					LPMain.getTextRespectUISPr("button.kunde"), GotoHelper.GOTO_KUNDE_AUSWAHL);
			wbuKunde.setActionCommand(Action.DIALOG_KUNDE);
			wbuKunde.addActionListener(this);
			return wbuKunde;
		}

		@Override
		protected void actionDialogAuswahl() throws Throwable {
			panelQueryFLR = PartnerFilterFactory.getInstance()
					.createPanelFLRKunde(getInternalFrame(), true, false);
			new DialogQuery(panelQueryFLR);
		}

		@Override
		protected PartnerDto setPartnerDto(Integer selectedIId) throws Throwable {
			kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(selectedIId);
			return kundeDto.getPartnerDto();
		}
		
		@Override
		protected PartnerDto getPartnerDto() {
			return kundeDto != null ? kundeDto.getPartnerDto() : null;
		}
		
		@Override
		protected MapPosition createMapPosition() {
			return new MapKunde(kundeDto);
		}
		
		@Override
		public void setDefaults() {
			super.setDefaults();
			kundeDto = null;
		}
	}
	
	private class PanelGotoLieferantAuswahl extends PanelDialogAuswahl {
		private static final long serialVersionUID = 6717695395111418407L;
		private LieferantDto lieferantDto;

		public PanelGotoLieferantAuswahl(InternalFrameMaps internalFrame) throws Throwable {
			super(internalFrame);
		}

		@Override
		protected WrapperGotoButton createGotoButton() {
			WrapperGotoButton wbuLieferant = new WrapperGotoButton(
					LPMain.getTextRespectUISPr("button.lieferant"), GotoHelper.GOTO_LIEFERANT_AUSWAHL);
			wbuLieferant.setActionCommand(Action.DIALOG_LIEFERANT);
			wbuLieferant.addActionListener(this);
			return wbuLieferant;
		}

		@Override
		protected void actionDialogAuswahl() throws Throwable {
			panelQueryFLR = PartnerFilterFactory.getInstance()
					.createPanelFLRLieferant(getInternalFrame(), null, true, false);
			new DialogQuery(panelQueryFLR);
		}

		@Override
		protected PartnerDto setPartnerDto(Integer selectedIId) throws Throwable {
			lieferantDto = DelegateFactory.getInstance().getLieferantDelegate().lieferantFindByPrimaryKey(selectedIId);
			return lieferantDto.getPartnerDto();
		}
		
		@Override
		protected PartnerDto getPartnerDto() {
			return lieferantDto != null ? lieferantDto.getPartnerDto() : null;
		}
		
		@Override
		protected MapPosition createMapPosition() {
			return new MapLieferant(lieferantDto);
		}
		
		@Override
		public void setDefaults() {
			super.setDefaults();
			lieferantDto = null;
		}
	}
	
	private class PanelManuelleAuswahl extends JPanel implements IMapPositionHolder {
		private static final long serialVersionUID = 2377952360748459642L;

		private WrapperLabel wlaManuelleAuswahl;
		private WrapperTextField wtfAusgangsadresse;
		private GeodatenDto geodatenDto;
		
		public PanelManuelleAuswahl() {
			super();
			initPanel();
		}

		public void setDefaults() {
			wtfAusgangsadresse.setText(null);
			geodatenDto = null;
		}

		private void initPanel() {
			wlaManuelleAuswahl = new WrapperLabel(LPMain.getTextRespectUISPr("part.adresse"));
			wtfAusgangsadresse = new WrapperTextField();
			wtfAusgangsadresse.setColumnsMax(400);
			wtfAusgangsadresse.setMandatoryField(true);
			
			wtfAusgangsadresse.getDocument().addDocumentListener(new DocumentListener() {
				public void removeUpdate(DocumentEvent arg0) {
					geodatenDto = null;
					addressChanged();
				}
				public void insertUpdate(DocumentEvent arg0) {
					geodatenDto = null;
					addressChanged();
				}
				public void changedUpdate(DocumentEvent arg0) {
					geodatenDto = null;
					addressChanged();
				}
			});
			wtfAusgangsadresse.addValueChangedListener(new IHvValueHolderListener() {
				public void valueChanged(Component reference, Object oldValue, Object newValue) {
					addressChanged();
				}
			});

			HvLayout layoutManuelleAuswahl = HvLayoutFactory.create(this, "ins 0", "[fill,200px|fill,grow]", "");
			layoutManuelleAuswahl.add(wlaManuelleAuswahl).add(wtfAusgangsadresse);
		}

		@Override
		public boolean isSet() {
			return !Helper.isStringEmpty(wtfAusgangsadresse.getText());
		}

		@Override
		public MapPosition get() {
			if (!isSet()) return null;
			
			if (geodatenDto == null) {
				geodatenDto = geodatenFinder.find(wtfAusgangsadresse.getText());
			}
			return geodatenDto != null ? new MapPosition(geodatenDto, wtfAusgangsadresse.getText()) : null;
		}
	}
}
