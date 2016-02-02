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

import java.util.Random;

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
	/** Générateur aléatoire utilisé lors de l'apparition d'items */
	private static final Random RANDOM = new Random();
	
	/**
	 * Crée un item placé à la position spécifiée.
	 * 
	 * @param type
	 * 		Type de l'item à créer.
	 * @param x
	 * 		Position en abscisse.
	 * @param y
	 * 		Position en ordonnée.
	 */
	public MyItemInstance(ItemType type, int x, int y)
	{	init(type,x,y);
	}
	
	/**
	 * Crée un item placé au hasard sur l'aire de jeu spécifiée.
	 * 
	 * @param board
	 * 		Aire de jeu destinée à contenir l'item.
	 */
	public MyItemInstance(MyBoard board)
	{	// tirage au sort du type d'item
		int idx = RANDOM.nextInt(ItemType.values().length);
		ItemType type = ItemType.values()[idx];
		
		// tirage a sort de la position de l'item
		//TODO
		
		// on finit l'init
		init(type,x,y);
	}
	
	/**
	 * Initialise un item.
	 * 
	 * @param type
	 * 		Type de l'item.
	 * @param x
	 * 		Position en abscisse.
	 * @param y
	 * 		Position en ordonnée.
	 */
	private void init(ItemType type, int x, int y)
	{	this.type = type;
		this.x = x;
		this.y = y;
		
		remainingTime = Constants.ITEM_DURATION;
	}
	
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
	
	/**
	 * Met à jour l'effet d'un item s'appliquant sur un serpent.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 * @param snake
	 * 		Serpent concerné.
	 * @return
	 * 		{@code true} ssi l'item a terminé son effet et doit être
	 * 		retiré de la liste par le serpent appelant cette méthode.
	 */
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

	/**
	 * Met à jour l'effet d'un item collectif.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 * @param board
	 * 		Aire de jeu concernée.
	 * @return
	 * 		{@code true} ssi l'item a terminé son effet et doit être
	 * 		retiré de la liste par la Board appelant cette méthode.
	 */
	public boolean updateEffect(long elapsedTime, MyBoard board)
	{	switch(type)
		{	case COLLECTIVE_CLEAN:
				// cet item ne devrait pas être traité ici
				break;
			case COLLECTIVE_TRAVERSE:
				board.hasBorder = false;
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
