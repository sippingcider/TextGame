package textGame;
import enums.AreaType;

/*
 * used in conjuction with Area class
 * link is the area that player moves to when activating this link
 * moveDes describes how the player moves to the linked area
 */
public class DirectionLink {
	private String moveDes;
	private AreaType link;
	
	public DirectionLink(String s, AreaType a) {
		setMoveDes(s);
		setLink(a);
	}

	public String getMoveDes() {
		if (moveDes==null) printError("getMoveDes");
		return moveDes;
	}

	public void setMoveDes(String moveDes) {
		if (moveDes==null) printError("setMoveDes");
		this.moveDes = moveDes;
	}

	public AreaType getLink() {
		if (link==null) printError("getLink");
		return link;
	}

	public void setLink(AreaType link) {
		if (link==null) printError("setLink");
		this.link = link;
	}
	
	/*prints errors to console so it is easier to debug
	 * when calling this method pass the name of the method that is calling it
	 */
	private void printError (String method) {
		System.out.println("Error-DirectionLink-"+method);
	}
}
