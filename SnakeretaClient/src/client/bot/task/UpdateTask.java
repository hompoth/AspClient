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
		for(Character c : getWorld().getCharacters().values()) {
			if(c.loginId == getSelf().loginId) continue;
			if(getWorld().groupContains(c.loginId)) continue;
			if(c.name.equals("Monk") || c.name.equals("Mime") || c.name.equals("Mute")) {
				getWorld().getCommunication().groupAdd(c.name);
			}
		}
		if(state != TaskState.FollowGroup && getWorld().getClassName().equals("Priest") && getBot().getFollowPoint() != null) {
			getBot().setTaskState(TaskState.FollowGroup);
			getBot().addTask(new FollowGroupTask(getBot()));
		}
		else if(state != TaskState.AttackMob && getBot().getAttackTarget(5) != null) {
	    //  	&& !((state == TaskState.GoToWaypoint || state == TaskState.Idle) && !self.isSurrounded(getWorld()))) {
			getBot().setTaskState(TaskState.AttackMob);
			getBot().addTask(new AttackMobTask(getBot()));
		}
		else if(state == TaskState.Idle) {
			/*if(getBot().getClosestItem(2) != null) {
				getBot().setTaskState(TaskState.PickUp);
				getBot().addTask(new PickUpTask(getBot()));
			}
			else*/ if(getWorld().getLevel() <= 20 && !(getWorld().getMapId() == 2)) {
				getBot().setTaskState(TaskState.GoToWaypoint);
				getBot().addTask(new GoToWaypointTask(getBot(), 2, 11, 14));
			}
			else if(getWorld().getLevel() > 20 && !(getWorld().getMapId() == 2 && 
					getSelf().x < 51 && getSelf().y < 56)) {
				getBot().setTaskState(TaskState.GoToWaypoint);
				getBot().addTask(new GoToWaypointTask(getBot(), 2, 12, 95));
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