package items;

import enums.ItemType;
import textGame.MainGame;

public class ShadowChains extends Item {

	@Override
	void initialize() {
		setType(ItemType.SHADOW_CHAINS);
		setName("Shadow Chains");
		setDes("Shadows twisted into the likeness of chains.  Empty of a host, they rest coiled in silence.");
		setMove("Chains are fastened to this realm.");
	}
	
	@Override
	public boolean useItem(MainGame g) {
		g.addLog("The chains sense your allegiance to Dugal, and will not bind you.  Unless...thats the sort of thing you like, but this isn't the time for that.");
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
