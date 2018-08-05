package client;

public class Item {

	private int __ItemId;
	public String name;
	public int stack;
	public int getItemId() {
		return __ItemId;
	}
	public void setItemId(int item) {
		__ItemId = item;
	}
	
	public Item(int itemId) {
		setItemId(itemId);
	}

}
