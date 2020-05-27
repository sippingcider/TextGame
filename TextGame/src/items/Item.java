package items;
import enums.ItemType;
import textGame.MainGame;

public abstract class Item {
	private ItemType type;
	private Item pickUp;
	private String name, des, move;
	
	/*initializes the item depending on what type of item it is
	 * if the item is too big to put into an inventory or can't be moved for other reasons, set the move string to the message that should show when the player tries to pick up the item
	 * if the item gives something other than itself when it is picked up, set the item type to be gained in the variable pickUp.
	 */
	public Item() {
		setPickUp(null);
		setMove(null);
		initialize();
	}
	
	/*
	 * set name, description, and other fields if need be
	 */
	abstract void initialize();
	
	//what happens when the item is used on another item
	public abstract Item[] mixItems(Item input, MainGame g);
	/*what happens when the player uses the item on themselves
	 * returns true if the item needs to be deleted, false if it still exists
	*/
	public abstract boolean useItem (MainGame g) ;

	public void setName(String s) {
		this.name = s;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDes(String s) {
		this.des = s;
	}
	
	public String getDes() {
		return des;
	}
	
	public void setType(ItemType t) {
		this.type = t;
	}

	public ItemType getType() {
		return type;
	}

	public Item getPickUp() {
		return pickUp;
	}

	public void setPickUp(Item pickUp) {
		this.pickUp = pickUp;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}
	
	/*prints errors to console so it is easier to debug
	 * when calling this method pass the name of the method that is calling it
	 */
	public void printError(String method) {
		System.out.println("Error-Item-"+method);
	}
}
