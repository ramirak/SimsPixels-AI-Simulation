package sims.items.Other;

import java.util.LinkedList;

import sims.character.Plan;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.items.itemInterface;
import sims.items.Hunger.Banana;
import sims.items.Hunger.Hamburger;
import sims.items.Hunger.Pizza;
import sims.items.Hunger.Salad;
import sims.map.Objects;
import sims.variables.Values;

public class Fridge extends GeneralItem implements itemInterface {
	private LinkedList<GeneralItem> content;
	private Objects obID2;

	public Fridge(Objects obID, Objects obID2) {
		super(1);
		super.setHeight(Values.FRIDGE_H);
		super.setWidth(Values.FRIDGE_W);
		super.setTime(Values.FRIDGE_T);
		super.setObjectID((obID));
		this.obID2 = obID2;
	}
	// Fill the fridge and reset its content.
	private void fill() {
		content = new LinkedList<GeneralItem>();
		Banana banana = new Banana(Objects.BANANA);
		Hamburger hamburger = new Hamburger(Objects.HAMBURGER);
		Pizza pizza = new Pizza(Objects.PIZZA);
		Salad salad = new Salad(Objects.SALAD);
		this.content.add(banana);
		this.content.add(hamburger);
		this.content.add(pizza);
		this.content.add(salad);
	}
	
	public LinkedList<GeneralItem> getContent() {
		fill();
		return content;
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
	public void satisfy(Sim sim) {
		// Non-satisfying object
	}

	@Override
	public void draw(Objects[][] arr) {
		super.simpleDraw(arr);
		for (int i = 0; i < super.getHeight(); i++) {
			arr[super.getY() + i][super.getX() + super.getWidth() - 1] = this.obID2;
		}
		super.setInteraction(new Interaction(this.getX() + this.getWidth(), this.getY()));
	}

	public Objects getObID2() {
		return obID2;
	}

	public void setObID2(Objects obID2) {
		this.obID2 = obID2;
	}

}
