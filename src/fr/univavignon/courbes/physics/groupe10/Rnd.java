package fr.univavignon.courbes.physics.groupe10;
import fr.univavignon.courbes.common.*;

import java.util.HashMap;
import java.util.Map;

import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.common.*;

public class Rnd implements PhysicsEngine {
	
	Board board;
	
	
	
	Rnd()
	{
		board = new Board();
	}
	
	
	// FONCTIONS QUI MANIPULENT BOARD
	
	@Override
	public Board init(int width, int height, int[] profileIds)
	{
		
		board.width = width;
		
		board.height = height;
		
		board.snakes = new Snake[profileIds.length];
		
		board.snakesMap = new HashMap<Position, Integer>();
		
		board.itemsMap = new HashMap<Position, Item>();
		
		for (int i = 0; i < profileIds.length; i++)
		{
			board.snakes[i] = new Snake();
			snakeCreator(board.snakes[i], i, profileIds[i], width/(profileIds.length+1)*(i+1) , height/2,0);
		}
		
		return board;
	}
	
	@Override
	public void update(long elapsedTime, Map<Integer,Direction> commands)
	{
		//on gere l'impact du temps ecoulé sur les snakes (au niveau des Itemes essentielement)
		
		boardTimeImpact(elapsedTime);
		
		//on gere le déplacement des snakes et les colisions
		
		int id;
		
		for (int i = 0; i < board.snakes.length; i++)
		{
			id = board.snakes[i].playerId;
			snakeMove(id, elapsedTime, commands.get(id));
		}
		

	}
	
	@Override
	public void forceUpdate(Board board) {
		// TODO Auto-generated method stub
		
	}
	

	
	
	/**
	 * Génère une item à une position aléatoire sur la board
	 * */
	public void generateItem()
	{
		int wdt = (int) (Math.random() * board.width);	// generate a random x
		
		int hgt = (int) (Math.random() * board.height);	// generate a random y
		
		Position p = new Position( wdt , hgt );		// Create a new position
		
		int it = (int)(Math.random()*Item.values().length);				// Item.value is an array that containts different items
		
		Item item = Item.values()[it];		// Generate a random item 																			
		
		board.itemsMap.put(p, item);													// add the new item in the itemsMap
		
	}
	
	
	public void boardTimeImpact(long time)
	{
		for (int i = 0; i < board.snakes.length; i++)
		{
			snakeTimeImpact(i, time);
		}
	}
	
	
	// FONCTIONS QUI MANIPULENT SNAKE
	
	/**
	 *  Ce consctructeur initialise tout les attributs
	 *  
	 *  @return Snake  avec les valeurs passées en paramètre
	 */
	public Snake snakeCreator(
			Snake s,
			int playerId, int profileId, 
			int currentX, int currentY, double currentAngle, double headRadius, double movingSpeed, double turningSpeed, 
			boolean state, boolean collision, boolean inversion, double holeRate, boolean fly, int currentScore)
	{
		
		s.playerId = playerId;
		s.profileId = profileId;
		s.currentX = currentX;
		s.currentY = currentY;
		s.currentAngle = currentAngle;
		s.headRadius = headRadius;
		s.movingSpeed = movingSpeed;
		s.turningSpeed = turningSpeed;
		s.state = state;
		s.collision = collision;
		s.inversion = inversion;
		s.holeRate = holeRate;
		s.fly = fly;
		s.currentScore = currentScore;
		
		s.currentItems = new HashMap<Item, Long>();

		return s;
	}

	/** 
	 * constructeur simplifié : créé un snake de base en début de partie
	 */
	public Snake snakeCreator(Snake s,int playerId, int profileId,int currentX, int currentY, double currentAngle)
	{
		
		s.playerId = playerId;
		s.profileId = profileId;
		s.currentX = currentX;
		s.currentY = currentY;
		s.currentAngle = 0.6;
		s.headRadius = 1;
		s.movingSpeed = 0.1;
		s.turningSpeed = 1;
		s.state = true;
		s.collision = true;
		s.inversion = false;
		s.holeRate = 0;
		s.fly = false;
		s.currentScore = 0;
		
		s.currentItems = new HashMap<Item, Long>();
		
		return s;
	}
	
	/**
	 * fonction qui bouge le snake selon la direction passée en paramètre
	 */
	
	public void snakeMove(int id, long elapsedTime, Direction command)
	{
		
		// On fais tourner le snake
		
		if (command == Direction.LEFT)
			board.snakes[id].currentAngle +=  board.snakes[id].turningSpeed*elapsedTime;

		if (command == Direction.RIGHT)
			board.snakes[id].currentAngle -=  board.snakes[id].turningSpeed*elapsedTime;
		
		// On regarde si l'angle sors des limites du certcle trigo
		
		// On determine les coordonnée du pixels objectif et la distance qui le separe
		
		//on fixe la position initiale de la tete
		double tmpX = board.snakes[id].currentX;
		
		double tmpY = board.snakes[id].currentY;
		//c'est egalement les coordonnée de la 'pointe du tracé'
		
		//on deplace la tete vers l'objectif
		board.snakes[id].currentX += (int) (Math.cos(board.snakes[id].currentAngle)*board.snakes[id].movingSpeed*elapsedTime);
		
		board.snakes[id].currentY += (int) (Math.sin(board.snakes[id].currentAngle)*board.snakes[id].movingSpeed*elapsedTime);
		
		//la 'pointe du tracé' va chercher à rejoindre en ligne droite la tete du Snake
		//en couvrant chaque pixel
		
	
		//on determine le pas de la tete du tracé en x et en y
		snakeDrawBody(id, tmpX, tmpY);
		
	}
	
	
	public void snakeDrawBody(int id, double headX, double headY)
	{
		
				
		//if( board.snakes[id].currentX < board.width && board.snakes[id].currentY < board.height )
				int j = 0;
				boolean outTheWall = false;
			//	Position lastDrawnPosition = new Position(0,0);
				
				double interX = board.snakes[id].currentX - headX;
				
				double interY = board.snakes[id].currentY - headY;
				
				double distance = Math.sqrt( Math.pow(interX, 2) + Math.pow(interY, 2));
				
				double stepX = interX / distance;
				
				double stepY = interY / distance;
				
				int valeurCollision = 0;
				
				//tant que la pointe du tracé n'a pas rejoint la tete,
				//on dessine le pixel et on incremente
				
				int i = 0;
						
				for ( ; i <= (int) distance ; i++)
				{
					j = i; 
					//System.out.println("coucou je suis là");
					Position po = new Position((int) Math.round(headX) , (int) Math.round(headY));
					
					
					valeurCollision = checkCollision(po, id);
					
					if(valeurCollision == 0 ||valeurCollision == 2)	 // 0 Pas de collision --- 2 collision avec item
					{
						//if(!lastDrawnPosition.equals(po))
						//{
							board.snakesMap.put(po, id);			// Je déssine la position sur la map
							
							
							
							if(po.x == board.width || po.y == board.height)
							{
								outTheWall = true;
								break;
							}
							//lastDrawnPosition = po;
					//	}	
						
					}
					else
					{
						if(valeurCollision == -1 || valeurCollision == 1)// Si il y a une collision avec un snake ou un mur je sors de la boucle
						{					
							break;
						}
					
					}
						
					headX += stepX;
					headY += stepY;
				}
				
				

		if (board.snakes[id].fly && outTheWall)
		{
			// traiter le cas de fly mode
			// déssiner jusqu'au bord du board
			// mettre à jour les coordonnées des la tête ( c'est à dire currentX ou currentY ou les 2)
		
			
				if (board.snakes[id].currentX >= board.width)
				{	
						headX = 0;
				}
				if (board.snakes[id].currentY >= board.height)
				{	
						headY = 0;
				}

			

				if (board.snakes[id].currentX < board.width)
				{	
					headX = board.width;
					
				}
				
				if (board.snakes[id].currentY < board.height)
				{	
					headY = board.height;
				
				}
				

			
			// dessiner jusqu'à la tête ( currentX ou currentY )
			for ( ; j <= (int) distance ; j++)
			{
				Position po = new Position((int) headX , (int) headY);
				
				
				valeurCollision = checkCollision(po, id);
				
				if(valeurCollision == 0 ||valeurCollision == 2)	 // 0 Pas de collision --- 2 collision avec item
				{
					//if(!lastDrawnPosition.equals(po))
						board.snakesMap.put(po, id);			// Je déssine la position sur la map
					
					//lastDrawnPosition = po;

					
				}
				else
				{
					if(valeurCollision == -1 || valeurCollision == 1)// Si il y a une collision avec un snake ou un mur je sors de la boucle
					{					
						break;
					}
				
				}
					
				headX += stepX;
				headY += stepY;
			}
			
			board.snakes[id].currentX = (int) headX;
			board.snakes[id].currentY = (int) headY;
			
			
			
		}
		System.out.println("This snake still alive : "+board.snakes[id].state);
		
		
		
		
	}

	/**Cette fonction detecte si la tete du snake rentre en collision avec des objets, soit avec un mur, un autre snake (ou son propre corps), 
	 * Retourne -1 s'il s'agit d'une collision avec le mur
	 * Retourne 1 s'il y a une collision avec snake
	 * Retourne 2 s'il s'agit d'un d'une collision avec item
	 * Retourne 0 pas de collision
	 * 
	 * @param p la position où je vérifie si il y a une collision
	 * @param id l'id du joueur
	 * */
	public int checkCollision(Position p, int id)
	{
		System.out.println(p.x+" "+p.y);

		if(p.x <= 0 || p.y <= 0 || p.x >= board.width-1 || p.y >= board.height-1) // Collision avec le mur
		{
			if(board.snakes[id].fly == false)					// mode avion n'est pas activé
			{
				board.snakes[id].state = false;			// Je change l'état du snake (mort)
				return -1; 
			}

		}
		

		

		if(board.snakesMap.containsKey(p) && board.snakes[id].fly == false)				// Si il y a une collision et le mode avion n'est pas activé
		{
			board.snakes[id].state = false;
			return 1;										// Collision avec un autre snake
		}
			
		if(board.itemsMap.containsKey(p))
		{												// Je rajoute l'item à la map des items de snake
			board.snakes[id].currentItems.put(board.itemsMap.get(p), (long) board.itemsMap.get(p).duration); 
			snakeAddItem(id, board.itemsMap.get(p));				// J'ajoute l'effect de l'item
			return 2;
		}
		
		
		return 0;								// Pas de collision
	}
	
	/**
	 * 	Fonction qui ajoute l'effet de l'item du snake id
	 * 
	 * @param id est le paramètre du joueur
	 * @param item c'est l'item qui se trouve dans la map des items du snake
	 */
	
	public void snakeAddItem(int id, Item item)
	{
		//ajout de l'item au map d'items
		
		//ajout des effets correspondants
		if(item == Item.USER_SPEED)
		{
			board.snakes[id].movingSpeed += 2 ;
		}
		
		if(item == Item.USER_SLOW)
		{
			board.snakes[id].movingSpeed -= 1.5;
		}
		
		if(item == Item.USER_BIG_HOLE)
		{
			board.snakes[id].holeRate += 0.5;
		}
		
		if(item == Item.OTHERS_SPEED)
		{
			for( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
				{
					board.snakes[i].movingSpeed += 1;
				}
			}
		}
		
		if(item == Item.OTHERS_THICK)
		{
			for( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
				{
					board.snakes[i].headRadius += 2;
				}
			}
		}
		
		if(item == Item.OTHERS_SLOW)
		{
			for ( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
				{
					board.snakes[i].movingSpeed -= 1;
				}
			}
		}
			
		if(item == Item.OTHERS_REVERSE)
		{
			for ( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
				{
					board.snakes[i].inversion = true;
				}
			}
		}
		
		if(item == Item.COLLECTIVE_THREE_CIRCLES)
		{
			
		}
		
		if(item == Item.COLLECTIVE_TRAVERSE_WALL)
		{
			board.snakes[id].fly = true;
		}
		
		if(item == Item.COLLECTIVE_ERASER)
		{
			// J'efface tout les tracés de snakes (Enlever le tracé de la map)
			board.snakesMap.clear();
			// Je mets la tete des snakes dans la map 
			for(int i = 0; i < board.snakes.length; i++)
			{
				board.snakesMap.put(new Position(board.snakes[i].currentX, board.snakes[i].currentY), board.snakes[i].playerId);
			}
		}

		
		
	}
	
	public void snakeDeleteItem(int id, Item item)
	{
		//suppression de l'item dans la map
		//suppression de l'effet de l'item
		if(item == Item.USER_SPEED)
		{
			board.snakes[id].movingSpeed -= 2 ;
		}
		
		if(item == Item.USER_SLOW)
		{
			board.snakes[id].movingSpeed += 1.5;
		}
		
		if(item == Item.USER_BIG_HOLE)
		{
			board.snakes[id].holeRate -= 0.5;
		}
		
		if(item == Item.OTHERS_SPEED)
		{
			for( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
				{
					board.snakes[i].movingSpeed -= 1;
				}
			}
		}
		
		if(item == Item.OTHERS_THICK)
		{
			for( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
				{
					board.snakes[i].headRadius -= 2;
				}
			}
		}
		
		if(item == Item.OTHERS_SLOW)
		{
			for ( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
				{
					board.snakes[i].movingSpeed += 1;
				}
			}
		}
			
		if(item == Item.OTHERS_REVERSE)
		{
			for ( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
				{
					board.snakes[i].inversion = false;
				}
			}
		}
		
		if(item == Item.COLLECTIVE_THREE_CIRCLES)
		{
			
		}
		
		if(item == Item.COLLECTIVE_TRAVERSE_WALL)
		{
			board.snakes[id].fly = false;
		}
		
		if(item == Item.COLLECTIVE_ERASER)
		{
			// J'efface tout les tracés de snakes (Enlever le tracé de la map)
			board.snakesMap.clear();
			// Je mets la tete des snakes dans la map 
			for(int i = 0; i < board.snakes.length; i++)
			{
				board.snakesMap.put(new Position(board.snakes[i].currentX, board.snakes[i].currentY), board.snakes[i].playerId);
			}
		}

	}
	
	
	public void snakeTimeImpact(int id, long time)
	{

		
		for (Map.Entry<Item, Long> item : board.snakes[id].currentItems.entrySet())	// je parcours la map des items du joueur id
		{
			board.snakes[id].currentItems.put(item.getKey(), item.getValue() - time);		// Je decrémente le temps écoulé depuis la dernière mise à jour
			
			if(item.getValue() <= 0)		// Si la valeur est inferieure ou égale à 0
			{
				snakeDeleteItem(id, item.getKey());			// J'enlève les effects des items
				board.snakes[id].currentItems.remove(item.getKey());		// Et je supprime l'item obselète de la map
			}

		}
	}
	

}
