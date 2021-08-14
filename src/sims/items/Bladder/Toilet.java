package sims.items.Bladder;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.items.Hygiene.Sink;
import sims.map.Objects;
import sims.variables.Values;

public class Toilet extends GeneralItem implements itemInterface {
	private Objects obID2;

	public Toilet(Objects obID, Objects obID2) {
		super(1);
		super.setHeight(Values.TOILET_H);
		super.setWidth(Values.TOILET_W);
		super.setObjectID(obID);
		super.setSatisfaction(Values.TOILET_S);
		super.setTime(Values.TOILET_T);
		this.obID2 = obID2;
	}

	@Override
	public Plan createPlan(Sim sim) {
		Sink sink = (Sink) sim.getBuilding().getItem(Objects.SINK);
		Plan plan = new Plan(this);
		plan.addToPlan(sink);
		sim.setMyPlan(plan);
		return plan;
	}

	@Override
	public boolean interact(Sim sim) {
		if (sim.getMyPlan().isDeadPlan()) {
			createPlan(sim);
		}
		boolean res = checkAndSet(sim);
		if (res)
			satisfy(sim);
		return res;
	}

	@Override
	public void satisfy(Sim sim) {
		sim.setBladder(sim.getBladder() + this.getSatisfaction());
	}

	@Override
	public void draw(Objects[][] arr) {
		for (int i = 0; i < this.getWidth(); i++) {
			arr[this.getY()][i + this.getX()] = this.obID2;
		}
		arr[this.getY() + 1][this.getX() + this.getWidth() / 2] = this.getObjectID();
		super.setInteraction(new Interaction(this.getX() + this.getWidth() / 2, this.getY() + 1));
	}

}
