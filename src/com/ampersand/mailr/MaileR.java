package com.ampersand.mailr;

import java.awt.CardLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import com.ampersand.lcu.validator.Validator;
import com.ampersand.mailr.panels.InformationPanel;
import com.ampersand.mailr.panels.LoginPanel;
import com.ampersand.mailr.panels.MainPanel;

public class MaileR extends JFrame {

	/*
	 * Attributes:
	 */
	private static final long serialVersionUID = -5805075407245778967L;

	private final MaileR m_instance;

	private FileListener m_file_listener;
	private EditionListener m_edition_listener;
	private HelpListener m_help_listener;

	// GUI

	private JMenuBar m_menu_bar;

	private JMenu m_file_menu;
	private JMenuItem m_disconnect;
	private JMenuItem m_open;
	private JMenuItem m_save;
	private JMenuItem m_save_as;
	private JMenuItem m_print;
	private JMenuItem m_exit;

	private JMenu m_edition_menu;
	private JMenuItem m_cancel;

	private JMenu m_help_menu;
	private JMenuItem m_about;

	private CardLayout m_card_layout;

	private LoginPanel m_login_pane;
	private MainPanel m_main_pane;
	private InformationPanel m_information_pane;

	/*
	 * Methods:
	 */

	// CONSTRUCTOR

	public MaileR() {

		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final ClassNotFoundException e) {

			e.printStackTrace();
		} catch (final InstantiationException e) {

			e.printStackTrace();
		} catch (final IllegalAccessException e) {

			e.printStackTrace();
		} catch (final UnsupportedLookAndFeelException e) {

			e.printStackTrace();
		}

		m_instance = this;

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent event) {
			}

			@Override
			public void windowIconified(WindowEvent event) {
			}

			@Override
			public void windowDeiconified(WindowEvent event) {
			}

			@Override
			public void windowDeactivated(WindowEvent event) {
			}

			@Override
			public void windowClosing(WindowEvent event) {
			}

			@Override
			public void windowClosed(WindowEvent event) {

				if (m_login_pane.getMailSender() != null) {

					m_login_pane.getMailSender().disconnect();
				}
			}

			@Override
			public void windowActivated(WindowEvent event) {
			}
		});

		// Window properties
		setSize(800, 650);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("res/icons/new_post-48.png")));
		setTitle("Mailer");
		setLocationRelativeTo(null);

		initMenu();
		initContent();
	}

	// INITIALIZATIONS:

	public void initMenu() {

		// FILE

		m_file_listener = new FileListener();

		m_disconnect = new JMenuItem("Se déconnecter",
				new ImageIcon(getClass().getResource("res/icons/menu/logout-32.png")));
		m_disconnect.addActionListener(m_file_listener);
		m_disconnect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
		m_disconnect.setMnemonic('d');

		m_open = new JMenuItem("Ouvrir", new ImageIcon(getClass().getResource("res/icons/menu/folder-32.png")));
		m_open.addActionListener(m_file_listener);
		m_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		m_open.setMnemonic('o');

		m_save = new JMenuItem("Enregistrer", new ImageIcon(getClass().getResource("res/icons/menu/save-32.png")));
		m_save.addActionListener(m_file_listener);
		m_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		m_save.setMnemonic('r');

		m_save_as = new JMenuItem("Enregistrer sous",
				new ImageIcon(getClass().getResource("res/icons/menu/save_as-32.png")));
		m_save_as.addActionListener(m_file_listener);
		m_save_as.setMnemonic('s');

		m_print = new JMenuItem("Imprimer", new ImageIcon(getClass().getResource("res/icons/menu/print-32.png")));
		m_print.addActionListener(m_file_listener);
		m_print.setMnemonic('i');

		m_exit = new JMenuItem("Quitter", new ImageIcon(getClass().getResource("res/icons/menu/switch_off-32.png")));
		m_exit.addActionListener(m_file_listener);
		m_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
		m_exit.setMnemonic('q');

		m_file_menu = new JMenu("Fichier");
		m_file_menu.add(m_disconnect);
		m_file_menu.addSeparator();
		m_file_menu.add(m_open);
		m_file_menu.add(m_save);
		m_file_menu.add(m_save_as);
		m_file_menu.addSeparator();
		m_file_menu.add(m_print);
		m_file_menu.addSeparator();
		m_file_menu.add(m_exit);
		m_file_menu.setMnemonic('f');

		// EDITION

		m_edition_listener = new EditionListener();

		m_cancel = new JMenuItem("Annuler", new ImageIcon(getClass().getResource("res/icons/menu/back-32.png")));
		m_cancel.addActionListener(m_edition_listener);
		m_cancel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		m_cancel.setMnemonic('q');

		m_edition_menu = new JMenu("Edition");
		m_edition_menu.add(m_cancel);
		m_edition_menu.setMnemonic('e');

		// HELP

		m_help_listener = new HelpListener();

		m_about = new JMenuItem("À propos", new ImageIcon(getClass().getResource("res/icons/menu/info-32.png")));
		m_about.addActionListener(m_help_listener);
		m_about.setAccelerator(KeyStroke.getKeyStroke("F1"));
		m_about.setMnemonic('p');

		m_help_menu = new JMenu("?");
		m_help_menu.add(m_about);
		m_help_menu.setMnemonic('?');

		// Menu

		m_menu_bar = new JMenuBar();
		m_menu_bar.add(m_file_menu);
		m_menu_bar.add(m_edition_menu);
		m_menu_bar.add(m_help_menu);
	}

	public void initContent() {

		m_main_pane = new MainPanel(this);
		m_information_pane = new InformationPanel(this);

		m_login_pane = new LoginPanel(this);
		m_login_pane.addObserver(m_main_pane);
		m_login_pane.addObserver(m_information_pane);

		m_card_layout = new CardLayout();

		setLayout(m_card_layout);

		add(m_login_pane, "login_pane");
		add(m_main_pane, "main_pane");
		add(m_information_pane, "information_pane");
	}

	public void initContent(String recipient_mail_address) {

		m_main_pane = new MainPanel(this);
		m_information_pane = new InformationPanel(this);

		m_login_pane = new LoginPanel(this);
		m_login_pane.addObserver(m_main_pane);
		m_login_pane.addObserver(m_information_pane);

		m_card_layout = new CardLayout();

		setLayout(m_card_layout);

		add(m_login_pane, "login_pane");
		add(m_main_pane, "main_pane");
		add(m_information_pane, "information_pane");
	}

	// LISTENERS

	public class FileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {

			if (event.getSource().equals(m_disconnect)) {

				m_login_pane.getMailSender().disconnect();

				goToLoginPane();
			} else if (event.getSource().equals(m_open)) {

				m_main_pane.open();
			} else if (event.getSource().equals(m_save)) {

				m_main_pane.save();
			} else if (event.getSource().equals(m_save_as)) {

				m_main_pane.saveAs();
			} else if (event.getSource().equals(m_print)) {

				m_main_pane.print();
			} else if (event.getSource().equals(m_exit)) {

				System.exit(0);
			}
		}
	}

	public class EditionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {

			if (event.getSource().equals(m_cancel)) {

			}
		}
	}

	public class HelpListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {

			if (event.getSource().equals(m_about)) {

				JOptionPane.showMessageDialog(m_instance, "&Mailer est un projet réalisé pour le fun!", "À propos",
						JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(getClass().getResource("res/icons/new_post-48.png")));
			}
		}
	}

	// IMPLEMENTED METHODS

	// ----------------------------------------------------[ G
	// ]----------------------------------------------------//

	public void goToLoginPane() {

		setJMenuBar(null);

		m_login_pane.setMailAddress("");
		m_login_pane.setMailAddressLocked(false);
		m_login_pane.setPassword("");

		m_card_layout.show(getContentPane(), "login_pane");
	}

	public void goToLoginPane(String user_mail_address) {

		setJMenuBar(null);

		if (Validator.E_MAIL_ADDRESS.isValid(user_mail_address)) {

			m_login_pane.setMailAddress(user_mail_address);
			m_login_pane.setMailAddressLocked(true);
		} else {

			m_login_pane.setMailAddress("");
			m_login_pane.setMailAddressLocked(false);
		}

		m_login_pane.setPassword("");

		m_card_layout.show(getContentPane(), "login_pane");
	}

	public void goToMainPane(String transmitter_mail_address, String transmitter_password) {

		m_card_layout.show(getContentPane(), "main_pane");

		m_login_pane.setMailAddress(transmitter_mail_address);
		m_login_pane.setPassword(transmitter_password);

		if (m_login_pane.getMailSender() == null || !m_login_pane.getMailSender().isConnected()) {

			m_login_pane.connect();
		}

		setJMenuBar(m_menu_bar);

		m_main_pane.setFromAddress(transmitter_mail_address);
		m_main_pane.setFromAddressLocked(true);
	}

	public void goToMainPane(String transmitter_mail_address, String transmitter_password, String recipient_address) {

		m_card_layout.show(getContentPane(), "main_pane");

		m_login_pane.setMailAddress(transmitter_mail_address);
		m_login_pane.setPassword(transmitter_password);

		if (m_login_pane.getMailSender() == null || !m_login_pane.getMailSender().isConnected()) {

			m_login_pane.connect();
		}

		setJMenuBar(m_menu_bar);

		m_main_pane.setFromAddress(transmitter_mail_address);
		m_main_pane.setFromAddressLocked(true);

		m_main_pane.addRecipient(recipient_address);
	}

	public void goToMainPane(String transmitter_mail_address, String transmitter_password, String recipient_address,
			File attachment) {

		m_card_layout.show(getContentPane(), "main_pane");

		m_login_pane.setMailAddress(transmitter_mail_address);
		m_login_pane.setPassword(transmitter_password);

		if (m_login_pane.getMailSender() == null || !m_login_pane.getMailSender().isConnected()) {

			m_login_pane.connect();
		}

		setJMenuBar(m_menu_bar);

		m_main_pane.setFromAddress(transmitter_mail_address);
		m_main_pane.setFromAddressLocked(true);

		m_main_pane.addAttachment(attachment);
		m_main_pane.addRecipient(recipient_address);
	}

	public void goToMainPane(String transmitter_mail_address, String transmitter_password, String recipient_address,
			Vector<File> attachments) {

		m_card_layout.show(getContentPane(), "main_pane");

		m_login_pane.setMailAddress(transmitter_mail_address);
		m_login_pane.setPassword(transmitter_password);

		if (m_login_pane.getMailSender() == null || !m_login_pane.getMailSender().isConnected()) {

			m_login_pane.connect();
		}

		setJMenuBar(m_menu_bar);

		m_main_pane.setFromAddress(transmitter_mail_address);
		m_main_pane.setFromAddressLocked(true);

		m_main_pane.addAttachments(attachments);
		m_main_pane.addRecipient(recipient_address);
	}

	public void goToMainPane(String transmitter_mail_address, String transmitter_password,
			Vector<String> recipients_addresses) {

		m_card_layout.show(getContentPane(), "main_pane");

		m_login_pane.setMailAddress(transmitter_mail_address);
		m_login_pane.setPassword(transmitter_password);

		if (m_login_pane.getMailSender() == null || !m_login_pane.getMailSender().isConnected()) {

			m_login_pane.connect();
		}

		setJMenuBar(m_menu_bar);

		m_main_pane.setFromAddress(transmitter_mail_address);
		m_main_pane.setFromAddressLocked(true);

		m_main_pane.addRecipients(recipients_addresses);
	}

	public void goToMainPane(String transmitter_mail_address, String transmitter_password,
			Vector<String> recipients_addresses, File attachment) {

		m_card_layout.show(getContentPane(), "main_pane");

		m_login_pane.setMailAddress(transmitter_mail_address);
		m_login_pane.setPassword(transmitter_password);

		if (m_login_pane.getMailSender() == null || !m_login_pane.getMailSender().isConnected()) {

			m_login_pane.connect();
		}

		setJMenuBar(m_menu_bar);

		m_main_pane.setFromAddress(transmitter_mail_address);
		m_main_pane.setFromAddressLocked(true);

		m_main_pane.addAttachment(attachment);
		m_main_pane.addRecipients(recipients_addresses);
	}

	public void goToMainPane(String transmitter_mail_address, String transmitter_password,
			Vector<String> recipients_addresses, Vector<File> attachments) {

		m_card_layout.show(getContentPane(), "main_pane");

		m_login_pane.setMailAddress(transmitter_mail_address);
		m_login_pane.setPassword(transmitter_password);

		if (m_login_pane.getMailSender() == null || !m_login_pane.getMailSender().isConnected()) {

			m_login_pane.connect();
		}

		setJMenuBar(m_menu_bar);

		m_main_pane.setFromAddress(transmitter_mail_address);
		m_main_pane.setFromAddressLocked(true);

		m_main_pane.addAttachments(attachments);
		m_main_pane.addRecipients(recipients_addresses);
	}

	public void goToInformationPane(int state) {

		setJMenuBar(null);

		m_information_pane.setCurrentState(state);

		m_card_layout.show(getContentPane(), "information_pane");
	}
}