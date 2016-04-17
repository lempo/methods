package methods;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import component.BgPanel;
import component.CustomPanel;
import component.CustomDialog;
import component.CustomLabel;
import customui.PanelCustomUI;
import customui.ButtonCustomUI;
import customui.ScrollBarCustomUI;
import defaults.ImageLinkDefaults;
import defaults.InterfaceTextDefaults;
import defaults.TextLinkDefaults;
import tests.AbstractTest;

public class Methods extends JFrame {

	private MethodGroup[] methodGroups;
	private Test[] tests;

	private int currentMethodGroup = 0;

	private int iconsSpace = 10;

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

	Methods methods;

	public Methods() {
		super("Methods");

		methods = this;

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

		showGroups();
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

		ImageIcon icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.CLOSE));
		closeLabel.setIcon(icon);
		closeLabel.setName("close");
		closeLabel.addMouseListener(l);
		closeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		closeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RESTORE));
		restoreLabel.setIcon(icon);
		restoreLabel.setName("restore");
		restoreLabel.addMouseListener(l);
		restoreLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		restoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.HIDE));
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

		ImageIcon icon = Utils
				.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_EXIT));
		exitLabel.setIcon(icon);
		exitLabel.setName("exit");
		exitLabel.addMouseListener(l);
		exitLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_ABOUT));
		aboutLabel.setIcon(icon);
		aboutLabel.setName("about");
		aboutLabel.addMouseListener(l);
		aboutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_HELP));
		helpLabel.setIcon(icon);
		helpLabel.setName("help");
		helpLabel.addMouseListener(l);
		helpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_TASKS));
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

		logoSpace = (int) Math.round(width * 0.28);

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

	public void showGroups() {

		menuPanel.setX1(width - iconsSpace * 5 - exitLabel.getWidth() - helpLabel.getWidth() - tasksLabel.getWidth()
				- aboutLabel.getWidth() - 3);
		menuPanel.setX2(
				width - iconsSpace * 5 - exitLabel.getWidth() - helpLabel.getWidth() - aboutLabel.getWidth() + 3);
		menuPanel.repaint();

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

		JPanel p = new JPanel();
		JTextPane text = new JTextPane();
		text.setEditable(false);
		text.setContentType("text/html;charset=utf-8");

		try {
			text.setPage(getClass().getResource(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.WELCOME)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		text.setOpaque(false);
		p.add(text);
		text.setPreferredSize(new Dimension((int) (width * 0.9), 175));
		p.setUI(new PanelCustomUI(true));

		Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.GROUPS));

		NodeList n = doc.getElementsByTagName("group");
		NamedNodeMap k = null;

		JLabel[] groups = new JLabel[n.getLength()];
		methodGroups = new MethodGroup[n.getLength()];

		ImageIcon icon;
		GroupsMouseListener l = new GroupsMouseListener();
		for (int i = 0; i < n.getLength(); i++) {
			k = n.item(i).getAttributes();
			methodGroups[i] = new MethodGroup(k.getNamedItem("name").getNodeValue(),
					k.getNamedItem("text").getNodeValue(), k.getNamedItem("image").getNodeValue(),
					k.getNamedItem("bigImage").getNodeValue(), k.getNamedItem("rolloverImage").getNodeValue(),
					k.getNamedItem("toolTipText").getNodeValue());

			icon = Utils.createImageIcon(methodGroups[i].getImage());
			groups[i] = new CustomLabel();
			groups[i].setIcon(icon);
			groups[i].setHorizontalTextPosition(JLabel.CENTER);
			groups[i].setVerticalTextPosition(JLabel.BOTTOM);
			groups[i].setName(Integer.toString(i));
			groups[i].addMouseListener(l);
			groups[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			groups[i].createToolTip();
			groups[i].setToolTipText(
					"<html><div style='font: bold 14pt Arial Narrow; color: #455a64; padding: 10px; padding-top: 13px; padding-bottom: 5px;'>"
							+ methodGroups[i].getName().toUpperCase() + "</div>"
							+ "<div style='font: 13pt Arial Narrow; color: #455a64; padding: 10px;  padding-top: 0px; padding-bottom: 13px;'>"
							+ methodGroups[i].getToolTipText() + "</div></html>");
		}

		actualPanel.removeAll();
		actualPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(38, 20, 0, 20);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;

		int i;
		for (i = 0; i < Math.ceil((double) groups.length / 3); i++) {
			for (int j = 0; j < 3; j++) {
				if ((i * 3 + j) == groups.length)
					break;
				c.gridx = j;
				c.gridy = i;

				actualPanel.add(groups[i * 3 + j], c);
			}
		}

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = i;
		c.insets = new Insets(10, 20, 10, 20);

		actualPanel.add(p, c);

		actualPanel.revalidate();
		actualPanel.repaint();
	}

	public void showAbout() {
		menuPanel.setX1(width - iconsSpace * 3 - exitLabel.getWidth() - aboutLabel.getWidth() - 3);
		menuPanel.setX2(width - iconsSpace * 3 - exitLabel.getWidth() + 3);
		menuPanel.repaint();

		currentMethod = "showAbout";
		paramTypes = new Class[] {};
		args = new Object[] {};

		// read version
		String v = Utils.getVersion();
		String dd = Utils.getVersionDate();

		// read config
		String name = Utils.getLicenceUserName();
		String keyNumber = Utils.getLicenceKey();

		// days left
		int days = HTTPClient.daysLeft(keyNumber, name);

		String from = HTTPClient.getFrom(keyNumber, name);

		showedTest = null;
		JLabel heading = new JLabel();
		String t = "<html><div style='font: bold 24pt Arial Narrow; color: rgb(70, 110, 122);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("about") + "</div></html>";
		heading.setText(t);
		ImageIcon icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.ARROW));
		heading.setIcon(icon);
		heading.setIconTextGap(20);
		heading.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		heading.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				showGroups();
			}
		});
		headerPanel.removeAll();
		headerPanel.add(Box.createHorizontalStrut(20));
		headerPanel.add(heading);
		heading.setAlignmentY(BOTTOM_ALIGNMENT);
		headerPanel.add(Box.createHorizontalGlue());
		headerPanel.revalidate();
		headerPanel.repaint();

		JLabel key = new JLabel();
		t = "<html><div style='font: bold 14pt Arial Narrow; color: rgb(70, 110, 122);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("key")
				+ ": <span style='font: 15pt Arial Narrow; color: rgb(115, 84, 73);'>" + keyNumber
				+ "</span></div></html>";
		key.setText(t);

		JLabel licenze = new JLabel();
		t = "<html><div style='font: bold 14pt Arial Narrow; color: rgb(70, 110, 122);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("license")
				+ ": <br><span style='font: 15pt Arial Narrow; color: rgb(115, 84, 73);'>" + name + " " + from
				+ "</span></div></html>";
		licenze.setText(t);

		String day = InterfaceTextDefaults.getInstance().getDefault("day");
		if (days % 10 == 1)
			if (days / 10 != 1)
				day = InterfaceTextDefaults.getInstance().getDefault("day_1");
		if ((days % 10 == 2) || (days % 10 == 3) || (days % 10 == 4))
			if (days / 10 != 1)
				day = InterfaceTextDefaults.getInstance().getDefault("day_234");
		JLabel left = new JLabel();
		t = "<html><div style='font: bold 14pt Arial Narrow; color: rgb(70, 110, 122);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("days_left")
				+ ": <span style='font: 15pt Arial Narrow; color: rgb(115, 84, 73);'>" + days + " " + day
				+ "</span></div></html>";
		left.setText(t);

		JLabel version = new JLabel();
		t = "<html><div style='font: bold 14pt Arial Narrow; color: rgb(70, 110, 122);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("version")
				+ ": <span style='font: 15pt Arial Narrow; color: rgb(115, 84, 73);'>" + v + " "
				+ InterfaceTextDefaults.getInstance().getDefault("from") + " " + dd + "</span></div></html>";
		version.setText(t);

		JButton checkUpdates = new JButton(InterfaceTextDefaults.getInstance().getDefault("check_updates"));
		icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.CIRCLE_ARROW));
		checkUpdates.setIcon(icon);
		checkUpdates.setIconTextGap(20);
		checkUpdates.setUI(new ButtonCustomUI(new Color(38, 166, 154)));
		checkUpdates.setBorder(null);
		checkUpdates.setOpaque(false);
		checkUpdates.setPreferredSize(new Dimension(252, 40));
		checkUpdates.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		checkUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ask DB for updates
				String location = HTTPClient.getVersion(dd);
				if (location != null) {
					// update
					// show dialog
					CustomDialog d1 = new CustomDialog(methods,
							InterfaceTextDefaults.getInstance().getDefault("do_update"),
							InterfaceTextDefaults.getInstance().getDefault("yes"),
							InterfaceTextDefaults.getInstance().getDefault("no"), true);
					if (d1.getAnswer() == 1) {
						try {
							System.out.println(location);
							Process proc = Runtime.getRuntime().exec("java -jar updater.jar " + location);
							System.exit(0);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else
						return;
				} else {
					// don't update
					// show dialog
					new CustomDialog(methods, InterfaceTextDefaults.getInstance().getDefault("no_updates"),
							InterfaceTextDefaults.getInstance().getDefault("ok"), null, true);
				}
			}
		});

		JCheckBox checkAuto = new JCheckBox("<html><div style='font: 15pt Arial Narrow; color: rgb(115, 84, 73);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("check_updates_auto") + "</div></html>");
		checkAuto.setOpaque(false);
		checkAuto.setSelected(Utils.getCheckUpdatesAuto());
		checkAuto.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String s = Utils.readFile(Utils.getFilePath() + "/config");
				if (checkAuto.isSelected())
					s = s.replaceAll("<checkUpdatesAuto>.*</checkUpdatesAuto>",
							"<checkUpdatesAuto>true</checkUpdatesAuto>");
				else
					s = s.replaceAll("<checkUpdatesAuto>.*</checkUpdatesAuto>",
							"<checkUpdatesAuto>false</checkUpdatesAuto>");
				Utils.writeFile(s, Utils.getFilePath() + "/config");
			}
		});

		JLabel about = new JLabel();
		t = "<html><div style='font: bold 22pt Arial Narrow; color: rgb(115, 84, 73);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("about") + "</div></html>";
		about.setText(t);

		JTextPane text = new JTextPane();
		text.setEditable(false);
		text.setContentType("text/html;charset=utf-8");

		try {
			text.setPage(getClass().getResource(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.ABOUT)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JScrollPane scroll = new JScrollPane(text);
		scroll.setPreferredSize(new Dimension((int) (width * 0.95), (int) (height * 0.45)));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUI(new ScrollBarCustomUI());

		actualPanel.removeAll();
		actualPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(40, 40, 0, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;

		actualPanel.add(key, c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.insets = new Insets(40, 0, 0, 0);

		actualPanel.add(version, c);

		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(20, 40, 0, 0);
		c.weightx = 0.0;

		actualPanel.add(licenze, c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.insets = new Insets(20, 0, 0, 0);

		actualPanel.add(checkUpdates, c);

		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(20, 40, 0, 0);

		actualPanel.add(left, c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(20, 0, 0, 0);

		actualPanel.add(checkAuto, c);

		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setBackground(new Color(176, 190, 197));
		sep.setForeground(new Color(176, 190, 197));
		sep.setPreferredSize(new Dimension((int) (scroll.getPreferredSize().getWidth() * 0.98), 1));

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(20, 40, 0, 20);

		actualPanel.add(sep, c);

		c.gridy = 4;
		c.insets = new Insets(20, 40, 0, 0);

		actualPanel.add(about, c);

		c.gridy = 5;
		c.insets = new Insets(0, 20, 0, 0);

		actualPanel.add(scroll, c);

		actualPanel.revalidate();
		actualPanel.repaint();
	}

	public void showHelp() {
		menuPanel.setX1(
				width - iconsSpace * 4 - exitLabel.getWidth() - helpLabel.getWidth() - aboutLabel.getWidth() - 3);
		menuPanel.setX2(width - iconsSpace * 4 - exitLabel.getWidth() - aboutLabel.getWidth() + 3);
		menuPanel.repaint();

		currentMethod = "showHelp";
		paramTypes = new Class[] {};
		args = new Object[] {};

		showedTest = null;
		JLabel heading = new JLabel();
		String t = "<html><div style='font: bold 24pt Arial Narrow; color: rgb(70, 110, 122);'>"
				+ InterfaceTextDefaults.getInstance().getDefault("help") + "</div></html>";
		heading.setText(t);
		ImageIcon icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.ARROW));
		heading.setIcon(icon);
		heading.setIconTextGap(20);
		heading.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		heading.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				showGroups();
			}
		});
		headerPanel.removeAll();
		headerPanel.add(Box.createHorizontalStrut(20));
		headerPanel.add(heading);
		heading.setAlignmentY(BOTTOM_ALIGNMENT);
		headerPanel.add(Box.createHorizontalGlue());
		headerPanel.revalidate();
		headerPanel.repaint();

		JLabel faq = new JLabel();
		String t1 = "<html><div style='font: 22pt Arial Narrow; color: rgb(115, 84, 73); padding-left: 17px;'><span style='font-weight: bold;'>?&nbsp;&nbsp;&nbsp;</span>"
				+ InterfaceTextDefaults.getInstance().getDefault("faq") + "</div></html>";
		faq.setText(t1);

		JTextPane text = new JTextPane();
		text.setEditable(false);
		text.setContentType("text/html;charset=utf-8");

		try {
			text.setPage(getClass().getResource(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.FAQ)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		actualPanel.removeAll();
		actualPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 0.0;
		c.weighty = 0.0;

		actualPanel.add(faq, c);

		JScrollPane scroll = new JScrollPane(text);
		scroll.setPreferredSize(new Dimension((int) (width * 0.95), (int) (height * 0.7)));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUI(new ScrollBarCustomUI());

		c.gridy = 1;

		actualPanel.add(scroll, c);

		actualPanel.revalidate();
		actualPanel.repaint();
	}

	private void showMethods(int i) {
		// TODO Auto-generated method stub
		menuPanel.setX1(0);
		menuPanel.setX2(0);
		menuPanel.repaint();

		currentMethod = "showMethods";
		paramTypes = new Class[] { int.class };
		args = new Object[] { i };

		showedTest = null;

		JLabel heading = new JLabel();
		String t = "<html><div style='font: bold 24pt Arial Narrow; color: rgb(70, 110, 122);'>"
				+ methodGroups[i].getName().toUpperCase() + "</div></html>";
		heading.setText(t);
		ImageIcon icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.ARROW));
		heading.setIcon(icon);
		heading.setIconTextGap(20);
		heading.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		heading.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				showGroups();
			}
		});
		headerPanel.removeAll();
		headerPanel.add(Box.createHorizontalStrut(20));
		headerPanel.add(heading);
		heading.setAlignmentY(BOTTOM_ALIGNMENT);
		headerPanel.add(Box.createHorizontalGlue());
		headerPanel.revalidate();
		headerPanel.repaint();

		Document doc = Utils.openXML(TextLinkDefaults.getInstance().getLink(TextLinkDefaults.Key.GROUPS));

		NodeList n = doc.getElementsByTagName("group");
		NamedNodeMap k = null;

		NodeList n1 = n.item(i).getChildNodes();
		tests = new Test[n1.getLength()];
		JLabel[] testsLabels = new JLabel[n1.getLength()];

		TestsMouseListener l = new TestsMouseListener();
		for (int j = 0; j < n1.getLength(); j++) {
			k = n1.item(j).getAttributes();
			tests[j] = new Test(k.getNamedItem("name").getNodeValue(), k.getNamedItem("image").getNodeValue(),
					k.getNamedItem("shortText").getNodeValue(), k.getNamedItem("longText").getNodeValue(),
					k.getNamedItem("longLongText").getNodeValue(), k.getNamedItem("bigImage").getNodeValue(),
					k.getNamedItem("className").getNodeValue(), k.getNamedItem("rolloverImage").getNodeValue());

			icon = Utils.createImageIcon(tests[j].getImage());
			testsLabels[j] = new CustomLabel();
			testsLabels[j].setIcon(icon);
			testsLabels[j].setHorizontalTextPosition(JLabel.CENTER);
			testsLabels[j].setVerticalTextPosition(JLabel.BOTTOM);
			testsLabels[j].setName(Integer.toString(j));
			testsLabels[j].addMouseListener(l);
			testsLabels[j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		actualPanel.removeAll();
		actualPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(38, 20, 0, 20);
		c.ipadx = 0;
		c.ipady = 0;
		c.weightx = 1.0;
		c.weighty = 0.0;

		for (i = 0; i < Math.ceil((double) testsLabels.length / 3); i++) {
			for (int j = 0; j < Math.min(3, testsLabels.length); j++) {
				if ((i * 3 + j) == testsLabels.length)
					break;
				c.gridx = j;
				c.gridy = i;

				actualPanel.add(testsLabels[i * 3 + j], c);
			}
		}

		actualPanel.revalidate();
		actualPanel.repaint();
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
				icon = Utils
						.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.CLOSE_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "restore":
				icon = Utils.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RESTORE_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "hide":
				icon = Utils
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
				icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.CLOSE));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "restore":
				icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.RESTORE));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "hide":
				icon = Utils.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.HIDE));
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
				showHelp();
				// TODO help
				break;
			case "about":
				// TODO about
				showAbout();
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
				icon = Utils.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_EXIT_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "help":
				icon = Utils.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_HELP_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "about":
				icon = Utils.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_ABOUT_ROLLOVER));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "tasks":
				icon = Utils.createImageIcon(
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
				icon = Utils
						.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_EXIT));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "help":
				icon = Utils
						.createImageIcon(ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_HELP));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "about":
				icon = Utils.createImageIcon(
						ImageLinkDefaults.getInstance().getLink(ImageLinkDefaults.Key.MAIN_MENU_ABOUT));
				l.setIcon(icon);
				l.updateUI();
				break;
			case "tasks":
				icon = Utils.createImageIcon(
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

	class GroupsMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			int i = Integer.parseInt(l.getName());
			currentMethodGroup = i;
			showMethods(i);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			int i = Integer.parseInt(l.getName());
			ImageIcon icon = Utils.createImageIcon(methodGroups[i].getRolloverImage());
			l.setIcon(icon);
			l.updateUI();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			int i = Integer.parseInt(l.getName());
			ImageIcon icon = Utils.createImageIcon(methodGroups[i].getImage());
			l.setIcon(icon);
			l.updateUI();
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}
	
	class TestsMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			int i = Integer.parseInt(l.getName());
			// TODO show test
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			int i = Integer.parseInt(l.getName());
			ImageIcon icon = Utils.createImageIcon(tests[i].getRolloverImage());
			l.setIcon(icon);
			l.updateUI();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			int i = Integer.parseInt(l.getName());
			ImageIcon icon = Utils.createImageIcon(tests[i].getImage());
			l.setIcon(icon);
			l.updateUI();
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}
}
