package sims.map;

public class Room {
	private int leftWall, UpperWall, width, height;
	
	public Room(int leftWall,int UpperWall,int width,int height) {
		this.leftWall = leftWall;
		this.UpperWall = UpperWall;
		this.width = width;
		this.height = height;
	}

	public int getLeftWall() {
		return leftWall;
	}

	public void setLeftWall(int leftWall) {
		this.leftWall = leftWall;
	}

	public int getUpperWall() {
		return UpperWall;
	}

	public void setUpperWall(int upperWall) {
		UpperWall = upperWall;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
