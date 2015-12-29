package fr.univavignon.courbes.network.groupe22;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.DatagramSocket;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.ServerCommunication;

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
public class ServerCom implements ServerCommunication
{
  /**
 * L'ip du serveur
 */
private String ip;
  /**
 * Le port sur lequel les clients se connectent
 */
private int port = 2345;
  /**
 * L'adresse de l'hôte
 */
private String host = "127.0.0.1";
  /**
 * Le socket du serveur
 */
private ServerSocket server = null;
  /**
 * Permet de couper la connexion
 */
private boolean isRunning = true;
  /**
 * Contient tous les clients connectés
 */
public static Socket tableauSocketClients[] = new Socket[100];
  /**
 * Le nombre de clients connectés
 */
public static int nbClient = 0;


  /**
       * Renvoie l'adresse IP du serveur auquel le client se connecte
       * (à préciser <i>avant</i> de se connecter, bien sûr).
       *
       * @return
       *     Une chaîne de caractères qui correspond à l'adresse IP du serveur.
       */
	@Override
    public String getIp()
    {
      return this.ip;
    }

  /**
    * Retourne la première adresse IP active trouvée sur le serveur.
    * @return adressIp
    *    Première adresse libre trouvée
    * @throws SocketException
    * 	 Exception levée si pas d'interfaces
  */


  private String findAdress() throws SocketException
  {
    Enumeration<NetworkInterface> interfaces;
    interfaces = NetworkInterface.getNetworkInterfaces();

    while (interfaces.hasMoreElements()) {
      NetworkInterface interfaceTest;
      interfaceTest = interfaces.nextElement();

      if (!interfaceTest.isLoopback() && interfaceTest.isUp()) {
        Enumeration<InetAddress> addresses = interfaceTest.getInetAddresses();
        while (addresses.hasMoreElements()) {
          InetAddress address = addresses.nextElement();
          String addressIp = address.getHostAddress();
          if (addressIp.startsWith("192.168.")) {
            return addressIp;
          }

        }
      }
    }

    return "";
  }

    /**
       * Renvoie le port du serveur auquel le client se connecte
       * (à préciser <i>avant</i> de se connecter, bien sûr).
       *
       * @return
       *     Un entier qui correspond au port du serveur.
       */
    @Override
	public int getPort()
    {
      return this.port;
    }


   /**
    * Modifie le port utilisé par le serveur pour accepter les connexions
    * de la part des clients. Cette valeur est à modifier avant d'utiliser
    * {@link #launchServer}.
    *
    * @param port
    * 		Le nouveau port du serveur.
    */
  @Override
public void setPort(int port)
  {
    this.port = port;
  }

  /**
   * Renvoie le premier port ouvert trouvé sur le serveur
   * @return
        Un entier correspondant au premier port du serveur ouvert
   */
  private int findPort()
  {
    try {

        ServerSocket socket = new ServerSocket(port);
        socket.close();

      } catch (IOException e) {

         for(int tmpPort = 1; tmpPort <= 65535; tmpPort++){
           try {
             ServerSocket socket = new ServerSocket(tmpPort);
             socket.close();
             return tmpPort;
           } catch (IOException ioe) {
             ; // Si le port est utilisé, on passe au suivant
           }
         }

      }
    return port;

  }


  /**
     * Permet de créer un serveur pour que les clients puissent s'y connecter.
     * <br/>
     * Cette méthode doit être appelée par l'Interface Utilisateur lorsque
     * l'utilisateur décide de créer une partie réseau.
     */


  @Override
public void launchServer()
  {
    //Créer le serveur

	try
	{
    //Récuperer l'adresse IP du serveur
    host = findAdress();
	} catch (SocketException e) {
		e.printStackTrace();
	}
  //Récupérer le port du serveur
    port = findPort();

    try {
         server = new ServerSocket(port, 100, InetAddress.getByName(host));

    } catch (UnknownHostException e) {
         e.printStackTrace();

    } catch (IOException e) {
         e.printStackTrace();
      }
    //Ouvrir le serveur
     Thread t = new Thread(new Runnable(){

  @Override
	public void run(){

        while(isRunning == true){

           try {

              //On attend une connexion d'un client

              Socket client = server.accept();
              tableauSocketClients[nbClient] = client;
              nbClient++;

              Thread tClient = new Thread(new ClientProcessorTCP(client));

           } catch (IOException e) {
              e.printStackTrace();
           }
        }

          try {

             server.close();

          } catch (IOException e) {

           e.printStackTrace();
           server = null;
          }

     }

    });

    t.start();
  }

  /**
     * Permet de stopper le serveur et ainsi déconnecter tous les clients.
     * <br/>
     * Cette méthode doit être appelée par l'Interface Utilisateur lorsque
     * l'utilisateur décide d'arrêter une partie réseau en cours.
     */
  @Override
public void closeServer()
  {
    this.isRunning = false;
  }


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
   *     Limite de point courante de la partie.
   */
  @Override
public void sendPointThreshold(int pointThreshold)
  {

    return;
  }

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
     *     Etat courant de l'aire de jeu.
     */
  @Override
public void sendBoard(Board board)
  {
    return;
  }

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
     *     Une map contenant les directions choisies par chaque joueur traité par
     *     un client. Si un client ne renvoie rien, les valeurs manquantes doivent
     *     être remplacées par des valeurs {@link Direction#NONE}.
     */
  @Override
public Map<Integer,Direction> retrieveCommands()
  {
    return null;
  }

  /**
     * Permet au serveur d'envoyer un message textuel à tous les clients qui lui sont
     * connectés.
     * <br/>
     * <b>Attention :</b> il est important que cette méthode ne soit pas bloquante :
     * l'Interface Utilisateur n'a pas à attendre que la transmission soit réalisée
     * avant de pouvoir continuer son exécution. La transmission doit se faire en
     * parallèle de l'exécution du jeu.
     *
     * @param message
     *     Contient le message destiné aux clients.
     */
  @Override
public void sendText(String message)
  {
    return;
  }

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
     *     Un tableau de chaînes de caractères, chacune envoyée par un client donné.
     *     En l'absence de message, la valeur associée à un client est {@code null}.
     */
  @Override
public String[] retrieveText()
  {
    return null;
  }

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
   *     Liste des profils des joueurs participant à une partie.
   */

@Override
public void sendProfiles(List<Profile> profiles) {
	// TODO Auto-generated method stub

}

/**
 * Récupère les profils envoyés par les clients, représentant des joueurs qui désirent
 * participer à la partie en cours de configuration. Le serveur peut refuser
 * certains joueurs, par exemple si la partie est complète (plus de place libre).
 * <br/>
 * À noter que La liste doit contenir les profils <i>dans l'ordre où ils ont été reçus</i>
 * par le Moteur Réseau, afin que l'Interface Utilisateur puisse déterminer lesquels refuser
 * le cas échéant. Le moteur réseau doit également garder trace de quel joueur correspond
 * à quel client.
 *
 * @return
 * 		La liste des profils reçus par le Moteur Réseau (peut être vide si aucun n'a été
 * 		reçu depuis la dernière fois que la méthode a été invoquée).
 */

@Override
public List<Profile> retrieveProfiles() {
	// TODO Auto-generated method stub
	return null;
}
}
