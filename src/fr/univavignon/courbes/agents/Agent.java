package fr.univavignon.courbes.agents;

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

import java.util.concurrent.Callable;

import fr.univavignon.courbes.common.Direction;

/**
 * Classe abstraite dont chaque agent doit hériter.
 * <br/>
 * La méthode importante à surcharger est {@link #processDirection}, qui doit
 * renvoyer la direction choisie par l'agent.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public abstract class Agent implements Callable<Direction>
{	
	/////////////////////////////////////////////////////////////////
	// INTERFACE CALLABLE
	/////////////////////////////////////////////////////////////////
	@Override
	public final Direction call() throws Exception
	{	Direction result = processDirection();
		if(result==null)
			result = Direction.NONE;
		
		return null;
	}	

	/////////////////////////////////////////////////////////////////
	// CONTRÔLE DE L'AGENT
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode qui calcule la prochaine direction que l'agent prendra.
	 * 
	 * @return
	 * 		Direction choisie par l'agent.
	 */
	public abstract Direction processDirection();
	
	
	/////////////////////////////////////////////////////////////////
	// TERMINAISON DE L'AGENT
	/////////////////////////////////////////////////////////////////
	/** Indicateur de demande de terminaison de l'agent (activé par le jeu à la fin de la partie) */
	private boolean stopRequest = false;
	
	/**
	 * Méthode appelée par le jeu pour demander la fin de l'agent.
	 * Elle modifie la valeur de l'indicateur {@code stopRequest}, ce qui permettra
	 * de lever une {@link StopRequestException} au prochain appel 
	 * de la méthode {@link #checkInterruption}.
	 * <br/>
	 * Cette méthode est réservée au moteur du jeu : à ne pas utiliser dans votre agent.
	 */
	public synchronized final void stopRequest()
	{	stopRequest = true;		
	}
	
	/**
	 * Méthode testant si le jeu a demandé la terminaison de l'agent.
	 * Si c'est le cas, une exception est levée, qui sera propagée jusqu'à {@link #call()}
	 * et forcera la terminaison de l'agent. 
	 * <br/>
	 * <b>Attention :</b> cette exception ne doit surtout pas être interceptée localement par 
	 * un {@code try/catch}, puisqu'elle est destinée à arrêter l'agent.
	 */
	public synchronized final void checkInterruption()
	{	Thread.yield();
		if(stopRequest)
			throw new StopRequestException();
	}

	/////////////////////////////////////////////////////////////////
	// DONNEES
	/////////////////////////////////////////////////////////////////
	/** Temps écoulé depuis la dernière mise à jour, exprimé en ms */
	private long elapsedTime;
	
	/**
	 * Méthode utilisée par le moteur pour mettre à jour les données de l'agent.
	 * <br>
	 * <b>Attention :</b> à ne surtout pas utiliser par l'agent lui-même.
	 */
	public void updateData()
	{
		// TODO
	}
	
	/**
	 * Renvoie le temps écoulé depuis la dernière mise à jour.
	 * 
	 * @return
	 * 		Temps écoulé depuis la dernière mise à jour, en ms.
	 */
	public long getElapsedTime()
	{	return elapsedTime;
	}
}
