package com.lp.service.findsteel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.lp.service.findsteel.schema.FindSteelApiSearchResponse;

public interface IFindSteelApiService {
	FindSteelSessionInfo logon(String username, String password) throws UnsupportedEncodingException, IOException;
	
	boolean logout(FindSteelSessionInfo sessionInfo) throws UnsupportedEncodingException, IOException;
	
	FindSteelApiSearchResponse search(FindSteelSessionInfo sessionInfo, String searchValue, 
			Integer pageNumber, Integer pageSize) throws UnsupportedEncodingException, IOException;
}
