package items;

import enums.ItemType;
import textGame.MainGame;

public class LitTorch extends Item{

	@Override
	void initialize() {
		setType(ItemType.LIT_TORCH);
		setName("Burning Torch");
		setDes("A wooden stick kept burning by an oil-doused cloth top.");
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
		g.addLog("So bright...");
		return false;
	}

}
