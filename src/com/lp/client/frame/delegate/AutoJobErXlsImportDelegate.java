package com.lp.client.frame.delegate;

import com.lp.server.eingangsrechnung.service.JobDetailsErImportFac;
import com.lp.server.system.service.JobDetailsErImportDto;

public class AutoJobErXlsImportDelegate extends AutoJobDelegate<JobDetailsErImportDto, JobDetailsErImportFac> {

	public AutoJobErXlsImportDelegate() {
		super(JobDetailsErImportFac.class);
	}

}
