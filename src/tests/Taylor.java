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

import customui.ButtonCustomUI;
import defaults.InterfaceTextDefaults;
import methods.Methods;
import methods.Test;

public class Taylor extends AbstractTest {
	
	public Taylor(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}
	
	int summ = 0;
	int lies = 0;
	int currentQuestionNumber = 1;
	final int nQuestions = 60;
	int[][] answers = 
		{{1,0,1,1,1,0,0,1,0,0,
			0,0,0,1,0,0,1,0,1,0,
			0,1,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,1,0,
			0,0,1,0,0,0,0,0,0,0,
			0,1,0,0,0,0,1,1,0,0},
		{0,0,0,0,0,1,1,0,1,0,
			1,1,1,0,1,0,0,1,0,0,
			1,0,1,1,1,1,0,1,0,1,
			1,1,1,1,1,1,1,1,0,1,
			0,1,0,1,1,1,1,1,1,1,
			0,0,1,1,0,1,0,0,0,1}};
	int[][] lieCheck = 
		{{0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,1,0,0,0,1,
			0,0,0,0,0,0,1,0,1,0,
			0,0,0,0,0,0,0,0,0,0,
			1,0,0,0,0,0,0,0,0,0,
			1,0,0,0,0,0,0,0,1,0},
		{0,1,0,0,0,0,0,0,0,1,
			0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,0,0,0,0,0,0,
			0,0,0,0,1,0,0,0,0,0}};
	JLabel question = new JLabel();
	
	JButton yesButton = new JButton(InterfaceTextDefaults.getInstance().getDefault("yes"));
	JButton noButton = new JButton(InterfaceTextDefaults.getInstance().getDefault("no"));

	@Override
	public void showInfo() {
		showStandartInfo();

	}
	
	@Override
	public void showTest() {
		// TODO Auto-generated method stub
		
		yesButton.setUI(new ButtonCustomUI(new Color(144, 106, 96)));
		yesButton.setBorder(null);
		yesButton.setOpaque(false);
		yesButton.setPreferredSize(new Dimension(200, 35));
		yesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentQuestionNumber >= nQuestions) {
					testTime = new Date().getTime() - testTime;
					showResults();
				}
				else {
				summ += answers[1][currentQuestionNumber];
				lies += lieCheck[1][currentQuestionNumber];
				currentQuestionNumber++;
				showQuestion();
				}
			}
		});
		
		noButton.setUI(new ButtonCustomUI(new Color(144, 106, 96)));
		noButton.setBorder(null);
		noButton.setOpaque(false);
		noButton.setPreferredSize(new Dimension(200, 35));
		noButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		noButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentQuestionNumber >= nQuestions) {
					testTime = new Date().getTime() - testTime;
					showResults();					
				}
				else {
				summ += answers[0][currentQuestionNumber];
				lies += lieCheck[0][currentQuestionNumber];
				currentQuestionNumber++;
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
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;

		this.add(question, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		this.add(yesButton, c);
		
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
		
		//Query question from xml
		String questionText = "<html><div style='font: bold 24pt Arial Narrow; color: rgb(144, 106, 96);'>"
		 + "Question N" + currentQuestionNumber + ": Lorem Ipsum i dalee po tekstu..." + "</div></html>";
		
		question.setText(questionText);
		
		question.repaint();
	}

	@Override
	public void showResults() {
		showStandartResults();
		// TODO Auto-generated method stub

	}

	@Override
	public void showSettings() {
		showStandartSettings();
		
	}

}
