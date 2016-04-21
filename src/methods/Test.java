package methods;

import javax.swing.JPanel;

public class Test extends JPanel {
	private static final long serialVersionUID = 2443449596442127488L;
	private String name;
	private String image;
	private String shortText;
	private String longText;
	private String bigImage;
	private String className;
	private String rolloverImage;

	public Test() {

	}

	public Test(String name, String image, String shortText, String longText, String bigImage, String className,
			String rolloverImage) {
		super();
		this.name = name;
		this.image = image;
		this.shortText = shortText;
		this.longText = longText;
		this.bigImage = bigImage;
		this.className = className;
		this.rolloverImage = rolloverImage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public String getLongText() {
		return longText;
	}

	public void setLongText(String longText) {
		this.longText = longText;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRolloverImage() {
		return rolloverImage;
	}

	public void setRolloverImage(String rolloverImage) {
		this.rolloverImage = rolloverImage;
	}

	public String getBigImage() {
		return bigImage;
	}

	public void setBigImage(String bigImage) {
		this.bigImage = bigImage;
	}
}
