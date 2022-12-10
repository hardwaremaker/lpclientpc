package com.lp.client.frame.delegate;

import com.lp.server.bestellung.service.JobDetailsWEJournalDto;
import com.lp.server.bestellung.service.JobWEJournalFac;

public class AutoJobWEJournalDelegate extends AutoJobDelegate<JobDetailsWEJournalDto, JobWEJournalFac> {

	public AutoJobWEJournalDelegate() {
		super(JobWEJournalFac.class);
	}

}
