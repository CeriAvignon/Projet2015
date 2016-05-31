package fr.univavignon.courbes.agents.groupe05;

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
import java.util.Set;
import java.util.TreeSet;

import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

/**
 * 
 * 
 * @author Akram Assili
 * @author Ibtissam Benachour
 * @author Meryem Ezzahidi
 * @author Zakaria Lahrach
 */
public class AgentImplV2 extends Agent
{	/** Moiti� de l'angle de vision de l'agent.*/
	private static double ANGLE_SIGHT = Math.PI/2;
	/** Distance en pixels � partir de laquelle on consid�re qu'on est dans un coin */
	private static int CORNER = 50;
	/** Direction courante du serpent de l'agent */
	private double currentAngle;
	/** Borne inf�rieure de l'angle de vision de l'agent */
	private double lowerBound;
	/** Borne sup�rieure de l'angle de vision de l'agent */
	private double upperBound;
	/** Direction pr�c�demment choisie par cet agent */
	private Direction previousDirection = Direction.NONE;
	/** */
	private static final Random RANDOM = new Random();
	/** Durée maximale de conservation de direction (en ms) */
	private static final long STRAIGHT_MAX_DURATION = 20;
	/** Durée minimale de conservation de direction (en ms) */
	private static final long STRAIGHT_MIN_DURATION = 5;
	/** Durée maximale de changement de direction (en ms) */
	
	/* * Cr�e un agent contr�lant le joueur sp�cifi�
	 * dans la partie courante.
	 * 
	 * @param playerId
	 * 		Num�ro du joueur contr�l� par cet agent.
	 */
	/** Temps avant que l'agent ne change de direction */ 
	private long timeBeforeDirChange = 0;

	/**
	 * Cr�e un agent contr�lant le joueur sp�cifi�
	 * dans la partie courante.
	 * 
	 * @param playerId
	 * 		Num�ro du joueur contr�l� par cet agent.
	 */
	public AgentImplV2(Integer playerId) 
	{	super(playerId);
	}

	/** Serpent contr�l� par l'agent */
	private Snake agentSnake;
	
	/**
	 * M�thode qui calcule la prochaine direction que l'agent prendra.
	 * 
	 * @return
	 * 		Direction choisie par l'agent.
	 */
	@Override
	public Direction processDirection()
	{	checkInterruption();	// on doit tester l'interruption au d�but de chaque m�thode
		
		int dist1=0,angle1=0;
		Direction result;
		// recuperer l'aire du jeu
		Board board = getBoard();
		
		// partie pas encore commenc�e : in change pas la direction
		if(board == null)
			result = previousDirection;
		
		// sinon, on applique la strat�gie
		else
		{	// on r�cup�re le serpent de l'agent par son id
			agentSnake = board.snakes[getPlayerId()];
			
			// si le serpent est dans un coin : il faut �viter qu'il alterne gauche et droite donc on force l'un des deux
			if(previousDirection!=Direction.NONE && isInCorner())
				result = previousDirection;
			
			// si on n'est pas dans un coin
			else
			{	updateAngles();
			// s'il est temps de changer de direction, ou qu'on est déjà en train de le faire
			
				// tableau @closestObstacle contient 2 valeurs distance � la position 
				// la plus proche constituant un obstacle et l'angle form� avec la t�te
				// du serpent contr�l� par cet agent
				double closestObstacle[] = {Double.POSITIVE_INFINITY, 0};
				
				// pour chaque serpent
				for(int i=0;i<board.snakes.length;++i)
				{	checkInterruption();	// on doit tester l'interruption au d�but de chaque boucle
					Snake snake = board.snakes[i];
					
					// on traite tous les serpents sauf celui de l'agent
					if(i != getPlayerId())
						// on met � jour la distance � l'obstacle le plus proche (serpent)
						processObstacleSnake(snake, closestObstacle);
				}
				
				// on teste si les bordures de l'aire de jeu sont proches
				processObstacleBorder(closestObstacle);
				// si le plus proche obstacle est loin de plus de 100 pixels on l'ignore 
				if(closestObstacle[0]>100){
					// s'il est temps de changer de direction, ou qu'on est d�j� en train de le faire
					if(timeBeforeDirChange<=0)
					{	
						
						eatItem(angle1, dist1);
						//si l'item est plus proche que l'obstacle on change la direction vers ce dernier 
						if(dist1<closestObstacle[0] && dist1!=0){
							result = getChangeDirection(angle1);
							
						}
						else{
							result =  getChangeDirection(closestObstacle[1]);
							timeBeforeDirChange = RANDOM.nextInt((int)(STRAIGHT_MAX_DURATION-STRAIGHT_MIN_DURATION)) + STRAIGHT_MIN_DURATION;
						}
						
					}
					// sinon on met � jour le temps restant avant le changement de direction, et on va tout droit
					else{
					timeBeforeDirChange = timeBeforeDirChange - getElapsedTime();
						result = Direction.NONE;
						
						eatItem(angle1, dist1);
						//si l'item est plus proche que l'obstacle on change la direction vers ce dernier 
						if(dist1<closestObstacle[0] && dist1!=0){
							result = getChangeDirection(angle1);
							
						}
					}
				}
				else{
				
				// on prend une direction de mani�re � �viter cet obstacle 
				result = getChangeDirection(closestObstacle[1]);
				
				
				eatItem(angle1, dist1);
				//si l'item est plus proche que l'obstacle on change la direction vers ce dernier 
				if(dist1<closestObstacle[0] && dist1!=0){
					result = getChangeDirection(angle1);
				}
			}
				
		}
		}

		previousDirection = result;
		return result;
	}
	
	
	
	/**
	 * Met � jour l'angle de vision de l'agent.
	 */
	
	private void updateAngles()
	{	checkInterruption();	// on doit tester l'interruption au d�but de chaque m�thode
		
		// angle de d�placement
		currentAngle = agentSnake.currentAngle;
		
		// calcul des bornes de l'angle de vision du serpent
		lowerBound = currentAngle - ANGLE_SIGHT;
		upperBound = currentAngle + ANGLE_SIGHT;
	}
	
	/**
	 * Teste si un angle donn� est compris dans l'angle de vision d'un agent.
	 * 
	 * @param angle 
	 * 		L'angle test� (entre 0 et 2PI).
	 * @return 
	 * 		{@code true} ssi l'angle est visible par le serpent.
	 */
	private boolean isInSight(double angle)
	{	checkInterruption();	// on doit tester l'interruption au d�but de chaque m�thode
		boolean result = false;
		
		if(angle>=lowerBound && angle<=upperBound)
			result = true;

		// premier cas limite : si la borne sup�rieure d�passe 2PI
		// on teste si l'angle est inf�rieur � upperBound - 2pi.
		else if(upperBound>2*Math.PI && angle<=upperBound-2*Math.PI)
			result = true;
			
		// second cas limite : si la borne inf�rieure est n�gative 
		// on teste si l'angle est sup�rieur � lowerBound + 2PI
		else if(lowerBound<0 && angle>=lowerBound+2*Math.PI)
			result = true;
			
		return result;
	}

	
	/**
	 * Renvoie la direction permettant au serpent de s'�carter d'un angle donn�.
	 * 
	 * @param angle 
	 * 		L'angle trait� (entre 0 et 2PI).
	 * @return 
	 * 		Direction permettant de s'�carter de cet angle (ou {@code null} si 
	 * 		l'angle n'est pas visible).
	 */
	private Direction getChangeDirection(double angle) 
	{	checkInterruption();	// on doit tester l'interruption au d�but de chaque m�thode
		Direction result = Direction.NONE;
		
		// on teste si l'angle est entre lowerBound et currentAngle 
		if(angle>=lowerBound && angle<=currentAngle)
			result = Direction.RIGHT;

		// on teste si l'angle est entre currentAngle et upperBound
		else if(angle>=currentAngle && angle<=upperBound)
			result = Direction.LEFT;
		
		// premier cas limite : si la borne sup�rieure d�passe 2PI
		// on teste si l'angle est inf�rieur � upperBound - 2pi
		else if(upperBound>2*Math.PI && angle<=upperBound-2*Math.PI)
			result = Direction.LEFT;
		
		// second cas limite : si la borne inf�rieure est n�gative
		// on teste si l'angle est sup�rieur � lowerBound + 2PI
		else if(lowerBound<0 && angle>=lowerBound+2*Math.PI)
			result = Direction.RIGHT;
		
		return result;
	}
	
	/**
	 * D�termine si on consid�re que la t�te du serpent de l'agent
	 * se trouve dans un coin de l'aire de jeu.
	 *  
	 * @return
	 * 		{@code true} ssi l'agent est dans un coin.
	 */
	private boolean isInCorner()
	{	checkInterruption();	// on doit tester l'interruption au d�but de chaque m�thode
		
		boolean result = agentSnake.currentX<CORNER && agentSnake.currentY<CORNER
			|| getBoard().width-agentSnake.currentX<CORNER && agentSnake.currentY<CORNER
			|| agentSnake.currentX<CORNER && getBoard().height-agentSnake.currentY<CORNER
			|| getBoard().width-agentSnake.currentX<CORNER && getBoard().height-agentSnake.currentY<CORNER;
		return result;
	}
	
	
	
	/**
	 * permet au snake de prendre un item
	 * D�termine l'item le plus proche du serpent , ainsi que l'angle form� avec la position 
	 * courante de la t�te du serpent.
	 * 
	 * @param distance
	 * 		la distance � la position d'un item 
	 * 
	 * @param angle1
	 * 		l'angle form� avec la t�te du serpent 
	 * 
	 */
	private void eatItem(double angle1,double distance)
	{	checkInterruption();
	
	if( itemsinboard()){
		
		for(ItemInstance item: getBoard().items)
		{	checkInterruption();
			double angle = Math.atan2(item.y-agentSnake.currentY, item.x-agentSnake.currentX);
			if(angle<0)
				angle = angle + 2*Math.PI;
			if(isInSight(angle))
			{
				double dist = Math.sqrt(
					Math.pow(agentSnake.currentX-item.x, 2) 
					+ Math.pow(agentSnake.currentY-item.y,2));
				
				if(dist<distance){
					distance = dist;
					angle1 = angle;
					
				}
			}
		}
	}
		
	}
	
	/**
	 * Teste s'il y a des items sur l'aire du jeu 
	 * @return
	 * 		{@code true} ssi des items sont pr�sents sur l'aire du jeu 
	 */
	private boolean itemsinboard(){
		if(getBoard().items.size()==0) return false;
		else return true;
	}

	
	/**
	 * Re�oit un serpent et d�termine le point le plus proche de sa
	 * train�e, ainsi que l'angle form� avec la position courante
	 * de la t�te du serpent de cet agent.
	 * 
	 * @param snake
	 * 		Le serpent � traiter (un autre joueur).
	 * @param result
	 * 		Un tableau de r�el contenant la distance du pixel le plus
	 * 		proche appartenant � un obstacle, et l'angle qu'il forme
	 * 		avec la position courante de cet agent.
	 */
	
	private void processObstacleSnake(Snake snake, double result[])
	{	checkInterruption();	// on doit tester l'interruption au d�but de chaque m�thode
		
		// on r�cup�re les positions de la train�e (compl�te) du serpent
		Set<Position> trail = new TreeSet<Position>(snake.oldTrail);
		trail.addAll(snake.newTrail);
		
		// pour chaque position de cette train�e
		for(Position position: trail)
		{	checkInterruption();	// une boucle, donc un autre test d'interruption
			
			// on r�cup�re l'angle entre la t�te du serpent de l'agent 
			// et la position trait�e (donc une valeur entre 0 et 2*PI)
			double angle = Math.atan2(position.y-agentSnake.currentY, position.x-agentSnake.currentX);
			if(angle<0)
				angle = angle + 2*Math.PI;
			
				
			// si la position est visible par le serpent de l'agent
			if(isInSight(angle))
			{	// on calcule la distance entre cette position et la t�te du serpent de l'agent
				double dist = Math.sqrt(
					Math.pow(agentSnake.currentX-position.x, 2) 
					+ Math.pow(agentSnake.currentY-position.y,2));
					
				// si la position est plus proche que le plus proche obstacle connu : on met � jour
				if(dist<result[0])
				{	result[0] = dist;	// mise � jour de la distance
					result[1] = angle;	// mise � jour de l'angle
				}			
			}
		}
	}
	
	/**
	 * D�termine le point le plus proche de la bordure constituant un
	 * obstacle pour cet agent, ainsi que l'angle form� avec la position 
	 * courante de la t�te du serpent de cet agent.
	 * 
	 * @param result
	 * 		Un tableau de r�el contenant la distance du pixel le plus
	 * 		proche appartenant � un obstacle, et l'angle qu'il forme
	 * 		avec la position courante de cet agent.
	 */
	private void processObstacleBorder(double result[])
	{	checkInterruption();	// on doit tester l'interruption au d�but de chaque m�thode
		
		// x = 0
		if( isInSight(Math.PI) || result[0]==Double.POSITIVE_INFINITY  && agentSnake.currentX<result[0])
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
