package people;

import enums.AreaType;
import enums.DirectionType;
import enums.PersonType;
import enums.StatusType;
import items.Item;
import textGame.Area;
import textGame.MainGame;

public class Dugal extends Person{

	@Override
	public void initialize() {
		setType(PersonType.DUGAL);
		setName("Dugal");
		convert();
		setIdleDes("sits on a throne of black obsidion.");
		setDes("A tall, human-like figure of darkness.  All light drains into it.");
		setDialogue("Well done.  Now, I shall breif you.  You have slept long in your recovery since your last mission.  Ages have passed, and already your victory is forgotten by the denizens of earth.  They tear down our idols and worship new gods, beings that threaten even me.  Our temples are abandoned, and my name fades behind decree of blasphemy.  Oh, the travesty! Please, bring me a tissue to wipe my tears.  There should be some in the torture chamber.");
	}

	@Override
	public Item[] recieveItem(Item item, MainGame g) {
		if (item == null) printError("recieveItem");
		Item[] results = new Item[1];
		switch (item.getType()) {
		case TISSUE:
			g.addLog("Dugal: Sniffle...\"Thank you, child.  Alas, there is some good news.  We have captured an agent of the enemy, a priest who worhips the god Stolhel.  I've moved him to the torture chamber.  See if you can get any information out of him by using the void whip.");
			setDialogue("Remember, chain the priest and then use the whip on him.");
			g.map.getArea(AreaType.DARK_TORTURE_CHAMBER).addPerson(new CapturedPriest());
			results = new Item[0];
			return results;
		default:
			results[0] = item;
			g.addLog(getName()+" did not want "+item.getName());
			break;
		}
		return results;
	}

	@Override
	public String talk(MainGame g) {
		Area a = g.map.getArea(AreaType.DARK_THRONE_ROOM);
		if (a.getDLink(DirectionType.WEST)==null) {
			a.setDLink(DirectionType.WEST, AreaType.DARK_TORTURE_CHAMBER);
			g.loadArea(a);
		}
		if (getStatus()==StatusType.READY_TO_TALK) {
			g.map.getPerson(PersonType.CAPTURED_PRIEST).setStatus(StatusType.BROKEN_SPIRIT);
			setStatus(StatusType.NEW_BORN);
		}
		return getDialogue();
	}

}
