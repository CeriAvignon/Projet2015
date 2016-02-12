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
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.univavignon.courbes.inter.ErrorHandler;

/**
 * Classe chargée d'écrire en permanence sur le flux de sortie du serveur.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ServerWriteRunnable implements Runnable
{	
	/**
	 * Crée un objet chargé de la communication en sortie avec le client.
	 * 
	 * @param serverCom
	 * 		Serveur concerné.
	 * @param index
	 * 		Numéro du client pour le serveur.
	 */
	public ServerWriteRunnable(ServerCommunicationImpl serverCom, int index)
	{	oos = serverCom.ooss[index];
		errorHandler = serverCom.errorHandler;
	}
	
	////////////////////////////////////////////////////////////////
	////	TRANSMISSION
	////////////////////////////////////////////////////////////////
	/** Flux de sortie utilisé pour communiquer avec le client */
	private ObjectOutputStream oos;
	/** Handler chargé des messages d'erreur */
	private ErrorHandler errorHandler;
	
	@Override
	public void run()
	{	setActive(true);
		
		try
		{	do
			{	Object object = objects.poll();
				if(object!=null)
				{	oos.writeObject(object);
					oos.flush();
				}
			}
			while(isActive());
		}
		catch (IOException e)
		{	e.printStackTrace();
			errorHandler.displayError("Erreur lors de l'envoi de données.");
		}
		finally
		{	try
			{	oos.close();
			}
			catch (IOException e)
			{	e.printStackTrace();
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
