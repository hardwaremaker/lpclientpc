package com.lp.client.geodaten;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.partner.service.GeodatenDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.service.google.geocoding.GeodatenFinder;
import com.lp.util.Helper;

public class BaseMapDataTransformer {
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());

	private Map<Integer,PartnerDto> hmPartner;
	private GeodatenFinder geodatenFinder;
	
	public BaseMapDataTransformer() throws Throwable {
		geodatenFinder = new GeodatenFinder();
	}
	
	protected Map<Integer,PartnerDto> getPartnerMap() {
		if (hmPartner == null) {
			hmPartner = new HashMap<Integer, PartnerDto>();
		}
		return hmPartner;
	}
	
	public void loadGeodaten() throws Throwable {
		if (getPartnerMap().isEmpty()) return;
		
		loadGeodatenByDialog();
//		loadGeodatenImpl();
	}

	private void loadGeodatenByDialog() throws Throwable {
		List<PartnerDto> requestList = getRequestList();
		if (requestList.size() < 1) return;
		
		myLogger.info("Geodaten von " + requestList.size() + " Adressen werden benoetigt.");
		DialogGeodatenAbfrage dialog = new DialogGeodatenAbfrage(LPMain.getInstance().getDesktop(), false);
		dialog.setVisible(true);
		dialog.setData(requestList, new IGeodatenDtoCallback() {
			public void onResult(GeodatenDto geodatenDto) {
				PartnerDto partnerDto = getPartnerMap().get(geodatenDto.getPartnerIId());
				if (partnerDto != null) partnerDto.setGeodatenDto(geodatenDto);
			}
		});
		dialog.doRequest();
	}

	private List<PartnerDto> getRequestList() {
		List<PartnerDto> requestList = new ArrayList<PartnerDto>();
		for (PartnerDto partner : getPartnerMap().values()) {
			if (partner.getGeodatenDto() == null) {
				requestList.add(partner);
			}
		}
		return requestList;
	}
	
	public List<PartnerDto> getPartnerOhneGeodaten() {
		return getRequestList();
	}

	protected boolean isValid(PartnerDto partnerDto) {
		return partnerDto != null && !Helper.isStringEmpty(partnerDto.formatAdresse());
	}
	
//	private void loadGeodatenImpl() {
//		GeodatenAbfrageCtrl ctrl = new GeodatenAbfrageCtrl();
//		ctrl.go();
////		latch.await();
//		myLogger.info("Done with loading geodaten");
//	}
	
//	private class GeodatenAbfrageCtrl implements IGeodatenabfrageCallback {
//		private CountDownLatch latch;
//		GeodatenCreator creator = new GeodatenCreator();
//		private int level;
//		private Map<Integer, ProcessingState> hmStati;
//		
//		public GeodatenAbfrageCtrl() {
//		}
//		
//		public Map<Integer, ProcessingState> getHmStati() {
//			if (hmStati == null) {
//				initStatiMap();
//			}
//			return hmStati;
//		}
//		
//		private void initStatiMap() {
//			hmStati = new HashMap<Integer, ProcessingState>();
//			for (PartnerDto partner : getPartnerMap().values()) {
//				hmStati.put(partner.getIId(), new FullAddressState());
//			}
//		}
//
//		public void go() {
//			List<PartnerDto> requestList = getRequestList();
//			for (level = 0; level < 3; level++) {
//				List<GeocodeApiRequest> apiRequests = createGeodatenApiRequests(requestList);
//				geodatenFinder.find(apiRequests, this);
//				requestList = getRequestList();
//				if (requestList.size() < 1) {
//					break;
//				}
//			}
//			finish();
//		}
//		
//		private void finish() {
//			creator.execute();
//		}
//
//		private List<GeocodeApiRequest> createGeodatenApiRequests(List<PartnerDto> list) {
//			List<GeocodeApiRequest> requests = new ArrayList<GeocodeApiRequest>();
//			for (PartnerDto dto : list) {
//				ProcessingState state = getHmStati().get(dto.getIId());
//				requests.add(new GeocodeApiRequest(dto.getIId(), state.getSearchString(dto)));
//			}
//			return requests;
//		}
//		
//		private List<PartnerDto> getRequestList() {
//			List<PartnerDto> requestList = new ArrayList<PartnerDto>();
//			for (PartnerDto partner : getPartnerMap().values()) {
//				if (partner.getGeodatenDto() == null) {
//					requestList.add(partner);
//				}
//			}
//			return requestList;
//		}
//
//		@Override
//		public void process(List<GeocodeResult> results) {
//			processImpl(results);
//		}
//		
//		private void processImpl(List<GeocodeResult> results) {
//			for (GeocodeResult result : results) {
//				PartnerDto partnerDto = getPartnerMap().get(result.getGeodatenDto().getPartnerIId());
//				if (GeocodeResultStatusEntry.OK.equals(result.getStatus())) {
//					partnerDto.setGeodatenDto(result.getGeodatenDto());
//					creator.add(result.getGeodatenDto());
//				}
//				ProcessingState state = getHmStati().get(partnerDto.getIId());
//				getHmStati().put(partnerDto.getIId(), state.getNewState(result.getStatus()));
//			}
//		}
//
//		@Override
//		public void processResultNotFound(Integer id) {
//		}
//
//		@Override
//		public void done() {
//			latch.countDown();
//		}
//		
//	}
//	
//	private class GeodatenCreator extends SwingWorker<Void, Void> {
//		private List<GeodatenDto> creationList;
//		
//		public GeodatenCreator(List<GeodatenDto> creationList) {
//			this.creationList = creationList;
//		}
//		
//		public GeodatenCreator() {
//		}
//		
//		private List<GeodatenDto> getCreationList() {
//			if (creationList == null) {
//				creationList = new ArrayList<GeodatenDto>();
//			}
//			return creationList;
//		}
//		
//		public void add(GeodatenDto geodatenDto) {
//			getCreationList().add(geodatenDto);
//		}
//		
//		@Override
//		protected Void doInBackground() throws Exception {
//			DelegateFactory.getInstance().getPartnerDelegate().createGeodaten(getCreationList());
//			return null;
//		}
//		
//	}
//	
//	public interface ProcessingState {
//		ProcessingState getNewState(GeocodeResultStatusEntry status);
//		String getSearchString(PartnerDto partnerDto);
//	}
//	
//	public class FullAddressState implements ProcessingState {
//		public ProcessingState getNewState(GeocodeResultStatusEntry status) {
//			if (GeocodeResultStatusEntry.ZERO_RESULTS.equals(status)) 
//				return new JustCityState();
//			return this;
//		}
//		
//		@Override
//		public String getSearchString(PartnerDto partnerDto) {
//			return partnerDto.formatAdresse();
//		}
//	}
//	
//	public class JustCityState implements ProcessingState {
//		public ProcessingState getNewState(GeocodeResultStatusEntry status) {
//			return this;
//		}
//		
//		@Override
//		public String getSearchString(PartnerDto partnerDto) {
//			return partnerDto.formatLKZPLZOrt();
//		}
//	}
}
