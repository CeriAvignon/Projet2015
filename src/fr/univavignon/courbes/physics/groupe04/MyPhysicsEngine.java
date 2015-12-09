package fr.univavignon.courbes.physics.groupe04;

import java.awt.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.PhysicsEngine;

/**
 * 
 */



public class MyPhysicsEngine implements PhysicsEngine{
	
	public double snakeTable[][];
	public Board ourBoard;
	public double itemRate = 1;   // taux d'apparition d'un item
	@Override
		/**
	 * Cette méthode doit être appelée par l'Interface Utilisateur
	 * au début de chaque manche.
	 * <br/>
	 * Le Moteur Physique doit s'initialiser, et instancier un objet
	 * de type {@link Board} représentant l'aire de jeu de la manche
	 * qui va se dérouler ensuite. Il doit pour cela utiliser les
	 * valeurs passées en paramètres, puis renvoyer cet objet pour 
	 * que l'Interface Utilisateur puisse l'utiliser à son tour.
	 * 
	 * @param width
	 * 		Largeur de l'aire de jeu, exprimée en pixel.
	 * @param height
	 * 		Hauteur de l'aire de jeu, exprimée en pixel.
	 * @param profileIds
	 * 		Tableau contenant les numéros de profils des joueurs impliqués dans 
	 * 		la manche (à utiliser pour initialiser les objets {@link Snake}).
	 * @return
	 * 		Un objet représentant l'aire de jeu de la manche.
	 */
	public Board init(int width, int height, int[] profileIds) {
		
	Position spawn;
	int playerNbr = profileIds.length;
	snakeTable = new double[playerNbr][2]; // cos et sin 
	
	/* initialisation Board */
	ourBoard = new Board ();
	ourBoard.width = width;
	ourBoard.height = height;
	ourBoard.snakes = new Snake[playerNbr];
	ourBoard.snakesMap = new HashMap<Position, Integer>();
	ourBoard.itemsMap = new HashMap<Position, Item>();

	for (int i = 0; i < playerNbr ; i++) // CREATION + POSITIONNEMENT SNAKES
	{
		spawn = snakeSpawnPos(width, height);
		ourBoard.snakes[i] = new Snake();
		initSnake(ourBoard.snakes[i], profileIds[i] , spawn);  // CONSTRUCTEUR
		System.out.println("Snake " + Integer.toString(i) + " spawn a la position " + Integer.toString(spawn.x) + " "+Integer.toString(spawn.y));
	}
	return ourBoard;  
	}

	
	/**
	 * Cette fonction initialise les données physiques d'un snake présent sur la board.
	 * 
	 * @param snake
	 * @param id
	 * @param spawnPosition
	 */
	public void initSnake(Snake snake, int id, Position spawnPosition) {
		snake.currentItems  = new HashMap<Item, Long>() ;
		snake.playerId 	    = id;
		snake.currentX      = spawnPosition.x;
		snake.currentY      = spawnPosition.y;
		snake.currentAngle  = (int)(Math.random() * 359); //Génération aléatoire d'un angle entre 0 et 359°
		snake.headRadius 	= 3;  					// 3px ?
		snake.movingSpeed   = 1;					// 1px / ms ?
		snake.turningSpeed  = 0.0015707963267949; // Est égal a 0.09 rad/ms?
		snake.state 		= true;
		snake.collision 	= true;
		snake.inversion     = false;
		snake.fly   		= false;
		snake.holeRate 	    = 0.05;			// 5% ??	
		System.out.println("Angle en degré : " + Double.toString(snake.currentAngle));	
	}
	
	
	/**
	 * @param width 
	 * @param height
	 * @return
	 */
	public Position snakeSpawnPos(int width, int height){
		
		Random r = new Random();
		// Création position avec deux paramétres aléatoires et avec une marge de 20px pour éviter de spawn sur les bords
		Position pos = new Position((r.nextInt((width-20)-20)+ 20), (r.nextInt((height-20)-20)+ 20));
		return pos;
	}
	
	@Override
	public void update(long elapsedTime, Map<Integer, Direction> commands) {

		// update coordonnées des snakes par rapport au temps
		updateSnakesPositions(elapsedTime);
		// update directions des snakes par rapport au temps
		updateSnakesDirections(elapsedTime, commands);
	}

	
		
	public void updateSnakesPositions(long time){
		long alterableTime;        // temps qui passe
		double pixel;              // permet de remplir la Map du board pixel à pixel
		boolean isMoving = false;  // permet le test de collisions
		Position pos = new Position(0,0);
		
		for (int i = 0; i < ourBoard.snakes.length ; i++){
			alterableTime = time;
			pixel = 0;
						
			while(ourBoard.snakes[i].state && alterableTime > 0){   // SI SNAKE EST EN VIE & MOUVEMENTS PAS TERMINÉS
				while(alterableTime > 0 && pixel < 1){ 	// DIMINUE LE TEMPS TANT QU'ON A PAS FAIT UN PIXEL DE MOUVEMENT
					alterableTime--;
					pixel += ourBoard.snakes[i].movingSpeed;
				}
	/*			// COMPARAISON entre snakeTable & ourBoard.snakes[i].current angle
				
				if (snakeTable[ourBoard.snakes[i].profileId][0] > Math.cos(Math.toRadians(ourBoard.snakes[i].currentAngle))){
				     
				     ourBoard.snakes[i].currentX--; 
				     
				}
				else if ( snakeTable[ourBoard.snakes[i].profileId][0] < Math.cos(Math.toRadians(ourBoard.snakes[i].currentAngle))) {
				     ourBoard.snakes[i].currentX++;
				    
				}
				
				else{
				     if (snakeTable[ourBoard.snakes[i].profileId][0] > 0){
				          ourBoard.snakes[i].currentX++;
				     }
				     
				     else if (snakeTable[ourBoard.snakes[i].profileId][0] < 0){
				          ourBoard.snakes[i].currentX--;
				     }
				}
				
				if (snakeTable[ourBoard.snakes[i].profileId][1] > Math.sin(Math.toRadians(ourBoard.snakes[i].currentAngle))){
				     
				     ourBoard.snakes[i].currentY--; 
				     
				}
				else if ( snakeTable[ourBoard.snakes[i].profileId][1] < Math.sin(Math.toRadians(ourBoard.snakes[i].currentAngle))) {
				     ourBoard.snakes[i].currentY++;
				    
				}
				
				else{
				     if (snakeTable[ourBoard.snakes[i].profileId][1] > 0){
				          ourBoard.snakes[i].currentY++;
				     }
				     
				     else if (snakeTable[ourBoard.snakes[i].profileId][1] < 0){
				          ourBoard.snakes[i].currentY--;
				     }
				}*/
				
				// VALEURS SERONT COMPRISES ENTRE -2 ET 2
				snakeTable[ourBoard.snakes[i].profileId][0] += Math.cos(Math.toRadians(ourBoard.snakes[i].currentAngle)); 	
				snakeTable[ourBoard.snakes[i].profileId][1] += Math.sin(Math.toRadians(ourBoard.snakes[i].currentAngle));

				// TESTS ANGLES COS && SIN
				if(snakeTable[ourBoard.snakes[i].profileId][0] >= 1 && snakeTable[ourBoard.snakes[i].profileId][1] >= 1) {
					ourBoard.snakes[i].currentY++;
					ourBoard.snakes[i].currentX++;
					pos.x = ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]--;
					snakeTable[ourBoard.snakes[i].profileId][0]--;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][1] <= -1 && snakeTable[ourBoard.snakes[i].profileId][0] >= 1) {
					ourBoard.snakes[i].currentY--;
					ourBoard.snakes[i].currentX++;
					pos.x = ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]++;
					snakeTable[ourBoard.snakes[i].profileId][0]--;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][1] <= -1 && snakeTable[ourBoard.snakes[i].profileId][0] <= -1) {
					ourBoard.snakes[i].currentY--;
					ourBoard.snakes[i].currentX--;
					pos.x = ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]++;
					snakeTable[ourBoard.snakes[i].profileId][0]++;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][1] >= 1 && snakeTable[ourBoard.snakes[i].profileId][0] <= -1) {
					ourBoard.snakes[i].currentY++;
					ourBoard.snakes[i].currentX--;
					pos.x = ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]--;
					snakeTable[ourBoard.snakes[i].profileId][0]++;
					isMoving = true;
				}
				// ON A DONC COS OU SINUS = 0, ETUDE DES DERNIERS CAS
				else if(snakeTable[ourBoard.snakes[i].profileId][1] >= 1) {
					ourBoard.snakes[i].currentY++;
					pos.x = ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]--;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][1] <= -1) {
					ourBoard.snakes[i].currentY--;
					pos.x = ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]++;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][0] >= 1) {
					ourBoard.snakes[i].currentX++;
					pos.x = ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][0]--;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][0] <= -1) {
					ourBoard.snakes[i].currentX--;
					pos.x = ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][0]++;
					isMoving = true;
				}

				pixel = 0;  // Si on peut dépasser 1, alors on doit pixel --
				System.out.println("New Position snake "+ Integer.toString(ourBoard.snakes[i].playerId)+ " x:" + Integer.toString(ourBoard.snakes[i].currentX) + " y:" + Integer.toString(ourBoard.snakes[i].currentY));

				if(isMoving) {    // tests de collision
					outOfBounds(ourBoard.snakes[i]);
					snakeVsSnake(ourBoard.snakes[i]);
					snakeVsItem(ourBoard.snakes[i],pos);
				}
			}
		}
	}
	
	
	public void outOfBounds(Snake snake) {
		// Si on sort du cadre
		if(snake.currentX < 0 || snake.currentX > ourBoard.width || snake.currentY < 0|| snake.currentY > ourBoard.height) {
			if (!snake.fly) { // S'il ne peut pas traverser les murs 
				snake.state = false; 
			} 
			else { // Envoyer le snake a l'opposé
				if(snake.currentX < 0)
					snake.currentX = ourBoard.width;
				else if(snake.currentX > ourBoard.width)
					snake.currentX = 0;
				if(snake.currentY < 0)
					snake.currentY = ourBoard.height;
				else if(snake.currentY > ourBoard.height)
					snake.currentY = 0;
			}	
		}
	}
	
	public void snakeVsSnake(Snake snake) {

		Position pos = new Position(snake.currentX,snake.currentY);
		int idPixel=0;
		try
		{
			idPixel = ourBoard.snakesMap.get(pos);
		}catch(NullPointerException e)
		{
			System.out.println("Position non possédée, pas de collision");
		}
		
		if(!ourBoard.snakesMap.containsKey(pos)){
			ourBoard.snakesMap.put(pos, snake.playerId);
		}
		if(idPixel != snake.playerId) {
			snake.state = false;
			System.out.println(snake.playerId + " is DEAD\nX="+pos.x+"   Y="+pos.y);
		}
		
	}
	
	
	public void snakeVsItem(Snake snake, Position pos) {
		Item newItem = ourBoard.itemsMap.get(pos);
		if( newItem != null ) {
			if(snake.state) { // if alive
				addSnakeItem(snake.playerId, newItem);
				ourBoard.itemsMap.remove(pos); // Suppression de l'item sur la map
			}
		}	
	}
	
	/**
	 * @param time
	 * @param commands
	 */
	public void updateSnakesDirections(long time, Map<Integer, Direction> commands)
	{
		Direction direction;
		for(int i = 0 ; i < ourBoard.snakes.length ; i++)
		{
			direction = commands.get(ourBoard.snakes[i].playerId);
			if(direction != null)
			{
				switch (direction)
				{
					case LEFT:  // test en cas d'inversion, si tout va bien, incrémentation de l'angle en fonction de la vitesse de rotation
						if(ourBoard.snakes[i].inversion == false)
							ourBoard.snakes[i].currentAngle += time*Math.toDegrees(ourBoard.snakes[i].turningSpeed);
						else
							ourBoard.snakes[i].currentAngle -= time*Math.toDegrees(ourBoard.snakes[i].turningSpeed);
						break;
					case RIGHT:
						if(ourBoard.snakes[i].inversion == false)
							ourBoard.snakes[i].currentAngle -= time*Math.toDegrees(ourBoard.snakes[i].turningSpeed);
						else
							ourBoard.snakes[i].currentAngle += time*Math.toDegrees(ourBoard.snakes[i].turningSpeed);
						break;
					case NONE:
						break;
					default:
						System.out.println("ERROR ???");
						break;
					}
			}
		}
	}
	
	
	
	
	
	@Override
	public void forceUpdate(Board board) {
		this.ourBoard = board;
	}
	
	
	
	
	/**
	 * Nouvel item, associé à un ou plusieurs snakes
	 * 
	 * @param id 
	 * @param item
	 */
	public void addSnakeItem(int id, Item item) {
		switch(item){
			case USER_SPEED:
				ourBoard.snakes[id].currentItems.put(item, (long)item.duration);
				ourBoard.snakes[id].movingSpeed *= 2;
				break;
			case USER_SLOW:
				ourBoard.snakes[id].currentItems.put(item, (long)item.duration);
				ourBoard.snakes[id].movingSpeed /= 1.5;
				break;
			case USER_BIG_HOLE:
				ourBoard.snakes[id].currentItems.put(item, (long)item.duration);
				ourBoard.snakes[id].holeRate /= 1.5;
				break;
			case OTHERS_SPEED:
				for(Snake snake : ourBoard.snakes) {
					if (snake.playerId != id) {
						snake.currentItems.put(item, (long)item.duration);
						snake.movingSpeed *= 2;
					}
				}
				break;
			case OTHERS_THICK:
				for(Snake snake : ourBoard.snakes) {
					if (snake.playerId != id) {
						snake.currentItems.put(item, (long)item.duration);
						snake.headRadius *= 1.5;
					}
				}
				break;
			case OTHERS_SLOW:
				for(Snake snake : ourBoard.snakes) {
					if (snake.playerId != id) {
						snake.currentItems.put(item, (long)item.duration);
						snake.movingSpeed /= 1.5;
					}
				}
				break;
			case OTHERS_REVERSE:
				for(Snake snake : ourBoard.snakes) {
					if (snake.playerId != id) {
						snake.currentItems.put(item, (long)item.duration);
						snake.inversion = true;
					}
				}
				break;
			case COLLECTIVE_THREE_CIRCLES:
				itemRate *= 3;
				break;
			case COLLECTIVE_TRAVERSE_WALL:
				for(Snake snake : ourBoard.snakes) {
					snake.currentItems.put(item, (long)item.duration);
					snake.fly = true;
				}
				break;
			case COLLECTIVE_ERASER:
				ourBoard.snakesMap.clear();
				break;
			default:
				System.out.println("Error ??");
				break;
			}
	}


	/**
	 * Supprime l'item ainsi que l'effet relatif à l'item ramassé au snake concerné.
	 * 
	 * @param id Id du Snake concerné
	 * @param item Item ramassé
	 */
	public void removeSnakeItem(int id, Item item) {
		
		switch(item)
		{
			case USER_SPEED:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].movingSpeed /= 2;
				break;
			case USER_SLOW:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].movingSpeed *= 1.5;
				break;
			case USER_BIG_HOLE:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].holeRate *= 1.5;
				break;
			case OTHERS_SPEED:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].movingSpeed /= 2;
				break;
			case OTHERS_THICK:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].headRadius /= 1.5;
				break;
			case OTHERS_SLOW:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].movingSpeed *= 1.5;
				break;
			case OTHERS_REVERSE:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].inversion = false;
				break;
			case COLLECTIVE_THREE_CIRCLES:
				itemRate /= 3;
				break;
			case COLLECTIVE_TRAVERSE_WALL:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].fly = false;
				break;
			default:
				System.out.println("Error ???");
				break;
		}
	}

	
}



