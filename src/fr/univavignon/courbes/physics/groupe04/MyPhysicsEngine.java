package fr.univavignon.courbes.physics.groupe04;

import java.awt.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.PhysicsEngine;


public Board ourBoard;

private double snakeTable[];



public class MyPhysicsEngine implements PhysicsEngine{

	@Override
	public Board init(int width, int height, int[] profileIds) {
	
	int playerNbr = profileIds.length;
	snakeTable = new double[playerNbr];
	
	/* initialisation Board */
	ourBoard = new Board ();
	ourBoard.width = width;
	ourBoard.height = height;
	ourBoard.snakes = new Snake[playerNbr];
	ourBoard.snakesMap = new HashMap<Position, Integer>();
	ourBoard.itemsMap = new HashMap<Position, Item>();
		
	
	
	
	
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
	
	
	
	
	
	
	
	
	
	
	
		
		/*int playerNbr = profileIds.length;
		deltaSnake = new double[playerNbr][2];

		board = new Board();
		board.width = width;
		board.height = height;
		board.snakesMap = new HashMap<Position, Integer>();
		board.itemsMap = new HashMap<Position, Item>();
		board.snakes = new Snake[playerNbr];
		Position posSpawn;

		for (int i = 0; i < playerNbr ; i++) 
		{
			posSpawn = generateSnakeSpawnPos(width, height);
			board.snakes[i] = new Snake();
			initSnake(board.snakes[i], profileIds[i] , posSpawn);
			System.out.println("Snake " + Integer.toString(i) + " spawn a la position " + Integer.toString(posSpawn.x) + " "+Integer.toString(posSpawn.y));
		}
		return board;  */
	}

	@Override
	public void update(long elapsedTime, Map<Integer, Direction> commands) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forceUpdate(Board board) {
		this.board = board;
	}
	
	
}


