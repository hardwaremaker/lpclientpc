package com.lp.client.stueckliste;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.pc.LPMain;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class PanelQueryFLRStuecklisteposition extends PanelQueryFLR {

	private static final long serialVersionUID = -6849586762698722442L;

	public PanelQueryFLRStuecklisteposition(FilterKriterium[] krit, InternalFrame internalFrame) throws Throwable {
		super(null, krit, QueryParameters.UC_ID_STUECKLISTEPOSITION,
				new String[] { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN }, internalFrame,
				LPMain.getTextRespectUISPr("lp.positionen"));
	}

//	@Override
//	protected JComponent getFirstFocusableComponent() throws Exception {
//		return getTable();
//	}
}
