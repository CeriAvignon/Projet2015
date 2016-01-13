package fr.univavignon.courbes.network;

import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerProfileHandler;

/**
 * Ensemble de méthodes permettant à l'Interface Utilisateur côté serveur
 * de communiquer avec l'Interface Utilisateur côté client, via le Moteur
 * Réseau.
 * <br/>
 * Chaque binôme de la composante Moteur Réseau doit définir une classe
 * implémentant cette interface, qui sera instanciée par l'Interface Utilisateur.
 * <br/>
 * La communication réseau doit être non-bloquante pour l'Interface Utilisateur.
 * Cela signifie que le Moteur Réseau doit mettre en place un système de buffering.
 * Autrement dit, quand il reçoit des données provenant des clients, il les garde
 * en mémoire jusqu'à ce que l'Interface Utilisateur le sollicite via l'une des
 * méthodes de type {@code retrieveXxxxx} pour obtenir cette information.
 * Inversement, quand l'Interface Utilisateur invoque une méthode de type {@code sendXxxxx},
 * l'expédition vers les clients doit se faire dans un thread dédié, afin de ne
 * pas bloquer l'exécution du jeu.
 */
public interface ServerCommunication
{	
	/**
     * Renvoie l'adresse IP de ce serveur, que les clients doivent
     * utiliser pour se connecter à lui. Cette valeur n'est pas 
     * modifiable, elle dépend directement du système.
     *
     * @return 
     * 		Une chaîne de caractères qui correspond à l'adresse IP du serveur.
     */
	public String getIp();
	
	/**
     * Renvoie le port utilisé par le serveur pour accepter les connexions
     * de la part des clients. (à préciser <i>avant</i> de se connecter, 
     * bien sûr).
     *
     * @return 
     * 		Un entier qui correspond au port du serveur.
     */
	public int getPort();

	/**
     * Modifie le port utilisé par le serveur pour accepter les connexions
     * de la part des clients. 
     * <br/>
     * Cette valeur est à modifier avant d'utiliser {@link #launchServer}.
     * 
     * @param port
     * 		Le nouveau port du serveur.
     */
	public void setPort(int port);
	
	/**
     * Permet à l'Interface Utilisateur d'indiquer au Moteur Réseau l'objet
     * à utiliser pour prévenir d'une erreur lors de l'exécution.
     * <br/>
     * Cette méthode doit être invoquée avant le lancement du serveur.
     * 
     * @param errorHandler
     * 		Un objet implémentant l'interface {@code ErrorHandler}.
     */
	public void setErrorHandler(ErrorHandler errorHandler);
	
	/**
     * Permet à l'Interface Utilisateur d'indiquer au Moteur Réseau l'objet
     * à utiliser pour prévenir d'une modification des joueurs lors de la
     * configuration d'une partie.
     * <br/>
     * Cette méthode doit être invoquée avant le lancement du serveur.
     * 
     * @param profileHandler
     * 		Un objet implémentant l'interface {@code ServerProfileHandler}.
     */
	public void setProfileHandler(ServerProfileHandler profileHandler);
	
	/**
     * Permet de créer un serveur pour que les clients puissent s'y connecter.
     * <br/>
     * Cette méthode doit être appelée par l'Interface Utilisateur lorsque
     * l'utilisateur décide de créer une partie réseau.
     */
	public void launchServer();

	/**
     * Permet de stopper le serveur et ainsi déconnecter tous les clients.
     * <br/>
     * Cette méthode doit être appelée par l'Interface Utilisateur lorsque
     * l'utilisateur décide d'arrêter une partie réseau en cours.
     */
	public void closeServer();

	/**
	 * Envoie la liste des profils des joueurs de la manche à tous les 
	 * clients connectés à ce serveur.
	 * <br/>
	 * Cette méthode est invoquée par l'Interface Utilisateur de manière
	 * à ce que le serveur transmette au client l'identité des joueurs 
	 * participant à une partie.
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu. 
	 * 
	 * @param profiles
	 * 		Liste des profils des joueurs participant à une partie.
	 */
	public void sendProfiles(List<Profile> profiles);
	
	/**
	 * Envoie la limite de points à atteindre pour gagner la partie,
	 * à tous les clients connectés à ce serveur.
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
	 * @param pointThreshold
	 * 		Limite de point courante de la partie.
	 */
	public void sendPointThreshold(int pointThreshold);

	/**
     * Permet au serveur d'envoyer des informations sur l'évolution de 
     * la manche en cours, à tous les clients connectés au serveur.
     * <br/>
     * Cette méthode est appelée par l'Interface Utilisateur à
     * chaque itération d'une manche.
      * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu. 
    * 
     * @param board
     * 		Etat courant de l'aire de jeu.
     */
	public void sendBoard(Board board);

	/**
     * Permet au serveur de recevoir les commandes envoyés par les clients. La méthode
     * renvoie une map, associant à l'ID d'un joueur la dernière commande qu'il a
     * envoyée. Bien sûr, les joueurs locaux au serveur ne sont pas gérés par des clients,
     * et leur ID n'apparaît donc pas dans cette map.
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu. 
     *
     * @return 
     * 		Une map contenant les directions choisies par chaque joueur traité par
     * 		un client. Si un client ne renvoie rien, les valeurs manquantes doivent
     * 		être remplacées par des valeurs {@link Direction#NONE}.
     */
	public Map<Integer,Direction> retrieveCommands();

//	/**
//     * Permet au serveur d'envoyer un message textuel à tous les clients qui lui sont
//     * connectés.
//     * <br/>
//     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
//     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
//     * avant de pouvoir continuer son exécution. La transmission doit se faire en
//     * parallèle de l'exécution du jeu. 
//     *
//     * @param message
//     * 		Contient le message destiné aux clients.
//     */
//	public void sendText(String message);
//	
//	/**
//     * Permet au serveur de recevoir des messages textuels provenant de ses clients.
//     * La méthode renvoie un tableau dont chaque valeur correspond à un client. En
//     * l'absence de message envoyé par un client, sa valeur est {@code null}.
//     * <br/>
//     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
//     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
//     * avant de pouvoir continuer son exécution. La transmission doit se faire en
//     * parallèle de l'exécution du jeu. 
//     *
//     * @return 
//     * 		Un tableau de chaînes de caractères, chacune envoyée par un client donné.
//     * 		En l'absence de message, la valeur associée à un client est {@code null}.
//     */
//	public String[] retrieveText();
}
