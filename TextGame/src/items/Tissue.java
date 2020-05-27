package items;

import enums.ItemType;
import textGame.MainGame;

public class Tissue extends Item {

	@Override
	void initialize() {
		setType(ItemType.TISSUE);
		setName("Tissue");
		setDes("A small white cloth, slightly moistened.");
	}
	
	@Override
	public boolean useItem(MainGame g) {
		g.addLog("Sniffle...sniffle...so sad.");
		return true;
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
