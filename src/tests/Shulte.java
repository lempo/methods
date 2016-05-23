package tests;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import defaults.InterfaceTextDefaults;
import methods.Methods;
import methods.Test;

public class Shulte extends AbstractTest {

	private final int tableNum = 5;
	private final int n = 5;
	private final int cellSize = 75;
	private final int borderSize = 3;
	private Color borderColor = new Color(144, 106, 96);

	private int progress = 1;

	private int tableCounter = 0;

	private long[] times = new long[tableNum];

	JPanel table;
	JLabel[] cells;
	JLabel heading;

	public Shulte(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	private void generateTable(int[][] table) {
		Random rnd = new Random(new Date().getTime());

		int tyk1, tyk2;
		for (int i = 1; i <= n * n;)
			while (true) {
				tyk1 = rnd.nextInt(n);
				tyk2 = rnd.nextInt(n);
				if (table[tyk1][tyk2] != 0)
					break;
				else {
					table[tyk1][tyk2] = i;
					i++;
					break;
				}
			}
	}

	private void showTable(int next) {

		int[][] content = new int[n][n];
		generateTable(content);

		for (int k = 0; k < n * n; k++) {
			cells[k].setText("<html><div style='font: bold 36pt Arial; color: rgb(144, 106, 96);'>"
					+ Integer.toString(content[k / n][k % n]) + "</div></html>");
			cells[k].setName(Integer.toString(content[k / n][k % n]));
		}

		table.revalidate();
		table.repaint();

		times[next] = new Date().getTime();
	}
	
	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		progress = 1;
		tableCounter = 0;

		removeAll();
		setLayout(new GridBagLayout());

		table = new JPanel();
		table.setOpaque(false);
		table.setLayout(new GridLayout(n, n));

		cells = new JLabel[n * n];
		CellsMouseListener l = new CellsMouseListener();
		for (int k = 0; k < n * n; k++) {
			cells[k] = new JLabel();
			cells[k].setBorder(BorderFactory.createMatteBorder(0, 0, borderSize, borderSize, borderColor));
			if (k % n == 0)
				cells[k].setBorder(BorderFactory.createMatteBorder(0, borderSize, borderSize, borderSize, borderColor));
			if (k / n == 0)
				cells[k].setBorder(BorderFactory.createMatteBorder(borderSize, 0, borderSize, borderSize, borderColor));
			if ((k % n == 0) && (k / n == 0))
				cells[k].setBorder(
						BorderFactory.createMatteBorder(borderSize, borderSize, borderSize, borderSize, borderColor));

			cells[k].setPreferredSize(new Dimension(cellSize, cellSize));
			cells[k].setHorizontalAlignment(JLabel.CENTER);
			table.add(cells[k]);
			cells[k].addMouseListener(l);
			cells[k].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			cells[k].setOpaque(true);
			cells[k].setBackground(Color.WHITE);
		}

		heading = new JLabel();
		String t = "<html><div style='font: bold 24pt Arial Narrow; color: rgb(144, 106, 96);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("table").toUpperCase() + " "
				+ Integer.toString(tableCounter + 1) + "</div></html>";
		heading.setText(t);

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 10, 50, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;

		add(heading, c);

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 100, 0);

		add(table, c);

		revalidate();
		repaint();

		showTable(0);

		testDate = new Date();
		testTime = new Date().getTime();
	}

	@Override
	public void showResults() {

		showStandartResults();

		float resultER = 0;
		for (int i = 0; i < times.length; i++)
			resultER += times[i];
		resultER /= times.length;
		resultER /= 1000;

		float resultVR = times[0] / resultER / 1000;
		float resultPU = times[3] / resultER / 1000;

		JLabel leftCol = new JLabel();
		String t = "<html><div style='font: 20pt Arial Narrow; color: rgb(144, 106, 96); text-align: right;'>"
				+ InterfaceTextDefaults.getInstance().getDefault("results_ER") + ": <br>"
				+ InterfaceTextDefaults.getInstance().getDefault("results_VR") + ": <br>"
				+ InterfaceTextDefaults.getInstance().getDefault("results_PU") + ": <br>" + "</div></html>";
		leftCol.setText(t);

		JLabel rightCol = new JLabel();

		DecimalFormat df = new DecimalFormat("#.##");
		t = "<html><div style='font: bold 20pt Arial; color: rgb(38, 166, 154);'>" + df.format(resultER) + "<br>"
				+ df.format(resultVR) + "<br>" + df.format(resultPU) + "<br>" + "</div></html>";
		rightCol.setText(t);

		/*
		 * t =
		 * "<html><div style='font: bold 20pt Arial; color: rgb(144, 106, 96); padding: 10px'>"
		 * ; if (summCorrect >= 0 && summCorrect <= 10) t +=
		 * d.item(1).getTextContent().toUpperCase(); if (summCorrect >= 11 &&
		 * summCorrect <= 14) t += d.item(2).getTextContent().toUpperCase(); if
		 * (summCorrect >= 15 && summCorrect <= 20) t +=
		 * d.item(3).getTextContent().toUpperCase(); t += "</div></html>";
		 * JPanel conclusion = new JPanel(); conclusion.add(new JLabel(t));
		 * conclusion.setUI(new PanelCustomUI(true));
		 */

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
		// leftCol.setPreferredSize(new Dimension(300, 350));
		leftCol.setVerticalAlignment(JLabel.TOP);
		resultsPanel.add(leftCol, c);

		c.gridx = 1;

		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 0, 0);
		c.gridwidth = 1;
		// rightCol.setPreferredSize(new Dimension(300, 350));
		rightCol.setVerticalAlignment(JLabel.TOP);
		resultsPanel.add(rightCol, c);

		/*
		 * c.anchor = GridBagConstraints.CENTER; c.insets = new Insets(20, 0, 0,
		 * 0); c.gridwidth = 2; c.gridx = 0; c.gridy = 2;
		 * resultsPanel.add(conclusion, c);
		 */

		this.revalidate();
		this.repaint();

	}

	@Override
	public void showSettings() {
		showStandartSettings();
	}

	class CellsMouseListener implements MouseListener {
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
			if (tableCounter == tableNum)
				return;
			JLabel l = (JLabel) e.getSource();
			int i = Integer.parseInt(l.getName());
			if (i == progress) {
				l.setBackground(Color.GREEN);
				progress++;
			} else
				l.setBackground(Color.RED);
			l.repaint();
			if (progress > n * n) {
				times[tableCounter] = new Date().getTime() - times[tableCounter];
				tableCounter++;
				if (tableCounter == tableNum) {
					testTime = new Date().getTime() - testTime;
					showResults();
					return;
				}
				progress = 1;
				showTable(tableCounter);
				String t = "<html><div style='font: bold 24pt Arial Narrow; color: rgb(144, 106, 96);'>"
						+ InterfaceTextDefaults.getInstance().getDefault("table").toUpperCase() + " "
						+ Integer.toString(tableCounter + 1) + "</div></html>";
				heading.setText(t);
				heading.repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			l.setBackground(Color.WHITE);
			l.repaint();
		}
	}

	@Override
	public void printResults() {
		standartPrintResults();
	}

}
