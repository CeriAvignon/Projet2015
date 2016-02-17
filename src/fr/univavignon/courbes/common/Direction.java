package fr.univavignon.courbes.common;

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

import java.io.Serializable;

/**
 * Cette classe représente les commandes possibles qu'un utilisateur
 * peut générer : tourner à gauche, tourner à droite, ou ne rien faire
 * du tout.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public enum Direction implements Serializable
{	/** L'utilisateur veut tourner à gauche */
	LEFT(-1),
	
	/** L'utilisateur veut tourner à droite */
	RIGHT(+1),
	
	/** L'utilisateur ne change pas de direction */
	NONE(0);
	
	/**
	 * Construit une direction.
	 * 
	 * @param value
	 * 		Valeur numérique associée à cette direction.
	 */
	private Direction(int value)
	{	this.value = value;
	}
	
	/** Valeur numérique associée à la direction */
	public final int value;

	/**
	 * Renvoie la direction inverse de cette direction.
	 * 
	 * @return
	 * 		Direction inverse de celle-ci.
	 */
	public Direction getInverse()
	{	Direction result = Direction.NONE;
		if(this==LEFT)
			result = Direction.RIGHT;
		else if(this==RIGHT)
			result = Direction.LEFT;
		return result;
	}
}
