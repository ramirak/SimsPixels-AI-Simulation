package sims.ai;

import java.util.Comparator;

import sims.character.Plan;
import sims.character.Sim;
import sims.variables.Values;

public class SortByHappiness implements Comparator<ActivityNode> {

	// Maximize [happiness-time-path] to each activity.
	// (The time and path should be minimal).

	@Override
	public int compare(ActivityNode o1, ActivityNode o2) {
		// Activate decay function for each of the nodes.
		processTime(o1);
		processTime(o2);
		return computeHappiness(o2) - computePath(o2) - (computeHappiness(o1) - computePath(o1));

	}

	public void processTime(ActivityNode o) {
		int t = computeTime(o);
		if (!o.hasChanged()) {
			o.setHasChanged(true);
			for (int i = 0; i < t / Values.SIM_DECAY_TIME; i++) {
				o.getSim().simDecay();
			}
		}
	}
	
	private int computeHappiness(ActivityNode node) {
		Sim sim = node.getSim();
		node.getActivity().satisfy(sim);
		return sim.computeHapiness();
	}

	private int computeTime(ActivityNode node) {
		Plan plan = node.getActivity().createPlan(node.getSim());
		int time = 0;
		// If the Sim has a plan, than find the time of each item in the plan and return
		// the sum.
		if (plan != null) {
			time += plan.getInitiator().getTime()[0].getSeconds();
			while (plan.nextStage() != null)
				time += plan.getMyPlan().get(plan.getPlanStage()).getTime()[0].getSeconds();
			return time;
		}
		// If the Sim does not have any plan, return the time of current activity.
		return node.getActivity().getTime()[0].getSeconds();
	}

	// Compute the path to the activity.
	// Path is determined by Manhattan distance.
	private int computePath(ActivityNode node) {
		int x1 = node.getActivity().getX();
		int y1 = node.getActivity().getY();
		int x2 = node.getSim().getX();
		int y2 = node.getSim().getY();
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);

	}

}
