package sims.items.Fun;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.map.Objects;
import sims.variables.Values;

public class Pool extends GeneralItem implements itemInterface {
	private Objects obID1, obID2;

	public Pool(Objects obID, Objects obID1, Objects obID2) {
		super(Values.POOL_W - 2);
		super.setHeight(Values.POOL_H);
		super.setWidth(Values.POOL_W);
		super.setSatisfaction(Values.POOL_S);
		super.setTime(Values.POOL_T);
		this.obID1 = obID1;
		this.obID2 = obID2;
		super.setObjectID(obID);
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
		sim.setFun(sim.getFun() + this.getSatisfaction());
	}

	@Override
	public void draw(Objects[][] arr) {
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				if (i == 0 || j == 0 || j == this.getWidth() - 1 || i == this.getHeight() - 1) {
					arr[this.getY() + i][this.getX() + j] = super.getObjectID();
					continue;
				}
				if (((i - j) & 2) == 0)
					arr[this.getY() + i][this.getX() + j] = this.obID1;
				else
					arr[this.getY() + i][this.getX() + j] = this.obID2;
				if (i == this.getHeight() - 2 && j > 0 && j < this.getWidth() - 1)
					super.setInteraction(new Interaction(this.getX() + j, this.getY() + i));
			}
		}
	}

}
