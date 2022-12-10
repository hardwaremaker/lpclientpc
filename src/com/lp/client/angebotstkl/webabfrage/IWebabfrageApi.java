package com.lp.client.angebotstkl.webabfrage;

import java.util.List;
import java.util.Map;

import com.lp.client.frame.ExceptionLP;

public interface IWebabfrageApi {

	public void find(Map<Integer,List<String>> idSearchStringMap, Integer requestType) throws ExceptionLP; //mit Callback
	
	public void cancelRequest(Integer requestType);
}
