package textGame;
import enums.AreaType;

/*
 * an array of all the areas in the game
 * still not sure if this needs to be its own class or not
 */
public class WorldMap {
	private Area[] map;
	
	public WorldMap() {
		map = new Area[AreaType.values().length];
        for (int i = 0; i < map.length; i++) {
        	map[i] = new Area(AreaType.values()[i]);
        }
	}
	
	public Area getArea(AreaType area) {
		return map[area.ordinal()];
	}
}
