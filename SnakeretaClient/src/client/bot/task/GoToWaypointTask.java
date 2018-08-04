package client.bot.task;

import client.World;
import client.Character;
import client.Log;
import client.bot.JoshBot;
import client.bot.Point;

public class GoToWaypointTask implements Task {
	private int targetX;
	private int targetY;
	private int targetMap;
	
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
	
	public GoToWaypointTask(JoshBot bot, int map, int x, int y) {
		setWorld(bot.getWorld());
		setBot(bot);
		setSelf(getWorld().getSelf());
		setInstant(System.nanoTime());
		targetMap = map;
		targetX = x;
		targetY = y;
	}

	public boolean handle() {		
		if(getBot().getTaskState() != TaskState.GoToWaypoint) {
			getBot().setTaskState(TaskState.Idle);
			return true;
		}
		Character self = getWorld().getSelf();
		if(targetMap == getWorld().getMapId() && targetX == self.x && targetY == self.y) {
			getBot().setTaskState(TaskState.Idle);
			return true;
		}
		Point currentTarget = getWaypoint(targetMap, targetX, targetY);
		getBot().setCurrentTarget(currentTarget);
		setInstant(System.nanoTime());
		return false;
	}
	private Point getWaypoint(int targetMap, int targetX, int targetY) {
		// Handle map portals (to new maps and new locations on the same map)
		switch(getWorld().getMapId()) {
		case 1:
			return new Point(1,50);
		case 8:
			return new Point(62,5);
		case 2:
			if(getWorld().getLevel() <= 25) {
				return new Point(targetX,targetY);
			}
			else {
				if(getSelf().x < 51 && getSelf().y < 56) { 
					return new Point(29,22);
				}
				else {
					return new Point(targetX,targetY);
				}
			}
		}
		return new Point(50,50);
	}

}