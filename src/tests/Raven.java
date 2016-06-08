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
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import customui.ButtonCustomUI;
import customui.ProgressBarCustomUI;
import defaults.ImageLinkDefaults;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;

public class Raven extends AbstractTest {

	Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.RAVEN));

	int summ = 0;
	int currentQuestionNumber = 0;
	int convertIQ[] = 
		{  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		       0,  0,  0,  0, 62, 65, 65, 66, 67, 69,
		      70, 71, 72, 73, 75, 76, 77, 79, 80, 82,
		      83, 84, 86, 87, 88, 90, 91, 92, 94, 95,
		      96, 98, 99,100,102,104,106,108,110,112,
		     114,116,118,120,122,124,126,128,130,140
		};	
	
	JLabel set;
	JLabel task;
	JPanel cards;
	JPanel answers;

	private Timer timer;
	private JProgressBar bar;
	private JLabel timeLeft;

	private final int TIME = 30;

	private int selected;

	public Raven(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		currentQuestionNumber = 0;

		task = new JLabel();
		set = new JLabel();
		answers = new JPanel();
		answers.setOpaque(false);
		cards = new JPanel();
		cards.setOpaque(false);
		cards.setLayout(new BoxLayout(cards, BoxLayout.Y_AXIS));

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
					timer.stop();
					testTime = new Date().getTime() - testTime;
					showResults();
				} else {
					currentQuestionNumber++;
					showQuestion();
				}
			}
		});

		bar = new JProgressBar();
		bar.setStringPainted(false);
		bar.setMinimum(0);
		bar.setMaximum(TIME);
		bar.setValue(0);
		bar.setUI(new ProgressBarCustomUI());
		bar.setBorder(null);

		timeLeft = new JLabel("<html><div style='font: 16pt Arial Narrow; color: rgb(144, 106, 96);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("time_left").toUpperCase() + ": " + TIME + " "
				+ InterfaceTextDefaults.getInstance().getDefault("min").toUpperCase() + "</div></html>");

		this.removeAll();
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 20, 0, 120);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;
		add(set, c);

		c.gridy = 1;
		add(task, c);

		c.anchor = GridBagConstraints.SOUTHWEST;
		c.ipady = 5;
		c.weighty = 0.1;
		c.gridy = 2;
		add(nextButton, c);

		c.anchor = GridBagConstraints.SOUTHWEST;
		c.insets = new Insets(0, 0, 0, 20);
		c.gridheight = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 0;
		add(cards, c);

		c.anchor = GridBagConstraints.SOUTHWEST;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(0, 20, 0, 20);
		add(timeLeft, c);

		c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 4;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		add(bar, c);

		this.revalidate();
		this.repaint();

		timer = new Timer(1000 * 60, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {

				if (bar.getValue() < TIME) {
					bar.setValue(bar.getValue() + 1);
					timeLeft.setText("<html><div style='font: 16pt Arial Narrow; color: rgb(144, 106, 96);'>"
							+ InterfaceTextDefaults.getInstance().getDefault("time_left").toUpperCase() + ": "
							+ (TIME - bar.getValue()) + " "
							+ InterfaceTextDefaults.getInstance().getDefault("min").toUpperCase() + "</div></html>");
					timeLeft.repaint();
				} else {
					timer.stop();
					testTime = new Date().getTime() - testTime;
					showResults();
				}

			}
		});
		timer.start();

		testDate = new Date();
		testTime = new Date().getTime();
		showQuestion();
	}

	public void showQuestion() {

		Node n = doc.getElementsByTagName("q").item(currentQuestionNumber);
		set.setText("<html><div style='font: 24pt Arial Narrow; color: rgb(0, 168, 155);'>"
				+ n.getAttributes().getNamedItem("set").getNodeValue().toUpperCase() + "</div></html>");
		task.setText("<html><div style='font: 24pt Arial Narrow; color: rgb(144, 106, 96);'>"
				+ n.getAttributes().getNamedItem("task").getNodeValue().toUpperCase() + "</div></html>");

		cards.removeAll();

		ImageIcon icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RAVEN)
				+ n.getAttributes().getNamedItem("image").getNodeValue());
		JLabel question = new JLabel();
		question.setIcon(icon);
		cards.add(question);
		question.setAlignmentX(Component.LEFT_ALIGNMENT);

		answers.removeAll();
		NodeList a = n.getChildNodes();
		answers.setLayout(new GridLayout(2, a.getLength() / 2));
		AnswersMouseListener l = new AnswersMouseListener();
		for (int i = 0; i < a.getLength(); i++) {
			icon = Utils.createImageIcon(
					ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RAVEN) + a.item(i).getTextContent());
			JLabel v = new JLabel();
			v.setName(Integer.toString(i));
			v.setIcon(icon);
			v.addMouseListener(l);
			v.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			answers.add(v);
		}
		answers.setMaximumSize(new Dimension(icon.getIconWidth() * (a.getLength() / 2), icon.getIconHeight() * 2));
		cards.add(answers);
		answers.setAlignmentX(Component.LEFT_ALIGNMENT);

		selected = 0;
		Dimension d = answers.getComponents()[0].getPreferredSize();
		((JLabel) answers.getComponents()[0])
				.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(38, 166, 154)));
		answers.getComponents()[0].setPreferredSize(d);

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
				+ "</div></html>";
		leftCol.setText(t);	
			
		t = "<html><div style='font: bold 20pt Arial; color: rgb(38, 166, 154);'>"
				+ convertIQ[summ] + "<br>" 
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

	class AnswersMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			selected = Integer.parseInt(l.getName());
			for (Component a : answers.getComponents()) {
				((JLabel) a).setBorder(null);
			}
			Dimension d = l.getPreferredSize();
			l.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(38, 166, 154)));
			l.setPreferredSize(d);
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
