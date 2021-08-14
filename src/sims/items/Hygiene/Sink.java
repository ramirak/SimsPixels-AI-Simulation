package sims.items.Hygiene;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.map.Objects;
import sims.variables.Values;

public class Sink extends GeneralItem implements itemInterface {

	public Sink(Objects obID) {
		super(1);
		super.setHeight(Values.SINK_H);
		super.setWidth(Values.SINK_W);
		super.setSatisfaction(Values.SINK_S);
		super.setTime(Values.SINK_T);
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
		sim.setHygiene(sim.getHygiene() + this.getSatisfaction());
	}

	@Override
	public void draw(Objects[][] arr) {
		super.simpleDraw(arr);
		super.setInteraction(new Interaction(super.getX() + 1, super.getY()));
	}



}
