package com.lp.service.google.geocoding;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.lp.service.google.geocoding.schema.GeocodeApiResponse;

public interface IGeocodeApi {

	GeocodeApiResponse findByAddress(String address,
			IGeocodeResponseCallback callback) throws InterruptedException,
			ExecutionException;

	GeocodeApiResponse findByAddress(GeocodeApiRequest request,
			IGeocodeResponseCallback callback) throws InterruptedException,
			ExecutionException;

	GeocodeRequestExecutor findByAddressAsynch(
			List<GeocodeApiRequest> requests, IGeocodeResponseCallback callback)
			throws InterruptedException;

	void cancel(GeocodeRequestExecutor executor);

	void awaitTermination(GeocodeRequestExecutor executor);

	GeocodeApiResponse findByAddress(String address) throws InterruptedException, ExecutionException;

	GeocodeApiResponse findByAddress(GeocodeApiRequest request)
			throws InterruptedException, ExecutionException;

	void findByAddress(List<GeocodeApiRequest> requests, long timeout,
			IGeocodeResponseCallback callback) throws InterruptedException, ExecutionException;

	void cancel();

}
