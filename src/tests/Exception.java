package tests;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import customui.ButtonCustomUI;
import defaults.ImageLinkDefaults;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;
import tests.Raven.AnswersMouseListener;

public class Exception extends AbstractTest {

	Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.EXCEPTION));

	JLabel[] answers = new JLabel[4];
	JPanel answersPanel = new JPanel();

	int currentQuestionNumber = 0;
	int summ = 0;

	private final int TABLESIZE = 2;

	private int selected = 0;
	
	public Exception(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		currentQuestionNumber = 0;

		JLabel question = new JLabel();
		String s = doc.getElementsByTagName("text").item(0).getTextContent();
		question.setText("<html><div style='font: 24pt Arial Narrow; color: rgb(0, 168, 155);'>" + s.toUpperCase()
				+ "</div></html>");

		answersPanel.setOpaque(false);
		answersPanel.setLayout(new GridLayout(TABLESIZE, TABLESIZE, 4, 4));
		AnswersMouseListener l = new AnswersMouseListener();
		for (int i = 0; i < TABLESIZE * TABLESIZE; i++) {
			answers[i] = new JLabel();
			answersPanel.add(answers[i]);
			answers[i].addMouseListener(l);
			answers[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		JButton nextButton = new JButton(InterfaceTextDefaults.getInstance().getDefault("next"));
		nextButton.setUI(new ButtonCustomUI(new Color(144, 106, 96)));
		nextButton.setBorder(null);
		nextButton.setOpaque(false);
		nextButton.setPreferredSize(new Dimension(200, 35));
		nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selected + 1 == Integer.parseInt(doc.getElementsByTagName("q").item(currentQuestionNumber).getAttributes().getNamedItem("answer").getNodeValue()))
					summ++;
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
		selected = 0;

		Node n = doc.getElementsByTagName("q").item(currentQuestionNumber);

		NodeList pic = n.getChildNodes();
		
		ImageIcon icon = Utils
				.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.EXCEPTION)
						+ pic.item(0).getTextContent().replaceAll(".png", "_rollover.png"));
		answers[0].setIcon(icon);
		answers[0].setName("0");
		
		for (int i = 1; i < pic.getLength(); i++) {
			icon = Utils
					.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.EXCEPTION)
							+ pic.item(i).getTextContent());
			answers[i].setIcon(icon);
			answers[i].setName(Integer.toString(i));
		}
	}

	@Override
	public void showResults() {
		showStandartResults();
		
		JLabel leftCol = new JLabel();
		JLabel rightCol = new JLabel();
		
		NodeList d = doc.getElementsByTagName("d");
			
		String t = "<html><div style='font: 20pt Arial Narrow; color: rgb(144, 106, 96); text-align: right;'>"
				+ d.item(0).getTextContent() + ": <br>" 
				+ "</div></html>";
		leftCol.setText(t);	
			
		t = "<html><div style='font: bold 20pt Arial; color: rgb(38, 166, 154);'>"
				+ summ + "<br>" 
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
		
		// TODO Replay button messes up the layout
	}

	@Override
	public void showSettings() {
		showStandartSettings();
	}

	class AnswersMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			selected = Integer.parseInt(l.getName());
			for (Component a : answersPanel.getComponents()) {
				String iconName = ((JLabel) a).getIcon().toString()
						.split(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.EXCEPTION))[1]
								.replaceAll("_rollover", "");
				ImageIcon icon = Utils
						.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.EXCEPTION)
								+ iconName);
				((JLabel) a).setIcon(icon);
			}
			String iconName = l.getIcon().toString()
					.split(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.EXCEPTION))[1]
							.replaceAll(".png", "_rollover.png");
			ImageIcon icon = Utils
					.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.EXCEPTION)
							+ iconName);
			l.setIcon(icon);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

	}

	@Override
	public void printResults() {
		standartPrintResults();
	}

}
