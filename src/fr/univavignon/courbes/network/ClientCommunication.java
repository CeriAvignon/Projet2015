package fr.univavignon.courbes.network;

import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.ErrorHandler;

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
public interface ClientCommunication
{	
	/**
     * Renvoie l'adresse IP du serveur auquel le client se connecte
     * (à préciser <i>avant</i> de se connecter, bien sûr).
     *
     * @return 
     * 		Une chaîne de caractères qui correspond à l'adresse IP du serveur.
     */
	public String getIp();

	/**
     * Modifie l'adresse IP du serveur auquel le client va se connecter.
     * <br/>
     * Cette valeur est à modifier avant d'utiliser {@link #launchClient}.
     *
     * @param ip
     * 		La nouvelle IP du serveur.
     */
	public void setIp(String ip);

	/**
     * Renvoie le port du serveur auquel le client se connecte
     * (à préciser <i>avant</i> de se connecter, bien sûr).
     *
     * @return 
     * 		Un entier qui correspond au port du serveur.
     */
	public int getPort();

	/**
     * Modifie le port du serveur auquel le client va se connecter.
     * <br/>
     * Cette valeur est à modifier avant d'utiliser {@link #launchClient}.
     * 
     * @param port
     * 		Le nouveau port du serveur.
     */
	public void setPort(int port);
	
	/**
     * Permet à l'Interface Utilisateur d'indiquer au Moteur Réseau l'objet
     * à utiliser pour prévenir d'une erreur lors de l'exécution. 
     * <br/>
     * Cette méthode doit être invoquée avant le lancement du client.
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
     * Cette méthode doit être invoquée avant le lancement du client.
     * 
     * @param profileHandler
     * 		Un objet implémentant l'interface {@code ClientProfileHandler}.
     */
	public void setProfileHandler(ClientProfileHandler profileHandler);

	/**
     * Permet au client de se connecter au serveur dont on a préalablement
     * configuré l'adresse IP et le port. 
     * <br/>
     * Cette méthode doit être appelée par l'Interface Utilisateur lorsque
     * l'utilisateur décide de se connecter à une partie réseau existante.
     */
	public void launchClient();

 	/**
     * Permet à un client de clore sa connexion avec le serveur.
     */
	public void closeClient();
	
	/**
-	 * Envoie au serveur le profil d'un joueur désirant participer à la partie
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
	public boolean addProfile(Profile profile);
	
	/**
	 * Envoie au serveur le profil d'un joueur inscrit mais ne désirant plus participer 
	 * à la partie en cours de configuration. Si le joueur n'est pas inscrit à la partie,
	 * alors rien ne se passe (pas d'erreur).
	 *   
	 * @param profile
	 * 		Profil du joueur à retirer de la partie.
	 */
	public void removeProfile(Profile profile);
	
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
	public Integer retrievePointThreshold();

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
	public Board retrieveBoard();

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
	public void sendCommands(Map<Integer,Direction> commands);

//	/**
//     * Permet au client de récupérer un message textuel envoyé par le serveur
//     * auquel il est connecté.
//     * <br/>
//     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
//     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
//     * avant de pouvoir continuer son exécution. La transmission doit se faire en
//     * parallèle de l'exécution du jeu. 
//     *
//     * @return
//     * 		Contient le message envoyé par le serveur, ou {@code null} si aucun message
//     * 		n'a été envoyé.
//     */
//	public String retrieveText();
//
//	/**
//     * Permet au client d'envoyer un message textuel au serveur auquel il est 
//     * connecté.
//     * <br/>
//     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante : 
//     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée 
//     * avant de pouvoir continuer son exécution. La transmission doit se faire en
//     * parallèle de l'exécution du jeu. 
//     *
//     * @param message 
//     * 		Le message textuel à envoyer au serveur.
//     */
//	public void sendText(String message);
}
