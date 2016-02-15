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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Classe chargée d'écrire en permanence sur le flux de sortie du serveur.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ServerWriteRunnable implements Runnable
{	/**  Indique s'il faut logger les échanges réseaux (debug) */
	private final static boolean LOG = false;
	
	/**
	 * Crée un objet chargé de la communication en sortie avec le client.
	 * 
	 * @param serverCom
	 * 		Serveur concerné.
	 * @param index
	 * 		Numéro du client pour le serveur.
	 */
	public ServerWriteRunnable(ServerCommunicationImpl serverCom, int index)
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
		ObjectOutputStream oos = null;
		
		try
		{	// on récupère le flux de sortie
			OutputStream os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.flush();
			
			do
			{	Object object = objects.poll();
				if(object!=null)
				{	oos.writeObject(object);
					oos.flush();
					oos.reset();
					if(LOG)
						System.out.println("SERVER>>> "+object.toString());
				}
			}
			while(isActive() || !objects.isEmpty());
		}
		catch(SocketException e)
		{	// fermeture normale : connexion probablement fermée par l'autre thread ou le client
			serverCom.lostConnection(index);
		}
		catch (IOException e)
		{	e.printStackTrace();
			serverCom.displayError("Erreur lors de l'envoi de données.");
		}
		finally
		{	try
			{	oos.close();
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
	/** File des objets déposés par l'Interface Utilisateur et en attente d'expédition vers le client */
	protected Queue<Object> objects = new ConcurrentLinkedQueue<Object>();
}
