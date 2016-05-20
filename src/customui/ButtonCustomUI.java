package customui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;

public class ButtonCustomUI extends BasicButtonUI {
	private Color color;
	private Color borderColor = null;
	private Font font = new Font("Arial Narrow", Font.PLAIN, 17);

	public ButtonCustomUI(Color color) {
		super();
		this.color = color;
	}

	public ButtonCustomUI(Color color, Color borderColor) {
		super();
		this.color = color;
		this.borderColor = borderColor;
	}

	@Override
	protected void paintButtonPressed(Graphics g, AbstractButton b) {
		if (b.isContentAreaFilled() && b.isOpaque()) {
			Rectangle area = new Rectangle();
			SwingUtilities.calculateInnerArea(b, area);
			g.setColor(color);
			g.fillRoundRect(area.x - 1, area.y - 1, area.width - 3, area.height - 3, 12, 12);
		}
	}

	@Override
	public void paint(Graphics g, JComponent c) {

		AbstractButton b = (AbstractButton) c;

		Rectangle area = new Rectangle();
		SwingUtilities.calculateInnerArea(b, area);
		g.setColor(color);
		g.fillRoundRect(area.x - 1, area.y - 1, area.width - 3, area.height - 3, 12, 12);

		if (borderColor != null) {
			g.setColor(borderColor);
			g.drawRoundRect(area.x, area.y, area.width - 4, area.height - 4, 10, 10);
		}

		super.paint(g, c);
	}

	@Override
	protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
		AbstractButton b = (AbstractButton) c;
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics(font);

		g.setColor(Color.WHITE);
		int w = (int) fm.getStringBounds(b.getText(), b.getGraphics()).getWidth();
		Rectangle area = new Rectangle();
		SwingUtilities.calculateInnerArea(b, area);
		int b_w = area.width;

		Icon i = currentIcon(b);
		if (i == null)
			BasicGraphicsUtils.drawString(g, text, -1, (int) Math.round((b_w - w) / 2.0 - textRect.getWidth() * 0.03),
					textRect.y + fm.getAscent() - 3);
		else
			BasicGraphicsUtils.drawString(g, text, -1, textRect.x - 10, textRect.y + fm.getAscent() - 3);
	}

	public Font getFont() {
		return font;
	}

	protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
		AbstractButton b = (AbstractButton) c;
		Icon i = currentIcon(b);

		if (i != null)
			i.paintIcon(c, g, iconRect.x - 10, iconRect.y);
	}

	static Icon currentIcon(AbstractButton b) {
		Icon i = b.getIcon();
		ButtonModel model = b.getModel();

		if (model.isPressed() && b.getPressedIcon() != null && b.isEnabled())
			i = b.getPressedIcon();

		else if (model.isRollover()) {
			if (b.isSelected() && b.getRolloverSelectedIcon() != null)
				i = b.getRolloverSelectedIcon();
			else if (b.getRolloverIcon() != null)
				i = b.getRolloverIcon();
		}

		else if (b.isSelected() && b.isEnabled()) {
			if (b.isEnabled() && b.getSelectedIcon() != null)
				i = b.getSelectedIcon();
			else if (b.getDisabledSelectedIcon() != null)
				i = b.getDisabledSelectedIcon();
		}

		else if (!b.isEnabled() && b.getDisabledIcon() != null)
			i = b.getDisabledIcon();

		return i;
	}
}
