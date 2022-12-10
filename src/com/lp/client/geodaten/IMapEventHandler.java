package com.lp.client.geodaten;

public interface IMapEventHandler {

	void onKundeClick(Integer id);
	void onInteressentClick(Integer id);
	void onLieferantClick(Integer id);
	void onPartnerClick(Integer id);
	void onPositionClick(String title);
}
