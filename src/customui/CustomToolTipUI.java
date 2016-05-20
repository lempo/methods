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
	Color border = new Color(144, 106, 96);

	@Override
	public void paint(Graphics g, JComponent c) {

		JToolTip tip = (JToolTip) c;
		tip.setOpaque(false);
		tip.setBorder(null);
		tip.setBackground(Color.WHITE);
		Rectangle area = new Rectangle();
		SwingUtilities.calculateInnerArea(c, area);

		g.setColor(Color.WHITE);
		g.fillRoundRect(area.x + 14, area.y, area.width - 5 - 14, area.height - 5, 8, 8);
		//            .
		//         y2 .
		//         .
		//    y1 .
		//         .
		//        y3  .
		//            .
		int x1 = area.x;
		int x2 = 14;
		int x3 = 14;
		int y1 = (int) (area.y + Math.round((area.height - 5) / 2.0));
		int y2 = (int) (area.y + Math.round((area.height - 5) / 2.0) - 7);
		int y3 = (int) (area.y + Math.round((area.height - 5) / 2.0) + 7);
		int xpoints[] = { x1, x2, x3 };
		int ypoints[] = { y1, y2, y3 };
		int npoints = 3;
		g.fillPolygon(xpoints, ypoints, npoints);

		g.setColor(border);
		g.drawRoundRect(area.x + 14, area.y, area.width - 5 - 14, area.height - 5, 8, 8);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x1, y1, x3, y3);

		g.setColor(Color.WHITE);
		g.drawLine(x2, y2, x3, y3);

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
