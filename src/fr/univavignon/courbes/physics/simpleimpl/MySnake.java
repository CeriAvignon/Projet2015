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
import java.util.Queue;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;

/**
 * Classe fille de {@link Snake}, permettant d'intégrer
 * des méthodes propres au Moteur Physique. 
 */
public class MySnake extends Snake
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	public MySnake(int playerId, Profile profile, Board board)
	{	this.playerId = playerId;
		this.profileId = profile.profileId;
		
		resetCharacs();
		
		currentX = (int)Math.floor(Math.random()*board.width);	// on tire une valeur entre 0 et width-1
		currentY = (int)Math.floor(Math.random()*board.width);	// pareil entre 0 et height-1
		
		currentAngle = Math.random()*Math.PI*2;	// on tire une valeur réelle entre 0 et 2pi
		
		alive = true;
		
		collision = false;	// TODO plus efficace de faire un "mode début", plus général plutot que de traiter chaque serpent individuellement
		
		holeRate = Constants.HOLE_RATE;
		remainingHoleWidth = 0;
		
		currentItems = new LinkedList<ItemInstance>();
	}
	
	private void resetCharacs()
	{	// radius
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
	
	public void update()
	{	
		
	}
	
	public void updatePosition()
	{	
		
	}
	
	public void updateItemsEffect(long elapsedTime)
	{	resetCharacs();
		Iterator<ItemInstance> it = currentItems.iterator();
		while(it.hasNext())
		{	MyItemInstance item = (MyItemInstance)it.next();
			boolean remove = item.updateEffect(elapsedTime,this);
			if(remove)
				it.remove();
		}
	}
}
