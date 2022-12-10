package com.lp.client.stueckliste;

import com.lp.client.frame.report.ReportClientConfigData;
import com.lp.client.stueckliste.ReportStueckliste.StklReportType;

public class ReportStklConfigData extends ReportClientConfigData {
	
	private StklReportType stklType;

	public ReportStklConfigData() {
		super(ReportConfigType.STKL);
	}
	
	public StklReportType getStklType() {
		return stklType;
	}
	
	public void setStklType(StklReportType stklType) {
		this.stklType = stklType;
	}

}
