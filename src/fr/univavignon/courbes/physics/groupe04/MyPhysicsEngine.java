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
		snake.currentItems  = new HashMap<Item, Long>() ;
		snake.playerId 	    = id;
		snake.currentX      = spawnPosition.x;
		snake.currentY      = spawnPosition.y;
		snake.currentAngle  = (int)(Math.random() * 359); //Génération aléatoire d'un angle entre 0 et 359°
		snake.headRadius 	= 3;  					// 3px ?
		snake.movingSpeed   = 10;					// 1px / ms ?
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
		for (int i = 0; i < ourBoard.snakes.length ; i++){
			if(ourBoard.snakes[i].state){   // SI SNAKE EST EN VIE
				
			/*	snakeTable[ourBoard.snakes[i].playerId][0] += Math.cos(Math.toRadians(ourBoard.snakes[i].currentAngle));
				snakeTable[ourBoard.snakes[i].playerId][1] += Math.sin(Math.toRadians(ourBoard.snakes[i].currentAngle));
				*/
				ourBoard.snakes[i].currentX += (int)ourBoard.snakes[i].movingSpeed*time;
				ourBoard.snakes[i].currentY += (int)ourBoard.snakes[i].movingSpeed*time;
				System.out.println("Snake " + ourBoard.snakes[i].playerId + " s'est déplacé a la position " + ourBoard.snakes[i].currentX + " "+ourBoard.snakes[i].currentY );
			}
		}
	}
	
	public void updateSnakesDirections(long time, Map<Integer,Direction> commands){
		
	}
	
	@Override
	public void forceUpdate(Board board) {
		this.ourBoard = board;
	}
	
	
}


