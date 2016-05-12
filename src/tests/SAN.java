package tests;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import customui.BorderButtonCustomUI;
import customui.ButtonCustomUI;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;

public class SAN extends AbstractTest {

	Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.SAN));
	NodeList nodelist = doc.getElementsByTagName("q");

	int currentQuestionNumber = 1;

	JButton[] answers = new JButton[7];
	JButton leftButton = new JButton();
	JButton rightButton = new JButton();
	JPanel answersPanel = new JPanel();

	public SAN(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		// TODO

		JLabel question = new JLabel();
		String s = doc.getElementsByTagName("text").item(0).getTextContent();
		question.setText("<html><div style='font: 24pt Arial Narrow; color: rgb(0, 168, 155);'>" + s.toUpperCase()
				+ "</div></html>");

		answersPanel.setOpaque(false);
		answersPanel.setLayout(new GridBagLayout());
		
		leftButton.setUI(new BorderButtonCustomUI(new Color(144, 106, 96)));
		leftButton.setBorder(null);
		leftButton.setOpaque(false);
		leftButton.setUI(new BorderButtonCustomUI(new Color(144, 106, 96)));
		
		rightButton.setUI(new BorderButtonCustomUI(new Color(144, 106, 96)));
		rightButton.setBorder(null);
		rightButton.setOpaque(false);
		rightButton.setUI(new BorderButtonCustomUI(new Color(144, 106, 96)));
		
		answersPanel.add(rightButton);
		
		for (int i = 0; i < answers.length; i++) {
			answers[answers.length - i - 1] = new JButton(Integer.toString((int) Math.abs(answers.length / 2 - i)));
			answers[answers.length - i - 1].setUI(new BorderButtonCustomUI(new Color(144, 106, 96)));
			answers[answers.length - i - 1].setBorder(null);
			answers[answers.length - i - 1].setOpaque(false);
			answers[answers.length - i - 1].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			answers[answers.length - i - 1].setPreferredSize(new Dimension(40, 40));
			answersPanel.add(answers[answers.length - i - 1]);
		}
		
		answersPanel.add(leftButton);

		JButton nextButton = new JButton(InterfaceTextDefaults.getInstance().getDefault("next"));
		nextButton.setUI(new ButtonCustomUI(new Color(144, 106, 96)));
		nextButton.setBorder(null);
		nextButton.setOpaque(false);
		nextButton.setPreferredSize(new Dimension(200, 35));
		nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Count summs here
				currentQuestionNumber++;
				showQuestion();

			}
		});

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

		add(question, c);

		c.gridy = 1;
		c.insets = new Insets(25, 0, 25, 0);

		add(answersPanel, c);

		c.anchor = GridBagConstraints.NORTH;
		c.gridy = 3;
		c.weighty = 0.1;
		c.insets = new Insets(40, 0, 80, 0);

		add(nextButton, c);

		this.revalidate();
		this.repaint();

		showQuestion();
	}

	public void showQuestion() {
		// TODO
		
		String t = nodelist.item(currentQuestionNumber - 1).getAttributes().item(1).getNodeName().toString();

		if (nodelist.item(currentQuestionNumber - 1).getAttributes().item(1).getNodeName().toString() == "positive") {
			System.out.println(t);
		}

		
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
