package com.lp.client.frame.delegate;

import com.lp.server.fertigung.service.JobArbeitszeitstatusFac;
import com.lp.server.fertigung.service.JobDetailsArbeitszeitstatusDto;

public class AutoJobArbeitszeitstatusDelegate
		extends AutoJobDelegate<JobDetailsArbeitszeitstatusDto, JobArbeitszeitstatusFac> {

	public AutoJobArbeitszeitstatusDelegate() {
		super(JobArbeitszeitstatusFac.class);
	}

}
