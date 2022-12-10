package com.lp.client.frame.delegate;

import com.lp.server.system.service.JobDetailsKpiReportDto;
import com.lp.server.system.service.JobKpiReportFac;

public class AutoJobKpiReportDelegate extends AutoJobDelegate<JobDetailsKpiReportDto, JobKpiReportFac> {

	public AutoJobKpiReportDelegate() {
		super(JobKpiReportFac.class);
	}
}
