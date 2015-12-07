package fr.univavignon.courbes.network.groupe15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.ServerCommunication;

/**
 * 
 * @author uapv1301073
 *
 */

public class Server implements ServerCommunication {
	
	/** Port du serveur */
	private int port;
	/** Connecteur Serveur/Clients */
	private Socket socket;
	/** Connecteur côté Serveur */
	private ServerSocket socketServeur;
	
	/**
	 * @param socket Connecteur Serveur/Clients
	 */
	public Server(Socket socket) {
	    this.socket = socket;
	}

	@Override
	public String getIp() {
		InetAddress IP;
		try {
			IP = InetAddress.getLocalHost();
			return IP.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getPort() {
		return this.port;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public void launchServer() {
		try {
			socketServeur = new ServerSocket(port);
			System.out.println("Lancement du serveur");
			while (true) {
				Socket socketClient = socketServeur.accept();
				Server Serveur = new Server(socketClient);
				System.out.println("Connexion avec le client : " + socket.getInetAddress());
				String[] s = Serveur.retrieveText();
				sendText(s[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void closeServer() {
		try {
			socketServeur.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendText(String message) {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			System.out.println(message);
			if(message.contains("yolo"))
				out.println("swag");
			else
				out.println("yolo?");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] retrieveText() {
		try {
			String[] messages = { "" };
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			messages[0] = in.readLine();
			System.out.println(messages[0]);
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void sendPlayers(List<Profile> profiles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPointThreshold(int pointThreshold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBoard(Board board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Integer, Direction> retrieveCommands() {
		// TODO Auto-generated method stub
		return null;
	}

}
