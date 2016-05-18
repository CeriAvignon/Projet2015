package fr.univavignon.courbes.agents.groupe05;


import java.util.Set;
import java.util.TreeSet;
import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;


/**
 * @author Assili,lahrach,ezzahidi,benchour
 *
 */
public class AgentImpl extends Agent
{	/** Moitié de l'angle de vision de l'agent.*/
	private static double ANGLE_SIGHT = Math.PI/2;
	/** Distance en pixels à partir de laquelle on considère qu'on est dans un coin */
	private static int CORNER = 50;
	/** Direction courante du serpent de l'agent */
	private double currentAngle;
	/** Borne inférieure de l'angle de vision de l'agent */
	private double lowerBound;
	/** Borne supérieure de l'angle de vision de l'agent */
	private double upperBound;
	/** Direction précédemment choisie par cet agent */
	private Direction previousDirection = Direction.NONE;
	
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
	
	/**
	 * Méthode qui calcule la prochaine direction que l'agent prendra.
	 * 
	 * @return
	 * 		Direction choisie par l'agent.
	 */
	@Override
	public Direction processDirection()
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		Direction result;
		// recuperer l'aire du jeu
		Board board = getBoard();
		
		// partie pas encore commencée : ON change pas la direction
		if(board == null)
			result = previousDirection;
		
		// sinon, on applique la stratégie
		else
		{	// on récupère le serpent de l'agent par son id
			agentSnake = board.snakes[getPlayerId()];
			
			// si le serpent est dans un coin : il faut éviter qu'il alterne gauche et droite donc on force l'un des deux
			if(previousDirection!=Direction.NONE && isInCorner())
				result = previousDirection;
			
			// si on n'est pas dans un coin
			else
			{	updateAngles();
				
				// tableau @closestObstacle contient 2 valeurs distance à la position 
				// la plus proche constituant un obstacle et l'angle formé avec la tête
				// du serpent contrôlé par cet agent
				double closestObstacle[] = {Double.POSITIVE_INFINITY, 0};
				
				// pour chaque serpent
				for(int i=0;i<board.snakes.length;++i)
				{	checkInterruption();	// on doit tester l'interruption au début de chaque boucle
					Snake snake = board.snakes[i];
					
					// on traite tous les serpents sauf celui de l'agent
					if(i != getPlayerId())
						// on met à jour la distance à l'obstacle le plus proche (serpent)
						processObstacleSnake(snake, closestObstacle);
				}
				
				// on teste si les bordures de l'aire de jeu sont proches
				processObstacleBorder(closestObstacle);
				
				// on prend une direction de manière à éviter cet obstacle 
				result = getChangeDirection(closestObstacle[1]);
			}
		}

		previousDirection = result;
		return result;
	}
	
	
	
	/**
	 * Met à jour l'angle de vision de l'agent.
	 */
	
	private void updateAngles()
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// angle de déplacement
		currentAngle = agentSnake.currentAngle;
		
		// calcul des bornes de l'angle de vision du serpent
		lowerBound = currentAngle - ANGLE_SIGHT;
		upperBound = currentAngle + ANGLE_SIGHT;
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

	
	/**
	 * Renvoie la direction permettant au serpent de s'écarter d'un angle donné.
	 * 
	 * @param angle 
	 * 		L'angle traité (entre 0 et 2PI).
	 * @return 
	 * 		Direction permettant de s'écarter de cet angle (ou {@code null} si 
	 * 		l'angle n'est pas visible).
	 */
	private Direction getChangeDirection(double angle) 
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		Direction result = Direction.NONE;
		
		// on teste si l'angle est entre lowerBound et currentAngle 
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
		
		boolean result = agentSnake.currentX<CORNER && agentSnake.currentY<CORNER
			|| getBoard().width-agentSnake.currentX<CORNER && agentSnake.currentY<CORNER
			|| agentSnake.currentX<CORNER && getBoard().height-agentSnake.currentY<CORNER
			|| getBoard().width-agentSnake.currentX<CORNER && getBoard().height-agentSnake.currentY<CORNER;
		return result;
	}

	
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
			if(angle>2*Math.PI)
				angle= angle - 2*Math.PI;
				
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
