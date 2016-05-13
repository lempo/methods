package tests;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import customui.BorderButtonCustomUI;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;

public class MAST extends AbstractTest {

	Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.MAST));
	NodeList n = doc.getElementsByTagName("q");

	JButton yesButton = new JButton(InterfaceTextDefaults.getInstance().getDefault("yes"));
	JButton noButton = new JButton(InterfaceTextDefaults.getInstance().getDefault("no"));

	JLabel question = new JLabel();

	int currentQuestionNumber = 0;

	public MAST(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		// TODO
		yesButton.setUI(new BorderButtonCustomUI(new Color(38, 166, 154)));
		yesButton.setBorder(null);
		yesButton.setOpaque(false);
		yesButton.setPreferredSize(new Dimension(200, 40));
		yesButton.setMinimumSize(new Dimension(200, 40));
		yesButton.setMaximumSize(new Dimension(200, 40));
		yesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO count points
				if (currentQuestionNumber >= doc.getElementsByTagName("q").getLength() - 1) {
					testTime = new Date().getTime() - testTime;
					showResults();
				} else {
					currentQuestionNumber++;
					showQuestion();
				}
			}
		});
		noButton.setUI(new BorderButtonCustomUI(new Color(239, 83, 80)));
		noButton.setBorder(null);
		noButton.setOpaque(false);
		noButton.setPreferredSize(new Dimension(200, 40));
		noButton.setMinimumSize(new Dimension(200, 40));
		noButton.setMaximumSize(new Dimension(200, 40));
		noButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		noButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO count points
				if (currentQuestionNumber >= doc.getElementsByTagName("q").getLength() - 1) {
					testTime = new Date().getTime() - testTime;
					showResults();
				} else {
					currentQuestionNumber++;
					showQuestion();
				}
			}
		});

		this.removeAll();
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 40, 20, 40);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;

		question.setHorizontalAlignment(JLabel.CENTER);
		question.setPreferredSize(new Dimension(800, 120));

		this.add(question, c);

		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0, 0, 200, 40);
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		this.add(yesButton, c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0, 40, 200, 0);
		c.gridx = 1;
		c.gridy = 1;
		this.add(noButton, c);

		this.revalidate();
		this.repaint();

		testDate = new Date();
		testTime = new Date().getTime();
		showQuestion();
	}

	public void showQuestion() {

		String questionText = "<html><div style='font: 24pt Arial Narrow; color: rgb(0, 168, 155);'>"
				+ n.item(currentQuestionNumber).getTextContent().toUpperCase() + "</div></html>";

		question.setText(questionText);

		question.repaint();
	}

	@Override
	public void showResults() {
		// TODO calculate and output results, do conclusion, high priority
		showStandartResults();
	}

	@Override
	public void showSettings() {
		showStandartSettings();
	}

}
