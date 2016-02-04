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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.ItemType;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

/**
 * Classe fille de {@link Board}, permettant d'intégrer
 * des méthodes propres au Moteur Physique. 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class MyBoard extends Board
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	/** Générateur aléatoire utilisé lors de l'apparition d'items */
	private static final Random RANDOM = new Random();
	
	/**
	 * Crée une nouvelle aire de jeu, à initialiser ensuite.
	 * 
	 * @param width
	 * 		Largeur en pixels.
	 * @param height
	 * 		hauteur en pixels.
	 */
	public MyBoard(int width, int height)
	{	this.width = width;
		this.height = height;
		
		items = new ArrayList<ItemInstance>();
		currentItems = new LinkedList<MyItemInstance>();
		totalTime = 0;
		mustClean = false;
		
		resetCharacs();
	}
	
	/** File contenant les items affectant actuellement cette aire de jeu */
	public transient List<MyItemInstance> currentItems;
	/** Probabilité courante qu'un item apparaisse à chaque ms */
	public transient float itemPopupRate;
	/** Temps total écoulé depuis le début de la partie */
	public transient long totalTime;
	/** Marqueur indiquant qu'il faut nettoyer l'aire de jeu des traînées */
	public transient boolean mustClean;
	
	/**
	 * Initialise l'aire de jeu pour une manche.
	 * 
	 * @param players
	 * 		Joueurs participants à la manche.
	 */
	public void init(Player[] players)
	{	snakes = new Snake[players.length];
		for(int i=0;i<players.length;i++)
		{	MySnake snake = new MySnake(i,this);
			snakes[i] = snake;
		}
	}
	
	/**
	 * Initialise l'aire de jeu de manière à tester le
	 * jeu (relativement) facilement.
	 * 
	 * @param players
	 * 		Joueurs participants à la manche.
	 */
	public void initDemo(Player[] players)
	{	// on initialise les serpents (on suppose qu'il y en a seulement 2)
		snakes = new Snake[players.length];
		for(int i=0;i<players.length;i++)
		{	MySnake snake = new MySnake(i,this);
			snake.movingSpeed = 0;	// on force leur vitesse à zéro
			snakes[i] = snake;
		}
		// seul le premier joueur a une vitesse de déplacement non-nulle
		snakes[0].movingSpeed = Constants.BASE_MOVING_SPEED;
		// on place le premier joueur
		snakes[0].currentX = width*3/4;
		snakes[0].currentY = height*3/4;
		snakes[0].currentAngle = 0;
		// on centre le second joueur
		snakes[1].currentX = width/2;
		snakes[1].currentY = height/2;
		
		// on rajoute un bout de trainée au deuxième serpent, pour pouvoir tester les collisions
		int x1 = snakes[1].currentX;
		int y0 = snakes[1].currentY - snakes[1].headRadius;
		for(int x=Constants.BORDER_THICKNESS;x<x1;x++)
		{	for(int dy=0;dy<snakes[1].headRadius*2;dy++)
			{	Position pos = new Position(x,y0+dy);
				snakes[1].trail.add(pos);
			}
		}
		
		// on rajoute les items
		int sep = (int)((width-2*Constants.BORDER_THICKNESS-2*Constants.ITEM_RADIUS*ItemType.values().length)/(ItemType.values().length+1f));
		int x = Constants.BORDER_THICKNESS;
		int y = Constants.BORDER_THICKNESS + sep + Constants.ITEM_RADIUS;
		for(ItemType itemType: ItemType.values())
		{	x = x + sep + Constants.ITEM_RADIUS;
			MyItemInstance item = new MyItemInstance(itemType,x,y);
			x = x + Constants.ITEM_RADIUS;
			items.add(item);
		}
	}
	
	/**
	 * Réinitialise les charactéristiques de l'aire de jeu
	 * qui sont susceptibles de changer à chaque itération.
	 */
	private void resetCharacs()
	{	itemPopupRate = Constants.BASE_ITEM_POPUP_RATE;
		hasBorder = true;
	}
	
	/**
	 * Mise à jour de l'aire de jeu et de tout ce
	 * qu'elle contient.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 * @param commands
	 * 		Commandes de chaque joueur.
	 */
	public void update(long elapsedTime, Direction[] commands)
	{	// on réinitialise les paramètres de l'aire de jeu susceptibles de changer à chaque itération
		resetCharacs();
		
		// si on est au début de jeu, on met le compteur à jour
		updateEntrance(elapsedTime);
		
		// on met à jour les items (items encore en jeu et items collectifs ramassés)
		updateItems(elapsedTime);
		
		// on met à jour les serpents
		updateSnakes(elapsedTime,commands);
		
		// si nécessaire, on nettoie l'aire de jeu des trainées des serpents 
		if(mustClean)
			cleanSnakes();
	}
	
	/**
	 * Met à jour l'état d'entrée du jeu, i.e. la période
	 * en début de manche, pendant laquelle les collisions
	 * sont désactivées.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateEntrance(long elapsedTime)
	{	totalTime = totalTime + elapsedTime;
		entrance = totalTime <= Constants.ENTRANCE_DURATION;
	}
	
	/**
	 * Mise à jour des items situés dans l'aire de jeu
	 * et de l'effet des items collectifs.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateItems(long elapsedTime)
	{	// màj des items présents dans l'aire de jeu
		{	Iterator<ItemInstance> it = items.iterator();
			while(it.hasNext())
			{	MyItemInstance item = (MyItemInstance)it.next();
				boolean remove = item.updateLife(elapsedTime);
				if(remove)
					it.remove();
			}
		}
		
		// màj des items collectifs déjà ramassés par des joueurs
		{	Iterator<MyItemInstance> it = currentItems.iterator();
			while(it.hasNext())
			{	MyItemInstance item = it.next();
				boolean remove = item.updateEffect(elapsedTime, this);
				if(remove)
					it.remove();
			}
		}
		
		// faut-il faire apparaitre un item?
		float p = RANDOM.nextFloat();
		if(!entrance && p<itemPopupRate*elapsedTime)
		{	MyItemInstance item = generateItem();
			if(item!=null)
				items.add(item);
		}
	}
	
	/**
	 * Mise à jour des serpents.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 * @param commands
	 * 		Commandes de chaque joueur.
	 */
	private void updateSnakes(long elapsedTime, Direction[] commands)
	{	// on évite de traiter les serpents toujours dans le même ordre, par souci d'équité
		List<Integer> idx = new ArrayList<Integer>();
		for(int i=0;i<snakes.length;i++)
			idx.add(i);
		Collections.shuffle(idx);
		
		// on traite ensuite chaque serpent, dans l'ordre prédéterminé
		for(int i: idx)
		{	Snake s = snakes[i];
			MySnake snake = (MySnake)s;
			Direction dir = commands[i];
			if(dir==null)
				dir = Direction.NONE;
			snake.update(this,elapsedTime,dir);
System.out.println(i+": "+snake.currentX+";"+snake.currentY);
		}
	}
	
	/**
	 * Supprime les trainées de tous les serpents.
	 */
	private void cleanSnakes()
	{	mustClean = false;
		for(Snake snake: snakes)
			snake.trail.clear();
	}
	
	/**
	 * Génère un item de type et de position aléatoires.
	 * 
	 * @return
	 * 		L'item généré, ou {@code null} s'il n'y a pas actuellement la place nécessaire 
	 * 		sur l'aire de jeu.
	 */
	private MyItemInstance generateItem()
	{	MyItemInstance result = null;
		
		// tirage a sort de la position de l'item
		// pour bien faire, il faudrait tirer au sort parmi toutes les positions disponibles, mais cela serait bien trop long
		// donc on va au plus rapide : on tire parmi toutes les positions, et on teste si elle est disponible. sinon, on recommence.
		// on met une limite sur le nombre d'essais, au cas où l'aire de jeu serait encombrée
		int x,y,i=0;
		boolean available = false;
		do
		{	// on tire la position au sort, hors-bordure
			x = RANDOM.nextInt(width-Constants.BORDER_THICKNESS*2)+Constants.BORDER_THICKNESS;
			y = RANDOM.nextInt(height-Constants.BORDER_THICKNESS*2)+Constants.BORDER_THICKNESS;
			
			// on récupère un disque représentant l'espace occupé par l'item (plus une marge)
			Position center = new Position(x,y);
			int radius = Constants.ITEM_RADIUS+10;
			Set<Position> itemReach = GraphicTools.processDisk(center, radius);
			available = true;
			
			// on compare aux autres items
			{	Iterator<ItemInstance> it = items.iterator();
				while(it.hasNext() && available)
				{	ItemInstance item = it.next();
					Position c = new Position(item.x,item.y);
					Set<Position> ir = GraphicTools.processDisk(c, radius);
					available = !itemReach.removeAll(ir);
				}
			}
			
			// on compare aux trainées des serpents
			{	int s = 0;
				while(s<snakes.length && available)
				{	Snake snake = snakes[s];
					available = !itemReach.removeAll(snake.trail);
					s++;
				}
			}
			
			i++;
		}
		while(i<10 && !available);
		
		if(available)
			result = new MyItemInstance(x,y);
		return result;
	}

	/**
	 * Normalise la position de manière à tenir compte des limites de l'aire
	 * de jeu. Autrement dit, si une position sort de l'aire de jeu, on la
	 * modifie de manière à apparaître de l'autre côté. Par exemple, si elle
	 * est trop basse, on la fait réapparaitre en haut de l'aire de jeu, de
	 * manière à donner l'illusion que les côtés opposés sont reliés.
	 * 
	 * @param position
	 * 		Position à normaliser.
	 */
	public void normalizePosition(Position position)
	{	position.x = (position.x + width) % width;
		position.y = (position.y + height) % height;
	}

	/**
	 * Normalise l'ensemble des positions spécifié, cf. la méthode
	 * {@link #normalizePosition(Position)} pour plus de détails.
	 * 
	 * @param positions
	 * 		Ensemble de positions à normaliser.
	 */
	public void normalizePositions(Set<Position> positions)
	{	for(Position pos: positions)
			normalizePosition(pos);
	}
}
