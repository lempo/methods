package tests;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import customui.ButtonCustomUI;
import customui.ScrollBarCustomUI;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;
import tests.Munsterberg.LettersMouseListener;

public class Correction extends AbstractTest {

	final int stringsNum = 40;
	final int lettersNum = 50;

	Color unselected = new Color(144, 106, 96);
	Color selected = new Color(38, 166, 154);

	int minuteCounter = 0;
	int cherta = 0;
	int summCorrect[];
	int summIncorrect[];
	int summMissed[];

	JLabel[] letters;
	JButton firstLetter;
	JButton secondLetter;
	Random r = new Random(new Date().getTime());

	private Timer timer;
	char[] alphabet = {'à', 'á', 'â', 'ã', 'ä', 'å', 'æ', 'ç', 'è', 'ê', 
			'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò', 'ó', 'ô', 
			'õ', 'ö', '÷', 'ø', 'ü', 'ý', 'þ', 'ÿ'};

	public Correction(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		minuteCounter = 0;
		cherta = 0;

		this.removeAll();

		letters = new JLabel[stringsNum * lettersNum];

		JPanel table = new JPanel();
		table.setOpaque(false);
		table.setLayout(new GridLayout(stringsNum, lettersNum));

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 2;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;

		LettersMouseListener l = new LettersMouseListener();

		for (int i = 0; i < stringsNum * lettersNum; i++) {
			letters[i] = new JLabel((alphabet[r.nextInt(alphabet.length)] + "").toUpperCase());
			letters[i].addMouseListener(l);
			letters[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			letters[i].setFont(new Font("Arial", Font.BOLD, 14));
			letters[i].setForeground(unselected);
			table.add(letters[i]);
		}

		// TODO scroll setOpaque
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension((int) Math.round(width * 0.9), (int) Math.round(height * 0.75)));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setBorder(null);
		scroll.getViewport().setOpaque(false);
		scroll.getVerticalScrollBar().setUI(new ScrollBarCustomUI());
		this.add(scroll, c);

		firstLetter = new JButton((alphabet[r.nextInt(alphabet.length)] + "").toUpperCase()); 
		firstLetter.setUI(new ButtonCustomUI(new Color(38, 166, 154)));
		firstLetter.setBorder(null);
		firstLetter.setOpaque(false);
		firstLetter.setPreferredSize(new Dimension(35, 35));
		char tempLetter = alphabet[r.nextInt(alphabet.length)];
		while (tempLetter == firstLetter.getText().charAt(0))
			tempLetter = alphabet[r.nextInt(alphabet.length)];
		secondLetter = new JButton((tempLetter + "").toUpperCase());
		secondLetter.setUI(new ButtonCustomUI(new Color(38, 166, 154)));
		secondLetter.setBorder(null);
		secondLetter.setOpaque(false);
		secondLetter.setPreferredSize(new Dimension(35, 35));

		c.gridx = 1;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.weighty = 1.0;
		this.add(firstLetter, c);

		c.gridy = 1;
		c.weighty = 0.0;
		this.add(secondLetter, c);

		JButton toResults = new JButton(InterfaceTextDefaults.getInstance().getDefault("finish_test"));
		toResults.setUI(new ButtonCustomUI(new Color(144, 106, 96)));
		toResults.setBorder(null);
		toResults.setOpaque(false);
		toResults.setPreferredSize(new Dimension(200, 35));
		toResults.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		toResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testTime = new Date().getTime() - testTime;
				timer.stop();
				showResults();
			}
		});

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.insets = new Insets(30, 0, 30, 0);

		this.add(toResults, c);

		this.revalidate();
		this.repaint();

		testDate = new Date();
		testTime = new Date().getTime();

		timer = new Timer(1000 * 60, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				minuteCounter++;
				// TODO Calculate mid-results - not needed now

				if (minuteCounter >= 8) {
					timer.stop();
					testTime = new Date().getTime() - testTime;
					showResults();
				} else {
					// Do something? Or not?
				}

			}
		});
		timer.start();
	}

	@Override
	public void showResults() {
		this.showStandartResults();
		// TODO count and show results, later

	}

	@Override
	public void showSettings() {
		showStandartSettings();
	}

	class LettersMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			if (l.getForeground().equals(unselected))
				l.setForeground(selected);
			else
				l.setForeground(unselected);
			l.repaint();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}

	@Override
	public void printResults() {
		standartPrintResults();
	}

}
