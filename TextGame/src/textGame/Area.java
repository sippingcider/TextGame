package textGame;
import java.util.ArrayList;

import enums.AreaType;
import enums.DirectionType;
import enums.ItemType;
import enums.PersonType;
import items.*;
import people.Dugal;
import people.Person;

public class Area {
	private ArrayList<Item> items;
	private ArrayList<Person> people;
	private String des, name;
	private AreaType type;
	private DirectionLink[] directionLinks;
	private int lighting;
	
	/*
	 * initializes area depending on type
	 * lighting is how well lit the area is, ranging from 0 to 255.  255 is brightest, 0 is darkest
	 * a lighting of -1 means the lighting depends on whether it is night or day.  This is the default for most areas.
	 * direction links are portals to areas connected to this one
	 */
	public Area(AreaType t) {
		if (t==null) printError("constructor");
		type = t;
		people = new ArrayList<Person>();
		items = new ArrayList<Item>();
		directionLinks = new DirectionLink[DirectionType.values().length];
		setLighting(-1);
		switch(type) {
		case INFINITE_DARKNESS:
			name = "Infinite Darkness";
			setDes("Welcome, child.  I know you long for the emptiness of sleep, but your service is required once again.  Take a moment to awaken your true sight, and when you are ready, bring yourself to the throne room.");
			setLighting(0);
			setDLink(DirectionType.NORTH, "through the darkness", AreaType.OUTSIDE_DARK_THRONE_ROOM);
			break;
		case OUTSIDE_DARK_THRONE_ROOM:
			name = "Throne Room Entrance";
			setDes("Good job, your movement is as good as it always was.  In order to prepare you for your next mission, I will take physical form inside the Throne Room.  Initiate conversation with me, and I will continue your breifing.");
			setDLink(DirectionType.NORTH, "in", AreaType.DARK_THRONE_ROOM);
			setLighting(250);
			break;
		case DARK_THRONE_ROOM:
			name = "Throne Room";
			setDes("A vast room of shadows, black as night.  Only your true sight allows you to see what is here.");
			addPerson(new Dugal());
			setLighting(250);
			setDLink(DirectionType.WEST, AreaType.DARK_TORTURE_CHAMBER);
			break;
		case DARK_TORTURE_CHAMBER:
			name = "Torture Chamber";
			setDes("Shadows coil and twist at strange angles.  They seem to be weeping, perhaps for what they have witnessed in this sad chamber.");
			addItem(new ShadowChains());
			addItem(new VoidWhip());
			addItem(new TissueBox());
			setDLink(DirectionType.EAST, AreaType.DARK_THRONE_ROOM);
			setLighting(250);
			break;
		}
	}
	
	public DirectionLink getDLink(DirectionType dir) {
		return directionLinks[dir.ordinal()];
	}
	

	public void setDLink(DirectionType dir, AreaType area) {
		setDLink(dir, dir.toString(), area);
	}
	
	public void setDLink (DirectionType dir, String s, AreaType a) {
		if (s==null || a==null) printError("setDLink");
		directionLinks[dir.ordinal()]=new DirectionLink(s,a);
	}
	
	public void clearDLink (DirectionType dir) {
		directionLinks[dir.ordinal()]= null;
	}
	
	public void removePerson(int pointer) {
		if (pointer>-1 && pointer<people.size()) {
			people.remove(pointer);
		} else {
			printError("removePerson");
		}
	}
	
	public void addPerson(Person Person) {
		if (Person!=null) {
			people.add(Person);
		} else {
			printError("addPerson");
		}
	}
	
	public Person getPerson(int pointer) {
		if (pointer>-1 && pointer<people.size()) {
			return people.get(pointer);
		} else {
			printError("getPerson");
		}
		return null;
	}
	
	public void removeItem(int pointer) {
		if (pointer>-1 && pointer<items.size()) {
			items.remove(pointer);
		} else {
			printError("removeItem");
		}
	}
	
	public void addItem(Item item) {
		if (item!=null) {
			items.add(item);
		} else {
			printError("addItem");
		}
	}
	
	public Item getItem(int pointer) {
		if (pointer>-1 && pointer<items.size()) {
			return items.get(pointer);
		} else {
			printError("getItem");
		}
		return null;
	}
	
	public void setDes(String s) {
		if (s==null) printError("setDes");
		des = s;
	}
	
	public String getDes() {
		return des;
	}

	public String getName() {
		return name;
	}
	

	public int getPeopleSize() {
		return people.size();
	}

	public int getItemsSize() {
		return items.size();
	}

	public int getLighting() {
		return lighting;
	}

	public void setLighting(int lighting) {
		this.lighting = lighting;
	}
	
	public boolean hasItem(ItemType type) {
		for (Item item:items) {
			if (item.getType()==type) return true;
		}
		return false;
	}

	/*prints errors to console so it is easier to debug
	 * when calling this method pass the name of the method that is calling it
	 */
	private void printError(String method) {
		System.out.println("Error-Area-"+method);
	}

}
