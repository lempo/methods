package tests;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import customui.ButtonCustomUI;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;
import tests.Munsterberg.LettersMouseListener;

public class Correction extends AbstractTest {

	Color unselected = new Color(144, 106, 96);
	Color selected = new Color(38, 166, 154);

	public Correction(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		// TODO randomize letters, middle priority

		this.removeAll();

		Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.CORRECTION));
		NodeList strings = doc.getElementsByTagName("blank");
		int stringNum = strings.getLength();

		JLabel[][] letters = new JLabel[stringNum][];

		JPanel[] stringPanels = new JPanel[stringNum];
		JPanel table = new JPanel();
		table.setOpaque(false);
		table.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;

		LettersMouseListener l = new LettersMouseListener();

		for (int i = 0; i < stringNum; i++) {
			String blank = strings.item(i).getTextContent();
			letters[i] = new JLabel[blank.length()];

			stringPanels[i] = new JPanel();
			stringPanels[i].setOpaque(false);
			stringPanels[i].setLayout(new BoxLayout(stringPanels[i], BoxLayout.X_AXIS));
			stringPanels[i].setMinimumSize(new Dimension(700, 30));
			stringPanels[i].setMaximumSize(new Dimension(700, 30));
			stringPanels[i].setPreferredSize(new Dimension(700, 30));
			stringPanels[i].add(Box.createHorizontalGlue());

			for (int j = 0; j < blank.length(); j++) {
				letters[i][j] = new JLabel(("" + blank.charAt(j)).toUpperCase());
				letters[i][j].addMouseListener(l);
				letters[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				letters[i][j].setFont(new Font("Arial", Font.BOLD, 14));
				letters[i][j].setForeground(unselected);
				stringPanels[i].add(letters[i][j]);
				letters[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				stringPanels[i].add(Box.createHorizontalGlue());
			}
			c.gridy = i;
			table.add(stringPanels[i], c);
		}

		c.gridheight = 2;
		c.gridx = 0;
		c.gridy = 0;
		this.add(table, c);
		
		JButton firstLetter = new JButton("Ê");
		firstLetter.setUI(new ButtonCustomUI(new Color(38, 166, 154)));
		firstLetter.setBorder(null);
		firstLetter.setOpaque(false);
		firstLetter.setPreferredSize(new Dimension(35, 35));
		JButton secondLetter = new JButton("Ð");
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
				showResults();
			}
		});

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		c.insets = new Insets(30, 0, 30, 0);

		this.add(toResults, c);

		this.revalidate();
		this.repaint();
		
		//TODO Add timer

	}

	@Override
	public void showResults() {
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

}
