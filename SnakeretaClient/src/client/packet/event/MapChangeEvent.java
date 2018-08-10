package client.packet.event;

import client.Communication;
import client.Packet;
import client.World;
import client.bot.Point;

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
		for(Point p : world.getItems().values()) {
			world.getTile(p.x,p.y).setItem(null);
		}
		world.getItems().clear();
		world.loadMap(mapId, mapName);
   		comm.doneLoadingMap(mapId);
   		world.getBot().afterMapChange();
    }
}

