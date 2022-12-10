
package com.lp.client.frame.delegate;

import com.lp.server.system.service.AutoLumiquoteFac;
import com.lp.server.system.service.JobDetailsLumiquoteDto;

public class AutoJobLumiquoteDelegate extends AutoJobDelegate<JobDetailsLumiquoteDto, AutoLumiquoteFac> {

	public AutoJobLumiquoteDelegate() {
		super(AutoLumiquoteFac.class);
	}

}
