package fr.univavignon.courbes.agents.groupe04;


import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.common.Constants;

/**
 * @author quentin castillo
 * @author mary pascal
 * @author christophe gala
 * @author gael cuminal
 */
public class AgentImpl extends Agent
{
	/** Moitié de l'angle de vision de l'agent, i.e. délimitant la zone traitée devant lui pour détecter des obstacles. Contrainte : doit être inférieure à PI */
	private static double ANGLE_WIDTH = Math.PI/2;
	/** Distance en pixels à partir de laquelle on considère qu'on est dans un coin */
	private static int CORNER_THRESHOLD = 120;
	/** Direction de l'agent */
	private static Direction direction_agent = Direction.NONE;
	/** blabla left */
	private static Direction LEFT = Direction.LEFT;
	/**	balbla right */
	private static Direction RIGHT = Direction.RIGHT;
	/** recuperation snake agent **/
	private static Snake agent;
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
	private long timeBeforeDirChange = 0;

	@SuppressWarnings("unused")
	@Override
	public Direction processDirection()
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// s'il n'est pas encore temps de changer de direction, ou qu'on est déjà en train de le faire
		if(timeBeforeDirChange>0)
		{	timeBeforeDirChange = timeBeforeDirChange - getElapsedTime();
			direction_agent =  previousDirection;
		}
		
		else
		{	Board board = getBoard();
			// partie pas encore commencée : on ne fait rien
			if(board == null)
				direction_agent = previousDirection;
			
			// sinon, on applique la stratégie
			else
			{	// on récupère le serpent de l'agent
				agentSnake = board.snakes[getPlayerId()];
				agent = agentSnake;
				// si le serpent est dans un coin : il faut éviter qu'il alterne gauche et droite donc on force l'un des deux
				if(previousDirection!=Direction.NONE && isInCorner() && checkIfSafe())
					direction_agent = previousDirection;
				
				// si le serpent est coincé entre le mur est un snake
				else if(false){
					// TODO
				}
				// si on n'est pas dans un coin
				else
				{	updateAngles();
					
				   // permet de s'assurer si les touches sont inversées ou non
					if(agentSnake.inversion){
						RIGHT = Direction.LEFT;
						LEFT = Direction.RIGHT;
					}else{
						RIGHT = Direction.RIGHT;
						LEFT = Direction.LEFT;
					}
					
				   if(!checkIfSafe()){
					   // tableau de réels contenant deux valeurs : 0) distance à la position
						// la plus proche constituant un obstacle, et 1) angle formé avec la tête
						// du serpent contrôlé par cet agent (entre 0 et 2PI)
						double closestObstacle[] = {Double.POSITIVE_INFINITY, 0};
						
						double closestDodgeMove[] = {Double.POSITIVE_INFINITY,0};

					
						// on teste si les bordures de l'aire de jeu sont proches
						processObstacleBorder(closestObstacle);
						
						
						// pour chaque serpent
						for(int i=0;i<board.snakes.length;++i)
						{	checkInterruption();	// on doit tester l'interruption au début de chaque boucle
							Snake snake = board.snakes[i];
							// on cherche l'espace ou on peut passer (trou ?)
							processNoObstacle(snake, closestDodgeMove);
							// on met à jour la distance à l'obstacle le plus proche
							processObstacleSnake(snake, closestObstacle);			
						}
						
						
						// on prend une direction de manière à éviter cet obstacle 
						direction_agent = getDodgeDirection(closestObstacle[1],closestDodgeMove[1]);
				   }
				   else{
					   double tab[] = itemChoose();
					   
					   if(tab != null && tab[2] < closestSnakeDist()){
						   direction_agent = goToItem(tab[0],tab[1]);
					   }
					   else direction_agent = aggression();
				   }
				}
			}
		}
		

		previousDirection = direction_agent;
		
		return direction_agent;
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
	 * @param angleEsquive angle qui permet la survie
	 * @return 
	 * 		Direction permettant de s'écarter de cet angle (ou {@code null} si 
	 * 		l'angle n'est pas visible).
	 */
	private Direction getDodgeDirection(double angle, double angleEsquive) 
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// 5° de vision
		if(agent.currentAngle <= angleEsquive+Math.PI/60 && agent.currentAngle>=angleEsquive-Math.PI/60){
			System.out.println("tout droit !");
			return Direction.NONE;
		}
		// on teste si l'angle est entre lowerBound et currentAngle 
		// attention : l'axe des y est orienté vers le bas
		// (en conséquence, par exemple, PI/2 est orienté vers le bas)
		if(angle>=lowerBound && angle<=currentAngle )
			return RIGHT;

		// on teste si l'angle est entre currentAngle et upperBound
		else if(angle>=currentAngle && angle<=upperBound )
			return LEFT;
		
		// premier cas limite : si la borne supérieure dépasse 2PI
		// on teste si l'angle est inférieur à upperBound - 2pi
		else if(upperBound>2*Math.PI && angle<=upperBound-2*Math.PI)
			return LEFT;
		
		// second cas limite : si la borne inférieure est négative
		// on teste si l'angle est supérieur à lowerBound + 2PI
		else if(lowerBound<0 && angle>=lowerBound+2*Math.PI)
			return RIGHT;
		
		return Direction.NONE;
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
			|| (getBoard().width-agentSnake.currentX)<CORNER_THRESHOLD && agentSnake.currentY<CORNER_THRESHOLD
			|| agentSnake.currentX<CORNER_THRESHOLD && (getBoard().height-agentSnake.currentY)<CORNER_THRESHOLD
			|| (getBoard().width-agentSnake.currentX)<CORNER_THRESHOLD && (getBoard().height-agentSnake.currentY)<CORNER_THRESHOLD;
		return result;
	}

	
	

	////////////////////////////////////////////////////////////////
	////	TRAITEMENT DES OBSTACLES
	////////////////////////////////////////////////////////////////
	/** On récupère la zone la plus sécurisé autour du snake
	 * @param snake par rapport à ce snake
	 * @param result tableau contenant le resultat
	 */
	private void processNoObstacle(Snake snake, double result[])
	{	checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		// on récupère les positions de la trainée (complète) du serpent
		Set<Position> trail = new TreeSet<Position>(snake.oldTrail);
		
		if(getPlayerId() != snake.playerId) trail.addAll(snake.newTrail);
		
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
			// si la position est visible par le serpent de l'agent
				if(!isInSight(angle))
				{	// on calcule la distance entre cette position et la tête du serpent de l'agent
					// si la position est plus proche que le plus proche obstacle connu : on met à jour
						if(dist<result[0] && dist<250*agent.movingSpeed*agent.headRadius)
						{	

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
		
		if(getPlayerId() != snake.playerId) trail.addAll(snake.newTrail);
		
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
			// si la position est visible par le serpent de l'agent
			if(getPlayerId() != snake.playerId ){
				if(isInSight(angle))
				{	// on calcule la distance entre cette position et la tête du serpent de l'agent
					// si la position est plus proche que le plus proche obstacle connu : on met à jour
						if(dist<result[0] && dist<250*agent.movingSpeed*agent.headRadius )
						{	

							result[0] = dist;	// mise à jour de la distance
							result[1] = angle;	// mise à jour de l'angle
						}	
				
				}
			}
			else{
				if(isInSight(angle))
				{	// on calcule la distance entre cette position et la tête du serpent de l'agent
					// si la position est plus proche que le plus proche obstacle connu : on met à jour
						if(dist<result[0] && dist > 5 && dist<250*agent.movingSpeed*agent.headRadius)
						{	
							result[0] = dist;	// mise à jour de la distance
							result[1] = angle;	// mise à jour de l'angle
						}	
				
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
	}
	
	
	
	/** on verifie qu'il n'y a rien dans la zone autour de nous
	 * @return si le danger est présent ou pas
	 */
	public boolean checkIfSafe(){
		checkInterruption();	// on doit tester l'interruption au début de chaque boucle
		
		double tab[] = {Double.POSITIVE_INFINITY,0};
		
		processObstacleBorder(tab);
		
		Board board = getBoard();
		Snake[] tab2 = board.snakes;
		for(Snake snake : tab2){
			processObstacleSnake(snake, tab);
		}
		
		if(tab[0] < 250*agent.movingSpeed*agent.headRadius){
			return false;
		}
		else return true;
	}
	
	
	/**
	 * Check le serpent le proche et fonce dessus
	 * 
	 * @return Direction toTake : direction que va prendre l'agent
	 */
	public Direction aggression()
	{
		checkInterruption();
		
		Direction toTake = Direction.NONE;
		
		//recup la position de ce serpent + prevision
		double result = closestSnake();
		
		// si le serpent se trouve entre lowerBound et currentAngle
		// alors on va à gauche
		if(result>=lowerBound && result<=currentAngle)
			toTake = LEFT;
		
		// si le serpent se trouve entre currentAngle et upperAngle
		// alors on va à droite
		else if(result >=currentAngle && result<=upperBound)
			toTake = RIGHT;
		
		// premier cas limite : si la borne supérieure dépasse 2PI
		// on teste si l'angle est inférieur à upperBound - 2pi
		else if(upperBound>2*Math.PI && result<=upperBound-2*Math.PI)
			toTake = RIGHT;
		
		// second cas limite : si la borne inférieure est négative
		// on teste si l'angle est supérieur à lowerBound + 2PI
		else if(lowerBound<0 && result>=lowerBound+2*Math.PI)
			toTake = LEFT;
		
		//on fonce dessus : return RIGHT ou LEFT
		return toTake;
	}
	
	
	//int maxIdx = 0;
	
			//checker les scores, recup le serpent qui a le plus haut score
			// Je n'arrive pas a recup le score des snakes , 
			//il faut relier le playerId du snake dans un tableau de player ?
					
					/*players.playerId = snake.playerId;				
					Player[] players.getPlayerId() = i
					if(players.totalScore > maxIdx)
						maxIdx = i;*/
							
			//deuxieme solution : trouver le serpent le plus proche: 
					
				
	
	/**
	 * Retourne l'angle entre la position du serpent le plus proche et l'agent
	 * 
	 * @return double dist 
	 */
	public double closestSnake()
	{
		Board board = getBoard();
		double angle = 0;
		double distFinal = Double.POSITIVE_INFINITY;
		for(int i=0; i<board.snakes.length ;i++)
		{	checkInterruption();
			
			Snake snake = board.snakes[i];	
			
			// on traite seulement les serpents des autres joueurs
			if(i != getPlayerId())
			{
				//On recup la trainée du serpent i
				Set<Position> trail = new TreeSet<Position>(snake.newTrail);
				
				for(Position position: trail)
				{	checkInterruption();	// une boucle, donc un autre test d'interruption
					
					// on récupère l'angle entre la tête du serpent de l'agent 
					// et la position traitée (donc une valeur entre 0 et 2*PI)
					double dista = Math.sqrt(
						Math.pow(agentSnake.currentX-position.x, 2) 
						+ Math.pow(agentSnake.currentY-position.y,2));
					
					if(dista < distFinal){
						angle = Math.atan2(position.y-agentSnake.currentY, position.x-agentSnake.currentX);
						if(angle<0)
							angle = angle + 2*Math.PI;
					}
				}
			}
		}
		return angle;	
	}
	
	
	/**
	 * Retourne le snake le plus proche et l'agent
	 * 
	 * @return double dist 
	 */
	public int closestSnakeSnake()
	{
		Board board = getBoard();
		double angle = 0;
		double dist = 0;
		int numAgent = 0;
		for(int i=0; i<board.snakes.length ;i++)
		{	checkInterruption();
			
			Snake snake = board.snakes[i];	
			
			// on traite seulement les serpents des autres joueurs
			if(i != getPlayerId())
			{
				//On recup la trainée du serpent i
				Set<Position> trail = new TreeSet<Position>(snake.newTrail);
				
				for(Position position: trail)
				{	checkInterruption();	// une boucle, donc un autre test d'interruption
					
					// on récupère l'angle entre la tête du serpent de l'agent 
					// et la position traitée (donc une valeur entre 0 et 2*PI)
					angle = Math.atan2(position.y-agentSnake.currentY, position.x-agentSnake.currentX);
					if(angle<0)
						angle = angle + 2*Math.PI;
						
					double dista = Math.sqrt(
							Math.pow(agentSnake.currentX-position.x, 2) 
							+ Math.pow(agentSnake.currentY-position.y,2));
					if(dista < dist){
						dist = dista;
						numAgent = snake.playerId;
					}
				}
			}
		}
		return numAgent;	
	}
	
	/**
	 * Retourne la distance entre la position du serpent le plus proche et l'agent
	 * 
	 * @return double dist 
	 */
	public double closestSnakeDist()
	{
		Board board = getBoard();
		double angle = 0;
		double dist = Double.POSITIVE_INFINITY;
		for(int i=0; i<board.snakes.length ;i++)
		{	checkInterruption();
			
			Snake snake = board.snakes[i];	
			
			// on traite seulement les serpents des autres joueurs
			if(i != getPlayerId())
			{
				//On recup la trainée du serpent i
				Set<Position> trail = new TreeSet<Position>(snake.newTrail);
				
				for(Position position: trail)
				{	checkInterruption();	// une boucle, donc un autre test d'interruption
					
					// on récupère l'angle entre la tête du serpent de l'agent 
					// et la position traitée (donc une valeur entre 0 et 2*PI)
					angle = Math.atan2(position.y-agentSnake.currentY, position.x-agentSnake.currentX);
					if(angle<0)
						angle = angle + 2*Math.PI;
					
					double dista = Math.sqrt(
							Math.pow(agentSnake.currentX-position.x, 2) 
							+ Math.pow(agentSnake.currentY-position.y,2));
					if(dista < dist){
						dist = dista;
					}
				}
			}
		}
		return dist;	
	}
	
	////////////////////////////////////////////////////////////////
	////	PATH FINDING ALGORITHM  
	////////////////////////////////////////////////////////////////
	  /**
     * The main A Star Algorithm in Java.
     *
     * finds an allowed path from start to goal coordinates on this map.
     * <p>
     * This method uses the A Star algorithm. The hCosts value is calculated in
     * the given Node implementation.
     * <p>
     * This method will return a LinkedList containing the start node at the
     * beginning followed by the calculated shortest allowed path ending
     * with the end node.
     * <p>
     * If no allowed path exists, an empty list will be returned.
     * <p>
     * <p>
     * x/y must be bigger or equal to 0 and smaller or equal to width/hight.

     * @return the path as calculated by the A Star algorithm
     */
   /* public final <T> List<T> findPath(int oldX, int oldY, int newX, int newY) {
    	checkInterruption();	// on doit tester l'interruption au début de chaque boucle
    	LinkedList<T>openList = new LinkedList<T>();
        LinkedList<T>closedList = new LinkedList<T>();
        openList.add(oldX,oldY); // add starting node to open list
 
        boolean done = false;
        T current;
        while (!done) {
            current = lowestFInOpen(); // get node with lowest fCosts from openList
            closedList.add(current); // add current node to closed list
            openList.remove(current); // delete current node from open list
 
            if ((current.getxPosition() == newX)
                    && (current.getyPosition() == newY)) { // found goal
                return calcPath(oldX,oldY,newX,newY);
            }
 
            // for all adjacent nodes:
            List<T> adjacentNodes = getAdjacent(current);
            for (int i = 0; i < adjacentNodes.size(); i++) {
                T currentAdj = adjacentNodes.get(i);
                if (!openList.contains(currentAdj)) { // node is not in openList
                    currentAdj.setPrevious(current); // set current node as previous for this node
                    currentAdj.sethCosts(newX,newY); // set h costs of this node (estimated costs to goal)
                    currentAdj.setgCosts(current); // set g costs of this node (costs from start to this node)
                    openList.add(currentAdj); // add node to openList
                } else { // node is in openList
                    if (currentAdj.getgCosts() > currentAdj.calculategCosts(current)) { // costs from current node are cheaper than previous costs
                        currentAdj.setPrevious(current); // set current node as previous for this node
                        currentAdj.setgCosts(current); // set g costs of this node (costs from start to this node)
                    }
                }
            }
 
            if (openList.isEmpty()) { // no path exists
                return new LinkedList<T>(); // return empty list
            }
        }
        return null; // unreachable
    }*/


	////////////////////////////////////////////////////////////////
	////	Direction des items
	////////////////////////////////////////////////////////////////
	public double[] itemChoose()
	{
		Board board = getBoard();
	    int []values=new int[board.items.size()];
	    int max=0;
	    int numItem = -1;
	    try{
		    for(int i=0;i<values.length;i++)
		    {
		       values[i]= takeIt(board.items,i,closestSnakeSnake());
		        if(values[i]>max)
		        {
		            max=values[i];
		            numItem=i;
		        }
		    }
		    double x=board.items.get(numItem).x;
		    double y=board.items.get(numItem).y;
		    double distance = Math.sqrt(Math.pow(agentSnake.currentX-x, 2) + Math.pow(agentSnake.currentY-y,2));
		    double []item=new double[3];
		    item[0]=x;
		    item[1]=y;
		    item[2]=distance;
		    return item;
	    }catch(NullPointerException e) {
	    	e.getStackTrace();
	    	return null;
	    }
	    catch(Exception e){
	    	e.getStackTrace();
	    	return null;
	    }

	}
	
	/**
	 * @param itemX  Position x de l'item vers lequel on doit aller
	 * @param itemY  Position y de l'item vers lequel on doit alelr
	 * @return la direction vers laquelle on doit aller pour aller à l'item
	 */
	public Direction goToItem(double itemX,double itemY)
	{
		checkInterruption();	// on doit tester l'interruption au début de chaque méthode
		
		Position dest = new Position((int) itemX,(int) itemY);
		
		if(agent.currentAngle >= (3*Math.PI)/2 && agent.currentAngle <= (7*Math.PI)/4) // le cercle des radians est inversé.
		{
			
			if(agent.currentX <= dest.x && agent.currentY >= dest.y) 
			{
				return RIGHT;
			}
			
			if(agent.currentX <= dest.x && agent.currentY <= dest.y)
			{
				return RIGHT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY >= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY <= dest.y)
			{
				return LEFT;
			}
		}
		
		if(agent.currentAngle >= (7*Math.PI)/4 && agent.currentAngle <= (2*Math.PI)/1)
		{
			
			if(agent.currentX <= dest.x && agent.currentY >= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX <= dest.x && agent.currentY <= dest.y)
			{
				return RIGHT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY >= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY <= dest.y)
			{
				return RIGHT;
			}
		}
		

		if(agent.currentAngle <= (3*Math.PI)/2 && agent.currentAngle >= (5*Math.PI)/4)
		{
			
			if(agent.currentX <= dest.x && agent.currentY >= dest.y)
			{
				return RIGHT;
			}
			
			if(agent.currentX <= dest.x && agent.currentY <= dest.y)
			{
				return RIGHT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY >= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY <= dest.y)
			{
				return LEFT;
			}
		}
		
		if(agent.currentAngle <= (5*Math.PI)/4 && agent.currentAngle >= (1*Math.PI)/1)
		{
			
			if(agent.currentX <= dest.x && agent.currentY >= dest.y)
			{
				return RIGHT;
			}
			
			if(agent.currentX <= dest.x && agent.currentY <= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY >= dest.y)
			{
				return RIGHT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY <= dest.y)
			{
				return LEFT;
			}
		}
		
	
		if(agent.currentAngle <= (1*Math.PI)/1 && agent.currentAngle >= (3*Math.PI)/4)
		{
			
			if(agent.currentX <= dest.x && agent.currentY >= dest.y)
			{
				return RIGHT;
			}
			
			if(agent.currentX <= dest.x && agent.currentY <= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY >= dest.y)
			{
				return RIGHT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY <= dest.y)
			{
				return LEFT;
			}
		}
		
		if(agent.currentAngle <= (3*Math.PI)/4 && agent.currentAngle >= (1*Math.PI)/2)
		{
			
			if(agent.currentX <= dest.x && agent.currentY >= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX <= dest.x && agent.currentY <= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY >= dest.y)
			{
				return RIGHT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY <= dest.y)
			{
				return RIGHT;
			}
		}
		
		if(agent.currentAngle <= (1*Math.PI)/2 && agent.currentAngle >= (1*Math.PI)/4)
		{
			
			if(agent.currentX <= dest.x && agent.currentY >= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX <= dest.x && agent.currentY <= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY >= dest.y)
			{
				return RIGHT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY <= dest.y)
			{
				return RIGHT;
			}
		}
		
		if(agent.currentAngle <= (1*Math.PI)/4 && agent.currentAngle >= 0)
		{
			
			if(agent.currentX <= dest.x && agent.currentY >= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX <= dest.x && agent.currentY <= dest.y)
			{
				return RIGHT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY >= dest.y)
			{
				return LEFT;
			}
			
			if(agent.currentX >= dest.x && agent.currentY <= dest.y)
			{
				return RIGHT;
			}
		}
		
		
		return Direction.NONE;
		
	}

    /**
	 * Appelle la fonction retournant la valeur estimée de la prise d'un item en fonction de son type
	 * @param items la liste des items présents sur le board
	 * @param j la position de l'item à évaluer dans la liste
	 * @param numEnemy la position du snake le plus proche dans le tableau des snakes du board
	 * @return int value la valeur de l'item
	 */

public int takeIt(List<ItemInstance> items, int j, int numEnemy)
{
	Board board = getBoard();
	Snake enemy = board.snakes[numEnemy];
	int value;
	switch(items.get(j).type)
	{
		case OTHERS_FAST:
			value=othersFast(items.get(j), enemy);
			return value;
		case OTHERS_REVERSE:
			value=othersReverse(items.get(j), enemy);
			return value;
		case OTHERS_THICK:
			value=othersThick(items.get(j), enemy);
			return value;
		case OTHERS_SLOW:
			value=othersSlow(items.get(j), enemy);
			return value;
		case USER_FAST:
			value=userFast(items.get(j), enemy);
			return value;
		case USER_FLY:
			value=userFly(items.get(j), enemy);
			return value;
		case USER_SLOW:
			value=userSlow(items.get(j), enemy);
			return value;
		case COLLECTIVE_CLEAN:
			value=collectiveClean(items.get(j), enemy);
			return value;
		case COLLECTIVE_TRAVERSE:
			value=collectiveTraverse(items.get(j), enemy);
			return value;
		case COLLECTIVE_WEALTH:
			value=collectiveWealth(items.get(j), enemy);
			return value;
	}
	return 0;
}

    /**
	 * Evalue la valeur d'un item de type othersFast
	 * Prend en compte les distances entre l'objet et l'agent, l'objet et l'adversaire et l'agent et l'adversaire
	 * @param object l'item à évaluer
	 * @param enemy le snake le plus proche
	 * @return la valeur de l'item
	 */

public static int othersFast(ItemInstance object, Snake enemy)
{
	double toKill=Math.sqrt(Math.pow(enemy.currentX-agent.currentX, 2) + Math.pow(enemy.currentY-agent.currentY,2))+enemy.movingSpeed*object.type.duration-agent.movingSpeed*Constants.MOVING_SPEED_COEFF*object.type.duration;
	if(Math.sqrt(Math.pow(agent.currentX-object.x, 2) + Math.pow(agent.currentY-object.y,2))<Math.sqrt(Math.pow(enemy.currentX-object.x, 2) + Math.pow(enemy.currentY-object.y,2)))
	{
		if(Math.sqrt(Math.pow(agent.currentX-object.x, 2) + Math.pow(agent.currentY-object.y,2))<100 && toKill>10)
		{
			return 100;
		}
		else
		{
			return 0;
		}
	}
	else
	{
		return -100;
	}
}

    /**
	 * Evalue la valeur d'un item de type othersReverse
	 * @param object l'item à évaluer
	 * @param enemy le snake le plus proche
	 * @return la valeur de l'item
	 */

public static int othersReverse(ItemInstance object, Snake enemy)
{
	return 1000;
}

    /**
	 * Evalue la valeur d'un item de type othersThick
	 * @param object l'item à évaluer
	 * @param enemy le snake le plus proche
	 * @return la valeur de l'item
	 */

public static int othersThick(ItemInstance object, Snake enemy)
{
	return 50;
}

    /**
	 * Evalue la valeur d'un item de type othersSlow
	 * Prend en compte les distances entre l'objet et l'agent, l'objet et l'adversaire et l'agent et l'adversaire
	 * @param object l'item à évaluer
	 * @param enemy le snake le plus proche
	 * @return la valeur de l'item
	 */

public static int othersSlow(ItemInstance object, Snake enemy)
{
	double toKill=Math.sqrt(Math.pow(enemy.currentX-agent.currentX, 2) + Math.pow(enemy.currentY-agent.currentY,2))+enemy.movingSpeed*object.type.duration-agent.movingSpeed*Constants.MOVING_SPEED_COEFF*object.type.duration;
	if(Math.sqrt(Math.pow(agent.currentX-object.x, 2) + Math.pow(agent.currentY-object.y,2))<Math.sqrt(Math.pow(enemy.currentX-object.x, 2) + Math.pow(enemy.currentY-object.y,2)))
	{
		if(Math.sqrt(Math.pow(agent.currentX-object.x, 2) + Math.pow(agent.currentY-object.y,2))<100 && toKill<-10)
		{
			return 100;
		}
		else
		{
			return 0;
		}
	}
	else
	{
		return -100;
	}
}

    /**
	 * Evalue la valeur d'un item de type userFast
	 * Prend en compte les distances entre l'objet et l'agent, l'objet et l'adversaire et l'agent et l'adversaire
	 * @param object l'item à évaluer
	 * @param enemy le snake le plus proche
	 * @return la valeur de l'item
	 */

public static int userFast(ItemInstance object, Snake enemy)
{
	double toKill=Math.sqrt(Math.pow(enemy.currentX-agent.currentX, 2) + Math.pow(enemy.currentY-agent.currentY,2))+enemy.movingSpeed*object.type.duration-agent.movingSpeed*Constants.MOVING_SPEED_COEFF*object.type.duration;
	if(Math.sqrt(Math.pow(agent.currentX-object.x, 2) + Math.pow(agent.currentY-object.y,2))<Math.sqrt(Math.pow(enemy.currentX-object.x, 2) + Math.pow(enemy.currentY-object.y,2)))
	{
		if(Math.sqrt(Math.pow(agent.currentX-object.x, 2) + Math.pow(agent.currentY-object.y,2))<100 && toKill<-60)
		{
			return 100;
		}
		else
		{
			return 0;
		}
	}
	else
	{
		return -100;
	}
}

    /**
	 * Evalue la valeur d'un item de type userFly
	 * @param object l'item à évaluer
	 * @param enemy le snake le plus proche
	 * @return la valeur de l'item
	 */

public static int userFly(ItemInstance object, Snake enemy)
{
	return 500;
}

    /**
	 * Evalue la valeur d'un item de type userSlow
	 * Prend en compte les distances entre l'objet et l'agent, l'objet et l'adversaire et l'agent et l'adversaire
	 * @param object l'item à évaluer
	 * @param enemy le snake le plus proche
	 * @return la valeur de l'item
	 */

public static int userSlow(ItemInstance object, Snake enemy)
{
	Double toKill=Math.sqrt(Math.pow(enemy.currentX-agent.currentX, 2) + Math.pow(enemy.currentY-agent.currentY,2))+enemy.movingSpeed*object.type.duration-agent.movingSpeed*Constants.MOVING_SPEED_COEFF*object.type.duration;
	if(Math.sqrt(Math.pow(agent.currentX-object.x, 2) + Math.pow(agent.currentY-object.y,2))<Math.sqrt(Math.pow(enemy.currentX-object.x, 2) + Math.pow(enemy.currentY-object.y,2)))
	{
		if(Math.sqrt(Math.pow(agent.currentX-object.x, 2) + Math.pow(agent.currentY-object.y,2))<100 && toKill>60)
		{
			return 100;
		}
		else
		{
			return 0;
		}
	}
	else
	{
		return -100;
	}
}

    /**
	 * Evalue la valeur d'un item de type collectiveClean
	 * @param object l'item à évaluer
	 * @param enemy le snake le plus proche
	 * @return la valeur de l'item
	 */

public static int collectiveClean(ItemInstance object, Snake enemy)
{
	return 0;
}

    /**
	 * Evalue la valeur d'un item de type collectiveTraverse
	 * @param object l'item à évaluer
	 * @param enemy le snake le plus proche
	 * @return la valeur de l'item
	 */

public static int collectiveTraverse(ItemInstance object, Snake enemy)
{
	return 50;
}

    /**
	 * Evalue la valeur d'un item de type collectiveWealth
	 * @param object l'item à évaluer
	 * @param enemy le snake le plus proche
	 * @return la valeur de l'item
	 */

public static int collectiveWealth(ItemInstance object, Snake enemy)
{
	return 0;
}
}