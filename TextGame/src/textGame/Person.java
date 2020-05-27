package textGame;
import enums.AreaType;
import enums.ItemType;
import enums.PersonType;
import enums.StatusType;
import items.Item;

public class Person {
	private PersonType type;
	private StatusType status;
	private String des, name, dialogue, idleDes;
	private boolean converted;
	
	/*
	 * initializes person based on type
	 * des is the text the player sees when inspecting the person
	 * dialogue is what the person says when the player talks to them
	 * idleDes is what the person is doing in the area
	 * boolean converted is whether the person worships Dugal or not.
	 */
	public Person(PersonType t) {
		if (t==null) printError("constructor");
		converted = false;
		type = t;
		setStatus(StatusType.NEW_BORN);
		switch(type) {
		case DUGAL:
			name = "Dugal";
			convert();
			setIdleDes("sits on a throne of black obsidion.");
			setDes("A tall, human-like figure of darkness.  All light drains into it.");
			setDialogue("Well done.  Now, I shall breif you.  You have slept long in your recovery since your last mission.  Ages have passed, and already your victory is forgotten by the denizens of earth.  They tear down our idols and worship new gods, beings that threaten even me.  Our temples are abandoned, and my name fades behind decree of blasphemy.  Oh, the travesty! Please, bring me a tissue to wipe my tears, there should be some in the torture chamber.");
			break;
		case CAPTURED_PRIEST:
			name = "Captured Priest";
			setDes("NEEDS DESCRIPTION-wears standard cloths for priest of Stolhel.  Holding hands defensively in front of face because he can't see in this dark room.");
			setIdleDes(" is cowering.");
			setDialogue("Who...who's there?");
			break;
		}
	}
	
	/*
	 * what happens when the person recieves an item
	 * the item recieved is removed from the game
	 * return an array of items to be added to the game.
	 * most of the time the only item returned is the item recieved
	 */
	public Item[] recieveItem(Item item, MainGame g) {
		if (item==null) printError("recieveItem");
		Item[] results = new Item[1];
		results[0] = item;
		switch(type) {
		case CAPTURED_PRIEST:
			switch(item.getType()) {
			case SHADOW_CHAINS:
				setStatus(StatusType.CHAINED);
				setIdleDes("is chained.");
				setDialogue("Stolhel protect me, and smite any who wish me harm!");
				g.addLog("Chained Priest.");
				results = new Item[0];
				return results;
			case VOID_WHIP:
				if (getStatus()==StatusType.CHAINED) {
					g.addLog("End of tutorial so far...");
					return results;
				}
				g.addLog("Priest: \"Ha, missed me!\"");
				return results;
			default:
				break;
			}
			break;
		case DUGAL:
			switch (item.getType()) {
			case TISSUE:
				g.addLog("Dugal: Sniffle...\"Thank you, child.  Alas, there is some good news.  We have captured an agent of the enemy, a priest who worhips the god Stolhel.  I've moved him to the torture chamber.  See if you can get any information out of him by using the void whip.  Don't forget to chain him first.");
				setDialogue("Remember, chain the priest and then use the whip on him.");
				g.map.getArea(AreaType.DARK_TORTURE_CHAMBER).addPerson(new Person(PersonType.CAPTURED_PRIEST));
				results = new Item[0];
				return results;
			default:
				break;
			}
			break;
		default:
			break;
		}
		g.addLog(name+" did not want "+item.getName());
		return results;
	}

	public String getName() {
		return name;
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
	private void printError(String method) {
		System.out.println("Error-Person-"+method);
	}
}
