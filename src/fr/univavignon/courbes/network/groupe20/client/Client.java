package fr.univavignon.courbes.network.groupe20.client;
import java.awt.Window.Type;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerProfileHandler;
import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.groupe20.ProfileReponse;
public class Client implements ClientCommunication {
	
	Socket client;
	
	/**
	 *L'adresse ip du serveur de type {@link String} 
	 */
	private String ip;
	

	/**
	 *Le port du serveur de type int 
	 */
	private int port;
	
	/**
	 * Collection de type {@link ProfileReponse} des profils qui sont accéptés par le serveur , 
	 * c'est à dire , qui ont l'action = <code>true</code>
	 **/
	List<ProfileReponse> addProfil = new CopyOnWriteArrayList<ProfileReponse>();
	 
	/**
	 *Score de la manche reçu depuis le serveur
	 **/
	 Integer point = null,pointRetour = null;

	/**
	  * Etat courant de l'aire de jeu reçu depuis le serveur
	  */
	 Board board = null;
	 
	 /**
	   * Instance de la classe de type {@link ErrorHandler} implémenté par IU
	   */
	 ErrorHandler errorHandler; 
	 

	/**
	  * Instance de la classe de type {@link ClientProfileHandler} implémenté par IU
	  */
	 ClientProfileHandler profileHandler;
	 
	 /**
	  * Etat du client
	  */
	 Boolean etatClient;
	 
	 /**
	   * Renvoie l'adresse IP du serveur auquel le client se connecte
	   * (à préciser <i>avant</i> de se connecter, bien sûr).
	   *
	   * @return 
	   * 		Une chaîne de caractères qui correspond à l'adresse IP du serveur.
	   */
	@Override
	public String getIp() {
		return ip;
	}
	
	/**
     * Modifie l'adresse IP du serveur auquel le client va se connecter.
     * <br/>
     * Cette valeur est à modifier avant d'utiliser {@link #launchClient}.
     *
     * @param ip
     * 		La nouvelle IP du serveur.
     */
	@Override
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
     * Renvoie le port du serveur auquel le client se connecte
     * (à préciser <i>avant</i> de se connecter, bien sûr).
     *
     * @return 
     * 		Un entier qui correspond au port du serveur.
     */
	@Override
	public int getPort() {
		return this.port;
	}
	
	/**
     * Modifie le port du serveur auquel le client va se connecter.
     * <br/>
     * Cette valeur est à modifier avant d'utiliser {@link #launchClient}.
     * 
     * @param port
     * 		Le nouveau port du serveur.
     */
	@Override
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
     * Permet à l'Interface Utilisateur d'indiquer au Moteur Réseau l'objet
     * à utiliser pour prévenir d'une erreur lors de l'exécution. 
     * <br/>
     * Cette méthode doit être invoquée avant le lancement du client.
     * 
     * @param errorHandler
     * 		Un objet implémentant l'interface {@code ErrorHandler}.
     */
	@Override
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
	
	/**
     * Permet à l'Interface Utilisateur d'indiquer au Moteur Réseau l'objet
     * à utiliser pour prévenir d'une modification des joueurs lors de la
     * configuration d'une partie. 
     * <br/>
     * Cette méthode doit être invoquée avant le lancement du client.
     * 
     * @param profileHandler
     * 		Un objet implémentant l'interface {@code ClientProfileHandler}.
     */
	@Override
	public void setProfileHandler(ClientProfileHandler profileHandler) {
		this.profileHandler = profileHandler;
	}
	
	/**
     * Permet au client de se connecter au serveur dont on a préalablement
     * configuré l'adresse IP et le port. 
     * <br/>
     * Cette méthode doit être appelée par l'Interface Utilisateur lorsque
     * l'utilisateur décide de se connecter à une partie réseau existante.
     */
	@Override
	public void launchClient() {
		try {
			etatClient = true;
			client = new Socket(this.ip, this.port);
			new ServiceServer(this);
		} catch (IOException e) {
			this.errorHandler.displayError("Aucun serveur avec ces informations");
		}
	}
	
	/**
     * Permet à un client de clore sa connexion avec le serveur.
     */
	@Override
	public void closeClient() {
		etatClient = false;
		this.closeClient();
	}
	
	/**
	- * Envoie au serveur le profil d'un joueur désirant participer à la partie
	  * en cours de configuration. Si plusieurs joueurs utilisent le même client,
	  * alors la méthode doit être appelée plusieurs fois successivement. Chaque
	  * joueur peut être refusé par le serveur, par exemple si la partie ne peut
	  * pas accueillir plus de joueurs.
	  *   
	  * @param profile
	  * 		Profil du joueur à ajouter à la partie.
	  * @return
	  * 		Un booléen indiquant si le profil a été accepté ({@code true}) ou 
	  * 		rejeté ({@code false}). 
	  */
	@Override
	public boolean addProfile(Profile profile) {
		boolean b = false;
		boolean ret = false;
		//Envoie le numeros 1 pour faire une demande d'ajout du profil
		this.sendObject(profile, (byte)1);
		while(b == false)
			for(ProfileReponse p :addProfil)
				if(p.getProfile().profileId == profile.profileId){
					ret = p.isAction();
					addProfil.remove(p);
					b = true;
					break;
				}
		return ret;
	}
	
	/**
	 * Envoie au serveur le profil d'un joueur inscrit mais ne désirant plus participer 
	 * à la partie en cours de configuration. Si le joueur n'est pas inscrit à la partie,
	 * alors rien ne se passe (pas d'erreur).
	 *   
	 * @param profile
	 * 		Profil du joueur à retirer de la partie.
	 */
	@Override
	public void removeProfile(Profile profile) {
		if(etatClient)
			//Envoie le numeros 2 pour supprimer le profil
			this.sendObject(profile, (byte)2);
	}
	
	/**
	 * Récupère la limite de points à atteindre pour gagner la partie,
	 * limite envoyée par le serveur auquel ce client est connecté.
	 * <br/>
	 * Cette méthode est invoquée par l'Interface Utilisateur à chaque
	 * début de manche. En effet, la limite peut changer à chaque 
	 * manche en fonction du nombre de points des joueurs (pour gagner,
	 * il faut avoir un certain nombre de points d'avance sur le 2ème). 
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu. 
	 * 
	 * @return pointThreshold
	 * 		Limite de point courante de la partie, ou {@code null} si aucune
	 * 		valeur n'a été envoyée.
	 */
	@Override
	public Integer retrievePointThreshold() {
		this.pointRetour = this.point;
		this.point = null;
		return pointRetour;
	}
	
	/**
     * Permet au client de récupérer des informations sur l'évolution de 
     * la manche en cours, envoyées par le serveur auquel il est connecté.
     * <br/>
     * Cette méthode est appelée par l'Interface Utilisateur à
     * chaque itération d'une manche.
      * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu. 
    * 
     * @return board
     * 		Etat courant de l'aire de jeu, ou {@code null} si aucune mise à jour
     * 		n'a été envoyée.
     */
	@Override
	public Board retrieveBoard() {
		Board nBoard = board;
		board = null;
		return nBoard;
	}
	
	/**
     * Permet au client d'envoyer les commandes générées par les joueurs qu'il gère.
     * Ces commandes sont passées sous forme de map: l'entier correspond à l'ID du joueur
     * <i>sur le serveur</i>, pour la manche en cours, et la direction correspond à la
     * commande générée par le joueur. Si un joueur n'a pas généré de commande, alors la 
     * valeur associée doit être {@link Direction#NONE}.
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu. 
     *
     * @param commands 
     * 		Une liste contenant les directions choisies par chaque joueur local au client.
     */
	@Override
	public void sendCommands(Map<Integer, Direction> commands) {
		if(etatClient)
			this.sendObject(commands);
    }
	
	/**
	 * L'envoie d'un objet(aprés la sérialisation de ce dernier) entré en paramétre 
	 * vers les clients connectés au serveur
	 * @param o 
	 * 			object de type {@link Object} à envoyer
	 */
	private void sendObject(Object o){
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
				dos.writeByte(0);
			    dos.write(data);
			    dos.flush();
			} catch (IOException e) {}
		
	}
	
	/**
	 * L'envoie d'un objet(aprés la sérialisation de ce dernier) entré en paramétre 
	 * vers un client entrer en paramétre
	 * 
	 * @param o
	 * 			object de type {@link Object} à envoyer
	 * @param b
	 * 			b de type {@link Byte}
	 */
	private void sendObject(Object o,byte b){

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
				dos.writeByte(b);
			    dos.write(data);
			    dos.flush();
			    
			} catch (IOException e) {}
	}
	
	//Getter and setter
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}	
	
	
	
}


