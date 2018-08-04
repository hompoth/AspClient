package client.packet.event;

import client.Communication;
import client.Message;
import client.Packet;
import client.World;

public class MessageBoxEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = getMessage();
    	if(message.length() > 2) {
        	int chatType = Integer.parseInt(message.substring(1,2));
        	String text = message.substring(2);
        	world.getMessageBox().add(new Message(text, chatType));
    	}
    }
}

