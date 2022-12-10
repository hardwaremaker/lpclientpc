package com.lp.client.frame.delegate;

import com.lp.server.artikel.service.JobDetailsWebabfrageArtikellieferantDto;
import com.lp.server.artikel.service.JobDetailsWebabfrageArtikellieferantFac;

public class AutoJobWebabfrageArtikellieferantDelegate
		extends AutoJobDelegate<JobDetailsWebabfrageArtikellieferantDto, JobDetailsWebabfrageArtikellieferantFac> {

	public AutoJobWebabfrageArtikellieferantDelegate() {
		super(JobDetailsWebabfrageArtikellieferantFac.class);
	}

}
