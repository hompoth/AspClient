package client;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

//	public void drawChatBox(int percentage, String message) {
//		// draw chat box
//		final Stage dialog = new Stage();
//	    dialog.initModality(Modality.APPLICATION_MODAL);
//	    dialog.setResizable(false);
//	   // dialog.initOwner(gameView.stage);
//	    VBox dialogVbox = new VBox(20);
//	   // dialogVbox.getChildren().add(new Text("Error:\n"+ e.getMessage()));
//	    Scene dialogScene = new Scene(dialogVbox, 250, 50);
//	    dialogVbox.setPadding(new Insets(5));
//	    dialog.setScene(dialogScene);
//	    dialog.show();
//	}
//	
	
 
}
