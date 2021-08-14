package sims.items.Hygiene;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.map.Objects;
import sims.variables.Values;

public class Shower extends GeneralItem implements itemInterface {
	private Objects obID2;

	public Shower(Objects obID, Objects obID2) {
		super(1);
		super.setHeight(Values.SHOWER_H);
		super.setWidth(Values.SHOWER_W);
		super.setSatisfaction(Values.SHOWER_S);
		super.setTime(Values.SHOWER_T);
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
		boolean res = checkAndSet(sim);
		if (res)
			satisfy(sim);
		return res;
	}

	@Override
	public void satisfy(Sim sim) {
		sim.setHygiene(sim.getHygiene() + this.getSatisfaction());
	}

	@Override
	public void draw(Objects[][] arr) {
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				if (i == this.getHeight() - 1 || j == 0) {
					if (j == this.getWidth() / 2) {
						arr[this.getY() + i][this.getX() + j] = Objects.DOOR;
						super.setInteraction(new Interaction(this.getX() + j, this.getY() + i - 1));
					} else
						arr[this.getY() + i][this.getX() + j] = getObjectID();
				} else
					arr[this.getY() + i][this.getX() + j] = this.obID2;
			}
		}
	}

}
