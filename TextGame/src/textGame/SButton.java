package textGame;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import enums.DirectionType;
import enums.SButtonType;
import items.Item;
import people.Person;

/*
 * SButton stands for special button.  I couldn't name it Button because I guess Java claimed that name already.
 */
public class SButton {
	private ColorScheme colors;
	private SButtonType type;
	private Rectangle bounds;
	private String text;
	private boolean visible,needsBounds,dragged,returning,highlighted;
	private int fontSize,pointer;
	private Point origin;
	
	/*
	 * initializes button parameters.
	 * bounds is where the button currently is.
	 * origin is the home of the button, where it should return when released
	 */
	public SButton(SButtonType t, int x, int y, String s, ColorScheme c, int size, int p) {
		type = t;
		bounds = new Rectangle();
		origin = new Point();
		setDragged(false);
		setReturning(false);
		setHighlighted(false);
		setVisible(true);
		setFontSize(size);
		setLocation(x,y);
		setText(s);
		setColors(c);
		setPointer(p);
	}
	
	/*
	 * readies button for cross-screen travel.
	 * called when button is clicked or grabbed for the first time since being idle
	 */
	public void grabbed() {
		setDragged(true);
		setReturning(false);
		setHighlighted(true);
	}
	
	/*
	 * button should be idle now
	 */
	public void released() {
		setDragged(false);
		setReturning(true);
	}
	
	/*
	 * places button left of another button
	 * spacing is how much space lies between buttons after move
	 */
	public void placeLeft(SButton b, int spacing) {
		setLocation(b.getOrigin().x - bounds.width - spacing, -1);
	}
	
	/*
	 * places button right of another button
	 * spacing is how much space lies between buttons after move
	 */
	public void placeRight(SButton b, int spacing) {
		setLocation(b.getOrigin().x+b.getBounds().width+spacing, -1);
	}
	/*
	 * places button above another button
	 * spacing is how much space lies between buttons after move
	 */
	public void placeUp(SButton b, int spacing) {
		setLocation(-1, b.getOrigin().y-getFontSize()-spacing);
	}
	/*
	 * places button below another button
	 * spacing is how much space lies between buttons after move
	 */
	public void placeDown(SButton b, int spacing) {
		setLocation(-1, b.getOrigin().y+b.getBounds().height+spacing);
	}
	/*
	 * moves the button towards its origin
	 * if its close to its origin, it pops into the origin exactly
	 */
	public void returnOrigin() {
		bounds.x-=(bounds.x-origin.x)/10;
		bounds.y-=(bounds.y-origin.y)/10;
		if (origin.distance(bounds.getLocation())<15) {
			setLocation(origin.x, origin.y);
			setReturning(false);
		}
	}
	
	/*
	 * what happens when this button is clicked
	 */
	public void click(MainGame g) {
		switch(type) {
		case LOG_UP:
			g.logOffset--;
			g.refreshLog();
			break;
		case LOG_DOWN:
			g.logOffset++;
			g.refreshLog();
			break;
		case INVENTORY:
			g.invOpen = !g.invOpen;
			g.updateInvText();
			for (SButton s:g.inventorybs) if(g.inventory[s.pointer]!=null) s.setVisible(g.invOpen);
			break;
		case PERSON:
			String con = "Non-believer.";
			Person p = g.location.getPerson(pointer);
			if(p.getConverted()) con = "Believer.";
			g.addLog(p.getName()+": "+p.getDes()+con);
			break;
		case AREA_ITEM:
			Item item = g.location.getItem(pointer);
			g.addLog(item.getName()+": "+item.getDes());
			break;
		case INVENTORY_ITEM:
			item = g.inventory[pointer];
			g.addLog(item.getName()+": "+item.getDes());
			break;
		case LOG:
			g.logOpen = !g.logOpen;
			g.refreshLog();
			if (!g.logOpen) {
				g.eventLog.clear();
				g.logs = 0;
				g.addLog("Cleared Log");
			}
			break;
		default:
			break;
		}
		released();
	}
	
	/*
	 * what happens when this button is dragged, or dropped onto another button
	 */
	public void drop(MainGame g) {
		SButton collision = null;
		for (int j = 0; j < g.buttons.size(); j++) {
			if (g.buttons.get(j)!=this) if (g.buttons.get(j).getHighlighted() && g.buttons.get(j).getVisible()) collision = g.buttons.get(j);
		}
		if (collision==null) {
			released();
			return;
		}
		switch(type) {
		case PLAYER:
			switch (collision.type) {
			case PERSON:
				Person p = g.location.getPerson(collision.getPointer());
				g.addLog(p.getName()+": \""+p.getDialogue()+"\"");
				break;
			case LOCATION_LINK:
				DirectionLink dl = g.location.getDLink(DirectionType.values()[collision.getPointer()]);
				Area a = g.map.getArea(dl.getLink());
				g.addLog("Went "+dl.getMoveDes()+ " to "+a.getName());
				g.location = a;
				g.loadArea(g.location);
				break;
			default:
				break;
			}
			break;
		case AREA_ITEM:
			switch(collision.type) {
			case INVENTORY:
				Item temp = g.location.getItem(pointer);
				if (temp.getPickUp()!=null) {
					g.invAddItem(temp.getPickUp());
					g.updateInvText();
					break;
				}
				if (temp.getMove()!=null) {
					g.addLog(temp.getMove());
					break;
				}
				if (g.invAddItem(temp)) {
					g.location.removeItem(pointer);;
					g.updateInvText();
					g.loadArea(g.location);
				}
				break;
			case AREA_ITEM:
				Item[] results = g.location.getItem(pointer).mixItems(g.location.getItem(collision.pointer),g);
				g.location.removeItem(pointer);
				int colp = collision.pointer;
				if (colp>pointer) colp--;
				g.location.removeItem(colp);
				for (Item item:results) g.location.addItem(item);
				g.loadArea(g.location);
				break;
			case INVENTORY_ITEM:
				results = g.location.getItem(pointer).mixItems(g.inventory[collision.pointer], g);
				g.location.removeItem(pointer);
				g.invRemove(collision.pointer);
				for (Item item:results) g.location.addItem(item);
				g.loadArea(g.location);
				g.updateInvText();
				break;
			case PERSON:
				results = g.location.getPerson(collision.pointer).recieveItem(g.location.getItem(pointer), g);
				g.location.removeItem(pointer);
				for (Item item:results) {
					g.location.addItem(item);
				}
				g.loadArea(g.location);
				break;
			case PLAYER:
				if (g.location.getItem(pointer).useItem(g)) {
					g.location.removeItem(pointer);
					g.loadArea(g.location);
				}
				break;
			default:
				break;
			}
			break;
		case INVENTORY_ITEM:
			switch(collision.type) {
			case AREA_HEADER:
				g.location.addItem(g.inventory[pointer]);
				g.invRemove(pointer);
				g.loadArea(g.location);
				g.updateInvText();
				break;
			case AREA_ITEM:
				Item[] results = g.inventory[pointer].mixItems(g.location.getItem(collision.pointer), g);
				g.location.removeItem(collision.pointer);
				g.invRemove(pointer);
				for (Item item:results) g.location.addItem(item);
				g.loadArea(g.location);
				g.updateInvText();
				break;
			case INVENTORY_ITEM:
				results = g.inventory[pointer].mixItems(g.inventory[collision.pointer], g);
				g.invRemove(pointer);
				int colp = collision.pointer;
				if (colp>pointer) colp--;
				g.invRemove(colp);
				for (Item item:results) g.location.addItem(item);
				g.loadArea(g.location);
				g.updateInvText();
				break;
			case PERSON:
				results = g.location.getPerson(collision.pointer).recieveItem(g.inventory[pointer], g);
				g.invRemove(pointer);
				for (Item item:results) g.location.addItem(item);
				g.loadArea(g.location);
				g.updateInvText();
				break;
			case PLAYER:
				if (g.inventory[pointer].useItem(g)) {
					g.invRemove(pointer);
					g.loadArea(g.location);
					g.updateInvText();
				}
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		released();
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setLocation(int x, int y) {
		if (!dragged) setOrigin(x,y);
		if (x!=-1) bounds.x = x;
		if (y!=-1) bounds.y = y;
	}

	public Point getOrigin() {
		return origin;
	}
	
	public void setOrigin(Point p) {
		setOrigin(p.x, p.y);
	}
	
	public void setOrigin(int x, int y) {
		if (x!=-1) origin.x = x;
		if (y!=-1) origin.y = y;
	}
	
	public int getPointer() {
		return pointer;
	}
	
	public void setPointer(int p) {
		pointer = p;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public void setFontSize(int s) {
		fontSize = s;
	}
	
	public boolean getVisible() {
		return visible;
	}
	
	public void setVisible(boolean b) {
		visible = b;
	}
	
	public boolean getHighlighted() {
		return highlighted;
	}
	
	public void setHighlighted(Boolean b) {
		highlighted = b;
	}
	
	public Boolean getReturning() {
		return returning;
	}

	public void setReturning(Boolean returning) {
		this.returning = returning;
	}

	public Boolean getDragged() {
		return dragged;
	}

	public void setDragged(Boolean dragged) {
		this.dragged = dragged;
	}

	public Boolean getNeedsBounds() {
		return needsBounds;
	}

	public void setNeedsBounds(Boolean needsBounds) {
		this.needsBounds = needsBounds;
	}

	public int getXRight() {
		return bounds.x + bounds.width;
	}
	
	public int getYDown() {
		return bounds.y + bounds.height;
	}

	public Color getColor() {
		if (getHighlighted()) {
			return this.getColors().getSelected();
		} else {
			return this.getColors().getColor();
		}
	}
	
	public ColorScheme getColors() {
		return colors;
	}

	public void setColors(ColorScheme colors) {
		this.colors = colors;
	}

	public void setText(String s) {
		text = s;
		setNeedsBounds(true);
	}
	
	/*
	 * returns all the words of the button as an array
	 */
	public String[] getTextArray() {
		int size = 1;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i)==' ') size++;
		}
		String[] results = new String[size];
		String nextWord = "";
		int spot = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if(c==' ') {
				results[spot] = new String(nextWord);
				nextWord = "";
				spot++;
			} else {
				nextWord+=c;
			}
		}
		results[spot] = nextWord;
		return results;
	}

	public String getText() {
		return text;
	}
	
	/*prints errors to console so it is easier to debug
	 * when calling this method pass the name of the method that is calling it
	 */
	private void printError(String method) {
		System.out.println("Errer-SButton-"+method);
	}
}
