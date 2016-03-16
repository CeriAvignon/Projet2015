package fr.univavignon.courbes.agents.randomagent;

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

import java.util.Random;

import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Direction;

/**
 * Ceci est un agent bidon servant à illustrer comment l'API IA
 * doit être utilisée. Cet agent se contente de changer aléatoirement
 * de direction toutes les quelques secondes.
 * <br/>
 * Il est recommandé d'invoquer la méthode {@link #checkInterruption()}
 * au début de chaque boucle, et au début de chaque méthode. Ceci permet
 * d'éviter que votre agent ne bloque son thread en cas de boucle infinie ou
 * de récursion infinie.
 * <br/>
 * Notez que cette classe bénéficie des méthodes définies dans sa classe mère
 * {@link Agent}.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class AgentImpl extends Agent
{	/** Générateur aléatoire utilisé lors de divers tirages */
	private static final Random RANDOM = new Random();
	/** Durée maximale de conservation de direction (en ms) */
	private static final long STRAIGHT_MAX_DURATION = 50;
	/** Durée minimale de conservation de direction (en ms) */
	private static final long STRAIGHT_MIN_DURATION = 10;
	/** Durée maximale de changement de direction (en ms) */
	private static final long TURN_MAX_DURATION = 100;
	/** Durée minimale de changement de direction (en ms) */
	private static final long TURN_MIN_DURATION = 25;

	/**
	 * Crée un agent contrôlant le joueur spécifié
	 * dans la partie courante.
	 * 
	 * @param playerId
	 * 		Numéro du joueur contrôlé par cet agent.
	 */
	public AgentImpl(Integer playerId)
	{	super(playerId);
	}
	
	/** Temps avant que l'agent ne change de direction */ 
	private long timeBeforeDirChange = 0;
	/** Temps avant que l'agent finisse de tourner */ 
	private long timeBeforeStopTurning = 0;
	/** Dernière direction prise */
	private Direction lastDirection = Direction.NONE;
	
	@Override
	public Direction processDirection()
	{	checkInterruption();
		Direction result;
		
		// s'il est temps de changer de direction, ou qu'on est déjà en train de le faire
		if(timeBeforeDirChange<=0)
		{	// si on a fini de changer de direction, réinitialise les durées et on va tout droit
			if(timeBeforeStopTurning<=0)
			{	timeBeforeDirChange = RANDOM.nextInt((int)(STRAIGHT_MAX_DURATION-STRAIGHT_MIN_DURATION)) + STRAIGHT_MIN_DURATION;
				timeBeforeStopTurning = RANDOM.nextInt((int)(TURN_MAX_DURATION-TURN_MIN_DURATION)) + TURN_MIN_DURATION;
				result = Direction.NONE;
			}
			
			// sinon, c'est qu'on doit continuer à tourner
			else
			{	timeBeforeStopTurning = timeBeforeStopTurning - getElapsedTime();
				if(lastDirection==Direction.NONE)
				{	float p = RANDOM.nextFloat();
					if(p>0.5)
						lastDirection = Direction.RIGHT;
					else
						lastDirection = Direction.LEFT;
				}
				result = lastDirection;
			}
		}
		
		// sinon on met à jour le temps restant avant le changement de direction, et on va tout droit
		else
		{	timeBeforeDirChange = timeBeforeDirChange - getElapsedTime();
			result = Direction.NONE;
		}
		
		lastDirection = result;
		return result;
	}
}
