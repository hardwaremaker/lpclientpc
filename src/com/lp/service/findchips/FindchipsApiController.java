package com.lp.service.findchips;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class FindchipsApiController implements IFindchipsApi {
	private FindchipsConfig config ;

	protected FindchipsApiController() {
	}
	
	public FindchipsApiController(FindchipsConfig config) {
		this.config = config ;
	}	
	
	public FindchipsConfig getConfig() {
		return config;
	}

	public void setConfig(FindchipsConfig config) {
		this.config = config;
	}
	
	public FindchipsApiResponse findByPartnr(String partnr) throws InterruptedException, ExecutionException {
		return findByPartnr(new FindchipsApiRequest(partnr)) ;
	}
	
	public FindchipsApiResponse findByPartnr(FindchipsApiRequest request) throws InterruptedException, ExecutionException {
		FindRequestExecutor executor = new FindRequestExecutor(getConfig()) ;
		return executor.executeOne(request) ; 
	}
	
	public FindchipsApiResponse findByPartnr(FindchipsApiRequest request, 
			IFindchipsResponseCallback callback) throws InterruptedException, ExecutionException {
		FindRequestExecutor executor = new FindRequestExecutor(getConfig()) ;
		return executor.executeOne(request, callback) ; 
 	}

	public void findByPartnr(List<FindchipsApiRequest> requests,
			IFindchipsResponseCallback callback) throws InterruptedException {
		FindRequestExecutor executor = new FindRequestExecutor(getConfig()) ;
		executor.executeAll(requests, callback);
		executor.awaitTermination();
	}
	
	public FindRequestExecutor findByPartnrAsynch(List<FindchipsApiRequest> requests,
			IFindchipsResponseCallback callback) throws InterruptedException {
		FindRequestExecutor executor = new FindRequestExecutor(getConfig()) ;
		executor.executeAll(requests, callback);
		return executor ;
	}
	
	public void cancel(FindRequestExecutor executor) {
		executor.cancel(); 
	}

	public void awaitTermination(FindRequestExecutor executor) {
		executor.awaitTermination(); 
	}
	
//	public void addFindByPartnrAsynch(FindRequestExecutor executor, List<FindchipsApiRequest> requests,
//			IFindchipsResponseCallback callback) throws InterruptedException {
//		executor.executeAll(requests, callback);
//	}	
}
