package client.packet.event;

import client.Communication;
import client.Log;
import client.Packet;
import client.World;
import client.World.GameState;

public class MapChangeEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = this.getMessage().substring(3);
    	String[] tokens;
		int mapId;
		String mapName;
		tokens = message.split(",");
		try {
			mapId = Integer.parseInt(tokens[0]);
			mapName = tokens[2];
		}
		catch (Exception e) {
			throw new Exception("Invalid map data.");
		}
		world.loadMap(mapId, mapName);
   		comm.doneLoadingMap(mapId);
   		world.getBot().afterMapChange();
    }
}

