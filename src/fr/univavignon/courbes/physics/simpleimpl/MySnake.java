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
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;

/**
 * Classe fille de {@link Snake}, permettant d'intégrer
 * des méthodes propres au Moteur Physique. 
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
	 * @param profile
	 * 		Profil associé à ce même joueur.
	 * @param board
	 * 		Aire de jeu de la manche en cours.
	 */
	public MySnake(int playerId, Profile profile, Board board)
	{	this.playerId = playerId;
		this.profileId = profile.profileId;
		
		resetCharacs();
		
		Random random = new Random();
		int marginX = board.width / 10;	// marge de sécurité: un dixième de l'aire de jeu
		currentX = random.nextInt(board.width-2*marginX) + marginX; // on tire une valeur entre margin et width-1-margin
		int marginY = board.height / 10;
		currentY = random.nextInt(board.height-2*marginY) + marginY;	// pareil entre margin et height-1-margin
		
		trail = new TreeSet<Position>();

		currentAngle = (float)(Math.random()*Math.PI*2);	// on tire une valeur réelle entre 0 et 2pi
		
		alive = true;
		
		collision = false;	// TODO ça serait plus efficace de faire un "mode début", plus général plutot que de traiter chaque serpent individuellement
		
		holeRate = Constants.HOLE_RATE;
		remainingHoleWidth = 0;
		
		currentItems = new LinkedList<ItemInstance>();
	}
	
	/** Rayon de la tête du serpent lors de la précédente itération */
	private transient int previousHeadRadius;
	
	/**
	 * Réinitialise les caractéristiques du serpent qui sont
	 * susceptibles d'évoluer à chaque itération.
	 */
	private void resetCharacs()
	{	// radius
		previousHeadRadius = headRadius;
		headRadius = Constants.BASE_HEAD_RADIUS;
		
		// speed
		movingSpeed = Constants.BASE_MOVING_SPEED;
		turningSpeed = Constants.BASE_TURNING_SPEED;

		// connection
		connected = true;
		
		// commandes
		inversion = false;
		
		// collisions
		fly = false;
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
		int x = currentX + (int)tempX;
		int y = currentY + (int)tempY;
		Position result = new Position(x,y);

		return result;
	}
	
	/**
	 * Cette méthode calcule les pixels nouvellement occupés par le serpent à la suite de
	 * son dernier déplacement. La méthode utilisée est un peu bourrine, mais assez simple:
	 * <ol>
	 * 	<li>On calcule les disques correspondant à l'ancienne position de la tête et à sa nouvelle.</li>
	 * 	<li>On calcule le rectangle plein reliant ces deux cercles.</li>
	 * 	<li>On retire le premier disque du rectangle plein.</li>
	 * 	<li>On prend l'union de la forme obtenue et du second cercle (nouvelle position).</li>
	 * </ol>
	 * On obtient l'ensemble des pixels occupés par le serpent suite à son déplacement
	 * Cet ensemble peut ensuite être testé pour les collisions potentielles.
	 * 
	 * @param board
	 * 		Aire de jeu.
	 * @param newPos
	 * 		Nouvelle position de la tête du serpent.
	 * @return
	 * 		Ensemble des pixels nouvellement occupés par le serpent.
	 */
	private Set<Position> processTrail(Board board, Position newPos)
	{	Position oldPos = new Position(currentX,currentY);
		
		// on calcule le disque associé à l'ancienne position
		Set<Position> oldCercle = GraphicTools.processDisk(oldPos, previousHeadRadius);
				
		// on calcule le disque associé à la nouvelle position
		Set<Position> newCercle = GraphicTools.processDisk(newPos, headRadius);
		
		// on calcule le rectangle plein reliant les deux cercles
		Set<Position> result = GraphicTools.processRectangle(oldPos,newPos,headRadius); 
		
		// on retire le premier disque du rectangle plein pour obtenir une forme intermédiaire
		result.removeAll(oldCercle);
		
		// on ajoute le second disque, pour compléter cette forme
		result.addAll(newCercle);
		
		return result;	
	}
	
	/**
	 * Détermine si les pixels constituant la nouvelle trainée entrent
	 * en contact avec des objets préexistants.
	 * 
	 * @param board
	 * 		Aire de jeu courante.
	 * @param newTrail
	 * 		Nouveaux pixels de la trainée (normalisés).
	 * @return 
	 * 		Code entier indiquant l'absence de collision létale ({@code null}),
	 * 		la collision avec la bordure (valeur négative) ou avec un
	 * 		joueur (numéro du joueur dans la manche).
	 */
	private Integer detectCollisions(Board board, Set<Position> newTrail)
	{	Integer result = null;
		
		// on traite d'abord les items
		// TODO en supposant qu'on ne peut en toucher qu'un seul en une itération
		{	boolean itemCollided = false;
			Iterator<ItemInstance> it = board.items.iterator();
			// on traite chaque item, et on s'arrête à la première collision détectée
			while(!itemCollided && it.hasNext())
			{	MyItemInstance item = (MyItemInstance)it.next();
				// on traite chaque nouveau pixel de la trainée, en s'arrêtant à la première collision
				Iterator<Position> it2 = newTrail.iterator();
				while(!itemCollided && it2.hasNext())
				{	Position pos = it2.next();
					// on teste juste si le pixel du serpent est dans le rayon de l'item
					double dist = Math.sqrt(Math.pow(item.x-pos.x,2)+Math.pow(item.y-pos.y,2));
					if(dist<=Constants.ITEM_RADIUS)
					{	// on indique qu'on a touché un item (pour sortir des deux boucles)
						itemCollided = true;
						// on le sort de la liste des items encore en jeu
						it.remove();
						// on réinitialise l'item
						item.remainingTime = item.type.duration;
						item.x = -1;
						item.y = -1;
						// on l'ajoute parmi les items actifs du serpent
						currentItems.offer(item);
					}
				}
			}
		}
		
		// on traite la bordure, si elle existe
		if(board.hasBorder)
		{	Iterator<Position> it = newTrail.iterator();
			while(it.hasNext())
			{	Position pos = it.next();
				if(pos.y<=Constants.BORDER_THICKNESS
					|| pos.y>=board.height-Constants.BORDER_THICKNESS
					|| pos.x<=Constants.BORDER_THICKNESS
					|| pos.x>=board.width-Constants.BORDER_THICKNESS)
				{	// on marque la collision
					result = -1;
					// on restreint la nouvelle position du serpent
					it.remove();
				}
			}
		}
		
		// on traite les autres joueurs
		if(result==null)
		{	int i = 0;
			while(i<board.snakes.length && result==null)
			{	Snake snake = board.snakes[i];
				boolean changed = trail.removeAll(snake.trail);
				if(changed)
					result = i;
				i++;
			}	
		}
		
		return result;
	}
	
	/**
	 * Met à jour les nouvelles position, angle et trainée
	 * du serpent, en fonction des calculs effectués auparavant.
	 * 
	 * @param newPos
	 * 		Nouvelle position.
	 * @param newTrail
	 * 		Pixels à rajouter à la traine.
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 * @param direction
	 * 		Commande du joueur pour cette itération.
	 */
	private void updateData(Position newPos, Set<Position> newTrail, long elapsedTime, Direction direction)
	{	// mise à jour de la position
		currentX = newPos.x;
		currentY = newPos.y;
		
		// mise à jour de la traînée
		trail.addAll(newTrail);
		
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
	 * @return
	 * 		Code entier indiquant l'absence de collision létale ({@code null}),
	 * 		la collision avec la bordure (valeur négative) ou avec un
	 * 		joueur (numéro du joueur dans la manche).
	 */
	public Integer update(Board board, long elapsedTime, Direction direction)
	{	Integer result = null;
		
		if(alive)
		{	MyBoard myBoard = (MyBoard)board;
			
			// on met à jour l'effet des items déjà ramassés
			updateItemsEffect(elapsedTime);
			
			// on calcule la nouvelle position (provisoire) de la tête du serpent
			Position newPos = processMove(elapsedTime);
			// on calcule la trainée engendrée par ce déplacement
			Set<Position> newTrail = processTrail(board,newPos);
			
			// on normalise la position et la trainée
			myBoard.normalizePosition(newPos);
			myBoard.normalizePositions(newTrail);
			
			// on détecte les collisions éventuelles
			result = detectCollisions(myBoard,newTrail);
			
			// on met à jour la position et la trainée de ce serpent
			updateData(newPos, newTrail, elapsedTime, direction);
		}
		return result;
	}
}

// TODO manque les trous !
// TODO manque la gestion du mode ghost
// 		- dessin de la tête (pixels temporaires, à effacer)
// 		- collision avec les items mais pas le reste ?
//		- collision du reste avec lui?
// TODO manque la gestion du mode "entrée de jeu"
