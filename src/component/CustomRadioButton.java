package component;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JRadioButton;

import defaults.ImageLinkDefaults;
import methods.Utils;

public class CustomRadioButton extends JRadioButton {

	String text;

	public CustomRadioButton(String s, boolean selected) {
		super();
		text = s;
		if (selected)
			setText("<html><div style='font: 18pt Arial Narrow; color: rgb(0, 168, 155);'>" + text + "</div></html>");
		else
			setText("<html><div style='font: 18pt Arial Narrow; color: rgb(115, 84, 73);'>" + text + "</div></html>");
		setOpaque(false);
		setSelected(selected);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setFocusPainted(false);
		setIcon(Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RADIO)));
		setSelectedIcon(
				Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RADIO_SELECTED)));
		addItemListener(new RadioListener());
	}

	class RadioListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			JRadioButton r = (JRadioButton) e.getSource();
			if (e.getStateChange() == ItemEvent.SELECTED) {
				r.setText("<html><div style='font: 18pt Arial Narrow; color: rgb(0, 168, 155);'>" + text
						+ "</div></html>");
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				r.setText("<html><div style='font: 18pt Arial Narrow; color: rgb(115, 84, 73);'>" + text
						+ "</div></html>");
			}
			r.repaint();
		}
	}
}
