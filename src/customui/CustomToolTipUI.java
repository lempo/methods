package customui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicToolTipUI;

import component.DropShadowBorder;

public class CustomToolTipUI extends BasicToolTipUI {
	Color border = new Color(120, 144, 156);

	@Override
	public void paint(Graphics g, JComponent c) {

		JToolTip tip = (JToolTip) c;
		tip.setOpaque(false);
		tip.setBorder(null);
		tip.setBackground(Color.WHITE);
		Rectangle area = new Rectangle();
		SwingUtilities.calculateInnerArea(c, area);

		g.setColor(Color.WHITE);
		g.fillRoundRect(area.x, area.y, area.width - 5, area.height - 5, 8, 8);
		g.setColor(border);
		g.drawRoundRect(area.x, area.y, area.width - 5, area.height - 5, 8, 8);
		super.paint(g, c);
		DropShadowBorder shadow = new DropShadowBorder();
		shadow.setShadowColor(Color.GRAY);
		shadow.setShadowSize(5);
		shadow.setShadowOpacity((float) 0.5);
		shadow.setShowRightShadow(true);
		shadow.setShowBottomShadow(true);
		tip.setBorder(shadow);
	}
}
