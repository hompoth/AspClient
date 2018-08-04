package client.packet.event;

import client.Communication;
import client.Packet;
import client.World;
import client.World.ConnectionState;

public class LoginFailEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
		String message = this.getMessage().substring(3);
		world.setConnectionState(ConnectionState.Disconnected);
		throw new Exception(message);
    }
}

