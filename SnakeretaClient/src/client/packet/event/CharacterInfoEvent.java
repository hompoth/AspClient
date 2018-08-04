package client.packet.event;

import client.Communication;
import client.Direction;
import client.Equipment;
import client.Log;
import client.Packet;
import client.World;
import client.Character;
import client.CharacterType;

/**
 * MKCString, returns the MKC packet string for this character
     * <p>
     * MKCid,character type,name,title,surname,guild,x,y,facing,hp percent,body, body pose,hair
     * id,chest id,chest r,g,b,a,helm id,helm r,g,b,a, pants id,pants r,g,b,a,shoes id,shoes r,g,b,a,
     * shield id,shield r,g,b,a,weapon id,weapon r,g,b,a,hair_r,hair_g,hair_b,hair_a,invis,head
 * @author Josh
 *
 */


public class CharacterInfoEvent extends Packet {
    @Override
	public void ready(World world, Communication comm) throws Exception {
    	String message = this.getMessage().substring(3);
    	String[] tokens;
    	tokens = message.split(",");
    	int loginId;
    	String name;
		try {
			loginId = Integer.parseInt(tokens[0]);
			name = tokens[2];
			Character c = world.getCharacter(loginId);
			if(c == null) { // Create object if it doesn't exist, otherwise reuse it.
				c = new Character(loginId);
			}
			c.loginId = loginId;
			c.characterType = CharacterType.fromInteger(Integer.parseInt(tokens[1]));
			c.name = name;
			c.title = tokens[3];
			c.surname = tokens[4];
			c.guild = tokens[5];
			c.x = Integer.parseInt(tokens[6]);
			c.y = Integer.parseInt(tokens[7]);
			c.facing = Direction.fromInteger(Integer.parseInt(tokens[8]));
			c.hp = Integer.parseInt(tokens[9]);
			c.body = Integer.parseInt(tokens[10]);
			c.pose = Integer.parseInt(tokens[11]);
			Log.println(loginId+":"+c.characterType+":"+name);
			//c.level = 1;
			// RGBA might be a single * or 0,0,0,0
			//c.hair = new Equipment(Integer.parseInt(tokens[12]), Integer.parseInt(tokens[43]), Integer.parseInt(tokens[44]), Integer.parseInt(tokens[45]), Integer.parseInt(tokens[46]));
			//c.chest = new Equipment(Integer.parseInt(tokens[13]), Integer.parseInt(tokens[14]), Integer.parseInt(tokens[15]), Integer.parseInt(tokens[16]), Integer.parseInt(tokens[17]));
			//c.helm = new Equipment(Integer.parseInt(tokens[18]), Integer.parseInt(tokens[19]), Integer.parseInt(tokens[20]), Integer.parseInt(tokens[21]), Integer.parseInt(tokens[22]));
			//c.pants = new Equipment(Integer.parseInt(tokens[23]), Integer.parseInt(tokens[24]), Integer.parseInt(tokens[25]), Integer.parseInt(tokens[26]), Integer.parseInt(tokens[27]));
			//c.shoes = new Equipment(Integer.parseInt(tokens[28]), Integer.parseInt(tokens[29]), Integer.parseInt(tokens[30]), Integer.parseInt(tokens[31]), Integer.parseInt(tokens[32]));
			//c.shield = new Equipment(Integer.parseInt(tokens[33]), Integer.parseInt(tokens[24]), Integer.parseInt(tokens[25]), Integer.parseInt(tokens[36]), Integer.parseInt(tokens[37]));
			//c.weapon = new Equipment(Integer.parseInt(tokens[38]), Integer.parseInt(tokens[29]), Integer.parseInt(tokens[30]), Integer.parseInt(tokens[41]), Integer.parseInt(tokens[42]));
			//c.invisible = Boolean.parseBoolean(tokens[47]);
			//c.face = Integer.parseInt(tokens[48]);
			
	    	world.setCharacter(loginId,c);
	    	
	    	// Log.println("CharacterInfoEvent: "+getMessage());
		}
		catch (Exception e) {
			Log.println(e.toString());
			return;
		}
    }
}
/**
 * MKCString, returns the MKC packet string for this character
 * <p>
 * MKCid,character type,name,title,surname,guild,x,y,facing,hp percent,body, body pose,hair
 * id,chest id,chest r,g,b,a,helm id,helm r,g,b,a, pants id,pants r,g,b,a,shoes id,shoes r,g,b,a,
 * shield id,shield r,g,b,a,weapon id,weapon r,g,b,a,hair_r,hair_g,hair_b,hair_a,invis,head
 * <p>
 * character type = 1 for player, 2 for regular npc, some others for banker and vendors will find
 * later hair id = 20ish for the normal hairs head = 70-73 for faces body pose/state = 1 for
 * normal, 3 for staff, 4 for sword body = values 100-166 are illusions, 1 is male, 11 is female.
 * 2/12 are naga. 3 is skeleton invis = not sure at moment
 * <p>
 * For item r,g,b,a of 0,0,0,0 you can use * instead
 */
/**public String mKCString() throws Exception {
    int pose = this.getBodyState();
    ItemSlot weapon = this.getInventory().getEquippedSlot(EquipSlots.Weapon);
    if (weapon != null) {
        pose = weapon.getItem().getBodyState();
    }

    return "MKC"
            + this.getLoginID()
            // + ",5,"
            + "," + (this.getAccess() == AccessStatus.GameMaster ? "12" : "5") + ","
            + (this.getAccess() == AccessStatus.GameMaster ? "GM " : "") + this.getName() + ","
            + this.getTitle() + "," + this.getSurname() + "," + "" + "," + this.getMapX() + ","
            + this.getMapY() + "," + this.getFacing() + ","
            + (int) (((float) this.getCurrentHP() / this.getMaxStats().getHP()) * 100) + ","
            + this.getCurrentBodyID() + "," + (this.getCurrentBodyID() >= 100 ? 1 : pose) + ","
            + (this.getCurrentBodyID() >= 100 ? 0 : this.getHairID()) + ","
            + this.getInventory().equippedDisplay() + this.getHairR() + "," + this.getHairG() + ","
            + this.getHairB() + "," + this.getHairA() + "," + "0" + ","
            + (this.getCurrentBodyID() >= 100 ? 0 : this.getFaceID());*/
