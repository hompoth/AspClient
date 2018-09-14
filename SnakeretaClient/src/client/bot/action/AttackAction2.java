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
import client.bot.Point;

public class AttackAction2 implements Action {
	
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
	
	public AttackAction2(JoshBot bot) {
		setWorld(bot.getWorld());
		setBot(bot);
		setSelf(getWorld().getSelf());
		setInstant(System.nanoTime());
	}

	public boolean handle() throws IOException {
		Character target;
		if(!getWorld().getClassName().equals("Priest") && getWorld().getLevel() == 50) {
			if(getWorld().getClassName().equals("Warrior") || getWorld().getClassName().equals("Rogue")) {
				if(getSelf().hp < 95) {
					setInstant(System.nanoTime());
					return false;
				}
				target = getBot().getCurrentTarget();
				Point cP = getBot().getCurrentPoint();
				if(target != null && getSelf().x == cP.x && getSelf().y == cP.y && getBot().distance(getSelf(),target)<=1) {
					attack(target,30);
					setInstant(System.nanoTime() + 500_000_000L);
					return false;
				}
			}
			else if(getWorld().getClassName().equals("Magus")){
				if(getSelf().mp < 95) {
					if(getSelf().hp > 90) {
						getWorld().getCommunication().cast(3,getSelf().loginId);
					}
					setInstant(System.nanoTime());
					return false;
				}
				target = getBot().getAttackTarget(14);
				if(target != null) {
					getWorld().getCommunication().cast(30,target.loginId);
					if(getSelf().hp > 90) {
						getWorld().getCommunication().cast(3,getSelf().loginId);
					}
					getBot().setCurrentTargetAttacked(true);
					setInstant(System.nanoTime() + 1_500_000_000L);
					return false;
				}
			}
		}
		else {
			target = getBot().getCurrentTarget();
			Point cP = getBot().getCurrentPoint();
			if(target != null && getSelf().x == cP.x && getSelf().y == cP.y && getBot().distance(getSelf(),target)<=1) {
				attack(target,0);
				setInstant(System.nanoTime() + 500_000_000L);
				return false;
			}
			
		}
		setInstant(System.nanoTime() + 100_000_000L);
		return false;
	}
	
	private void attack(Character target, int spellId) throws IOException {
		if(target == null) return;
		Direction facing = Direction.fromCharacter(getSelf(),target);
		if(facing != null && getSelf().facing != facing) {
			getSelf().facing = facing;
			getWorld().getCommunication().face(facing);
		}
		if(spellId < 1) {
			getWorld().getCommunication().attack();;
			getBot().setCurrentTargetAttacked(true);
		}
		else {
			getWorld().getCommunication().cast(spellId,getSelf().loginId);
			getBot().setCurrentTargetAttacked(true);
		}
	}
}
