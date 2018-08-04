package client.bot.task;

import client.Character;
import client.Log;
import client.World;
import client.bot.AttackType;
import client.bot.JoshBot;
import client.bot.Point;

public class AttackMobTask implements Task {

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
	
	private int __LastTargetId;
	private int getLastTargetId() {
		return __LastTargetId;
	}
	private void setLastTargetId(int loginId) {
		__LastTargetId = loginId;
	}
	
	private int __LastTargetInitialHp;
	private int getLastTargetInitialHp() {
		return __LastTargetInitialHp;
	}
	private void setLastTargetInitialHp(int hp) {
		__LastTargetInitialHp = hp;
	}
	
	private long __TimeFirstAttackedLastTarget;
	private long getTimeFirstAttackedLastTarget() {
		return __TimeFirstAttackedLastTarget;
	}
	private void setTimeFirstAttackedLastTarget(long time) {
		__TimeFirstAttackedLastTarget = time;
	}
	
	public AttackMobTask(JoshBot bot) {
		setWorld(bot.getWorld());
		setBot(bot);
		setSelf(getWorld().getSelf());
		setInstant(System.nanoTime());
	}

	public boolean handle() {
		if(getBot().getTaskState() != TaskState.AttackMob) {
			getBot().setTaskState(TaskState.Idle);
			return true;
		}
		getBot().clearAttackPoints();
		Character target = getBot().getAttackTarget(5);		
		if(target == null) {
			getBot().setTaskState(TaskState.Idle);
			return true;
		}
		if(getLastTargetId() == target.loginId) {
			long currentTime = getTimeFirstAttackedLastTarget() + 5000000000L;
			if(currentTime < System.nanoTime()) { // 5 seconds past
				if(getLastTargetInitialHp() < target.hp + 10) {
					getBot().ignoreLoginId(target.loginId);
					getBot().setTaskState(TaskState.Idle);
					return true;
				}
			}
		}
		else {
			setLastTargetId(target.loginId);
			setLastTargetInitialHp(target.hp);
			setTimeFirstAttackedLastTarget(System.nanoTime());
			Log.println("1----------------------------"+getTimeFirstAttackedLastTarget());
		}
		getBot().addAttackPoints(target, AttackType.Melee,1);
		Point attackPosition = getBot().getClosestAttackPoint();
		getBot().setCurrentTarget(attackPosition);
		setInstant(System.nanoTime());
		return false;
	}
}
