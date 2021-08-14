package sims.variables;

public class Time {
	private int seconds;
	private long startTime;
	private boolean hasStarted;

	public Time(int seconds) {
		this.seconds = seconds;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public void StartTimer() {
		this.startTime = System.currentTimeMillis();
		setHasStarted(true);
	}

	public boolean checkTimer() {
		if (System.currentTimeMillis() - this.startTime > 1000 * this.seconds && hasStarted()) {
			setHasStarted(false);
			return true;
		}
		return false;
	}

	public boolean hasStarted() {
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}

}
