package component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.FocusEvent;

public class CustomTextField extends JTextField {
	private static final long serialVersionUID = -4303240544556069274L;
	Color back = new Color(245, 235, 233);
	Color border = new Color(144, 106, 96);

	String hiht;

	public CustomTextField(int i, String hint) {
		super(i);
		this.hiht = hint;
		this.setBorder(null);
		this.setOpaque(false);
		this.setFont(new Font("ArialNarrow", Font.PLAIN, 14));
		this.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				(int) (this.getPreferredSize().getHeight() + 15)));
		setForeground(Color.GRAY);
		setText(hint);

		this.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				String s = getText().trim();
				if (s.equals(hint)) {
					setText("");
					setForeground(new Color(144, 106, 96));
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				String s = getText().trim();
				if (s.length() == 0) {
					setText(hint);
					setForeground(Color.GRAY);
				}
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		Rectangle area = new Rectangle();
		SwingUtilities.calculateInnerArea(this, area);
		g.setColor(back);
		g.fillRoundRect(area.x + 2, area.y + 2, area.width - 4, area.height - 4, 6, 6);
		g.setColor(border);
		((Graphics2D) g).setStroke(new BasicStroke(2));
		g.drawRoundRect(area.x + 2, area.y + 2, area.width - 4, area.height - 4, 6, 6);
		String result = this.getText().replaceAll("   ", "");
		this.setText("   " + result);
		super.paintComponent(g);
	}
}
