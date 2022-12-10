package com.lp.service.google.geocoding;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.lp.service.google.geocoding.schema.GeocodeApiResponse;

public class GeocodeApiController implements IGeocodeApi {
	private GeocodeRequestConfig config;
	private GeocodeRequestExecutor executor;

	public GeocodeApiController(GeocodeRequestConfig config) {
		setConfig(config);
	}
	
	public GeocodeRequestConfig getConfig() {
		return config;
	}
	
	public void setConfig(GeocodeRequestConfig config) {
		this.config = config;
	}
	
	@Override
	public GeocodeApiResponse findByAddress(String address, IGeocodeResponseCallback callback) throws InterruptedException, ExecutionException {
		return findByAddress(new GeocodeApiRequest(address), callback);
	}
	
	@Override
	public GeocodeApiResponse findByAddress(GeocodeApiRequest request, IGeocodeResponseCallback callback) throws InterruptedException, ExecutionException {
		GeocodeRequestExecutor executor = new GeocodeRequestExecutor(getConfig());
		return executor.executeOne(request, callback);
	}
	
	@Override
	public void findByAddress(List<GeocodeApiRequest> requests, long timeout, IGeocodeResponseCallback callback) throws InterruptedException, ExecutionException {
		executor = new GeocodeRequestExecutor(getConfig());
		executor.executeAllSync(requests, timeout, callback);
	}
	
	@Override
	public void cancel() {
		if (executor != null) {
			executor.cancelSync();
		}
	}
	
	@Override
	public GeocodeRequestExecutor findByAddressAsynch(List<GeocodeApiRequest> requests, IGeocodeResponseCallback callback) throws InterruptedException {
		GeocodeRequestExecutor executor = new GeocodeRequestExecutor(getConfig());
		executor.executeAll(requests, callback);
		return executor;
	}
	
	@Override
	public void cancel(GeocodeRequestExecutor executor) {
		executor.cancel();
	}
	
	@Override
	public void awaitTermination(GeocodeRequestExecutor executor) {
		executor.awaitTermination();
	}
	
	@Override
	public GeocodeApiResponse findByAddress(String address) throws InterruptedException, ExecutionException {
		return findByAddress(new GeocodeApiRequest(address), new IGeocodeResponseCallback() {
			public void onSuccess(GeocodeApiRequest request, GeocodeApiResponse response) {
			}
			public void onException(GeocodeApiRequest request, Exception e) {
			}
			public void onError(GeocodeApiRequest request) {
			}
		});
	}
	
	@Override
	public GeocodeApiResponse findByAddress(GeocodeApiRequest request) throws InterruptedException, ExecutionException {
		return findByAddress(request, new IGeocodeResponseCallback() {
			public void onSuccess(GeocodeApiRequest request, GeocodeApiResponse response) {
			}
			public void onException(GeocodeApiRequest request, Exception e) {
			}
			public void onError(GeocodeApiRequest request) {
			}
		});
	}
}
