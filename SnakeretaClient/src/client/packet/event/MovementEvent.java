package client.packet.event;

import client.Communication;
import client.Log;
import client.Packet;
import client.World;
import client.Character;

public class MovementEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = this.getMessage().substring(3);
    	String[] tokens;
    	tokens = message.split(",");
    	int loginId = Integer.parseInt(tokens[0]);
    	int x = Integer.parseInt(tokens[1]);
    	int y = Integer.parseInt(tokens[2]);
    	world.getCharacter(loginId).move(x,y);

    	// Log.println("MovementEvent: "+getMessage());
    }
}

