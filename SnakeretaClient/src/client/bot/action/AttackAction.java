package client.bot.action;

import java.io.IOException;
import java.util.ArrayList;

import client.Character;
import client.CharacterType;
import client.Direction;
import client.Log;
import client.World;
import client.bot.Bot;
import client.bot.JoshBot;

public class AttackAction implements Action {
	
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
	
	public AttackAction(JoshBot bot) {
		setWorld(bot.getWorld());
		setBot(bot);
		setSelf(getWorld().getSelf());
		setInstant(System.nanoTime());
	}

	public boolean handle() throws IOException {
		Character target = getBot().getCurrentTarget();
		if(target != null) {
			attack(target);
		}
		setInstant(System.nanoTime() + 100_000_000);
		return false;
	}
	
	private void attack(Character target) throws IOException {
		//if(melee) warrior/rogue spell
		if(target == null) return;
		Direction facing = Direction.fromCharacter(getSelf(),target);
		if(facing != null && getSelf().facing != facing) {
			getSelf().facing = facing;
			getWorld().getCommunication().face(facing);
		}
		getWorld().getCommunication().attack();
		getBot().setCurrentTargetAttacked(true);
	}
}
