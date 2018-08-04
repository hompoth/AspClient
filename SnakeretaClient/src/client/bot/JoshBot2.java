package client.bot;

import client.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import client.Character;
import client.CharacterType;
import client.Communication;
import client.Direction;
import client.Log;
import client.Tile;
enum BotMovementState {
	Idle,
	GotoWaypoint
}
enum BotAttackState {
	Idle,
	AttackIfSurrounded,
	AttackClosest
}
public class JoshBot2 implements Bot {
	private World world;
	private Communication comm;
	private BotMovementState movementState;
	private BotAttackState attackState;
	private ArrayList<Point> portal;
	private int portalIndex;
	private Stack<Point> path;
	private Point[][] map;
	private boolean mustFaceTarget;
	private int requiredDistance;
	private int targetId;
	
	public JoshBot2(World world) {
		this.world = world;
		this.comm = world.getCommunication();
		this.movementState = BotMovementState.Idle;
		this.attackState = BotAttackState.Idle;
		this.portal = new ArrayList<Point>();
		this.mustFaceTarget = false;
		this.requiredDistance = 0;
		this.portalIndex = 0;
		this.path = new Stack<Point>();
		this.targetId = 0;
	}
	
	@Override
	public void update() throws IOException {
		Character self = world.getSelf();
		if(self.hp < 60) {
			this.comm.cast(1, self.loginId);
		}
		if(portal.size() > 0) {
			// Generate path to next waypoint
			int targetX = portal.get(portalIndex).x;
			int targetY = portal.get(portalIndex).y;
			Character c = findClosestTarget(999, false, targetX, targetY);
			if(c != null) {// && (attackState == BotAttackState.AttackClosest
					//|| attackState == BotAttackState.AttackIfSurrounded && c.isSurrounded(world))) {
				targetX = c.x;
				targetY = c.y;
				targetId = c.loginId;
			}
			else {
				targetId = 0;
			}
			path = getPath(self.x, self.y, targetX, targetY);
			if(path.empty()) {
				portalIndex = (portalIndex + 1) % portal.size();
				Log.println("Switch portal");
			} else {
				Point p = path.peek();
				if(p.x == self.x && p.y == self.y) p = path.pop();
				comm.move(Direction.fromXY(p.x-self.x, p.y-self.y));
			}
			if(targetId > 0) {
				comm.attack();
				Log.println(path.size());
			}
		}
		
	}
	public Stack<Point> getPath() {
		return path;
	}
	private Stack<Point> getPath(int x, int y, int targetX, int targetY) {
		LinkedList<Point> openList = new LinkedList<Point>();
		LinkedList<Point> closedList = new LinkedList<Point>();
		map = new Point[100][100];
        map[y][x]=new Point(x,y);
        openList.add(map[y][x]); // add starting node to open list

        Point current;
        while (true) {
            current = closestDistance(openList, targetX, targetY); // get node with lowest fCosts from openList
            closedList.add(current); // add current node to closed list
            openList.remove(current); // delete current node from open list

            if ((current.x == targetX)
                    && (current.y == targetY)) { // found goal
                return createPath(map[y][x], current);
            }

            // for all adjacent nodes:
            LinkedList<Point> adjPoints = getAdjacent(closedList,current);
            for (int i = 0; i < adjPoints.size(); i++) {
                Point currentAdj = adjPoints.get(i);
                if (!openList.contains(currentAdj)) { // node is not in openList
                    currentAdj.previous = current; // set current node as previous for this node
                    currentAdj.estimatedDistance = distance(current.x, current.y, targetX, targetY); // set h costs of this node (estimated costs to goal)
                    currentAdj.currentDistance = current.currentDistance + 1; // set g costs of this node (costs from start to this node)
                    openList.add(currentAdj); // add node to openList
                } else { // node is in openList
                    if (currentAdj.currentDistance > current.currentDistance + 1) { // costs from current node are cheaper than previous costs
                        currentAdj.previous = current; // set current node as previous for this node
                        currentAdj.currentDistance = current.currentDistance + 1; // set g costs of this node (costs from start to this node)
                    }
                }
            }

            if (openList.isEmpty()) { // no path exists
                return new Stack<Point>(); // return empty list
            }
        }
	}

	private LinkedList<Point> getAdjacent(LinkedList<Point> closedList, Point p) {
		LinkedList<Point> list = new LinkedList<Point>();
		for(int x = -1; x <= 1; x+=2) {
			if(p.x+x-1 >= 0 && p.x+x-1 < 100) {
				if(map[p.y-1][p.x+x-1] == null){
					map[p.y-1][p.x+x-1] = new Point(p.x+x,p.y);
				}
				if(!closedList.contains(map[p.y-1][p.x+x-1]) && !world.blockedTile(p.x+x,p.y)) list.add(map[p.y-1][p.x+x-1]);
			}
		}
		for(int y = -1; y <= 1; y+=2) {
			if(p.y+y-1 >= 0 && p.y+y-1 < 100) {
				if(map[p.y+y-1][p.x-1] == null) {
					map[p.y+y-1][p.x-1] = new Point(p.x,p.y+y);
				}
				if(!closedList.contains(map[p.y+y-1][p.x-1]) && !world.blockedTile(p.x, p.y+y)) list.add(map[p.y+y-1][p.x-1]);
			}
		}
		return list;
	}

	private Stack<Point> createPath(Point point, Point current) {
		Stack<Point> path = new Stack<Point>();
		path.add(current);
		current = current.previous;
		while(current!=null && current.previous!=null) {
			path.add(current);
			current = current.previous;
		}
		return path;
	}

	private Point closestDistance(LinkedList<Point> openList, int targetX, int targetY) {
		Point closestPoint = null;
		int minDistance = Integer.MAX_VALUE;
		for(Point p : openList) {
			int curDistance = distance(p.x,p.y,targetX,targetY);
			if(curDistance < minDistance) {
				minDistance = curDistance;
				closestPoint = p;
			}
		}
		return closestPoint;
	}

	private int distanceBetweenCharacters(Character c1, Character c2) {
		return distance(c1.x, c2.x, c1.y, c2.y);
	}
	private int distance(int x, int y, int x2, int y2) {
		return Math.abs(x-x2) + Math.abs(y-y2);
	}
	
	
	private Character findClosestTarget(int withinRange, boolean mustFaceTarget, int targetX, int targetY){
		Log.println(targetId+ " : " + world.getSelf().loginId);
		if(targetId > 0 && targetId != world.getSelf().loginId) {
			return world.getCharacter(targetId);
		}
		Character self = world.getSelf();
		Character closestCharacter = null;
		int minDistance = withinRange, curDistance;
		for(Character c : world.getCharacters().values()) {
			if(c.characterType == CharacterType.Mob) {
				if(mustFaceTarget && !self.isFacing(c, targetX, targetY)) continue;
				curDistance = distanceBetweenCharacters(c, self);
				if(curDistance < minDistance) {
					minDistance = curDistance;
					closestCharacter = c;
				}
			}
		}
		return closestCharacter;
	}
	
	public void afterMapChange() throws IOException {
		prepareBot();
	}
	public void prepareBot() throws IOException {
		Character self = world.getSelf();
		if(self != null) {
			movementState = BotMovementState.Idle;
			attackState = BotAttackState.Idle;
			requiredDistance = 0;
			mustFaceTarget = false;
			portalIndex = 0;
			portal.clear();
			path.clear();
			if(world.getLevel() <= 25) {
				switch(world.getMapId()) {
				case 1: // Minita
					// Set state = Head to portal and attack if surrounded.
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackIfSurrounded;
					portal.add(new Point(1,50));
					break;
				case 8: // Roadkill Woods
					// Set state = Head to portal and attack if surrounded.
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackIfSurrounded;
					portal.add(new Point(62,5));
					break;
				case 2: // Its the Bat Cave Robin
					// Set state = Kill closest and head to next waypoint
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackClosest;
					requiredDistance = 5;
					mustFaceTarget = true;
					portal.add(new Point(11,14));
					portal.add(new Point(11,55));
					portal.add(new Point(40,45));
					portal.add(new Point(42,17));
					break;
				}
			}
			else if(world.getLevel() <= 45) {
				switch(world.getMapId()) {
				case 1: // Minita
					// Set state = Head to portal and attack if surrounded.
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackIfSurrounded;
					portal.add(new Point(1,50));
				case 8: // Roadkill Woods
					// Set state = Head to portal and attack if surrounded.
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackIfSurrounded;
					portal.add(new Point(62,5));
				case 2: // Its the Bat Cave Robin
					if(self.x < 51 && self.y < 56) { // Bats
						// Set state = Head to portal and attack if surrounded.
						movementState = BotMovementState.GotoWaypoint;
						attackState = BotAttackState.AttackIfSurrounded;
						portal.add(new Point(29,22));
					}
					else { // Unloveds
						// Set state = Kill closest and head to next waypoint
						movementState = BotMovementState.GotoWaypoint;
						attackState = BotAttackState.AttackClosest;
						requiredDistance = 2;
						portal.add(new Point(9,81));
						portal.add(new Point(11,95));
						portal.add(new Point(20,89));
					}
				}
			}
			else {
				switch(world.getMapId()) {
				case 11: // Otherlands
					// Set state = Head to portal and attack if surrounded.
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackIfSurrounded;
					portal.add(new Point(100,22));
				case 12: // Mindless Mines
					// Set state = Kill closest and head to next waypoint
					movementState = BotMovementState.GotoWaypoint;
					attackState = BotAttackState.AttackClosest;
					requiredDistance = 5;
					portal.add(new Point(19,13));
					portal.add(new Point(22,37));
					portal.add(new Point(93,60));
					portal.add(new Point(90,24));
				default:
					comm.use(30); // Teleport to Otherlands
				}
			}
			if(attackState == BotAttackState.AttackIfSurrounded) mustFaceTarget = true;
		}
	}

	@Override
	public boolean getEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterMissCharacter(int loginId) {
		// TODO Auto-generated method stub
		
	}

}
