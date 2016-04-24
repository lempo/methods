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
	
	public PanelCustomUI(boolean fill, Color color, Color borderColor) {
		this.fill = fill;
		back = color;
		border = borderColor;
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		Rectangle area = new Rectangle();
		SwingUtilities.calculateInnerArea(c, area);
		if (fill) {
			((Graphics2D) g).setStroke(new BasicStroke(3));
			g.setColor(back);
			g.fillRoundRect(area.x + 1, area.y + 1, area.width, area.height, 8, 8);
		}
		g.setColor(border);
		g.drawRoundRect(area.x + 1, area.y + 1, area.width - 3, area.height - 3, 8, 8);
	}
}
