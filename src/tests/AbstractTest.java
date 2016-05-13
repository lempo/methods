package tests;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import component.CustomRadioButton;
import component.CustomTextField;
import customui.ButtonCustomUI;
import customui.ScrollBarCustomUI;
import defaults.ImageLinkDefaults;
import defaults.InterfaceTextDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;

public abstract class AbstractTest extends JPanel {
	protected Methods methods;

	protected long startTime;

	protected String userName = "";
	protected String userSex = InterfaceTextDefaults.getInstance().getDefault("male");
	protected int userAge;
	protected Date testDate;
	protected long testTime;

	protected Test test;

	protected int width;
	protected int height;

	protected JPanel resultsPanel;

	public AbstractTest(Methods methods, int width, int height, Test test) {
		super();
		this.methods = methods;
		this.width = width;
		this.height = height;
		this.test = test;
		this.setPreferredSize(new Dimension(width, height));
		this.setOpaque(false);
		showInfo();
	}

	public void showStandartInfo() {
		// TODO layout and text into a scroll, middle priority
		JLabel image = new JLabel();
		ImageIcon icon = Utils.createImageIcon(test.getBigImage());
		image.setIcon(icon);

		JTextPane text = new JTextPane();
		text.setEditable(false);
		text.setContentType("text/html;charset=utf-8");
		text.setText("<html><div style='font: bold 22pt Arial Narrow; color: rgb(115, 84, 73); padding-bottom: 20 px'>"
				+ test.getName().toUpperCase()
				+ "</div><div  style='font: 16pt Arial Narrow; color: rgb(115, 84, 73);'>" + test.getLongText()
				+ "</div></html>");
		text.setOpaque(false);
		text.setPreferredSize(new Dimension((int) (width * 0.6 - image.getWidth()), (int) (height * 0.5)));
		// text.setPreferredSize(
		// new Dimension(485, 700));

		JButton start = new JButton(InterfaceTextDefaults.getInstance().getDefault("begin_task"));
		start.setUI(new ButtonCustomUI(new Color(38, 166, 154)));
		start.setBorder(null);
		start.setOpaque(false);
		start.setPreferredSize(new Dimension(200, 35));
		start.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSettings();
			}
		});

		GridBagConstraints c = new GridBagConstraints();

		removeAll();
		setLayout(new GridBagLayout());

		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;

		add(image, c);

		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.insets = new Insets(0, 0, 0, 0);
		c.weightx = 1.0;

		add(text, c);

		c.gridy = 1;
		c.insets = new Insets(40, 40, 0, 40);
		add(start, c);

		revalidate();
		repaint();
	}

	public void showStandartSettings() {
		JLabel heading = new JLabel();
		String t = "<html><div style='font: bold 24pt Arial Narrow; color: rgb(115, 84, 73);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("settings") + "</div></html>";
		heading.setText(t);

		JLabel ageLabel = new JLabel("<html><div style='font: 18pt Arial Narrow; color: rgb(115, 84, 73);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("age") + ":" + "</div></html>");
		JLabel sexLabel = new JLabel("<html><div style='font: 18pt Arial Narrow; color: rgb(115, 84, 73);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("sex") + ":" + "</div></html>");
		JLabel nameLabel = new JLabel("<html><div style='font: 18pt Arial Narrow; color: rgb(115, 84, 73);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("name") + ":" + "</div></html>");
		CustomTextField ageTextField = new CustomTextField(20, "");
		CustomTextField nameTextField = new CustomTextField(20, "");

		JRadioButton maleButton = new CustomRadioButton(InterfaceTextDefaults.getInstance().getDefault("male"), true);
		maleButton.setActionCommand("male");

		JRadioButton femaleButton = new CustomRadioButton(InterfaceTextDefaults.getInstance().getDefault("female"),
				false);
		femaleButton.setActionCommand("female");

		ButtonGroup group = new ButtonGroup();
		group.add(maleButton);
		group.add(femaleButton);

		JButton start = new JButton(InterfaceTextDefaults.getInstance().getDefault("begin_task"));
		start.setUI(new ButtonCustomUI(new Color(38, 166, 154)));
		start.setBorder(null);
		start.setOpaque(false);
		start.setPreferredSize(new Dimension(200, 35));
		start.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userName = nameTextField.getText().trim();
				if (!ageTextField.getText().trim().equals("") && (ageTextField.getText().trim().length() < 5))
					userAge = Math.min(9001, Integer.parseInt(ageTextField.getText().trim().replaceAll("[^0-9]", "")));
				else
					userAge = 0;
				if (femaleButton.isSelected())
					userSex = InterfaceTextDefaults.getInstance().getDefault("female");
				showTest();
			}
		});

		removeAll();
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 20, 100, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;
		add(heading, c);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(10, 200, 0, 0);
		c.gridwidth = 1;
		add(nameLabel, c);

		c.gridy = 2;
		add(sexLabel, c);

		c.gridy = 3;
		add(ageLabel, c);

		c.gridx = 2;
		c.gridy = 1;
		c.insets = new Insets(10, 20, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(nameTextField, c);

		c.gridy = 3;
		add(ageTextField, c);

		c.gridy = 2;
		c.gridwidth = 1;
		add(maleButton, c);

		c.gridx = 3;
		add(femaleButton, c);

		c.anchor = GridBagConstraints.CENTER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridx = 2;
		c.gridy = 4;
		c.insets = new Insets(40, 0, 0, 0);
		c.weighty = 1.0;
		add(start, c);

		revalidate();
		repaint();
	}

	public void showStandartResults() {

		resultsPanel = new JPanel();
		resultsPanel.setOpaque(false);
		resultsPanel.setLayout(new GridBagLayout());

		JLabel heading = new JLabel();
		String t = "<html><div style='font: bold 24pt Arial Narrow; color: rgb(144, 106, 96);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("test_results").toUpperCase() + "</div></html>";
		heading.setText(t);

		JButton print = new JButton(InterfaceTextDefaults.getInstance().getDefault("print_results"));
		print.setUI(new ButtonCustomUI(new Color(144, 106, 96)));
		print.setBorder(null);
		print.setOpaque(false);
		print.setPreferredSize(new Dimension(200, 35));
		print.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO print results, later
				showResults();
			}
		});

		JLabel repeat = new JLabel();
		repeat.setIcon(Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.REPEAT)));

		JLabel leftCol = new JLabel();
		t = "<html><div style='font: 20pt Arial Narrow; color: rgb(144, 106, 96); text-align: right;'>"
				+ InterfaceTextDefaults.getInstance().getDefault("name") + ": <br>"
				+ InterfaceTextDefaults.getInstance().getDefault("sex") + ": <br>"
				+ InterfaceTextDefaults.getInstance().getDefault("age") + ": <br>"
				+ InterfaceTextDefaults.getInstance().getDefault("test_date") + ": <br>"
				+ InterfaceTextDefaults.getInstance().getDefault("test_time") + ": <br>" + "</div></html>";
		leftCol.setText(t);

		JLabel rightCol = new JLabel();

		DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		t = "<html><div style='font: bold 20pt Arial; color: rgb(38, 166, 154);'>" + userName + "<br>" + userSex
				+ "<br>" + userAge + "<br>" + format.format(testDate) + "<br>"
				+ (testTime / 1000 / 60 / 10 == 0 ? "0" + testTime / 1000 / 60 : testTime / 1000 / 60) + ":"
				+ ((testTime / 1000 % 60) / 10 == 0 ? "0" + (testTime / 1000 % 60) : (testTime / 1000 % 60)) + "<br>"
				+ "</div></html>";
		rightCol.setText(t);

		removeAll();
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 20, 40, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;
		add(heading, c);

		c.insets = new Insets(10, 0, 0, 20);
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		resultsPanel.add(leftCol, c);

		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 0);
		c.gridwidth = GridBagConstraints.REMAINDER;
		resultsPanel.add(rightCol, c);

		resultsPanel.setPreferredSize(new Dimension(width, height / 2));

		/*
		 * JScrollPane scroll = new JScrollPane(resultsPanel);
		 * scroll.setPreferredSize(new Dimension((int) (width * 0.9), (int)
		 * (height * 0.7))); scroll.setHorizontalScrollBarPolicy(JScrollPane.
		 * HORIZONTAL_SCROLLBAR_NEVER);
		 * scroll.setVerticalScrollBarPolicy(JScrollPane.
		 * VERTICAL_SCROLLBAR_AS_NEEDED); scroll.setBorder(null);
		 * scroll.getVerticalScrollBar().setUI(new ScrollBarCustomUI());
		 * scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
		 */

		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 0, 0);
		add(resultsPanel, c);

		c.anchor = GridBagConstraints.WEST;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(40, 40, 0, 0);
		c.weighty = 1.0;
		add(repeat, c);

		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(40, 0, 0, 40);
		add(print, c);

		revalidate();
		repaint();
	}

	public abstract void showInfo();

	public abstract void showTest();

	public abstract void showResults();

	public abstract void showSettings();

}
