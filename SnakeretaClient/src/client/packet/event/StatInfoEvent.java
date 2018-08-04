package client.packet.event;

import client.Character;
import client.Communication;
import client.Log;
import client.Packet;
import client.World;

public class StatInfoEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = this.getMessage().substring(3);
    	String[] tokens;
    	tokens = message.split(",");
		try {
			world.setGuild(tokens[0]);
			world.setClassName(tokens[2]);
			world.setLevel(Integer.parseInt(tokens[3]));
		}
		catch (Exception e) {
			Log.println("StatInfoEvent: " + e.toString());
			return;
		}
	}
}
