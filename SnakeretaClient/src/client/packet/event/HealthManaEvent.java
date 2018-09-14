package client.packet.event;

import client.Communication;
import client.Packet;
import client.World;
import client.Character;

public class HealthManaEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = this.getMessage().substring(2);
    	String[] tokens;
    	tokens = message.split(",");
    	int loginId = Integer.parseInt(tokens[0]);
    	int hp = Integer.parseInt(tokens[1]);
    	int mp = Integer.parseInt(tokens[2]);
    	Character c = world.getCharacter(loginId);
    	if(c != null) {
	    	c.hp = hp;
	    	c.mp = mp;
    	}
    }
}
