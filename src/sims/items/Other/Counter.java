package sims.items.Other;

import java.util.Random;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.map.Objects;
import sims.variables.Values;

public class Counter extends GeneralItem implements itemInterface {

	public Counter(Objects obID) {
		super(3);
		super.setHeight(Values.COUNTER_H);
		super.setWidth(Values.COUNTER_W);
		super.setTime(Values.COUNTER_T);
		super.setObjectID(obID);
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
	public void draw(Objects[][] arr) {
		super.simpleDraw(arr);
		Random rand = new Random();
		int x[] = new int[super.getInteraction().length];

		for (int i = 0; i < super.getInteraction().length; i++) {
			x[i] = rand.nextInt(super.getWidth());
			if (i > 0)
				while (x[i] == x[i - 1])
					x[i] = rand.nextInt(super.getWidth());
			super.setInteraction(new Interaction(super.getX() + x[i], super.getY() + 2));
		}

	}

	@Override
	public void satisfy(Sim sim) {
		// Non-satisfying object
	}

}
