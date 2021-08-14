package sims.items.Social;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.map.Objects;
import sims.variables.Values;

public class Phone extends GeneralItem implements itemInterface {

	public Phone(Objects obID) {
		super(1);
		super.setHeight(Values.PHONE_H);
		super.setWidth(Values.PHONE_W);
		super.setSatisfaction(Values.PHONE_S);
		super.setTime(Values.PHONE_T);
		super.setObjectID(obID);
	}

	@Override
	public Plan createPlan(Sim sim) {
		// No plan
		return null;
	}

	@Override
	public boolean interact(Sim sim) {
		if (checkAndSet(sim)) {
			satisfy(sim);
			return true;
		}
		return false;
	}

	@Override
	public void satisfy(Sim sim) {
		sim.setSocial(sim.getSocial() + this.getSatisfaction());
	}

	@Override
	public void draw(Objects[][] arr) {
		super.simpleDraw(arr);
		super.setInteraction(new Interaction(this.getX(), this.getY()-1));
	}

}
