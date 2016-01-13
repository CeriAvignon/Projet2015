package fr.univavignon.courbes.network.groupe22;
import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import java.net.*;
import java.util.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Ensemble de méthodes permettant à l'Interface Utilisateur côté client
 * de communiquer avec l'Interface Utilisateur côté serveur, via le Moteur
 * Réseau.
 * <br/>
 * Chaque binôme de la composante Moteur Réseau doit définir une classe
 * implémentant cette interface, qui sera instanciée par l'Interface Utilisateur.
 * <br/>
 * La communication réseau doit être non-bloquante pour l'Interface Utilisateur.
 * Cela signifie que le Moteur Réseau doit mettre en place un système de buffering.
 * Autrement dit, quand il reçoit des données provenant du serveur, il les garde
 * en mémoire jusqu'à ce que l'Interface Utilisateur le sollicite via l'une des
 * méthodes de type {@code retrieveXxxxx} pour obtenir cette information.
 * Inversement, quand l'Interface Utilisateur invoque une méthode de type {@code sendXxxxx},
 * l'expédition vers le serveur doit se faire dans un thread dédié, afin de ne
 * pas bloquer l'exécution du jeu.
 */
public class ClientCom implements ClientCommunication
{
	/**
	* L'ip du serveur
	*/
    private String ipServer;
    /**
	* Le port du serveur
	*/
    private int portServer;
    /**
	* Le socket du client connecté au serveur
	*/
    private Socket connexion=null;
    /**
	* Le plateau de jeu
	*/
    private Board board=null;
    /**
	* Le nombre de points à atteindre
	*/
    private int pointThreshold=0;
    /**
	* Erreur de profil
	*/
    private ClientProfileHandler profileHandler;
    /**
	* Erreur à envoyer à l'interface utilisateur
	*/
    private ErrorHandler errorHandler;
  /**
     * Renvoie l'adresse IP du serveur auquel le client se connecte.
     *
     * @return
     *         Une chaîne de caractères qui correspond à l'adresse IP du serveur.
     */
    @Override
    public String getIp() {
        return ipServer;
    }

    /**
     * Modifie l'adresse IP du serveur auquel le client va se connecter.
     * Cette valeur est à modifier avant d'utiliser {@link #launchClient}.
     *
     * @param ip
     *         La nouvelle IP du serveur.
     */
    @Override
    public void setIp(String ip) {
        ipServer=ip;

    }

    /**
     * Renvoie le port du serveur auquel le client se connecte.
     *
     * @return
     *         Un entier qui correspond au port du serveur.
     */
    @Override
    public int getPort() {
        return portServer;
    }

    /**
     * Modifie le port du serveur auquel le client va se connecter.
     * Cette valeur est à modifier avant d'utiliser {@link #launchClient}.
     *
     * @param port
     *         Le nouveau port du serveur.
     */
    @Override
    public void setPort(int port) {
        portServer=port;
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
             connexion = new Socket(ipServer, portServer);
        }
        catch (UnknownHostException e) {
             e.printStackTrace();
        }
        catch (IOException e) {
             e.printStackTrace();
        }
    }

     /**
     * Permet à un client de clore sa connexion avec le serveur.
     */
    @Override
    public void closeClient() {
        try {
            connexion.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
	    * Envoie au serveur le profil d'un joueur désirant participer à la partie
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
        return sendObject(profile);
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
      public void removeProfile(final Profile profile) {
        Thread sendProfile = new Thread(new Runnable(){
			       @Override
			       public void run(){
				              sendObject(profile);
			       }
		    });
		    sendProfile.start();
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
     *         Limite de point courante de la partie, ou {@code null} si aucune
     *         valeur n'a été envoyée.
     */
    @Override
    public Integer retrievePointThreshold() {
      return this.pointThreshold;
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
     *         Etat courant de l'aire de jeu, ou {@code null} si aucune mise à jour
     *         n'a été envoyée.
     */
      @Override
      public Board retrieveBoard() {
    	  Thread retrieveBoard = new Thread(new Runnable(){
  			@Override
  			public void run(){
  				try{
  					ObjectInputStream ois = new ObjectInputStream(connexion.getInputStream());
  					Board b = (Board)ois.readObject();
  					board = b;
  				} catch(Exception e){
  					board = null;
  					e.printStackTrace();
  				}
  			}
  		});
  		retrieveBoard.start();
  		return board;
    }

    /**
     * Permet au client d'envoyer les commandes générées par les joueurs qu'il gère.
     * Ces commandes sont passées sous forme de map: l'entier correspond à l'ID du joueur
     * <i>sur le serveur</i>, pour la manche en cours, et la direction correspond à la
     * commande générée par le joueur. Si un joueur n'a pas généré de commande, alors la
     * valeur associée doit être {@link Direction#NONE}.
     *
     * @param commands
     *         Une liste contenant les directions choisies par chaque joueur local au client.
     */
    @Override
    public void sendCommands(final Map<Integer,Direction> commands) {
      Thread send = new Thread(new Runnable(){
			     @Override
			     public void run(){
				         sendObject(commands);
		       }
		  });
		  send.start();
		  return;
    }

    /**
     * Permet d'envoyer un objet
     * <br/>
     *
     * @param object
     * 		   Objet à envoyer.
     *
     * @return
     *         booléen retournant true si l'envoi à fonctionner et false sinon.
     */
     private synchronized boolean sendObject(Object object) {
		     try {
		    	 ObjectOutputStream oos = new ObjectOutputStream(connexion.getOutputStream());
				 oos.writeObject(object);
				 oos.flush();
		     } catch (Exception e) {
			        e.printStackTrace();
              return false;
		     }
         return true;
	    }

      /*
    public String retrieveText() {
      try {
			    String message = "";
			    BufferedReader messageIn = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
			    message = messageIn.readLine();
			    return message;
		  }
      catch (IOException e) {
			     e.printStackTrace();
		  }
		return null;
    }

    /**
     * Permet au client d'envoyer un message textuel au serveur auquel il est
     * connecté.
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante :
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu.
     *
     * @param message
     *         Le message textuel à envoyer au serveur.
     */
    /*public void sendText(String message) {
      try {
			    PrintWriter messageOut = new PrintWriter(connexion.getOutputStream(), true);
    		  messageOut.println(message);
		  }
      catch (IOException e) {
			     e.printStackTrace();
		  }
    }*/
}
