package fr.univavignon.courbes.network;

import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;

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
 * <br/>
 * Il faut noter que cette classe doit gérer plusieurs clients, et que chacun d'entre
 * eux est capable d'héberger plusieurs joueurs. L'Interface Utilisateur n'a pas accès
 * à ces informations : pour elle, les joueurs sont simplement numérotés, qu'ils soient
 * locaux ou distants. L'implémentation de cette interface doit donc être capable de déterminer
 * par quel client un joueur donné est hébergé, et quel est son numéro sur ce client-là.
 * Autrement dit, il faut pouvoir faire la conversion entre un numéro global attribué sur
 * le serveur, et un numéro local attribué sur un client.
 */
public interface ServerCommunication
{	
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
	public void shutdownServer();

	/**
	 * Envoie la liste des joueurs de la manche à tous les clients connectés
	 * à ce serveur.
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
	 * @param players
	 * 		Liste des joueurs participant à une partie.
	 */
	public void sendPlayers(List<Profile> players);
	
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
     * Permet au serveur d'envoyer un message textuel à tous les clients connectés
     * au serveur.
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu. 
     *
     * @param message
     * 		Contient le message destiné aux clients.
     */
	public void sendText(String message);

	/**
     * Permet au serveur de recevoir les commandes envoyés par les clients. La méthode
     * renvoie une map, associant à l'ID d'un joueur la dernière commande qu'il a
     * envoyée. Bien sûr, les joueurs locaux au serveur ne sont pas gérés par des clients,
     * et leur ID n'apparait donc pas dans cette map.
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
	
	/**
     * Permet au serveur de recevoir des messages textuels provenant de ses clients.
     * La méthode renvoie un tableau dont chaque valeur correspond à un client. En
     * l'absence de message envoyé par un client, sa valeur est {@code null}.
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu. 
     *
     * @return 
     * 		Un tableau de chaînes de caractères, chacune envoyée par un client donné.
     * 		En l'absence de message, la valeur associée à un client est {@code null}.
     */
	public String[] retrieveText();
}
