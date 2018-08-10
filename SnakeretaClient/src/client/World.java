package client;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	
	private HashMap<String,Point> __Items;
	public HashMap<String,Point> getItems() {
		return __Items;
	}
	public void setItems(HashMap<String,Point> items) {
		__Items = items;
	}
	
	private int[] __Group;
	public int[] getGroup(){
		return __Group;
	}
	public void setGroup(int[] group) {
		__Group = group;
	}
	public boolean groupContains(int loginId) {
		for(int id : getGroup()) {
			if(id == loginId) return true;
			if(id == 0) return false;
		}
		return false;
	}
	
	private HashMap<Integer,Character> __Characters;
	public HashMap<Integer,Character> getCharacters() {
		return __Characters;		
	}
	public void setCharacters(HashMap<Integer,Character> characters) {
		__Characters = characters;		
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
	public Character getSelf() {
		return getCharacter(getSelfId());
	}
	public void setSelfId(int loginId) {
		__SelfId = loginId;	
	}
	
	private GameState __GameState;
	public GameState getGameState() {
		return __GameState;		
	}
	public void setGameState(GameState gameState) {
		__GameState = gameState;		
		Log.println("GameState: " + gameState);
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
		setGroup(new int[10]);
		setMaps(new HashMap<Integer,Map>());
		setItems(new HashMap<String,Point>());
		//setItemDrops or add to/update map
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
<<<<<<< HEAD
		folder = new File("src/data");
=======
		folder = new File(Paths.get("src","data").toString());
>>>>>>> 82c6be7739b8e611e7cecd9e45ade314e8b9d22b
		for (File file : folder.listFiles(new ExtensionFilter("adf"))) {
			AsperetaFileReader.load(file, this);
		}
		setLoadingInfo(20, "Loading map data.");
<<<<<<< HEAD
		folder = new File("src/maps");
=======
		folder = new File(Paths.get("src","maps").toString());
>>>>>>> 82c6be7739b8e611e7cecd9e45ade314e8b9d22b
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
		Character self = getSelf();
		GameView gv = this.getGameView();
		
		gv.clear();
		
		if(self != null) {
			int x,y;
			gv.setCenter(self.x, self.y);
			{
				Point p = null;
				while((p = gv.getNextPoint(p)) != null) {
					x = p.x;
					y = p.y;
					Tile tile = getTile(x,y);
					if(tile != null && tile.block) { //if(blockedTile(x,y)) {
						gv.fillRect(x, y, 1, 1, Color.LIGHTSLATEGRAY);	
					}
					else {
						gv.fillRect(x, y, 1, 1, Color.BLANCHEDALMOND);
					}	
				}
			}
			for(Point p : ((JoshBot)getBot()).getPath()) {
				x = p.x;
				y = p.y;
				gv.fillRect(x, y, 1, 1, Color.PALEVIOLETRED);
			}
			{
				Point p = ((JoshBot)getBot()).getCurrentPoint();
				if(p != null) {
					x = p.x;
					y = p.y;
					gv.fillRect(x, y, 1, 1, Color.INDIANRED);
				}
			}
			for(Point p : ((JoshBot)getBot()).getAttackPoints()) {
				x = p.x;
				y = p.y;
				gv.fillRect(x, y, 1, 1, Color.LIGHTBLUE);
			}
			for(Point p : getItems().values()) {
				x = p.x;
				y = p.y;
				gv.fillOval(x, y, 0.8, 0.8, Color.LIGHTYELLOW);
			}
			/*{
				Point p;
				while(p = getNextPoint(p)) {
					x = p.x;
					y = p.y;
					if(blockedTile(x,y)) {
						gv.fillRect(x, y, 1, 1, Color.LIGHTSLATEGRAY);
					}	
				}
			}*/
			Color color;
			for(Character c : getCharacters().values()) {
				x = c.x;
				y = c.y;
				if(c.characterType == CharacterType.Player) {
					color = Color.LIGHTGREEN;
				}
				else if(c.characterType == CharacterType.Admin) {
					color = Color.PLUM;
				}
				else {
					color = Color.BURLYWOOD;
				}
				gv.fillOval(x, y, 0.8, 0.8, color);
				
				double x2 = 0, y2 = 0;
				if(c.facing != null) {
					switch(c.facing) {
					case UP:
						y2 = -0.5;
						break;
					case DOWN:
						y2 = 0.5;
						break;
					case LEFT:
						x2 = -0.5;
						break;
					case RIGHT:
						x2 = 0.5;
						break;
					}
				}
				gv.fillOval(x + x2, y + y2, 0.2, 0.2, Color.CADETBLUE);
				gv.fillRect(x, y, 0.8, 0.1, Color.FLORALWHITE);
				gv.fillRect(x, y, 0.8 * c.hp / 100, 0.1, Color.INDIANRED);
				gv.fillText(x, y, c.name + " ["+c.hp+"]", Color.DARKSLATEBLUE);
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
