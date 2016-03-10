package com.ampersand.mailr;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {

			final MaileR mailr = new MaileR();
			mailr.setVisible(true);
		});
	}
}
