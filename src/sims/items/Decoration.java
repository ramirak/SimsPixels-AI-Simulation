package sims.items;

import sims.character.Plan;
import sims.character.Sim;
import sims.map.Objects;

public class Decoration extends GeneralItem implements itemInterface {

	public Decoration(Objects obID, int height, int width) {
		// No interactions, just a simple decoration ..
		super(0);
		super.setHeight(height);
		super.setWidth(width);
		super.setObjectID(obID);
	}

	@Override
	public Plan createPlan(Sim sim) {
		// just a simple decoration ..
		return null;
	}

	@Override
	public boolean interact(Sim sim) {
		// Simple decoration item. Does not have any interaction
		return false;
	}

	@Override
	public void satisfy(Sim sim) {
		// Just a decoration
	}

	@Override
	public void draw(Objects[][] arr) {
		super.simpleDraw(arr);
	}

}
