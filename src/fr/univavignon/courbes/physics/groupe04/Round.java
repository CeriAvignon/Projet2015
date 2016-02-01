package fr.univavignon.courbes.physics.groupe04;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.ItemType;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.PhysicsEngine;

/**
 * @author Castillo Quentin
 * @author Latif Alexandre
 */
public class Round implements PhysicsEngine {

	/** Represente le plateau de jeu de la manche courante **/
	public Board board;
	/** Représente les coordonnées aprés la virgule de la position d'un snake **/
	private double deltaSnake[][]; 
	/** Représente la valeur du nombre de pixel que le snake peut parcourir, utile au déplacement **/
	private double[] pixStep; 
	/** Sert a retrouver l'id continu de chaque snake par rapport a l'id du profile (ex : 0->30, 1->46,..)**/
	private Map<Integer, Integer> deltaID;
	/** Represente la chance qu'un item apparaisse sur le plateau **/
	private double itemRate = 1;
	/** Represente une valeur qui augmente et qui fait spawn un objet quand elle arrive a 1 **/
	private double itemStack = 0;
	/** Represente le nombre de 'ms' avant le prochain spawn d'item, depend aussi de itemRate **/
	private double itemTick;
	/** Rayon d'un item **/
	private int radItem = 20;
	/** Represente à quel moment le snake 'n' va faire un trou **/
	private Map<Integer, Integer> holeTick;
	/** Represente combien de déplacement le snake 'n' a effectué **/
	private Map<Integer, Integer> moveCount;
	/** Represente la durée durant laquelle les snakes ne peuvent pas avoir de collisions au début du round (en ms)**/
	private int invincibleTime = 2000;
	/** Contient le coordonées temporaires de la tête à afficher pour le MG si le snake dessine un trou **/
	private Map<Position, Integer> tempHead;
	/** Est vrai si le snake à dessiné une tête temporaire **/
	private Map<Integer, Boolean> isTempHead;

	@Override
	public Board init(int width, int height, int[] profileIds) {

		int playerNbr = profileIds.length;
		deltaSnake = new double[playerNbr][2];
		pixStep = new double[playerNbr];
		holeTick = new HashMap<Integer, Integer>();
		moveCount = new HashMap<Integer, Integer>();
		deltaID = new HashMap<Integer, Integer>();
		tempHead = new HashMap<Position, Integer>();
		isTempHead = new HashMap<Integer, Boolean>();
		itemTick = 7000 +(int)(Math.random() * 13000);
		board = new Board();
		board.width = width;
		board.height = height;
		board.snakesMap = new HashMap<Position, Integer>();
		board.itemsMap = new HashMap<Position, ItemType>();
		board.snakes = new Snake[playerNbr];
		Position posSpawn;

		for (int i = 0; i < playerNbr ; i++) 
		{
			posSpawn = snakeSpawnPos(width, height);
			board.snakes[i] = new Snake();
			deltaID.put(profileIds[i], i);
			isTempHead.put(profileIds[i], false);
			initSnake(board.snakes[i], profileIds[i] , posSpawn);
			System.out.println("Snake " + Integer.toString(i) + " spawn a la position x:" + Integer.toString(posSpawn.x) + "  y:"+Integer.toString(posSpawn.y));
		}
		return board;
	}

	/**
	 * Cette fonction initialise les données physiques d'un snake présent sur la board.
	 * 
	 * @param snake Snake à initialiser
	 * @param id Id du Snake
	 * @param spawnPosition Position ou spawn le snake sur la board
	 */
	public void initSnake(Snake snake, int id, Position spawnPosition) {
		snake.currentItems  = new HashMap<ItemType, Long>() ;
		snake.playerId 	    = id;
		snake.currentX      = spawnPosition.x;
		snake.currentY      = spawnPosition.y;
		snake.currentAngle  = (int)(Math.random() * 359); //Génération aléatoire d'un angle entre 0 et 359°
		snake.headRadius 	= Constants.BASE_HEAD_RADIUS;
		snake.movingSpeed   = Constants.BASE_MOVING_SPEED;
		snake.turningSpeed  = Constants.BASE_TURNING_SPEED;
		snake.alive 		= true;
		snake.collision 	= true;
		snake.inversion     = false;
		snake.fly   		= false;
		snake.holeRate 	    = 0.25;	
		holeTick.put(snake.playerId , (int)(Math.random() * 100 - snake.holeRate*100));
		moveCount.put(snake.playerId  , 0);
		System.out.println("Angle en degré : " + Double.toString(snake.currentAngle));	
	}


	@Override
	public void update(long elapsedTime, Map<Integer, Direction> commands) {

		// Mise à jour du temps d'invincibilité de début de round
		if(invincibleTime > 0)
			invincibleTime -= elapsedTime;
		// Mise à jour des coordonnées des snakes
		majSnakesPositions(elapsedTime);
		// Mise à jour des directions des snakes
		majSnakesDirections(elapsedTime, commands);
		// Mise à jour des effets liés aux snakes
		majSnakesEffects(elapsedTime);
		// Mise à jour du prochain spawn d'item
		majSpawnItem(elapsedTime);
	}

	/**
	 * Cette fonction augmente une valeur en fonction du taux de spawn d'un item,
	 * si ce seuil est passé, une fonction ajoute un item sur la map.
	 * @param elapsedTime Temps écoulé depuis la dernière mise à jour, exprimé en ms.
	 */
	public void majSpawnItem(long elapsedTime) {
		itemStack += elapsedTime*itemRate;
		if(itemStack >= itemTick) {
			spawnRandomItem();
			itemStack = 0;
			itemTick = 5000 +(int)(Math.random() * 13000);
		}
	}


	/**
	 *  Crée un item
	 */
	public void spawnRandomItem() {

		boolean flgSpawn = false;
		do {
			int itCenterX = (int)( Math.random()*( (board.height - radItem) - radItem + 1 ) ) + radItem;
			int itCenterY = (int)( Math.random()*( (board.width - radItem) - radItem + 1 ) ) + radItem;
			Position posC = new Position(itCenterX, itCenterY); // Coordonnée du centre de l'item
			if(board.snakesMap.get(posC) == null) {
				flgSpawn = true;
				ItemType randItem = ItemType.values()[(int) (Math.random() * ItemType.values().length)];
				board.itemsMap.put(posC, randItem);
				System.out.println(randItem.toString() + " ajouté a la pos: " + posC.x + "  " + posC.y);
			}
		} while (flgSpawn == false);
	}



	/**
	 * @param elapsedTime Temps écoulé depuis la dernière mise à jour de la board (ms)
	 */
	public void majSnakesEffects(long elapsedTime) {

		for(Snake snake : board.snakes)
		{
			for (Iterator<Entry<ItemType, Long>> i = snake.currentItems.entrySet().iterator(); i.hasNext(); ) {

				Entry<ItemType, Long> entry = i.next();
				long remainingTime = entry.getValue();
				long refreshedTime = remainingTime - elapsedTime;

				// Enlever l'effet et supprimer l'objet de la liste
				if (refreshedTime <= 0 ) {
					removeSnakeItem(snake.playerId, entry.getKey(), i);
					System.out.println("Le snake " + snake.playerId + " perd l'effet de l'item");
				}
				// Mettre à jour le temps restant pour l'effet de l'Item
				else if (refreshedTime > 0) {
					snake.currentItems.put(entry.getKey(), refreshedTime);
				}
			}
		}
	}


	/**
	 * Génére une position aléatoire sur la plateau, la fonction générera une position qui n'est pas
	 * trop rapproché des bords du plateau et verifiera qu'elle n'est pas trop proche de la position
	 * d'un autre snake.
	 *
	 * @param widthBoard Largeur de l'aire de jeu, exprimée en pixel.
	 * @param heightBoard Hauteur de l'aire de jeu, exprimée en pixel.
	 * @return La position généré aléatoirement
	 */
	/**				VALIDÉ
	 * @param width Longueur du board
	 * @param height Largeur du board
	 * @return Renvoi une position (aléatoire) 
	 */
	public Position snakeSpawnPos(int width, int height){
		
		Random r = new Random();
		// Création position avec deux paramétres aléatoires et avec une marge de 20px pour éviter de spawn sur les bords
		Position pos = new Position((r.nextInt((width-20)-20)+ 20), (r.nextInt((height-20)-20)+ 20));
		return pos;
	}


	/**
	 * @param elapsedTime Temps écoulé depuis la dernière mise à jour de la board (ms)
	 */
	public void majSnakesPositions(long elapsedTime) {
		long elapsed;
		boolean snakeMove = false;
		Position pos = null;
		for(Snake snake : board.snakes)
		{
			int id = deltaID.get(snake.playerId);
			elapsed = elapsedTime;
			while (elapsed > 0 && snake.alive == true)
			{

				/** Gestion de la future position du snake en fonction de son angle **/
				while(pixStep[id] < 1 && elapsed > 0) {
					elapsed--;
					pixStep[id] += snake.movingSpeed;
				}
				if(pixStep[id] >= 1) { 

					deltaSnake[id][0] += Math.cos(Math.toRadians(snake.currentAngle));
					deltaSnake[id][1] += Math.sin(Math.toRadians(snake.currentAngle));

					if(deltaSnake[id][1] >= 1 && deltaSnake[id][0] >= 1) {
						snake.currentY--;
						snake.currentX++;
						pos = new Position(snake.currentX, snake.currentY);
						deltaSnake[id][1]--;
						deltaSnake[id][0]--;
						moveCount.put(snake.playerId, moveCount.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(deltaSnake[id][1] <= -1 && deltaSnake[id][0] >= 1) {
						snake.currentY++;
						snake.currentX++;
						pos = new Position(snake.currentX, snake.currentY);
						deltaSnake[id][1]++;
						deltaSnake[id][0]--;
						moveCount.put(snake.playerId, moveCount.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(deltaSnake[id][1] <= -1 && deltaSnake[id][0] <= -1) {
						snake.currentY++;
						snake.currentX--;
						pos = new Position(snake.currentX, snake.currentY);
						deltaSnake[id][1]++;
						deltaSnake[id][0]++;
						moveCount.put(snake.playerId, moveCount.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(deltaSnake[id][1] >= 1 && deltaSnake[id][0] <= -1) {
						snake.currentY--;
						snake.currentX--;
						pos = new Position(snake.currentX, snake.currentY);
						deltaSnake[id][1]--;
						deltaSnake[id][0]++;
						moveCount.put(snake.playerId, moveCount.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(deltaSnake[id][1] >= 1) {
						snake.currentY--;
						pos = new Position(snake.currentX, snake.currentY);
						deltaSnake[id][1]--;
						moveCount.put(snake.playerId, moveCount.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(deltaSnake[id][1] <= -1) {
						snake.currentY++;
						pos = new Position(snake.currentX, snake.currentY);
						deltaSnake[id][1]++;
						moveCount.put(snake.playerId, moveCount.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(deltaSnake[id][0] >= 1) {
						snake.currentX++;
						pos = new Position(snake.currentX, snake.currentY);
						deltaSnake[id][0]--;
						moveCount.put(snake.playerId, moveCount.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(deltaSnake[id][0] <= -1) {
						snake.currentX--;
						pos = new Position(snake.currentX, snake.currentY);
						deltaSnake[id][0]++;
						moveCount.put(snake.playerId, moveCount.get(snake.playerId)+1);
						snakeMove = true;
					}

					pixStep[id] --;

					if(snakeMove) {
						if(isTempHead.get(snake.playerId) == true) {
							clearTempHead(snake);
							isTempHead.put(snake.playerId, false);
						}
						if ((moveCount.get(snake.playerId) <= holeTick.get(snake.playerId)
								|| moveCount.get(snake.playerId) > holeTick.get(snake.playerId) + snake.holeRate*100)
								&& invincibleTime <= 0) {
							board.snakesMap.put(pos , snake.playerId);
							System.out.println("Snake "+ snake.playerId+ " X:" + snake.currentX + "  Y:" + snake.currentY);
							fillSnakeHead(snake);
						}
						else {
							fillTempHead(snake);
							System.out.println("Snake " + snake.playerId + " vient de faire un trou");
						}
						
						if(invincibleTime <= 0){
							snakeEncounterSnake(snake);
							snakeEncounterBounds(snake);
						}	
						snakeEncounterItem(snake);
						if(moveCount.get(snake.playerId) == 100) {
							refreshSnakeHoleTick(snake);
						}
					}
				}
			}
		}
	}

	/**
	 * @param snake Snake à mettre à jour
	 */
	public void refreshSnakeHoleTick(Snake snake) {
		moveCount.put(snake.playerId, 0);
		holeTick.put(snake.playerId, (int)(Math.random() * (100 - (snake.holeRate*100))));
	}

	/**.
	 * @param snake Snake à tester
	 */
	public void snakeEncounterBounds(Snake snake) {
		if(snake.currentX < 1 || snake.currentX > board.width-1 || snake.currentY < 1|| snake.currentY > board.height-1) {
			if (!snake.fly) { 
				snake.alive = false; 
				System.out.println(snake.playerId + " n'a pas vu le mur!");
			} 
			else { // Envoyer le snake a l'opposé
				if(snake.currentX < 0)
					snake.currentX = board.width;
				else if(snake.currentX > board.width)
					snake.currentX = 0;
				if(snake.currentY < 0)
					snake.currentY = board.height;
				else if(snake.currentY > board.height)
					snake.currentY = 0;
			}	
		}
	}

	/**
	 * @param snake Snake à tester
	 */
	public void snakeEncounterSnake(Snake snake) {

		int hitBox[][] = new int[3][2];
		hitBox[0][0] = snake.currentX + (int) ((snake.headRadius+3) * Math.cos(Math.toRadians(snake.currentAngle)));
		hitBox[0][1] = snake.currentY - (int) ((snake.headRadius+3) * Math.sin(Math.toRadians(snake.currentAngle)));
		hitBox[1][0] = snake.currentX + (int) ((snake.headRadius+3) * Math.cos(Math.toRadians(snake.currentAngle + 75)));
		hitBox[1][1] = snake.currentY - (int) ((snake.headRadius+3) * Math.sin(Math.toRadians(snake.currentAngle + 75)));
		hitBox[2][0] = snake.currentX + (int) ((snake.headRadius+3) * Math.cos(Math.toRadians(snake.currentAngle - 75)));
		hitBox[2][1] = snake.currentY - (int) ((snake.headRadius+3) * Math.sin(Math.toRadians(snake.currentAngle - 75)));

		for(int i = 0; i < 3; i++) {
			Position posChk = new Position(hitBox[i][0], hitBox[i][1]);
			Integer flg = board.snakesMap.get(posChk);
			if(flg != null) {
				snake.alive = false;	
				System.out.println("snake " + snake.playerId + " a dit bonjour à " + flg);
			}
		}
	}


	/**
	 * @param snake Snake à tester
	 */
	public void snakeEncounterItem(Snake snake) {
		Position posItem = new Position(0,0);
		for (Iterator<Entry<Position, ItemType>> i = board.itemsMap.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry<Position,ItemType> entry = i.next();
			posItem = entry.getKey(); 
			if(Math.sqrt(Math.pow(posItem.x - snake.currentX, 2) + Math.pow(posItem.y - snake.currentY, 2)) < radItem + snake.headRadius)// Detecte si le snake passe dans le rayon de l'objet
			{
				addSnakeItem(snake.playerId, entry.getValue()); // Ajoute l'item et l'effet
				i.remove(); // Suppression item
				System.out.println("Snake "+snake.playerId+" <-> item");
			}
		}
	}

	/**
	 * @param elapsedTime Temps écoulé depuis la dernière mise à jour de la board (ms)
	 * @param commands Collection des différentes commandes demandés pour chaque snake 
	 */
	public void majSnakesDirections(long elapsedTime, Map<Integer, Direction> commands)
	{
		Direction direction;
		for(Snake snake : board.snakes)
		{
			direction = commands.get(snake.playerId);
			if(direction != null)
			{
				switch (direction)
				{
				case LEFT:
					if(!snake.inversion)
						snake.currentAngle += elapsedTime*Math.toDegrees(snake.turningSpeed);
					else
						snake.currentAngle -= elapsedTime*Math.toDegrees(snake.turningSpeed);
					break;
				case RIGHT:
					if(!snake.inversion)
						snake.currentAngle -= elapsedTime*Math.toDegrees(snake.turningSpeed);
					else
						snake.currentAngle += elapsedTime*Math.toDegrees(snake.turningSpeed);
					break;
				case NONE:
					break;
				default:
					break;
				}
			}
		}
	}

	/**
	 * @param snake Snake dont la trace doit être marqué.
	 */
	public void fillSnakeHead(Snake snake) {
		Position pos;
		for(int i = snake.currentX - (int)snake.headRadius; i < snake.currentX + (int)snake.headRadius ; i++) {
			for(int j = snake.currentY - (int)snake.headRadius; j < snake.currentY + (int)snake.headRadius ; j++) {
				if(Math.sqrt(Math.pow(i - snake.currentX, 2) + Math.pow(j - snake.currentY, 2)) < (int)snake.headRadius) {
					pos = new Position(i,j);
					if(board.snakesMap.get(pos) == null) {
						board.snakesMap.put(pos , snake.playerId);
					}	
				}
			}
		}
	}

	/**
	 * @param snake Snake dont la trace doit être marqué.
	 */
	public void fillTempHead(Snake snake) {
		Position pos;
		for(int i = snake.currentX - (int)snake.headRadius; i < snake.currentX + (int)snake.headRadius ; i++) {
			for(int j = snake.currentY - (int)snake.headRadius; j < snake.currentY + (int)snake.headRadius ; j++) {
				if(Math.sqrt(Math.pow(i - snake.currentX, 2) + Math.pow(j - snake.currentY, 2)) < (int)snake.headRadius) {
					pos = new Position(i,j);
					if(board.snakesMap.get(pos) == null) {
						board.snakesMap.put(pos , snake.playerId);
						tempHead.put(pos, snake.playerId);
					}
				}
			}
		}
		isTempHead.put(snake.playerId, true);
	}

	
	/**
	 * @param snake Snake dont la trace doit être marqué.
	 */
	public void clearTempHead(Snake snake) {
		for (Iterator<Entry<Position, Integer>> i = tempHead.entrySet().iterator(); i.hasNext(); ) {
			Entry<Position, Integer> entry = i.next();
			if(entry.getValue() == snake.playerId) {
				board.snakesMap.remove(entry.getKey());
				i.remove();
			}
		}
	}

	@Override
	public void forceUpdate(Board board) {

		this.board = board;

	}

	/**
	 * @param idProfile Id du Snake ayant ramassé l'objet
	 * @param item Item ramassé
	 */
	public void addSnakeItem(int idProfile, ItemType item) {
		int id = deltaID.get(idProfile);
		switch(item)
		{
		case COLLECTIVE_CLEAN:
			board.snakesMap.clear();
			break;
		case COLLECTIVE_TRAVERSE:
			for(Snake snake : board.snakes) {
				snake.currentItems.put(item, (long)item.duration);
				snake.fly = true;
			}
			break;
		case COLLECTIVE_WEALTH:
			board.snakes[id].currentItems.put(item, (long)item.duration);
			itemRate = 3;
			break;
		case OTHERS_REVERSE:
			for(Snake snake : board.snakes) {
				if (snake.playerId != idProfile) {
					snake.currentItems.put(item, (long)item.duration);
					snake.inversion = true;
				}
			}
			break;
		case OTHERS_SLOW:
			for(Snake snake : board.snakes) {
				if (snake.playerId != idProfile) {
					snake.currentItems.put(item, (long)item.duration);
					snake.movingSpeed = Constants.SLOW_MOVING_SPEED;
					snake.turningSpeed = Constants.FAST_TURNING_SPEED;
				}
			}
			break;
		case OTHERS_THICK:
			for(Snake snake : board.snakes) {
				if (snake.playerId != idProfile) {
					snake.currentItems.put(item, (long)item.duration);
					snake.headRadius = Constants.LARGE_HEAD_RADIUS;
				}
			}
			break;
		case OTHERS_FAST:
			for(Snake snake : board.snakes) {
				if (snake.playerId != idProfile) {
					snake.currentItems.put(item, (long)item.duration);
					snake.movingSpeed = Constants.FAST_MOVING_SPEED;
				}
			}
			break;
		case USER_FLY:
			board.snakes[id].currentItems.put(item, (long)item.duration);
			board.snakes[id].holeRate *= 2;
			break;
		case USER_SLOW:
			board.snakes[id].currentItems.put(item, (long)item.duration);
			board.snakes[id].movingSpeed = Constants.SLOW_MOVING_SPEED;
			board.snakes[id].turningSpeed = Constants.FAST_TURNING_SPEED;
			break;
		case USER_FAST:
			board.snakes[id].currentItems.put(item, (long)item.duration);
			board.snakes[id].movingSpeed = Constants.FAST_MOVING_SPEED;
			break;
		default:
			break;
		}
	}

	/**
	 * Supprime l'item ainsi que l'effet relatif à l'item ramassé au snake concerné.
	 * 
	 * @param idProfile Id du Snake concerné
	 * @param item Item à enlever du snake
	 * @param i test
	 */ 
	public void removeSnakeItem(int idProfile, ItemType item, Iterator<Entry<ItemType, Long>> i) {
		int id = deltaID.get(idProfile);
		switch(item)
		{
		case COLLECTIVE_CLEAN:
			break;
		case COLLECTIVE_TRAVERSE:
			i.remove();
			board.snakes[id].fly = false;
			break;
		case COLLECTIVE_WEALTH:
			i.remove();
			itemRate = 1;
			break;
		case OTHERS_REVERSE:
			i.remove();
			board.snakes[id].inversion = false;
			break;
		case OTHERS_SLOW:
			i.remove();
			board.snakes[id].movingSpeed = Constants.BASE_MOVING_SPEED;
			board.snakes[id].turningSpeed = Constants.BASE_TURNING_SPEED;
			break;
		case OTHERS_THICK:
			i.remove();
			board.snakes[id].headRadius = Constants.BASE_HEAD_RADIUS;
			break;
		case OTHERS_FAST:
			i.remove();
			board.snakes[id].movingSpeed = Constants.BASE_MOVING_SPEED;
			break;
		case USER_FLY:
			i.remove();
			board.snakes[id].holeRate /= 2;
			break;
		case USER_SLOW:
			i.remove();
			board.snakes[id].movingSpeed = Constants.BASE_MOVING_SPEED;
			board.snakes[id].turningSpeed = Constants.BASE_TURNING_SPEED;
			break;
		case USER_FAST:
			i.remove();
			board.snakes[id].movingSpeed = Constants.BASE_MOVING_SPEED;
			break;
		default:
			break;
		}
	}

	
	@Override
	public Board initDemo(int width, int height, int[] profileIds) {
		int playerNbr = 2;
		if(profileIds.length != 2){
			System.out.println("2 profiles requis");
			return null;
		}
		deltaSnake = new double[playerNbr][2];
		pixStep = new double[playerNbr];
		holeTick = new HashMap<Integer, Integer>();
		moveCount = new HashMap<Integer, Integer>();
		deltaID = new HashMap<Integer, Integer>();
		tempHead = new HashMap<Position, Integer>();
		isTempHead = new HashMap<Integer, Boolean>();
		itemTick = 7000 +(int)(Math.random() * 13000);
		board = new Board();
		board.width = width;
		board.height = height;
		board.snakesMap = new HashMap<Position, Integer>();
		board.itemsMap = new HashMap<Position, ItemType>();
		board.snakes = new Snake[playerNbr];
		Position posSpawn;

		for (int i = 0; i < playerNbr ; i++) 
		{
			posSpawn = snakeSpawnPos(width, height);
			board.snakes[i] = new Snake();
			deltaID.put(profileIds[i], i);
			isTempHead.put(profileIds[i], false);
			initSnake(board.snakes[i], profileIds[i] , posSpawn);
			System.out.println("Snake " + Integer.toString(i) + " spawn a la position x:" + Integer.toString(posSpawn.x) + "  y:"+Integer.toString(posSpawn.y));
		}

		/** Spawn de tout les items du jeu **/
		ItemType[] itemTab = new ItemType[]{ItemType.USER_SLOW, ItemType.USER_FAST, ItemType.OTHERS_FAST, ItemType.OTHERS_SLOW,
				ItemType.OTHERS_REVERSE, ItemType.OTHERS_THICK, ItemType.USER_FLY, ItemType.COLLECTIVE_WEALTH,
				ItemType.COLLECTIVE_CLEAN, ItemType.COLLECTIVE_TRAVERSE};
		board.itemsMap.put(new Position(100,100), itemTab[0] );
		board.itemsMap.put(new Position(200,100), itemTab[1] );
		board.itemsMap.put(new Position(300,100), itemTab[2] );
		board.itemsMap.put(new Position(400,100), itemTab[3] );
		board.itemsMap.put(new Position(100,200), itemTab[4] );
		board.itemsMap.put(new Position(100,300), itemTab[5] );
		board.itemsMap.put(new Position(100,400), itemTab[6] );
		board.itemsMap.put(new Position(200,200), itemTab[7] );
		board.itemsMap.put(new Position(300,300), itemTab[8] );
		board.itemsMap.put(new Position(300,300), itemTab[9] );
		return board;
	}


}



