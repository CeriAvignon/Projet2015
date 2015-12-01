package fr.univavignon.courbes.physics.groupe16;

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

public class Round implements PhysicsEngine {
	private Board board;
	/** Représente les coordonnées aprés la virgule de la position d'un snake **/
	private double deltaSnake[][]; 
	/** Tableau contenant la vitesse angulaire de chaque snake en degrés par ms**/
	private double vitAngleSnake[];


	@Override
	public Board init(int width, int height, int playerNbr) {
		deltaSnake = new double[playerNbr][2];
		vitAngleSnake = new double[playerNbr];

		board = new Board();
		board.width = width;
		board.height = height;

		board.snakesMap = new HashMap<Position, Integer>();
		board.itemsMap = new HashMap<Position, Item>();
		board.snakes = new Snake[playerNbr];
		Position posSpawn;

		for (int i = 0; i < playerNbr ; i++) 
		{
			posSpawn = generateSpawnPos(width, height);
			board.snakes[i] = new Snake();
			initSnake(board.snakes[i], i, posSpawn);
			System.out.println("Snake " + Integer.toString(i) + " spawn a la position " + Integer.toString(posSpawn.x) + " "+Integer.toString(posSpawn.y));
		}

		return board;
	}

	/**
	 * @param snake
	 * @param id
	 * @param spawnPosition
	 */
	private void initSnake(Snake snake, int id, Position spawnPosition) {
		snake.currentItems  = new HashMap<Item, Long>() ;
		snake.playerId 	    = id;
		snake.currentX      = spawnPosition.x;
		snake.currentY      = spawnPosition.y;
		snake.currentAngle  = (int)(Math.random() * 359); //Génération aléatoire d'un angle entre 0 et 359°
		snake.headRadius 	= 1;		
		snake.currentSpeed  = 0.1;	
		vitAngleSnake[id]    = 0.5;
		snake.state 		= true;
		snake.collision 	= true;
		snake.inversion     = false;
		snake.fly   		= false;
		snake.holeRate 	    = 1.0;	
		System.out.println("Angle en degré : " + Double.toString(snake.currentAngle));	
	}


	@Override
	public void update(long elapsedTime, Map<Integer, Direction> commands) {

		// Mise à jour des coordonnées des snakes
		majSnakesPositions(elapsedTime);
		// Mise à jour des directions des snakes
		majSnakesDirections(elapsedTime, commands);
		// Mise à jour des effets liés aux snakes
		majSnakesEffects(elapsedTime);
	}


	/**
	 * Ajoute l'effet relatif à l'item ramassé aux snakes concernés.
	 * 
	 * @param id Id du Snake ayant ramassé l'objet
	 * @param item Item ramassé
	 */
	public void addSnakeEffect(int id, Item item) {
		switch(item)
		{
		case COLLECTIVE_ERASER: //TODO
			break;
		case COLLECTIVE_TRAVERSE_WALL:
			for(Snake snake : board.snakes) {
				board.snakes[id].collision = false;
			}
			break;
		case COLLECTIVE_THREE_CIRCLES: //TODO
			break;
		case OTHERS_REVERSE:
			for(Snake snake : board.snakes) {
				if (snake.playerId != id) {
					board.snakes[id].inversion = true;
				}
			}
			break;
		case OTHERS_SLOW:
			for(Snake snake : board.snakes) {
				if (snake.playerId != id) {
					board.snakes[id].currentSpeed /= 2;
				}
			}
			break;
		case OTHERS_THICK:
			for(Snake snake : board.snakes) {
				if (snake.playerId != id) {
					board.snakes[id].headRadius *= 2;
				}
			}
			break;
		case OTHERS_SPEED:
			for(Snake snake : board.snakes) {
				if (snake.playerId != id) {
					board.snakes[id].currentSpeed *= 2;
				}
			}
			break;
		case USER_BIG_HOLE:
			board.snakes[id].holeRate /= 2;
			break;
		case USER_SLOW:
			board.snakes[id].currentSpeed /= 2;
			break;
		case USER_SPEED:
			board.snakes[id].currentSpeed *= 2;
			break;
		default:
			break;
		}
	}

	/**
	 * Met à jour le temps restant des effets des snakes et met fin aux effets
	 * si leur temps est terminé.
	 * 
	 * @param elapsedTime Temps ecoulé depuis la derniére mise à jour de la board.
	 */
	private void majSnakesEffects(long elapsedTime) {

		for(Snake snake : board.snakes)
		{
			for (Map.Entry<Item, Long> entry : snake.currentItems.entrySet())
			{
				long remainingTime = entry.getValue();
				long refreshedTime = remainingTime - elapsedTime;

				// Enlever l'effet et supprimer l'objet de la liste
				if (refreshedTime <= 0 ) {
					snake.currentItems.remove(entry.getKey());
				}
				// Mettre à jour le temps restant pour l'effet de l'Item
				if (refreshedTime > 0) {
					snake.currentItems.put(entry.getKey(), refreshedTime);
				}



			}
		}

	}

	@Override
	public void forceUpdate(Board board) {
		// TODO Auto-generated method stub

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
	private Position generateSpawnPos(int widthBoard, int heightBoard) {

		Boolean flagPos;
		Position posSpawn = new Position();

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
	 * Elle verifie aussi si le snake n'est pas entré en contact avec un autre snake ou un item.
	 * @param elapsedTime Temps ecoulé en ms depuis le dernier update du plateau
	 */
	public void majSnakesPositions(long elapsedTime) {
		long elapsed;
		double pixStep;
		Position pos = new Position();
		for(Snake snake : board.snakes)
		{
			if(snake.state == true)
			{
				elapsed = elapsedTime;
				pixStep = 0;
				while (elapsed > 0)
				{
					while(pixStep < 1 && elapsed > 0)
					{
						elapsed--;
						pixStep += snake.currentSpeed;
					}
					if(pixStep >= 1)
					{
						deltaSnake[snake.playerId][0] += Math.cos(Math.toRadians(snake.currentAngle));
						deltaSnake[snake.playerId][1] += Math.sin(Math.toRadians(snake.currentAngle));

						if(deltaSnake[snake.playerId][1] >= 1 && deltaSnake[snake.playerId][0] >= 1) {
							snake.currentY--;
							snake.currentX++;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]--;
							deltaSnake[snake.playerId][0]--;
						}
						else if(deltaSnake[snake.playerId][1] <= -1 && deltaSnake[snake.playerId][0] >= 1) {
							snake.currentY++;
							snake.currentX++;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]++;
							deltaSnake[snake.playerId][0]--;
						}
						else if(deltaSnake[snake.playerId][1] <= -1 && deltaSnake[snake.playerId][0] <= -1) {
							snake.currentY++;
							snake.currentX--;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]++;
							deltaSnake[snake.playerId][0]++;
						}
						else if(deltaSnake[snake.playerId][1] >= 1 && deltaSnake[snake.playerId][0] <= -1) {
							snake.currentY--;
							snake.currentX--;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]--;
							deltaSnake[snake.playerId][0]++;
						}
						else if(deltaSnake[snake.playerId][1] >= 1) {
							snake.currentY--;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]--;
						}
						else if(deltaSnake[snake.playerId][1] <= -1) {
							snake.currentY++;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][1]++;
						}
						else if(deltaSnake[snake.playerId][0] >= 1) {
							snake.currentX++;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][0]--;
						}
						else if(deltaSnake[snake.playerId][0] <= -1) {
							snake.currentX--;
							pos.x = snake.currentX;
							pos.y = snake.currentY;
							board.snakesMap.put(pos , snake.playerId);
							deltaSnake[snake.playerId][0]++;
						}

						pixStep --;
						System.out.println("Position snake "+ Integer.toString(snake.playerId)+ " x:" + Integer.toString(snake.currentX) + " y:" + Integer.toString(snake.currentY));
					}
				}
				//TODO gérer si le snake se prend un mur

				// Gestion si le snake cogne un autre snake
				pos.x = snake.currentX;
				pos.y = snake.currentY;
				Integer idSnake = board.snakesMap.get(pos);
				if(idSnake != null && idSnake != snake.playerId)
				{
					snake.state = false;	
				}
				// Gérer si le snake se prend un item
				Item itemRecup = board.itemsMap.get(pos);
				if( itemRecup != null )
				{
					if(snake.state)
					{
						snake.currentItems.put(itemRecup, (long)itemRecup.duration); // TODO supprimer car c'est pas la que l'effet est ajouté  Ajout de l'item au Snake
						addSnakeEffect(snake.playerId, itemRecup); // Declenche l'effet de l'item
						board.itemsMap.remove(pos); // Suppression de l'item sur la map
					}
				}
			}
		}
	}


	/**
	 * Cette méthode met à jour les différents angles courants des snakes selon la direction
	 * demandée.
	 * @param elapsedTime 
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
					snake.currentAngle += elapsedTime*vitAngleSnake[snake.playerId];
					break;
				case RIGHT:
					snake.currentAngle -= elapsedTime*vitAngleSnake[snake.playerId];
					break;
				case NONE:
					break;
				default:
					break;
				}
			}
		}
	}
}
