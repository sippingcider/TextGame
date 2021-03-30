package people;

import enums.AreaType;
import enums.PersonType;
import enums.StatusType;
import items.Item;
import items.ShadowChains;
import textGame.MainGame;

public class CapturedPriest extends Person{

	@Override
	public void initialize() {
		setType(PersonType.CAPTURED_PRIEST);
		setName("Captured Priest");
		setIdleDes(" is cowering.");
		setDes("A bald, middle aged man, wearing white robes.  A yellow emblem of a fist grasping a sun is stitched to the front.");
		setDialogue("Who...who's there?");
		setStatus(StatusType.NEW_BORN);
	}

	@Override
	public Item[] recieveItem(Item item, MainGame g) {
		if (item == null) printError("recieveItem");
		Item[] results = new Item[1];
		switch (item.getType()) {
		case SHADOW_CHAINS:
			if (!getConverted()) {
				setStatus(StatusType.CHAINED);
				setIdleDes("is chained.");
				setDialogue("Stolhel protect me, and smite any who wish me harm!");
				g.addLog("Chained Priest.");
				results = new Item[0];
				return results;
			} else {
				return results;
			}
		case VOID_WHIP:
			if (getStatus()==StatusType.CHAINED) {
				setDes(getDes()+"  Fresh welts show on his skin.");
				g.addLog("Priest: \"Ouch!  Okay, I'll tell you what you want to know.  Just put that wicked thing away.\"");
				setDialogue("I'm but a humble priest, who services the Church of Stolhel in the small town of Idun.  I am not privy to important information that you seek.");
				g.addEvent(PersonType.CAPTURED_PRIEST, "Dugal: \"The Priest gives us more than he knows.  I must have a word with you in the throne room.\"");
				g.map.getPerson(PersonType.DUGAL).setDialogue("Talk to the priest when he is ready.");
				setStatus(StatusType.READY_TO_TALK);
				setIdleDes("is ready to talk.");
				results[0] = item;
				return results;
			} else {
				if (getStatus()==StatusType.BROKEN_SPIRIT) {
					convert();
					g.addLog("Please, make it stop!  I'll do anything...");
					setIdleDes(" is cowering.");
					g.location.addItem(new ShadowChains());
				} else {
					g.addLog("Priest: \"Ha, missed me!\"");
					g.addLog("Dugal: \"If the priest is causing you any issues, try chaining him down first.\"");
				}
			}
			results[0] = item;
			return results;
		default:
			results[0] = item;
			g.addLog(getName()+" did not want "+item.getName());
			break;
		}
		return results;
	}
	
	@Override
	public void convert() {
		setStatus(StatusType.NEW_BORN);
		setDes("A bald, middle aged man, wearing black robes.  A white emblem of a sickle moon is stitched to the front.");
		setDialogue("I serve Dugal, master of darkness.");
	}

	@Override
	public String talk(MainGame g) {
		if (getStatus()==StatusType.READY_TO_TALK) {
			Person p = g.map.getPerson(PersonType.DUGAL);
			p.setDialogue("I would have the Priest worshipping me instead of Stolhel.  I suggest using the whip, but if you find another way to convert him its all the same to me.");
			p.setStatus(StatusType.READY_TO_TALK);
		}
		return getDialogue();
	}
}
