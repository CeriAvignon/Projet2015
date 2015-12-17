package fr.univavignon.courbes.network.groupe15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
	/** Liste des connecteurs Serveur/Clients */
	private volatile ArrayList<Socket> sockets;
	
	/**
	 * @return Connecteurs Serveur/Clients
	 */
	public ArrayList<Socket> getSockets() {
		return sockets;
	}

	/** Connecteur côté Serveur */
	private ServerSocket serverSocket;
	
	/** Liste des messages récupéré des clients */
	String[] messages = { "" };
	/** Message à envoyer au côté client */
	String messageSent;
	
	/**
	 * Initialize la liste de sockets.
	 */
	public Server() {
	    this.sockets = new ArrayList<Socket>();
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
	
	/**
	 * On synchronise l'affichage
	 * @param s Message à afficher
	 */
	public void safePrintln(String s) {
		synchronized(System.out) {
			System.out.println(s);
		}
	}
	
	@Override
	public void launchServer() {
		try {
			this.serverSocket = new ServerSocket(this.port);
			System.out.println("Lancement du serveur");
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while(true) {
							sockets.add(serverSocket.accept());
							safePrintln("Connexion avec le client : " + sockets.get(sockets.size()-1).getInetAddress());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

	@Override
	public void closeServer() {
		try {
			for(Socket socket:sockets){
				socket.close();
			}
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendText(String message) {
		messageSent = message;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					for(Socket socket:sockets){
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						out.println(messageSent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	@Override
	public String[] retrieveText() {
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					int i = 0;
					for(Socket socket:sockets){
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						messages[i] = in.readLine();
						System.out.println(messages[i]);
						i++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		return messages;
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
		for(Socket socket:sockets) {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(board);
				oos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Map<Integer, Direction> retrieveCommands() {
		// TODO Auto-generated method stub
		return null;
	}

}
