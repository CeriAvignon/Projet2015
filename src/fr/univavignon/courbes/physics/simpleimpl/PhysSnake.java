package fr.univavignon.courbes.physics.simpleimpl;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Board.State;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager;

/**
 * Classe fille de {@link Snake}, permettant d'intégrer
 * des méthodes propres au Moteur Physique. 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class PhysSnake extends Snake
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	/** Taille maximale de la file {@link #prevDisks} */
	private final static int PREV_DISK_SIZE = 20;
	
	/**
	 * Crée le serpent associé au numéro indiqué, pour le profil
	 * indiqué, sur l'aire de jeu indiquée.
	 * 
	 * @param playerId
	 * 		Numéro du joueur dans la manche en cours.
	 * @param board
	 * 		Aire de jeu de la manche en cours.
	 */
	public PhysSnake(int playerId, Board board)
	{	this.playerId = playerId;
		
		movingSpeed = Constants.BASE_MOVING_SPEED;
		resetCharacs();
		
		int boardWidth = SettingsManager.getBoardWidth();
		int boardHeight = SettingsManager.getBoardHeight();
		Random random = new Random();
		int marginX = boardWidth / 10;	// marge de sécurité: un dixième de l'aire de jeu
		currentX = random.nextInt(boardWidth-2*marginX) + marginX; // on tire une valeur entre margin et width-1-margin
		realX = currentX;
		int marginY = boardHeight / 10;
		currentY = random.nextInt(boardHeight-2*marginY) + marginY;	// pareil entre margin et height-1-margin
		realY = currentY;
		prevPos = new Position(currentX,currentY);
		
		newTrail = new TreeSet<Position>();
		oldTrail = new TreeSet<Position>();
		prevDisks = new LinkedList<Set<Position>>();
		
		currentAngle = (float)(Math.random()*Math.PI*2);	// on tire une valeur réelle entre 0 et 2pi
				
		eliminatedBy = null;
		connected = true;
		
		remainingHole = 0;
		timeSinceLastHole = 0;
		
		currentItems = new LinkedList<ItemInstance>();
	}
	
	/**
	 * Empty constructor used for Kryonet network
	 */
	public PhysSnake(){}

	/**
	 * Crée le serpent associé au numéro indiqué, pour le profil
	 * indiqué, sur l'aire de jeu indiquée,à l'emplacement indiqué
	 * (méthode utilisée pour le mode démo).
	 * 
	 * @param playerId
	 * 		Numéro du joueur dans la manche en cours.
	 * @param board
	 * 		Aire de jeu de la manche en cours.
	 * @param x
	 * 		Position en abscisse.
	 * @param y
	 * 		Position en ordonnée.
	 */
	public PhysSnake(int playerId, Board board, int x, int y)
	{	this(playerId,board);
		
		currentX = x;
		realX = currentX;
		currentY = y;
		realY = currentY;
		prevPos = new Position(currentX,currentY);
	}

	/**
	 * Crée un nouveau serpent, qui est une copie de
	 * celui passée en paramètre.
	 * 
	 * @param snake
	 * 		Serpent à recopier.
	 */
	public PhysSnake(PhysSnake snake)
	{	// classe Snake
		this.playerId = snake.playerId;
		this.currentX = snake.currentX;
		this.currentY = snake.currentY;
		
		this.newTrail = new TreeSet<Position>(snake.newTrail);
		this.clearedTrail = snake.clearedTrail;
		
		this.currentAngle = snake.currentAngle;
		this.headRadius = snake.headRadius;
		this.movingSpeed = snake.movingSpeed;
		this.turningSpeed = snake.turningSpeed;
		
		this.eliminatedBy = snake.eliminatedBy;
		this.connected = snake.connected;
		
		this.inversion = snake.inversion;
		this.fly = snake.fly;
		
		this.currentItems = new LinkedList<ItemInstance>();
		Iterator<ItemInstance> it = snake.currentItems.iterator();
		while(it.hasNext())
		{	PhysItemInstance item = (PhysItemInstance)it.next();
			PhysItemInstance copy = new PhysItemInstance(item);
			this.currentItems.add(copy);
		}
		
		// classe PhysSnake
		this.oldTrail = new TreeSet<Position>(snake.oldTrail);
		this.remainingHole = snake.remainingHole;
		this.timeSinceLastHole = snake.timeSinceLastHole;
		this.currentHoleWidth = snake.currentHoleWidth;
		this.realX = snake.realX;
		this.realY = snake.realY;
		
		this.prevDisks = new LinkedList<Set<Position>>();
		for(Set<Position> set: snake.prevDisks)
		{	Set<Position> copy = new TreeSet<Position>(set);
			this.prevDisks.add(copy);
		}
		
		this.prevPos = new Position(snake.prevPos);
	}

	/** Ancienne section de la trainée du serpent sur l'aire de jeu */
	public Set<Position> oldTrail;
	/** Nombre de pixels restants pour terminer le trou courant */
	private float remainingHole;
	/** Temps écoulé depuis la fin du dernier trou (en ms) */
	private long timeSinceLastHole;
	/** Taille courante d'un trou (en pixels) */
	public int currentHoleWidth;
	/** Position en abscisse de la tête exprimée avec un réel */
	public float realX;
	/** Position en ordonnée de la tête exprimée avec un réel */
	public float realY;
	/** File contenant les derniers disques tracés pour représenter la trainée du serpent (utilisée pour les collisions) */
	public LinkedList<Set<Position>> prevDisks;
	/** Position <i>non-normalisée</i> à l'itération précédente */
	private Position prevPos;
	
	/**
	 * Réinitialise les caractéristiques du serpent qui sont
	 * susceptibles d'évoluer à chaque itération.
	 */
	private void resetCharacs()
	{	// radius
		headRadius = Constants.BASE_HEAD_RADIUS;
		
		// speed
		if(movingSpeed!=0) // juste pour le mode démo
			movingSpeed = Constants.BASE_MOVING_SPEED;
		turningSpeed = Constants.BASE_TURNING_SPEED;

		// connection
//		connected = true;
		
		// commandes
		inversion = false;
		
		// collisions
		fly = false;
		
		// trous
		currentHoleWidth = Constants.BASE_HOLE_WIDTH;
	}
	
	/**
	 * Met à jour l'effet des items sur ce serpent.
	 * Cela implique de supprimer les items qui ne sont
	 * plus actifs. 
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière itération.
	 */
	private void updateItemsEffect(long elapsedTime)
	{	resetCharacs();
		
		Iterator<ItemInstance> it = currentItems.iterator();
		while(it.hasNext())
		{	PhysItemInstance item = (PhysItemInstance)it.next();
			boolean remove = item.updateEffect(elapsedTime,this);
			if(remove)
				it.remove();
		}
	}
	
	/**
	 * Met à jour les variables relatives à la création de trous
	 * dans la traînée du serpent.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateHole(long elapsedTime)
	{	// on est déjà en train de faire un trou, on ne va pas en commencer un autre
		if(remainingHole>0)
		{	float dist = movingSpeed*elapsedTime;
			remainingHole = remainingHole - dist;
			timeSinceLastHole = 0;
		}
		
		// le délai minimal entre deux trous ne s'est pas encore écoulé  
		else if(timeSinceLastHole<Constants.MIN_HOLE_DELAY)
		{	remainingHole = 0;
			timeSinceLastHole = timeSinceLastHole + elapsedTime;
		}
		
		// le délai minimal est passé et on n'a pas de trou en cours
		else
		{	// on tire au sort pour déterminer si on commence un nouveau trou
			double p = Math.random();
			if(p<Constants.HOLE_RATE*elapsedTime)
			{	remainingHole = currentHoleWidth;
				timeSinceLastHole = 0;
			}
		}
	}
	
	/**
	 * Calcule la nouvelle position occupée par la tête du serpent.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 * @return
	 * 		Nouvelle position, non-normalisée.
	 */
	private Position processMove(long elapsedTime)
	{	// on considère la position courante comme le centre d'un repère polaire
		// la nouvelle position est exprimée par l'angle courant et la distance couverte
		// il suffit donc de convertir à des positions cartésiennes, puis de translater
		
		// distance parcourue dans le temps écoulé
		float dist = elapsedTime*movingSpeed;
		
		// conversion de polaire vers cartésien
		double tempX = dist*Math.cos(currentAngle);
		double tempY = dist*Math.sin(currentAngle);
		
		// translation vers les coordonnées réelles de l'aire de jeu
		realX = realX + (float)tempX;
		realY = realY + (float)tempY;
		Position result = new Position((int)Math.round(realX),(int)Math.round(realY));		
		
		return result;
	}
	
	/**
	 * Cette méthode calcule les pixels nouvellement occupés par le serpent à la suite de
	 * son dernier déplacement. Cf. les commentaires dans la méthode pour plus de détails.
	 * 
	 * @param board
	 * 		Aire de jeu.
	 * @param newPos
	 * 		Nouvelle position de la tête du serpent.
	 * @param graphTrail 
	 * 		Ensemble de pixels représentant la trainée <i>graphique</i>, i.e. qui sera affichée plus tard.
	 * 		Il faut noter que le Moteur Graphique est supposé dessiner la tête par dessus cette trainée.
	 * @param physTrail 
	 * 		Ensemble de pixels représentant la trainée <i>physique</i>, qui sera utilisée pour la gestion
	 * 		des collisions.
	 */
	private void processTrail(Board board, Position newPos, Set<Position> graphTrail, Set<Position> physTrail)
	{	//Position oldPos = new Position(currentX,currentY);
		
		// on identifie la trainée de pixels correspondant au déplacement
		if(!prevPos.equals(newPos))
		{	// segment allant de l'ancienne à la nouvelle position
			List<Position> segment = GeometricTools.processSegment(prevPos, newPos);
			
			Set<Position> newDisk = null;
			// on parcourt le segment en traçant les disques correspondants
			// (pas vraiment optimal comme algo, mais ça suffit ici)
			for(Position pos: segment)
			{	// on calcule le disque centré sur la position courante
				newDisk = GeometricTools.processDisk(pos, headRadius);
				// on le rajoute à la trainée graphique
				graphTrail.addAll(newDisk);
				// on le rajoute à la file des derniers disques
				prevDisks.offer(newDisk);
				if(prevDisks.size()>PREV_DISK_SIZE)
					prevDisks.poll();
			}
			
			// on calcule la trainée physique en retirant du tout dernier disque les disques le précédant
			physTrail.addAll(newDisk);
			for(Set<Position> prevDisk: prevDisks)
			{	if(prevDisk!=newDisk)
					physTrail.removeAll(prevDisk);
			}
		}
	}
	
	/**
	 * Détermine si les pixels constituant la nouvelle trainée entrent
	 * en contact avec des objets préexistants.
	 * 
	 * @param board
	 * 		Aire de jeu courante.
	 * @param physicalTrail 
	 * 		Ensemble de pixels représentant la trainée <i>physique</i>, qui sera utilisée pour la gestion
	 * 		des collisions.
	 * @return
	 * 		{@code true} ssi une collision létale a été détectée.
	 */
	private boolean detectCollisions(PhysBoard board, Set<Position> physicalTrail)
	{	boolean result = false;
		int boardWidth = SettingsManager.getBoardWidth();
		int boardHeight = SettingsManager.getBoardHeight();
		
		// on traite d'abord les items
		// TODO en supposant qu'on ne peut en toucher qu'un seul en une itération
		{	boolean itemCollided = false;
			Iterator<ItemInstance> it = board.items.iterator();
			// on traite chaque item, et on s'arrête à la première collision détectée
			while(!itemCollided && it.hasNext())
			{	PhysItemInstance item = (PhysItemInstance)it.next();
				// on traite chaque nouveau pixel de la trainée, en s'arrêtant à la première collision
				Iterator<Position> it2 = physicalTrail.iterator();
				while(!itemCollided && it2.hasNext())
				{	Position pos = it2.next();
					// on teste juste si le pixel du serpent est dans le rayon de l'item
					double dist = Math.sqrt(Math.pow(item.x-pos.x,2)+Math.pow(item.y-pos.y,2));
					if(dist<=Constants.ITEM_RADIUS)
					{	// on indique qu'on a touché un item (pour sortir des deux boucles)
						itemCollided = true;
						// on le sort de la liste des items encore en jeu
						it.remove();
						board.removedItems.add(item.itemId);
						// on le ramasse
						item.pickUp(board,this);
					}
				}
			}
		}
		
		// on traite la bordure, si elle existe
		if(board.hasBorder)
		{	Iterator<Position> it = physicalTrail.iterator();
			while(it.hasNext())
			{	Position pos = it.next();
				if(pos.y<=Constants.BORDER_THICKNESS
					|| pos.y>=boardHeight-Constants.BORDER_THICKNESS
					|| pos.x<=Constants.BORDER_THICKNESS
					|| pos.x>=boardWidth-Constants.BORDER_THICKNESS)
				{	// on marque la collision
					eliminatedBy = -1;
					result = true;
					// on restreint la nouvelle position du serpent
					it.remove();
				}
			}
		}
		
		// on traite les autres joueurs
		if(eliminatedBy==null && board.state==State.REGULAR && !fly && remainingHole<=0)
		{	int i = 0;
			while(i<board.snakes.length && eliminatedBy==null)
			{	PhysSnake snake = (PhysSnake)board.snakes[i];
//				if(snake!=this)
				{	boolean changed = physicalTrail.removeAll(snake.oldTrail)
						|| physicalTrail.removeAll(snake.newTrail);
					if(changed)
					{	eliminatedBy = i;
						result = true;
					}
				}
				i++;
			}	
		}
		
		return result;
	}
	
	/**
	 * Met à jour les nouvelles position, angle et trainée
	 * du serpent, en fonction des calculs effectués auparavant.
	 * 
	 * @param board
	 * 		Aire de jeu courante.
	 * @param newPos
	 * 		Nouvelle position.
	 * @param graphicalTrail 
	 * 		Ensemble de pixels représentant la trainée <i>graphique</i>, i.e. qui sera affichée plus tard.
	 * 		Il faut noter que le Moteur Graphique est supposé dessiner la tête par dessus cette trainée.
	 * @param physicalTrail 
	 * 		Ensemble de pixels représentant la trainée <i>physique</i>, qui a été utilisée pour la gestion
	 * 		des collisions.
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 * @param direction
	 * 		Commande du joueur pour cette itération.
	 */
	private void updateData(Board board, Position newPos, Set<Position> graphicalTrail, Set<Position> physicalTrail, long elapsedTime, Direction direction)
	{	// mise à jour de la position
		currentX = newPos.x;
		currentY = newPos.y;
		
		// mise à jour de la traînée
		if(board.state==State.REGULAR && !fly && remainingHole<=0)
		{	oldTrail.addAll(newTrail);
			newTrail.clear();
			if(eliminatedBy==null)
				newTrail.addAll(graphicalTrail);
			else
				newTrail.addAll(physicalTrail);
		}
		
		// mise à jour de l'angle
		if(inversion)
			direction = direction.getInverse();
		float delta = elapsedTime*turningSpeed*direction.value;
		currentAngle = (float)((currentAngle + delta + 2*Math.PI) % (2*Math.PI));
	}

	/**
	 * Met à jour le serpent : effets des items, déplacement, collisions.
	 * 
	 * @param board
	 * 		Aire de jeu courante.
	 * @param elapsedTime
	 * 		Temps écoulé.
	 * @param direction
	 * 		Commande du joueur pour cette itération.
	 * @return
	 * 		{@code true} ssi le serpent a été éliminé à cette itération.
	 */
	public boolean update(Board board, long elapsedTime, Direction direction)
	{	boolean result = false;
		
		if(eliminatedBy==null && board.state!=State.PRESENTATION)
		{	PhysBoard myBoard = (PhysBoard)board;
			
			// on met à jour l'effet des items déjà ramassés
			updateItemsEffect(elapsedTime);
			
			if(movingSpeed>0)
			{	// on met à jour les variables relatives à la création de trous dans la trainée
				updateHole(elapsedTime); // pourrait alternativement être placé à la fin de l'update
				
				// on calcule la nouvelle position (provisoire) de la tête du serpent
				Position newPos = processMove(elapsedTime);
				// on calcule la trainée engendrée par ce déplacement
				Set<Position> graphTrail = new TreeSet<Position>();
				Set<Position> physTrail = new TreeSet<Position>();
				processTrail(board,newPos,graphTrail,physTrail);
				
				// on normalise la nouvelle position de la tête et les positions contenues dans les deux trainées
				prevPos = newPos;
				newPos = myBoard.normalizePosition(newPos);
				graphTrail = myBoard.normalizePositions(graphTrail);
				physTrail = myBoard.normalizePositions(physTrail);
				
				// on détecte les collisions éventuelles
				result = detectCollisions(myBoard,physTrail);
				
				// on met à jour la position et la trainée de ce serpent
				updateData(board, newPos, graphTrail, physTrail, elapsedTime, direction);
			}
		}
	
		return result;
	}
}
