package client;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import client.World.GameState;
import client.bot.Bot;
import client.bot.JoshBot;
import client.bot.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class World {
	public enum ConnectionState {
		Disconnected,
		Connecting,
		Connected
	}
	public enum GameState {
		LoadingScreen,
		Map
	}

	private int X_TILES = 13;
	private int Y_TILES = 9;
	
	private HashMap<Integer,Character> __Characters;
	public void setCharacters(HashMap<Integer,Character> characters) {
		__Characters = characters;		
	}
	public HashMap<Integer,Character> getCharacters() {
		return __Characters;		
	}
	public Character getCharacter(int loginId) {
		return getCharacters().get(loginId);
	}
	public void setCharacter(int loginId, Character character) {
		getCharacters().put(loginId, character);
	}
	
	private HashMap<Integer,Map> __Maps;
	public void setMaps(HashMap<Integer,Map> maps) {
		__Maps = maps;		
	}
	public HashMap<Integer,Map> getMaps() {
		return __Maps;		
	}
	public Map getCurrentMap() {
		return getMaps().get(getMapId());
	}
	public Tile getTile(int x, int y) {
		if(x-1 >= 0 && x-1 < 100 && y-1 >= 0 && y-1 < 100) {
			return getCurrentMap().tiles[y-1][x-1];
		}
		return null;
	}
	
	private int __SelfId;
	public int getSelfId() {
		return __SelfId;		
	}
	public void setSelfId(int loginId) {
		__SelfId = loginId;	
	}
	public Character getSelf() {
		return getCharacter(getSelfId());
	}
	
	private GameState __GameState;
	public void setGameState(GameState gameState) {
		__GameState = gameState;		
		Log.println("GameState: " + gameState);
	}
	public GameState getGameState() {
		return __GameState;		
	}
	
	private ConnectionState __ConnectionState;
	public ConnectionState getConnectionState() {
		return __ConnectionState;
	}
	public void setConnectionState(ConnectionState connectionState) {
		__ConnectionState = connectionState;
		Log.println("ConnectionState: " + connectionState);
	}
	public boolean isConnecting() {
		return __ConnectionState == ConnectionState.Connecting;
	}
	public boolean isRunning() {
		return __ConnectionState == ConnectionState.Connected;
	}
	
	private Communication __Communication;
	public Communication getCommunication() {
		return __Communication;
	}
	public void setCommunication(Communication communication) {
		__Communication = communication;
	}

	private ActionHandler __ActionHandler;
	public void setActionHandler(ActionHandler actionHandler) {
		__ActionHandler = actionHandler;
	}
	public ActionHandler getActionHandler() {
		return __ActionHandler;
	}

	private MessageBox __MessageBox;
	public void setMessageBox(MessageBox mb) {
		__MessageBox = mb;
	}
	public MessageBox getMessageBox() {
		return __MessageBox;
	}
	
	private GameView __GameView;
	public void setGameView(GameView gameView) {
		__GameView = gameView;
	}
	public GameView getGameView() {
		return __GameView;
	}
	private int __LoadingPercent;
	public int getLoadingPercent() {
		return __LoadingPercent;
	}
	public void setLoadingPercent(int loadingPercent) {
		__LoadingPercent = Math.min(100, loadingPercent);
	}
	public void addLoadingPercent(int loadingPercent) {
		setLoadingPercent(getLoadingPercent() + loadingPercent);
	}
	
	private String __LoadingMessage = "";
	public void setLoadingMessage(String loadingMessage) {
		__LoadingMessage = loadingMessage;
		
	}	
	public String getLoadingMessage() {
		return __LoadingMessage;
	}
	
	private int __MapId;
	public int getMapId() {
		return __MapId;
	}
	public void setMapId(int mapId) {
		__MapId = mapId;
		Log.println("MapId: " + mapId);
	}
	
	private String __MapName;
	public String getMapName() {
		return __MapName;
	}
	public void setMapName(String mapName) {
		__MapName = mapName;
		Log.println("MapName: " + mapName);
	}
	
	private Bot __Bot;
	public void setBot(Bot bot) {
		__Bot = bot;
	}
	public Bot getBot() {
		return __Bot;
	}
	
	private String __Guild;
	public void setGuild(String guild) {
		__Guild = guild;
	}
	public String getGuild() {
		return __Guild;
	}
	
	private String __ClassName;
	public void setClassName(String className) {
		__ClassName = className;
	}
	public String getClassName() {
		return __ClassName;
	}
	
	private int __Level;
	public void setLevel(int level) {
		__Level = level;
	}
	public int getLevel() {
		return __Level;
	}
	
	private void setBotEnabled(boolean enabled) {
		getBot().setEnabled(enabled);
	}
	private boolean getBotEnabled() {
		return getBot().getEnabled();
	}
	
	public void setLoadingInfo(int percent, String message) throws Exception {
		setLoadingPercent(percent);
		setLoadingMessage(message);
		render();
	}
	
	public World(Socket socket, GameView gameView) throws Exception {
		setConnectionState(ConnectionState.Disconnected);
		setGameState(GameState.LoadingScreen);
		setGameView(gameView);
		setCommunication(new Communication(socket, this));
		setMessageBox(new MessageBox());
		setActionHandler(new ActionHandler(this, getGameView().stage)); // This handler handles keyboard/mouse actions. It will use world to send packets and update world objects (players/npcs).
		setCharacters(new HashMap<Integer,Character>());
		setMaps(new HashMap<Integer,Map>());
		setBot(new JoshBot(this));
	}
	
	int i = 0;
	public void update(double delta) throws Exception {
		getCommunication().update();
		if(getBotEnabled()) {
			getBot().update();
		}	
	}
	
	public void ConnectToServer(String username, String password) throws Exception {
		setConnectionState(ConnectionState.Connecting);
		getCommunication().start();
		getCommunication().login(username, password, "ALPHA33,3.5.2");
	}

	public void render() {
		switch(getGameState()) {
		case LoadingScreen:
			this.getGameView().drawLoadingScreen(getLoadingPercent(), getLoadingMessage());
			break;
		case Map:
			this.drawMap();
			break;
		}
		
	}
	
	// TODO: Load game data
	public void loadGame() throws Exception {
		File folder;
		getGameView().setGameScene(); // Display game
		setGameState(GameState.LoadingScreen);
		setLoadingInfo(0, "Loading game data.");
		folder = new File("src/data");
		for (File file : folder.listFiles(new ExtensionFilter("adf"))) {
			AsperetaFileReader.load(file, this);
		}
		setLoadingInfo(20, "Loading map data.");
		folder = new File("src/maps");
		for (File file : folder.listFiles(new ExtensionFilter("map"))) {
			String fileName = file.getName();
			int mapId = Integer.parseInt(fileName.substring(3, fileName.indexOf(".")));
			getMaps().put(mapId,new Map(file));
		}
		setLoadingInfo(30, "Loading sprites.");
		setLoadingInfo(40, "Loading items.");
		setLoadingInfo(50, "Loading spells.");
		setLoadingInfo(60, "Loading NPCs.");
		setLoadingInfo(70, "Waiting for location.");
	}

	// TODO: Load map data
	public void loadMap(int mapId, String mapName) throws Exception {
		setMapId(mapId);
		setMapName(mapName);
		setGameState(GameState.LoadingScreen); // TODO: Move this to warp event (if exists)
		setLoadingInfo(70, "Loading map.");
		// Load map data
		setLoadingInfo(100, "Entering map.");
		setGameState(GameState.Map);
	}
	
	// Tiles squares, players circles
	public void drawMap() {
		GameView gv = this.getGameView();
		GraphicsContext context = gv.context;
		context.setFont(new Font(26.0).font("Calibri", FontWeight.BOLD, 26.0));
		double width = gv.width;
		double height = gv.height;
		double tileWidth = width/X_TILES, tileHeight = height/Y_TILES;
		Character self = getSelf();
		
		context.setFill(Color.BLACK);
		context.fillRect(0, 0, width, height);
		
		if(self != null) {
			int x,y;
			for(int i = 0; i < X_TILES; ++i) {
				for(int j = 0; j < Y_TILES; ++j) {
					x = i + self.x - X_TILES/2;
					y = j + self.y - Y_TILES/2;

					if(x >= 1 && x <= 100 && y >= 1 && y <= 100) {
						context.setFill(Color.BLANCHEDALMOND);
						context.fillRect(i*tileWidth, j*tileHeight, tileWidth, tileHeight);
					}
				}
			}
			for(Point p : ((JoshBot)getBot()).getPath()) {
				x = p.x - self.x;
				y = p.y - self.y;
				if(x >= -X_TILES/2 && x <= X_TILES/2 && y >= -Y_TILES/2 && y <= Y_TILES/2) {
					context.setFill(Color.RED);
					context.fillRect((x+X_TILES/2)*tileWidth, (y+Y_TILES/2)*tileHeight, tileWidth, tileHeight);
				}
			}
			for(Point p : ((JoshBot)getBot()).getAttackPoints()) {
				x = p.x - self.x;
				y = p.y - self.y;
				if(x >= -X_TILES/2 && x <= X_TILES/2 && y >= -Y_TILES/2 && y <= Y_TILES/2) {
					context.setFill(Color.LIGHTBLUE);
					context.fillRect((x+X_TILES/2)*tileWidth, (y+Y_TILES/2)*tileHeight, tileWidth, tileHeight);
				}
			}
			for(int i = 0; i < X_TILES; ++i) {
				for(int j = 0; j < Y_TILES; ++j) {
					x = i + self.x - X_TILES/2;
					y = j + self.y - Y_TILES/2;

					if(x >= 1 && x <= 100 && y >= 1 && y <= 100) {
						if(blockedTile(x,y)) {
							context.setFill(Color.LIGHTSLATEGRAY);
							context.fillRect(i*tileWidth, j*tileHeight, tileWidth, tileHeight);
						}
					}
				}
			}
			for(Character c : getCharacters().values()) {
				if(c.x >= 1 && c.x <= 100 && c.y >= 1 && c.y <= 100) {
					x = c.x - self.x;
					y = c.y - self.y;
					if(x >= -X_TILES/2 && x <= X_TILES/2 && y >= -Y_TILES/2 && y <= Y_TILES/2) {
						if(c.characterType == CharacterType.Player) {
							context.setFill(Color.LIGHTGREEN);
						}
						else if(c.characterType == CharacterType.Admin) {
							context.setFill(Color.PLUM);
						}
						else {
							context.setFill(Color.BURLYWOOD);
						}
						context.fillOval((x+X_TILES/2)*tileWidth, (y+Y_TILES/2)*tileHeight, tileWidth, tileHeight);
						
						double x2 = 0, y2 = 0;
						switch(c.facing) {
						case UP:
							x2 = 0.5;
							break;
						case DOWN:
							x2 = 0.5;
							y2 = 1;
							break;
						case LEFT:
							y2 = 0.5;
							break;
						case RIGHT:
							x2 = 1;
							y2 = 0.5;
							break;
						}
						context.setFill(Color.CADETBLUE);
						context.fillOval(((x+X_TILES/2) + x2 - 0.1) * tileWidth, ((y+Y_TILES/2) + y2 - 0.1) * tileHeight, tileWidth * 0.2, tileHeight * 0.2);
						context.setFill(Color.FLORALWHITE);
						context.fillRect((x+X_TILES/2 + 0.1)*tileWidth, (y+Y_TILES/2 + 0.2)*tileHeight, tileWidth * 0.8, tileHeight * 0.1);
						context.setFill(Color.INDIANRED);
						context.fillRect((x+X_TILES/2 + 0.1)*tileWidth, (y+Y_TILES/2 + 0.2)*tileHeight, tileWidth * 0.8 * c.hp / 100, tileHeight * 0.1);
						context.setFill(Color.DARKSLATEBLUE);
						context.fillText(c.name + " ["+c.hp+"]", (x+X_TILES/2)*tileWidth, (y+Y_TILES/2 + 0.2)*tileHeight);
					}
				}
			}
		}
	}
	public boolean blockedTile(int x, int y) {
		Tile tile = getTile(x,y);
		if(!(x >= 1 && x <= 100 && y >= 1 && y <= 100)) {
			return true;
		}
		if(tile != null && tile.block) {
			return true;
		}
		for(Character c : getCharacters().values()) {
			if(c.loginId == getSelfId()) continue;
			if(c.x == x && c.y == y) return true;
		}
		return false;
	}
	public void toggleBot() {
		setBotEnabled(!getBotEnabled());
		Log.println("Bot enabled: "+getBotEnabled());
	}
}
