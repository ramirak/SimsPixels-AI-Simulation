package sims.character;

import java.util.LinkedList;

import sims.items.GeneralItem;

public class Plan {
	LinkedList<GeneralItem> myPlan;
	private int planStage;
	private boolean deadPlan;
	private GeneralItem initiator;

	public Plan(GeneralItem item) {
		setMyPlan(new LinkedList<GeneralItem>());
		setDeadPlan(true);
		setPlanStage(-1);
		setInitiator(item);
	}

	private void setInitiator(GeneralItem initiator) {
		this.initiator = initiator;
	}

	public GeneralItem getInitiator() {
		return initiator;
	}

	public void addToPlan(GeneralItem item) {
		this.myPlan.add(item);
		setDeadPlan(false);
	}

	public GeneralItem nextStage() {
		if (isDeadPlan() || getPlanStage() >= getMyPlan().size() - 1) {
			setDeadPlan(true);
			return null;
		}
		setPlanStage(planStage + 1);
		return getMyPlan().get(planStage);
	}

	public LinkedList<GeneralItem> getMyPlan() {
		return myPlan;
	}

	private void setMyPlan(LinkedList<GeneralItem> myPlan) {
		this.myPlan = myPlan;
	}

	public int getPlanStage() {
		return planStage;
	}

	private void setPlanStage(int planStage) {
		this.planStage = planStage;
	}

	public boolean isDeadPlan() {
		return deadPlan;
	}

	public void setDeadPlan(boolean deadPlan) {
		this.deadPlan = deadPlan;
	}

}
