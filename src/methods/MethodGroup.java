package methods;

public class MethodGroup {
	private String name;
	private String toolTipText;
	private String image;
	private String rolloverImage;

	public MethodGroup(String name, String image, String rolloverImage, String toolTipText) {
		super();
		this.name = name;
		this.image = image;
		this.rolloverImage = rolloverImage;
		this.toolTipText = toolTipText.replace("!linebreak!", "<br/>");
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

	public String getRolloverImage() {
		return rolloverImage;
	}

	public void setRolloverImage(String rolloverImage) {
		this.rolloverImage = rolloverImage;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}
}
