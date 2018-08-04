package client.packet.event;

import client.Communication;
import client.Log;
import client.Packet;
import client.World;

public class MapNameEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = this.getMessage();
		String mapName;
		mapName = message.substring(3);
		
		world.setMapName(mapName);
	}

}
