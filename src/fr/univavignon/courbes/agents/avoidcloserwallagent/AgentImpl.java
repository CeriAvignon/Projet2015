package fr.univavignon.courbes.agents.avoidcloserwallagent;

import java.util.Set;
import java.util.TreeSet;

import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.simpleimpl.PhysSnake;

/**
 * Cet agent regarde devant lui, identifie l'obstacle (mur ou trainé de snake) le plus proche, puis s'en écarte.
 * Il ne prend pas en compte sa propre trainé.
 * 
 * La vision du snake est limitée à un angle de largeur ANGLE_WIDTH.
 * 
 * @author zach
 *
 */
public class AgentImpl extends Agent{

	/** Half the width of the angle in which an obstacle is considered (must be lower than Math.PI) */
	private static double ANGLE_WIDTH = Math.PI/2;

	/** Temps avant que l'agent ne change de direction */ 
	private long timeBeforeDirChange = 0;
	
	/** Current angle of the agent snake */
	private double currentAngle;
	
	/** Minimal angle seen by the snake */
	private double lowerBound;
	
	/** Maximal angle seen by the snake */
	private double upperBound;
	
	/** Previous direction selected */
	private Direction previous_direction = Direction.NONE;
	
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

	@Override
	public Direction processDirection() {
		
		Direction result = previous_direction;
		
		// s'il n'est pas encore temps de changer de direction, ou qu'on est déjà en train de le faire
		if(timeBeforeDirChange>0){
			timeBeforeDirChange = timeBeforeDirChange - getElapsedTime();
			return previous_direction;
		}
		
		Board b = getBoard();
		
		if(b == null)
			return previous_direction;
		
		Snake agentSnake = b.snakes[getPlayerId()];
		
		// Distance en pixel à partir de laquelle on considère qu'on est dans un coin
		int pixelBorderBound = 100;
		
		// Si le snake est dans un coin (il faut éviter qu'il alterne gauche et droite donc on force l'un des deux)
		if(previous_direction != Direction.NONE 
				&& (agentSnake.currentX < pixelBorderBound && agentSnake.currentY < pixelBorderBound
				|| getBoard().width - agentSnake.currentX < pixelBorderBound && agentSnake.currentY < pixelBorderBound
				|| agentSnake.currentX < pixelBorderBound && getBoard().height - agentSnake.currentY < pixelBorderBound
				|| getBoard().width - agentSnake.currentX < pixelBorderBound && getBoard().height - agentSnake.currentY < pixelBorderBound))
				return previous_direction;
		
		currentAngle = agentSnake.currentAngle;
		
		/* Calcul des bornes dans lesquelles le snake voit peuvent se trouver l'angle */
		lowerBound = currentAngle - ANGLE_WIDTH;
		upperBound = currentAngle + ANGLE_WIDTH;
		
		/* Distance du plus proche obstacle */
		Double d_closestObstacle = null;
		
		/* Angle du plus proche obstacle (entre 0 et 2pi) */ 
		Double angle_closest_obstacle = 0.0;
		
		/* Pour chaque snake */
		for(int i = 0 ; i < b.snakes.length ; ++i){
			
			Snake s = b.snakes[i];
			
			// Si le snake n'est pas celui de l'agent
			if(i != getPlayerId())
			{
				
				// Positions du tracé
				Set<Position> s_p = new TreeSet<>(((PhysSnake)s).oldTrail);
				s_p.addAll(s.newTrail);
				
				/* Pour chaque position de la trainé */
				for(Position p : s_p){
					
					/* Récupérer l'angle entre le snake de l'agent et celui-ci (valeur entre 0 et 2*PI */
					double angle = Math.atan2(p.y-(double)agentSnake.currentY, p.x-(double)agentSnake.currentX);
					
					if(angle < 0)
						angle += 2*Math.PI;
						
					/* Si la position est visible par le snake de l'agent */
					if(isInSight(angle)){
	
						/* Calcule la distance avec le snake */
						double dist = Math.sqrt(Math.pow((double)(agentSnake.currentX-p.x), 2) + Math.pow(((double)agentSnake.currentY-p.y), 2));
							
						/* Si c'est la première position visible */
						if(d_closestObstacle == null){
							
							/* Elle devient le plus proche obstacle */
							d_closestObstacle = dist;
							angle_closest_obstacle = angle;
							
						}
						
						/* Si la position est plus proche que le plus proche obstacle connu */
						else if(d_closestObstacle > dist){
							d_closestObstacle = dist;
							angle_closest_obstacle = angle;	
						}			
					}
				}
			}
		}
		
		/* On teste si les bords du Board sont proches */
				
		/* x = 0 */
		if(d_closestObstacle == null || isInSight(Math.PI) && (double)agentSnake.currentX < d_closestObstacle){
			d_closestObstacle = (double)agentSnake.currentX;
			angle_closest_obstacle = Math.PI;
		}

		/* y = 0 */
		if(isInSight(3*Math.PI/2.0) && (double)agentSnake.currentY < d_closestObstacle){
			d_closestObstacle = (double)agentSnake.currentY;
			angle_closest_obstacle = 3*Math.PI/2.0;
		}

		/* x = max_x */
		if(isInSight(0.0) && (double)getBoard().width - (double)agentSnake.currentX < d_closestObstacle){
			d_closestObstacle = (double)getBoard().width - (double)agentSnake.currentX;
			angle_closest_obstacle = 0.0;
		}

		/* y == max_y */
		if(isInSight(Math.PI/2.0) && (double)getBoard().height - (double)agentSnake.currentY < d_closestObstacle){
			d_closestObstacle = (double)getBoard().height - (double)agentSnake.currentY;
			angle_closest_obstacle = Math.PI/2.0;
		}
		
		result = directionToAvoidAngle(angle_closest_obstacle);

		previous_direction =result;
		return result;
	}

	/**
	 * Test si un angle est dans la vision du snake d'un agent
	 * @param angle L'angle testé (entre 0 et 2pi)
	 * @return Retourne vrai si l'angle est visible par le snake
	 */
	private boolean isInSight(double angle) {

		boolean result = false;
		
		if(angle >= lowerBound && angle <= upperBound)
			result = true;

		/* Premier cas limite : si la borne supérieure dépasse 2pi
		 * On teste si l'angle est inférieur à upperBound - 2pi. */
		else if(upperBound > 2*Math.PI && angle <= upperBound - 2*Math.PI)
			result = true;
			
		/* Second cas limite : si la borne inférieure est négative 
		 * On teste si l'angle est supérieur à lowerBound + 2pi*/
		else if(lowerBound < 0 && angle >= lowerBound + 2*Math.PI)
			result = true;
			
		return result;
	}
	

	/**
	 * Renvoie la direction permettant au snake de s'écarter d'un angle
	 * @param angle L'angle testé (entre 0 et 2pi)
	 * @return Retourne la direction permettant de s'écarter de cet angle (ou null si l'angle n'est pas visible)
	 */
	private Direction directionToAvoidAngle(double angle) {
		
		Direction result = Direction.NONE;
		
		/* Calcul des bornes dans lesquelles peuvent se trouver l'angle */
		double lowerBound = currentAngle - ANGLE_WIDTH;
		double upperBound = currentAngle + ANGLE_WIDTH;

		/* On teste si l'angle est entre lowerBound et currentAngle 
		 * Attention l'axe des y est orienté vers le bas
		 * (en conséquence, par exemple, pi/2 est orienté vers le bas) */
		if(angle >= lowerBound && angle <= currentAngle)
			result = Direction.RIGHT;

		/* On teste si l'angle est entre currentAngle et upperBound*/
		else if(angle >= currentAngle && angle <= upperBound)
			result = Direction.LEFT;
		
		/* Premier cas limite : si la borne supérieure dépasse 2pi
		 * On teste si l'angle est inférieur à upperBound - 2pi */
		else if(upperBound > 2*Math.PI && angle <= upperBound - 2*Math.PI)
			result = Direction.LEFT;
		
		/* Second cas limite : si la borne inférieure est négative
		 * On teste si l'angle est supérieur à lowerBound + 2pi*/
		else if(lowerBound < 0 && angle >= lowerBound + 2*Math.PI)
			result = Direction.RIGHT;
		
		return result;
	}

}
