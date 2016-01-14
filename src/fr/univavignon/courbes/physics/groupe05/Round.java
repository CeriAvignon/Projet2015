package fr.univavignon.courbes.physics.groupe05;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.common.*;


/**
 * @author HUEBER Dimitri
 * @author NOVOTNY Cyril
 */

public class Round implements PhysicsEngine {
	
	//Board représentant le terrain de jeu
	Board board;
	
	//Frequence d'apparition des items sur le terrain, modifiable par l'objet "MoreLoops"
	double itemFrequency = 0.3;
	
	//Constructeur du Round, n'impliquant que la création d'un Board (initialisation par le biais d'une autre méthode)
	Round() {
		board = new Board();
	}
	
	//Initialise les données du moteur physique ainsi que l’aire de jeu, en utilisant les paramètres passés en paramètre. L’aire de jeu instanciée est renvoyée par la méthode afin d’être utilisée par le reste des composantes.
	@Override
	public Board init(int width, int height, int[] profileId) {
		board.width = width; //Largeur du terrain
		board.height = height; //Hauteur du terrain
		board.snakes = new Snake[profileId.length]; //Création de chacun des snakes (avec l'appartenance et sa longueur initiale)
		board.snakesMap = new HashMap<Position, Integer>(); //HashMap des corps des snakes
		board.itemsMap = new HashMap<Position, Item>(); //HashMap de l'emplacement des items
		
		//Pour chaque joueur (profileId) on créer un nouveau snake par le biais de la méthode snakeInit
		for (int i = 0; i < profileId.length; i++) {
			board.snakes[i] = new Snake();
			snakeInit(board.snakes[i], i, profileId[i], width/(profileId.length+1)*(i+1) , height/2,0);
		}
		
		//L'initialisation du board est terminée, on retourne le board
		return board;
	}
	
	//Met à jour les données physiques représentant l’état du jeu, en tenant compte du temps écoulé depuis la dernière mise à jour, qui est passé sous la forme du paramètre elapsedTime (exprimé en ms), et des dernières commandes des joueurs, passées sous forme de map.
	@Override
	public void update(long elapsedTime, Map<Integer,Direction> commands) {
		//On lance la fonction boardTimeModif lançant les modifications de temps sur le terrain et donc sur les snakes
		boardTimeModif(elapsedTime);
		
		//Création d'un identifiant player pour permettre le déplacement des snakes (touchant également au système de collisions)
		int playerId;
		
		//Pour chaque snake, on redéfinit playerId et lance la fonction de mouvement
		for (int i = 0; i < board.snakes.length; i++) {
			if(board.snakes[i].state) {
				playerId = board.snakes[i].profileId;
				snakeMovement(i, elapsedTime, commands.get(playerId));
			}
		}
		
		//Lancement de la procédure de création (ou non) d'item
		if(new Random().nextDouble() <= itemFrequency) {
			itemSpawn();
		}
	}

	//Méthode lançant les modifications de temps sur les snakes en jeu (nécéssaire pour la mise à jour des objets)
	public void boardTimeModif(long time) {
		for (int i = 0; i < board.snakes.length; i++) {
			snakeTimeModif(i, time);
		}
	}

	//Méthode lançant les mises à jour des objets des snakes (effets, durée, suppression)
	public void snakeTimeModif(int id, long time) {	
		//Parcours de l'HashMap des items du snake		
		for (Map.Entry<Item, Long> item : board.snakes[id].currentItems.entrySet()) {
			//Les temps sont soustraits depuis la dernière update
			board.snakes[id].currentItems.put(item.getKey(), item.getValue() - time);
			
			//Si le temps d'effet restant sur l'objet est inférieur ou égal à 0, les effets sont dissout par le biais d'itemDelete et l'objet est retiré de l'HashMap
			if(item.getValue() <= 0) {
				itemDelete(id, item.getKey());
				board.snakes[id].currentItems.remove(item.getKey());
			}
		}
	}

	//Méthode de suppression d'objet sur un snake (suppression des effets)
	public void itemDelete(int id, Item item) {
		//Suppression du bonus de vitesse
		if(item == Item.USER_SPEED) {
			board.snakes[id].currentSpeed /= 1.5 ;
		}
		
		//Suppression du malus de vitesse
		if(item == Item.USER_SLOW) {
			board.snakes[id].currentSpeed *= 1.5;
		}
		
		//Suppression du du changement de taux de trou
		if(item == Item.USER_BIG_HOLE) {
			board.snakes[id].holeRate -= 0.2;
		}
		
		//Suppression du bonus de vitesse pour les autres joueurs
		if(item == Item.OTHERS_SPEED) {
			for( int i = 0; i < board.snakes.length; i++) {
				if(i != id) {
					board.snakes[i].currentSpeed /= 1.5;
				}
			}
		}
		
		//Suppression du bonus de largeur de trait pour les autres joueurs
		if(item == Item.OTHERS_THICK) {
			for( int i = 0; i < board.snakes.length; i++) {
				if(i != id) {
					board.snakes[i].headRadius -= 2;
				}
			}
		}
		
		//Suppression du malus de vitesse pour les autres joueurs
		if(item == Item.OTHERS_SLOW) {
			for ( int i = 0; i < board.snakes.length; i++) {
				if(i != id) {
					board.snakes[i].currentSpeed *= 1.5;
				}
			}
		}
			
		//Suppression du malus de touches inversées
		if(item == Item.OTHERS_REVERSE) {
			for ( int i = 0; i < board.snakes.length; i++) {
				if(i != id) {
					board.snakes[i].inversion = false;
				}
			}
		}
		
		//Suppression de la modification de fréquence d'apparition des items sur la map
		if(item == Item.COLLECTIVE_THREE_CIRCLES) {
			itemFrequency /= 2;
		}
		
		//Suppression du bonus collectif de désactivation des collisions
		if(item == Item.COLLECTIVE_TRAVERSE_WALL) {
			board.snakes[id].fly = false;
		}
		
		// Efface entièrement le terrain (suppression des tracés, remise à 0 de l'Hashmap des corps des snakes)
		if(item == Item.COLLECTIVE_ERASER) {
			board.snakesMap.clear(); 
			for(int i = 0; i < board.snakes.length; i++) {
				board.snakesMap.put(new Position(board.snakes[i].currentX, board.snakes[i].currentY), board.snakes[i].playerId);
			}
		}
	}

	//Méthode d'ajout' d'objet sur un snake (ajout des effets)
	public void snakeAddItem(int id, Item item) {
		//Ajout du bonus de vitesse
		if(item == Item.USER_SPEED) {
			board.snakes[id].currentSpeed *= 1.5 ;
		}
		
		//Ajout du malus de vitesse
		if(item == Item.USER_SLOW) {
			board.snakes[id].currentSpeed /= 1.5;
		}
		
		//Ajout du du changement de taux de trou
		if(item == Item.USER_BIG_HOLE) {
			board.snakes[id].holeRate += 0.2;
		}
		
		//Ajout du bonus de vitesse pour les autres joueurs
		if(item == Item.OTHERS_SPEED) {
			for( int i = 0; i < board.snakes.length; i++) {
				if(i != id) {
					board.snakes[i].currentSpeed *= 1.5;
				}
			}
		}
		
		//Ajout du bonus de largeur de trait pour les autres joueurs
		if(item == Item.OTHERS_THICK) {
			for( int i = 0; i < board.snakes.length; i++) {
				if(i != id) {
					board.snakes[i].headRadius += 2;
				}
			}
		}
		
		//Ajout du malus de vitesse pour les autres joueurs
		if(item == Item.OTHERS_SLOW) {
			for ( int i = 0; i < board.snakes.length; i++) {
				if(i != id) {
					board.snakes[i].currentSpeed /= 1.5;
				}
			}
		}
			
		//Ajout du malus de touches inversées
		if(item == Item.OTHERS_REVERSE) {
			for ( int i = 0; i < board.snakes.length; i++) {
				if(i != id) {
					board.snakes[i].inversion = true;
				}
			}
		}
		
		//Ajout de la modification de fréquence d'apparition des items sur la map
		if(item == Item.COLLECTIVE_THREE_CIRCLES) {
			itemFrequency *= 2;
		}
		
		//Ajout du bonus collectif de désactivation des collisions
		if(item == Item.COLLECTIVE_TRAVERSE_WALL) {
			board.snakes[id].fly = true;
		}
		
		// Efface entièrement le terrain (suppression des tracés, remise à 0 de l'Hashmap des corps des snakes)
		if(item == Item.COLLECTIVE_ERASER) {
			board.snakesMap.clear(); 
			for(int i = 0; i < board.snakes.length; i++) {
				board.snakesMap.put(new Position(board.snakes[i].currentX, board.snakes[i].currentY), board.snakes[i].playerId);
			}
		}		
	}
	
	//Remplace l’aire de jeu courante par celle passée en paramètre. Le remplacement doit se faire de manière à ne pas induire de modification dans le traitement effectué par les autres composantes.
	@Override
	public void forceUpdate(Board board) {
		//Mise à jour de la hauteur
		this.board.height = board.height;
		//Mise à jour de la largeur
		this.board.width = board.width;
		
		//Mise à jour des snakes
		for(int i = 0; i < this.board.snakes.length; i++) {
			this.board.snakes[i] = board.snakes[i];
		}
		
		//Mise à jour des corps des snakes (clear)		
		this.board.snakesMap.clear();
		//Mise à jour des corps des snakes (reprise par un autre board)
		this.board.snakesMap.putAll(board.snakesMap);;
		//Mise à jour des items (clear)
		this.board.itemsMap.clear();
		//Mise à jour des items (reprise par un autre board)
		this.board.itemsMap.putAll(board.itemsMap);
	}
	
	//Création d'un item sur la map (emplacement et type aléatoire)
	public void itemSpawn() {
		//On génère des coordonnées (x et y) aléatoires
		int x = (int) (Math.random() * board.width);
		int y = (int) (Math.random() * board.height);
		
		//On génère une position à partir de ces coordonnées		
		Position p = new Position(x, y);
				
		//On choisit aléatoirement un objet dans la liste des valeurs possibles
		int nb = (int)(Math.random()*Item.values().length);
		
		//On créée l'item		
		Item item = Item.values()[nb];
				
		//On ajoute l'item à l'HashMap dédiée avec comme clé la position, comme valeur l'item
		board.itemsMap.put(p, item);
	}
	
	//Constructeur de snake (si l'on ne donne aucun paramètre, ceux par défaut sont appliqués)
	public Snake snakeInit(Snake snake, int playerId, int profileId, int currentX, int currentY, double currentAngle=0, double headRadius=1, double currentSpeed=0.5, double turningSpeed=1, boolean state=true, boolean collision=true, boolean inversion=false, double holeRate=0, boolean fly=false, int currentScore=0) {
		snake.playerId = playerId;
		snake.profileId = profileId;
		snake.currentX = currentX;
		snake.currentY = currentY;
		snake.currentAngle = currentAngle;
		snake.headRadius = headRadius;
		snake.currentSpeed = currentSpeed;
		snake.turningSpeed = turningSpeed;
		snake.state = state;
		snake.collision = collision;
		snake.inversion = inversion;
		snake.holeRate = holeRate;
		snake.fly = fly;
		snake.currentScore = currentScore;
		
		snake.currentItems = new HashMap<Item, Long>();

		return snake;
	}

	//Méthode traitant le mouvement du snake (et donc par le biais des méthodes appelées, le test des collisions)
	public void snakeMovement(int id, long elapsedTime, Direction command) {
		//Si une commande est rentrée, le snake tourne dans la direction souhaitée (vérification de l'inversion)
		if (command == Direction.LEFT || (command == Direction.RIGHT && board.snakes[id].inversion)){
			board.snakes[id].currentAngle +=  board.snakes[id].turningSpeed*elapsedTime;
		}

		if (command == Direction.RIGHT || (command == Direction.LEFT && board.snakes[id].inversion)) {
			board.snakes[id].currentAngle -=  board.snakes[id].turningSpeed*elapsedTime;
		}
		
		//On stocke la position initiale de la tête du snake (pour conserver une trace)
		double tmpX = board.snakes[id].currentX;
		double tmpY = board.snakes[id].currentY;
		//on deplace la tete vers l'objectif
		//On modifie par trigonométrie les futures coordonnées de la tête
		board.snakes[id].currentX += (int) Math.round((Math.cos(board.snakes[id].currentAngle)*board.snakes[id].currentSpeed*elapsedTime));
		board.snakes[id].currentY += (int) Math.round((Math.sin(board.snakes[id].currentAngle)*board.snakes[id].currentSpeed*elapsedTime));
		
		//On appelle la méthode de tracé	
		snakeAddBody(id, tmpX, tmpY);
	}

	// Méthode de "check" des collisions permettant de savoir s'il y a collision et s'il y a, de quel type de collision il s'agit : avec un tracé, un item, une bordure
	public Collision checkCollision(Position p, int id) {
		//Première vérification : y'a t-il une collision avec les bordures du terrain ? 
		if(p.x <= 0 || p.y <= 0 || p.x >= board.width-1 || p.y >= board.height-1) { 
			//Vérification de l'activation du mode avion permettant d'ignorer ce type de collision
			if(board.snakes[id].fly == false) {
				//Il y a collision avec la bordure, l'état du snake passe à "false" : il est mort
				board.snakes[id].state = false;
				return Collision.BORDER; 
			}
		}

		//Seconde vérification : y'a t-il une collision avec le tracé d'un snake ? 
		if(board.snakesMap.containsKey(p)) {
			//Vérification de l'activation du mode collision permettant d'ignorer ce type de collision
			if(board.snakes[id].collision == true) {
				//Il y a collsion avec un tracé, l'état du snake passe à "false" : il est mort
				board.snakes[id].state = false;
				return Collision.SNAKE;										
			}
		}
			
		//Dernière vérification : y'a t-il une collision avec un item ? 
		if(board.itemsMap.containsKey(p)) {
			//Ajout de l'Item à la liste des items actifs du snake
			board.snakes[id].currentItems.put(board.itemsMap.get(p), (long) board.itemsMap.get(p).duration);
			//Lancement de la méthode d'application des effets de l'objet
			snakeAddItem(id, board.itemsMap.get(p));
			return Collision.ITEM;
		}
		
		//Si aucun retour n'a été fait, c'est qu'il n'y a pas eu de collision
		return Collision.NONE;
	}

public void snakeAddBody(int id, double headX, double headY)
	{
		Position lastDrawnPosition = new Position(-1,-1);		
		double interX = board.snakes[id].currentX - headX;		
		double interY = board.snakes[id].currentY - headY;		
		double distance = Math.sqrt( Math.pow(interX, 2) + Math.pow(interY, 2)); 
		double stepX = interX / distance;
		double stepY = interY / distance;
		Collision vCollision;

		headX += stepX;
		headY += stepY;
					
		for (int i = 0 ; i < (int) distance ; i++) {
			Position po = new Position((int) Math.round(headX) , (int) Math.round(headY));
			if(!lastDrawnPosition.equals(po)) {	
				vCollision = checkCollision(po, id);					
				if(vCollision == Collision.NONE ||vCollision == Collision.ITEM) {	
					if(new Random().nextDouble() >= board.snakes[id].holeRate) {
						board.snakesMap.put(po, id);						
						board.snakes[id].currentX = po.x;						
						board.snakes[id].currentY = po.y;								
						System.out.println(po.x+" "+po.y);
					}

					lastDrawnPosition = po;				
				}
				

				else {
					if(vCollision == Collision.BORDER || vCollision == Collision.SNAKE) {					
						break;
					}
				}
			}
				
			headX += stepX;
			headY += stepY;
		
			if(board.snakes[id].fly) {
				if (headX >= board.width) {
					headX = 0;
				}
				if (headY >= board.height) {
					headY = 0;
				}
				if (headX < 0) {
					headX = board.width-1;
				}
				if (headY < 0) {
					headY = board.height-1;
				}
			}
		}
	}