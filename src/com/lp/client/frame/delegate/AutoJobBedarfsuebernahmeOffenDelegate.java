package com.lp.client.frame.delegate;

import com.lp.server.fertigung.service.JobBedarfsuebernahmeOffeneFac;
import com.lp.server.fertigung.service.JobDetailsBedarfsuebernahmeOffeneDto;

public class AutoJobBedarfsuebernahmeOffenDelegate
		extends AutoJobDelegate<JobDetailsBedarfsuebernahmeOffeneDto, JobBedarfsuebernahmeOffeneFac> {

	public AutoJobBedarfsuebernahmeOffenDelegate() {
		super(JobBedarfsuebernahmeOffeneFac.class);
	}

}
