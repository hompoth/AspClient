package client.bot;

import java.util.LinkedList;
import java.util.Stack;

import client.Log;
import client.World;

public class PathFinding {
	private static Point[][] __Map;
	private static void setMap(Point[][] map) {
		__Map = map;
	}
	private static Point getPoint(int x, int y) {
		if(x >=1 && x <= 100 && y >=1 && y <= 100) {
			return __Map[y-1][x-1];
		}
		return null;
	}
	private static void setPoint(int x, int y) {
		if(x >=1 && x <= 100 && y >=1 && y <= 100) {
			if(__Map[y-1][x-1] == null) {
				__Map[y-1][x-1] = new Point(x,y);
			}
		}
	}

	public static Stack<Point> getPath(World world, int x, int y, int targetX, int targetY) {
		LinkedList<Point> openList = new LinkedList<Point>();
		LinkedList<Point> closedList = new LinkedList<Point>();
		setMap(new Point[100][100]);
        setPoint(x,y);
        if(getPoint(x,y)==null) {
        	Log.println("getPoint("+x+","+y+") = null");
        }
    	openList.add(getPoint(x,y)); // add starting node to open list

        Point current;
        while (true) {
            current = closestDistance(openList, targetX, targetY); // get node with lowest fCosts from openList
            closedList.add(current); // add current node to closed list
            openList.remove(current); // delete current node from open list

            if ((current.x == targetX)
                    && (current.y == targetY)) { // found goal
                return createPath(current);
            }

            // for all adjacent nodes:
            LinkedList<Point> adjPoints = getAdjacent(world, closedList, current);
            for (int i = 0; i < adjPoints.size(); i++) {
                Point currentAdj = adjPoints.get(i);
                if(currentAdj == null) {
                	Log.println("LOL WHAT");
                }
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

	private static LinkedList<Point> getAdjacent(World world, LinkedList<Point> closedList, Point p) {
		LinkedList<Point> list = new LinkedList<Point>();
		for(int x = -1; x <= 1; x+=2) {
			setPoint(p.x+x,p.y);
			if(getPoint(p.x+x,p.y) != null && !closedList.contains(getPoint(p.x+x,p.y)) && !world.blockedTile(p.x+x,p.y)) {
				list.add(getPoint(p.x+x,p.y));
			}
		}
		for(int y = -1; y <= 1; y+=2) {
			setPoint(p.x,p.y+y);
			if(getPoint(p.x,p.y+y) != null && !closedList.contains(getPoint(p.x,p.y+y)) && !world.blockedTile(p.x,p.y+y)) {
				list.add(getPoint(p.x,p.y+y));
			}
		}
		return list;
	}

	private static Stack<Point> createPath(Point current) {
		Stack<Point> path = new Stack<Point>();
		path.add(current);
		current = current.previous;
		while(current!=null && current.previous!=null) {
			path.add(current);
			current = current.previous;
		}
		return path;
	}

	private static Point closestDistance(LinkedList<Point> openList, int targetX, int targetY) {
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

	private static int distance(int x, int y, int x2, int y2) {
		return Math.abs(x-x2) + Math.abs(y-y2);
	}
}
