package com.ampersand.mailr.observers;

import com.ampersand.lcu.mail.MailSender;

public interface IdentifiersObserver {

	public void update(MailSender mail_sender);
}
