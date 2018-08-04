package client.packet.event;

import client.Communication;
import client.Direction;
import client.Log;
import client.Packet;
import client.World;

public class FacingEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = getMessage().substring(3);
    	String[] tokens;
    	tokens = message.split(",");
    	int loginId = Integer.parseInt(tokens[0]);
    	Direction facing = Direction.fromInteger(Integer.parseInt(tokens[1]));
    	world.getCharacter(loginId).facing = facing;
    	
    	// Log.println("FacingEvent: "+getMessage());
    }
}

