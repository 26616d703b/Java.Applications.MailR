package com.ampersand.mailr.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import com.ampersand.lcu.gui.GUIFactory;
import com.ampersand.lcu.gui.color.ColorPalette;
import com.ampersand.lcu.gui.component.button.HighlightButton;
import com.ampersand.lcu.gui.component.field.TextValidationField;
import com.ampersand.lcu.gui.component.list.FilesList;
import com.ampersand.lcu.gui.component.list.MailsList;
import com.ampersand.lcu.gui.component.scrollbar.FlatScrollBarUI;
import com.ampersand.lcu.gui.font.FontManager;
import com.ampersand.lcu.io.IOUtils;
import com.ampersand.lcu.mail.MailSender;
import com.ampersand.lcu.validator.Validator;
import com.ampersand.mailr.MaileR;
import com.ampersand.mailr.observers.IdentifiersObserver;

public class MainPanel extends JPanel implements ActionListener, IdentifiersObserver {

	/*
	 * Attributes
	 */
	private static final long serialVersionUID = 6165048484268592563L;

	private boolean m_recorded;
	private String m_current_file_path;

	private final MaileR m_parent;
	private MailSender m_mail_sender;

	private final Vector<String> m_recipients;
	private final Vector<File> m_attachments;

	// GUI

	private final JToolBar m_tool_bar;

	private final JPanel m_header_panel;

	private final JPanel m_header_labels_panel;
	private final JLabel m_transmitter_label;
	private final JLabel m_recipient_label;
	private final JLabel m_message_object_label;
	private final JLabel m_attachments_label;

	private final JPanel m_header_fields_panel;
	private final TextValidationField m_transmitter_field;
	private final HighlightButton m_recipients_button;
	private final JTextField m_message_object_field;
	private final HighlightButton m_attachments_button;

	private final MailsList m_recipients_list;
	private final FilesList m_attachments_list;

	private final JPanel m_body_panel;
	private final JTextArea m_text_area;

	private final HighlightButton m_font_type;
	private final HighlightButton m_font_size;
	private final HighlightButton m_font_color;
	private final HighlightButton m_background_color;

	private final HighlightButton m_send;

	/*
	 * Methods
	 */

	public MainPanel(MaileR parent) {

		setLayout(new BorderLayout());

		m_parent = parent;

		m_recipients = new Vector<String>();
		m_attachments = new Vector<File>();

		m_recipients_list = new MailsList(m_recipients);
		m_recipients_list.setFont(FontManager.CENTURY_GOTHIC_18);

		m_attachments_list = new FilesList(m_attachments);
		m_attachments_list.setFont(FontManager.CENTURY_GOTHIC_18);

		// NORTH

		m_transmitter_label = new JLabel("De :", SwingConstants.CENTER);
		m_transmitter_label.setFont(FontManager.CENTURY_GOTHIC_BOLD_16);
		m_transmitter_label.setPreferredSize(new Dimension(120, 45));

		m_recipient_label = new JLabel("à :", SwingConstants.CENTER);
		m_recipient_label.setFont(FontManager.CENTURY_GOTHIC_BOLD_16);
		m_recipient_label.setPreferredSize(new Dimension(120, 45));

		m_message_object_label = new JLabel("Sujet :", SwingConstants.CENTER);
		m_message_object_label.setFont(FontManager.CENTURY_GOTHIC_BOLD_16);
		m_message_object_label.setPreferredSize(new Dimension(120, 45));

		m_attachments_label = new JLabel("Pièces jointes :", SwingConstants.CENTER);
		m_attachments_label.setFont(FontManager.CENTURY_GOTHIC_BOLD_16);
		m_attachments_label.setPreferredSize(new Dimension(120, 45));

		m_header_labels_panel = new JPanel(new GridLayout(5, 1));
		m_header_labels_panel.add(m_transmitter_label);
		m_header_labels_panel.add(m_recipient_label);
		m_header_labels_panel.add(m_message_object_label);
		m_header_labels_panel.add(m_attachments_label);
		m_header_labels_panel.add(new JLabel());

		m_transmitter_field = new TextValidationField(Validator.E_MAIL_ADDRESS);
		m_transmitter_field.setBorder(new LineBorder(ColorPalette.BLACK, 2));
		m_transmitter_field.setFont(FontManager.CENTURY_GOTHIC_16);
		m_transmitter_field.setPreferredSize(new Dimension(getWidth(), 45));

		m_recipients_button = new HighlightButton(
				new ImageIcon(getClass().getResource("res/icons/action/address_book-32.png")));
		m_recipients_button.addActionListener(this);

		m_message_object_field = new JTextField();
		m_message_object_field.setBorder(new LineBorder(ColorPalette.BLACK, 2));
		m_message_object_field.setFont(FontManager.CENTURY_GOTHIC_16);
		m_message_object_field.setPreferredSize(new Dimension(getWidth(), 45));

		m_attachments_button = new HighlightButton(
				new ImageIcon(getClass().getResource("res/icons/action/attach-32.png")));
		m_attachments_button.addActionListener(this);

		m_header_fields_panel = new JPanel(new GridLayout(5, 1));
		m_header_fields_panel.add(m_transmitter_field);
		m_header_fields_panel.add(m_recipients_button);
		m_header_fields_panel.add(m_message_object_field);
		m_header_fields_panel.add(m_attachments_button);
		m_header_fields_panel.add(new JLabel());

		m_header_panel = new JPanel(new BorderLayout());
		m_header_panel.add(m_header_labels_panel, BorderLayout.WEST);
		m_header_panel.add(m_header_fields_panel, BorderLayout.CENTER);

		add(m_header_panel, BorderLayout.NORTH);

		// CENTER

		m_font_type = new HighlightButton(
				new ImageIcon(getClass().getResource("res/icons/toolbar/font_family-32.png")));
		m_font_type.addActionListener(this);

		m_font_size = new HighlightButton(new ImageIcon(getClass().getResource("res/icons/toolbar/font_size-32.png")));
		m_font_size.addActionListener(this);

		m_font_color = new HighlightButton(
				new ImageIcon(getClass().getResource("res/icons/toolbar/font_color-32.png")));
		m_font_color.addActionListener(this);

		m_background_color = new HighlightButton(
				new ImageIcon(getClass().getResource("res/icons/toolbar/background_color-32.png")));
		m_background_color.addActionListener(this);

		m_tool_bar = new JToolBar();
		m_tool_bar.add(m_font_type);
		m_tool_bar.add(m_font_size);
		m_tool_bar.add(m_font_color);
		m_tool_bar.addSeparator();
		m_tool_bar.add(m_background_color);
		m_tool_bar.setBackground(ColorPalette.WHITE_SMOKE);

		m_text_area = new JTextArea();
		m_text_area.addCaretListener(event -> m_recorded = false);

		m_text_area.setBackground(ColorPalette.WHITE);
		m_text_area.setBorder(new LineBorder(ColorPalette.BLACK, 2));
		m_text_area.setFont(FontManager.CALIBRI_18);

		final JScrollPane scroll_pane = new JScrollPane(m_text_area);
		scroll_pane.getVerticalScrollBar().setUI(new FlatScrollBarUI());
		scroll_pane.getHorizontalScrollBar().setUI(new FlatScrollBarUI());

		m_body_panel = new JPanel(new BorderLayout());
		m_body_panel.add(m_tool_bar, BorderLayout.NORTH);
		m_body_panel.add(scroll_pane, BorderLayout.CENTER);

		add(m_body_panel, BorderLayout.CENTER);

		// WEST

		// SOUTH

		m_send = new HighlightButton(new ImageIcon(getClass().getResource("res/icons/action/post-48.png")));
		m_send.addActionListener(this);

		add(m_send, BorderLayout.SOUTH);
	}

	// RE-IMPLEMENTED METHODS

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource().equals(m_send)) {

			if (m_recipients.isEmpty()) {

				JOptionPane.showMessageDialog(null,
						"Veuillez d'abord au moins une adresses destinataire avant de pouvoir continuer.",
						"Aucune adresse destinataire spécifiée", JOptionPane.WARNING_MESSAGE,
						new ImageIcon(getClass().getResource("res/icons/menu/info-32.png")));
			} else {

				if (m_mail_sender.isConnected()) {

					if (m_mail_sender.send(m_recipients, m_message_object_field.getText(), m_text_area.getText(),
							m_attachments)) {

						m_parent.goToInformationPane(InformationPanel.SENDING_SUCCESSFUL);
					} else {

						m_parent.goToInformationPane(InformationPanel.SENDING_FAILS);
					}
				} else {

					if (m_mail_sender.connect()) {

						if (m_mail_sender.send(m_recipients, m_message_object_field.getText(), m_text_area.getText(),
								m_attachments)) {

							m_parent.goToInformationPane(InformationPanel.SENDING_SUCCESSFUL);
						} else {

							m_parent.goToInformationPane(InformationPanel.SENDING_FAILS);
						}
					}
				}
			}
		} else if (event.getSource().equals(m_recipients_button)) {

			final HighlightButton add = new HighlightButton(
					new ImageIcon(getClass().getResource("res/icons/action/add_row-32.png")));
			add.addActionListener(event1 -> {

				final TextValidationField field = new TextValidationField(Validator.E_MAIL_ADDRESS);
				field.setPreferredSize(new Dimension(300, 50));

				int state = 0;

				do {

					state = JOptionPane.showConfirmDialog(null, field, "Ajout d'adresses", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							new ImageIcon(getClass().getResource("res/icons/menu/help-32.png")));

					if (state == JOptionPane.OK_OPTION) {

						if (field.inputIsValid()) {

							m_recipients.add(field.getText());
							m_recipients_list.updateUI();
						} else {

							JOptionPane.showMessageDialog(null,
									"l'Adresse que vous avez saisin'est pas une adresse e-mail est invalide.",
									"Adresse invalide", JOptionPane.WARNING_MESSAGE,
									new ImageIcon(getClass().getResource("res/icons/menu/info-32.png")));
						}
					}

				} while (state == JOptionPane.OK_OPTION && !field.inputIsValid());
			});

			final HighlightButton delete = new HighlightButton(
					new ImageIcon(getClass().getResource("res/icons/action/delete_row-32.png")));
			delete.addActionListener(event1 -> {

				if (m_recipients.size() > 0 && m_recipients_list.getSelectedIndex() >= 0) {

					m_recipients.remove(m_recipients_list.getSelectedIndex());
					m_recipients_list.updateUI();
				}
			});

			final JPanel actions_panel = new JPanel(new GridLayout());
			actions_panel.add(add);
			actions_panel.add(delete);

			final JScrollPane scroll_pane = new JScrollPane(m_recipients_list);
			scroll_pane.getVerticalScrollBar().setUI(new FlatScrollBarUI());
			scroll_pane.getHorizontalScrollBar().setUI(new FlatScrollBarUI());

			final JPanel panel = new JPanel(new BorderLayout());
			panel.add(scroll_pane, BorderLayout.CENTER);
			panel.add(actions_panel, BorderLayout.SOUTH);

			final JDialog dialog = GUIFactory.createDialog("Liste des destinataires", 450, 450,
					new ImageIcon(getClass().getResource("res/icons/action/address_book-32.png")));

			dialog.setContentPane(panel);
			dialog.setVisible(true);
		} else if (event.getSource().equals(m_attachments_button)) {

			final HighlightButton add = new HighlightButton(
					new ImageIcon(getClass().getResource("res/icons/action/add_row-32.png")));
			add.addActionListener(event1 -> {

				final JFileChooser file_chooser = new JFileChooser();

				if (file_chooser.showOpenDialog(m_parent) == JFileChooser.APPROVE_OPTION) {

					final File selected_file = file_chooser.getSelectedFile();

					if (file_chooser.accept(selected_file)) {

						m_attachments.add(selected_file);
						m_attachments_list.updateUI();
					}
				}
			});

			final HighlightButton delete = new HighlightButton(
					new ImageIcon(getClass().getResource("res/icons/action/delete_row-32.png")));
			delete.addActionListener(event1 -> {

				if (m_attachments.size() > 0 && m_attachments_list.getSelectedIndex() >= 0) {

					m_attachments.remove(m_attachments_list.getSelectedIndex());
					m_attachments_list.updateUI();
				}
			});

			final JPanel actions_panel = new JPanel(new GridLayout());
			actions_panel.add(add);
			actions_panel.add(delete);

			final JScrollPane scroll_pane = new JScrollPane(m_attachments_list);
			scroll_pane.getVerticalScrollBar().setUI(new FlatScrollBarUI());
			scroll_pane.getHorizontalScrollBar().setUI(new FlatScrollBarUI());

			final JPanel panel = new JPanel(new BorderLayout());
			panel.add(scroll_pane, BorderLayout.CENTER);
			panel.add(actions_panel, BorderLayout.SOUTH);

			final JDialog dialog = GUIFactory.createDialog("Liste des pièces-jointes", 450, 450,
					new ImageIcon(getClass().getResource("res/icons/action/attach-32.png")));

			dialog.setContentPane(panel);
			dialog.setVisible(true);
		} else if (event.getSource().equals(m_font_type)) {

			final JDialog text_type_dialog = GUIFactory.createDialog("Choix de la police", 300, 100,
					new ImageIcon(getClass().getResource("res/icons/toolbar/font_family-32.png")), true);

			final String[] items = FontManager.getAvailableFontFamilyNames();

			final JComboBox<Object> text_type_box = new JComboBox<Object>(items);

			text_type_box.addItemListener(e -> {

				m_text_area.setFont(new Font(String.valueOf(text_type_box.getSelectedItem()),
						m_text_area.getFont().getStyle(), m_text_area.getFont().getSize()));

				text_type_dialog.dispose();
			});

			text_type_box.setFont(FontManager.CENTURY_GOTHIC_BOLD_18);
			text_type_box.setSelectedItem(m_text_area.getFont().getFamily());

			text_type_dialog.add(text_type_box);

			text_type_dialog.setVisible(true);
		} else if (event.getSource().equals(m_font_size)) {

			final JDialog text_size_dialog = GUIFactory.createDialog("Choix de la police", 300, 100,
					new ImageIcon(getClass().getResource("res/icons/toolbar/font_size-32.png")), true);

			final JComboBox<Object> text_size_box = new JComboBox<Object>();

			for (int i = 7; i < 100; i++) {

				text_size_box.addItem(i + 1);
			}

			text_size_box.addItemListener(e -> {

				m_text_area.setFont(new Font(m_text_area.getFont().getFamily(), m_text_area.getFont().getStyle(),
						Integer.valueOf(text_size_box.getSelectedItem().toString())));

				text_size_dialog.dispose();
			});

			text_size_box.setFont(FontManager.CENTURY_GOTHIC_20);
			text_size_box.setSelectedItem(m_text_area.getFont().getSize());
			((JLabel) text_size_box.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

			text_size_dialog.add(text_size_box);

			text_size_dialog.setVisible(true);
		} else if (event.getSource().equals(m_font_color)) {

			m_text_area
					.setForeground(JColorChooser.showDialog(this, "Choix de la couleur", m_text_area.getForeground()));
		} else if (event.getSource().equals(m_background_color)) {

			m_text_area
					.setBackground(JColorChooser.showDialog(this, "Choix de la couleur", m_text_area.getBackground()));
		}
	}

	@Override
	public void update(MailSender mail_sender) {

		m_mail_sender = mail_sender;
	}

	// IMPLEMENTED METHODS

	public void setFromAddress(String address) {

		m_transmitter_field.setText(address);
	}

	public void setFromAddressLocked(boolean flag) {

		m_transmitter_field.setEnabled(!flag);
	}

	public void setMessageObject(String object) {

		m_message_object_field.setText(object);
	}

	// IMPLEMENTED METHODS

	public void addAttachment(File file) {

		m_attachments.add(file);
	}

	public void addAttachments(Vector<File> files) {

		m_attachments.addAll(files);
	}

	public void addRecipient(String address) {

		m_recipients.add(address);
	}

	public void addRecipients(Vector<String> addresses) {

		m_recipients.addAll(addresses);
	}

	public void open() {

		final JFileChooser file_chooser = new JFileChooser();
		file_chooser.setAcceptAllFileFilterUsed(false);
		file_chooser.addChoosableFileFilter(new FileFilter() {

			@Override
			public String getDescription() {

				return null;
			}

			@Override
			public boolean accept(File file) {

				return file.getPath().endsWith(".txt");
			}
		});

		if (file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

			m_text_area.setText(IOUtils.readStringFromFile(file_chooser.getSelectedFile()));

			m_recorded = true;
			m_current_file_path = file_chooser.getSelectedFile().getPath();
		}
	}

	public void save() {

		if (m_recorded) {

			IOUtils.writeStringToFile(new File(m_current_file_path), m_text_area.getText());
		} else {

			saveAs();
		}
	}

	public void saveAs() {

		final JFileChooser file_chooser = new JFileChooser();
		file_chooser.addChoosableFileFilter(new FileFilter() {

			@Override
			public String getDescription() {

				return null;
			}

			@Override
			public boolean accept(File file) {

				return file.getPath().endsWith(".txt");
			}
		});

		if (file_chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

			IOUtils.writeStringToFile(file_chooser.getSelectedFile(), m_text_area.getText());

			m_recorded = true;
			m_current_file_path = file_chooser.getSelectedFile().getPath();
		}
	}

	public void print() {
	}
}
