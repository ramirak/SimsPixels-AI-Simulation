package sims.items.Other;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.map.Objects;
import sims.variables.Values;

public class Microwave extends GeneralItem implements itemInterface {
	private Objects obID2;

	public Microwave(Objects obID, Objects obID2) {
		super(1);
		super.setHeight(Values.MICROWAVE_H);
		super.setWidth(Values.MICROWAVE_W);
		super.setTime(Values.MICROWAVE_T);
		super.setObjectID(obID);
		this.obID2 = obID2;
	}
	@Override
	public Plan createPlan(Sim sim) {
		// No plan
		return null;
	}
	@Override
	public boolean interact(Sim sim) {
		return checkAndSet(sim);
	}

	@Override
	public void satisfy(Sim sim) {
		// Non-satisfying object
	}

	@Override
	public void draw(Objects[][] arr) {
		super.simpleDraw(arr);
		for (int i = 0; i < super.getHeight(); i++) {
			arr[super.getY() + i][super.getX() + super.getWidth() - 1] = this.obID2;
		}
		super.setInteraction(new Interaction(super.getX() + super.getWidth(), super.getY()));
	}

}
