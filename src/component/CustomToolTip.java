package component;

import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JToolTip;

import customui.CustomToolTipUI;

public class CustomToolTip extends JToolTip {

	private static final long serialVersionUID = -3690555922698451452L;

	public CustomToolTip() {
		super();
		setUI(new CustomToolTipUI());
	}

	public void addNotify() {
		super.addNotify();
		setOpaque(false);
		Container parent = getParent();
		if (parent instanceof JComponent) {
			((JComponent) parent).setOpaque(false);
		}
	}

}
