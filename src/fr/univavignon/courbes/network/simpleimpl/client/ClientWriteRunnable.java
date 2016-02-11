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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.ErrorHandler;

/**
 * Classe chargée d'écrire en permanence sur le flux de sortie du client.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientWriteRunnable implements Runnable
{	
	public ClientWriteRunnable(ClientCommunicationImpl clientCom)
	{	oos = clientCom.oos;
		errorHandler = clientCom.errorHandler;
	}
	
	private ObjectOutputStream oos;
	private ErrorHandler errorHandler;
	
	@Override
	public void run()
	{	setActive(true);
		
		try
		{	do
			{	Object object = objects.poll();
				if(object!=null)
					oos.writeObject(object);
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
	
	private boolean active = true;
	
	public void setActive(boolean goOn)
	{	this.active = goOn;
	}
	
	private boolean isActive()
	{	return active;
	}
	
	protected Queue<Object> objects = new ConcurrentLinkedQueue<Object>();
}
