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
	/** Gestion des erreurs de profil */
	private ServerProfileHandler profileHandler;
	/** Gestion du message d'erreur */
	private ErrorHandler errorHandler;
	
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
	/** Les directions choisies par chaque joueur */
	Map<Integer, Direction> map;
	
	/**
	 * On initialize la liste de sockets
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
			Thread t = new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						while(!serverSocket.isClosed()) {
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
			for(Socket socket:sockets) {
				socket.close();
			}
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendProfiles(List<Profile> profiles) {
		this.currentProfiles = profiles;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				sendObject(currentProfiles);
			}
		});
		t.start();
	}

	@Override
	public void sendPointThreshold(int pointThreshold) {
		this.currentPointThreshold = pointThreshold;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				sendObject(currentPointThreshold);
			}
		});
		t.start();
	}

	@Override
	public void sendBoard(Board board) {
		this.currentBoard = board;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				sendObject(currentBoard);
			}
		});
		t.start();
	}

	/**
	 * Méthode non bloquante permettant d'envoyer un objet à tous les clients du serveur
	 * @param object Objet à envoyer au(x) client(s)
	 */
	private synchronized void sendObject(Object object) {
		try {
			for(Socket socket:sockets) {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(object);
				oos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<Integer, Direction> retrieveCommands() {
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				map = retrieveMap();
			}
		});
		t.start();
		return map;
	}

	/**
	 * Méthode non bloquante permettant de récupérer une map
	 * @return Une map contenant les directions choisies par chaque joueur
	 */
	private synchronized Map<Integer, Direction> retrieveMap() {
		try {
			map = null;
			for(Socket socket:sockets) {
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Object obj = ois.readObject();
				if(obj instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<Integer, Direction> tmpMap = (Map<Integer, Direction>)obj;
					map.putAll(tmpMap);
				}
			}
		} catch (Exception e) {
			map = null;
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	@Override
	public void setProfileHandler(ServerProfileHandler profileHandler) {
		this.profileHandler = profileHandler;
	}
}
