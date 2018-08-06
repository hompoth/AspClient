package client.packet.event;

import client.Communication;
import client.Character;
import client.Log;
import client.Packet;
import client.World;

public class GroupUpdateEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = getMessage().substring(3);
    	String tokens[] = message.split(",");
    	int groupId = Integer.parseInt(tokens[0])-1;
    	int loginId = Integer.parseInt(tokens[1]);
    	//String name = tokens[2];
    	//int level = Integer.parseInt(tokens[3]);
    	//String className = tokens[4];
    	
    	if(groupId >= 10) return;
    	world.getGroup()[groupId]=loginId;
    	
    	Log.println("Group Update: "+getMessage());
    }
}
