package people;

import enums.PersonType;
import enums.StatusType;
import items.Item;
import textGame.MainGame;

public class CapturedPriest extends Person{

	@Override
	public void initialize() {
		setType(PersonType.CAPTURED_PRIEST);
		setName("Captured Priest");
		setIdleDes(" is cowering.");
		setDes("NEEDS DESCRIPTION-wears standard cloths for priest of Stolhel.  Holding hands defensively in front of face because he can't see in this dark room.");
		setDialogue("Who...who's there?");
	}

	@Override
	public Item[] recieveItem(Item item, MainGame g) {
		if (item == null) printError("recieveItem");
		Item[] results = new Item[1];
		switch (item.getType()) {
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
			results[0] = item;
			g.addLog(getName()+" did not want "+item.getName());
			break;
		}
		return results;
	}

}
