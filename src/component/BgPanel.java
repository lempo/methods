package component;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BgPanel extends JPanel {
	String image;
	private int width;
	private int height;

	public BgPanel(String image, int width, int height) {
		super();
		this.image = image;
		this.width = width;
		this.height = height;
	}

	public void paintComponent(Graphics g) {
		Image im = null;
		try {
			im = ImageIO.read(getClass().getResource(image));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("problem reading file " + image);
		}
		g.drawImage(im, 0, 0, width, height, this);
	}
}
