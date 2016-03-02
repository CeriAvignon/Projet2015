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
import fr.univavignon.courbes.common.Snake;

/**
 * Classe fille de {@link ItemInstance}, permettant d'intégrer
 * des méthodes propres au Moteur Physique. 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class PhysItemInstance extends ItemInstance
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	/** Compteur pour attribuer des numéros uniques aux items */
	private static int ID_COUNT = 0;
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
	public PhysItemInstance(ItemType type, int x, int y)
	{	init(type,x,y);
	}
	
	/**
	 * Crée un item de type aléatoire.
	 * 
	 * @param x
	 * 		Position en abscisse.
	 * @param y
	 * 		Position en ordonnée.
	 */
	public PhysItemInstance(int x, int y)
	{	// tirage au sort du type d'item
		int idx = RANDOM.nextInt(ItemType.values().length);
		ItemType type = ItemType.values()[idx];
		
		// on finit l'init
		init(type,x,y);
	}
	
	/**
	 * Empty constructor used by kryonet network
	 */
	public PhysItemInstance(){}

	/**
	 * Instancie un nouvel item qui est une copie de celui
	 * passé en paramètre.
	 * 
	 * @param item
	 * 		L'item à cloner.
	 */
	public PhysItemInstance(PhysItemInstance item)
	{	// classe ItemInstance
		this.x = item.x;
		this.y = item.y;
		this.type = item.type;
		this.remainingTime = item.remainingTime;
		
		// classe PhysItemInstance
		this.itemId = item.itemId;
	}
	
	/** Numéro de l'item (utilisé pour le mode réseau) */
	public int itemId;
	
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
	{	this.itemId = ID_COUNT++;
		
		this.type = type;
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
	{	boolean remove = false;
		if(remainingTime>0)
		{	remainingTime = remainingTime - elapsedTime;
			remove = remainingTime<0;
		}
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
	public boolean updateEffect(long elapsedTime, PhysSnake snake)
	{	switch(type)
		{	case OTHERS_FAST:
				snake.movingSpeed = snake.movingSpeed*Constants.MOVING_SPEED_COEFF;
				break;
			case OTHERS_REVERSE:
				snake.inversion = true;
				break;
			case OTHERS_THICK:
				snake.headRadius = (int)(snake.headRadius*Constants.HEAD_RADIUS_COEFF);
				snake.currentHoleWidth = (int)(snake.currentHoleWidth*Constants.HEAD_RADIUS_COEFF);
				break;
			case OTHERS_SLOW:
				snake.movingSpeed = snake.movingSpeed/Constants.MOVING_SPEED_COEFF;
				snake.turningSpeed = snake.turningSpeed/Constants.MOVING_SPEED_COEFF;
				break;
			case USER_FAST:
				snake.movingSpeed = snake.movingSpeed*Constants.MOVING_SPEED_COEFF;
				snake.turningSpeed = snake.turningSpeed*Constants.MOVING_SPEED_COEFF;
				break;
			case USER_FLY:
				snake.fly = true;
				break;
			case USER_SLOW:
				snake.movingSpeed = snake.movingSpeed/Constants.MOVING_SPEED_COEFF;
				snake.turningSpeed = snake.turningSpeed/Constants.MOVING_SPEED_COEFF*Constants.TURNING_COEFF;
				break;
		}
		
		// on met à jour le temps restant
		remainingTime = remainingTime - elapsedTime;
		boolean remove = remainingTime<=0;
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
	public boolean updateEffect(long elapsedTime, PhysBoard board)
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
		boolean remove = remainingTime<=0;
		return remove;
	}
	
	/**
	 * Effectue les actions associées au ramassage de cet item.
	 * Celles-ci dépendent du type de l'item.
	 * 
	 * @param board
	 * 		Aire de jeu contenant l'item.
	 * @param snake
	 * 		Serpent ayant ramassé l'item.
	 */
	public void pickUp(PhysBoard board, PhysSnake snake)
	{	x = -1;
		y = -1;
		
		// item collectif avec effet ponctuel
		if(type==ItemType.COLLECTIVE_CLEAN)
			board.mustClean = true;
	
		// item collectif avec effet dans la durée
		else if(type==ItemType.COLLECTIVE_TRAVERSE)
		{	remainingTime = type.duration;
			board.currentItems.add(this);
			// doit quand même être rajouté à chaque serpent, pour des raisons graphiques
			for(Snake s: board.snakes)
			{	if(s.eliminatedBy==null)
				{	PhysItemInstance item = new PhysItemInstance(this);
					s.currentItems.offer(item);
				}
			}
		}
		else if(type==ItemType.COLLECTIVE_WEALTH)
		{	remainingTime = type.duration;
			board.currentItems.add(this);
		}
		
		// item individuel visant le ramasseur
		else if(type==ItemType.USER_FAST || type==ItemType.USER_FLY || type==ItemType.USER_SLOW)
		{	remainingTime = type.duration;
			snake.currentItems.offer(this);
		}
		
		// item individuel visant les joueurs autres que le ramasseur
		else //if(type==ItemType.OTHERS_FAST || type==ItemType.OTHERS_REVERSE || type==ItemType.OTHERS_SLOW || type==ItemType.OTHERS_THICK)
		{	remainingTime = type.duration;
			for(Snake s: board.snakes)
			{	if(s!=snake && s.eliminatedBy==null)
				{	PhysItemInstance item = new PhysItemInstance(this);
					s.currentItems.offer(item);
				}
			}
		}
	}
}
