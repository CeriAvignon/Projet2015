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
		
	Position posSpawn;
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
		posSpawn = generateSnakeSpawnPos(width, height);
		ourBoard.snakes[i] = new Snake();
		initSnake(ourBoard.snakes[i], profileIds[i] , posSpawn);  // CONSTRUCTEUR
		System.out.println("Snake " + Integer.toString(i) + " spawn a la position " + Integer.toString(posSpawn.x) + " "+Integer.toString(posSpawn.y));
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
		snake.ourBoard.snakes[i].currentItems  = new HashMap<Item, Long>() ;
		snake.playerId 	    = id;
		snake.ourBoard.snakes[i].currentX      = spawnPosition.x;
		snake.ourBoard.snakes[i].currentY      = spawnPosition.y;
		snake.ourBoard.snakes[i].currentAngle  = (int)(Math.random() * 359); //Génération aléatoire d'un angle entre 0 et 359°
		snake.headRadius 	= 3;  					// 3px ?
		snake.movingSpeed   = 1;					// 1px / ms ?
		snake.turningSpeed  = 0.0015707963267949; // Est égal a 0.09 rad/ms?
		snake.state 		= true;
		snake.collision 	= true;
		snake.inversion     = false;
		snake.fly   		= false;
		snake.holeRate 	    = 0.05;			// 5% ??	
		System.out.println("Angle en degré : " + Double.toString(snake.ourBoard.snakes[i].currentAngle));	
	}
	
	
	/**
	 * @param width 
	 * @param height
	 * @return
	 */
	public Position generateSnakeSpawnPos(int width, int height){
		
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
		long alterableTime;
		double pixel;
		boolean isMoving = false;
		Position pos = new Position(0,0);
		
		for (int i = 0; i < ourBoard.snakes.length ; i++){
			alterableTime = time;
			pixel = 0;
						
			while(ourBoard.snakes[i].state && alterableTime > 0){   // SI SNAKE EST EN VIE & number of pixel moved
				while(alterableTime > 0 && pixel < 1){ 	// DIMINUE LE TEMPS TANT QU'ON A PAS FAIT UN PIXEL DE MOUVEMENTT 
					alterableTime--;
					pixel += ourBoard.snakes[i].movingSpeed;
				}
				// COMPARAISON entre snakeTable & ourBoard.snakes[i].current angle
				
				if (snakeTable[ourBoard.snakes[i].profileId][0] > Math.cos(Math.toRadians(snake.ourBoard.snakes[i].currentAngle))){
				     
				     ourBoard.snakes[i].currentX--; 
				     
				}
				else if ( snakeTable[ourBoard.snakes[i].profileId][0] < Math.cos(Math.toRadians(snake.ourBoard.snakes[i].currentAngle)) {
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
				
				     ---------------------------------------------------
				if (snakeTable[ourBoard.snakes[i].profileId][1] > Math.sin(Math.toRadians(snake.ourBoard.snakes[i].currentAngle))){
				     
				     ourBoard.snakes[i].currentY--; 
				     
				}
				else if ( snakeTable[ourBoard.snakes[i].profileId][1] < Math.sin(Math.toRadians(snake.ourBoard.snakes[i].currentAngle)) {
				     ourBoard.snakes[i].currentY++;
				    
				}
				
				else{
				     if (snakeTable[ourBoard.snakes[i].profileId][1] > 0){
				          ourBoard.snakes[i].currentY++;
				     }
				     
				     else if (snakeTable[ourBoard.snakes[i].profileId][1] < 0){
				          ourBoard.snakes[i].currentY--;
				     }
				}
				/*snakeTable[ourBoard.snakes[i].profileId][0] += Math.cos(Math.toRadians(ourBoard.snakes[i].ourBoard.snakes[i].currentAngle)); 	
				snakeTable[ourBoard.snakes[i].profileId][1] += Math.sin(Math.toRadians(ourBoard.snakes[i].ourBoard.snakes[i].currentAngle));

				// TESTS ANGLES COS && SIN
				if(snakeTable[ourBoard.snakes[i].profileId][0] >= 1 && snakeTable[ourBoard.snakes[i].profileId][1] >= 1) {
					ourBoard.snakes[i].ourBoard.snakes[i].currentY++;
					ourBoard.snakes[i].ourBoard.snakes[i].currentX++;
					pos.x = ourBoard.snakes[i].ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]--;
					snakeTable[ourBoard.snakes[i].profileId][0]--;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][1] <= -1 && snakeTable[ourBoard.snakes[i].profileId][0] >= 1) {
					ourBoard.snakes[i].ourBoard.snakes[i].currentY--;
					ourBoard.snakes[i].ourBoard.snakes[i].currentX++;
					pos.x = ourBoard.snakes[i].ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]++;
					snakeTable[ourBoard.snakes[i].profileId][0]--;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][1] <= -1 && snakeTable[ourBoard.snakes[i].profileId][0] <= -1) {
					ourBoard.snakes[i].ourBoard.snakes[i].currentY--;
					ourBoard.snakes[i].ourBoard.snakes[i].currentX--;
					pos.x = ourBoard.snakes[i].ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]++;
					snakeTable[ourBoard.snakes[i].profileId][0]++;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][1] >= 1 && snakeTable[ourBoard.snakes[i].profileId][0] <= -1) {
					ourBoard.snakes[i].ourBoard.snakes[i].currentY++;
					ourBoard.snakes[i].ourBoard.snakes[i].currentX--;
					pos.x = ourBoard.snakes[i].ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]--;
					snakeTable[ourBoard.snakes[i].profileId][0]++;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][1] >= 1) {
					ourBoard.snakes[i].ourBoard.snakes[i].currentY++;
					pos.x = ourBoard.snakes[i].ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]--;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][1] <= -1) {
					ourBoard.snakes[i].ourBoard.snakes[i].currentY--;
					pos.x = ourBoard.snakes[i].ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][1]++;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][0] >= 1) {
					ourBoard.snakes[i].ourBoard.snakes[i].currentX++;
					pos.x = ourBoard.snakes[i].ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][0]--;
					isMoving = true;
				}
				else if(snakeTable[ourBoard.snakes[i].profileId][0] <= -1) {
					ourBoard.snakes[i].ourBoard.snakes[i].currentX--;
					pos.x = ourBoard.snakes[i].ourBoard.snakes[i].currentX;
					pos.y = ourBoard.snakes[i].ourBoard.snakes[i].currentY;
					ourBoard.snakesMap.put(pos , ourBoard.snakes[i].playerId);
					snakeTable[ourBoard.snakes[i].profileId][0]++;
					isMoving = true;
				}*/

				pixel --;
				System.out.println("Position snake "+ Integer.toString(ourBoard.snakes[i].playerId)+ " x:" + Integer.toString(ourBoard.snakes[i].ourBoard.snakes[i].currentX) + " y:" + Integer.toString(ourBoard.snakes[i].ourBoard.snakes[i].currentY));

				/*if(isMoving) {    // tests de collision
					fillSnakeHead(ourBoard.snakes[i]);
					outOfBounds(ourBoard.snakes[i]);
					snakeVsSnake(ourBoard.snakes[i]);
					snakeVsItem(ourBoard.snakes[i],pos);
				}*/
			}
		}
	}
	
	
	/**
	 * @param time
	 * @param commands
	 */
	public void updateSnakesDirections(long time, Map<Integer,Direction> commands){
		
	}
	
	
	
	
	
	@Override
	public void forceUpdate(Board board) {
		this.ourBoard = board;
	}
	
	
}


