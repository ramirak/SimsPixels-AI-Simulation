package sims.character;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

import sims.ai.Simulation;
import sims.ai.SortByDistance;
import sims.items.GeneralItem;
import sims.items.Interaction;
import sims.map.Building;
import sims.map.Objects;
import sims.variables.Time;
import sims.variables.Values;

public class Sim {
	// Index of each emotion is set according to the Emotion Enum.
	// Each Sim has a unique character
	private static int ID = 0;

	private int emotions[], emotionsPrioritiy[];
	private int x, y, steps, myId;
	private Time timer;
	private Building building;
	private GeneralItem currentInteraction, lastInteraction;
	private Interaction currentTarget;
	private Sim lastState;
	private Plan myPlan;

	private boolean inInteraction, isAlive;
	private sims.map.Objects lastOb, simCOLOR, occupiedActivity;

	public Sim(int x, int y, Objects simCOLOR, Building building) {
		this.myId = ID++;
		this.emotions = new int[Emotions.values().length];
		this.emotionsPrioritiy = new int[Emotions.values().length];
		// The Sim starts his life as a happy person :)
		for (int i = 0; i < emotions.length; i++)
			emotions[i] = Values.MAX_VALUE;
		this.building = building;
		this.lastOb = this.building.getHome()[y][x];
		this.building.getHome()[y][x] = simCOLOR;
		setTimer(new Time(Values.SIM_DECAY_TIME));
		setSimColor(simCOLOR);
		setX(x);
		setY(y);
		setSteps(0);
		setPriorities();
		setAlive(true);
		// The Sim should start his life with an empty plan
		setMyPlan(new Plan(null));
	}

	// Copy constructor
	public Sim(Sim sim) {
		this.myId = sim.myId;
		this.building = sim.building;
		this.lastOb = sim.lastOb;
		this.simCOLOR = sim.simCOLOR;
		this.timer = sim.timer;
		this.x = sim.x;
		this.y = sim.y;
		this.currentInteraction = sim.currentInteraction;
		this.currentTarget = sim.currentTarget;
		this.lastState = sim.lastState;
		this.myPlan = sim.myPlan;
		this.inInteraction = sim.inInteraction;
		this.emotions = new int[Emotions.values().length];
		this.emotionsPrioritiy = new int[Emotions.values().length];
		this.isAlive = true;
		this.occupiedActivity = sim.occupiedActivity;
		for (int i = 0; i < sim.emotions.length; i++) {
			this.emotions[i] = sim.emotions[i];
			this.emotionsPrioritiy[i] = sim.emotionsPrioritiy[i];
		}

	}

	// Randomize the Sim character.
	public void setPriorities() {
		Random rand = new Random();
		LinkedList<Integer> nums = new LinkedList<Integer>();
		// Randomizing unique numbers between 1 to the number of emotions.
		for (int i = 1; i <= emotions.length; i++) {
			if (i <= 3)
				nums.add(i + 3);
			else
				nums.add(i);
		}
		for (int i = 0; i < this.emotionsPrioritiy.length; i++) {
			int rndIndex = rand.nextInt(nums.size());
			this.emotionsPrioritiy[i] = nums.get(rndIndex);
			nums.remove(rndIndex);
		}
	}

	// Decay function that runs at the chosen time stamps.
	public void simDecay() {
		final int decayVal = Values.SIM_DECAY_VALUE;
		for (int i = 0; i < emotions.length; i++) {
			emotions[i] = limit(emotions[i] - decayVal);
		}
	}

	// Happiness of each Sim depends on his character (emotions priorities).
	public int computeHapiness() {
		int happiness = 0;
		for (int i = 0; i < emotions.length; i++) {
			happiness += emotions[i] * emotionsPrioritiy[i];
		}
		if (happiness * 100 / getMaxHappiness() <= Values.LIFE_THRESHOLD) {
			setAlive(false);
			getBuilding().getHome()[this.getY()][this.getX()] = Objects.DEAD_SIM;
		}
		return happiness;
	}

	public int getMaxHappiness() {
		int sum = 0;
		for (int i = 0; i < emotions.length; i++) {
			sum += emotionsPrioritiy[i] * 100;
		}
		return sum;
	}

	// Check if the target was found.
	public static boolean checkTarget(Interaction i1, int x, int y) {
		int yTarget = i1.getY();
		int xTarget = i1.getX();
		return x == xTarget && y == yTarget;
	}

	public int getDistanceFrom(Sim sim) {
		return Math.abs(this.getX() - sim.getX()) + Math.abs(this.getY() - sim.getY());
	}

	// Satisfy sim social needs if close to another..
	public void socializing() {
		for (int i = 0; i < Simulation.sims.length; i++) {
			if (Simulation.sims[i].simCOLOR != this.simCOLOR
					&& getDistanceFrom(Simulation.sims[i]) < Values.SOCIAL_DISTANCE)
				this.setSocial(this.getSocial() + 1);
		}
	}

	public static boolean isNextTo(Interaction i1, int x, int y) {
		if (checkTarget(i1, x + 1, y))
			return true;
		if (checkTarget(i1, x - 1, y))
			return true;
		if (checkTarget(i1, x, y + 1))
			return true;
		if (checkTarget(i1, x, y - 1))
			return true;
		return false;
	}

	// Implementation of A-star algorithm to find shortest path to the target.
	public void nextMove(Interaction i1) {
		int colorArr[][] = new int[this.getBuilding().getMSZ()][this.getBuilding().getMSZ()];
		sims.map.Objects home[][] = this.getBuilding().getHome();
		PriorityQueue<Sim> queue = new PriorityQueue<Sim>(1, new SortByDistance());
		Sim simClone = new Sim(this);
		queue.add(simClone);
		int y, x;
		while (!queue.isEmpty()) {
			Sim pcurrent, pnext;
			pcurrent = queue.remove();
			y = pcurrent.getY();
			x = pcurrent.getX();
			colorArr[y][x] = 1;

			// Found the target
			if (checkTarget(i1, pcurrent.getX(), pcurrent.getY())) {

				// Change current node to previous color
				home[this.getY()][this.getX()] = this.getLastOb();
				Sim firstState = getFirstState(pcurrent);
				// Get the right step
				int newY = firstState.getY();
				int newX = firstState.getX();
				// Save the color of current node
				setLastOb(home[newY][newX]);
				// Set the node color to draw the sim later
				home[newY][newX] = this.getSimColor();
				// Set sim new location
				this.setX(newX);
				this.setY(newY);
				break;
			}

			// Check each direction of the sim and add nodes to the queue accordingly
			// Right
			pnext = checkNextStep(i1, colorArr, pcurrent, x + 1, y);
			if (pnext != null)
				queue.add(pnext);
			// Left
			pnext = checkNextStep(i1, colorArr, pcurrent, x - 1, y);
			if (pnext != null)
				queue.add(pnext);
			// Bottom
			pnext = checkNextStep(i1, colorArr, pcurrent, x, y + 1);
			if (pnext != null)
				queue.add(pnext);
			// Top
			pnext = checkNextStep(i1, colorArr, pcurrent, x, y - 1);
			if (pnext != null)
				queue.add(pnext);

		}
	}

	// Check a step toward the target, add to the queue if the step is permitted.
	public Sim checkNextStep(Interaction i1, int colorArr[][], Sim pcurrent, int x, int y) {
		Sim pnext;
		sims.map.Objects home[][] = this.getBuilding().getHome();
		// Next step should not be a node with objects other than floor.
		// Should also not be a node that has already been visited.
		if ((sims.map.Objects.isFloor(home[y][x]) || checkTarget(i1, x, y)) && colorArr[y][x] != 1) {
			pnext = new Sim(pcurrent);
			pnext.setSteps(pcurrent.getSteps() + 1);
			pnext.setX(x);
			pnext.setY(y);
			// Set the parent of the node
			pnext.setLastState(pcurrent);
			return pnext;
		}
		return null;
	}

	// Prevent wrong values
	private int limit(int num) {
		if (num > Values.MAX_VALUE)
			return Values.MAX_VALUE;
		else if (num < Values.MIN_VALUE)
			return Values.MIN_VALUE;
		return num;
	}

	// Returns the most promising step according to the A-star algorithm.
	private Sim getFirstState(Sim sim) {
		Sim current = sim, next = sim;
		while (current.getLastState() != null) {
			next = current;
			current = current.getLastState();
		}
		return next;
	}

	public Sim getLastState() {
		return lastState;
	}

	public void setLastState(Sim lastState) {
		this.lastState = lastState;
	}

	public sims.map.Objects getSimColor() {
		return simCOLOR;
	}

	public void setSimColor(sims.map.Objects simCOLOR) {
		this.simCOLOR = simCOLOR;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getEnergy() {
		return this.emotions[Emotions.ENERGY.ordinal()];
	}

	public void setEnergy(int energy) {
		this.emotions[Emotions.ENERGY.ordinal()] = limit(energy);
	}

	public int getHunger() {
		return this.emotions[Emotions.HUNGER.ordinal()];
	}

	public void setHunger(int hunger) {
		this.emotions[Emotions.HUNGER.ordinal()] = limit(hunger);
	}

	public int getFun() {
		return this.emotions[Emotions.FUN.ordinal()];
	}

	public void setFun(int fun) {
		this.emotions[Emotions.FUN.ordinal()] = limit(fun);
	}

	public int getBladder() {
		return this.emotions[Emotions.BLADDER.ordinal()];
	}

	public void setBladder(int bladder) {
		this.emotions[Emotions.BLADDER.ordinal()] = limit(bladder);
	}

	public int getHygiene() {
		return this.emotions[Emotions.HYGIENE.ordinal()];
	}

	public void setHygiene(int hygiene) {
		this.emotions[Emotions.HYGIENE.ordinal()] = limit(hygiene);
	}

	public int getSocial() {
		return this.emotions[Emotions.SOCIAL.ordinal()];
	}

	public void setSocial(int social) {
		this.emotions[Emotions.SOCIAL.ordinal()] = limit(social);
	}

	public static int getMinvalue() {
		return Values.MIN_VALUE;
	}

	public Time getTimer() {
		return timer;
	}

	public static int getMaxvalue() {
		return Values.MAX_VALUE;
	}

	public void setTimer(Time timer) {
		this.timer = timer;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public GeneralItem getCurrentInteraction() {
		return currentInteraction;
	}

	public void setCurrentInteraction(GeneralItem currentInteraction) {
		this.currentInteraction = currentInteraction;
	}

	public boolean isInInteraction() {
		return inInteraction;
	}

	public void setInInteraction(boolean inInteraction) {
		this.inInteraction = inInteraction;
	}

	public sims.map.Objects getLastOb() {
		return lastOb;
	}

	public void setLastOb(sims.map.Objects objects) {
		this.lastOb = objects;
	}

	public Interaction getCurrentTarget() {
		return currentTarget;
	}

	public void setCurrentTarget(Interaction currentTarget) {
		this.currentTarget = currentTarget;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public Plan getMyPlan() {
		return myPlan;
	}

	public void setMyPlan(Plan myPlan) {
		this.myPlan = myPlan;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public GeneralItem getLastInteraction() {
		return lastInteraction;
	}

	public void setLastInteraction(GeneralItem lastInteraction) {
		this.lastInteraction = lastInteraction;
	}

	public sims.map.Objects getOccupiedActivity() {
		return occupiedActivity;
	}

	public void setOccupiedActivity(sims.map.Objects occupiedActivity) {
		this.occupiedActivity = occupiedActivity;
	}

	public int getMyId() {
		return myId;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	@Override
	public String toString() {
		return "Sim ID - " + myId + "\nMax value = " + getMaxvalue() + "\nHunger = " + getHunger() + "\nEnergy = "
				+ getEnergy() + "\nHygiene = " + getHygiene() + "\nbladder = " + getBladder() + "\nSocial = "
				+ getSocial() + "\nFun = " + getFun();
	}

}
