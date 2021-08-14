package sims.items;

import sims.character.Plan;
import sims.character.Sim;
import sims.map.Objects;

public interface itemInterface {
	/**
	 *  Set a plan for the Sim if needed.
	 *  Example: TV --> SOFA .. 
	 * @param sim
	 * @return
	 */
	public Plan createPlan(Sim sim);
	/** 
	 * Should set a plan for each item and call the satisfy method when the interaction finishes.
	 * @param sim
	 * @return
	 */
	public boolean interact(Sim sim);

	/**
	 * Satisfy the right variable according to the item used.
	 * @param sim
	 */
	public void satisfy(Sim sim);

	/**
	 * Provide a more accurate drawing for each item
	 * @param arr
	 */
	public void draw(Objects[][] arr);

}
