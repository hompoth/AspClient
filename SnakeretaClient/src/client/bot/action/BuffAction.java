package client.bot.action;

import java.io.IOException;

import client.Character;
import client.Log;
import client.World;
import client.bot.Bot;
import client.bot.JoshBot;

public class BuffAction implements Action {

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
	
	public BuffAction(JoshBot bot) {
		setWorld(bot.getWorld());
		setBot(bot);
		setSelf(getWorld().getSelf());
		setInstant(System.nanoTime());
		lastInstant = System.nanoTime();
	}
	long lastInstant;
	public boolean handle() throws IOException {
		if(lastInstant + 1_000_000_000L * 15 < getInstant()) {
			getWorld().getCommunication().cast(2,getSelf().loginId);
			lastInstant = getInstant();
		}
		setInstant(System.nanoTime());
		return false;
	}

}
