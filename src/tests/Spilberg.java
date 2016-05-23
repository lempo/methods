package tests;

import java.awt.Color;
import java.awt.Component;
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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import customui.BorderButtonCustomUI;
import customui.ButtonCustomUI;
import customui.PanelCustomUI;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;
import tests.Descriptor.AnswersListener;

public class Spilberg extends AbstractTest {

	Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.SPILBERG));

	int currentQuestionNumber = 0;
	int summ1 = 0;
	int summ2 = 0;
	int summAnxiety = 0;

	JButton questionButton = new JButton();
	ArrayList<JButton> answers = new ArrayList<JButton>();
	JPanel answersPanel = new JPanel();

	public Spilberg(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		currentQuestionNumber = 0;
		summ1 = 0;
		summ2 = 0;
		summAnxiety = 0;
		
		questionButton.setUI(new ButtonCustomUI(new Color(0, 168, 155)));
		questionButton.setBorder(null);
		questionButton.setOpaque(false);
		questionButton.setPreferredSize(new Dimension(200, 45));
		questionButton.setMinimumSize(new Dimension(200, 45));

		NodeList answersText = doc.getElementsByTagName("a");
		AnswersListener l = new AnswersListener();

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
					if (((BorderButtonCustomUI) answers.get(i).getUI()).getBorderColor().equals(new Color(0, 168, 155))) {
						switch (Integer.parseInt(doc.getElementsByTagName("q").item(currentQuestionNumber).getAttributes().getNamedItem("scale").getNodeValue())){
						case 1:
							summ1 += i+1;
							break;
						case 2:
							summ2 += i+1;
							break;
						}
					}
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
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridy = 0;
		c.insets = new Insets(15, 0, 25, 0);

		add(task, c);
		
		c.gridy = 1;
		c.insets = new Insets(100, 0, 20, 0);

		add(questionButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridy = 2;
		c.insets = new Insets(10, 20, 10, 20);
		c.weightx = 0.1;

		for (int i = 0; i < answersText.getLength(); i++) {
			JButton b = new JButton(answersText.item(i).getTextContent());
			b.setUI(new BorderButtonCustomUI(new Color(144, 106, 96)));
			b.setBorder(null);
			b.setOpaque(false);
			b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			Font f = ((BorderButtonCustomUI) b.getUI()).getFont();
			FontMetrics fm = b.getFontMetrics(f);
			int w = (int) fm.getStringBounds(b.getText(), b.getGraphics()).getWidth();
			b.setPreferredSize(new Dimension(w + 40, 45));
			b.addActionListener(l);

			answers.add(b);
		}
		
		((BorderButtonCustomUI) answers.get(0).getUI()).setBorderColor(new Color(0, 168, 155));

		// Myetod: "chje-rjez zhoo-puu"......
		c.insets = new Insets(10, 60, 10, 15);
		this.add(answers.get(0), c);
		c.insets = new Insets(10, 15, 10, 15);
		for (int i = 1; i < answers.size() - 1; i++) {
			c.gridx = i;
			this.add(answers.get(i), c);
		}
		c.gridx = 3;
		c.insets = new Insets(10, 15, 10, 60);
		this.add(answers.get(3), c);
		// ...ne, nu esli inache nikak....

		c.fill = GridBagConstraints.NONE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.weightx = 0.0;
		c.anchor = GridBagConstraints.NORTH;
		c.gridy = 3;
		c.weighty = 0.1;
		c.insets = new Insets(55, 0, 80, 0);

		add(nextButton, c);

		this.revalidate();
		this.repaint();

		testDate = new Date();
		testTime = new Date().getTime();
		showQuestion();
	}

	public void showQuestion() {
		Node q = doc.getElementsByTagName("q").item(currentQuestionNumber);
		questionButton.setText(q.getTextContent());
		Font f = new Font("Arial Narrow", Font.BOLD, 17);
		FontMetrics fm = questionButton.getFontMetrics(f);
		int w = (int) fm.getStringBounds(questionButton.getText(), questionButton.getGraphics()).getWidth();
		questionButton.setPreferredSize(new Dimension(w + 40, 45));
		questionButton.repaint();
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

		summAnxiety = summ1 - summ2 + 35;
		
		t = "<html><div style='font: bold 20pt Arial; color: rgb(38, 166, 154);'>"
				+ summAnxiety + "<br>";
		t += "</div></html>";
		rightCol.setText(t);
	
		t = "<html><div style='font: bold 20pt Arial; color: rgb(144, 106, 96); padding: 10px'>";
		if (summAnxiety <= 11) t += d.item(1).getTextContent().toUpperCase();
		if (summAnxiety >= 12 && summAnxiety <= 30) t += d.item(2).getTextContent().toUpperCase();
		if (summAnxiety >= 31 && summAnxiety <= 45) t += d.item(3).getTextContent().toUpperCase();
		if (summAnxiety >= 46 ) t += d.item(4).getTextContent().toUpperCase();
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

	@Override
	public void printResults() {
		standartPrintResults();
	}

}
