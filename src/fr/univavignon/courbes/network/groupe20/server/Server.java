package fr.univavignon.courbes.network.groupe20.server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.ServerCommunication;

public class Server implements ServerCommunication {

	/**
	 * le port utilisé par le serveur pour accepter les connexions
     * de la part des clients.
	 */
	private int port;
	
	/**
	 * le port utilisé par le serveur pour accepter les connexions
     * de la part des clients.
	 */
	private ServerSocket server;
	
	/**
     * Renvoie l'adresse IP de ce serveur, que les clients doivent
     * utiliser pour se connecter à lui.
     *
     * @return 
     * 		Une chaîne de caractères qui correspond à l'adresse IP du serveur.
     * 	 	En cas d'erreur,elle renvoit null.
     */
	
	@Override
	public String getIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @return un entier qui correspond au port utiliser par le serveur.
	 */
	
	@Override
	public int getPort() {return port;}

	/**
	 * Modifie le port utilisé par le serveur pour accepter les connexions
     * de la part des clients. Cette valeur est à modifier avant d'utiliser 
     * {@link #launchServer}.
     * 
     * @param port
     * 		Le nouveau port doit être un entier
     * 		Le nouveau port du serveur.
     * 		
	 */
	
	@Override
	public void setPort(int port) {this.port = port;}

	@Override
	public void launchServer() {

	}

	@Override
	public void closeServer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendProfiles(List<Profile> profiles) {
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

	@Override
	public void sendText(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] retrieveText() {
		// TODO Auto-generated method stub
		return null;
	}

}
