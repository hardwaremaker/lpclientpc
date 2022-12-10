package com.lp.client.finanz;

import java.math.BigDecimal;

public interface IMwstListener {

	public void updateMwstBetrag(BigDecimal mwstBetrag);
	
	public void updateMwstSatz(Integer mwstSatzBezIId);
	
	public void enableMwstSatz(Boolean value);
}
