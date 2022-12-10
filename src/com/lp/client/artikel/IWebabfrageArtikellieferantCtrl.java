package com.lp.client.artikel;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.artikel.service.WebabfrageArtikellieferantResult;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.LieferantId;

public interface IWebabfrageArtikellieferantCtrl {
	
	WebabfrageArtikellieferantResult request(ArtikelId artikelId, LieferantId lieferantId) throws ExceptionLP, Throwable;
	
	void request(ArtikelId artikelId, LieferantId lieferantId, IWebabfrageArtikellieferantCallback callback);
}
