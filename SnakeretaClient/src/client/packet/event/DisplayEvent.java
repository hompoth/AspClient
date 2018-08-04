package client.packet.event;

import client.Communication;
import client.DisplayType;
import client.Packet;
import client.World;
import client.Character;

public class DisplayEvent  extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = this.getMessage().substring(2);
    	String[] tokens;
    	tokens = message.split(",");
    	int loginId = Integer.parseInt(tokens[0]);
    	DisplayType displayType = DisplayType.fromInteger(Integer.parseInt(tokens[1]));
    	//String display = tokens[2];
    	//String name = tokens[3]; // Array out of bounds
    	//Character c = world.getCharacter(loginId);
    	//c.name = name;
    	if(displayType == DisplayType.Miss) {
    		world.getBot().afterMissCharacter(loginId);
    	}
    	//else if(displayType == DisplayType.Attack) {
    		
    	//}
    }
}

