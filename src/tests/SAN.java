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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import customui.BorderButtonCustomUI;
import customui.ButtonCustomUI;
import customui.PanelCustomUI;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;

public class SAN extends AbstractTest {

	Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.SAN));
	NodeList nodelist = doc.getElementsByTagName("q");

	int currentQuestionNumber = 0;
	int buttonSelected = 0;
	int summWellbeing = 0;
	int summActivity = 0;
	int summMood = 0;

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
		JLabel question = new JLabel();
		String s = doc.getElementsByTagName("text").item(0).getTextContent();
		question.setText("<html><div style='font: 24pt Arial Narrow; color: rgb(0, 168, 155);'>" + s.toUpperCase()
				+ "</div></html>");

		answersPanel.setOpaque(false);
		answersPanel.setLayout(new GridBagLayout());
		answersPanel.setPreferredSize(new Dimension(760, 60));

		leftButton.setUI(new BorderButtonCustomUI(new Color(144, 106, 96)));
		leftButton.setBorder(null);
		leftButton.setOpaque(false);
		leftButton.setPreferredSize(new Dimension(240, 40));

		rightButton.setUI(new BorderButtonCustomUI(new Color(144, 106, 96)));
		rightButton.setBorder(null);
		rightButton.setOpaque(false);
		rightButton.setPreferredSize(new Dimension(240, 40));

		answersPanel.add(leftButton);

		for (int i = 0; i < answers.length; i++) { 
			answers[i] = new JButton(Integer.toString((int) Math.abs(answers.length / 2 - i)));
			answers[i].setUI(new BorderButtonCustomUI(new Color(144, 106, 96)));
			answers[i].setBorder(null);
			answers[i].setOpaque(false);
			answers[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			answers[i].setPreferredSize(new Dimension(40, 40));
			answers[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton b = (JButton) e.getSource();
					if (((BorderButtonCustomUI) b.getUI()).getBorderColor().equals(new Color(144, 106, 96))) {
						for (int i = 0; i < answers.length; i++) {
							if (Integer.parseInt(answers[i].getName()) == buttonSelected)
								((BorderButtonCustomUI) answers[i].getUI()).setBorderColor(new Color(144, 106, 96));
						}
						((BorderButtonCustomUI) b.getUI()).setBorderColor(new Color(0, 168, 155));
					}
					buttonSelected = Integer.parseInt(b.getName());

					answersPanel.repaint();
				}
			});
			answersPanel.add(answers[i]);
		}

		answersPanel.add(rightButton);

		JButton nextButton = new JButton(InterfaceTextDefaults.getInstance().getDefault("next"));
		nextButton.setUI(new ButtonCustomUI(new Color(144, 106, 96)));
		nextButton.setBorder(null);
		nextButton.setOpaque(false);
		nextButton.setPreferredSize(new Dimension(200, 35));
		nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (Integer.parseInt(nodelist.item(currentQuestionNumber).getAttributes()
						.getNamedItem("scale").getNodeValue())) {
				case 1:
					summWellbeing += buttonSelected;
					break;
				case 2:
					summActivity += buttonSelected;
					break;
				case 3:
					summMood += buttonSelected;
					break;
				}
				
				if (currentQuestionNumber >= doc.getElementsByTagName("q").getLength() - 1) {
					testTime = new Date().getTime() - testTime;
					showResults();
				} else {
					
					currentQuestionNumber++;
					//buttonSelected = 0;
					showQuestion();
				}
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

		testDate = new Date();
		testTime = new Date().getTime();
		showQuestion();
	}

	public void showQuestion() {
		// Cleanup form previous question
		for (int i = 0; i < answers.length; i++) ((BorderButtonCustomUI) answers[i].getUI()).setBorderColor(new Color(144, 106, 96));

		int t = Integer.parseInt(nodelist.item(currentQuestionNumber).getTextContent());

		for (int i = 0; i < 7; i++) {
			answers[i].setName(Integer.toString(Math.abs(i - t)));
		}
		
		((BorderButtonCustomUI) answers[3].getUI()).setBorderColor(new Color(0, 168, 155));
		buttonSelected = Integer.parseInt(answers[3].getName());

		leftButton.setText(nodelist.item(currentQuestionNumber).getAttributes().getNamedItem("left").getNodeValue()
				.toString());
		rightButton.setText(nodelist.item(currentQuestionNumber).getAttributes().getNamedItem("right")
				.getNodeValue().toString());
		if (t > 0) {
			((BorderButtonCustomUI) leftButton.getUI()).setBorderColor(new Color(38, 166, 154));
			((BorderButtonCustomUI) rightButton.getUI()).setBorderColor(new Color(239, 83, 80));
		}
		else {
			((BorderButtonCustomUI) rightButton.getUI()).setBorderColor(new Color(38, 166, 154));
			((BorderButtonCustomUI) leftButton.getUI()).setBorderColor(new Color(239, 83, 80));
		}

		this.revalidate();
		this.repaint();
	}

	@Override
	public void showResults() {
		showStandartResults();
		
		JLabel leftCol = new JLabel();
		JLabel rightCol = new JLabel();
		
		NodeList d = doc.getElementsByTagName("d");
			
		String t = "<html><div style='font: 20pt Arial Narrow; color: rgb(144, 106, 96); text-align: right;'>"
				+ d.item(0).getTextContent() + ": <br>" 
				+ d.item(1).getTextContent() + ": <br>"
				+ d.item(2).getTextContent() + ": <br>"
				+ "</div></html>";
		leftCol.setText(t);	
			
		t = "<html><div style='font: bold 20pt Arial; color: rgb(38, 166, 154);'>"
				+ summWellbeing + "<br>" 
				+ summActivity + "<br>"
				+ summMood + "<br>"
				+ "</div></html>";
		rightCol.setText(t);
	
		/*t = "<html><div style='font: bold 20pt Arial; color: rgb(144, 106, 96); padding: 10px'>";
		t += d.item(3).getTextContent();
		if (summCorrect >= 0 && summCorrect <= 10) t += d.item(1).getTextContent().toUpperCase();
		if (summCorrect >= 11 && summCorrect <= 14) t += d.item(2).getTextContent().toUpperCase();
		if (summCorrect >= 15 && summCorrect <= 20) t += d.item(3).getTextContent().toUpperCase();
		Vdruk-k-k-k pri-i-igoditsyaa-a-a....
		
		t += "</div></html>";
		JPanel conclusion = new JPanel();
		conclusion.add(new JLabel(t));
		conclusion.setUI(new PanelCustomUI(true));*/
		
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
		//leftCol.setPreferredSize(new Dimension(300, 350));
		leftCol.setVerticalAlignment(JLabel.TOP);
		resultsPanel.add(leftCol, c);

		c.gridx = 1;
		
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 0);
		c.gridwidth = 1;
		//rightCol.setPreferredSize(new Dimension(300, 350));
		rightCol.setVerticalAlignment(JLabel.TOP);
		resultsPanel.add(rightCol, c);
		
		/*c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(20, 0, 0, 0);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 2;
		resultsPanel.add(conclusion, c);*/
		
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
