package sims.items.Energy;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.map.Objects;
import sims.variables.Values;

public class Bed extends GeneralItem implements itemInterface {
	private boolean direction;
	private Objects obID2;

	public Bed(Objects obID, Objects obID2, boolean direction) {
		super(Values.BED_W);
		super.setHeight(Values.BED_H);
		super.setWidth(Values.BED_W);
		super.setSatisfaction(Values.BED_S);
		super.setTime(Values.BED_T);
		super.setObjectID(obID);
		this.obID2 = obID2;
		this.direction = direction;
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
		sim.setEnergy(sim.getEnergy() + this.getSatisfaction());
	}
	@Override
	public void draw(Objects[][] arr) {
		super.simpleDraw(arr);
		for (int i = 0; i < super.getWidth(); i++) {
			if (direction) {
				arr[super.getY()][super.getX() + i] = this.obID2;
				super.setInteraction(new Interaction(super.getX() + i, super.getY()));
			} else {
				arr[super.getY() + super.getHeight() - 1][super.getX() + i] = this.obID2;
				super.setInteraction(new Interaction(super.getX() + i, super.getY() + super.getHeight() - 1));
			}
		}
	}

	public boolean isDirection() {
		return direction;
	}

	public void setDirection(boolean direction) {
		this.direction = direction;
	}

	public Objects getObID2() {
		return obID2;
	}

	public void setObID2(Objects obID2) {
		this.obID2 = obID2;
	}


}
