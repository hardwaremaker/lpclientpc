package com.lp.client.geodaten;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Map;

import javax.swing.SwingConstants;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperGotoSerienbrief;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.SerienbriefDto;
import com.lp.server.partner.service.SerienbriefEmpfaengerDto;
import com.lp.server.util.HvOptional;

public class FilterSerienbriefPanel extends PanelBasis implements IMapDataFilter {
	private static final long serialVersionUID = -2837292028438821281L;

	private static final String ACTION_DIALOG_SERIENBRIEF = "action_special_DIALOG_SERIENBRIEF";

	private PanelQueryFLR panelQueryFLRSerienbrief;
	private SerienbriefTransformer dataTransformer;
	private WrapperGotoButton wbuSerienbrief;
	private WrapperTextField wtfSerienbrief;
	private WrapperLabel wlaInfo;

	private WrapperLabel wlaLocaleKommunikation = null;
	private WrapperComboBox wcoLocaleKommunikation = null;

	private Collection<IGeodatenFilterListener> filterListener;

	public FilterSerienbriefPanel(InternalFrameMaps internalFrame) throws Throwable {
		super(internalFrame, "");
		filterListener = new HashSet<IGeodatenFilterListener>();
		jbInit();
	}

	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		wbuSerienbrief = new WrapperGotoSerienbrief(LPMain.getTextRespectUISPr("button.serienbrief"));
		wbuSerienbrief.setActionCommand(ACTION_DIALOG_SERIENBRIEF);
		wbuSerienbrief.addActionListener(this);

		wtfSerienbrief = new WrapperTextField(50);
		wlaInfo = new WrapperLabel();
		wlaInfo.setHorizontalAlignment(SwingConstants.LEFT);

		wlaLocaleKommunikation = new WrapperLabel(LPMain.getTextRespectUISPr("part.sprache.kommunikation"));
		Map<?, ?> tmLocales = DelegateFactory.getInstance().getLocaleDelegate()
				.getAllLocales(LPMain.getTheClient().getLocUi());
		wcoLocaleKommunikation = new WrapperComboBox();

		wcoLocaleKommunikation.addActionListener(this);

		wcoLocaleKommunikation.emptyEntry = LPMain.getTextRespectUISPr("part.geodaten.locale.alle");

		wcoLocaleKommunikation.setMap(tmLocales);

		HvLayout layout = HvLayoutFactory.create(this, "", "10[fill,200px|fill,grow]10", "");
		layout.add(wbuSerienbrief).add(wtfSerienbrief).wrap();

		layout.add(wlaLocaleKommunikation).add(wcoLocaleKommunikation).wrap()

				.add(wlaInfo).skip(1);

	}

	@Override
	protected void eventActionSpecial(ActionEvent event) throws Throwable {
		if (ACTION_DIALOG_SERIENBRIEF.equals(event.getActionCommand())) {
			actionSerienbriefAuswahl();
		}

		if (event.getSource().equals(wcoLocaleKommunikation) && wbuSerienbrief.getOKey() != null) {
			Integer serienbriefIId = (Integer) wbuSerienbrief.getOKey();

			SerienbriefEmpfaengerDto[] empfaenger = DelegateFactory.getInstance().getKundeDelegate()
					.getSerienbriefEmpfaenger(serienbriefIId, (String) wcoLocaleKommunikation.getKeyOfSelectedItem());
			dataTransformer = new SerienbriefTransformer(empfaenger);
			wlaInfo.setText(createInfoText());
			notifyFilterChanged();
		}

	}

	@Override
	protected void eventItemchanged(EventObject eo) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eo;
		if (e.getSource() != panelQueryFLRSerienbrief)
			return;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			actionSerienbriefSelected((Integer) ((ISourceEvent) e.getSource()).getIdSelected());
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			actionLeeren();
		}
	}

	private void actionLeeren() {
		wtfSerienbrief.setText(null);
		wbuSerienbrief.setOKey(null);
		wlaInfo.setText(null);
		dataTransformer = null;
		notifyFilterChanged();
	}

	private void actionSerienbriefSelected(Integer serienbriefIId) throws Throwable {
		SerienbriefDto serienbriefDto = DelegateFactory.getInstance().getPartnerServicesDelegate()
				.serienbriefFindByPrimaryKey(serienbriefIId);
		wbuSerienbrief.setOKey(serienbriefDto.getIId());
		wtfSerienbrief.setText(serienbriefDto.getCBez());

		wcoLocaleKommunikation.setKeyOfSelectedItem(serienbriefDto.getLocaleCNr());

		SerienbriefEmpfaengerDto[] empfaenger = DelegateFactory.getInstance().getKundeDelegate()
				.getSerienbriefEmpfaenger(serienbriefIId, (String) wcoLocaleKommunikation.getKeyOfSelectedItem());
		dataTransformer = new SerienbriefTransformer(empfaenger);
		wlaInfo.setText(createInfoText());
		notifyFilterChanged();
	}

	private String createInfoText() {
		if (dataTransformer == null)
			return "";

		String info = dataTransformer.getEmpfaengerRaw().length + " Serienbriefempf\u00e4nger, "
				+ dataTransformer.getEmpfaenger().size() + " mit Adresse und davon "
				+ dataTransformer.getPartnerOhneGeodaten().size() + " ohne Geodaten.";
		return info;
	}

	private void actionSerienbriefAuswahl() throws Throwable {
		panelQueryFLRSerienbrief = PartnerFilterFactory.getInstance().createPanelFLRSerienbrief(getInternalFrame(),
				true);
		new DialogQuery(panelQueryFLRSerienbrief);
	}

	@Override
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
//		wtfSerienbrief.setText(null);

	}

	@Override
	public HvOptional<IMapData> getMapDataController() {
		return HvOptional.ofNullable((IMapData) dataTransformer);
	}

	@Override
	public void loadGeodaten() {
		if (dataTransformer == null)
			return;

		try {
			dataTransformer.loadGeodaten();
		} catch (Throwable e) {
			handleException(e, false);
		}
	}

	@Override
	public void registerGeodatenFilterListener(IGeodatenFilterListener listener) {
		filterListener.add(listener);
	}

	private void notifyFilterChanged() {
		for (IGeodatenFilterListener listener : filterListener) {
			listener.filterChanged();
		}
	}

	@Override
	public boolean mustLoadGeodaten() {
		return dataTransformer != null && !dataTransformer.getPartnerOhneGeodaten().isEmpty();
	}
}
