package sims.items;

import sims.ai.Simulation;
import sims.character.Sim;
import sims.map.Objects;
import sims.map.Room;
import sims.variables.Time;

public abstract class GeneralItem implements itemInterface {
	int x, y, height, width;
	private Objects objectID;
	private int satisfaction;
	private Time time[];
	private Interaction interaction[];
	private int captureID[];

	public GeneralItem(int interactions) {
		// Each item has different number of interactions.
		this.interaction = new Interaction[interactions];
		// When the Sim finds an available interaction, he catches it for later use.
		this.setCaptureID(new int[interactions]);
		this.time = new Time[interactions];
		for (int i = 0; i < captureID.length; i++) {
			captureID[i] = -1;
		}
	}

	// Each new interaction gets its own place in the array.
	public boolean setInteraction(Interaction interaction) {
		for (int i = 0; i < getInteraction().length; i++) {
			if (getInteraction()[i] == null) {
				getInteraction()[i] = interaction;
				return true;
			}
		}
		return false;
	}

	// General drawing function
	public void simpleDraw(Objects arr[][]) {
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				arr[this.getY() + i][this.getX() + j] = getObjectID();
			}
		}
	}
	
	// Make sure no Sim is blocking the target.
	private boolean checkIfBlocked(Sim sim, int interactionIndex) {
		// If the Sim is already interacting, no need to check..
		if (sim.isInInteraction())
			return false;
		for (int i = 0; i < Simulation.sims.length; i++) {
			Sim aSim = Simulation.sims[i];
			if (aSim.getMyId() != sim.getMyId()) // Exclude current Sim from the check.
				if ((aSim.getX() == this.getInteraction()[interactionIndex].getX()
						&& aSim.getY() == this.getInteraction()[interactionIndex].getY())
						|| Sim.isNextTo(this.getInteraction()[interactionIndex], aSim.getX(), aSim.getY()))
					return true;
		}
		return false;
	}

	public int findMyPlace(Sim sim) {
		int index = -1;
		for (int i = 0; i < captureID.length; i++) {
			if (!checkIfBlocked(sim, i)) {
				if (captureID[i] == sim.getMyId())
					return i;
				if (captureID[i] == -1)
					index = i;
			} else if (captureID[i] == sim.getMyId())
				captureID[i] = -1;
		}
		return index;
	}

	// General function to check interactions and set next steps of the Sim.
	public boolean checkAndSet(Sim sim) {
		int index = findMyPlace(sim);
		if (index != -1)
			captureID[index] = sim.getMyId();
		else {
			// Abort the plan, find a different activity.
			sim.setOccupiedActivity(this.getObjectID());
			sim.getMyPlan().setDeadPlan(true);
			sim.setInInteraction(false);
			return true;
		}

		if (sim.getX() == getInteraction()[index].getX() && sim.getY() == getInteraction()[index].getY()) {
			// interact if not already using the object
			if (!sim.isInInteraction()) {
				sim.setInInteraction(true);
				getTime()[index].StartTimer();
				return false;
			} else {
				// Reset interaction after finishing
				if (this.getTime()[index].checkTimer()) {
					this.captureID[index] = -1;
					sim.setInInteraction(false);
				} else // Still interacting
					return false;
			}
		} else {
			sim.setCurrentTarget(this.getInteraction()[index]);
			sim.nextMove(this.getInteraction()[index]);
			return false;
		}
		// Finished interaction successfully
		return true;
	}

	// Location of the object in the chosen room
	public void setLocation(Room room, int x, int y) {
		this.x = room.getLeftWall() + x;
		this.y = room.getUpperWall() + y;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(int satisfaction) {
		this.satisfaction = satisfaction;
	}

	public Time[] getTime() {
		return time;
	}

	public void setTime(int time) {
		for (int i = 0; i < this.time.length; i++) {
			this.time[i] = new Time(time);
		}
	}

	public Objects getObjectID() {
		return objectID;
	}

	public void setObjectID(Objects obID) {
		this.objectID = obID;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Interaction[] getInteraction() {
		return interaction;
	}

	public int[] getCaptureID() {
		return captureID;
	}

	public void setCaptureID(int captureID[]) {
		this.captureID = captureID;
	}

}
