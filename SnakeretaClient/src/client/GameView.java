package client;

import client.bot.Point;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// This will handle all visuals for the game.
public class GameView {

		private static int X_TILES = 25;
		private static int Y_TILES = 17;
		
		Stage stage;
		Scene scene;
		GraphicsContext context;
		double width, height, ratio;
		int centerX, centerY, offsetX, offsetY;
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
		public void setCenter(int x, int y) {
			centerX = x;
			centerY = y;
			offsetX = - X_TILES/2 + centerX;
			offsetY = - Y_TILES/2 + centerY;
		}
		// TODO: Add outline (spruce it up). Add entering map: MapName
		public void drawLoadingScreen(int percentage, String message) {
			if(!Platform.isFxApplicationThread()) {
				Platform.runLater(() -> drawLoadingScreen(percentage, message));
				return;
			}
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
		public void clear() {
			if(!Platform.isFxApplicationThread()) {
				Platform.runLater(() -> clear());
				return;
			}
			context.setFill(Color.BLACK);
			context.fillRect(0, 0, width, height);
		}
		public void fillOval(double x, double y, double width, double height, Color color) {
			if(!Platform.isFxApplicationThread()) {
				final double fX=x, fY=y;
				Platform.runLater(() -> fillOval(fX, fY, width, height, color));
				return;
			}
			double tileWidth = this.width/X_TILES, tileHeight = this.height/Y_TILES;
			if(x >= 1 && x <= 100 && y >= 1 && y <= 100) {
				x = x - offsetX;
				y = y - offsetY;
				if(x >= 0 && x < X_TILES && y >= 0 && y < Y_TILES) {
					context.setFill(color);
					context.fillOval((x + 0.5-(width/2))*tileWidth, (y + 0.5-(height/2))*tileHeight, tileWidth*width, tileHeight*height);
				}
			}
		}
		public void fillRect(double x, double y, double width, double height, Color color) {	
			if(!Platform.isFxApplicationThread()) {
				final double fX=x, fY=y;
				Platform.runLater(() -> fillRect(fX, fY, width, height, color));
				return;
			}
			double tileWidth = this.width/X_TILES, tileHeight = this.height/Y_TILES;
			if(x >= 1 && x <= 100 && y >= 1 && y <= 100) {
				x = x - offsetX;
				y = y - offsetY;
				if(x >= 0 && x < X_TILES && y >= 0 && y < Y_TILES) {
					context.setFill(color);
					context.fillRect((x + 0.5-(width/2))*tileWidth, (y + 0.5-(height/2))*tileHeight, tileWidth*width, tileHeight*height);
				}
			}
		}
		public void fillText(double x, double y, String text, Color color) {	
			if(!Platform.isFxApplicationThread()) {
				final double fX=x, fY=y;
				Platform.runLater(() -> fillText(fX, fY, text, color));
				return;
			}
			double tileWidth = this.width/X_TILES, tileHeight = this.height/Y_TILES;	
			if(x >= 1 && x <= 100 && y >= 1 && y <= 100) {
				context.setFont(new Font(15.0).font("Calibri", FontWeight.BOLD, 26.0));
				x = x - offsetX;
				y = y - offsetY;
				if(x >= 0 && x < X_TILES && y >= 0 && y < Y_TILES) {
					context.setFill(color);
					context.fillText(text, x * tileWidth, y * tileHeight);
				}
			}
		}
		public Point getNextPoint(Point p) {
			if(p == null) {
				return new Point(offsetX, offsetY);
			}
			int x = p.x - offsetX, y = p.y - offsetY;
			y = (y + 1) % Y_TILES;
			x = (y == 0) ? (x + 1) % X_TILES : x;
			return (x == 0 && y == 0) ? null : new Point(x + offsetX, y + offsetY);
		}
	}
