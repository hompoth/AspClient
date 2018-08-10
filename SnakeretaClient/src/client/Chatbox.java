package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.event.*;
import javax.swing.text.DefaultCaret;

public class Chatbox extends JFrame {	
	
	private static World __World;
	private static World getWorld() {
		return __World;
	}
	private void setWorld(World world) {
		__World = world;
	}
	
	static JPanel jp;
	static JTextField jt;
	static JTextArea ta;
	static JLabel l;

	public Chatbox(World world) {
		setWorld(world);
		createAndShowGUI();
	}

	private void createAndShowGUI() {

		setTitle("Chatbox");
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		jp = new JPanel();
		jp.setLayout(new GridLayout(2, 1));
		l = new JLabel();
		jp.add(l);
		jt = new JTextField(); // Create JTextField, add it.
		jp.add(jt);

		add(jp, BorderLayout.SOUTH); // Add panel to the south,

		jt.addKeyListener(new KeyAdapter() { // Add a KeyListener
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_ENTER) {	//send packet message
//					showLabel(jt.getText());
					try {
						sendMessage(jt.getText());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		ta = new JTextArea(); // Create a textarea
		DefaultCaret caret = (DefaultCaret) ta.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); // JScrollPane auto scroll
		ta.setEditable(false); // Make it non-editable
		ta.setMargin(new Insets(7, 7, 7, 7)); // Set some margin, for the text
		JScrollPane js = new JScrollPane(ta); // Set a scrollpane
		add(js);

		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent we) {
				jt.requestFocus(); // Get the focus when window is opened
			}
		});

		setSize(400, 200);
		// setLocationRelativeTo(null);
		setLocation(0, 800);
		setVisible(true);
	}
	
	// TODO: Add colour
	//	$7 = basic text
	//	$6 = whisper
	//	$3 = group
	//	$2 = guild
	/**
	 * ChatEvent, event for ";" packet
	 * <p>
	 * Called when someone types a message Packet format: ;Message
	 * <p>
	 * Server responds: ^LoginID, Name: Message Server sends the response to everyone in the area
	 * including the player
	 */
	
	public static void showLabel(String text) {
		if (text.trim().isEmpty())	// If text is empty return
			return;
		ta.append(text + "\n");
	}
	
	private static void sendMessage(String text) throws IOException {
		if (text.trim().isEmpty())	// If text is empty return
			return;
		
		if (text.startsWith("/"))	//enter a command, such as "/tell krt Hey, punk."
			getWorld().getCommunication().command(text);
		else if (text.startsWith("|"))	//use with caution. Directly write a packet, such as "|ATT" or "|F1"
			getWorld().getCommunication().directWrite(text.substring(1));
		else
			getWorld().getCommunication().chat(text);
			
		 jt.setText("");
		 l.setText("");
	}
}