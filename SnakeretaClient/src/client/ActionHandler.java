package client;


import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ActionHandler {

	// TODO: Handle movements / start bot script
	// TODO: Handle all other events
	public ActionHandler(World world, Stage stage) {
		stage.addEventHandler(MouseEvent.DRAG_DETECTED, event -> {});
		stage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {});
		stage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {});
		stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {});
		stage.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {});
		stage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {});

		stage.addEventHandler(KeyEvent.KEY_PRESSED, new KeyboardActionHandler(world));
		stage.addEventHandler(KeyEvent.KEY_TYPED, new KeyboardActionHandler(world));
	}

}
