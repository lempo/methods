package tests;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.Timer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import component.CustomRadioButton;
import component.CustomTextField;
import customui.ButtonCustomUI;
import customui.ProgressBarCustomUI;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;

public class Eysenck extends AbstractTest {

	Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.EYSENCK));

	JLabel task;
	JLabel blank;
	JPanel answers;
	CustomTextField input = new CustomTextField(20, "");
	ButtonGroup radioButtonGroup = new ButtonGroup();
	ArrayList<JRadioButton> radioButtonList = new ArrayList<JRadioButton>();

	int currentQuestionNumber = 0;
	String questionType;

	private Timer timer;
	private JProgressBar bar;
	private JLabel timeLeft;

	private final int TIME = 30;

	public Eysenck(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		// TODO layout, high priority

		task = new JLabel();
		blank = new JLabel();
		answers = new JPanel();
		answers.setOpaque(false);

		JButton nextButton = new JButton(InterfaceTextDefaults.getInstance().getDefault("next"));
		nextButton.setUI(new ButtonCustomUI(new Color(144, 106, 96)));
		nextButton.setBorder(null);
		nextButton.setOpaque(false);
		nextButton.setPreferredSize(new Dimension(200, 35));
		nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO time?
				if (currentQuestionNumber >= doc.getElementsByTagName("q").getLength() - 1) {
					showResults();
				} else {
					// TODO calculate results, high priority
					switch (questionType) {
					case "text":
						break;
					case "radio":

						break;
					case "picture":
						break;
					}
					currentQuestionNumber++;
					showQuestion();
				}
			}
		});

		bar = new JProgressBar();
		bar.setStringPainted(false);
		bar.setMinimum(0);
		bar.setMaximum(TIME);
		bar.setValue(0);
		bar.setUI(new ProgressBarCustomUI());
		bar.setBorder(null);

		timeLeft = new JLabel("<html><div style='font: 16pt Arial Narrow; color: rgb(144, 106, 96);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("time_left").toUpperCase() + ": " + TIME + " "
				+ InterfaceTextDefaults.getInstance().getDefault("min").toUpperCase() + "</div></html>");

		this.removeAll();
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(40, 0, 40, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;

		add(task, c);

		c.gridy = 1;
		c.insets = new Insets(25, 0, 25, 0);

		add(answers, c);

		c.anchor = GridBagConstraints.NORTH;
		c.gridy = 3;
		c.weighty = 0.1;
		c.insets = new Insets(40, 0, 80, 0);

		add(nextButton, c);

		c.gridy = 4;
		c.insets = new Insets(40, 0, 0, 0);
		add(timeLeft, c);

		c.gridy = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		add(bar, c);

		this.revalidate();
		this.repaint();

		timer = new Timer(1000 * 60, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

				if (bar.getValue() < TIME) {
					bar.setValue(bar.getValue() + 1);
					timeLeft.setText("<html><div style='font: 16pt Arial Narrow; color: rgb(144, 106, 96);'>"
							+ InterfaceTextDefaults.getInstance().getDefault("time_left").toUpperCase() + ": "
							+ (TIME - bar.getValue()) + " " + InterfaceTextDefaults.getInstance().getDefault("min").toUpperCase()
							+ "</div></html>");
					timeLeft.repaint();
				} else {
					timer.stop();
					showResults();
				}

			}
		});
		timer.start();

		showQuestion();

	}

	public void showQuestion() {
		// TODO layout, high priority
		Node n = doc.getElementsByTagName("q").item(currentQuestionNumber);
		String s = n.getChildNodes().item(0).getTextContent();
		task.setText("<html><div style='font: 24pt Arial Narrow; color: rgb(0, 168, 155);'>" + s.toUpperCase()
				+ "</div></html>");

		String type = n.getAttributes().getNamedItem("type").getNodeValue();
		answers.removeAll();
		switch (type) {
		case "text":
			s = n.getAttributes().getNamedItem("blank").getNodeValue();
			blank.setText("<html><div style='font: 24pt Arial Narrow; color: rgb(0, 168, 155);'>" + s.toUpperCase()
					+ "</div></html>");
			answers.add(blank);
			answers.add(input);
			break;
		case "radio":
			for (int i = 0; i < radioButtonList.size(); i++) {
				this.remove(radioButtonList.get(i));
				radioButtonGroup.remove(radioButtonList.get(i));
			}
			radioButtonList = new ArrayList<JRadioButton>();
			int optionsNum = n.getChildNodes().getLength() - 1;
			for (int i = 0; i < optionsNum; i++) {
				JRadioButton b = new CustomRadioButton(n.getChildNodes().item(i + 1).getTextContent(), false);
				radioButtonList.add(b);
				radioButtonGroup.add(b);
				b.setOpaque(false);
				answers.add(b);
			}
			radioButtonList.get(0).setSelected(true);
			break;
		case "picture":
			// TODO case "picture" type, high priority
			break;
		}

		this.revalidate();
		this.repaint();
	}

	@Override
	public void showResults() {
		showStandartResults();
		// TODO calculate and output results, do conclusion, high priority
	}

	@Override
	public void showSettings() {
		showStandartSettings();
	}

}
