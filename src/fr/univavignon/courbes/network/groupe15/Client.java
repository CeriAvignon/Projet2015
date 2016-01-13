package fr.univavignon.courbes.network.groupe15;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.network.ClientCommunication;

/**
 * 
 * @author uapv1504059
 *
 */

public class Client implements ClientCommunication {

	/** Port du serveur */
	private int port;
	/** Adresse ip du serveur */
	private String ip;
	/** Connecteur côté client */
	private Socket socket;
	/** Plateau de jeu */
	private Board board;
	/**	Limite de points de la partie */
	private Integer pointThreshold;
	/**	Map des commandes envoyées par le client */
	private Map<Integer, Direction> commandsSent;
	/**	Profil envoyé par le client */
	private Profile profileSent;
	/**	Prend la valeur TRUE si le dernier transfert (réception ou envoi) s'est effectué avec succes et FALSE sinon */
	private boolean success;
	
	/** Gestion des erreurs de profil */
	private ClientProfileHandler profileHandler;
	/** Gestion du message d'erreur */
	private ErrorHandler errorHandler;

    @Override
	public String getIp() {
		return this.ip;
	}

	@Override
	public void setIp(String ip) {
		this.ip = ip;
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
	public void launchClient() {
		try {
			this.socket = new Socket(ip, port);
			if(socket == null) {
    			System.out.println("Connexion échouée...");
    		} else {
    			System.out.println("Connexion établie !!");
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void closeClient() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer retrievePointThreshold() {
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				pointThreshold = (Integer)retrieveObject();
			}
		});
		t.start();
		return this.pointThreshold;
	}

	@Override
	public Board retrieveBoard() {
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				board = (Board)retrieveObject();
			}
		});
		t.start();
		return this.board;
	}

	@Override
	public void sendCommands(Map<Integer, Direction> commands) {
		this.commandsSent = commands;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				sendObject(commandsSent);
			}
		});
		t.start();
	}

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;		
	}

	@Override
	public void setProfileHandler(ClientProfileHandler profileHandler) {
		this.profileHandler = profileHandler;
	}

	@Override
	public boolean addProfile(Profile profile) {
		this.profileSent = profile;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				sendObject(profileSent);
			}
		});
		t.start();
		return this.success;
	}

	@Override
	public void removeProfile(Profile profile) {
		this.profileSent = profile;
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				sendObject(profileSent);
			}
		});
		t.start();
	}
	
	/**
	 * Fonction qui permet de récupérer un objet depuis le serveur
	 * @return Renvois l'objet que le serveur à envoyé
	 */
	public synchronized Object retrieveObject(){
		Object object;
		try{
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			object = ois.readObject();
			this.success = true;
		} catch(Exception e){
			e.printStackTrace();
			this.success = false;
			return null;
		}
		return object;
	}
	
	/**
	 * Fonction qui permet d'envoyer un objet sur le serveur
	 * @param object Objet à envoyer au serveur
	 */
	public synchronized void sendObject(Object object){
		try{
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(object);
			oos.flush();
			this.success = true;
		}catch(Exception e){
			e.printStackTrace();
			this.success = false;
		}
	}

}
