package fr.univavignon.courbes.agents.avoidcloserwallagent;

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

import java.util.Set;
import java.util.TreeSet;

import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.simpleimpl.PhysSnake;

/**
 * Ceci est un agent bidon servant à illustrer comment l'API IA doit être utilisée. 
 * Cet agent regarde devant lui, identifie l'obstacle (mur ou trainée d'un autre serpent) 
 * le plus proche, puis s'en écarte. Il ne prend pas en compte sa propre trainée, ni les items.
 * Sa vision est limitée à un angle de largeur {@link #ANGLE_WIDTH}.
 * <br/>
 * Il est recommandé d'invoquer la méthode {@link #checkInterruption()} au début de chaque boucle, 
 * et au début de chaque méthode. Ceci permet d'éviter que votre agent ne bloque son thread en cas 
 * de boucle infinie ou de récursion infinie.
 * <br/>
 * Notez que cette classe bénéficie des méthodes définies dans sa classe mère
 * {@link Agent}.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class AgentImpl extends Agent
{	/** Moitié de l'angle de vision de l'agent, i.e. délimitant la zone traitée devant lui pour détecter des obstacles. Contrainte : doit être inférieure à {@code Math.PI} */
	private static double ANGLE_WIDTH = Math.PI/2;
	/** Distance en pixels à partir de laquelle on considère qu'on est dans un coin */
	private static int CORNER_THRESHOLD = 100;
	
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
	/** Direction courante du serpent de l'agent */
	private double currentAngle;
	/** Borne inférieure de l'angle de vision de l'agent */
	private double lowerBound;
	/** Borne supérieure de l'angle de vision de l'agent */
	private double upperBound;
	/** Direction précédemment choisie par cet agent */
	private Direction previousDirection = Direction.NONE;

	@Override
	public Direction processDirection()
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		Direction result;
		
		// s'il n'est pas encore temps de changer de direction, ou qu'on est déjà en train de le faire
		if(timeBeforeDirChange>0)
		{	timeBeforeDirChange = timeBeforeDirChange - getElapsedTime();
			result = previousDirection;
		}
		
		else
		{	Board b = getBoard();
			if(b == null)
				result = previousDirection;
			
			else
			{	Snake agentSnake = b.snakes[getPlayerId()];
				
				// si le serpent est dans un coin : il faut éviter qu'il alterne gauche et droite donc on force l'un des deux
				if(previousDirection != Direction.NONE 
						&& (agentSnake.currentX<CORNER_THRESHOLD && agentSnake.currentY<CORNER_THRESHOLD
						|| getBoard().width-agentSnake.currentX<CORNER_THRESHOLD && agentSnake.currentY<CORNER_THRESHOLD
						|| agentSnake.currentX<CORNER_THRESHOLD && getBoard().height-agentSnake.currentY<CORNER_THRESHOLD
						|| getBoard().width-agentSnake.currentX<CORNER_THRESHOLD && getBoard().height-agentSnake.currentY<CORNER_THRESHOLD))
					result = previousDirection;
				
				// si on n'est pas dans un coin
				else
				{	updateAngles(agentSnake);
					
					// distance du plus proche obstacle
					double distClosestObst = Double.POSITIVE_INFINITY;
					// angle formé avec le plus proche obstacle (entre 0 et 2pi) 
					double angleClosestObst = 0;
					
					/* pour chaque serpent */
					for(int i=0;i<b.snakes.length;++i)
					{	checkInterruption();	// on teste l'interruption au début de chaque boucle
						Snake s = b.snakes[i];
						
						// si le serpent n'est pas celui de l'agent
						if(i != getPlayerId())
						{	// on récupère les positions de sa trainée
							Set<Position> trail = new TreeSet<Position>(((PhysSnake)s).oldTrail);
							trail.addAll(s.newTrail);
							
							// pour chaque position de cette trainée
							for(Position position: trail)
							{	checkInterruption();	// une autre boucle, donc un autre test d'interruption
								
								// on récupère l'angle entre le serpent de l'agent et celui-ci (valeur entre 0 et 2*PI)
								double angle = Math.atan2(position.y-agentSnake.currentY, position.x-agentSnake.currentX);
								if(angle<0)
									angle = angle + 2*Math.PI;
									
								// si la position est visible par le serpent de l'agent
								if(isInSight(angle))
								{	// on calcule la distance avec le serpent
									double dist = Math.sqrt(Math.pow(
										agentSnake.currentX-position.x, 2) + Math.pow(agentSnake.currentY-position.y, 
										2));
										
									// si la position est la première position visible
									// ou si elle est plus proche que le plus proche obstacle connu
									if(distClosestObst > dist)
									{	distClosestObst = dist;
										angleClosestObst = angle;	
									}			
								}
							}
						}
					}
					
					// on teste si les bordures de l'aire de jeu sont proches
					// x = 0
					if(distClosestObst==Double.POSITIVE_INFINITY 
							|| isInSight(Math.PI) && agentSnake.currentX<distClosestObst)
					{	distClosestObst = agentSnake.currentX;
						angleClosestObst = Math.PI;
					}
					// y = 0
					if(isInSight(3*Math.PI/2.0) && agentSnake.currentY<distClosestObst)
					{	distClosestObst = agentSnake.currentY;
						angleClosestObst = 3*Math.PI/2.0;
					}
					// x = max_x
					if(isInSight(0.0) && getBoard().width-agentSnake.currentX<distClosestObst)
					{	distClosestObst = getBoard().width - agentSnake.currentX;
						angleClosestObst = 0.0;
					}
					// y == max_y
					if(isInSight(Math.PI/2.0) && getBoard().height-agentSnake.currentY<distClosestObst)
					{	distClosestObst = getBoard().height - agentSnake.currentY;
						angleClosestObst = Math.PI/2.0;
					}
					
					result = getDodgeDirection(angleClosestObst);
				}
			}
		}

		previousDirection = result;
		return result;
	}
	
	/**
	 * Met à jour l'angle de vision de l'agent.
	 * 
	 * @param snake
	 * 		Serpent de l'agent. 
	 */
	private void updateAngles(Snake snake)
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// angle de déplacement
		currentAngle = snake.currentAngle;
		
		// calcul des bornes de l'angle de vision du serpent
		lowerBound = currentAngle - ANGLE_WIDTH;
		upperBound = currentAngle + ANGLE_WIDTH;
	}
	
	/**
	 * Teste si un angle donné est compris dans l'angle de vision d'un agent.
	 * 
	 * @param angle 
	 * 		L'angle testé (entre 0 et 2pi).
	 * @return 
	 * 		{@code true} ssi l'angle est visible par le serpent.
	 */
	private boolean isInSight(double angle)
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		boolean result = false;
		
		if(angle>=lowerBound && angle<=upperBound)
			result = true;

		// premier cas limite : si la borne supérieure dépasse 2pi
		// on teste si l'angle est inférieur à upperBound - 2pi.
		else if(upperBound>2*Math.PI && angle<=upperBound-2*Math.PI)
			result = true;
			
		// second cas limite : si la borne inférieure est négative 
		// on teste si l'angle est supérieur à lowerBound + 2pi
		else if(lowerBound<0 && angle>=lowerBound+2*Math.PI)
			result = true;
			
		return result;
	}

	/**
	 * Renvoie la direction permettant au serpent de s'écarter d'un angle donné.
	 * 
	 * @param angle 
	 * 		L'angle traité (entre 0 et 2pi).
	 * @return 
	 * 		Direction permettant de s'écarter de cet angle (ou {@code null} si 
	 * 		l'angle n'est pas visible).
	 */
	private Direction getDodgeDirection(double angle) 
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		Direction result = Direction.NONE;
		
		// calcul des bornes dans lesquelles peuvent se trouver l'angle
		double lowerBound = currentAngle - ANGLE_WIDTH;
		double upperBound = currentAngle + ANGLE_WIDTH;

		// on teste si l'angle est entre lowerBound et currentAngle 
		// attention : l'axe des y est orienté vers le bas
		// (en conséquence, par exemple, pi/2 est orienté vers le bas)
		if(angle>=lowerBound && angle<=currentAngle)
			result = Direction.RIGHT;

		// on teste si l'angle est entre currentAngle et upperBound
		else if(angle>=currentAngle && angle<=upperBound)
			result = Direction.LEFT;
		
		// premier cas limite : si la borne supérieure dépasse 2pi
		// on teste si l'angle est inférieur à upperBound - 2pi
		else if(upperBound>2*Math.PI && angle<=upperBound-2*Math.PI)
			result = Direction.LEFT;
		
		// second cas limite : si la borne inférieure est négative
		// on teste si l'angle est supérieur à lowerBound + 2pi
		else if(lowerBound<0 && angle>=lowerBound+2*Math.PI)
			result = Direction.RIGHT;
		
		return result;
	}
}
