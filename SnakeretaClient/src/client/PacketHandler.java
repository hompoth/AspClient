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

public class PacketHandler {
	private Communication comm;
    private LinkedHashMap<Long, Packet> events;
    private HashMap<String, Supplier<Packet>> stringToEvent;
    // TODO: Make sure that this doesn't error on Long.MAX_VALUE. (Likely not going to be reached)
    private volatile long lastId;

    public  PacketHandler(Communication comm) throws Exception {
    	lastId = 0;
    	this.comm = comm;
        this.events = new LinkedHashMap<Long, Packet>();
        this.stringToEvent = new HashMap<String, Supplier<Packet>>();
		this.stringToEvent.put("LOK", LoginSuccessEvent::new);
		this.stringToEvent.put("LNO", LoginFailEvent::new);
		this.stringToEvent.put("PING", PingEvent::new);
		this.stringToEvent.put("SCM", MapChangeEvent::new);
		this.stringToEvent.put("$", MessageBoxEvent::new);
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
