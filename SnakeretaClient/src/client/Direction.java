package client;

import javafx.scene.input.KeyCode;

public enum Direction {
	UP,DOWN,LEFT,RIGHT;
	
    public static Direction fromInteger(int x) {
        switch(x) {
        case 1:
            return UP;
        case 2:
            return RIGHT;
        case 3:
            return DOWN;
        case 4:
            return LEFT;
        }
        return null;
    }

	public static int toInteger(Direction direction, boolean facing) {
		switch(direction) {
        case UP:
        	return 1;
        case RIGHT:
        	return (facing)?4:2;
        case DOWN:
        	return (facing)?2:3;
        case LEFT:
        	return (facing)?3:4;
		}
		return 0;
	}

	public static Direction fromKeyCode(KeyCode code) {
		switch(code) {
        case UP:
        	return UP;
        case RIGHT:
        	return RIGHT;
        case DOWN:
        	return DOWN;
        case LEFT:
        	return LEFT;
        default:
        	return null;
		}
	}

	public static Direction fromXY(int xDiff, int yDiff) {
		if(xDiff > 0) return RIGHT;
		if(xDiff < 0) return LEFT;
		if(yDiff > 0) return DOWN;
		if(yDiff < 0) return UP;
		return null;
	}

	public static Direction fromCharacter(Character a, Character b) {
		return fromXY(b.x-a.x,b.y-a.y);
	}
}
