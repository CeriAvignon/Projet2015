package fr.univavignon.courbes.network.kryonet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

import javax.swing.text.StyleContext.SmallAttributeSet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.SmallUpdate;
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerGameHandler;
import fr.univavignon.courbes.inter.ServerProfileHandler;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import fr.univavignon.courbes.network.ServerCommunication;
import fr.univavignon.courbes.network.kryonet.Network;
import fr.univavignon.courbes.network.simpleimpl.NetworkConstants;

/**
 * Implémentation de la classe {@link ServerCommunication}. Elle se repose
 * sur deux autres classes pour les entrées ({@link ServerReadRunnable}) et 
 * les sorties ({@link ServerWriteRunnable}) associées à chaque client.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ServerCommunicationKryonetImpl implements ServerCommunication
{	
	////////////////////////////////////////////////////////////////
	////	ADRESSE IP
	////////////////////////////////////////////////////////////////
	/** Variable qui contient l'adresse ip de ce serveur */
	private String ip = null;
	
	int currentNumberOfClients = 0;
	
	
	@Override
	public String getIp()
	{	if(ip==null)
		{	try
			{	Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
				while(nis.hasMoreElements() && ip==null)
				{	NetworkInterface ni = nis.nextElement();
					Enumeration<InetAddress> ias = ni.getInetAddresses();
					while(ias.hasMoreElements() && ip==null)
					{	InetAddress ia = ias.nextElement();
						String iaStr = ia.getHostAddress();
						if(iaStr.startsWith("192.168."))
								ip = iaStr;
					}
				}
			}
			catch (SocketException e)
			{	e.printStackTrace();
			}
		}
		return ip;
	}

	////////////////////////////////////////////////////////////////
	////	PORT
	////////////////////////////////////////////////////////////////
	/** Variable qui contient le port de ce serveur */
	private int port = Constants.DEFAULT_PORT;//2345;

	@Override
	public int getPort()
	{	return port;
	}

	@Override
	public void setPort(int port)
	{	this.port = port;
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER DE PROFILS
	////////////////////////////////////////////////////////////////
	/** Handler de profils */
	public ServerProfileHandler profileHandler;

	@Override
	public void setProfileHandler(ServerProfileHandler configHandler)
	{	this.profileHandler = configHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param profile
	 * 		Le profil à transmettre.
	 * @param index
	 * 		Le numéro de client à transmettre.
	 */
	protected void fetchProfile(Profile profile, int index)
	{	if(profileHandler!=null)
			profileHandler.fetchProfile(profile,index);
		else
			System.err.println("Le handler de profils n'a pas été renseigné !");
	}

	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param index
	 * 		Le numéro de client à transmettre.
	 */
	public void connectionLost(int index)
	{	if(profileHandler!=null)
			profileHandler.connectionLost(index);
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
		if(gameHandler!=null)
			gameHandler.connectionLost(index);
//		else
//			System.err.println("Le handler de la partie n'a pas été renseigné !");
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER DE LA PARTIE
	////////////////////////////////////////////////////////////////
	/** Handler de la partie */
	public ServerGameHandler gameHandler;

	@Override
	public void setGameHandler(ServerGameHandler gameHandler)
	{	this.gameHandler = gameHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param index
	 * 		Le numéro de client à transmettre.
	 */
	public void fetchAcknowledgment(int index)
	{	if(gameHandler!=null)
			gameHandler.fetchAcknowledgment(index);
		else
			System.err.println("Le handler de partie n'a pas été renseigné !");
	}
	
	////////////////////////////////////////////////////////////////
	////	HANDLER D'ERREUR
	////////////////////////////////////////////////////////////////
	/** Handler d'erreurs */
	public ErrorHandler errorHandler;

	@Override
	public void setErrorHandler(ErrorHandler errorHandler)
	{	this.errorHandler = errorHandler;
	}
	
	/**
	 * Transmet cet appel au handler concerné.
	 * 
	 * @param message
	 * 		Message à transmettre au handler.
	 */
	public void displayError(String message)
	{	if(errorHandler!=null)
		errorHandler.displayError(message);
		else
			System.err.println("Le handler d'erreur n'a pas été renseigné !");
	}
	
	////////////////////////////////////////////////////////////////
	////	CONNEXION
	////////////////////////////////////////////////////////////////
	Server server;
	
	@Override
	public void launchServer()
	{	server = new Server(){
			protected Connection newConnection () 
			{	// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				System.out.println("SCI: new connection");
				return new ProfileConnection();
			}
		};

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.		
		Network.register(server);

		server.addListener(new Listener(){

			@Override
			public void received(Connection c, Object object){
			       // We only use one type of connections
			       ProfileConnection connection = (ProfileConnection)c;

			       if(object instanceof Profile){
			    	   System.out.println("SCI: Received Profile");
			    	   // Ignore the object if a client has already registered a profile. This is
			    	   // impossible with our client, but a hacker could send messages at any time.
			    	   if(connection.profile != null){
			    		   System.err.println("ServerCommunicationImpl: The profile of the new client is already defined");
			    		   return;
			    	   }
			    	   

			    	   
			    	   // S'il reste de la place pour que le client se connecte
			    	   if(currentNumberOfClients < clients.length)
			    	   {	server.sendToTCP(connection.getID(), NetworkConstants.ANNOUNCE_ACCEPTED_CONNECTION);
			    	   System.out.println("SCI: send accepted connection");
//			    	   	connection.profile = ((MessageProfile)object).getProfile();
			    	   	connection.profile = (Profile)object;
			    	   		clients[currentNumberOfClients] = connection;
							fetchProfile(connection.profile, currentNumberOfClients);
			    	   		currentNumberOfClients++;
			    	   }
			    	   else{
			    		   server.sendToTCP(connection.getID(), NetworkConstants.ANNOUNCE_REJECTED_CONNECTION);
			    		   System.out.println("SCI: send rejected connection");
			    		   connection.close();
			    	   }
			    	
			       }
			       
			       else if(object instanceof Integer){
			    	   int id = getProfileId(connection.profile);
			    	   if(id != -1){
			    		   Direction dir = Direction.NONE;
			    		   switch((Integer)object){
			    		   case -1: dir = Direction.LEFT;break;
			    		   case  1: dir = Direction.RIGHT;break;
			    		   }
			    		   
			    		   clients[id].setDirection(dir);
			    	   }
			    	   else{
			    		   if(connection.profile == null)
			    			   System.out.println("Erreur profile null");
			    		   else
			    			   System.out.println("Erreur profile non trouvé");
			    	   }
			       }
			       
			       else if(object instanceof String){
			    	   String string = (String)object;
			    	   System.out.println("SCI: Received String: "+ string);
			    	   
			    	   if(string.equals(NetworkConstants.ANNOUNCE_ACKNOWLEDGMENT))
			    		   fetchAcknowledgment(getProfileId(connection.profile));
			       }
					else if(!(object instanceof FrameworkMessage.KeepAlive))
						System.out.println("SCI: unknown class: "+ object.getClass());
					else
						System.out.print(".");
			       

			}

			@Override
			public void disconnected(Connection c){
System.out.println("SCI: disconnected");
			       ProfileConnection connection = (ProfileConnection)c;
			       if(connection.profile != null)
			    	   kickClient(connection);
			       	

			}

		});		
		
		try {
System.out.println("SCI: opening with port " + getPort());			
			server.bind(getPort(), getPort()+1);
			System.out.println("SCI: opened");
		} catch (IOException e) {
			//TODO
			e.printStackTrace();
		}
		
		server.start();
	}
	
	public int getProfileId(Profile p){
		
		int result = -1;

 	    boolean found = false;
 	    int i = 0;
 	     	   
 	    while(i < currentNumberOfClients && !found){
// 	    	if(clients[i].profile.userName.equals(p.userName) && clients[i].profile.password.equals(p.password))
 		    if(clients[i].profile.equals(p)){
 			   found = true;
 			   result = i;
 		    }
 		    else
 			    ++i;
 	   }
		
		return result;
		
	}
	
	private void kickClient(ProfileConnection pc){
 	   
 		kickClient(getProfileId(pc.profile));
		
	}
	
	static class ProfileConnection extends Connection
	{	public Profile profile; 
		private Direction direction = Direction.NONE;
		
		public synchronized Direction getDirection(){
			return direction;
		}
		
		public synchronized void setDirection(Direction d){
			direction = d;
		}
	}

	
	@Override
	public synchronized void closeServer()
	{	server.close();
	}
	
	@Override
	public synchronized void setClientNumber(int clientNumber)
	{	
		System.out.println("SCI: setClientNumber(" + clientNumber + ")");
		
		if(clients == null){
			clients = new ProfileConnection[clientNumber];
			lastProfiles = new Profile[clientNumber];
		}
		
		// si le nombre diminue, il faut en fermer certains	
		if(clientNumber < clients.length)	
		{	
			if(clientNumber < clients.length)
				// on prévient les clients rejetés				
				for(int i=clients.length-1 ; i>=clientNumber ; i--)
				{	ProfileConnection connection = clients[i];
					server.sendToTCP(connection.getID(), NetworkConstants.ANNOUNCE_REJECTED_PROFILE);
					System.out.println("SCI: send rejected profile");
					kickClient(connection);
				}
			
		}
		
		// dans tous les cas, on redimensionne les tableaux
		System.out.println("SCI: previous clients size: " + clients.length);
		lastProfiles = Arrays.copyOf(lastProfiles, clientNumber);
		clients = Arrays.copyOf(clients, clientNumber);
		System.out.println("SCI: new clients size: " + clients.length);
	}
	
	////////////////////////////////////////////////////////////////
	////	ENTREES
	////////////////////////////////////////////////////////////////
	
	@Override
	public synchronized Direction[] retrieveCommands()
	{	Direction[] result = new Direction[clients.length];
			
		for(int i=0;i < clients.length;i++){
			result[i] = clients[i].getDirection();
		}
		
		return result;
	}
	
	////////////////////////////////////////////////////////////////
	////	SORTIES
	////////////////////////////////////////////////////////////////
	/** Dernière version de la liste de profils */
	private Profile[] lastProfiles = new Profile[1];
	private ProfileConnection[] clients = null;
	
	@Override
	public void sendProfiles(Profile[] profiles)
	{	lastProfiles = Arrays.copyOf(lastProfiles,profiles.length);
		
//		for(int i = 0 ; i < lastProfiles.length ; ++i)
//			if(lastProfiles[i] != null)
//				lastProfiles[i].profile = profiles[i];
	System.out.println("SCI: send " + profiles.length + " profiles");
		sendObject(profiles);
	}
		
	@Override
	public void sendPointThreshold(int pointThreshold)
	{	Integer value = new Integer(pointThreshold);
		sendObject(value);
	}
	
	@Override
	public void sendUpdate(UpdateInterface updateData)
	{	sendObject(updateData);
		if(updateData instanceof SmallUpdate){
			SmallUpdate su = (SmallUpdate)updateData;
			
			if(su.newItem != null){
				System.out.println("\nSCI: send SmallUpdate with new item: " + su.newItem.type);
			}
		}
	}
	
	@Override
	public void sendRound(Round round)
	{	sendObject(round);
	}
	
	@Override
	public void kickClient(int index)
	{	if(index >= 0 && index < currentNumberOfClients){
			ProfileConnection connection = clients[index];
			server.sendToTCP(connection.getID(), NetworkConstants.ANNOUNCE_REJECTED_PROFILE);
			System.out.println("SCI: send rejected profile");
			
			if(index != currentNumberOfClients - 1)
				clients[index] = clients[currentNumberOfClients-1];
			
			clients[currentNumberOfClients-1] = null;
	 		currentNumberOfClients--;
	 		connection.close();
		}
	}
	
	/**
	 * Envoie un objet quelconque à tous les clients.
	 * 
	 * @param object
	 * 		L'objet à envoyer.
	 */
	public void sendObject(Object object)
	{	server.sendToAllTCP(object);
	}
	

	
//	@Override
//	public void run()
//	{	try
//		{	try
//			{	serverSocket = new ServerSocket(port);
//			}
//			catch (IOException e1)
//			{	e1.printStackTrace();
//			}
//		
//			Socket socket;
//			do
//			{	try
//				{	socket = serverSocket.accept();
//					processClient(socket);
//				}
//				catch(UnknownHostException e)
//				{	
////					errorHandler.displayError("Impossible de se connecter au client.");
////					e.printStackTrace();
//				}
//				catch(IOException e)
//				{	// si c'est une SocketException, on la re-lève
//					if(e instanceof SocketException)
//					{	SocketException se = (SocketException)e;
//						throw se;
//					}
//					// sinon, on la traite ici
//					else
//					{	errorHandler.displayError("Impossible de se connecter au client.");
//						e.printStackTrace();
//					}
//				}
//			}
//			while(true);
//		}
//		catch(SocketException e)
//		{	// rien à faire : c'est juste qu'un autre thread a fermé ce socket
//		}
//
//	}
	
//	/**
//	 * Initialise les objets et les theads nécessaires
//	 * au traitement d'un client.
//	 * 
//	 * @param socket
//	 * 		Socket utilisé pour communiquer avec le client.
//	 * 
//	 * @throws UnknownHostException
//	 * 		Problème lors de la connexion au client.
//	 * @throws IOException
//	 * 		Problème lors de la connexion au client.
//	 */
//	private synchronized void processClient(Socket socket) throws UnknownHostException, IOException
//	{	// on détermine le premier slot dispo
//		int index = 0;
//		if(sockets==null)
//		{	sockets = new Socket[1];
//			srrs = new ServerReadRunnable[1];
//			swrs = new ServerWriteRunnable[1];
//		}
//		else
//		{	while(index<sockets.length && sockets[index]!=null)
//				index++;
//		}
//		
//		// cas où il reste de la place sur le serveur
//		if(index<sockets.length)
//		{	// on ouvre le socket
//			sockets[index] = socket;
//			
//			// on crée un thread pour s'occuper des sorties
//			swrs[index] = new ServerWriteRunnable(this,index);
//			Thread outThread = new Thread(swrs[index],"Courbes-Server-"+index+"-Out");
//			outThread.start();
//			
//			// on crée un thread pour s'occuper des entrées
//			srrs[index] = new ServerReadRunnable(this,index);
//			Thread inThread = new Thread(srrs[index],"Courbes-Server-"+index+"-In");
//			inThread.start();
//			
//			// on répond favorablement au client
//			swrs[index].objects.offer(NetworkConstants.ANNOUNCE_ACCEPTED_CONNECTION);
//		}
//		
//		// cas où le serveur n'a plus de place
//		else
//		{	// on récupère le flux de sortie
//			OutputStream os = socket.getOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(os);
//			oos.flush();
//			
////			// celui d'entrée (pour rien)
////			socket.getInputStream();
//			
//			// on répond défavorablement au client
//			oos.writeObject(NetworkConstants.ANNOUNCE_REJECTED_CONNECTION);
//			
//			// on ferme la connexion
//			socket.close();
//		}
//	}
	

	
//	/**
//	 * Méthode appelée quand la connexion avec un client est perdue
//	 * accidentellement.
//	 * 
//	 * @param index
//	 * 		Numéro du client concerné.
//	 */
//	protected synchronized void lostConnection(int index)
//	{	if(sockets[index]!=null)
//		{	connectionLost(index);
//			closeConnection(index);
//		}
//	}
}
