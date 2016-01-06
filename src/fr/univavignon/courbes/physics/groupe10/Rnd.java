package fr.univavignon.courbes.physics.groupe10;
import fr.univavignon.courbes.common.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.common.*;

/**
 * 
 * 
 * cette classe implémente l'interface PhysicsEngine
 * Elle permet de manipuler le moteur physique d'une manche (round)
 * le moteur physique gère la position, le déplacements et les colisions des Snakes et des Items sur la Board
 * 
 * @see PhysicsEngine
 * @see Board
 * @see Snake
 * @see Item
 *
 * @author Charlie & Sabri - groupe 10
 */
public class Rnd implements PhysicsEngine {
	
	
	// |||||||||||||||||||| VARIABLES DE CLASSES ||||||||||||||||||||
	
	/**
	 * C'est la Board du jeu, elle contient tout les éléments d'une manche.
	 * 
	 * @see Board
	 */
	Board board;
	
	/**
	 * Cette variable donne la probabilité d'apparition d'une Item.
	 */
	double itemProbability = 0.3;
	int residualLengthHole = 0;
	
	// |||||||||||||||||||| FONCTIONS DE CLASSES ||||||||||||||||||||
	
	/**
	 * Constructeur de de la classe Rnd.
	 * construit une Board avec son constructeur par défaut
	 */
	Rnd()
	{
		board = new Board();
	}

	// ---------- FONCTIONS QUI MANIPULENT BOARD ----------

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
		//on gère l'impact du temps ecoulé sur la board
		boardTimeImpact(elapsedTime);
		
		//on fait bouger les snakes
		int idProfil;
		for (int i = 0; i < board.snakes.length; i++)
		{
			if(board.snakes[i].state)
			{
				idProfil = board.snakes[i].profileId;
				snakeMove(i, elapsedTime, commands.get(idProfil));
			}
		}
		
		//on gère la génération des items sur la board
		if(new Random().nextDouble() <= itemProbability)	
			generateItem();
	}
	
	@Override
	public void forceUpdate(Board board) {
		// TODO Auto-generated method stub
		
		//on ecrase les dimensions
		this.board.height = board.height;
		this.board.width = board.width;
		
		//on ecrase les snakes
		for(int i = 0; i < this.board.snakes.length; i++)
			this.board.snakes[i] = board.snakes[i];
		
		//on ecrases les la snakesMap
		this.board.snakesMap.clear();
		this.board.snakesMap.putAll(board.snakesMap);;
		
		//on ecrases les items
		this.board.itemsMap.clear();
		this.board.itemsMap.putAll(board.itemsMap);
	}
	
	/**
	 * Génère une item à une position aléatoire sur la board.
	 * */
	public void generateItem()
	{
		// on créé une nouvelle position aléatoire
		int x = (int) (Math.random() * board.width); // Generer un x aléatoire
		int y = (int) (Math.random() * board.height);// Generer un y aléatoire
		Position p = new Position( x , y );	
		
		// on genère un item aléatoire 	
		int it = (int)(Math.random()*Item.values().length);	
		Item item = Item.values()[it]; 																		
		
		// On ajoute l'item
		board.itemsMap.put(p, item); 
	}
	
	/**
	 * Applique à la Board les effets du au temps. 
	 * @param time
	 *				le temps passé depuis le dernier appel de cette fonction
	 */
	public void boardTimeImpact(long time)
	{
		//on applique l'effet du temps sur chaque snakes
		for (int i = 0; i < board.snakes.length; i++)
		{
			snakeTimeImpact(i, time);
		}
	}
	
	
	// ---------- FONCTIONS QUI MANIPULENT SNAKE ----------
	
	/**
	 *  Ce constructeur initialise un snake en demendant tout les attributs qui le constituent.
	 *  les attributs du snake entré en paramètre seront écrasés.
	 *  
	 * @param s				la reference du snake que l'on veux initialiser
	 * @param playerId		l'id du snake (id local à la manche)
	 * @param profileId 	l'id du joueur qui joue le snake (id global au jeu)
	 * @param currentX 		abscisse de la tete du snake
	 * @param currentY 		ordonnée de la tete du snake
	 * @param currentAngle 	direction du snake (exprimé en rad)
	 * @param headRadius 	rayon de la tete en pixel
	 * @param movingSpeed 	vitesse du snake en px/ms
	 * @param turningSpeed  vitesse de rotation du snake en rad/ms
	 * @param state 		état du snake : True -> vivant, False -> mort
	 * @param collision     True -> le snake est sensible au colisions, False -> il ne l'est pas
	 * @param inversion 	True -> les commande sont inversé, False -> commandes normales
	 * @param holeRate 		Probabilité de trouver des trous dans le tracé du corp du snake
	 * @param fly 			False -> mode fly désactivé, True -> mode fly activé
	 * @param currentScore  Score courant du snake (score locale à la manche)
	 *  
	 *  @see Snake
	 *  
	 *  @return Snake  		retourne un snake initialisé selon les paramètres
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
	 * Construit un snake de manière plus simple.
	 * Seul les paramètres de positions et de déplacements son demandées.
	 * Les autres paramètres sont dans l'état 'de base'
	 * 
	 * @param s 			la reference du snake que l'on veux initialiser
	 * @param playerId 		l'id du snake (id local à la manche)
	 * @param profileId 	l'id du joueur qui joue le snake (id global au jeu)
	 * @param currentX 		abscisse de la tete du snake
	 * @param currentY 		ordonnée de la tete du snake
	 * @param currentAngle 	direction du snake (exprimé en rad)
	 * 
	 *  @return Snake  		retourne un snake initialisé selon les paramètres
	 *  
	 *  @see Snake
	 */
	public Snake snakeCreator(Snake s,int playerId, int profileId,int currentX, int currentY, double currentAngle)
	{
		/*
		valeurs deduite de curveFever
			headRadius = 4
		 	board: 850 * 850
		 	turningSpeed = 0.003 rad/ms
		 	movingSpeed = 0.1 px/ms 
		*/
		
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
		s.currentScore = 0;
		s.currentItems = new HashMap<Item, Long>();
		
		return s;
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
		     (command == Direction.RIGHT && board.snakes[id].inversion))
		{
			board.snakes[id].currentAngle +=  board.snakes[id].turningSpeed*elapsedTime;
		}
		//a droite	
		if (command == Direction.RIGHT ||
		     (command == Direction.LEFT && board.snakes[id].inversion))
		{
			board.snakes[id].currentAngle -=  board.snakes[id].turningSpeed*elapsedTime;
		}
				
		// On regarde si l'angle sors des limites du certcle trigo
		
		// On stocke la positon de la tete avant de la déplacer
		double tmpX = board.snakes[id].currentX;
		double tmpY = board.snakes[id].currentY;
		
		//on deplace la tete vers sa destination finale
		board.snakes[id].currentX += (int) Math.round((Math.cos(board.snakes[id].currentAngle)*board.snakes[id].movingSpeed*elapsedTime));
		board.snakes[id].currentY += (int) Math.round((Math.sin(board.snakes[id].currentAngle)*board.snakes[id].movingSpeed*elapsedTime));

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
		double interX = board.snakes[id].currentX - headX;		
		double interY = board.snakes[id].currentY - headY;
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
			board.snakes[id].currentX = (int) Math.round(headX);
			board.snakes[id].currentY = (int) Math.round(headY);
			
			//si la tete n'est pas dans un trou, on en créé probablement un
			if (residualLengthHole == 0 && Math.random() <= board.snakes[id].holeRate)
			{
				residualLengthHole = 20; //CONSTANTE
			}
			
			// calcule la distance qui séparce la position actuel de la tete avec sa derniere position dessiné
			double distanceSinceLastIteration = Math.sqrt( Math.pow(headX-lastPixelX, 2) + Math.pow(headY - lastPixelY, 2));
			
			//Avant de dessiner la nouvelle tete, on test si cette distance est d'au moin 
			//(si la tete est au même endroit ou a 1 pixel de distance, cela créé des problèmes de collisions)
			if (distanceSinceLastIteration >= 2 )
			{
				System.out.println(distanceSinceLastIteration);

				// --- TEST COLISION ET LEURS APPLICATION ---
				
				//on ne test la collision que si la tete n'est pas dans un trou
				if (residualLengthHole == 0)
					//on test si il y a une collision aux abort de la tete du snake
					//si il y a une colision, ses effets seront automatiquement appliqués
					vCollision = snakeHeadCollision(id);
				else
					vCollision = Collision.NONE;

				
				
				//AFFICHAGE DEBUG
				if (vCollision != Collision.NONE)
					System.out.println("collision du snake " + id + " : " + vCollision);
								
				//si la colision est mortel, on break car le snake est deja mort
				if (vCollision == Collision.BORDER || vCollision == Collision.SNAKE)
				{
					break;
				}
				
				// --- DESSIN DE LA TETE ---
				
				//si le snake est dans un trou,
				//on ne dessine pas la tete
				if (residualLengthHole == 0)
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
			if (residualLengthHole != 0) residualLengthHole -= 1;
			
			
			
			//on gere le cas ou le snake est en mode fly et se trouve aux limites du plateau
			if(board.snakes[id].fly)
			{
				if (headX >= board.width)  headX = 0;
		
				if (headY >= board.height) headY = 0;
		
				if (headX < 0) headX = board.width-1;
		
				if (headY < 0) headY = board.height-1;
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
		
		int centerX = board.snakes[id].currentX;
		int centerY = board.snakes[id].currentY;
		int radius = (int) board.snakes[id].headRadius ;
		
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
					board.snakesMap.put(new Position(x, y), id);
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
		
		if (board.snakes[id].headRadius == 1)
		{
			nbAngles = 1;
			tabAngle[0] = board.snakes[id].currentAngle;								
		}
		
		else if (board.snakes[id].headRadius < 5)
		{
			nbAngles = 3;
			tabAngle[0] = board.snakes[id].currentAngle + Math.PI / 5;
			tabAngle[1] = board.snakes[id].currentAngle;
			tabAngle[2] = board.snakes[id]. currentAngle - Math.PI / 5;
		}
		else
		{
			nbAngles = 5;
			tabAngle[0] = board.snakes[id].currentAngle + Math.PI / 7;
			tabAngle[1] = board.snakes[id].currentAngle + Math.PI / 8;
			tabAngle[2] = board.snakes[id].currentAngle;
			tabAngle[3] = board.snakes[id].currentAngle - Math.PI / 8;
			tabAngle[4] = board.snakes[id].currentAngle - Math.PI / 7;		
		}
		
		//on enumere les angles, et l'on en déduit le pixel correspondant
		//ce pixel se trouve a la périphérie de la tete

		Collision vCol; //valeurs des colision énumeré
		Position pos = new Position(-1,-1); //valeur des position énumré
		
		for (int i = 0; i < nbAngles; i++)
		{
			//on utilise sinus et cosinus
			pos.x = (int) Math.round((double) board.snakes[id].currentX + Math.cos(tabAngle[i]) * board.snakes[id].headRadius);
			pos.y = (int) Math.round((double) board.snakes[id].currentY + Math.sin(tabAngle[i]) * board.snakes[id].headRadius);
			
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
		if (board.snakes[id].fly == false)
		{
			//si le pixel est hors des limites de l'ecran, il y a une colision avec le bord
			if(p.x <= 0 || p.y <= 0 || p.x >= board.width-1 || p.y >= board.height-1) // Collision avec le mur
			{
				//dans ce cas, on tue le snake
				board.snakes[id].state = false;
				
				return Collision.BORDER; 
			}
			
			//si le pixel appartient au corpe d'un des snakes de la board
			if(board.snakesMap.containsKey(p))	 
			{	
				//dans ce cas, on tue le snake
				board.snakes[id].state = false;
				
				return Collision.SNAKE;										
			}
		}
		
		//si le pixel appartient au un item
		if(board.itemsMap.containsKey(p))			
		{
			// On rajoute l'item à la map des items de snake
			snakeAddItem(id, board.itemsMap.get(p));
			
			return Collision.ITEM;
		}
		
		//si aucune collsion n'a été détécté, on en retourne une de type NONE
		return Collision.NONE;
	}
	
	/**
	 * 	Ajoute l'item dans le currentItem d'un snake et applique son effet a ce snake
	 * 
	 * @param id 	id du snake a qui l'on veux ajouter l'item
	 * @param item 	item que l'on veux affecter au snake
	 */
	public void snakeAddItem(int id, Item item)
	{
		//ajout de l'item au map currentItem
		board.snakes[id].currentItems.put(item, (long) item.duration);
		
		//ajout des effets correspondants
		if(item == Item.USER_SPEED)
			board.snakes[id].movingSpeed *= 1.5 ;
		
		if(item == Item.USER_SLOW)
			board.snakes[id].movingSpeed /= 1.5;
		
		if(item == Item.USER_BIG_HOLE)
			board.snakes[id].holeRate += 0.2;
		
		if(item == Item.OTHERS_SPEED)
			for( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
					board.snakes[i].movingSpeed *= 1.5;
			}
		
		if(item == Item.OTHERS_THICK)
			for( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
					board.snakes[i].headRadius += 2;
			}
		
		if(item == Item.OTHERS_SLOW)
			for ( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
					board.snakes[i].movingSpeed /= 1.5;
			}
			
		if(item == Item.OTHERS_REVERSE)
			for ( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
					board.snakes[i].inversion = true;
			}
		
		// Augmenter la probabilité d'apparition d'un item
		if(item == Item.COLLECTIVE_THREE_CIRCLES)
			itemProbability *= 2;
		
		if(item == Item.COLLECTIVE_TRAVERSE_WALL)
			board.snakes[id].fly = true;
		
		if(item == Item.COLLECTIVE_ERASER)
		{
			// On efface tout les tracés de snakes (Enlever le tracé de la map)
			board.snakesMap.clear();
			
			// On met la tete des snakes dans la map 
			for(int i = 0; i < board.snakes.length; i++)
				snakeDrawHead(i);
		}	
		
	}
	
	/**
	 * 	Supprime l'item dans le currentItem d'un snake et  supprime son effet sur ce snake
	 * 
	 * @param id 	id du snake a qui l'on veux ajouter l'item
	 * @param item 	item que l'on veux affecter au snake
	 */
	public void snakeDeleteItem(int id, Item item)
	{
		//suppression de l'item dans la map currentItem
		board.snakes[id].currentItems.remove(item);
		
		//suppression de l'effet de l'item
		if(item == Item.USER_SPEED)
			board.snakes[id].movingSpeed /= 1.5 ;

		if(item == Item.USER_SLOW)
			board.snakes[id].movingSpeed *= 1.5;
		
		if(item == Item.USER_BIG_HOLE)
			board.snakes[id].holeRate -= 0.2;
		
		if(item == Item.OTHERS_SPEED)
			for( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
					board.snakes[i].movingSpeed /= 1.5;
			}
		
		if(item == Item.OTHERS_THICK)
			for( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
					board.snakes[i].headRadius -= 2;
			}
		
		if(item == Item.OTHERS_SLOW)
			for ( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
					board.snakes[i].movingSpeed *= 1.5;
			}
			
		if(item == Item.OTHERS_REVERSE)
			for ( int i = 0; i < board.snakes.length; i++)
			{
				if(i != id)
					board.snakes[i].inversion = false;
			}
		
		// rendre la probabilité d'apparition d'un item à sa valeur par défault
		if(item == Item.COLLECTIVE_THREE_CIRCLES)
			itemProbability /= 2;
		
		if(item == Item.COLLECTIVE_TRAVERSE_WALL)
			board.snakes[id].fly = false;
		
		//Item.COLLECTIVE_ERASER -> Pas d'effets a la fin de cette item
	}
	
	
	/**
	 * Applique à un snake les effets du au temps. 
	 * @param id 	l'id du snake
	 * @param time	le temps passé depuis le dernier appel de cette fonction
	 */
	public void snakeTimeImpact(int id, long time)
	{

		// Pour chaque items
		for (Map.Entry<Item, Long> item : board.snakes[id].currentItems.entrySet())	
		{
			// Je decrémente le temps écoulé depuis la dernière mise à jour
			board.snakes[id].currentItems.put(item.getKey(), item.getValue() - time);
			
			// Si le temps d'action est inferieure ou égale à 0, on enlève les effects de l'item
			if(item.getValue() <= 0) 
				snakeDeleteItem(id, item.getKey()); 
		}
	}

}