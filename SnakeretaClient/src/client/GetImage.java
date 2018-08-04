package client;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

import javafx.scene.image.Image;
public class GetImage {
	public static Image getImage(String location, int width, int height) {
	File file = new File("C:\\Program Files (x86)\\Aspereta\\data\\"+location);
    byte[] fileData = new byte[(int) file.length()];
    DataInputStream dis;
	try {
		dis = new DataInputStream(new FileInputStream(file));
		Image finalImage;
		finalImage = new Image(dis,width,height,false,true);
		return finalImage;
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return null;
	}
}