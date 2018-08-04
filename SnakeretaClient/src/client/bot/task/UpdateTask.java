package client.bot.task;

import java.io.IOException;

import client.Log;
import client.World;
import client.Character;
import client.bot.JoshBot;

public class UpdateTask implements Task {
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
	
	public UpdateTask(JoshBot bot) {
		setWorld(bot.getWorld());
		setBot(bot);
		setSelf(getWorld().getSelf());
		setInstant(System.nanoTime());
	}
	public boolean handle() throws IOException {
		TaskState state = getBot().getTaskState();
		Character self = getWorld().getSelf();
		Log.println("Task State: "+state);
		if(getBot().getAttackTarget(5) != null && state != TaskState.AttackMob
                && !((state == TaskState.GoToWaypoint || state == TaskState.Idle) && !self.isSurrounded(getWorld()))) {
			getBot().setTaskState(TaskState.AttackMob);
			getBot().addTask(new AttackMobTask(getBot()));
		}
		else if(state == TaskState.Idle) {
			if(getWorld().getLevel() <= 25 && !(getWorld().getMapId() == 2)) {
				getBot().setTaskState(TaskState.GoToWaypoint);
				getBot().addTask(new GoToWaypointTask(getBot(), 2, 11, 14));
			}
			else if(getWorld().getLevel() > 25 && !(getWorld().getMapId() == 2 && 
					getSelf().x < 51 && getSelf().y < 56)) {
				getBot().setTaskState(TaskState.GoToWaypoint);
				getBot().addTask(new GoToWaypointTask(getBot(), 2, 21, 96));
			}
			else {
				getBot().setTaskState(TaskState.Wander);
				getBot().addTask(new WanderTask(getBot()));
			}
		}
		setInstant(System.nanoTime());
		return false;
	}

}


/*
			if(self.level <= 25) {
				switch(world.getMapId()) {
				case 1: // Minita
					// Set state = Head to portal and attack if surrounded.
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackIfSurrounded;
					portal.add(new Point(1,50));
					break;
				case 8: // Roadkill Woods
					// Set state = Head to portal and attack if surrounded.
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackIfSurrounded;
					portal.add(new Point(62,5));
					break;
				case 2: // Its the Bat Cave Robin
					// Set state = Kill closest and head to next waypoint
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackClosest;
					requiredDistance = 5;
					mustFaceTarget = true;
					portal.add(new Point(11,14));
					portal.add(new Point(11,55));
					portal.add(new Point(40,45));
					portal.add(new Point(42,17));
					break;
				}
			}
			else if(self.level <= 45) {
				switch(world.getMapId()) {
				case 1: // Minita
					// Set state = Head to portal and attack if surrounded.
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackIfSurrounded;
					portal.add(new Point(1,50));
				case 8: // Roadkill Woods
					// Set state = Head to portal and attack if surrounded.
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackIfSurrounded;
					portal.add(new Point(62,5));
				case 2: // Its the Bat Cave Robin
					if(self.x < 51 && self.y < 56) { // Bats
						// Set state = Head to portal and attack if surrounded.
						movementState = BotMovementState.GotoWaypoint;
						attackState = BotAttackState.AttackIfSurrounded;
						portal.add(new Point(29,22));
					}
					else { // Unloveds
						// Set state = Kill closest and head to next waypoint
						movementState = BotMovementState.GotoWaypoint;
						attackState = BotAttackState.AttackClosest;
						requiredDistance = 2;
						portal.add(new Point(9,81));
						portal.add(new Point(11,95));
						portal.add(new Point(20,89));
					}
				}
			}
			else {
				switch(world.getMapId()) {
				case 11: // Otherlands
					// Set state = Head to portal and attack if surrounded.
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackIfSurrounded;
					portal.add(new Point(100,22));
				case 12: // Mindless Mines
					// Set state = Kill closest and head to next waypoint
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackClosest;
					requiredDistance = 5;
					portal.add(new Point(19,13));
					portal.add(new Point(22,37));
					portal.add(new Point(93,60));
					portal.add(new Point(90,24));
				default:
					comm.use(30); // Teleport to Otherlands
				}
			}
*/