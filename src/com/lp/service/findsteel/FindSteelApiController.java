package com.lp.service.findsteel;

import java.io.IOException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvCreatingCachingProvider;
import com.lp.client.util.logger.LpLogger;
import com.lp.service.findsteel.schema.FindSteelApiSearchResponse;
import com.lp.util.EJBExceptionLP;

public class FindSteelApiController implements IFindSteelApiController {
	private final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());
	
	private String sutUser = "gerold.pummer@heliumv.com";
	private String sutPassword = "!ghp.logp.66?";

	private IFindSteelApiService apiService;
	private SessionCache sessionCache;
	
	public FindSteelApiController() {
		this(new FindSteelApiService());
	}
	
	public FindSteelApiController(IFindSteelApiService apiService) {
		this.apiService = apiService;
		this.sessionCache = new SessionCache();
	}
	
	private IFindSteelApiService getApiService() {
		return apiService;
	}

	private SessionCache getSessionCache() {
		return sessionCache;
	}
	
	@Override
	public boolean logon(Integer lieferantId) throws ExceptionLP {
		myLogger.debug("logon for lieferantId='" + lieferantId + "'.");
		FindSteelSessionInfo sessionInfo = getSessionCache().getValueOfKey(lieferantId);
		return sessionInfo != null;
	}
	
	protected FindSteelSessionInfo logonImpl(Integer lieferantId) throws IOException {
		return getApiService().logon(sutUser, sutPassword);
	}
	
	public boolean logout(Integer lieferantId) throws ExceptionLP {
		myLogger.debug("logout for lieferantId='" + lieferantId + "'.");
		boolean removed = false;
		
		if(getSessionCache().containsKey(lieferantId)) {
			try {
				getApiService().logout(getSessionCache().getValueOfKey(lieferantId));				
			} catch(IOException e){
				myLogger.info("IOException on logout", e);
			}
			
			getSessionCache().remove(lieferantId);
			removed = true;
		}
		
		return removed;
	}

	public FindSteelApiSearchResponse search(Integer lieferantId, 
			String searchValue, int pageNumber, int pageSize) throws ExceptionLP {
		myLogger.debug("search for lieferantId='" + lieferantId + "', search='" 
			+ searchValue + "', page=" + pageNumber + ", size=" + pageSize + ".");
		FindSteelSessionInfo sessionInfo = getSessionCache().getValueOfKey(lieferantId);
		try {
			return getApiService().search(sessionInfo, searchValue, pageNumber, pageSize);			
		} catch(IOException e) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER_HTTP_POST_IO, e);
		}
	}
	
	private class SessionCache extends HvCreatingCachingProvider<Integer, FindSteelSessionInfo> {
		@Override
		protected FindSteelSessionInfo provideValue(Integer key, Integer transformedKey)
				throws ExceptionLP {
			try {
				return logonImpl(key);				
			} catch(IOException e) {
				throw new ExceptionLP(EJBExceptionLP.FEHLER_HTTP_POST_IO, e);
			}
		}
	}
}
