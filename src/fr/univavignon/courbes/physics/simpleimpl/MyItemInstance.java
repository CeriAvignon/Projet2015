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

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.ItemType;

/**
 * Classe fille de {@link ItemInstance}, permettant d'intégrer
 * des méthodes propres au Moteur Physique. 
 */
public class MyItemInstance extends ItemInstance
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	public MyItemInstance(ItemType type, int x, int y)
	{	this.type = type;
		this.x = x;
		this.y = y;
		
		remainingTime = Constants.ITEM_DURATION;
		inGame = true;
	}
	
	/** Indique si l'item est toujours en jeu ({@code true}) ou bien s'il a déjà été ramassé ({@code false}) */
	private boolean inGame = true; 
	
	/**
	 * Met à jour un item actuellement en jeu (i.e. pas encore ramassé).
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 * @return
	 * 		{@code true} ssi l'item est arrivé en fin de vie et doit disparaitre.
	 */
	public boolean updateLife(long elapsedTime)
	{	remainingTime = remainingTime - elapsedTime;
		boolean remove = remainingTime>0;
		return remove;
	}
	
	// appelée par le serpent concerné
	public boolean updateEffect(long elapsedTime, MySnake snake)
	{	switch(type)
		{	case OTHERS_FAST:
				
				break;
			case OTHERS_REVERSE:
				
				break;
			case OTHERS_THICK:
				
				break;
			case OTHERS_SLOW:
				
				break;
			case USER_FAST:
				
				break;
			case USER_FLY:
				
				break;
			case USER_SLOW:
				
				break;
		}
		
		// on met à jour le temps restant
		remainingTime = remainingTime - elapsedTime;
		boolean remove = remainingTime>0;
		return remove;
	}

	// appelée par le board
	public boolean updateEffect(long elapsedTime, MyBoard board)
	{	switch(type)
		{	case COLLECTIVE_CLEAN:
				// cet item ne devrait pas être traité ici
				break;
			case COLLECTIVE_TRAVERSE:
				board.border = false;
				break;
			case COLLECTIVE_WEALTH:
				board.itemPopupRate = board.itemPopupRate*Constants.ITEM_POPUP_COEFF;
				break;
		}
	
		// on met à jour le temps restant
		remainingTime = remainingTime - elapsedTime;
		boolean remove = remainingTime>0;
		return remove;
	}
}
