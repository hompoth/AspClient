package client;

import java.io.IOException;
import java.net.Socket;
import client.GameView;
import client.World.ConnectionState;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Game implements Runnable {
	private World world;
	private Socket socket;
	private String host; //grab from file
	private int port;
	private String username, password;
	private Thread thread;
	private GameView gameView;
	
	public Game(String username, String password, String host, int port, GameView gameView) {
		this.username = username;
		this.password = password;
		
		this.host = host;
		this.port = port;
		
		this.gameView = gameView;
		
		thread = new Thread(this);
		thread.start();
	}
	
    public void setConnectionState(ConnectionState connectionState) {
    	if(world == null) {
    		return;
    	}
    	world.setConnectionState(connectionState);
    }
    public boolean isRunning() {
    	if(world == null) {
    		return false;
    	}
        return world.isRunning();
    }
    public boolean isConnecting() {
    	if(world == null) {
    		return false;
    	}
    	return world.isConnecting();
    }
    
	@Override
	public void run() {
		try{
			String response = "";
			
			if (username.isEmpty() || password.length() < 3) {
				throw new Exception("Username or password is empty.");
			}
			try {
				socket = new Socket(host, port);
			}
			catch (Exception e) {
				throw new Exception("Cannot connect to host.");
			}
/*
        	Scanner in = new Scanner(new InputStreamReader(socket.getInputStream())).useDelimiter("\u0001");
        	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	        out.writeBytes("\u0001" + "LOGIN" + username + "," + password + ",ALPHA33,3.5.2\u0001");

	        boolean attemptConnect = true;
	        while(attemptConnect) {
	        	if(in.hasNext()) {
	        		response = in.next();
	            	attemptConnect = false;
	        	}
	        	Thread.sleep(1);
	        }
			System.out.println("Message: "+response);
			String code = response.substring(0, 3);
			String message = response.substring(3);
			switch(code) {
				case "LOK":
					System.out.println("LOK!");
					out.writeBytes("LCNT");
					attemptConnect = false;
					break;
				case "LNO":
					throw new Exception(message);
				default:
					System.out.println(response);
					throw new Exception("I don't know what happened!\n"+response); 
			}
			world = new World(socket);
			world.setRunning(true);*/
			
			world = new World(socket, gameView);
			world.ConnectToServer(username, password);
			
        	// TODO: Launch popup that says "Attempting to connect"
        	// With cycling periods ... .. .
			// If the popup button is pressed, the login is cancelled. (Just cancel the loop below)
			while(isConnecting()) {
				world.getCommunication().update();
				Thread.sleep(1);
			}
			if(!isRunning()) {		
		        this.quitGame(); 
				return;
			}
			
			// TODO: add all popups before gameLoop()
			new Chatbox(world);
			this.gameLoop();
		}
		catch (Exception e) {
			Platform.runLater(() -> {
				Log.println(e.getStackTrace());
				e.printStackTrace();
				// TODO: Make the popup appear nicer
	            final Stage dialog = new Stage();
	            dialog.initModality(Modality.APPLICATION_MODAL);
	            dialog.setResizable(false);
	            dialog.initOwner(gameView.stage);
	            VBox dialogVbox = new VBox(20);
	            dialogVbox.getChildren().add(new Text("Error:\n"+ e.getMessage()));
//	            Chatbox.showLabel(e.getMessage());	// display in new chatbox
	            Scene dialogScene = new Scene(dialogVbox, 250, 50);
	            dialogVbox.setPadding(new Insets(5));
	            dialog.setScene(dialogScene);
	            dialog.show();
	            System.out.println(world == null);
	            this.setConnectionState(ConnectionState.Disconnected);
			});
		}
	}
	
	public void gameLoop() throws Exception {

		long lastLoopTime = System.nanoTime();
		final int TARGET_FPS = 60;
		final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;
		long lastFpsTime = 0;
		long fps = 0;
		
		// keep looping round til the game ends
		while (this.isRunning())
		{
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			double delta = updateLength / ((double)OPTIMAL_TIME);

			lastFpsTime += updateLength;
			fps++;
        	
			// use delta for animations
			world.update(delta);

			world.render();
			
			if (lastFpsTime >= 1_000_000_000)
			{
				//Log.println("(FPS: "+fps+")");
				lastFpsTime = 0;
				fps = 0;
			}

			long sleepTime = (OPTIMAL_TIME - updateLength) / 1_000_000;
			if (sleepTime > 0) {
			//	Thread.sleep(sleepTime);
			}
			Thread.sleep(100);
		}		
        this.quitGame(); 
    }
	
	private void quitGame() throws IOException {
		socket.close();
		this.setConnectionState(ConnectionState.Disconnected);
		// send onQuit data
		// destroy world
	}
	
}
