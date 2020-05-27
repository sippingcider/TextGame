package items;

import enums.ItemType;
import textGame.MainGame;

public class VoidWhip extends Item {

	@Override
	void initialize() {
		setType(ItemType.VOID_WHIP);
		setName("Void Whip");
		setDes("A long, black whip spiked with neon purple blades and stained with blood.");
	}
	
	@Override
	public boolean useItem(MainGame g) {
		g.addLog("Owe.");
		return false;
	}

	@Override
	public Item[] mixItems(Item input, MainGame g) {
		if (input==null) printError("mixItems");
		if (input == null) printError("mixItems");
		Item[] results;
    	results = new Item[2];
    	switch (input.getType()) {
		default:
			results[0] = this;
	    	results[1] = input;
	    	g.addLog(getName()+" did nothing to "+input.getName());
			break;
    	}
    	return results;
	}
}
