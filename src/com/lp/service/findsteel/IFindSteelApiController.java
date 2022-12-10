package com.lp.service.findsteel;

import com.lp.client.frame.ExceptionLP;
import com.lp.service.findsteel.schema.FindSteelApiSearchResponse;

public interface IFindSteelApiController {

	boolean logon(Integer lieferantId) throws ExceptionLP;
	boolean logout(Integer lieferantId) throws ExceptionLP;
	FindSteelApiSearchResponse search(Integer lieferantId, 
			String searchValue, int pageNumber, int pageSize) throws ExceptionLP;
}
