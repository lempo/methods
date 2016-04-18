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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import methods.Methods;
import methods.Test;

public class Shulte extends AbstractTest {

	private final int n = 5;
	private final int cellSize = 75;
	private final int borderSize = 3;
	private Color borderColor = new Color(144, 106, 96);
	
	private int progress = 1;

	public Shulte(Methods methods, int width, int height, Test test) {
		super(methods, width, height, test);
	}

	@Override
	public void showInfo() {
		showStandartInfo();
	}

	@Override
	public void showTest() {
		// TODO Auto-generated method stub
		progress = 1;
		
		JPanel table = new JPanel();
		table.setOpaque(false);
		table.setLayout(new GridLayout(n, n));
		JLabel[] cells = new JLabel[n * n];
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

			cells[k].setText("<html><div style='font: bold 36pt Arial; color: rgb(144, 106, 96);'>"
					+ Integer.toString(k + 1) + "</div></html>");

			cells[k].setPreferredSize(new Dimension(cellSize, cellSize));
			cells[k].setHorizontalAlignment(JLabel.CENTER);
			table.add(cells[k]);
			cells[k].setName(Integer.toString(k + 1));
			cells[k].addMouseListener(l);
			cells[k].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			cells[k].setOpaque(true);
			cells[k].setBackground(Color.WHITE);
		}

		removeAll();
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(20, 40, 0, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;

		add(table, c);

		revalidate();
		repaint();
	}

	@Override
	public void showResults() {
		// TODO Auto-generated method stub

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
			JLabel l = (JLabel) e.getSource();
			int i = Integer.parseInt(l.getName());
			if (i == progress) {
				l.setBackground(Color.GREEN);
				progress++;
			}
			else
				l.setBackground(Color.RED);
			l.repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			l.setBackground(Color.WHITE);
			l.repaint();
		}
	}

}
