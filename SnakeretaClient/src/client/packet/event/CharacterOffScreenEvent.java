package client.packet.event;

import client.Communication;
import client.Log;
import client.Packet;
import client.World;

public class CharacterOffScreenEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = this.getMessage().substring(3);
		int loginId;
		loginId = Integer.parseInt(message);
		world.getCharacters().remove(loginId);
		if(loginId==world.getSelfId()) {
			throw new Error();
		}
		// Log.println("Offscreen: " + message);
    }
}

