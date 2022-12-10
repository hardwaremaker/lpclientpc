package com.lp.client.auftrag;

import com.lp.client.auftrag.ReportAuftragOffene2.OffeneReportType;
import com.lp.client.frame.report.ReportClientConfigData;

public class ReportAuftragOffeneConfigData  extends ReportClientConfigData {
	
	private OffeneReportType offeneType;

	public ReportAuftragOffeneConfigData() {
		super(ReportConfigType.AUFTRAG_OFFENE);
	}
	
	public OffeneReportType getOffeneType() {
		return offeneType;
	}
	
	public void setOffeneType(OffeneReportType offeneType) {
		this.offeneType = offeneType;
	}

}
