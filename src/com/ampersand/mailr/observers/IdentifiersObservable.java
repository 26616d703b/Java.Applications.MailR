package com.ampersand.mailr.observers;

import com.ampersand.lcu.mail.MailSender;

public interface IdentifiersObservable {

	public void addObserver(IdentifiersObserver observer);

	public void notifyObservers(MailSender mail_sender);

	public void removeObservers();
}
