package client.packet.event;

import client.Communication;
import client.Item;
import client.Log;
import client.Packet;
import client.World;
import client.bot.Point;

public class ItemDropEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = getMessage().substring(3);
    	String tokens[] = message.split(",");
    	int itemId = Integer.parseInt(tokens[0]);
    	int x = Integer.parseInt(tokens[1]);
    	int y = Integer.parseInt(tokens[2]);
    	String name = tokens[3];
    	int stack = Integer.parseInt(tokens[4]);
    	String rgba = tokens[5];
    	
    	Item item = new Item(itemId);
    	item.name = name;
    	item.stack = stack;
    	// add color
    	
    	world.getTile(x,y).setItem(item);
    	world.getItems().put(itemId, new Point(x,y));
    }
}

