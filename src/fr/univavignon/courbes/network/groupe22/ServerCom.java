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

import java.util.*;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.network.ServerCommunication;
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
	* Message d'erreur pour l'interface utilisateur
	*/
	private ErrorHandler errorHandler;
	/**
	* Erreur liée au profil
	*/
	private ServerProfileHandler profileHandler;
	/**
	* Liste de socket à envoyer
	*/
	private ArrayList<Socket> socketArray = new ArrayList<Socket>();
	/**
	* Contient les commandes envoyées par le client
	*/
	private Map<Integer, Direction> commands= null;

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
		 * Retourne la première adresse IP actie trouvée sur le serveur.
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
		* Un entier correspondant au premier port du serveur ouvert
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
	     * Permet à l'Interface Utilisateur d'indiquer au Moteur Réseau l'objet
	     * à utiliser pour prévenir d'une erreur lors de l'exécution.
	     * <br/>
	     * Cette méthode doit être invoquée avant le lancement du serveur.
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
	     * Cette méthode doit être invoquée avant le lancement du serveur.
	     * 
	     * @param profileHandler
	     * 		Un objet implémentant l'interface {@code ServerProfileHandler}.
	     */
		@Override
		public void setProfileHandler(ServerProfileHandler profileHandler) {
			this.profileHandler = profileHandler;
		}

		/**
	 	* Permet de créer un serveur pour que les clients puissent s'y connecter.
	 	* <br/>
	 	* Cette méthode doit être appelée par l'Interface Utilisateur lorsque
	 	* l'utilisateur décide de créer une partie réseau.
	 	*/


		@Override
		public void launchServer() {
			//Créer le serveur

			try {
				host = findAdress();
			} catch (SocketException e) {
				e.printStackTrace();
			}
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
		@Override
		public void sendProfiles(final List<Profile> profiles) {
			Thread send = new Thread(new Runnable(){
		 			@Override
		 			public void run(){
			 			sendObject(profiles);
		 			}
	 		});
	 		send.start();
	 		return;
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
	 	* 		Limite de point courante de la partie.
	 	*/
		
		@Override
		public void sendPointThreshold(final int pointThreshold) {
				Thread send = new Thread(new Runnable(){
					@Override
					public void run(){
							sendObject(pointThreshold);
					}
				});
				send.start();
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
    * 		Etat courant de l'aire de jeu.
    */
		@Override
		public void sendBoard(final Board board) {
			Thread send = new Thread(new Runnable(){
				@Override
				public void run(){
						sendObject(board);
				}
			});
			send.start();
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
    * 		Une map contenant les directions choisies par chaque joueur traité par
    * 		un client. Si un client ne renvoie rien, les valeurs manquantes doivent
    * 		être remplacées par des valeurs {@link Direction#NONE}.
    */
	@Override
	public Map<Integer,Direction> retrieveCommands() {
		return this.commands;
	}

	/**
     * Permet au serveur d'envoyer un objet au client.
     *
     * @param object
     * 		Objet à envoyer.
     */

			private synchronized void sendObject(Object object) {
			 	for(Socket socket:socketArray)
				try {
			 		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(object);
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
}