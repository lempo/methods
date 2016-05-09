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
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import customui.ButtonCustomUI;
import customui.PanelCustomUI;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import defaults.TextLinkDefaults.Key;
import methods.Methods;
import methods.Test;
import methods.Utils;

public class Munsterberg extends AbstractTest {
	
	Color unselected = new Color(144, 106, 96);
	Color selected = new Color(38, 166, 154);
	
	JLabel[][] letters;
	
	int summCorrect = 0;
	int summSkipped = 0;
	int summWrong = 0;
	
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> answers = new ArrayList<String>();

	public Munsterberg(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
	
		this.removeAll();
		
		Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.MUNSTERBERG));

		NodeList strings = doc.getElementsByTagName("blank");
		int stringNum = strings.getLength();
		
		JPanel table = new JPanel();
		table.setOpaque(false);
		table.setLayout(new GridBagLayout());
		table.setMinimumSize(new Dimension(900, 400));
		table.setMaximumSize(new Dimension(900, 400));
		table.setPreferredSize(new Dimension(900, 400));
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		
		LettersMouseListener l = new LettersMouseListener();
		
		/*for (int i = 0; i < stringNum; i++) {
			String blank = strings.item(i).getTextContent();
			letters[i] = new JLabel[blank.length()];
			
			stringPanels[i] = new JPanel();
			stringPanels[i].setOpaque(false);
			stringPanels[i].setLayout(new BoxLayout(stringPanels[i], BoxLayout.X_AXIS));
			stringPanels[i].setMinimumSize(new Dimension(900, 30));
			stringPanels[i].setMaximumSize(new Dimension(900, 30));
			stringPanels[i].setPreferredSize(new Dimension(900, 30));
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
		}*/
		
		// TODO justify labels, low priority
		
		letters = new JLabel[stringNum][];
		
		for (int i = 0; i < stringNum; i++) {
			String blank = strings.item(i).getTextContent();
			letters[i] = new JLabel[blank.length()];
			
			c.gridy = i;
			
			for (int j = 0; j < blank.length(); j++) {
				letters[i][j] = new JLabel(("" + blank.charAt(j)).toUpperCase());
				letters[i][j].addMouseListener(l);
				letters[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				letters[i][j].setFont(new Font("Arial", Font.BOLD, 14));
				letters[i][j].setForeground(unselected);
				c.gridx = j;
				table.add(letters[i][j], c);
			}
		}
		
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
		
		this.add(table, c);
		
		JButton toResults = new JButton(InterfaceTextDefaults.getInstance().getDefault("finish_test"));
		toResults.setUI(new ButtonCustomUI(new Color(144, 106, 96)));
		toResults.setBorder(null);
		toResults.setOpaque(false);
		toResults.setPreferredSize(new Dimension(200, 35));
		toResults.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		toResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testTime = new Date().getTime() - testTime;
				showResults();
			}
		});
		
		//TODO Run and intercept a 2 minute timer to end test
		
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(30, 0, 0, 0);
		
		this.add(toResults, c);
		
		this.revalidate();
		this.repaint();
		
		testDate = new Date();
		testTime = new Date().getTime();
	}

	@Override
	public void showResults() {

		this.showStandartResults();

		Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.MUNSTERBERG));
		NodeList k = doc.getElementsByTagName("key");
		for (int i = 0; i < k.getLength(); i++)
			keys.add(k.item(i).getTextContent().toUpperCase());
		
		boolean wordFound = false;
		String word = "";
		
		for (int i = 0; i < letters.length; i++) {
			for (int j = 0; j < letters[i].length; j++) {
				if (wordFound == false && letters[i][j].getForeground().equals(selected)) {
					wordFound = true;				
				}
				if (wordFound == true && letters[i][j].getForeground().equals(selected)) {
					word += letters[i][j].getText();
				}
				if  (wordFound == true && letters[i][j].getForeground().equals(unselected)) {
					wordFound = false;
					answers.add(word);
					word = "";
				}
				

			}
		}
		
		for (String i : answers) {

			if (keys.contains(i))  
				summCorrect++;
			else summWrong++;
		}
		for (String i : keys) {
			if (!answers.contains(i))  
				summSkipped++;
		}
		
		JLabel leftCol = new JLabel();
		JLabel rightCol = new JLabel();
		
		NodeList d = doc.getElementsByTagName("d");
		
		String t = "<html><div style='font: 20pt Arial Narrow; color: rgb(144, 106, 96); text-align: right;'>"
				+ d.item(0).getTextContent() + ": <br>" 
				+ d.item(1).getTextContent() + ": <br>" 
				+ d.item(2).getTextContent() + ": <br>" + 
				"</div></html>";
		leftCol.setText(t);	
			
		t = "<html><div style='font: bold 20pt Arial; color: rgb(38, 166, 154);'>"
				+ summCorrect + "<br>"
				+ summWrong + "<br>"
				+ summSkipped + "<br>";
		t += "</div></html>";
		rightCol.setText(t);
	
		/* TODO Conclusion - have to ask for a detailed interpretation
		t = "<html><div style='font: bold 20pt Arial; color: rgb(144, 106, 96); padding: 10px'>";
		if (summCorrect >= 0 && summCorrect <= 10) t += d.item(1).getTextContent().toUpperCase();
		if (summCorrect >= 11 && summCorrect <= 14) t += d.item(2).getTextContent().toUpperCase();
		if (summCorrect >= 15 && summCorrect <= 20) t += d.item(3).getTextContent().toUpperCase();
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
	
	class LettersMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			if (l.getForeground().equals(unselected))
				l.setForeground(selected);
			else
				l.setForeground(unselected);
			l.repaint();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}

}
