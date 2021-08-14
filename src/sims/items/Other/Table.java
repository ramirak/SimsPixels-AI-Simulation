package sims.items.Other;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.map.Objects;
import sims.variables.Values;

public class Table extends GeneralItem implements itemInterface {
	private Objects obID2;

	public Table(Objects obID, Objects obID2) {
		super(4);
		super.setHeight(Values.TABLE_H);
		super.setWidth(Values.TABLE_W);
		super.setTime(1);
		super.setObjectID(obID);
		this.obID2 = obID2;
	}

	public void setChairs() {
		int x = super.getX();
		int y = super.getY();
		super.setInteraction(new Interaction(x - 1, y + (super.getHeight() - 1) / 2));
		super.setInteraction(new Interaction(x + super.getWidth(), y + (super.getHeight() - 1) / 2));
		super.setInteraction(new Interaction(x + (super.getWidth() - 1) / 2, y + super.getHeight()));
		super.setInteraction(new Interaction(x + (super.getWidth() - 1) / 2, y - 1));
	}
	@Override
	public Plan createPlan(Sim sim) {
		// No plan
		return null;
	}
	@Override
	public boolean interact(Sim sim) {
		boolean res = checkAndSet(sim);
		return res;
	}

	@Override
	public void satisfy(Sim sim) {
		// Non satisfying object
	}

	@Override
	public void draw(Objects[][] arr) {
		setChairs();
		super.simpleDraw(arr);
		for (int i = 0; i < super.getInteraction().length; i++) { // draw chairs
			arr[super.getInteraction()[i].getY()][super.getInteraction()[i].getX()] = this.obID2;
		}
	}

}
