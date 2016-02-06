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
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

/**
 * Classe fille de {@link Snake}, permettant d'intégrer
 * des méthodes propres au Moteur Physique. 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class MySnake extends Snake
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Crée le serpent associé au numéro indiqué, pour le profil
	 * indiqué, sur l'aire de jeu indiquée.
	 * 
	 * @param playerId
	 * 		Numéro du joueur dans la manche en cours.
	 * @param board
	 * 		Aire de jeu de la manche en cours.
	 */
	public MySnake(int playerId, Board board)
	{	this.playerId = playerId;
		
		resetCharacs();
		
		Random random = new Random();
		int marginX = board.width / 10;	// marge de sécurité: un dixième de l'aire de jeu
		currentX = random.nextInt(board.width-2*marginX) + marginX; // on tire une valeur entre margin et width-1-margin
		realX = currentX;
		int marginY = board.height / 10;
		realY = currentY;
		currentY = random.nextInt(board.height-2*marginY) + marginY;	// pareil entre margin et height-1-margin
		
		trail = new TreeSet<Position>();

		currentAngle = (float)(Math.random()*Math.PI*2);	// on tire une valeur réelle entre 0 et 2pi
		
		eliminatedBy = null;
		
		remainingHole = 0;
		timeSinceLastHole = 0;
		
		currentItems = new LinkedList<ItemInstance>();
	}
	
	/** Rayon de la tête du serpent lors de la précédente itération (en pixels) */
	private transient int previousHeadRadius;
	/** Nombre de pixels restants pour terminer le trou courant */
	private transient int remainingHole;
	/** Temps écoulé depuis la fin du dernier trou (en ms) */
	private transient long timeSinceLastHole;
	/** Taille courante d'un trou (en pixels) */
	public transient int currentHoleWidth;
	/** Position en abscisse de la tête exprimée avec un réel */
	private float realX;
	/** Position en ordonnée de la tête exprimée avec un réel */
	private float realY;
	
	/**
	 * Réinitialise les caractéristiques du serpent qui sont
	 * susceptibles d'évoluer à chaque itération.
	 */
	private void resetCharacs()
	{	// radius
		previousHeadRadius = headRadius;
		headRadius = Constants.BASE_HEAD_RADIUS;
		
		// speed
		if(movingSpeed!=0) // juste pour le mode démo
			movingSpeed = Constants.BASE_MOVING_SPEED;
		turningSpeed = Constants.BASE_TURNING_SPEED;

		// connection
		connected = true;
		
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
		{	MyItemInstance item = (MyItemInstance)it.next();
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
			remainingHole = remainingHole - (int)dist;
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
if(currentAngle!=0)
	System.out.print("");
		double tempX = dist*Math.cos(currentAngle);
		double tempY = dist*Math.sin(currentAngle);
		
		// translation vers les coordonnées réelles de l'aire de jeu
		int x = currentX + (int)tempX;
		int y = currentY + (int)tempY;
		Position result = new Position(x,y);		
		
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
	 * @param graphicalTrail 
	 * 		Ensemble de pixels représentant la trainée <i>graphique</i>, i.e. qui sera affichée plus tard.
	 * 		Il faut noter que le Moteur Graphique est supposé dessiner la tête par dessus cette trainée.
	 * @param physicalTrail 
	 * 		Ensemble de pixels représentant la trainée <i>physique</i>, qui sera utilisée pour la gestion
	 * 		des collisions.
	 */
	private void processTrail(Board board, Position newPos, Set<Position> graphicalTrail, Set<Position> physicalTrail)
	{	Position oldPos = new Position(currentX,currentY);
		
		// on calcule le disque associé à l'ancienne position
		Set<Position> oldDisk = GraphicTools.processDisk(oldPos, previousHeadRadius);
				
		// on calcule le disque associé à la nouvelle position
		Set<Position> newDisk = GraphicTools.processDisk(newPos, headRadius);
		
		// on calcule le rectangle plein reliant les deux cercles
		Set<Position> rectangle = GraphicTools.processRectangle(oldPos,newPos,2*headRadius);
		
		// on soustrait le premier disque du rectangle pour obtenir un rectangle diminué des pixels déjà présent dans le premier disque
		rectangle.removeAll(oldDisk);
		// on soustrait la trainée courante du premier disque, pour obtenir un disque diminué de ses pixels déjà présent dans la traine
		oldDisk.removeAll(trail);

		// on fusionne le rectangle et le disque diminués, pour obtenir la trainée graphique (nouveaux pixels mais sans la nouvelle tête)
		graphicalTrail.addAll(rectangle);
		graphicalTrail.addAll(oldDisk);
		
		// on ajoute le second disque à cette forme, pour obtenir la trainée physique utilisée ensuite pour les collisions
		physicalTrail.addAll(graphicalTrail);
		physicalTrail.addAll(newDisk);
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
	 */
	private void detectCollisions(MyBoard board, Set<Position> physicalTrail)
	{	// on traite d'abord les items
		// TODO en supposant qu'on ne peut en toucher qu'un seul en une itération
		{	boolean itemCollided = false;
			Iterator<ItemInstance> it = board.items.iterator();
			// on traite chaque item, et on s'arrête à la première collision détectée
			while(!itemCollided && it.hasNext())
			{	MyItemInstance item = (MyItemInstance)it.next();
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
					|| pos.y>=board.height-Constants.BORDER_THICKNESS
					|| pos.x<=Constants.BORDER_THICKNESS
					|| pos.x>=board.width-Constants.BORDER_THICKNESS)
				{	// on marque la collision
					eliminatedBy = -1;
					// on restreint la nouvelle position du serpent
					it.remove();
				}
			}
		}
		
		// on traite les autres joueurs
		if(eliminatedBy==null && !board.entrance && !fly)
		{	int i = 0;
			while(i<board.snakes.length && eliminatedBy==null)
			{	Snake snake = board.snakes[i];
				// que pour les autres serpents, bien entendu
				if(snake!=this)
				{	boolean changed = physicalTrail.removeAll(snake.trail);
					if(changed)
						eliminatedBy = i;
				}
				i++;
			}	
		}
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
		if(!board.entrance && !fly && remainingHole<=0)
		{	if(eliminatedBy==null)
				trail.addAll(graphicalTrail);
			else
				trail.addAll(physicalTrail);
		}
		
		// mise à jour de l'angle
		if(inversion)
			direction = direction.getInverse();
		float delta = elapsedTime*turningSpeed*direction.value;
		currentAngle = (float)((currentAngle + delta + 2*Math.PI) % Math.PI);
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
	 */
	public void update(Board board, long elapsedTime, Direction direction)
	{	if(eliminatedBy==null)
		{	MyBoard myBoard = (MyBoard)board;
			
			// on met à jour l'effet des items déjà ramassés
			updateItemsEffect(elapsedTime);
			
			if(movingSpeed>0)
			{	// on met à jour les variables relatives à la création de trous dans la trainée
				updateHole(elapsedTime); // pourrait alternativement être placé à la fin de l'update
				
				// on calcule la nouvelle position (provisoire) de la tête du serpent
				Position newPos = processMove(elapsedTime);
				// on calcule la trainée engendrée par ce déplacement
				Set<Position> graphicalTrail = new TreeSet<Position>();
				Set<Position> physicalTrail = new TreeSet<Position>();
				processTrail(board,newPos,graphicalTrail,physicalTrail);
				
				// on normalise la nouvelle position de la tête et les positions contenues dans les deux trainées
				myBoard.normalizePosition(newPos);
				myBoard.normalizePositions(graphicalTrail);
				myBoard.normalizePositions(physicalTrail);
				
				// on détecte les collisions éventuelles
				detectCollisions(myBoard,physicalTrail);
				
				// on met à jour la position et la trainée de ce serpent
				updateData(board, newPos, graphicalTrail, physicalTrail, elapsedTime, direction);
			}
		}
	}
}

// TODO disposition aléatoire des items
