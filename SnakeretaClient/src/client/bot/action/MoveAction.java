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
		getBot().moveTo(getBot().getCurrentTarget());
		Direction facing = getBot().getMoveDirection();
		if(facing != null) {
			getWorld().getCommunication().move(facing);
		}
		setInstant(System.nanoTime() + 400_000_000);
		return false;
	}

}
