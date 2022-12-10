package com.lp.client.frame.delegate;

import com.lp.server.system.service.Job4VendingExportFac;
import com.lp.server.system.service.JobDetails4VendingExportDto;

public class AutoJob4VendingExportDelegate extends
		AutoJobDelegate<JobDetails4VendingExportDto, Job4VendingExportFac> {

	public AutoJob4VendingExportDelegate() {
		super(Job4VendingExportFac.class);
	}

}
