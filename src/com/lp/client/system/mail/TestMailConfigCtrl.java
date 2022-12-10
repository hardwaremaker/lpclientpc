package com.lp.client.system.mail;

import com.lp.server.system.mail.service.MailTestMessage;

public class TestMailConfigCtrl {
	
	private MailTestMessage lastMailTestMessage;

	public TestMailConfigCtrl() {
	}
	
	public void setLastMailTestMessage(MailTestMessage lastMailTestMessage) {
		this.lastMailTestMessage = lastMailTestMessage;
	}
	
	public MailTestMessage getLastMailTestMessage() {
		return lastMailTestMessage;
	}
	
	public boolean hasLastMailTestMessage() {
		return getLastMailTestMessage() != null;
	}
}
