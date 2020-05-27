package items;

import enums.ItemType;
import textGame.MainGame;

public class DarkTorch extends Item{

	@Override
	void initialize() {
		setType(ItemType.DARK_TORCH);
		setName("Torch");
		setDes("A wooden stick with one end wrapped in cloth and oil.");
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

	@Override
	public boolean useItem(MainGame g) {
		g.addLog("Smells like charcoal.");
		return false;
	}

}
