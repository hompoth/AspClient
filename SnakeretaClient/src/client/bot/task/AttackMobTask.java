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
	
	private int __LastTargetAttackedId;
	private int getLastTargetAttackedId() {
		return __LastTargetAttackedId;
	}
	private void setLastTargetAttackedId(int loginId) {
		__LastTargetAttackedId = loginId;
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
		Character currentTarget = getBot().getCurrentTarget();
		if(getBot().getTaskState() != TaskState.AttackMob) {
			getBot().setTaskState(TaskState.Idle);
			getBot().setCurrentTarget(null);
			return true;
		}
		getBot().clearAttackPoints();
		Character target = getBot().getAttackTarget(5);		
		if(target == null) {
			getBot().setTaskState(TaskState.Idle);
			getBot().setCurrentTarget(null);
			return true;
		}
		if(getLastTargetAttackedId() == target.loginId) {
			long currentTime = getTimeFirstAttackedLastTarget();
			if(currentTime + 3_000_000_000L < System.nanoTime()) { // 3 seconds past
				if(getLastTargetInitialHp() - 5 < target.hp) {
					getBot().ignoreLoginId(target.loginId);
					getBot().setTaskState(TaskState.Idle);
					getBot().setCurrentTarget(null);
					return true;
				}
			}
		}
		else if(getBot().getCurrentTargetAttacked() && currentTarget != null) { 
			setLastTargetAttackedId(currentTarget.loginId);
			setLastTargetInitialHp(currentTarget.hp);
			setTimeFirstAttackedLastTarget(System.nanoTime());
		}
		
		getBot().addAttackPoints(target, AttackType.Melee,1,1);
		Point attackPosition = getBot().getClosestAttackPoint();
		getBot().setCurrentPoint(attackPosition);
		getBot().setCurrentTarget(target);
		setInstant(System.nanoTime());
		return false;
	}
}
