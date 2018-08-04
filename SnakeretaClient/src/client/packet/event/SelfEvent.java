package client.packet.event;

import client.Communication;
import client.Character;
import client.Log;
import client.Packet;
import client.World;

public class SelfEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = getMessage().substring(3);
    	int loginId = Integer.parseInt(message);
    	world.setSelfId(loginId);
    	
    	Log.println("Self: "+getMessage());
    }
}
