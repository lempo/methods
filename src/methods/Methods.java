package methods;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import component.BgPanel;
import component.CustomPanel;
import defaults.ImageLinkDefaults;
import defaults.InterfaceTextDefaults;
import tests.AbstractTest;

public class Methods extends JFrame {

	BgPanel panel;

	private int width = 1020;
	private int height = 790;

	private Point lastDragPosition;

	private MouseListener draggingMouseListener;
	private MouseMotionListener draggingMouseMotionListener;

	// panel with window buttons
	JPanel windowPanel;

	// panel with program menu
	CustomPanel menuPanel;

	// panel with header
	JPanel headerPanel;

	// panel with current content
	JPanel actualPanel;

	// menu labels
	JLabel exitLabel;
	JLabel aboutLabel;
	JLabel helpLabel;
	JLabel tasksLabel;

	// window labels
	JLabel closeLabel;
	JLabel restoreLabel;
	JLabel hideLabel;

	private boolean resized = false;
	
	public AbstractTest showedTest = null;
	
	/** Current method name */
	private String currentMethod;
	/** Parameter types of current method */
	Class[] paramTypes;
	/** Arguments of current method */
	Object[] args;

	public Methods() {
		super("Methods");
		setBounds(50, 50, width, height);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setUndecorated(true);

		draggingMouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				lastDragPosition = e.getLocationOnScreen();
			}
		};

		draggingMouseMotionListener = new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {

				Point currentDragPosition = e.getLocationOnScreen();
				int deltaX = currentDragPosition.x - lastDragPosition.x;
				int deltaY = currentDragPosition.y - lastDragPosition.y;
				if (deltaX != 0 || deltaY != 0) {
					int x = getLocation().x + deltaX;
					int y = getLocation().y + deltaY;
					setLocation(x, y);
					lastDragPosition = currentDragPosition;
				}
			}
		};

		composeWindow();
	}

	private void composeWindow() {
		panel = new BgPanel(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.BACKGROUND), width, height);

		createWindowButtons();

		createMainMenu();

		headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
		headerPanel.setPreferredSize(new Dimension(width, 60));

		actualPanel = new JPanel();
		actualPanel.setOpaque(false);
		actualPanel
				.setPreferredSize(new Dimension(width,
						(int) (height - windowPanel.getPreferredSize().getHeight()
								- menuPanel.getPreferredSize().getHeight()
								- headerPanel.getPreferredSize().getHeight())));

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(windowPanel);
		panel.add(menuPanel);
		panel.add(headerPanel);
		panel.add(actualPanel);
		panel.add(Box.createVerticalStrut(7));

		setContentPane(panel);
		panel.revalidate();
		panel.repaint();
		
		if (!resized) {
			windowPanel.addMouseListener(draggingMouseListener);
			windowPanel.addMouseMotionListener(draggingMouseMotionListener);
		}
	}

	public void createWindowButtons() {
		windowPanel = new JPanel();
		windowPanel.setOpaque(false);

		closeLabel = new JLabel();
		restoreLabel = new JLabel();
		hideLabel = new JLabel();
		WindowMouseListener l = new WindowMouseListener();

		ImageIcon icon = Utills.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.CLOSE));
		closeLabel.setIcon(icon);
		closeLabel.setName("close");
		closeLabel.addMouseListener(l);
		closeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		icon = Utills.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RESTORE));
		restoreLabel.setIcon(icon);
		restoreLabel.setName("restore");
		restoreLabel.addMouseListener(l);
		restoreLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		restoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		icon = Utills.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.HIDE));
		hideLabel.setIcon(icon);
		hideLabel.setName("hide");
		hideLabel.addMouseListener(l);
		hideLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		hideLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		windowPanel.removeAll();
		windowPanel.setPreferredSize(new Dimension(width, (int) Math.round(height * 0.038)));
		windowPanel.setLayout(new BoxLayout(windowPanel, BoxLayout.X_AXIS));
		windowPanel.add(Box.createHorizontalGlue());

		float space = (float) (width * 0.032);
		// TODO resize
		// if (resized)
		// space *= 1.05;

		windowPanel.add(hideLabel);
		hideLabel.setAlignmentY(BOTTOM_ALIGNMENT);
		windowPanel.add(Box.createHorizontalStrut((int) Math.round(space)));
		windowPanel.add(restoreLabel);
		restoreLabel.setAlignmentY(BOTTOM_ALIGNMENT);
		windowPanel.add(Box.createHorizontalStrut((int) Math.round(space)));
		windowPanel.add(closeLabel);
		closeLabel.setAlignmentY(BOTTOM_ALIGNMENT);
		windowPanel.add(Box.createHorizontalStrut((int) Math.round(space * 0.89)));

		windowPanel.revalidate();
		windowPanel.repaint();
	}

	private void createMainMenu() {
		menuPanel = new CustomPanel(new Color(55, 71, 79));
		menuPanel.setOpaque(false);

		exitLabel = new JLabel();
		aboutLabel = new JLabel();
		helpLabel = new JLabel();
		tasksLabel = new JLabel();

		MenuMouseListener l = new MenuMouseListener();

		ImageIcon icon = Utills
				.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_EXIT));
		exitLabel.setIcon(icon);
		exitLabel.setName("exit");
		exitLabel.addMouseListener(l);
		exitLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		icon = Utills.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_ABOUT));
		aboutLabel.setIcon(icon);
		aboutLabel.setName("about");
		aboutLabel.addMouseListener(l);
		aboutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		icon = Utills.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_HELP));
		helpLabel.setIcon(icon);
		helpLabel.setName("help");
		helpLabel.addMouseListener(l);
		helpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		icon = Utills.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_TASKS));
		tasksLabel.setIcon(icon);
		tasksLabel.setName("tasks");
		tasksLabel.addMouseListener(l);
		tasksLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		int logoSpace = 289;
		logoSpace = (int) Math.round(width * 0.28);

		menuPanel.setPreferredSize(new Dimension(width, (int) Math.round(height * 0.076)));
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));

		JLabel logo = new JLabel();
		logo.setPreferredSize(new Dimension(logoSpace, (int) menuPanel.getPreferredSize().getHeight()));

		BufferedImage img = null;
		try {
			img = ImageIO
					.read(getClass().getResource(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.LOGO)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dimg = img.getScaledInstance((int) Math.round(logo.getPreferredSize().getWidth() * 0.93),
				(int) Math.round(logo.getPreferredSize().getHeight() * 0.65), Image.SCALE_SMOOTH);
		logo.setIcon(new ImageIcon(dimg));

		menuPanel.add(Box.createHorizontalStrut(25));
		menuPanel.add(logo);
		menuPanel.add(Box.createHorizontalGlue());

		int iconsSpace = 10;

		menuPanel.add(tasksLabel);
		menuPanel.add(Box.createHorizontalStrut(iconsSpace));
		menuPanel.add(helpLabel);
		menuPanel.add(Box.createHorizontalStrut(iconsSpace));
		menuPanel.add(aboutLabel);
		menuPanel.add(Box.createHorizontalStrut(iconsSpace));
		menuPanel.add(exitLabel);
		menuPanel.add(Box.createHorizontalStrut(iconsSpace * 2));

		menuPanel.revalidate();
		menuPanel.repaint();
	}

	private void showGroups() {
		currentMethod = "showGroups";
		paramTypes = new Class[] {};
		args = new Object[] {};
		
		JLabel heading = new JLabel();
		String t = "<html><div style='font: bold 24pt Arial Narrow; color: rgb(70, 110, 122);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("task_groups") + "</div></html>";
		heading.setText(t);

		headerPanel.removeAll();
		headerPanel.add(Box.createHorizontalStrut(20));
		headerPanel.add(heading);
		heading.setAlignmentY(BOTTOM_ALIGNMENT);
		headerPanel.add(Box.createHorizontalGlue());
		headerPanel.revalidate();
		headerPanel.repaint();

		// TODO
	}

	private void resize() {

		Dimension sSize;

		if (!resized) {
			sSize = Toolkit.getDefaultToolkit().getScreenSize();
			resized = true;
			windowPanel.removeMouseListener(draggingMouseListener);
			windowPanel.removeMouseMotionListener(draggingMouseMotionListener);
		} else {
			sSize = new Dimension(1020, 790);
			resized = false;
			windowPanel.addMouseListener(draggingMouseListener);
			windowPanel.addMouseMotionListener(draggingMouseMotionListener);
		}

		height = sSize.height;
		width = sSize.width;

		if (resized)
			setBounds(0, 0, width, height);
		else
			setBounds(50, 50, width, height);

		panel = new BgPanel(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.BACKGROUND), width, height);

		composeWindow();

		if (showedTest != null) {
			actualPanel.revalidate();
			actualPanel.repaint();
		} else {
			Method classMethod;
			try {
				classMethod = Methods.class.getMethod(currentMethod, paramTypes);
				classMethod.invoke(this, args);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	class WindowMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			switch (l.getName()) {
			case "close":
				System.exit(0);
				break;
			case "restore":
				// TODO resize
				resize();
				break;
			case "hide":
				setState(JFrame.ICONIFIED);
				break;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			ImageIcon icon;
			switch (l.getName()) {
			case "close":
				icon = Utills
						.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.CLOSE_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "restore":
				icon = Utills.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RESTORE_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "hide":
				icon = Utills
						.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.HIDE_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			ImageIcon icon;
			switch (l.getName()) {
			case "close":
				icon = Utills.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.CLOSE));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "restore":
				icon = Utills.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RESTORE));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "hide":
				icon = Utills.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.HIDE));
				l.setIcon(icon);
				l.updateUI();
				break;
			}
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}

	class MenuMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			switch (l.getName()) {
			case "exit":
				// TODO logout
				break;
			case "help":
				// TODO help
				break;
			case "about":
				// TODO about
				break;
			case "tasks":
				// TODO tasks, popup
				break;
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			ImageIcon icon;
			switch (l.getName()) {
			case "exit":
				icon = Utills.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_EXIT_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "help":
				icon = Utills.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_HELP_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "about":
				icon = Utills.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_ABOUT_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "tasks":
				icon = Utills.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_TASKS_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			ImageIcon icon;
			switch (l.getName()) {
			case "exit":
				icon = Utills
						.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_EXIT));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "help":
				icon = Utills
						.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_HELP));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "about":
				icon = Utills.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_ABOUT));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "tasks":
				icon = Utills.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_TASKS));
				l.setIcon(icon);
				l.updateUI();
				break;
			}
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}
}
