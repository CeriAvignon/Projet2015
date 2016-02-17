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

import java.awt.Color;
import java.io.Serializable;

/**
 * Type énuméré représentant les différents types
 * d'items présents dans le jeu. Chacun possède un
 * champ représentant la durée de sont effet exprimée 
 * en ms.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public enum ItemType implements Serializable
{	
	////////////////////////////////////////////////////////////////
	////	EFFET SUR SOI
	////////////////////////////////////////////////////////////////
	/** Le joueur qui ramasse l'item accélère (bonus) */
	USER_FAST(3000l,new Color(168,211,117)),
	/** Le joueur qui ramasse l'item ralentit (bonus) */
	USER_SLOW(10000l,new Color(168,211,117)),
	/** Le joueur qui ramasse l'item vole au dessus des obstacles (bonus) */
	USER_FLY(6000l,new Color(168,211,117)),
	
	////////////////////////////////////////////////////////////////
	////	EFFET SUR LES AUTRES JOUEURS
	////////////////////////////////////////////////////////////////
	/** Les autres joueurs accélèrent (malus) */
	OTHERS_FAST(3000l,new Color(227,103,103)),
	/** Les autres joueurs laissent des trainées plus épaisses (malus) */
	OTHERS_THICK(7000l,new Color(227,103,103)),
	/** Les autres joueurs ralentissent (malus) */
	OTHERS_SLOW(5000l,new Color(227,103,103)),
	/** Les commandes des autres joueurs sont inversées (malus) */
	OTHERS_REVERSE(5000l,new Color(227,103,103)),
	
	////////////////////////////////////////////////////////////////
	////	EFFET SUR TOUS
	////////////////////////////////////////////////////////////////
	/** La probabilité d'apparition d'un item augmente */
	COLLECTIVE_WEALTH(60000l,new Color(106,156,207)),
	/** Tous les joueurs peuvent traverser les murs d'enceinte */
	COLLECTIVE_TRAVERSE(10000l,new Color(106,156,207)),
	/** L'aire de jeu est réinitialisée (les trainées existantes sont effacées) */
	COLLECTIVE_CLEAN(1l,new Color(106,156,207));
	
	/**
	 * Intialise un type d'item avec la durée appropriée.
	 * 
	 * @param duration
	 * 		Durée de l'effet de l'item, en ms.
	 * @param color
	 * 		Couleur associée à l'item.
	 */
	ItemType(long duration, Color color)
	{	this.duration = duration;
		this.color = color;
	}

	/** Durée associée à l'effet de l'item, exprimée en ms */
	public long duration;
	
	/** Couleur associée à l'item */
	public Color color;
}
