package com.lp.client.finanz;

import java.math.BigDecimal;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.BuchungDetailSummary;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

public class PanelQueryBuchungDetail extends PanelQuery {

	private static final long serialVersionUID = 4837100810599902533L;

	public PanelQueryBuchungDetail(QueryType[] typesI, FilterKriterium[] filtersI, int idUsecaseI,
			String[] aWhichButtonIUseI, InternalFrame internalFrameI, String add2TitleI,
			boolean refreshWhenYouAreSelectedI) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI, add2TitleI, refreshWhenYouAreSelectedI);
	}

	private BuchungDetailSummary getSummary() {
		return (BuchungDetailSummary)getResultData() ;
	}

	public boolean isAzkSummeFehlerhaft() {
		return getSummary() == null ? false : getSummary().isAzkFehlerhafteSumme() ;
	}

	public BigDecimal getSaldo() {
		return getSummary().getSaldo();
	}
}
