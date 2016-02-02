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
import java.util.Map;
import java.util.Set;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.ItemType;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;

/**
 * Classe fille de {@link Board}, permettant d'intégrer
 * des méthodes propres au Moteur Physique. 
 */
public class MyBoard extends Board
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
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
		currentItems = new LinkedList<MyItemInstance>();
		
		resetCharacs();
	}
	
	/** File contenant les items affectant actuellement cette aire de jeu */
	public transient List<MyItemInstance> currentItems;
	/** Probabilité courante qu'un item apparaisse à chaque ms */
	public transient float itemPopupRate;
	
	/**
	 * Initialise l'aire de jeu pour une manche.
	 * 
	 * @param profiles
	 * 		profils des joueurs participants à la manche.
	 */
	public void init(Profile[] profiles)
	{	snakes = new Snake[profiles.length];
		for(int i=0;i<profiles.length;i++)
		{	MySnake snake = new MySnake(i,profiles[i],this);
			snakes[i] = snake;
		}
	}
	
	/**
	 * Initialise l'aire de jeu de manière à tester le
	 * jeu (relativement) facilement.
	 * 
	 * @param profiles
	 * 		profils des joueurs participants à la manche.
	 */
	public void initDemo(Profile[] profiles)
	{	// on initialise les serpents
		snakes = new Snake[profiles.length];
		for(int i=0;i<profiles.length;i++)
		{	MySnake snake = new MySnake(i,profiles[i],this);
			snake.movingSpeed = 0;	// on force leur vitesse à zéro
			snakes[i] = snake;
		}
		// seul le premier joueur a une vitesse de déplacement non-nulle
		snakes[0].movingSpeed = Constants.BASE_MOVING_SPEED;
		
		// on rajoute un bout de trainée au deuxième serpent, pour pouvoir tester les collisions
		int x1 = snakes[1].currentX;
		int y0 = snakes[1].currentY - Constants.BASE_HEAD_RADIUS/2;
		for(int x=0;x<x1;x++)
		{	for(int dy=0;dy<Constants.BASE_HEAD_RADIUS;dy++)
			{	Position pos = new Position(x,y0+dy);
				snakes[1].trail.add(pos);
			}
		}
		
		// on rajoute les items
		int sep = (Constants.ITEM_RADIUS+10)/2;
		int x = 0;
		int y = sep;
		for(ItemType itemType: ItemType.values())
		{	x = x + sep;
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
	public void update(long elapsedTime, Map<Integer,Direction> commands)
	{	resetCharacs();
		updateItems(elapsedTime);
		updateSnakes(elapsedTime,commands);
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
		double p = Math.random();
		if(p<itemPopupRate*elapsedTime)
		{	MyItemInstance item = new MyItemInstance(this);
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
	private void updateSnakes(long elapsedTime, Map<Integer,Direction> commands)
	{	// on évite de traiter les serpents toujours dans le même ordre, par souci d'équité
		List<Integer> idx = new ArrayList<Integer>();
		for(int i=0;i<snakes.length;i++)
			idx.add(i);
		Collections.shuffle(idx);
		
		// on traite ensuite chaque serpent, dans l'ordre prédéterminé
		for(int i: idx)
		{	Snake s = snakes[i];
			MySnake snake = (MySnake)s;
			Direction dir = commands.get(i);
			if(dir==null)
				dir = Direction.NONE;
			snake.update(this,elapsedTime,dir);
		}
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
