package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.SwingWorker;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.partner.service.GeodatenDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.service.google.geocoding.DefaultGeocodeRequestConfig;
import com.lp.service.google.geocoding.GeocodeApiController;
import com.lp.service.google.geocoding.GeocodeApiRequest;
import com.lp.service.google.geocoding.IGeocodeApi;
import com.lp.service.google.geocoding.IGeocodeResponseCallback;
import com.lp.service.google.geocoding.schema.GeocodeApiResponse;
import com.lp.service.google.geocoding.schema.GeocodeResultStatusEntry;
import com.lp.util.Helper;

public class WrapperGeodatenButton extends WrapperButton implements	ActionListener {
	private static final long serialVersionUID = 9113827513551033203L;
	
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());
	private PartnerDto partnerDto;
	private GeodatenDto geodatenDto;
	private IGeocodeApi geocodeApiController;
	
	public WrapperGeodatenButton(PartnerDto partnerDto) {
		super(IconFactory.getGeocoordinates());
		setPartnerDto(partnerDto);
		addActionListener(this);
		setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		setPreferredSize(HelperClient.getToolsPanelButtonDimension());
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}
	
	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
		disableIfAddressIsEmpty();
		refreshGeodaten();
	}
	
	private void disableIfAddressIsEmpty(){
		this.setEnabled(!hasAddress());
	}

	private boolean hasAddress() {
		return getPartnerDto() == null || Helper.isStringEmpty(getPartnerDto().formatAdresse());
	}

	public GeodatenDto getGeodatenDto() {
		return geodatenDto;
	}
	
	private void setGeodatenDto(GeodatenDto geodatenDto) {
		this.geodatenDto = geodatenDto;
		refreshToolTipText();
	}
	
	private void refreshGeodaten() {
		if (getPartnerDto() == null || getPartnerDto().getIId() == null) {
			setGeodatenDto(null);
			return;
		}
		
		try {
			setGeodatenDto(DelegateFactory.getInstance().getPartnerDelegate()
					.geodatenFindByPartnerIIdOhneExc(getPartnerDto().getIId()));
		} catch (ExceptionLP e) {
			setGeodatenDto(null);
			myLogger.error("Geodaten von Partner '" + getPartnerDto().formatFixName1Name2()
					+ "' (iId = " + getPartnerDto().getIId() + ") konnten nicht vom Server geladen werden", e);
		}
	}

	private void refreshToolTipText() {
		setToolTipText(getGeodatenDto() == null ? LPMain.getTextRespectUISPr("part.geodaten.aktualisiere") : 
			LPMain.getTextRespectUISPr("part.geodaten.aktualisiere") + " - " + formatKoordinaten());
	}

	private String formatKoordinaten() {
		return LPMain.getTextRespectUISPr("part.geodaten.koordinaten") 
				+ ": lng = " + Helper.formatZahl(geodatenDto.getLaengengrad(), 6, LPMain.getInstance().getUISprLocale())
				+ ", lat = " + Helper.formatZahl(geodatenDto.getBreitengrad(), 6, LPMain.getInstance().getUISprLocale());
	}
	
	private IGeocodeApi getGeocodeApiController() {
		try {
			if (geocodeApiController == null)
				geocodeApiController = new GeocodeApiController(new DefaultGeocodeRequestConfig());
		} catch (Throwable e) {
			myLogger.error("Fehler beim Erstellen der DefaultGeocodeRequestConfig", e);
		}
		return geocodeApiController;
	}
	
	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		if (getGeocodeApiController() == null) return;
		
		requestGeocode(getGeocodeApiController());
	}

	private void requestGeocode(IGeocodeApi geocodeApiController) {
		GeocodeWorker geocodeWorker = new GeocodeWorker(getPartnerDto().formatAdresse());
		geocodeWorker.execute();
	}

	private class GeocodeWorker extends SwingWorker<Void, Void> implements IGeocodeResponseCallback {
		private String address;
		
		public GeocodeWorker(String address) {
			this.address = address;
		}
		
		@Override
		public void onSuccess(GeocodeApiRequest request, GeocodeApiResponse response) {
			if (GeocodeResultStatusEntry.OK.equals(response.getStatus())) {
				GeodatenDto geodatenDto = getGeodatenDto() != null ? getGeodatenDto() : new GeodatenDto();
				geodatenDto.setPartnerIId(getPartnerDto().getIId());
				geodatenDto.setLaengengrad(new BigDecimal(response.getResults().get(0).getGeometry().getLocation().getLongitude()));
				geodatenDto.setBreitengrad(new BigDecimal(response.getResults().get(0).getGeometry().getLocation().getLatitude()));
				setGeodatenDto(updateGeodaten(geodatenDto));
			} else if (GeocodeResultStatusEntry.ZERO_RESULTS.equals(response.getStatus())) {
				if (getGeodatenDto() != null) {
					removeGeodaten(getGeodatenDto().getIId());
					refreshGeodaten();
				}
			} else {
				myLogger.error("Kein Update der Geodaten von Partner '" + getPartnerDto().formatFixName1Name2() 
						+ "' (iId = " + getPartnerDto().getIId() + ") fuer Addresse '" + address 
						+ "'. Response lieferte Status '" + response.getStatus() + "'"
						+ (!Helper.isStringEmpty(response.getErrorMessage()) ? " und Errormessage '" + response.getErrorMessage() + "'" : ""));
			}
		}

		@Override
		public void onError(GeocodeApiRequest request) {
			myLogger.error("Geodaten von Partner '" + getPartnerDto().formatFixName1Name2()
					+ "' (iId = " + getPartnerDto().getIId() + ") fuer Addresse '" 
					+ address + "' konnten nicht abgerufen werden");
		}

		@Override
		public void onException(GeocodeApiRequest request, Exception e) {
			myLogger.error("Geodaten von Partner '" + getPartnerDto().formatFixName1Name2()
					+ "' (iId = " + getPartnerDto().getIId() + ") fuer Addresse '" 
					+ address + "'konnten nicht abgerufen werden", e);
		}

		@Override
		protected Void doInBackground() throws Exception {
			getGeocodeApiController().findByAddress(address, this);
			return null;
		}
		
	}
	
	private GeodatenDto updateGeodaten(GeodatenDto geodatenDto) {
		try {
			if (geodatenDto.getIId() == null) {
				Integer iId = DelegateFactory.getInstance().getPartnerDelegate().createGeodaten(geodatenDto);
				geodatenDto.setIId(iId);
			} else {
				DelegateFactory.getInstance().getPartnerDelegate().updateGeodaten(geodatenDto);
			}
			return geodatenDto;
		} catch (ExceptionLP e) {
			myLogger.error("Geodaten von Partner '" + getPartnerDto().formatFixName1Name2()
					+ "' (iId = " + getPartnerDto().getIId() + ") konnten nicht erstellt oder upgedatet werden", e);
			return getGeodatenDto();
		}
	}
	
	public void removeGeodaten(Integer iId) {
		try {
			DelegateFactory.getInstance().getPartnerDelegate().removeGeodaten(iId);
		} catch (ExceptionLP e) {
			myLogger.error("Geodaten von Partner '" + getPartnerDto().formatFixName1Name2()
					+ "' (iId = " + getPartnerDto().getIId() + ") konnten nicht geloescht werden", e);
		}
	}

	public void setEditable(boolean bEditable) {
	    setEnabled(!bEditable);
	}
	
	public void setMinimumSize(Dimension d){
		super.setMinimumSizeImpl(d);
	}
	
	public void setPreferredSize(Dimension d){
		super.setPreferredSizeImpl(d);
	}
	
}
