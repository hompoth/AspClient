package client.bot.action;

import java.io.IOException;

import client.Character;
import client.World;
import client.bot.JoshBot;

public class RegenBuffAction implements Action {

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
	
	public RegenBuffAction(JoshBot bot) {
		setWorld(bot.getWorld());
		setBot(bot);
		setSelf(getWorld().getSelf());
		setInstant(System.nanoTime());
	}

	@Override
	public boolean handle() throws IOException {
		if(getWorld().getClassName().equals("Priest")) {
			getWorld().getCommunication().cast(2,getSelf().loginId);
			for(Integer i : getWorld().getGroup()) {
				if(i == 0) continue;
				Character c = getWorld().getCharacter(i);
				if(c != null) {
					getWorld().getCommunication().cast(3, c.loginId);
				}
			}
			setInstant(System.nanoTime() + 1_000_000_000L * 15);
			return false;
		}
		else {
			getWorld().getCommunication().cast(2,getSelf().loginId);
			setInstant(System.nanoTime() + 1_000_000_000L * 30);
			return false;
		}
	}

}
