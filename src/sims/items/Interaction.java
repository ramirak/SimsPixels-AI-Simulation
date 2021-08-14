package sims.items;

public class Interaction {
	private int x, y;

	public int getX() {
		return x;
	}

	public Interaction(int x, int y) {
		setX(x);
		setY(y);
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

}
