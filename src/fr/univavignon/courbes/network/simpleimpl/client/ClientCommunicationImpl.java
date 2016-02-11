package fr.univavignon.courbes.network.simpleimpl.client;

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

import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.simpleimpl.NetworkConstants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ClientConfigHandler;
import fr.univavignon.courbes.inter.ErrorHandler;

/**
 * Classe fille de ClientCommunication, elle en implémente toutes les méthodes.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientCommunicationImpl implements ClientCommunication
{	
	/** Variable qui contient l'adresse ip du serveur */
	private String ip;

	@Override
	public String getIp()
	{	return ip;
	}

	@Override
	public void setIp(String ip)
	{	this.ip = ip;
	}

	
	
	
	/** Variable qui contient le port du serveur */
	private int port = 2345;

	@Override
	public int getPort()
	{	return port;
	}

	@Override
	public void setPort(int port)
	{	this.port = port;
	}
	
	
	
	
	/** Handler normal */
	public ClientConfigHandler configHandler;

	@Override
	public void setClientHandler(ClientConfigHandler profileHandler)
	{	this.configHandler = profileHandler;
	}
	/** Handler d'erreurs */
	public ErrorHandler errorHandler;

	@Override
	public void setErrorHandler(ErrorHandler errorHandler)
	{	this.errorHandler = errorHandler;
	}
	
	
	
	
	/** Socket du client connecté au serveur */
	private Socket socket = null;
	/** Flux d'entrée */
	public ObjectInputStream ois;
	/** Flux de sortie */
	public ObjectOutputStream oos;
	
	@Override
	public boolean launchClient()
	{	boolean result = true;
	
		try
		{	// on ouvre le socket
			socket = new Socket(ip, port);
			
			// on récupère le flux d'entrée
			InputStream is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			
			// on récupère le flux de sortie
			OutputStream os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			
			// on crée un thread pour s'occuper des entrées
			crr = new ClientReadRunnable(this);
			Thread inThread = new Thread(crr);
			inThread.start();

			// et un autre pour les sorties
			cwr = new ClientWriteRunnable(this);
			Thread outThread = new Thread(cwr);
			outThread.start();
		}
		catch(UnknownHostException e)
		{	result = false;
			errorHandler.displayError("Impossible de se connecter au serveur.");
			e.printStackTrace();
		}
		catch(IOException e)
		{	result = false;
			errorHandler.displayError("Impossible de se connecter au serveur.");
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void closeClient()
	{	
//		try
//		{	socket.close();
//		}
//		catch(IOException e)
//		{	e.printStackTrace();
//			errorHandler.displayError("Erreur lors de la fermeture du socket.");
//		}
		
		// on indique aux deux threads de se terminer (proprement)
		crr.setActive(false);
		cwr.setActive(false);
	}

	
	
	
	private ClientReadRunnable crr;

	@Override
	public Integer retrievePointThreshold()
	{	Integer result = crr.pointsLimits.poll();
		return result;
	}

	@Override
	public Board retrieveBoard()
	{	Board result = crr.boards.poll();
		return result;
	}
	
	
	
	
	private ClientWriteRunnable cwr;
	
	@Override
	public void sendCommand(Direction direction)
	{	cwr.objects.offer(direction);
	}

	@Override
	public void sendProfile(Profile profile)
	{	cwr.objects.offer(profile);
	}

	@Override
	public void requestProfiles()
	{	cwr.objects.offer(NetworkConstants.REQUEST_PROFILES);
	}
}
