package fr.univavignon.courbes.physics.groupe16;


import java.util.HashMap;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.PhysicsEngine;

public class Round implements PhysicsEngine {

	/** Represente le plateau de jeu de la manche courante **/
	public Board board;
	/** Représente les coordonnées aprés la virgule de la position d'un snake **/
	private double deltaSnake[][]; 
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
	
	@Override
	public Board init(int width, int height, int[] profileIds) {

		int playerNbr = profileIds.length;
		deltaSnake = new double[playerNbr][2];
		holeTick = new HashMap<Integer, Integer>();
		moveCount = new HashMap<Integer, Integer>();
		deltaID = new HashMap<Integer, Integer>();
		itemTick = 7000 +(int)(Math.random() * 13000);
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
			deltaID.put(profileIds[i], i);
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
		snake.currentItems  = new HashMap<Item, Long>() ;
		snake.playerId 	    = id;
		snake.currentX      = spawnPosition.x;
		snake.currentY      = spawnPosition.y;
		snake.currentAngle  = (int)(Math.random() * 359); //Génération aléatoire d'un angle entre 0 et 359°
		snake.headRadius 	= Constants.REGULAR_HEAD_RADIUS;
		snake.movingSpeed   = Constants.REGULAR_MOVING_SPEED;
		snake.turningSpeed  = Constants.REGULAR_TURNING_SPEED;
		snake.state 		= true;
		snake.collision 	= true;
		snake.inversion     = false;
		snake.fly   		= false;
		snake.holeRate 	    = 0.1;	
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
	 * Ajoute un item aléatoire sur la board à une position aléatoire 
	 * et vérifie que cette position ne se trouve pas sur un tracé de Snake,
	 * sinon elle regénére une position
	 */
	public void spawnRandomItem() {

		boolean flgSpawn = false;
		do {
			int itCenterX = radItem + (int)(Math.random() * board.height - radItem); 
			int itCenterY = radItem + (int)(Math.random() * board.width - radItem); 
			Position posC = new Position(itCenterX, itCenterY); // Coordonnée du centre de l'item
			if(board.snakesMap.get(posC) == null) {
				flgSpawn = true;
				Item randItem = Item.values()[(int) (Math.random() * Item.values().length)];
				board.itemsMap.put(posC, randItem);
				System.out.println(randItem.toString() + " ajouté a la pos: " + posC.x + "  " + posC.y);
			}
		} while (flgSpawn == false);
	}

	/**
	 * Ajoute l'item ainsi que l'effet relatif à l'item ramassé aux snakes concernés.
	 * 
	 * @param id Id du Snake ayant ramassé l'objet
	 * @param item Item ramassé
	 */
	public void addSnakeItem(int id, Item item) {
		switch(item)
		{
		case COLLECTIVE_ERASER:
			board.snakesMap.clear();
			
			break;
		case COLLECTIVE_TRAVERSE_WALL:
			for(Snake snake : board.snakes) {
				snake.currentItems.put(item, (long)item.duration);
				snake.collision = false;
			}
			break;
		case COLLECTIVE_THREE_CIRCLES:
			itemRate *= 3;
			break;
		case OTHERS_REVERSE:
			for(Snake snake : board.snakes) {
				if (snake.playerId != id) {
					snake.currentItems.put(item, (long)item.duration);
					snake.inversion = true;
				}
			}
			break;
		case OTHERS_SLOW:
			for(Snake snake : board.snakes) {
				if (snake.playerId != id) {
					snake.currentItems.put(item, (long)item.duration);
					snake.movingSpeed = Constants.SLOW_MOVING_SPEED;
					snake.turningSpeed = Constants.FAST_TURNING_SPEED;
				}
			}
			break;
		case OTHERS_THICK:
			for(Snake snake : board.snakes) {
				if (snake.playerId != id) {
					snake.currentItems.put(item, (long)item.duration);
					snake.headRadius = Constants.LARGE_HEAD_RADIUS;
				}
			}
			break;
		case OTHERS_SPEED:
			for(Snake snake : board.snakes) {
				if (snake.playerId != id) {
					snake.currentItems.put(item, (long)item.duration);
					snake.movingSpeed = Constants.FAST_MOVING_SPEED;
					snake.turningSpeed = Constants.SLOW_TURNING_SPEED;
				}
			}
			break;
		case USER_BIG_HOLE:
			board.snakes[id].currentItems.put(item, (long)item.duration);
			board.snakes[id].holeRate /= 2;
			break;
		case USER_SLOW:
			board.snakes[id].currentItems.put(item, (long)item.duration);
			board.snakes[id].movingSpeed = Constants.SLOW_MOVING_SPEED;
			board.snakes[id].turningSpeed = Constants.FAST_TURNING_SPEED;
			break;
		case USER_SPEED:
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
	 * @param id Id du Snake concerné
	 * @param item Item à enlever du snake
	 */
	public void removeSnakeItem(int id, Item item) {
		switch(item)
		{
		case COLLECTIVE_ERASER:
			board.snakesMap.clear();
			break;
		case COLLECTIVE_TRAVERSE_WALL:
			board.snakes[id].currentItems.remove(item);
			board.snakes[id].collision = true;
			break;
		case COLLECTIVE_THREE_CIRCLES:
			itemRate /= 3;
			break;
		case OTHERS_REVERSE:
			board.snakes[id].currentItems.remove(item);
			board.snakes[id].inversion = false;
			break;
		case OTHERS_SLOW:
			board.snakes[id].currentItems.remove(item);
			board.snakes[id].movingSpeed = Constants.REGULAR_MOVING_SPEED;
			board.snakes[id].turningSpeed = Constants.REGULAR_TURNING_SPEED;
			break;
		case OTHERS_THICK:
			board.snakes[id].currentItems.remove(item);
			board.snakes[id].headRadius = Constants.REGULAR_HEAD_RADIUS;
			break;
		case OTHERS_SPEED:
			board.snakes[id].currentItems.remove(item);
			board.snakes[id].movingSpeed = Constants.REGULAR_MOVING_SPEED;
			board.snakes[id].turningSpeed = Constants.REGULAR_TURNING_SPEED;
			break;
		case USER_BIG_HOLE:
			board.snakes[id].currentItems.remove(item);
			board.snakes[id].holeRate *= 2;
			break;
		case USER_SLOW:
			board.snakes[id].currentItems.remove(item);
			board.snakes[id].movingSpeed = Constants.REGULAR_MOVING_SPEED;
			board.snakes[id].turningSpeed = Constants.REGULAR_TURNING_SPEED;
			break;
		case USER_SPEED:
			board.snakes[id].currentItems.remove(item);
			board.snakes[id].movingSpeed = Constants.REGULAR_MOVING_SPEED;
			break;
		default:
			break;
		}
	}


	/**
	 * Met à jour le temps restant des effets des snakes et met fin aux effets
	 * si leur temps est terminé.
	 * 
	 * @param elapsedTime Temps écoulé depuis la dernière mise à jour de la board (ms)
	 */
	public void majSnakesEffects(long elapsedTime) {

		for(Snake snake : board.snakes)
		{
			for (Map.Entry<Item, Long> entry : snake.currentItems.entrySet())
			{
				long remainingTime = entry.getValue();
				long refreshedTime = remainingTime - elapsedTime;

				// Enlever l'effet et supprimer l'objet de la liste
				if (refreshedTime <= 0 ) {
					removeSnakeItem(snake.playerId, entry.getKey());
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
	public Position generateSnakeSpawnPos(int widthBoard, int heightBoard) {

		Boolean flagPos;
		Position posSpawn = new Position(0,0);

		do {
			posSpawn.x = 20 + (int)(Math.random() * heightBoard - 20); 
			posSpawn.y = 20 + (int)(Math.random() * widthBoard - 20); 
			flagPos = true;

			for(int i = 0; i < board.snakes.length ; i++)// Teste de la proximité avec un autre snake
			{
				if(board.snakes[i] != null)
				{
					if(Math.abs(posSpawn.x - board.snakes[i].currentX) +  Math.abs(posSpawn.y - board.snakes[i].currentY) < 40)
					{
						flagPos = false; // Proximité détécté, on cherche alors une nouvelle position
					}
				}
			}
		}while(!flagPos);

		return posSpawn;
	}


	/**
	 * Cette méthode met à jour les positions des têtes de tout les snakes du jeu encore en vie graçe à leur
	 * vitesse et leur direction en degré, elle remplit aussi dans le même temps la Map avec les tracés des snakes.
	 * Elle verifie aussi si le snake n'est pas entré en contact avec un autre snake, un item ou la bordure du plateau.
	 * 
	 * @param elapsedTime Temps écoulé depuis la dernière mise à jour de la board (ms)
	 */
	public void majSnakesPositions(long elapsedTime) {
		long elapsed;
		float pixStep;
		boolean snakeMove = false;
		Position pos = null;
		for(Snake snake : board.snakes)
		{
			int id = deltaID.get(snake.playerId);
			elapsed = elapsedTime;
			pixStep = 0;
			while (elapsed > 0 && snake.state == true)
			{

				/** Gestion de la future position du snake en fonction de son angle **/
				while(pixStep < 1 && elapsed > 0) {
					elapsed--;
					pixStep += snake.movingSpeed;
				}
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

				pixStep --;

				if(snakeMove) {
					if (moveCount.get(snake.playerId) <= holeTick.get(snake.playerId)
					 || moveCount.get(snake.playerId) > holeTick.get(snake.playerId) +10) {
						board.snakesMap.put(pos , snake.playerId);
						System.out.println("Position snake "+ Integer.toString(snake.playerId)+ " x:" + Integer.toString(snake.currentX) + " y:" + Integer.toString(snake.currentY));
						snakeEncounterItem(snake);
					}
					else {
						System.out.println("Snake " + snake.playerId + " hole");
					}
					snakeEncounterBounds(snake);
					if(invincibleTime <= 0)
						snakeEncounterSnake(snake);
					fillSnakeHead(snake);
				 	if(moveCount.get(snake.playerId) == 100) {
						refreshSnakeHoleTick(snake);
					}
				}
			}
		}
	}

	/**
	 * Cette méthode ré-initialise les attributs liés aux trous laissés par un snake.
	 * @param snake Snake à mettre à jour
	 */
	private void refreshSnakeHoleTick(Snake snake) {
		moveCount.put(snake.playerId, 0);
		holeTick.put(snake.playerId, (int)(Math.random() * 100 - snake.holeRate*100));
		System.out.println("Hole tick refresh");
		
	}

	/**
	 * Gestion du snake dans le cas ou il depasse les bordures, si il posséde l'état de
	 * non-collision, il pourra traverser les bordures et réaparaitre de l'autre coté.
	 * @param snake Snake à tester
	 */
	public void snakeEncounterBounds(Snake snake) {
		if(snake.currentX - snake.headRadius <= 0 
				|| snake.currentX + snake.headRadius >= board.width 
				|| snake.currentY - snake.headRadius <= 0
				|| snake.currentY + snake.headRadius >= board.height) {
			if (snake.collision == true) { // Le snake meurt pitoyablement
				snake.state = false; 
				System.out.println("Le snake "+snake.playerId+ " est mort contre la bordure du plateau");
			} else { // Translater position de l'autre coté de la board
				if(snake.currentX - snake.headRadius <= 0)
					snake.currentX = (int) (board.width - snake.headRadius);
				else if(snake.currentX + snake.headRadius >= board.width )
					snake.currentX = (int) snake.headRadius;
				else if(snake.currentY - snake.headRadius <= 0)
					snake.currentY = (int) (board.height - snake.headRadius);
				else if(snake.currentY + snake.headRadius >= board.height)
					snake.currentY = (int) snake.headRadius;
			}	
		}
	}

	/**
	 * Gestion du snake dans le cas ou il cogne un autre snake. Cette fonction definit
	 * la zone physique autour de du snake où il peut entrer en collision avec un autre snake (hitbox).
	 * 
	 * @param snake Snake à tester
	 */
	public void snakeEncounterSnake(Snake snake) {

		int hitBox[][] = new int[3][2];
		hitBox[0][0] = snake.currentX + (int) ((snake.headRadius+1) * Math.cos(Math.toRadians(snake.currentAngle)));
		hitBox[0][1] = snake.currentY - (int) ((snake.headRadius+1) * Math.sin(Math.toRadians(snake.currentAngle)));
		hitBox[1][0] = snake.currentX + (int) ((snake.headRadius+2) * Math.cos(Math.toRadians(snake.currentAngle + 75)));
		hitBox[1][1] = snake.currentY - (int) ((snake.headRadius+2) * Math.sin(Math.toRadians(snake.currentAngle + 75)));
		hitBox[2][0] = snake.currentX + (int) ((snake.headRadius+2) * Math.cos(Math.toRadians(snake.currentAngle - 75)));
		hitBox[2][1] = snake.currentY - (int) ((snake.headRadius+2) * Math.sin(Math.toRadians(snake.currentAngle - 75)));
		
		for(int i = 0; i < 3; i++) {
			Position posChk = new Position(hitBox[i][0], hitBox[i][1]);
			Integer flg = board.snakesMap.get(posChk);
			if(flg != null) {
				snake.state = false;	
				System.out.println("Le snake " + snake.playerId + " est mort contre le snake " + flg);
			}
		}
	}

	
	/**
	 * Gestion du snake si il rencontre un item, l'effet est alors ajouté au(x)
	 * snake(s) concerné(s).
	 * 
	 * @param snake Snake à tester
	 */
	public void snakeEncounterItem(Snake snake) {
		Position posItem = new Position(0,0);
		for (Map.Entry<Position, Item> entry : board.itemsMap.entrySet()) {
			posItem = entry.getKey();
			if(Math.sqrt(Math.pow(posItem.x - snake.currentX, 2) + Math.pow(posItem.y - snake.currentY, 2)) < radItem + snake.headRadius)// Detecte si le snake passe dans le rayon de l'objet
			{
				addSnakeItem(snake.playerId, entry.getValue()); // Ajoute l'item et l'effet
				board.itemsMap.remove(posItem); // Suppression de l'item sur la map
				System.out.println("Snake a rencontré un item");
			}
		}
	}

	/**
	 * Cette méthode met à jour les différents angles courants des snakes selon la direction
	 * envoyé.
	 * 
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
					if(snake.inversion == false)
						snake.currentAngle += elapsedTime*Math.toDegrees(snake.turningSpeed);
					else
						snake.currentAngle -= elapsedTime*Math.toDegrees(snake.turningSpeed);
					break;
				case RIGHT:
					if(snake.inversion == false)
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
	 * Remplit la snakeMap avec les coordonnées du serpent relatives à la trace
	 * laissé par la tête du snake (plus la tête est grosse plus la trace l'est).
	 * 
	 * @param snake Snake dont la trace doit être marqué.
	 */
	public void fillSnakeHead(Snake snake) {
		int id  = snake.playerId;
		int xS  = snake.currentX;
		int yS  = snake.currentY;
		int rad = (int) snake.headRadius;
		Position pos;
		// On met la tête dans un carré et on ajoute chaque coordonnée dans 
		// la map si racine_carre((x_point - x_centre)² + (y_centre - y_point)²) < rayonHead
		for(int i = xS - rad; i < xS + rad ; i++) {
			for(int j = yS - rad; j < yS + rad ; j++) {
				if(Math.sqrt(Math.pow(i - xS, 2) + Math.pow(j - yS, 2)) < rad) {
					pos = new Position(i,j);
					if(board.snakesMap.get(pos) == null) {
						board.snakesMap.put(pos , id);
						//System.out.println("Snake "+snake.playerId +"  Point x:" + i + " y:" + j + " ajouté");
					}
					else if(board.snakesMap.get(pos) == snake.playerId){ 
						//System.out.println("Point du snake deja écrit"); 
					}		
				}
			}
		}
	}


	@Override
	public void forceUpdate(Board board) {
				
		this.board.width = board.width;
		this.board.height = board.height;
		this.board.snakesMap = board.snakesMap;
		this.board.itemsMap = board.itemsMap;
		this.board.snakes = board.snakes;
		
	}
	
	
}
		
	
	
