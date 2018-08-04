package client.packet.event;

import client.Communication;
import client.Log;
import client.Packet;
import client.World;

public class PingEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
   		comm.pong();
   		// TODO: Close client after no response
   		// Log.println("PingEvent: "+"PONG");
    }

}
