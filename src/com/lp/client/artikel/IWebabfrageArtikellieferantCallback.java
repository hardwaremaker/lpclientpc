package com.lp.client.artikel;

import com.lp.server.artikel.service.WebabfrageArtikellieferantResult;

public interface IWebabfrageArtikellieferantCallback {

	void done(WebabfrageArtikellieferantResult result);

	void doneExc(Throwable e);
}
