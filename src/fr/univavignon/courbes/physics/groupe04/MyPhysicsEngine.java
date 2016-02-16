package fr.univavignon.courbes.physics.groupe04;

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
 * @author Castillo Quentin
 * @author Latif Alexandre
 */
public class MyPhysicsEngine implements PhysicsEngine{
	/**
	 * snakeTable[][] : tableau à 2D contenant :
	 * 1er ligne   : playerID
	 * 2ème ligne  : cos respectifs
	 * 3ème ligne  : sin respectifs 
	 */
	public double[][] snakeTable;
	/**
	 * ourBoard : Board créé permettant de placer les snakes/items
	 */
	public Board ourBoard;
	/**
	 * itemRate : taux d'apparition d'un item, permettra de faire spawn un objet
	 */
	public double itemRate;   
	
	

	
	/**				VALIDÉ
	 * @param width Largeur de l'aire de jeu, exprimée en pixel.
	 * @param height Hauteur de l'aire de jeu, exprimée en pixel.
	 * @param profileIds Tableau contenant les numéros de profils des joueurs impliqués
	 * @return Un objet représentant l'aire de jeu de la manche.
	 */
	
	@Override

	public Board init(int width, int height, int[] profileIds) {
		
	Position spawn;
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
		spawn = snakeSpawnPos(width, height);
		ourBoard.snakes[i] = new Snake();
		initSnake(ourBoard.snakes[i], profileIds[i] , spawn);  // CONSTRUCTEUR
		System.out.println("Snake " + Integer.toString(i) + " spawn a la position " + Integer.toString(spawn.x) + " "+Integer.toString(spawn.y));
	}
	return ourBoard;  
	}

	
	
	/**				VALIDÉ
	 * Constructeur de Snake
	 * 
	 * @param snake Snake qui va être construit
	 * @param id Id du player dans la partie
	 * @param spawnPosition Position aléatoire récupérée grâce à snakeSpawnPos
	 */
	public void initSnake(Snake snake, int id, Position spawnPosition) {
		snake.currentItems  = new HashMap<Item, Long>() ;
		snake.playerId 	    = id;
		snake.currentX      = spawnPosition.x;
		snake.currentY      = spawnPosition.y;
		snake.currentAngle  = (int)(Math.random() * 359); //Génération aléatoire d'un angle entre 0 et 359°
		snake.headRadius 	= 4;  					// 4px 
		snake.movingSpeed   = 0.1;					// 1px / ms ?
		snake.turningSpeed  = 0.008; 				// ?
		snake.state 		= true;
		snake.collision 	= true;
		snake.inversion     = false;
		snake.fly   		= false;
		snake.holeRate 	    = 0.05;					// 5% ??	
		System.out.println("Angle en degré : " + Double.toString(snake.currentAngle));	
	}
	
	
	
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
	
	
	public void itemSpawn(int width, int height){
		Random r = new Random();
		// Création position avec deux paramétres aléatoires et avec une marge de 20px pour éviter de spawn sur les bords
		Position pos = new Position((r.nextInt((width-20)-20)+ 20), (r.nextInt((height-20)-20)+ 20));	
		Item p = Item.values()[(int) (Math.random() * Item.values().length)];
		sizeItemsPixels(p,pos);   // rempli le hashMap item
	}
	
	public void sizeItemsPixels(Item item, Position posi) { 
		Position pos = new Position(0,0);
		// faire un carré, puis récupérer les valeurs qui se trouve dans un cercle de centre snake et de diametre headRadius
		for(int i = posi.x - 20; i < posi.x + 20 ; i++) {    // Soit 20 la taille de l'item
			for(int j = posi.y - 20; j < posi.y + 20; j++) {
				if(Math.sqrt(Math.pow(i - posi.x, 2) + Math.pow(j - posi.y, 2)) < 20) {
					pos.x = i;
					pos.y = j;
					ourBoard.itemsMap.put(pos, item);
				}
			}
		}

	}
	
	
	/**				PAS TERMINÉ
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour, exprimé en ms.
	 * @param commands
	 * 		Map associant un joueur ID à la dernière commande générée par le joueur correspondant.
	 */
	@Override
	public void update(long elapsedTime, Map<Integer, Direction> commands) {

		// update directions des snakes par rapport au temps
		updateSnakesDirections(elapsedTime, commands);
		// update coordonnées des snakes par rapport au temps
		updateSnakesPositions(elapsedTime);
		
		if(itemRate == 1){
			itemSpawn(ourBoard.width,ourBoard.height);
		}
		else{
			itemRate+=0.01;		// a choisir
		}
		
		// TODO : Quoi d'autre a mettre a jour ? 
		// TODO : UPDATE ITEM pour savoir QUAND rajouter un item
	}

	
		
	/**					IMPORTANT A MODIFIER
	 * @param time Temps écoulé
	 */
	public void updateSnakesPositions(long time){
		long alterableTime;        // temps qui passe
		double pixel;              // permet de remplir la Map du board pixel à pixel
		boolean isMoving, samePos = false;  // permet le test de collisions
		Position pos = new Position(0,0);
		Position prec = new Position(0,0);
		
		for (int i = 0; i < ourBoard.snakes.length ; i++){
			alterableTime = time;
			pixel = 0;
			while(ourBoard.snakes[i].state && alterableTime > 0){   // SI SNAKE EST EN VIE & MOUVEMENTS PAS TERMINÉS
				while(alterableTime > 0 && pixel < 1){ 	// DIMINUE LE TEMPS TANT QU'ON A PAS FAIT UN PIXEL DE MOUVEMENT
					alterableTime--;
					pixel += ourBoard.snakes[i].movingSpeed;
				}
				
				/*  NOUS PERMET DE RECUPERER LA POSITION PRECEDENTE POUR GERER LA COLLISION*/
				prec.x = ourBoard.snakes[i].currentX;
				prec.y = ourBoard.snakes[i].currentY;
				
				snakeTable[ourBoard.snakes[i].profileId][0] += Math.cos(Math.toRadians(ourBoard.snakes[i].currentAngle)); 	
				snakeTable[ourBoard.snakes[i].profileId][1] += Math.sin(Math.toRadians(ourBoard.snakes[i].currentAngle));
				
				if(Math.round(snakeTable[ourBoard.snakes[i].profileId][1]) >= 1 && Math.round(snakeTable[ourBoard.snakes[i].profileId][0]) >= 1) {
					ourBoard.snakes[i].currentY++;
					ourBoard.snakes[i].currentX++;
					snakeTable[ourBoard.snakes[i].profileId][1]--;
					snakeTable[ourBoard.snakes[i].profileId][0]--;
					
					isMoving = true;
				}
				else if(Math.round(snakeTable[ourBoard.snakes[i].profileId][1]) <= -1 && Math.round(snakeTable[ourBoard.snakes[i].profileId][0]) >= 1) {
					ourBoard.snakes[i].currentY--;
					ourBoard.snakes[i].currentX++;
					snakeTable[ourBoard.snakes[i].profileId][1]++;
					snakeTable[ourBoard.snakes[i].profileId][0]--;
					
					isMoving = true;
				}
				else if(Math.round(snakeTable[ourBoard.snakes[i].profileId][1]) <= -1 && Math.round(snakeTable[ourBoard.snakes[i].profileId][0]) <= -1) {
					ourBoard.snakes[i].currentY--;
					ourBoard.snakes[i].currentX--;
					snakeTable[ourBoard.snakes[i].profileId][1]++;
					snakeTable[ourBoard.snakes[i].profileId][0]++;
					
					isMoving = true;
				}
				else if(Math.round(snakeTable[ourBoard.snakes[i].profileId][1]) >= 1 && Math.round(snakeTable[ourBoard.snakes[i].profileId][0]) <= -1) {
					ourBoard.snakes[i].currentY++;
					ourBoard.snakes[i].currentX--;
					snakeTable[ourBoard.snakes[i].profileId][1]--;
					snakeTable[ourBoard.snakes[i].profileId][0]++;
					isMoving = true;
				}
				// ON A DONC COS OU SINUS = 0, ETUDE DES DERNIERS CAS
				else if(Math.round(snakeTable[ourBoard.snakes[i].profileId][1]) >= 1) {
					ourBoard.snakes[i].currentY++;
					snakeTable[ourBoard.snakes[i].profileId][1]--;
					isMoving = true;
				}
				else if(Math.round(snakeTable[ourBoard.snakes[i].profileId][1]) <= -1) {
					ourBoard.snakes[i].currentY--;
					snakeTable[ourBoard.snakes[i].profileId][1]++;
					isMoving = true;
				}
				else if(Math.round(snakeTable[ourBoard.snakes[i].profileId][0])>= 1) {
					ourBoard.snakes[i].currentX++;
					snakeTable[ourBoard.snakes[i].profileId][0]--;
					isMoving = true;
				}
				else if(Math.round(snakeTable[ourBoard.snakes[i].profileId][0]) <= -1) {
					ourBoard.snakes[i].currentX--;				
					snakeTable[ourBoard.snakes[i].profileId][0]++;
					isMoving = true;
				}
				else{
					isMoving = true;
					samePos = true;
				}

				pixel--;  
				System.out.println("New Position snake "+ Integer.toString(ourBoard.snakes[i].playerId)+ " x:" + Integer.toString(ourBoard.snakes[i].currentX) + " y:" + Integer.toString(ourBoard.snakes[i].currentY));


				if(isMoving && !samePos){  // évite une répétition sur le même pixel
					snakeVsSnake(ourBoard.snakes[i],prec);
				}
				if(isMoving) {    // tests de collision
					outOfBounds(ourBoard.snakes[i]);
					snakeVsItem(ourBoard.snakes[i],pos);
					sizeSnakePixels(ourBoard.snakes[i]);
				}
			}
		}
	}
	
	
	
	
	
	/**				Faut Tester-> http://www.developpez.net/forums/d209/general-developpement/algorithme-mathematiques/general-algorithmique/savoir-1-point-l-interieur-d-cercle/
	 * 				racine_carre((x_point - x_centre)² + (y_centre - y_point)) < rayon ??
	 * @param snake Snake concerné
	 */
	public void sizeSnakePixels(Snake snake) { // Calculer les pixels qui doivent etre mit dans le hashmap a partir du size
		Position pos = new Position(0,0);  // contiendra les multiplise positions des pixels
		// faire un carré, puis récupérer les valeurs qui se trouve dans un cercle de centre snake et de diametre headRadius
		for(int i = snake.currentX - (int)snake.headRadius; i < snake.currentX + (int)snake.headRadius ; i++) {
			for(int j = snake.currentY - (int)snake.headRadius; j < snake.currentY + (int)snake.headRadius; j++) {
				if(Math.sqrt(Math.pow(i - snake.currentX, 2) + Math.pow(j - snake.currentY, 2)) < (int)snake.headRadius) {
					pos.x = i;
					pos.y = j;
					ourBoard.snakesMap.put(pos, snake.playerId);
				}
			}
		}

	}


	/**				VALIDÉ
	 * @param snake Snake testé
	 */
	public void outOfBounds(Snake snake) {
		// Si on sort du cadre
		if(snake.currentX < 1 || snake.currentX > ourBoard.width-1 || snake.currentY < 1|| snake.currentY > ourBoard.height-1) {
			if (!snake.fly) { // S'il ne peut pas traverser les murs 
				snake.state = false; 
				System.out.println(snake.playerId + " dead because of bounds!");
			} 
			else { // Envoyer le snake a l'opposé
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
	
	
	
	
	/**				VALIDÉ
	 * @param snake Snake testé
	 * @param prec Position du snake précedent
	 */
	public void snakeVsSnake(Snake snake, Position prec) {

	try{
		Position pos = new Position(snake.currentX,snake.currentY);
		boolean flag = false;
		if(	(Math.abs(pos.x - prec.x) <= 1 && Math.abs(pos.y - prec.y) <= 1) && (ourBoard.snakesMap.containsKey(prec) != ourBoard.snakesMap.containsKey(pos))){
			flag = true;
		}
		
		if(ourBoard.snakesMap.containsKey(pos) && snake.collision && !flag){
			snake.state = false;
			System.out.println("Snake n°"+snake.playerId+" is DEAD !");
			System.out.println("X = "+snake.currentX+"\tY= "+snake.currentY);
			System.out.println("Snake n°"+snake.playerId+" vient de rencontrer snake n°"+ourBoard.snakesMap.get(pos));			
		}
	}catch(NullPointerException e)
	{
		System.out.println("lol");
	}
	}
	
	

	
	/**			VALIDÉ
	 * @param snake Snake testé
	 * @param pos Position du snake et de l'objet supposé
	 */
	public void snakeVsItem(Snake snake, Position pos) {
		Item newItem = ourBoard.itemsMap.get(pos);
		if( newItem != null ) {
			if(snake.state) { // if alive
				addSnakeItem(snake.playerId, newItem);
				ourBoard.itemsMap.remove(pos); // Suppression de l'item sur la map
			}
		}	
	}
	
	
	
	/**				VALIDÉ
	 * @param time Temps écoulé
	 * @param commands Commande en cours pour chaque player (LEFT,RIGHT,NONE)
	 */
	public void updateSnakesDirections(long time, Map<Integer, Direction> commands)
	{
		Direction direction; // permet de récuper la direction de chaque snakes
		for(int i = 0 ; i < ourBoard.snakes.length ; i++)
		{
			direction = commands.get(ourBoard.snakes[i].playerId); // LEFT/RIGHT/NONE
			if(direction != null)
			{
				switch (direction)
				{
					case LEFT: // Si on va a gauche, il faut augmenter l'angle
						if(ourBoard.snakes[i].inversion) // test en cas d'inversion, si tout va bien, incrémentation de l'angle en fonction de la vitesse de rotation
							ourBoard.snakes[i].currentAngle -= time*Math.toDegrees(ourBoard.snakes[i].turningSpeed);
						else
							ourBoard.snakes[i].currentAngle += time*Math.toDegrees(ourBoard.snakes[i].turningSpeed);
						break;
					case RIGHT: // Si on va à droite, il faut diminuer l'angle
						if(ourBoard.snakes[i].inversion)
							ourBoard.snakes[i].currentAngle += time*Math.toDegrees(ourBoard.snakes[i].turningSpeed);
						else
							ourBoard.snakes[i].currentAngle -= time*Math.toDegrees(ourBoard.snakes[i].turningSpeed);
						break;
					case NONE: // rien ne change
						break;
					default:
						System.out.println("ERROR ???");
						break;
					}
			}
		}
	}
	
	
	
	/**						VALIDÉ
	 * Mise à jour forcé du board courant
	 * 
	 * @param board Board qui va écraser le board courant
	 */
	@Override
	public void forceUpdate(Board board) {
		this.ourBoard = board;
	}
	
	
	
	/**						VALIDÉ
	 * Nouvel item, associé à un ou plusieurs snakes
	 * 
	 * @param id Id player
	 * @param item Item concerné
	 */
	public void addSnakeItem(int id, Item item) {
		switch(item){
			case USER_SPEED:
				ourBoard.snakes[id].currentItems.put(item, (long)item.duration);
				ourBoard.snakes[id].movingSpeed *= 2;
				break;
			case USER_SLOW:
				ourBoard.snakes[id].currentItems.put(item, (long)item.duration);
				ourBoard.snakes[id].movingSpeed /= 1.5;
				break;
			case USER_BIG_HOLE:
				ourBoard.snakes[id].currentItems.put(item, (long)item.duration);
				ourBoard.snakes[id].holeRate /= 1.5;
				break;
			case OTHERS_SPEED:
				for(Snake snake : ourBoard.snakes) {
					if (snake.playerId != id) {
						snake.currentItems.put(item, (long)item.duration);
						snake.movingSpeed *= 2;
					}
				}
				break;
			case OTHERS_THICK:
				for(Snake snake : ourBoard.snakes) {
					if (snake.playerId != id) {
						snake.currentItems.put(item, (long)item.duration);
						snake.headRadius *= 1.5;
					}
				}
				break;
			case OTHERS_SLOW:
				for(Snake snake : ourBoard.snakes) {
					if (snake.playerId != id) {
						snake.currentItems.put(item, (long)item.duration);
						snake.movingSpeed /= 1.5;
					}
				}
				break;
			case OTHERS_REVERSE:
				for(Snake snake : ourBoard.snakes) {
					if (snake.playerId != id) {
						snake.currentItems.put(item, (long)item.duration);
						snake.inversion = true;
					}
				}
				break;
			case COLLECTIVE_THREE_CIRCLES:
				for(int i = 0 ; i<3;i++)
				{
					itemSpawn(ourBoard.width,ourBoard.height);
				}
				break;
			case COLLECTIVE_TRAVERSE_WALL:
				for(Snake snake : ourBoard.snakes) {
					snake.currentItems.put(item, (long)item.duration);
					snake.fly = true;
				}
				break;
			case COLLECTIVE_ERASER:
				ourBoard.snakesMap.clear();
				break;
			default:
				System.out.println("Error ??");
				break;
			}
	}


	
	
	/**						VALIDÉ
	 * Effet Item terminé
	 * 
	 * @param id Id player
	 * @param item Item concerné
	 */
	
	public void removeSnakeItem(int id, Item item) {
		
		switch(item)
		{
			case USER_SPEED:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].movingSpeed /= 2;
				break;
			case USER_SLOW:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].movingSpeed *= 1.5;
				break;
			case USER_BIG_HOLE:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].holeRate *= 1.5;
				break;
			case OTHERS_SPEED:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].movingSpeed /= 2;
				break;
			case OTHERS_THICK:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].headRadius /= 1.5;
				break;
			case OTHERS_SLOW:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].movingSpeed *= 1.5;
				break;
			case OTHERS_REVERSE:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].inversion = false;
				break;
			case COLLECTIVE_TRAVERSE_WALL:
				ourBoard.snakes[id].currentItems.remove(item);
				ourBoard.snakes[id].fly = false;
				break;
			default:
				System.out.println("Error ???");
				break;
		}
	}



	@Override
	public Board initDemo(int width, int height, int[] profileIds) {
		// TODO Auto-generated method stub
		return null;
	}	
}