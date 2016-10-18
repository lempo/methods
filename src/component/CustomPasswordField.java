package component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CustomPasswordField extends JTextField {
	private static final long serialVersionUID = -4303240544556069274L;
	Color back = new Color(245, 235, 233);
	Color border = new Color(144, 106, 96);
	Color text = new Color(144, 106, 96);

	String hiht;
	String pass = "";

	public CustomPasswordField(int i, String hint) {
		super(i);
		this.hiht = hint;
		this.setBorder(null);
		this.setOpaque(false);
		this.setFont(new Font("ArialNarrow", Font.PLAIN, 14));
		this.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				(int) (this.getPreferredSize().getHeight() + 15)));
		setForeground(Color.GRAY);
		setText("   " + hint);

		this.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				String s = getText().trim();
				if (s.equals(hint)) {
					setText("   ");
					setForeground(text);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				String s = getText().trim();
				if (s.length() == 0) {
					setText("   " + hint);
					setForeground(Color.GRAY);
				}
			}
		});

		getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
			}

			public void removeUpdate(DocumentEvent e) {
				if (e.getOffset() > 0) {
					if (e.getOffset() < 3) {
						Runnable doHighlight = new Runnable() {
					        @Override
					        public void run() {
					        	setText("   ");
					        }
					    };       
					    SwingUtilities.invokeLater(doHighlight);
					}
					else {
						pass = pass.substring(0, pass.length() - 1);
					}
				}
			}

			public void insertUpdate(DocumentEvent e) {
				if (e.getOffset() > 0) {
					Runnable doHighlight = new Runnable() {
				        @Override
				        public void run() {
				        	pass = pass + getText().charAt(getText().length() - 1);
				        	setText("   " + getText().substring(3, getText().length()).replaceAll(".", "*"));
				        }
				    };       
				    SwingUtilities.invokeLater(doHighlight);
				}
			}

		});
	}

	public CustomPasswordField(int i, String hint, Color back, Color border, Color text) {
		this(i, hint);
		this.back = back;
		this.border = border;
		this.text = text;
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
		//String result = this.getText().replaceAll("   ", "");
		//this.setText("   " + result);
		super.paintComponent(g);
	}

	public String getPass() {
		return pass;
	}
}
