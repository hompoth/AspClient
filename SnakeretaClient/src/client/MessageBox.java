package client;

import java.util.LinkedList;
import java.util.List;

public class MessageBox {
	private final int capacity;
	private final List<Message> items;
	
	public MessageBox() {
		capacity = 200;
		items = new LinkedList<Message>();
	}
	
	public void add(Message message) {
		if(items.size() > capacity-1) {
			items.remove(0);
		}
		items.add(message);		
	}

}
