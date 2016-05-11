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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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

	int currentQuestionNumber = 0;
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
		// TODO layout, high priority

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
				// TODO time?
				if (currentQuestionNumber >= doc.getElementsByTagName("q").getLength() - 1) {
					showResults();
				} else {
					// TODO calculate results, high priority
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

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(40, 0, 40, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;
		add(set, c);

		c.gridy = 1;
		add(task, c);

		c.gridy = 2;
		add(nextButton, c);

		c.gridheight = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 0;
		add(cards, c);

		c.gridx = 0;
		c.gridy = 3;
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
					showResults();
				}

			}
		});
		timer.start();

		showQuestion();
	}

	public void showQuestion() {
		// TODO layout, high priority

		Node n = doc.getElementsByTagName("q").item(currentQuestionNumber);
		set.setText("<html><div style='font: 24pt Arial Narrow; color: rgb(0, 168, 155);'>"
				+ n.getAttributes().getNamedItem("set").getNodeValue().toUpperCase() + "</div></html>");
		task.setText("<html><div style='font: 24pt Arial Narrow; color: rgb(0, 168, 155);'>"
				+ n.getAttributes().getNamedItem("task").getNodeValue().toUpperCase() + "</div></html>");

		cards.removeAll();

		ImageIcon icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RAVEN)
				+ n.getAttributes().getNamedItem("image").getNodeValue());
		JLabel question = new JLabel();
		question.setIcon(icon);
		cards.add(question);

		answers.removeAll();
		NodeList a = n.getChildNodes();
		answers.setLayout(new GridLayout(2, a.getLength() / 2));
		AnswersMouseListener l = new AnswersMouseListener();
		for (int i = 0; i < a.getLength(); i++) {
			icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RAVEN)
					+ a.item(i).getTextContent());
			JLabel v = new JLabel();
			v.setName(Integer.toString(i));
			v.setIcon(icon);
			v.addMouseListener(l);
			v.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			answers.add(v);
		}
		
		cards.add(answers);
		
		selected = 0;

		this.revalidate();
		this.repaint();
	}

	@Override
	public void showResults() {
		showStandartResults();
		// TODO calculate and output results, do conclusion, high priority
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

}
