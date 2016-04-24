package customui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;

public class BorderButtonCustomUI extends BasicButtonUI {
	private Color borderColor = null;

	public BorderButtonCustomUI(Color borderColor) {
		super();
		this.borderColor = borderColor;
	}

	@Override
	public void paint(Graphics g, JComponent c) {

		AbstractButton b = (AbstractButton) c;

		Rectangle area = new Rectangle();
		SwingUtilities.calculateInnerArea(b, area);
		g.setColor(borderColor);
		((Graphics2D) g).setStroke(new BasicStroke(3));
		g.drawRoundRect(area.x + 2, area.y + 2, area.width - 4, area.height - 4, 10, 10);

		super.paint(g, c);
	}

	@Override
	protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
		AbstractButton b = (AbstractButton) c;
		Font f = new Font("Arial Narrow", Font.BOLD, 17);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics(f);

		g.setColor(borderColor);
		BasicGraphicsUtils.drawString(g, text.toUpperCase(), -1, textRect.x - 4, textRect.y + fm.getAscent() - 3);
	}
}
