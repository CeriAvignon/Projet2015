package fr.univavignon.courbes.physics.groupe07;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.PhysicsEngine;

/**
 * 
 * @author Laurent Harkiolakis
 * @author Rudy Bardout
 *
 */
public class Round implements PhysicsEngine
{
	/** Plateau de jeu */
	public Board ourBoard;
	
	/** Donne les coordonnées de la position d'un snake */
	private double[][] coordSnake;
	
	/** Chance qu'un item puisse apparaître */
	private double itemRate = 0.5;
	
	/** Permet de gérer le flux de spawn d'objet */
	private double ratioItem = 0;
	
	/** Nombre de pixels que le snake peut parcourir **/
	private double[] nbPix; 
	
	/** Retrouve l'id du snake par rapport à l'id du profil */
	private Map<Integer, Integer> currentId;

	/** Map pour faire spawn un trou **/
	private Map<Integer, Integer> holeMap;
	
	/** Map pour compter les déplacements **/
	private Map<Integer, Integer> moveMap;
	
	/** En début de partie le snake ne peut pas mourir (en ms)**/
	private int noCollision = 100;
	
	
	/**
	 * Getter pour ratioItem
	 * 
	 * @return double
	 */
	public double getRatioItem()
	{
		return ratioItem;
	}

	/**
	 * Setter pour ratioItem
	 * 
	 * @param ratioItem Permet de gérer le flux de spawn d'objet
	 */
	public void setRatioItem(double ratioItem)
	{
		this.ratioItem = ratioItem;
	}
	
	/**
	 * Getter pour itemRate
	 * 
	 * @return itemRate
	 */
    public double getItemRate() 
    {
         return itemRate; 
    }
    
    /**
	 * Setter pour itemRate
	 * 
	 * @param itemRate Chance qu'un item puisse apparaître
	 */
    public void setItemRate(double itemRate) 
    {  
    	this.itemRate = itemRate; 
    }
    
    /**
	 * Cette méthode correspond au constructeur de Round.
	 * 
	 * @param width Largeur
	 * @param height Hauteur
	 * @param profileIds Nombre de joueurs
	 */
	public Round(int width, int height, int[] profileIds)
	{
		ourBoard = init(width,height,profileIds);
	}
	
	
	
	/**
	 * Cette méthode correspond au constructeur de Board.
	 * <br/>
	 * Il faut créer tous les tableaux nécessaires, c'est-à-dire pour
	 * connaître les positions de tous les snakes et des items.
	 * 
	 * 
	 * @param width Largeur
	 * @param height Hauteur
	 * @param profileIds nombre de joueurs
	 */

	@Override
	public Board init(int width, int height, int[] profileIds)
	{
		Position posSpawn;
		int playerNbr = profileIds.length;
		coordSnake = new double[playerNbr][2];
		nbPix = new double[playerNbr];
		currentId = new HashMap<Integer, Integer>();
		holeMap = new HashMap<Integer, Integer>();
		moveMap = new HashMap<Integer, Integer>();
	
		ourBoard = new Board();
		ourBoard.width = width;
		ourBoard.height = height;
		ourBoard.snakes = new Snake[playerNbr];	// tableau de snakes
		ourBoard.snakesMap = new HashMap<Position, Integer>();	// pour connaître la position des snakes
		ourBoard.itemsMap = new HashMap<Position, Item>();	// pour connaître la position des items

		for (int i = 0; i < playerNbr ; i++)
		{
			posSpawn = snakeSpawnPos(width, height);
			ourBoard.snakes[i] = new Snake();
			currentId.put(profileIds[i], i);
			init(ourBoard.snakes[i], profileIds[i] , posSpawn);
			System.out.println("Snake " + Integer.toString(i) + " spawn a la position (" + Integer.toString(posSpawn.x) + ","+ Integer.toString(posSpawn.y) + ")");
		}
		return ourBoard;  
	}
	
	
	
	/**	
	 * Cette méthode correspond au constructeur de Snake.
	 * <br/>
	 * Les valeurs sont pour le moment fixées arbitrairement.
	 * 
	 * @return Snake
	 * @param snake Snake
	 * @param id Id du Snake
	 * @param spawnPosition Position où le snake apparaît
	 */

	public Snake init(Snake snake, int id, Position spawnPosition)
	{
		snake.playerId = id;
		snake.currentX = spawnPosition.x;
		snake.currentY = spawnPosition.y;
		snake.currentAngle = (int)(Math.random() * 359);
		snake.headRadius = 2;
		snake.movingSpeed = 0.25;
		snake.turningSpeed = 0.01;
		snake.state = true;
		snake.collision = true;
		snake.inversion = false;
		snake.holeRate = 0.05;
		snake.fly = false;
		snake.currentItems = new HashMap<Item, Long>();
		holeMap.put(snake.playerId , (int)(Math.random() * 100 - snake.holeRate*100));
		moveMap.put(snake.playerId  , 0);
		snake.currentScore = 0;		// à enlever pour l'IU ?
		
		return snake;
	}
	
	
	
	/**
	 * Cette méthode retourne une position aléatoire où un snake va spawn.
	 * <br/>
	 * Il faut prévoir une marge pour ne pas spawn sur les bords.
	 * @return Position
	 * @param width Largeur
	 * @param height Hauteur
	 */
	
	public Position snakeSpawnPos(int width, int height)
	{
		
		Random snake = new Random();
		Position pos = new Position((snake.nextInt((width-100)-100)+ 100), (snake.nextInt((height-100)-100)+ 100));
		return pos;
	}
	
	
	
	/**
	 * Cette méthode créé un Item de façon aléatoire à des coordonnées aléatoires.
	 */
	
	public void itemSpawnPos()
	{
		int width = (int) (Math.random() * ourBoard.width);
		int height = (int) (Math.random() * ourBoard.height);
		Position posNewItem = new Position(width,height);
		int nbItems = (int)(Math.random()*Item.values().length);
		Item newItem = Item.values()[nbItems];																			
		ourBoard.itemsMap.put(posNewItem,newItem);
	}
	
	
	
	/**
	 * Cette méthode affecte un item à un ou plusieurs snakes.
	 * 
	 * @param id Id
	 * @param item Item
	 */
	
	public void addItemToSnake(int id, Item item)
	{
		int idSnake = currentId.get(id);
		switch(item)
		{
			case USER_SPEED:
				ourBoard.snakes[idSnake].currentItems.put(item, (long)item.duration);
				ourBoard.snakes[idSnake].movingSpeed *= 2;
				break;
			case USER_SLOW:
				ourBoard.snakes[idSnake].currentItems.put(item, (long)item.duration);
				ourBoard.snakes[idSnake].movingSpeed *= 0.5;
				break;
			case USER_BIG_HOLE:
				ourBoard.snakes[idSnake].currentItems.put(item, (long)item.duration);
				ourBoard.snakes[idSnake].holeRate *= 0.5;
				break;
			case OTHERS_SPEED:
				for(Snake snake : ourBoard.snakes)
				{
					if (snake.playerId != id)
					{
						snake.currentItems.put(item, (long)item.duration);
						snake.movingSpeed *= 2;
					}
				}
				break;
			case OTHERS_THICK:
				for(Snake snake : ourBoard.snakes)
				{
					if (snake.playerId != id)
					{
						snake.currentItems.put(item, (long)item.duration);
						snake.headRadius *= 2;
					}
				}
				break;
			case OTHERS_SLOW:
				for(Snake snake : ourBoard.snakes)
				{
					if (snake.playerId != id)
					{
						snake.currentItems.put(item, (long)item.duration);
						snake.movingSpeed *= 0.5;
					}
				}
				break;
			case OTHERS_REVERSE:
				for(Snake snake : ourBoard.snakes)
				{
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
				for(Snake snake : ourBoard.snakes)
				{
					snake.currentItems.put(item, (long)item.duration);
					snake.fly = true;
				}
				break;
			case COLLECTIVE_ERASER:
				ourBoard.snakesMap.clear();
				break;
			default:
				System.out.println("Erreur");
				break;
			}
	}
	
	
	
	/**
	 * Cette méthode retire l'effet d'un item à un ou plusieurs snakes.
	 * 
	 * @param id Id du profil
	 * @param item Item
	 */
	
	public void removeItemToSnake(int id, Item item)
	{
		int idSnake = currentId.get(id);
		switch(item)
		{
			case USER_SPEED:
				ourBoard.snakes[idSnake].currentItems.remove(item);
				ourBoard.snakes[idSnake].movingSpeed *= 0.5;
				break;
			case USER_SLOW:
				ourBoard.snakes[idSnake].currentItems.remove(item);
				ourBoard.snakes[idSnake].movingSpeed *= 2;
				break;
			case USER_BIG_HOLE:
				ourBoard.snakes[idSnake].currentItems.remove(item);
				ourBoard.snakes[idSnake].holeRate *= 2;
				break;
			case OTHERS_SPEED:
				ourBoard.snakes[idSnake].currentItems.remove(item);
				ourBoard.snakes[idSnake].movingSpeed *= 0.5;
				break;
			case OTHERS_THICK:
				ourBoard.snakes[idSnake].currentItems.remove(item);
				ourBoard.snakes[idSnake].headRadius *= 0.5;
				break;
			case OTHERS_SLOW:
				ourBoard.snakes[idSnake].currentItems.remove(item);
				ourBoard.snakes[idSnake].movingSpeed *= 2;
				break;
			case OTHERS_REVERSE:
				ourBoard.snakes[idSnake].currentItems.remove(item);
				ourBoard.snakes[idSnake].inversion = false;
				break;
			case COLLECTIVE_TRAVERSE_WALL:
				ourBoard.snakes[idSnake].currentItems.remove(item);
				ourBoard.snakes[idSnake].fly = false;
				break;
			default:
				System.out.println("Erreur");
				break;
		}
	}
	

	
	/**
	 * Cette méthode effectue la mise à jour des snakes et des items.
	 */
	@Override
	public void update(long elapsedTime, Map<Integer, Direction> commands)
	{
		if(noCollision > 0)
			noCollision -= elapsedTime;
		// Mise à jour des coordonnées des snakes
		updateSnakesPositions(elapsedTime);
		// Mise à jour des directions des snakes
		updateSnakesDirections(elapsedTime, commands);
		// Mise à jour des effets liés aux snakes
		updateSnakesEffects(elapsedTime);
		// Mise à jour du prochain spawn d'item
		updateSpawnItem(elapsedTime);
	}
	

	
	/**
	 * Cette méthode effectue la mise à jour de la position du snake.
	 * @param elapsedTime Temps écoulé
	 */
	
	public void updateSnakesPositions(long elapsedTime) {
		long elapsed;
		boolean snakeMove = false;
		Position pos = new Position(0,0);
		for(Snake snake : ourBoard.snakes)
		{
			int id = currentId.get(snake.playerId);
			elapsed = elapsedTime;
			while (elapsed > 0 && snake.state == true)
			{

				/** Gestion de la future position du snake en fonction de son angle **/
				while(nbPix[id] < 1 && elapsed > 0) {
					elapsed--;
					nbPix[id] += snake.movingSpeed;
				}
				
				if(nbPix[id] >= 1) {
					coordSnake[snake.playerId][0] += Math.cos(Math.toRadians(snake.currentAngle));
					coordSnake[snake.playerId][1] += Math.sin(Math.toRadians(snake.currentAngle));
	
					if(coordSnake[snake.playerId][1] >= 1 && coordSnake[snake.playerId][0] >= 1)
					{
						snake.currentY--;
						snake.currentX++;
						pos.x = snake.currentX;
						pos.y = snake.currentY;
						ourBoard.snakesMap.put(pos , snake.playerId);
						coordSnake[snake.playerId][1]--;
						coordSnake[snake.playerId][0]--;
						moveMap.put(snake.playerId, moveMap.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(coordSnake[snake.playerId][1] <= -1 && coordSnake[snake.playerId][0] >= 1)
					{
						snake.currentY++;
						snake.currentX++;
						pos.x = snake.currentX;
						pos.y = snake.currentY;
						ourBoard.snakesMap.put(pos , snake.playerId);
						coordSnake[snake.playerId][1]++;
						coordSnake[snake.playerId][0]--;
						moveMap.put(snake.playerId, moveMap.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(coordSnake[snake.playerId][1] <= -1 && coordSnake[snake.playerId][0] <= -1)
					{
						snake.currentY++;
						snake.currentX--;
						pos.x = snake.currentX;
						pos.y = snake.currentY;
						ourBoard.snakesMap.put(pos , snake.playerId);
						coordSnake[snake.playerId][1]++;
						coordSnake[snake.playerId][0]++;
						moveMap.put(snake.playerId, moveMap.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(coordSnake[snake.playerId][1] >= 1 && coordSnake[snake.playerId][0] <= -1)
					{
						snake.currentY--;
						snake.currentX--;
						pos.x = snake.currentX;
						pos.y = snake.currentY;
						ourBoard.snakesMap.put(pos , snake.playerId);
						coordSnake[snake.playerId][1]--;
						coordSnake[snake.playerId][0]++;
						moveMap.put(snake.playerId, moveMap.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(coordSnake[snake.playerId][1] >= 1) {
						snake.currentY--;
						pos.x = snake.currentX;
						pos.y = snake.currentY;
						ourBoard.snakesMap.put(pos , snake.playerId);
						coordSnake[snake.playerId][1]--;
						moveMap.put(snake.playerId, moveMap.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(coordSnake[snake.playerId][1] <= -1) {
						snake.currentY++;
						pos.x = snake.currentX;
						pos.y = snake.currentY;
						ourBoard.snakesMap.put(pos , snake.playerId);
						coordSnake[snake.playerId][1]++;
						moveMap.put(snake.playerId, moveMap.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(coordSnake[snake.playerId][0] >= 1) {
						snake.currentX++;
						pos.x = snake.currentX;
						pos.y = snake.currentY;
						ourBoard.snakesMap.put(pos , snake.playerId);
						coordSnake[snake.playerId][0]--;
						moveMap.put(snake.playerId, moveMap.get(snake.playerId)+1);
						snakeMove = true;
					}
					else if(coordSnake[snake.playerId][0] <= -1) {
						snake.currentX--;
						pos.x = snake.currentX;
						pos.y = snake.currentY;
						ourBoard.snakesMap.put(pos , snake.playerId);
						coordSnake[snake.playerId][0]++;
						moveMap.put(snake.playerId, moveMap.get(snake.playerId)+1);
						snakeMove = true;
					}
	
					nbPix[id] --;
					System.out.println("Position snake "+ Integer.toString(snake.playerId)+ " x:" + Integer.toString(snake.currentX) + " y:" + Integer.toString(snake.currentY));
	
					if(snakeMove)
					{
						/*fillSnakeHead(snake);
						deathVsBounds(snake);
						deathVsSnake(snake);
						removeItem(snake,pos);*/
						
						if (moveMap.get(snake.playerId) <= holeMap.get(snake.playerId) || moveMap.get(snake.playerId) > holeMap.get(snake.playerId) + snake.holeRate*100) {
							ourBoard.snakesMap.put(pos , snake.playerId);
							System.out.println("Position snake "+ Integer.toString(snake.playerId)+ " x:" + Integer.toString(snake.currentX) + " y:" + Integer.toString(snake.currentY));
							fillSnakeHead(snake);
						}
						else {
							System.out.println("Snake " + snake.playerId + " hole");
						}
						deathVsBounds(snake);
						if(noCollision <= 0) {
							deathVsSnake(snake);
						}
						removeItem(snake,pos);
					}
					
				}
			}
		}
	}
	
	
	
	/**
	 * Remplit la snakeMap en fonction des coordonnées du snake
	 * 
	 * @param snake Snake
	 */
	public void fillSnakeHead(Snake snake)
	{
		int id  = snake.playerId;
		int xSnake  = snake.currentX;
		int ySnake  = snake.currentY;
		int rayonHeadSnake = (int) snake.headRadius;
		Position pos = new Position(0,0);

		// On met la tête dans un carré et on ajoute chaque coordonnée dans 
		// la map si racine_carre((x_point - x_centre)² + (y_centre - y_point)²) < rayonHead
		for(int i = xSnake - rayonHeadSnake; i < xSnake + rayonHeadSnake ; i++)
		{
			for(int j = ySnake - rayonHeadSnake; j < ySnake + rayonHeadSnake ; j++)
			{
				if(Math.sqrt(Math.pow(i - xSnake, 2) + Math.pow(j - ySnake, 2)) < rayonHeadSnake)
				{
					pos.x = i;
					pos.y = j;
					ourBoard.snakesMap.put(pos, id);
				}
			}
		}
	}
	
	
	
	/**
	 * Cette méthode teste si le snake rentre en contact avec les bords.
	 * <br/>
	 * Il faut prévoir le cas où l'item COLLECTIVE_TRAVERSE_WALL a été utilisé.
	 * 
	 * @param snake Snake
	 */
	
	public void deathVsBounds(Snake snake)
	{
		if(snake.currentX < 0 || snake.currentX > ourBoard.width || snake.currentY < 0|| snake.currentY > ourBoard.height) {
			if (!snake.fly)
			{
				snake.state = false; 
				System.out.println("Snake " + snake.playerId + " a touché les bords");
			} 
			else
			{
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
	
	
	
	/**
	 * Cette méthode teste si le snake rentre en contact avec la trainée d'un autre snake.
	 * 
	 * @param snake Snake
	 */
	
	public void deathVsSnake(Snake snake)
	{
		int hitBox[][] = new int[3][2];
		hitBox[0][0] = snake.currentX + (int) ((snake.headRadius+1) * Math.cos(Math.toRadians(snake.currentAngle)));
		hitBox[0][1] = snake.currentY - (int) ((snake.headRadius+1) * Math.sin(Math.toRadians(snake.currentAngle)));
		hitBox[1][0] = snake.currentX + (int) ((snake.headRadius+2) * Math.cos(Math.toRadians(snake.currentAngle + 90)));
		hitBox[1][1] = snake.currentY - (int) ((snake.headRadius+2) * Math.sin(Math.toRadians(snake.currentAngle + 90)));
		hitBox[2][0] = snake.currentX + (int) ((snake.headRadius+2) * Math.cos(Math.toRadians(snake.currentAngle - 90)));
		hitBox[2][1] = snake.currentY - (int) ((snake.headRadius+2) * Math.sin(Math.toRadians(snake.currentAngle - 90)));
		
		for(int i = 0; i < 3; i++)
		{
			Position posCollision = new Position(hitBox[i][0],hitBox[i][1]);
			Integer opponent = ourBoard.snakesMap.get(posCollision);
			if(opponent != null)
			{
				snake.state = false;	
				System.out.println("Snake " + snake.playerId + " est mort");
			}
		}
		
	}
	
	
	
	/**
	 * Cette méthode retire l'item de la map dès qu'il est récupéré.
	 * 
	 * @param snake Snake
	 * @param pos Position
	 */
	public void removeItem(Snake snake, Position pos)
	{
		Item itemRecup = ourBoard.itemsMap.get(pos);
		if(itemRecup != null)
		{
			if(snake.state)
			{
				addItemToSnake(snake.playerId, itemRecup);
				ourBoard.itemsMap.remove(pos);		// l'item est pris
			}
		}
	}
	

	
	/**
	 * Cette méthode effectue la mise à jour de la direction du snake.
	 * 
	 * @param elapsedTime Temps écoulé
	 * @param commands Commandes rentrés
	 */
	
	public void updateSnakesDirections(long elapsedTime, Map<Integer, Direction> commands)
	{
		Direction direction;
		for(Snake snake : ourBoard.snakes)
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
	 * Cette méthode affecte un item à un snake.
	 * 
	 * @param elapsedTime Temps écoulé
	 */
	
	public void updateSnakesEffects(long elapsedTime)
	{
		for(Snake snake : ourBoard.snakes)
		{
			for (Map.Entry<Item, Long> entry : snake.currentItems.entrySet())
			{
				long roundTime = entry.getValue();
				long timeLeft = roundTime - elapsedTime;

				// Enlever l'effet et supprimer l'objet de la liste
				if (timeLeft <= 0 )
				{
					removeItemToSnake(snake.playerId, entry.getKey());
				}
				// Mettre à jour le temps restant pour l'effet de l'Item
				else if (timeLeft > 0)
				{
					snake.currentItems.put(entry.getKey(), timeLeft);
				}
			}
		}
	}
	

	
	/**
	 * Cette méthode génère l'apparition d'un item pendant la mise à jour
	 * 
	 * @param elapsedTime Temps écoulé
	 */
	
	private void updateSpawnItem(long elapsedTime)
	{
		ratioItem += elapsedTime*getItemRate();
		if(ratioItem >= 1000)
		{
			itemSpawnPos();
			ratioItem = 0;
		}
	}
	
	

	/**
	 * Cette méthode écrase le tableau actuel.
	 * 
	 * @param board Board
	 */
	@Override
	public void forceUpdate(Board board)
	{
		this.ourBoard = board;
	}
	
	
	
	/**
	 * 
	 * @param width Largeur
	 * @param height Hauteur
	 * @param profileIds Nombre de joueurs
	 * @return Board
	 */
	@Override
	public Board initDemo(int width, int height, int[] profileIds) {
		int playerNbr = 2;
		if(profileIds.length < 1){
			System.out.println("1 joueur minimum");
			return null;
		}
		coordSnake = new double[playerNbr][2];
		nbPix = new double[playerNbr];
		holeMap = new HashMap<Integer, Integer>();
		moveMap = new HashMap<Integer, Integer>();
		currentId = new HashMap<Integer, Integer>();
		ourBoard = new Board();
		ourBoard.width = width;
		ourBoard.height = height;
		ourBoard.snakesMap = new HashMap<Position, Integer>();
		ourBoard.itemsMap = new HashMap<Position, Item>();
		ourBoard.snakes = new Snake[playerNbr];
		Position posSpawn;

		for (int i = 0; i < playerNbr ; i++) 
		{
			posSpawn = snakeSpawnPos(width, height);
			ourBoard.snakes[i] = new Snake();
			currentId.put(profileIds[i], i);
			init(ourBoard.snakes[i], profileIds[i] , posSpawn);
			System.out.println("Snake " + Integer.toString(i) + " spawn a la position x:" + Integer.toString(posSpawn.x) + "  y:"+Integer.toString(posSpawn.y));
		}

		/** Spawn de tout les items du jeu **/
		Item[] itemTab = new Item[] {
				Item.USER_SPEED,
				Item.USER_SLOW,
				Item.USER_BIG_HOLE,
				Item.OTHERS_SPEED,
				Item.OTHERS_THICK,
				Item.OTHERS_SLOW,
				Item.OTHERS_REVERSE,
				Item.COLLECTIVE_THREE_CIRCLES,
				Item.COLLECTIVE_TRAVERSE_WALL,
				Item.COLLECTIVE_ERASER
		};
		ourBoard.itemsMap.put(new Position(100,100), itemTab[0] );
		ourBoard.itemsMap.put(new Position(200,100), itemTab[1] );
		ourBoard.itemsMap.put(new Position(300,100), itemTab[2] );
		ourBoard.itemsMap.put(new Position(400,100), itemTab[3] );
		ourBoard.itemsMap.put(new Position(100,200), itemTab[4] );
		ourBoard.itemsMap.put(new Position(100,300), itemTab[5] );
		ourBoard.itemsMap.put(new Position(100,400), itemTab[6] );
		ourBoard.itemsMap.put(new Position(200,200), itemTab[7] );
		ourBoard.itemsMap.put(new Position(300,300), itemTab[8] );
		ourBoard.itemsMap.put(new Position(300,300), itemTab[9] );
		return ourBoard;
	}
	
}