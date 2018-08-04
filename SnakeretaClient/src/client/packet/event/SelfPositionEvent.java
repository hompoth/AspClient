package client.packet.event;

import client.Communication;
import client.Log;
import client.Packet;
import client.World;
import client.Character;

public class SelfPositionEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = getMessage().substring(3);
    	String[] tokens;
    	tokens = message.split(",");
    	int x = Integer.parseInt(tokens[0]);
    	int y = Integer.parseInt(tokens[1]);
    	Character c = world.getSelf();
    	c.x = x;
    	c.y = y;
    	
    	// Log.println("Self Position: "+getMessage());
    }
}
