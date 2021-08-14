package sims.items.Fun;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.items.Energy.Sofa;
import sims.map.Objects;
import sims.variables.Values;

public class TV extends GeneralItem implements itemInterface {

	public TV(Objects obID) {
		super(1);
		super.setHeight(Values.TV_H);
		super.setWidth(Values.TV_W);
		super.setSatisfaction(Values.TV_S);
		super.setTime(Values.TV_T2);
		super.setObjectID(obID);
	}

	@Override
	public Plan createPlan(Sim sim) {
		Sofa sofa = (Sofa) sim.getBuilding().getItem(Objects.SOFA);
		Plan plan = new Plan(this);
		plan.addToPlan(sofa);
		sim.setMyPlan(plan);
		return plan;
	}

	@Override
	public boolean interact(Sim sim) {
		if (sim.getMyPlan().isDeadPlan())
			createPlan(sim);

		return checkAndSet(sim);
	}

	@Override
	public void satisfy(Sim sim) {
		sim.setFun(sim.getFun() + this.getSatisfaction());
	}

	@Override
	public void draw(Objects[][] arr) {
		super.simpleDraw(arr);
		super.setInteraction(new Interaction(this.getX() - 1, this.getY() + this.getHeight() - 1));
	}

}
