package tests;

import java.awt.Dimension;

import javax.swing.JPanel;

import methods.Methods;

public abstract class AbstractTest extends JPanel {
	protected Methods methods;
	
	protected long startTime;
	
	protected String userName = "";
	protected String userSex = "";
	protected int userAge;

	public AbstractTest(Methods methods, int width, int height) {
		super();
		this.methods = methods;
		this.setPreferredSize(new Dimension(width, height));
		this.setOpaque(false);
	}
	
	public void showStandartInfo() {
		// TODO
	}
	
	public void showStandartSettings() {
		// TODO
	}
	
	public void showStandartResults() {
		// TODO
	}
	
	public abstract void showInfo();
	public abstract void showTest();
	public abstract void showResults();
	public abstract void showSettings();
}
