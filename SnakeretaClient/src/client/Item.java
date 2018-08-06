package client;

public class Item {

	public String name;
	public int stack;
	private int __TileGraphicId;
	public int getTileGraphicId() {
		return __TileGraphicId;
	}
	public void setTileGraphicId(int tileGraphicId) {
		__TileGraphicId = tileGraphicId;
	}
	
	public Item(int tileGraphicId) {
		setTileGraphicId(tileGraphicId);
	}

}
