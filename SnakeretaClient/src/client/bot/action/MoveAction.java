package client.bot.action;

import java.io.IOException;
import java.util.Stack;


import client.Direction;
import client.Log;
import client.World;
import client.bot.JoshBot;
import client.bot.Point;

public class MoveAction implements Action {
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
	public void setInstant(long instant) {
		__Instant = instant;
	}
	
	public MoveAction(JoshBot bot) {
		setWorld(bot.getWorld());
		setBot(bot);
		setInstant(System.nanoTime());
	}

	public boolean handle() throws IOException {
		getBot().moveTo(getBot().getCurrentPoint());
		Direction facing = getBot().getMoveDirection();
		if(facing != null) {
			if(getBot().canPickUp(facing)) {
				getWorld().getCommunication().pickup();
			}
			getWorld().getCommunication().move(facing);
			if(getBot().canPickUp(facing)) {
				getWorld().getCommunication().pickup();
			}
		}
		setInstant(System.nanoTime() + 450_000_000L);
		return false;
	}

}
