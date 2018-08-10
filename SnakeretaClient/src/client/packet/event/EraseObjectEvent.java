package client.packet.event;

import client.Communication;
import client.Item;
import client.Packet;
import client.World;

public class EraseObjectEvent extends Packet {
	    @Override
		public void ready(World world, Communication comm) throws Exception {
	    	String message = this.getMessage().substring(3);
	    	String[] tokens;
	    	tokens = message.split(",");
	    	int x = Integer.parseInt(tokens[0]);
	    	int y = Integer.parseInt(tokens[1]);
	    	
	    	Item oldItem = world.getTile(x, y).getItem();
	    	world.getTile(x, y).setItem(null);
    		world.getItems().remove(x+","+y);
	    }
}
