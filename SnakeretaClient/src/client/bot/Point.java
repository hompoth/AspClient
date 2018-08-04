package client.bot;

import client.Direction;

public class Point {
	public int x;
	public int y;
	public Point previous;
	public int estimatedDistance;
	public int currentDistance;
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
