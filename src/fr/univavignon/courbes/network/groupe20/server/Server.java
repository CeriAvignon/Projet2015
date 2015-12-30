package fr.univavignon.courbes.network.groupe20.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

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
					while(true){
						Socket client = server.accept();
						System.out.println("Nouveau client");
						clients.add(client);
						new ServiceClient(Server.this,client);
					}
				} catch (IOException e) {e.printStackTrace();}
			}
		}).start();
	}

	@Override
	public void closeServer() {
		// TODO Auto-generated method stub

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
	
	
	
	private void sendObject(Object o,List<Socket> clients){
		for(Socket client : clients)
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos;
				oos = new ObjectOutputStream(bos);
				oos.writeObject(o);
				oos.flush();
				oos.close();
				bos.close();
				
				DataOutputStream dos = new DataOutputStream(client.getOutputStream());  
				byte [] data = bos.toByteArray();
				dos.writeInt(data.length);
			    dos.write(data);
			    dos.flush();
			} catch (IOException e) {e.printStackTrace();}
		
	}
	public static void main(String[] args) {
		Server s = new Server();
		s.launchServer();
		//Test de l'ajout d'un profil
		while(true)
			if(s.profileClients.size() == 3){
				System.out.println(".");
				Profile p = new Profile();
				p.profileId = 15;
				List<Profile> pp = new ArrayList<Profile>();
				pp.add(p);
				s.sendProfiles(pp);
				break;
			}
	}

}
