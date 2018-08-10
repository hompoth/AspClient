package client.bot;

import client.World;
import client.bot.action.Action;
import client.bot.action.AttackAction;
import client.bot.action.BuffAction;
import client.bot.action.HealAction;
import client.bot.action.MoveAction;
import client.bot.task.AttackMobTask;
import client.bot.task.Task;
import client.bot.task.TaskState;
import client.bot.task.UpdateTask;
import client.bot.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeSet;

import client.Character;
import client.CharacterType;
import client.Communication;
import client.Direction;
import client.Item;
import client.Log;
import client.Tile;

public class JoshBot implements Bot {

	public boolean enabled = false;
	private Character __CurrentTarget;
	public Character getCurrentTarget() {
		return __CurrentTarget;
	}
	public void setCurrentTarget(Character character) {
		if(((character == null || getCurrentTarget() == null) && character != getCurrentTarget()) ||
		 (character != null && getCurrentTarget() != null && character.loginId != getCurrentTarget().loginId )) {
			setCurrentTargetAttacked(false);
		}
		__CurrentTarget = character;
	}
	
	private boolean __CurrentTargetAttacked;
	public boolean getCurrentTargetAttacked() {
		return __CurrentTargetAttacked;
	}
	public void setCurrentTargetAttacked(boolean wasAttacked) {
		__CurrentTargetAttacked = wasAttacked;
	}
	
	private Point __CurrentPoint;
	public Point getCurrentPoint() {
		return __CurrentPoint;
	}
	public void setCurrentPoint(Point point) {
		__CurrentPoint = point;
	}
	
	private World __World;
	public World getWorld() {
		return __World;
	}
	private void setWorld(World world) {
		__World = world;
	}

	private Stack<Point> __Path;
	public Stack<Point> getPath() {
		return __Path;
	}
	private void setPath(Stack<Point> path) {
		__Path = path;
	}
	
	private TaskState __TaskState;
	public TaskState getTaskState() {
		return __TaskState;
	}
	public void setTaskState(TaskState state) {
		__TaskState = state;
	}
	
	private TreeSet<Integer> __IgnoredLoginIds;
	public TreeSet<Integer> getIgnoredLoginIds() {
		return __IgnoredLoginIds;
	}
	public void setIgnoredLoginIds(TreeSet<Integer> set) {
		__IgnoredLoginIds = set;
	}
	public void ignoreLoginId(int loginId) {
		getIgnoredLoginIds().add(loginId);
	}
	public boolean isIgnored(int loginId) {
		return getIgnoredLoginIds().contains(loginId);
	}
	
	// Tasks: Pickup, Go to map, Kill mob, Kill boss, Run away, Buy/Sell items, Wander and kill, Update tasks/actions
	// Larger tasks added to TaskState enum
	// Only set a new larger task when TaskState.Idle
	private Queue<Task> __TaskQueue;
	private void setTaskQueue(Queue<Task> queue) {
		__TaskQueue = queue;
	}
	private Queue<Task> getTaskQueue(){
		return __TaskQueue;
	}
	public void addTask(Task task) {
		for(Task t : getTaskQueue()) { // Don't add the same task twice
			if(t.getClass().equals(task.getClass())) {
				return;
			}
		}
		getTaskQueue().add(task);
	}
	private void handleTask() throws IOException {
		if(!getTaskQueue().isEmpty()) {
			Task task = getTaskQueue().peek();
			if(task.getInstant() < System.nanoTime()) {
				boolean taskDone = task.handle();
				//Log.println(task.getClass());
				getTaskQueue().remove(task);
				if(!taskDone) { // Re-add task and re-compute instant priority
					addTask(task);
				}
			}
		}
	}
	private static Comparator<Task> taskComparator = new Comparator<Task>(){
		@Override
		public int compare(Task t1, Task t2) {
            return (int) (t1.getInstant() - t2.getInstant());
        }
	};
	
	// Actions: Move, Buff, Pickup, Attack, Heal
	private Queue<Action> __ActionQueue;
	private void setActionQueue(Queue<Action> queue) {
		__ActionQueue = queue;
	}
	private Queue<Action> getActionQueue(){
		return __ActionQueue;
	}
	public void addAction(Action action) {
		for(Action a : getActionQueue()) { // Don't add the same action twice
			if(a.getClass().equals(action.getClass())) {
				return;
			}
		}
		getActionQueue().add(action);
	}
	private void handleAction() throws IOException {
		if(!getActionQueue().isEmpty()) {
			Action action = getActionQueue().peek();
			//Log.println(action.getClass()+"----"+action.getInstant());
			if(action.getInstant() < System.nanoTime()) {
				//Log.println("2"+action.getClass()+"----"+action.getInstant());
				boolean actionDone = action.handle();
				getActionQueue().remove(action);
				if(!actionDone) { // Re-add action and re-compute instant priority
					addAction(action);
				}
			}
		}
	}
	private static Comparator<Action> actionComparator = new Comparator<Action>(){
		@Override
		public int compare(Action a1, Action a2) {
            return (int) (a1.getInstant() - a2.getInstant());
        }
	};
	
	private boolean __Enabled;
	public boolean getEnabled() {
		return __Enabled;
	}
	public void setEnabled(boolean enabled) {
		__Enabled = enabled;
		setTaskState(TaskState.Idle);
		getPath().clear();
		clearQueues();
		clearAttackPoints();
		if(enabled) {
			loadDefaultQueues();
		}
	}
	
	private ArrayList<Point> __AttackPoints;
	public ArrayList<Point> getAttackPoints() {
		return __AttackPoints;
	}
	public void setAttackPoints(ArrayList<Point> attackPoints) {
		__AttackPoints = attackPoints;
	}
	public void addAttackPoints(Character character, AttackType type, int closeRange, int outRange) {
		int x = character.x, y = character.y;
		if(type == AttackType.Melee) {
			for(int r = closeRange; r <= outRange; ++r) {
				getAttackPoints().add(new Point(x,y+r));
				getAttackPoints().add(new Point(x,y-r));
				getAttackPoints().add(new Point(x+r,y));
				getAttackPoints().add(new Point(x-r,y));
			}
		}
		else if(type == AttackType.Spray) {
			for(int r = closeRange; r <= outRange; ++r) {
				for(int s = -(r-1); s <= (r-1); ++s) {
					getAttackPoints().add(new Point(x+s,y+r));
					getAttackPoints().add(new Point(x+s,y-r));
					getAttackPoints().add(new Point(x+r,y+s));
					getAttackPoints().add(new Point(x-r,y+s));
				}
			}
		}
	}
	public void clearAttackPoints() {
		getAttackPoints().clear();
	}
	
	public JoshBot(World world) {
		setWorld(world);
		setPath(new Stack<Point>());
		setAttackPoints(new ArrayList<Point>());
		setTaskQueue(new PriorityQueue<Task>(taskComparator));
		setActionQueue(new PriorityQueue<Action>(actionComparator));
		setTaskState(TaskState.Idle);
		setIgnoredLoginIds(new TreeSet<Integer>());
		//setEnabled(true);
	}
	
	public void loadDefaultQueues() {
		addTask(new UpdateTask(this));
		addAction(new AttackAction(this));
		addAction(new BuffAction(this));
		addAction(new HealAction(this));
		addAction(new MoveAction(this));
	}
	public void clearQueues() {
		getTaskQueue().clear();
		getActionQueue().clear();
	}
	
	
	@Override
	public void update() throws IOException {
		// Handle Task
		handleTask();
		
		// Handle Action
		handleAction();
	}

	public void moveTo(Point p) {
		if(p != null) {
			moveTo(p.x,p.y);
		}
	}
	public void moveTo(int x, int y) {
		Character self = getWorld().getSelf();
		Stack<Point> path = PathFinding.getPath(getWorld(), self.x, self.y, x, y);
		setPath(path);
	}
	
	public Direction getMoveDirection() {
		if(!getPath().isEmpty()) {
			Character s = getWorld().getSelf();
			Point p = getPath().pop();
			return Direction.fromXY(p.x-s.x, p.y-s.y);
		}
		return null;
	}
	public Character getAttackTarget(int requiredDistance) {
		return getClosestCharacterInAttackRange(requiredDistance);
	}
	public Character getClosestCharacterInAttackRange(int requiredDistance){ // Consider moving this to AttackMobTask
		if(requiredDistance == 0) { // Default value
			requiredDistance = Integer.MAX_VALUE;
		}
		Character closestCharacter = null;
		Character self = getWorld().getSelf();
		int minDistance = Integer.MAX_VALUE;
		for(Character c : getWorld().getCharacters().values()) {
			if(c.loginId == getWorld().getSelfId()) continue;
			if(c.characterType != CharacterType.Mob) continue; // If not pvp map
			if(isIgnored(c.loginId)) continue;
			int curDistance = distance(c,self);
			if(curDistance < minDistance) {
				minDistance = curDistance;
				closestCharacter = c;
			}
		}
		return (minDistance <= requiredDistance)? closestCharacter:null;
	}
	public int distance(Character a, Character b) {
		if(a == null || b == null) return 0;
		return Math.abs(a.x-b.x) + Math.abs(a.y-b.y);
	}
	public int distance(Point a, Point b) {
		if(a == null || b == null) return 0;
		return Math.abs(a.x-b.x) + Math.abs(a.y-b.y);
	}
	public Point getClosestAttackPoint() {
		Character self = getWorld().getSelf();
		Point currentLocation = new Point(self.x, self.y);
		Point closestPoint = null;
		int minDistance = Integer.MAX_VALUE;
		for(Point p : getAttackPoints()) {
			if(getWorld().blockedTile(p.x, p.y)) continue;
			int curDistance = distance(p,currentLocation);
			if(curDistance < minDistance) {
				minDistance = curDistance;
				closestPoint = p;
			}
		}
		return closestPoint;
	}
	public Point getClosestItem(int requiredDistance) {
		if(requiredDistance == 0) { // Default value
			requiredDistance = Integer.MAX_VALUE;
		}
		Character self = getWorld().getSelf();
		Point currentLocation = new Point(self.x, self.y);
		Point closestPoint = null;
		int minDistance = Integer.MAX_VALUE;
		for(Point p : getWorld().getItems().values()) {
			int curDistance = distance(p,currentLocation);
			if(curDistance < minDistance) {
				minDistance = curDistance;
				closestPoint = p;
			}
		}
		return (minDistance <= requiredDistance)? closestPoint:null;
	}
	
	public boolean canPickUp() {
		Character self = getWorld().getSelf();
		Item item = getWorld().getTile(self.x, self.y).getItem();
		return item != null;
	}
	
	public void afterMapChange() throws IOException {
	}

	public void afterCharacterMiss(int loginId) {
		ignoreLoginId(loginId);
	}
	
	public void afterCharacterLeave(int loginId) {
		Character currentTarget = getCurrentTarget();
		if(currentTarget != null && currentTarget.loginId == loginId) {
			setCurrentTarget(null);
		}
	}
	public Point getFollowPoint() {
		Character self, player;
		int x,y,x2,y2;
		int radius = 3;
		int groupLoginId = getWorld().getGroup()[0];
		if(groupLoginId == 0) {
			return null;
		}
		
		self = getWorld().getSelf();
		player = getWorld().getCharacter(groupLoginId);

		if(distance(self,player) <= radius) {
			return new Point(self.x,self.y);
		}
		
		x = self.x;
		y = self.y;
		x2 = player.x;
		y2 = player.y;
		
		double angle = Math.atan2(y2-y, x2-x);
		int diffX = (int) Math.round(radius * Math.cos(angle));
		int diffY = (int) Math.round(radius * Math.sin(angle));
		
		Point newLocation = new Point(x+diffX,y+diffY);
		
		return newLocation;
	}
}
