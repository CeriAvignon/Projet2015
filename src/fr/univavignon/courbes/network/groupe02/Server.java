package fr.univavignon.courbes.network.groupe02;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerProfileHandler;
import fr.univavignon.courbes.network.ServerCommunication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;

/**
 * 
<<<<<<< HEAD
 * @author Marie et Mary    
 * 
 * Création de la classe server qui implémente les fonction de l'interface serverCommunication
 *
 */
public class Server implements ServerCommunication
{
	/**
	 * 	Variable de type integer contenant le port par défaut du serveur
	 */
	protected int port = 2345;
	
	/**
	 *  Variable de type String contenant l'adresse ip du serveur
	 */
	protected String ip;

	/**
	 *  Variable de type boolean permettant d'attendre une connexion
	 *  tant qu'elle est à 1
	 */
	protected boolean isRunning = false;
	
	/**
	 *  Variable contenant le socket du serveur
	 */
	private ServerSocket server = null;
	
	/**
	 * Variable de type integer contenant le nombre maximum de clients 
	 * que le serveur peut accueillir.
	 */
	protected static int size = 6;
	
	/**
	 * Variable de type integer contenant le nombre actuel de clients connectés
	 */
	protected static int nbClients = 0;
	
	/**
	 * Liste qui contient toute les sockets des clients
	 */
	protected static ArrayList<Socket> arrayOfSocket = new ArrayList<Socket>();
	
	
	/**
	 * @author Marie et Mary
	 * Fonction permettant de récupérer l'adresse ip du serveur
	 * 
	 * @param null
	 * @return ip
	 */
	@Override
	public String getIp() 
	{
		return this.ip;
	}
	
	/**
	 * @author Marie et Mary
	 * Fonction permettant de récupérer le port du serveur
	 * 
	 * @param null
	 * @return port
	 */
	@Override
	public int getPort() 
	{
		return this.port;
	}
	
	/**
	 * @author Marie et Mary
	 * Fonction permettant de modifier le port du serveur
	 * 
	 * @param port
	 * @return null
	 */
	@Override
	public void setPort(int port) 
	{
		this.port = port;
	}
	
	@Override
	public void setErrorHandler(ErrorHandler errorHandler) 
	{
		
	
	}

	@Override
	public void setProfileHandler(ServerProfileHandler profileHandler) 
	{
	
	}

	/**
	 * @author Marie et Mary
	 * Fonction permettant lancer un serveur, et donc de creer une partie en local
	 * 
	 * @param null
	 * @return null
	 */
	@Override
	public void launchServer() 
	{
	// Premier try/catch pour définir l'adresse ip du serveur :
		
		String findAddressIp; 
		// Variable qui permettra de stocker l'adresse ip trouvée
		try
		{
			Enumeration<NetworkInterface> enumInterface = NetworkInterface.getNetworkInterfaces();
			// Commande permettant d'énumerer toutes les interface de l'ordinateur
			
			while(enumInterface.hasMoreElements()) // Tan qu'il y a des interfaces non lues
			{
				NetworkInterface inetAdressIp = enumInterface.nextElement(); 
				// On passe à la suivante
				
				if(inetAdressIp.isLoopback() || !inetAdressIp.isUp()) continue;
				// Condition qui permet d'enlever l'addresse de bouclage et les interfaces inactives des résultats
				
				Enumeration<InetAddress> enumAdressIp = inetAdressIp.getInetAddresses();
				// Commande permettant d'énumerer toutes adresses disponible
				
				InetAddress addressIp = enumAdressIp.nextElement();
				// On énumère toutes les adresses
				
				findAddressIp = addressIp.getHostAddress();
				// On stocke l'adresse de l'hôte trouvée
				
				this.ip = findAddressIp;
				// On l'alloue à la variable ip
				
				System.out.println(this.ip);
			}
		}
		catch (SocketException e) 
		{
 	        throw new RuntimeException(e);
 	    }
		
	// Deuxième try/catch pour définir un port si celui par défaut est inaccessible
		
		try
		{
			this.server = new ServerSocket(this.port); 
			// Création du serveur pour déterminer le port
		}
		catch (IOException e)
		{
			this.port = 0;
			// Erreur si le port est à zéro : il est déjà occupé
		}
		 if(this.port == 0) // Il faut donc en allouer un autre
		 {
			 for (int searchNewPort = 1 ; searchNewPort <=3000; searchNewPort++)
			 {
				 try
				 {
					 this.server = new ServerSocket(searchNewPort); 
					 // On crée un socket pour chaque port trouvé afin de déterminer s'il est utilisable ou pas
					 
					 this.port = searchNewPort;
					 // On le remplace par le port actuel
					 
					 break;
				 }
				 catch (IOException e)
				 {
				 }
			 }
		 }
		 
	// Troisième et dernier try/catch pour lancer le serveur
		
		try 
		{
			server.close();
			// On ferme les serveur précedemment crées
			
			server = new ServerSocket(this.port, Server.size, InetAddress.getByName(this.ip));
			// On crée le bon serveur avec toutes les informations mises à jour
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		Thread serverLaunch = new Thread(new Runnable() // Lancement du thread qui contiendra le serveur tournant en boucle 
		{
			@Override
			public void run() 
			{
				while(isRunning) // Tant que le serveur n'est pas fermé
				{
					try
					{
						Socket socketClient = server.accept();
						// On attend que les client se connectent au serveur
						
						arrayOfSocket.add(socketClient);
						// On l'ajoute à la liste de clients
												
 	    				nbClients++;
 	    				// On augement le compteur de clients au serveur
 	    				
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
				
				try 
				{
 	    			server.close();
 	    		}
				catch (IOException e) 
				{
 	    			e.printStackTrace();
 	    			server = null;
 	    		}
			}
			
		});
		
	serverLaunch.start();
	// On lance le thread contenant serveur
			
	}
	
	/**
	 * 
	 * @author Marie et Mary
	 * Fonction permettant fermer un serveur (lors d'une fin de partie)
	 * 
	 * @param null
	 * @return null
	 */
	@Override
	public void closeServer() 
	{
		if(server != null)
		{
			try
			{
				server.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
				server = null;
			}
		}
	}
	
	/**
	 * @author Marie et Mary
	 * Fonction permettant d'envoyer les profils en TCP des joueurs connectés au serveur
	 * 
	 * @param profiles
	 * @return null
	 */
	@Override
	public void sendProfiles(List<Profile> profiles) 
	{
		try 
		{
			for(Socket sock:arrayOfSocket)
			{
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(profiles);
				oos.flush();
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	
	}
	
	/**
	 * @author Marie et Mary
	 * Fonction permettant d'envoyer la limite des points à atteindre pour gagner la partie
	 * 
	 * @param pointThreshold
	 * @return null
	 */
	@Override
	public void sendPointThreshold(int pointThreshold) 
	{
		
	}
	
	/**
	 * @author Marie et Mary
	 * Fonction permettant d'envoyer un board 
	 * 
	 * @param board
	 * @retun null
	 */
	@Override
	public void sendBoard(Board board) 
	{
		try 
		{
			for(Socket sock:arrayOfSocket)
			{
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(board);
				oos.flush();
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Map<Integer, Direction> retrieveCommands() 
	{
		return null;
	}

	
	/*@Override
	public void sendText(String message) 
	{
		Thread t = new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				while(isRunning)
				{
					try
					{
						Socket SocketClient = server.accept();
						String message="";
						BufferedWriter write = new BufferedWriter(new OutputStreamWriter(SocketClient.getOutputStream()));
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
	}
*/

	/*@Override
	public String[] retrieveText() {
		
		return null;
	}
*/
	
	
}
