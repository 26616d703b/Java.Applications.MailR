package com.ampersand.mailr.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.ampersand.lcu.gui.color.ColorPalette;
import com.ampersand.lcu.gui.component.button.HighlightButton;
import com.ampersand.lcu.gui.component.field.TextValidationField;
import com.ampersand.lcu.gui.component.panel.image.ImagePane;
import com.ampersand.lcu.gui.font.FontManager;
import com.ampersand.lcu.mail.MailSender;
import com.ampersand.lcu.validator.Validator;
import com.ampersand.mailr.MaileR;
import com.ampersand.mailr.observers.IdentifiersObservable;
import com.ampersand.mailr.observers.IdentifiersObserver;

public class LoginPanel extends JPanel implements ActionListener, KeyListener, IdentifiersObservable {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = 7062631006430756447L;

	private final MaileR m_parent;
	private MailSender m_mail_sender;
	private ArrayList<IdentifiersObserver> m_observers;

	// GUI

	private ImagePane m_image_pane;

	private JPanel m_content_panel;

	private JPanel m_labels_panel;
	private JLabel m_mail_label;
	private JLabel m_password_label;

	private JPanel m_fields_panel;
	private TextValidationField m_mail_field;
	private TextValidationField m_password_field;

	private HighlightButton m_connect;

	/*
	 * Methods
	 */

	// CONSTRUCTOR

	public LoginPanel(MaileR parent) {

		setLayout(new BorderLayout());

		m_parent = parent;
		m_observers = new ArrayList<IdentifiersObserver>();

		initContent();
	}

	// ACCESSORS and MUTATORS

	public MailSender getMailSender() {

		return m_mail_sender;
	}

	// INITIALIZATIONS

	public void initContent() {

		// CENTER

		m_image_pane = new ImagePane(new ImageIcon(getClass().getResource("res/images/login.png")).getImage());
		m_image_pane.setLayout(new BorderLayout());

		m_mail_label = new JLabel("Adresse mail");
		m_mail_label.setFont(FontManager.CENTURY_GOTHIC_BOLD_16);
		m_mail_label.setPreferredSize(new Dimension(120, 50));

		m_password_label = new JLabel("Mot de passe");
		m_password_label.setFont(FontManager.CENTURY_GOTHIC_BOLD_16);
		m_password_label.setPreferredSize(new Dimension(120, 50));

		m_labels_panel = new JPanel(new GridLayout(3, 1, 0, 5));
		m_labels_panel.add(m_mail_label);
		m_labels_panel.add(m_password_label);
		m_labels_panel.setOpaque(false);

		m_mail_field = new TextValidationField(Validator.E_MAIL_ADDRESS);
		m_mail_field.setBorder(new LineBorder(ColorPalette.BLACK, 2));
		m_mail_field.setFont(FontManager.CENTURY_GOTHIC_16);

		m_password_field = new TextValidationField(Validator.WEAK_PASSWORD);
		m_password_field.addKeyListener(this);
		m_password_field.setBorder(new LineBorder(ColorPalette.BLACK, 2));
		m_password_field.setFont(FontManager.CENTURY_GOTHIC_16);

		m_connect = new HighlightButton(new ImageIcon(getClass().getResource("res/icons/action/login-32.png")));
		m_connect.addActionListener(this);
		m_connect.setPreferredSize(new Dimension(getWidth(), 50));

		m_fields_panel = new JPanel(new GridLayout(3, 1, 0, 5));
		m_fields_panel.add(m_mail_field);
		m_fields_panel.add(m_password_field);
		m_fields_panel.add(m_connect);
		m_fields_panel.setOpaque(false);

		m_content_panel = new JPanel(new BorderLayout());
		m_content_panel.add(m_labels_panel, BorderLayout.WEST);
		m_content_panel.add(m_fields_panel, BorderLayout.CENTER);
		m_content_panel.setOpaque(false);

		final JLabel[] separators = new JLabel[4];

		for (int i = 0; i < separators.length; i++) {

			separators[i] = new JLabel();
		}

		separators[0].setPreferredSize(new Dimension(getWidth(), 320));
		separators[1].setPreferredSize(new Dimension(getWidth(), 170));
		separators[2].setPreferredSize(new Dimension(250, getHeight()));
		separators[3].setPreferredSize(new Dimension(120, getHeight()));

		m_image_pane.add(separators[0], BorderLayout.NORTH);
		m_image_pane.add(separators[1], BorderLayout.SOUTH);
		m_image_pane.add(separators[2], BorderLayout.EAST);
		m_image_pane.add(separators[3], BorderLayout.WEST);
		m_image_pane.add(m_content_panel, BorderLayout.CENTER);

		add(m_image_pane, BorderLayout.CENTER);

		// SOUTH

	}

	// RE-IMPLEMENTED METHODS

	// ACTION LISTENER

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource().equals(m_connect)) {

			if (m_mail_field.isEmpty() || m_password_field.isEmpty()) {

				JOptionPane.showMessageDialog(this,
						"Vérifier que tous les champs sont bien remplis avant de pouvoir continuer.",
						"Champ(s) vide(s)", JOptionPane.WARNING_MESSAGE,
						new ImageIcon(getClass().getResource("res/icons/menu/error.png")));
			} else {

				if (!m_mail_field.inputIsValid()) {

					JOptionPane.showMessageDialog(this, "Veuillez entrer une adresse mail valide.",
							"Adresse mail invalide", JOptionPane.WARNING_MESSAGE,
							new ImageIcon(getClass().getResource("res/icons/menu/error.png")));
				} else {

					if (!m_password_field.inputIsValid()) {

						JOptionPane.showMessageDialog(this, "Veuillez entrer un motde passe valide.",
								"Mot de passe invalide", JOptionPane.WARNING_MESSAGE,
								new ImageIcon(getClass().getResource("res/icons/menu/error.png")));
					} else {

						m_mail_sender = new MailSender(m_mail_field.getText(), m_password_field.getText());

						if (m_mail_sender.connect()) {

							m_parent.goToMainPane(m_mail_field.getText(), m_password_field.getText());

							notifyObservers(m_mail_sender);
						} else {

							m_parent.goToInformationPane(InformationPanel.AUTHENTIFICATION_FAILURE);
						}
					}
				}
			}
		}
	}

	// KEY LISTENER

	@Override
	public void keyPressed(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.VK_ENTER) {

			connect();
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}

	// OBSERVABLE

	@Override
	public void addObserver(IdentifiersObserver observer) {

		m_observers.add(observer);
	}

	@Override
	public void notifyObservers(MailSender mail_sender) {

		for (final IdentifiersObserver observer : m_observers) {

			observer.update(mail_sender);
		}
	}

	@Override
	public void removeObservers() {

		m_observers = new ArrayList<IdentifiersObserver>();
	}

	// IMPLEMENTED METHODS

	// ----------------------------------------------------[ C
	// ]----------------------------------------------------//

	public void connect() {

		actionPerformed(new ActionEvent(m_connect, 0, null));
	}

	// ----------------------------------------------------[ G
	// ]----------------------------------------------------//

	public String getMailAddress() {

		return m_mail_field.getText();
	}

	public String getPassword() {

		return m_password_field.getText();
	}

	// ----------------------------------------------------[ S
	// ]----------------------------------------------------//

	public void setMailAddress(String address) {

		m_mail_field.setText(address);
	}

	public void setMailAddressLocked(boolean locked) {

		m_mail_field.setEnabled(!locked);
	}

	public void setPassword(String password) {

		m_password_field.setText(password);
	}
}
