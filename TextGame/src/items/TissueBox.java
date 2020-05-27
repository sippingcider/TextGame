package items;

import enums.ItemType;
import textGame.MainGame;

public class TissueBox extends Item{

	@Override
	void initialize() {
		setType(ItemType.TISSUE_BOX);
		setName("Tissue Box");
		setDes("A box of tissues.  Probably for the torturer after a long day's work.");
		setPickUp(new Tissue());
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
		g.addLog("Too heavy to pick up.  Who knew tissues could weigh so much?");
		return false;
	}

}
