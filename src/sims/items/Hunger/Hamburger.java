package sims.items.Hunger;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.itemInterface;
import sims.items.Other.Counter;
import sims.items.Other.Fridge;
import sims.items.Other.Microwave;
import sims.items.Other.Table;
import sims.map.Building;
import sims.map.Objects;
import sims.variables.Values;

public class Hamburger extends Meal implements itemInterface {
	public Hamburger(Objects obID) {
		super(1);
		super.setSatisfaction(Values.HAMBURGER_S);
		super.setTime(Values.HAMBURGER_T);
		super.setObjectID(obID);
	}

	@Override
	public Plan createPlan(Sim sim) {
		Building building = sim.getBuilding();
		Fridge fridge = (Fridge) building.getItem(Objects.FRIDGE);
		Counter counter = (Counter) building.getItem(Objects.COUNTER);
		Microwave micro = (Microwave) building.getItem(Objects.MICROWAVE);
		Table table = (Table) building.getItem(Objects.TABLE);
		Plan plan = new Plan(this);
		plan.addToPlan(fridge);
		plan.addToPlan(micro);
		plan.addToPlan(counter);
		plan.addToPlan(table);
		plan.addToPlan(this);
		sim.setMyPlan(plan);
		return plan;
	}

	@Override
	public boolean interact(Sim sim) {
		if (sim.getMyPlan().isDeadPlan()) {
			createPlan(sim);
			return true;
		} else {
			boolean res = checkAndSet(sim);
			if(res)
				satisfy(sim);
			return res;
		}
	}

	@Override
	public void satisfy(Sim sim) {
		sim.setHunger(sim.getHunger() + this.getSatisfaction());
	}

	@Override
	public void draw(Objects[][] arr) {
		// arr[this.getY()][this.getX()] = this.getObjectID();
	}

}
