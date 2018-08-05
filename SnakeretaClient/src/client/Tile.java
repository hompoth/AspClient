package client;

public class Tile {
	public byte[] data;
	public boolean block;
	
	private Item __Item;
	public Item getItem() {
		return __Item;
	}
	public void setItem(Item item) {
		__Item = item;
	}
	
	public Tile(){
		this.data = new byte[17];
	}
}
