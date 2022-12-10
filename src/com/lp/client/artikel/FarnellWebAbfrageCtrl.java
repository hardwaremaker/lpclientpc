package com.lp.client.artikel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.artikel.service.WebabfrageArtikellieferantProperties;
import com.lp.server.artikel.service.WebabfrageArtikellieferantResult;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.LieferantId;

public class FarnellWebAbfrageCtrl implements IWebabfrageArtikellieferantCtrl {
	private IWebabfrageArtikellieferantCallback callback;

	public FarnellWebAbfrageCtrl() {
	}

	public FarnellWebAbfrageCtrl(IWebabfrageArtikellieferantCallback callback) {
		this();
		this.callback = callback;
	}

	@Override
	public WebabfrageArtikellieferantResult request(ArtikelId artikelId, LieferantId lieferantId) throws ExceptionLP, Throwable {
		if (callback != null) {
			return requestImpl(artikelId, lieferantId, callback);
		} else {
			return requestImpl(artikelId, lieferantId);
		}
	}

	@Override
	public void request(ArtikelId artikelId, LieferantId lieferantId, IWebabfrageArtikellieferantCallback callback) {
		requestImpl(artikelId, lieferantId, callback);
	}

	private WebabfrageArtikellieferantResult requestImpl(ArtikelId artikelId, LieferantId lieferantId, IWebabfrageArtikellieferantCallback callback) {
		try {
			WebabfrageArtikellieferantResult result = requestImpl(artikelId, lieferantId);
			callback.done(result);
			return result;
		} catch (ExceptionLP e) {
			callback.doneExc(e);
		} catch (Throwable e) {
			callback.doneExc(e);
		}
		return null;
	}

	private WebabfrageArtikellieferantResult requestImpl(ArtikelId artikelId, LieferantId lieferantId) throws ExceptionLP, Throwable {
		WebabfrageArtikellieferantProperties webabfrageProperties = new WebabfrageArtikellieferantProperties(artikelId, lieferantId);
		webabfrageProperties.setUpdate(true);
		return DelegateFactory.getInstance().getArtikelDelegate().aktualisiereArtikellieferantByWebabfrage(webabfrageProperties);
	}
}
