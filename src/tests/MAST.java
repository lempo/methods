package tests;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import customui.BorderButtonCustomUI;
import customui.PanelCustomUI;
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
	int summ = 0;

	public MAST(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		currentQuestionNumber = 0;
		summ = 0;
		
		yesButton.setUI(new BorderButtonCustomUI(new Color(38, 166, 154)));
		yesButton.setBorder(null);
		yesButton.setOpaque(false);
		yesButton.setPreferredSize(new Dimension(200, 40));
		yesButton.setMinimumSize(new Dimension(200, 40));
		yesButton.setMaximumSize(new Dimension(200, 40));
		yesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				summ += Integer
						.parseInt(n.item(currentQuestionNumber).getAttributes().getNamedItem("yes").getNodeValue());
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
				summ += Integer
						.parseInt(n.item(currentQuestionNumber).getAttributes().getNamedItem("no").getNodeValue());
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
		showStandartResults();

		JLabel leftCol = new JLabel();
		JLabel rightCol = new JLabel();
		JPanel conclusion = new JPanel();
		String t;

		NodeList d = doc.getElementsByTagName("d");

		t = "<html><div style='font: 20pt Arial Narrow; color: rgb(144, 106, 96); text-align: right;'>"
				+ d.item(0).getTextContent() + ": <br>" + "</div></html>";
		leftCol.setText(t);

		t = "<html><div style='font: bold 20pt Arial; color: rgb(38, 166, 154);'>" + summ + "<br>";
		t += "</div></html>";
		rightCol.setText(t);

		t = "<html><div style='font: bold 20pt Arial; color: rgb(144, 106, 96); padding: 10px'>";
		if (summ >= 0 && summ <= 4)
			t += d.item(1).getTextContent().toUpperCase();
		if (summ >= 5 && summ <= 7)
			t += d.item(2).getTextContent().toUpperCase();
		if (summ > 7)
			t += d.item(3).getTextContent().toUpperCase();
		t += "</div></html>";

		conclusion.add(new JLabel(t));
		conclusion.setUI(new PanelCustomUI(true));

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;

		c.insets = new Insets(10, 0, 0, 20);
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		// leftCol.setPreferredSize(new Dimension(300, 350));
		leftCol.setVerticalAlignment(JLabel.TOP);
		resultsPanel.add(leftCol, c);

		c.gridx = 1;

		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 0);
		c.gridwidth = 1;
		// rightCol.setPreferredSize(new Dimension(300, 350));
		rightCol.setVerticalAlignment(JLabel.TOP);
		resultsPanel.add(rightCol, c);

		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(20, 0, 0, 0);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 2;
		resultsPanel.add(conclusion, c);

		this.revalidate();
		this.repaint();
	}

	@Override
	public void showSettings() {
		showStandartSettings();
	}

	@Override
	public void printResults() {
		standartPrintResults();
	}

}
