package fr.univavignon.courbes.network.groupe15;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerProfileHandler;
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
	
	/** Liste des profils actuels */
	List<Profile> currentProfiles;
	/** Le score à atteindre pour gagner la partie actuelle */
	int currentPointThreshold;
	/** Le plateau actuel */
	Board currentBoard;
	/** Les directions choisies par chaque joueur*/
	Map<Integer, Direction> map;
	
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

	/*@Override
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
						ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
						messages[i] = (String)ois.readObject();
						System.out.println(messages[i]);
						ois.reset();
						i++;
					}
				} catch (Exception e) {
					messages = null;
					e.printStackTrace();
				}
			}
		});
		t.start();
		return messages;
	}*/

	@Override
	public void sendProfiles(List<Profile> profiles) {
		this.currentProfiles = profiles;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					for(Socket socket:sockets){
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(currentProfiles);
						oos.flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	@Override
	public void sendPointThreshold(int pointThreshold) {
		this.currentPointThreshold = pointThreshold;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					for(Socket socket:sockets){
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(currentPointThreshold);
						oos.flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	@Override
	public void sendBoard(Board board) {
		this.currentBoard = board;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					for(Socket socket:sockets){
						ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
						oos.writeObject(currentBoard);
						oos.flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	@Override
	public Map<Integer, Direction> retrieveCommands() {
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					for(Socket socket:sockets){
						ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
						Object obj = ois.readObject();
						if(obj instanceof Map) {
							@SuppressWarnings("unchecked")
							Map<Integer, Direction> tmpMap = (Map<Integer, Direction>)obj;
							map.putAll(tmpMap);
						} else {
							map = null;
						}
					}
				} catch (Exception e) {
					map = null;
					e.printStackTrace();
				}
			}
		});
		t.start();
		return map;
	}

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProfileHandler(ServerProfileHandler profileHandler) {
		// TODO Auto-generated method stub
		
	}

}
