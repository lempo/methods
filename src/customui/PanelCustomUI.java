package customui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicPanelUI;

public class PanelCustomUI extends BasicPanelUI {

	Color back = new Color(249, 245, 243);
	Color border = new Color(161, 136, 127);

	boolean fill = false;

	public PanelCustomUI(boolean fill) {
		this.fill = fill;
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		Rectangle area = new Rectangle();
		SwingUtilities.calculateInnerArea(c, area);
		if (fill) {
			((Graphics2D) g).setStroke(new BasicStroke(3));
			g.setColor(back);
			g.fillRoundRect(area.x, area.y, area.width, area.height, 8, 8);
		}
		g.setColor(border);
		g.drawRoundRect(area.x, area.y, area.width - 1, area.height - 1, 8, 8);
	}
}
