package client;

public enum CharacterType {
	Mob, Player, Admin;
	
    public static CharacterType fromInteger(int x) {
        switch(x) {
        case 2:
            return Mob;
        case 5:
            return Player;
        case 12:
            return Admin;
        }
        return CharacterType.Player;
    }

	public static int toInteger(CharacterType type) {
		switch(type) {
        case Mob:
        	return 2;
        case Player:
        	return 5;
        case Admin:
        	return 12;
		}
		return 5;
	}
}
