package client.packet.event;
import client.Communication;
import client.DisplayType;
import client.Packet;
import client.World;
import client.Character;

public class NewWindowEvent  extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = this.getMessage().substring(3);

    	int windowId = Integer.parseInt(message);
    	for(int i = 6; i <= 30; ++i) {
    		comm.sellSlot(1998,i);
    	}
    	comm.closeWindow(windowId,1998);
    }
}

