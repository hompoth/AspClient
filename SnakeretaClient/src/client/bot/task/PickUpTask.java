package client.bot.task;

import client.Character;
import client.Log;
import client.World;
import client.bot.AttackType;
import client.bot.JoshBot;
import client.bot.Point;

public class PickUpTask implements Task {

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
	
	public PickUpTask(JoshBot bot) {
		setWorld(bot.getWorld());
		setBot(bot);
		setSelf(getWorld().getSelf());
		setInstant(System.nanoTime());
	}

	public boolean handle() {
		if(getBot().getTaskState() != TaskState.PickUp) {
			getBot().setTaskState(TaskState.Idle);
			return true;
		}
		Point goToPoint = getBot().getClosestItem(2);
		if(goToPoint == null) {
			getBot().setTaskState(TaskState.Idle);
			return true;
		}
		getBot().setCurrentPoint(goToPoint);
		setInstant(System.nanoTime());
		return false;
	}
}
