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
		if(getBot().canPickUp()) {
			getWorld().getCommunication().pickup();
		}
		getBot().moveTo(getBot().getCurrentPoint());
		Direction facing = getBot().getMoveDirection();
		if(facing != null) {
			getWorld().getCommunication().move(facing);
		}
		setInstant(System.nanoTime() + 350_000_000L);
		return false;
	}

}
