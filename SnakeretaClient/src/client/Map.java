package client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Map {
	public Tile tiles[][];
	
	public Map(File file) throws IOException {
		tiles = new Tile[100][100];
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

		byte a = in.readByte();
		byte b = in.readByte();
		byte c = in.readByte();
		byte d = in.readByte();
		Log.println(file.getName()+":"+a+" "+b+" "+c+" "+d);
		for(int i = 0; i < 100; i++) {
			for(int j = 0; j < 100; j++) {
				tiles[i][j] = new Tile();
				for(int k = 0; k < 17; k++) {
					if(k==0) {
						tiles[i][j].block = in.readBoolean();
					}
					else {
						tiles[i][j].data[k] = in.readByte();
					}
				}
			}
		}
		in.close();
	}
	public void showBytes(int x, int y) {
		String bytes = "";
		for(int k = 0; k < 17; k++) {
			bytes = bytes + " ^ " + tiles[y-1][x-1].data[k];
		}
		Log.println("map["+y+"-1,"+x+"-1]: " + bytes + " ; " + tiles[y-1][x-1].block);
	}
}
