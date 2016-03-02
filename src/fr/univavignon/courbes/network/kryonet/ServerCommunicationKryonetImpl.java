package fr.univavignon.courbes.network.kryonet;

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

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.ServerGameHandler;
import fr.univavignon.courbes.inter.ServerProfileHandler;

import fr.univavignon.courbes.network.ServerCommunication;
import fr.univavignon.courbes.network.kryonet.NetworkConstants;

/**
 * Implémentation de la classe {@link ServerCommunication}. Elle repose
 * sur la bibliothèque <a href="https://github.com/EsotericSoftware/kryonet">Kryonet</>.
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
	
	/**
	 * Number of client currently connected
	 */
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
						int i = 0;
						while(ip==null && i<Constants.IP_PREFIXES.length)
						{	if(iaStr.startsWith(Constants.IP_PREFIXES[i]))
								ip = iaStr;
							i++;
						}
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
//		else
//			System.err.println("Le handler de profils n'a pas été renseigné !");
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
//		else
//			System.err.println("Le handler de partie n'a pas été renseigné !");
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
//		else
//			System.err.println("Le handler d'erreur n'a pas été renseigné !");
	}
	
	////////////////////////////////////////////////////////////////
	////	CONNEXION
	////////////////////////////////////////////////////////////////
	/**
	 * Objet de la bibliothèque Kryonet représentant le serveur.
	 */
	Server server;
	
	@Override
	public void launchServer()
	{	server = new Server(NetworkConstants.WRITE_BUFFER_SIZE, NetworkConstants.READ_BUFFER_SIZE)
		{	/**
			 * Called when a client connects to the server
			 */
			@Override
			protected Connection newConnection () 
			{	// By providing our own connection implementation, we can store per
				// connection state without a connection ID to state look up.
				ProfileConnection connection = new ProfileConnection();
				return connection;
			}
		};

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.		
		ClassRegisterer.register(server);
		server.addListener(new Listener()
		{	@Override
			public void received(Connection c, Object object)
			{	// We only use one type of connections
				ProfileConnection connection = (ProfileConnection)c;

				if(object instanceof Profile)
				{	// Ignore the object if a client has already registered a profile. This is
		    	   	// impossible with our client, but a hacker could send messages at any time.
					if(connection.profile != null)
					{	System.err.println("ServerCommunicationImpl: The profile of the new client is already defined");
		    		   	return;
					}

					if(connection.id != -1)
					{	connection.profile = (Profile)object;
						fetchProfile(connection.profile, connection.id);
					}
				}

				else if(object instanceof Integer)
				{	if(connection.id != -1)
					{	Direction dir = Direction.NONE;
						switch((Integer)object)
						{	case -1: 
								dir = Direction.LEFT;
								break;
							case  1: 
								dir = Direction.RIGHT;
								break;
						}

						clients[connection.id].setDirection(dir);
					}
				}

				else if(object instanceof String)
				{	String string = (String)object;

					if(string.equals(NetworkConstants.ANNOUNCE_ACKNOWLEDGMENT))
						fetchAcknowledgment(connection.id);
				}

				else if(object instanceof Boolean)
				{	// S'il reste de la place pour que le client se connecte
					if(currentNumberOfClients < clients.length)
					{	clients[currentNumberOfClients] = connection;
						connection.id = currentNumberOfClients;
						currentNumberOfClients++;
						server.sendToTCP(connection.getID(), NetworkConstants.ANNOUNCE_ACCEPTED_CONNECTION);
					}
					else
					{	server.sendToTCP(connection.getID(), NetworkConstants.ANNOUNCE_REJECTED_CONNECTION);
						connection.close();
					}
				}
			}

			@Override
			public void disconnected(Connection c)
			{	ProfileConnection connection = (ProfileConnection)c;
				if(connection.profile != null)
					kickClient(connection);
			}
		});		
		
		try
		{	server.bind(getPort(), getPort()+1);
		} 
		catch (IOException e) 
		{	e.printStackTrace();
		}

		server.start();
	}
	
//	public int getProfileId(Profile p)
//	{	int result = -1;
//
//		boolean found = false;
//		int i = 0;
//
//		while(i < currentNumberOfClients && !found)
//		{	
//// 	    	if(clients[i].profile.userName.equals(p.userName) && clients[i].profile.password.equals(p.password))
//			if(clients[i].profile.equals(p))
//			{	found = true;
//				result = i;
//			}
//			else
//				++i;
//		}
//
//		return result;
//	}
	
	/**
	 * Kick a client thanks to its profile connection.
	 * 
	 * @param pc 
	 * 		The profile connection of the client.
	 */
	private void kickClient(ProfileConnection pc)
	{	kickClient(pc.id);
	}
	
	/**
	 * Connection with a client identified by its profile and its id in the list of clients.
	 *
	 * @author	L3 Info UAPV 2015-16
	 */
	static class ProfileConnection extends Connection
	{	/**
		 * Profile of the client
		 */
		public Profile profile;
		
		/**
		 * Last direction given by the client
		 */
		private Direction direction = Direction.NONE;
		
		/**
		 * Id of the connection in the array <clients>
		 */
		public int id = -1;
		
		/**
		 * Getter for the direction
		 * @return The direction of the client
		 */
		public synchronized Direction getDirection()
		{	return direction;
		}
		
		/**
		 * Set the direction
		 * @param d The new direction
		 */
		public synchronized void setDirection(Direction d)
		{	direction = d;
		}
	}
	
	@Override
	public synchronized void closeServer()
	{	server.close();
	}
	
	@Override
	public synchronized void setClientNumber(int clientNumber)
	{	if(clients == null)
		{	clients = new ProfileConnection[clientNumber];
			lastProfiles = new Profile[clientNumber];
		}
		
		// si le nombre diminue, il faut en fermer certains	
		if(clientNumber < clients.length)	
		{	// on prévient les clients rejetés				
			for(int i=clients.length-1 ; i>=clientNumber ; i--)
			{	ProfileConnection connection = clients[i];
				server.sendToTCP(connection.getID(), NetworkConstants.ANNOUNCE_REJECTED_PROFILE);
				connection.id = -1;
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
			
		for(int i=0;i < clients.length;i++)
		{	// May happened when a client is disconnected 
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
	
	/**
	 * Array of clients (a cell is null if no client is currently connected)
	 */
	private ProfileConnection[] clients = null;
	
	@Override
	public void sendProfiles(Profile[] profiles)
	{	lastProfiles = Arrays.copyOf(lastProfiles,profiles.length);
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
//		if(updateData instanceof SmallUpdate){
//			SmallUpdate su = (SmallUpdate)updateData;
//		}
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
			
			if(index != currentNumberOfClients - 1)
			{	clients[index] = clients[currentNumberOfClients-1];
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

	@Override
	public void finalizeRound()
	{	// Nothing to do
	}
}
