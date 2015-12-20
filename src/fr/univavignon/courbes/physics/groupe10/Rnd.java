package fr.univavignon.courbes.physics.groupe10;
import fr.univavignon.courbes.common.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.common.*;

public class Rnd implements PhysicsEngine {
	
	Board board;
			// Une fois sur dix un item apparait
	
	double itemProbability = 0.3;
	
	
	/* CES VARIABLES SONT POUR DEBUGGER */
	//elles seront enlevé
	double currentAngleTEST =  0;
	double headRadiusTEST = 3;
	double angleVisionTEST = 2 * Math.PI / 3;
	
	
	
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
		//on gère l'impact du temps ecoulé sur les snakes (au niveau des Itemes essentiellement)
		
		boardTimeImpact(elapsedTime);
		
		//on gère le déplacement des snakes et les collisions
		
		int idP;
		
		for (int i = 0; i < board.snakes.length; i++)
		{
			if(board.snakes[i].state)
			{
				idP = board.snakes[i].profileId;
				snakeMove(i, elapsedTime, commands.get(idP));
			}
		
		}
		
		if(new Random().nextDouble() <= itemProbability)	
			generateItem();
	}
	
	@Override
	public void forceUpdate(Board board) {
		// TODO Auto-generated method stub
		
		this.board.height = board.height;
		
		this.board.width = board.width;
		
		for(int i = 0; i < this.board.snakes.length; i++)
			this.board.snakes[i] = board.snakes[i];
		
		
		
		this.board.snakesMap.clear();
		
		this.board.snakesMap.putAll(board.snakesMap);;
		
		this.board.itemsMap.clear();
		
		this.board.itemsMap.putAll(board.itemsMap);
		
	}
	

	
	
	/**
	 * Génère une item à une position aléatoire sur la board
	 * */
	public void generateItem()
	{
		
				int wdt = (int) (Math.random() * board.width);	// Generer un x aléatoire
				
				int hgt = (int) (Math.random() * board.height);	// Generer un y aléatoire
				
				Position p = new Position( wdt , hgt );		// Créer une noubelle position
				
				int it = (int)(Math.random()*Item.values().length);				// Item.value est un tableau qui contient les differents items
				
				Item item = Item.values()[it];		// Generer un item aléatoire 																			
				
				board.itemsMap.put(p, item);			// Ajouter l'item à itemsMap
		
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
		s.currentAngle = 0/*Math.random()*(2*Math.PI)*/;
		s.headRadius = 5/*5*/;
		s.movingSpeed = 1;
		s.turningSpeed = 0.02;
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
		// PS** le cas d'inversion de commande est-ce que c'est à nous d'inverser les commandes ou plutôt IU  ??
		// On fais tourner le snake
		
		if (command == Direction.LEFT ||
								(command == Direction.RIGHT && board.snakes[id].inversion))
			board.snakes[id].currentAngle +=  board.snakes[id].turningSpeed*elapsedTime;

		if (command == Direction.RIGHT ||
								(command == Direction.LEFT && board.snakes[id].inversion))
			board.snakes[id].currentAngle -=  board.snakes[id].turningSpeed*elapsedTime;
		
		// On regarde si l'angle sors des limites du certcle trigo
		
		// On determine les coordonnée du pixels objectif et la distance qui le separe
		
		//on fixe la position initiale de la tete
		double tmpX = board.snakes[id].currentX;
		
		double tmpY = board.snakes[id].currentY;
		//c'est egalement les coordonnée de la 'pointe du tracé'
		
		//on deplace la tete vers l'objectif
		board.snakes[id].currentX += (int) Math.round((Math.cos(board.snakes[id].currentAngle)*board.snakes[id].movingSpeed*elapsedTime));
		
		board.snakes[id].currentY += (int) Math.round((Math.sin(board.snakes[id].currentAngle)*board.snakes[id].movingSpeed*elapsedTime));
		
		
		//la 'pointe du tracé' va chercher à rejoindre en ligne droite la tete du Snake
		//en couvrant chaque pixel
		
	
		// Appel à la fonction qui dessine le tracé du snake
		snakeDrawBody(id, tmpX, tmpY);
		
	}
	
	/**
	 * Fonction qui dessine le tracé du snake spécifié avec son id.
	 * 
	 * @param id du snake
	 * @param headX la coordonnée en abscisse de la tête avant le déplacement.
	 * @param headY la coordonnée en ordonnée de la tête avant le déplacement.
	 * 
	 * */
	public void snakeDrawBody(int id, double headX, double headY)
	{
		// Remarque : 1 - il faut eviter de déssiner la tête du snake avant le déplacement dans la 1ere itération, 
		// Comme elle sera dessiné dans la dernière MAJ on tombre dans une collision!! 
		// le mieux est de la déssiner dans la dernière itération
	
				
			//Position lastDrawnPosition = new Position(-1,-1);	
			/* Objet qui stocke la dernière position dessinée
			 Pour ne pas dessiner sur une case déjà dessinée dans l'iteration
			 précedente */
			
			double interX = board.snakes[id].currentX - headX;		
				
			double interY = board.snakes[id].currentY - headY;
			// Determiner la distance entre la tête avant et après le déplacement				
			double distance = Math.sqrt( Math.pow(interX, 2) + Math.pow(interY, 2)); 
				
			double stepX = interX / distance;
				
			double stepY = interY / distance;
				
			Collision vCollision;
				

				
			// Je deplace la tête du snake vers le pixel suivant
				
			headX += stepX;
			headY += stepY;
				
		
				
			// Tant que la pointe du tracé n'a pas rejoint la tete, on dessine le pixel et on incremente
					
						
			for (int i = 0 ; i < (int) distance ; i++)
			{
					
				// Créer l'objet Position avec les Coordonnées actuelles de la tête
				Position po = new Position((int) Math.round(headX) , (int) Math.round(headY));
				
				/* DEBUT : VERSION QUI NE PREND PAS EN COMPTE LA LARGEUR DU TRACE
				
				// Si cette position n'est pas dessiné dans l'iteration précedente
				if(!lastDrawnPosition.equals(po))				
				{	
						vCollision = checkCollision(po, id);	
						// Appel à la fonction qui vérifie la collision
					
					if(vCollision == Collision.NONE ||vCollision == Collision.ITEM)	 
					{	
							
						// Pour faire des trous dans le corps du snake
						if(new Random().nextDouble() >= board.snakes[id].holeRate)  
						{
							board.snakesMap.put(po, id);			// Je déssine la position sur la map
							
							board.snakes[id].currentX = po.x;
							
							board.snakes[id].currentY = po.y;
									
							System.out.println(po.x+" "+po.y);
						}
							
						lastDrawnPosition = po;			// Mettre à jour la position dessinée
								
						
					}
					else
					{
						if(vCollision == Collision.BORDER || vCollision == Collision.SNAKE)
						{					
							break;
						}
					
					}
				}
				
				FIN : VERSION QUI NE PREND PAS EN COMPTE LA LARGEUR DU TRACE */
				
				
				
				/* DEBUT : VERSION QUI PREND EN CHARGE LA LARGEUR DU TRACE  */
				
				//on test si il y a une collision aux abort de la tete du snake
				//si il y a une collision, ses effets seront automatiquement appliqués
				vCollision = snakeHeadCollision(id);
			
				
				//en fonction du type de colision, on decide de continuer ou non a dessiner le corps :
				
				//Si la colision n'est pas mortel, on dessine le corp du tracé
				if(vCollision == Collision.NONE ||vCollision == Collision.ITEM)	 
				{	
					snakeDrawHead(po.x, po.y, (int) board.snakes[id].headRadius);
				}
				//si la colision est mortel, on ne dessine plus le tracé car le snake est deja mort
				else if (vCollision == Collision.BORDER || vCollision == Collision.SNAKE)
				{
					break;
				}
				

				/* FIN  : VERSION QUI PREND EN CHARGE LA LARGEUR DU TRACE */
					
			
					headX += stepX;
					headY += stepY;
					
					if(board.snakes[id].fly)
					{
					if (headX >= board.width)
							headX = 0;

					if (headY >= board.height)
							headY = 0;

					if (headX < 0)
						headX = board.width-1;

					
					if (headY < 0)
						headY = board.height-1;

					}
					
		}
				
	/*	board.snakes[id].currentX = lastDrawnPosition.x;
		board.snakes[id].currentY = lastDrawnPosition.y;*/
				
		
			/*
		// Pour tester
		System.out.println("currentX : "+ board.snakes[id].currentX +" - currentY :"+board.snakes[id].currentY);
		
		System.out.println("This snake still alive ? "+board.snakes[id].state);
		*/
		
		
	}
	
	/**
	 * Fonction qui dessine la tete du snake dans la HashMap
	 * 
	 * @param id c'est l'id du snake que l'on veux dessiner
	 */
	public void snakeDrawHead(int centerX, int centerY, int radius)
	{
		/*
		int centerX = board.snakes[id].currentX;
		int centerY = board.snakes[id].currentY;
		int radius = (int) board.snakes[id].headRadius ;
		*/
		
		//on enumere les pixels du carre dans lequel le cercle s'inscrit
		for (int x  = centerX - radius ; x <= centerX + radius; x++)
		{
			for (int y = centerY - radius ; y <= centerY + radius; y++)
			{
				//on regarde si le pixel enuemre appartient au disque
				//si sa distance au centre est <= au rayon
				if (Math.sqrt( Math.pow( x-centerX ,2) + Math.pow( y-centerY, 2) ) <= radius )
				{
					
					System.out.println( (int) Math.round(x) + " " + (int) Math.round(y) + " green");
					//board.snakesMap.put(new Position(x, y), id);
				}
			}
		}
	}
	
	/**
	 * Fonction qui detecte si la tete d'un snake est en colision avec quelque chose
	 * Si jamais une colision est detecté, active automatiquement les conséquence de la colision
	 * La colision est calculé sur un ensemble de pixels periphérique a la Head, dans son angle de déplacement
	 * 
	 * @param id
	 */
	public Collision snakeHeadCollision(int id)
	{
		double[] tabAngle = new double[5];
		int nbAngles;
		
		if (board.snakes[id].headRadius == 1)
		{
			tabAngle[0] = board.snakes[id].currentAngle;
			
			nbAngles = 1;
								
		}
		
		else if (board.snakes[id].headRadius < 5)
		{
			tabAngle[0] = board.snakes[id].currentAngle + Math.PI / 4;
			tabAngle[1] = board.snakes[id].currentAngle;
			tabAngle[2] = board.snakes[id]. currentAngle - Math.PI / 4;
			
			nbAngles = 3;
		}
		else
		{
			/*
			double[] tabAngle = {board.snakes[id].currentAngle + Math.PI / 8,
					board.snakes[id].currentAngle + Math.PI / 4,
					board.snakes[id].currentAngle,
					board.snakes[id].currentAngle - Math.PI / 4,
					board.snakes[id]. currentAngle - Math.PI / 8};*/
			tabAngle[0] = board.snakes[id].currentAngle + Math.PI / 4;
			tabAngle[1] = board.snakes[id].currentAngle + Math.PI / 8;
			tabAngle[2] = board.snakes[id].currentAngle;
			tabAngle[3] = board.snakes[id].currentAngle - Math.PI / 8;
			tabAngle[4] = board.snakes[id].currentAngle - Math.PI / 4;
			
			nbAngles = 5;
					
		}
		
	
		/*
		
		double headRadius = board.snakes[id].headRadius;
		
		//on calcule le perimetre de la tete (en pxl)
		int perimetreHead = (int) Math.round(headRadius * Math.PI * 2);
		//on calcule l'angle qu'il y a entre deux pixels adjacent du perimetre
		double pasAngle = (2 * Math.PI) / perimetreHead;
		//Notre pas sera le double de cette angle car on prendre 1 pixel sur 2 du perimetre
		
		//l'angleTest, c'est le 'champ de vision du snake', soit la largeur de la head sensible aux colisions
		double angleTest = angleVisionTEST; // on prend un angle de 90° ici
		
		// nbAngle est le nombre d'angles que l'on va enumerer
		int nbAngle = (int) Math.round(angleTest / (2 * Math.PI) * perimetreHead) + 1;
		double[] tabAngle = new double[nbAngle]; // ce tableau stocke les angles
		
		//la variable angle va prendre successivement les valeur de nos angles
		double angle = board.snakes[id].currentAngle - (angleTest / 2);
		
		
		
		for (int i = 0; i < nbAngle; i++)
		{
			tabAngle[i] = angle;
			angle += pasAngle;
			//System.out.println(180 * tabAngle[i] / Math.PI + "°");
			//Angle en degré = 180 * (angle en radian) / pi 
		}
		*/
		
		//Maintenant que l'on a les angles a enumerer
		//on en déduis pour chacun d'eux la coordonné de son pixel, perihérique a Head
		
		Position pos = new Position(-1,-1);
		for (int i = 0; i < nbAngles; i++)
		{
			pos.x = (int) Math.round((double) board.snakes[id].currentX + Math.cos(tabAngle[i]) * board.snakes[id].headRadius);
			pos.y = (int) Math.round((double) board.snakes[id].currentY + Math.sin(tabAngle[i]) * board.snakes[id].headRadius);
			
			//System.out.println(pos.x + " " + pos.y + " blue");
			return checkCollision(pos, id);
		}
		return Collision.NONE;

	}
	
	/**Cette fonction detecte si la tete du snake rentre en collision avec des objets, soit avec un mur, un autre snake (ou son propre corps), 
	 * Retourne Collision.BORDER s'il s'agit d'une collision avec le mur
	 * Retourne Collision.SNAKE s'il y a une collision avec snake
	 * Retourne Collision.ITEM s'il s'agit d'un d'une collision avec item
	 * Retourne Collision.NONE pas de collision
	 * 
	 * @param p la position où je vérifie si il y a une collision
	 * @param id l'id du joueur
	 * @return Un type enumeré qui spécifie le type de la collision
	 * */
	
	public Collision checkCollision(Position p, int id)
	{
		
		if(p.x <= 0 || p.y <= 0 || p.x >= board.width-1 || p.y >= board.height-1) // Collision avec le mur
		{
			if(board.snakes[id].fly == false)					// mode avion n'est pas activé
			{
				board.snakes[id].state = false;			// Je change l'état du snake (mort)
				return Collision.BORDER; 
			}

		}

		if(board.snakesMap.containsKey(p) && board.snakes[id].fly == false)	 // Si il y a une collision et le mode avion n'est pas activé
		{																		// Collision avec un autre snake
			board.snakes[id].state = false;
			return Collision.SNAKE;										
		}
			
		if(board.itemsMap.containsKey(p))			// Collision avec un item
		{
				// Je rajoute l'item à la map des items de snake
			board.snakes[id].currentItems.put(board.itemsMap.get(p), (long) board.itemsMap.get(p).duration);
			
			snakeAddItem(id, board.itemsMap.get(p));				// J'ajoute l'effect de l'item
			
			return Collision.ITEM;
		}
		
		return Collision.NONE;								// Pas de collision
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
			board.snakes[id].movingSpeed *= 1.5 ;
		}
		
		if(item == Item.USER_SLOW)
		{
			board.snakes[id].movingSpeed /= 1.5;
		}
		
		if(item == Item.USER_BIG_HOLE)
		{
			board.snakes[id].holeRate += 0.2;
		}
		
		if(item == Item.OTHERS_SPEED)
		{
			for( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
				{
					board.snakes[i].movingSpeed *= 1.5;
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
					board.snakes[i].movingSpeed /= 1.5;
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
			// Augmenter la probabilité d'apparition d'un item
			itemProbability *= 2;
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
			board.snakes[id].movingSpeed /= 1.5 ;
		}
		
		if(item == Item.USER_SLOW)
		{
			board.snakes[id].movingSpeed *= 1.5;
		}
		
		if(item == Item.USER_BIG_HOLE)
		{
			board.snakes[id].holeRate -= 0.2;
		}
		
		if(item == Item.OTHERS_SPEED)
		{
			for( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
				{
					board.snakes[i].movingSpeed /= 1.5;
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
					board.snakes[i].movingSpeed *= 1.5;
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
			// rendre la probabilité d'apparition d'un item à sa valeur par défault
			itemProbability /= 2;
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
