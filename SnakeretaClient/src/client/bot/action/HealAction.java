package client.bot.action;

import java.io.IOException;

import client.Character;
import client.Log;
import client.World;
import client.bot.Bot;
import client.bot.JoshBot;

public class HealAction implements Action {

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
	
	public HealAction(JoshBot bot) {
		setWorld(bot.getWorld());
		setBot(bot);
		setSelf(getWorld().getSelf());
		setInstant(System.nanoTime());
	}

	public boolean handle() throws IOException {
		for(Integer i : getWorld().getGroup()) {
			if(i == 0) continue;
			Character c = getWorld().getCharacter(i);
			if(c != null) {
				if(c.hp < 75) {
					getWorld().getCommunication().cast(1, c.loginId);
				}
			}
		}
		if(getSelf().hp < 75) {
			getWorld().getCommunication().cast(1, getSelf().loginId);
		}
		setInstant(System.nanoTime() + 10_000_000L);
		return false;
	}

}
