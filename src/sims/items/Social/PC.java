package sims.items.Social;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.map.Objects;
import sims.variables.Values;

public class PC extends GeneralItem implements itemInterface {
	public PC(Objects obID) {
		super(1);
		super.setHeight(Values.PC_H);
		super.setWidth(Values.PC_W);
		super.setSatisfaction(Values.PC_S);
		super.setTime(Values.PC_T);
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
		sim.setSocial(sim.getSocial() + this.getSatisfaction());
	}

	@Override
	public void draw(Objects[][] arr) {
		super.simpleDraw(arr);
		super.setInteraction(new Interaction(this.getX() + 2, this.getY() + this.getHeight() / 2));
	}

}
