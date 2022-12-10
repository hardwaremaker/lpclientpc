package com.lp.service.findchips;

public interface IFindchipsResponseCallback {
	void onSuccess(FindchipsApiRequest request, FindchipsApiResponse response) ;
	void onError(FindchipsApiRequest request) ;
	void onException(FindchipsApiRequest request, Exception e) ;
}
