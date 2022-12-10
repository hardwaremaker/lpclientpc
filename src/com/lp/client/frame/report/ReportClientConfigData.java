package com.lp.client.frame.report;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lp.client.auftrag.ReportAuftragOffeneConfigData;
import com.lp.client.stueckliste.ReportStklConfigData;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "reportConfigType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ReportStklConfigData.class, name = "STKL"),
    @JsonSubTypes.Type(value = ReportAuftragOffeneConfigData.class, name = "AUFTRAG_OFFENE")
})
public abstract class ReportClientConfigData {

	protected enum ReportConfigType {
		STKL,
		AUFTRAG_OFFENE;
	}
	
	private ReportConfigType reportConfigType;
	
	public ReportClientConfigData(ReportConfigType type) {
		setReportConfigType(type);
	}
	
	protected void setReportConfigType(ReportConfigType reportConfigType) {
		this.reportConfigType = reportConfigType;
	}
	
	public ReportConfigType getReportConfigType() {
		return reportConfigType;
	}
}
