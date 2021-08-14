package sims.ai;

import java.util.Comparator;

import sims.character.Sim;
import sims.items.Interaction;

public class SortByDistance implements Comparator<Sim> {

	// The sim represents here a clone which is used to find the next move and not
	// an actual sim
	@Override
	public int compare(Sim s1, Sim s2) {
		return (getDistance(s1.getCurrentTarget(), s1) + s1.getSteps()) - (getDistance(s2.getCurrentTarget(), s2) + s2.getSteps());
	}

	public int getDistance(Interaction last, Sim s) {
		return Math.abs(s.getX() - last.getX()) + Math.abs(s.getY() - last.getY());
	}
}
