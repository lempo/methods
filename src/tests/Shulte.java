package tests;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import customui.ButtonCustomUI;
import defaults.ImageLinkDefaults;
import defaults.InterfaceTextDefaults;
import methods.Methods;
import methods.Test;
import methods.Utils;

public class Shulte extends AbstractTest {

	private final int tableNum = 1;
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
		// TODO
		for (int i = 0; i < table.length; i++)
			for (int j = 0; j < table[0].length; j++)
				table[i][j] = i * table.length + j + 1;
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
		// TODO
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		// TODO Auto-generated method stub
		progress = 1;
		
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

		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, (int) (-table.getPreferredSize().getWidth() * 0.7), 50, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;

		add(heading, c);

		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 0, 0);

		add(table, c);

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

		c.anchor = GridBagConstraints.SOUTHEAST;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 2;
		c.insets = new Insets(50, 0, 0, (int) (-table.getPreferredSize().getWidth() * 0.7));

		add(toResults, c);

		revalidate();
		repaint();
		
		showTable(0);
		
		testDate = new Date();
		testTime = new Date().getTime();
	}

	@Override
	public void showResults() {
		// TODO Auto-generated method stub
		showStandartResults();
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
					// TODO show results
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

}