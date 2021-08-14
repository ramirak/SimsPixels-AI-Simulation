package sims.items.Hunger;

import sims.character.Sim;
import sims.items.GeneralItem;

public abstract class Meal extends GeneralItem {

	public Meal(int interactions) {
		super(interactions);
	}
	
	@Override
	public boolean checkAndSet(Sim sim) {
		if (!sim.isInInteraction()) {
			sim.setInInteraction(true);
			super.getTime()[0].StartTimer();
			return false;
		} else {
			// Reset interaction after finishing
			if (this.getTime()[0].checkTimer()) {
				sim.setInInteraction(false);
			} else // Still interacting
				return false;
		}
		return true;
	}

}
