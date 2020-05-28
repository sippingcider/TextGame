package people;
import enums.PersonType;
import enums.StatusType;
import items.Item;
import textGame.MainGame;

public abstract class Person {
	private PersonType type;
	private StatusType status;
	private String des, name, dialogue, idleDes;
	private boolean converted;
	
	public Person() {
		converted = false; //most people are not converted by default.  If someone is, set converted to true in the initialize method
		setStatus(StatusType.NEW_BORN);
		initialize();
		if (type==null) printError("Person Constructor-Forgot to set Type");
	}
	
	/*
	 * initializes person based on type
	 * des is the text the player sees when inspecting the person
	 * dialogue is what the person says when the player talks to them
	 * idleDes is what the person is doing in the area
	 */
	public abstract void initialize();
	
	/*
	 * what happens when the person recieves an item
	 * the item recieved is removed from the game
	 * return an array of items to be added to the game.
	 */
	public abstract Item[] recieveItem(Item item, MainGame g);

	public String getName() {
		return name;
	}
	
	public void setName(String s) {
		name = s;
	}
	
	public PersonType getType() {
		return type;
	}
	
	public void setType(PersonType p) {
		type = p;
	}
	
	public boolean getConverted() {
		return converted;
	}
	
	public void convert() {
		converted = true;
	}
	
	public String getIdleDes() {
		if (idleDes==null) printError("getIdleDes");
		return idleDes;
	}

	public void setIdleDes(String idleDes) {
		if (idleDes==null) printError("setIdleDes");
		this.idleDes = idleDes;
	}

	public String getDialogue() {
		if (dialogue==null) printError("getDialogue");
		return dialogue;
	}

	public void setDialogue(String dialogue) {
		if (dialogue==null) printError("setDialogue");
		this.dialogue = dialogue;
	}

	public String getDes() {
		if (des==null) printError("getDes");
		return des;
	}

	public void setDes(String des) {
		if (des==null) printError("setDes");
		this.des = des;
	}
	
	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	/*prints errors to console so it is easier to debug
	 * when calling this method pass the name of the method that is calling it
	 */
	public void printError(String method) {
		System.out.println("Error-"+type.toString()+"-"+method);
	}
}
