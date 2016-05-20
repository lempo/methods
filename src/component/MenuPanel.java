package component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;

import defaults.ImageLinkDefaults;
import defaults.InterfaceTextDefaults;
import methods.Methods;
import methods.Utils;

public class MenuPanel extends JPanel {
	private static final long serialVersionUID = 1988458431909302046L;
	JLabel[] labels;
	Popup popup;
	Methods r;

	public MenuPanel(Popup popup, Methods r) {
		super();
		this.popup = popup;
		this.r = r;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setOpaque(false);
		this.setBackground(new Color(0,0,0,64));
		init();
		DropShadowBorder shadow = new DropShadowBorder();
		shadow.setShadowColor(Color.GRAY);
		shadow.setShadowSize(3);
		shadow.setShadowOpacity((float) 0.5);
		shadow.setShowRightShadow(true);
		shadow.setShowBottomShadow(true);
		setBorder(shadow);
	}

	private void init() {
		labels = new JLabel[5];
		for (int i = 1; i < 6; i++) {
			labels[i - 1] = new JLabel();
			labels[i - 1].setName(Integer.toString(i));
			labels[i - 1].setIcon(Utils
					.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MENU) + i + ".png"));
			labels[i - 1].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			labels[i - 1].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					JLabel l = (JLabel) e.getSource();
					l.setIcon(Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MENU)
							+ l.getName() + "_rollover.png"));
					l.updateUI();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					JLabel l = (JLabel) e.getSource();
					l.setIcon(Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MENU)
							+ l.getName() + ".png"));
					l.updateUI();
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					JLabel l = (JLabel) e.getSource();
					if (r.showedTest != null && ! r.showedTest.isDontShowBreakingDialog()) {
						CustomDialog d1 = new CustomDialog(r,
								InterfaceTextDefaults.getInstance().getDefault("sure_break_task"),
								InterfaceTextDefaults.getInstance().getDefault("break"),
								InterfaceTextDefaults.getInstance().getDefault("cancel"), true);
						if (d1.getAnswer() == 1)
							r.showTests(Integer.parseInt(l.getName()) - 1);
					} else
						r.showTests(Integer.parseInt(l.getName()) - 1);
				}
			});
			this.add(labels[i - 1]);
		}
	}
}
