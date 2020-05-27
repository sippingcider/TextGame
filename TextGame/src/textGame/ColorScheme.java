package textGame;
import java.awt.Color;

/*
 * used in conjunction with SButton class.  color is the color of the text, selected is the color of the text when selected
 */
public class ColorScheme {
	private Color color, selected;
	
	public ColorScheme(Color c) {
		setColor(c);
	}
	
	public void setColor(Color c) {
		color = c;
		ghostColor(155);
	}
	
	/*
	 * changes transparency of a selected, ranging from 0 to 255
	 */
	public void ghostColor(int trans) {
		if (trans<0 || trans>255) printError("ghostColor");
		selected = new Color(color.getRed(),color.getGreen(),color.getBlue(),trans);
	}

	public Color getColor() {
		return color;
	}

	public Color getSelected() {
		return selected;
	}
	
	/*prints errors to console so it is easier to debug
	 * when calling this method pass the name of the method that is calling it
	 */
	private void printError(String method) {
		System.out.println("Error-ColorScheme-"+method);
	}
}
