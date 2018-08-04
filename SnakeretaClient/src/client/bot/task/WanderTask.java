package client.bot.task;

import java.util.ArrayList;

import client.Character;
import client.Log;
import client.World;
import client.bot.JoshBot;
import client.bot.Point;

public class WanderTask implements Task {
	private int targetMap;
	
	private int __PathIndex;
	private int getPathIndex() {
		return __PathIndex;
	}
	private void setPathIndex(int index) {
		__PathIndex = index;
	}
	
	private ArrayList<Point> __OptimalPath;
	private ArrayList<Point> getOptimalPath(){
		return __OptimalPath;
	}
	private void setOptimalPath(ArrayList<Point> path) {
		__OptimalPath = path;
	}
	
	private Point getCurrentTarget() {
		return getOptimalPath().get(getPathIndex());
	}
	private void incrementPathIndex() {
		setPathIndex((getPathIndex()+1) % getOptimalPath().size());
	}
	
	private World __World;
	private World getWorld() {
		return __World;
	}
	private void setWorld(World world) {
		__World = world;
	}
	private JoshBot __Bot;
	private JoshBot getBot() {
		return __Bot;
	}
	private void setBot(JoshBot bot) {
		__Bot = bot;
	}

	private long __Instant;
	public long getInstant() {
		return __Instant;
	}
	private void setInstant(long instant) {
		__Instant = instant;
	}
	
	private Character __Self;
	public Character getSelf() {
		return __Self;
	}
	private void setSelf(Character self) {
		__Self = self;
	}
	
	public WanderTask(JoshBot bot) {
		setWorld(bot.getWorld());
		setBot(bot);
		setSelf(getWorld().getSelf());
		setInstant(System.nanoTime());
		targetMap = getWorld().getMapId();
		
		// Generate ArrayList path to take. Save index
		// Function: Cover as much of the map as possible with the least amount of waypoints. 
		// First find bounded box, and next find all points near an edge that cover as much space as possible, and then cover the rest in the order that covers as much space as possible. Also choose a point far away from the bounding wall.
		// Cost function: Edge 2, others 1. When an area is retrieved, fill in the new edges on the outer edge
		// Use simulated annealing to create optimal path
		// Loop through optimal path and choose closest node to start on. Then cycle on the optimal path
		setOptimalPath(new ArrayList<Point>());
		if(getWorld().getLevel() <= 25) {
			getOptimalPath().add(new Point(11,14));
			getOptimalPath().add(new Point(11,55));
			getOptimalPath().add(new Point(40,45));
			getOptimalPath().add(new Point(42,17));
		}
		else {
			getOptimalPath().add(new Point(21,96));
			getOptimalPath().add(new Point(10,96));
			getOptimalPath().add(new Point(9,90));
			getOptimalPath().add(new Point(9,81));
			getOptimalPath().add(new Point(21,88));
		}
		setPathIndex(0);
	}
	
	public boolean handle() {
		Character self = getWorld().getSelf();
		if(getBot().getTaskState() != TaskState.Wander) {
			getBot().setTaskState(TaskState.Idle);
			return true;
		}
		if(targetMap != getWorld().getMapId()) { // Character died or was teleported, so stop wandering
			getBot().setTaskState(TaskState.Idle);
			return true;
		}
		if(getCurrentTarget().x == self.x && getCurrentTarget().y == self.y) {
			incrementPathIndex();
		}
		getBot().setCurrentTarget(getCurrentTarget());
		setInstant(System.nanoTime());
		return false;
	}
}
