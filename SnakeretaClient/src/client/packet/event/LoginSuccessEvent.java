package client.packet.event;

import client.Communication;
import client.Log;
import client.Packet;
import client.World;
import client.World.ConnectionState;

public class LoginSuccessEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
		world.loadGame();
		world.setConnectionState(ConnectionState.Connected);
		comm.loginContinued();		

		// Log.println("LoginSuccessEvent: "+"LCNT");
    }
}

