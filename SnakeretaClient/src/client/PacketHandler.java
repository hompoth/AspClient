package client;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import client.packet.event.AttackEvent;
import client.packet.event.BuffInfoEvent;
import client.packet.event.CharacterInfoEvent;
import client.packet.event.CharacterOffScreenEvent;
import client.packet.event.DisplayEvent;
import client.packet.event.EquipmentInfoEvent;
import client.packet.event.FacingEvent;
import client.packet.event.HealthManaEvent;
import client.packet.event.InventoryInfoEvent;
import client.packet.event.ItemDropEvent;
import client.packet.event.LoginFailEvent;
import client.packet.event.LoginSuccessEvent;
import client.packet.event.MapChangeEvent;
import client.packet.event.MapNameEvent;
import client.packet.event.MessageBoxEvent;
import client.packet.event.MovementEvent;
import client.packet.event.PingEvent;
import client.packet.event.SelfEvent;
import client.packet.event.SelfPositionEvent;
import client.packet.event.SpellEvent;
import client.packet.event.SpellInfoEvent;
import client.packet.event.StatInfoEvent;
import client.packet.event.TempEvent;

//	this.stringToEvent.put("PlayerPosition", PlayerPositionEvent.create(player, data));
//	this.stringToEvent.put("LOGIN", LoginEvent.create(player, data));
//	this.stringToEvent.put("LCNT", LoginContinuedEvent.create(player, data));
//	this.stringToEvent.put("DLM", DoneLoadingMapEvent.create(player, data));
//	this.stringToEvent.put(";", ChatEvent.create(player, data));
//	this.stringToEvent.put("M1", MoveEvent.create(player, data));
//	this.stringToEvent.put("M2", MoveEvent.create(player, data));
//	this.stringToEvent.put("M3", MoveEvent.create(player, data));
//	this.stringToEvent.put("M4", MoveEvent.create(player, data));
//	this.stringToEvent.put("F1", FacingEvent.create(player, data));
//	this.stringToEvent.put("F2", FacingEvent.create(player, data));
//	this.stringToEvent.put("F3", FacingEvent.create(player, data));
//	this.stringToEvent.put("F4", FacingEvent.create(player, data));
//	this.stringToEvent.put("/tell ", TellEvent.create(player, data));
//	this.stringToEvent.put("/who", WhoEvent.create(player, data));
//	this.stringToEvent.put("/changeclass ", ChangeClassEvent.create(player, data));
//	this.stringToEvent.put("/summon ", SummonEvent.create(player, data));
//	this.stringToEvent.put("/warp ", WarpEvent.create(player, data));
//	this.stringToEvent.put("/approach ", ApproachEvent.create(player, data));
//	//TODO - Need to change the way swapping is updating the database so that the saveItems does not have to loop like crazy through every item.
//	this.stringToEvent.put("CHANGE", InventoryChangeSlotEvent.create(player, data));
//	//TODO - Prob same ^^
//	this.stringToEvent.put("SPLIT", InventorySplitEvent.create(player, data));
//	//TODO - Maybe? ^^ See if its worth it
//	this.stringToEvent.put("USE", InventoryUseEvent.create(player, data));
//	//TODO - Could also save on pickup, dunno test it out.
//	this.stringToEvent.put("GET", PickupItemEvent.create(player, data));
//	//TODO - ^^
//	this.stringToEvent.put("DRP", PlayerDropItemEvent.create(player, data));
//	this.stringToEvent.put("/dropgold ", PlayerDropGoldEvent.create(player, data));
//	this.stringToEvent.put("ATT", PlayerAttackEvent.create(player, data));
//	this.stringToEvent.put("PONG", PlayerPongEvent.create(player, data));
//	this.stringToEvent.put("/shutdown", ShutdownCommandEvent.create(player, data));
//	this.stringToEvent.put("/location", LocationEvent.create(player, data));
//	this.stringToEvent.put("/refresh", RefreshPositionEvent.create(player, data));
//	this.stringToEvent.put("CAST", PlayerCastSpellEvent.create(player, data));
//	this.stringToEvent.put("/getitem ", GMGetItemCommandEvent.create(player, data));
//	this.stringToEvent.put("/hax ", HaxCommandEvent.create(player, data));
//	this.stringToEvent.put("/togglegroup", ToggleGroupCommandEvent.create(player, data));
//	this.stringToEvent.put("/group ", GroupChatEvent.create(player, data));
//	this.stringToEvent.put("/groupadd ", GroupAddEvent.create(player, data));
//	this.stringToEvent.put("/groupremove", GroupRemoveEvent.create(player, data));
//	this.stringToEvent.put("RC", PlayerRightClickEvent.create(player, data));
//	this.stringToEvent.put("WBC", WindowButtonClickEvent.create(player, data));
//	this.stringToEvent.put("VPI", VendorPurchaseInventoryEvent.create(player, data));
//	this.stringToEvent.put("VSI", VendorSellInventoryEvent.create(player, data));
//	this.stringToEvent.put("/ban ", GMBanCommandEvent.create(player, data));
//	this.stringToEvent.put("/kick ", GMKickCommandEvent.create(player, data));
//	this.stringToEvent.put("GID", ItemInfoEvent.create(player, data));
//	this.stringToEvent.put("/shout ", ShoutCommandEvent.create(player, data));
//	this.stringToEvent.put("/auction ", AuctionCommandEvent.create(player, data));
//	this.stringToEvent.put("/random", RandomCommandEvent.create(player, data));
//	this.stringToEvent.put("/broadcast ", GMBroadcastCommandEvent.create(player, data));
//	this.stringToEvent.put("EMOT", EmoteEvent.create(player, data));
//	this.stringToEvent.put("/buyvita", BuyVitaCommandEvent.create(player, data));
//	this.stringToEvent.put("/buymana", BuyManaCommandEvent.create(player, data));
//	this.stringToEvent.put("DITM", DestroyItemEvent.create(player, data));
//	this.stringToEvent.put("DSPL", DestroySpellEvent.create(player, data));
//	this.stringToEvent.put("SWAP", SpellbookSwapEvent.create(player, data));
//	this.stringToEvent.put("OCB", OpenCombineBagEvent.create(player, data));
//	this.stringToEvent.put("ITW", InventoryToWindowEvent.create(player, data));
//	this.stringToEvent.put("WTI", WindowToInventoryEvent.create(player, data));
//	this.stringToEvent.put("/charinfo", CharacterInfoCommandEvent.create(player, data));
//	this.stringToEvent.put("/guildcreate ", GuildCreateCommandEvent.create(player, data));
//	this.stringToEvent.put("/guildadd ", GuildAddCommandEvent.create(player, data));
//	this.stringToEvent.put("/guildremove", GuildRemoveCommandEvent.create(player, data));
//	this.stringToEvent.put("/guildmotd", GuildMotdCommandEvent.create(player, data));
//	this.stringToEvent.put("/guildowner ", GuildOwnerCommandEvent.create(player, data));
//	this.stringToEvent.put("/guildofficer ", GuildOfficerCommandEvent.create(player, data));
//	this.stringToEvent.put("/guild ", GuildChatCommandEvent.create(player, data));
//	this.stringToEvent.put("/rank", RankCommandEvent.create(player, data));
//	this.stringToEvent.put("/setconfig ", SetConfigCommandEvent.create(player, data));
//	this.stringToEvent.put("/saveconfig", SaveConfigCommandEvent.create(player, data));
//	this.stringToEvent.put("/respawnmap", RespawnMapCommandEvent.create(player, data));
//	this.stringToEvent.put("/changepassword ", ChangePasswordCommandEvent.create(player, data));
//	this.stringToEvent.put("KBUF", KillBuffEvent.create(player, data));
//	this.stringToEvent.put("/toggle ", ToggleCommandEvent.create(player, data));
//	this.stringToEvent.put("/aether ", AetherCommandEvent.create(player, data));
//	this.stringToEvent.put("/instalevel", InstaLevelCommandEvent.create(player, data));
//	this.stringToEvent.put("/petlist", PetListCommandEvent.create(player, data));
//	this.stringToEvent.put("/petspawn ", PetSpawnCommandEvent.create(player, data));
//	this.stringToEvent.put("/petinfo ", PetInfoCommandEvent.create(player, data));
//	this.stringToEvent.put("/petdamage ", PetDamageCommandEvent.create(player, data));
//	this.stringToEvent.put("/petvita ", PetVitaCommandEvent.create(player, data));
//	this.stringToEvent.put("/petdelete ", PetDeleteCommandEvent.create(player, data));
//	this.stringToEvent.put("/unban ", UnbanCommandEvent.create(player, data));
//	this.stringToEvent.put("/checkname ", CheckNameCommandEvent.create(player, data));
//	this.stringToEvent.put("/classchange ", GMClassChangeCommandEvent.create(player, data));
//	this.stringToEvent.put("/changename ", ChangeNameCommandEvent.create(player, data));
//	this.stringToEvent.put("/giveexperience ", GMGiveExperienceCommandEvent.create(player, data));
//	this.stringToEvent.put("/shutdownserver ", GMShutdownServer.create(player, data));
//	this.stringToEvent.put("/spawn ", GMSpawnNPC.create(player, data));

public class PacketHandler {
	private Communication comm;
    private LinkedHashMap<Long, Packet> events;
    private HashMap<String, Supplier<Packet>> stringToEvent;
    // TODO: Make sure that this doesn't error on Long.MAX_VALUE. (Likely not going to be reached)
    private volatile long lastId;

    public  PacketHandler(Communication comm) throws Exception {
//    	Log.println(comm);
    	lastId = 0;
    	this.comm = comm;
        this.events = new LinkedHashMap<Long, Packet>();
        this.stringToEvent = new HashMap<String, Supplier<Packet>>();
        //EMOT = EMOTE
		this.stringToEvent.put("LOK", LoginSuccessEvent::new);
		this.stringToEvent.put("LNO", LoginFailEvent::new);
		this.stringToEvent.put("PING", PingEvent::new);
		this.stringToEvent.put("SCM", MapChangeEvent::new);
		this.stringToEvent.put("$", MessageBoxEvent::new);
		this.stringToEvent.put("^", MessageBoxEvent::new);
		this.stringToEvent.put("#", MessageBoxEvent::new);
		this.stringToEvent.put("SIS", InventoryInfoEvent::new);
		this.stringToEvent.put("WNF", EquipmentInfoEvent::new);
		this.stringToEvent.put("SSS", SpellInfoEvent::new);
		this.stringToEvent.put("BUF", BuffInfoEvent::new);
		this.stringToEvent.put("MKC", CharacterInfoEvent::new);
		this.stringToEvent.put("ERC", CharacterOffScreenEvent::new);
		this.stringToEvent.put("MOC", MovementEvent::new);
		this.stringToEvent.put("CHH", FacingEvent::new);
		this.stringToEvent.put("SPA", SpellEvent::new);
		this.stringToEvent.put("MOB", ItemDropEvent::new);
		this.stringToEvent.put("ATT", AttackEvent::new);
		this.stringToEvent.put("SMN", MapNameEvent::new);
		this.stringToEvent.put("SUC", SelfEvent::new);
		this.stringToEvent.put("SUP", SelfPositionEvent::new);
		this.stringToEvent.put("SNF", StatInfoEvent::new);
		this.stringToEvent.put("BT", DisplayEvent::new);
		this.stringToEvent.put("VC", HealthManaEvent::new); // hp/mp
		// TODO: Create event classes for any temporary events
		this.stringToEvent.put("TNL", TempEvent::new);
		this.stringToEvent.put("WPS", TempEvent::new);
		this.stringToEvent.put("SPP", TempEvent::new);
		this.stringToEvent.put("DSM", TempEvent::new);
		this.stringToEvent.put("CHP", TempEvent::new);
    }
    
    public synchronized boolean addEvent(String message) {
        for (String key : stringToEvent.keySet()) {
            if (message.startsWith(key)) {
            	Log.println(" --> " + message);
//	            Chatbox.showLabel(" --> " + message);	// display in new chatbox
                Packet e = stringToEvent.get(key).get();
                e.setId(++lastId);
                e.setMessage(message);
                events.put(e.getId(), e);
                return true;
            }
        }
        // TODO: Will only hit if we don't have an event to handle it. Add any that are found
        Log.println("No Event: "+message);
        return false;
    }
    
    public synchronized boolean removeEvent(long Id) {
    	return events.remove(Id) != null;
    }
    
    public synchronized void update(World world) throws Exception {
    	LinkedHashMap<Long, Packet> readyEvents;
    	readyEvents = new LinkedHashMap<>();
    	readyEvents.putAll(events); // TODO: Perhaps add a lock to prevent adding to this.events when this is happening.

        for (Map.Entry<Long, Packet> entry : readyEvents.entrySet()) {
        	Packet e = entry.getValue();
            e.ready(world, comm);
            if(!removeEvent(entry.getKey())) throw new Exception("Event wasn't removed.");
        }
    }
}
