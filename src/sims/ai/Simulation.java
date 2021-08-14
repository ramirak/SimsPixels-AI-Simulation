package sims.ai;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

import sims.Main;
import sims.character.Sim;
import sims.items.GeneralItem;
import sims.items.Other.Fridge;
import sims.map.Building;
import sims.map.Objects;
import sims.variables.Time;
import sims.variables.Values;

public class Simulation {
	public static final int numOfSims = 3;
	private Building building;
	public static Sim[] sims;
	private final int intelligenceDepth = 3;
	private PriorityQueue<ActivityNode> queue;

	public Simulation(Building building) {
		this.building = building;
		sims = new Sim[numOfSims];

		// Create random points for each Sim...
		for (int i = 0; i < numOfSims; i++) {
			Random rand = new Random();
			int x = rand.nextInt(building.getMSZ() - 2) + 1;
			int y = rand.nextInt(building.getMSZ() - 2) + 1;
			while (!Objects.isFloor(building.getHome()[y][x])) {
				x = rand.nextInt(building.getMSZ() - 2) + 1;
				y = rand.nextInt(building.getMSZ() - 2) + 1;
			}
			// Sim creation
			sims[i] = new Sim(x, y, Objects.values()[Objects.SIM1.ordinal() + i], building);
		}
	}

	public boolean showStates(Sim sim) {
		if (sim.getCurrentInteraction() != null)
			Main.textArea.append("\nCurrent interaction - " + sim.getCurrentInteraction().getObjectID().name() + "\n");
		else
			Main.textArea.append("\nThinking\n");
		Main.textArea.append("\nAI-level = " + this.intelligenceDepth + "\n" + sim.toString() + "\nHappiness = "
				+ (sim.computeHapiness() * 100 / sim.getMaxHappiness()) + "%");
		if (!sim.isAlive()) {
			Main.textArea.append("\nThe sim has died");
			return false;
		}
		return true;
	}

	public void run() {
		for (int i = 0; i < numOfSims; i++) {
			Main.textArea.setText("");
			showStates(sims[Main.simCounter]);
			Sim currentSim = sims[i];
			if (!currentSim.isAlive())
				continue;
			currentSim.socializing();
			// If the plan has ended, find a new activity
			if (currentSim.getCurrentInteraction() == null)
				findNextActivity(currentSim);
			// If interacted successfully, go to the next stage of the plan
			if (currentSim.getCurrentInteraction().interact(sims[i]))
				currentSim.setCurrentInteraction(currentSim.getMyPlan().nextStage());
			// Timer for decay value
			if (!currentSim.getTimer().hasStarted()) {
				currentSim.setTimer(new Time(Values.SIM_DECAY_TIME));
				currentSim.getTimer().StartTimer();
			}
			// If the timer has ended, activate decay function.
			if (currentSim.getTimer().checkTimer()) {
				currentSim.simDecay();
			}
		}
		try {
			// Slow down the program
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void findNextActivity(Sim sim) {
		this.queue = new PriorityQueue<ActivityNode>(20, new SortByHappiness());
		LinkedList<GeneralItem> optionalActivities = this.building.getInteractions();

		// Init first run
		for (int i = 0; i < optionalActivities.size(); i++) {
			if (optionalActivities.get(i).getObjectID() != sim.getOccupiedActivity()) {
				if (optionalActivities.get(i).getObjectID() == Objects.FRIDGE) {
					Fridge fridge = (Fridge) optionalActivities.get(i);
					LinkedList<GeneralItem> content = fridge.getContent();
					for (int j = 0; j < content.size(); j++) {
						ActivityNode node = new ActivityNode(content.get(j), new Sim(sim));
						queue.add(node);
					}
				} else {
					ActivityNode node = new ActivityNode(optionalActivities.get(i), new Sim(sim));
					queue.add(node);
				}
			}
		}
		ActivityNode node;
		// Plan ahead
		while (true) {
			// Get the most promising node

			node = queue.remove();
			// When the node reaches depth value, break the loop.
			if (node.getNodeDepth() > this.intelligenceDepth)
				break;

			// Find the next promising activity from the current promising node, depth ++ .
			for (int i = 0; i < optionalActivities.size(); i++) {
				if (optionalActivities.get(i).getObjectID() == Objects.FRIDGE) {
					Fridge fridge = (Fridge) optionalActivities.get(i);
					LinkedList<GeneralItem> content = fridge.getContent();
					for (int j = 0; j < content.size(); j++) {
						Sim currentSim = new Sim(node.getSim());
						ActivityNode currentNode = new ActivityNode(content.get(j), currentSim);
						currentNode.setParent(node);
						queue.add(currentNode);
					}
				} else {
					Sim currentSim = new Sim(node.getSim());
					ActivityNode currentNode = new ActivityNode(optionalActivities.get(i), currentSim);
					currentNode.setParent(node);
					queue.add(currentNode);
				}
			}
		}
		ActivityNode BestActivity = null;

		// Return to the first activity that was found to be the best in terms of
		// happiness, and set it to be the current activity of the Sim.
		while (node.getParent() != null) {
			BestActivity = node.getParent();
			node = BestActivity;
		}
		sim.setCurrentInteraction(BestActivity.getActivity());
		sim.setOccupiedActivity(null);
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Sim[] getSims() {
		return sims;
	}

	public int getNumOfSims() {
		return numOfSims;
	}
}
