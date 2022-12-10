package com.lp.client.geodaten;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.partner.service.GeodatenDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.service.google.geocoding.GeocodeApiRequest;
import com.lp.service.google.geocoding.GeocodeResult;
import com.lp.service.google.geocoding.GeodatenFinder;
import com.lp.service.google.geocoding.IGeodatenabfrageCallback;
import com.lp.service.google.geocoding.schema.GeocodeResultStatusEntry;

public class DialogGeodatenAbfrage extends JDialog implements ActionListener {
	private static final long serialVersionUID = 2526702116229017700L;
	protected final LpLogger log = (LpLogger) LpLogger.getInstance(this.getClass());

	private Map<Integer, RequestData> hmData;
	private List<PartnerDto> data;
	
	private GeodatenResultsTableModel tableModel;
	private JTable tableResults;
	private JScrollPane panelTableResults;
	private JProgressBar progressBar;
	private WrapperButton wrbCancel;
	
	private GeodatenFinder finder;
	private GeodatenAbfrageWorker requestWorker;
	private IGeodatenDtoCallback geodatenDtoCallback;
	
	private class RequestData implements Comparable<RequestData> {
		private PartnerDto partnerDto;
		private GeocodeResult result;
		
		public RequestData(PartnerDto partner) {
			setPartnerDto(partner);
		}
		public PartnerDto getPartnerDto() {
			return partnerDto;
		}
		public void setPartnerDto(PartnerDto partnerDto) {
			this.partnerDto = partnerDto;
		}
		public GeocodeResult getResult() {
			return result;
		}
		public void setResult(GeocodeResult result) {
			this.result = result;
		}
		
		@Override
		public int compareTo(RequestData other) {
			return 0;
		}
	}
	
	public DialogGeodatenAbfrage(Frame owner, boolean modal) throws Throwable {
		super(owner, LPMain.getTextRespectUISPr("geodaten.dialog.abfrage.titel"), modal);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		finder = new GeodatenFinder();
		jbInit();
		pack();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);
	}
	
	public void setData(List<PartnerDto> partner) {
		setData(partner, new IGeodatenDtoCallback() {
			public void onResult(GeodatenDto geodatenDto) {
			}
		});
	}
	
	public void setData(List<PartnerDto> partner, IGeodatenDtoCallback callback) {
		data = partner;
		initResults();
		geodatenDtoCallback = callback;
	}
	
	private List<PartnerDto> getData() {
		if (data == null) 
			data = new ArrayList<PartnerDto>();
		return data;
	}
	
	public Map<Integer, RequestData> getHmData() {
		if (hmData == null) {
			hmData = new HashMap<Integer, RequestData>();
		}
		return hmData;
	}
	
	private void initResults() {
		hmData = new HashMap<Integer, RequestData>();
		for (PartnerDto partner : getData()) {
			hmData.put(partner.getIId(), new RequestData(partner));
		}
		onDataChanged();
	}
	
	public void doRequest() {
		cancelWorker();
		requestWorker = new GeodatenAbfrageWorker(getData());
		requestWorker.execute();
		log.info("Worker started");
	}
	
	private void cancelWorker() {
		if (requestWorker == null) return;
		
		requestWorker.cancel();
		log.info("Worker canceled");
	}
	
	private void onDataChanged() {
		progressBar.setMaximum(getData().size());
		tableModel.fireTableDataChanged();
	}
	
	private void jbInit() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				cancelWorker();
			}
		});
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setVisible(true);
		
		tableModel = new GeodatenResultsTableModel();
		tableResults = new JTable(tableModel);
		setPreferredColumnWidth(tableResults, new Integer[]{500, 500, 100});
		
		panelTableResults = new JScrollPane(tableResults);
		panelTableResults.setPreferredSize(new Dimension(1200, 400));
		
		wrbCancel = new WrapperButton(LPMain.getTextRespectUISPr("lp.abbrechen"));
		wrbCancel.addActionListener(this);
		
		getContentPane().setLayout(new MigLayout("wrap 2", "[fill,grow|fill,150]", "[fill|fill,grow|fill]"));
		getContentPane().add(progressBar, "span 2");
		getContentPane().add(panelTableResults, "span 2");
		getContentPane().add(wrbCancel, "skip 1");
	}
	
	private void setPreferredColumnWidth(JTable table, Integer[] preferredSizes) {
		for (int i = 0; i < preferredSizes.length; i++) {
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setPreferredWidth(preferredSizes[i]);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == wrbCancel) {
			cancelWorker();
			if (LPMain.getTextRespectUISPr("button.ok").equals(wrbCancel.getText())) {
				dispose();
			}
		}
	}
	
	protected void actionWorkerDoneByError(Throwable e) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				cancelWorker();
				wrbCancel.setText(LPMain.getTextRespectUISPr("button.ok"));
				// TODO exception handling
			}
		});
	}

	public class GeodatenAbfrageWorker extends SwingWorker<Void, GeocodeResult> {
		private Map<Integer, ProcessingState> hmStati;
		private List<GeodatenDto> creationList;

		public GeodatenAbfrageWorker(List<PartnerDto> toRequest) {
			initStates(toRequest);
			creationList = new ArrayList<GeodatenDto>();
		}
		
		private void initStates(List<PartnerDto> toRequest) {
			hmStati = new HashMap<Integer, ProcessingState>();
			for (PartnerDto partner : toRequest) {
				hmStati.put(partner.getIId(), new FullAddressState(partner));
			}
		}

		@Override
		protected Void doInBackground() throws Exception {
			doRequest(createApiRequests());
			return null;
		}
		
		public void cancel() {
			finder.cancel();
			cancel(true);
		}
		
		private List<GeocodeApiRequest> createApiRequests() {
			List<GeocodeApiRequest> requests = new ArrayList<GeocodeApiRequest>();
			for (ProcessingState state : hmStati.values()) {
				requests.add(new GeocodeApiRequest(state.getId(), state.getSearchString()));
			}
			return requests;
		}

		private void doRequest(List<GeocodeApiRequest> requests) {
			if (requests.isEmpty()) {
				return;
			}
			
			finder.find(requests, new IGeodatenabfrageCallback() {
				@Override
				public void processResult(GeocodeResult result) {
					ProcessingState state = hmStati.get(result.getId()).getNewState(result.getStatus());
					
					if (state.isDone()) {
						hmStati.remove(result.getId());
					} else {
						hmStati.put(result.getId(), state);
					}
					
					publish(result);
				}
				
				@Override
				public void doneByError(Throwable e) {
					actionWorkerDoneByError(e);
				}
				
				@Override
				public void done() {
				}
			});
//			for (GeocodeApiRequest request : requests) {
//				if (isCancelled()) break;
//				
//				GeocodeResult result = request(request);
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					continue;
//				}
//				if (result == null) continue;
//				
//				ProcessingState state = hmStati.get(request.getId()).getNewState(result.getStatus());
//				
//				if (state.isDone()) {
//					hmStati.remove(request.getId());
//				} else {
//					hmStati.put(request.getId(), state);
//				}
//				
//				publish(result);
//			}
//			
			requests = createApiRequests();
			doRequest(requests);
		}
		
		//		private GeocodeResult request(GeocodeApiRequest request) {
//			try {
//				return finder.find(request);
//			} catch (InterruptedException | ExecutionException e) {
//				log.error("InterruptedException | ExecutionException", e);
//			}
//			return null;
//		}
//		
		@Override
		protected void process(List<GeocodeResult> results) {
			for (GeocodeResult result : results) {
				if (GeocodeResultStatusEntry.OK.equals(result.getStatus()))
					createGeodaten(result.getGeodatenDto());
				
				getHmData().get(result.getId()).setResult(result);
			}
			tableModel.fireTableDataChanged();
			progressBar.setValue(progressBar.getMaximum() - hmStati.size());
		}
		
		private void createGeodaten(GeodatenDto geodatenDto) {
			try {
				DelegateFactory.getInstance().getPartnerDelegate().createGeodaten(geodatenDto);
				geodatenDtoCallback.onResult(geodatenDto);
			} catch (ExceptionLP e) {
				log.error("ExceptionLP", e);
			}
		}
		
//		private boolean createGeodaten() {
//			try {
//				DelegateFactory.getInstance().getPartnerDelegate().createGeodaten(creationList);
//				return true;
//			} catch (ExceptionLP e) {
//				log.error("ExceptionLP", e);
//			}
//			return false;
//		}
		
		@Override
		protected void done() {
			wrbCancel.setText(LPMain.getTextRespectUISPr("button.ok"));
		}
	}

	
	private interface ProcessingState {
		ProcessingState getNewState(GeocodeResultStatusEntry status);
		boolean isSuccessful();
		Integer getId();
		String getSearchString();
		boolean isDone();
	}
	
	private class FullAddressState implements ProcessingState {
		private PartnerDto partnerDto;
		public FullAddressState(PartnerDto partnerDto) {
			this.partnerDto = partnerDto;
		}
		public ProcessingState getNewState(GeocodeResultStatusEntry status) {
			if (GeocodeResultStatusEntry.OK.equals(status))
				return new FoundState(partnerDto);
			
			if (GeocodeResultStatusEntry.ZERO_RESULTS.equals(status)) 
				return new JustCityState(partnerDto);
			
			return this;
		}
		
		@Override
		public String getSearchString() {
			return partnerDto.formatAdresse();
		}
		@Override
		public Integer getId() {
			return partnerDto.getIId();
		}
		@Override
		public boolean isDone() {
			return false;
		}
		@Override
		public boolean isSuccessful() {
			return false;
		}
	}
	
	private class JustCityState implements ProcessingState {
		private PartnerDto partnerDto;
		public JustCityState(PartnerDto partnerDto) {
			this.partnerDto = partnerDto;
		}
		public ProcessingState getNewState(GeocodeResultStatusEntry status) {
			if (GeocodeResultStatusEntry.OK.equals(status))
				return new FoundState(partnerDto);
			
			if (GeocodeResultStatusEntry.ZERO_RESULTS.equals(status)) 
				return new NotFoundState(partnerDto);

			return this;
		}
		
		@Override
		public String getSearchString() {
			return partnerDto.formatLKZPLZOrt();
		}
		@Override
		public Integer getId() {
			return partnerDto.getIId();
		}
		@Override
		public boolean isDone() {
			return false;
		}
		@Override
		public boolean isSuccessful() {
			return false;
		}
	}

	private class NotFoundState implements ProcessingState {
		private PartnerDto partnerDto;
		public NotFoundState(PartnerDto partnerDto) {
			this.partnerDto = partnerDto;
		}
		
		@Override
		public ProcessingState getNewState(GeocodeResultStatusEntry status) {
			return this;
		}

		@Override
		public Integer getId() {
			return partnerDto.getIId();
		}

		@Override
		public String getSearchString() {
			return null;
		}
		@Override
		public boolean isDone() {
			return true;
		}
		@Override
		public boolean isSuccessful() {
			return false;
		}
	}
	
	private class FoundState implements ProcessingState {
		private PartnerDto partnerDto;
		public FoundState(PartnerDto partnerDto) {
			this.partnerDto = partnerDto;
		}
		
		@Override
		public ProcessingState getNewState(GeocodeResultStatusEntry status) {
			return this;
		}
		@Override
		public Integer getId() {
			return partnerDto.getIId();
		}
		@Override
		public String getSearchString() {
			return null;
		}
		@Override
		public boolean isDone() {
			return true;
		}
		@Override
		public boolean isSuccessful() {
			return true;
		}
	}
	
	protected class GeodatenResultsTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 7760745215512487490L;
		private String[] columnNames = new String[] {
				LPMain.getTextRespectUISPr("lp.name"),
				LPMain.getTextRespectUISPr("part.kunde.lieferstatistik.adresse"),
				LPMain.getTextRespectUISPr("lp.status"),
		};
		private Class[] columnClasses = new Class[] {String.class, String.class, String.class};

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Class<?> getColumnClass(int col) {
			return columnClasses[col];
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		@Override
		public int getRowCount() {
			return getData().size();
		}

		@Override
		public Object getValueAt(int row, int column) {
			if (row >= getData().size()) return null;
			
			PartnerDto partnerDto = getData().get(row);
			if (0 == column) return partnerDto.formatFixName1Name2();
			if (1 == column) return partnerDto.formatAdresse();
			
			RequestData requestData = getHmData().get(partnerDto.getIId());
			GeocodeResult result = requestData.getResult();
			if (result == null) return null;
			
			if (2 == column) return result.getStatus().name();
			
			return null;
		}
		
	}
}
