package fr.univavignon.courbes.agents.groupe03;


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
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

/**
 * Ceci est un agent qui evites les obstacles en se basant d 'un algorithme de prediction
 */
public class AgentImpl extends Agent
{	/** Moitié de l'angle de vision de l'agent, i.e. délimitant la zone traitée devant lui pour détecter des obstacles. Contrainte : doit être inférieure à PI */
	private static double ANGLE_WIDTH = Math.PI/1.0001;
	/** Distance en pixels à partir de laquelle on considère qu'on est dans un coin */
	private static int CORNER_THRESHOLD = 50;
	/**
	 * Zone de couverture  pour la recherche d'obstacle
	 */
	private static int zonedecouverture = 50;
	
	/**
	 * Nombre d iteration a faire pour la boucle recursive
	 */
	private static int nombreiteration = 150000;
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
		Set<Position> traine = new TreeSet<Position>() ;
		Board board = getBoard();
		/**
		 * Si les directions sont renverse
		 */
		boolean isreverse = false;
		// partie pas encore commencée : on ne fait rien
		if(board == null )
			result = previousDirection;
		
		// sinon, on applique la stratégie
		else
		{	// on récupère le serpent de l'agent
			agentSnake = board.snakes[getPlayerId()];
			isreverse = agentSnake.inversion;
			Position newpos = new Position(agentSnake.currentX,agentSnake.currentY);
			//System.out.println("position x :"+agentSnake.currentX+" y:"+agentSnake.currentY+" angle "+agentSnake.currentAngle);
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
						processObstacleSnake(snake, closestObstacle,traine);
					else
						 processObstacleOwnSnake(snake, closestObstacle,traine);
				}
			
				
				// on teste si les bordures de l'aire de jeu sont proches
				processObstacleBorder(closestObstacle);
				if(prediction( new Position(agentSnake.currentX,agentSnake.currentY), traine,zonedecouverture,newpos,0,nombreiteration))
				{
					//System.out.println("prediction: "+newpos.x+" y "+newpos.y);
					double angle = Math.atan2(newpos.y-agentSnake.currentY, newpos.x-agentSnake.currentX);
					if(angle<0)
						angle = angle + 2*Math.PI;
					result = AllerVers(angle);
				}
				else
				// on prend une direction de manière à éviter cet obstacle 
				//On prend une direction pour prendre l' item
				if(processItemsBonus(closestObstacle))
				{
				result = AllerVers(closestObstacle[1]);
				}
				/*else if(false)//Angry(closestObstacle))
				{
					result = getItemDirection(closestObstacle[1]);
					System.out.println("angry: "+newpos.x+" y "+newpos.y);
				}*/
				else
					result = getDodgeDirection(closestObstacle[1]);
			}
			
		}
		
		previousDirection = result;
		if(isreverse)
		{
			if(Direction.LEFT == result)
				result = Direction.RIGHT;
			if(Direction.RIGHT == result)
				result = Direction.LEFT;
		}
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
	 * Renvoie la direction permettant au serpent de s'écarter d'aller dans un angle donne.
	 * 
	 * @param angle 
	 * 		L'angle traité (entre 0 et 2PI).
	 * @return 
	 * 		Direction permettant de s'écarter de cet angle (ou {@code null} si 
	 * 		l'angle n'est pas visible).
	 */
	private Direction AllerVers(double angle) 
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		Direction result = Direction.NONE;
		
		// on teste si l'angle est entre lowerBound et currentAngle 
		// attention : l'axe des y est orienté vers le bas
		// (en conséquence, par exemple, PI/2 est orienté vers le bas)
		if(angle>=lowerBound && angle<=currentAngle)
			result = Direction.LEFT;

		// on teste si l'angle est entre currentAngle et upperBound
		else if(angle>=currentAngle && angle<=upperBound)
			result = Direction.RIGHT;
		
		// premier cas limite : si la borne supérieure dépasse 2PI
		// on teste si l'angle est inférieur à upperBound - 2pi
		else if(upperBound>2*Math.PI && angle<=upperBound-2*Math.PI)
			result = Direction.RIGHT;
		
		// second cas limite : si la borne inférieure est négative
		// on teste si l'angle est supérieur à lowerBound + 2PI
		else if(lowerBound<0 && angle>=lowerBound+2*Math.PI)
			result = Direction.LEFT;
		
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
	 * @param contenu 
	 * Les traits qui appartient au rayon
	 */
	private void processObstacleSnake(Snake snake, double result[],Set<Position> contenu)
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
			double dist = Math.sqrt(
					Math.pow(agentSnake.currentX-position.x, 2) 
					+ Math.pow(agentSnake.currentY-position.y,2));
				if(dist <= zonedecouverture)
				{
					contenu.add(position);
				}
			// si la position est visible par le serpent de l'agent
			if(isInSight(angle))
			{	// on calcule la distance entre cette position et la tête du serpent de l'agent
				
				// si la position est plus proche que le plus proche obstacle connu : on met à jour
				if(dist<result[0])
				{	
					//System.out.println("choix2 :"+dist+" angle "+angle);
					result[0] = dist;	// mise à jour de la distance
					result[1] = angle;	// mise à jour de l'angle
				}			
			}
		}
	}
	
	
	
	
	
	/**
	 * Reçoit un serpent et détermine le point le plus proche de sa
	 * trainée, ainsi que l'angle formé avec la position courante
	 * de la tête du serpent de cet agent.
	 * 
	 * @param snake
	 * 		Le sserpent du joueur.
	 * @param result
	 * 		Un tableau de réel contenant la distance du pixel le plus
	 * 		proche appartenant à un obstacle, et l'angle qu'il forme
	 *		avec la position courante de cet agent.
	  @param contenu 
	 * Les traits qui appartient au rayon
	 */
	private void processObstacleOwnSnake(Snake snake, double result[],Set<Position> contenu)
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
			double dist = Math.sqrt(
					Math.pow(agentSnake.currentX-position.x, 2) 
					+ Math.pow(agentSnake.currentY-position.y,2));
				if(dist <= zonedecouverture)
				{
					contenu.add(position);
				}
			// si la position est visible par le serpent de l'agent
			if(isInSight(angle))
			{	// on calcule la distance entre cette position et la tête du serpent de l'agent
				
				// si la position est plus proche que le plus proche obstacle connu : on met à jour
				if(dist<result[0] && (angle >= 0 && angle <= Math.PI) && dist > 50 )
				{	//System.out.println("choix1 :"+dist+" angle "+angle);
					result[0] = dist;	// mise à jour de la distance
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
		//System.out.println("choix3 :"+result[0]+" angle "+result[1]);
	}

////////////////////////////////////////////////////////////////
////	Gestion des Items
////////////////////////////////////////////////////////////////

	/**
	 *Cherche l'tem le plus proche et ca chercher un chemin pour le
		 * prendre et l'autre il va chercher 
		 * @param result
		 * Un tableau de réel contenant la distance du pixel le plus
		 * 		proche appartenant à un obstacle, et l'angle qu'il forme
		 * 		avec la position courante de cet agent.
	 * @return True : Si on doit prendre un item et False : Si on doit pas prendre un item
	 * 
	 */
	private boolean processItemsBonus(double result[])
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		/**
		 * Boolean qui indique est ce qu on peut prendre un item ou non
		 */
		boolean bool = false;
		// x = 0
		for(ItemInstance item : getBoard().items) // pour chaque item
		{
			// on calcule la distance entre cette position et la tête du serpent de l'agent
			double dist = Math.sqrt(
				Math.pow(agentSnake.currentX-item.x, 2) 
				+ Math.pow(agentSnake.currentY-item.y,2));
			double time = agentSnake.movingSpeed * dist ;// le temps pour prendre l'item
			
			if(time > item.remainingTime)
			{
				if(result[0] > 80) // On regarde est ce que l'obstacle le plus proche n'est pas superieur a 100
				{
					double angle = Math.atan2(item.y-agentSnake.currentY, item.x-agentSnake.currentX);
					if(angle<0)
						angle = angle + 2*Math.PI;
					
					result[0] = dist;
					result[1] = angle;
					bool = true; //SIgnaler qu'on va prendre un item
				}
			}
		}
		return bool;
	
	
	}

	//
	// PREDICTION
	//
	
	/**
	 * @param pos
	 * Position du Snake 
	 * @param traine 
	 * Traine qui appartient au rayon de vision du snake
	 * @param dist 
	 * Rayon de vision
	 * @param newpos 
	 * La postion choisie qu on doit prendre
	 * @param de 
	 * L'origine
	 * @param jusqua
	 * Arrive 
	 * 
	 * @return True si il ne va pas mourir en prennant la devision
	 * et false si ilm va mourir en prenant la decision
	 */
	public boolean prediction(Position pos,Set<Position> traine,double dist,Position newpos,int de,int jusqua)
	{checkInterruption();
		if(touch(traine,pos))
			return false;
		if( Math.sqrt(Math.pow(agentSnake.currentX-pos.x, 2) + Math.pow(agentSnake.currentY-pos.y,2) ) >= dist)
		{
			
			newpos.x=pos.x;
			newpos.y=pos.y;
			return true;
		}
		if( Math.sqrt(Math.pow(agentSnake.currentX-pos.x, 2) + Math.pow(agentSnake.currentY-pos.y,2) ) > Math.sqrt(Math.pow(agentSnake.currentX-newpos.x, 2) + Math.pow(agentSnake.currentY-newpos.y,2) ))
		{
			
			newpos.x=pos.x;
			newpos.y=pos.y;
		
		}
		de = de + 1;
		if(de >= jusqua && Math.sqrt(Math.pow(agentSnake.currentX-newpos.x, 2) + Math.pow(agentSnake.currentY-newpos.y,2))>(dist/4))
			return true;
		
		pos.x = pos.x+1;
		pos.y = pos.y+1;
		if(prediction (pos,traine,dist,newpos,de,jusqua))
			return true;
		pos.x = pos.x-1;
		pos.y = pos.y-1;
		pos.x = pos.x+1;
		pos.y = pos.y;
		if(prediction (pos,traine,dist,newpos,de,jusqua))
			return true;
		pos.x = pos.x-1;
		pos.y = pos.y;
		pos.x = pos.x;
		pos.y = pos.y+1;
		if(prediction (pos,traine,dist,newpos,de,jusqua))
			return true;
		pos.x = pos.x;
		pos.y = pos.y-1;
		pos.x = pos.x-1;
		pos.y = pos.y+1;
		if(prediction (pos,traine,dist,newpos,de,jusqua))
			return true;
		pos.x = pos.x+1;
		pos.y = pos.y-1;
		pos.x = pos.x-1;
		pos.y = pos.y;
		if(prediction (pos,traine,dist,newpos,de,jusqua))
			return true;
		pos.x = pos.x+1;
		pos.y = pos.y;
		pos.x = pos.x-1;
		pos.y = pos.y-1;
		if(prediction (pos,traine,dist,newpos,de,jusqua))
			return true;
		pos.x = pos.x+1;
		pos.y = pos.y+1;
		pos.x = pos.x;
		pos.y = pos.y-1;
		if(prediction (pos,traine,dist,newpos,de,jusqua))
			return true;
		pos.x = pos.x;
		pos.y = pos.y+1;
		pos.x = pos.x+1;
		pos.y = pos.y-1;
		if(prediction (pos,traine,dist,newpos,de,jusqua))
			return true;
		return false;
		
	}
	/*public boolean refrech(PhysBoard phys,Direction direct)
	{
		Direction dir[] = new Direction [getBoard().snakes.length] ;
		int i = 0;
		for(Snake snake :getBoard().snakes)
		{
			if(snake.playerId != agentSnake.playerId)
				dir[i]=Direction.NONE;
			else
				dir[i]=direct;
		}
		phys.update(this.elapsedTime, dir);
		
		for(int elim :phys.getEliminatedPlayers())
		{
		
			if(elim == agentSnake.playerId)
				{
				
				return false;
				
				}
		}
		return true;
	}
	public boolean prediction(PhysBoard phys,int compter,int de,int jusqua)
	{
		de++;
		if(de >= jusqua)
		{
			return true;
		}
		if(!refrech(phys,Direction.RIGHT))
		{
			if(!refrech(phys,Direction.LEFT))
				if(!refrech(phys,Direction.NONE))
				{
					return false;
				}
		}
		compter++;
		return prediction(phys,compter,de,jusqua);
		
		
	}
	public Direction prediction(int de,int jusqua)
	{
		Direction dir;
		Boolean bool=false;
		int left =0;
		int none = 0;
		int right = 0;
		PhysBoard physleft = new PhysBoard((PhysBoard)getBoard());
		PhysBoard physnone = new PhysBoard((PhysBoard)getBoard());
		PhysBoard physright = new PhysBoard((PhysBoard)getBoard());
		refrech(physleft,Direction.LEFT);
		prediction(physleft,left,de,jusqua);
		dir = Direction.LEFT;
		refrech(physright,Direction.RIGHT);
		prediction(physleft,right,de,jusqua);
		if(left<right)
		dir = Direction.RIGHT;
		else
			bool = true;
		refrech(physnone,Direction.NONE);
		prediction(physnone,none,de,jusqua);
		if(bool && left < none)
			dir = Direction.NONE;
		else if(right < none)
			dir = Direction.NONE;
			return dir;
	}*/
	/**
	 * @param train
	 * Les position des snake qui appartiennent au rayon
	 * @param pos
	 * la position du snake
	 * @return
	 * True Si on est mort
	 * False si on est pas mort
	 */
	public boolean touch(Set<Position> train,Position pos)
	{checkInterruption();
		for(Position position: train)
		{
			if(pos.x == position.x || pos.y == position.y  )
				return true;
		}
		if( pos.x <= Constants.BORDER_THICKNESS || pos.y <= Constants.BORDER_THICKNESS ||  getBoard().width - pos.x<=Constants.BORDER_THICKNESS || getBoard().height<=Constants.BORDER_THICKNESS)
			return true;
		return false;
	}

	/**
	 * @param result 
	 * Les choix
	 * @return
	 * True si on peut suivre la tete
	 * False si on ne peut pas suivre la tete
	 */
	public boolean Angry(double result[])
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		/**
		 * Boolean qui indique est ce qu on peut prendre un item ou non
		 */
		boolean bool = false;
		// x = 0
		for(Snake snake : getBoard().snakes) // pour chaque item
		{
			if(snake.playerId != agentSnake.playerId)
			{
				// on calcule la distance entre cette position et la tête du serpent de l'agent
			double dist = Math.sqrt(
				Math.pow(agentSnake.currentX-snake.currentX, 2) 
				+ Math.pow(agentSnake.currentY-snake.currentY,2));
				if(result[0] > 50) // On regarde est ce que l'obstacle le plus proche n'est pas superieur a 100
				{
					double angle = Math.atan2(snake.currentY-agentSnake.currentY, snake.currentX-agentSnake.currentX);
					if(angle<0)
						angle = angle + 2*Math.PI;
					if(dist > 10)
					{
					result[0] = dist;
					result[1] = angle;
					bool = true; //SIgnaler qu'on va prendre un item
					}
				}
			}
		}
		return bool;
	
	
	}
}


