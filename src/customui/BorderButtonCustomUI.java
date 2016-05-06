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
	private Color textColor = null;
	private Font font = new Font("Arial Narrow", Font.BOLD, 17);

	public BorderButtonCustomUI(Color borderColor) {
		super();
		this.borderColor = borderColor;
	}

	public BorderButtonCustomUI(Color borderColor, Color textColor) {
		super();
		this.borderColor = borderColor;
		this.textColor = textColor;
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
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);

		if (textColor != null)
			g.setColor(textColor);
		else
			g.setColor(borderColor);

		int w = (int) fm.getStringBounds(b.getText(), b.getGraphics()).getWidth();
		Rectangle area = new Rectangle();
		SwingUtilities.calculateInnerArea(b, area);
		int b_w = area.width;

		BasicGraphicsUtils.drawString(g, text.toUpperCase(), -1, (int) Math.round((b_w - w) / 2.0),
				textRect.y + fm.getAscent() - 3);
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public Font getFont() {
		return font;
	}
}
