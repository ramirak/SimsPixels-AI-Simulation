package sims.ai;

import sims.character.Sim;
import sims.items.GeneralItem;

public class ActivityNode {
	private GeneralItem activity;
	private ActivityNode parent;
	private int nodeDepth;
	private Sim simClone;
	private boolean hasChanged;

	public ActivityNode(GeneralItem activity, Sim simClone) {
		this.setActivity(activity);
		this.nodeDepth = 0;
		this.setSim(simClone);
		this.setHasChanged(false);
	}

	public void setParent(ActivityNode node) {
		this.parent = node;
		this.nodeDepth = node.getNodeDepth() + 1;
	}

	public ActivityNode getParent() {
		return parent;
	}

	public int getNodeDepth() {
		return nodeDepth;
	}

	public void setNodeDepth(int nodeDepth) {
		this.nodeDepth = nodeDepth;
	}

	public Sim getSim() {
		return simClone;
	}

	public void setSim(Sim simClone) {
		this.simClone = simClone;
	}

	public GeneralItem getActivity() {
		return activity;
	}

	public void setActivity(GeneralItem activity) {
		this.activity = activity;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

}
