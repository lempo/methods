package tests;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

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

public class Classification extends AbstractTest {

	Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.CLASSIFICATION));

	int currentQuestionNumber = 0;
	int summCorrect = 0;

	ArrayList<JButton> answers = new ArrayList<JButton>();

	public Classification(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		JLabel task = new JLabel();
		String s = doc.getElementsByTagName("task").item(0).getTextContent();
		task.setText("<html><div style='font: 24pt Arial Narrow; color: rgb(0, 168, 155);'>" + s.toUpperCase()
				+ "</div></html>");

		JButton nextButton = new JButton(InterfaceTextDefaults.getInstance().getDefault("next"));
		nextButton.setUI(new ButtonCustomUI(new Color(144, 106, 96)));
		nextButton.setBorder(null);
		nextButton.setOpaque(false);
		nextButton.setPreferredSize(new Dimension(200, 35));
		nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < answers.size(); i++) {
					if ( Integer.parseInt(doc.getElementsByTagName("q").item(currentQuestionNumber).getChildNodes().item(i)
							.getAttributes().getNamedItem("true").getNodeValue()) == 1
							&& ((BorderButtonCustomUI) answers.get(i).getUI()).getBorderColor()
									.equals(new Color(0, 168, 155))) 
						summCorrect++;
				}
				
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
		// Previous questions clean-up
		for (int i = 0; i < answers.size(); i++) {
			this.remove(answers.get(i));
		}
		answers.clear();

		NodeList answersText = doc.getElementsByTagName("q").item(currentQuestionNumber).getChildNodes();

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(40, 20, 40, 20);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.1;
		c.weighty = 0.0;

		AnswersListener l = new AnswersListener();

		for (int i = 0; i < answersText.getLength(); i++) {
			JButton b = new JButton(answersText.item(i).getTextContent());
			b.setUI(new BorderButtonCustomUI(new Color(144, 106, 96)));
			b.setBorder(null);
			b.setOpaque(false);
			b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			Font f = ((BorderButtonCustomUI) b.getUI()).getFont();
			FontMetrics fm = b.getFontMetrics(f);
			int w = (int) fm.getStringBounds(b.getText(), b.getGraphics()).getWidth();
			b.setPreferredSize(new Dimension(w + 40, 35));
			b.addActionListener(l);

			answers.add(b);
		}

		// Myetod: "chje-rjez zhoo-puu"......
		c.insets = new Insets(40, 60, 40, 15);
		this.add(answers.get(0), c);
		c.insets = new Insets(40, 15, 40, 15);
		for (int i = 1; i < answers.size() - 1; i++) {
			c.gridx = i;
			this.add(answers.get(i), c);
		}
		c.gridx = 4;
		c.insets = new Insets(40, 15, 40, 60);
		this.add(answers.get(4), c);
		// ...ne, nu esli inache nikak....

		this.revalidate();
		this.repaint();
	}

	@Override
	public void showResults() {
		this.showStandartResults();
		
		JLabel leftCol = new JLabel();
		JLabel rightCol = new JLabel();
		
		NodeList d = doc.getElementsByTagName("d");
			
		String t = "<html><div style='font: 20pt Arial Narrow; color: rgb(144, 106, 96); text-align: right;'>"
				+ d.item(0).getTextContent() + ": <br>" + "</div></html>";
		leftCol.setText(t);	
			
		t = "<html><div style='font: bold 20pt Arial; color: rgb(38, 166, 154);'>"
				+ summCorrect + "<br>";
		t += "</div></html>";
		rightCol.setText(t);
	
		t = "<html><div style='font: bold 20pt Arial; color: rgb(144, 106, 96); padding: 10px'>";
		if (summCorrect >= 0 && summCorrect <= 10) t += d.item(1).getTextContent().toUpperCase();
		if (summCorrect >= 11 && summCorrect <= 14) t += d.item(2).getTextContent().toUpperCase();
		if (summCorrect >= 15 && summCorrect <= 17) t += d.item(3).getTextContent().toUpperCase();
		t += "</div></html>";
		JPanel conclusion = new JPanel();
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

	class AnswersListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			for (JButton a : answers) {
				((BorderButtonCustomUI) a.getUI()).setBorderColor(new Color(144, 106, 96));
				a.repaint();
			}
			((BorderButtonCustomUI) b.getUI()).setBorderColor(new Color(0, 168, 155));
			b.repaint();
		}
	}

}
