package fr.univavignon.courbes.network.groupe20.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerProfileHandler;
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
	 * List de type socket qui contient les clients qui sont connéctés au serveur
	 */
	private List<Socket> clients = new CopyOnWriteArrayList<Socket>();
	
	/**
	 * 
	 *List des  profils envoyés par les clients, et chaque profil représente un joueur qui désire
	 * participer à la partie en cours de configuration.
	 */
	List<Profile> profileClients = new CopyOnWriteArrayList<Profile>();
	
	/**
	 * List des maps contenant les commandes envoyés par les clients.
	 */
	List<Map<Integer,Direction>> directions = new CopyOnWriteArrayList<Map<Integer,Direction>>();
	
	/**
	 * List qui contient les playerId des joueurs qui jouent dans une partie.
	 */
	List<Integer> player = new CopyOnWriteArrayList<Integer>();
	
	/**
     * Renvoie l'adresse IP de ce serveur, que les clients doivent
     * utiliser pour se connecter à lui.
     *
     * @return 
     * 		Une chaîne de caractères qui correspond à l'adresse IP du serveur.
     * 	 	En cas d'erreur,elle renvoit null.
     */
	public ErrorHandler errorHandler;
	ServerProfileHandler profileHandler;
	Boolean lancer = false;
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
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	@Override
	public void setProfileHandler(ServerProfileHandler profileHandler) {
		this.profileHandler = profileHandler;
	}
	/**
     * Permet de lancer un serveur pour que les clients puissent s'y connecter.
     * <br/>
     * Cette méthode doit être appelée par l'Interface Utilisateur lorsque
     * l'utilisateur décide de créer une partie réseau.
     */
	@Override
	public void launchServer() {
		new Thread(new Runnable() {
			public void run() {
				try {
					server = new ServerSocket(1117);
					lancer = true;
					while(lancer){
						Socket client = server.accept();
						System.out.println("Nouveau client");
						clients.add(client);
						new ServiceClient(Server.this,client);
					}
				} catch (IOException e) {
					errorHandler.displayError("Erreur lors du lancement du serveur");
				}
			}
		}).start();
	}

	@Override
	public void closeServer() {
		try {
			this.lancer = false;
			server.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Envoie la liste des profils des joueurs de la manche à tous les 
	 * clients connectés à ce serveur.
	 * 
	  @param profiles
	 * 		Liste des profils des joueurs participant à une partie.
	 */
	@Override
	public void sendProfiles(List<Profile> profiles) {
		List<Profile> tProfiles = new ArrayList<Profile>();
		for(Profile p : profiles)
			tProfiles.add(p);
		for(Profile p : profileClients)
			tProfiles.add(p);
		this.sendObject(tProfiles, clients);
	}

	/**
	 * Envoie la limite de points à atteindre pour gagner la partie,
	 * à tous les clients connectés à ce serveur.
	 *
	 * @param pointThreshold
	 * 		pointThreshold doit être un entier
	 * 		Limite de point courante de la partie.
	 */
	@Override
	public void sendPointThreshold(final int pointThreshold) {
		sendObject(pointThreshold, clients);
	}
	
	/**
	 * Permet au serveur d'envoyer des informations sur l'évolution de 
     * la manche en cours, à tous les clients connectés au serveur.
	 *
	 * @param board
	 * 		board est de type {@link Board}
	 * 		Etat courant de l'aire de jeu.
	 */
	@Override
	public void sendBoard(Board board) {
		directions.clear();
		player.clear();
		for(int i=0;i<board.snakes.length;i++){
			player.add(board.snakes[i].playerId);
		}
		Server.this.sendObject(board, clients);
	}

	/**
     * Permet au serveur de recevoir les commandes envoyés par les clients. La méthode
     * renvoie une map, associant à l'ID d'un joueur la dernière commande qu'il a
     * envoyée.
     *
     * @return 
     * 		Une map contenant les directions choisies par chaque joueur traité par
     * 		un client. Si un client ne renvoie rien, les valeurs manquantes doivent
     * 		être remplacées par des valeurs {@link Direction#NONE}.
     */
	@Override
	public Map<Integer, Direction> retrieveCommands() {
		Map< Integer, Direction> map = new HashMap<Integer, Direction>();
		boolean b = false;
			for(Map<Integer,Direction> direction : directions){
				b = true;
				for(Integer player: player){
					if(direction.containsKey(player))
							map.put(player, direction.get(player));
					else
						map.put(player, Direction.NONE);
				}
				directions.remove(direction);
				break;
			}
			if(b == false)
				for(Integer player: player)
					map.put(player, Direction.NONE);
		return map;
	}
	
	
	/**
	 * L'envoie d'un objet(aprés la sérialisation de ce dérnier) entrer en paramétre 
	 * vers les clients connectés au serveur
	 * @param o 
	 * 			object de type {@link Object} à envoyer
	 * @param clients 
	 * 			la liste des clients  de type {@link Socket}
	 */
	private void sendObject(Object o,List<Socket> clients){
		if(lancer)
			for(Socket client : clients){
				try {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ObjectOutputStream oos;
					oos = new ObjectOutputStream(bos);
					oos.flush();
					oos.writeObject(o);
					oos.flush();
					oos.close();
					bos.close();
					
					DataOutputStream dos = new DataOutputStream(client.getOutputStream());  
					byte [] data = bos.toByteArray();
					dos.writeInt(data.length);
				    dos.write(data);
				    dos.flush();
				    
				} catch (IOException e) {
					sendObject(o, clients);
				}
			}
	}
	
}
