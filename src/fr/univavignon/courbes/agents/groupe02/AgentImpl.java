package fr.univavignon.courbes.agents.groupe02;

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

import java.util.LinkedList;

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
import fr.univavignon.courbes.common.ItemInstance;

/**
 * 
 * 
 * @author Florian Demol
 * @author Thomas Hernandez
 * @author Nasreddine Kabbouch
 */
public class AgentImpl extends Agent
{	/** Moitié de l'angle de vision de l'agent, i.e. délimitant la zone traitée devant lui pour détecter des obstacles. Contrainte : doit être inférieure à PI */
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

	/** Serpent contrôlé par l'agent */
	private Snake agentSnake;
	/** Temps avant que l'agent ne change de direction */ 

	@Override
	public Direction processDirection()
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		Direction result;
		boolean r = false;
		
		Board board = getBoard();
		
		// partie pas encore commencée : on ne fait rien
		if(board == null)
			result = previousDirection;
		
		// sinon, on applique la stratégie
		else
		{	// on récupère le serpent de l'agent
			agentSnake = board.snakes[getPlayerId()];
			
			// si le serpent est dans un coin : il faut éviter qu'il alterne gauche et droite donc on force l'un des deux
			if(previousDirection!=Direction.NONE && isInCorner())
				result = previousDirection;
			
			// si on n'est pas dans un coin
			else
			{	updateAngles();
				
				// tableau de réels contenant deux valeurs : 0) distance à la position
				// la plus proche constituant un obstacle, et 1) angle formé avec la tête
				// du serpent contrôlé par cet agent (entre 0 et 2PI)
				double closestObstacle[] = {Double.POSITIVE_INFINITY, 0};
				
				// pour chaque serpent
					for(int i=0;i<board.snakes.length;++i)
					{	checkInterruption();	// on doit tester l'interruption au début de chaque boucle
						Snake snake = board.snakes[i];
						
						// on traite seulement les serpents des autres joueurs
						if(i != getPlayerId())
							// on met à jour la distance à l'obstacle le plus proche
							processObstacleSnake(snake, closestObstacle);
						else
							IsMe(closestObstacle);
					}
					for(int i=0;i<board.snakes.length;++i)
					{	checkInterruption();
						Snake snake = board.snakes[i];
						if(i != getPlayerId()){
							if(!r)
								//calcul si il est possible de tué un snake
								r = KillSnake(snake,closestObstacle);
						}
					}
					if(!r)
						//si aucun il n'est pas possible de tuer un serpent alors on donnée essayé d'avoir un objet
						r = takeObject(closestObstacle,board);	
					// on teste si les bordures de l'aire de jeu sont proches
					processObstacleBorder(closestObstacle);
					
					// on prend une direction de manière à éviter cet obstacle 
					result = getDodgeDirection(closestObstacle[1]);
					//dans cette configuration, nous donnons la prioriété a la survie du serpent. 
				}
			}
		previousDirection = result;
		return result;
	}
	
	////////////////////////////////////////////////////////////////
	////	ANGLE DE VISION
	////////////////////////////////////////////////////////////////
	/** Direction courante du serpent de l'agent */
	private double currentAngle;
	/** Borne inférieure de l'angle de vision de l'agent */
	private double lowerBound;
	/** Borne supérieure de l'angle de vision de l'agent */
	private double upperBound;
	
	/**
	 * Met à jour l'angle de vision de l'agent.
	 */
	private void updateAngles()
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// angle de déplacement
		currentAngle = agentSnake.currentAngle;
		
		// calcul des bornes de l'angle de vision du serpent
		lowerBound = currentAngle - ANGLE_WIDTH;
		upperBound = currentAngle + ANGLE_WIDTH;
	}
	
	/**
	 * Teste si un angle donné est compris dans l'angle de vision d'un agent.
	 * 
	 * @param angle 
	 * 		L'angle testé (entre 0 et 2PI).
	 * @return 
	 * 		{@code true} ssi l'angle est visible par le serpent.
	 */
	private boolean isInSight(double angle)
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		boolean result = false;
		
		if(angle>=lowerBound && angle<=upperBound)
			result = true;

		// premier cas limite : si la borne supérieure dépasse 2PI
		// on teste si l'angle est inférieur à upperBound - 2pi.
		else if(upperBound>2*Math.PI && angle<=upperBound-2*Math.PI)
			result = true;
			
		// second cas limite : si la borne inférieure est négative 
		// on teste si l'angle est supérieur à lowerBound + 2PI
		else if(lowerBound<0 && angle>=lowerBound+2*Math.PI)
			result = true;
			
		return result;
	}

	////////////////////////////////////////////////////////////////
	////	GESTION DES DIRECTIONS
	////////////////////////////////////////////////////////////////
	/** Direction précédemment choisie par cet agent */
	private Direction previousDirection = Direction.NONE;
	
	/**
	 * Renvoie la direction permettant au serpent de s'écarter d'un angle donné.
	 * 
	 * @param angle 
	 * 		L'angle traité (entre 0 et 2PI).
	 * @return 
	 * 		Direction permettant de s'écarter de cet angle (ou {@code null} si 
	 * 		l'angle n'est pas visible).
	 */
	private Direction getDodgeDirection(double angle) 
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		Direction result = Direction.NONE;
		
		// on teste si l'angle est entre lowerBound et currentAngle 
		// attention : l'axe des y est orienté vers le bas
		// (en conséquence, par exemple, PI/2 est orienté vers le bas)
		if(angle>=lowerBound && angle<=currentAngle)
			result = Direction.RIGHT;

		// on teste si l'angle est entre currentAngle et upperBound
		else if(angle>=currentAngle && angle<=upperBound)
			result = Direction.LEFT;
		
		// premier cas limite : si la borne supérieure dépasse 2PI
		// on teste si l'angle est inférieur à upperBound - 2pi
		else if(upperBound>2*Math.PI && angle<=upperBound-2*Math.PI)
			result = Direction.LEFT;
		
		// second cas limite : si la borne inférieure est négative
		// on teste si l'angle est supérieur à lowerBound + 2PI
		else if(lowerBound<0 && angle>=lowerBound+2*Math.PI)
			result = Direction.RIGHT;
		
		return result;
	}
	
	/**
	 * Détermine si on considère que la tête du serpent de l'agent
	 * se trouve dans un coin de l'aire de jeu.
	 *  
	 * @return
	 * 		{@code true} ssi l'agent est dans un coin.
	 */
	private boolean isInCorner()
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		boolean result = agentSnake.currentX<CORNER_THRESHOLD && agentSnake.currentY<CORNER_THRESHOLD
			|| getBoard().width-agentSnake.currentX<CORNER_THRESHOLD && agentSnake.currentY<CORNER_THRESHOLD
			|| agentSnake.currentX<CORNER_THRESHOLD && getBoard().height-agentSnake.currentY<CORNER_THRESHOLD
			|| getBoard().width-agentSnake.currentX<CORNER_THRESHOLD && getBoard().height-agentSnake.currentY<CORNER_THRESHOLD;
		return result;
	}

	////////////////////////////////////////////////////////////////
	////	TRAITEMENT DES OBSTACLES
	////////////////////////////////////////////////////////////////
	/**
	 * Reçoit un serpent et détermine le point le plus proche de sa
	 * trainée, ainsi que l'angle formé avec la position courante
	 * de la tête du serpent de cet agent.
	 * 
	 * @param snake
	 * 		Le serpent à traiter (un autre joueur).
	 * @param result
	 * 		Un tableau de réel contenant la distance du pixel le plus
	 * 		proche appartenant à un obstacle, et l'angle qu'il forme
	 * 		avec la position courante de cet agent.
	 */
	private void processObstacleSnake(Snake snake, double result[])
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// on récupère les positions de la trainée (complète) du serpent
		Set<Position> trail = new TreeSet<Position>(snake.oldTrail);
		trail.addAll(snake.newTrail);
		
		// pour chaque position de cette trainée
		for(Position position: trail)
		{	checkInterruption();	// une boucle, donc un autre test d'interruption
			
			// on récupère l'angle entre la tête du serpent de l'agent 
			// et la position traitée (donc une valeur entre 0 et 2*PI)
			double angle = Math.atan2(position.y-agentSnake.currentY, position.x-agentSnake.currentX);
			if(angle<0)
				angle = angle + 2*Math.PI;
				
			// si la position est visible par le serpent de l'agent
			if(isInSight(angle))
			{	// on calcule la distance entre cette position et la tête du serpent de l'agent
				double dist = Math.sqrt(
					Math.pow(agentSnake.currentX-position.x, 2) 
					+ Math.pow(agentSnake.currentY-position.y,2));
					
				// si la position est plus proche que le plus proche obstacle connu : on met à jour
				if(dist<result[0])
				{	result[0] = dist;	// mise à jour de la distance
					result[1] = angle;	// mise à jour de l'angle
				}			
			}
		}
	}
	/**
	 * On indique au serpent si sa trainer dans sur sa route ou non 
	 * 
	 * @param result
	 * 	tableau de reel, trouvant le plus proche obstacle
	*/
	private void IsMe(double result[]){
		checkInterruption();
		Set<Position> trail = new TreeSet<Position>(agentSnake.oldTrail);
		for(Position position: trail)
		{	checkInterruption();
			if(!(position.y<=agentSnake.currentY && position.y>=(agentSnake.currentY)-100)){
				if(!(position.x<=agentSnake.currentX && position.x>=(agentSnake.currentX)-100)){	
					double angle = Math.atan2(position.y-agentSnake.currentY, position.x-agentSnake.currentX);
					if(angle<0)
						angle = angle + 2*Math.PI;
					if(isInSight(angle/(3/4)))
					{
						double dist = Math.sqrt(
							Math.pow(agentSnake.currentX-position.x, 2) 
							+ Math.pow(agentSnake.currentY-position.y,2));
						if(dist<result[0])
						{	result[0] = dist;
							result[1] = angle;
						}			
					}
				}
			}
		}
	}
	
	/**
	 * Fonction permetant de tuer un snake, si la position de l'agent le permet
	 * @param snake
	 * snake différent de l'agent
	 * @param result
	 * tableau de coordonnée 
	 * @return
	 * true si il est possible de tuè le snake. false sinon
	 */
	private boolean KillSnake(Snake snake, double result[])
	{
		if(snake.movingSpeed == agentSnake.movingSpeed){
			double angle = Math.atan2(snake.currentY-agentSnake.currentY, snake.currentX-agentSnake.currentX);
			if(angle<0)
				angle = angle + 2*Math.PI;
			if(isInSight(angle/(3/4)))
			{
				double dist = Math.sqrt(
					Math.pow(agentSnake.currentX-snake.currentX, 2) 
					+ Math.pow(agentSnake.currentY-snake.currentY,2));
				if(dist<result[0])
				{	result[0] = dist;
					result[1] = angle + 10;
					return true;
				}		
			}
		}
		return false;
	}
	
	/**
	 * permet au snake de prendre un objet
	 * @param result
	 * tableau de coordonnée 
	 * @param board
	 * board actuelle
	 * @return
	 * true si il est possible de prendre un objet. False sinon
	 */
	private boolean takeObject(double result[], Board board)
	{	checkInterruption();
		LinkedList<ItemInstance> l = new LinkedList<ItemInstance>(board.items);
		for(ItemInstance item: l)
		{	checkInterruption();
			double angle = Math.atan2(item.y-agentSnake.currentY, item.x-agentSnake.currentX);
			if(angle<0)
				angle = angle + 2*Math.PI;
			if(isInSight(angle/(3/4)))
			{
				double dist = Math.sqrt(
					Math.pow(agentSnake.currentX-item.x, 2) 
					+ Math.pow(agentSnake.currentY-item.y,2));
				if(dist<result[0])
				{	result[0] = dist;
					result[1] = angle;
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Détermine le point le plus proche de la bordure constituant un
	 * obstacle pour cet agent, ainsi que l'angle formé avec la position 
	 * courante de la tête du serpent de cet agent.
	 * 
	 * @param result
	 * 		Un tableau de réel contenant la distance du pixel le plus
	 * 		proche appartenant à un obstacle, et l'angle qu'il forme
	 * 		avec la position courante de cet agent.
	 */
	private void processObstacleBorder(double result[])
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// x = 0
		if(result[0]==Double.POSITIVE_INFINITY 
				|| isInSight(Math.PI) && agentSnake.currentX<result[0])
		{	result[0] = agentSnake.currentX;
			result[1] = Math.PI;
		}
		
		// y = 0
		if(isInSight(3*Math.PI/2) && agentSnake.currentY<result[0])
		{	result[0] = agentSnake.currentY;
			result[1] = 3*Math.PI/2;
		}
		
		// x = max_x
		if(isInSight(0) && getBoard().width-agentSnake.currentX<result[0])
		{	result[0] = getBoard().width - agentSnake.currentX;
			result[1] = 0;
		}
		
		// y == max_y
		if(isInSight(Math.PI/2) && getBoard().height-agentSnake.currentY<result[0])
		{	result[0] = getBoard().height - agentSnake.currentY;
			result[1] = Math.PI/2.0;
		}
	}
}
