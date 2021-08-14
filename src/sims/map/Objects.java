package sims.map;

public enum Objects {
	// Sims
	SIM1, SIM2, SIM3, DEAD_SIM,
	// Floors
	SPACE1, SPACE2, BATHROOM1, BATHROOM2, GARDEN, SHOWER_F, DOOR,
	// Walls
	WALL,
	// Furniture
	TABLE, CHAIR, PC_CHAIR, FRIDGE, SOFA, BED, PILLOWS, PC, TV, MICROWAVE, COUNTER, SINK, SHOWER, TOILET, TABLE_DECO,
	PHONE,
	// Additions
	SALOON_TABLE, FRIDGE_ADD, SOPA_ADD, POOL_WATER1, POOL_WATER2, POOL, MICROWAVE_ADD, TOILET_ADD,
	// Foods
	BANANA, HAMBURGER, PIZZA, SALAD;

	public static boolean isFloor(Objects object) {
		if (object.name() == SPACE1.name() || object.name() == SPACE2.name() || object.name() == BATHROOM1.name()
				|| object.name() == BATHROOM2.name() || object.name() == GARDEN.name()
				|| object.name() == SHOWER_F.name() || object.name() == DOOR.name() || object.name() == POOL.name())
			return true;
		return false;
	}

}
