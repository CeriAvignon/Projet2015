package fr.univavignon.courbes.network.groupe06;

import fr.univavignon.courbes.network.ClientCommunication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;

/**
 * @author Loïc
 *
 */
public class Client implements ClientCommunication {

	/** Variable qui contient l'adresse ip du serveur */
	protected String ip;
	/** Variable qui contient le port du serveur */
	protected int port = 2345;
	/** Socket du client connecté au serveur */
	protected Socket serverConnexion = null;
	/** Buffer pour le board */
	protected Board board = null;
	/**  Buffer pour le nombre de point a atteindre */
	protected int pointThreshold = -1;
	/**	Buffer pour la liste des profiles */
	protected List<Profile> profiles = null;
	/** Buffer pour les messages du serveur */
	protected String messageText = "";
	/** Erreur de profil */
	protected ClientProfileHandler profileError;
	/**Envoie d'un message d'erreur a l'IU	 */
	protected ErrorHandler messageError;
	
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
			this.serverConnexion = new Socket(ip, port);
			this.threadRetrieveBoard();
			this.retrieveSerializable();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void closeClient() {
		try {
			this.sendText("/quit");
			Thread.sleep(0500); //Pour laisser le temps au client d'envoyer le message avant de fermer sa connexion.
			this.serverConnexion.close();
			this.serverConnexion = null;
		} catch (IOException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Integer retrievePointThreshold() {

		return this.pointThreshold;
	}

	@Override
	public Board retrieveBoard() {
		return this.board;
	}
	
	/**
	 * Permet de constament recevoir un objet board 
	 * et de le stocker dans la variable de classe. (UDP)
	 */
	public void threadRetrieveBoard()
	{
		Thread retrieve = new Thread(new Runnable(){
			@Override
			public void run(){
				while(serverConnexion != null)
				{
					try {
						DatagramSocket socket = new DatagramSocket(port+1);

						byte[] data = new byte[4];
						DatagramPacket packet = new DatagramPacket(data, data.length );
						socket.receive(packet);

						int len = 0;
						// byte[] -> int
						for (int i = 0; i < 4; ++i) {
							len |= (data[3-i] & 0xff) << (i << 3);
						}

						// now we know the length of the payload
						byte[] buffer = new byte[len];
						packet = new DatagramPacket(buffer, buffer.length );
						socket.receive(packet);

						ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
						ObjectInputStream oos = new ObjectInputStream(baos);
						board = (Board)oos.readObject();
						socket.close();
				    
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		retrieve.start();
		
	}
	@Override
	public void sendCommands(final Map<Integer, Direction> commands) {
		Thread send = new Thread(new Runnable(){
			@Override
			public void run(){
				sendObject(profiles);
			}
		});
		send.start();
		return;
	}

	@Override
	public String retrieveText() {
		return this.messageText;
	}

	@Override
	public void sendText(final String message) {
		Thread send = new Thread(new Runnable(){
			@Override
			public void run(){
				sendObject(message);
			}
		});
		send.start();
		return;
		
	}
	
	/**
	 * Pour récupérer les objets qui ont été sérializé. (TCP)
	 */
	public void retrieveSerializable(){
		Thread retrieve = new Thread(new Runnable(){
			@Override
			public void run(){
				ObjectInputStream ois;
				while(serverConnexion != null) {
					try {
						ois = null;
						ois = new ObjectInputStream(serverConnexion.getInputStream());
						Object objet = ois.readObject();
						if (objet instanceof List<?>) 
							profiles = (List<Profile>)objet;
						else if (objet instanceof String) 
							messageText = (String)objet;
						else if (objet instanceof Integer)
							pointThreshold = (int)objet;
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});
		retrieve.start();
		return;
	}
	
	/**
	 * @param objet
	 * 		Envoyer tout ce dont on a besoin.
	 * @return 
	 * 		Un bool pour valider que l'envoie c'est bien passé.
	 */
	private synchronized boolean sendObject(Object objet) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(serverConnexion.getOutputStream());
			oos.writeObject(objet);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		messageError = errorHandler;
		
	}

	@Override
	public void setProfileHandler(ClientProfileHandler profileHandler) {
		profileError = profileHandler;
		return;
	}

	@Override
	public boolean addProfile(Profile profile) {
		return sendObject(profile);
	}

	@Override
	public void removeProfile(final Profile profile) {
		Thread send = new Thread(new Runnable(){
			@Override
			public void run(){
				sendObject(profile);
			}
		});
		send.start();
		return;
	}
}

