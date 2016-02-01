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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
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
	
	public MyBoard(int width, int height)
	{	this.width = width;
		this.height = height;
		snakesMap = new HashMap<Position, Integer>();
		currentItems = new LinkedList<ItemInstance>();
		
		resetCharacs();
	}
	
	/** File contenant les items affectant actuellement cette aire de jeu */
	public Queue<ItemInstance> currentItems;
	/** Probabilité courange qu'un item apparaisse */
	public float itemPopupRate;
	/** Présence de la bordure ({@code true}) ou pas ({@code false}) */ 
	public boolean border;
	
	public void init(Profile[] profiles)
	{	snakes = new Snake[profiles.length];
		for(int i=0;i<profiles.length;i++)
		{	MySnake snake = new MySnake(i,profiles[i],this);
			snakes[i] = snake;
		}
	}
	
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
				snakesMap.put(pos,1);
			}
		}
		
		// on rajoute les items
		int sep = (Constants.ITEM_SIZE+10)/2;
		int x = 0;
		int y = sep;
		for(ItemType itemType: ItemType.values())
		{	x = x + sep;
			MyItemInstance item = new MyItemInstance(itemType,x,y);
			x = x + Constants.ITEM_SIZE;
			Position pos = new Position(x,y);
			itemsMap.put(pos, item);
		}
	}
	
	private void resetCharacs()
	{	itemPopupRate = Constants.BASE_ITEM_POPUP_RATE;
		border = true;
	}
	
	public void update()
	{	resetCharacs();
		
		updateItems();
	}
	
	private void updateItems()
	{
		// TODO

	}
}
