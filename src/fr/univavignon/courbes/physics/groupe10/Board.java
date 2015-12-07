package fr.univavignon.courbes.physics.groupe10;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;





/**
 * Cette classe correspond à l'ensemble des informations propres à 
 * l'aire de jeu utilisée pendant une manche.
 * <br/>
 * Il faut bien distinguer la notion de partie et de manche. Les joueurs
 * sont confrontés lors d'une parties se déroulant sur plusieurs manches
 * distinctes. À chaque, chaque joueur marque un certain nombre de points.
 * Un joueur gagne la partie quand son score dépasse une certaine valeur
 * limite. 
 */
public class Board implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	/** Largeur de l'aire de jeu, en pixels */
	public int width;
	/** Hauteur de l'aire de jeu, en pixels */
	public int height;
	
	/** Trainées des snakes sur l'aire de jeu: associe la position d'un pixel à un ID de joueur */
	public Map<Position, Integer> snakesMap;
	/** Tableau contentant tous les snakes de la manche, placés dans l'ordre des ID des joueurs correspondants */
	public Snake snakes[];
	
	/** Position des items sur l'aire de jeu: associe la position d'un item à la valeur de cet item */
	public Map<Position, Item> itemsMap;
	
	public Board(int width, int height, int[] profileIds)
	{
		this.width = width;
		
		this.height = height;
		
		snakesMap = new HashMap<Position, Integer>();
		
		itemsMap = new HashMap<Position, Item>();
		
		snakes = new Snake[profileIds.length];
		for (int i = 0; i < profileIds.length; i++)
		{
			snakes[i] =  new Snake(i, profileIds[i], width/(profileIds.length+1)*(i+1), height/2, 0.6/*Math.random() * (2*Math.PI)*/);
		}
		
	}
	
	public void moveSnake(int id, long elapsedTime, fr.univavignon.courbes.physics.groupe10.Direction command)
	{
		
		// On fais tourner le snake
		
		if (command == fr.univavignon.courbes.physics.groupe10.Direction.LEFT)
			snakes[id].setCurrentAngle(snakes[id].getCurrentAngle() * elapsedTime);

		if (command == fr.univavignon.courbes.physics.groupe10.Direction.RIGHT)
			snakes[id].setCurrentAngle( -1 * snakes[id].getCurrentAngle() * elapsedTime);
		
		// On regarde si l'angle sors des limites du certcle trigo
		
		// On determine les coordonnée du pixels objectif et la distance qui le separe
		
		//on fixe la position initiale de la tete
		double tmpX = snakes[id].currentX;
		double tmpY = snakes[id].currentY;
		//c'est egalement les coordonnée de la 'pointe du tracé'
		
		//on deplace la tete vers l'objectif
		snakes[id].currentX += (int) (Math.cos(snakes[id].currentAngle)*snakes[id].movingSpeed*elapsedTime);
		snakes[id].currentY += (int) (Math.sin(snakes[id].currentAngle)*snakes[id].movingSpeed*elapsedTime);
		
		//la 'pointe du tracé' va cherche a rejoindre en ligne droite la tete du Snake
		//en couvrant chaque pixel
		
		//on determine le pas de la tete du tracé en x et en y
		double interX = snakes[id].currentX - tmpX;
		double interY = snakes[id].currentY - tmpY;
		
		double distance = Math.sqrt( Math.pow(interX, 2) + Math.pow(interY, 2));
		
		double stepX = interX / distance;
		double stepY = interY / distance;
		

		//tant que la pointe du tracé n'a pas rejoint la tete,
		//on dessine le pixel et on incremente
		for (int i = 0; i <= (int) distance ; i++)
		{
			Position po = new Position((int) tmpX, (int) tmpY);
			System.out.println(po.x+" "+po.y);
			
			if(checkCollision(po, id) == 0 || checkCollision(po, id) == 2)	 // 0 Pas de collision --- 2 collision avec item
			{
				snakesMap.put(po, id);			// Je déssine la position sur la map
				
			}
			else
			{
				if(checkCollision(po, id) == -1 || checkCollision(po, id) == 1)
			 			// Si il y a une collision avec un snake ou un mur je sors de la boucle
				{					// **Collision */
				break;
				}
			
			}
				
			tmpX += stepX;
			tmpY += stepY;
		}
		
		// on trace le trace sur la HashMap pixel par pixel
		//si l'on tombe sur un pixel deja utilisé, on renvois un entier:
			//0 : pas de colision
			//1 : colision mortelle avec un autre snake
			//2 : colision avec une item
	}
	
	/**Cette fonction detecte si la tete du snake rentre en collision avec des objets, soit avec un mur, un autre snake (ou son propre corps), 
	 * Retourne -1 s'il s'agit d'une collision avec le mur
	 * Retourne 1 s'il y a une collision avec snake
	 * Retourne 2 s'il s'agit d'un d'une collision avec item
	 * Retourne 0 pas de collision
	 * */
	
	public int checkCollision(Position p, int id)
	{
		

		if(p.x <= 0 || p.y <= 0 || p.x >= width-1 || p.y >= height-1) // Collision avec le mur
		{
			if(snakes[id].fly == false)					// mode avion n'est pas activé
			{
				snakes[id].state = false;			// Je change l'état du snake (mort)
				return -1; 
			}
		}
		

				
		
		if(snakesMap.containsKey(p) && snakes[id].fly == false)				// Si il y a une collision et le mode avion n'est pas activé
		{
			snakes[id].state = false;
			return 1;										// Collision avec un autre snake
		}
			
		

		
		for (HashMap.Entry<Position, Item> entry : itemsMap.entrySet())
		{	
				if(entry.getKey().x == p.x && entry.getKey().y == p.y)				// Collision avec item
				{
					snakes[id].currentItems.put(entry.getValue(), (long) entry.getValue().duration); 
					return 2;
				}
		}
		
		return 0;								// Pas de collision
	}
	

	
	public void itemEffect()
	{
			for(int j = 1; j < snakes.length; j++)
			{
				for (HashMap.Entry<Item, Long> item : snakes[j].currentItems.entrySet())
				{	
						if(item.getKey() == Item.USER_SPEED)
						{
							snakes[j].movingSpeed += 1 ;
						}
						
						if(item.getKey() == Item.USER_SLOW)
						{
							snakes[j].movingSpeed -= 1.5;
						}
						
						if(item.getKey() == Item.USER_BIG_HOLE)
						{
							snakes[j].holeRate += 0.5;
						}
						
						if(item.getKey() == Item.OTHERS_SPEED)
						{
							for( int i = 0; i < snakes.length; i++)
							{
								if(i != j)
								{
									snakes[i].movingSpeed += 1;
								}
							}
						}
						
						if(item.getKey() == Item.OTHERS_THICK)
						{
							for( int i = 0; i < snakes.length; i++)
							{
								if(i != j)
								{
									snakes[i].headRadius += 2;
								}
							}
						}
						
						if(item.getKey() == Item.OTHERS_SLOW)
						{
							for ( int i = 0; i < snakes.length; i++)
							{
								if(i != j)
								{
									snakes[i].movingSpeed -= 1;
								}
							}
						}
							
						if(item.getKey() == Item.OTHERS_REVERSE)
						{
							for ( int i = 0; i < snakes.length; i++)
							{
								if(i != j)
								{
									snakes[i].inversion = true;
								}
							}
						}
						
						if(item.getKey() == Item.COLLECTIVE_THREE_CIRCLES)
						{
							
						}
						
						if(item.getKey() == Item.COLLECTIVE_TRAVERSE_WALL)
						{
							
						}
						
						if(item.getKey() == Item.COLLECTIVE_ERASER)
						{
							// J'efface tout les tracés de snakes (Enlever le tracé de la map)
							snakesMap.clear();
							// Je mets la tete des snakes dans la map 
							for(int i = 0; i < snakes.length; i++)
							{
								snakesMap.put(new Position(snakes[i].currentX, snakes[i].currentY), snakes[i].playerId);
							}
						}
	
			}
	}
		
		
	}
	
	
	public void generateItem()
	{
		int wdt = (int) (Math.random() * width);	// generate a random x
		
		int hgt = (int) (Math.random() * height);	// generate a random y
		
		Position p = new Position( wdt , hgt );		// Create a new position
		
		int it = (int)(Math.random()*Item.values().length);				// Item.value is an array that containts different items
		
		Item item = Item.values()[it];		// Generate a random item 																			
		
		itemsMap.put(p, item);													// add the new item in the itemsMap
		
	}

	
}
