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

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import customui.BorderButtonCustomUI;
import customui.ButtonCustomUI;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;

public class Descriptor extends AbstractTest {

	Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.DESCRIPTOR));

	int summCorrect = 0;
	int currentQuestionNumber = 0;
	int summSelected = 0;

	JButton questionButton = new JButton();
	ArrayList<JButton> answers = new ArrayList<JButton>();

	public Descriptor(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		// TODO margins, middle priority

		questionButton.setUI(new ButtonCustomUI(new Color(0, 168, 155)));
		questionButton.setBorder(null);
		questionButton.setOpaque(false);
		questionButton.setPreferredSize(new Dimension(200, 35));

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
				if (currentQuestionNumber >= doc.getElementsByTagName("q").getLength()) {
					testTime = new Date().getTime() - testTime;
					showResults();
				} else {
					int counter = 0;
					for (int i = 0; i < answers.size(); i++) {
						if ( Integer.parseInt(doc.getElementsByTagName("q").item(currentQuestionNumber).getChildNodes().item(i)
								.getAttributes().getNamedItem("true").getNodeValue()) == 1
								&& ((BorderButtonCustomUI) answers.get(i).getUI()).getBorderColor()
										.equals(new Color(0, 168, 155))) 
							counter++;
					}
					if (counter == 2)
						summCorrect++;
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
		c.insets = new Insets(0, 0, 0, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;

		add(questionButton, c);

		c.gridy = 1;

		add(task, c);

		c.gridy = 3;

		add(nextButton, c);

		this.revalidate();
		this.repaint();

		testDate = new Date();
		testTime = new Date().getTime();
		showQuestion();
	}

	public void showQuestion() {

		// Clean-up
		for (int i = 0; i < answers.size(); i++) {
			this.remove(answers.get(i));
		}
		answers.clear();
		summSelected = 0;

		NodeList answersText = doc.getElementsByTagName("q").item(currentQuestionNumber).getChildNodes();

		questionButton.setText(doc.getElementsByTagName("q").item(currentQuestionNumber).getAttributes()
				.getNamedItem("word").getNodeValue().toUpperCase());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(0, 0, 0, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;

		AnswersListener l = new AnswersListener();

		for (int i = 0; i < answersText.getLength(); i++) {
			JButton b = new JButton(answersText.item(i).getTextContent());
			b.setUI(new BorderButtonCustomUI(new Color(144, 106, 96)));
			b.setBorder(null);
			b.setOpaque(false);
			b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			// TODO button padding?
			Font f = ((BorderButtonCustomUI) b.getUI()).getFont();
			FontMetrics fm = b.getFontMetrics(f);
			int w = (int) fm.getStringBounds(b.getText(), b.getGraphics()).getWidth();
			b.setPreferredSize(new Dimension(w + 40, 35));
			b.addActionListener(l);

			answers.add(b);

			c.gridx = i;

			// this.add(b, c);
		}

		for (int i = 0; i < answers.size(); i++) {
			c.gridx = i;
			this.add(answers.get(i), c);
		}

		this.revalidate();
		this.repaint();

	}

	@Override
	public void showResults() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showSettings() {
		showStandartSettings();
	}

	class AnswersListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();

			if (((BorderButtonCustomUI) b.getUI()).getBorderColor().equals(new Color(0, 168, 155))) {
				((BorderButtonCustomUI) b.getUI()).setBorderColor(new Color(144, 106, 96));
				summSelected--;
			} else {
				if (summSelected < 2) {
					((BorderButtonCustomUI) b.getUI()).setBorderColor(new Color(0, 168, 155));
					summSelected++;
				}
			}

			b.repaint();
		}
	}

}
