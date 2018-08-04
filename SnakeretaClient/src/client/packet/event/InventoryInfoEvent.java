package client.packet.event;

import client.Communication;
import client.Log;
import client.Packet;
import client.World;

public class InventoryInfoEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = getMessage();
    	Log.println("InventoryInfo: "+getMessage());
    	String[] data = message.substring(3).split(",");
    	// TODO: Save this in an Item class in the Inventory class of the Player class.  
    	if(data.length > 1) {
	    	int position = Integer.parseInt(data[0]);
	    	int item_id = Integer.parseInt(data[1]);
	    	String name = data[2];
	    	int stack = Integer.parseInt(data[3]);
	    	int graphicTile = Integer.parseInt(data[4]);
	    	int r = Integer.parseInt(data[5]);
	    	int g = Integer.parseInt(data[6]);
	    	int b = Integer.parseInt(data[7]);
	    	int a = Integer.parseInt(data[8]);
    	}
    	else {
    		//handle empty slot
    	}  	
    }
}

