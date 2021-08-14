package sims.items.Energy;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.map.Objects;
import sims.variables.Values;

public class Sofa extends GeneralItem implements itemInterface {
	private boolean horizontal;
	private Objects obID2;

	public Sofa(boolean horizontal, Objects obID, Objects obID2) {
		super(Values.SOFA_H + Values.SOFA_W - 4);
		super.setHeight(Values.SOFA_H);
		super.setWidth(Values.SOFA_W);
		super.setSatisfaction(Values.SOFA_S);
		super.setTime(Values.TV_T);
		super.setObjectID(obID);
		setObID2(obID2);
		setHorizontal(horizontal);
	}

	@Override
	public Plan createPlan(Sim sim) {
		// No plan
		return null;
	}

	@Override
	public boolean interact(Sim sim) {
		boolean res = checkAndSet(sim);
		if (res) {
			// The Sim gets satisfied both by resting on the sofa, and by watching the TV.
			GeneralItem init = sim.getMyPlan().getInitiator();
			init.satisfy(sim);
			satisfy(sim);
		}
		return res;
	}

	@Override
	public void satisfy(Sim sim) {
		sim.setEnergy(sim.getEnergy() + this.getSatisfaction());
	}

	@Override
	public void draw(Objects[][] arr) {
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				if (i == 0 || j == 0) {
					arr[this.getY() + i][this.getX() + j] = this.obID2;
				} else if (i == 1 || j == 1) {
					arr[this.getY() + i][this.getX() + j] = this.getObjectID();
					if (!(i == 1 && j == 1))
						setInteraction(new Interaction(this.getX() + j, this.getY() + i));
				}
			}
		}
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
	}

	public Objects getObID2() {
		return obID2;
	}

	public void setObID2(Objects obID2) {
		this.obID2 = obID2;
	}

}
