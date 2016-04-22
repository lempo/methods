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

import customui.ButtonCustomUI;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;

public class Audit extends AbstractTest {

	Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.AUDIT));
	NodeList nodelist = doc.getElementsByTagName("q");
	
	int summ = 0;
	int answer = 0;
	int currentQuestionNumber = 1;
	final int nQuestions = 10;
	
	JLabel question = new JLabel();
	
	JButton nextButton = new JButton(InterfaceTextDefaults.getInstance().getDefault("next"));
		
	public Audit(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showSettings() {
		showStandartSettings();
	}

	@Override
	public void showTest() {
		// TODO Auto-generated method stub
		nextButton.setUI(new ButtonCustomUI(new Color(144, 106, 96)));
		nextButton.setBorder(null);
		nextButton.setOpaque(false);
		nextButton.setPreferredSize(new Dimension(200, 35));
		nextButton.setMinimumSize(new Dimension(200, 35));
		nextButton.setMaximumSize(new Dimension(200, 35));
		nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentQuestionNumber >= nQuestions) {
					testTime = new Date().getTime() - testTime;
					showResults();
				}
				else {
				summ += Integer.parseInt(nodelist.item(currentQuestionNumber-1).getChildNodes().item(answer).getAttributes().getNamedItem("points").getNodeValue());
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
		c.insets = new Insets(0, 40, 0, 40);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;

		question.setHorizontalAlignment(JLabel.CENTER);
		
		//TODO Radio Buttons should be placed into a grid here
		
		this.add(question, c);
		
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0, 0, 0, 40);
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 40, 0, 0);
		c.gridx = 1;
		c.gridy = 1;
		this.add(nextButton, c);
		
		this.revalidate();
		this.repaint();
		
		testDate = new Date();
		testTime = new Date().getTime();
		showQuestion();


	}

	public void showQuestion() {
		
		String questionText = "<html><div style='font: bold 24pt Arial Narrow; color: rgb(144, 106, 96);'>"
		 + nodelist.item(currentQuestionNumber-1).getAttributes().getNamedItem("text").getNodeValue() + "</div></html>";
		question.setText(questionText);
		
		//Create Radio Buttons
		
		this.repaint();
	}	
	
	@Override
	public void showResults() {
		showStandartResults();
		// TODO Auto-generated method stub

	}


}
