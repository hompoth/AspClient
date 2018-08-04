package client;

// TODO: Clean this up
public class Character {
	public String name;
	public int loginId;
	public CharacterType characterType;
	public String title;
	public String surname;
	public String guild;
	public int x;
	public int y;
	public Direction facing;
	public int hp;
	public int mp;
	public int body;
	public int pose;
	public Equipment hair;
	public Equipment chest;
	public Equipment helm;
	public Equipment pants;
	public Equipment shoes;
	public Equipment shield;
	public Equipment weapon;
	public boolean invisible;
	public int face;
	public String className;
	private long lastMoveInstance;
	public Character(int loginId) {
		this.loginId = loginId;
	}
	public boolean canMove() {		
		long currentTime = System.nanoTime();
		if(currentTime - lastMoveInstance > 1_000_000_000 * 0.4) {
			lastMoveInstance = currentTime;
			return true;
		}
		return false;
	}
	public void move(int x, int y) {
		facing = Direction.fromXY(x-this.x,y-this.y);
		this.x = x;
		this.y = y;
	}
	public void move(Direction direction) {
		facing = direction;
		switch(direction) {
			case UP:
				move(x,y-1);
				break;
			case DOWN:
				move(x,y+1);
				break;
			case LEFT:
				move(x-1,y);
				break;
			case RIGHT:
				move(x+1,y);
				break;
		}
	}
	// TODO: Check if in the radius of a cone facing the targetX/Y
	public boolean isFacing(Character c, int targetX, int targetY) {
		if(facing == Direction.DOWN && (c.y - y) >= 0 ||
			facing == Direction.UP && (c.y - y) <= 0 ||
			facing == Direction.RIGHT && (c.x - x) >= 0 ||
			facing == Direction.LEFT && (c.x - x) <= 0 ) return true;
		return false;
	}
	public boolean isSurrounded(World world) {
		if(world.blockedTile(x-1,y) &&
			world.blockedTile(x+1,y) &&
			world.blockedTile(x,y-1) &&
			world.blockedTile(x,y+1)) {
			return true;
		}
		return false;
	}
}
