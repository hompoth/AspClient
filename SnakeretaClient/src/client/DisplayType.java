package client;

public enum DisplayType {
	Attack, Heal, Dodge, Miss, Stunned, LevelUp; // LevelUp/Aether/Fizzle = 60
	
    public static DisplayType fromInteger(int x) {
        switch(x) {
        case 1:
            return Attack;
        case 7:
            return Heal;
        case 20:
            return Dodge;
        case 21:
            return Miss;
        case 50:
            return Stunned;
        case 60:
            return LevelUp;
        }
        return null;
    }

	public static int toInteger(DisplayType type) {
        switch(type) {
        case Attack:
        	return 1;
        case Heal:
        	return 7;
        case Dodge:
        	return 20;
        case Miss:
        	return 21;
        case Stunned:
        	return 50;
        case LevelUp:
        	return 60;
        }
        return 0;
	}
}
