package client;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import client.World.ConnectionState;

public class Communication extends Thread {
	private Socket socket;
	private World world;
	Scanner in;
	BufferedOutputStream out;
	
	private PacketHandler __MessageHandler;
	
	public void setMessageHandler(PacketHandler messageHandler) {
		__MessageHandler = messageHandler;
	}
	public PacketHandler getMessageHandler() {
		return __MessageHandler;
	}
	
	public Communication(Socket socket, World world) throws Exception {
		this.socket = socket;
		this.world = world;
		in = new Scanner(new InputStreamReader(socket.getInputStream())).useDelimiter("\u0001");
		out = new BufferedOutputStream(socket.getOutputStream());
		setMessageHandler(new PacketHandler(this));
	}

	@Override
	public void run(){
		String message;
		while((world.isRunning() || world.isConnecting()) && in.hasNext()) {
			message = in.next();
			getMessageHandler().addEvent(message);
		}
	}
	
	public void update() throws Exception {
		getMessageHandler().update(world);
	}

	private void write(String message) throws IOException {
		String packet = message + "\u0001";
		out.write(packet.getBytes());
		out.flush();
	}
	public void login(String username, String password, String version) throws IOException {
		write("LOGIN" + username + "," + password + "," + version);
	}
	public void loginContinued() throws IOException {
		write("LCNT");
	}
	public void doneLoadingMap(int mapId) throws IOException {
		write("DLM"+mapId);
	}
	public void pong() throws IOException {
		write("PONG");
	}
	public void face(Direction facing) throws IOException {
		int direction = Direction.toInteger(facing, true);
		write("F" + direction);
	}
	public void move(Direction facing) throws IOException {
		if(facing != null) {
			int direction = Direction.toInteger(facing, false);
			write("M" + direction);
			int face = Direction.toInteger(facing, true);
			write("F" + face);
			world.getSelf().move(Direction.fromInteger(direction));
		}
	}
	public void refresh() throws IOException{
		write("/refresh");
	}
	public void cast(int i, int id) throws IOException {
		write("CAST"+i+","+id);
	}
	public void use(int slot) throws IOException {
		write("USE"+slot);
	}
	public void attack() throws IOException {
		write("ATT");
	}
	public void pickup() throws IOException {
		write("GET");
	}
	public void groupAdd(String name) throws IOException {
		write("/groupadd "+name);
		
	}
}
