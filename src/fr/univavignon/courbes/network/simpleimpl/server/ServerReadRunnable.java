package fr.univavignon.courbes.network.simpleimpl.server;

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

import fr.univavignon.courbes.network.simpleimpl.NetworkConstants;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;

/**
 * Classe chargée de lire en permanence sur le flux d'entrée du serveur.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ServerReadRunnable implements Runnable
{	/**  Indique s'il faut logger les échanges réseaux (debug) */
	private final static boolean LOG = false;
	
	/**
	 * Crée un objet chargé de la communication en entrée avec le client.
	 * 
	 * @param serverCom
	 * 		Serveur concerné.
	 * @param index
	 * 		Numéro du client pour le serveur.
	 */
	public ServerReadRunnable(ServerCommunicationImpl serverCom, int index)
	{	this.serverCom = serverCom;
		this.index = index;
		socket = serverCom.sockets[index];
	}
	
	////////////////////////////////////////////////////////////////
	////	TRANSMISSION
	////////////////////////////////////////////////////////////////
	/** Numéro du client */
	private int index;
	/** Socket utilisé pour communiquer avec le client */
	private Socket socket;
	/** Classe principale du serveur */
	private ServerCommunicationImpl serverCom;

	@Override
	public void run()
	{	setActive(true);
		ObjectInputStream ois = null;

		try
		{	InputStream is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			
			do
			{	Object object = ois.readObject();
				if(LOG)
					System.out.println("SERVER<<< "+object.toString());

				// objets mis en tampon
				if(object instanceof String)
				{	String string = (String)object;
//					if(string.equals(NetworkConstants.REQUEST_PROFILES))
//						serverCom.reSendProfiles();
//					else 
					if(string.equals(NetworkConstants.ANNOUNCE_ACKNOWLEDGMENT))
						serverCom.fetchAcknowledgment(index);
				}
				else if(object instanceof Direction)
				{	Direction direction = (Direction)object;
					directions.offer(direction);
				}
				
				// objets passés au server handler
				else if(object instanceof Profile)
				{	Profile profile = (Profile)object;
					serverCom.fetchProfile(profile,index);
				}
				
			}
			while(isActive());
		}
		catch(EOFException | SocketException e)
		{	// fermeture normale : connexion probablement fermée par l'autre thread ou le client
			serverCom.lostConnection(index);
		}
		catch (ClassNotFoundException e)
		{	e.printStackTrace();
		}
		catch (IOException e)
		{	e.printStackTrace();
			serverCom.displayError("Erreur lors de la réception de données.");
		}
		finally
		{	try
			{	ois.close();
			}
			catch (IOException e)
			{	//e.printStackTrace();
			}
		}
	}
	
	////////////////////////////////////////////////////////////////
	////	ETAT
	////////////////////////////////////////////////////////////////
	/** Indique si le thread est actif */
	private boolean active = true;
	
	/**
	 * Change l'état d'activité du thread (généralement utilisé
	 * pour stopper le thread, et donc se déconnecter du serveur).
	 *  
	 * @param active
	 * 		Nouvel état d'activité.
	 */
	public void setActive(boolean active)
	{	this.active = active;
	}
	
	/**
	 * Indique si le thread est actif.
	 * 
	 * @return
	 * 		{@code true} ssi le thread est actuellement actif.
	 */
	private boolean isActive()
	{	return active;
	}
	
	////////////////////////////////////////////////////////////////
	////	FILES DE DONNEES
	////////////////////////////////////////////////////////////////
	/** File des directions reçues du client et en attente de récupération par l'Interface Utilisateur */
	protected Queue<Direction> directions = new ConcurrentLinkedQueue<Direction>();
}
