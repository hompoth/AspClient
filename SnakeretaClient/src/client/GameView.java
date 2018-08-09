package client;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// This will handle all visuals for the game.
public class GameView {
	Stage stage;
	Scene scene;
	GraphicsContext context;
	double width;
	double height;
	double ratio;
	GameView(Stage stage, Scene scene, GraphicsContext context, double width, double height){
		this.stage = stage;
		this.scene = scene;
		this.context = context;
		this.width = width;
		this.height = height;
		this.ratio = width/480;
	}
	public void setGameScene(){
		Platform.runLater(() -> {
			stage.setScene(scene);
		});
	}
	public void setFill(Color color) {
		context.setFill(color);
	}
	public void draw(double x, double y, double w, double h) {
		context.fillRect(x, y, w, h);
	}
	// TODO: Add outline (spruce it up). Add entering map: MapName
	public void drawLoadingScreen(int percentage, String message) {
		// draw loading image
		context.drawImage(new Image("skins/Background.bmp"), 0, 0, width, height);
		context.setFill(Color.AZURE);
		context.fillRect(50 * ratio, 300 * ratio, 250 * ratio, 15 * ratio);
		context.setFill(Color.BLUE);
		context.fillRect(50 * ratio, 300 * ratio, 250 * ((double)percentage / 100) * ratio, 15 * ratio);
		context.setFont(new Font("Courier", 10 * ratio));
		context.setFill(Color.BLACK);
		context.fillText("Loading " + percentage + "%: " + message, 50 * ratio, 325 * ratio);
	}
}