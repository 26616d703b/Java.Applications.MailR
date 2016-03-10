package com.ampersand.mailr.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.ampersand.lcu.gui.component.button.HighlightButton;
import com.ampersand.lcu.gui.component.panel.image.ImagePane;
import com.ampersand.lcu.mail.MailSender;
import com.ampersand.mailr.MaileR;
import com.ampersand.mailr.observers.IdentifiersObserver;

public class InformationPanel extends JPanel implements ActionListener, IdentifiersObserver {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = -314433187049310649L;

	private int m_current_state;
	private final MaileR m_parent;
	private MailSender m_mail_sender;

	// GUI

	private final ImagePane m_image_pane;
	private final HighlightButton m_return;

	public static final int AUTHENTIFICATION_FAILURE = 0;
	public static final int SENDING_FAILS = 1;
	public static final int SENDING_SUCCESSFUL = 2;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public InformationPanel(MaileR parent) {

		setLayout(new BorderLayout());

		m_parent = parent;

		m_image_pane = new ImagePane(
				new ImageIcon(getClass().getResource("res/images/authentification_failure.png")).getImage());

		add(m_image_pane, BorderLayout.CENTER);

		m_return = new HighlightButton(new ImageIcon(getClass().getResource("res/icons/action/back-32.png")));
		m_return.addActionListener(this);
	}

	// ACCESSORS and MUTATORS

	public int getCurrentState() {

		return m_current_state;
	}

	public void setCurrentState(int state) {

		m_current_state = state;

		String image_path = "res/images/";

		if (state == AUTHENTIFICATION_FAILURE) {

			image_path += "authentification_failure.png";

			add(m_return, BorderLayout.WEST);
		} else if (state == SENDING_FAILS) {

			image_path += "sending_fails.png";

			add(m_return, BorderLayout.WEST);
		} else if (state == SENDING_SUCCESSFUL) {

			image_path += "sending_successful.png";

			remove(m_return);
		}

		m_image_pane.setBackgroundImage(new ImageIcon(getClass().getResource(image_path)).getImage());
	}

	// RE-IMPLEMENTED METHODS

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource().equals(m_return)) {

			if (m_current_state == AUTHENTIFICATION_FAILURE) {

				m_parent.goToLoginPane();
			} else if (m_current_state == SENDING_FAILS) {

				if (m_mail_sender == null) {

					m_parent.goToLoginPane();
				} else {

					m_mail_sender.disconnect();

					m_parent.goToMainPane(m_mail_sender.getUserAddress(), m_mail_sender.getUserPassword());
				}
			}
		}
	}

	@Override
	public void update(MailSender mail_sender) {

		m_mail_sender = mail_sender;
	}
}
