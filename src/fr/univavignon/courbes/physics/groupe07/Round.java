package fr.univavignon.courbes.physics.groupe07;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.univavignon.courbes.common.*;
import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.physics.groupe07.groupe18.*;
import fr.univavignon.courbes.physics.groupe07.groupe23.*;

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
		
		/** Chance qu'un item puisse apparaître */
		private double itemRate = 0.01;
		 
		/** Distance de trou à parcourir */
		int holeLength = 0;
		
		/** Retrouve l'id du snake par rapport à l'id du profil */
		private Map<Integer, Integer> currentId;

		
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
			currentId = new HashMap<Integer, Integer>();
		
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
				init(ourBoard.snakes[i], i, profileIds[i], width/(profileIds.length+1)*(i+1) , height/2,0);
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

		public Snake init(Snake s,int playerId, int profileId,int currentX, int currentY, double currentAngle)
		{
			s.playerId = playerId;
			s.profileId = profileId;
			s.currentX = currentX;
			s.currentY = currentY;
			s.currentAngle = 0;
			s.headRadius = Constants.REGULAR_HEAD_RADIUS;
			s.movingSpeed = Constants.REGULAR_MOVING_SPEED;
			s.turningSpeed = Constants.REGULAR_TURNING_SPEED;
			s.state = true;
			s.collision = true;
			s.inversion = false;
			s.holeRate = 0;
			s.fly = false;
			s.currentItems = new HashMap<Item, Long>();
			
			return s;
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
		 * Applique à un snake les effets du au temps. 
		 * @param id 	l'id du snake
		 * @param time	le temps passé depuis le dernier appel de cette fonction
		 */
		public void snakeTimeImpact(int id, long time)
		{

			// Pour chaque items
			for (Map.Entry<Item, Long> item : ourBoard.snakes[id].currentItems.entrySet())	
			{
				// Je decrémente le temps écoulé depuis la dernière mise à jour
				ourBoard.snakes[id].currentItems.put(item.getKey(), item.getValue() - time);
				
				// Si le temps d'action est inferieure ou égale à 0, on enlève les effects de l'item
				if(item.getValue() <= 0) 
					removeItemToSnake(id, item.getKey()); 
			}
		}

		
		/**
		 * Applique à la Board les effets du au temps. 
		 * @param time
		 *				le temps passé depuis le dernier appel de cette fonction
		 */
		public void boardTimeImpact(long time)
		{
			//on applique l'effet du temps sur chaque snakes
			for (int i = 0; i < ourBoard.snakes.length; i++)
			{
				snakeTimeImpact(i, time);
			}
		}
		
		@Override
		public void update(long elapsedTime, Map<Integer,Direction> commands)
		{
			//on gère l'impact du temps ecoulé sur la board
			boardTimeImpact(elapsedTime);
			
			//on fait bouger les snakes
			int idProfil;
			for (int i = 0; i < ourBoard.snakes.length; i++)
			{
				if(ourBoard.snakes[i].state)
				{
					idProfil = ourBoard.snakes[i].playerId;
					snakeMove(i, elapsedTime, commands.get(idProfil));
				}
			}
			
			//on gère la génération des items sur la board
			if(new Random().nextDouble() <= itemRate)	
				itemSpawnPos();
		}

		/**
		 * Cette méthode écrase le tableau actuel.
		 * 
		 * @param board Board
		 */
		@Override
		public void forceUpdate(Board board)
		{
			this.ourBoard.height = board.height;
			this.ourBoard.width = board.width;
			
			//on ecrase les snakes
			for(int i = 0; i < this.ourBoard.snakes.length; i++)
				this.ourBoard.snakes[i] = board.snakes[i];
			
			//on ecrases les la snakesMap
			this.ourBoard.snakesMap.clear();
			this.ourBoard.snakesMap.putAll(board.snakesMap);;
			
			//on ecrases les items
			this.ourBoard.itemsMap.clear();
			this.ourBoard.itemsMap.putAll(board.itemsMap);
		}
		
		
		
		
		/**
		 * Bouge le snake dans la direction donné et en fonction d'un temps donné
		 * 
		 * @param id 			id du snake 
		 * @param elapsedTime 	temps qui s'est passé depuis la derniere update
		 * @param command 		la commande qu'il faut applique au snake : aller a droite, a gauche ou ne rien faire
		 * 
		 */
		public void snakeMove(int id, long elapsedTime, Direction command)
		{
			// On fais tourner le snake
			
			//a gauche
			if (command == Direction.LEFT ||
			     (command == Direction.RIGHT && ourBoard.snakes[id].inversion))
			{
				ourBoard.snakes[id].currentAngle += ourBoard.snakes[id].turningSpeed*elapsedTime;
			}
			//a droite	
			if (command == Direction.RIGHT ||
			     (command == Direction.LEFT && ourBoard.snakes[id].inversion))
			{
				ourBoard.snakes[id].currentAngle -=  ourBoard.snakes[id].turningSpeed*elapsedTime;
			}
					
			// On regarde si l'angle sors des limites du certcle trigo
			
			// On stocke la positon de la tete avant de la déplacer
			double tmpX = ourBoard.snakes[id].currentX;
			double tmpY = ourBoard.snakes[id].currentY;
			
			//on deplace la tete vers sa destination finale
			ourBoard.snakes[id].currentX += (int) Math.round((Math.cos(ourBoard.snakes[id].currentAngle)*ourBoard.snakes[id].movingSpeed*elapsedTime));
			ourBoard.snakes[id].currentY += (int) Math.round((Math.sin(ourBoard.snakes[id].currentAngle)*ourBoard.snakes[id].movingSpeed*elapsedTime));

			// On dessine le corp du snake
			snakeDrawBody(id, tmpX, tmpY);
			
		}
		
		/**
		 * Dessine le corp du snake.
		 * Précisément, dessine le corp du snake (en prenant en compte sa largeur)
		 * en ligne droite entre son ancienne position et sa nouvelle position
		 * son ancienne position étant passé en paramètre, sa nouvelle étant les attributs currentX et currentY
		 * 
		 * remarque : currentX et currentY sont à la position nouvelle du snake lors de l'appel de la fonction,
		 * mais à l'interieur de la fonction, currentX et currentY sont égaux a headX et headY
		 * 
		 * @param id 	id du snake
		 * @param headX la coordonnée en abscisse de la tête avant le déplacement.
		 * @param headY la coordonnée en ordonnée de la tête avant le déplacement.
		 * 
		 * */
		public void snakeDrawBody(int id, double headX, double headY)
		{	
			// ----- CALCUL DE DISTANCES ------
			
			//distance entre l'ancienne et la nouvelle position en x et en y
			double interX = ourBoard.snakes[id].currentX - headX;		
			double interY = ourBoard.snakes[id].currentY - headY;
			// distance entre l'ancienne et la nouvelle position (Th de Pythagore)				
			double distance = Math.sqrt( Math.pow(interX, 2) + Math.pow(interY, 2)); 
			// distance en x et en y effectué quand la tete se déplace de 1 pxl
			double stepX = (interX / distance);
			double stepY =(interY / distance);
			
			// ------ BOUCLE -----
			
			Collision vCollision; //cette variable prendra les valeurs des colisions
			
			//on stocke aussi le pixel de la derniere colision
			//que l'on initialise a une valeur spéciale pour que lors de la première itération,
			//la condition s'execute toujours
			double lastPixelX = -10;
			double lastPixelY = -10;
			
			
			// Je deplace la tête du snake vers le pixel suivant
			//car l'on ne dessine jamais sur la position initiale 
			//(pour éviter de redissiner la tete a l'identique, ce qui poserait des problèmes de colisions)
			headX += stepX;
			headY += stepY;
					
			//on avance sur la ligne droite a dessiner pixels par pixels		
			for (int i = 0 ; i < (int) distance ; i++)
			{
				
				//la tete du snake locale a la fonction est appliqué a la tete de l'objet snake de la board
				ourBoard.snakes[id].currentX = (int) Math.round(headX);
				ourBoard.snakes[id].currentY = (int) Math.round(headY);
				
				//si la tete n'est pas dans un trou, on en créé probablement un
				if (holeLength == 0 && Math.random() <= ourBoard.snakes[id].holeRate)
				{
					holeLength = 20; //CONSTANTE
				}
				
				// calcule la distance qui séparce la position actuel de la tete avec sa derniere position dessiné
				double distanceSinceLastIteration = Math.sqrt( Math.pow(headX-lastPixelX, 2) + Math.pow(headY - lastPixelY, 2));
				
				//Avant de dessiner la nouvelle tete, on test si cette distance est d'au moin 
				//(si la tete est au même endroit ou a 1 pixel de distance, cela créé des problèmes de collisions)
				if (distanceSinceLastIteration >= 2 )
				{
					//System.out.println(distanceSinceLastIteration);

					// --- TEST COLISION ET LEURS APPLICATION ---
					
					//on ne test la collision que si la tete n'est pas dans un trou
					if (holeLength == 0)
						//on test si il y a une collision aux abort de la tete du snake
						//si il y a une colision, ses effets seront automatiquement appliqués
						vCollision = snakeHeadCollision(id);
					else
						vCollision = Collision.NONE;

					
					
					//AFFICHAGE DEBUG
					if (vCollision != Collision.NONE)
						//System.out.println("collision du snake " + id + " : " + vCollision);
									
					//si la colision est mortel, on break car le snake est deja mort
					if (vCollision == Collision.BORDER || vCollision == Collision.SNAKE)
					{
						break;
					}
					
					// --- DESSIN DE LA TETE ---
					
					//si le snake est dans un trou,
					//on ne dessine pas la tete
					if (holeLength == 0)
						snakeDrawHead(id);
					
					//on sauvegarde la position de la tete avant de la changer
					lastPixelX = headX;
					lastPixelY = headY;
					
				}
				
				//on incrémente la position de la tete pour qu'elle soit 1 px plus loin
				//lors de la prochaine itération
				headX += stepX;
				headY += stepY;
				//Dans le cas d'un trou, on a avancé de 1 px, 
				//on decremente donc la distance troué a parcourir
				if (holeLength != 0) holeLength -= 1;
				
				
				
				//on gere le cas ou le snake est en mode fly et se trouve aux limites du plateau
				if(ourBoard.snakes[id].fly)
				{
					if (headX >= ourBoard.width)  headX = 0;
			
					if (headY >= ourBoard.height) headY = 0;
			
					if (headX < 0) headX = ourBoard.width-1;
			
					if (headY < 0) headY = ourBoard.height-1;
				}		
			}
			
			//System.out.println("This snake still alive ? "+board.snakes[id].state);
		}
		
		/**
		 * Dessine la tete d'un snake dans la Board.
		 * Ajoute les pixels qui constituent le disque centré sur (currentX, currentY) et de rayon headRadius
		 * dans snakesMap
		 * 
		 * @param id	c'est l'id du snake dont on veux dessiner la tête
		 */
		public void snakeDrawHead(int id)
		{
			
			int centerX = ourBoard.snakes[id].currentX;
			int centerY = ourBoard.snakes[id].currentY;
			int radius = (int) ourBoard.snakes[id].headRadius ;
			
			//on enumere les pixels du carre dans lequel le cercle s'inscrit
			for (int x  = centerX - radius ; x <= centerX + radius; x++)
			{
				for (int y = centerY - radius ; y <= centerY + radius; y++)
				{
					//on regarde si le pixel enuemre appartient au disque
					//si sa distance au centre est <= au rayon
					if (Math.sqrt( Math.pow( x-centerX ,2) + Math.pow( y-centerY, 2) ) <= radius )
					{
						
						//System.out.println( (int) Math.round(x) + " " + (int) Math.round(y) + " green");
						ourBoard.snakesMap.put(new Position(x, y), id);
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
		 * 
		 * @return un objet de type Collision, correspondant au type de collision détecté (Collision.NULL sinon)
		 */
		public Collision snakeHeadCollision(int id)
		{
			//tabAngle contient les angles des pixels qui vont être testé
			//cet ensemble d'angle sera toujours centré sur currentAngle
			double[] tabAngle = new double[5];
			int nbAngles; //le nombre d'angles testé
			
			//On choisi un nombre d'angles a tester croissant au rayon de la tete:
			
			if (ourBoard.snakes[id].headRadius == 1)
			{
				nbAngles = 1;
				tabAngle[0] = ourBoard.snakes[id].currentAngle;								
			}
			
			else if (ourBoard.snakes[id].headRadius < 5)
			{
				nbAngles = 3;
				tabAngle[0] = ourBoard.snakes[id].currentAngle + Math.PI / 5;
				tabAngle[1] = ourBoard.snakes[id].currentAngle;
				tabAngle[2] = ourBoard.snakes[id]. currentAngle - Math.PI / 5;
			}
			else
			{
				nbAngles = 5;
				tabAngle[0] = ourBoard.snakes[id].currentAngle + Math.PI / 7;
				tabAngle[1] = ourBoard.snakes[id].currentAngle + Math.PI / 8;
				tabAngle[2] = ourBoard.snakes[id].currentAngle;
				tabAngle[3] = ourBoard.snakes[id].currentAngle - Math.PI / 8;
				tabAngle[4] = ourBoard.snakes[id].currentAngle - Math.PI / 7;		
			}
			
			//on enumere les angles, et l'on en déduit le pixel correspondant
			//ce pixel se trouve a la périphérie de la tete

			Collision vCol; //valeurs des colision énumeré
			Position pos = new Position(-1,-1); //valeur des position énumré
			
			for (int i = 0; i < nbAngles; i++)
			{
				//on utilise sinus et cosinus
				pos.x = (int) Math.round((double) ourBoard.snakes[id].currentX + Math.cos(tabAngle[i]) * ourBoard.snakes[id].headRadius);
				pos.y = (int) Math.round((double) ourBoard.snakes[id].currentY + Math.sin(tabAngle[i]) * ourBoard.snakes[id].headRadius);
				
				//System.out.println(pos.x + " " + pos.y + " blue");
				
				//la colision est ici testé et ses effets son appliqué
				vCol = checkCollision(pos, id);
				
				//si l'on viens de détécter une collision, on quitte la fonction : le snake est mort
				if (vCol == Collision.BORDER || vCol == Collision.SNAKE)
				{
					return vCol;
				}
			}
			
			//si aucune colision mortel n'est detecté,
			return Collision.NONE;

		}
		
		/**
		 * Cette fonction detecte un pixel est en collision avec un objet et applique a un snake l'effet de la colision
		 * Soit avec un mur, un autre snake (ou son propre corps), ou avec un item.
		 * La fonction test si le pixel appartient deja a un objet de la map ou sors de la board.
		 * Retourne :
		 * <ul>
		 * 		<li> Collision.BORDER s'il s'agit d'une collision avec le mur </li>
		 * 		<li> Collision.SNAKE s'il y a une collision avec snake </li>
		 * 		<li> Collision.ITEM s'il s'agit d'un d'une collision avec item </li>
		 * 		<li> Collision.NONE pas de collision </li>
		 * </ul>
		 * 
		 * @param p 	la position où l'on vérifie si il y a une collision
		 * @param id 	l'id du snake a qui l'on applique l'effet de la collision
		 * 
		 * @return Un type enumeré qui spécifie le type de la collision
		 * */
		public Collision checkCollision(Position p, int id)
		{
			//on test les colision de type BORDER et SNAKE si le snake n'est pas en mode fly
			if (ourBoard.snakes[id].fly == false)
			{
				//si le pixel est hors des limites de l'ecran, il y a une colision avec le bord
				if(p.x <= 0 || p.y <= 0 || p.x >= ourBoard.width-1 || p.y >= ourBoard.height-1) // Collision avec le mur
				{
					//dans ce cas, on tue le snake
					ourBoard.snakes[id].state = false;
					
					return Collision.BORDER; 
				}
				
				//si le pixel appartient au corpe d'un des snakes de la board
				if(ourBoard.snakesMap.containsKey(p))	 
				{	
					//dans ce cas, on tue le snake
					ourBoard.snakes[id].state = false;
					
					return Collision.SNAKE;										
				}
			}
			
			//si le pixel appartient au un item
			if(ourBoard.itemsMap.containsKey(p))			
			{
				// On rajoute l'item à la map des items de snake
				addItemToSnake(id, ourBoard.itemsMap.get(p));
				
				return Collision.ITEM;
			}
			
			//si aucune collsion n'a été détécté, on en retourne une de type NONE
			return Collision.NONE;
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
			return null;
		}
}
