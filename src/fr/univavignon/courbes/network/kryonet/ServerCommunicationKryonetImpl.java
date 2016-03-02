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
				
				ProfileConnection connection = new ProfileConnection();
		    	   
				return connection;
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
			    	   
			    	   if(connection.id != -1){
			    		   connection.profile = (Profile)object;
			    		   fetchProfile(connection.profile, connection.id);
			    	   }
			    	 			    	
			       }
			       
			       else if(object instanceof Integer){
			    	   if(connection.id != -1){
			    		   Direction dir = Direction.NONE;
			    		   switch((Integer)object){
			    		   case -1: dir = Direction.LEFT;break;
			    		   case  1: dir = Direction.RIGHT;break;
			    		   }
			    		   
			    		   clients[connection.id].setDirection(dir);
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
			    		   fetchAcknowledgment(connection.id);
			       }
			       
					else if(object instanceof Boolean){

						System.out.println("SCI: received Boolean");
				    	   // S'il reste de la place pour que le client se connecte
				    	   if(currentNumberOfClients < clients.length)
				    	   {	
				    	   		System.out.println("SCI: send accepted connection");
				    	   		clients[currentNumberOfClients] = connection;
				    	   		connection.id = currentNumberOfClients;
				    	   		currentNumberOfClients++;
				    	   		server.sendToTCP(connection.getID(), NetworkConstants.ANNOUNCE_ACCEPTED_CONNECTION);
				    	   }
				    	   else{
				    		   server.sendToTCP(connection.getID(), NetworkConstants.ANNOUNCE_REJECTED_CONNECTION);
				    		   System.out.println("SCI: send rejected connection");
				    		   connection.close();
				    	   }
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
	
//	public int getProfileId(Profile p){
//		
//		int result = -1;
//
// 	    boolean found = false;
// 	    int i = 0;
// 	     	   
// 	    while(i < currentNumberOfClients && !found){
//// 	    	if(clients[i].profile.userName.equals(p.userName) && clients[i].profile.password.equals(p.password))
// 		    if(clients[i].profile.equals(p)){
// 			   found = true;
// 			   result = i;
// 		    }
// 		    else
// 			    ++i;
// 	   }
//		
//		return result;
//		
//	}
	
	private void kickClient(ProfileConnection pc){
 	   
 		kickClient(pc.id);
		
	}
	
	static class ProfileConnection extends Connection
	{	public Profile profile; 
		private Direction direction = Direction.NONE;
		
		/**
		 * Id of the connection in the array <clients>
		 */
		public int id = -1;
		
		public synchronized Direction getDirection(){
			return direction;
		}
		
		public synchronized void setDirection(Direction d){
			direction = d;
		}
	}

	
	@Override
	public synchronized void closeServer()
	{	System.out.println("SCI: close server");
		server.close();
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
			// on prévient les clients rejetés				
			for(int i=clients.length-1 ; i>=clientNumber ; i--)
			{	ProfileConnection connection = clients[i];
				server.sendToTCP(connection.getID(), NetworkConstants.ANNOUNCE_REJECTED_PROFILE);
				connection.id = -1;
				System.out.println("SCI: send rejected profile");
				kickClient(connection);
			}
			
		}
		
		// dans tous les cas, on redimensionne les tableaux
		lastProfiles = Arrays.copyOf(lastProfiles, clientNumber);
		clients = Arrays.copyOf(clients, clientNumber);
	}
	
	////////////////////////////////////////////////////////////////
	////	ENTREES
	////////////////////////////////////////////////////////////////
	
	@Override
	public synchronized Direction[] retrieveCommands()
	{	Direction[] result = new Direction[clients.length];
			
		for(int i=0;i < clients.length;i++){
			
			/* May happened when a client is disconnected */
			if(clients[i] != null)
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
			connection.id = -1;
			
			server.sendToTCP(connection.getID(), NetworkConstants.ANNOUNCE_REJECTED_PROFILE);
			System.out.println("SCI: send rejected profile");
			
			if(index != currentNumberOfClients - 1){
				clients[index] = clients[currentNumberOfClients-1];
				clients[index].id = index;
			}
			
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
	

}
